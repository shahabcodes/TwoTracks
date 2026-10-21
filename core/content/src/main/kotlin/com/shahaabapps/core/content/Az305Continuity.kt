package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Continuity {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val backupDr = Topic(
        id = "sa-backup-dr", title = "Backup and disaster recovery: meeting RPO and RTO", minutes = 30,
        references = listOf(
            B.ref("What is the Azure Backup service?", LEARN + "azure/backup/backup-overview"),
            B.ref("Overview of the Backup vaults", LEARN + "azure/backup/backup-vault-overview"),
            B.ref("About Azure Site Recovery", LEARN + "azure/site-recovery/site-recovery-overview"),
            B.ref("Business continuity and disaster recovery for Azure SQL Database", LEARN + "azure/azure-sql/database/business-continuity-high-availability-disaster-recover-hadr-overview"),
            B.ref("Automated backups in Azure SQL Database", LEARN + "azure/azure-sql/database/automated-backups-overview"),
            B.ref("Long-term retention for Azure SQL Database", LEARN + "azure/azure-sql/database/long-term-retention-overview"),
            B.ref("Overview of Azure Blob backup", LEARN + "azure/backup/blob-backup-overview"),
            B.ref("Learn module: Design a solution for backup and disaster recovery", LEARN + "training/modules/design-solution-for-backup-disaster-recovery/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-backup-dr-w1", "sa-backup-dr",
                "The office booking system may lose at most 15 minutes of data if its region fails. Is a nightly backup enough?",
                listOf("No; it needs replication to another region", "Yes, if the backup is geo-redundant", "Yes, if it is kept for a year"), 0,
                "Think about how much data is lost between the last copy and the moment the region goes down.",
            ),
            B.plain("""
                Backup is a copy you keep so you can go back after a mistake, like a photo of your notes each night.
                Disaster recovery, DR for short, is the plan for carrying on when a whole site is gone, like a spare
                office you can move into. Two numbers set the design. RPO, the recovery point objective, is how much
                recent data you can afford to lose. RTO, the recovery time objective, is how long you can be down.
            """),
            B.diagram(
                caption = "RPO looks back from the disaster to the last good copy; RTO looks forward to the moment the app runs again.",
                nodes = listOf(
                    DNode("last", "Last good copy", 0.15f, 0.25f, w = 0.26f, h = 0.2f, tone = 1),
                    DNode("boom", "Disaster", 0.5f, 0.25f, w = 0.22f, h = 0.2f, tone = 2),
                    DNode("back", "Running again", 0.85f, 0.25f, w = 0.26f, h = 0.2f, tone = 1),
                    DNode("rpo", "RPO = data you can lose", 0.27f, 0.7f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    DNode("rto", "RTO = time until running again", 0.75f, 0.7f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("last", "boom", label = "RPO"),
                    DEdge("boom", "back", label = "RTO"),
                ),
                aspect = 0.55f,
            ),
            B.h("Reading the requirement"),
            B.table(
                listOf("The question says", "It is about", "It points to"),
                listOf("Restore yesterday's file, a deleted row", "Mistakes and corruption", "Backup with point-in-time restore"),
                listOf("Keep monthly copies for seven years", "Compliance retention", "Long-term retention in a backup policy"),
                listOf("Survive a region outage, RPO in minutes", "Disaster recovery", "Replication: Site Recovery, geo-replication"),
                listOf("Survive a region outage, hours are fine", "Cheap disaster recovery", "Geo-redundant backup and restore elsewhere"),
            ),
            B.tip("Replication copies mistakes too: a deleted table is deleted in the replica seconds later. A design that needs both \"survive a region\" and \"undo a mistake\" needs replication and backup."),
            B.h("Azure Backup and its two vaults"),
            B.p("""
                Azure Backup is the managed backup service: you choose what to protect and a policy, and it takes the
                copies. Copies live in a vault, a locked store for backups that sits apart from your own resources.
                A backup policy is the rule for the vault: how often to copy, and how long to keep each copy (daily,
                weekly, monthly and yearly points).
            """),
            B.compare("Which vault?",
                B.card("Recovery Services vault", "VMs, Azure Files, SQL or SAP HANA inside a VM, on-premises servers, Site Recovery.", "The older, broad vault", "Holds the MARS agent backups of office machines", "Site Recovery is managed from here too"),
                B.card("Backup vault", "Blobs, managed disks, PostgreSQL, AKS and newer workloads.", "The newer vault for newer workloads", "Blob vaulted backup lives here", "Same idea: policies, RBAC, redundancy"),
            ),
            B.bullets(
                "VM backup takes a snapshot of the disks, keeps it for fast restore, then copies it to the vault.",
                "Azure Files backup uses share snapshots, and can also copy them to the vault.",
                "Blob backup has two kinds: operational (continuous, kept in the storage account, for quick undo) and vaulted (copies in the vault, safe even if the account is deleted).",
                "On-premises: the MARS agent backs up files and folders straight to Azure, which is the hybrid answer.",
                "Choose the vault's storage redundancy (LRS, ZRS or GRS); GRS with Cross Region Restore lets you restore in the paired region.",
                "Soft delete keeps deleted backups for 14 days, so an attacker cannot wipe them at once.",
            ),
            B.check(
                "sa-backup-dr-k1", "sa-backup-dr",
                "Two Tracks must back up its Azure blob containers so they survive even if the storage account is deleted. What do you recommend?",
                listOf("Vaulted blob backup in a Backup vault", "Operational blob backup only", "A Recovery Services vault with the MARS agent"), 0,
                "Vaulted backup copies the blobs into the vault, outside the account, so deleting the account does not delete them.",
                whyWrong = mapOf(
                    1 to "Operational backup keeps its data inside the source account, so it goes with the account.",
                    2 to "MARS backs up files from Windows machines, mostly on premises, not blob containers.",
                ),
            ),
            B.h("Databases: built-in backups"),
            B.p("""
                Azure SQL Database backs itself up with no set-up: full, differential and transaction log backups,
                the log every 5 to 10 minutes. PITR, point-in-time restore, rebuilds the database as it was at any
                moment in the retention window, 1 to 35 days (7 by default). It always restores as a new database.
                Long-term retention (LTR) keeps weekly, monthly or yearly full backups for up to 10 years. Geo-restore
                builds a new database in any region from geo-redundant backups; it is cheap, but data loss and
                downtime run to minutes or hours.
            """),
            B.h("Site Recovery: a spare site for whole machines"),
            B.p("""
                Azure Site Recovery keeps a running copy of your machines somewhere else by replicating their disks all
                the time. It covers Azure VMs between regions, and on-premises VMware, Hyper-V and physical servers to
                Azure. When disaster strikes you fail over: Azure builds the VMs from the copy. Later you fail back.
                A test failover builds the copies in an isolated network while replication carries on, so you can
                prove your RTO without touching production. A recovery plan sets the order: database first, then the API.
            """),
            B.diagram(
                caption = "Site Recovery replicates machines to a second site and is managed from a Recovery Services vault; blobs and disks back up to a Backup vault.",
                nodes = listOf(
                    DNode("vm", "Office VMs (on-premises)", 0.18f, 0.18f, w = 0.32f, h = 0.2f, tone = 2),
                    DNode("asr", "Site Recovery", 0.38f, 0.5f, w = 0.3f, h = 0.18f, tone = 1, sub = "replicate, fail over"),
                    DNode("azure", "Azure, second region", 0.82f, 0.18f, w = 0.32f, h = 0.2f),
                    DNode("blob", "Blobs and disks", 0.8f, 0.5f, w = 0.3f, h = 0.18f, tone = 2),
                    DNode("rsv", "Recovery Services vault", 0.25f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER),
                    DNode("bv", "Backup vault", 0.75f, 0.85f, w = 0.4f, h = 0.2f, kind = NodeKind.CYLINDER),
                ),
                edges = listOf(
                    DEdge("vm", "asr", label = "replicate"),
                    DEdge("asr", "azure", label = "fail over"),
                    DEdge("asr", "rsv", label = "managed in", dashed = true),
                    DEdge("blob", "bv", label = "backup"),
                ),
                aspect = 0.8f,
            ),
            B.check(
                "sa-backup-dr-k2", "sa-backup-dr",
                "The DR plan for the office VMs must be proven every quarter without any downtime for users. What do you run?",
                listOf("A test failover into an isolated network", "A planned failover and a failback", "A restore from the latest backup"), 0,
                "Test failover builds the VMs from the replica in a separate network while production and replication carry on.",
                whyWrong = mapOf(
                    1 to "A planned failover really moves production, so users see the switch.",
                    2 to "A restore tests backup, not the Site Recovery RTO, and is far slower.",
                ),
            ),
            B.table(
                listOf("Option", "Data loss (RPO)", "Downtime (RTO)", "Cost"),
                listOf("Backup, restore in place", "Hours (since last backup)", "Hours", "Low"),
                listOf("Geo-restore of SQL", "Minutes to hours", "Minutes to hours", "Lowest"),
                listOf("Site Recovery for VMs", "Seconds to minutes", "Minutes", "Medium"),
                listOf("SQL failover group", "Seconds", "Under a minute or so", "Higher: a second database"),
            ),
            B.check(
                "sa-backup-dr-k3", "sa-backup-dr",
                "An auditor needs a monthly copy of the orders database kept for 7 years. What do you configure?",
                listOf("A long-term retention policy", "PITR retention of 35 days", "Active geo-replication"), 0,
                "LTR keeps weekly, monthly or yearly full backups for up to 10 years; PITR stops at 35 days.",
                whyWrong = mapOf(
                    1 to "35 days is the ceiling for point-in-time restore, far short of 7 years.",
                    2 to "A replica is a live copy for outages; it keeps no history at all.",
                ),
            ),
            B.worked("""
                Worked example. Requirement: the office file server must survive a region outage with an RPO of
                minutes and an RTO of one hour; deleted files must be restorable for 30 days. Options: nightly
                backup only, geo-restore, or Site Recovery plus backup. Choice: Site Recovery replicates the VM to
                North Europe, which meets the minutes and the hour, and Azure Backup with a 30-day policy covers
                deleted files. Why: replication alone would replicate the deletion too.
            """),
            B.faded(
                "sa-backup-dr-f1",
                "Same problem for the office booking system: RPO 15 minutes, RTO 2 hours, must survive a region outage, and mistakes must be undoable for 14 days.",
                listOf(
                    "Write down the targets: 15 minutes of data, 2 hours of downtime, a whole region lost.",
                    "A nightly backup loses up to a day, so replicate the VM with Azure Site ___ to a second region.",
                    "Add an Azure Backup policy with 14 days of retention, because replication copies mistakes too.",
                    "Run a test failover each quarter to prove the 2-hour RTO.",
                ),
                blank = 1,
                answers = listOf("recovery", "recovery service"),
                note = "Only replication gets the data loss down to minutes; a backup is only as fresh as its last run.",
            ),
            B.steps("Do it for real",
                "az backup vault create --name tt-rsv --resource-group tt-rg --location westeurope" to "A Recovery Services vault.",
                "az backup vault backup-properties set --name tt-rsv --resource-group tt-rg --backup-storage-redundancy GeoRedundant --cross-region-restore-flag true" to "Backups copied to the paired region and restorable there. Set this before protecting anything.",
                "az backup protection enable-for-vm --resource-group tt-rg --vault-name tt-rsv --vm tt-office-vm --policy-name DefaultPolicy" to "The VM is protected by the default daily policy.",
                "az sql db str-policy set --resource-group tt-rg --server tt-sql --name ttdb --retention-days 35" to "Point-in-time restore now reaches back 35 days.",
                "az sql db ltr-policy set --resource-group tt-rg --server tt-sql --name ttdb --monthly-retention P12M --yearly-retention P7Y --week-of-year 1" to "Monthly copies for a year, yearly copies for seven.",
            ),
            B.mistakes(
                "Treating replication as backup; a deleted table is deleted in the replica seconds later.",
                "Promising an RPO of minutes with a nightly backup, which can lose a whole day of work.",
                "Stretching PITR to cover years of compliance copies; it stops at 35 days, LTR goes to 10 years.",
                "Never running a test failover, so the first real failover is also the first time anyone tries it.",
                "Putting blob backup in a Recovery Services vault; blobs, disks and PostgreSQL use a Backup vault.",
            ),
            B.example("Two Tracks goes global: Site Recovery copies the office VMs to Azure, Azure Backup keeps 30 days of VM and file share points, the orders database has 35-day PITR plus 7-year LTR for GDPR audits, and a test failover runs every quarter."),
            B.h("Check yourself"),
            B.match(
                "sa-x-backup-match", "Pair each requirement with what it points to.",
                "Undo a deleted row from last Tuesday" to "Point-in-time restore",
                "Keep yearly copies for 10 years" to "Long-term retention",
                "Office VMs back up in another region within minutes" to "Azure Site Recovery",
                "Back up blob containers" to "Backup vault",
            ),
            B.gap(
                "sa-x-backup-gap1", "The time you may be down after a disaster.",
                "That number is the ___, the recovery time objective.", "RTO", "rto",
            ),
            B.gap(
                "sa-x-backup-gap2", "A drill that leaves production alone.",
                "A ___ failover builds the copies in an isolated network.", "test", "Test",
            ),
            B.tryIt("Try it in 15 minutes", "Uses a small test VM; delete the resource group afterwards so the vault stops charging.",
                "Create a Recovery Services vault and set its redundancy before protecting anything",
                "Enable backup on a small VM with the default policy and trigger a backup now",
                "Open the policy and note how often it runs and how long it keeps points",
                "In the Site Recovery blade of the same vault, read the steps to replicate a VM to another region",
            ),
            B.explain(
                "sa-backup-dr-x1",
                "Explain how you would protect the Two Tracks office VM against both mistakes and a region outage, using RPO and RTO.",
                "First I write down the RPO, how much data we can lose, and the RTO, how long we can be down. A region outage with an RPO of minutes needs Azure Site Recovery to replicate the VM to a second region, with a test failover to prove the RTO. Replication copies mistakes, so I also add Azure Backup with a policy that keeps daily restore points in a Recovery Services vault.",
                "RPO and RTO set the design" to listOf("rpo", "rto", "recovery point", "recovery time"),
                "Site Recovery replicates to another region" to listOf("site recovery", "replicate", "replication", "failover"),
                "Backup still needed for mistakes" to listOf("azure backup", "backup policy", "restore point", "vault"),
            ),
            B.glossary(
                "RPO" to "Recovery point objective: the most recent data you can afford to lose.",
                "RTO" to "Recovery time objective: how long the app may be down before it runs again.",
                "Recovery Services vault" to "Backup store for VMs, Files, SQL in VMs and on-premises; also hosts Site Recovery.",
                "Backup vault" to "Newer backup store for blobs, disks, PostgreSQL and AKS.",
                "Point-in-time restore" to "Rebuild a database as it was at a chosen moment, within 1 to 35 days.",
                "Long-term retention" to "Weekly, monthly or yearly database backups kept for up to 10 years.",
                "Test failover" to "A DR drill that builds the replicas in an isolated network.",
            ),
            B.recap(
                "RPO is the data you may lose, RTO the time you may be down; they decide backup or replication.",
                "Backup undoes mistakes (PITR to 35 days, LTR to 10 years); Site Recovery and geo-replication survive regions.",
                "Recovery Services vault for VMs, Files and on-premises; Backup vault for blobs, disks and PostgreSQL.",
            ),
        ),
    )

    private val highAvailability = Topic(
        id = "sa-high-availability", title = "High availability: zones, regions and replicas", minutes = 30,
        references = listOf(
            B.ref("What are availability zones?", LEARN + "azure/reliability/availability-zones-overview"),
            B.ref("Availability options for Azure Virtual Machines", LEARN + "azure/virtual-machines/availability"),
            B.ref("Azure region pairs and nonpaired regions", LEARN + "azure/reliability/regions-paired"),
            B.ref("Availability through local and zone redundancy in Azure SQL Database", LEARN + "azure/azure-sql/database/high-availability-sla-local-zone-redundancy"),
            B.ref("Failover groups overview for Azure SQL Database", LEARN + "azure/azure-sql/database/failover-group-sql-db"),
            B.ref("Consistency levels in Azure Cosmos DB", LEARN + "azure/cosmos-db/consistency-levels"),
            B.ref("Azure Storage redundancy", LEARN + "azure/storage/common/storage-redundancy"),
            B.ref("Learn module: Design for high availability", LEARN + "training/modules/design-solution-for-high-availability/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-high-availability-w1", "sa-high-availability",
                "Two VMs in an availability set, or two VMs in two availability zones: which survives a whole datacenter fire?",
                listOf("The two zones", "The availability set", "Both equally"), 0,
                "One of them spreads VMs over racks; the other over buildings.",
            ),
            B.plain("""
                High availability, HA for short, means the app keeps running when a part breaks, with no one having
                to step in. The trick is always the same: more than one copy, kept in places that do not fail
                together. Like keeping your house keys in two pockets, then in two bags, then in two houses: each step
                costs more and survives a bigger accident.
            """),
            B.diagram(
                caption = "One load balancer spreads users over three zones in a region; a geo-replica in a second region waits for a regional outage.",
                nodes = listOf(
                    DNode("users", "Users", 0.5f, 0.1f, w = 0.16f, h = 0.14f, kind = NodeKind.USER),
                    DNode("lb", "Zone-redundant load balancer", 0.5f, 0.32f, w = 0.6f, h = 0.13f, tone = 1),
                    DNode("z1", "Zone 1", 0.17f, 0.55f, w = 0.26f, h = 0.16f, sub = "VM + SQL replica"),
                    DNode("z2", "Zone 2", 0.5f, 0.55f, w = 0.26f, h = 0.16f, sub = "VM + SQL replica"),
                    DNode("z3", "Zone 3", 0.83f, 0.55f, w = 0.26f, h = 0.16f, sub = "VM + SQL replica"),
                    DNode("r2", "Second region (North Europe)", 0.5f, 0.85f, w = 0.7f, h = 0.16f, tone = 2, sub = "geo-replica, standby"),
                ),
                edges = listOf(
                    DEdge("users", "lb"),
                    DEdge("lb", "z1"),
                    DEdge("lb", "z2"),
                    DEdge("lb", "z3"),
                    DEdge("z2", "r2", label = "async geo-replication", dashed = true),
                ),
                aspect = 0.9f,
            ),
            B.h("Racks, buildings, regions"),
            B.table(
                listOf("Option", "Copies sit in", "Survives", "VM SLA (2+ VMs)"),
                listOf("Availability set", "Different racks in one datacenter", "A rack or host failing, planned maintenance", "99.95%"),
                listOf("Availability zones", "Separate datacenters in one region", "A whole datacenter going down", "99.99%"),
                listOf("Two regions", "Different parts of the world", "A whole region going down", "Your own design"),
            ),
            B.bullets(
                "An availability set spreads VMs over fault domains (racks with their own power and network) and update domains (groups patched one at a time).",
                "An availability zone is one or more datacenters with their own power, cooling and network; a zone region has at least three.",
                "Microsoft now recommends zones over availability sets wherever the region has them.",
                "A region pair is two regions Azure links for geo-redundant storage and staggered updates; some newer regions have no pair.",
                "A single VM on Premium SSD has a 99.9% SLA: no redundancy at all, so a tempting trap.",
            ),
            B.h("SLAs and the composite SLA"),
            B.p("""
                An SLA, service level agreement, is Microsoft's uptime promise for one service, such as 99.99%. When a
                request passes through several services in a row, all must be up, so you multiply. App Service at
                99.95% in front of SQL Database at 99.99% gives 0.9995 x 0.9999, about 99.94%, lower than either.
                Running two copies side by side does the opposite: both must fail together, which lifts the figure.
            """),
            B.check(
                "sa-high-availability-k1", "sa-high-availability",
                "The API runs on App Service (99.95%) and calls SQL Database (99.99%). What is the composite SLA?",
                listOf("About 99.94%", "99.99%", "About 99.97%"), 0,
                "A chain multiplies: 0.9995 x 0.9999 is about 0.9994, so the whole is less available than its weakest part.",
                whyWrong = mapOf(
                    1 to "The chain cannot beat its weakest link, and the weakest here is 99.95%.",
                    2 to "Averaging is not how it works; both must be up at once, so you multiply.",
                ),
            ),
            B.h("Compute that survives a zone"),
            B.p("""
                A virtual machine scale set runs many identical VMs as one group and can add or remove them as load
                changes. Spread it over zones 1, 2 and 3 and put a zone-redundant Standard Load Balancer or
                Application Gateway v2 in front: if a zone dies, the others carry on. For two regions, Azure Front Door
                (web traffic) or Traffic Manager (DNS) sends users to the region that is healthy. App Service, Container
                Apps and AKS can be zone-redundant too.
            """),
            B.h("Relational data"),
            B.p("""
                Azure SQL Database already keeps spare copies inside a region. Turn on zone redundancy and the copies
                sit in different zones; for Business Critical and Premium the SLA rises to 99.995%, and Business
                Critical also gives a free readable replica. For a region failing, you add a copy in another region.
            """),
            B.compare("Region copies for Azure SQL",
                B.card("Active geo-replication", "One database, extra readable copies, you fail over by hand.", "Up to four secondaries, even in the same region", "Connection string changes after failover", "SQL Database only"),
                B.card("Failover group", "Several databases fail over together and the app keeps one connection string.", "Read-write and read-only listener names stay the same", "One secondary server in another region", "Also works for SQL Managed Instance"),
            ),
            B.check(
                "sa-high-availability-k2", "sa-high-availability",
                "Five databases must move to North Europe together in an outage, and the API must not change its connection string. What do you use?",
                listOf("A failover group", "Active geo-replication on each database", "Geo-restore"), 0,
                "A failover group moves many databases as one and keeps the same listener names.",
                whyWrong = mapOf(
                    1 to "Geo-replication fails over one database at a time and the endpoint changes.",
                    2 to "Geo-restore builds new databases from backups: slow, and new names to point at.",
                ),
            ),
            B.h("Semi-structured and unstructured data"),
            B.p("""
                Azure Cosmos DB can copy data to as many regions as you add. With multi-region writes every region
                accepts writes and the SLA reaches 99.999%. The price is consistency, how fresh a read is promised to
                be. Strong means every read sees the latest write, but it cannot be used with multi-region writes.
                Session, the default, means a user always sees their own writes. Eventual is fastest and loosest.
            """),
            B.table(
                listOf("Storage redundancy", "Copies", "Survives"),
                listOf("LRS", "Three in one datacenter", "A disk or rack failing"),
                listOf("ZRS", "Three across zones", "A zone failing, with no data loss"),
                listOf("GRS / RA-GRS", "LRS here plus LRS in the paired region", "A region; RA- lets you read the copy"),
                listOf("GZRS / RA-GZRS", "ZRS here plus LRS in the paired region", "A zone and a region; the strongest"),
            ),
            B.tip("\"Read from the secondary region during an outage\" points to RA-GRS or RA-GZRS. \"Survive a zone with no data loss\" points to ZRS. Geo copies are asynchronous, so a regional failover can lose a little data."),
            B.check(
                "sa-high-availability-k3", "sa-high-availability",
                "Users in Europe and Australia must both write to the progress store with the lowest latency, and the SLA must be the highest Cosmos DB offers. What do you configure?",
                listOf("Multi-region writes with session consistency", "One write region with strong consistency", "Multi-region writes with strong consistency"), 0,
                "Multi-region writes let each user write nearby and give 99.999%; session keeps each user's own writes visible to them.",
                whyWrong = mapOf(
                    1 to "One write region makes Australian writes cross the world and gives a lower write SLA.",
                    2 to "Strong consistency cannot be combined with multi-region writes.",
                ),
            ),
            B.worked("""
                Worked example. Requirement: the Two Tracks API must stay up if a datacenter fails, with 99.99% for
                the VMs, and the database must lose no data in that case. Options: an availability set, a single big
                VM, or a scale set across zones. Choice: a Flexible scale set over zones 1, 2 and 3 behind a
                zone-redundant Standard Load Balancer, and SQL Database with zone redundancy. Why: only zones survive
                a datacenter; an availability set stays inside one building.
            """),
            B.faded(
                "sa-high-availability-f1",
                "Same problem for the Coach worker VMs: they must keep running if one datacenter in West Europe burns down.",
                listOf(
                    "Note the failure to survive: one datacenter, not a whole region.",
                    "Spread the VMs over three availability ___ in West Europe with a scale set.",
                    "Put a zone-redundant load balancer in front so traffic skips the dead zone.",
                ),
                blank = 1,
                answers = listOf("zones", "zone"),
                note = "Zones are separate datacenters; an availability set only spreads over racks in one of them.",
            ),
            B.steps("Do it for real",
                "az vmss create --resource-group tt-rg --name tt-api-vmss --image Ubuntu2204 --orchestration-mode Flexible --zones 1 2 3 --instance-count 3 --admin-username azureuser --generate-ssh-keys" to "Three VMs, one per zone, behind a new load balancer.",
                "az sql db update --resource-group tt-rg --server tt-sql --name ttdb --zone-redundant true" to "The database's copies now sit in different zones.",
                "az sql failover-group create --name tt-fog --resource-group tt-rg --server tt-sql --partner-server tt-sql-ne --add-db ttdb --failover-policy Manual" to "A secondary in North Europe and one listener name the API keeps using.",
                "az storage account create --name ttmedia --resource-group tt-rg --location westeurope --sku Standard_RAGZRS" to "Blobs across three zones, copied to the paired region, readable there.",
            ),
            B.mistakes(
                "Choosing an availability set to survive a datacenter outage; it only spreads over racks in one building.",
                "Adding up or averaging SLAs; services in a chain multiply, so the whole is lower than each part.",
                "Asking for strong consistency with Cosmos DB multi-region writes, which cannot be combined.",
                "Using active geo-replication when several databases must fail over together with one connection string.",
                "Expecting GRS to let you read the secondary copy before a failover; that needs RA-GRS or RA-GZRS.",
            ),
            B.example("Two Tracks goes global: a Flexible scale set over three West Europe zones, zone-redundant SQL Database in a failover group with North Europe, Cosmos DB with multi-region writes in West Europe and Australia East, and RA-GZRS for media."),
            B.h("Check yourself"),
            B.match(
                "sa-x-ha-match", "Pair each requirement with what it points to.",
                "Survive a rack failure, cheapest" to "Availability set",
                "Survive a datacenter outage" to "Availability zones",
                "Many databases fail over with one endpoint" to "Failover group",
                "Write in Europe and Australia, 99.999%" to "Cosmos DB multi-region writes",
            ),
            B.gap(
                "sa-x-ha-gap1", "Services in a chain.",
                "To get a composite SLA you ___ the SLAs together.", "multiply", "Multiply",
            ),
            B.gap(
                "sa-x-ha-gap2", "Storage across zones in one region.",
                "Zone-redundant storage is called ___.", "ZRS", "zrs",
            ),
            B.tryIt("Try it in 15 minutes", "A few cents of compute; delete the resource group afterwards.",
                "Create a Flexible scale set across zones 1, 2 and 3 and check each VM's zone in the portal",
                "Look up the SLA for App Service and SQL Database and work out their composite SLA",
                "Open a storage account's Redundancy page and see which options your region offers",
                "Read the failover group settings of a SQL server and note the two listener names",
            ),
            B.explain(
                "sa-high-availability-x1",
                "Explain how Two Tracks stays available when a datacenter fails and when a whole region fails.",
                "For a datacenter failure the API runs in a scale set across three availability zones behind a zone-redundant load balancer, and SQL Database uses zone redundancy. For a region failure the databases sit in a failover group with North Europe, so the listener name stays the same. Cosmos DB uses multi-region writes with session consistency, and media uses RA-GZRS storage.",
                "Zones for a datacenter failure" to listOf("availability zone", "zone-redundant", "zone redundancy", "three zones"),
                "A second region for a regional failure" to listOf("failover group", "geo-replication", "second region", "north europe", "paired region"),
                "Data services chosen for HA" to listOf("cosmos", "multi-region", "gzrs", "grs", "zrs"),
            ),
            B.glossary(
                "Availability set" to "VMs spread over racks (fault domains) and patch groups (update domains) in one datacenter.",
                "Availability zone" to "A separate datacenter group in a region with its own power, cooling and network.",
                "Composite SLA" to "The uptime of services in a chain: their SLAs multiplied together.",
                "Failover group" to "Databases that fail over to another region together, keeping one listener name.",
                "Active geo-replication" to "A readable copy of one SQL database in another region, failed over by hand.",
                "Multi-region writes" to "Cosmos DB accepting writes in every region; 99.999% SLA, no strong consistency.",
                "GZRS" to "Storage copied across three zones and also to the paired region.",
            ),
            B.recap(
                "Sets survive a rack, zones a datacenter, a second region a whole region; each costs more.",
                "Chained SLAs multiply; redundant copies side by side raise availability.",
                "SQL: zone redundancy, then failover groups; Cosmos: multi-region writes; storage: ZRS, GZRS and RA- to read.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(backupDr, highAvailability)

    private const val BDR = "sa-backup-dr"
    private const val HA = "sa-high-availability"

    val quiz: Quiz = Quiz(
        id = "sa-continuity-test", title = "Business continuity module test",
        questions = B.spread(listOf(
            B.q("sa-b1", BDR,
                "What does a recovery point objective (RPO) of 15 minutes mean?",
                listOf("At most 15 minutes of recent data may be lost", "The app must be running again within 15 minutes", "Backups must be kept for 15 days"),
                setOf(0), "RPO is measured backwards from the disaster: how much recent data you can afford to lose.",
                whyWrong = mapOf(1 to "That is the RTO, the time until the app runs again.", 2 to "Retention is a backup policy setting, not an objective for a disaster."),
            ),
            B.q("sa-b2", BDR,
                "Which requirement can a nightly backup NOT meet on its own?",
                listOf("Lose no more than 10 minutes of data in a region outage", "Restore a file deleted last week", "Keep monthly copies for a year"),
                setOf(0), "A nightly backup can be up to a day old, so the data loss can be up to a day. Minutes need replication.",
                whyWrong = mapOf(1 to "Restoring last week's file is exactly what backup is for.", 2 to "Monthly retention is a normal backup policy setting."),
            ),
            B.q("sa-b3", BDR,
                "Which vault do you create to back up Azure Blob storage with vaulted backup?",
                listOf("A Backup vault", "A Recovery Services vault", "A Key Vault"),
                setOf(0), "Blobs, managed disks, PostgreSQL and AKS back up to the newer Backup vault.",
                whyWrong = mapOf(1 to "Recovery Services vaults cover VMs, Azure Files, SQL in VMs and on-premises, not blobs.", 2 to "Key Vault stores secrets and keys, not backups."),
            ),
            B.q("sa-b4", BDR,
                "The office file server is on premises. You need its folders backed up to Azure with the least infrastructure. What do you recommend?",
                listOf("The MARS agent with a Recovery Services vault", "Azure Site Recovery", "A Backup vault with operational backup"),
                setOf(0), "The MARS agent runs on the Windows server and sends files and folders straight to a Recovery Services vault.",
                whyWrong = mapOf(1 to "Site Recovery replicates whole machines for DR; it is not a file backup with history.", 2 to "Backup vaults hold newer Azure workloads such as blobs, not on-premises folders."),
            ),
            B.q("sa-b5", BDR,
                "A developer ran a bad UPDATE on the orders database two hours ago. What is the quickest way to get the old data back?",
                listOf("Point-in-time restore to just before the UPDATE", "Fail over the failover group", "Geo-restore to another region"),
                setOf(0), "PITR rebuilds the database as a new database at a chosen moment inside the retention window.",
                whyWrong = mapOf(1 to "The replica received the bad UPDATE too, seconds later.", 2 to "Geo-restore is for a lost region and uses older geo-copied backups."),
            ),
            B.q("sa-b6", BDR,
                "What is the longest retention you can set for point-in-time restore on Azure SQL Database?",
                listOf("35 days", "7 days", "10 years"),
                setOf(0), "PITR retention runs from 1 to 35 days, 7 by default. Anything longer is long-term retention.",
                whyWrong = mapOf(1 to "7 days is the default, not the maximum.", 2 to "10 years is the limit for long-term retention, a separate feature."),
            ),
            B.q("sa-b7", BDR,
                "The cheapest way to recover Azure SQL Database from a region outage, when hours of downtime are acceptable, is:",
                listOf("Geo-restore from geo-redundant backups", "A failover group", "Active geo-replication with four secondaries"),
                setOf(0), "Geo-restore needs no standing replica, so it costs least, but RPO and RTO are minutes to hours.",
                whyWrong = mapOf(1 to "A failover group pays for a running secondary database, which buys seconds, not hours.", 2 to "Four replicas are the most expensive choice of all."),
            ),
            B.q("sa-b8", BDR,
                "On-premises VMware VMs must fail over to Azure within an hour, losing only minutes of data. What do you recommend?",
                listOf("Azure Site Recovery", "Azure Backup with a daily policy", "Azure Migrate"),
                setOf(0), "Site Recovery continuously replicates VMware, Hyper-V and physical servers to Azure and fails them over.",
                whyWrong = mapOf(1 to "A daily backup could lose a day of data and restores slowly.", 2 to "Azure Migrate moves servers once; it is not an ongoing DR plan."),
            ),
            B.q("sa-b9", BDR,
                "A three-tier app on Azure VMs must fail over in order: database, then API, then web. What in Site Recovery does this?",
                listOf("A recovery plan", "A backup policy", "A test failover"),
                setOf(0), "A recovery plan groups machines and sets the order of failover, with optional scripts between groups.",
                whyWrong = mapOf(1 to "A backup policy sets schedules and retention for backups, not failover order.", 2 to "A test failover is a drill; the order still comes from the recovery plan."),
            ),
            B.q("sa-b10", BDR,
                "Backups must be restorable in the paired region at any time, not only after Microsoft declares an outage. What do you enable on the vault?",
                listOf("Geo-redundant storage with Cross Region Restore", "Locally redundant storage", "Zone-redundant storage"),
                setOf(0), "Cross Region Restore on a GRS vault lets you restore from the secondary region whenever you choose, for drills too.",
                whyWrong = mapOf(1 to "LRS keeps all copies in one datacenter in the primary region.", 2 to "ZRS spreads copies across zones of one region, not to another region."),
            ),
            B.q("sa-b11", HA,
                "Which option protects VMs against the failure of a whole datacenter in a region?",
                listOf("Availability zones", "An availability set", "A single VM on Premium SSD"),
                setOf(0), "Each zone is a separate datacenter group with its own power, cooling and network.",
                whyWrong = mapOf(1 to "An availability set spreads VMs over racks inside one datacenter.", 2 to "One VM has no second copy at all."),
            ),
            B.q("sa-b12", HA,
                "What VM SLA does Microsoft offer for two or more VMs spread across availability zones?",
                listOf("99.99%", "99.95%", "99.9%"),
                setOf(0), "Zones give 99.99%; an availability set gives 99.95%; a single Premium SSD VM gives 99.9%.",
                whyWrong = mapOf(1 to "99.95% is the availability set figure.", 2 to "99.9% is for a single VM on Premium SSD."),
            ),
            B.q("sa-b13", HA,
                "A web tier with a 99.95% SLA calls a database with a 99.99% SLA. What is the composite SLA?",
                listOf("About 99.94%", "99.99%", "99.95%"),
                setOf(0), "Services in a chain multiply: 0.9995 x 0.9999 is about 0.9994.",
                whyWrong = mapOf(1 to "The chain is weaker than its weakest part, so it cannot reach 99.99%.", 2 to "Both must be up at once, so the result falls below the lower SLA."),
            ),
            B.q("sa-b14", HA,
                "What does an availability set's fault domain protect against?",
                listOf("A rack losing power or network", "A whole region going down", "A bad deployment of your code"),
                setOf(0), "Fault domains are racks with their own power and network switch; VMs in different fault domains do not fail together.",
                whyWrong = mapOf(1 to "A set lives in one datacenter, so a region outage takes all of it.", 2 to "No infrastructure spread stops your own bug; that needs safe deployment."),
            ),
            B.q("sa-b15", HA,
                "Which Azure SQL Database configuration gives the 99.995% SLA?",
                listOf("Business Critical with zone redundancy", "General Purpose without zone redundancy", "Basic with geo-restore"),
                setOf(0), "Business Critical or Premium with zone redundancy carries the highest SQL Database SLA, 99.995%.",
                whyWrong = mapOf(1 to "Without zone redundancy the copies are not spread across zones.", 2 to "Geo-restore is a slow DR option and adds nothing to the SLA."),
            ),
            B.q("sa-b16", HA,
                "Which statement about active geo-replication and failover groups for Azure SQL Database is true?",
                listOf("Only failover groups keep the same connection endpoint after failover", "Only active geo-replication can fail over several databases together", "Both work for SQL Managed Instance"),
                setOf(0), "Failover groups give read-write and read-only listeners whose names stay the same after failover.",
                whyWrong = mapOf(1 to "It is the other way round: failover groups move many databases together.", 2 to "SQL Managed Instance supports failover groups, not active geo-replication."),
            ),
            B.q("sa-b17", HA,
                "A reporting job needs a readable copy of the orders database in the same region as the primary. What can do this?",
                listOf("Active geo-replication", "A failover group", "Geo-restore"),
                setOf(0), "Active geo-replication allows readable secondaries, even in the same region; a failover group's partner must be in another region.",
                whyWrong = mapOf(1 to "A failover group's secondary server sits in a different region.", 2 to "Geo-restore makes a one-off new database, not a kept-in-sync copy."),
            ),
            B.q("sa-b18", HA,
                "Cosmos DB uses multi-region writes. Which consistency level can you NOT choose?",
                listOf("Strong", "Session", "Eventual"),
                setOf(0), "Strong consistency needs one place to agree every write, so it cannot be paired with multi-region writes.",
                whyWrong = mapOf(1 to "Session is the default and works with multi-region writes.", 2 to "Eventual is the loosest level and works with multi-region writes."),
            ),
            B.q("sa-b19", HA,
                "Media blobs must survive a zone failure and a region failure, and be readable in the secondary region during an outage. Which redundancy?",
                listOf("RA-GZRS", "ZRS", "GRS"),
                setOf(0), "GZRS spreads copies over zones and to the paired region; the RA- prefix lets you read the secondary copy.",
                whyWrong = mapOf(1 to "ZRS stays in one region, so a regional outage takes it down.", 2 to "GRS has no zone spread and no read access to the secondary before failover."),
            ),
            B.q("sa-b20", HA,
                "The API runs on VMs and must keep serving if one zone fails, scaling out under load. What do you recommend?",
                listOf("A scale set across three zones behind a zone-redundant load balancer", "Two VMs in an availability set", "One large VM with Premium SSD"),
                setOf(0), "A scale set adds and removes VMs, spread over zones; the zone-redundant load balancer skips a dead zone.",
                whyWrong = mapOf(1 to "An availability set does not survive a zone and does not scale out on its own.", 2 to "One VM is a single point of failure."),
            ),
        )),
    )
}
