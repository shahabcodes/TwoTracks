package com.shahaabapps.core.content

/** Networks: performance, security, load balancing and routing: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesNetworkOptimize {
    private const val T = "sa-network-optimize"

    private val classic = Podcast(
        T,
        listOf(
            maya("Hi, and welcome back. This is the second networks episode: spreading the load, keeping attackers out, and choosing which way traffic flows. Our story is still Two Tracks goes global: the Android app is a company with a backend on Azure, and users in Europe and Australia. [pause] It's a design exam, so we learn which service each requirement points to."),
            leo("Sounds like a lot of boxes. Where do we start?"),
            maya("With a restaurant. A host at the door sends each guest to a free table. That's load balancing: spreading requests over several servers, so none is swamped. A server is just a computer that answers requests, like one waiter."),
            leo("And Azure has a host for the door?"),
            maya("Four of them, and the exam loves asking which. To choose, you need two ideas. The first is layer 4 versus layer 7."),
            leo("Layers. Like a cake?"),
            maya("[chuckle] Like the post. Layer 4 means the balancer only reads the envelope: the address and the port. A port is the door number on a machine, so one computer can run several services behind different doors."),
            leo("And layer 7?"),
            maya("Layer 7 opens the letter and reads the web request inside. For example, the URL path: the part of a web address after the site's name, like slash images. So it can send image requests to one set of servers, and the rest to another."),
            leo("And the second idea?"),
            maya("Regional or global. A region is a group of Azure data centres in one part of the world, like West Europe. A regional balancer spreads work inside one region; a global one picks between regions."),
            leo("Okay, four boxes. Go."),
            maya("Azure Load Balancer: layer 4, regional. It never reads the request, so it passes any kind of traffic straight through, with very little delay. Picture a live quiz game sending UDP."),
            leo("UDP? Say that in English."),
            maya("One of the two basic ways machines send data. TCP checks that every piece arrives, like registered post. UDP just throws the pieces, fast, like shouting across a room. Games love UDP, and the two web balancers can't carry it."),
            leo("Second box?"),
            maya("Application Gateway: layer 7, regional. It routes by URL path inside one region. And it can decrypt HTTPS for your servers."),
            leo("Decrypt? What's HTTPS, in plain words?"),
            maya("HTTPS is web traffic in a sealed envelope, so nobody on the way can read it. Opening the seal takes work. The gateway opens it for everyone, like an office mailroom, so your servers don't have to."),
            leo("Third box?"),
            maya("Azure Front Door: layer 7, and global. It sits at the edge, meaning in Microsoft's buildings close to users all over the world. It caches, keeping copies of your images near users, like a corner shop stocking the popular items. And if a region fails, it moves users to the other at once."),
            leo("And the fourth?"),
            maya("Traffic Manager. It's global, but it never touches your traffic. It only answers the DNS question. DNS is the internet's phone book, which turns a name into an address. Traffic Manager just hands out the number of the best region."),
            leo("Sounds handy. What's the catch?"),
            maya("Phones and laptops remember a phone book answer for a while. So when a region fails, they keep calling the old number until they forget it, and failover is slower than Front Door. Its strength: any kind of traffic, not just web, across regions."),
            leo("So how do I choose in the exam?"),
            maya("Two questions. Is it web traffic, and does it cover more than one region? Web and global: Front Door. Web and one region: Application Gateway. Not web and global: Traffic Manager. Not web and one region: Load Balancer."),
            leo("Give me a real one."),
            maya("Say you're building the Two Tracks web API, the door the app calls on the backend, in West Europe and Australia East. Users want caching and protection from web attacks. You pick."),
            leo("Web, two regions, caching. Front Door?"),
            maya("Front Door, with a WAF policy. And inside each region, the VMs, the rented computers, sit behind a Load Balancer."),
            leo("WAF? Sounds like a dog."),
            maya("[laugh] It's a web application firewall (WAF). Picture a guard who reads every web request and stops known attacks. It runs on Application Gateway or Front Door."),
            leo("What attacks?"),
            maya("For example, SQL injection: someone types database commands into the sign-up form, hoping your database runs them. Like writing, and also open the safe, at the bottom of a delivery note."),
            leo("Can't I just block that with a network rule?"),
            maya("That's the trap. A network security group (NSG) is a guest list of allow and deny rules by address and port, on a subnet, which is a room in your network. Rules are checked in order, lowest number first."),
            leo("So like the firewall on my own server. Allow 443, block the rest."),
            maya("Exactly, and that's why it can't help. It never reads the request. The attack comes in on port 443, the usual door for secure web traffic, looking like any visit."),
            leo("So what's an NSG good for?"),
            maya("Basic filtering everywhere, for free. And an application security group (ASG) keeps it tidy. It's a name tag for a set of VMs, such as app servers, so one rule says web servers may reach app servers on 443, with no addresses listed."),
            leo("And Azure Firewall? Another guard?"),
            maya("One central checkpoint, usually in the hub, the shared network in the middle, for traffic between networks and the internet. It can allow or block by domain name, such as anything ending in twotracks dot app. Premium can even look inside encrypted traffic."),
            leo("And floods? I've heard of DDoS."),
            maya("A DDoS attack floods you with fake traffic, like a crowd blocking a shop door so real customers can't get in. Basic protection is free and always on. DDoS Network Protection covers a network's public addresses, with expert help and cost protection during an attack."),
            leo("How do admins get into the VMs, then? Open a port?"),
            maya("Never open the remote-control port to the internet. Use Azure Bastion, a guarded side door for admins. They open RDP or SSH, the remote-control screens for Windows and Linux, right in the browser, so the VMs need no public address."),
            leo("Now speed. Can the network go faster?"),
            maya("Accelerated networking lets a VM's network card skip the host's software switch, like an express lane past the checkout. It cuts latency, the delay before a message arrives, on supported VM sizes."),
            leo("Any other trick?"),
            maya("A proximity placement group keeps VMs physically close in one data centre, like seating a chatty team at one desk. That gives the lowest delay between them."),
            leo("Last piece. Routing. Which way does traffic actually go?"),
            maya("Azure routes it for you with system routes, like the default signposts in a building. A user-defined route, UDR for short, is your own signpost that overrides them. You put routes in a route table and attach it to a subnet."),
            leo("Give me an example."),
            maya("All outbound traffic from the spokes must pass through the firewall. So you add the all-zeros route, which means everything else, pointing at the firewall's private address."),
            leo("All zeros. Like the default route on my home router: anything unknown goes there."),
            maya("Exactly. Its next hop, simply the next stop, is a virtual appliance: a machine that handles traffic, here the firewall."),
            leo("Couldn't an NSG rule do that?"),
            maya("No. An NSG decides whether traffic may pass, not which way it travels. Only a route changes the path. And forced tunnelling goes further: internet-bound traffic goes back to the office first, so the company's own firewall checks it."),
            leo("So what do I remember for the exam?"),
            maya("If the question says global HTTP with caching, the answer is Front Door. If URL path routing in one region, Application Gateway. If non-web across regions, Traffic Manager. If TCP or UDP in one region, Load Balancer. If SQL injection, a WAF, not an NSG. If all outbound through a firewall, a route, not an NSG rule."),
            leo("And anything to try today?"),
            maya("Read the load-balancing decision tree on the Architecture Center, and trace the Two Tracks web API through it. Create a virtual network, an NSG and an ASG, and add one rule that targets the ASG. Then create a route table, attach it to a subnet, and check a VM's effective routes."),
            leo("Envelopes, letters and signposts. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        T,
        listOf(
            maya("Welcome to Case Files. Today's case: The Front Door That Wasn't. Two Tracks has gone global: the Android app is a company with a backend on Azure, and users in Europe and Australia. In one bad week the app crawled in Australia, the sign-up form was attacked, and a VM, a rented computer, was broken into."),
            leo("Detective Leo, on the case. First, the words. What's load balancing?"),
            maya("Picture a restaurant host who sends each guest to a free table. Load balancing spreads requests over several servers, so none is swamped. A server is just a computer that answers requests."),
            leo("And Azure has a host for that?"),
            maya("Four, and picking the wrong one was the first clue. To tell them apart, start with layers. Layer 4 means the balancer only reads the envelope: the address and the port. A port is the door number on a machine."),
            leo("And layer 7?"),
            maya("It opens the letter and reads the web request inside, such as the URL path: the part of a web address after the site's name, like slash images."),
            leo("Anything else I need?"),
            maya("Regional or global. A region is a group of Azure data centres in one part of the world, like West Europe. Regional works inside one region; global picks between regions."),
            leo("Right. The first crime."),
            maya("Users in Australia waited ages for pictures. And when Europe went down, the app stayed broken for many minutes. The design used Traffic Manager in front of both regions."),
            leo("Suspect: Traffic Manager was too slow at moving the traffic."),
            maya("[pause] Closer than you think, but it moves no traffic at all. Traffic Manager only answers the DNS question. DNS is the internet's phone book, turning a name into an address. It hands out the best region's number, then steps aside."),
            leo("So why the long outage?"),
            maya("How it works: phones remember a phone book answer for a while. When Europe failed, they kept calling the old number until they forgot it. And there was no caching, which means keeping copies of images near users, like a corner shop stocking popular items."),
            leo("So the right door is?"),
            maya("Azure Front Door: layer 7, global, caching at the edge, meaning in Microsoft's buildings close to users worldwide. It fails over fast, because the traffic really passes through it. Traffic Manager still fits non-web traffic across regions."),
            leo("Second crime?"),
            maya("An earlier draft put one Application Gateway in West Europe in front of both regions. Application Gateway is layer 7, but regional. It routes by URL path, and can decrypt HTTPS, the sealed kind of web traffic, so the servers don't have to."),
            leo("Suspect: it worked fine, just slow for Australia."),
            maya("Worse. One gateway in one region is a single point of failure. If West Europe falls, Australia falls with it. Regional means regional; that's where it stops working. The fourth box, Azure Load Balancer, is layer 4 and regional too, for non-web traffic like a live game's fast messages."),
            leo("Third crime. The attack."),
            maya("The sign-up form got SQL injection attempts. That's someone typing database commands into a form box, hoping your database runs them, like writing, and also open the safe, on a delivery note."),
            leo("Easy. Add an NSG rule to block it."),
            maya("That's the suspect the attacker hoped for. A network security group (NSG) is a guest list of allow and deny rules by address and port, on a subnet, a room in your network."),
            leo("Like the firewall on the server itself. Port 443 open, everything else shut. Sounds safe."),
            maya("Safe from the wrong ports. But it never reads the request. The attack came in on port 443, the usual door for secure web traffic, like any visitor."),
            leo("Then what reads the request?"),
            maya("A web application firewall (WAF): a guard who reads every web request and stops known attacks with ready-made rules. Under the picture, it's a policy you attach to Front Door or Application Gateway. Two Tracks put a WAF policy on Front Door, at the edge."),
            leo("And floods? Did they get flooded?"),
            maya("Almost. A DDoS attack floods you with fake traffic, like a crowd blocking a shop door. Basic protection is free and always on. DDoS Network Protection adds expert help and cost protection for a network's public addresses. It absorbs floods; it never judges a single request."),
            leo("Fourth crime. The break-in."),
            maya("An admin had opened RDP, the remote-control screen for Windows, on port 3389, to the whole internet. Strangers guessed passwords all night."),
            leo("Suspect: a weak password."),
            maya("A clue, not the cause. That door should never have been open. Azure Bastion is a guarded side door for admins. They open RDP, or SSH, its Linux cousin, right in the browser, and the VM needs no public address at all."),
            leo("Please tell me there's no fifth."),
            maya("[sigh] There's a fifth. The rule said all outbound traffic from the spokes must pass through Azure Firewall, one central checkpoint in the hub, the shared network in the middle. It can allow or block by domain name, and Premium can even look inside encrypted traffic."),
            leo("Let me guess. They added an NSG rule that allows the firewall."),
            maya("Exactly, and traffic still went straight out. An NSG decides whether traffic may pass, not which way it goes. Only a route changes the path."),
            leo("Did they get anything right?"),
            maya("One tidy thing: an application security group (ASG), a name tag for a set of VMs, so rules name the tag, not addresses."),
            leo("So what's a route, then?"),
            maya("Azure's system routes are the default signposts. A user-defined route, UDR for short, is your own signpost that overrides them. It lives in a route table, attached to each spoke subnet."),
            leo("And the signpost says what?"),
            maya("The all-zeros route, meaning everything else, goes to the firewall's private address, as a virtual appliance. Now every packet leaving a spoke passes the checkpoint."),
            leo("And forced tunnelling?"),
            maya("One step further: internet-bound traffic goes back to the office first, so the company's own firewall checks it. And for speed, accelerated networking is an express lane past the host's software switch, cutting latency, the delay before a message arrives."),
            leo("Where does the office link come from?"),
            maya("That's the network connectivity episode: hubs, spokes, and the line to the office. This case was the guards and the signposts."),
            leo("So, the case summary for the exam?"),
            maya("If the question says global HTTP with caching, Front Door. If URL path routing in one region, Application Gateway. If non-web across regions, Traffic Manager. If non-web in one region, Load Balancer. If SQL injection, a WAF, not an NSG. If outbound must pass a firewall, a route, not an NSG rule."),
            leo("Case closed. Anything to try today?"),
            maya("Read the load-balancing decision tree on the Architecture Center and trace the Two Tracks web app through it. Create a virtual network, an NSG and an ASG, and add one rule that targets the ASG. Create a route table, attach it to a subnet, and check a VM's effective routes."),
            leo("Read the letter, not just the envelope. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        T,
        listOf(
            maya("Did you hear about the Two Tracks platform team this week? Grab a coffee. The Android app is a company now, going global, with a backend on Azure and users in Europe and Australia. Five people, five network stories, and one very long Friday."),
            leo("[laugh] I heard someone opened a door they shouldn't have. But start me at zero. What's load balancing?"),
            maya("Picture a restaurant host sending each guest to a free table. Load balancing spreads requests over several servers, computers that answer requests, so none is swamped. Azure has four, and the team argued about all of them."),
            leo("How do you tell them apart?"),
            maya("Two ideas. First, layer 4 versus layer 7. Layer 4 means the balancer only reads the envelope: the address and the port. A port is the door number on a machine."),
            leo("And layer 7?"),
            maya("It opens the letter and reads the web request inside, such as the URL path: the part of a web address after the site's name, like slash images."),
            leo("And the second idea?"),
            maya("Regional or global. A region is a group of Azure data centres in one part of the world, like West Europe or Australia East. Regional works inside one; global picks between them."),
            leo("Okay. Who went first?"),
            maya("Tom, Monday. Tom picked Traffic Manager to send users to the nearest region. It's global, but it only answers the DNS question. DNS is the internet's phone book: name in, address out."),
            leo("Sounds fine. What went wrong?"),
            maya("The requirement said caching and a WAF. Caching means keeping copies of images near users, like a corner shop stocking popular items. Traffic Manager does neither, because it never touches the traffic. And phones remember its answer, so failover was slow."),
            leo("And WAF? Everyone keeps saying it."),
            maya("A web application firewall (WAF): a guard who reads every web request and stops known attacks. The answer for Tom was Azure Front Door: layer 7, global, caching at the edge, which means Microsoft's buildings close to users, with a WAF policy on top."),
            leo("Poor Tom. Tuesday?"),
            maya("Marcus. Marcus loves Application Gateway. It's layer 7 and routes by URL path, and it can decrypt HTTPS, the sealed kind of web traffic, so the servers don't have to. He put one in West Europe, in front of both regions, and called it global."),
            leo("Was it?"),
            maya("No. Application Gateway is regional. One in Europe is a single point of failure for Australia, and every Australian request crossed the planet. Regional means regional."),
            leo("Did anyone use the plain Load Balancer?"),
            maya("The game team, quietly, with no drama at all. Azure Load Balancer is layer 4 and regional. It passes any kind of traffic straight through, such as UDP, the fast, unchecked way a live quiz game sends its answers."),
            leo("Wednesday?"),
            maya("Dana. The sign-up form got SQL injection attempts. That's someone typing database commands into a form box, hoping your database runs them, like writing, and also open the safe, on a delivery note."),
            leo("What did Dana do?"),
            maya("She added a rule to a network security group (NSG). An NSG is a guest list of allow and deny rules by address and port, on a subnet, which is a room in your network. Rules are checked in order, lowest number first."),
            leo("And it didn't help?"),
            maya("Not at all. An NSG never reads the request. The attacks came in on port 443, the usual door for secure web traffic, like every normal visitor. A WAF reads the request. Dana's NSG is still useful, just for its own job."),
            leo("Which is?"),
            maya("Basic filtering by address and port, free, on every subnet. And she tidied it with an application security group (ASG): a name tag for a set of VMs, the rented computers, so a rule names the tag instead of listing addresses."),
            leo("[chuckle] I sense Thursday was worse."),
            maya("Thursday, the intern. Bless them. They needed to fix a VM, so they opened RDP, the remote-control screen for Windows, on port 3389, to the whole internet. By lunch, strangers were guessing passwords."),
            leo("Oh no. What should they have done?"),
            maya("Azure Bastion: a guarded side door for admins. RDP or SSH, its Linux cousin, opens right in the browser, and the VM needs no public address at all. Under the picture, Bastion sits in your network and connects to the VM's private address for you."),
            leo("And the floods? Was there a DDoS?"),
            maya("Just a scare. A DDoS attack floods you with fake traffic, like a crowd blocking a shop door. Basic protection is free and always on. DDoS Network Protection adds expert help and cost protection for a network's public addresses."),
            leo("Friday. The long one."),
            maya("Priya. The rule said all outbound traffic must pass through Azure Firewall, one central checkpoint in the hub, the shared network in the middle. It can block by domain name, and Premium can look inside encrypted traffic."),
            leo("So what did Priya do?"),
            maya("Added an NSG rule allowing the firewall. That was it."),
            leo("And the traffic ignored her?"),
            maya("Went straight out. An NSG decides whether traffic may pass, not which way it goes. Only a route changes the path. Azure's system routes are the default signposts. A user-defined route, UDR for short, is your own signpost that overrides them."),
            leo("How did she fix it?"),
            maya("A route table on each spoke subnet, with the all-zeros route, meaning everything else, pointing at the firewall's private address as a virtual appliance. Some companies go further with forced tunnelling: internet-bound traffic goes back to the office first, for its own firewall."),
            leo("Anything about speed? I heard someone bragging."),
            maya("That was Priya too. Accelerated networking lets a VM's network card skip the host's software switch, like an express lane past the checkout, cutting latency, the delay before a message arrives. And the office link itself is in the network connectivity episode."),
            leo("So, the gossip I need for the exam?"),
            maya("If the question says global HTTP with caching, Front Door. If URL path routing in one region, Application Gateway. If non-web across regions, Traffic Manager. If non-web in one region, Load Balancer. If SQL injection, a WAF, not an NSG. If outbound must pass a firewall, a route, not an NSG rule."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Read the load-balancing decision tree on the Architecture Center and trace the Two Tracks web app through it. Create a virtual network, an NSG and an ASG, and add one rule that targets the ASG. Create a route table, attach it to a subnet, and check a VM's effective routes."),
            leo("Read the letter, use the side door, fix the signposts. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        T,
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight is about who gets in, who gets turned away, and which way everyone walks home. Networks, part two."),
            ivy("Mm. Bouncers and walks home. I'm listening, Nova."),
            nova("Start with a restaurant at the end of the night. A host at the door sends each guest to a free table. That's load balancing: spreading requests over several servers, so none is swamped. A server is just a computer that answers requests."),
            ivy("And Azure has more than one host?"),
            nova("Four. [pause] To choose, you need two ideas. Layer 4 means the balancer only reads the envelope: the address and the port. A port is the door number on a machine."),
            ivy("And layer 7?"),
            nova("Layer 7 opens the letter. It reads the web request inside, such as the URL path: the part of a web address after the site's name, like slash images."),
            ivy("Opening letters. How intimate. And the second idea?"),
            nova("Regional or global. A region is a group of Azure data centres in one part of the world. Tonight's story is Two Tracks goes global: the Android app's backend runs in West Europe and Australia East. Regional works inside one; global picks between them."),
            ivy("So tell me the four."),
            nova("Azure Load Balancer: layer 4, regional. It passes any traffic straight through, even UDP, the fast, unchecked way a live game sends its messages."),
            ivy("And the one that reads letters, close to home?"),
            nova("[pause] Application Gateway: layer 7, regional. It routes by URL path, and can decrypt HTTPS, the sealed kind of web traffic."),
            ivy("And the global two?"),
            nova("Azure Front Door: layer 7, global, at the edge, which means in Microsoft's buildings close to users. It caches, keeping copies of images near people, like a corner shop stocking favourites. And Traffic Manager, which only answers the DNS question. DNS is the internet's phone book, turning a name into an address."),
            ivy("Only answers? It doesn't carry anything?"),
            nova("Nothing. It hands out the best region's number and steps aside. And phones remember that answer for a while, so when a region fails, they keep calling the old number. [pause] Failover is slower than Front Door. Its gift: any kind of traffic, across regions."),
            ivy("So how do I choose, half asleep?"),
            nova("Two questions. Web traffic? More than one region? Web and global, Front Door. Web and one region, Application Gateway. Not web and global, Traffic Manager. Not web and one region, Load Balancer."),
            ivy("Now the bouncers. Who keeps the wrong people out?"),
            nova("First, a network security group (NSG): a guest list of allow and deny rules by address and port, on a subnet, a room in your network. [pause] Checked in order, lowest number first. And an application security group (ASG) is a name tag for a set of VMs, the rented computers, so rules name the tag."),
            ivy("So the guest list stops everything bad?"),
            nova("No, and this is where it stops working. An NSG never reads the request. SQL injection, someone typing database commands into your sign-up form, arrives on port 443, the usual door for secure web traffic, like any guest. For that you need a web application firewall (WAF), a guard who reads every request."),
            ivy("Where does that guard stand?"),
            nova("On Front Door or Application Gateway, as a WAF policy. Under the picture, it's ready-made rules that match known attack patterns. And for the network as a whole, Azure Firewall: one central checkpoint in the hub, the shared network in the middle, that can block by domain name."),
            ivy("And if a crowd just storms the door?"),
            nova("That's a DDoS attack, fake traffic flooding you, like a crowd blocking a shop door. Basic protection is free and always on. [pause] DDoS Network Protection adds expert help and cost protection for a network's public addresses."),
            ivy("And the admins, sneaking in after hours?"),
            nova("They use Azure Bastion, a guarded side door. RDP or SSH, the remote-control screens for Windows and Linux, open in the browser, so the VMs need no public address. Never leave the remote-control port open to the internet, not even for one night."),
            ivy("[sigh] Fine. And the walk home? Which way does traffic go?"),
            nova("Azure has system routes, like the default signposts in a building. A user-defined route, UDR for short, is your own signpost that overrides them. You put it in a route table, and attach it to a subnet."),
            ivy("Give me one I'd actually write."),
            nova("All outbound traffic must pass the firewall. So the all-zeros route, which means everything else, points at the firewall's private address, as a virtual appliance. [pause] And forced tunnelling sends internet-bound traffic back to the office first, for its own firewall."),
            ivy("Couldn't the guest list do that?"),
            nova("No. An NSG decides whether you may pass, not which way you walk. Only a route changes the path. And if you want speed, accelerated networking is an express lane past the host's software switch, cutting latency, the delay before a message arrives."),
            ivy("And the road to the office?"),
            nova("That's the network connectivity episode, for another late night. [pause] Tonight was the doors and the signposts."),
            ivy("Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says global HTTP with caching, Front Door. If URL path routing in one region, Application Gateway. If non-web across regions, Traffic Manager. If non-web in one region, Load Balancer. If SQL injection, a WAF, not an NSG. If outbound must pass a firewall, a route, not an NSG rule."),
            ivy("And something to try today, before I sleep?"),
            nova("Read the load-balancing decision tree on the Architecture Center and trace the Two Tracks web app through it. [pause] Create a virtual network, an NSG and an ASG, and add one rule that targets the ASG. Create a route table, attach it to a subnet, and check a VM's effective routes."),
            ivy("Read the letter, not just the envelope. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        T,
        listOf(
            nova("Tonight is date night, darling. Low lights, a little wine, and you and me, Rafe, talking about who gets let in."),
            rafe("I hope I'm on the list."),
            nova("[laugh] We'll see. Networks, part two: load balancers, guards and routes. Our story is Two Tracks goes global: the Android app is a company with a backend on Azure, and users in Europe and Australia."),
            rafe("Then start with the balancing. What's load balancing, in plain words?"),
            nova("A restaurant host sending each guest to a free table. Requests get spread over several servers, computers that answer requests, so none is swamped. Azure has four, and the exam loves asking which."),
            rafe("How do I tell them apart?"),
            nova("Layer 4 only reads the envelope: the address and the port. A port is the door number on a machine."),
            rafe("And layer 7?"),
            nova("Layer 7 opens the letter and reads the web request inside, such as the URL path: the part of a web address after the site's name."),
            rafe("Opening letters. Bold. And the second idea?"),
            nova("Regional or global. A region is a group of Azure data centres in one part of the world. Regional works inside one; global picks between them."),
            rafe("Then the four, darling. Start close to home."),
            nova("Load Balancer: layer 4, regional. Application Gateway: layer 7, regional, and it routes by URL path."),
            rafe("And the global pair?"),
            nova("Front Door: layer 7, global, at the edge, which means Microsoft's buildings close to users. It caches, keeping copies near people, like a corner shop. Traffic Manager only answers the DNS question, the internet's phone book, then steps aside, so its failover is slower."),
            rafe("Then here's your first question, darling. Global users, web traffic, needs caching and a WAF. Which one?"),
            nova("Front Door, with a WAF policy. Web and global points straight at it."),
            rafe("Right answer. Global, and still close to me. [kiss]"),
            nova("Mm. But do you know what a WAF is, or are you just saying it?"),
            rafe("A web application firewall (WAF): a guard who reads every web request and stops known attacks, like SQL injection, someone typing database commands into your sign-up form. It runs on Front Door or Application Gateway."),
            nova("Show-off. My question, then. A live quiz game sends UDP, the fast, unchecked kind of message, to servers in one region. Which balancer?"),
            rafe("Application Gateway. It's the clever one."),
            nova("No kiss for that one. Application Gateway only speaks web. Not web and one region means Azure Load Balancer: layer 4, regional, and it passes UDP straight through."),
            rafe("Ouch. Let me earn it back. Ask me about guards."),
            nova("First, the basics. A network security group (NSG) is a guest list of allow and deny rules by address and port, on a subnet, a room in your network. An application security group (ASG) is a name tag for a set of VMs, the rented computers, so rules can name the tag."),
            rafe("Guest lists and name tags. Very exclusive."),
            nova("So. The sign-up form is hit by SQL injection. Will an NSG rule stop it?"),
            rafe("No. An NSG never reads the request; the attack arrives on port 443, the usual door for secure web traffic, like any visitor. A WAF stops it."),
            nova("Right answer. You read between the lines. [kiss]"),
            rafe("And the big guard?"),
            nova("Azure Firewall: one central checkpoint in the hub, the shared network in the middle. It can block by domain name. And for crowds: a DDoS attack floods you with fake traffic, like a crowd blocking a shop door. Basic protection is free; Network Protection adds expert help."),
            rafe("My turn to ask. An admin must reach a VM that has no public address, with no ports open to the internet. What do you use?"),
            nova("Azure Bastion, a guarded side door. RDP or SSH, the remote-control screens for Windows and Linux, open right in the browser."),
            rafe("Right answer. Discreet entrances only. [kiss]"),
            nova("Now the walk home. Azure's system routes are the default signposts. A user-defined route, UDR for short, is your own signpost that overrides them, kept in a route table attached to a subnet."),
            rafe("So. All outbound traffic from the spokes must pass through Azure Firewall. What do you configure?"),
            nova("A route table on each spoke subnet, with the all-zeros route, meaning everything else, pointing at the firewall's private address as a virtual appliance. Not an NSG rule: that decides whether, not which way."),
            rafe("Right answer. You always know the way. [kiss]"),
            nova("And forced tunnelling? Where does it go?"),
            rafe("Back to the office first. Internet-bound traffic goes home before it goes out, so the company's own firewall checks it. And accelerated networking is an express lane past the host's software switch, cutting latency, the delay before a message arrives. The office link is in the network connectivity episode."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("Global HTTP with caching: Front Door. URL path routing in one region: Application Gateway. Non-web across regions: Traffic Manager. Non-web in one region: Load Balancer. SQL injection: a WAF, not an NSG. Outbound through a firewall: a route, not an NSG rule."),
            nova("Six for six. That one earns a long one. [kiss]"),
            rafe("Mm. And what shall we try today, darling?"),
            nova("Read the load-balancing decision tree on the Architecture Center and trace the Two Tracks web app through it. Create a virtual network, an NSG and an ASG, and add one rule that targets the ASG. Create a route table, attach it to a subnet, and check a VM's effective routes."),
            rafe("Read the letter, use the side door. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun layers() = DiagramSpec(
        "Layer 4 reads only the envelope, the address and port; layer 7 opens the letter and reads the web request.",
        nodes = listOf(
            DNode("env", "Envelope", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "address and port"),
            DNode("letter", "Letter inside", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the URL path"),
            DNode("l4", "Layer 4", 0.25f, 0.76f, w = 0.42f, h = 0.22f, tone = 1, sub = "Load Balancer"),
            DNode("l7", "Layer 7", 0.75f, 0.76f, w = 0.42f, h = 0.22f, tone = 1, sub = "App Gateway, Front Door"),
        ),
        edges = listOf(
            DEdge("env", "l4", "reads"),
            DEdge("letter", "l7", "reads"),
        ),
        aspect = 0.8f,
    )

    private fun tree() = DiagramSpec(
        "Two questions, web traffic or not and one region or many, point to one of the four load balancers.",
        nodes = listOf(
            DNode("web", "Web traffic?", 0.5f, 0.12f, w = 0.5f, h = 0.16f, tone = 1),
            DNode("gy", "Many regions?", 0.27f, 0.44f, w = 0.4f, h = 0.16f, tone = 2),
            DNode("gn", "Many regions?", 0.75f, 0.44f, w = 0.4f, h = 0.16f, tone = 2),
            DNode("afd", "Front Door", 0.13f, 0.8f, w = 0.23f, h = 0.22f, sub = "L7, global"),
            DNode("agw", "App Gateway", 0.38f, 0.8f, w = 0.23f, h = 0.22f, sub = "L7, regional"),
            DNode("tm", "Traffic Manager", 0.63f, 0.8f, w = 0.23f, h = 0.22f, sub = "DNS, global"),
            DNode("lb", "Load Balancer", 0.88f, 0.8f, w = 0.23f, h = 0.22f, sub = "L4, regional"),
        ),
        edges = listOf(
            DEdge("web", "gy", "yes"),
            DEdge("web", "gn", "no"),
            DEdge("gy", "afd", "yes"),
            DEdge("gy", "agw", "no"),
            DEdge("gn", "tm", "yes"),
            DEdge("gn", "lb", "no"),
        ),
        aspect = 0.8f,
    )

    private fun worldwide() = DiagramSpec(
        "Front Door with a WAF greets users worldwide and caches; each region keeps its VMs behind a Load Balancer.",
        nodes = listOf(
            DNode("users", "Users", 0.5f, 0.12f, w = 0.2f, h = 0.16f, kind = NodeKind.USER),
            DNode("tm", "Traffic Manager: DNS only", 0.82f, 0.12f, w = 0.34f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
            DNode("afd", "Front Door", 0.5f, 0.46f, w = 0.5f, h = 0.2f, tone = 1, sub = "edge, caching, WAF"),
            DNode("eu", "West Europe", 0.25f, 0.82f, w = 0.42f, h = 0.2f, sub = "Load Balancer, VMs"),
            DNode("au", "Australia East", 0.75f, 0.82f, w = 0.42f, h = 0.2f, sub = "Load Balancer, VMs"),
        ),
        edges = listOf(
            DEdge("users", "afd"),
            DEdge("afd", "eu"),
            DEdge("afd", "au"),
        ),
        aspect = 0.8f,
    )

    private fun guards() = DiagramSpec(
        "A WAF reads each web request, NSGs filter by address and port, and Bastion is the admins' side door.",
        nodes = listOf(
            DNode("users", "Visitors", 0.12f, 0.16f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("waf", "WAF", 0.5f, 0.16f, w = 0.36f, h = 0.2f, tone = 1, sub = "reads each request"),
            DNode("ddos", "DDoS Protection", 0.84f, 0.16f, w = 0.3f, h = 0.2f, tone = 2, sub = "absorbs floods"),
            DNode("nsg", "NSG", 0.25f, 0.52f, w = 0.42f, h = 0.2f, sub = "guest list: IP, port"),
            DNode("asg", "ASG", 0.75f, 0.52f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "name tag on VMs"),
            DNode("fw", "Azure Firewall", 0.25f, 0.86f, w = 0.42f, h = 0.2f, tone = 1, sub = "central checkpoint"),
            DNode("bastion", "Azure Bastion", 0.75f, 0.86f, w = 0.42f, h = 0.2f, tone = 2, sub = "admins' side door"),
        ),
        edges = listOf(
            DEdge("users", "waf", "requests"),
            DEdge("waf", "nsg", "then"),
            DEdge("asg", "nsg", "named in"),
            DEdge("ddos", "waf", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun speed() = DiagramSpec(
        "Accelerated networking, proximity placement and Front Door's cache each cut the delay a different way.",
        nodes = listOf(
            DNode("accel", "Accelerated networking", 0.26f, 0.18f, w = 0.46f, h = 0.22f, tone = 1, sub = "express lane"),
            DNode("ppg", "Proximity placement", 0.76f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "one desk"),
            DNode("cache", "Front Door cache", 0.26f, 0.76f, w = 0.46f, h = 0.22f, sub = "copies near users"),
            DNode("lat", "Lower latency", 0.76f, 0.76f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("accel", "lat"),
            DEdge("ppg", "lat"),
            DEdge("cache", "lat"),
        ),
        aspect = 0.8f,
    )

    private fun routes() = DiagramSpec(
        "A route table sends everything else from the app subnet to Azure Firewall, which passes it on or back to the office.",
        nodes = listOf(
            DNode("vm", "App VMs", 0.15f, 0.3f, w = 0.26f, h = 0.2f, sub = "spoke subnet"),
            DNode("fw", "Azure Firewall", 0.5f, 0.3f, w = 0.28f, h = 0.2f, tone = 1, sub = "in the hub"),
            DNode("net", "Internet", 0.85f, 0.3f, w = 0.24f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
            DNode("rt", "Route table", 0.3f, 0.76f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "all zeros to firewall"),
            DNode("office", "Office", 0.8f, 0.76f, w = 0.3f, h = 0.2f, sub = "forced tunnelling"),
        ),
        edges = listOf(
            DEdge("vm", "fw", "UDR"),
            DEdge("fw", "net", "allowed"),
            DEdge("fw", "office", "forced", dashed = true),
            DEdge("rt", "vm", "attached"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's questions on load balancing, security and routing, each as the phrase to listen for and its answer.",
        nodes = listOf(
            DNode("afd", "Global HTTP, caching", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Front Door"),
            DNode("agw", "URL path, one region", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Application Gateway"),
            DNode("tm", "Non-web, many regions", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Traffic Manager"),
            DNode("lb", "Non-web, one region", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Load Balancer"),
            DNode("waf", "SQL injection", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a WAF, not an NSG"),
            DNode("route", "Outbound via firewall", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a route, not an NSG"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("afd", "agw", "tm", "lb", "waf", "route")
    private val listen = BoardCard("Listen for the phrase", "Web or not, one region or many; then the guard or the route each need asks for.")
    private val today = BoardCard("Today", "Trace the decision tree, an NSG rule on an ASG, a route table, effective routes.")
    private val lbCard = BoardCard("Load balancing", "A restaurant host sending each guest to a free table, so no server is swamped.")
    private val layer4 = BoardCard("Layer 4", "Reads only the envelope: the address and the port, a machine's door number.")
    private val layer7 = BoardCard("Layer 7", "Opens the letter: reads the web request, such as the URL path.")
    private val region = BoardCard("Region", "A group of Azure data centres in one part of the world, like West Europe.")
    private val loadBalancer = BoardCard("Azure Load Balancer", "Layer 4, regional: any traffic, even a game's UDP, straight through.")
    private val appGateway = BoardCard("Application Gateway", "Layer 7, regional: routes by URL path and decrypts HTTPS.")
    private val frontDoor = BoardCard("Azure Front Door", "Layer 7, global, at the edge: caches near users, fails over at once.")
    private val trafficManager = BoardCard("Traffic Manager", "Global, DNS only: hands out the best region's number and steps aside.")
    private val slowFailover = BoardCard("Slower failover", "Phones remember the old number for a while. Strength: any traffic.")
    private val twoQuestions = BoardCard("Two questions", "Web traffic? More than one region? The answers pick the balancer.")
    private val wafCard = BoardCard("WAF", "A web application firewall: a guard who reads every request.")
    private val sqli = BoardCard("SQL injection", "Database commands typed into a form, like 'open the safe' on a note.")
    private val nsgCard = BoardCard("NSG", "A guest list of allow and deny rules by address and port. Never reads.")
    private val asgCard = BoardCard("ASG", "A name tag for a set of VMs, so rules name the tag, not addresses.")
    private val fwCard = BoardCard("Azure Firewall", "One central checkpoint in the hub; it can block by domain name.")
    private val ddosCard = BoardCard("DDoS Protection", "Floods of fake traffic absorbed. Basic is free; Network adds expert help.")
    private val bastionCard = BoardCard("Azure Bastion", "A guarded side door: RDP or SSH in the browser, no public address.")
    private val rdpOpen = BoardCard("RDP open to all", "The remote-control port open to the internet: strangers guess passwords.")
    private val udr = BoardCard("User-defined route (UDR)", "Your own signpost, in a route table attached to a subnet.")
    private val allZeros = BoardCard("Everything else to the firewall", "The all-zeros route points at the firewall, a virtual appliance.")
    private val forced = BoardCard("Forced tunnelling", "Internet-bound traffic goes back to the office first, for its firewall.")
    private val notRoute = BoardCard("An NSG is not a route", "It decides whether traffic passes, never which way it goes.")
    private val accel = BoardCard("Accelerated networking", "An express lane past the host's software switch: lower latency.")

    private fun classicBoard() = PodcastBoard(
        T,
        listOf(
            // 0: "Hi, and welcome back. This is the second networks episode..."
            Scene(
                0, "Spreading the load", layers(),
                beats = listOf(
                    Beat(0, card = BoardCard("Two Tracks goes global", "Spread the load, keep attackers out, choose which way traffic flows.")),
                    Beat(2, card = lbCard),
                    Beat(6, show = setOf("env", "l4"), light = setOf("l4"), pulse = "env" to "l4", card = layer4),
                    Beat(8, show = setOf("letter", "l7"), light = setOf("l7"), pulse = "letter" to "l7", card = layer7),
                ),
            ),
            // 9: "And the second idea?"
            Scene(
                9, "The four balancers", tree(),
                beats = listOf(
                    Beat(10, show = setOf("web"), card = region),
                    Beat(12, show = setOf("gn", "lb"), light = setOf("lb"), pulse = "gn" to "lb", card = loadBalancer),
                    Beat(14, card = BoardCard("TCP and UDP", "TCP checks every piece arrives, like registered post. UDP just throws.")),
                    Beat(16, show = setOf("gy", "agw"), light = setOf("agw"), pulse = "gy" to "agw", card = appGateway),
                    Beat(18, card = BoardCard("HTTPS", "Web traffic in a sealed envelope; the gateway opens it for your servers.")),
                    Beat(20, show = setOf("afd"), light = setOf("afd"), pulse = "gy" to "afd", card = frontDoor),
                    Beat(22, show = setOf("tm"), light = setOf("tm"), pulse = "gn" to "tm", card = trafficManager),
                    Beat(24, light = setOf("tm"), card = slowFailover),
                    Beat(26, light = setOf("web", "gy", "gn"), pulse = "web" to "gy", card = twoQuestions),
                ),
            ),
            // 27: "Give me a real one."
            Scene(
                27, "Two Tracks, worldwide", worldwide(),
                beats = listOf(
                    Beat(28, light = setOf("afd", "eu", "au"), pulse = "afd" to "eu", card = BoardCard("The Two Tracks answer", "Web, global, caching: Front Door with a WAF; a Load Balancer per region.")),
                ),
            ),
            // 31: "WAF? Sounds like a dog."
            Scene(
                31, "The guards", guards(),
                beats = listOf(
                    Beat(32, show = setOf("users", "waf"), light = setOf("waf"), pulse = "users" to "waf", card = wafCard),
                    Beat(34, card = sqli),
                    Beat(36, show = setOf("nsg"), light = setOf("nsg"), pulse = "waf" to "nsg", card = nsgCard),
                    Beat(40, show = setOf("asg"), light = setOf("asg"), pulse = "asg" to "nsg", card = asgCard),
                    Beat(42, show = setOf("fw"), light = setOf("fw"), card = fwCard),
                    Beat(44, show = setOf("ddos"), light = setOf("ddos"), pulse = "ddos" to "waf", card = ddosCard),
                    Beat(46, show = setOf("bastion"), light = setOf("bastion"), card = bastionCard),
                ),
            ),
            // 47: "Now speed. Can the network go faster?"
            Scene(
                47, "Speed", speed(),
                beats = listOf(
                    Beat(48, show = setOf("accel", "lat"), light = setOf("accel"), pulse = "accel" to "lat", card = accel),
                    Beat(50, show = setOf("ppg"), light = setOf("ppg"), pulse = "ppg" to "lat", card = BoardCard("Proximity placement group", "VMs close together in one data centre, like a chatty team at one desk.")),
                ),
            ),
            // 51: "Last piece. Routing."
            Scene(
                51, "Signposts", routes(),
                beats = listOf(
                    Beat(52, show = setOf("vm", "rt"), light = setOf("rt"), pulse = "rt" to "vm", card = udr),
                    Beat(54, show = setOf("fw", "net"), light = setOf("fw"), pulse = "vm" to "fw", card = allZeros),
                    Beat(58, show = setOf("office"), light = setOf("office"), pulse = "fw" to "office", card = forced),
                ),
            ),
            // 59: "So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("afd", "waf", "route"), card = listen),
                    Beat(62, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "$T--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Front Door That Wasn't."
            Scene(
                0, "The Front Door That Wasn't", layers(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Slow pictures in Australia, an attacked form, and a VM broken into.")),
                    Beat(2, card = lbCard),
                    Beat(4, show = setOf("env", "l4"), light = setOf("l4"), pulse = "env" to "l4", card = layer4),
                    Beat(6, show = setOf("letter", "l7"), light = setOf("l7"), pulse = "letter" to "l7", card = layer7),
                ),
            ),
            // 7: "Anything else I need?"
            Scene(
                7, "Case one: slow pictures", tree(),
                beats = listOf(
                    Beat(8, show = setOf("web"), card = region),
                    Beat(10, show = setOf("gn", "tm"), light = setOf("tm"), pulse = "gn" to "tm", card = BoardCard("The suspect", "Traffic Manager in front of both regions: slow pictures, long outage.")),
                    Beat(12, light = setOf("tm"), card = trafficManager),
                    Beat(14, card = BoardCard("How it fails", "Phones remember the old number. And there is no caching near users.")),
                    Beat(16, show = setOf("gy", "afd"), light = setOf("afd"), pulse = "gy" to "afd", card = frontDoor),
                ),
            ),
            // 17: "Second crime?"
            Scene(
                17, "Case two: one gateway", tree(),
                beats = listOf(
                    Beat(18, light = setOf("agw"), pulse = "gy" to "agw", card = appGateway),
                    Beat(20, light = setOf("lb"), pulse = "gn" to "lb", card = BoardCard("Regional means regional", "One gateway in Europe fails Australia too. Load Balancer: non-web.")),
                ),
            ),
            // 21: "Third crime. The attack."
            Scene(
                21, "Case three: the attack", guards(),
                beats = listOf(
                    Beat(22, show = setOf("users"), light = setOf("users"), card = sqli),
                    Beat(24, show = setOf("nsg"), light = setOf("nsg"), card = BoardCard("NSG", "Allow and deny by address and port. The attack came on port 443.")),
                    Beat(28, show = setOf("waf"), light = setOf("waf"), pulse = "users" to "waf", card = BoardCard("WAF policy", "Ready-made rules that read each request, on Front Door or App Gateway.")),
                    Beat(30, show = setOf("ddos"), light = setOf("ddos"), pulse = "ddos" to "waf", card = ddosCard),
                ),
            ),
            // 31: "Fourth crime. The break-in."
            Scene(
                31, "Case four: the break-in", guards(),
                beats = listOf(
                    Beat(32, card = rdpOpen),
                    Beat(34, light = setOf("bastion"), card = bastionCard),
                ),
            ),
            // 35: "Please tell me there's no fifth."
            Scene(
                35, "Case five: the signposts", routes(),
                beats = listOf(
                    Beat(36, show = setOf("vm", "fw", "net"), light = setOf("fw"), card = BoardCard("Azure Firewall", "One central checkpoint in the hub; Premium looks inside encrypted traffic.")),
                    Beat(38, card = notRoute),
                    Beat(42, show = setOf("rt"), light = setOf("rt"), pulse = "rt" to "vm", card = udr),
                    Beat(46, show = setOf("office"), light = setOf("office"), pulse = "fw" to "office", card = forced),
                ),
            ),
            // 49: "So, the case summary for the exam?"
            Scene(
                49, "Case closed", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("afd", "waf", "route"), card = listen),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "$T--gossip",
        listOf(
            // 0: "Did you hear about the Two Tracks platform team this week?"
            Scene(
                0, "The platform team's week", layers(),
                beats = listOf(
                    Beat(0, card = BoardCard("Five people, five stories", "Two Tracks goes global, and the platform team argues about balancers.")),
                    Beat(2, card = lbCard),
                    Beat(4, show = setOf("env", "l4"), light = setOf("l4"), pulse = "env" to "l4", card = layer4),
                    Beat(6, show = setOf("letter", "l7"), light = setOf("l7"), pulse = "letter" to "l7", card = layer7),
                ),
            ),
            // 7: "And the second idea?"
            Scene(
                7, "Monday and Tuesday", tree(),
                beats = listOf(
                    Beat(8, show = setOf("web"), card = region),
                    Beat(10, show = setOf("gn", "tm"), light = setOf("tm"), pulse = "gn" to "tm", card = BoardCard("Tom's pick", "Traffic Manager: global, but it only answers the DNS question.")),
                    Beat(12, card = BoardCard("Caching", "Copies of images near users, like a corner shop. Traffic Manager can't.")),
                    Beat(14, show = setOf("gy", "afd"), light = setOf("afd"), pulse = "gy" to "afd", card = BoardCard("The answer for Tom", "Front Door: layer 7, global, caching at the edge, with a WAF policy.")),
                    Beat(16, show = setOf("agw"), light = setOf("agw"), pulse = "gy" to "agw", card = BoardCard("Marcus's pick", "Application Gateway: layer 7, routes by URL path, decrypts HTTPS.")),
                    Beat(18, card = BoardCard("Regional means regional", "One gateway in Europe is a single point of failure for Australia.")),
                    Beat(20, show = setOf("lb"), light = setOf("lb"), pulse = "gn" to "lb", card = loadBalancer),
                ),
            ),
            // 21: "Wednesday?"
            Scene(
                21, "Wednesday: Dana", guards(),
                beats = listOf(
                    Beat(22, show = setOf("users"), light = setOf("users"), card = sqli),
                    Beat(24, show = setOf("nsg"), light = setOf("nsg"), card = nsgCard),
                    Beat(26, show = setOf("waf"), light = setOf("waf"), pulse = "users" to "waf", card = BoardCard("Not at all", "An NSG never reads the request. A WAF does.")),
                    Beat(28, show = setOf("asg"), light = setOf("asg"), pulse = "asg" to "nsg", card = asgCard),
                ),
            ),
            // 29: "[chuckle] I sense Thursday was worse."
            Scene(
                29, "Thursday: the intern", guards(),
                beats = listOf(
                    Beat(30, card = rdpOpen),
                    Beat(32, light = setOf("bastion"), card = bastionCard),
                    Beat(34, light = setOf("ddos"), pulse = "ddos" to "waf", card = ddosCard),
                ),
            ),
            // 35: "Friday. The long one."
            Scene(
                35, "Friday: Priya", routes(),
                beats = listOf(
                    Beat(36, show = setOf("vm", "fw", "net"), light = setOf("fw"), card = fwCard),
                    Beat(40, show = setOf("rt"), light = setOf("rt"), pulse = "rt" to "vm", card = udr),
                    Beat(42, show = setOf("office"), light = setOf("office"), pulse = "fw" to "office", card = forced),
                    Beat(44, card = accel),
                ),
            ),
            // 45: "So, the gossip I need for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("afd", "waf", "route"), card = listen),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "$T--late-night",
        listOf(
            // 0: "Hey, you. It's late, and it's just us."
            Scene(
                0, "Who gets in", layers(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Who gets in, who gets turned away, and which way everyone walks home.")),
                    Beat(2, card = lbCard),
                    Beat(4, show = setOf("env", "l4"), light = setOf("l4"), pulse = "env" to "l4", card = layer4),
                    Beat(6, show = setOf("letter", "l7"), light = setOf("l7"), pulse = "letter" to "l7", card = layer7),
                ),
            ),
            // 7: "Opening letters. How intimate. And the second idea?"
            Scene(
                7, "The four hosts", tree(),
                beats = listOf(
                    Beat(8, show = setOf("web"), card = region),
                    Beat(10, show = setOf("gy", "gn", "lb", "agw"), light = setOf("lb", "agw"), pulse = "gn" to "lb", card = BoardCard("Two regional hosts", "Load Balancer: layer 4, any traffic. App Gateway: layer 7, URL paths.")),
                    Beat(14, show = setOf("afd", "tm"), light = setOf("afd", "tm"), pulse = "gy" to "afd", card = BoardCard("Two global hosts", "Front Door: the edge and caching. Traffic Manager: only answers DNS.")),
                    Beat(16, light = setOf("tm"), card = slowFailover),
                    Beat(18, light = setOf("web"), pulse = "web" to "gy", card = twoQuestions),
                ),
            ),
            // 19: "Now the bouncers."
            Scene(
                19, "The bouncers", guards(),
                beats = listOf(
                    Beat(20, show = setOf("nsg", "asg"), light = setOf("nsg", "asg"), pulse = "asg" to "nsg", card = BoardCard("NSG and ASG", "A guest list by address and port; an ASG is a name tag for VMs.")),
                    Beat(22, show = setOf("users", "waf"), light = setOf("waf"), pulse = "users" to "waf", card = wafCard),
                    Beat(24, show = setOf("fw"), light = setOf("fw"), card = fwCard),
                    Beat(26, show = setOf("ddos"), light = setOf("ddos"), pulse = "ddos" to "waf", card = ddosCard),
                    Beat(28, show = setOf("bastion"), light = setOf("bastion"), card = bastionCard),
                ),
            ),
            // 29: "[sigh] Fine. And the walk home?"
            Scene(
                29, "The walk home", routes(),
                beats = listOf(
                    Beat(30, show = setOf("vm", "rt"), light = setOf("rt"), pulse = "rt" to "vm", card = udr),
                    Beat(32, show = setOf("fw", "net", "office"), light = setOf("fw", "office"), pulse = "fw" to "office", card = forced),
                    Beat(34, card = notRoute),
                ),
            ),
            // 37: "Before you send me off to bed."
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("afd", "waf", "route"), card = listen),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "$T--couples",
        listOf(
            // 0: "Tonight is date night, darling."
            Scene(
                0, "Date night: who gets in", layers(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Nova and Rafe quiz each other: every right answer earns a kiss.")),
                    Beat(4, card = lbCard),
                    Beat(6, show = setOf("env", "l4"), light = setOf("l4"), pulse = "env" to "l4", card = layer4),
                    Beat(8, show = setOf("letter", "l7"), light = setOf("l7"), pulse = "letter" to "l7", card = layer7),
                ),
            ),
            // 9: "Opening letters. Bold. And the second idea?"
            Scene(
                9, "The four balancers", tree(),
                beats = listOf(
                    Beat(12, show = setOf("web", "gy", "gn", "lb", "agw"), light = setOf("lb", "agw"), card = BoardCard("Two regional", "Load Balancer: layer 4. Application Gateway: layer 7, URL paths.")),
                    Beat(14, show = setOf("afd", "tm"), light = setOf("afd", "tm"), card = BoardCard("Two global", "Front Door: edge and caching. Traffic Manager: DNS only, slower failover.")),
                    Beat(15, light = setOf("afd"), pulse = "gy" to "afd", card = BoardCard("Quiz: global web with a WAF", "Answer: Front Door with a WAF policy.")),
                    Beat(19, card = wafCard),
                    Beat(20, card = BoardCard("Quiz: a UDP game, one region", "Which balancer carries a live game's fast, unchecked messages?")),
                    Beat(22, light = setOf("lb"), pulse = "gn" to "lb", card = BoardCard("No kiss", "Application Gateway only speaks web. Load Balancer passes UDP.")),
                ),
            ),
            // 23: "Ouch. Let me earn it back. Ask me about guards."
            Scene(
                23, "The guards", guards(),
                beats = listOf(
                    Beat(24, show = setOf("nsg", "asg"), light = setOf("nsg", "asg"), pulse = "asg" to "nsg", card = BoardCard("NSG and ASG", "A guest list by address and port; an ASG is a name tag for VMs.")),
                    Beat(26, show = setOf("users"), light = setOf("users"), card = BoardCard("Quiz: SQL injection", "Will an NSG rule stop it?")),
                    Beat(27, show = setOf("waf"), light = setOf("waf"), pulse = "users" to "waf", card = BoardCard("A WAF stops it", "An NSG never reads the request; the attack arrives on port 443.")),
                    Beat(30, show = setOf("fw", "ddos"), light = setOf("fw", "ddos"), pulse = "ddos" to "waf", card = BoardCard("Firewall and DDoS", "A central checkpoint in the hub; floods absorbed, basic for free.")),
                    Beat(32, show = setOf("bastion"), light = setOf("bastion"), card = bastionCard),
                ),
            ),
            // 34: "Now the walk home."
            Scene(
                34, "The walk home", routes(),
                beats = listOf(
                    Beat(34, show = setOf("vm", "rt"), light = setOf("rt"), pulse = "rt" to "vm", card = udr),
                    Beat(36, show = setOf("fw", "net"), light = setOf("fw"), pulse = "vm" to "fw", card = allZeros),
                    Beat(39, show = setOf("office"), light = setOf("office"), pulse = "fw" to "office", card = forced),
                ),
            ),
            // 40: "Last question, for the exam."
            Scene(
                40, "For the exam", exam(),
                beats = listOf(
                    Beat(41, show = all6, light = setOf("afd", "waf", "route"), card = listen),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
