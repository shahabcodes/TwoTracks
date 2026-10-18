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
            // 0 "An app needs to read a secret... where does the app keep its own credential?"
            Scene(
                0, "The question",
                DiagramSpec(
                    "An app reads a secret from the vault, but to get in it seems to need a secret of its own.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.42f, w = 0.3f, h = 0.2f),
                        DNode("vault", "Key Vault", 0.78f, 0.42f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("cred", "Its own credential?", 0.2f, 0.8f, w = 0.34f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(DEdge("app", "vault", "read a secret"), DEdge("cred", "app", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "vault"), pulse = "app" to "vault"),
                    Beat(1, show = setOf("cred"), light = setOf("cred"), card = BoardCard("An app setting?", "That only moves the secret somewhere else.")),
                ),
            ),
            // 2 "Key Vault holds three kinds of thing." 4 secrets, keys, certificates.
            Scene(
                2, "What the vault holds",
                DiagramSpec(
                    "Key Vault holds secrets, keys and certificates; keys never leave it, so data goes in to be encrypted.",
                    nodes = listOf(
                        DNode("vault", "Key Vault", 0.5f, 0.2f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("secrets", "Secrets", 0.17f, 0.62f, w = 0.3f, h = 0.16f, tone = 1, sub = "versioned, readable"),
                        DNode("keys", "Keys", 0.5f, 0.62f, w = 0.3f, h = 0.16f, tone = 1, sub = "never leave"),
                        DNode("certs", "Certificates", 0.83f, 0.62f, w = 0.3f, h = 0.16f, tone = 1, sub = "renew themselves"),
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
                    Beat(4, show = setOf("secrets", "keys", "certs"), card = BoardCard("Three kinds of thing", "Secrets you can read back, keys that never leave, certificates that renew.")),
                    Beat(5, show = setOf("data"), light = setOf("keys", "data"), pulse = "data" to "keys", card = BoardCard("Keys never leave", "You send data in; the operation happens inside the vault.")),
                    Beat(6, show = setOf("tier"), light = setOf("keys", "tier"), card = BoardCard("Hardware-protected keys?", "Premium tier: HSM-backed. Standard: software-protected.")),
                ),
            ),
            // 7 "What about permissions on the vault?" 8 RBAC, least privilege.
            Scene(
                7, "Who may use it",
                DiagramSpec(
                    "Grant the app the least role it needs on the vault: Secrets User to read, not Administrator.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("vault", "Key Vault", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("user", "Secrets User", 0.3f, 0.78f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("admin", "Administrator", 0.74f, 0.78f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(DEdge("app", "vault", "RBAC role")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(7, show = setOf("app", "vault")),
                    Beat(8, show = setOf("user", "admin"), light = setOf("user", "app"), pulse = "app" to "vault", card = BoardCard("RBAC, least privilege", "Only reads secrets? Key Vault Secrets User, not Administrator.")),
                ),
            ),
            // 9 "And if someone deletes a secret by mistake?" 10 soft delete, purge protection.
            Scene(
                9, "Deleted by mistake",
                DiagramSpec(
                    "Soft delete keeps a deleted secret recoverable; purge protection stops anyone erasing it for good in that window.",
                    nodes = listOf(
                        DNode("secret", "A secret", 0.2f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("soft", "Soft delete", 0.78f, 0.3f, w = 0.36f, h = 0.2f, tone = 1, sub = "recoverable"),
                        DNode("purge", "Purge protection", 0.78f, 0.78f, w = 0.4f, h = 0.2f, sub = "no permanent delete"),
                    ),
                    edges = listOf(DEdge("secret", "soft", "deleted"), DEdge("soft", "purge", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(9, show = setOf("secret")),
                    Beat(10, show = setOf("soft", "purge"), light = setOf("purge"), pulse = "secret" to "soft", card = BoardCard("\"Prevent permanent deletion\"", "That exam phrase means purge protection.")),
                ),
            ),
            // 11 "The app with no credential." 12 managed identity.
            Scene(
                11, "Secrets without secrets",
                DiagramSpec(
                    "Azure creates and manages the app's identity; the identity holds a role on the vault, and no password is stored.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("id", "Managed identity", 0.5f, 0.78f, w = 0.42f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("vault", "Key Vault", 0.82f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(DEdge("app", "id", "is"), DEdge("id", "vault", "Secrets User")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(11, show = setOf("app", "vault")),
                    Beat(12, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("Managed identity", "An Entra identity Azure creates and manages. No password anywhere.")),
                ),
            ),
            // 14 system-assigned. 16 user-assigned. 18 shared and created first means user-assigned.
            Scene(
                14, "Two kinds of identity",
                DiagramSpec(
                    "A system-assigned identity belongs to one resource; a user-assigned one is its own resource, shared by many.",
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
                    Beat(14, show = setOf("sys", "one"), light = setOf("sys", "one"), card = BoardCard("System-assigned", "One toggle. Created and deleted with its one resource.")),
                    Beat(16, show = setOf("usr", "a1", "a2", "a3"), light = setOf("usr", "a1", "a2", "a3"), card = BoardCard("User-assigned", "Its own resource: create it first, grant roles once, attach it to many.")),
                    Beat(18, light = setOf("usr"), card = BoardCard("Shared, and exists before deploy?", "User-assigned. A system-assigned one cannot be shared.")),
                ),
            ),
            // 19 "How does the code actually pick up the identity?" 20 DefaultAzureCredential.
            Scene(
                19, "The same code everywhere",
                DiagramSpec(
                    "DefaultAzureCredential uses your az login on your machine and the managed identity in Azure.",
                    nodes = listOf(
                        DNode("dev", "Your machine", 0.18f, 0.18f, w = 0.34f, h = 0.18f, tone = 1, sub = "after az login"),
                        DNode("azure", "In Azure", 0.18f, 0.76f, w = 0.34f, h = 0.18f, tone = 1, sub = "managed identity"),
                        // Split at the word, so it wraps as two words rather than mid-name.
                        DNode("dac", "DefaultAzure Credential", 0.48f, 0.47f, w = 0.32f, h = 0.22f),
                        DNode("vault", "Key Vault", 0.87f, 0.47f, w = 0.26f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(DEdge("dev", "dac"), DEdge("azure", "dac"), DEdge("dac", "vault")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(19, show = setOf("dac", "vault")),
                    Beat(20, show = setOf("dev", "azure"), light = setOf("dac"), pulse = "dac" to "vault", card = BoardCard("C#", "new SecretClient(vaultUri, new DefaultAzureCredential())", code = true)),
                ),
            ),
            // 21 "And if I don't want to write any code at all?" 22 Key Vault reference. 24 still needs the role.
            Scene(
                21, "No code at all",
                DiagramSpec(
                    "A Key Vault reference in an app setting is resolved at start-up with the app's managed identity, which still needs its role.",
                    nodes = listOf(
                        DNode("setting", "App setting", 0.22f, 0.22f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "a Key Vault reference"),
                        DNode("app", "App Service", 0.22f, 0.72f, w = 0.34f, h = 0.2f),
                        DNode("id", "Managed identity", 0.6f, 0.72f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("vault", "Key Vault", 0.82f, 0.24f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(DEdge("setting", "app", "start-up"), DEdge("id", "vault", "Secrets User"), DEdge("vault", "setting", "resolves", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(21, show = setOf("app")),
                    Beat(22, show = setOf("setting", "vault"), light = setOf("setting"), pulse = "vault" to "setting", card = BoardCard("App setting", "@Microsoft.KeyVault(VaultName=v;SecretName=s)", code = true)),
                    Beat(24, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("It still needs the role", "The reference saves you the code, not the permission.")),
                ),
            ),
            // 26 the mistakes.
            Scene(
                26, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Key Vault access wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("admin", "Administrator for a reader", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("pin", "A pinned secret version", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("role", "Identity, no role: 403", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("gone", "Deleting a shared identity", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(26, show = setOf("admin", "pin", "role", "gone"), card = BoardCard("Rotation doing nothing?", "Leave the version off the reference to get the latest.")),
                ),
            ),
            // 27 "Three lines?" 28 the summary; 30 today's task.
            Scene(
                27, "In three lines",
                DiagramSpec(
                    "The episode in three lines: what the vault holds, which identity, and how the app reads with no credential.",
                    nodes = listOf(
                        DNode("one", "Secrets, keys, certificates", 0.5f, 0.16f, w = 0.8f, h = 0.2f, tone = 1, sub = "Premium for hardware"),
                        DNode("two", "System or user-assigned", 0.5f, 0.5f, w = 0.8f, h = 0.2f, tone = 1, sub = "roles through RBAC"),
                        DNode("three", "DefaultAzureCredential", 0.5f, 0.84f, w = 0.8f, h = 0.2f, tone = 1, sub = "or a Key Vault reference"),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(28, show = setOf("one", "two", "three")),
                    Beat(30, card = BoardCard("Today", "A vault with RBAC, a secret, Secrets User; then a reference that shows a green tick.")),
                ),
            ),
        ),
    )
}
