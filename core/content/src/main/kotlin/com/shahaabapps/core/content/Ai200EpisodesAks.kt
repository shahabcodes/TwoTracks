package com.shahaabapps.core.content

/** Azure Kubernetes Service: deploy with manifest files: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAks {
    private val classic = Podcast(
        "ai-aks",
        listOf(
            maya("Hi, and welcome back. Today is Azure Kubernetes Service. First, the job it does. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. You want three copies of that backend running at all times, and a fresh one started whenever one falls over."),
            leo("Three copies? I can barely keep one running. Where do I even start?"),
            maya("With a container. That's your app packed in a box with everything it needs, so it runs the same anywhere. The ready-to-start copy of that box is called an image. Think of a frozen meal: cooked once, heated anywhere."),
            leo("And where do the frozen meals live?"),
            maya("On a private shelf called a registry. Azure's is called ACR, Azure Container Registry: your team's own shelf of images. Each image carries a tag, a version label such as v2."),
            leo("Okay. So what's Kubernetes, in plain words?"),
            maya("A system that runs lots of containers across lots of machines, and keeps them running. Picture a restaurant manager. You say, I want three cooks on the grill. If one goes home sick, the manager calls in another, without asking you."),
            leo("And AKS, the short name, is that manager, run by Azure?"),
            maya("Half of it. Kubernetes works on a cluster. A cluster is a group of computers working as one, like the whole staff of one restaurant. Every cluster has two halves, and AKS splits them between you and Azure."),
            leo("Which half is Azure's?"),
            maya("The control plane, the brain. Think of head office, which remembers what should be running and gives the orders. Azure runs that half for you. That's what AKS is: Kubernetes with the brain looked after."),
            leo("And the other half?"),
            maya("The nodes. Picture a network as dots joined by lines: a node is one dot. In a cluster, each dot is one computer, a virtual machine, which means a computer you rent in Azure's data centre by the hour. Your containers actually run on the nodes."),
            leo("And a node pool? Is that where the nodes swim?"),
            maya("[chuckle] No swimming. A node pool is a group of identical nodes, same size, same setup, like a row of identical rental cars."),
            leo("So how do I tell head office what I want? Click around a website?"),
            maya("You write it down in a file called a manifest. Think of it as an order form: three copies of this image, please. You fill it in once, and the cluster follows it."),
            leo("What does the form actually look like?"),
            maya("It's written in YAML, which is just plain text: one name and its value per line, like name, colon, coach api. Indents show what belongs to what, like items listed under a heading on a shopping list."),
            leo("And how does the order form reach the cluster?"),
            maya("With kubectl, the command-line tool that talks to the cluster, like a remote control you type into. You type kubectl apply and the file's name. From then on, Kubernetes keeps reality matching the file. That's called desired state. A thermostat works the same way: you set the temperature, it keeps it there."),
            leo("What goes on the order form?"),
            maya("The smallest thing Kubernetes runs is a pod. Picture a pea pod: a little wrapper holding one or more containers, which share one network address. For the Coach, one pod is one running copy of the Python backend."),
            leo("And three copies means three pods. So I write three pods?"),
            maya("No, you write a Deployment. It's like a shift manager who always keeps the same number of cashiers at the tills. You tell it how many replicas you want, and it keeps that many identical pods running. One dies, it starts another."),
            leo("Replicas. Is that a fancy word for copies?"),
            maya("Exactly that. A replica is one identical copy, like one photocopy of the same page. Replicas three means three photocopies of your pod, all running at once."),
            leo("Nice. Then my phone app just calls one of those pods?"),
            maya("Never directly. Pods come and go, and each new pod gets a new IP address, its street address on the network. So you put a Service in front. A Service is a stable name and address that never changes, like a shop's phone number that rings whichever cashier is on shift."),
            leo("How does the Service know which pods are its cashiers?"),
            maya("By labels. A label is a name tag stuck on a pod, such as app equals coach api. Every pod the Deployment makes wears the same tag."),
            leo("And the Service reads the name tags?"),
            maya("With a label selector. That's a rule on the Service that says, send calls to every pod wearing this tag. A classic mistake: if the tag and the rule don't match, even by one letter, the Service has no pods to send to."),
            leo("Name tags. Very office party. Now say I ship version three, and it's broken. Do I panic?"),
            maya("No panic. You change the image tag in the file to v3 and apply it again. The Deployment does a rolling update: it starts new pods and stops old ones a few at a time, so the app never fully goes down. Like swapping cashiers one by one while the shop stays open."),
            leo("And how do I watch it happen?"),
            maya("kubectl rollout status waits and tells you when the update has finished. If v3 is broken, kubectl rollout undo takes you back to the previous version, with another rolling update."),
            leo("Undo. My favourite button. Then I'm done?"),
            maya("One step people forget. Put the old tag back in the file too, because the file is the source of truth, or the next apply brings the broken version straight back. And never use the tag called latest, or no rollout can tell versions apart."),
            leo("Next problem. My pods won't start. They say ImagePullBackOff. What does that even mean?"),
            maya("It's Kubernetes saying, I can't download the image, so I'm waiting before I try again. If the tag exists, the nodes probably aren't allowed to read your private registry. The fix is one command in az, the Azure command-line tool: az aks update, attach acr, and the registry's name."),
            leo("What does attaching actually do?"),
            maya("It gives a role, a set of permissions, called AcrPull: permission to pull, which means download, images from your registry. And it gives that role to the cluster's kubelet identity."),
            leo("Kubelet. Sounds like a baby kettle."),
            maya("[laugh] The kubelet is the small agent on every node, like a site foreman who starts the containers. Its identity is a managed identity: a login Azure gives a resource, like a staff badge, with no password to leak."),
            leo("And when my Python code needs to read the Coach's database? Does it borrow the node's badge?"),
            maya("No, your code gets its own badge, through workload identity. Each pod runs as a service account, which is its Kubernetes identity, like a name on the staff list. Workload identity links that name to a managed identity in Azure."),
            leo("And then my code does some sign-in dance?"),
            maya("No dance. DefaultAzureCredential, a Python helper that finds whatever sign-in is around, picks up the badge by itself. No password, and no code change."),
            leo("So should every app go on AKS?"),
            maya("No. Picture a small app with one simple backend. Azure Container Apps runs containers for you, even shrinking to zero copies when nobody calls, with no cluster to manage. Pick AKS when you need Kubernetes itself, or full control, like your own node pools with graphics chips."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If the question says keep three copies running, the answer is a Deployment with replicas three. If pods keep getting new addresses, a Service that finds them by label. If a release is bad, kubectl rollout undo. If ImagePullBackOff from ACR and the tag exists, attach the registry for AcrPull. If you don't need Kubernetes itself, Container Apps."),
            leo("And anything to try today?"),
            maya("Create a two-node AKS cluster, and get its credentials. Write a Deployment and a Service for a public sample image, and apply them. Change the image tag, apply again, and watch the rollout status. Run rollout undo, and check the old pods return. Then delete the resource group, the folder holding it all."),
            leo("Three cooks, one manager, and an undo button. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-aks",
        listOf(
            maya("Welcome to Case Files. Today's case: The Phone That Rang and Rang. A small Android app called Flashcard Fox helps students drill their flashcards. Its Python backend has just moved onto Azure Kubernetes Service, AKS. Moving day went fine. Then the app asked the backend for cards, and nobody answered."),
            leo("[chuckle] Detective Leo, on the case. Before I suspect anyone, what is AKS, in plain words?"),
            maya("Start with a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes runs lots of those boxes across lots of machines, and keeps them running, like a restaurant manager who keeps enough cooks on shift."),
            leo("And where does Azure come in?"),
            maya("Kubernetes works on a cluster. A cluster is a group of computers working as one, like the whole staff of one restaurant. AKS is Kubernetes with Azure running half of that cluster for you."),
            leo("Which half?"),
            maya("The brain, called the control plane. That's head office, which remembers what should be running. Azure runs head office for you."),
            leo("And the rest of the building?"),
            maya("The nodes. Picture a network as dots joined by lines: a node is one dot. In a cluster, each dot is one computer, a virtual machine you rent from Azure by the hour. Your containers run on the nodes. Flashcard Fox has two."),
            leo("So how did the team tell head office what to run?"),
            maya("In a manifest. That's an order form: run three copies of this, please. It's written in YAML, which is plain text with one name and value per line, and indents to show what belongs to what."),
            leo("And who delivers the form?"),
            maya("kubectl, the command-line tool for the cluster, like a remote control you type into. Under the picture, the control plane keeps comparing the form with what's really running, and fixes any difference. That's called desired state, like a thermostat holding a temperature."),
            leo("Fine. What was on the order form?"),
            maya("A Deployment. It's like a shift manager who keeps the same number of cashiers at the tills. Each cashier is a pod, the smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers."),
            leo("And how many cashiers?"),
            maya("The form said replicas three. A replica is one identical copy, like a photocopy, so three copies of the backend. All three pods were up and healthy."),
            leo("Healthy pods, silent app. First suspect: the pods aren't ready yet. Add a readiness probe."),
            maya("[pause] Good instinct, wrong suspect. A readiness probe is a check, like a nurse taking a pulse, that says when a pod is ready for callers. These pods were ready. The clue is in how the app reaches them. Each new pod gets a new IP address, its street address on the network."),
            leo("So you can't phone a pod directly."),
            maya("Exactly. You put a Service in front: a stable name and address that never changes, like a shop's phone number that rings whichever cashier is on."),
            leo("And how does the phone know who's on shift?"),
            maya("Labels. A label is a name tag on each pod. The Service carries a label selector, a rule that says, send calls to every pod wearing this tag."),
            leo("Second suspect, then. The Service type is wrong. Make it a LoadBalancer."),
            maya("It already was one. LoadBalancer asks Azure for an address outside callers can reach. But the type decides how calls arrive, not which pods get them. Look at the labels. The pods wore app equals flashcards. The Service was looking for app equals flashcard api."),
            leo("[laugh] The phone rang in an empty shop."),
            maya("Under the picture, a Service keeps a list of the ready pods that match its selector, called its endpoints: the cashiers actually on shift right now. That list was empty. They fixed one label, applied the file again, and the cards came back. But a second case opened the same week."),
            leo("Go on."),
            maya("Version three of the backend served the wrong cards. They had changed the image tag in the manifest from v2 to v3. An image is the ready-to-start copy of the app, like a frozen meal, and the tag is its version label."),
            leo("Kubernetes swapped all the pods at once, then? Big outage?"),
            maya("No. A rolling update: new pods start and old ones stop a few at a time, so the app never fully goes down. kubectl rollout status watches it finish. And kubectl rollout undo returns the Deployment to its previous version."),
            leo("Suspect: they should delete the Deployment and create it again. Clean slate."),
            maya("That takes the whole app offline, and the bad tag is still in the file. Rollout undo is the fastest way back, and the junior developer ran it. Version two returned. Two days later, someone applied the file for a small change, and version three was back."),
            leo("Because the file still said v3."),
            maya("Exactly. The file is the source of truth. After an undo, put the old tag back in the file too. And never use the tag called latest, or no rollout can tell the versions apart."),
            leo("Any more crimes?"),
            maya("Two. The team added a node pool, a second group of identical nodes, and its pods showed ImagePullBackOff. That's Kubernetes saying, I can't download the image, so I'm waiting before I try again. And the tag existed."),
            leo("Then the image is somewhere private, and the nodes have no key."),
            maya("Sharp. The image lives on a private shelf called ACR, Azure Container Registry, where the team keeps its images. The fix is one command in az, the Azure command-line tool: az aks update, attach acr, and the registry's name."),
            leo("What does attaching actually grant?"),
            maya("A role called AcrPull, permission to download images. It goes to the kubelet identity. The kubelet is the agent on each node that starts the containers, and its identity is a managed identity: a login Azure gives a resource, like a staff badge with no password."),
            leo("And the last crime?"),
            maya("Someone committed a Secret file to Git, where the code's history lives, to hand the backend its database password. A Kubernetes Secret is only base64 encoded, a scramble anyone can undo. Keep real secrets in Key Vault, Azure's safe, or better, need no password at all."),
            leo("No password at all? How?"),
            maya("Workload identity. A pod runs as a service account, its Kubernetes identity, like a name on the staff list. Workload identity links that name to a managed identity in Azure."),
            leo("And how does that turn into a sign-in?"),
            maya("The cluster hands the pod a token, a short-lived signed pass, like a festival wristband. Entra ID, which is Microsoft's sign-in service, trusts that wristband through a trust link set up ahead of time. In Python, DefaultAzureCredential, a helper that finds whatever sign-in is around, picks it up."),
            leo("Last question. Was AKS even the right home for Flashcard Fox?"),
            maya("Honestly, maybe not. For one simple backend, Azure Container Apps runs containers with no cluster to manage, and can shrink to zero copies when nobody calls. AKS earns its place when you need Kubernetes itself, or full control. The Container Apps episode compares the two."),
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
            maya("First, a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running, like a restaurant manager keeping enough cooks on shift."),
            leo("And the Azure part? Everyone just says AKS."),
            maya("AKS is the short name. Kubernetes works on a cluster, which is a group of computers working as one, like the whole staff of a restaurant. AKS is Kubernetes with Azure running half of that cluster."),
            leo("Which half?"),
            maya("The brain, called the control plane. That's head office, which remembers what should be running. Azure runs that half for you."),
            leo("And the half the team runs?"),
            maya("The nodes. Picture a network as dots joined by lines: a node is one dot. In a cluster, each dot is one computer, a virtual machine rented by the hour. That's where the containers actually run."),
            leo("And how do you talk to head office?"),
            maya("With an order form called a manifest: run three copies of this, please. It's written in YAML, which is plain text, one name and value per line, with indents to show what belongs to what."),
            leo("And who carries the order form over?"),
            maya("kubectl, the command-line tool for the cluster, like a remote control you type into. Under the hood, the control plane keeps comparing the file with what's really running, and fixes any difference. That's called desired state, like a thermostat."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom wrote the Deployment, which is like a shift manager keeping the same number of cashiers at the tills. Each cashier is a pod, the smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers."),
            leo("How many cashiers did Tom ask for?"),
            maya("Replicas three. A replica is one identical copy, like a photocopy, so three copies of the backend, all healthy. And the Android app got nothing."),
            leo("Nothing? Three healthy copies and nothing?"),
            maya("Pods get a new IP address, a new street address on the network, every time they're replaced. So the app calls a Service, a stable address that never changes, like a shop's phone number. The Service finds its pods by labels, name tags on each pod. Tom's pods said coach api. His Service was looking for coach backend."),
            leo("[sigh] Poor Tom. The phone rang in an empty shop."),
            maya("Exactly. Under the picture, a Service keeps a list of the matching ready pods, called its endpoints: the cashiers actually on shift. Tom's list was empty. One label fixed, and it worked. And his Service was a LoadBalancer, which asks Azure for an address outsiders can reach, like the phone app."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. The backend needed settings. Plain settings go in a ConfigMap, handed to the pods as environment variables, values a program reads at start. But the database password went in a Secret, and Dana committed that Secret file to Git, the code's history."),
            leo("Isn't a Secret, well, secret?"),
            maya("That's what Dana thought. A Kubernetes Secret is only base64 encoded, a scramble anyone can undo. Not a lock. Security had words with her before lunch. Real secrets go in Key Vault, Azure's safe. The Key Vault episode covers it."),
            leo("I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. He shipped version three by changing the image in the manifest. An image is the ready-to-start copy of the app, like a frozen meal. But instead of a version tag, a label like v3, Marcus used the tag called latest."),
            leo("What's wrong with latest? It's, well, the latest."),
            maya("Latest isn't a version, it's a moving target. So when v3 turned out broken, nobody could say which image was which, and rolling back was guesswork. Proper tags let Kubernetes tell versions apart."),
            leo("How does rolling back even work?"),
            maya("A new tag in the file causes a rolling update. New pods start and old ones stop a few at a time, so the app never fully goes down. kubectl rollout status watches it. And kubectl rollout undo goes back to the previous version."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. Version four was broken, and the intern did exactly the right thing. Rollout undo, back to the old version, errors gone. Applause in the channel."),
            leo("That sounds like a good day."),
            maya("Until Friday morning, when someone applied the file for a tiny change, and version four came straight back. The file still said v4. The file is the source of truth, so after an undo, put the old tag back in the file too."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. A new node pool, a second group of identical nodes, came up, and every pod on it said ImagePullBackOff. That's Kubernetes for, I can't download the image, so I'm waiting before I try again. And the tag existed."),
            leo("So the nodes couldn't open the cupboard."),
            maya("The cupboard is called ACR, Azure Container Registry, the team's private shelf of images. Priya ran one az command, the Azure command-line tool: az aks update, attach acr. That gives the AcrPull role, permission to download images, to the kubelet identity, the login the nodes use."),
            leo("Kubelet?"),
            maya("The agent on each node that starts the containers, like a site foreman. Its identity is a managed identity, a login Azure gives a resource, like a staff badge with no password."),
            leo("And the backend's own code? Does it borrow the foreman's badge?"),
            maya("No, it gets its own, through workload identity. A pod runs as a service account, its name on the Kubernetes staff list, and workload identity links that to a managed identity. Then DefaultAzureCredential, the Python helper that finds a sign-in, just works."),
            leo("So did anything go right?"),
            maya("The best gossip of all. Priya asked whether the Coach needed AKS at all. For one simple backend, Azure Container Apps runs containers with no cluster to manage, and can shrink to zero copies when nobody calls. AKS is for when you need Kubernetes itself, or full control. The Container Apps episode compares the two."),
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
            nova("Azure Kubernetes Service, AKS for short. Kubernetes works on a cluster, which is a group of computers working as one, [pause] like the whole night staff of one hotel."),
            ivy("And who's in charge of the hotel?"),
            nova("The control plane, the brain of the cluster. It's the part that remembers what should be running. And in AKS, Azure runs the brain for you."),
            ivy("And what do I run?"),
            nova("The nodes. Picture a network as dots joined by lines: a node is one dot. [pause] In a cluster, each dot is one computer, a virtual machine you rent from Azure by the hour. Your containers actually run there."),
            ivy("And a node pool? It sounds like a swim at midnight."),
            nova("Nothing so wet. A node pool is a group of identical nodes, same size, same setup, like a row of identical rooms on one floor of the hotel."),
            ivy("So how do I whisper my wishes to this brain?"),
            nova("You write them down. A manifest is a wish list in a file: three copies of this, please. It's written in YAML, which is plain text, one name and value per line, with indents to show what belongs to what."),
            ivy("And how does my wish list reach the brain?"),
            nova("With kubectl, the command-line tool for the cluster, like a remote control you type into. You send it with kubectl apply. [pause] And the cluster keeps your wish true. That's called desired state, like a thermostat holding the room warm."),
            ivy("Tell me what's really happening there."),
            nova("Under the picture, the control plane runs a loop, all night long. It compares what your file asks for with what's actually running, and fixes any difference. A container dies at three in the morning, [pause] and a new one is started before you've rolled over."),
            ivy("Now that's devotion. What goes on the wish list?"),
            nova("Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. The smallest thing Kubernetes runs is a pod. Picture a pea pod: a little wrapper round one or more containers. One pod is one running copy of the Coach's backend."),
            ivy("And I want more than one copy, don't I."),
            nova("You do. So you write a Deployment, which keeps a set number of identical pods running. You set replicas to three. A replica is one identical copy, like a photocopy, so three copies. It's the night manager's rota."),
            ivy("And how does the app find them, in the dark?"),
            nova("Not by name. Pods come and go, and each new one gets a new IP address, its street address on the network. So you put a Service in front: a stable address that never changes, [pause] like one phone number that rings whoever is on shift."),
            ivy("And how does the Service know who's on shift?"),
            nova("Labels. A label is a name tag on each pod, such as app equals coach api. The Service has a label selector, a rule that says, ring everyone wearing this tag. If the tags don't match, the phone rings in an empty room."),
            ivy("And does the Service keep a list of who's in?"),
            nova("It does. That list is called its endpoints: the ready pods that match the tag, the staff actually on shift right now. [pause] Empty list, empty room."),
            ivy("Lonely phones. Again. And can strangers call?"),
            nova("Only if you let them. ClusterIP, the default, is for callers inside the cluster. LoadBalancer asks Azure for an address the outside world can reach, like the Coach's Android app."),
            ivy("Settings? Secrets? Where do I keep those?"),
            nova("Plain settings go in a ConfigMap, handed to the pods as values a program reads when it wakes up. Sensitive ones go in a Secret. [pause] But a Kubernetes Secret is only base64 encoded, a scramble anyone can undo. Never commit one to Git, your code's history. Real secrets belong in Key Vault, and the Key Vault episode is a good night's listening."),
            ivy("Now. What happens when I change my mind? A new version?"),
            nova("You put a new image tag in the file. An image is the ready-to-start copy of the app, and the tag is its version label, like v3. Apply it, and you get a rolling update: new pods start, old ones stop, a few at a time. The app never fully goes dark."),
            ivy("And if version three is a mistake? It happens. Late at night."),
            nova("It does. kubectl rollout status watches the update. If it's bad, kubectl rollout undo takes you back to the previous version. [pause] And then, the part everyone forgets: put the old tag back in the file too. The file is the source of truth, and the next apply believes it."),
            ivy("Mm. And where does all this stop working?"),
            nova("A few places. Kubernetes can't pull an image it isn't allowed to read. The pods sit there saying ImagePullBackOff, which means, I can't download the image, so I'm waiting before I try again."),
            ivy("And the cure?"),
            nova("If the image lives on your team's private shelf, called ACR, Azure Container Registry, it's one command in az, the Azure command-line tool: az aks update, attach acr. It gives a role called AcrPull, permission to download, to the kubelet identity."),
            ivy("Kubelet. Say it softly and it sounds like a pet."),
            nova("[chuckle] The kubelet is the agent on each node that starts the containers, like a night porter. Its identity is a managed identity: a login Azure gives a resource, like a staff badge with no password."),
            ivy("And my own code? Does it get a badge?"),
            nova("Through workload identity. A pod runs as a service account, its name on the Kubernetes staff list, linked to a managed identity. The cluster hands the pod a token, a short-lived signed pass, like a wristband at a festival. Entra ID, Microsoft's sign-in service, swaps it for an Azure one. No password at all."),
            ivy("And is AKS always the answer?"),
            nova("No. [pause] For one simple backend, Azure Container Apps runs containers with no cluster to manage, and can shrink to zero copies when nobody calls. AKS is for when you need Kubernetes itself, or full control. The Container Apps episode is your next late night."),
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
            nova("Start with a container: your app packed in a box with everything it needs, so it runs the same anywhere. Kubernetes runs lots of those boxes across lots of machines, and keeps them running, like a restaurant manager who always keeps enough cooks on shift."),
            rafe("And the Azure flavour? The one everyone calls AKS?"),
            nova("Azure Kubernetes Service. Kubernetes works on a cluster, which is a group of computers working as one, like a whole kitchen staff. AKS is Kubernetes with Azure doing half the work on that cluster."),
            rafe("Half the work. My kind of partner. Which half?"),
            nova("The brain, called the control plane, which remembers what should be running. Azure runs that half."),
            rafe("And the half I get to hold?"),
            nova("The nodes. Picture a network as dots joined by lines: a node is one dot. In a cluster, each dot is one computer, a virtual machine rented by the hour, where the containers live."),
            rafe("And how do I tell it what I want? I'm told communication matters."),
            nova("You write it down. A manifest is an order form in a file: three copies of this, please. It's written in YAML, which is plain text, one name and value per line, with indents."),
            rafe("And who carries my order form over?"),
            nova("kubectl, the command-line tool for the cluster, like a remote control you type into. And the cluster keeps your wishes true. That's called desired state, like a thermostat holding the room just how you like it."),
            rafe("Desired state. I know all about that."),
            nova("Under the hood, the control plane keeps comparing your file with what's really running, and fixes any difference. Take the Two Tracks Coach, the study assistant in the Android app. The smallest thing Kubernetes runs is a pod. Picture a pea pod: a little wrapper round one or more containers."),
            rafe("Then let me test you, darling. Exam style. You want three copies of the Coach's backend always running. What do you write?"),
            nova("A Deployment, which keeps a set number of identical pods running, with replicas set to three. A replica is one identical copy, like a photocopy. One dies, it starts another."),
            rafe("Right answer. I like a partner who keeps copies of me around. [kiss]"),
            nova("Mm. My turn. Pods are replaced, and each new one gets a new IP address, its street address on the network. So how does the app still reach them?"),
            rafe("Through a Service, a stable address that never changes, like one phone number that rings whoever's on shift. It finds the pods by labels, name tags on each pod, through its label selector, a rule that says, everyone wearing this tag."),
            nova("Right answer. Wearing the right name tag gets you a call. [kiss]"),
            rafe("And if the tags don't match?"),
            nova("Then the phone rings in an empty room. The Service keeps a list of the matching ready pods, called its endpoints, the staff actually on shift, and that list is empty. A Service can be ClusterIP, the default, inside the cluster only, or LoadBalancer, an address outsiders can reach."),
            rafe("Inside only, or open to the world. Like some people I know. Now, secrets. Where do we keep ours?"),
            nova("Plain settings go in a ConfigMap, handed to pods as values a program reads at start. Sensitive ones go in a Secret. But a Kubernetes Secret is only base64 encoded, a scramble anyone can undo."),
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
            rafe("Fine, fine. Next question for you. The team's private image shelf is called ACR, Azure Container Registry. Pods say ImagePullBackOff from it, and the tag exists. What's the fix?"),
            nova("ImagePullBackOff means, I can't download the image, so I'm waiting to try again. So attach the registry, with az, the Azure command-line tool: az aks update, attach acr. That gives the AcrPull role, permission to download, to the kubelet identity, the login of the agent on each node that starts the containers."),
            rafe("Right answer. Some cupboards need the right key. [kiss]"),
            nova("And where does it stop working, Rafe? Every romance has its limits."),
            rafe("The kubelet's badge is for the nodes, not your code. For the code there's workload identity: a pod's service account, its name on the Kubernetes staff list, is linked to a managed identity, which is a login Azure gives, like a staff badge with no password."),
            nova("And how does the pod prove who it is?"),
            rafe("The cluster gives it a token, a short-lived signed pass, like a wristband at a festival. Entra ID, Microsoft's sign-in service, trusts the wristband, and DefaultAzureCredential, the Python sign-in helper, picks it up."),
            nova("Smooth. And when is AKS the wrong partner altogether?"),
            rafe("When you only have one simple backend. Azure Container Apps runs containers with no cluster to manage, and can shrink to zero copies when nobody calls. AKS is for when you need Kubernetes itself, or full control. The Container Apps episode compares them."),
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
            // 8: "Half of it. Kubernetes works on a cluster..."
            Scene(
                8, "Inside a cluster", cluster(),
                beats = listOf(
                    Beat(8, card = BoardCard("Cluster", "A group of computers working as one, like one restaurant's whole staff.")),
                    Beat(10, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain, head office: remembers what should run. Azure runs it.")),
                    Beat(12, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Node", "One dot in a network. In a cluster: one computer, a rented virtual machine.")),
                    Beat(14, card = BoardCard("Node pool", "A group of identical nodes, like a row of identical rental cars.")),
                    Beat(16, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "An order form in a file: three copies of this image, please.")),
                    Beat(18, card = BoardCard("YAML", "replicas: 3", code = true)),
                    Beat(20, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = applyCode),
                ),
            ),
            // 21: "What goes on the order form?"
            Scene(
                21, "Pods, Deployment, Service", objects(),
                beats = listOf(
                    Beat(22, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "Like a pea pod: a wrapper round one or more containers, one address.")),
                    Beat(24, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment", "A shift manager: keeps a set number of identical pods running.")),
                    Beat(26, card = BoardCard("Replica", "One identical copy, like a photocopy. Replicas 3: three pods.")),
                    Beat(28, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "A stable name and address in front of pods that come and go.")),
                    Beat(30, light = setOf("pods"), card = BoardCard("Label", "A name tag on a pod, such as app: coach-api.")),
                    Beat(32, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Label selector", "The Service's rule: call every pod wearing this tag. No match, no pods.")),
                ),
            ),
            // 33: "Name tags. Very office party. Now say I ship version three..."
            Scene(
                33, "Rolling out v3", rollout(),
                beats = listOf(
                    Beat(34, show = setOf("file", "v2", "v3"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "New pods start, old ones stop, a few at a time. Never fully down.")),
                    Beat(36, show = setOf("undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(38, light = setOf("file"), pulse = "file" to "v3", card = BoardCard("The file is the truth", "Put the old tag back in the file. And never use latest.")),
                ),
            ),
            // 39: "Next problem. My pods won't start. They say ImagePullBackOff."
            Scene(
                39, "Pulling from ACR", access(),
                beats = listOf(
                    Beat(39, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(40, show = setOf("kubelet"), light = setOf("kubelet", "acr"), card = attachCode),
                    Beat(42, pulse = "kubelet" to "acr", card = BoardCard("AcrPull", "Permission to download images, given to the kubelet identity.")),
                    Beat(44, light = setOf("kubelet"), card = BoardCard("Kubelet identity", "The agent on each node; its managed identity is a badge, no password.")),
                    Beat(46, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "The pod's service account, its name on the staff list, gets a badge.")),
                    Beat(48, card = BoardCard("DefaultAzureCredential", "Finds whatever sign-in is around. No password, no code change.")),
                ),
            ),
            // 49: "So should every app go on AKS?"
            Scene(
                49, "AKS or Container Apps?", choose(),
                beats = listOf(
                    Beat(50, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "No cluster or nodes to manage; can shrink to zero copies.")),
                ),
            ),
            // 51: "So what do I remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("copies", "bad", "pull"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: Deployment, Service, undo, AcrPull.")),
                    Beat(54, card = today),
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
                    Beat(4, card = BoardCard("Cluster", "A group of computers working as one, like one restaurant's whole staff.")),
                ),
            ),
            // 5: "Which half?"
            Scene(
                5, "Head office", cluster(),
                beats = listOf(
                    Beat(6, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain: remembers what should be running. Azure runs it.")),
                    Beat(8, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Node", "One dot in a network. In a cluster: one computer, a rented virtual machine.")),
                    Beat(10, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "An order form in YAML: one name and value a line, with indents.")),
                    Beat(12, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("Desired state", "Like a thermostat: the cluster keeps reality matching the form.")),
                ),
            ),
            // 13: "Fine. What was on the order form?"
            Scene(
                13, "The suspects", objects(),
                beats = listOf(
                    Beat(14, show = setOf("deploy", "pods"), light = setOf("deploy", "pods"), pulse = "deploy" to "pods", card = BoardCard("Deployment and pods", "A shift manager keeping cashiers; each cashier is a pod.")),
                    Beat(16, card = BoardCard("Replica", "One identical copy, like a photocopy. Replicas 3, all healthy.")),
                    Beat(17, light = setOf("pods"), card = BoardCard("Suspect: a readiness probe", "Cleared. The pods were ready; the clue is how the app reaches them.")),
                    Beat(20, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "A stable address, like a shop's phone number, in front of the pods.")),
                    Beat(22, light = setOf("pods"), card = BoardCard("Label and selector", "A name tag on each pod; the Service's rule: call everyone wearing it.")),
                    Beat(23, light = setOf("svc"), card = BoardCard("Suspect: the Service type", "Cleared. LoadBalancer was right; the type doesn't pick the pods.")),
                    Beat(24, light = setOf("svc", "pods"), card = BoardCard("The labels", "Pods wore flashcards; the Service wanted flashcard api.")),
                    Beat(26, pulse = "svc" to "pods", card = BoardCard("Endpoints", "The Service's list of matching ready pods. It was empty.")),
                ),
            ),
            // 27: "Go on."
            Scene(
                27, "The second case", rollout(),
                beats = listOf(
                    Beat(28, show = setOf("file", "v3"), light = setOf("v3"), pulse = "file" to "v3", card = BoardCard("Image and tag", "The ready-to-start copy of the app; the tag is its version, v3.")),
                    Beat(30, show = setOf("v2"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "New pods start, old ones stop, a few at a time.")),
                    Beat(31, light = setOf("v3"), card = BoardCard("Suspect: delete and recreate", "Takes the app offline, and the bad tag is still in the file.")),
                    Beat(32, show = setOf("undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(34, light = setOf("file"), card = BoardCard("The file is the truth", "After an undo, put the old tag back in the file. Never use latest.")),
                ),
            ),
            // 35: "Any more crimes?"
            Scene(
                35, "The locked cupboard", access(),
                beats = listOf(
                    Beat(36, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(38, show = setOf("kubelet"), light = setOf("kubelet"), card = attachCode),
                    Beat(40, pulse = "kubelet" to "acr", card = BoardCard("AcrPull", "Download permission for the kubelet identity, the nodes' badge.")),
                    Beat(42, show = setOf("pod"), light = setOf("pod"), card = BoardCard("A Secret in Git", "Only base64, a scramble anyone can undo. Key Vault, or no password.")),
                    Beat(44, show = setOf("wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "The pod's service account, linked to a managed identity in Azure.")),
                    Beat(46, card = BoardCard("Token", "A short-lived signed pass, like a festival wristband. Entra ID trusts it.")),
                ),
            ),
            // 47: "Last question. Was AKS even the right home for Flashcard Fox?"
            Scene(
                47, "The right home?", choose(),
                beats = listOf(
                    Beat(48, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Maybe Container Apps", "One simple backend: no cluster to manage. AKS for Kubernetes itself.")),
                ),
            ),
            // 49: "So, the case summary for the exam?"
            Scene(
                49, "Case closed", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("addr", "bad", "pull"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Match labels, fix files.")),
                    Beat(52, card = today),
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
                    Beat(4, card = BoardCard("Cluster", "A group of computers working as one, like a restaurant's whole staff.")),
                ),
            ),
            // 6: "The brain, called the control plane..."
            Scene(
                6, "Head office", cluster(),
                beats = listOf(
                    Beat(6, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain, head office: remembers what should run. Azure runs it.")),
                    Beat(8, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Node", "One dot in a network. In a cluster: one computer, a rented virtual machine.")),
                    Beat(10, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "An order form in YAML: one name and value a line, with indents.")),
                    Beat(12, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("Desired state", "Sent with kubectl; like a thermostat, the cluster keeps it true.")),
                ),
            ),
            // 13: "Okay. So who went first?"
            Scene(
                13, "Monday: Tom", objects(),
                beats = listOf(
                    Beat(14, show = setOf("deploy", "pods"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment and pods", "A shift manager keeping cashiers; each cashier is a pod.")),
                    Beat(16, light = setOf("pods"), card = BoardCard("Replica", "One identical copy, like a photocopy. Three healthy, and the app got nothing.")),
                    Beat(18, show = setOf("app", "svc"), light = setOf("svc", "pods"), pulse = "app" to "svc", card = BoardCard("Labels didn't match", "Pods said coach api; the Service looked for coach backend.")),
                    Beat(20, pulse = "svc" to "pods", card = BoardCard("Empty endpoints", "The Service's list of matching ready pods was empty. One label fixed it.")),
                ),
            ),
            // 21: "And Tuesday?"
            Scene(
                21, "Tuesday: Dana", config(),
                beats = listOf(
                    Beat(22, show = setOf("cm", "pods", "sec"), light = setOf("cm"), pulse = "cm" to "pods", card = BoardCard("ConfigMap", "Plain settings, handed to pods as environment variables.")),
                    Beat(24, show = setOf("kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("A Secret in Git", "Only base64, a scramble anyone can undo. Real secrets: Key Vault.")),
                ),
            ),
            // 25: "I sense a Wednesday."
            Scene(
                25, "Wednesday: Marcus", rollout(),
                beats = listOf(
                    Beat(26, show = setOf("file", "v3"), light = setOf("file"), pulse = "file" to "v3", card = BoardCard("The tag called latest", "Not a version, a moving target. Nobody could tell images apart.")),
                    Beat(30, show = setOf("v2"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "New pods start, old ones stop, a few at a time.")),
                ),
            ),
            // 31: "Thursday. Go."
            Scene(
                31, "Thursday: the intern", rollout(),
                beats = listOf(
                    Beat(32, show = setOf("v2", "v3", "undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(34, show = setOf("file"), light = setOf("file", "v3"), pulse = "file" to "v3", card = BoardCard("Friday, v4 was back", "The file still said v4. After an undo, fix the file too.")),
                ),
            ),
            // 35: "And Friday? There's always a Friday."
            Scene(
                35, "Friday: Priya", access(),
                beats = listOf(
                    Beat(36, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(38, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(40, light = setOf("kubelet"), card = BoardCard("Managed identity", "A login Azure gives a resource, like a staff badge with no password.")),
                    Beat(42, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Two badges", "The kubelet identity for the nodes; workload identity for the code.")),
                ),
            ),
            // 43: "So did anything go right?"
            Scene(
                43, "The best gossip", choose(),
                beats = listOf(
                    Beat(44, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Did it need AKS?", "One simple backend fits Container Apps. AKS for Kubernetes itself.")),
                ),
            ),
            // 45: "So, the gossip I should remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("addr", "file", "pull"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be the story.")),
                    Beat(48, card = today),
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
                    Beat(4, card = BoardCard("Cluster", "A group of computers working as one, like a hotel's whole night staff.")),
                    Beat(6, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain of the cluster. In AKS, Azure runs it for you.")),
                    Beat(8, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Node", "One dot in a network. In a cluster: one computer, a rented virtual machine.")),
                    Beat(10, card = BoardCard("Node pool", "A group of identical nodes, like identical rooms on one floor.")),
                    Beat(12, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "A wish list in YAML: one name and value a line, with indents.")),
                    Beat(14, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("Desired state", "Sent with kubectl apply; like a thermostat, the cluster keeps it true.")),
                    Beat(16, light = setOf("cp", "nodes"), card = BoardCard("A loop all night", "Compare the file with what runs; fix any difference.")),
                ),
            ),
            // 17: "Now that's devotion. What goes on the wish list?"
            Scene(
                17, "The rota", objects(),
                beats = listOf(
                    Beat(18, show = setOf("pods"), light = setOf("pods"), card = BoardCard("Pod", "Like a pea pod: a wrapper round containers. One copy of the backend.")),
                    Beat(20, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Deployment and replicas", "Keeps identical pods running. A replica is one copy, like a photocopy.")),
                    Beat(22, show = setOf("app", "svc"), light = setOf("svc"), pulse = "app" to "svc", card = BoardCard("Service", "One stable address that rings whoever is on shift.")),
                    Beat(24, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Labels", "Name tags. The selector rings every pod wearing the tag.")),
                    Beat(26, light = setOf("pods"), card = BoardCard("Endpoints", "The Service's list of ready, matching pods: who's on shift right now.")),
                    Beat(28, light = setOf("svc"), card = BoardCard("ClusterIP or LoadBalancer", "Inside the cluster only, or an address the outside world can reach.")),
                ),
            ),
            // 29: "Settings? Secrets? Where do I keep those?"
            Scene(
                29, "Settings and secrets", config(),
                beats = listOf(
                    Beat(30, show = setOf("cm", "pods", "sec", "kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("Secret", "Only base64. Never commit it to Git; real secrets go in Key Vault.")),
                ),
            ),
            // 31: "Now. What happens when I change my mind?..."
            Scene(
                31, "Changing your mind", rollout(),
                beats = listOf(
                    Beat(32, show = setOf("file", "v2", "v3"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "A new tag in the file; pods change a few at a time. Never dark.")),
                    Beat(34, show = setOf("undo"), light = setOf("undo", "file"), pulse = "v3" to "undo", card = undoCode),
                ),
            ),
            // 35: "Mm. And where does all this stop working?"
            Scene(
                35, "Where it stops", access(),
                beats = listOf(
                    Beat(36, show = setOf("acr"), light = setOf("acr"), card = BoardCard("ImagePullBackOff", "Can't download the image; waiting before trying again.")),
                    Beat(38, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(40, light = setOf("kubelet"), card = BoardCard("Kubelet identity", "The night porter on each node; its badge is a managed identity.")),
                    Beat(42, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "A token, a wristband from the cluster, swapped by Entra ID. No password.")),
                ),
            ),
            // 43: "And is AKS always the answer?"
            Scene(
                43, "Not always AKS", choose(),
                beats = listOf(
                    Beat(44, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "One simple backend, no cluster to manage. Your next late night.")),
                ),
            ),
            // 45: "Mm. Before you send me off to bed..."
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("copies", "addr", "simple"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(48, card = today),
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
                    Beat(6, card = BoardCard("Cluster", "A group of computers working as one, like a whole kitchen staff.")),
                ),
            ),
            // 7: "Half the work. My kind of partner. Which half?"
            Scene(
                7, "The brain", cluster(),
                beats = listOf(
                    Beat(8, show = setOf("cp"), light = setOf("cp"), card = BoardCard("Control plane", "The brain: remembers what should be running. Azure runs it.")),
                    Beat(10, show = setOf("nodes"), light = setOf("nodes"), pulse = "cp" to "nodes", card = BoardCard("Node", "One dot in a network. In a cluster: one computer, a rented virtual machine.")),
                    Beat(12, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Manifest", "An order form in YAML: one name and value a line, with indents.")),
                    Beat(14, light = setOf("dev", "cp"), pulse = "dev" to "cp", card = BoardCard("Desired state", "Sent with kubectl; like a thermostat, the cluster keeps it true.")),
                ),
            ),
            // 17: "Then let me test you, darling. Exam style..."
            Scene(
                17, "Three copies of you", objects(),
                beats = listOf(
                    Beat(17, show = setOf("pods"), card = BoardCard("Quiz: three copies", "Three copies of the Coach's backend, always running. What do you write?")),
                    Beat(18, show = setOf("deploy"), light = setOf("deploy"), pulse = "deploy" to "pods", card = BoardCard("Right answer: a Deployment", "Replicas 3. One pod dies, it starts another.")),
                    Beat(20, show = setOf("app", "svc"), light = setOf("app"), card = BoardCard("Quiz: new addresses", "Replaced pods get new IP addresses. How does the app reach them?")),
                    Beat(21, light = setOf("svc", "pods"), pulse = "svc" to "pods", card = BoardCard("Right answer: a Service", "A stable address that finds pods by label.")),
                    Beat(24, pulse = "app" to "svc", card = BoardCard("Endpoints", "The matching ready pods. Wrong tags, empty list.")),
                ),
            ),
            // 25: "Inside only, or open to the world..."
            Scene(
                25, "Our secrets", config(),
                beats = listOf(
                    Beat(26, show = setOf("cm", "pods", "sec"), light = setOf("cm"), pulse = "cm" to "pods", card = BoardCard("ConfigMap", "Plain settings, handed to pods as environment variables.")),
                    Beat(28, show = setOf("kv"), light = setOf("sec", "kv"), pulse = "sec" to "kv", card = BoardCard("Never commit a Secret", "Only base64. Real secrets go in Key Vault.")),
                ),
            ),
            // 29: "Deal. Now the dangerous part. A new version..."
            Scene(
                29, "Second chances", rollout(),
                beats = listOf(
                    Beat(30, show = setOf("file", "v2", "v3"), light = setOf("v2", "v3"), pulse = "v2" to "v3", card = BoardCard("Rolling update", "A new tag; new pods start, old ones stop, a few at a time.")),
                    Beat(31, light = setOf("v3"), card = BoardCard("Quiz: v3 is broken", "Users are angry. What is the fastest way back?")),
                    Beat(32, show = setOf("undo"), light = setOf("undo"), pulse = "v3" to "undo", card = undoCode),
                    Beat(36, light = setOf("file"), pulse = "file" to "v3", card = BoardCard("No kiss: fix the file", "The file is the truth. Put the old tag back. Never use latest.")),
                ),
            ),
            // 37: "Fine, fine. Next question for you..."
            Scene(
                37, "The right key", access(),
                beats = listOf(
                    Beat(37, show = setOf("acr"), light = setOf("acr"), card = BoardCard("Quiz: ImagePullBackOff", "Pods can't download from ACR, and the tag exists. The fix?")),
                    Beat(38, show = setOf("kubelet"), light = setOf("kubelet", "acr"), pulse = "kubelet" to "acr", card = attachCode),
                    Beat(41, show = setOf("pod", "wi"), light = setOf("pod", "wi"), pulse = "pod" to "wi", card = BoardCard("Workload identity", "The nodes' badge isn't for your code. The pod signs in on its own.")),
                    Beat(43, card = BoardCard("Token", "A short-lived signed pass, like a festival wristband. Entra ID trusts it.")),
                ),
            ),
            // 44: "Smooth. And when is AKS the wrong partner altogether?"
            Scene(
                44, "The wrong partner", choose(),
                beats = listOf(
                    Beat(45, light = setOf("simple", "aca"), pulse = "simple" to "aca", card = BoardCard("Container Apps", "One simple backend, no cluster to manage. AKS for Kubernetes itself.")),
                ),
            ),
            // 46: "Last question, for the exam. What do we remember?"
            Scene(
                46, "For the exam", exam(),
                beats = listOf(
                    Beat(47, show = all6, light = setOf("copies", "bad", "simple"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
