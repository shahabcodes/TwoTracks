package com.shahaabapps.core.content

/** Azure Kubernetes Service: deploy with manifest files: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAks {
    private val classic = Podcast(
        "ai-aks",
        listOf(
            maya("Hi, and welcome back. Zaiqa is running: the Android app, the partner dashboard, and the Zaiqa API, which is the backend the phone calls for everything. Its image waits in ACR, Azure Container Registry, and Container Apps runs it. Today, the day that stops being enough."),
            leo("Container Apps has been good to us, though. Ingress, scaling at dinner time, revisions. What breaks?"),
            maya("Nothing breaks, and that is the point. Ingress is the front door that lets traffic in from outside. A revision is one numbered version of your app. Container Apps gives you both free. The trouble starts with the Taste service."),
            leo("The one that answers, something like dal makhani, but vegan?"),
            maya("That one. It turns dishes into lists of numbers, and that work wants a graphics chip. Container Apps will not rent you one. Reason two: say you're building something that writes its own rules into the platform. Container Apps keeps the platform away from you, on purpose."),
            leo("Fine. So what is Kubernetes, in plain words?"),
            maya("A system that runs lots of containers across lots of machines and keeps them running. Picture a kitchen manager. You say, three cooks on the grill. One goes home sick, and the manager calls another in, without asking you."),
            leo("So AKS is that manager, and Azure rents it to us?"),
            maya("Half of it. AKS is Azure Kubernetes Service. Kubernetes runs on a cluster, which is a group of computers working as one, like one restaurant's whole staff. A cluster has two halves, and Azure keeps one."),
            leo("Which half does Azure keep?"),
            maya("The control plane, the brain. Think of head office: it remembers what should be running and gives the orders. Azure runs it, patches it, keeps it alive. You never log in."),
            leo("And the half I get?"),
            maya("The nodes. A node is a single dot in a network, and in a cluster each dot is one computer, a virtual machine you rent by the hour. Your containers run there. You pay for nodes, not the brain."),
            leo("So I am renting machines again. I thought we had stopped doing that."),
            maya("We had. That is the first thing you give up. Nodes come in node pools, which are groups of identical machines. Zaiqa gets three: a system pool for Kubernetes own parts, a user pool for our containers, and a graphics pool for the Taste service."),
            leo("Right. So how do I tell the cluster what I want?"),
            maya("You write a manifest, which is a file saying what should be running. An order form: three copies of this image, this port, this much memory. You send it with kubectl apply, and the cluster makes reality match the file. Change the file, apply again, and it moves."),
            leo("So what goes on the order form?"),
            maya("Three things. A Pod, a Deployment and a Service. A pod is the smallest thing Kubernetes runs: a wrapper round one or more containers sharing one address. Like a pea pod."),
            leo("And a Deployment?"),
            maya("The shift manager. It says, keep three identical pods of the Zaiqa API running from this image. A replica is a copy identical to the rest, so replicas three means three pods."),
            leo("And the Service is the phone number, I am guessing."),
            maya("Exactly. Pods come and go, and each new pod gets a new address, so nobody calls a pod directly. A Service is one stable address in front of them, and it finds them by label, which is a name tag stuck on a pod."),
            leo("What do our name tags say?"),
            maya("The Zaiqa API pods wear app equals zaiqa-api. The Service says, send traffic to every pod wearing that tag. Get one letter wrong and the Service points at nothing, and the app goes dark while every pod sits there perfectly healthy. [pause]"),
            leo("[sigh] A bad afternoon. And how does the outside world reach that Service?"),
            maya("Through an ingress controller, which is a pod whose whole job is to be the front door: it takes each request from the internet to the right Service inside. On Container Apps that came free. Here you install it and patch it."),
            leo("Thing you give up, number two."),
            maya("Number two. Next, sizing. For each container you write requests and limits. A request is what the pod needs before it is let in: a quarter of a processor, 256 megabytes. A limit is the ceiling it may never pass."),
            leo("And what are those two actually for?"),
            maya("The request chooses the node: the cluster puts your pod where there is room. Ask too little and pods pile onto one machine and fight. Past the memory limit a pod is killed and restarted; past the processor limit it just runs slower."),
            leo("Okay. Friday, eight in the evening, everybody orders at once. Who adds machines?"),
            maya("Two different things, and the exam loves the difference. One autoscaler adds pods when the pods get busy. The cluster autoscaler adds nodes when pods have nowhere to go. More cooks, or a bigger kitchen."),
            leo("How does it know there is nowhere to go?"),
            maya("The new pods sit in Pending, which means waiting for room. The cluster autoscaler sees them, adds a machine to the pool, and they start. When dinner is over it takes it away again."),
            leo("And if I ship a bad build in the middle of all that?"),
            maya("Change the image tag, apply, and the Deployment swaps pods a few at a time, so the app never goes fully down. If the build is wrong, kubectl rollout undo puts the last version back. Then fix the file, or the next apply breaks it again."),
            leo("Noted. Now the thing that always bites me. Pods that will not start, saying ImagePullBackOff."),
            maya("They cannot download the image, and are waiting before trying again. Nine times in ten it is permission: our images are private, and nobody told the cluster it may take them."),
            leo("So a password goes in the manifest?"),
            maya("No. You run az aks update with attach ACR, and the registry grants AcrPull, which is permission to download images and nothing else, to the cluster's kubelet identity."),
            leo("Kubelet identity. Plain words, please."),
            maya("The kubelet is the agent on every node that actually starts containers. Its identity is the badge those agents carry, so they prove who they are with no password. And here is the detail the exam likes: one kubelet identity for the whole cluster, not one per node pool. Attach once, and every pool is covered."),
            leo("Good. And our code, reading the orders out of Azure SQL. Same badge?"),
            maya("A different one. The nodes are the landlord; your code is the tenant. The code uses workload identity. You create a managed identity, which is an account Azure gives your app so it can sign in with no password, and tie it to the pod's service account: the pod's name on the staff list."),
            leo("Tied with what?"),
            maya("A federated credential in Entra, which is Microsoft's directory, the thing that knows who everyone is. You label the pod to use it, and DefaultAzureCredential picks it up with no code change. [pause]"),
            leo("Honestly, this is a lot. Is it over-engineering?"),
            maya("For most apps, yes. Picture a small recipe app with one backend: Container Apps runs it, scales it, shrinks to zero copies while nobody is awake. AKS never goes to zero, and you own upgrades, patches, the front door and the machines."),
            leo("So what do I remember for the exam?"),
            maya("A set number of copies: a Deployment with replicas. Pods replaced but callers need one address: a Service with a label selector. Pods in Pending at dinner: the cluster autoscaler. A pod killed again and again: the memory limit."),
            leo("And the permission ones?"),
            maya("ImagePullBackOff on a private registry: attach ACR for AcrPull on the kubelet identity, one per cluster. Azure read with no password: workload identity. Graphics chips, or the Kubernetes API itself: AKS. Anything simpler: Container Apps."),
            leo("And something to try today?"),
            maya("Create a two-node cluster and fetch its credentials. Write a Deployment and a Service for a public sample image, and apply the file. Change the image tag, apply again, and watch kubectl rollout status. Run rollout undo and see the old pods come back. Then delete the resource group, because a cluster bills by the hour."),
            leo("Three cooks on the grill, and a bill for the kitchen. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-aks",
        listOf(
            maya("Welcome to Case Files. Today's case: The Restaurant That Vanished. On Friday at eight, Zaiqa showed every diner the same thing: no restaurants near you. Nothing was down. Every machine was green. And the week before, we had moved the Zaiqa API, the backend the phone calls, off Container Apps and onto AKS."),
            leo("Detective Leo, reporting. AKS. That is a word I have only ever heard in meetings. What is it, in plain words? I ship Android apps."),
            maya("Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running, like a kitchen manager who calls in another cook when one goes home sick. AKS is that manager, with Azure minding half of it."),
            leo("Which half?"),
            maya("Kubernetes runs on a cluster, which is a group of computers working as one. One half is the control plane, the brain that remembers what should be running. Azure runs that. The other half is yours."),
            leo("And the other half is?"),
            maya("The nodes. A node is a single dot in a network, and here each dot is one computer, a virtual machine rented by the hour. They come in node pools, groups of identical machines. Zaiqa has a system pool and a user pool."),
            leo("So, suspect one. Somebody deleted a machine."),
            maya("No machine was deleted. Here is the first clue. The Service in front of the Zaiqa API had no endpoints, which means no pods behind it at all. And yet three pods were running and ready."),
            leo("Hold on. A pod is what?"),
            maya("The smallest thing Kubernetes runs: a wrapper round one or more containers that share one address. Like a pea pod. A Service is one stable address in front of a set of pods, because pods come and go and each new one gets a new address."),
            leo("Then how does a Service know which pods are its own?"),
            maya("By label, which is a name tag stuck on a pod. The Service says, send traffic to every pod wearing this tag. Second clue: the pods wore app equals zaiqa-api, and the Service was looking for app equals zaiqaapi."),
            leo("[sigh] One hyphen."),
            maya("One hyphen. No pod matched, so the Service had nobody to send diners to, and the app went dark with everything perfectly healthy. Case closed on the vanishing restaurants. But the file told me two more stories."),
            leo("Go on, then. What file?"),
            maya("The manifest, which is a file describing what should be running. Think of an order form: three copies of this image, this port, this much memory. You send it with kubectl apply, and the cluster makes reality match it."),
            leo("And what else did the order form confess?"),
            maya("That the Taste service, the bit that answers, something like dal makhani, but vegan, had never started at all. Its pods said ImagePullBackOff."),
            leo("Easy. The image is missing from the registry."),
            maya("Wrong suspect. The tag was there in ACR, Azure Container Registry, the private shelf our images wait on. ImagePullBackOff means the nodes could not download it, and are waiting before trying again. It is nearly always permission."),
            leo("Then somebody should put the registry password in the file."),
            maya("Nobody puts a password anywhere. You run az aks update with attach ACR, which grants AcrPull, permission to download images and nothing else, to the cluster's kubelet identity."),
            leo("Which is what, a user account?"),
            maya("The kubelet is the agent on every node that actually starts containers. Its identity is the badge those agents carry, so they prove who they are with no password. And this is the clue that cracks it: there is one kubelet identity for the whole cluster."),
            leo("One for the cluster. So why did the Taste pods fail and the others not?"),
            maya("Because the team believed the opposite. They had added a third node pool with graphics chips for the Taste service, and somebody went looking for the place to attach the registry to that new pool."),
            leo("And never found it."),
            maya("There is nothing to find. Attaching once covers every pool, now and later. The real cause was that the first attach had been undone during a rebuild, so no pool could pull. The older pods kept running on images they had already downloaded."),
            leo("Sneaky. Already downloaded, so nobody noticed. And the third story?"),
            maya("The Taste pods that could pull sat in Pending, which means waiting for room. Nothing was wrong with them. The graphics pool had one machine, and each pod asks for a quarter of a processor and 256 megabytes."),
            leo("Asks whom?"),
            maya("The cluster. For every container you write requests and limits. A request is what the pod needs before it is let onto a machine. A limit is the ceiling it may never pass: past the memory limit the pod is killed and restarted."),
            leo("So the pods were queuing for a machine that was full."),
            maya("And nobody had turned on the cluster autoscaler, which adds nodes when pods have nowhere to go. One switch, and the pool grows at dinner time and shrinks afterwards. Different from the autoscaler that adds pods: more cooks, or a bigger kitchen."),
            leo("Right. Last loose end. The Zaiqa API reads Azure SQL. Where is that password hidden?"),
            maya("There is no password. The code uses workload identity. You make a managed identity, which is an account Azure gives your app so it can sign in with nothing stored, and you tie it to the pod's service account, the pod's name on the staff list."),
            leo("Tie it with what, string?"),
            maya("A federated credential in Entra, which is Microsoft's directory, the thing that knows who everyone is. Then you label the pod to use it, and DefaultAzureCredential picks it up with no code change."),
            leo("Case closed, then. Though I have a question. Was any of this worth it?"),
            maya("That is the honest part. Container Apps gave us the front door, numbered versions of the app, and scaling to zero copies overnight, all for free. We swapped that for graphics chips and the Kubernetes API itself."),
            leo("And the front door now?"),
            maya("An ingress controller, which is a pod whose whole job is to take requests from the internet and send each one to the right Service inside. You install it, you patch it, you own it. Same with upgrades, and the bill for the machines."),
            leo("So for the exam, what should a detective listen for?"),
            maya("A Service with no endpoints while pods are healthy: the labels do not match. ImagePullBackOff on a private registry: attach ACR for AcrPull on the kubelet identity, one per cluster, not per node pool. Pods in Pending at dinner: the cluster autoscaler."),
            leo("And the rest of the file?"),
            maya("Keep a set number of copies: a Deployment with replicas, where a replica is a copy identical to the rest. Read Azure with no password: workload identity. A plain backend with no need for Kubernetes: Container Apps, every time."),
            leo("Case closed. Anything to try today?"),
            maya("Create a two-node cluster and fetch its credentials. Write a Deployment and a Service for a public sample image, and apply the file. Change the image tag, apply again, and watch kubectl rollout status. Run rollout undo and watch the old pods return. Then delete the resource group, because a cluster bills by the hour."),
            leo("One hyphen, one dark city. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-aks",
        listOf(
            maya("Okay, did you hear what the Zaiqa team did this fortnight? Sit down. They moved the Zaiqa API, the backend the phone calls, off Container Apps and onto AKS. Five people, five separate disasters. I have notes."),
            leo("[laugh] I heard the word cluster used as a swear word. Start at the start. What is AKS, in plain words?"),
            maya("Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running. Picture a kitchen manager: you say three cooks on the grill, and when one goes home sick, another is called in."),
            leo("And the cluster bit?"),
            maya("A cluster is a group of computers working as one, like one restaurant's whole staff. Azure runs the brain of it, called the control plane, and you rent the rest."),
            leo("Rent what, exactly?"),
            maya("Nodes. A node is a single dot in a network, and here each dot is one computer, a virtual machine by the hour. They come in node pools, which are groups of identical machines. Zaiqa took a system pool and a user pool."),
            leo("Right. Who went first?"),
            maya("Tom. Monday. Tom wrote the manifest, which is a file saying what should be running. Think of an order form: three copies of this image, this port, this much memory. You send it with kubectl apply and the cluster matches it."),
            leo("That sounds fine so far."),
            maya("The file was lovely. It asked for a Deployment, which keeps a set number of identical pods running, and a Service in front of them. A pod is the smallest thing Kubernetes runs, a wrapper round one or more containers sharing one address."),
            leo("And the Service?"),
            maya("One stable address, because pods come and go and each new one gets a new address. It finds its pods by label, a name tag stuck on a pod. Tom's pods wore app equals zaiqa-api. Tom's Service went looking for app equals zaiqaapi."),
            leo("[sigh] Oh, Tom."),
            maya("Eight on Friday evening, and every diner saw: no restaurants near you. Three pods, all running, all healthy, and nobody talking to them. Tom has not mentioned hyphens since."),
            leo("Tuesday. Who is it?"),
            maya("Dana. Dana needed the registry password in the cluster, so she pasted it into a Kubernetes Secret and committed the file to the repository."),
            leo("A Secret is encrypted, surely."),
            maya("It is not. It is only encoded, which means scrambled in a way anyone can unscramble in one line. And she did not need it at all. You run az aks update with attach ACR, and ACR, Azure Container Registry, grants AcrPull to the cluster."),
            leo("AcrPull being?"),
            maya("Permission to download images and nothing else. It goes to the cluster's kubelet identity. The kubelet is the agent on every node that actually starts containers, and its identity is the badge those agents carry, so no password exists to leak."),
            leo("Wednesday, then."),
            maya("Marcus. Marcus added a node pool with graphics chips for the Taste service, the bit that answers, something like dal makhani, but vegan. Then he spent a morning hunting for how to attach the registry to that pool."),
            leo("And the answer was?"),
            maya("There is no such thing. One kubelet identity serves the whole cluster, not one per node pool. Attach once, and every pool is covered, including the ones you add later. Marcus found this out from the intern, which stung."),
            leo("[chuckle] Harsh. Thursday?"),
            maya("Priya, and this one is gentler. The Taste pods sat in Pending, which means waiting for room. Nothing wrong with them. The graphics pool had exactly one machine, and every container asks for a share before it is let on."),
            leo("Asks for a share of what?"),
            maya("You write requests and limits. The request is what the pod needs: a quarter of a processor, 256 megabytes. The limit is the ceiling it may never pass. Past the memory limit, the pod is killed and restarted."),
            leo("So the pool was simply full."),
            maya("Full. And the cluster autoscaler was switched off. That is the thing that adds nodes when pods have nowhere to go, and takes them away when dinner is over. Not to be confused with the autoscaler that adds pods: more cooks, or a bigger kitchen."),
            leo("And Friday? Please tell me Friday was quiet."),
            maya("Friday was the intern, and honestly the intern did best. The code needed to read Azure SQL, so the intern asked where the connection password lived."),
            leo("And it lives where?"),
            maya("Nowhere. You use workload identity. You create a managed identity, which is an account Azure gives your app so it can sign in with nothing stored, and tie it to the pod's service account, the pod's name on the staff list."),
            leo("Tie it how?"),
            maya("A federated credential in Entra, Microsoft's directory, the thing that knows who everyone is. Then you label the pod to use it, and DefaultAzureCredential picks it up with no code change. The intern had read the documentation. Nobody else had."),
            leo("And the bad release? There is always a bad release."),
            maya("Saturday. Tom again, tagging an image latest, so nobody could tell versions apart. He changed the tag, applied, pods rolled over a few at a time, and it was wrong. Kubectl rollout undo put the old one back in a minute."),
            leo("Then that is fine, surely."),
            maya("He forgot to fix the file. Next apply, the broken version came straight back. The file is the truth, always. Give every build its own tag and never deploy latest."),
            leo("Honestly, after all that, was the move worth it?"),
            maya("That is the gossip nobody enjoys. Container Apps gave them the front door, numbered versions and scaling to zero copies overnight, for nothing. On AKS the front door is an ingress controller, a pod whose job is to send each request from the internet to the right Service."),
            leo("Which they now patch themselves."),
            maya("They patch it, they upgrade the cluster, they pay for idle machines. Worth it for graphics chips and the Kubernetes API itself. Not worth it for a plain backend. Half the team would go back tomorrow."),
            leo("So what do I remember for the exam, so I am not next fortnight's story?"),
            maya("Healthy pods and a Service sending traffic nowhere: the labels do not match. ImagePullBackOff on a private registry: attach ACR for AcrPull, one kubelet identity for the whole cluster. Pods in Pending at dinner: the cluster autoscaler."),
            leo("And the rest of them?"),
            maya("A set number of copies: a Deployment with replicas, a replica being one identical copy. Azure with no password: workload identity, never a Secret in the repository. No need for Kubernetes itself: Container Apps."),
            leo("And something to try today, quietly, where nobody sees?"),
            maya("Create a two-node cluster and fetch its credentials. Write a Deployment and a Service for a public sample image, and apply the file. Change the image tag, apply again, and watch kubectl rollout status. Run rollout undo and see the old pods return. Then delete the resource group, because a cluster bills by the hour."),
            leo("Check your hyphens, people. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-aks",
        listOf(
            nova("Hey, you. It is late, the kitchen is closed, and it is just the three of us. Me, Ivy, and you, still up. [pause] Tonight, the night an app grows too big for the easy option."),
            ivy("Too big for the easy option. That is most of my evenings. Which app, Nova?"),
            nova("Zaiqa. The Android app, the partner dashboard, and the Zaiqa API, which is the backend the phone calls for everything. It has been living happily on Container Apps, with the front door and the scaling done for it."),
            ivy("And something spoiled that?"),
            nova("Two things wanted more. [pause] The Taste service, the bit that answers, something like dal makhani, but vegan, wants a graphics chip to turn dishes into lists of numbers. And someone wants to write their own rules into the platform itself."),
            ivy("So you move house. To AKS, I assume, since that is tonight."),
            nova("Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running. Picture a kitchen manager. Three cooks on the grill, you say, and one goes home sick, and another is quietly called in."),
            ivy("I would like a kitchen manager. And AKS does what for me?"),
            nova("It runs half of it. Kubernetes lives on a cluster, which is a group of computers working as one, like the whole staff of one restaurant. [pause] Azure keeps the brain, which is called the control plane. You keep the rest."),
            ivy("The brain. What does it do all night?"),
            nova("Remembers what should be running, and gives the orders. Azure patches it, upgrades it, keeps it breathing. You never log in. What you do get is the nodes."),
            ivy("Nodes. Say that slowly."),
            nova("A node is one dot in a network. In a cluster, each dot is one computer, a virtual machine you rent by the hour. [pause] They come in node pools, which are groups of identical machines. Zaiqa takes a system pool and a user pool."),
            ivy("And the graphics chips get their own pool."),
            nova("Later, yes, because one pool is one machine size. [pause] Now, how do you tell the cluster what you want? You write a manifest, a file saying what should be running. An order form: three copies of this image, this port, this much memory."),
            ivy("And someone takes the order."),
            nova("Kubectl apply sends it, and the cluster makes reality match the file. Change the file, apply again, and it moves. You describe where you want to end up, never the steps."),
            ivy("Lovely. So what do you write on this order form of yours?"),
            nova("Three things. A Pod, a Deployment and a Service. A pod is the smallest thing Kubernetes runs: a wrapper round one or more containers sharing one address. Like a pea pod."),
            ivy("And the Deployment is the one that counts them."),
            nova("The shift manager. Keep three identical pods of the Zaiqa API running, from this image. A replica is a copy identical to the rest, so replicas three gives three pods. One falls over, another appears."),
            ivy("And the Service? Let me guess. Somebody has to answer the phone."),
            nova("[pause] Somebody does. Pods come and go, and every new pod gets a new address, so nobody calls a pod directly. A Service is one stable address in front of them all, and it finds them by label, a name tag stuck on a pod."),
            ivy("Name tags. And if a tag is wrong?"),
            nova("Then the Service finds nobody, and the app goes dark while every pod sits there in perfect health. [pause] One hyphen can do that. It is the most common way a first week on AKS ends badly."),
            ivy("Noted. And the world outside, how does it get in?"),
            nova("Through an ingress controller, which is a pod whose only job is to be the front door: requests come in from the internet, and it sends each one to the right Service. On Container Apps that came free. Here, you install it and you patch it."),
            ivy("Mm. The price of a bigger house. What else is on me now?"),
            nova("Sizing. For each container you write requests and limits. A request is what the pod needs before it is let onto a machine: a quarter of a processor, 256 megabytes. A limit is the ceiling it may never pass."),
            ivy("And what happens at the ceiling?"),
            nova("Past the memory limit, the pod is killed and started again. Past the processor limit, it just runs slow. [pause] And the request is what the cluster uses to choose a node, so get it wrong and either your pods fight or you rent machines to do nothing."),
            ivy("Friday evening. Everybody is hungry at once. Who finds more room?"),
            nova("Two helpers, and they are often confused. One autoscaler adds pods when the pods get busy. The cluster autoscaler adds nodes when the pods have nowhere to go. More cooks, or a bigger kitchen."),
            ivy("How does it know there is nowhere to go?"),
            nova("The pods sit in Pending, which means waiting for room. [pause] The cluster autoscaler sees them, grows the pool, and they start. When the rush is over it gives the machines back."),
            ivy("Good. Now the awkward bit. How do the machines get the images?"),
            nova("From ACR, Azure Container Registry, the private shelf the images wait on. If the pods say ImagePullBackOff, they could not download it and are waiting before trying again. It is nearly always permission."),
            ivy("And I suppose the answer is a password somewhere."),
            nova("Never. [pause] You run az aks update with attach ACR, and the registry grants AcrPull, permission to download images and nothing more, to the cluster's kubelet identity."),
            ivy("Kubelet identity. Gently, please."),
            nova("The kubelet is the agent on every node that actually starts containers. Its identity is the badge those agents carry, so they prove who they are with nothing written down. One badge for the whole cluster, by the way. Not one per node pool."),
            ivy("One badge. So attaching once is enough, forever."),
            nova("Forever, and for every pool you add later. [pause] But that is only the nodes. Your own code still has to read Azure SQL, where Zaiqa keeps the orders. That needs a badge of its own."),
            ivy("Of course it does. Tell me."),
            nova("Workload identity. You create a managed identity, which is an account Azure gives your app so it can sign in with nothing stored anywhere, and you tie it to the pod's service account, the pod's name on the staff list."),
            ivy("And who does the tying?"),
            nova("A federated credential in Entra, which is Microsoft's directory, the thing that knows who everyone is. Then you label the pod to use it. [pause] DefaultAzureCredential finds it, and the code never changes."),
            ivy("[sigh] It is lovely, Nova. It is also a lot. Should everyone do this?"),
            nova("No, and I will say so out loud. Container Apps gives you the front door, numbered versions and scaling to zero copies while nobody is awake. AKS never goes to zero, and the upgrades, the patching and the idle machines are yours now."),
            ivy("So one simple backend stays where it is."),
            nova("Stays exactly where it is. You move for graphics chips, or for the Kubernetes API itself. [pause] Not for the feeling of having moved."),
            ivy("Before I fall asleep on you. What do I keep for the exam?"),
            nova("Healthy pods and a Service with nobody behind it: the labels do not match. ImagePullBackOff on a private registry: attach ACR for AcrPull, one kubelet identity per cluster. Pods in Pending at dinner: the cluster autoscaler."),
            ivy("And the quieter ones?"),
            nova("A set number of copies: a Deployment with replicas. Azure read with no password: workload identity. A pod killed over and over: the memory limit is too low. And no need for Kubernetes itself: Container Apps."),
            ivy("And something to try today, before the kettle goes cold?"),
            nova("Create a two-node cluster and fetch its credentials. Write a Deployment and a Service for a public sample image, and apply the file. Change the image tag, apply again, and watch kubectl rollout status. Run rollout undo and watch the old pods return. [pause] Then delete the resource group, because a cluster bills by the hour."),
            ivy("Three cooks on the grill, and the kitchen locked up behind you. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-aks",
        listOf(
            nova("Tonight is date night, darling, and I have picked the restaurant. Well. I have picked the app that finds the restaurant. Zaiqa has outgrown its old home, and we are moving it."),
            rafe("Moving on date night. Very us. Where does Zaiqa live now?"),
            nova("On Container Apps, with the Zaiqa API, which is the backend the phone calls for everything. The front door and the scaling are done for it. It has been comfortable, love. Maybe too comfortable."),
            rafe("And what does it want that it cannot have?"),
            nova("A graphics chip for the Taste service, the bit that answers, something like dal makhani, but vegan. And the Kubernetes API itself, for someone who wants to write their own rules into the platform."),
            rafe("Then we move to AKS, and you can tell me what that is over the starters."),
            nova("Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running. Picture a kitchen manager: three cooks on the grill, and when one goes home sick, another is called in."),
            rafe("I like a manager who notices when someone leaves. And Azure's half?"),
            nova("Kubernetes runs on a cluster, which is a group of computers working as one, like a restaurant's whole staff. Azure keeps the brain, the control plane, which remembers what should be running. We keep the nodes."),
            rafe("Nodes. Quiz me, I have been reading. A node is one dot in a network, and in a cluster each dot is one computer, a virtual machine rented by the hour. They come in node pools, which are groups of identical machines."),
            nova("Right answer, and showing off with it. [kiss] One pool is one machine size, which is why the graphics chips get a pool of their own."),
            rafe("So how do we tell this cluster what we want? I am told you are very direct about what you want."),
            nova("I am. You write a manifest, which is a file saying what should be running. An order form: three copies of this image, this port, this much memory. Kubectl apply sends it, and the cluster makes reality match the file."),
            rafe("Describe the end you want, not every step along the way. Noted, for later."),
            nova("[chuckle] Behave. On the order form go three things. A Pod, a Deployment and a Service. A pod is the smallest thing Kubernetes runs: a wrapper round one or more containers sharing one address."),
            rafe("And the Deployment keeps them coming."),
            nova("Keeps three identical pods of the Zaiqa API running, from this image. A replica is a copy identical to the rest, so replicas three gives three pods. One dies, another arrives before you notice."),
            rafe("Then let me test you, darling. Exam style. Our pods are replaced all night and each new one gets a new address. Callers still need one address that never changes. What goes in front?"),
            nova("A Service, which is one stable address in front of the pods. It finds them by label, a name tag stuck on each pod. Ours wear app equals zaiqa-api, and the Service asks for exactly that tag."),
            rafe("Right answer. Knowing exactly who you are looking for. [kiss]"),
            nova("And if the tag is off by one hyphen, the Service finds nobody and the app goes dark while every pod sits there in perfect health. A quiet heartbreak, that one."),
            rafe("Cruel. And how does the outside world find its way in to us?"),
            nova("Through an ingress controller, which is a pod whose only job is to be the front door: it takes requests from the internet and sends each to the right Service. Container Apps gave us that free. Here we install it, and we patch it."),
            rafe("The price of a bigger place. What else do we take on?"),
            nova("Sizing. For every container you write requests and limits. The request is what the pod needs before it is let onto a machine. The limit is the ceiling it may never pass."),
            rafe("My turn to be quizzed, then. Go on."),
            nova("Exam style. A pod is killed and restarted, over and over, and the logs stop mid-sentence. Nothing else is wrong. What would you look at first?"),
            rafe("The processor limit. It must be running out of processor."),
            nova("[sigh] No kiss for that one. Past the processor limit a pod simply runs slower. Past the memory limit it is killed and started again. Raise the memory limit, or find what is eating it."),
            rafe("Memory kills, processor only slows. I will not forget that twice. Now, Friday at eight, everyone is hungry. Who finds us more room?"),
            nova("Two helpers, and people mix them up. One autoscaler adds pods when the pods are busy. The cluster autoscaler adds nodes when the pods have nowhere to go. More cooks, or a bigger kitchen."),
            rafe("And how does it know there is nowhere to go? Quiz me back if you like."),
            nova("Go on, then. What is a pod doing when it says Pending?"),
            rafe("Waiting for room. Nothing is wrong with it. The cluster autoscaler notices, grows the pool, and the pod starts. When the rush is over the machines go back. And before you ask, that is why you never leave it switched off."),
            nova("Right answer, and the follow-up for free. [kiss] Now the part where someone needs a key."),
            rafe("I knew we would get here. Whose key?"),
            nova("The machines. They fetch images from ACR, Azure Container Registry, the private shelf our images wait on. If the pods say ImagePullBackOff, they could not download the image and are waiting before trying again."),
            rafe("And the answer is a password in the file."),
            nova("Never a password, love. You run az aks update with attach ACR, and the registry grants AcrPull, permission to download images and nothing else, to the cluster's kubelet identity."),
            rafe("Which is the agent on every node that actually starts containers, wearing a badge so it never has to say a password out loud. And here is the bit people miss. One kubelet identity for the whole cluster."),
            nova("Right answer, and the bit people miss. [kiss] Not one per node pool. Attach once, and every pool you ever add is covered."),
            rafe("So the machines are in. What about our own code, reading the orders out of Azure SQL?"),
            nova("That needs its own badge. Workload identity. You create a managed identity, which is an account Azure gives your app so it can sign in with nothing stored, and tie it to the pod's service account, the pod's name on the staff list."),
            rafe("Tied with what? I am told the tying is the romantic part."),
            nova("A federated credential in Entra, which is Microsoft's directory, the thing that knows who everyone is. Then you label the pod to use it, and DefaultAzureCredential finds it with no code change at all."),
            rafe("Beautiful. And now the question I have been saving. Was any of this a good idea?"),
            nova("Honestly? For most apps, no. Container Apps gave us the front door, numbered versions and scaling to zero copies overnight. AKS never goes to zero, and the upgrades, the patching and the idle machines are ours now."),
            rafe("So we moved for the graphics chips and the Kubernetes API, and for nothing else."),
            nova("For nothing else. Moving because you can is how couples end up with a house they cannot heat."),
            rafe("[chuckle] Pointed. Last one, for the exam. What do we remember?"),
            nova("Healthy pods and a Service with nobody behind it: the labels do not match. ImagePullBackOff on a private registry: attach ACR for AcrPull, one kubelet identity per cluster. Pods in Pending at dinner: the cluster autoscaler."),
            rafe("That is three. Keep going, darling."),
            nova("A set number of copies: a Deployment with replicas. Azure read with no password: workload identity. A pod killed again and again: the memory limit. And no need for Kubernetes itself: Container Apps."),
            rafe("Perfect, every one. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, my love?"),
            rafe("Create a two-node cluster and fetch its credentials. Write a Deployment and a Service for a public sample image, and apply the file. Change the image tag, apply again, and watch kubectl rollout status. Run rollout undo and watch the old pods return. Then delete the resource group, because a cluster bills by the hour."),
            nova("Three cooks on the grill, and the lights off behind us. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun outgrow() = DiagramSpec(
        "Zaiqa leaves Container Apps for AKS when it needs graphics chips or the Kubernetes API itself.",
        nodes = listOf(
            DNode("aca", "Container Apps", 0.25f, 0.18f, w = 0.44f, h = 0.2f, tone = 1, sub = "front door, scaling"),
            DNode("gpu", "Graphics chips", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "Taste service"),
            DNode("k8sapi", "Kubernetes API", 0.25f, 0.76f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "your own rules"),
            DNode("aks", "AKS", 0.75f, 0.76f, w = 0.42f, h = 0.22f, sub = "Kubernetes itself"),
        ),
        edges = listOf(
            DEdge("aca", "aks", "outgrown"),
            DEdge("gpu", "aks"),
            DEdge("k8sapi", "aks"),
        ),
        aspect = 0.8f,
    )

    private fun cluster() = DiagramSpec(
        "A manifest goes to the control plane, which Azure runs, and it keeps the node pools running what the file asks for.",
        nodes = listOf(
            DNode("dev", "kubectl apply", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the manifest"),
            DNode("cp", "Control plane", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "run by Azure"),
            DNode("nodes", "Node pools", 0.75f, 0.76f, w = 0.42f, h = 0.22f, sub = "virtual machines"),
        ),
        edges = listOf(
            DEdge("dev", "cp", "desired state"),
            DEdge("cp", "nodes", "runs pods"),
        ),
        aspect = 0.8f,
    )

    private fun objects() = DiagramSpec(
        "A Deployment keeps three pods running; a Service gives them one stable address and finds them by label.",
        nodes = listOf(
            DNode("app", "Phone", 0.15f, 0.18f, w = 0.24f, h = 0.2f, kind = NodeKind.USER),
            DNode("svc", "Service", 0.64f, 0.18f, w = 0.56f, h = 0.2f, tone = 1, sub = "one stable address"),
            DNode("deploy", "Deployment", 0.26f, 0.76f, w = 0.46f, h = 0.22f, sub = "replicas: 3"),
            DNode("pods", "Three pods", 0.76f, 0.76f, w = 0.42f, h = 0.22f, sub = "label: zaiqa-api"),
        ),
        edges = listOf(
            DEdge("app", "svc", "calls"),
            DEdge("svc", "pods", "by label"),
            DEdge("deploy", "pods", "keeps 3"),
        ),
        aspect = 0.8f,
    )

    private fun sizing() = DiagramSpec(
        "A request is what a pod needs before a node lets it on; a limit is the ceiling, and memory past it kills the pod.",
        nodes = listOf(
            DNode("req", "Request", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "250m, 256Mi"),
            DNode("lim", "Limit", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "500m, 512Mi"),
            DNode("node", "Node with room", 0.25f, 0.76f, w = 0.42f, h = 0.22f, sub = "scheduler picks it"),
            DNode("killed", "Over memory", 0.75f, 0.76f, w = 0.42f, h = 0.22f, tone = 2, sub = "pod restarted"),
        ),
        edges = listOf(
            DEdge("req", "node", "needs room"),
            DEdge("lim", "killed", "passed"),
        ),
        aspect = 0.8f,
    )

    private fun scaling() = DiagramSpec(
        "Busy pods bring more pods; pods with nowhere to go sit in Pending until the cluster autoscaler adds a node.",
        nodes = listOf(
            DNode("busy", "Pods get busy", 0.25f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "dinner rush"),
            DNode("more", "More pods", 0.75f, 0.15f, w = 0.42f, h = 0.2f, tone = 1, sub = "more cooks"),
            DNode("pending", "Pending", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "waiting for room"),
            DNode("auto", "Cluster autoscaler", 0.25f, 0.5f, w = 0.44f, h = 0.2f, sub = "adds a node"),
            DNode("pool", "User node pool", 0.5f, 0.85f, w = 0.56f, h = 0.2f, sub = "grows, then shrinks"),
        ),
        edges = listOf(
            DEdge("busy", "more", "scale out"),
            DEdge("more", "pending", "no room"),
            DEdge("pending", "auto", "seen by"),
            DEdge("auto", "pool", "adds a machine"),
        ),
        aspect = 0.8f,
    )

    private fun access() = DiagramSpec(
        "The nodes pull images with the kubelet identity and AcrPull; the code signs in through workload identity.",
        nodes = listOf(
            DNode("acr", "ACR", 0.25f, 0.18f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "private images"),
            DNode("kubelet", "Kubelet identity", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "one per cluster"),
            DNode("pod", "Pod", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "service account"),
            DNode("wi", "Workload identity", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "no password"),
        ),
        edges = listOf(
            DEdge("kubelet", "acr", "AcrPull"),
            DEdge("pod", "wi", "signs in"),
        ),
        aspect = 0.8f,
    )

    private fun choose() = DiagramSpec(
        "A plain backend fits Container Apps; AKS is for graphics chips or the Kubernetes API, and you own the rest.",
        nodes = listOf(
            DNode("simple", "A plain backend", 0.25f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "scales to zero"),
            DNode("full", "Full control", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "graphics chips"),
            DNode("aca", "Container Apps", 0.25f, 0.76f, w = 0.44f, h = 0.22f, tone = 1, sub = "no cluster to run"),
            DNode("aks", "AKS", 0.75f, 0.76f, w = 0.42f, h = 0.22f, sub = "you patch it all"),
        ),
        edges = listOf(
            DEdge("simple", "aca"),
            DEdge("full", "aks"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's AKS questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("copies", "Keep 3 copies running", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Deployment, replicas 3"),
            DNode("addr", "Service, no traffic", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "labels do not match"),
            DNode("pend", "Pods in Pending", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "cluster autoscaler"),
            DNode("pull", "ImagePullBackOff", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "attach ACR: AcrPull"),
            DNode("simple", "No need for Kubernetes", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Apps"),
            DNode("nopass", "Azure with no password", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "workload identity"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("copies", "addr", "pend", "pull", "simple", "nopass")
    private val applyCode = BoardCard("Shell", "kubectl apply -f zaiqa.yaml", code = true)
    private val attachCode = BoardCard("Shell", "az aks update -g zaiqa-rg -n zaiqa-aks --attach-acr zaiqa", code = true)
    private val today = BoardCard("Today", "A two-node cluster, apply, change the tag, rollout undo, delete it all.")

    private fun classicBoard() = PodcastBoard(
        "ai-aks",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is running..."
            Scene(
                0, "Outgrowing Container Apps", outgrow(),
                beats = listOf(
                    Beat(0, show = setOf("aca"), light = setOf("aca"), card = BoardCard("Where Zaiqa lives", "The Zaiqa API on Container Apps, its image waiting in ACR.")),
                    Beat(2, light = setOf("aca"), card = BoardCard("Ingress and revisions", "The front door that lets traffic in, and numbered versions, free.")),
                    Beat(4, show = setOf("gpu", "k8sapi", "aks"), light = setOf("gpu", "k8sapi"), pulse = "aca" to "aks", card = BoardCard("Two reasons to leave", "Graphics chips for the Taste service, and the Kubernetes API itself.")),
                ),
            ),
            // 5: "Fine. So what is Kubernetes, in plain words?"
            Scene(
                5, "Inside a cluster", cluster(),
                beats = listOf(
                    Beat(6, card = BoardCard("Kubernetes", "Runs many containers on many machines, and keeps them running.")),
                    Beat(8, card = BoardCard("Cluster", "A group of computers working as one, like one restaurant's whole staff.")),
                    Beat(10, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain: it remembers what should run. Azure runs it, you never log in.")),
                    Beat(12, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Node", "A single dot in a network. Here, one computer rented by the hour.")),
                    Beat(14, light = setOf("nodes"), card = BoardCard("Node pools", "Groups of identical machines: system, user, and one with graphics chips.")),
                    Beat(15, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "A file saying what should run: an order form the cluster must match.")),
                    Beat(16, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = applyCode),
                ),
            ),
            // 17: "So what goes on the order form?"
            Scene(
                17, "Pods, Deployment, Service", objects(),
                beats = listOf(
                    Beat(18, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "A wrapper round one or more containers sharing one address. A pea pod.")),
                    Beat(20, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "The shift manager: keeps three identical pods running from one image.")),
                    Beat(22, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "One stable address in front of pods that come and go.")),
                    Beat(24, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Label selector", "Send traffic to every pod wearing this tag. One letter wrong, no pods.")),
                    Beat(26, light = setOf("svc"), card = BoardCard("Ingress controller", "A pod that is the front door: each request to the right Service.")),
                ),
            ),
            // 28: "Number two. Next, sizing..."
            Scene(
                28, "Requests and limits", sizing(),
                beats = listOf(
                    Beat(28, light = setOf("req", "lim"), card = BoardCard("Requests and limits", "What the pod needs, and the ceiling it may never pass.")),
                    Beat(29, light = setOf("lim", "killed"), pulse = "lim" to "killed", card = BoardCard("Past the limit", "Memory: killed and restarted. Processor: it simply runs slower.")),
                    Beat(30, light = setOf("req", "node"), pulse = "req" to "node", card = BoardCard("The request picks the node", "The cluster puts the pod where there is room. Too little, and pods fight.")),
                ),
            ),
            // 31: "Okay. Friday, eight in the evening..."
            Scene(
                31, "The dinner rush", scaling(),
                beats = listOf(
                    Beat(32, light = setOf("busy", "more"), pulse = "busy" to "more", card = BoardCard("More pods", "One autoscaler adds pods when the pods get busy: more cooks.")),
                    Beat(34, light = setOf("pending", "auto", "pool"), pulse = "pending" to "auto", card = BoardCard("Pending", "Waiting for room. The cluster autoscaler adds a node: a bigger kitchen.")),
                ),
            ),
            // 35: "And if I ship a bad build in the middle of all that?"
            Scene(
                35, "A bad build", cluster(),
                beats = listOf(
                    Beat(36, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("Rolling update", "New pods in, old pods out, a few at a time. Wrong? rollout undo.")),
                ),
            ),
            // 37: "Noted. Now the thing that always bites me..."
            Scene(
                37, "Pulling from ACR", access(),
                beats = listOf(
                    Beat(38, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Cannot download the image; waiting before trying again. Usually permission.")),
                    Beat(40, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(42, light = setOf("kubelet"), card = BoardCard("Kubelet identity", "The node agents' badge. One for the whole cluster, not per node pool.")),
                    Beat(44, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A managed identity tied to the pod's service account. No password.")),
                    Beat(46, light = setOf("wi"), card = BoardCard("Entra", "Microsoft's directory. A federated credential links the two.")),
                ),
            ),
            // 47: "Honestly, this is a lot. Is it over-engineering?"
            Scene(
                47, "Is it over-engineering?", choose(),
                beats = listOf(
                    Beat(48, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "Runs it, scales it, shrinks to zero. AKS never goes to zero.")),
                ),
            ),
            // 49: "So what do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("copies", "addr", "pend"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer: Deployment, Service, autoscaler.")),
                    Beat(52, light = setOf("pull", "nopass", "simple"), card = BoardCard("And the permission ones", "Attach ACR for AcrPull, workload identity, or Container Apps instead.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-aks--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Restaurant That Vanished..."
            Scene(
                0, "The Restaurant That Vanished", cluster(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Friday at eight: every diner sees, no restaurants near you.")),
                    Beat(2, light = setOf("cp"), card = BoardCard("AKS", "Kubernetes runs containers across machines; Azure runs half of it.")),
                    Beat(4, light = setOf("cp"), card = BoardCard("Control plane", "The brain that remembers what should be running. Azure keeps it.")),
                    Beat(6, light = setOf("nodes"), card = BoardCard("Nodes and node pools", "One computer each, rented by the hour, in groups of identical machines.")),
                ),
            ),
            // 8: "No machine was deleted. Here is the first clue..."
            Scene(
                8, "Clue one: nobody behind it", objects(),
                beats = listOf(
                    Beat(8, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("No endpoints", "The Service had no pods behind it, though three were running.")),
                    Beat(10, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "A wrapper round one or more containers sharing one address.")),
                    Beat(12, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Clue two: the label", "Pods wore app equals zaiqa-api. The Service asked for zaiqaapi.")),
                    Beat(14, light = setOf("svc"), card = BoardCard("One hyphen", "No pod matched, so the app went dark with everything healthy.")),
                    Beat(16, show = setOf("deploy"), light = setOf("deploy"), card = BoardCard("Manifest", "A file saying what should be running, sent with kubectl apply.")),
                ),
            ),
            // 18: "That the Taste service... had never started at all."
            Scene(
                18, "The Taste pods that never ran", access(),
                beats = listOf(
                    Beat(18, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "The Taste pods could not download their image.")),
                    Beat(20, light = setOf("acr"), card = BoardCard("ACR", "Azure Container Registry: the private shelf our images wait on.")),
                    Beat(22, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(24, light = setOf("kubelet"), card = BoardCard("Kubelet identity", "The node agents' badge, so they pull with no password.")),
                    Beat(26, light = setOf("kubelet"), card = BoardCard("One per cluster", "Not one per node pool. Attaching once covers every pool, now and later.")),
                ),
            ),
            // 29: "Sneaky. Already downloaded, so nobody noticed. And the third story?"
            Scene(
                29, "Clue three: Pending", sizing(),
                beats = listOf(
                    Beat(30, light = setOf("node"), card = BoardCard("Pending", "Waiting for room. Nothing wrong with the pod itself.")),
                    Beat(32, light = setOf("req", "lim"), pulse = "req" to "node", card = BoardCard("Requests and limits", "What a pod needs before a machine takes it, and the ceiling.")),
                ),
            ),
            // 33: "So the pods were queuing for a machine that was full."
            Scene(
                33, "A kitchen with one machine", scaling(),
                beats = listOf(
                    Beat(34, light = setOf("pending", "auto", "pool"), pulse = "pending" to "auto", card = BoardCard("Cluster autoscaler", "Switched off. It adds nodes when pods have nowhere to go.")),
                ),
            ),
            // 35: "Right. Last loose end. The Zaiqa API reads Azure SQL..."
            Scene(
                35, "The last loose end", access(),
                beats = listOf(
                    Beat(36, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A managed identity tied to the pod's service account. No password.")),
                    Beat(38, light = setOf("wi"), card = BoardCard("Entra", "Microsoft's directory. A federated credential ties the two together.")),
                ),
            ),
            // 39: "Case closed, then. Though I have a question. Was any of this worth it?"
            Scene(
                39, "What we gave up", choose(),
                beats = listOf(
                    Beat(40, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("The trade", "Container Apps gave the front door, versions and zero copies, free.")),
                    Beat(42, light = setOf("full", "aks"), pulse = "full" to "aks", card = BoardCard("Ingress controller", "Now a pod you install and patch, plus upgrades and the machines.")),
                ),
            ),
            // 43: "So for the exam, what should a detective listen for?"
            Scene(
                43, "Case closed", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("addr", "pull", "pend"), card = BoardCard("Listen for the phrase", "Labels, AcrPull on the kubelet identity, the cluster autoscaler.")),
                    Beat(46, light = setOf("copies", "nopass", "simple"), card = BoardCard("And the rest", "Deployment with replicas, workload identity, or Container Apps.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-aks--gossip",
        listOf(
            // 0: "Okay, did you hear what the Zaiqa team did this fortnight?"
            Scene(
                0, "What the Zaiqa team did", outgrow(),
                beats = listOf(
                    Beat(0, show = setOf("aca", "aks"), light = setOf("aca"), pulse = "aca" to "aks", card = BoardCard("The fortnight", "The Zaiqa API moved off Container Apps and onto AKS.")),
                    Beat(2, show = setOf("gpu"), light = setOf("gpu"), card = BoardCard("Kubernetes", "Runs containers across machines and keeps them running.")),
                    Beat(4, show = setOf("k8sapi"), light = setOf("aks"), card = BoardCard("Cluster", "A group of computers working as one. Azure runs the brain.")),
                    Beat(6, light = setOf("aks"), card = BoardCard("Nodes and node pools", "One computer each, by the hour, in groups of identical machines.")),
                ),
            ),
            // 7: "Right. Who went first?"
            Scene(
                7, "Monday: Tom", objects(),
                beats = listOf(
                    Beat(8, show = setOf("deploy"), light = setOf("deploy"), card = BoardCard("Manifest", "A file saying what should run, sent with kubectl apply.")),
                    Beat(10, show = setOf("pods"), light = setOf("pods"), pulse = "deploy" to "pods", card = BoardCard("Deployment and pod", "Keeps a set number of identical pods, each a wrapper round containers.")),
                    Beat(12, show = setOf("app", "svc"), light = setOf("svc"), pulse = "svc" to "pods", card = BoardCard("Service, by label", "One stable address, finding its pods by the name tag they wear.")),
                    Beat(14, light = setOf("svc"), card = BoardCard("One hyphen", "Pods wore zaiqa-api; the Service asked for zaiqaapi. The app went dark.")),
                ),
            ),
            // 15: "Tuesday. Who is it?"
            Scene(
                15, "Tuesday: Dana", access(),
                beats = listOf(
                    Beat(16, show = setOf("acr"), light = setOf("acr"), card = BoardCard("A Secret in the repository", "Only encoded, not encrypted. And she did not need it at all.")),
                    Beat(18, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(20, light = setOf("kubelet"), card = BoardCard("AcrPull", "Download images and nothing else, granted to the kubelet identity.")),
                ),
            ),
            // 21: "Wednesday, then."
            Scene(
                21, "Wednesday: Marcus", access(),
                beats = listOf(
                    Beat(22, light = setOf("kubelet"), card = BoardCard("A pool with graphics chips", "Marcus hunted for where to attach the registry to the new pool.")),
                    Beat(24, light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = BoardCard("One per cluster", "There is one kubelet identity for the whole cluster. Attach once.")),
                ),
            ),
            // 25: "[chuckle] Harsh. Thursday?"
            Scene(
                25, "Thursday: Priya", scaling(),
                beats = listOf(
                    Beat(26, light = setOf("pending"), card = BoardCard("Pending", "Waiting for room. The graphics pool had exactly one machine.")),
                    Beat(28, light = setOf("pending"), card = BoardCard("Requests and limits", "What a pod needs before a machine takes it, and the ceiling.")),
                    Beat(30, light = setOf("auto", "pool"), pulse = "auto" to "pool", card = BoardCard("Cluster autoscaler", "Switched off. It adds nodes when pods have nowhere to go.")),
                ),
            ),
            // 31: "And Friday? Please tell me Friday was quiet."
            Scene(
                31, "Friday: the intern", access(),
                beats = listOf(
                    Beat(32, light = setOf("pod"), card = BoardCard("Reading Azure SQL", "Where does the connection password live? Nowhere.")),
                    Beat(34, light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A managed identity tied to the pod's service account.")),
                    Beat(36, light = setOf("wi"), card = BoardCard("Entra", "Microsoft's directory. A federated credential ties the two.")),
                ),
            ),
            // 37: "And the bad release? There is always a bad release."
            Scene(
                37, "Saturday: Tom again", cluster(),
                beats = listOf(
                    Beat(38, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("A bad release", "Tagged latest, applied, rolled over, and wrong. rollout undo fixed it.")),
                    Beat(40, light = setOf("dev"), card = BoardCard("The file is the truth", "He forgot to fix the file, so the next apply brought it back.")),
                ),
            ),
            // 41: "Honestly, after all that, was the move worth it?"
            Scene(
                41, "Was it worth it?", choose(),
                beats = listOf(
                    Beat(42, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("What they gave up", "The front door, numbered versions and zero copies, all free.")),
                    Beat(44, light = setOf("full", "aks"), pulse = "full" to "aks", card = BoardCard("What they took on", "The ingress controller, upgrades, patching and idle machines.")),
                ),
            ),
            // 45: "So what do I remember for the exam...?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("addr", "pull", "pend"), card = BoardCard("Listen for the phrase", "Labels, AcrPull on the kubelet identity, the cluster autoscaler.")),
                    Beat(48, light = setOf("copies", "nopass", "simple"), card = BoardCard("And the rest", "Deployment with replicas, workload identity, or Container Apps.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-aks--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen is closed..."
            Scene(
                0, "Too big for the easy option", outgrow(),
                beats = listOf(
                    Beat(0, show = setOf("aca"), light = setOf("aca"), card = BoardCard("Tonight", "The night an app grows too big for the easy option.")),
                    Beat(2, light = setOf("aca"), card = BoardCard("Where Zaiqa lives", "The Zaiqa API on Container Apps: front door and scaling done for it.")),
                    Beat(4, show = setOf("gpu", "k8sapi"), light = setOf("gpu", "k8sapi"), card = BoardCard("Two things wanted more", "A graphics chip for the Taste service, and the Kubernetes API itself.")),
                    Beat(6, show = setOf("aks"), light = setOf("aks"), pulse = "aca" to "aks", card = BoardCard("AKS", "Kubernetes with Azure minding half of it: a kitchen manager.")),
                ),
            ),
            // 8: "It runs half of it. Kubernetes lives on a cluster..."
            Scene(
                8, "Inside a cluster", cluster(),
                beats = listOf(
                    Beat(8, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Cluster", "A group of computers working as one. Azure keeps the brain.")),
                    Beat(10, light = setOf("cp"), card = BoardCard("Control plane", "Remembers what should be running. Azure patches it; you never log in.")),
                    Beat(12, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Nodes and node pools", "One computer each, by the hour, in groups of identical machines.")),
                    Beat(14, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "A file saying what should run: an order form for the cluster.")),
                    Beat(16, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = applyCode),
                ),
            ),
            // 17: "Lovely. So what do you write on this order form of yours?"
            Scene(
                17, "The order form", objects(),
                beats = listOf(
                    Beat(18, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "A wrapper round one or more containers sharing one address.")),
                    Beat(20, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "Keeps three identical pods running from one image.")),
                    Beat(22, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "One stable address in front of pods that come and go.")),
                    Beat(24, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("One wrong tag", "The Service finds nobody, and the app goes dark in perfect health.")),
                    Beat(26, light = setOf("svc"), card = BoardCard("Ingress controller", "A pod that is the front door. Here, you install it and patch it.")),
                ),
            ),
            // 27: "Mm. The price of a bigger house. What else is on me now?"
            Scene(
                27, "The price of a bigger house", sizing(),
                beats = listOf(
                    Beat(28, light = setOf("req", "lim"), card = BoardCard("Requests and limits", "What the pod needs before a machine takes it, and the ceiling.")),
                    Beat(30, light = setOf("killed"), pulse = "lim" to "killed", card = BoardCard("At the ceiling", "Memory: killed and restarted. Processor: it just runs slower.")),
                ),
            ),
            // 31: "Friday evening. Everybody is hungry at once..."
            Scene(
                31, "Friday evening", scaling(),
                beats = listOf(
                    Beat(32, light = setOf("busy", "more"), pulse = "busy" to "more", card = BoardCard("More pods", "One autoscaler adds pods when the pods get busy: more cooks.")),
                    Beat(34, light = setOf("pending", "auto", "pool"), pulse = "pending" to "auto", card = BoardCard("Pending", "Waiting for room. The cluster autoscaler grows the pool, then shrinks it.")),
                ),
            ),
            // 35: "Good. Now the awkward bit. How do the machines get the images?"
            Scene(
                35, "Getting the images", access(),
                beats = listOf(
                    Beat(36, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ACR", "The private shelf. ImagePullBackOff means it could not download.")),
                    Beat(38, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(40, light = setOf("kubelet"), card = BoardCard("Kubelet identity", "The node agents' badge. One for the whole cluster, not per pool.")),
                    Beat(42, show = setOf("pod", "wi"), light = setOf("pod"), card = BoardCard("Only the nodes", "Your own code still has to read Azure SQL. That needs its own badge.")),
                    Beat(44, light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A managed identity tied to the pod's service account. No password.")),
                    Beat(46, light = setOf("wi"), card = BoardCard("Entra", "Microsoft's directory. A federated credential ties the two.")),
                ),
            ),
            // 47: "It is lovely, Nova. It is also a lot..."
            Scene(
                47, "Should everyone do this?", choose(),
                beats = listOf(
                    Beat(48, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "The front door, numbered versions, and zero copies while you sleep.")),
                    Beat(50, light = setOf("full", "aks"), pulse = "full" to "aks", card = BoardCard("Move for a reason", "Graphics chips, or the Kubernetes API. Not for having moved.")),
                ),
            ),
            // 51: "Before I fall asleep on you. What do I keep for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("addr", "pull", "pend"), card = BoardCard("Listen for the phrase", "Labels, AcrPull on the kubelet identity, the cluster autoscaler.")),
                    Beat(54, light = setOf("copies", "nopass", "simple"), card = BoardCard("The quieter ones", "Deployment with replicas, workload identity, the memory limit.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-aks--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", outgrow(),
                beats = listOf(
                    Beat(0, show = setOf("aca"), light = setOf("aca"), card = BoardCard("Date night", "Zaiqa has outgrown its old home, and we are moving it.")),
                    Beat(2, light = setOf("aca"), card = BoardCard("Where it lives", "The Zaiqa API on Container Apps: front door and scaling done for it.")),
                    Beat(4, show = setOf("gpu", "k8sapi"), light = setOf("gpu", "k8sapi"), card = BoardCard("What it wants", "A graphics chip for the Taste service, and the Kubernetes API.")),
                    Beat(6, show = setOf("aks"), light = setOf("aks"), pulse = "aca" to "aks", card = BoardCard("AKS", "Kubernetes with Azure minding half: a kitchen manager for containers.")),
                ),
            ),
            // 8: "Kubernetes runs on a cluster..."
            Scene(
                8, "Quiz: what is a node?", cluster(),
                beats = listOf(
                    Beat(8, light = setOf("cp"), card = BoardCard("Cluster", "A group of computers working as one. Azure keeps the control plane.")),
                    Beat(9, light = setOf("nodes"), card = BoardCard("Quiz: nodes", "One computer each, by the hour, in node pools of identical machines.")),
                    Beat(10, light = setOf("nodes"), card = BoardCard("Right answer", "One pool is one machine size, so graphics chips get a pool of their own.")),
                    Beat(12, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = applyCode),
                ),
            ),
            // 14: "Behave. On the order form go three things..."
            Scene(
                14, "The order form", objects(),
                beats = listOf(
                    Beat(14, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "A wrapper round one or more containers sharing one address.")),
                    Beat(16, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "Keeps three identical pods running from one image.")),
                    Beat(17, show = setOf("app", "svc"), light = setOf("svc"), card = BoardCard("Quiz: one address", "Pods are replaced all night. Callers need one address. What goes in front?")),
                    Beat(18, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("A Service, by label", "One stable address, finding its pods by the name tag they wear.")),
                    Beat(20, light = setOf("svc"), card = BoardCard("One hyphen", "The Service finds nobody, and the app goes dark in perfect health.")),
                    Beat(22, light = setOf("app", "svc"), pulse = "app" to "svc", card = BoardCard("Ingress controller", "A pod that is the front door. Here, we install it and patch it.")),
                ),
            ),
            // 24: "Sizing. For every container you write requests and limits..."
            Scene(
                24, "Quiz: memory or processor?", sizing(),
                beats = listOf(
                    Beat(24, light = setOf("req", "lim"), card = BoardCard("Requests and limits", "What the pod needs before a machine takes it, and the ceiling.")),
                    Beat(26, light = setOf("lim"), card = BoardCard("Quiz: killed over and over", "A pod is killed and restarted again and again. What do you look at?")),
                    Beat(28, light = setOf("killed"), pulse = "lim" to "killed", card = BoardCard("No kiss for that one", "Memory kills the pod; the processor limit only makes it slower.")),
                ),
            ),
            // 29: "Memory kills, processor only slows... Now, Friday at eight..."
            Scene(
                29, "Friday at eight", scaling(),
                beats = listOf(
                    Beat(30, light = setOf("busy", "more"), pulse = "busy" to "more", card = BoardCard("More pods", "One autoscaler adds pods when the pods are busy: more cooks.")),
                    Beat(32, light = setOf("pending"), card = BoardCard("Quiz: Pending", "What is a pod doing when it says Pending?")),
                    Beat(33, light = setOf("pending", "auto"), pulse = "pending" to "auto", card = BoardCard("Right answer", "Waiting for room. The cluster autoscaler grows the pool, then shrinks it.")),
                ),
            ),
            // 35: "I knew we would get here. Whose key?"
            Scene(
                35, "Whose key?", access(),
                beats = listOf(
                    Beat(36, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ACR", "The private shelf. ImagePullBackOff means it could not download.")),
                    Beat(38, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(40, light = setOf("kubelet"), card = BoardCard("Right answer", "One kubelet identity for the whole cluster, not one per node pool.")),
                    Beat(42, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A managed identity tied to the pod's service account. No password.")),
                    Beat(44, light = setOf("wi"), card = BoardCard("Entra", "Microsoft's directory. A federated credential ties the two.")),
                ),
            ),
            // 45: "Beautiful. And now the question I have been saving..."
            Scene(
                45, "Was it a good idea?", choose(),
                beats = listOf(
                    Beat(46, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("What we gave up", "The front door, numbered versions and zero copies, all free.")),
                    Beat(48, light = setOf("full", "aks"), pulse = "full" to "aks", card = BoardCard("Move for a reason", "Graphics chips, or the Kubernetes API. Not because you can.")),
                ),
            ),
            // 49: "Pointed. Last one, for the exam. What do we remember?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("addr", "pull", "pend"), card = BoardCard("Listen for the phrase", "Labels, AcrPull on the kubelet identity, the cluster autoscaler.")),
                    Beat(52, light = setOf("copies", "nopass", "simple"), card = BoardCard("And the rest", "Deployment with replicas, workload identity, the memory limit.")),
                    Beat(55, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
