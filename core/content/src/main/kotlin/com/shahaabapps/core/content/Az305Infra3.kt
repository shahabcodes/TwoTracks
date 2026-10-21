package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Infra3 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"
    private const val T = "sa-network-optimize"

    private val networkOptimize = Topic(
        id = T, title = "Networks: performance, security, load balancing and routing", minutes = 30,
        references = listOf(
            B.ref("Load-balancing options (Azure Architecture Center)", LEARN + "azure/architecture/guide/technology-choices/load-balancing-overview"),
            B.ref("Network security groups", LEARN + "azure/virtual-network/network-security-groups-overview"),
            B.ref("Application security groups", LEARN + "azure/virtual-network/application-security-groups"),
            B.ref("What is Azure Firewall?", LEARN + "azure/firewall/overview"),
            B.ref("What is Azure Web Application Firewall?", LEARN + "azure/web-application-firewall/overview"),
            B.ref("Azure DDoS Protection tier comparison", LEARN + "azure/ddos-protection/ddos-protection-sku-comparison"),
            B.ref("What is Azure Bastion?", LEARN + "azure/bastion/bastion-overview"),
            B.ref("What is a private endpoint?", LEARN + "azure/private-link/private-endpoint-overview"),
            B.ref("Accelerated networking overview", LEARN + "azure/virtual-network/accelerated-networking-overview"),
            B.ref("Proximity placement groups", LEARN + "azure/virtual-machines/co-location"),
            B.ref("Virtual network traffic routing", LEARN + "azure/virtual-network/virtual-networks-udr-overview"),
            B.ref("Azure Firewall forced tunneling", LEARN + "azure/firewall/forced-tunneling"),
            B.ref("Learn module: Design network solutions", LEARN + "training/modules/design-network-solutions/"),
        ),
        blocks = listOf(
            B.warmup(
                "$T-w1", T,
                "Two Tracks serves a web app to Europe and Australia and wants one front door that caches, routes by URL and blocks web attacks. Which service?",
                listOf("Azure Front Door", "Azure Traffic Manager", "Azure Load Balancer"), 0,
                "Two of these never look inside a web request. Which one is global and reads the URL?",
            ),
            B.plain("""
                Picture a busy restaurant. A host at the door sends each guest to a free table: that is load
                balancing, spreading requests over several servers so none is swamped. A bouncer checks who may
                come in at all: that is network security. The map of corridors decides which way each tray
                travels: that is routing.
            """),
            B.diagram(
                caption = "Ask two questions: is it web traffic, and does it span regions? The answers point to one of four load balancers, a short form of the Architecture Center's decision tree.",
                nodes = listOf(
                    DNode("web", "Web traffic (HTTP/S)?", 0.5f, 0.1f, w = 0.5f, h = 0.12f, tone = 1),
                    DNode("gy", "Global, many regions?", 0.27f, 0.38f, w = 0.44f, h = 0.12f, tone = 2),
                    DNode("gn", "Global, many regions?", 0.75f, 0.38f, w = 0.44f, h = 0.12f, tone = 2),
                    DNode("afd", "Front Door", 0.13f, 0.72f, w = 0.24f, h = 0.18f, sub = "L7, global, CDN"),
                    DNode("agw", "App Gateway + WAF", 0.38f, 0.72f, w = 0.24f, h = 0.18f, sub = "L7, regional"),
                    DNode("tm", "Traffic Manager", 0.63f, 0.72f, w = 0.24f, h = 0.18f, sub = "DNS, global"),
                    DNode("lb", "Load Balancer", 0.87f, 0.72f, w = 0.24f, h = 0.18f, sub = "L4, regional"),
                    DNode("note", "Global non-web: also cross-region Load Balancer", 0.5f, 0.92f, w = 0.9f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("web", "gy", label = "yes"),
                    DEdge("web", "gn", label = "no"),
                    DEdge("gy", "afd", label = "yes"),
                    DEdge("gy", "agw", label = "no"),
                    DEdge("gn", "tm", label = "yes"),
                    DEdge("gn", "lb", label = "no"),
                ),
                aspect = 1.0f,
            ),
            B.h("Load balancing: the four choices"),
            B.p("""
                Two words help. Layer 4 means the balancer only sees addresses and ports, like a postman who
                reads the envelope. Layer 7 means it reads the web request inside, such as the URL path, like a
                clerk who opens the letter. Regional means it spreads work inside one Azure region; global means
                it picks between regions.
            """),
            B.table(
                listOf("Service", "Layer and reach", "Listen for"),
                listOf("Azure Load Balancer", "Layer 4, regional; a cross-region version spans regions", "TCP or UDP, any protocol, very low delay, VMs in one region"),
                listOf("Application Gateway", "Layer 7, regional", "Routing by URL path, decrypting HTTPS, a WAF, one region"),
                listOf("Azure Front Door", "Layer 7, global, with caching near users", "Users worldwide, fast failover, caching, a WAF at the edge"),
                listOf("Traffic Manager", "DNS, global", "Any protocol across regions, or office endpoints too"),
            ),
            B.p("""
                Traffic Manager never touches your traffic: it only answers the DNS question, the phone book
                lookup that turns a name into an address. Devices remember those answers for a while, so failover
                is slower than with Front Door. Decrypting HTTPS at the balancer, called TLS offload, saves your
                servers that work. A WAF, web application firewall, is a guard that reads each web request and
                stops known attacks.
            """),
            B.tip("\"Global HTTP with caching or acceleration\" points to Front Door. \"Path-based routing inside one region\" points to Application Gateway. \"Non-HTTP across regions\" points to Traffic Manager. \"TCP or UDP in one region\" points to Load Balancer."),
            B.check(
                "$T-k1", T,
                "Two Tracks runs its web API in West Europe and Australia East. It needs one global entry point that caches images, fails over fast and blocks web attacks. Which service?",
                listOf("Azure Front Door with a WAF policy", "Traffic Manager with performance routing", "One Application Gateway in West Europe"), 0,
                "Front Door is global, works at layer 7, caches at the edge and carries a WAF, so it covers every requirement in one service.",
                whyWrong = mapOf(
                    1 to "Traffic Manager only answers DNS lookups: no caching, no WAF, and failover waits for DNS caches to expire.",
                    2 to "Application Gateway is regional; one in Europe becomes a single point of failure and adds latency for Australia.",
                ),
            ),
            B.h("Security: who may pass"),
            B.p("""
                A network security group, NSG, is a guest list of allow and deny rules on a subnet or a network card.
                Each rule names who is calling, who is called, a port (the door number on a machine) and a protocol.
                Rules are checked in order, lowest number first. An application security group, ASG, is a name tag
                for a set of VMs, such as "api-servers", so a rule can say "web servers may reach api-servers on
                443" without listing addresses.
            """),
            B.compare("NSG, Azure Firewall or WAF?",
                B.card("NSG", "Free, basic filtering by address and port on every subnet.", "Sees only addresses, ports and protocols", "Attached to subnets or network cards", "Never reads what is inside the traffic"),
                B.card("Azure Firewall", "One central checkpoint for traffic between networks and the internet.", "A managed firewall in the hub", "Can allow or block by domain name, such as *.twotracks.app", "Premium can also look inside encrypted traffic"),
                B.card("WAF", "A public web app must be shielded from attacks such as SQL injection.", "Reads each web request at layer 7", "Runs on Application Gateway or Front Door", "Ready-made rules for common web attacks"),
            ),
            B.bullets(
                "A DDoS attack floods you with fake traffic, like a crowd blocking a shop door. Azure's basic infrastructure protection is free and always on.",
                "DDoS Network Protection covers the public addresses in a virtual network, priced per 100 addresses, and adds expert help during an attack and cost protection.",
                "DDoS IP Protection covers single public addresses, priced per address, without those extras.",
                "Azure Bastion is a managed jump box, a guarded side door for admins. They open RDP or SSH, the remote-control screens for Windows and Linux, in the browser, so VMs need no public address.",
                "A private endpoint gives a PaaS service, such as a storage account, a private address inside your virtual network, so you can switch its public access off.",
            ),
            B.check(
                "$T-k2", T,
                "The Two Tracks sign-up page has been probed with SQL injection attempts. What stops these at the edge?",
                listOf("A Web Application Firewall policy", "A network security group rule on the web subnet", "DDoS Network Protection"), 0,
                "A WAF reads each HTTP request and blocks known attack patterns such as SQL injection and cross-site scripting.",
                whyWrong = mapOf(
                    1 to "An NSG only sees IPs and ports; the attack arrives on port 443 like any normal request.",
                    2 to "DDoS protection absorbs floods of traffic; it does not judge what a single request contains.",
                ),
            ),
            B.h("Performance"),
            B.bullets(
                "Accelerated networking lets a VM's network card skip the host's software switch, like an express lane past the checkout. It lowers delay and its wobble (jitter) and frees processor time, on supported VM sizes.",
                "A proximity placement group keeps VMs physically close in one datacenter, like seating a chatty team at one desk, for the lowest delay between them.",
                "Front Door's caching keeps copies of content in places near users far from your region.",
            ),
            B.h("Routing: user-defined routes and forced tunnelling"),
            B.diagram(
                caption = "A route table sends all outbound traffic from the app subnet to Azure Firewall, which passes it on to the internet or on-premises.",
                nodes = listOf(
                    DNode("vm", "App VMs", 0.15f, 0.3f, w = 0.26f, h = 0.2f, tone = 1, sub = "NSG + ASG rules"),
                    DNode("fw", "Azure Firewall", 0.5f, 0.3f, w = 0.28f, h = 0.2f, sub = "hub, inspects"),
                    DNode("net", "Internet", 0.85f, 0.3f, w = 0.24f, h = 0.2f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("bastion", "Azure Bastion", 0.15f, 0.72f, w = 0.26f, h = 0.2f, tone = 2, sub = "admin over TLS"),
                    DNode("rt", "Route table 0.0.0.0/0 to firewall", 0.5f, 0.72f, w = 0.36f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                    DNode("onprem", "On-premises", 0.85f, 0.72f, w = 0.26f, h = 0.2f, tone = 1, sub = "VPN gateway"),
                ),
                edges = listOf(
                    DEdge("vm", "fw", label = "UDR"),
                    DEdge("fw", "net", label = "allowed"),
                    DEdge("fw", "onprem", label = "forced", dashed = true),
                    DEdge("bastion", "vm", label = "RDP/SSH"),
                ),
                aspect = 0.7f,
            ),
            B.p("""
                Azure routes traffic for you with system routes, like the default signposts in a building. A
                user-defined route, UDR, is your own signpost that overrides them: you put routes in a route table
                and attach it to a subnet. The common one sends 0.0.0.0/0, meaning "everything else", to the
                firewall's private address, with the next hop type Virtual appliance. Forced tunnelling goes further:
                internet-bound traffic goes back to the office first, so the company's own firewall checks it.
            """),
            B.check(
                "$T-k3", T,
                "All outbound traffic from the spoke subnets must pass through Azure Firewall in the hub. What do you configure?",
                listOf("A route table with 0.0.0.0/0 to the firewall's private IP, attached to each spoke subnet", "An outbound NSG rule that allows the firewall", "A service endpoint on each subnet"), 0,
                "Only a route changes where packets go. The UDR points the default route at the firewall as a virtual appliance.",
                whyWrong = mapOf(
                    1 to "An NSG decides whether traffic may pass, not which way it travels.",
                    2 to "A service endpoint optimises the path to a PaaS service; it does not send traffic through a firewall.",
                ),
            ),
            B.worked("""
                Worked example. Requirement: the Two Tracks web API runs on VMs in two regions, users are in
                Europe and Australia, and they want caching and a WAF. Step 1: it is HTTPS, so layer 7. Step 2:
                it spans regions, so global. Step 3: caching points to Front Door. Choice: Front Door with a WAF
                policy. Inside each region the VMs sit behind a Load Balancer, the pairing the Architecture Center's
                decision tree gives for VMs.
            """),
            B.faded(
                "$T-f1",
                "A live quiz game for Two Tracks streams answers over UDP to game servers in one region, Australia East.",
                listOf(
                    "Is it web traffic? No: the game speaks UDP, so layer 4 is enough.",
                    "Does it span regions? No: one region only.",
                    "Pick Azure ___: layer 4, regional, and it passes UDP straight through.",
                ),
                blank = 2,
                answers = listOf("load balancer", "loadbalancer", "azure load balancer"),
                note = "Non-web and one region is the Load Balancer branch; Front Door only speaks web protocols, and Application Gateway has no UDP.",
            ),
            B.steps("Do it for real",
                "az network asg create -g tt-rg -n tt-api-asg" to "An application security group to tag the API VMs.",
                "az network nsg rule create -g tt-rg --nsg-name tt-app-nsg -n allow-https --priority 100 --destination-asgs tt-api-asg --destination-port-ranges 443 --protocol Tcp --access Allow" to "HTTPS reaches any VM tagged tt-api-asg, with no IPs listed.",
                "az network route-table route create -g tt-rg --route-table-name tt-spoke-rt -n to-fw --address-prefix 0.0.0.0/0 --next-hop-type VirtualAppliance --next-hop-ip-address 10.0.1.4" to "The default route now points at the firewall's private IP.",
                "az network vnet subnet update -g tt-rg --vnet-name tt-spoke -n app --route-table tt-spoke-rt --network-security-group tt-app-nsg" to "The subnet uses both the route table and the NSG.",
                "In the portal, open a VM, choose Connect, then Bastion" to "An RDP or SSH session opens in the browser; the VM has no public IP.",
            ),
            B.mistakes(
                "Choosing Traffic Manager when the requirement mentions WAF, caching or URL paths; it only answers DNS.",
                "Expecting an NSG to block SQL injection; it never reads the request, only IPs and ports.",
                "Putting one Application Gateway in front of two regions and calling it global.",
                "Opening RDP port 3389 to the internet instead of using Azure Bastion.",
                "Adding an NSG rule and expecting traffic to flow through the firewall; only a route changes the path.",
            ),
            B.example("Two Tracks goes global: Front Door with a WAF policy greets users in Europe and Australia and caches images. Each region has a Load Balancer for its API VMs, NSGs with ASGs on every subnet, a UDR sending outbound traffic to Azure Firewall, DDoS Network Protection on the hub, Bastion for admins and private endpoints for the database."),
            B.h("Check yourself"),
            B.match(
                "sa-x-netopt-match", "Pair each requirement with the service it points to.",
                "Global HTTPS with caching" to "Azure Front Door",
                "URL path routing in one region" to "Application Gateway",
                "UDP across VMs in one region" to "Azure Load Balancer",
                "Any protocol, DNS-based, across regions" to "Traffic Manager",
                "RDP to VMs with no public IP" to "Azure Bastion",
            ),
            B.gap(
                "sa-x-netopt-gap1", "Think of the postman and the clerk.",
                "Application Gateway and Front Door work at layer ___.", "7", "seven",
            ),
            B.gap(
                "sa-x-netopt-gap2", "The tag that groups VMs by role for NSG rules.",
                "Rules can target an application ___ group instead of IP addresses.", "security",
            ),
            B.tryIt("Try it in 15 minutes", "Free or a few cents; delete the resource group afterwards.",
                "Read the load-balancing decision tree on the Architecture Center and trace the Two Tracks web API through it",
                "Create a virtual network, an NSG and an ASG, and add one rule that targets the ASG",
                "Create a route table with a 0.0.0.0/0 route to a virtual appliance and attach it to a subnet",
                "Check a VM's effective routes in the portal and see your route override the system route",
            ),
            B.explain(
                "$T-x1",
                "Explain how Two Tracks serves its web app to Europe and Australia and keeps the network behind it safe.",
                "Azure Front Door is the global layer 7 entry point; it caches content and fails over between regions. A web application firewall (WAF) policy on Front Door blocks attacks such as SQL injection. Inside each region, network security groups with application security groups allow only the ports each tier needs, a user-defined route sends outbound traffic to Azure Firewall, and admins reach VMs through Azure Bastion, so no VM has a public IP.",
                "Global layer 7 entry" to listOf("front door", "frontdoor"),
                "Web attack protection" to listOf("waf", "web application firewall"),
                "Port and IP filtering" to listOf("network security group", "nsg"),
                "Admin access without public IPs" to listOf("bastion", "no public ip"),
            ),
            B.glossary(
                "Layer 4 / layer 7" to "Layer 4 sees addresses and ports; layer 7 reads the web request itself.",
                "Network security group (NSG)" to "Allow and deny rules by IP, port and protocol on a subnet or network card.",
                "Application security group (ASG)" to "A role tag for VMs that NSG rules can name instead of IPs.",
                "Web Application Firewall (WAF)" to "Blocks web attacks such as SQL injection; runs on App Gateway or Front Door.",
                "Azure Bastion" to "Browser RDP and SSH to VMs that have no public IP.",
                "User-defined route (UDR)" to "A route you add to a route table to override Azure's system routes.",
                "Forced tunnelling" to "Sending internet-bound traffic back on-premises for inspection first.",
            ),
            B.recap(
                "Web and global is Front Door; web and regional is Application Gateway; non-web global is Traffic Manager; non-web regional is Load Balancer.",
                "NSGs and ASGs filter ports and IPs, Azure Firewall inspects centrally, a WAF stops web attacks, DDoS Protection absorbs floods, Bastion replaces public RDP.",
                "A UDR with 0.0.0.0/0 to a virtual appliance forces traffic through a firewall; accelerated networking and proximity placement cut latency.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(networkOptimize)

    val quiz: Quiz = Quiz(
        id = "sa-infra-test", title = "Infrastructure module test",
        questions = B.spread(
            listOf(
                // Compute
                B.q(
                    "sa-n1", "sa-compute",
                    "A legacy Windows app needs full control of the operating system and a custom driver. Which compute option fits?",
                    listOf("Azure Virtual Machines", "Azure App Service", "Azure Functions"), setOf(0),
                    "Only a virtual machine gives you the operating system itself, so you can install drivers and anything else you need.",
                    whyWrong = mapOf(
                        1 to "App Service is a managed platform; you cannot install drivers on its operating system.",
                        2 to "Functions runs small pieces of code on demand and hides the operating system completely.",
                    ),
                ),
                B.q(
                    "sa-n2", "sa-compute",
                    "Image thumbnails must be made whenever a file lands in Blob Storage, paying nothing while idle. Which option?",
                    listOf("Azure Functions on the Flex Consumption or Consumption plan", "A virtual machine scale set", "Azure Kubernetes Service"), setOf(0),
                    "Event-driven, short tasks that should cost nothing when idle point to serverless Functions with a blob trigger.",
                    whyWrong = mapOf(
                        1 to "A scale set keeps at least one VM running and billed, and you manage the operating system.",
                        2 to "AKS is powerful but you run a cluster all the time; it is far more than this job needs.",
                    ),
                ),
                B.q(
                    "sa-n3", "sa-compute",
                    "Thousands of video files must be rendered in parallel overnight on many VMs, with Azure scheduling the work. Which service?",
                    listOf("Azure Batch", "Azure Logic Apps", "Azure App Service"), setOf(0),
                    "Azure Batch runs large parallel and high-performance jobs, creating a pool of VMs and scheduling tasks on them.",
                    whyWrong = mapOf(
                        1 to "Logic Apps orchestrates workflows between services; it is not a compute farm for rendering.",
                        2 to "App Service hosts web apps and APIs, not large parallel batch jobs.",
                    ),
                ),
                B.q(
                    "sa-n4", "sa-compute",
                    "Several microservices in containers need scale to zero and HTTP and event-based scaling, without managing Kubernetes. Which option?",
                    listOf("Azure Container Apps", "Azure Kubernetes Service", "Azure Container Instances"), setOf(0),
                    "Container Apps runs microservices on a managed platform with KEDA-based scaling, including scale to zero, and hides the cluster.",
                    whyWrong = mapOf(
                        1 to "AKS gives full Kubernetes control, but the team must operate the cluster, which the requirement rules out.",
                        2 to "Container Instances runs single containers or groups with no built-in autoscaling across many services.",
                    ),
                ),
                B.q(
                    "sa-n5", "sa-compute",
                    "A team needs full Kubernetes API access, custom operators and node pool control. Which service?",
                    listOf("Azure Kubernetes Service", "Azure Container Apps", "Azure Functions"), setOf(0),
                    "When the requirement names the Kubernetes API, operators or node control, the answer is AKS.",
                    whyWrong = mapOf(
                        1 to "Container Apps is built on Kubernetes but does not expose its API to you.",
                        2 to "Functions is serverless code, not a container orchestrator.",
                    ),
                ),
                // Application architecture
                B.q(
                    "sa-n6", "sa-app-architecture",
                    "Orders must be processed exactly in order, with duplicate detection and dead-lettering. Which messaging service?",
                    listOf("Azure Service Bus with sessions", "Azure Queue Storage", "Azure Event Grid"), setOf(0),
                    "Service Bus is the enterprise broker: sessions give ordered processing, and it has duplicate detection and dead-letter queues.",
                    whyWrong = mapOf(
                        1 to "Queue Storage is a simple, cheap queue with no ordering guarantee or duplicate detection.",
                        2 to "Event Grid pushes notifications that something happened; it is not an ordered command queue.",
                    ),
                ),
                B.q(
                    "sa-n7", "sa-app-architecture",
                    "Millions of telemetry readings per second must be ingested and replayed by several readers. Which service?",
                    listOf("Azure Event Hubs", "Azure Service Bus", "Azure Event Grid"), setOf(0),
                    "Event Hubs is a streaming platform built for high-volume ingestion, with partitions and consumer groups that can replay the stream.",
                    whyWrong = mapOf(
                        1 to "Service Bus is for business messages that need careful handling, not millions of readings a second.",
                        2 to "Event Grid routes discrete events to handlers; it does not keep a replayable stream.",
                    ),
                ),
                B.q(
                    "sa-n8", "sa-app-architecture",
                    "Partners must call the Two Tracks APIs with keys, rate limits and a developer portal. Which service?",
                    listOf("Azure API Management", "Azure Front Door", "Azure Load Balancer"), setOf(0),
                    "API Management is the API gateway: subscription keys, rate-limit policies, versioning and a developer portal.",
                    whyWrong = mapOf(
                        1 to "Front Door routes and protects web traffic globally, but has no developer portal or API subscriptions.",
                        2 to "Load Balancer spreads TCP and UDP traffic; it knows nothing about APIs.",
                    ),
                ),
                B.q(
                    "sa-n9", "sa-app-architecture",
                    "The database is slow under repeated reads of the same leaderboard. What reduces the load with minimal change?",
                    listOf("Cache the results in Azure Managed Redis using cache-aside", "Add a second Application Gateway", "Move the database to a bigger VM"), setOf(0),
                    "An in-memory cache serves repeated reads in milliseconds; cache-aside loads it on a miss and keeps the database for writes.",
                    whyWrong = mapOf(
                        1 to "A gateway spreads incoming requests; every request would still hit the database.",
                        2 to "Scaling up costs more and still repeats the same expensive queries.",
                    ),
                ),
                B.q(
                    "sa-n10", "sa-app-architecture",
                    "Many apps share settings and feature flags that change without redeploying. Where should they live?",
                    listOf("Azure App Configuration", "Azure Key Vault", "Each app's own settings file"), setOf(0),
                    "App Configuration centralises settings and feature flags; secrets stay in Key Vault and are referenced from it.",
                    whyWrong = mapOf(
                        1 to "Key Vault is for secrets, keys and certificates, not everyday settings and feature flags.",
                        2 to "Per-app files drift apart and need a redeploy to change.",
                    ),
                ),
                // Migrations
                B.q(
                    "sa-n11", "sa-migrations",
                    "Before migrating, Two Tracks must discover its on-premises servers, map their dependencies and estimate Azure costs. Which tool?",
                    listOf("Azure Migrate", "Azure Site Recovery", "Azure Data Box"), setOf(0),
                    "Azure Migrate discovers servers, analyses dependencies, and produces assessments with sizing and cost estimates.",
                    whyWrong = mapOf(
                        1 to "Site Recovery replicates machines for disaster recovery; it does not assess an estate.",
                        2 to "Data Box is a physical device for moving large amounts of data offline.",
                    ),
                ),
                B.q(
                    "sa-n12", "sa-migrations",
                    "A SQL Server database must move to Azure with near-zero compatibility changes, including SQL Agent jobs and cross-database queries. Which target?",
                    listOf("Azure SQL Managed Instance", "Azure SQL Database single database", "Azure Cosmos DB"), setOf(0),
                    "SQL Managed Instance is close to full SQL Server compatibility, with SQL Agent and cross-database queries, as a managed service.",
                    whyWrong = mapOf(
                        1 to "A single database lacks SQL Agent and cross-database queries.",
                        2 to "Cosmos DB is a NoSQL database; moving to it means rewriting the app.",
                    ),
                ),
                B.q(
                    "sa-n13", "sa-migrations",
                    "80 TB of archived files must reach Azure Storage within two weeks over a slow office link. What do you use?",
                    listOf("Azure Data Box", "AzCopy over the internet", "Azure File Sync"), setOf(0),
                    "Data Box is shipped to you, filled locally and sent back, which beats a slow link for tens of terabytes.",
                    whyWrong = mapOf(
                        1 to "AzCopy is fast for online copies, but a slow link cannot move 80 TB in two weeks.",
                        2 to "File Sync keeps file servers in sync with Azure Files; it still uploads over the same slow link.",
                    ),
                ),
                B.q(
                    "sa-n14", "sa-migrations",
                    "In the Cloud Adoption Framework, which phase sets out the business reasons, outcomes and first projects for moving to Azure?",
                    listOf("Strategy", "Ready", "Manage"), setOf(0),
                    "The Strategy phase records the motivations and expected business outcomes; Plan, Ready and Adopt follow it.",
                    whyWrong = mapOf(
                        1 to "Ready prepares the landing zone, the Azure environment workloads move into.",
                        2 to "Manage covers running the estate once workloads are in Azure.",
                    ),
                ),
                B.q(
                    "sa-n15", "sa-migrations",
                    "A PostgreSQL database must move to Azure Database for PostgreSQL with minimal downtime. Which approach?",
                    listOf("An online migration that keeps syncing changes until cutover", "An offline backup and restore during business hours", "Rebuild the database by hand in Azure"), setOf(0),
                    "An online migration copies the data, keeps replicating changes and lets you cut over in minutes, so downtime stays minimal.",
                    whyWrong = mapOf(
                        1 to "Offline migration stops the app for the whole copy, which is the downtime the requirement forbids.",
                        2 to "Rebuilding by hand risks data loss and takes the longest.",
                    ),
                ),
                // Network connectivity
                B.q(
                    "sa-n16", "sa-network-connectivity",
                    "The head office needs a private, dedicated connection to Azure with predictable latency that does not cross the public internet. What do you recommend?",
                    listOf("ExpressRoute", "A site-to-site VPN", "A point-to-site VPN"), setOf(0),
                    "ExpressRoute is a private circuit through a connectivity provider: it never crosses the internet and gives predictable performance.",
                    whyWrong = mapOf(
                        1 to "A site-to-site VPN is encrypted but still runs over the public internet.",
                        2 to "Point-to-site connects single devices, not a whole office.",
                    ),
                ),
                B.q(
                    "sa-n17", "sa-network-connectivity",
                    "Two virtual networks in different regions must talk privately with low latency over the Microsoft backbone. What do you use?",
                    listOf("Global virtual network peering", "A NAT gateway", "Azure Bastion"), setOf(0),
                    "Global peering joins virtual networks across regions so traffic stays on Microsoft's backbone with private IPs.",
                    whyWrong = mapOf(
                        1 to "A NAT gateway gives a subnet outbound internet access; it does not join networks.",
                        2 to "Bastion is for admin RDP and SSH, not network-to-network traffic.",
                    ),
                ),
                B.q(
                    "sa-n18", "sa-network-connectivity",
                    "Many VMs in a subnet need outbound internet access through a fixed set of public IPs, with no inbound access. What do you add?",
                    listOf("A NAT gateway on the subnet", "A public IP on every VM", "Traffic Manager"), setOf(0),
                    "A NAT gateway gives a whole subnet outbound-only connectivity through static public IPs and scales the connections.",
                    whyWrong = mapOf(
                        1 to "A public IP per VM opens each one to inbound traffic and is harder to manage.",
                        2 to "Traffic Manager directs incoming DNS lookups; it does not provide outbound access.",
                    ),
                ),
                B.q(
                    "sa-n19", "sa-network-connectivity",
                    "Dozens of branch offices and several regions must connect through one managed hub with built-in routing. Which service?",
                    listOf("Azure Virtual WAN", "One site-to-site VPN per pair of sites", "Application Gateway"), setOf(0),
                    "Virtual WAN provides managed hubs that join branches, VPN users, ExpressRoute and virtual networks with automatic routing.",
                    whyWrong = mapOf(
                        1 to "A mesh of separate VPNs grows quickly and must be routed by hand.",
                        2 to "Application Gateway balances web traffic in one region; it does not connect offices.",
                    ),
                ),
                // Network optimisation and security
                B.q(
                    "sa-n20", T,
                    "A TCP game protocol must be spread across VMs in one region with the lowest latency. Which service?",
                    listOf("Azure Load Balancer", "Application Gateway", "Azure Front Door"), setOf(0),
                    "Non-web traffic in one region is the Load Balancer branch of the decision tree: layer 4 and very low latency.",
                    whyWrong = mapOf(
                        1 to "Application Gateway is built for web traffic; its TCP proxy ends and reopens every connection, while Load Balancer passes packets straight through.",
                        2 to "Front Door handles only web traffic and is global, not regional.",
                    ),
                ),
                B.q(
                    "sa-n21", T,
                    "A non-HTTP service runs in three regions and on-premises, and users should reach the closest healthy one. Which service?",
                    listOf("Traffic Manager with performance routing", "Application Gateway", "A network security group"), setOf(0),
                    "Traffic Manager works through DNS, so it handles any protocol and any endpoint, including on-premises, across regions.",
                    whyWrong = mapOf(
                        1 to "Application Gateway is regional and handles web traffic only.",
                        2 to "An NSG filters traffic; it never chooses where traffic goes.",
                    ),
                ),
                B.q(
                    "sa-n22", T,
                    "Admins must manage VMs over RDP without any VM having a public IP or port 3389 open to the internet. What do you deploy?",
                    listOf("Azure Bastion", "An NSG rule allowing 3389 from any source", "Azure Front Door"), setOf(0),
                    "Bastion gives browser-based RDP and SSH over TLS from the portal, so VMs keep only private IPs.",
                    whyWrong = mapOf(
                        1 to "That opens RDP to the whole internet, the exact risk to remove.",
                        2 to "Front Door delivers web apps; it does not carry admin RDP sessions.",
                    ),
                ),
                B.q(
                    "sa-n23", T,
                    "Outbound traffic from spokes must be filtered by domain name, such as allowing only *.twotracks.app, with central logging. Which service?",
                    listOf("Azure Firewall", "A network security group", "A Web Application Firewall"), setOf(0),
                    "Azure Firewall filters by FQDN, applies threat intelligence and logs centrally, usually in a hub with UDRs pointing at it.",
                    whyWrong = mapOf(
                        1 to "An NSG works with IPs, ports and service tags; it cannot filter by domain name.",
                        2 to "A WAF protects inbound web apps; it does not control outbound traffic.",
                    ),
                ),
                B.q(
                    "sa-n24", T,
                    "A storage account must be reachable only from the Two Tracks virtual network, through a private IP, with public access off. What do you use?",
                    listOf("A private endpoint", "A NAT gateway", "DDoS IP Protection"), setOf(0),
                    "A private endpoint gives the storage account a private IP in your virtual network, so public network access can be disabled.",
                    whyWrong = mapOf(
                        1 to "A NAT gateway handles outbound internet access; it does not make a service private.",
                        2 to "DDoS protection absorbs attack floods on public IPs; it does not remove the public endpoint.",
                    ),
                ),
                B.q(
                    "sa-n25", T,
                    "Two VM tiers exchange many small messages and need the lowest possible latency between them. Which two settings help most?",
                    listOf("Accelerated networking and a proximity placement group", "Traffic Manager and a NAT gateway", "A WAF and DDoS Network Protection"), setOf(0),
                    "Accelerated networking skips the host's software switch, and a proximity placement group keeps the VMs physically close.",
                    whyWrong = mapOf(
                        1 to "Neither changes the path between two VMs in the same region.",
                        2 to "Both are security features; they add no speed between VMs.",
                    ),
                ),
                B.q(
                    "sa-n26", T,
                    "Rules must allow web servers to reach API servers on 443 without listing IP addresses, even as VMs come and go. What do you use?",
                    listOf("Application security groups in NSG rules", "A user-defined route", "Azure Front Door"), setOf(0),
                    "Application security groups tag VMs by role, and an NSG rule can name them as source and destination.",
                    whyWrong = mapOf(
                        1 to "A route changes where traffic goes; it does not allow or deny it.",
                        2 to "Front Door sits at the internet edge; it does not filter traffic between tiers.",
                    ),
                ),
            ),
        ),
    )
}
