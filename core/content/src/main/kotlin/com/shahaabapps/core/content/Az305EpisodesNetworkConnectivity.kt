package com.shahaabapps.core.content

/** Networks: reaching the internet and on-premises: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesNetworkConnectivity {
    private const val T = "sa-network-connectivity"

    private val classic = Podcast(
        T,
        listOf(
            maya("Hi, and welcome back. Today is networks: how your machines in Azure reach the internet, your office, and each other. Our story is Two Tracks goes global: the Android app is now a company, with a backend on Azure, users in Europe and Australia, and an old office system. [pause] It's a design exam, so we learn to choose."),
            leo("Choose. Fine. But start at the bottom. What is a network, in plain words?"),
            maya("Computers joined so they can send each other messages. Each one has an IP address, which is its street address, so a message knows where to go. A private address is like a room number inside one building: it means nothing to the outside world."),
            leo("And a virtual network? Everyone says VNet."),
            maya("A virtual network, VNet for short, is your own private floor inside Azure's huge building. Nobody else's machines are on your floor. The machines on it talk to each other freely, and nothing gets in or out unless you add a door."),
            leo("Machines meaning what, exactly?"),
            maya("For example, a VM, a virtual machine. That's a computer you rent in Azure's data centre by the hour. The Two Tracks backend runs on a few of them."),
            leo("Got it. Then what's a subnet?"),
            maya("A subnet is a room on your floor. You split the VNet into rooms, say one for the app machines and one for the databases, so each room can have its own rules."),
            leo("And how big is the floor?"),
            maya("That's the address space: the block of private addresses the VNet owns, like the range of room numbers in a building. Design rule: two networks with the same numbers can never be joined later."),
            leo("Noted. Now the Two Tracks backend has to get out. How does it reach the internet?"),
            maya("Through a way out. Outbound just means traffic leaving your floor. Azure is phasing out the old hidden way out, so new networks get private subnets, with no exit until you add one."),
            leo("So what's the recommended door?"),
            maya("A NAT gateway. Picture a company post room. Every letter going out gets the company's return address stamped on it, not the sender's desk. NAT means network address translation: swapping each machine's private address for one shared public address."),
            leo("Public address meaning what?"),
            maya("A public IP is a street address the whole internet can see and reply to. The NAT gateway holds one or more fixed ones, and every VM in the subnet leaves through them."),
            leo("Why would I care that it's fixed?"),
            maya("Say you're building the payment step. The Two Tracks payment partner has an API, which is the door their software opens for other programs to call. And that door only accepts calls from addresses on its list. Ten VMs, one known address: a NAT gateway on their subnet."),
            leo("Couldn't I give each VM its own public IP?"),
            maya("You could, but then you have ten addresses to list, and ten machines strangers can knock on. A public IP is for one VM that must accept calls from outside."),
            leo("Okay. Now Two Tracks has users in Europe and Australia. Does that change the network?"),
            maya("It means two regions. A region is a group of Azure data centres in one part of the world, like West Europe or Australia East. Each region gets its own VNets, and they need joining."),
            leo("Joining how?"),
            maya("With VNet peering. Picture a private corridor between two floors. Traffic between them runs over Microsoft's own cables and never touches the internet. Global peering is the same corridor between regions."),
            leo("Great, so I peer everything to everything?"),
            maya("There's a trap. Peering is not transitive. Transitive means it passes things along. If A is peered to B, and B to C, A still cannot reach C. A friend of your friend is not your friend."),
            leo("[chuckle] Harsh, but true. So how do big companies cope?"),
            maya("Hub and spoke. Picture a bicycle wheel. Shared things, such as the door to the office, sit in the hub, a VNet in the middle. Each workload gets its own spoke VNet, peered only to the hub."),
            leo("And if two spokes must talk to each other?"),
            maya("Peer them directly, or send their traffic through a firewall in the hub. A firewall is a security guard for traffic, checking what may pass between networks. A route tells the spokes to go through it."),
            leo("What if there are dozens of offices and regions?"),
            maya("Then Azure Virtual WAN. WAN means wide area network: one network stretching across many sites. With Virtual WAN, Microsoft runs the hubs for you, and every site, spoke and region can reach every other."),
            leo("Now the old office. Two Tracks still has a system in its own building. How does Azure reach it?"),
            maya("That's called on-premises: your own computers, in your own building. Two choices. First, a VPN, a virtual private network. It's a sealed tunnel through the public internet, like a locked van driving on public roads."),
            leo("Locked van. And where does the van park?"),
            maya("At a VPN gateway in Azure, usually in the hub. Site-to-site VPN joins the whole office router to that gateway. Point-to-site is for single laptops, for example developers working from home, each building its own tunnel."),
            leo("And the second choice?"),
            maya("ExpressRoute. A private line from your network to Microsoft, through a telecoms provider. Picture your own private road. Nobody else drives on it, so it's fast and steady."),
            leo("Steady how? Doesn't everything just take as long as it takes?"),
            maya("That's latency: the delay before a message arrives, like the wait between posting a letter and it landing. On the internet, latency jumps around with the traffic. ExpressRoute's is predictable. But it takes weeks to order, and it's private, not encrypted by default."),
            leo("So VPN is the quick cheap one, and ExpressRoute the serious one?"),
            maya("Exactly. A VPN is ready in hours, but rides the internet. The best of both is ExpressRoute with a site-to-site VPN as failover. Failover means a backup takes over when the main path breaks."),
            leo("Now the database. Two Tracks keeps its data in Azure SQL. Isn't that just out on the internet?"),
            maya("By default it has a public address, yes. It's PaaS, platform as a service, which means Azure runs the machine and you just use the database. To keep it private, you give it a private endpoint."),
            leo("Endpoint. What's that, in plain words?"),
            maya("An endpoint is the address you call to reach a service. A private endpoint gives one database an address inside your own subnet, like an extension line on your own office switchboard. Then you switch its public access off."),
            leo("And how does my app find that extension?"),
            maya("Through DNS, the internet's phone book, which turns a name into an address. You add a Private DNS zone: a phone book only your linked VNets can read, so the database's name points to the private address. Forget it, and the name still finds the public one."),
            leo("Isn't there a free version? I heard service endpoint."),
            maya("A service endpoint lets one subnet reach the service over Microsoft's cables, and the service lets that subnet in. But the service keeps its public address, and it never works from the office. Office over ExpressRoute to the database? Private endpoint."),
            leo("So what does the whole Two Tracks design look like?"),
            maya("A hub and spoke in each region, hubs peered. ExpressRoute plus a VPN into Europe. NAT gateways for the payment partner. Private endpoints for every database."),
            leo("So what do I remember for the exam?"),
            maya("If the question says fixed outbound addresses for many VMs, the answer is a NAT gateway. If spokes peered to one hub must talk, remember peering is not transitive. If predictable latency or no internet path, ExpressRoute, with a VPN as failover. If single laptops, point-to-site. If a database is reached privately, even from the office, a private endpoint with a Private DNS zone."),
            leo("And anything to try today?"),
            maya("Create a hub VNet and a spoke VNet with different address ranges. Peer them in both directions, and check both show Connected. Add a NAT gateway to the spoke subnet. Start a small VM there, and check its outbound address matches the NAT gateway IP. Then delete the resource group, the folder holding it all."),
            leo("Different room numbers, one shared door, and no friends of friends. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        T,
        listOf(
            maya("Welcome to Case Files. Today's case: The Corridor That Went Nowhere. Two Tracks has gone global. The Android app is now a company, with a backend on Azure, users in Europe and Australia, and an old office system. In launch week, four things broke. Every one was a design choice."),
            leo("Detective Leo, reporting. Before I suspect anyone, I need the building plan. What's a network here?"),
            maya("Computers joined so they can send each other messages. Each has an IP address, which is its street address on the network. A private address is like a room number: it only means something inside your own building."),
            leo("And the building in Azure?"),
            maya("A virtual network, a VNet. That's your own private floor in Azure's huge building. Under the picture, it's a block of private addresses Azure keeps apart from every other customer's, and the machines on it talk freely."),
            leo("Machines being?"),
            maya("VMs, virtual machines: computers you rent in Azure's data centre by the hour. And the floor is split into subnets, which are rooms, say one for the app and one for the data, each with its own rules."),
            leo("Floors, rooms. Now the first crime."),
            maya("Two Tracks runs in two regions. A region is a group of Azure data centres in one part of the world: West Europe for European users, Australia East for Australian ones. Each region got its own VNets."),
            leo("And the crime?"),
            maya("The design was a hub and spoke. Picture a bicycle wheel: shared things in the middle VNet, the hub, and each workload in its own spoke VNet, joined only to the hub. The API spoke could not reach the data spoke."),
            leo("API. Remind me what that is."),
            maya("An API is the door a piece of software opens for other programs to call. The Two Tracks app calls its backend through one. And that door couldn't reach the database next door."),
            leo("Wait, how are two VNets joined at all?"),
            maya("By VNet peering, a private corridor between two floors. How it works: the two address blocks are linked over Microsoft's own cables, so traffic never touches the internet. Global peering does the same between regions."),
            leo("First suspect, then. Different regions. Peering doesn't cross the sea."),
            maya("[pause] Good guess, wrong suspect. Global peering crosses regions fine. Look at the clue: both spokes were peered to the hub, but not to each other."),
            leo("So the hub should pass the traffic along. Like a good receptionist."),
            maya("That's exactly the trap. Peering is not transitive, meaning it never passes traffic along. A to B and B to C does not give A to C. A friend of a friend is not your friend."),
            leo("[sigh] So how do the spokes talk?"),
            maya("Three fixes. Peer the two spokes directly. Or put a firewall in the hub, a security guard for traffic, and send spoke traffic through it with a route. Or, for many sites, Azure Virtual WAN, where Microsoft runs the hubs and every site reaches every other."),
            leo("Case one closed. Case two?"),
            maya("The payment partner. Their API only accepts calls from addresses on a list. Every Two Tracks payment call was refused."),
            leo("Suspect: the VMs had no way out at all. Give each one a public IP."),
            maya("Half right about the cause. New Azure networks get private subnets by default, with no hidden way out, so these VMs needed a door. A public IP is a street address the whole internet can see. But ten of them means ten addresses to list, and ten machines open to strangers."),
            leo("So what's the door?"),
            maya("A NAT gateway on their subnet. NAT means network address translation. Picture the company post room stamping every outgoing letter with one return address. Under the hood, it swaps each VM's private address for its fixed public one, and remembers each conversation so the reply finds the right VM."),
            leo("One known address for the partner's list. Case two closed. Three?"),
            maya("The office. Two Tracks still has a system on-premises, which means in its own building, on its own computers. The design used a site-to-site VPN to reach Azure."),
            leo("VPN. Translate, please."),
            maya("A virtual private network: a sealed tunnel through the public internet, like a locked van on public roads. Site-to-site joins the whole office router to a VPN gateway in the hub. Point-to-site is for single laptops, for example developers at home."),
            leo("And what broke?"),
            maya("The nightly data transfer. It needed a steady speed and predictable latency. Latency is the delay before a message arrives, like the wait for a letter. Over the internet it jumped about every night."),
            leo("Suspect: one tunnel is too few. Add a second VPN."),
            maya("That's the distractor. Every VPN rides the internet, so latency is never predictable, however many vans you add. The words predictable latency point to ExpressRoute: a private line to Microsoft through a telecoms provider, like your own private road."),
            leo("And where does ExpressRoute stop working?"),
            maya("It takes weeks to order, and it's private but not encrypted by default. And a single line can fail. So the fix is ExpressRoute with a site-to-site VPN as failover, a backup that takes over. Both gateways sit in the same VNet, and Azure prefers the private road."),
            leo("Case four. I can feel it."),
            maya("The database. Two Tracks keeps its data in Azure SQL, which is PaaS, platform as a service: Azure runs the machine, you just use the database. The rule said office staff reach it over ExpressRoute, and never from the internet."),
            leo("Suspect: they used a service endpoint."),
            maya("Sharp. An endpoint is the address you call to reach a service. A service endpoint lets one Azure subnet reach the service over Microsoft's cables, but the service keeps its public address, and it never works from the office."),
            leo("So the right answer is a private endpoint?"),
            maya("Yes. It gives that one database an address inside your subnet, like an extension line on your own switchboard. The office reaches it over ExpressRoute, and public access is switched off. But they made a second mistake."),
            leo("Of course they did."),
            maya("No Private DNS zone. DNS is the internet's phone book, turning a name into an address. A Private DNS zone is a phone book only linked VNets can read. Without it, the database's name still pointed to the public address, and the calls bounced."),
            leo("One more clue in the file. Overlapping something?"),
            maya("The address space, the range of room numbers a VNet owns. A test VNet reused the office's numbers. Two buildings with the same room numbers can never be joined, so it had to be rebuilt. Plan ranges that never overlap."),
            leo("And the load balancers and firewalls out front?"),
            maya("That's the network security and load balancing episode, next door. This case was about getting connected."),
            leo("So, the case summary for the exam?"),
            maya("If the question says fixed outbound addresses for many VMs, a NAT gateway. If spokes peered to a hub must talk, peering is not transitive: peer them, route through the hub's firewall, or Virtual WAN. If predictable latency or no internet path, ExpressRoute with VPN failover. If single laptops, point-to-site. If a database is reached privately from the office, a private endpoint and a Private DNS zone."),
            leo("Case closed. Anything to try today?"),
            maya("Rebuild the scene. Create a hub VNet and a spoke VNet with different address ranges. Peer them in both directions and check both show Connected. Add a NAT gateway to the spoke subnet, start a small VM there, and check its outbound address matches the NAT gateway IP. Then delete the resource group."),
            leo("Different room numbers, and one shared door. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        T,
        listOf(
            maya("Did you hear what happened on the Two Tracks network team this week? Grab a coffee. The Android app is a real company now, going global, with a backend on Azure and users in Europe and Australia. And the network? Five people, five stories."),
            leo("[laugh] I heard there was a whiteboard incident. But start me at zero. What's a network, in plain words?"),
            maya("Computers joined so they can message each other. Each has an IP address, its street address on the network. A private address is like a room number: it only means something inside your own building."),
            leo("And Azure's building?"),
            maya("A virtual network, VNet for short: your own private floor in Azure's huge building. Under the picture, it's a block of private addresses kept apart from everyone else's. Machines on it talk freely; nothing gets in or out without a door."),
            leo("Machines meaning?"),
            maya("VMs, virtual machines, which are computers rented in Azure's data centre by the hour. And the floor is split into subnets, rooms such as app and data, each with its own rules."),
            leo("Okay. Who's first?"),
            maya("Priya, Monday. She made the test VNet. Its address space, which is the range of room numbers a VNet owns, was the very same as the office network's."),
            leo("Is that bad? Numbers are numbers."),
            maya("Two buildings with the same room numbers can never share one post room. Networks with overlapping ranges can never be joined. Priya rebuilt it on Tuesday. Lesson: plan ranges that never overlap, before you build."),
            leo("Poor Priya. Tuesday?"),
            maya("Tom. Two Tracks has two regions. A region is a group of Azure data centres in one part of the world: West Europe and Australia East. Tom built a hub and spoke. Picture a bicycle wheel: shared things in the middle VNet, the hub, and workloads in spoke VNets around it."),
            leo("Sounds tidy. What went wrong?"),
            maya("Each spoke was peered to the hub. VNet peering is a private corridor between two floors, over Microsoft's own cables, never the internet. Global peering crosses regions. Tom expected the app spoke to reach the data spoke through the hub."),
            leo("And it didn't?"),
            maya("Peering is not transitive, meaning it never passes traffic along. A friend of a friend is not your friend. Tom glared at the whiteboard, then sent spoke traffic through a firewall in the hub, a security guard that checks traffic, using a route."),
            leo("[chuckle] The whiteboard incident. And if there were dozens of sites?"),
            maya("Then Azure Virtual WAN, where Microsoft runs the hubs and every site reaches every other. Wednesday was Dana."),
            leo("Go on."),
            maya("Dana linked the office. That's on-premises, the company's own building and computers. She chose a site-to-site VPN. A VPN is a virtual private network: a sealed tunnel through the public internet, like a locked van on public roads."),
            leo("Sounds sensible."),
            maya("Usually. But the requirement said predictable latency. Latency is the delay before a message arrives, like waiting for the post. On the internet it wobbles. Dana's nightly transfer kept missing its window."),
            leo("So what should she have picked?"),
            maya("ExpressRoute: a private line to Microsoft through a telecoms provider, like your own private road. Steady, because nobody else drives on it. Where it stops: it takes weeks to order, and it's private but not encrypted by default."),
            leo("And if the private road breaks?"),
            maya("Keep Dana's VPN as failover, the backup that takes over. Both gateways sit in one VNet, and Azure prefers ExpressRoute. So Dana was half right, and she's telling everyone."),
            leo("Thursday?"),
            maya("Marcus. The database, Azure SQL, is PaaS, platform as a service: Azure runs the machine, you just use the database. The rule was no public access. So Marcus made a private endpoint."),
            leo("Endpoint. What's that, in plain words?"),
            maya("An endpoint is the address you call to reach a service. A private endpoint gives one database an address inside your subnet, like an extension line on your own switchboard. Then you turn its public access off."),
            leo("Sounds perfect. Where's the gossip?"),
            maya("He forgot the Private DNS zone. DNS is the internet's phone book: name in, address out. A Private DNS zone is a phone book only linked VNets can read. Without it, the name still pointed at the public address, and every call failed."),
            leo("[sigh] So close. Friday?"),
            maya("The intern. Bless them. The office had to reach a storage account, and the intern used a service endpoint. That lets an Azure subnet reach the service over Microsoft's cables, and the service lets that subnet in."),
            leo("Free, I heard. What's wrong with it?"),
            maya("The service keeps its public address, and it only works from subnets inside Azure. The office isn't one. Office traffic means a private endpoint, every time."),
            leo("Did anyone get anything right?"),
            maya("The best gossip. The payment partner's API, the door their software opens for other programs, only accepts calls from known addresses. And new networks get private subnets, with no way out until you add one. So Priya, back from Monday, added a NAT gateway."),
            leo("Which is?"),
            maya("A shared exit for a whole subnet, with fixed public addresses. NAT means network address translation. Picture the post room stamping every letter with the company's return address. Under the hood, it swaps each VM's private address for the public one, and remembers each conversation so replies come home."),
            leo("Redemption. And people working from home?"),
            maya("Point-to-site VPN: each laptop builds its own tunnel to the VPN gateway. Site-to-site joins a whole office router. And next week's drama, load balancers and firewalls, is in the network security and load balancing episode."),
            leo("So, the gossip I need for the exam?"),
            maya("If the question says fixed outbound addresses for many VMs, a NAT gateway. If spokes peered to a hub must talk, peering is not transitive. If predictable latency or no internet path, ExpressRoute, with a VPN as failover. If single laptops, point-to-site. If a database is reached privately, even from the office, a private endpoint plus a Private DNS zone."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a hub VNet and a spoke VNet with different address ranges. Peer them in both directions and check both show Connected. Add a NAT gateway to the spoke subnet. Start a small VM there and check its outbound address matches the NAT gateway IP. Then delete it all."),
            leo("Different numbers, one door, no friends of friends. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        T,
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about how things find each other in the dark. Networks."),
            ivy("Mm. Finding each other in the dark. I'm listening, Nova."),
            nova("A network is just computers joined so they can send each other messages. Each one has an IP address, which is its street address, so a message knows where to go. [pause] A private address is like a room number: it only means something inside your own building."),
            ivy("And in Azure, whose building is it?"),
            nova("Azure's. But you get your own floor. That's a virtual network, a VNet. [pause] Under the picture, it's a block of private addresses Azure keeps apart from everyone else's. On your floor, machines talk freely. Nothing comes in or goes out unless you add a door."),
            ivy("Machines. Tell me what kind."),
            nova("VMs, virtual machines, which are computers you rent in Azure's data centre by the hour. Tonight's story is Two Tracks goes global: the Android app is a company now, with a backend on Azure and users in Europe and Australia."),
            ivy("Global. Fancy. And the rooms on the floor?"),
            nova("Subnets. A subnet is a room: one for the app, one for the data, each with its own rules. [pause] And the address space is the range of room numbers the floor owns. Just one rule. Never let two networks share numbers, or they can never be joined."),
            ivy("Noted. Now, the backend wants to go out. Into the night."),
            nova("[chuckle] It needs a door. New networks get private subnets by default now, with no way out until you add one. The recommended door is a NAT gateway."),
            ivy("Which is?"),
            nova("Picture a post room at the office. Every letter leaving gets one return address stamped on it, the company's, not the sender's desk. NAT means network address translation. Under the hood, the gateway swaps each VM's private address for a fixed public one, and remembers each conversation, so replies find their way home."),
            ivy("And why would I want one fixed address?"),
            nova("Because the payment partner is choosy. Their API, the door their software opens for other programs, only answers addresses on its list. [pause] Ten VMs, one known address. A public IP on each VM would mean ten addresses, and ten machines strangers can knock on."),
            ivy("Choosy partners. I respect that. Now, two continents. How do they meet?"),
            nova("Two regions. A region is a group of Azure data centres in one part of the world, like West Europe or Australia East. [pause] You join their VNets with peering: a private corridor between two floors, over Microsoft's own cables. Global peering reaches across regions."),
            ivy("So everyone who's peered can wander everywhere?"),
            nova("No, and this is the part to remember. Peering is not transitive, which means it never passes traffic along. A to B and B to C, and still A can't reach C. A friend of a friend is not your friend, however late it gets."),
            ivy("Mm. So how do the big ones keep it tidy?"),
            nova("Hub and spoke. Picture a bicycle wheel. Shared things, like a firewall, a security guard for traffic, sit in the middle VNet, the hub. Each workload has a spoke, peered only to the hub. For dozens of sites, Azure Virtual WAN, where Microsoft runs the hubs for you."),
            ivy("And the old office? It's still out there, all alone."),
            nova("On-premises, we call it: your own building, your own computers. [pause] One way in is a VPN, a virtual private network. A sealed tunnel through the public internet, like a locked van on public roads. Site-to-site joins the whole office; point-to-site is for one laptop at a time."),
            ivy("A locked van at night. Very romantic. And the other way?"),
            nova("ExpressRoute. A private line to Microsoft through a telecoms provider, like your own private road. Nobody else is on it, so the delay stays steady. That delay is called latency: the little wait between sending your text and it arriving."),
            ivy("I know that wait. [pause] So where does the private road stop working?"),
            nova("It takes weeks to order, and it's private, not encrypted by default. And one road can close. So the best of both is ExpressRoute with a site-to-site VPN as failover, a backup that takes over. Azure prefers the private road."),
            ivy("And the database? Where do the secrets sleep?"),
            nova("In Azure SQL, which is PaaS, platform as a service: Azure runs the machine, you just use the database. [pause] By default it has a public address. To keep it private, you give it a private endpoint."),
            ivy("Endpoint. Say it slowly. What is that, in plain words?"),
            nova("An endpoint is the address you call to reach a service. A private endpoint gives one database an address inside your own subnet, like an extension line on your own switchboard. Then you switch its public access off. The office can reach it too, over ExpressRoute."),
            ivy("And how does my app find the extension?"),
            nova("Through DNS, the phone book that turns a name into an address. You add a Private DNS zone, a phone book only your linked VNets can read. [pause] Forget it, and the name still finds the public address."),
            ivy("And the free one? A service endpoint?"),
            nova("It lets one Azure subnet reach the service over Microsoft's cables. But the service keeps its public address, and it never works from the office. That's where it stops. Load balancers and firewalls wait for you in the network security and load balancing episode."),
            ivy("[sigh] Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says fixed outbound addresses for many VMs, a NAT gateway. If spokes peered to a hub must talk, peering is not transitive. If predictable latency or no internet, ExpressRoute, with a VPN as failover. If single laptops, point-to-site. If a database is reached privately, even from the office, a private endpoint with a Private DNS zone."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a hub VNet and a spoke VNet with different address ranges. Peer them in both directions and check both show Connected. [pause] Add a NAT gateway to the spoke subnet. Start a small VM there and check its outbound address matches the NAT gateway IP. Then delete it all."),
            ivy("One shared door, and no friends of friends. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        T,
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about connections."),
            rafe("Ours?"),
            nova("[laugh] Networks. How machines in Azure find each other, reach out, and keep things private. Our story is Two Tracks goes global: the Android app is a company now, with a backend on Azure and users in Europe and Australia."),
            rafe("Then start at the beginning. What's a network, in plain words?"),
            nova("Computers joined so they can send each other messages. Each has an IP address, which is its street address. A private address is like a room number: it only means something inside your own building."),
            rafe("And in Azure, where do we get a room?"),
            nova("First you get a floor: a virtual network, VNet for short. It's your own private floor in Azure's big building. Under the picture, it's a block of private addresses kept apart from everyone else's. And subnets are the rooms on it."),
            rafe("A private floor. Nobody else's machines on it?"),
            nova("Nobody's. Just our VMs, virtual machines, which are computers rented by the hour. And one rule for the floor: its address space, the range of room numbers it owns, must never overlap another network's, or the two can never be joined."),
            rafe("Never overlap. Unlike us on the sofa. Now how does our backend get out for the night?"),
            nova("Through a door. New networks get private subnets by default, with no way out until you add one. The recommended door is a NAT gateway: one shared exit for a whole subnet, with fixed public addresses."),
            rafe("Public address being?"),
            nova("A public IP: a street address the whole internet can see and reply to. NAT means network address translation. Picture a post room stamping one return address on every letter. It swaps each VM's private address for the public one, and remembers who said what, so replies come home."),
            rafe("Then let me test you, darling. Exam style. Ten VMs with no public IPs must call a partner's API, the door their software opens for others, which only accepts one known address. What do you add?"),
            nova("A NAT gateway on their subnet. One shared door, one known address."),
            rafe("Right answer. One address on the partner's list, and only one for me. [kiss]"),
            nova("Mm. Two continents, though. Two regions, meaning two groups of Azure data centres, one in Europe and one in Australia. How do their VNets meet?"),
            rafe("With VNet peering, a private corridor between two floors, over Microsoft's own cables. Global peering crosses regions. No internet in between."),
            nova("Lovely. And hub and spoke is a bicycle wheel: shared things in the middle VNet, the hub, and each workload in a spoke peered to it. So, my question. Spoke A and spoke B are both peered to the hub. Can A reach B?"),
            rafe("Of course. The hub passes it along."),
            nova("No kiss for that one. Peering is not transitive: it never passes traffic along. A friend of a friend is not your friend. Peer A and B directly, or route through a firewall in the hub, a guard that checks traffic, or use Virtual WAN, where Microsoft runs the hubs."),
            rafe("Harsh. Fine, my turn to impress you. What about the old office?"),
            nova("On-premises, darling: your own building, your own computers. Tell me the two ways in."),
            rafe("A VPN, a virtual private network: a sealed tunnel through the public internet, like a locked van on public roads. Or ExpressRoute: a private line to Microsoft through a telecoms provider, your own private road."),
            nova("And the difference that matters?"),
            rafe("The delay. Latency is the delay before a message arrives, like the wait for a reply to your text. On a VPN it wobbles with the internet. On ExpressRoute it's steady and predictable."),
            nova("So. Nightly transfers need a steady speed and predictable latency, and a backup path if the line fails. What do you recommend?"),
            rafe("ExpressRoute, with a site-to-site VPN as failover, the backup that takes over. Azure prefers the private road."),
            nova("Right answer. Steady, with a backup plan. Very attractive. [kiss]"),
            rafe("And where does ExpressRoute stop working?"),
            nova("It takes weeks to order, and it's private but not encrypted by default. Now a quick one. Thirty developers work from home, and there's no office router. Which VPN?"),
            rafe("Point-to-site. Each laptop builds its own tunnel to the VPN gateway. Site-to-site is for a whole office router."),
            nova("Right answer. Everyone gets their own private tunnel. [kiss]"),
            rafe("Now secrets. Our database, Azure SQL, is PaaS, platform as a service: Azure runs the machine, we just use it. How do we keep it private?"),
            nova("With a private endpoint. An endpoint is the address you call to reach a service. A private endpoint gives one database an address inside your own subnet, like an extension line on our own switchboard. Then public access goes off."),
            rafe("Discreet. And how does the app find the extension?"),
            nova("DNS, the phone book that turns a name into an address. You add a Private DNS zone, a phone book only linked VNets can read. Without it, the name still finds the public address."),
            rafe("Then here's your question. Office staff must reach the database over ExpressRoute, and it must never be reachable from the internet. Service endpoint or private endpoint?"),
            nova("Private endpoint, with public access off. A service endpoint only works from subnets inside Azure, and the service keeps its public address. The office isn't an Azure subnet."),
            rafe("Right answer. Private, and reachable from the office. [kiss]"),
            nova("And for the rest of the night, firewalls and load balancers, there's the network security and load balancing episode."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Fixed outbound addresses for many VMs: a NAT gateway. Spokes peered to a hub that must talk: peering is not transitive. Predictable latency or no internet path: ExpressRoute, with a VPN as failover. Single laptops: point-to-site. A database reached privately, even from the office: a private endpoint with a Private DNS zone."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And what shall we try today, darling?"),
            rafe("Create a hub VNet and a spoke VNet with different address ranges. Peer them in both directions and check both show Connected. Add a NAT gateway to the spoke subnet. Start a small VM there and check its outbound address matches the NAT gateway IP. Then delete it all."),
            nova("Never overlapping, always connected. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun floor() = DiagramSpec(
        "A virtual network is your private floor in Azure, split into subnets, the rooms, with a range of numbers of its own.",
        nodes = listOf(
            DNode("vnet", "Virtual network", 0.5f, 0.14f, w = 0.8f, h = 0.18f, tone = 1, sub = "your private floor"),
            DNode("app", "Subnet: app", 0.25f, 0.46f, w = 0.42f, h = 0.2f, sub = "a room"),
            DNode("data", "Subnet: data", 0.75f, 0.46f, w = 0.42f, h = 0.2f, sub = "a room"),
            DNode("vm", "VM", 0.25f, 0.82f, w = 0.42f, h = 0.2f, sub = "a rented computer"),
            DNode("range", "Address space", 0.75f, 0.82f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "never overlap"),
        ),
        edges = listOf(
            DEdge("vnet", "app", "room"),
            DEdge("vnet", "data", "room"),
            DEdge("app", "vm", "runs in"),
        ),
        aspect = 0.8f,
    )

    private fun wayOut() = DiagramSpec(
        "VMs with no public IP leave through one NAT gateway, so the partner sees a single fixed address.",
        nodes = listOf(
            DNode("vms", "VMs", 0.25f, 0.18f, w = 0.42f, h = 0.2f, sub = "no public IP"),
            DNode("nat", "NAT gateway", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "one shared door"),
            DNode("partner", "Payment partner", 0.25f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "known addresses only"),
            DNode("net", "Internet", 0.75f, 0.76f, w = 0.42f, h = 0.28f, kind = NodeKind.CLOUD, tone = 2),
        ),
        edges = listOf(
            DEdge("vms", "nat", "outbound"),
            DEdge("nat", "net", "fixed IP"),
            DEdge("net", "partner", "calls"),
        ),
        aspect = 0.8f,
    )

    private fun peer() = DiagramSpec(
        "Each spoke is peered to the hub, but peering is not transitive, so the spokes cannot reach each other through it.",
        nodes = listOf(
            DNode("hub", "Hub VNet", 0.5f, 0.16f, w = 0.5f, h = 0.2f, tone = 1, sub = "shared things"),
            DNode("a", "Spoke: API", 0.22f, 0.66f, w = 0.4f, h = 0.2f),
            DNode("b", "Spoke: data", 0.78f, 0.66f, w = 0.4f, h = 0.2f),
            DNode("wan", "Many sites? Virtual WAN", 0.5f, 0.91f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("a", "hub", "peering", bidirectional = true),
            DEdge("hub", "b", "peering", bidirectional = true),
            DEdge("a", "b", "not transitive", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun hybrid() = DiagramSpec(
        "The office reaches the hub by ExpressRoute, a private road, with a site-to-site VPN as failover; laptops use point-to-site.",
        nodes = listOf(
            DNode("office", "Office", 0.2f, 0.16f, w = 0.36f, h = 0.2f, tone = 2, sub = "on-premises"),
            DNode("hub", "Hub VNet", 0.8f, 0.16f, w = 0.36f, h = 0.2f, tone = 1, sub = "the gateways"),
            DNode("er", "ExpressRoute", 0.5f, 0.5f, w = 0.5f, h = 0.2f, sub = "private road"),
            DNode("vpn", "Site-to-site VPN", 0.5f, 0.84f, w = 0.5f, h = 0.2f, sub = "locked van, failover"),
            DNode("laptop", "Laptops", 0.12f, 0.84f, w = 0.18f, h = 0.2f, kind = NodeKind.USER),
        ),
        edges = listOf(
            DEdge("office", "er"),
            DEdge("er", "hub"),
            DEdge("office", "vpn"),
            DEdge("vpn", "hub"),
            DEdge("laptop", "hub", "point-to-site", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun privateLink() = DiagramSpec(
        "A private endpoint gives the database an address in your subnet, and a Private DNS zone points its name there.",
        nodes = listOf(
            DNode("app", "Two Tracks API", 0.25f, 0.16f, w = 0.42f, h = 0.2f, sub = "in a spoke"),
            DNode("dns", "Private DNS zone", 0.75f, 0.16f, w = 0.42f, h = 0.2f, tone = 1, sub = "inside phone book"),
            DNode("se", "Service endpoint: Azure subnets only", 0.5f, 0.46f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
            DNode("pe", "Private endpoint", 0.25f, 0.78f, w = 0.42f, h = 0.2f, tone = 1, sub = "extension line"),
            DNode("sql", "Azure SQL", 0.75f, 0.78f, w = 0.42f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "public access off"),
        ),
        edges = listOf(
            DEdge("app", "dns", "name?"),
            DEdge("app", "pe", "calls"),
            DEdge("pe", "sql", "private"),
        ),
        aspect = 0.8f,
    )

    private fun design() = DiagramSpec(
        "Two Tracks goes global: a hub and spoke per region, the office on ExpressRoute plus VPN, NAT for the way out.",
        nodes = listOf(
            DNode("office", "Office", 0.17f, 0.16f, w = 0.3f, h = 0.2f, tone = 2),
            DNode("hub", "Hub VNet", 0.5f, 0.16f, w = 0.3f, h = 0.2f, tone = 1, sub = "gateways"),
            DNode("net", "Internet", 0.84f, 0.16f, w = 0.3f, h = 0.26f, kind = NodeKind.CLOUD, tone = 2),
            DNode("api", "Spoke: API", 0.27f, 0.76f, w = 0.42f, h = 0.2f, sub = "NAT gateway"),
            DNode("data", "Spoke: data", 0.75f, 0.76f, w = 0.42f, h = 0.2f, sub = "private endpoints"),
        ),
        edges = listOf(
            DEdge("office", "hub", "ExpressRoute + VPN", bidirectional = true),
            DEdge("hub", "api", "peering", bidirectional = true),
            DEdge("hub", "data", "peering", bidirectional = true),
            DEdge("api", "net", "NAT"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's network questions, each as the requirement to listen for and the feature it points to.",
        nodes = listOf(
            DNode("nat", "Fixed outbound IPs", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "NAT gateway"),
            DNode("peer", "Spokes must talk", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "not transitive"),
            DNode("er", "Predictable latency", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "ExpressRoute + VPN"),
            DNode("p2s", "Single laptops", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "point-to-site"),
            DNode("pe", "Private, from office", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "private endpoint + DNS"),
            DNode("range", "New VNets", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ranges never overlap"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("nat", "peer", "er", "p2s", "pe", "range")
    private val listen = BoardCard("Listen for the phrase", "Each requirement points to one feature: NAT, peering, ExpressRoute, endpoint.")
    private val today = BoardCard("Today", "Hub and spoke VNets, peer both ways, a NAT gateway, check the VM's address.")
    private val ip = BoardCard("IP address", "A machine's street address. A private one is a room number in one building.")
    private val vnetCard = BoardCard("Virtual network (VNet)", "Your own private floor in Azure: machines on it talk freely.")
    private val subnetCard = BoardCard("Subnet", "A room on the floor, such as app or data, each with its own rules.")
    private val rangeCard = BoardCard("Address space", "The VNet's range of room numbers. Never overlap with another network.")
    private val region = BoardCard("Region", "A group of Azure data centres in one part of the world, like West Europe.")
    private val peering = BoardCard("VNet peering", "A private corridor between two floors, over Microsoft's own cables.")
    private val transitive = BoardCard("Not transitive", "A to B and B to C never gives A to C. A friend's friend isn't yours.")
    private val hubSpoke = BoardCard("Hub and spoke", "A bicycle wheel: shared things in the hub, workloads in spokes.")
    private val wan = BoardCard("Virtual WAN", "Many sites and regions: Microsoft runs the hubs, and all reach all.")
    private val natCard = BoardCard("NAT gateway", "A post room: every letter leaves with one company return address.")
    private val publicIp = BoardCard("Public IP", "A street address the whole internet can see and reply to.")
    private val api = BoardCard("API", "The door a program opens for other programs. This one checks addresses.")
    private val vpnCard = BoardCard("VPN", "A sealed tunnel through the internet, like a locked van on public roads.")
    private val erCard = BoardCard("ExpressRoute", "A private line through a telecoms provider: your own private road.")
    private val latency = BoardCard("Latency", "The delay before a message arrives. Steady on ExpressRoute only.")
    private val failover = BoardCard("Failover", "ExpressRoute first; the site-to-site VPN takes over if it fails.")
    private val p2s = BoardCard("Point-to-site", "Each laptop builds its own tunnel. Site-to-site joins an office router.")
    private val paas = BoardCard("PaaS", "Platform as a service: Azure runs the machine, you use the database.")
    private val peCard = BoardCard("Private endpoint", "An address in your subnet for one database: an extension line.")
    private val dnsCard = BoardCard("Private DNS zone", "A phone book only linked VNets can read: the name finds the private address.")
    private val seCard = BoardCard("Service endpoint", "Free, from Azure subnets only; the service keeps its public address.")

    private fun classicBoard() = PodcastBoard(
        T,
        listOf(
            // 0: "Hi, and welcome back. Today is networks..."
            Scene(
                0, "Two Tracks goes global", floor(),
                beats = listOf(
                    Beat(0, show = setOf("vnet"), card = BoardCard("Two Tracks goes global", "Backend on Azure, users in Europe and Australia, an old office system.")),
                    Beat(2, card = ip),
                    Beat(4, light = setOf("vnet"), card = vnetCard),
                    Beat(6, show = setOf("vm"), light = setOf("vm"), card = BoardCard("VM", "A virtual machine: a computer you rent in Azure by the hour.")),
                    Beat(8, show = setOf("app", "data"), light = setOf("app", "data"), pulse = "vnet" to "app", card = subnetCard),
                    Beat(10, show = setOf("range"), light = setOf("range"), card = rangeCard),
                ),
            ),
            // 11: "Noted. Now the Two Tracks backend has to get out."
            Scene(
                11, "The way out", wayOut(),
                beats = listOf(
                    Beat(12, show = setOf("vms"), light = setOf("vms"), card = BoardCard("Outbound", "Traffic leaving your floor. New subnets are private: no way out at first.")),
                    Beat(14, show = setOf("nat"), light = setOf("nat"), pulse = "vms" to "nat", card = natCard),
                    Beat(16, show = setOf("net"), light = setOf("nat", "net"), pulse = "nat" to "net", card = publicIp),
                    Beat(18, show = setOf("partner"), light = setOf("partner"), pulse = "net" to "partner", card = api),
                    Beat(20, light = setOf("vms"), card = BoardCard("A public IP per VM?", "Ten addresses to list, ten machines open. Only for a VM taking calls.")),
                ),
            ),
            // 21: "Okay. Now Two Tracks has users in Europe and Australia."
            Scene(
                21, "Joining networks", peer(),
                beats = listOf(
                    Beat(22, show = setOf("a", "b"), light = setOf("a", "b"), card = region),
                    Beat(24, show = setOf("hub"), light = setOf("a", "hub"), pulse = "a" to "hub", card = peering),
                    Beat(26, light = setOf("a", "b"), pulse = "a" to "b", card = transitive),
                    Beat(28, light = setOf("hub"), card = hubSpoke),
                    Beat(30, light = setOf("hub", "b"), pulse = "hub" to "b", card = BoardCard("Firewall in the hub", "A guard for traffic. A route sends spoke-to-spoke traffic through it.")),
                    Beat(32, show = setOf("wan"), light = setOf("wan"), card = wan),
                ),
            ),
            // 33: "Now the old office."
            Scene(
                33, "Reaching the office", hybrid(),
                beats = listOf(
                    Beat(34, show = setOf("office", "hub", "vpn"), light = setOf("office", "vpn"), pulse = "office" to "vpn", card = vpnCard),
                    Beat(36, show = setOf("laptop"), light = setOf("laptop", "hub"), pulse = "laptop" to "hub", card = p2s),
                    Beat(38, show = setOf("er"), light = setOf("er"), pulse = "office" to "er", card = erCard),
                    Beat(40, light = setOf("er"), card = latency),
                    Beat(42, light = setOf("er", "vpn"), pulse = "vpn" to "hub", card = failover),
                ),
            ),
            // 43: "Now the database."
            Scene(
                43, "The database, privately", privateLink(),
                beats = listOf(
                    Beat(44, show = setOf("sql"), light = setOf("sql"), card = paas),
                    Beat(46, show = setOf("app", "pe"), light = setOf("pe"), pulse = "pe" to "sql", card = peCard),
                    Beat(48, show = setOf("dns"), light = setOf("dns"), pulse = "app" to "dns", card = dnsCard),
                    Beat(50, show = setOf("se"), light = setOf("se"), card = seCard),
                ),
            ),
            // 51: "So what does the whole Two Tracks design look like?"
            Scene(
                51, "The whole design", design(),
                beats = listOf(
                    Beat(52, light = setOf("hub", "api", "data"), pulse = "office" to "hub", card = BoardCard("The design", "Hub and spoke per region, ExpressRoute plus VPN, NAT, private endpoints.")),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("nat", "peer", "er"), card = listen),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "$T--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Corridor That Went Nowhere."
            Scene(
                0, "The Corridor That Went Nowhere", floor(),
                beats = listOf(
                    Beat(0, show = setOf("vnet"), card = BoardCard("The case", "Launch week: four things broke, and each one was a design choice.")),
                    Beat(2, card = ip),
                    Beat(4, light = setOf("vnet"), card = BoardCard("Virtual network (VNet)", "Under the picture: a block of private addresses kept apart from others.")),
                    Beat(6, show = setOf("vm", "app", "data"), light = setOf("app", "data"), pulse = "vnet" to "app", card = subnetCard),
                ),
            ),
            // 8: "Two Tracks runs in two regions."
            Scene(
                8, "Case one: the spokes", peer(),
                beats = listOf(
                    Beat(8, show = setOf("a", "b"), card = region),
                    Beat(10, show = setOf("hub"), light = setOf("hub"), card = hubSpoke),
                    Beat(12, light = setOf("a"), card = api),
                    Beat(14, light = setOf("a", "hub"), pulse = "a" to "hub", card = peering),
                    Beat(16, light = setOf("a", "b"), card = BoardCard("The clue", "Both spokes were peered to the hub, but not to each other.")),
                    Beat(18, pulse = "a" to "b", card = transitive),
                    Beat(20, show = setOf("wan"), light = setOf("hub", "wan"), card = BoardCard("Three fixes", "Peer the spokes, route through a hub firewall, or Virtual WAN.")),
                ),
            ),
            // 21: "Case one closed. Case two?"
            Scene(
                21, "Case two: the payment", wayOut(),
                beats = listOf(
                    Beat(22, show = setOf("vms", "partner"), light = setOf("partner"), card = BoardCard("Refused", "The partner's API accepts calls only from addresses on its list.")),
                    Beat(24, show = setOf("net"), light = setOf("vms"), card = BoardCard("A public IP per VM?", "Ten addresses to list, and ten machines open to strangers.")),
                    Beat(26, show = setOf("nat"), light = setOf("nat"), pulse = "vms" to "nat", card = BoardCard("NAT gateway", "Swaps each private address for one fixed public one, and remembers replies.")),
                ),
            ),
            // 27: "One known address for the partner's list. Case two closed. Three?"
            Scene(
                27, "Case three: the office", hybrid(),
                beats = listOf(
                    Beat(28, show = setOf("office", "hub"), light = setOf("office"), card = BoardCard("On-premises", "Your own building, your own computers.")),
                    Beat(30, show = setOf("vpn", "laptop"), light = setOf("vpn"), pulse = "office" to "vpn", card = vpnCard),
                    Beat(32, light = setOf("vpn"), card = BoardCard("Latency", "The delay before a message arrives. Over the internet it jumps about.")),
                    Beat(34, show = setOf("er"), light = setOf("er"), pulse = "office" to "er", card = erCard),
                    Beat(36, light = setOf("er", "vpn"), pulse = "vpn" to "hub", card = BoardCard("Failover", "Weeks to order, not encrypted by default; a VPN takes over if it fails.")),
                ),
            ),
            // 37: "Case four. I can feel it."
            Scene(
                37, "Case four: the database", privateLink(),
                beats = listOf(
                    Beat(38, show = setOf("sql"), light = setOf("sql"), card = paas),
                    Beat(40, show = setOf("app", "se"), light = setOf("se"), card = seCard),
                    Beat(42, show = setOf("pe"), light = setOf("pe"), pulse = "pe" to "sql", card = peCard),
                    Beat(44, show = setOf("dns"), light = setOf("dns"), pulse = "app" to "dns", card = BoardCard("Private DNS zone", "Without it, the name still pointed to the public address.")),
                ),
            ),
            // 45: "One more clue in the file. Overlapping something?"
            Scene(
                45, "One more clue", floor(),
                beats = listOf(
                    Beat(46, light = setOf("range"), card = BoardCard("Overlapping ranges", "The test VNet reused the office's numbers, so it had to be rebuilt.")),
                    Beat(48, light = setOf("vnet"), card = BoardCard("Next door", "Load balancers and firewalls: the network security episode.")),
                ),
            ),
            // 49: "So, the case summary for the exam?"
            Scene(
                49, "Case closed", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("nat", "peer", "er"), card = listen),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "$T--gossip",
        listOf(
            // 0: "Did you hear what happened on the Two Tracks network team this week?"
            Scene(
                0, "The network team's week", floor(),
                beats = listOf(
                    Beat(0, show = setOf("vnet"), card = BoardCard("Five people, five stories", "Two Tracks goes global, and the network team has quite a week.")),
                    Beat(2, card = ip),
                    Beat(4, light = setOf("vnet"), card = vnetCard),
                    Beat(6, show = setOf("vm", "app", "data"), light = setOf("app", "data"), pulse = "vnet" to "app", card = subnetCard),
                    Beat(8, show = setOf("range"), light = setOf("range"), card = rangeCard),
                    Beat(10, card = BoardCard("Priya's lesson", "Overlapping ranges can never be joined. Plan them before you build.")),
                ),
            ),
            // 11: "Poor Priya. Tuesday?"
            Scene(
                11, "Tuesday: Tom", peer(),
                beats = listOf(
                    Beat(12, show = setOf("hub", "a", "b"), light = setOf("hub"), card = hubSpoke),
                    Beat(14, light = setOf("a", "hub"), pulse = "a" to "hub", card = peering),
                    Beat(16, light = setOf("a", "b"), pulse = "a" to "b", card = transitive),
                    Beat(18, show = setOf("wan"), light = setOf("wan"), card = wan),
                ),
            ),
            // 19: "Go on."
            Scene(
                19, "Wednesday: Dana", hybrid(),
                beats = listOf(
                    Beat(20, show = setOf("office", "hub", "vpn"), light = setOf("office", "vpn"), pulse = "office" to "vpn", card = vpnCard),
                    Beat(22, light = setOf("vpn"), card = BoardCard("Latency", "The delay before a message arrives. On the internet it wobbles.")),
                    Beat(24, show = setOf("er"), light = setOf("er"), pulse = "office" to "er", card = erCard),
                    Beat(26, light = setOf("er", "vpn"), pulse = "vpn" to "hub", card = failover),
                ),
            ),
            // 27: "Thursday?"
            Scene(
                27, "Thursday: Marcus", privateLink(),
                beats = listOf(
                    Beat(28, show = setOf("sql"), light = setOf("sql"), card = paas),
                    Beat(30, show = setOf("pe"), light = setOf("pe"), pulse = "pe" to "sql", card = peCard),
                    Beat(32, show = setOf("app", "dns"), light = setOf("dns"), pulse = "app" to "dns", card = BoardCard("He forgot this", "No Private DNS zone, so the name still found the public address.")),
                ),
            ),
            // 33: "[sigh] So close. Friday?"
            Scene(
                33, "Friday: the intern", privateLink(),
                beats = listOf(
                    Beat(34, show = setOf("se"), light = setOf("se"), card = seCard),
                    Beat(36, light = setOf("pe"), card = BoardCard("Office traffic", "Service endpoints only work from Azure subnets: use a private endpoint.")),
                ),
            ),
            // 37: "Did anyone get anything right?"
            Scene(
                37, "Priya's redemption", wayOut(),
                beats = listOf(
                    Beat(38, light = setOf("partner"), card = api),
                    Beat(40, show = setOf("nat"), light = setOf("nat"), pulse = "vms" to "nat", card = natCard),
                    Beat(42, card = p2s),
                ),
            ),
            // 43: "So, the gossip I need for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("nat", "peer", "er"), card = listen),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "$T--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Finding each other", floor(),
                beats = listOf(
                    Beat(0, show = setOf("vnet"), card = BoardCard("Tonight", "How machines find each other in the dark: networks.")),
                    Beat(2, card = ip),
                    Beat(4, light = setOf("vnet"), card = vnetCard),
                    Beat(6, show = setOf("vm"), light = setOf("vm"), card = BoardCard("VM", "A virtual machine: a computer you rent in Azure by the hour.")),
                    Beat(8, show = setOf("app", "data", "range"), light = setOf("app", "data", "range"), pulse = "vnet" to "app", card = subnetCard),
                ),
            ),
            // 9: "Noted. Now, the backend wants to go out."
            Scene(
                9, "A door for the night", wayOut(),
                beats = listOf(
                    Beat(10, show = setOf("vms"), light = setOf("vms"), card = BoardCard("Outbound", "New subnets are private: no way out until you add a door.")),
                    Beat(12, show = setOf("nat", "net"), light = setOf("nat"), pulse = "vms" to "nat", card = natCard),
                    Beat(14, show = setOf("partner"), light = setOf("partner"), pulse = "net" to "partner", card = api),
                ),
            ),
            // 15: "Choosy partners. I respect that. Now, two continents."
            Scene(
                15, "Two continents", peer(),
                beats = listOf(
                    Beat(16, show = setOf("hub", "a"), light = setOf("a", "hub"), pulse = "a" to "hub", card = peering),
                    Beat(18, show = setOf("b"), light = setOf("a", "b"), pulse = "a" to "b", card = transitive),
                    Beat(20, show = setOf("wan"), light = setOf("hub", "wan"), card = hubSpoke),
                ),
            ),
            // 21: "And the old office? It's still out there, all alone."
            Scene(
                21, "The old office", hybrid(),
                beats = listOf(
                    Beat(22, show = setOf("office", "hub", "vpn", "laptop"), light = setOf("office", "vpn"), pulse = "office" to "vpn", card = vpnCard),
                    Beat(24, show = setOf("er"), light = setOf("er"), pulse = "office" to "er", card = BoardCard("ExpressRoute", "Your own private road, so the latency, the delay, stays steady.")),
                    Beat(26, light = setOf("er", "vpn"), pulse = "vpn" to "hub", card = failover),
                ),
            ),
            // 27: "And the database? Where do the secrets sleep?"
            Scene(
                27, "Where the secrets sleep", privateLink(),
                beats = listOf(
                    Beat(28, show = setOf("sql"), light = setOf("sql"), card = paas),
                    Beat(30, show = setOf("pe"), light = setOf("pe"), pulse = "pe" to "sql", card = peCard),
                    Beat(32, show = setOf("app", "dns"), light = setOf("dns"), pulse = "app" to "dns", card = dnsCard),
                    Beat(34, show = setOf("se"), light = setOf("se"), card = seCard),
                ),
            ),
            // 35: "[sigh] Before you send me off to bed."
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("nat", "peer", "er"), card = listen),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "$T--couples",
        listOf(
            // 0: "Tonight is date night, darling."
            Scene(
                0, "Date night: connections", floor(),
                beats = listOf(
                    Beat(0, show = setOf("vnet"), card = BoardCard("Date night", "Nova and Rafe quiz each other: every right answer earns a kiss.")),
                    Beat(4, card = ip),
                    Beat(6, show = setOf("app", "data"), light = setOf("vnet"), pulse = "vnet" to "app", card = vnetCard),
                    Beat(8, show = setOf("vm", "range"), light = setOf("range"), card = rangeCard),
                ),
            ),
            // 9: "Never overlap. Unlike us on the sofa."
            Scene(
                9, "The way out", wayOut(),
                beats = listOf(
                    Beat(10, show = setOf("vms", "nat"), light = setOf("nat"), pulse = "vms" to "nat", card = natCard),
                    Beat(12, show = setOf("net"), light = setOf("nat", "net"), pulse = "nat" to "net", card = publicIp),
                    Beat(13, show = setOf("partner"), light = setOf("partner"), pulse = "net" to "partner", card = BoardCard("Quiz: ten VMs, one address", "The partner's API accepts one known address. Answer: a NAT gateway.")),
                ),
            ),
            // 16: "Mm. Two continents, though."
            Scene(
                16, "Peering, and its limits", peer(),
                beats = listOf(
                    Beat(16, show = setOf("a", "b"), card = region),
                    Beat(17, show = setOf("hub"), light = setOf("a", "hub"), pulse = "a" to "hub", card = peering),
                    Beat(18, light = setOf("a", "b"), card = BoardCard("Quiz: can A reach B?", "Both spokes are peered to the hub, not to each other.")),
                    Beat(20, pulse = "a" to "b", card = transitive),
                ),
            ),
            // 21: "Harsh. Fine, my turn to impress you. What about the old office?"
            Scene(
                21, "The old office", hybrid(),
                beats = listOf(
                    Beat(23, show = setOf("office", "hub", "vpn", "er"), light = setOf("vpn", "er"), pulse = "office" to "er", card = BoardCard("VPN or ExpressRoute", "A locked van on public roads, or your own private road.")),
                    Beat(25, light = setOf("er"), card = latency),
                    Beat(27, light = setOf("er", "vpn"), pulse = "vpn" to "hub", card = failover),
                    Beat(31, show = setOf("laptop"), light = setOf("laptop"), pulse = "laptop" to "hub", card = p2s),
                ),
            ),
            // 33: "Now secrets. Our database, Azure SQL, is PaaS..."
            Scene(
                33, "Keeping it private", privateLink(),
                beats = listOf(
                    Beat(33, show = setOf("sql"), light = setOf("sql"), card = paas),
                    Beat(34, show = setOf("pe"), light = setOf("pe"), pulse = "pe" to "sql", card = peCard),
                    Beat(36, show = setOf("app", "dns"), light = setOf("dns"), pulse = "app" to "dns", card = dnsCard),
                    Beat(38, show = setOf("se"), light = setOf("se", "pe"), card = BoardCard("Quiz: from the office", "A private endpoint. A service endpoint only works from Azure subnets.")),
                ),
            ),
            // 41: "Last question, for the exam."
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("nat", "peer", "er"), card = listen),
                    Beat(45, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
