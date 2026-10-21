package com.shahaabapps.core.content

/** Azure Kubernetes Service: deploy with manifest files: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAks {
    private val classic = Podcast(
        "ai-aks",
        listOf(
            maya("Hi, and welcome back. Today is Azure Kubernetes Service, AKS for short. First, the job it does. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. You want three copies of that backend running at all times, and a fresh one started whenever one falls over."),
            leo("Three copies? I can barely keep one running. Where do I even start?"),
            maya("With a container. That's your app packed in a box with everything it needs, so it runs the same on your laptop and in the cloud. The ready-to-start copy of that box is called an image. Think of a frozen meal: cooked once, heated anywhere."),
            leo("And where do the frozen meals live?"),
            maya("On a private shelf called a registry. For you that's ACR, Azure Container Registry, where your team keeps its images. Each image carries a tag, a version label such as v2."),
            leo("Okay. So what's Kubernetes, in plain words?"),
            maya("A system that runs lots of containers across lots of machines, and keeps them running. Picture a restaurant manager. You say, I want three cooks on the grill. If one goes home sick, the manager calls in another, without asking you."),
            leo("And AKS is that manager, run by Azure?"),
            maya("Half of it. A Kubernetes setup is called a cluster, and it has two halves. The first is the control plane, the brain. Think of head office, which remembers what should be running. Azure runs that half for you."),
            leo("And the other half?"),
            maya("The nodes. A node is a virtual machine, a computer you rent in Azure's data centre by the hour. Your containers actually run on the nodes. A group of identical nodes is a node pool."),
            leo("So how do I tell head office what I want? Click around a website?"),
            maya("You write it down in a file called a manifest. It's written in YAML, a plain-text format of names and values, one per line, with indents to show what belongs to what. Think of it as an order form: three copies of this image, please."),
            leo("And how does the order form reach the cluster?"),
            maya("With kubectl, the command-line tool that talks to the cluster. You type kubectl apply, with the f flag, f for file, and the file's name. From then on, Kubernetes keeps reality matching the file. That's called desired state. A thermostat works the same way: you set the temperature, it keeps it there."),
            leo("What goes on the order form? What are the actual things?"),
            maya("The smallest thing Kubernetes runs is a pod. A pod wraps one or more containers that share one network address. For the Coach, one pod is one running copy of the Python backend."),
            leo("And three copies means three pods. So I write three pods?"),
            maya("No, you write a Deployment. It's like a shift manager who always keeps the same number of cashiers at the tills. You set replicas to three, meaning three copies, and it keeps three identical pods running. One dies, it starts another."),
            leo("Nice. Then my phone app just calls one of those pods?"),
            maya("Never directly. Pods come and go, and each new pod gets a new IP address, its street address on the network. So you put a Service in front. A Service is a stable name and address that never changes, like a shop's phone number that rings whichever cashier is on shift."),
            leo("How does the Service know which pods are its cashiers?"),
            maya("By labels. A label is a name tag on a pod, such as app equals coach api. The Service has a label selector, a rule that says, send calls to every pod wearing this tag. And a classic mistake: if the tags don't match, the Service has no pods to send to."),
            leo("Name tags. Very office party. What about settings, like which AI model the backend should use?"),
            maya("Plain settings go in a ConfigMap. Kubernetes hands them to the pods as environment variables, values a program reads when it starts, like a note taped to the fridge."),
            leo("And passwords? Same fridge?"),
            maya("Sensitive values go in a Secret. But careful. A Kubernetes Secret is only base64 encoded, a scramble anyone can undo, not a lock. So never commit a Secret file to Git, where your code's history lives. Real secrets belong in Key Vault, Azure's safe."),
            leo("Got it. Now I ship version three, and it's broken. Do I panic?"),
            maya("[chuckle] No panic. You change the image tag in the file to v3 and apply it again. The Deployment does a rolling update: it starts new pods and stops old ones a few at a time, so the app never fully goes down. Like swapping cashiers one by one while the shop stays open."),
            leo("And how do I watch it happen?"),
            maya("kubectl rollout status waits and tells you when the update has finished. If v3 is broken, kubectl rollout undo takes you back to the previous version, with another rolling update."),
            leo("Undo. My favourite button. Then I'm done?"),
            maya("One step people forget. Put the old tag back in the file too, because the file is the source of truth, or the next apply brings the broken version straight back. And never use the tag called latest, or no rollout can tell versions apart."),
            leo("Next problem. My pods won't start. They say ImagePullBackOff. What does that even mean?"),
            maya("It's Kubernetes saying, I can't download the image, so I'm waiting before I try again. If the tag exists, the nodes probably aren't allowed to read your private registry. The fix is one command in az, the Azure command-line tool: az aks update, attach acr, and the registry's name."),
            leo("What does attaching actually do?"),
            maya("It gives a role called AcrPull, permission to pull, which means download, images from your registry. And it gives that role to the cluster's kubelet identity."),
            leo("Kubelet. Sounds like a baby kettle."),
            maya("[laugh] The kubelet is the small agent on every node that starts the containers. Its identity is a managed identity: a login Azure gives a resource, like a staff badge, with no password to leak."),
            leo("And when my Python code needs to read the Coach's database? Does it borrow the node's badge?"),
            maya("No, your code gets its own, through workload identity. A pod runs as a service account, its Kubernetes identity. Workload identity links that service account to a managed identity. Then DefaultAzureCredential, the Python helper that finds whatever sign-in is around, picks it up with no password and no code change."),
            leo("So should every app go on AKS?"),
            maya("No. Picture a small app with one simple backend. Azure Container Apps runs containers and scales them for you, even down to zero copies when nobody calls, with no cluster or nodes to manage. Pick AKS when you need Kubernetes itself, or full control, like your own pools of machines with graphics chips for AI."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If the question says keep three copies running, the answer is a Deployment with replicas three. If pods keep getting new addresses, a Service that finds them by label. If a release is bad, kubectl rollout undo. If ImagePullBackOff from ACR and the tag exists, attach the registry for AcrPull. If you don't need Kubernetes itself, Container Apps."),
            leo("And anything to try today?"),
            maya("Create a two-node AKS cluster, and get its credentials so kubectl can reach it. Write a Deployment and a Service for a public sample image, and apply them. Change the image tag, apply again, and watch the rollout status. Run rollout undo, and check the old pods return. Then delete the resource group, the folder holding it all: a cluster costs money by the hour."),
            leo("Three cooks, one manager, and an undo button. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-aks",
        listOf(
            maya("Welcome to Case Files. Today's case: The Phone That Rang and Rang. A small Android app called Flashcard Fox helps students drill their flashcards. Its Python backend has just moved onto Azure Kubernetes Service, AKS. Moving day went fine. Then the app asked the backend for cards, and nobody answered."),
            leo("[chuckle] Detective Leo, on the case. Before I suspect anyone, what is AKS, in plain words?"),
            maya("Start with a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running. Picture a restaurant manager who always keeps the right number of cooks on shift. AKS is Kubernetes with Azure doing half the work."),
            leo("Which half?"),
            maya("A Kubernetes setup is called a cluster, and its brain is the control plane. That's head office, which remembers what should be running. Azure runs head office for you."),
            leo("And the rest of the building?"),
            maya("The nodes. A node is a virtual machine, a computer you rent from Azure by the hour, and your containers actually run on the nodes. Flashcard Fox has two."),
            leo("So how did the team tell head office what to run?"),
            maya("In a manifest, a file written in YAML, a plain-text format of names and values with indents. It's an order form. They sent it with kubectl, the command-line tool for the cluster. Under the picture, the control plane keeps comparing the form with what's really running, and fixes any difference. That's called desired state."),
            leo("Fine. What was on the order form?"),
            maya("A Deployment. It's like a shift manager who keeps the same number of cashiers at the tills. Each cashier is a pod, the smallest thing Kubernetes runs: one or more containers sharing one network address. The form said replicas three, so three copies. All three pods were up and healthy."),
            leo("Healthy pods, silent app. First suspect: the pods aren't ready yet. Add a readiness probe."),
            maya("[pause] Good instinct, wrong suspect. A readiness probe is a check that says when a pod is ready for callers, and these pods were ready. The clue is in how the app reaches them. Pods come and go, and each new one gets a new IP address, its street address on the network."),
            leo("So you can't phone a pod directly."),
            maya("Exactly. You put a Service in front: a stable name and address that never changes, like a shop's phone number that rings whichever cashier is on. The Service finds its pods by labels, name tags on each pod. Its label selector says, send calls to every pod wearing this tag."),
            leo("Second suspect, then. The Service type is wrong. Make it a LoadBalancer."),
            maya("It already was one. LoadBalancer asks Azure for an address outside callers can reach; the default, ClusterIP, works only inside the cluster. But the type decides how calls arrive, not which pods get them. Look at the labels. The pods wore app equals flashcards. The Service was looking for app equals flashcard api."),
            leo("[laugh] The phone rang in an empty shop."),
            maya("Under the picture, a Service keeps a list of the ready pods that match its selector, called endpoints. That list was empty. They fixed one label, applied the file again, and the cards came back. But a second case opened the same week."),
            leo("Go on."),
            maya("Version three of the backend shipped, and it served the wrong cards. They had changed the image tag in the manifest from v2 to v3. An image is the ready-to-start copy of the app, like a frozen meal, and the tag is its version label."),
            leo("Kubernetes swapped all the pods at once, then? Big outage?"),
            maya("No. A rolling update: new pods start and old ones stop a few at a time, so the app never fully goes down. kubectl rollout status watches it finish. And the way back is kubectl rollout undo, which returns the Deployment to its previous version."),
            leo("Suspect: they should delete the Deployment and create it again. Clean slate."),
            maya("That takes the whole app offline, and the bad tag is still in the file. Rollout undo is the fastest way back, and the junior developer ran it. Version two returned. Then, two days later, someone applied the file for a small change, and version three was back."),
            leo("Because the file still said v3."),
            maya("Exactly. The file is the source of truth. After an undo, put the old tag back in the file too. And never use the tag called latest, or no rollout can tell the versions apart."),
            leo("Any more crimes?"),
            maya("Two. The team added a node pool, a second group of identical machines, and its pods showed ImagePullBackOff. That's Kubernetes saying, I can't download the image, so I'm waiting before I try again. And the tag existed."),
            leo("Then the image is somewhere private, and the nodes have no key."),
            maya("Sharp. The image lives in ACR, Azure Container Registry, a private shelf for the team's images. The fix is one command in az, the Azure command-line tool: az aks update, attach acr, and the registry's name."),
            leo("What does attaching actually grant?"),
            maya("A role called AcrPull, permission to download images. It goes to the kubelet identity. The kubelet is the agent on each node that starts the containers, and its identity is a managed identity: a login Azure gives a resource, like a staff badge with no password. The Container Registry episode has the rest."),
            leo("And the last crime?"),
            maya("Someone committed a Secret file to Git, where the code's history lives, to hand the backend its database password. A Kubernetes Secret is only base64 encoded, a scramble anyone can undo. That's where Secrets stop being safe. Keep real secrets in Key Vault, Azure's safe, or better, need no password at all."),
            leo("No password at all? How?"),
            maya("Workload identity. A pod runs as a service account, its Kubernetes identity, and workload identity links that to a managed identity. Under the hood, the cluster hands the pod a signed token, a short-lived digital pass, and Entra ID, Microsoft's sign-in service, trusts it through a federated credential, a trust link. In Python, DefaultAzureCredential, the helper that finds whatever sign-in is around, picks it up."),
            leo("Last question. Was AKS even the right home for Flashcard Fox?"),
            maya("Honestly, maybe not. For one simple backend, Azure Container Apps runs containers with no cluster or nodes to manage, and can shrink to zero copies when nobody calls. AKS earns its place when you need Kubernetes itself, or full control, like your own node pools with graphics chips for AI. The Container Apps episode compares the two."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If the question says keep three copies running, the answer is a Deployment with replicas three. If pods keep getting new addresses, a Service that finds them by label. If a release is bad and you need the old one fast, kubectl rollout undo. If ImagePullBackOff from ACR and the tag exists, attach the registry for AcrPull. If you don't need Kubernetes itself, Container Apps."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a two-node AKS cluster and get its credentials. Write a Deployment and a Service for a public sample image, and apply them. Change the image tag, apply again, and watch the rollout status. Run rollout undo and check the old pods return. Then delete the resource group, the folder holding it all."),
            leo("Match your labels, and fix your files. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-aks",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They moved the Two Tracks Coach, the study assistant in the Android app, onto Azure Kubernetes Service. The Python backend, the lot. And let's just say, it was a week."),
            leo("[laugh] I heard someone cried in the kitchen. Start at the start. What is Azure Kubernetes Service?"),
            maya("First, a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running, like a restaurant manager keeping enough cooks on shift. AKS is Kubernetes with Azure doing half the work."),
            leo("Which half?"),
            maya("The brain. A Kubernetes setup is a cluster, and its brain is the control plane, head office, which remembers what should be running. Azure runs that. You get the nodes, virtual machines, which are computers rented by the hour, where the containers actually run."),
            leo("And how do you talk to head office?"),
            maya("With an order form called a manifest, a file in YAML, a plain-text format of names and values with indents. You send it with kubectl, the command-line tool for the cluster. Under the hood, the control plane keeps comparing the file with what's really running, and fixes any difference. That's desired state."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom wrote the Deployment, which keeps a set number of identical pods running. A pod is the smallest thing Kubernetes runs, a wrapper round one or more containers. Replicas three, three copies of the backend, all healthy. And the Android app got nothing."),
            leo("Nothing? Three healthy copies and nothing?"),
            maya("Pods get a new IP address, a new street address on the network, every time they're replaced. So the app calls a Service, a stable address that never changes, like a shop's phone number. The Service finds its pods by labels, name tags on each pod. Tom's pods said coach api. His Service was looking for coach backend."),
            leo("[sigh] Poor Tom. The phone rang in an empty shop."),
            maya("Exactly. Under the picture, a Service keeps a list of the matching ready pods, called endpoints, and Tom's was empty. One label fixed, and it worked. Oh, and his Service was a LoadBalancer, which asks Azure for an address outsiders can reach. The default, ClusterIP, only works inside the cluster."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. The backend needed settings. Plain settings go in a ConfigMap, handed to the pods as environment variables, values a program reads at start. But the database password went in a Secret, and Dana committed that Secret file to Git, the code's history."),
            leo("Isn't a Secret, well, secret?"),
            maya("That's what Dana thought. A Kubernetes Secret is only base64 encoded, a scramble anyone can undo. Not a lock. Security had words with her before lunch. Real secrets go in Key Vault, Azure's safe. The Key Vault episode covers it."),
            leo("I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. He shipped version three by changing the image in the manifest. An image is the ready-to-start copy of the app, like a frozen meal. But instead of a version tag, a label like v3, Marcus used the tag called latest."),
            leo("What's wrong with latest? It's, well, the latest."),
            maya("Latest isn't a version, it's a moving target. So when v3 turned out broken, nobody could say which image was which, and rolling back was guesswork. Proper tags let Kubernetes tell versions apart."),
            leo("How does rolling back even work?"),
            maya("A new tag in the file causes a rolling update. New pods start and old ones stop a few at a time, so the app never fully goes down. kubectl rollout status watches it. And kubectl rollout undo goes back to the previous version, with another rolling update."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. Version four was broken, and the intern did exactly the right thing. Rollout undo, back to the old version, errors gone. Applause in the channel."),
            leo("That sounds like a good day."),
            maya("Until Friday morning, when someone applied the file for a tiny change, and version four came straight back. The file still said v4. The file is the source of truth, so after an undo, put the old tag back in the file too."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. A new node pool, a second group of identical machines, came up, and every pod on it said ImagePullBackOff. That's Kubernetes for, I can't download the image, so I'm waiting before I try again. And the tag existed."),
            leo("So the nodes couldn't open the cupboard."),
            maya("The cupboard is ACR, Azure Container Registry, the team's private shelf of images. Priya ran one az command, the Azure command-line tool: az aks update, attach acr. That gives the AcrPull role, permission to download images, to the kubelet identity, the login the nodes use."),
            leo("Kubelet?"),
            maya("The agent on each node that starts the containers. Its identity is a managed identity, a login Azure gives a resource, like a staff badge with no password. And for the backend's own code, there's workload identity: it links a pod's service account, its Kubernetes identity, to a managed identity, so DefaultAzureCredential, the helper that finds a sign-in, just works."),
            leo("So did anything go right?"),
            maya("The best gossip of all. Priya asked whether the Coach needed AKS at all. For one simple backend, Azure Container Apps runs containers with no cluster or nodes to manage, and can shrink to zero copies when nobody calls. AKS is for when you need Kubernetes itself, or full control, like machines with graphics chips for AI. The Container Apps episode compares the two."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. If the question says keep three copies running, a Deployment with replicas three. If pods keep changing address, a Service that finds them by label. If a release is bad, kubectl rollout undo, then fix the file. If ImagePullBackOff from ACR and the tag exists, attach the registry for AcrPull. If you don't need Kubernetes itself, Container Apps."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a two-node AKS cluster and get its credentials. Write a Deployment and a Service for a public sample image, and apply them. Change the image tag, apply again, and watch the rollout status. Run rollout undo and check the old pods return. Then delete the resource group, the folder holding it all."),
            leo("Real tags, matching labels. Got it, Marcus. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-aks",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about keeping things running while you sleep."),
            ivy("Mm. Something that stays up all night so I don't have to? Tell me more, Nova."),
            nova("It's called Kubernetes. Start with a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes runs lots of those boxes across lots of machines, and keeps them running. Picture a night manager who keeps the right number of staff on shift, and quietly calls in cover when someone leaves."),
            ivy("A night manager. I like them already. And Azure's version?"),
            nova("Azure Kubernetes Service, AKS. A Kubernetes setup is called a cluster. Its brain is the control plane, the part that remembers what should be running, [pause] and in AKS, Azure runs the brain for you."),
            ivy("And what do I run?"),
            nova("The nodes. A node is a virtual machine, a computer you rent from Azure by the hour. Your containers actually run there. A group of identical nodes is a node pool."),
            ivy("So how do I whisper my wishes to this brain?"),
            nova("You write them down. A manifest is a file in YAML, a plain-text format of names and values with indents. It's a wish list. You send it with kubectl, the command-line tool for the cluster, using kubectl apply. [pause] And the cluster keeps your wish true. That's called desired state."),
            ivy("Tell me what's really happening there."),
            nova("Under the picture, the control plane runs a loop, all night long. It compares what your file asks for with what's actually running, and fixes any difference. A container dies at three in the morning, [pause] and a new one is started before you've rolled over."),
            ivy("Now that's devotion. What goes on the wish list?"),
            nova("Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. The smallest thing Kubernetes runs is a pod: one or more containers sharing one network address. One pod is one running copy of the Coach's backend."),
            ivy("And I want more than one copy, don't I."),
            nova("You do. So you write a Deployment, which keeps a set number of identical pods running. Replicas three, three copies. It's the night manager's rota."),
            ivy("And how does the app find them, in the dark?"),
            nova("Not by name. Pods come and go, and each new one gets a new IP address, its street address on the network. So you put a Service in front: a stable address that never changes, [pause] like one phone number that rings whoever is on shift."),
            ivy("And how does the Service know who's on shift?"),
            nova("Labels. Name tags on each pod, such as app equals coach api. The Service has a label selector that says, ring everyone wearing this tag. If the tags don't match, the phone rings in an empty room. Under the hood, the Service keeps a list of matching ready pods, called endpoints."),
            ivy("Lonely phones. Again. And can strangers call?"),
            nova("Only if you let them. ClusterIP, the default, is for callers inside the cluster. LoadBalancer asks Azure for an address the outside world can reach, like the Coach's Android app."),
            ivy("Settings? Secrets? Where do I keep those?"),
            nova("Plain settings go in a ConfigMap, handed to the pods as environment variables, values a program reads when it wakes up. Sensitive ones go in a Secret. [pause] But a Kubernetes Secret is only base64 encoded, a scramble anyone can undo. Never commit one to Git, your code's history. Real secrets belong in Key Vault, and the Key Vault episode is a good night's listening."),
            ivy("Now. What happens when I change my mind? A new version?"),
            nova("You put a new image tag in the file. An image is the ready-to-start copy of the app, and the tag is its version label, like v3. Apply it, and you get a rolling update: new pods start, old ones stop, a few at a time. The app never fully goes dark."),
            ivy("And if version three is a mistake? It happens. Late at night."),
            nova("It does. kubectl rollout status watches the update. If it's bad, kubectl rollout undo takes you back to the previous version. [pause] And then, the part everyone forgets: put the old tag back in the file too. The file is the source of truth, and the next apply believes it."),
            ivy("Mm. And where does all this stop working?"),
            nova("A few places. Kubernetes can't pull an image it isn't allowed to read. The pods sit there saying ImagePullBackOff, which means, I can't download the image, so I'm waiting before I try again."),
            ivy("And the cure?"),
            nova("If the image lives in ACR, Azure Container Registry, your team's private shelf, one command in az, the Azure command-line tool: az aks update, attach acr. It gives a role called AcrPull, permission to download, to the kubelet identity. The kubelet is the agent on each node that starts containers, and its identity is a managed identity, a staff badge with no password."),
            ivy("And my own code? Does it get a badge?"),
            nova("Through workload identity. A pod runs as a service account, its Kubernetes identity, linked to a managed identity. The cluster hands the pod a signed token, a short-lived digital pass, and Entra ID, Microsoft's sign-in service, swaps it for an Azure one. DefaultAzureCredential, the Python helper that finds a sign-in, does the rest. No password at all."),
            ivy("And is AKS always the answer?"),
            nova("No. [pause] For one simple backend, Azure Container Apps runs containers with no cluster to manage, and can shrink to zero copies when nobody calls. AKS is for when you need Kubernetes itself, or full control, like machines with graphics chips for AI. The Container Apps episode is your next late night."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. If the question says keep three copies running, a Deployment with replicas three. If pods keep changing address, a Service that finds them by label. If a release is bad, kubectl rollout undo. If ImagePullBackOff from ACR and the tag exists, attach the registry for AcrPull. If you don't need Kubernetes itself, Container Apps."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a two-node AKS cluster and get its credentials. Write a Deployment and a Service for a public sample image, and apply them. [pause] Change the image tag, apply again, and watch the rollout status. Run rollout undo and see the old pods return. Then delete the resource group, the folder holding it all."),
            ivy("Something that keeps running, so I can rest. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-aks",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about something that never lets you down."),
            rafe("Me?"),
            nova("[laugh] Kubernetes. Something that keeps things running all night long, without being asked twice."),
            rafe("Then it is me. Go on, tell me what it really is."),
            nova("Start with a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes runs lots of those boxes across lots of machines, and keeps them running, like a restaurant manager who always keeps enough cooks on shift. Azure Kubernetes Service, AKS, is Kubernetes with Azure doing half the work."),
            rafe("Half the work. My kind of partner. Which half?"),
            nova("The brain. A Kubernetes setup is called a cluster, and its brain is the control plane, which remembers what should be running. Azure runs that. You get the nodes, virtual machines, computers rented by the hour, where the containers live."),
            rafe("And how do I tell it what I want? I'm told communication matters."),
            nova("You write it down. A manifest, a file in YAML, a plain-text format of names and values with indents. Send it with kubectl, the command-line tool for the cluster. And it keeps your wishes true. That's desired state."),
            rafe("Desired state. I know all about that."),
            nova("Under the hood, the control plane keeps comparing your file with what's really running, and fixes any difference. Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend. The smallest thing Kubernetes runs is a pod, one or more containers sharing one network address."),
            rafe("Then let me test you, darling. Exam style. You want three copies of the Coach's backend always running. What do you write?"),
            nova("A Deployment, which keeps a set number of identical pods running, with replicas set to three. One dies, it starts another."),
            rafe("Right answer. I like a partner who keeps copies of me around. [kiss]"),
            nova("Mm. My turn. Pods are replaced, and each new one gets a new IP address, its street address on the network. So how does the app still reach them?"),
            rafe("Through a Service, a stable address that never changes, like one phone number that rings whoever's on shift. It finds the pods by labels, name tags, through its label selector."),
            nova("Right answer. Wearing the right name tag gets you a call. [kiss]"),
            rafe("And if the tags don't match?"),
            nova("Then the phone rings in an empty room. The Service keeps a list of the matching ready pods, called endpoints, and it's empty. A Service can be ClusterIP, the default, inside the cluster only, or LoadBalancer, an address outsiders can reach."),
            rafe("Inside only, or open to the world. Like some people I know. Now, secrets. Where do we keep ours?"),
            nova("Plain settings go in a ConfigMap, handed to pods as environment variables, values a program reads at start. Sensitive ones go in a Secret. But a Kubernetes Secret is only base64 encoded, a scramble anyone can undo."),
            rafe("So never commit a Secret. To Git, I mean, where the code's history lives."),
            nova("[chuckle] Never commit a Secret to Git. Commit to me instead. Real secrets go in Key Vault, Azure's safe; the Key Vault episode has the details."),
            rafe("Deal. Now the dangerous part. A new version. What happens?"),
            nova("You change the image tag in the file. An image is the ready-to-start copy of the app, and the tag is its version label, like v3. Apply it, and you get a rolling update: new pods start and old ones stop, a few at a time, so the app never goes down."),
            rafe("Slow and steady. And kubectl rollout status watches it finish. So, darling. Version three is broken and users are angry. Fastest way back?"),
            nova("kubectl rollout undo. It returns the Deployment to the previous version, with another rolling update."),
            rafe("Right answer. Everyone deserves a second chance. [kiss]"),
            nova("And then?"),
            rafe("Then I'm done. Back to the wine."),
            nova("No kiss for that one. You forgot the file. It's the source of truth, so put the old tag back in it, or the next apply brings version three straight back. And never use the tag called latest. Nobody can tell versions apart."),
            rafe("Fine, fine. Next question for you. Pods say ImagePullBackOff from ACR, the team's private image shelf, Azure Container Registry. The tag exists. What's the fix?"),
            nova("ImagePullBackOff means, I can't download the image, so I'm waiting to try again. So attach the registry, with az, the Azure command-line tool: az aks update, attach acr. That gives the AcrPull role, permission to download, to the kubelet identity, the login of the agent on each node that starts the containers."),
            rafe("Right answer. Some cupboards need the right key. [kiss]"),
            nova("And where does it stop working, Rafe? Every romance has its limits."),
            rafe("The kubelet's badge is for the nodes, not your code. For the code there's workload identity: a pod's service account, its Kubernetes identity, is linked to a managed identity, a login with no password. Entra ID, Microsoft's sign-in service, trusts the pod's signed token, a short-lived digital pass, and DefaultAzureCredential, the Python sign-in helper, picks it up."),
            nova("Smooth. And when is AKS the wrong partner altogether?"),
            rafe("When you only have one simple backend. Azure Container Apps runs containers with no cluster to manage, and can shrink to zero copies when nobody calls. AKS is for when you need Kubernetes itself, or machines with graphics chips for AI. The Container Apps episode compares them."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("Keep three copies running: a Deployment with replicas three. Pods changing address: a Service that finds them by label. A bad release: kubectl rollout undo, then fix the file. ImagePullBackOff from ACR with the tag there: attach the registry for AcrPull. And no need for Kubernetes itself: Container Apps."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a two-node AKS cluster and get its credentials. Write a Deployment and a Service for a public sample image, and apply them. Change the image tag, apply again, and watch the rollout status. Run rollout undo and check the old pods return. Then delete the resource group, the folder holding it all."),
            rafe("Three copies of you, and an undo button. Perfect night. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun intro() = DiagramSpec(
        "Your app is packed into an image, kept on a private shelf in ACR, and Kubernetes keeps copies of it running.",
        nodes = listOf(
            DNode("app", "Your app", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "packed in a box"),
            DNode("image", "Image", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "tag: v2"),
            DNode("acr", "ACR", 0.75f, 0.76f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "private shelf"),
            DNode("k8s", "Kubernetes", 0.25f, 0.76f, w = 0.42f, h = 0.22f, tone = 1, sub = "keeps it running"),
        ),
        edges = listOf(
            DEdge("app", "image", "packed"),
            DEdge("image", "acr", "stored"),
            DEdge("acr", "k8s", "pulled"),
        ),
        aspect = 0.8f,
    )

    private fun cluster() = DiagramSpec(
        "A manifest goes to the control plane, which Azure runs, and it keeps the nodes running what the file asks for.",
        nodes = listOf(
            DNode("dev", "kubectl apply", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the manifest"),
            DNode("cp", "Control plane", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "run by Azure"),
            DNode("nodes", "Node pool", 0.75f, 0.76f, w = 0.42f, h = 0.22f, sub = "virtual machines"),
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
            DNode("pods", "Three pods", 0.76f, 0.76f, w = 0.42f, h = 0.22f, sub = "label: coach-api"),
        ),
        edges = listOf(
            DEdge("app", "svc", "calls"),
            DEdge("svc", "pods", "by label"),
            DEdge("deploy", "pods", "keeps 3"),
        ),
        aspect = 0.8f,
    )

    private fun config() = DiagramSpec(
        "Plain settings come from a ConfigMap; a Secret is only base64, so real secrets belong in Key Vault.",
        nodes = listOf(
            DNode("cm", "ConfigMap", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "plain settings"),
            DNode("sec", "Secret", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "base64, not a lock"),
            DNode("pods", "Pods", 0.25f, 0.76f, w = 0.42f, h = 0.22f, sub = "environment variables"),
            DNode("kv", "Key Vault", 0.75f, 0.76f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 1, sub = "real secrets"),
        ),
        edges = listOf(
            DEdge("cm", "pods", "env vars"),
            DEdge("sec", "kv", "better", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun rollout() = DiagramSpec(
        "A new tag in the file rolls pods from v2 to v3 a few at a time; rollout undo goes back, and the file must follow.",
        nodes = listOf(
            DNode("file", "coach.yaml", 0.5f, 0.14f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "the source of truth"),
            DNode("v2", "Pods on v2", 0.25f, 0.5f, w = 0.42f, h = 0.2f),
            DNode("v3", "Pods on v3", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "broken"),
            DNode("undo", "rollout undo", 0.5f, 0.85f, w = 0.6f, h = 0.18f, kind = NodeKind.PILL, sub = "back one version"),
        ),
        edges = listOf(
            DEdge("file", "v3", "new tag"),
            DEdge("v2", "v3", "rolling update"),
            DEdge("v3", "undo"),
            DEdge("undo", "v2", "back"),
        ),
        aspect = 0.8f,
    )

    private fun access() = DiagramSpec(
        "The nodes pull images with the kubelet identity and AcrPull; the code signs in through workload identity.",
        nodes = listOf(
            DNode("acr", "ACR", 0.25f, 0.18f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "private images"),
            DNode("kubelet", "Kubelet identity", 0.75f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "the nodes' badge"),
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
        "One simple backend fits Container Apps; AKS is for when you need Kubernetes itself or full control.",
        nodes = listOf(
            DNode("simple", "One simple backend", 0.25f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("full", "Full control", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "graphics chips"),
            DNode("aca", "Container Apps", 0.25f, 0.76f, w = 0.44f, h = 0.22f, tone = 1, sub = "no cluster to run"),
            DNode("aks", "AKS", 0.75f, 0.76f, w = 0.42f, h = 0.22f, sub = "Kubernetes itself"),
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
            DNode("addr", "New pod addresses", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a Service, by label"),
            DNode("bad", "A bad release", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "rollout undo"),
            DNode("pull", "ImagePullBackOff", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "attach ACR: AcrPull"),
            DNode("simple", "No need for Kubernetes", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Apps"),
            DNode("file", "After an undo", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "fix the file too"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("copies", "addr", "bad", "pull", "simple", "file")
    private val applyCode = BoardCard("Shell", "kubectl apply -f coach.yaml", code = true)
    private val undoCode = BoardCard("Shell", "kubectl rollout undo deployment/coach-api", code = true)
    private val attachCode = BoardCard("Shell", "az aks update -g tt-rg -n tt-aks --attach-acr ttcoach", code = true)
    private val today = BoardCard("Today", "A two-node cluster, apply, change the tag, rollout undo, delete it all.")

    private fun classicBoard() = PodcastBoard(
        "ai-aks",
        listOf(
            // 0: "Hi, and welcome back. Today is Azure Kubernetes Service..."
            Scene(
                0, "Why Kubernetes?", intro(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Three copies, always", "The Coach's Python backend: three copies, a fresh one if one falls.")),
                    Beat(2, show = setOf("image"), light = setOf("image"), pulse = "app" to "image", card = BoardCard("Container and image", "The app packed in a box; the image is the ready-to-start copy.")),
                    Beat(4, show = setOf("acr"), light = setOf("acr"), pulse = "image" to "acr", card = BoardCard("ACR", "Azure Container Registry: the private shelf for images, each with a tag.")),
                    Beat(6, show = setOf("k8s"), light = setOf("k8s"), pulse = "acr" to "k8s", card = BoardCard("Kubernetes", "Runs many containers on many machines, and keeps them running.")),
                ),
            ),
            // 8: "Half of it. A Kubernetes setup is called a cluster..."
            Scene(
                8, "Inside a cluster", cluster(),
                beats = listOf(
                    Beat(8, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain, head office: remembers what should run. Azure runs it.")),
                    Beat(10, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Nodes and node pools", "Virtual machines rented by the hour; identical ones form a pool.")),
                    Beat(12, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "An order form in YAML: names and values, with indents.")),
                    Beat(14, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = applyCode),
                ),
            ),
            // 15: "What goes on the order form? What are the actual things?"
            Scene(
                15, "Pods, Deployment, Service", objects(),
                beats = listOf(
                    Beat(16, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "The smallest thing Kubernetes runs: containers sharing one address.")),
                    Beat(18, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "Keeps a set number of identical pods: replicas 3, three copies.")),
                    Beat(20, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "A stable name and address in front of pods that come and go.")),
                    Beat(22, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Labels must match", "The selector picks pods by name tag. No match, no pods to call.")),
                ),
            ),
            // 23: "Name tags. Very office party. What about settings..."
            Scene(
                23, "Settings and secrets", config(),
                beats = listOf(
                    Beat(24, show = setOf("cm", "pods"), light = setOf("cm"), pulse = "cm" to "pods", card = BoardCard("ConfigMap", "Plain settings, handed to pods as environment variables.")),
                    Beat(26, show = setOf("sec", "kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("Secret", "Only base64, a scramble anyone can undo. Never commit it to Git.")),
                ),
            ),
            // 27: "Got it. Now I ship version three, and it's broken."
            Scene(
                27, "Rolling out v3", rollout(),
                beats = listOf(
                    Beat(28, show = setOf("file", "v2", "v3"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "New pods start, old ones stop, a few at a time. Never fully down.")),
                    Beat(30, show = setOf("undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(32, light = setOf("file"), pulse = "file" to "v3", card = BoardCard("The file is the truth", "Put the old tag back in the file. And never use latest.")),
                ),
            ),
            // 33: "Next problem. My pods won't start. They say ImagePullBackOff."
            Scene(
                33, "Pulling from ACR", access(),
                beats = listOf(
                    Beat(33, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(34, show = setOf("kubelet"), light = setOf("kubelet", "acr"), card = attachCode),
                    Beat(36, pulse = "kubelet" to "acr", card = BoardCard("AcrPull", "Permission to download images, given to the kubelet identity.")),
                    Beat(38, light = setOf("kubelet"), card = BoardCard("Kubelet identity", "The agent on each node; its managed identity is a badge, no password.")),
                    Beat(40, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "The pod's own sign-in, through its service account. No password.")),
                ),
            ),
            // 41: "So should every app go on AKS?"
            Scene(
                41, "AKS or Container Apps?", choose(),
                beats = listOf(
                    Beat(42, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "No cluster or nodes to manage; can shrink to zero copies.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("copies", "bad", "pull"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: Deployment, Service, undo, AcrPull.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-aks--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Phone That Rang and Rang..."
            Scene(
                0, "The Phone That Rang and Rang", intro(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("The case", "Flashcard Fox moved to AKS. The app asked for cards; nobody answered.")),
                    Beat(2, show = setOf("k8s"), light = setOf("k8s"), card = BoardCard("Kubernetes", "Runs many containers on many machines, and keeps them running.")),
                ),
            ),
            // 3: "Which half?"
            Scene(
                3, "Head office", cluster(),
                beats = listOf(
                    Beat(4, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain: remembers what should be running. Azure runs it.")),
                    Beat(6, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Nodes", "Virtual machines rented by the hour, where containers run.")),
                    Beat(8, show = setOf("dev"), light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("Desired state", "The manifest is an order form; the cluster keeps reality matching it.")),
                ),
            ),
            // 9: "Fine. What was on the order form?"
            Scene(
                9, "The suspects", objects(),
                beats = listOf(
                    Beat(10, show = setOf("deploy", "pods"), light = setOf("deploy", "pods"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "Keeps a set number of identical pods: replicas 3, all healthy.")),
                    Beat(11, light = setOf("pods"), card = BoardCard("Suspect: a readiness probe", "Cleared. The pods were ready; the clue is how the app reaches them.")),
                    Beat(14, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "A stable address in front of pods; it finds them by label.")),
                    Beat(15, light = setOf("svc"), card = BoardCard("Suspect: the Service type", "Cleared. LoadBalancer was right; the type doesn't pick the pods.")),
                    Beat(16, light = setOf("svc", "pods"), card = BoardCard("The labels", "Pods wore flashcards; the Service wanted flashcard api.")),
                    Beat(18, pulse = "svc" to "pods", card = BoardCard("Endpoints", "The Service's list of matching ready pods. It was empty.")),
                ),
            ),
            // 19: "Go on."
            Scene(
                19, "The second case", rollout(),
                beats = listOf(
                    Beat(20, show = setOf("file", "v3"), light = setOf("v3"), pulse = "file" to "v3", card = BoardCard("Image and tag", "The ready-to-start copy of the app; the tag is its version, v3.")),
                    Beat(22, show = setOf("v2"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "New pods start, old ones stop, a few at a time.")),
                    Beat(23, light = setOf("v3"), card = BoardCard("Suspect: delete and recreate", "Takes the app offline, and the bad tag is still in the file.")),
                    Beat(24, show = setOf("undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(26, light = setOf("file"), card = BoardCard("The file is the truth", "After an undo, put the old tag back in the file. Never use latest.")),
                ),
            ),
            // 27: "Any more crimes?"
            Scene(
                27, "The locked cupboard", access(),
                beats = listOf(
                    Beat(28, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(30, show = setOf("kubelet"), light = setOf("kubelet"), card = attachCode),
                    Beat(32, pulse = "kubelet" to "acr", card = BoardCard("AcrPull", "Download permission for the kubelet identity, the nodes' badge.")),
                    Beat(34, show = setOf("pod"), light = setOf("pod"), card = BoardCard("A Secret in Git", "Only base64, a scramble anyone can undo. Key Vault, or no password.")),
                    Beat(36, show = setOf("wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A signed pass from the cluster, trusted by Entra ID. No password.")),
                ),
            ),
            // 37: "Last question. Was AKS even the right home for Flashcard Fox?"
            Scene(
                37, "The right home?", choose(),
                beats = listOf(
                    Beat(38, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Maybe Container Apps", "One simple backend: no cluster to manage. AKS for Kubernetes itself.")),
                ),
            ),
            // 39: "So, the case summary for the exam?"
            Scene(
                39, "Case closed", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("addr", "bad", "pull"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Match labels, fix files.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-aks--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "The Coach team's week", intro(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Moving week", "The Coach's Python backend moved onto Azure Kubernetes Service.")),
                    Beat(2, show = setOf("image", "acr", "k8s"), light = setOf("k8s"), card = BoardCard("Kubernetes", "Runs many containers on many machines, and keeps them running.")),
                ),
            ),
            // 4: "The brain. A Kubernetes setup is a cluster..."
            Scene(
                4, "Head office", cluster(),
                beats = listOf(
                    Beat(4, show = setOf("cp", "nodes"), light = setOf("cp"), pulse = "cp" to "nodes", card = BoardCard("Control plane and nodes", "Azure runs the brain; containers run on rented virtual machines.")),
                    Beat(6, show = setOf("dev"), light = setOf("dev"), pulse = "dev" to "cp", card = BoardCard("Manifest", "An order form in YAML, sent with kubectl. The cluster keeps it true.")),
                ),
            ),
            // 7: "Okay. So who went first?"
            Scene(
                7, "Monday: Tom", objects(),
                beats = listOf(
                    Beat(8, show = setOf("deploy", "pods"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "Replicas 3: three healthy pods. And the app got nothing.")),
                    Beat(10, show = setOf("app", "svc"), light = setOf("svc", "pods"), pulse = "app" to "svc", card = BoardCard("Labels didn't match", "Pods said coach api; the Service looked for coach backend.")),
                    Beat(12, pulse = "svc" to "pods", card = BoardCard("Empty endpoints", "The Service's list of matching ready pods was empty. One label fixed it.")),
                ),
            ),
            // 13: "And Tuesday?"
            Scene(
                13, "Tuesday: Dana", config(),
                beats = listOf(
                    Beat(14, show = setOf("cm", "pods", "sec"), light = setOf("cm"), pulse = "cm" to "pods", card = BoardCard("ConfigMap", "Plain settings, handed to pods as environment variables.")),
                    Beat(16, show = setOf("kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("A Secret in Git", "Only base64, a scramble anyone can undo. Real secrets: Key Vault.")),
                ),
            ),
            // 17: "I sense a Wednesday."
            Scene(
                17, "Wednesday: Marcus", rollout(),
                beats = listOf(
                    Beat(18, show = setOf("file", "v3"), light = setOf("file"), pulse = "file" to "v3", card = BoardCard("The tag called latest", "Not a version, a moving target. Nobody could tell images apart.")),
                    Beat(22, show = setOf("v2"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "New pods start, old ones stop, a few at a time.")),
                ),
            ),
            // 23: "Thursday. Go."
            Scene(
                23, "Thursday: the intern", rollout(),
                beats = listOf(
                    Beat(24, show = setOf("v2", "v3", "undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(26, show = setOf("file"), light = setOf("file", "v3"), pulse = "file" to "v3", card = BoardCard("Friday, v4 was back", "The file still said v4. After an undo, fix the file too.")),
                ),
            ),
            // 27: "And Friday? There's always a Friday."
            Scene(
                27, "Friday: Priya", access(),
                beats = listOf(
                    Beat(28, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(30, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(32, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Two badges", "The kubelet identity for the nodes; workload identity for the code.")),
                ),
            ),
            // 33: "So did anything go right?"
            Scene(
                33, "The best gossip", choose(),
                beats = listOf(
                    Beat(34, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Did it need AKS?", "One simple backend fits Container Apps. AKS for Kubernetes itself.")),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("addr", "file", "pull"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be the story.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-aks--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Something that stays up", intro(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Tonight", "Keeping things running while you sleep.")),
                    Beat(2, show = setOf("k8s"), light = setOf("app", "k8s"), card = BoardCard("Kubernetes", "A night manager for containers: calls in cover when one leaves.")),
                ),
            ),
            // 3: "A night manager. I like them already..."
            Scene(
                3, "The brain", cluster(),
                beats = listOf(
                    Beat(4, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain of the cluster. In AKS, Azure runs it for you.")),
                    Beat(6, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Nodes", "Virtual machines rented by the hour, where the containers run.")),
                    Beat(8, show = setOf("dev"), light = setOf("dev"), pulse = "dev" to "cp", card = BoardCard("Manifest", "A wish list in YAML, sent with kubectl apply.")),
                    Beat(10, light = setOf("cp", "nodes"), card = BoardCard("A loop all night", "Compare the file with what runs; fix any difference.")),
                ),
            ),
            // 11: "Now that's devotion. What goes on the wish list?"
            Scene(
                11, "The rota", objects(),
                beats = listOf(
                    Beat(12, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "Containers sharing one address: one copy of the backend.")),
                    Beat(14, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "Keeps a set number of identical pods. Replicas 3.")),
                    Beat(16, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "One stable address that rings whoever is on shift.")),
                    Beat(18, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Labels", "Name tags. The selector rings every pod wearing the tag.")),
                    Beat(20, light = setOf("svc"), card = BoardCard("ClusterIP or LoadBalancer", "Inside the cluster only, or an address the outside world can reach.")),
                ),
            ),
            // 21: "Settings? Secrets? Where do I keep those?"
            Scene(
                21, "Settings and secrets", config(),
                beats = listOf(
                    Beat(22, show = setOf("cm", "pods", "sec", "kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("Secret", "Only base64. Never commit it to Git; real secrets go in Key Vault.")),
                ),
            ),
            // 23: "Now. What happens when I change my mind?..."
            Scene(
                23, "Changing your mind", rollout(),
                beats = listOf(
                    Beat(24, show = setOf("file", "v2", "v3"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "A new tag in the file; pods change a few at a time. Never dark.")),
                    Beat(26, show = setOf("undo"), light = setOf("undo", "file"), pulse = "v3" to "undo", card = undoCode),
                ),
            ),
            // 27: "Mm. And where does all this stop working?"
            Scene(
                27, "Where it stops", access(),
                beats = listOf(
                    Beat(28, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(30, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(32, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A signed pass from the cluster, swapped by Entra ID. No password.")),
                ),
            ),
            // 33: "And is AKS always the answer?"
            Scene(
                33, "Not always AKS", choose(),
                beats = listOf(
                    Beat(34, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "One simple backend, no cluster to manage. Your next late night.")),
                ),
            ),
            // 35: "Mm. Before you send me off to bed..."
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("copies", "addr", "simple"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-aks--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", intro(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Date night", "Something that never lets you down: Kubernetes.")),
                    Beat(4, show = setOf("image", "acr", "k8s"), light = setOf("k8s"), card = BoardCard("Kubernetes", "Runs many containers on many machines, and keeps them running.")),
                ),
            ),
            // 5: "Half the work. My kind of partner. Which half?"
            Scene(
                5, "The brain", cluster(),
                beats = listOf(
                    Beat(6, show = setOf("cp", "nodes"), light = setOf("cp"), pulse = "cp" to "nodes", card = BoardCard("Control plane and nodes", "Azure runs the brain; containers live on rented virtual machines.")),
                    Beat(8, show = setOf("dev"), light = setOf("dev"), pulse = "dev" to "cp", card = BoardCard("Desired state", "A manifest in YAML, sent with kubectl. The cluster keeps it true.")),
                ),
            ),
            // 11: "Then let me test you, darling. Exam style..."
            Scene(
                11, "Three copies of you", objects(),
                beats = listOf(
                    Beat(11, show = setOf("pods"), card = BoardCard("Quiz: three copies", "Three copies of the Coach's backend, always running. What do you write?")),
                    Beat(12, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Right answer: a Deployment", "Replicas 3. One pod dies, it starts another.")),
                    Beat(14, show = setOf("app", "svc"), light = setOf("app"), card = BoardCard("Quiz: new addresses", "Replaced pods get new IP addresses. How does the app reach them?")),
                    Beat(15, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Right answer: a Service", "A stable address that finds pods by label.")),
                    Beat(18, pulse = "app" to "svc", card = BoardCard("Endpoints", "The matching ready pods. Wrong tags, empty list.")),
                ),
            ),
            // 19: "Inside only, or open to the world..."
            Scene(
                19, "Our secrets", config(),
                beats = listOf(
                    Beat(20, show = setOf("cm", "pods", "sec"), light = setOf("cm"), pulse = "cm" to "pods", card = BoardCard("ConfigMap", "Plain settings, handed to pods as environment variables.")),
                    Beat(22, show = setOf("kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("Never commit a Secret", "Only base64. Real secrets go in Key Vault.")),
                ),
            ),
            // 23: "Deal. Now the dangerous part. A new version..."
            Scene(
                23, "Second chances", rollout(),
                beats = listOf(
                    Beat(24, show = setOf("file", "v2", "v3"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "A new tag; new pods start, old ones stop, a few at a time.")),
                    Beat(25, light = setOf("v3"), card = BoardCard("Quiz: v3 is broken", "Users are angry. What is the fastest way back?")),
                    Beat(26, show = setOf("undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(30, light = setOf("file"), pulse = "file" to "v3", card = BoardCard("No kiss: fix the file", "The file is the truth. Put the old tag back. Never use latest.")),
                ),
            ),
            // 31: "Fine, fine. Next question for you..."
            Scene(
                31, "The right key", access(),
                beats = listOf(
                    Beat(31, show = setOf("acr"), light = setOf("acr"), card = BoardCard("Quiz: ImagePullBackOff", "Pods can't download from ACR, and the tag exists. The fix?")),
                    Beat(32, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(35, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "The nodes' badge isn't for your code. The pod signs in on its own.")),
                ),
            ),
            // 36: "Smooth. And when is AKS the wrong partner altogether?"
            Scene(
                36, "The wrong partner", choose(),
                beats = listOf(
                    Beat(37, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "One simple backend, no cluster to manage. AKS for Kubernetes itself.")),
                ),
            ),
            // 38: "Last question, for the exam. What do we remember?"
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(39, show = all6, light = setOf("copies", "bad", "simple"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
