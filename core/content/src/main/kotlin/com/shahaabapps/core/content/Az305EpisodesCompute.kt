package com.shahaabapps.core.content

/** Choosing compute: VMs, containers, serverless and batch: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesCompute {
    private val classic = Podcast(
        "sa-compute",
        listOf(
            maya("Hi, and welcome back. Today: choosing compute. Compute just means the place your code runs. Our story is Two Tracks goes global: the Android app is now a company with a backend on Azure, and every piece of that backend needs somewhere to run."),
            leo("And there's more than one somewhere, I take it."),
            maya("A whole ladder. At the bottom you rent a whole computer and look after it yourself: it's like an empty flat. At the top you hand Azure a small function and it runs it when something happens, like ordering a meal. The higher you climb, the less you manage, and the less you control."),
            leo("So which rung does the exam want?"),
            maya("The lowest-effort rung that still meets every requirement. [pause] That's the whole game. The question hides requirement words, and they point to the rung. Let's name the rungs first."),
            leo("Go on."),
            maya("The bottom rung is IaaS, infrastructure as a service. You get virtual machines, VMs for short: computers you rent by the hour in Azure's buildings, and you patch the operating system yourself. The operating system is the base software, like Windows or Linux."),
            leo("The empty flat. And the next rung?"),
            maya("PaaS, platform as a service. A serviced flat: you bring your app, and Azure looks after the machines underneath, patches and all. App Service is the classic one: Azure's hosting for web sites and web APIs."),
            leo("API. Every episode, API. Remind me?"),
            maya("An API is the door other programs knock on to ask for data. The Two Tracks API is the door the phone app knocks on to save a workout. And the top rung is serverless: your code runs only when an event arrives, and you pay only for that time."),
            leo("Okay. Give me requirement words."),
            maya("Picture a small office's old reporting server. It needs a vendor agent installed on the operating system, and it must move to Azure unchanged. Moving unchanged is called lift and shift. Only a VM gives you that control, so that's the answer."),
            leo("And when I pick a VM, do I just pick big?"),
            maya("You pick a size, the shape of the machine: how many processors and how much memory. Sizes come in families named by letters, like clothes cut for different body types. D is general purpose, for web servers. E is memory heavy, for big databases. F is processor heavy. N has graphics chips, for AI and video."),
            leo("[laugh] Fashion for computers. And if I need many VMs?"),
            maya("A VM Scale Set: a group of identical VMs that Azure grows or shrinks on a rule, for example when average processor use passes 70 percent. And Azure Virtual Desktop streams Windows desktops from Azure to staff laptops, like watching a film instead of owning the disc."),
            leo("Now containers. I hear that word a lot."),
            maya("A container is an app packed with everything it needs, like a ready meal in a sealed box: it runs the same anywhere. Four Azure services can run containers, and the exam loves to compare them."),
            leo("Four! Why four?"),
            maya("Because people need different amounts of control. First, Kubernetes. That's the open-source system that runs many containers across many machines, like a kitchen manager deciding which oven heats which box. It works on a cluster, which is a group of computers working as one."),
            leo("And Azure's version of the kitchen manager?"),
            maya("AKS, Azure Kubernetes Service. It's the one where Azure runs the manager and you run the machines: the most control, and the most effort. Pick it when the question says you need the Kubernetes API, the controls of Kubernetes itself."),
            leo("And if my team has never touched Kubernetes?"),
            maya("Container Apps. It's built on Kubernetes but hides it from you, and it scales to zero replicas. A replica is one running copy of your app, like a photocopy, so zero means nothing runs and nothing bills when nobody calls. Perfect for microservices, an app split into small services that each do one job."),
            leo("And the other two?"),
            maya("Container Instances, ACI, runs one container fast, for a short task, billed per second, with no cluster at all, but it doesn't grow by itself. And App Service runs a web app as code or as a container. Simplest for websites."),
            leo("Now serverless. That's Functions?"),
            maya("Azure Functions runs small pieces of code. A trigger is the event that starts one, such as a queue message or a timer, like a doorbell that wakes the cook. Logic Apps is for workflows you draw in a designer, joined to products like Outlook through ready-made connectors."),
            leo("And how do I pay for a function?"),
            maya("The hosting plan decides. Flex Consumption, the serverless plan for new apps, scales to zero and bills per run. Premium keeps copies warm, so there's no cold start, the pause while a sleeping app wakes up."),
            leo("Okay. Last family. Batch?"),
            maya("Say you're building the Two Tracks video feature. Every night, 200,000 uploaded workout videos must become thumbnails, and each video is done on its own. Azure Batch runs huge piles of work like that, like a print shop sharing one big order across many printers."),
            leo("How does it share the order out?"),
            maya("You give it a pool, a set of VMs it creates and grows. Each VM in the pool is called a node: picture a network as dots joined by lines, and each dot is one computer. A job is the whole order; tasks are the single pages. Batch itself costs nothing; you pay for the VMs."),
            leo("Free scheduler. Can it be even cheaper?"),
            maya("Spot VMs. They're Azure's spare machines at a big discount, like standby airline seats. Azure can take them back with 30 seconds' notice, and they have no SLA, no uptime promise. So use them only for work that can restart. An evicted thumbnail task just runs again."),
            leo("And for machines that run all day?"),
            maya("A reservation. You commit to a VM size in a region for one or three years, for a big discount. It's for machines that never sleep, like that old reporting server. And Azure Hybrid Benefit lets you bring Windows Server and SQL Server licences you already own."),
            leo("Put Two Tracks together for me."),
            maya("The API is three container microservices, busy in Europe's evening, idle at 4 a.m., and nobody knows Kubernetes. So Container Apps, scaling to zero. Thumbnails: Batch on spot nodes. Weekly emails: a queue-triggered Function. The reporting server: a reserved D-series VM."),
            leo("Why not AKS for the API? Sounds more serious."),
            maya("AKS needs cluster skills and keeps machines running all night. Container Apps runs the same standard containers with less work, and they can still move to AKS later if the team ever needs it."),
            leo("So what do I remember for the exam?"),
            maya("If the question says install software on the operating system, or move unchanged, the answer is a VM. Containers without managing Kubernetes, or scale to zero: Container Apps. The Kubernetes API: AKS. A website with least management: App Service. Thousands of independent tasks, cheaply: Azure Batch with spot VMs. A machine that runs all the time: a reservation."),
            leo("And anything to try today?"),
            maya("Open the compute decision tree in the Architecture Center and follow it for the Two Tracks API. Deploy the sample container app and set its minimum replicas to zero. Then list VM sizes in your region and find a D, an E and an F."),
            leo("The lowest rung that does the job. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-compute",
        listOf(
            maya("Welcome to Case Files. Today's case: The Bill That Never Slept. Two Tracks has gone global, with its backend on Azure. Then the finance team opened the monthly cloud bill, and it was three times the plan. And the nightly video job still finished late."),
            leo("[chuckle] Detective Leo, on the money trail. Start with the basics. What are they paying for?"),
            maya("Compute, which just means the place your code runs. Azure offers a ladder. At the bottom you rent whole computers and look after them, like an empty flat. At the top you hand over a small function and Azure runs it when something happens, like ordering a meal."),
            leo("And the higher the rung?"),
            maya("The less you manage, and the less you control. The bottom rung is IaaS, infrastructure as a service: VMs, virtual machines, computers rented by the hour, where you patch the operating system, the base software like Linux. The middle is PaaS, platform as a service: you bring the app, Azure runs the machines."),
            leo("And the top?"),
            maya("Serverless. Code runs only when an event arrives, and you pay only for that time. The exam's rule: pick the lowest-effort rung that meets every requirement. Now, the first exhibit: the API."),
            leo("The API is the door the phone app knocks on for data, right?"),
            maya("That's the one. It's three small container services. A container is an app packed with everything it needs, like a ready meal in a sealed box. A contractor put them on AKS, Azure Kubernetes Service."),
            leo("Kubernetes. In plain words?"),
            maya("The open-source system that runs many containers across many machines, like a kitchen manager deciding which oven heats which box. It works on a cluster, which is a group of computers working as one. With AKS, Azure runs the manager, and you run, and pay for, the machines."),
            leo("First suspect, then. Too many copies of the API."),
            maya("[pause] Wrong suspect. Look at 4 a.m. Nobody uses the app, and the cluster's machines are all still running, all still billing. And nobody on the team knows Kubernetes, so upgrades were being skipped."),
            leo("So what should it have been?"),
            maya("Container Apps. It's built on Kubernetes but hides it, and it scales to zero replicas. A replica is one running copy of your app, like a photocopy, so zero means nothing runs and nothing bills at night."),
            leo("Under the picture, how does it wake up?"),
            maya("It watches incoming requests, and when one arrives it starts a copy, then adds more as traffic grows. The first caller waits a moment; that's the catch. And AKS is still right when the question says you need the Kubernetes API, the controls of Kubernetes itself."),
            leo("Second exhibit?"),
            maya("The nightly thumbnails. 200,000 workout videos, each turned into a small picture on its own. The team built a home-made scheduler, a program that hands out work, on four VMs, and bought a three-year reservation for them."),
            leo("A reservation. Meaning?"),
            maya("A commitment to a VM size in a region for one or three years, for a big discount. Great for a machine that runs all day. Those VMs worked two hours a night, and sat idle, paid for, the other twenty-two."),
            leo("[sigh] Second suspect: the VMs were too small. Finish faster."),
            maya("Size changes speed, not the waste. A VM size is the machine's shape, processors and memory, in letter families: D for general work, E for memory, F for processors, N for graphics. But the real clue is the home-made scheduler. Azure already has one, free."),
            leo("Azure Batch."),
            maya("Exactly. Batch runs huge piles of independent work, like a print shop sharing one big order across many printers. You give it a pool, a set of VMs it creates, grows and removes. A job is the whole order; tasks are the single pages. Batch costs nothing itself."),
            leo("And each machine in the pool?"),
            maya("Is a node. Picture a network as dots joined by lines: a node is one dot, here one computer. And the cheap trick is spot nodes. Spot VMs are Azure's spare machines at a big discount, like standby airline seats."),
            leo("What's the catch with standby?"),
            maya("Azure can take them back with 30 seconds' notice, and they have no SLA, no uptime promise. For a thumbnail it doesn't matter: an evicted task just runs again on another node. That's where spot works."),
            leo("Third exhibit."),
            maya("Someone saw how cheap spot was, and moved the reporting database VM onto it. Thursday evening, Azure needed the capacity back. Thirty seconds later the database was gone, and so were the finance reports."),
            leo("So spot stops working where the work can't restart."),
            maya("Exactly. Spot for interruptible work, never for a database. And while we're here, Azure Hybrid Benefit lets you bring Windows Server and SQL Server licences you already own, which cuts the bill legally."),
            leo("Any more crimes?"),
            maya("One. The team's small marketing website ran on two VMs, and someone spent a day every month patching their operating systems. A website with least management points to App Service, Azure's web hosting, on the PaaS rung."),
            leo("And the weekly email to users?"),
            maya("That became Azure Functions: small pieces of code started by a trigger, an event like a queue message or a timer, like a doorbell that wakes the cook. The Flex Consumption plan scales to zero and bills per run."),
            leo("Does serverless ever stop working?"),
            maya("When an app sleeps, the first call waits for a cold start, the pause while it wakes. If that's not acceptable, the Premium plan keeps copies warm. And for workflows joining products like Outlook with ready-made connectors, Logic Apps. The application architecture episode covers the queues."),
            leo("And the old finance reporting server itself?"),
            maya("It needed a vendor agent on the operating system and had to move unchanged, called lift and shift. That's the one honest VM, running all day, so it kept the reservation. For many identical VMs, a VM Scale Set grows them on a rule; the high availability episode spreads one across zones."),
            leo("So, the case summary for the exam?"),
            maya("If the question says install software on the operating system, or move unchanged, the answer is a VM. Containers without managing Kubernetes, or scale to zero: Container Apps. The Kubernetes API: AKS. Thousands of independent tasks, cheaply: Azure Batch with spot VMs. A machine that runs all the time: a reservation."),
            leo("Case closed. Anything to try today?"),
            maya("Open the compute decision tree in the Architecture Center and follow it for the Two Tracks API. Deploy the sample container app and set its minimum replicas to zero. Then, in the portal's VM create page, tick the spot option and read the eviction settings."),
            leo("Right rung, right price. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-compute",
        listOf(
            maya("Okay, did you hear what happened at Two Tracks this week? The company's gone global, and everyone had to choose where their bit of the backend runs on Azure. Choosing compute, they call it. And honestly, the choices."),
            leo("[laugh] I heard the finance team cried. Start me off. Compute means?"),
            maya("The place your code runs. Azure gives you a ladder. At the bottom you rent a whole computer and look after it, like an empty flat. At the top you hand Azure a small function and it runs it when something happens, like ordering a meal."),
            leo("And the rungs have names?"),
            maya("IaaS, infrastructure as a service, is the bottom: VMs, virtual machines, computers rented by the hour, and you patch the operating system, the base software like Windows. PaaS, platform as a service, is the serviced flat: you bring the app, Azure runs the machines. Serverless is the meal."),
            leo("And the rule?"),
            maya("Pick the lowest-effort rung that meets every requirement. Which brings us to Monday. Tom."),
            leo("What did Tom do?"),
            maya("Tom had the marketing website. A plain website. He put it on two VMs, because control. Then he spent every month patching their operating systems. A website with least management points to App Service, Azure's web hosting."),
            leo("Poor Tom. Did any VM choice make sense this week?"),
            maya("One. The finance team's old reporting server needs a vendor agent installed on the operating system and must move unchanged, which is called lift and shift. Only a VM gives that control. Dana sized it."),
            leo("Sized it how?"),
            maya("A VM size is the machine's shape: processors and memory. Families come in letters, like clothes cut for different body types: D general purpose, E memory heavy, F processor heavy, N with graphics chips. Dana picked a D. And for many identical VMs, a VM Scale Set grows them on a rule."),
            leo("Tuesday?"),
            maya("Tuesday was Marcus and the API, the door the phone app knocks on for data. It's three small container services. A container is an app packed with everything it needs, like a ready meal in a sealed box."),
            leo("Where did Marcus put the boxes?"),
            maya("On AKS, Azure Kubernetes Service. Kubernetes is the open-source system that runs many containers across many machines, like a kitchen manager. It works on a cluster, which is a group of computers working as one."),
            leo("Sounds very grown-up."),
            maya("Very. Except nobody on the team knows Kubernetes, the API is idle at 4 a.m., and the cluster's machines bill all night anyway. Marcus picked the most control, when nobody needed the Kubernetes API, the controls of Kubernetes itself."),
            leo("So what should he have used?"),
            maya("Container Apps. Built on Kubernetes, but hidden, and it scales to zero replicas. A replica is one running copy of the app, like a photocopy. Zero means nothing running and nothing billing. Under the picture, it watches incoming requests and starts copies as they arrive."),
            leo("And the other container options? I heard there were four."),
            maya("Container Instances, ACI, runs one container fast for a short task, billed per second, but it doesn't grow by itself. And App Service runs a web app as code or a container. Marcus has a slide on it now."),
            leo("[chuckle] Wednesday?"),
            maya("Priya and the thumbnails. 200,000 workout videos to turn into small pictures every night, each one on its own. Priya wrote her own task scheduler, a program that hands out work, on four VMs."),
            leo("Sounds clever."),
            maya("It was a week of work for something Azure does for free. Azure Batch runs huge piles of independent tasks, like a print shop splitting one big order across many printers. You give it a pool, a set of VMs it creates and grows. Batch itself costs nothing."),
            leo("And each machine in the pool?"),
            maya("Is a node. Picture a network as dots joined by lines: a node is one dot, here one computer. A job is the whole order, tasks are the single pages."),
            leo("Thursday. Go."),
            maya("Thursday was Dana again, on cost. She read about spot VMs, Azure's spare machines at a big discount, like standby airline seats. Azure can take them back with 30 seconds' notice, and they have no SLA, no uptime promise."),
            leo("Perfect for the thumbnails, then."),
            maya("Perfect, because an evicted task just runs again. But Dana also moved the reporting database onto spot. Thursday night, Azure took it back. Thirty seconds' notice. Spot is for work that can restart. Never a database."),
            leo("[sigh] And Friday? There's always a Friday."),
            maya("The intern. The intern bought a reservation, a one- or three-year commitment to a VM size for a big discount, for a test machine that runs two hours a week. Reservations are for machines that run all the time."),
            leo("Ouch. Anything about serverless?"),
            maya("The weekly email went to Azure Functions, small code started by a trigger, an event like a queue message or a timer. Flex Consumption scales to zero and bills per run. Logic Apps is for drawn workflows with ready-made connectors, like Outlook."),
            leo("Where does serverless stop working?"),
            maya("On a cold start, the pause while a sleeping app wakes. If users can't wait, the Premium plan keeps copies warm. And Azure Hybrid Benefit lets you reuse Windows Server and SQL Server licences you own. The high availability episode spreads these VMs across zones."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the question says install software on the operating system, or move unchanged, the answer is a VM. Containers without managing Kubernetes, or scale to zero: Container Apps. The Kubernetes API: AKS. Thousands of independent tasks, cheaply: Azure Batch with spot VMs. A machine that runs all the time: a reservation."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Open the compute decision tree in the Architecture Center and follow it for the Two Tracks API. Deploy the sample container app and set its minimum replicas to zero. Then list VM sizes in your region and find a D, an E and an F."),
            leo("Right rung, Marcus. Right rung. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-compute",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: where your code sleeps, and where it works the night shift."),
            ivy("Mm. My code never sleeps, Nova. Neither do I."),
            nova("[chuckle] Then let's find it a nice place to live. That's choosing compute. Compute just means the place your code runs, and Azure has a whole ladder of places."),
            ivy("A ladder. Climb me through it."),
            nova("At the bottom, you rent a whole computer and look after it yourself, like an empty flat. At the top, you hand Azure a little function and it runs it when something happens, like ordering a meal at midnight. The higher you climb, [pause] the less you manage, and the less you control."),
            ivy("Names for the rungs?"),
            nova("IaaS, infrastructure as a service, is the bottom: VMs, virtual machines, computers rented by the hour, and you patch the operating system, the base software like Linux. PaaS, platform as a service, is the serviced flat: you bring the app, Azure keeps the machines. Serverless runs only when an event arrives."),
            ivy("And how do I choose, at this hour?"),
            nova("The exam's rule: pick the lowest-effort rung that meets every requirement. Take Two Tracks, gone global, with its backend on Azure. The finance team has an old reporting server that needs a vendor agent installed on the operating system, and must move unchanged."),
            ivy("That sounds like it needs a whole flat."),
            nova("It does. Moving unchanged is called lift and shift, and only a VM gives that control. You pick a size, the machine's shape: processors and memory, in letter families. D for general work, E for memory, F for processor, N for graphics chips."),
            ivy("And if I want lots of the same VM?"),
            nova("A VM Scale Set, a group of identical VMs that grows or shrinks on a rule, like adding night staff when the lobby fills. And Azure Virtual Desktop streams Windows desktops from Azure to laptops, like streaming a film instead of owning the disc."),
            ivy("Now the app itself. The API?"),
            nova("The API is the door the phone app knocks on for data. At Two Tracks it's three small container services. A container is an app packed with everything it needs, [pause] like a ready meal in a sealed box."),
            ivy("And who heats the boxes?"),
            nova("Kubernetes, the open-source system that runs many containers across many machines, like a kitchen manager. It works on a cluster, which is a group of computers working as one. AKS, Azure Kubernetes Service, is Azure running the manager while you run the machines."),
            ivy("And if nobody in the house knows Kubernetes?"),
            nova("Then Container Apps. It's built on Kubernetes, but hides it, and it scales to zero replicas. A replica is one running copy, like a photocopy, and zero means nothing runs, nothing bills. The API sleeps at 4 a.m. [pause] Just like you should."),
            ivy("Tell me what really happens when it wakes."),
            nova("Under the picture, Container Apps watches incoming requests. When one arrives, it starts a copy, and adds more as traffic grows. The first caller waits a moment. AKS is only worth it when you need the Kubernetes API itself, the controls of Kubernetes."),
            ivy("And the little ones? Small jobs?"),
            nova("Container Instances, ACI, runs one container fast, for a short task, billed per second, with no cluster. And Azure Functions runs small pieces of code started by a trigger, an event like a queue message or a timer. A doorbell that wakes the cook."),
            ivy("And if the cook is slow to wake?"),
            nova("That's a cold start, the pause while a sleeping app wakes. Flex Consumption, the serverless plan, scales to zero and bills per run, so it can be sleepy. Premium keeps copies warm. And Logic Apps is for workflows you draw, with ready-made connectors to products like Outlook."),
            ivy("Now the night shift. The heavy work."),
            nova("Two Tracks must turn 200,000 workout videos into thumbnails overnight, each on its own. That's Azure Batch, which runs huge piles of independent tasks, like a print shop sharing one big order across many printers."),
            ivy("How does it work, in the dark?"),
            nova("You give it a pool, a set of VMs it creates and grows. Each VM in the pool is a node: picture a network as dots joined by lines, and a node is one dot. A job is the whole order, [pause] tasks are the single pages. Batch costs nothing itself; you pay for the VMs."),
            ivy("And can I pay less? It's late, and I'm frugal."),
            nova("Spot VMs. Azure's spare machines at a big discount, like standby seats on the last flight. Azure can take them back with 30 seconds' notice, and they have no SLA, no uptime promise. An evicted thumbnail task just runs again."),
            ivy("Where does that stop working?"),
            nova("Anywhere the work can't restart. Never a database on spot. [pause] For a machine that runs all day and all night, a reservation: a one- or three-year commitment to a VM size, for a big discount. And Azure Hybrid Benefit lets you reuse licences you already own."),
            ivy("And if a whole building goes dark while I sleep?"),
            nova("Then spread your scale set across zones. The high availability episode is your next late night."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says install software on the operating system, or move unchanged, the answer is a VM. Containers without managing Kubernetes, or scale to zero: Container Apps. The Kubernetes API: AKS. Thousands of independent tasks, cheaply: Azure Batch with spot VMs. A machine that runs all the time: a reservation."),
            ivy("And something to try today, before I sleep?"),
            nova("Open the compute decision tree in the Architecture Center and follow it for the Two Tracks API. [pause] Deploy the sample container app and set its minimum replicas to zero. Then list VM sizes in your region and find a D, an E and an F."),
            ivy("The lowest rung that does the job. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-compute",
        listOf(
            nova("Tonight is date night, darling. Candlelight, and the question every couple faces sooner or later."),
            rafe("Where do we live?"),
            nova("[laugh] Exactly. Where does your code live? It's called choosing compute: compute just means the place your code runs."),
            rafe("And the options?"),
            nova("A ladder. At the bottom, you rent a whole computer and look after it, like an empty flat. At the top, you hand Azure a small function and it runs it when something happens, like ordering dinner in. The higher you climb, the less you manage."),
            rafe("And the less control. I know how that goes."),
            nova("IaaS, infrastructure as a service, is the bottom: VMs, virtual machines, computers rented by the hour, and you patch the operating system, the base software like Windows. PaaS, platform as a service, is the serviced flat: bring your app, Azure does the chores. Serverless is the takeaway."),
            rafe("A partner who does the chores. Marry me. And the rule for the exam?"),
            nova("The lowest-effort rung that meets every requirement. So, a quiz, exam style. Two Tracks has gone global, backend on Azure. The finance team's old reporting server needs a vendor agent on the operating system, and must move unchanged. Which compute?"),
            rafe("A VM. Moving unchanged is lift and shift, and only a VM gives you the operating system."),
            nova("Right answer. Some of us like full control. [kiss]"),
            rafe("And how do I size my VM?"),
            nova("A size is the machine's shape: processors and memory, in letter families, like clothes cut for different bodies. D for general work, E for memory, F for processors, N for graphics. And a VM Scale Set is a group of identical VMs that grows on a rule."),
            rafe("Growing on a rule. Behave. My turn to quiz you. The API, which is the door the phone app knocks on for data, runs as containers. It must scale to zero at night, and the team has no Kubernetes skills. Which host?"),
            nova("First, a container is an app packed with everything it needs, like a ready meal in a sealed box. Kubernetes runs many containers on many machines, on a cluster, which is a group of computers working as one. So the answer is Container Apps."),
            rafe("Why not AKS? Azure Kubernetes Service sounds so committed."),
            nova("AKS needs Kubernetes skills, and its machines bill all night. Container Apps is built on Kubernetes but hides it, and scales to zero replicas. A replica is one running copy, like a photocopy, so zero means nothing bills."),
            rafe("Right answer. Nothing billing while we sleep. [kiss]"),
            nova("Under the picture, it watches incoming requests and starts a copy when one arrives, so the first caller waits a moment. Pick AKS only when you need the Kubernetes API itself."),
            rafe("And the quick option? One container, fast?"),
            nova("[chuckle] Container Instances, ACI. One container, started fast, billed per second, no cluster, but it won't grow by itself. My turn. 200,000 workout videos must become thumbnails overnight, each on its own, as cheaply as possible. What do you recommend?"),
            rafe("Azure Batch. It runs huge piles of independent tasks, like a print shop sharing one order across many printers. You give it a pool, a set of VMs it creates and grows. Each one is a node, one dot in a picture of dots joined by lines."),
            nova("And cheaply?"),
            rafe("Spot nodes. Spot VMs are Azure's spare machines at a big discount, like standby seats. Azure can take them back with 30 seconds' notice, but an evicted task just runs again."),
            nova("Right answer. Cheap, and still worth it. [kiss]"),
            rafe("Then I'll put the reporting database on spot too. Saves a fortune."),
            nova("No kiss for that one. Spot has no SLA, no uptime promise, and 30 seconds' notice. Spot is for work that can restart. A database that runs all the time wants a reservation, a one- or three-year commitment to a VM size for a big discount."),
            rafe("A long-term commitment. I'm listening. And a plain website?"),
            nova("App Service, Azure's web hosting, the PaaS rung. No operating systems to patch. And small bits of code go on Azure Functions: a trigger, an event like a queue message or a timer, starts them, like a doorbell."),
            rafe("And where does serverless let you down?"),
            nova("A cold start, the pause while a sleeping app wakes up. Not your problem, darling. The Premium plan keeps copies warm, and Flex Consumption scales to zero and bills per run. The high availability episode spreads all this across zones."),
            rafe("Then one more quiz. A workflow, drawn in a designer, joins Outlook and other products through ready-made connectors. Which service?"),
            nova("Logic Apps. Functions is for code you write; Logic Apps is for workflows you draw, box by box."),
            rafe("Right answer. You always draw me in. [kiss]"),
            nova("Last question, for the exam. What do we remember?"),
            rafe("If the question says install software on the operating system, or move unchanged, the answer is a VM. Containers without managing Kubernetes, or scale to zero: Container Apps. The Kubernetes API: AKS. Thousands of independent tasks, cheaply: Azure Batch with spot VMs. A machine that runs all the time: a reservation."),
            nova("Five for five, darling. That earns a long one. [kiss]"),
            rafe("Mm. And something to try today?"),
            nova("Open the compute decision tree in the Architecture Center and follow it for the Two Tracks API. Deploy the sample container app and set its minimum replicas to zero. Then list VM sizes in your region and find a D, an E and an F."),
            rafe("The lowest rung, and the warmest copy. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun ladder() = DiagramSpec(
        "The compute ladder: the higher the rung, the less you manage and control. Pick the lowest-effort rung that fits.",
        nodes = listOf(
            DNode("sless", "Serverless", 0.5f, 0.15f, w = 0.8f, h = 0.2f, tone = 1, sub = "Functions, Logic Apps"),
            DNode("paas", "PaaS", 0.5f, 0.5f, w = 0.8f, h = 0.2f, sub = "App Service, Container Apps"),
            DNode("iaas", "IaaS", 0.5f, 0.85f, w = 0.8f, h = 0.2f, tone = 2, sub = "virtual machines"),
        ),
        edges = listOf(
            DEdge("iaas", "paas", "less to manage"),
            DEdge("paas", "sless", "less to manage"),
        ),
        aspect = 0.8f,
    )

    private fun vms() = DiagramSpec(
        "A VM gives full control of the operating system; pick its family and size, and group identical VMs in a scale set.",
        nodes = listOf(
            DNode("lift", "Lift and shift", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "full OS control"),
            DNode("size", "VM size", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "D, E, F, N"),
            DNode("vmss", "VM Scale Set", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "grows on a rule"),
            DNode("avd", "Virtual Desktop", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "streams Windows"),
        ),
        edges = listOf(
            DEdge("lift", "size", "pick"),
            DEdge("size", "vmss", "many of them"),
        ),
        aspect = 0.8f,
    )

    private fun containers() = DiagramSpec(
        "Four hosts run containers: Container Apps hides Kubernetes, AKS gives you its API, ACI runs one box, App Service runs web apps.",
        nodes = listOf(
            DNode("box", "Your containers", 0.5f, 0.12f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
            DNode("aca", "Container Apps", 0.25f, 0.45f, w = 0.42f, h = 0.2f, tone = 1, sub = "scales to zero"),
            DNode("aks", "AKS", 0.75f, 0.45f, w = 0.42f, h = 0.2f, sub = "Kubernetes API"),
            DNode("aci", "Container Instances", 0.25f, 0.8f, w = 0.42f, h = 0.2f, tone = 2, sub = "one quick box"),
            DNode("app", "App Service", 0.75f, 0.8f, w = 0.42f, h = 0.2f, tone = 2, sub = "web apps"),
        ),
        edges = listOf(
            DEdge("box", "aca"),
            DEdge("box", "aks"),
            DEdge("box", "aci"),
            DEdge("box", "app"),
        ),
        aspect = 0.8f,
    )

    private fun serverless() = DiagramSpec(
        "A trigger starts a function, the hosting plan decides how it runs and bills, and Logic Apps draws workflows.",
        nodes = listOf(
            DNode("trig", "Trigger", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "queue or timer"),
            DNode("fn", "Function", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "small code"),
            DNode("logic", "Logic Apps", 0.25f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "drawn workflow"),
            DNode("plan", "Hosting plan", 0.75f, 0.76f, w = 0.42f, h = 0.2f, sub = "Flex or Premium"),
        ),
        edges = listOf(
            DEdge("trig", "fn", "starts"),
            DEdge("fn", "plan", "runs on"),
        ),
        aspect = 0.8f,
    )

    private fun batch() = DiagramSpec(
        "Batch reads the videos, splits the job into tasks across a pool of nodes, and writes the thumbnails back.",
        nodes = listOf(
            DNode("in", "Videos", 0.17f, 0.32f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
            DNode("pool", "Batch pool", 0.5f, 0.32f, w = 0.3f, h = 0.3f, tone = 1, sub = "nodes"),
            DNode("out", "Thumbnails", 0.83f, 0.32f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
            DNode("note", "One job, thousands of independent tasks", 0.5f, 0.82f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("in", "pool", "tasks"),
            DEdge("pool", "out", "results"),
        ),
        aspect = 0.8f,
    )

    private fun cost() = DiagramSpec(
        "Spot VMs suit work that can restart; a reservation suits a machine that runs all the time.",
        nodes = listOf(
            DNode("spot", "Spot VM", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "cheap, can be evicted"),
            DNode("res", "Reservation", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "1 or 3 years"),
            DNode("restart", "Work that can restart", 0.25f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("always", "Runs all the time", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("restart", "spot"),
            DEdge("always", "res"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's compute questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("os", "OS control", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a virtual machine"),
            DNode("k8s", "Kubernetes API", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "AKS"),
            DNode("zero", "No Kubernetes skills", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Apps"),
            DNode("web", "Website, least work", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "App Service"),
            DNode("tasks", "Thousands of tasks", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Batch with spot"),
            DNode("always", "Runs all the time", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a reservation"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("os", "k8s", "zero", "web", "tasks", "always")
    private val allHosts = setOf("box", "aca", "aks", "aci", "app")
    private val today = BoardCard("Today", "The decision tree, a container app at zero replicas, and VM sizes.")
    private val takeaways = BoardCard("Listen for the phrase", "OS: a VM. No Kubernetes skills: Container Apps. Its API: AKS. Many tasks: Batch.")

    private fun classicBoard() = PodcastBoard(
        "sa-compute",
        listOf(
            // 0: "Hi, and welcome back. Today: choosing compute..."
            Scene(
                0, "The compute ladder", ladder(),
                beats = listOf(
                    Beat(0, card = BoardCard("Compute", "The place your code runs. Two Tracks needs several.")),
                    Beat(2, show = setOf("iaas", "sless"), light = setOf("iaas", "sless"), card = BoardCard("A ladder", "Bottom: an empty flat you look after. Top: a meal you order.")),
                    Beat(4, card = BoardCard("The exam's rule", "The lowest-effort rung that meets every requirement.")),
                    Beat(6, light = setOf("iaas"), card = BoardCard("IaaS", "Infrastructure as a service: VMs, and you patch the OS.")),
                    Beat(8, show = setOf("paas"), light = setOf("paas"), pulse = "iaas" to "paas", card = BoardCard("PaaS", "Platform as a service: bring the app; Azure runs the machines.")),
                    Beat(10, light = setOf("sless"), pulse = "paas" to "sless", card = BoardCard("API and serverless", "API: the door apps knock on. Serverless: code that runs on events.")),
                ),
            ),
            // 11: "Okay. Give me requirement words."
            Scene(
                11, "Virtual machines", vms(),
                beats = listOf(
                    Beat(12, show = setOf("lift"), light = setOf("lift"), card = BoardCard("Lift and shift", "An agent on the OS, moved unchanged: only a VM allows it.")),
                    Beat(14, show = setOf("size"), light = setOf("size"), pulse = "lift" to "size", card = BoardCard("VM size", "The machine's shape. D general, E memory, F processor, N graphics.")),
                    Beat(16, show = setOf("vmss", "avd"), light = setOf("vmss", "avd"), pulse = "size" to "vmss", card = BoardCard("VM Scale Set", "Identical VMs that grow or shrink on a rule, like CPU over 70%.")),
                ),
            ),
            // 17: "Now containers. I hear that word a lot."
            Scene(
                17, "Four container hosts", containers(),
                beats = listOf(
                    Beat(18, show = setOf("box"), light = setOf("box"), card = BoardCard("Container", "An app packed with all it needs, like a ready meal in a box.")),
                    Beat(20, card = BoardCard("Kubernetes and cluster", "Runs many containers on many machines; a cluster works as one.")),
                    Beat(22, show = setOf("aks"), light = setOf("aks"), pulse = "box" to "aks", card = BoardCard("AKS", "Azure runs the manager, you run the machines. The Kubernetes API.")),
                    Beat(24, show = setOf("aca"), light = setOf("aca"), pulse = "box" to "aca", card = BoardCard("Container Apps", "Hidden Kubernetes; scales to zero replicas, zero running copies.")),
                    Beat(26, show = setOf("aci", "app"), light = setOf("aci", "app"), card = BoardCard("ACI and App Service", "One quick container billed per second; or a web app, simplest.")),
                ),
            ),
            // 27: "Now serverless. That's Functions?"
            Scene(
                27, "Serverless", serverless(),
                beats = listOf(
                    Beat(28, show = setOf("trig", "fn", "logic"), light = setOf("trig", "fn"), pulse = "trig" to "fn", card = BoardCard("Trigger", "The event that starts a function: a queue message or a timer.")),
                    Beat(30, show = setOf("plan"), light = setOf("plan"), pulse = "fn" to "plan", card = BoardCard("Hosting plan", "Flex Consumption: scale to zero. Premium: warm, no cold start.")),
                ),
            ),
            // 31: "Okay. Last family. Batch?"
            Scene(
                31, "Azure Batch", batch(),
                beats = listOf(
                    Beat(32, show = setOf("in"), light = setOf("in"), card = BoardCard("Azure Batch", "200,000 videos, each on its own: one job, many tasks.")),
                    Beat(34, show = setOf("pool", "out"), light = setOf("pool"), pulse = "in" to "pool", card = BoardCard("Pool and node", "A pool of VMs Batch creates; each VM is a node, one dot.")),
                    Beat(36, pulse = "pool" to "out", card = BoardCard("Spot VMs", "Spare machines, big discount; taken back with 30 seconds' notice.")),
                ),
            ),
            // 37: "And for machines that run all day?"
            Scene(
                37, "Paying less", cost(),
                beats = listOf(
                    Beat(37, show = setOf("spot", "restart"), light = setOf("spot"), pulse = "restart" to "spot", card = BoardCard("Spot", "Only for work that can restart, like a thumbnail task.")),
                    Beat(38, show = setOf("res", "always"), light = setOf("res"), pulse = "always" to "res", card = BoardCard("Reservation", "One or three years on a VM size, for machines that never sleep.")),
                ),
            ),
            // 39: "Put Two Tracks together for me."
            Scene(
                39, "Two Tracks, put together", containers(),
                beats = listOf(
                    Beat(40, show = allHosts, light = setOf("aca"), pulse = "box" to "aca", card = BoardCard("The Two Tracks API", "Container Apps: idle at 4 a.m., and no Kubernetes skills.")),
                    Beat(42, light = setOf("aks"), card = BoardCard("Why not AKS", "Cluster skills and machines all night. It can still move later.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("os", "zero", "k8s", "tasks"), card = takeaways),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-compute--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Bill That Never Slept..."
            Scene(
                0, "The Bill That Never Slept", ladder(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "The cloud bill: three times the plan. The video job: still late.")),
                    Beat(2, show = setOf("iaas", "sless"), light = setOf("iaas", "sless"), card = BoardCard("Compute", "The place your code runs: a ladder from flat to meal.")),
                    Beat(4, show = setOf("paas"), light = setOf("iaas", "paas"), pulse = "iaas" to "paas", card = BoardCard("IaaS and PaaS", "VMs you patch, or bring the app and Azure runs the machines.")),
                    Beat(6, light = setOf("sless"), pulse = "paas" to "sless", card = BoardCard("The rule", "The lowest-effort rung that meets every requirement.")),
                ),
            ),
            // 7: "The API is the door the phone app knocks on for data, right?"
            Scene(
                7, "Exhibit one: the API", containers(),
                beats = listOf(
                    Beat(8, show = setOf("box", "aks"), light = setOf("aks"), pulse = "box" to "aks", card = BoardCard("Containers on AKS", "Ready meals in boxes, on Azure Kubernetes Service.")),
                    Beat(10, card = BoardCard("Kubernetes and cluster", "A kitchen manager for containers, on a group of computers.")),
                    Beat(12, card = BoardCard("Suspect cleared: copies", "At 4 a.m. the cluster's machines still run, and still bill.")),
                    Beat(14, show = setOf("aca"), light = setOf("aca"), pulse = "box" to "aca", card = BoardCard("Container Apps", "Hidden Kubernetes; scales to zero replicas, so nothing bills.")),
                    Beat(16, show = setOf("aci", "app"), card = BoardCard("Under the picture", "Watches requests and starts copies. AKS when you need its API.")),
                ),
            ),
            // 17: "Second exhibit?"
            Scene(
                17, "Exhibit two: thumbnails", cost(),
                beats = listOf(
                    Beat(18, show = setOf("res"), light = setOf("res"), card = BoardCard("A home-made scheduler", "Four VMs on a three-year reservation, busy two hours a night.")),
                    Beat(20, show = setOf("always"), light = setOf("always", "res"), pulse = "always" to "res", card = BoardCard("Reservation", "One or three years on a VM size: for machines that run all day.")),
                ),
            ),
            // 21: "Second suspect: the VMs were too small."
            Scene(
                21, "Azure Batch", batch(),
                beats = listOf(
                    Beat(22, card = BoardCard("Suspect cleared: VM size", "Size changes speed, not waste. Azure already has a scheduler.")),
                    Beat(24, show = setOf("in", "pool", "out"), light = setOf("pool"), pulse = "in" to "pool", card = BoardCard("Azure Batch", "A pool of VMs; one job, many tasks. Batch itself is free.")),
                    Beat(26, light = setOf("pool"), card = BoardCard("Node and spot", "A node is one computer in the pool. Spot: spare and discounted.")),
                    Beat(28, pulse = "pool" to "out", card = BoardCard("Evicted? Retried", "30 seconds' notice, no SLA; a thumbnail task just runs again.")),
                ),
            ),
            // 29: "Third exhibit."
            Scene(
                29, "Exhibit three: spot", cost(),
                beats = listOf(
                    Beat(30, show = setOf("spot"), light = setOf("spot"), card = BoardCard("A database on spot", "Taken back with 30 seconds' notice. The reports were gone.")),
                    Beat(32, show = setOf("restart"), light = setOf("restart", "spot"), pulse = "restart" to "spot", card = BoardCard("Spot for restartable work", "Never a database. Hybrid Benefit reuses licences you own.")),
                ),
            ),
            // 33: "Any more crimes?"
            Scene(
                33, "The website and the email", serverless(),
                beats = listOf(
                    Beat(34, card = BoardCard("A website on VMs", "Patched every month. App Service: the least management.")),
                    Beat(36, show = setOf("trig", "fn"), light = setOf("fn"), pulse = "trig" to "fn", card = BoardCard("Azure Functions", "Small code started by a trigger: a queue message or a timer.")),
                    Beat(38, show = setOf("plan", "logic"), light = setOf("plan"), pulse = "fn" to "plan", card = BoardCard("Cold start", "Flex scales to zero; Premium keeps copies warm.")),
                ),
            ),
            // 39: "And the old finance reporting server itself?"
            Scene(
                39, "The honest VM", vms(),
                beats = listOf(
                    Beat(40, show = setOf("lift", "size", "vmss", "avd"), light = setOf("lift"), pulse = "lift" to "size", card = BoardCard("Lift and shift", "An agent on the OS, moved unchanged: a VM, and it's reserved.")),
                ),
            ),
            // 41: "So, the case summary for the exam?"
            Scene(
                41, "Case closed", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("os", "zero", "k8s", "tasks", "always"), card = takeaways),
                    Beat(44, card = BoardCard("Today", "The decision tree, a container app at zero, the spot settings.")),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-compute--gossip",
        listOf(
            // 0: "Okay, did you hear what happened at Two Tracks this week?..."
            Scene(
                0, "A week of choices", ladder(),
                beats = listOf(
                    Beat(0, card = BoardCard("Choosing compute", "Everyone picked where their part of the backend runs.")),
                    Beat(2, show = setOf("iaas", "sless"), light = setOf("iaas", "sless"), card = BoardCard("The ladder", "An empty flat at the bottom, a meal you order at the top.")),
                    Beat(4, show = setOf("paas"), light = setOf("paas"), pulse = "iaas" to "paas", card = BoardCard("IaaS, PaaS, serverless", "VMs you patch; an app Azure runs; code that runs on events.")),
                    Beat(6, card = BoardCard("The rule", "The lowest-effort rung that meets every requirement.")),
                    Beat(8, light = setOf("paas"), card = BoardCard("Monday: Tom", "A plain website on VMs. App Service would mean no patching.")),
                ),
            ),
            // 9: "Poor Tom. Did any VM choice make sense this week?"
            Scene(
                9, "Dana's VM", vms(),
                beats = listOf(
                    Beat(10, show = setOf("lift"), light = setOf("lift"), card = BoardCard("Lift and shift", "An agent on the OS, moved unchanged: only a VM.")),
                    Beat(12, show = setOf("size", "vmss"), light = setOf("size"), pulse = "lift" to "size", card = BoardCard("VM size", "D general, E memory, F processor, N graphics. Dana chose D.")),
                ),
            ),
            // 13: "Tuesday?"
            Scene(
                13, "Tuesday: Marcus", containers(),
                beats = listOf(
                    Beat(14, show = setOf("box"), light = setOf("box"), card = BoardCard("API and container", "The door the app knocks on; an app packed like a ready meal.")),
                    Beat(16, show = setOf("aks"), light = setOf("aks"), pulse = "box" to "aks", card = BoardCard("AKS", "Kubernetes on a cluster: Azure runs the manager, you the machines.")),
                    Beat(18, card = BoardCard("Too much control", "No Kubernetes skills, idle at 4 a.m., machines billing all night.")),
                    Beat(20, show = setOf("aca"), light = setOf("aca"), pulse = "box" to "aca", card = BoardCard("Container Apps", "Hidden Kubernetes; zero replicas means nothing billing.")),
                    Beat(22, show = setOf("aci", "app"), light = setOf("aci", "app"), card = BoardCard("ACI and App Service", "One quick container; or a web app as code or a container.")),
                ),
            ),
            // 23: "Wednesday?"
            Scene(
                23, "Wednesday: Priya", batch(),
                beats = listOf(
                    Beat(24, show = setOf("in"), light = setOf("in"), card = BoardCard("A home-made scheduler", "A week of work on four VMs, for 200,000 thumbnails.")),
                    Beat(26, show = setOf("pool", "out"), light = setOf("pool"), pulse = "in" to "pool", card = BoardCard("Azure Batch", "A pool of VMs; one job, many tasks. Batch itself is free.")),
                    Beat(28, pulse = "pool" to "out", card = BoardCard("Node", "One computer in the pool: one dot in a network.")),
                ),
            ),
            // 29: "Thursday. Go."
            Scene(
                29, "Thursday and Friday", cost(),
                beats = listOf(
                    Beat(30, show = setOf("spot", "restart"), light = setOf("spot"), pulse = "restart" to "spot", card = BoardCard("Spot VMs", "Spare and cheap; taken back with 30 seconds' notice, no SLA.")),
                    Beat(32, light = setOf("restart"), card = BoardCard("Never a database", "Dana's reporting database: gone in thirty seconds.")),
                    Beat(34, show = setOf("res", "always"), light = setOf("res"), pulse = "always" to "res", card = BoardCard("Friday: a reservation", "For a test machine running two hours a week. Wrong fit.")),
                ),
            ),
            // 35: "Ouch. Anything about serverless?"
            Scene(
                35, "Serverless", serverless(),
                beats = listOf(
                    Beat(36, show = setOf("trig", "fn", "logic"), light = setOf("fn"), pulse = "trig" to "fn", card = BoardCard("Azure Functions", "Started by a trigger; Flex Consumption bills per run.")),
                    Beat(38, show = setOf("plan"), light = setOf("plan"), pulse = "fn" to "plan", card = BoardCard("Cold start", "The pause while a sleeping app wakes; Premium keeps it warm.")),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("os", "zero", "k8s", "tasks", "always"), card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-compute--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "The night shift", ladder(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Where your code sleeps, and where it works the night shift.")),
                    Beat(2, card = BoardCard("Compute", "The place your code runs.")),
                    Beat(4, show = setOf("iaas", "sless"), light = setOf("iaas", "sless"), card = BoardCard("The ladder", "The higher you climb, the less you manage and control.")),
                    Beat(6, show = setOf("paas"), light = setOf("paas"), pulse = "iaas" to "paas", card = BoardCard("IaaS and PaaS", "VMs you patch; or bring the app and Azure keeps the machines.")),
                    Beat(8, light = setOf("iaas"), card = BoardCard("The rule", "The lowest-effort rung that meets every requirement.")),
                ),
            ),
            // 9: "That sounds like it needs a whole flat."
            Scene(
                9, "A whole flat", vms(),
                beats = listOf(
                    Beat(10, show = setOf("lift", "size"), light = setOf("lift"), pulse = "lift" to "size", card = BoardCard("Lift and shift", "Moved unchanged with an OS agent: only a VM. Then pick a size.")),
                    Beat(12, show = setOf("vmss", "avd"), light = setOf("vmss"), pulse = "size" to "vmss", card = BoardCard("VM Scale Set", "Identical VMs that grow on a rule, like extra night staff.")),
                ),
            ),
            // 13: "Now the app itself. The API?"
            Scene(
                13, "Boxes in the kitchen", containers(),
                beats = listOf(
                    Beat(14, show = setOf("box"), light = setOf("box"), card = BoardCard("API and container", "The door the app knocks on; an app packed in a sealed box.")),
                    Beat(16, show = setOf("aks"), light = setOf("aks"), pulse = "box" to "aks", card = BoardCard("Kubernetes and AKS", "Runs containers on a cluster; Azure runs the manager.")),
                    Beat(18, show = setOf("aca"), light = setOf("aca"), pulse = "box" to "aca", card = BoardCard("Container Apps", "Hidden Kubernetes; zero replicas at 4 a.m., nothing billing.")),
                    Beat(20, show = setOf("aci", "app"), card = BoardCard("When it wakes", "It starts a copy as a request arrives; the first caller waits.")),
                ),
            ),
            // 21: "And the little ones? Small jobs?"
            Scene(
                21, "Small jobs", serverless(),
                beats = listOf(
                    Beat(22, show = setOf("trig", "fn"), light = setOf("fn"), pulse = "trig" to "fn", card = BoardCard("ACI and Functions", "One quick container; or small code started by a trigger.")),
                    Beat(24, show = setOf("plan", "logic"), light = setOf("plan"), pulse = "fn" to "plan", card = BoardCard("Cold start", "Flex sleeps and bills per run; Premium keeps copies warm.")),
                ),
            ),
            // 25: "Now the night shift. The heavy work."
            Scene(
                25, "The heavy work", batch(),
                beats = listOf(
                    Beat(26, show = setOf("in"), light = setOf("in"), card = BoardCard("Azure Batch", "200,000 videos, each on its own, overnight.")),
                    Beat(28, show = setOf("pool", "out"), light = setOf("pool"), pulse = "in" to "pool", card = BoardCard("Pool and node", "VMs Batch creates; each is a node. One job, many tasks.")),
                ),
            ),
            // 29: "And can I pay less?"
            Scene(
                29, "Paying less", cost(),
                beats = listOf(
                    Beat(30, show = setOf("spot", "restart"), light = setOf("spot"), pulse = "restart" to "spot", card = BoardCard("Spot VMs", "Spare seats on the last flight: 30 seconds' notice, no SLA.")),
                    Beat(32, show = setOf("res", "always"), light = setOf("res"), pulse = "always" to "res", card = BoardCard("Reservation", "One or three years, for machines that run all night.")),
                    Beat(34, card = BoardCard("Your next late night", "The high availability episode spreads scale sets over zones.")),
                ),
            ),
            // 35: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("os", "zero", "k8s", "tasks", "always"), card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-compute--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Where do we live?", ladder(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Where does your code live? Choosing compute.")),
                    Beat(4, show = setOf("iaas", "sless"), light = setOf("iaas", "sless"), card = BoardCard("The ladder", "Bottom: an empty flat. Top: dinner ordered in.")),
                    Beat(6, show = setOf("paas"), light = setOf("paas"), pulse = "iaas" to "paas", card = BoardCard("IaaS, PaaS, serverless", "VMs you patch; Azure does the chores; code on events.")),
                ),
            ),
            // 8: "The lowest-effort rung that meets every requirement. So, a quiz..."
            Scene(
                8, "Quiz: the old server", vms(),
                beats = listOf(
                    Beat(8, show = setOf("lift"), light = setOf("lift"), card = BoardCard("Quiz: an agent on the OS", "Moved unchanged. Which compute?")),
                    Beat(9, card = BoardCard("A VM", "Lift and shift needs the operating system itself.")),
                    Beat(12, show = setOf("size", "vmss"), light = setOf("size", "vmss"), pulse = "lift" to "size", card = BoardCard("VM size and scale set", "D, E, F, N families; identical VMs that grow on a rule.")),
                ),
            ),
            // 13: "Growing on a rule. Behave. My turn to quiz you..."
            Scene(
                13, "Quiz: the API", containers(),
                beats = listOf(
                    Beat(13, show = setOf("box"), light = setOf("box"), card = BoardCard("Quiz: scale to zero", "Containers, and no Kubernetes skills. Which host?")),
                    Beat(14, show = setOf("aca"), light = setOf("aca"), pulse = "box" to "aca", card = BoardCard("Container Apps", "Hidden Kubernetes on a cluster; zero replicas, nothing bills.")),
                    Beat(15, show = setOf("aks"), light = setOf("aks"), pulse = "box" to "aks", card = BoardCard("Why not AKS", "Kubernetes skills, and machines billing all night.")),
                    Beat(18, light = setOf("aca"), card = BoardCard("Under the picture", "A request arrives, a copy starts; the first caller waits.")),
                    Beat(20, show = setOf("aci", "app"), light = setOf("aci"), card = BoardCard("ACI", "One container, fast, billed per second; won't grow by itself.")),
                ),
            ),
            // 21: "Azure Batch. It runs huge piles of independent tasks..."
            Scene(
                21, "Quiz: thumbnails", batch(),
                beats = listOf(
                    Beat(21, show = setOf("in", "pool", "out"), light = setOf("pool"), pulse = "in" to "pool", card = BoardCard("Azure Batch", "One job, many independent tasks, on a pool of nodes.")),
                    Beat(23, pulse = "pool" to "out", card = BoardCard("Spot nodes", "Spare machines, cheap; an evicted task just runs again.")),
                ),
            ),
            // 25: "Then I'll put the reporting database on spot too."
            Scene(
                25, "No kiss for that one", cost(),
                beats = listOf(
                    Beat(25, show = setOf("spot"), light = setOf("spot"), card = BoardCard("A database on spot?", "30 seconds' notice and no SLA. No kiss.")),
                    Beat(26, show = setOf("restart", "res", "always"), light = setOf("res"), pulse = "always" to "res", card = BoardCard("Reservation", "One or three years, for a database that runs all the time.")),
                ),
            ),
            // 27: "A long-term commitment. I'm listening. And a plain website?"
            Scene(
                27, "Websites and small code", serverless(),
                beats = listOf(
                    Beat(28, show = setOf("trig", "fn"), light = setOf("fn"), pulse = "trig" to "fn", card = BoardCard("App Service and Functions", "A plain website; small code started by a trigger.")),
                    Beat(30, show = setOf("plan"), light = setOf("plan"), pulse = "fn" to "plan", card = BoardCard("Cold start", "Premium keeps copies warm; Flex scales to zero.")),
                    Beat(32, show = setOf("logic"), light = setOf("logic"), card = BoardCard("Logic Apps", "Workflows you draw, joined by ready-made connectors.")),
                ),
            ),
            // 34: "Last question, for the exam. What do we remember?"
            Scene(
                34, "For the exam", exam(),
                beats = listOf(
                    Beat(35, show = all6, light = setOf("os", "zero", "k8s", "tasks", "always"), card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
