package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Infra2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val migrations = Topic(
        id = "sa-migrations", title = "Migrations: moving to Azure with a plan", minutes = 30,
        references = listOf(
            B.ref("Cloud Adoption Framework for Azure", LEARN + "azure/cloud-adoption-framework/overview"),
            B.ref("Select your cloud migration strategies", LEARN + "azure/cloud-adoption-framework/plan/select-cloud-migration-strategy"),
            B.ref("About Azure Migrate", LEARN + "azure/migrate/migrate-services-overview"),
            B.ref("What is Azure Database Migration Service?", LEARN + "azure/dms/dms-overview"),
            B.ref("Get started with AzCopy", LEARN + "azure/storage/common/storage-use-azcopy-v10"),
            B.ref("Azure Data Box overview", LEARN + "azure/databox/data-box-overview"),
            B.ref("Introduction to Azure Storage Mover", LEARN + "azure/storage-mover/service-overview"),
            B.ref("Learn module: Design migrations", LEARN + "training/modules/design-migrations/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-migrations-w1", "sa-migrations",
                "Two Tracks wants its office server running in Azure next month, with no code changes at all. Which approach do you think fits?",
                listOf("Rehost it on an Azure virtual machine", "Rebuild it as a new cloud app", "Replace it with a bought service"), 0,
                "Moving fast with no code changes is the plainest kind of move there is.",
            ),
            B.plain("""
                A migration is moving apps and data from your own server room into Azure. Think of moving house: first
                you list everything you own, then you decide what each item becomes (move it as it is, repair it, or
                buy a new one), then you pack, move and unpack. Azure gives you a planning guide for the whole move and
                a tool for each kind of box: servers, databases and files.
            """),
            B.diagram(
                caption = "Azure Migrate takes stock of the office, an assessment picks a strategy per app, and each app lands on IaaS, PaaS or storage.",
                nodes = listOf(
                    DNode("office", "On-premises office", 0.15f, 0.2f, w = 0.26f, h = 0.2f, tone = 2),
                    DNode("migrate", "Azure Migrate", 0.5f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "discover and assess"),
                    DNode("decide", "Pick a strategy per app", 0.85f, 0.2f, w = 0.26f, h = 0.2f, kind = NodeKind.NOTE),
                    DNode("iaas", "Azure VMs", 0.15f, 0.75f, w = 0.26f, h = 0.2f, sub = "rehost"),
                    DNode("paas", "App Service, Azure SQL", 0.5f, 0.75f, w = 0.4f, h = 0.2f, sub = "replatform"),
                    DNode("files", "Blobs and Azure Files", 0.85f, 0.75f, w = 0.26f, h = 0.2f, kind = NodeKind.CYLINDER),
                ),
                edges = listOf(
                    DEdge("office", "migrate", label = "appliance"),
                    DEdge("migrate", "decide", label = "report"),
                    DEdge("decide", "iaas"), DEdge("decide", "paas"), DEdge("decide", "files"),
                ),
                aspect = 0.7f,
            ),
            B.h("The Cloud Adoption Framework, the light version"),
            B.p("""
                The Cloud Adoption Framework (CAF) is Microsoft's free guidebook for moving a whole company to Azure.
                It runs in phases, like chapters: Strategy (why are we moving?), Plan (which apps, in what order?),
                Ready (prepare the ground), and Adopt (move or build). Govern, Secure and Manage then run alongside
                for as long as you use Azure.
            """),
            B.p("""
                A landing zone is that prepared ground in Azure: subscriptions, networks, rules and sign-in set up
                before anything moves in, like fitting the locks and wiring before the furniture arrives.
            """),
            B.h("The Rs: what each app becomes"),
            B.table(
                listOf("Strategy", "What changes", "Pick it when you hear"),
                listOf("Rehost", "Nothing; the server moves to an Azure VM as it is", "Fast, no code changes, datacenter lease ending"),
                listOf("Replatform", "Small changes so it runs on a platform service (PaaS)", "Less server patching, minimal code changes"),
                listOf("Refactor", "The code is tidied up for the cloud, with no new features", "Old shortcuts in the code must be cleaned up"),
                listOf("Rearchitect", "The design changes, for example into smaller services", "Parts must scale separately"),
                listOf("Rebuild", "Written again from scratch for the cloud", "The old code is too outdated to keep"),
                listOf("Replace", "Swapped for a bought online product (SaaS)", "A ready-made product does the job"),
            ),
            B.remember("""
                The CAF lists eight Rs: these six plus retire (switch it off) and retain (leave it where it is).
                Older material used "refactor" for the small move onto PaaS that the CAF now calls replatform, so
                read the requirement words: "minimal code changes onto PaaS" is that move, whatever the label.
            """),
            B.compare("Landing on IaaS or PaaS?",
                B.card("IaaS: Azure virtual machines", "Speed matters most, or the app needs full control of the operating system.",
                    "You rent the machine and still patch it",
                    "Fewest changes, fastest move", "You keep the operating system work"),
                B.card("PaaS: App Service, Azure SQL", "You want Azure to run the servers and can accept small changes.",
                    "Azure runs and patches the machine for you",
                    "Built-in scaling, backups and high availability", "Some code or configuration changes first"),
            ),
            B.check(
                "sa-migrations-k1", "sa-migrations",
                "A web app must move to Azure so the team stops patching servers, with only minimal code changes. Which strategy?",
                listOf("Replatform onto App Service", "Rehost on an Azure VM", "Rebuild as microservices"), 0,
                "Small changes that let the app run on a platform service remove the server work. The CAF calls that replatform; older material says refactor.",
                whyWrong = mapOf(
                    1 to "A VM still has an operating system someone must patch, which is the work they want to stop.",
                    2 to "Rebuilding is a full rewrite, far more than minimal code changes.",
                ),
            ),
            B.h("Azure Migrate: take stock first"),
            B.p("""
                Azure Migrate is the free hub for the whole move. It starts with discovery, which means finding out
                what you have. You run the Azure Migrate appliance, a small virtual machine in your server room that
                acts like a stocktaker: it watches your servers and reports their size and load to Azure.
            """),
            B.bullets(
                "It finds servers, SQL Server databases and web apps.",
                "An assessment says whether each server is ready for Azure, what size it needs, and what it will cost a month.",
                "Dependency analysis shows which servers talk to which, so partners move together.",
                "A business case compares today's running costs with Azure's.",
                "Then it copies servers into Azure VMs with little downtime.",
            ),
            B.h("Databases: online or offline"),
            B.p("""
                Azure Database Migration Service (DMS) moves SQL Server databases into Azure. Offline is like moving
                house with the shop shut: the old database takes no writes for the whole copy. Online keeps the shop
                open while changes keep flowing to Azure. Downtime is then only the cutover, the short moment you
                switch the app across.
            """),
            B.table(
                listOf("Target", "In DMS", "Pick it when"),
                listOf("SQL Server on an Azure VM", "Online or offline", "You need the full SQL Server and its operating system"),
                listOf("Azure SQL Managed Instance", "Online or offline", "Near-full SQL Server features, such as SQL Agent jobs, with no server to patch"),
                listOf("Azure SQL Database", "Offline only (DMS moves the schema too)", "One modern database, fully managed; for minimal downtime use transactional replication instead of DMS"),
            ),
            B.tip("""
                "Minimal downtime" means an online migration. With DMS, online moves go only to SQL Managed Instance
                or SQL Server on a VM; DMS moves to Azure SQL Database offline only. If the target must be Azure SQL
                Database with minimal downtime, use transactional replication: Azure SQL Database is set up as a
                subscriber and receives every change while the old server keeps running, and the cutover is a quick
                switch of the app's connection string. Older guides show a wizard inside Azure Data Studio; that tool
                retired on 28 February 2026, and the same work now runs from DMS in the portal, PowerShell or the CLI,
                with assessment through Azure Arc.
            """),
            B.check(
                "sa-migrations-k2", "sa-migrations",
                "A SQL Server database with SQL Agent jobs must move to Azure with downtime of only a few minutes. Which design?",
                listOf("Online migration with DMS to SQL Managed Instance", "Offline migration with DMS to Azure SQL Database", "Copy the backup files with AzCopy"), 0,
                "Managed Instance keeps SQL Agent, and an online migration keeps the source working until a short cutover.",
                whyWrong = mapOf(
                    1 to "Offline means downtime for the whole copy, and Azure SQL Database has no SQL Agent.",
                    2 to "AzCopy moves files, not a live database; changes made during the copy would be lost.",
                ),
            ),
            B.h("Files and blobs: AzCopy, Data Box, Storage Mover"),
            B.compare("Moving unstructured data",
                B.card("AzCopy", "A modest amount of data and a decent network.",
                    "A free command-line tool that copies files into Blob storage or Azure Files",
                    "Scriptable, and sync copies only what changed"),
                B.card("Azure Data Box", "Tens of terabytes and a slow or busy network.",
                    "Microsoft posts you a rugged storage box (the newest hold 120 TB or 525 TB); you fill it and ship it back",
                    "Offline: the data travels by courier, like a removal van instead of the post"),
                B.card("Azure Storage Mover", "Many file shares to move and track, or catching up after a Data Box.",
                    "A managed service with an agent near your shares",
                    "Sends only new and changed files, with progress per share"),
            ),
            B.code("bash", """
                # Copy a folder into a blob container, then copy only what changed since
                azcopy copy "D:\recordings" "https://ttstore.blob.core.windows.net/recordings" --recursive
                azcopy sync "D:\recordings" "https://ttstore.blob.core.windows.net/recordings" --recursive
            """),
            B.check(
                "sa-migrations-k3", "sa-migrations",
                "100 TB of archive files must reach Azure within three weeks over a 100 Mbps line that the office also uses. What do you recommend?",
                listOf("Azure Data Box", "AzCopy over the internet", "Azure Database Migration Service"), 0,
                "At 100 Mbps, 100 TB would take about three months over the wire. Shipping a device skips the network.",
                whyWrong = mapOf(
                    1 to "AzCopy is right for the data, but this line is far too slow for this much of it.",
                    2 to "DMS moves databases, not files.",
                ),
            ),
            B.worked("""
                Worked example. Requirements: the office timetable app, its SQL Server database with SQL Agent jobs
                (scheduled tasks), and 60 TB of recordings must move within a month; database downtime under 30
                minutes; a team that hates patching; a 100 Mbps line. Choice: Azure Migrate assesses the app as
                ready for App Service, so replatform it there. The database moves online to SQL Managed Instance.
                The recordings would take about eight weeks over the line, so a Data Box carries the bulk and Storage
                Mover sends the changes. Why: each piece gets the least work that meets its requirement.
            """),
            B.faded(
                "sa-migrations-f1",
                "Two Tracks' Australian partner has 80 TB of videos, a 50 Mbps line, and a month to get them into Azure. People keep editing files while the move happens.",
                listOf(
                    "At 50 Mbps, 80 TB would take about five months over the network, so ship the bulk on an Azure Data Box.",
                    "While the device is in transit, keep the share in use; nothing is lost.",
                    "Copy the files changed since the device left with Azure Storage ___, which sends only the differences.",
                ),
                blank = 2,
                answers = listOf("mover"),
                note = "Data Box for the bulk when the line is too slow, then Storage Mover (or AzCopy sync) for the online catch-up.",
            ),
            B.steps("Do it for real",
                "In the portal, create an Azure Migrate project in your landing zone subscription" to "A project page with Servers, databases and web apps.",
                "Download the appliance and run it on a server next to the ones you want to assess" to "Discovered servers appear in the project within a few hours.",
                "Create an assessment with performance-based sizing" to "Readiness, a VM size and a monthly cost for each server.",
                "Turn on dependency analysis and group servers that talk to each other" to "Groups you can move together in one wave.",
                "Move a test folder with azcopy copy, then run azcopy sync" to "The second run copies only new or changed files.",
            ),
            B.mistakes(
                "Rehosting an app that already has problems; the problems move to Azure with it.",
                "Choosing an offline database migration when the requirement says minimal downtime.",
                "Pushing tens of terabytes over a slow line instead of ordering a Data Box.",
                "Migrating a server without its dependencies, so it cannot reach its partner after the move.",
            ),
            B.example("""
                Two Tracks goes global: Azure Migrate found 14 office servers. Two old report servers are retired,
                the booking app is replatformed onto App Service, its database moves online to SQL Managed Instance, the
                build server is rehosted on a VM, and the recordings arrive on a Data Box.
            """),
            B.h("Check yourself"),
            B.match(
                "sa-x-migr-match", "Pair each migration strategy with what it does.",
                "Rehost" to "Moves the server to an Azure VM unchanged",
                "Replatform" to "Small changes so the app runs on PaaS",
                "Rearchitect" to "Changes the design so parts scale separately",
                "Replace" to "Swaps the app for a bought SaaS product",
            ),
            B.gap(
                "sa-x-migr-gap1", "A database migration where the source keeps working until a short cutover.",
                "That is an ___ migration.", "online", "Online",
            ),
            B.tryIt("Try it in 15 minutes", "Free apart from a few cents of storage.",
                "Create an Azure Migrate project and look at the Servers, databases and web apps page",
                "Create a storage account and a blob container",
                "Copy a local folder into it with azcopy copy",
                "Change one file and run azcopy sync; check only that file is sent",
            ),
            B.explain(
                "sa-migrations-x1",
                "Explain how you would plan a move of an office app, its SQL Server database and a large file share to Azure.",
                "First run Azure Migrate with its appliance to discover the servers and assess readiness, size and cost, and use dependency analysis to move partners together. Then choose a strategy per app: rehost onto a VM when speed matters, or replatform onto App Service when the team wants no patching. Move the database with Database Migration Service, online for minimal downtime to SQL Managed Instance. Move the files with AzCopy on a fast line, or a Data Box when the line is too slow, then Storage Mover for the catch-up.",
                "Discover and assess with Azure Migrate" to listOf("azure migrate", "appliance", "assessment", "assess"),
                "Pick a strategy such as rehost or replatform" to listOf("rehost", "refactor", "rearchitect", "replatform"),
                "DMS online for the database" to listOf("database migration service", "dms", "online migration", "managed instance"),
                "AzCopy or Data Box for the files" to listOf("azcopy", "data box", "storage mover"),
            ),
            B.glossary(
                "Cloud Adoption Framework" to "Microsoft's guidebook for moving a company to Azure, in phases from Strategy to Manage.",
                "Landing zone" to "Subscriptions, networks and policies prepared in Azure before workloads move in.",
                "Rehost" to "Moving a server to an Azure VM with no changes, also called lift and shift.",
                "Azure Migrate" to "The free hub that discovers, assesses and migrates servers, databases and web apps.",
                "Online migration" to "The source keeps working while changes flow across; downtime is only the cutover.",
                "Azure Data Box" to "A shipped storage device for moving large amounts of data offline.",
                "Azure Storage Mover" to "A managed service that migrates file shares and sends only changes.",
            ),
            B.recap(
                "CAF guides the move; a landing zone comes first; each app gets an R, such as rehost, replatform, rearchitect, rebuild, replace or retire.",
                "Azure Migrate discovers and assesses; DMS moves SQL Server, online when downtime must be minimal.",
                "Files: AzCopy on a good line, Data Box when the line is too slow, Storage Mover to catch up.",
            ),
        ),
    )

    private val networkConnectivity = Topic(
        id = "sa-network-connectivity", title = "Networks: reaching the internet and on-premises", minutes = 30,
        references = listOf(
            B.ref("What is Azure Virtual Network?", LEARN + "azure/virtual-network/virtual-networks-overview"),
            B.ref("What is Azure NAT Gateway?", LEARN + "azure/nat-gateway/nat-overview"),
            B.ref("Hub-spoke network topology in Azure", LEARN + "azure/architecture/networking/architecture/hub-spoke"),
            B.ref("Virtual network peering", LEARN + "azure/virtual-network/virtual-network-peering-overview"),
            B.ref("What is Azure VPN Gateway?", LEARN + "azure/vpn-gateway/vpn-gateway-about-vpngateways"),
            B.ref("Azure ExpressRoute overview", LEARN + "azure/expressroute/expressroute-introduction"),
            B.ref("Use site-to-site VPN as a backup for ExpressRoute private peering", LEARN + "azure/expressroute/use-s2s-vpn-as-backup-for-expressroute-privatepeering"),
            B.ref("What is a private endpoint?", LEARN + "azure/private-link/private-endpoint-overview"),
            B.ref("Virtual network service endpoints", LEARN + "azure/virtual-network/virtual-network-service-endpoints-overview"),
            B.ref("What is Azure Private DNS?", LEARN + "azure/dns/private-dns-overview"),
            B.ref("What is Azure Virtual WAN?", LEARN + "azure/virtual-wan/virtual-wan-about"),
            B.ref("Learn module: Design network solutions", LEARN + "training/modules/design-network-solutions/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-network-connectivity-w1", "sa-network-connectivity",
                "Ten VMs with no public IPs must call a partner's API, which only accepts one known address. What would you add?",
                listOf("A NAT gateway on their subnet", "A public IP on every VM", "A VPN gateway"), 0,
                "Think of one shared front door that every machine leaves through.",
            ),
            B.plain("""
                A virtual network is your own private floor inside Azure, and subnets are the rooms on it. Machines in
                it talk to each other freely. To get out you need a door: a NAT gateway for outbound internet traffic.
                To reach your office you need a tunnel over the internet (a VPN) or your own private line
                (ExpressRoute). To reach Azure services without the internet you bring them onto your floor.
            """),
            B.diagram(
                caption = "Hub and spoke: the hub holds the shared gateway to the office and the way out, and each spoke is peered to it.",
                nodes = listOf(
                    DNode("office", "Office network", 0.15f, 0.2f, w = 0.26f, h = 0.2f, tone = 2),
                    DNode("hub", "Hub VNet", 0.5f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "gateway, firewall"),
                    DNode("net", "Internet", 0.86f, 0.2f, w = 0.24f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("api", "Spoke: API", 0.27f, 0.75f, w = 0.3f, h = 0.2f),
                    DNode("data", "Spoke: data", 0.73f, 0.75f, w = 0.3f, h = 0.2f),
                ),
                edges = listOf(
                    DEdge("office", "hub", label = "VPN or ExpressRoute", bidirectional = true),
                    DEdge("hub", "net", label = "NAT"),
                    DEdge("hub", "api", label = "peering", bidirectional = true),
                    DEdge("hub", "data", label = "peering", bidirectional = true),
                ),
                aspect = 0.75f,
            ),
            B.h("Virtual networks and the way out"),
            B.p("""
                A virtual network (VNet) has an address space, a block of private addresses such as 10.1.0.0/16,
                like the range of flat numbers in a building. You split it into subnets. Plan the blocks so they
                never overlap with the office or other VNets: two buildings with the same flat numbers cannot share
                one post room later.
            """),
            B.table(
                listOf("Outbound option", "What it is", "Pick it when"),
                listOf("NAT gateway", "A shared exit for a whole subnet with fixed public IPs", "Many VMs need outbound only, from known addresses"),
                listOf("Public IP on a VM", "An address of its own on one machine", "That one VM must also accept traffic from outside"),
                listOf("Load balancer outbound rules", "Outbound through the load balancer's IPs", "The VMs already sit behind a Standard load balancer"),
                listOf("Azure Firewall via a route", "All traffic is sent through a firewall in the hub", "Outbound traffic must be inspected and filtered"),
            ),
            B.tip("""
                Azure used to give VMs a hidden way out to the internet, called default outbound access, and is
                phasing it out. New virtual networks created with API versions after 31 March 2026 get private
                subnets by default (the portal already did this), so their VMs cannot reach the internet until you
                add a way out. Existing networks are unchanged. For most cases the recommended way out is a NAT gateway.
            """),
            B.h("Joining networks: peering, hub and spoke, Virtual WAN"),
            B.p("""
                VNet peering joins two virtual networks over Microsoft's own cables, so traffic never touches the
                internet; global peering does the same across regions. Peering is not transitive, which means it
                does not pass traffic along: if A is peered to B and B to C, A still cannot reach C. That is why big
                estates use a hub and spoke, like a bicycle wheel: shared things sit in the middle and each spoke
                connects only to the hub.
            """),
            B.compare("Hub and spoke or Virtual WAN?",
                B.card("Hub and spoke you build", "A few regions and full control of every part.",
                    "Shared things such as the office gateway and firewall live in a hub VNet you run",
                    "Workloads live in spokes peered to the hub",
                    "Gateway transit lets spokes use the hub's gateway"),
                B.card("Azure Virtual WAN", "Many branches and regions that must all reach each other.",
                    "Microsoft runs the hubs for you",
                    "Any site, spoke and region can reach any other through the hubs",
                    "All the office connections below in one place"),
            ),
            B.check(
                "sa-network-connectivity-k1", "sa-network-connectivity",
                "Spoke A and spoke B are each peered to a hub, but not to each other. Can a VM in A reach a VM in B through the hub?",
                listOf("Not by peering alone; add a firewall or router in the hub, or peer A and B", "Yes, peering passes traffic along automatically", "Only if both spokes are in the same region"), 0,
                "Peering is not transitive. Traffic between spokes needs a hop in the hub (such as Azure Firewall with routes), a direct peering, or Virtual WAN.",
                whyWrong = mapOf(
                    1 to "This is the classic trap: peering joins exactly two networks and passes nothing on.",
                    2 to "Regions do not matter; global peering works across them, and transitivity is still missing.",
                ),
            ),
            B.h("Reaching the office: VPN or ExpressRoute"),
            B.compare("Hybrid connections",
                B.card("Site-to-site VPN", "A whole office must connect cheaply and quickly.",
                    "A VPN is a sealed tunnel through the public internet, like a locked van driving on public roads",
                    "It joins the office router to a VPN gateway in Azure",
                    "Ready in hours; speed and delay depend on the internet"),
                B.card("Point-to-site VPN", "Single laptops, such as people working from home.",
                    "Each device builds its own tunnel to the VPN gateway",
                    "No office router needed; can sign in with Entra ID"),
                B.card("ExpressRoute", "Steady high bandwidth, predictable delay, or traffic must avoid the internet.",
                    "A private line, like your own private road, from your network to Microsoft through a telecoms provider",
                    "Fast and steady, because nobody else shares the road",
                    "Takes weeks to order; private but not encrypted by default"),
            ),
            B.remember("""
                For the best of both, run ExpressRoute with a site-to-site VPN as failover: both gateways sit in the
                same VNet, Azure prefers the ExpressRoute path, and the VPN carries traffic if the private line fails.
            """),
            B.check(
                "sa-network-connectivity-k2", "sa-network-connectivity",
                "Nightly transfers need a steady 1 Gbps with predictable delay, and a backup path if that link fails. What do you recommend?",
                listOf("ExpressRoute, with a site-to-site VPN as failover", "Two site-to-site VPNs", "Point-to-site VPN"), 0,
                "ExpressRoute gives the private, predictable line; a VPN over the internet is the cheaper standby.",
                whyWrong = mapOf(
                    1 to "VPNs ride the internet, so the delay is never predictable, however many you add.",
                    2 to "Point-to-site is for single devices, not an office link.",
                ),
            ),
            B.h("Names: DNS and Private DNS zones"),
            B.p("""
                DNS is the internet's phone book: it turns a name like api.twotracks.app into an address. A Private
                DNS zone is a phone book only the VNets linked to it can read, for names that must stay inside.
                Azure DNS Private Resolver lets office machines look up those private names too.
            """),
            B.h("Reaching PaaS privately"),
            B.compare("Private endpoint or service endpoint?",
                B.card("Private endpoint", "Traffic must stay private, from the office too, to one exact resource.",
                    "Gives one storage account or database a private address inside your subnet, like an extension line on your own switchboard",
                    "Reachable over peering, VPN and ExpressRoute",
                    "Needs a Private DNS zone so the name finds the private address",
                    "Costs a little per hour and per GB"),
                B.card("Service endpoint", "A free way to let one subnet in and keep others out.",
                    "The subnet reaches the service over Microsoft's cables; the service keeps its public address",
                    "The service's firewall then allows that subnet",
                    "Not usable from the office, and covers the service type, not one resource"),
            ),
            B.code("bash", """
                # A private address for the database, plus the phone book that points to it
                az network private-endpoint create -g tt-net -n pe-sql --vnet-name spoke-data --subnet db \
                  --private-connection-resource-id <sql-server-id> --group-id sqlServer --connection-name sql
                az network private-dns zone create -g tt-net -n privatelink.database.windows.net
            """),
            B.check(
                "sa-network-connectivity-k3", "sa-network-connectivity",
                "Office staff must reach an Azure SQL database over ExpressRoute, and the database must not be reachable from the internet. Which feature?",
                listOf("A private endpoint with public access turned off", "A service endpoint on the office subnet", "A NAT gateway on the database"), 0,
                "A private endpoint gives the database an address in your VNet, which the office reaches over ExpressRoute.",
                whyWrong = mapOf(
                    1 to "Service endpoints only work from subnets in Azure, and the office is not one.",
                    2 to "A NAT gateway is for outbound traffic from VMs, not for reaching a database.",
                ),
            ),
            B.worked("""
                Worked example. Requirements: the office reaches the database privately at a steady 1 Gbps and must
                survive a line failure; the API calls a payment partner that only accepts known addresses; no
                database may be reachable from the internet. Choice: a hub and spoke in each region, hubs peered.
                ExpressRoute into the Europe hub with a site-to-site VPN as failover. A NAT gateway on each API subnet
                for fixed outbound addresses. Private endpoints with Private DNS zones for the databases, public
                access off. Why: each requirement word points at one feature.
            """),
            B.faded(
                "sa-network-connectivity-f1",
                "Thirty Two Tracks developers work from home and need their laptops to reach a test VNet. There is no office router to connect.",
                listOf(
                    "Site-to-site is out: it joins a network router, and there is none.",
                    "Put a VPN gateway in the hub and configure a ___-to-site VPN, so each laptop builds its own tunnel.",
                    "Let the developers sign in with Entra ID, and use gateway transit so the tunnel reaches the peered test spoke.",
                ),
                blank = 1,
                answers = listOf("point"),
                note = "Site-to-site for a whole network, point-to-site for single devices, ExpressRoute for a private line.",
            ),
            B.steps("Do it for real",
                "az network vnet create -g tt-net -n spoke-api --address-prefixes 10.2.0.0/16 --subnet-name app --subnet-prefixes 10.2.1.0/24" to "A spoke VNet with one subnet.",
                "az network public-ip create -g tt-net -n nat-ip --sku Standard" to "A fixed address for the way out.",
                "az network nat gateway create -g tt-net -n nat-api --public-ip-addresses nat-ip" to "The NAT gateway, not yet in use.",
                "az network vnet subnet update -g tt-net --vnet-name spoke-api -n app --nat-gateway nat-api" to "Every VM in the subnet now leaves from nat-ip.",
                "az network vnet peering create -g tt-net -n api-to-hub --vnet-name spoke-api --remote-vnet hub --allow-vnet-access" to "Half of the peering; create the reverse from the hub too.",
            ),
            B.mistakes(
                "Expecting two spokes to talk because both are peered to the same hub.",
                "Choosing a VPN when the requirement says predictable latency or no internet path.",
                "Creating a private endpoint without a Private DNS zone, so the name still resolves to the public address.",
                "Using a service endpoint when the traffic comes from on-premises; it only works from Azure subnets.",
                "Giving VNets overlapping address ranges and finding later they can never be peered.",
            ),
            B.example("""
                Two Tracks goes global: a hub in West Europe and one in Australia East, peered; ExpressRoute plus VPN
                failover to the office; NAT gateways on the API spokes for the payment partner; private endpoints for
                every database; and point-to-site VPN for the developers at home.
            """),
            B.h("Check yourself"),
            B.match(
                "sa-x-netconn-match", "Pair each requirement with the feature it points to.",
                "Fixed outbound IP for many VMs" to "NAT gateway",
                "Private line with predictable latency" to "ExpressRoute",
                "Single laptops connecting from home" to "Point-to-site VPN",
                "One database reachable privately from the office" to "Private endpoint",
            ),
            B.gap(
                "sa-x-netconn-gap1", "A peered to B and B peered to C still does not let A reach C.",
                "VNet peering is not ___.", "transitive", "Transitive",
            ),
            B.tryIt("Try it in 15 minutes", "A NAT gateway costs a few cents an hour; delete the resource group afterwards.",
                "Create a hub VNet and a spoke VNet with different address ranges",
                "Peer them in both directions and check both show Connected",
                "Add a NAT gateway to the spoke subnet",
                "Start a small VM there and check its outbound address matches the NAT gateway IP",
            ),
            B.explain(
                "sa-network-connectivity-x1",
                "Explain how you would connect an office to Azure, give VMs a way out to the internet, and reach a database privately.",
                "Use ExpressRoute when the office needs a private line with predictable latency, with a site-to-site VPN as failover; a VPN alone is cheaper and quicker but rides the internet. Put the gateway in a hub VNet and peer the spokes to it, remembering peering is not transitive. Give VMs a fixed way out with a NAT gateway on their subnet. Reach the database through a private endpoint with a Private DNS zone, which also works from the office, unlike a service endpoint.",
                "ExpressRoute or VPN for the office" to listOf("expressroute", "site-to-site", "vpn gateway", "vpn"),
                "Hub and spoke with peering" to listOf("hub", "spoke", "peering", "peered"),
                "NAT gateway for outbound" to listOf("nat gateway", "outbound"),
                "Private endpoint for PaaS" to listOf("private endpoint", "private dns", "privatelink"),
            ),
            B.glossary(
                "Virtual network" to "Your private network in Azure, split into subnets.",
                "NAT gateway" to "A shared outbound exit for a subnet, with fixed public IPs.",
                "VNet peering" to "Joins two VNets over Microsoft's backbone; not transitive.",
                "Hub and spoke" to "Shared services in a hub VNet, workloads in spokes peered to it.",
                "ExpressRoute" to "A private connection to Microsoft through a provider, not over the internet.",
                "Private endpoint" to "A private address in your subnet for one PaaS resource.",
                "Private DNS zone" to "Name records that only linked VNets can look up.",
            ),
            B.recap(
                "VNets and subnets with non-overlapping ranges; a NAT gateway is the recommended way out.",
                "Peering is not transitive, so use hub and spoke or Virtual WAN; VPN for quick and cheap, ExpressRoute for private and predictable, VPN as its failover.",
                "Private endpoint plus Private DNS for PaaS reached privately, including from the office; service endpoints only from Azure subnets.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(migrations, networkConnectivity)
}
