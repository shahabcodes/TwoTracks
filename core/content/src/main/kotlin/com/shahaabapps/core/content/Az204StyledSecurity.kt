package com.shahaabapps.core.content

/** The Security module's AZ-204 topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledSecurity {
    private val keyVaultCase = Podcast(
        "az-keyvault",
        listOf(
            maya("Welcome to Case Files. Today's case: The Midnight 403. A small bakery, Crumb and Company, sells through an Android app. The app talks to a small backend on Azure App Service. At midnight they deploy a new backend. By five past, nobody can pay. Every checkout fails with the same error, a 403."),
            leo("[chuckle] A crime scene with croissants. Detective Leo is on it. What's a 403, in plain words?"),
            maya("It's the web's way of saying forbidden. Not lost, not broken. Forbidden. Someone knocked, and the guard said, you're not allowed in."),
            leo("So who knocked, and which door?"),
            maya("The backend knocked, to get the database password. And the door belongs to Azure Key Vault. Think of Key Vault as a safe in the cloud. You put secrets in it, like passwords and connection strings, and only the people and apps you allow can take them out."),
            leo("Then my first suspect is obvious. The password is wrong. Someone typed it badly."),
            maya("[pause] Good instinct, wrong suspect. A wrong password would fail later, at the database, with a different error. This failed at the safe itself. The backend never even saw the password."),
            leo("Suspect two, then. The vault's firewall is blocking the backend."),
            maya("Closer, but a blocked network looks different too. You'd get a timeout, not a polite forbidden. Here the backend got all the way to the guard. Which tells us the guard knew who it was."),
            leo("Wait. How does the guard know who the backend is? Does it have its own password for the safe?"),
            maya("That's the clue that cracks it. In the new version, the developer moved the database password out of the settings file and into Key Vault. And to get in, the backend doesn't use a password at all. It uses a managed identity."),
            leo("Which is what, in plain words?"),
            maya("Picture an employee badge that Azure makes for your app. Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system. Azure keeps that account's credentials itself and never shows them to you. This one was a system-assigned identity: it belongs to the backend alone, switched on with one click, and deleted along with it."),
            leo("But if I never see a password, how does the backend prove it's the backend? That sounds like magic."),
            maya("It's simpler than magic. Azure started the backend, so Azure knows exactly which app is running there. It gives the backend a private local address to ask for tokens, and nobody outside can reach it. The backend asks, Azure hands back a signed token that runs out within hours, and the backend shows that token to the vault."),
            leo("So the token is the badge being swiped. The guard read it. And still said forbidden. [pause] Oh. The badge has no permission."),
            maya("[laugh] Detective Leo, finally. A badge says who you are. A role says what you may do. The developer switched on the identity and forgot the role. In Azure, permission comes from role-based access control, RBAC for short. A role is a named permission, like a job title."),
            leo("So the fix is to give the backend a role. The biggest one, to be safe. Key Vault Administrator."),
            maya("And that's how you open the next case. The backend only reads one secret. So it gets Key Vault Secrets User, which reads secrets and nothing more. Give the smallest role that does the job. A backend with Administrator could delete every secret in the vault if it were ever hacked."),
            leo("Fine. Secrets User. Checkouts work again. Case closed?"),
            maya("Not quite. There's a second witness. At one in the morning, the night intern had a faster idea. Skip the backend, and let the Android app read the vault directly. So they put a client secret inside the app."),
            leo("[sigh] Inside the app that's on thousands of phones?"),
            maya("Exactly. Anything inside an app on a phone can be taken apart in minutes. So a phone app never gets a managed identity, and never holds a secret. The phone signs the user in and calls the backend, and the backend, with its own identity, opens the vault. The MSAL episode covers the phone side of that."),
            leo("So a managed identity only works on Azure. What about my laptop, while I'm building?"),
            maya("That's the edge of the idea. On your laptop, you sign in with az login, as yourself. In a GitHub Actions pipeline, Azure can be told to trust GitHub's own token, called federation. And DefaultAzureCredential, one line of code, picks whichever is there: the managed identity in Azure, your login on your laptop. The same code works in both places."),
            leo("And if the bakery wants no vault code at all?"),
            maya("Then a Key Vault reference does it. It's a special value in the app's settings that says, fetch this from the vault. When the backend starts, Azure fetches the secret for it. But remember the night's lesson. The reference saves you code, not permission. The identity still needs its role."),
            leo("Any other clues in the file?"),
            maya("Two more, from the week after. The bakery changed the database password, and the backend kept using the old one. The reference had a version number pinned on it, so it always fetched that old version. Leave the version off, and you get the latest."),
            leo("And the second?"),
            maya("Someone deleted the secret by accident. No panic. Soft delete is on by default, a recycle bin you can restore from. And because the shop takes payments, they also turned on purge protection, which locks the bin, so nobody, not even an administrator, can empty it early."),
            leo("What about keys for encrypting card data? Same safe?"),
            maya("Same vault, different drawer. A secret is text you can read back. A key scrambles data and never leaves the vault: you send the data in. A certificate is the file behind the browser padlock. And if the bank says keys must live in hardware, that's the Premium tier, with HSM-backed keys."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. A 403 from Key Vault, with an identity switched on, means a missing role. Reading secrets only is Key Vault Secrets User, never Administrator. Several apps sharing one identity, or one made before they exist, is user-assigned. And no credentials in code means a managed identity, with DefaultAzureCredential or a Key Vault reference. A mobile app is never the one holding it."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case. Create a vault that uses roles, and add a secret. Grant yourself the Secrets User role, and read the secret back from the command line. Then create a function app, switch on its system identity, add a Key Vault reference setting, and look for the green tick in the portal. Then remove the role, and watch the 403 arrive."),
            leo("A crime I can commit on purpose. My favourite kind. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val keyVaultGossip = Podcast(
        "az-keyvault",
        listOf(
            maya("Okay, did you hear what happened with Tom from the payments team? Grab a coffee, because this one is about secrets. Passwords, connection strings, the stuff that should never leave the building."),
            leo("[laugh] I heard something about a password and a very bad Monday. What did Tom do?"),
            maya("Tom was building a small recipe app for Android on the side, with a little backend on Azure that keeps the users in a database. The backend needs the database password. So Tom put the password in the code. And pushed the code to a public repository."),
            leo("[sigh] Oh no. Public, as in anyone on the internet can read it?"),
            maya("Anyone. Bots scan public code all day for exactly that. Within an hour someone was in his database. Which is why the whole office now talks about one thing. Azure Key Vault."),
            leo("Which is what, in plain words? For those of us who are not Tom."),
            maya("A safe in the cloud. You put your secrets in it, and only the people and apps you allow can take them out. Priya, who cleaned up after Tom, moved the password into the vault that same afternoon."),
            leo("Okay, but then the backend needs a password to open the safe. Doesn't that just move the problem?"),
            maya("That's exactly what Tom said. And Priya said, the backend doesn't get a password at all. It gets a managed identity. Picture an employee badge Azure makes for your app. Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system, and Azure keeps its credentials so nobody ever sees them."),
            leo("Then how does the backend prove who it is? Tom wanted to know that too."),
            maya("Azure started the backend, so it knows which app is running there. It gives the backend a private local address to ask for tokens, one nobody outside can reach. The backend asks, gets a signed token that runs out within hours, and shows it to the vault. Nothing long-lived to leak."),
            leo("[chuckle] Poor Tom. So is that the end of the drama?"),
            maya("Oh no. Week two. The intern set up an identity for another backend, and wanted it to just work. So they gave it Key Vault Administrator."),
            leo("Which sounds powerful. Is that bad?"),
            maya("It's like giving the work experience student keys to the whole building because they needed the stationery cupboard. Permissions in Azure come from role-based access control, RBAC. A role is a named permission, like a job title. A backend that only reads secrets gets Key Vault Secrets User. The smallest role that does the job."),
            leo("Got it. Smallest role. What's week three?"),
            maya("Week three is the intern again, bless them. They decided the Android app should read the vault itself, to skip the backend. So they put a client secret inside the app."),
            leo("Inside the app on everyone's phone?"),
            maya("Priya caught it in review. Anything inside a phone app can be taken apart in minutes. A phone app never gets a managed identity and never holds a secret. It signs the user in and calls the backend, and the backend opens the vault. The MSAL episode covers the phone side, if you want the whole story."),
            leo("Noted. Week four?"),
            maya("Dana. Dana did everything right. Identity on, the right role. Then the database password changed, and Dana's backend kept using the old one for three days."),
            leo("Why? Was the vault broken?"),
            maya("No. Dana's backend read the password through a Key Vault reference, a setting that says, fetch this from the vault when the app starts. And Dana had pinned a version number in it. So it kept fetching that exact old version. Leave the version off, and you get the latest."),
            leo("And the big one? I heard there was a Friday."),
            maya("The Friday. Marcus deleted an identity he thought nobody used. It was a user-assigned identity, one you create on its own and hand to as many apps as you like. Three apps were wearing it. All three lost access at once, at five on a Friday."),
            leo("Of course it was five on a Friday. So what's the other kind?"),
            maya("A system-assigned identity. It belongs to one app only, you switch it on with one click, and it's deleted when the app is. Marcus couldn't have broken three apps with one of those. But for a website, a mobile backend and a report job that share the same access, user-assigned is right. You just don't delete it while it's in use."),
            leo("Did anyone lose a secret for good in all this?"),
            maya("Nearly. Someone deleted the wrong secret in the clean-up. But soft delete is on by default, a recycle bin you can restore from. And the payments team had turned on purge protection, which locks that bin so nobody, not even an administrator, can empty it early."),
            leo("What about Tom? Did Tom redeem himself?"),
            maya("[laugh] He did. He learned DefaultAzureCredential. One line of code. On his laptop it uses his own login, after az login. In Azure, the backend's managed identity. In his GitHub pipeline, federation, where Azure trusts GitHub's own token. The same code everywhere."),
            leo("And the keys and certificates I keep hearing about?"),
            maya("Same vault. A key scrambles data and never leaves the vault, you send the data in. A certificate is the file behind the browser padlock. And when a bank client demanded keys in hardware, the team moved to the Premium tier, with HSM-backed keys."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. A backend that only reads secrets gets Key Vault Secrets User, never Administrator. A shared identity, or one that must exist before the apps, is user-assigned. Nobody may delete a secret for good means purge protection. And no credentials in code means a managed identity, with DefaultAzureCredential or a Key Vault reference. Never a secret in a mobile app."),
            leo("And something to try today, so I'm not the next story?"),
            maya("Create a vault that uses roles, and add a secret. Give yourself the Secrets User role, and read the secret back from the command line. Then create a function app, switch on its system identity, add a Key Vault reference setting, and look for the green tick in the portal."),
            leo("No public repositories. Got it, Tom. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(keyVaultCase, keyVaultGossip)

    // The same scenes a few episodes draw, each told with its own lines.

    private fun roles(from: Int, app: String, admin: BoardCard, user: BoardCard, at: Int = from + 1) = Scene(
        from, if (from == 0) "Roles" else "The smallest role",
        DiagramSpec(
            "Give the app's identity the smallest role it needs on the vault: Secrets User to read, not Administrator.",
            nodes = listOf(
                DNode("app", app, 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                DNode("vault", "Key Vault", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                DNode("user", "Secrets User", 0.3f, 0.78f, w = 0.36f, kind = NodeKind.PILL),
                DNode("admin", "Administrator", 0.74f, 0.78f, w = 0.36f, kind = NodeKind.PILL, tone = 2),
            ),
            edges = listOf(DEdge("app", "vault", "a role")),
            aspect = 0.8f,
        ),
        beats = listOf(
            Beat(from, show = setOf("app", "vault", "admin"), light = setOf("admin"), card = admin),
            Beat(at, show = setOf("user"), light = setOf("user", "app"), pulse = "app" to "vault", card = user),
        ),
    )

    private fun tokens(from: Int, at: Int, then: Beat? = null) = Scene(
        from, "No magic: a token",
        DiagramSpec(
            "Azure started the backend, so it answers the backend's token request on a private local address; the backend shows the signed token to the vault.",
            nodes = listOf(
                DNode("app", "Backend", 0.2f, 0.3f, w = 0.3f, h = 0.2f),
                DNode("vault", "Key Vault", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                DNode("local", "Local token address", 0.22f, 0.78f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "only from inside"),
                DNode("token", "Signed token", 0.72f, 0.78f, w = 0.36f, kind = NodeKind.PILL, sub = "lasts hours"),
            ),
            edges = listOf(
                DEdge("app", "local", "asks"),
                DEdge("local", "token", "issues"),
                DEdge("token", "vault", "shown"),
            ),
            aspect = 0.8f,
        ),
        beats = listOfNotNull(
            Beat(from, show = setOf("app", "vault")),
            Beat(at, show = setOf("local", "token"), light = setOf("local", "token"), pulse = "local" to "token", card = BoardCard("How Azure knows", "It started the app, so it answers it on a private address.")),
            then,
        ),
    )

    private fun phone(from: Int, at: Int, first: BoardCard) = Scene(
        from, "A secret on a phone",
        DiagramSpec(
            "A phone app never holds a secret: the user signs in, the app calls the backend, and the backend's identity opens the vault.",
            nodes = listOf(
                DNode("phone", "Android app", 0.24f, 0.22f, w = 0.4f, h = 0.2f, sub = "signs the user in"),
                DNode("secret", "A client secret?", 0.76f, 0.22f, w = 0.38f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                DNode("app", "Backend", 0.24f, 0.76f, w = 0.32f, h = 0.2f, sub = "managed identity"),
                DNode("vault", "Key Vault", 0.78f, 0.74f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
            ),
            edges = listOf(
                DEdge("phone", "app", "calls"),
                DEdge("app", "vault", "its identity"),
            ),
            aspect = 0.8f,
        ),
        beats = listOf(
            Beat(from, show = setOf("phone", "secret"), light = setOf("secret"), card = first),
            Beat(at, show = setOf("app", "vault"), light = setOf("phone", "app", "vault"), pulse = "app" to "vault", card = BoardCard("A phone never holds a secret", "It calls your backend; the backend's identity opens the vault. MSAL: the phone side.")),
        ),
    )

    private fun everywhere(from: Int, at: Int, laptop: String, first: BoardCard? = null) = Scene(
        from, "The same code everywhere",
        DiagramSpec(
            "DefaultAzureCredential uses az login on a laptop, the managed identity in Azure, and federation in a GitHub pipeline.",
            nodes = listOf(
                DNode("dev", laptop, 0.2f, 0.15f, w = 0.36f, h = 0.17f, tone = 1, sub = "az login"),
                DNode("azure", "In Azure", 0.2f, 0.47f, w = 0.36f, h = 0.17f, tone = 1, sub = "managed identity"),
                DNode("gh", "GitHub Actions", 0.2f, 0.79f, w = 0.36f, h = 0.17f, tone = 1, sub = "federation"),
                DNode("dac", "DefaultAzure Credential", 0.56f, 0.47f, w = 0.3f, h = 0.22f),
                DNode("vault", "Key Vault", 0.88f, 0.47f, w = 0.2f, h = 0.3f, kind = NodeKind.CYLINDER),
            ),
            edges = listOf(
                DEdge("dev", "dac"),
                DEdge("azure", "dac"),
                DEdge("gh", "dac"),
                DEdge("dac", "vault"),
            ),
            aspect = 0.8f,
        ),
        beats = listOfNotNull(
            first?.let { Beat(from, show = setOf("dev"), light = setOf("dev"), card = it) },
            Beat(at, show = setOf("dev", "azure", "gh", "dac", "vault"), light = setOf("dac"), pulse = "dac" to "vault", card = BoardCard("C#", "new SecretClient(vaultUri, new DefaultAzureCredential())", code = true)),
        ),
    )

    private fun reference(from: Int, at: Int, then: Int? = null) = Scene(
        from, "No code at all",
        DiagramSpec(
            "A Key Vault reference in an app setting is fetched when the app starts, with the app's managed identity, which still needs its role.",
            nodes = listOf(
                DNode("setting", "App setting", 0.22f, 0.22f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "a Key Vault reference"),
                DNode("app", "Backend", 0.22f, 0.72f, w = 0.34f, h = 0.2f),
                DNode("id", "Managed identity", 0.6f, 0.72f, w = 0.36f, kind = NodeKind.PILL),
                DNode("vault", "Key Vault", 0.82f, 0.24f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
            ),
            edges = listOf(
                DEdge("setting", "app", "start-up"),
                DEdge("id", "vault", "Secrets User"),
                DEdge("vault", "setting", "fetched", dashed = true),
            ),
            aspect = 0.8f,
        ),
        beats = listOfNotNull(
            Beat(from, show = setOf("app")),
            Beat(at, show = setOf("setting", "vault", "id"), light = setOf("setting"), pulse = "vault" to "setting", card = BoardCard("App setting", "@Microsoft.KeyVault(VaultName=v;SecretName=s)", code = true)),
            then?.let { Beat(it, light = setOf("setting"), card = BoardCard("A pinned version", "Leave the version off the reference to get the latest.")) },
        ),
    )

    private fun recycleBin(from: Int, at: Int, heading: String) = Scene(
        from, heading,
        DiagramSpec(
            "Soft delete keeps a deleted secret recoverable; purge protection stops anyone erasing it for good in that window.",
            nodes = listOf(
                DNode("secret", "A secret", 0.2f, 0.3f, w = 0.3f, h = 0.2f),
                DNode("soft", "Soft delete", 0.78f, 0.3f, w = 0.36f, h = 0.2f, tone = 1, sub = "a recycle bin"),
                DNode("purge", "Purge protection", 0.78f, 0.78f, w = 0.4f, h = 0.2f, sub = "the bin is locked"),
            ),
            edges = listOf(
                DEdge("secret", "soft", "deleted"),
                DEdge("soft", "purge", dashed = true),
            ),
            aspect = 0.8f,
        ),
        beats = listOf(
            Beat(from, show = setOf("secret")),
            Beat(at, show = setOf("soft", "purge"), light = setOf("purge"), pulse = "secret" to "soft", card = BoardCard("Purge protection", "Locks the recycle bin: nobody can delete a secret for good too early.")),
        ),
    )

    private fun drawers(from: Int, at: Int, heading: String) = Scene(
        from, heading,
        DiagramSpec(
            "Key Vault holds secrets, keys and certificates; keys never leave it, and Premium keeps keys in hardware.",
            nodes = listOf(
                DNode("vault", "Key Vault", 0.5f, 0.2f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                DNode("secrets", "Secrets", 0.17f, 0.62f, w = 0.3f, tone = 1, sub = "readable, versioned"),
                DNode("keys", "Keys", 0.5f, 0.62f, w = 0.3f, tone = 1, sub = "never leave"),
                DNode("certs", "Certificates", 0.83f, 0.62f, w = 0.3f, tone = 1, sub = "the padlock"),
                DNode("tier", "Premium: HSM-backed", 0.5f, 0.9f, w = 0.5f, h = 0.14f, kind = NodeKind.NOTE),
            ),
            edges = listOf(DEdge("vault", "secrets"), DEdge("vault", "keys"), DEdge("vault", "certs")),
            aspect = 0.8f,
        ),
        beats = listOf(
            Beat(from, show = setOf("vault")),
            Beat(at, show = setOf("secrets", "keys", "certs", "tier"), light = setOf("keys", "tier"), card = BoardCard("Keys in hardware?", "The bank's rule means the Premium tier, with HSM-backed keys.")),
        ),
    )

    private fun keyVaultCaseBoard() = PodcastBoard(
        "az-keyvault--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Midnight 403..."
            Scene(
                0, "The Midnight 403",
                DiagramSpec(
                    "The bakery's Android app calls its backend, which asks Key Vault for the database password and is turned away with a 403.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "403 Forbidden", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("vault", "Key Vault", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "checkout"),
                        DEdge("app", "vault", "the password?"),
                        DEdge("vault", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "err"), light = setOf("err"), pulse = "phone" to "app", card = BoardCard("The case", "Midnight deploy. By five past, every checkout fails with a 403.")),
                    Beat(2, light = setOf("err"), card = BoardCard("403 means forbidden", "Not lost, not broken. Someone knocked and the guard said no.")),
                    Beat(4, show = setOf("vault"), light = setOf("vault"), pulse = "app" to "vault", card = BoardCard("Key Vault", "A safe in the cloud for passwords and connection strings.")),
                ),
            ),
            // 5: "Then my first suspect is obvious. The password is wrong..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Each wrong cause fails a different way; a polite 403 from the vault means the guard knew who knocked.",
                    nodes = listOf(
                        DNode("pw", "Wrong password", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "fails at the database"),
                        DNode("fw", "Firewall", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "a timeout"),
                        DNode("known", "The guard knew the backend", 0.5f, 0.74f, w = 0.66f, h = 0.22f, sub = "a polite 403"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("pw"), light = setOf("pw")),
                    Beat(6, card = BoardCard("Suspect one: cleared", "A wrong password fails later, at the database, with another error.")),
                    Beat(7, show = setOf("fw"), light = setOf("fw")),
                    Beat(8, show = setOf("known"), light = setOf("known"), card = BoardCard("Suspect two: cleared", "A blocked network times out. This backend reached the guard.")),
                ),
            ),
            // 9: "Wait. How does the guard know who the backend is?..."
            Scene(
                9, "The badge",
                DiagramSpec(
                    "The backend has a system-assigned managed identity, an account in Entra ID whose credentials Azure keeps.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("id", "Managed identity", 0.5f, 0.78f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "system-assigned"),
                        DNode("entra", "Entra ID", 0.82f, 0.3f, w = 0.3f, h = 0.2f, tone = 1, sub = "the account"),
                    ),
                    edges = listOf(
                        DEdge("app", "id", "wears"),
                        DEdge("id", "entra", "is an account in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("app")),
                    Beat(10, show = setOf("id"), light = setOf("id"), card = BoardCard("The clue", "No password for the safe at all. The backend uses a managed identity.")),
                    Beat(12, show = setOf("entra"), light = setOf("id", "entra"), pulse = "id" to "entra", card = BoardCard("Under the picture", "An account in Entra ID. Azure keeps its credentials; you never see them.")),
                ),
            ),
            // 13: "But if I never see a password, how does the backend prove it's the backend?..."
            tokens(
                13, 14,
                Beat(15, light = setOf("token", "vault"), pulse = "token" to "vault", card = BoardCard("Who you are, not what you may do", "The vault knows who it is. A role (RBAC) says what. This one had none.")),
            ),
            // 17: "So the fix is to give the backend a role. The biggest one..."
            roles(
                17, "Backend",
                BoardCard("Administrator?", "Too much. Hacked, it could delete every secret in the vault."),
                BoardCard("Key Vault Secrets User", "Reads secrets and nothing more. The smallest role that does the job."),
            ),
            // 20: "Not quite. There's a second witness..."
            phone(20, 22, BoardCard("The second witness", "One in the morning: a client secret inside the Android app.")),
            // 23: "So a managed identity only works on Azure. What about my laptop?..."
            everywhere(23, 24, "Your laptop"),
            // 25: "And if the bakery wants no vault code at all?"
            reference(25, 26, then = 28),
            // 29: "And the second?"
            recycleBin(29, 30, "Deleted by accident"),
            // 31: "What about keys for encrypting card data? Same safe?"
            drawers(31, 32, "Different drawers"),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed",
                DiagramSpec(
                    "The exam's Key Vault questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("role", "403, identity on", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "a missing role"),
                        DNode("read", "Only reads secrets", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Secrets User"),
                        DNode("user", "Shared or made first", 0.5f, 0.45f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "user-assigned"),
                        DNode("none", "No credentials in code", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "managed identity"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("role", "read", "user", "none"), light = setOf("none"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Never a secret in a mobile app.")),
                    Beat(36, card = BoardCard("Today", "Recreate it: a vault, Secrets User, a reference; then remove the role.")),
                ),
            ),
        ),
    )

    private fun keyVaultGossipBoard() = PodcastBoard(
        "az-keyvault--gossip",
        listOf(
            // 0: "Okay, did you hear what happened with Tom from the payments team?..."
            Scene(
                0, "What Tom did",
                DiagramSpec(
                    "Tom's password sat in his backend's code, the code went to a public repository, and bots found it within an hour.",
                    nodes = listOf(
                        DNode("code", "Tom's backend code", 0.24f, 0.3f, w = 0.42f, h = 0.2f, sub = "the password inside"),
                        DNode("repo", "Public repository", 0.76f, 0.3f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("bots", "Bots", 0.76f, 0.78f, w = 0.3f, h = 0.18f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("code", "repo", "pushed"),
                        DEdge("bots", "repo", "scan"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("code")),
                    Beat(2, show = setOf("repo"), light = setOf("code"), pulse = "code" to "repo", card = BoardCard("The password in the code", "Then pushed to a public repository anyone can read.")),
                    Beat(4, show = setOf("bots"), light = setOf("bots"), pulse = "bots" to "repo", card = BoardCard("Within an hour", "Bots scan public code all day for exactly that.")),
                ),
            ),
            // 5: "Which is what, in plain words? For those of us who are not Tom."
            Scene(
                5, "What Priya did",
                DiagramSpec(
                    "The password moves into Key Vault, and the backend opens the vault with a managed identity, an account in Entra ID.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("id", "Managed identity", 0.5f, 0.78f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "an Entra ID account"),
                        DNode("vault", "Key Vault", 0.82f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("app", "id", "wears"),
                        DEdge("id", "vault", "a role"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("app", "vault"), light = setOf("vault"), card = BoardCard("Key Vault", "A safe in the cloud. Priya moved the password in that afternoon.")),
                    Beat(7, card = BoardCard("A password for the safe?", "That would only move the problem.")),
                    Beat(8, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("Under the picture", "An account in Entra ID. Azure keeps its credentials; nobody sees them.")),
                ),
            ),
            // 9: "Then how does the backend prove who it is? Tom wanted to know that too."
            tokens(9, 10),
            // 12: "Oh no. Week two. The intern set up an identity for another backend..."
            roles(
                12, "Backend",
                BoardCard("Administrator, to just work", "The keys to the whole building, for the stationery cupboard."),
                BoardCard("Key Vault Secrets User", "Only reads secrets? The smallest role that does the job."),
                at = 14,
            ),
            // 16: "Week three is the intern again, bless them..."
            phone(16, 18, BoardCard("Week three: the intern again", "A client secret inside the Android app, to skip the backend.")),
            // 19: "Noted. Week four?"
            Scene(
                19, "Week four: Dana",
                DiagramSpec(
                    "Dana's Key Vault reference named one version, so the backend kept fetching the old password.",
                    nodes = listOf(
                        DNode("ref", "Key Vault reference", 0.24f, 0.3f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "version pinned"),
                        DNode("v1", "Old version", 0.76f, 0.3f, w = 0.36f, h = 0.18f, tone = 2),
                        DNode("v2", "Latest", 0.76f, 0.76f, w = 0.36f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("ref", "v1", "fetches"),
                        DEdge("ref", "v2", "no version", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("ref", "v1"), light = setOf("v1"), card = BoardCard("Three days on the old one", "The password changed. Dana's backend never noticed.")),
                    Beat(22, show = setOf("v2"), light = setOf("ref", "v2"), pulse = "ref" to "v2", card = BoardCard("A pinned version", "Leave the version off the reference to get the latest.")),
                ),
            ),
            // 23: "And the big one? I heard there was a Friday."
            Scene(
                23, "The Friday",
                DiagramSpec(
                    "A user-assigned identity is made on its own and shared by many apps; a system-assigned one belongs to one app.",
                    nodes = listOf(
                        DNode("sys", "System-assigned", 0.24f, 0.2f, w = 0.42f, kind = NodeKind.PILL),
                        DNode("one", "One app", 0.24f, 0.62f, w = 0.32f, h = 0.18f, tone = 1),
                        DNode("usr", "User-assigned", 0.76f, 0.2f, w = 0.42f, kind = NodeKind.PILL),
                        DNode("a1", "Website", 0.6f, 0.62f, w = 0.22f, tone = 1),
                        DNode("a2", "Mobile API", 0.88f, 0.62f, w = 0.24f, tone = 1),
                        DNode("a3", "Report job", 0.74f, 0.9f, h = 0.14f, tone = 1),
                    ),
                    edges = listOf(DEdge("sys", "one"), DEdge("usr", "a1"), DEdge("usr", "a2"), DEdge("usr", "a3")),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("usr", "a1", "a2", "a3"), light = setOf("usr"), card = BoardCard("Marcus deleted an identity", "User-assigned, worn by three apps. All three lost access at once.")),
                    Beat(26, show = setOf("sys", "one"), light = setOf("sys", "one"), card = BoardCard("System-assigned", "One click. Belongs to one app, and is deleted with it.")),
                ),
            ),
            // 27: "Did anyone lose a secret for good in all this?"
            recycleBin(27, 28, "Nearly lost for good"),
            // 29: "What about Tom? Did Tom redeem himself?"
            everywhere(29, 30, "Tom's laptop"),
            // 31: "And the keys and certificates I keep hearing about?"
            drawers(31, 32, "The rest of the vault"),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Key Vault questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("read", "Only reads secrets", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Secrets User"),
                        DNode("user", "Shared or made first", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "user-assigned"),
                        DNode("purge", "No delete for good", 0.5f, 0.45f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "purge protection"),
                        DNode("none", "No credentials in code", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "managed identity"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("read", "user", "purge", "none"), light = setOf("none"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Never a secret in a mobile app.")),
                    Beat(36, card = BoardCard("Today", "A vault with roles, a secret, Secrets User; then a reference with a green tick.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(keyVaultCaseBoard(), keyVaultGossipBoard()) }
}
