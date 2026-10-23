package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Identity2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val authorization = Topic(
        id = "sa-authorization", title = "Authorizing access: roles, on-premises access and secrets", minutes = 28,
        references = listOf(
            B.ref("What is Azure role-based access control (Azure RBAC)?", LEARN + "azure/role-based-access-control/overview"),
            B.ref("Azure roles, Microsoft Entra roles, and classic subscription administrator roles", LEARN + "azure/role-based-access-control/rbac-and-directory-admin-roles"),
            B.ref("What is Microsoft Entra Privileged Identity Management?", LEARN + "entra/id-governance/privileged-identity-management/pim-configure"),
            B.ref("Publish on-premises apps with Microsoft Entra application proxy", LEARN + "entra/identity/app-proxy/overview-what-is-app-proxy"),
            B.ref("About Azure Key Vault", LEARN + "azure/key-vault/general/overview"),
            B.ref("Azure Key Vault Managed HSM overview", LEARN + "azure/key-vault/managed-hsm/overview"),
            B.ref("Learn module: Design authentication and authorization solutions", LEARN + "training/modules/design-authentication-authorization-solutions/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-authorization-w1", "sa-authorization",
                "A tester must restart the VMs in one resource group for a week. Where would you grant that?",
                listOf("On that resource group, only for the time needed", "On the whole subscription, for good", "On the tenant root, so it is simple"), 0,
                "Think of the smallest place and the shortest time that still gets the job done.",
            ),
            B.plain("""
                Signing in proves who you are; authorization decides what you may then do. Picture an office building:
                your badge proves your name, but which doors it opens is a separate list. In Azure that list is
                Azure role-based access control, RBAC for short: rules that say which person or app may do which
                actions on which resources. This topic designs those rules, reaches apps still in the old office,
                and keeps passwords and keys in a safe.
            """),
            B.diagram(
                caption = "A role assignment joins who, what and where, and everything inside the scope inherits it.",
                nodes = listOf(
                    DNode("who", "Who", 0.2f, 0.17f, w = 0.34f, h = 0.24f, tone = 1, sub = "user, group or identity"),
                    DNode("what", "What", 0.2f, 0.5f, w = 0.34f, h = 0.24f, tone = 1, sub = "role: allowed actions"),
                    DNode("where", "Where", 0.2f, 0.83f, w = 0.34f, h = 0.24f, tone = 1, sub = "scope: MG, sub, RG"),
                    DNode("assign", "Role assignment", 0.72f, 0.5f, w = 0.4f, h = 0.24f, sub = "who + what + where"),
                    DNode("note", "Inherited by everything below the scope", 0.72f, 0.85f, w = 0.48f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("who", "assign"), DEdge("what", "assign"), DEdge("where", "assign"), DEdge("assign", "note", dashed = true)),
                aspect = 0.8f,
            ),
            B.h("Role assignments: who, what, where"),
            B.p("""
                A security principal is anyone or anything that can be given access: a user, a group, an app's own
                identity (a service principal) or a managed identity, which is an identity Azure runs for a resource.
                A role definition is a named list of allowed actions, like a job description. A scope is where it
                applies. From biggest to smallest: a management group, which is a folder of subscriptions; a
                subscription, which is one Azure account with its own bill; a resource group, which is a box of
                related resources; or one single resource. Access granted at a scope flows down to everything
                inside it, the way a floor key opens every room on that floor.
            """),
            B.table(
                listOf("Built-in role", "What it allows", "Pick it when"),
                listOf("Owner", "Everything, including granting access to others", "Someone runs the scope end to end"),
                listOf("Contributor", "Create and change everything, but not grant access", "A team builds and runs its own resources"),
                listOf("Reader", "See everything, change nothing", "Auditors, support staff looking only"),
                listOf("User Access Administrator", "Manage who has access, nothing else", "A security team hands out access"),
                listOf("Service roles", "One service only, for example Virtual Machine Contributor", "The job touches one kind of resource"),
            ),
            B.bullets(
                "Least privilege: the smallest role at the narrowest scope that still does the job.",
                "Assign roles to groups, not people, so joiners and leavers change one membership instead of many assignments.",
                "A custom role is your own list of actions, for when no built-in role fits. It is a short text file that also names the scopes where it may be assigned.",
                "Actions cover managing a resource, like building or knocking down a shed: the control plane. Data actions cover what is inside it, like reading a file or a secret: the data plane.",
            ),
            B.compare("Two kinds of role, two different jobs",
                B.card("Microsoft Entra roles", "The job is about the directory itself: users, groups, licences, app registrations.", "Manage the directory, not Azure resources", "Examples: Global Administrator, User Administrator", "Assigned in the Entra admin center"),
                B.card("Azure roles (RBAC)", "The job is about Azure resources: VMs, storage, databases.", "Scopes: management group, subscription, resource group, resource", "Examples: Owner, Contributor, Reader", "A Global Administrator has none of these by default"),
            ),
            B.tip("\"Only manage VMs in one resource group\" points to Virtual Machine Contributor at that resource group. \"No built-in role fits\" points to a custom role. \"Manage users and licences\" is an Entra role, not an Azure role."),
            B.check(
                "sa-authorization-k1", "sa-authorization",
                "A Global Administrator cannot see any VMs in the subscription. Why?",
                listOf("Entra roles do not grant access to Azure resources", "The VMs need a resource lock removed", "Global Administrator is a custom role"), 0,
                "Entra roles manage the directory; Azure resources need an Azure role. A Global Administrator can elevate themselves to User Access Administrator at the root when needed.",
                whyWrong = mapOf(
                    1 to "A lock stops changes or deletion; it never hides resources from view.",
                    2 to "Global Administrator is a built-in Entra role, and it governs the directory, not resources.",
                ),
            ),
            B.h("Just-in-time admin rights with PIM"),
            B.p("""
                Standing admin rights are a gift to an attacker who steals one password. Privileged Identity Management,
                PIM, makes powerful roles something you switch on for a short time, like borrowing the master key from
                reception and signing it back. A person is made eligible for a role rather than active. When they need
                it they activate it for a few hours, giving a reason, passing multifactor sign-in and, if set, waiting
                for an approver. Every activation is logged. PIM works for Entra roles, Azure roles and group
                membership. It needs a paid licence: Microsoft Entra ID P2 or Microsoft Entra ID Governance.
            """),
            B.check(
                "sa-authorization-k2", "sa-authorization",
                "Admins may hold Owner only when needed, with approval, for at most four hours. What do you recommend?",
                listOf("PIM with eligible assignments and approval", "A custom role with fewer actions", "A resource lock on the subscription"), 0,
                "Time-limited, approved activation of a role is exactly what PIM's eligible assignments give.",
                whyWrong = mapOf(
                    1 to "A smaller role is still a standing one; nothing makes it expire or need approval.",
                    2 to "A lock stops deletion or changes for everyone; it has nothing to do with who holds a role.",
                ),
            ),
            B.h("Reaching apps still on-premises"),
            B.p("""
                Many companies still run web apps in their own building, on-premises. Users first need one identity
                for both worlds. A sync tool, Entra Connect Sync or Cloud Sync, copies the office directory, Active
                Directory, into Entra ID, so the same account signs in to both. Remote users often reach office apps
                through a VPN, a private tunnel into the office network. Microsoft Entra application proxy removes
                the need for one. A small program, the private network connector, sits inside the office. It only
                calls out to the cloud, like a receptionist who phones out but never unlocks the front door. Entra
                checks the user first, with multifactor sign-in and Conditional Access, its if-then sign-in rules.
                Then the connector signs the user in to the app. That works even for apps using Kerberos, the
                built-in Windows sign-in.
            """),
            B.diagram(
                caption = "The user signs in to Entra first; the connector inside the office only calls outwards, so no inbound port is opened.",
                nodes = listOf(
                    DNode("user", "Remote user", 0.1f, 0.25f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
                    DNode("entra", "Entra ID sign-in", 0.5f, 0.2f, w = 0.3f, h = 0.28f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("proxy", "App proxy service", 0.5f, 0.55f, w = 0.34f, h = 0.2f, tone = 1),
                    DNode("conn", "Private network connector", 0.5f, 0.85f, w = 0.4f, h = 0.2f),
                    DNode("app", "On-premises app", 0.85f, 0.85f, w = 0.28f, h = 0.2f, sub = "Kerberos sign-in"),
                    DNode("fw", "No inbound ports open", 0.15f, 0.7f, w = 0.26f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("user", "entra", label = "1 sign in"), DEdge("user", "proxy", label = "2 token"),
                    DEdge("conn", "proxy", label = "outbound only"), DEdge("conn", "app", label = "3 request"),
                ),
                aspect = 0.7f,
            ),
            B.h("Secrets, keys and certificates: Key Vault"),
            B.p("""
                A secret is a password or connection string; a key is used to encrypt or sign; a certificate proves a
                website's identity. Azure Key Vault is the safe for all three. A hardware security module, HSM, is a
                tamper-proof box that keeps a key inside and does the maths there, so the key itself never leaves.
                FIPS 140-3 Level 3 is a US government test for such boxes; Level 3 means the box resists tampering
                and shows it if someone tries. Single-tenant means the HSMs serve only you, not other customers. The
                security domain is the master backup of an HSM's keys. Whoever holds it holds the only spare key to
                the safe.
            """),
            B.table(
                listOf("Option", "What protects the keys", "Holds", "Pick it when"),
                listOf("Key Vault Standard", "Software", "Secrets, keys, certificates", "Ordinary app secrets and certificates"),
                listOf("Key Vault Premium", "Shared HSMs, FIPS 140-3 Level 3", "Secrets, keys, certificates", "Keys must be HSM-protected, at low cost"),
                listOf("Managed HSM", "Your own single-tenant HSM pool", "Keys only", "A dedicated HSM, your own security domain, strict compliance"),
            ),
            B.bullets(
                "Use Azure RBAC on the vault: Key Vault Secrets User to read secrets, Key Vault Crypto User to use keys, Key Vault Administrator for the team that runs it.",
                "One vault per app, per environment, per region, so one leaked vault exposes as little as possible.",
                "Soft delete is a recycle bin for deleted items; purge protection stops anyone, admins included, emptying it early.",
                "Rotation means swapping in a fresh key or secret on a schedule. Keys can rotate on a policy, and certificates can renew themselves with the companies that issue them. A secret about to expire can trigger a small function that replaces it.",
                "Apps read the vault with a managed identity, so no password is needed to fetch the passwords.",
            ),
            B.tip("\"FIPS 140-3 Level 3\" plus \"cheapest\" means Key Vault Premium. \"Single-tenant\" or \"we must hold the security domain\" means Managed HSM. Needing secrets or certificates too rules Managed HSM out."),
            B.check(
                "sa-authorization-k3", "sa-authorization",
                "A bank needs encryption keys in a single-tenant HSM whose security domain it controls. Which option?",
                listOf("Key Vault Managed HSM", "Key Vault Premium", "Key Vault Standard"), 0,
                "Managed HSM is a dedicated, single-tenant pool with its own security domain held by the customer.",
                whyWrong = mapOf(
                    1 to "Premium's HSMs are validated but shared between customers, so it is not single-tenant.",
                    2 to "Standard protects keys in software, with no HSM at all.",
                ),
            ),
            B.code("bash", """
                # Least privilege: VM operators only, one resource group
                az role assignment create --assignee-object-id <group-id> \
                  --role "Virtual Machine Contributor" \
                  --scope /subscriptions/<sub-id>/resourceGroups/tt-api-rg
            """),
            B.worked("""
                Worked design decision. Two Tracks goes global needs a key to sign in-app purchase receipts, the API's
                database connection string, and the TLS certificate (the padlock for twotracks.app). Requirements: the
                signing key must be HSM-protected under FIPS 140-3 Level 3; nobody asks for a dedicated HSM; cost
                matters. Options: Standard, Premium, Managed HSM. Standard is out, its keys are software only. Managed
                HSM is out, it holds keys only and costs far more. Choice: one Premium vault per environment holding
                all three, with the API reading it by managed identity and the Key Vault Secrets User role.
            """),
            B.faded(
                "sa-authorization-f1",
                "A health app must keep its record-encryption key in a dedicated HSM whose security domain the company holds; its app secrets need a safe place too.",
                listOf(
                    "Premium is out for the key: its HSMs are shared between customers.",
                    "Put the encryption key in a ___ HSM, which is single-tenant and holds keys only.",
                    "Keep the app secrets in an ordinary Key Vault, because Managed HSM cannot hold secrets.",
                ),
                blank = 1,
                answers = listOf("managed"),
                note = "Shared HSM and low cost: Premium. Dedicated HSM and your own security domain: Managed HSM, which stores keys only.",
            ),
            B.steps("Grant just-in-time access for real",
                "In the Entra admin center open ID Governance, then Privileged Identity Management, then Azure resources, and pick the production subscription" to "You see its roles and current assignments.",
                "Add an assignment: role Owner, the on-call group, type Eligible, ending in six months" to "The group shows under Eligible assignments, not Active.",
                "Open the Owner role settings and require approval, a justification and a maximum of four hours" to "Activation now waits for an approver.",
                "As a group member open My roles and activate Owner with a reason" to "After approval the role is active and expires on its own.",
                "Open the audit history" to "The request, the approver and the times are all recorded.",
            ),
            B.mistakes(
                "Assigning Owner at the subscription when Contributor on one resource group would do.",
                "Expecting a Global Administrator to manage VMs; Entra roles do not reach Azure resources.",
                "Giving admins standing Owner rights instead of making them eligible in PIM.",
                "Opening an inbound firewall port for an internal web app instead of using application proxy.",
                "Choosing Managed HSM and then finding it cannot store the app's secrets or certificates.",
            ),
            B.example("Two Tracks goes global: developers are Contributors on their team's resource groups through Entra groups, on-call engineers are eligible for Owner on production in PIM, the old office timesheet app is published with application proxy, and each environment has its own Premium vault read by managed identity."),
            B.h("Check yourself"),
            B.match(
                "sa-x-authz-match", "Pair each requirement with what it points to.",
                "Admin rights only when needed, with approval" to "Privileged Identity Management",
                "Remote access to an internal web app, no VPN" to "Entra application proxy",
                "Keys in a dedicated HSM with your own security domain" to "Managed HSM",
                "No built-in role has the right actions" to "A custom role",
            ),
            B.gap(
                "sa-x-authz-gap1", "The rule that keeps rights small.",
                "Grant the smallest role at the narrowest scope: the principle of least ___.", "privilege", "Privilege",
            ),
            B.tryIt("Try it in 15 minutes", "Free in any subscription where you are Owner; nothing here costs money.",
                "Create a resource group and assign a test group Reader on it",
                "Open Access control (IAM), then Check access, and confirm what that group can do",
                "Find the Virtual Machine Contributor role and read its actions list",
                "Create a Key Vault with RBAC, grant yourself Key Vault Secrets User and read a secret back",
            ),
            B.explain(
                "sa-authorization-x1",
                "Explain how you would give a team the access it needs in Azure, keep admin rights safe, and store the team's secrets.",
                "A role assignment joins a security principal, a role and a scope, and the scope is inherited downwards, so I assign the smallest built-in role to an Entra group at the narrowest scope, following least privilege, and write a custom role only if none fits. Powerful roles like Owner are made eligible in Privileged Identity Management, so people activate them just in time with approval and the activation is logged. Secrets, keys and certificates go in Key Vault, read with a managed identity; Premium when keys must be HSM-protected and Managed HSM for a dedicated single-tenant HSM.",
                "Role assignment at a narrow scope, least privilege" to listOf("least privilege", "scope", "role assignment", "built-in role", "custom role"),
                "PIM for just-in-time admin rights" to listOf("privileged identity management", "pim", "eligible", "just in time", "just-in-time"),
                "Key Vault and the right tier" to listOf("key vault", "managed hsm", "premium", "hsm"),
            ),
            B.glossary(
                "Azure RBAC" to "Rules saying which principal may do which actions on which Azure resources.",
                "Scope" to "Where a role applies: management group, subscription, resource group or resource; inherited downwards.",
                "Least privilege" to "Granting the smallest role at the narrowest scope that does the job.",
                "PIM" to "Privileged Identity Management: roles made eligible and switched on for a short, logged time.",
                "Application proxy" to "Publishes an on-premises web app through Entra, using a connector that only calls out.",
                "HSM" to "A hardware security module: a tamper-proof box that keeps a key inside and uses it there.",
                "Managed HSM" to "A dedicated, single-tenant HSM pool for keys, with a security domain you hold.",
            ),
            B.recap(
                "A role assignment is who, what and where; grant the least role at the narrowest scope, to groups.",
                "Entra roles run the directory, Azure roles run resources; PIM makes powerful roles eligible and time-limited.",
                "Application proxy reaches on-premises web apps with no inbound ports; Key Vault Standard, Premium or Managed HSM by how keys must be protected.",
            ),
        ),
    )

    private val governance = Topic(
        id = "sa-governance", title = "Governance: management groups, policy, tags and identity governance", minutes = 30,
        references = listOf(
            B.ref("Organize your resources with management groups", LEARN + "azure/governance/management-groups/overview"),
            B.ref("What is Azure Policy?", LEARN + "azure/governance/policy/overview"),
            B.ref("Azure Policy definitions effect basics", LEARN + "azure/governance/policy/concepts/effect-basics"),
            B.ref("Use tags to organize your Azure resources", LEARN + "azure/azure-resource-manager/management/tag-resources"),
            B.ref("Lock your resources to protect your infrastructure", LEARN + "azure/azure-resource-manager/management/lock-resources"),
            B.ref("Regulatory compliance in Defender for Cloud", LEARN + "azure/defender-for-cloud/concept-regulatory-compliance-standards"),
            B.ref("Microsoft Entra ID Governance overview", LEARN + "entra/id-governance/identity-governance-overview"),
            B.ref("What is an Azure landing zone?", LEARN + "azure/cloud-adoption-framework/ready/landing-zone/"),
            B.ref("Learn module: Design governance", LEARN + "training/modules/design-governance/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-governance-w1", "sa-governance",
                "Nothing may be created outside EU regions, in every subscription now and later. Where would you set that rule?",
                listOf("Once, on a management group above them all", "On each resource group as it is made", "As a tag on every resource"), 0,
                "Rules set high up flow down to everything underneath, including what is added later.",
            ),
            B.plain("""
                Governance is the house rules for a growing estate: how things are filed, who pays for what, and what
                is never allowed. Picture a block of flats. The building has rules for every flat, each flat has its
                own bills, and each room holds things that belong together. Azure has the same layers, and a rule set
                on a higher layer applies to everything below it.
            """),
            B.diagram(
                caption = "Management groups hold subscriptions, which hold resource groups; rules set high up flow down.",
                nodes = listOf(
                    DNode("root", "Tenant root group", 0.5f, 0.1f, w = 0.44f, h = 0.14f, tone = 2),
                    DNode("tt", "Two Tracks", 0.5f, 0.32f, w = 0.4f, h = 0.14f),
                    DNode("plat", "Platform", 0.17f, 0.54f, w = 0.3f, h = 0.14f, tone = 1),
                    DNode("lz", "Landing zones", 0.5f, 0.54f, w = 0.3f, h = 0.14f, tone = 1),
                    DNode("sand", "Sandbox", 0.83f, 0.54f, w = 0.3f, h = 0.14f, tone = 1),
                    DNode("s1", "Connectivity sub", 0.17f, 0.76f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    DNode("s2", "App sub: tt-prod", 0.5f, 0.76f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    DNode("s3", "Dev sub", 0.83f, 0.76f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    DNode("rg", "Resource groups", 0.5f, 0.94f, w = 0.4f, h = 0.1f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("root", "tt"), DEdge("tt", "plat"), DEdge("tt", "lz"), DEdge("tt", "sand"),
                    DEdge("plat", "s1"), DEdge("lz", "s2"), DEdge("sand", "s3"), DEdge("s2", "rg"),
                ),
                aspect = 0.9f,
            ),
            B.h("Four layers and how to structure them"),
            B.table(
                listOf("Layer", "What it is", "Structure it by"),
                listOf("Management group", "A folder of subscriptions, for shared rules and access", "Kinds of workload that need different rules, not the org chart"),
                listOf("Subscription", "A billing and scale boundary with its own limits", "Workload and environment, for example prod and dev apart"),
                listOf("Resource group", "A box of resources that live and die together", "Lifecycle: what is deployed and deleted as one"),
                listOf("Resource", "One VM, database or storage account", "Belongs to exactly one resource group"),
            ),
            B.bullets(
                "Every directory has one root management group, the Tenant root group; new subscriptions land there unless moved.",
                "Management groups nest up to six levels deep, not counting the root or the subscriptions, and each subscription has exactly one parent. Keep it shallow, three or four levels.",
                "Separate production and non-production subscriptions so a test cannot eat production's limits or break its rules.",
            ),
            B.p("""
                The Cloud Adoption Framework, Microsoft's guide for moving to Azure, describes landing zones: a
                subscription made ready before any app moves in, with networking, identity, policy and monitoring
                already in place, like a building plot with water and power connected. Platform landing zones hold
                shared services (identity, management, connectivity); application landing zones hold the apps, often
                split into Corp (private, linked to the office) and Online (public facing), with a Sandbox group for
                experiments.
            """),
            B.h("A tagging strategy"),
            B.p("""
                A tag is a name and value label stuck on a resource, like costCentre = mobile. Tags let you split the
                bill, find an owner at 3 a.m. or spot which data is sensitive. Agree a short list up front: cost
                centre, owner, environment, application and data classification. Tags are not inherited from the
                resource group by default. Azure Policy, Azure's rule checker described next, can require them or
                copy them down.
            """),
            B.check(
                "sa-governance-k1", "sa-governance",
                "Finance wants costs split by department across many subscriptions. What do you recommend?",
                listOf("A required costCentre tag, enforced with Azure Policy", "One management group per department", "A resource lock per department"), 0,
                "Tags label resources wherever they sit and show up in Cost Management, and a policy makes sure nobody forgets them.",
                whyWrong = mapOf(
                    1 to "Management groups are for rules and access; reorganising them around departments fights the workload structure.",
                    2 to "A lock stops deletion or changes; it carries no cost information.",
                ),
            ),
            B.h("Azure Policy: rules about what may exist"),
            B.p("""
                Azure Policy checks resources against rules, whoever creates them. A policy definition is one rule,
                such as "only these regions". An initiative is a bundle of definitions, such as everything a standard
                asks for. An assignment attaches a definition or an initiative to a scope, with exclusions for
                anything left out. Policy checks new and changed resources as they are deployed, like a building
                inspector at the door. It also scans existing ones and reports which follow the rules, called
                compliance. The effect is what a rule does when something breaks it.
            """),
            B.table(
                listOf("Effect", "What it does", "Requirement words"),
                listOf("deny", "Blocks the create or change", "must not, prevent, only allow"),
                listOf("audit", "Allows it, marks it non-compliant", "report, find out, without blocking"),
                listOf("modify", "Adds or changes tags and some properties", "add the tag, inherit, fix automatically"),
                listOf("deployIfNotExists", "Deploys a missing companion, a resource that should sit beside it, such as a diagnostic setting", "ensure diagnostics or an agent is always deployed"),
                listOf("auditIfNotExists", "Reports a missing companion resource", "report VMs without an add-on installed"),
                listOf("disabled", "Turns the rule off for this assignment", "pause a rule, test"),
            ),
            B.remember("modify and deployIfNotExists act on new resources at once, but existing ones are fixed only by a remediation task, which runs as the assignment's managed identity."),
            B.compare("Policy or RBAC?",
                B.card("Azure RBAC", "The question is who may act.", "Grants actions to principals at a scope", "Says nothing about what a resource looks like", "Example: Contributor on a resource group"),
                B.card("Azure Policy", "The question is what may exist, whoever acts.", "Checks resource properties at deploy time and later", "Even an Owner is blocked by deny", "Example: only EU regions, only small VM sizes"),
            ),
            B.check(
                "sa-governance-k2", "sa-governance",
                "Every new SQL database must send its logs to the central workspace, with no manual step. Which effect?",
                listOf("deployIfNotExists", "audit", "deny"), 0,
                "deployIfNotExists adds the missing diagnostic setting as each database is created.",
                whyWrong = mapOf(
                    1 to "audit only reports the missing setting; nobody fixes it.",
                    2 to "deny would block the database instead of completing it.",
                ),
            ),
            B.h("Compliance and protection"),
            B.p("""
                Microsoft Defender for Cloud is Azure's security check-up: it scores how safe your setup is. Its
                regulatory compliance dashboard is a report card against a standard, a published list of rules. It
                starts with the Microsoft cloud security benchmark, Microsoft's own baseline list of checks. You
                can add others, such as GDPR (the EU privacy law), ISO 27001 (an international security standard)
                or PCI DSS (the card-payment rules). Adding standards beyond the default needs at least one paid
                Defender plan. Each standard maps its controls, the individual rules, to policies, so you see which
                pass. Older designs used Azure Blueprints to stamp out governed subscriptions. Blueprints is being
                retired: a phased shutdown began on 31 July 2026 and it ends on 31 January 2027. Microsoft points to
                deployment stacks instead, which deploy a set of resources as one unit and guard it.
            """),
            B.p("""
                A resource lock is a guard against mistakes. CanNotDelete lets people change a resource but not delete
                it; ReadOnly allows neither. Locks are inherited and stop everyone, Owners included, until the lock is
                removed.
            """),
            B.h("Identity governance"),
            B.table(
                listOf("Feature", "What it does", "Requirement words"),
                listOf("Access reviews", "Owners or managers confirm, on a schedule, who still needs access", "recertify, every quarter, remove stale guests"),
                listOf("Entitlement management", "Access packages people request, with approval and an end date", "partners request access, bundle of resources, expires"),
                listOf("Lifecycle workflows", "Automatic tasks when people join, move or leave", "first day, leaver, disable account on last day"),
                listOf("Privileged Identity Management", "Admin roles made eligible and switched on for a short time", "just in time, approval for admin rights"),
            ),
            B.tip("These are paid features: access reviews, entitlement management and PIM come with Microsoft Entra ID P2, and lifecycle workflows need Microsoft Entra ID Governance. Guests from a partner company who ask for a set of resources with approval and expiry point to entitlement management access packages."),
            B.check(
                "sa-governance-k3", "sa-governance",
                "Managers must confirm every quarter that guest users still need their group memberships. What do you use?",
                listOf("Access reviews", "Lifecycle workflows", "A resource lock"), 0,
                "A recurring access review asks reviewers to confirm or remove each member, and can remove denied guests automatically.",
                whyWrong = mapOf(
                    1 to "Lifecycle workflows react to joining, moving and leaving, not to a quarterly check.",
                    2 to "A lock protects resources from deletion, not group membership.",
                ),
            ),
            B.code("bash", """
                # Allowed locations, assigned once on the landing zones management group
                az policy assignment create --name eu-only \
                  --policy e56962a6-4747-49cd-b67b-bf8b01975c4c \
                  --scope /providers/Microsoft.Management/managementGroups/tt-landingzones \
                  --params '{ "listOfAllowedLocations": { "value": ["westeurope", "northeurope"] } }'
                az lock create --name keep-db --lock-type CanNotDelete --resource-group tt-prod-data
            """),
            B.worked("""
                Worked design decision. Two Tracks goes global must keep European user data in the EU for GDPR, and
                some subscriptions will be created next year. Options: a deny policy per subscription, an audit
                policy, or one deny assignment of the built-in Allowed locations policy on the Landing zones
                management group. Per subscription is easy to forget for new ones; audit only reports. Choice: deny
                with Allowed locations at the management group, so every current and future subscription under it
                is covered, with the Sandbox group left outside it.
            """),
            B.faded(
                "sa-governance-f1",
                "Every resource must carry its resource group's costCentre tag, including the hundreds that already exist, without blocking deployments.",
                listOf(
                    "deny is out: it would block deployments and still not copy the value.",
                    "Assign the built-in inherit-a-tag policy, which uses the ___ effect to add the tag from the resource group.",
                    "Run a remediation task with the assignment's managed identity to fix the resources that already exist.",
                ),
                blank = 1,
                answers = listOf("modify"),
                note = "Blocking: deny. Reporting: audit. Changing tags or properties: modify. Adding a missing companion resource: deployIfNotExists.",
            ),
            B.steps("Set up a management group and a rule",
                "In the portal open Management groups and create tt-landingzones under the Tenant root group" to "The new group appears in the tree.",
                "Move a test subscription under it" to "The subscription now shows the group as its parent.",
                "Open Policy, then Definitions, search Allowed locations and assign it to tt-landingzones with only West Europe allowed" to "The assignment appears on the Assignments page.",
                "Try to create a storage account in East US in that subscription" to "The deployment fails with a policy denial naming eu-only.",
                "Open Compliance after a while" to "The assignment shows its compliance state for existing resources.",
            ),
            B.mistakes(
                "Mirroring the org chart in management groups, then rebuilding it after every reorganisation.",
                "Assigning a policy to each subscription by hand, so new subscriptions arrive without it.",
                "Expecting resource group tags to appear on resources without a modify policy.",
                "Assuming deployIfNotExists fixes existing resources without running a remediation task.",
                "Using audit when the requirement says resources must not be created at all.",
            ),
            B.example("Two Tracks goes global: a Two Tracks management group under the root, with Platform, Landing zones (Corp and Online) and Sandbox below; Allowed locations deny on Landing zones; a required costCentre tag inherited by modify; GDPR tracked in Defender for Cloud; quarterly access reviews for partner guests."),
            B.h("Check yourself"),
            B.match(
                "sa-x-gov-match", "Pair each requirement with the effect or feature it points to.",
                "Resources must never be created outside the EU" to "deny",
                "Ensure every new database sends logs to the workspace" to "deployIfNotExists",
                "Stop anyone deleting the production database by accident" to "CanNotDelete lock",
                "Partners request a bundle of resources that expires" to "Entitlement management",
            ),
            B.gap(
                "sa-x-gov-gap1", "A bundle of policy definitions assigned together.",
                "A group of policy definitions assigned as one is called an ___.", "initiative", "Initiative",
            ),
            B.gap(
                "sa-x-gov-gap2", "The top of every hierarchy.",
                "Every directory has one ___ management group above all others.", "root", "Root",
            ),
            B.tryIt("Try it in 15 minutes", "Free: management groups, policy and locks cost nothing.",
                "Create a management group and move a test subscription under it",
                "Assign Allowed locations and watch a deployment in another region fail",
                "Add a tag to a resource group and see that its resources do not have it",
                "Put a CanNotDelete lock on a resource group and try to delete it",
            ),
            B.explain(
                "sa-governance-x1",
                "Explain how you would organise a growing Azure estate and keep it compliant.",
                "Subscriptions sit under management groups arranged by kinds of workload, such as platform, landing zones and sandbox, so a policy or role assigned high up is inherited by every subscription, including new ones. Azure Policy enforces the rules: deny to block things like resources outside allowed regions, modify to add or inherit tags, deployIfNotExists to add missing diagnostics, with remediation for existing resources. A tagging strategy with cost centre and owner splits the bill. Defender for Cloud shows regulatory compliance, and access reviews and entitlement management keep identity access current.",
                "Management group hierarchy and inheritance" to listOf("management group", "landing zone", "inherited", "hierarchy"),
                "Azure Policy and its effects" to listOf("azure policy", "deny", "deployifnotexists", "modify", "initiative"),
                "Tags, compliance and identity governance" to listOf("tagging", "cost centre", "defender for cloud", "access review", "entitlement management"),
            ),
            B.glossary(
                "Management group" to "A folder of subscriptions; rules and roles assigned to it flow down.",
                "Landing zone" to "A subscription prepared in advance with networking, identity, policy and monitoring.",
                "Tag" to "A name and value label on a resource, used for cost, ownership and search.",
                "Initiative" to "A bundle of policy definitions assigned together.",
                "Remediation task" to "Applies a modify or deployIfNotExists policy to resources that already exist.",
                "Resource lock" to "CanNotDelete or ReadOnly protection that stops even Owners until removed.",
                "Access package" to "A bundle of access people request, with approval and an end date.",
            ),
            B.recap(
                "Management groups by workload, subscriptions by workload and environment, resource groups by lifecycle; rules flow down.",
                "Azure Policy decides what may exist: deny blocks, audit reports, modify fixes tags, deployIfNotExists adds what is missing.",
                "Tags for cost and ownership, locks against accidents, Defender for Cloud for standards, access reviews and entitlement management for identity.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(authorization, governance)

    val quiz: Quiz = Quiz(
        id = "sa-identity-test", title = "Identity, governance and monitoring module test",
        questions = B.spread(
            listOf(
                B.q(
                    "sa-i1", "sa-monitoring",
                    "Logs from many Azure resources must be queried together with KQL and kept for two years. What do you recommend?",
                    listOf("Diagnostic settings sending to a Log Analytics workspace", "Diagnostic settings sending to an event hub", "The activity log viewed in the portal"), setOf(0),
                    "A Log Analytics workspace stores logs from many sources in one place and is queried with KQL, with retention you set.",
                    whyWrong = mapOf(
                        1 to "An event hub streams logs onwards to another system; it does not store or query them.",
                        2 to "The activity log only covers control-plane events and keeps them for 90 days.",
                    ),
                ),
                B.q(
                    "sa-i2", "sa-monitoring",
                    "Security wants Azure resource logs streamed to a third-party SIEM in near real time. Where should diagnostic settings send them?",
                    listOf("Azure Event Hubs", "A storage account", "Application Insights"), setOf(0),
                    "Event Hubs is the streaming destination that outside tools such as a third-party SIEM read from.",
                    whyWrong = mapOf(
                        1 to "A storage account is cheap archive, not a real-time stream.",
                        2 to "Application Insights monitors an app's own requests; it is not a route out to another tool.",
                    ),
                ),
                B.q(
                    "sa-i3", "sa-monitoring",
                    "Audit logs must be kept for seven years at the lowest cost and are read about once a year. What do you recommend?",
                    listOf("A storage account destination, with a cool or archive tier", "An event hub", "Keeping them only in the activity log"), setOf(0),
                    "Storage is the cheapest place for logs that are rarely read, and lifecycle rules can move them to colder tiers.",
                    whyWrong = mapOf(
                        1 to "An event hub is a pipe, not a store: it keeps events for 90 days at most, even on its top tiers, far short of seven years.",
                        2 to "The activity log keeps 90 days, far short of seven years.",
                    ),
                ),
                B.q(
                    "sa-i4", "sa-monitoring",
                    "Developers need request timings, failed calls to dependencies and end-to-end traces for a web API. What do you recommend?",
                    listOf("Application Insights", "Azure Monitor metrics alone", "Defender for Cloud"), setOf(0),
                    "Application Insights is the application performance monitoring part of Azure Monitor: requests, dependencies, exceptions and distributed traces.",
                    whyWrong = mapOf(
                        1 to "Platform metrics show numbers like CPU, but not each request or the call chain behind it.",
                        2 to "Defender for Cloud looks at security posture, not application performance.",
                    ),
                ),
                B.q(
                    "sa-i5", "sa-monitoring",
                    "Event logs and performance counters from inside 200 VMs must reach a workspace, configured centrally. What do you recommend?",
                    listOf("Azure Monitor Agent with a data collection rule", "Diagnostic settings on each VM resource", "Application Insights on each VM"), setOf(0),
                    "The Azure Monitor Agent collects guest operating system data, and one data collection rule says what to collect and where to send it for many VMs.",
                    whyWrong = mapOf(
                        1 to "Diagnostic settings route platform logs about the resource, not what happens inside the guest operating system.",
                        2 to "Application Insights watches an application's requests, not the VM's event logs and counters.",
                    ),
                ),
                B.q(
                    "sa-i6", "sa-identity",
                    "Staff should use their office password for cloud sign-in, and cloud sign-in must keep working if the office is offline. What do you recommend?",
                    listOf("Password hash synchronisation", "Pass-through authentication", "Federation with AD FS"), setOf(0),
                    "Password hash sync lets Entra ID check the password itself, so cloud sign-in does not depend on servers in the office.",
                    whyWrong = mapOf(
                        1 to "Pass-through authentication checks every sign-in against on-premises agents, so an office outage stops it.",
                        2 to "Federation sends every sign-in to the office's own servers, the most on-premises dependency of all.",
                    ),
                ),
                B.q(
                    "sa-i7", "sa-identity",
                    "Users must pass multifactor sign-in only when they sign in from outside the company network. What do you recommend?",
                    listOf("A Conditional Access policy with a named location", "Security defaults", "Per-user MFA for everyone"), setOf(0),
                    "Conditional Access applies conditions such as location before granting access, so MFA can be required only outside trusted networks.",
                    whyWrong = mapOf(
                        1 to "Security defaults are one fixed setting for the whole tenant, with no location conditions.",
                        2 to "Per-user MFA has no location rule of its own. Skipping office sign-ins needs the separate legacy trusted IPs setting, and Microsoft recommends a Conditional Access named location instead.",
                    ),
                ),
                B.q(
                    "sa-i8", "sa-identity",
                    "Staff at a partner company must sign in to your internal app with their own work accounts. What do you recommend?",
                    listOf("Entra B2B collaboration, inviting them as guests", "Creating new accounts for them in your directory", "Sharing one service account"), setOf(0),
                    "B2B collaboration lets partners use their own organisation's identities as guests in your tenant, so you manage no passwords for them.",
                    whyWrong = mapOf(
                        1 to "New accounts mean new passwords you must manage and remember to remove.",
                        2 to "A shared account cannot tell people apart and breaks every audit.",
                    ),
                ),
                B.q(
                    "sa-i9", "sa-identity",
                    "A public consumer app needs self-service sign-up with email or social accounts, separate from staff identities. What do you recommend?",
                    listOf("Microsoft Entra External ID in an external tenant", "Entra B2B guests in the workforce tenant", "Active Directory Domain Services"), setOf(0),
                    "External ID with an external tenant is built for customer identities: sign-up, social sign-in and branding, kept apart from staff.",
                    whyWrong = mapOf(
                        1 to "B2B is for partners joining the workforce tenant, not millions of consumers signing themselves up.",
                        2 to "Domain Services is a Windows domain for servers and old apps, not consumer sign-up.",
                    ),
                ),
                B.q(
                    "sa-i10", "sa-identity",
                    "An app on a VM must read from a storage account with no credential stored anywhere. What do you recommend?",
                    listOf("A managed identity with a storage data role", "A storage account key in an app setting", "A shared access signature in the code"), setOf(0),
                    "A managed identity is an Entra identity Azure creates and rotates for the VM; grant it a role such as Storage Blob Data Reader.",
                    whyWrong = mapOf(
                        1 to "An account key is a stored credential, and it unlocks the whole account.",
                        2 to "A shared access signature in code is still a secret that can leak.",
                    ),
                ),
                B.q(
                    "sa-i11", "sa-authorization",
                    "Operators must start, stop and resize VMs in one resource group and nothing else. What do you assign?",
                    listOf("Virtual Machine Contributor at that resource group", "Contributor at the subscription", "Owner at that resource group"), setOf(0),
                    "The service-specific role at the narrowest scope is least privilege.",
                    whyWrong = mapOf(
                        1 to "Contributor on the subscription covers every resource type everywhere, far more than needed.",
                        2 to "Owner also lets them grant access to others.",
                    ),
                ),
                B.q(
                    "sa-i12", "sa-authorization",
                    "Admins need Owner on production only during incidents, with approval, MFA and an automatic end. What do you recommend?",
                    listOf("Eligible assignments in Privileged Identity Management", "A permanent Owner assignment with a strong password", "A custom role without delete actions"), setOf(0),
                    "PIM makes the role eligible; it is activated for a set time with approval and MFA, and every activation is logged.",
                    whyWrong = mapOf(
                        1 to "A permanent assignment is standing access, which is exactly what the requirement rules out.",
                        2 to "A custom role is still standing access with no approval or expiry.",
                    ),
                ),
                B.q(
                    "sa-i13", "sa-authorization",
                    "Remote staff must reach an internal web app that uses Windows sign-in, with MFA, no VPN and no inbound firewall ports. What do you recommend?",
                    listOf("Entra application proxy with Kerberos constrained delegation", "A public IP on the app server", "A site-to-site VPN"), setOf(0),
                    "Application proxy pre-authenticates in Entra with MFA, and its connector only calls outwards and signs the user in to the app with Kerberos.",
                    whyWrong = mapOf(
                        1 to "A public IP exposes the server directly and needs an inbound port.",
                        2 to "A site-to-site VPN joins networks, not remote users, and the requirement rules out a VPN.",
                    ),
                ),
                B.q(
                    "sa-i14", "sa-authorization",
                    "Keys must be in FIPS 140-3 Level 3 HSMs at the lowest cost, and the vault also stores app secrets. What do you recommend?",
                    listOf("Key Vault Premium", "Key Vault Managed HSM", "Key Vault Standard"), setOf(0),
                    "Premium keeps keys in validated HSMs shared between customers, at low cost, and still holds secrets and certificates.",
                    whyWrong = mapOf(
                        1 to "Managed HSM costs far more and stores keys only, so the secrets would need another vault.",
                        2 to "Standard protects keys in software, not in an HSM.",
                    ),
                ),
                B.q(
                    "sa-i15", "sa-authorization",
                    "A support team must read VM settings and restart VMs, but no built-in role matches exactly. What do you recommend?",
                    listOf("A custom role with just those actions", "Contributor, since it includes them", "A resource lock"), setOf(0),
                    "A custom role lists exactly the actions needed and the scopes where it may be assigned.",
                    whyWrong = mapOf(
                        1 to "Contributor also allows creating and deleting everything, which breaks least privilege.",
                        2 to "A lock prevents changes; it grants nobody anything.",
                    ),
                ),
                B.q(
                    "sa-i16", "sa-authorization",
                    "A user is a Global Administrator but gets access denied on every subscription. What explains it?",
                    listOf("Entra roles do not grant Azure resource access", "Global Administrator has expired", "The subscriptions have ReadOnly locks"), setOf(0),
                    "Entra roles govern the directory; Azure resources need Azure roles. A Global Administrator can elevate to User Access Administrator at the root to fix it.",
                    whyWrong = mapOf(
                        2 to "A ReadOnly lock still lets people see resources; it does not deny access to view them.",
                    ),
                ),
                B.q(
                    "sa-i17", "sa-governance",
                    "Resources must only be created in EU regions in all subscriptions, including ones created later. What do you recommend?",
                    listOf("Allowed locations with deny, assigned to a management group", "Allowed locations with audit, on each subscription", "A tag naming the region on each resource"), setOf(0),
                    "Assigned to a management group, the policy is inherited by every subscription under it, and deny blocks the deployment.",
                    whyWrong = mapOf(
                        1 to "Audit only reports, and per-subscription assignments miss subscriptions added later.",
                        2 to "A tag is a label; it cannot stop a deployment.",
                    ),
                ),
                B.q(
                    "sa-i18", "sa-governance",
                    "Nobody, Owners included, may delete the production database by accident, but schema and settings must stay changeable. What do you recommend?",
                    listOf("A CanNotDelete lock", "A ReadOnly lock", "Removing Owner from everyone"), setOf(0),
                    "CanNotDelete stops deletion for everyone while still allowing changes.",
                    whyWrong = mapOf(
                        1 to "ReadOnly also blocks changes to settings, which the requirement keeps.",
                        2 to "Someone still needs Owner, and a mistake by them would still delete it.",
                    ),
                ),
                B.q(
                    "sa-i19", "sa-governance",
                    "Every new storage account must automatically get a diagnostic setting sending logs to the central workspace. Which policy effect?",
                    listOf("deployIfNotExists", "auditIfNotExists", "append"), setOf(0),
                    "deployIfNotExists deploys the missing companion resource; existing accounts are fixed with a remediation task.",
                    whyWrong = mapOf(
                        1 to "auditIfNotExists only reports the missing setting.",
                        2 to "append adds fields to the resource being created; it cannot deploy a separate diagnostic setting.",
                    ),
                ),
                B.q(
                    "sa-i20", "sa-governance",
                    "Partner staff should request a bundle of groups and apps, approved by a sponsor, with access ending after 90 days. What do you recommend?",
                    listOf("Entitlement management access packages", "Access reviews", "Lifecycle workflows"), setOf(0),
                    "An access package bundles resources with a request, an approval and an expiry, and can add partners as guests when approved.",
                    whyWrong = mapOf(
                        1 to "Access reviews recheck existing access; they do not handle requests.",
                        2 to "Lifecycle workflows react to employees joining, moving or leaving.",
                    ),
                ),
                B.q(
                    "sa-i21", "sa-governance",
                    "Auditors want to see how the estate measures up against ISO 27001, control by control. What do you recommend?",
                    listOf("The regulatory compliance dashboard in Defender for Cloud", "Azure Advisor cost recommendations", "Resource locks on every subscription"), setOf(0),
                    "Adding the ISO 27001 standard in Defender for Cloud maps its controls to policies and shows which pass.",
                    whyWrong = mapOf(
                        1 to "Advisor gives best-practice tips; it does not map controls of a standard.",
                        2 to "Locks prevent deletion; they report nothing about compliance.",
                    ),
                ),
                B.q(
                    "sa-i22", "sa-governance",
                    "Tags set on resource groups must appear on all resources inside, including existing ones. What do you recommend?",
                    listOf("An inherit-tag policy with modify, plus a remediation task", "Nothing; resources inherit tags automatically", "A deny policy requiring the tag"), setOf(0),
                    "Tags are not inherited by default; modify copies them and a remediation task fixes what already exists.",
                    whyWrong = mapOf(
                        1 to "Resources never inherit their group's tags on their own.",
                        2 to "deny blocks new untagged resources but never copies a value onto existing ones.",
                    ),
                ),
            ),
        ),
    )
}
