package com.shahaabapps.core.content

/** Troubleshooting AKS and Container Apps: logs, events and connectivity: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesContainerTroubleshoot {
    private val classic = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Hi, and welcome back. Today is the Friday release that will not start. The Zaiqa API, which is the dot NET service behind the Android app, has its image in our registry."),
            leo("And we have moved it around a bit. App Service first, then Container Apps."),
            maya("Right. The API runs on Container Apps now, behind ingress, which just means the front door that lets outside calls reach your app."),
            leo("And last time we outgrew that, and the order worker moved to Kubernetes."),
            maya("To AKS, which is Azure Kubernetes Service: a cluster of our own. A cluster is just a group of computers working as one."),
            leo("Two homes, then. So what broke on Friday?"),
            maya("We shipped at five on Friday, and the new version never came up. Three cities, empty restaurant lists. Learn the order you check things in, not the commands."),
            leo("Go on."),
            maya("Three questions, always in this order. Did it start? Why did it stop? Can traffic reach it? Looking in the wrong place costs you an hour."),
            leo("Where does question one get answered?"),
            maya("On the cluster, with kubectl get pods. Kubectl is the Kubernetes command line, and a pod is the little wrapper your container runs inside, like a box with a handle."),
            leo("And it prints what?"),
            maya("One line per pod: its name, its status, a ready count like one of one, and how many restarts. That line tells you which question you are really asking."),
            leo("And on Friday it said?"),
            maya("ImagePullBackOff. Which means the machine tried to download the image, failed, and is waiting before trying again. Back off, as in, I will try later."),
            leo("Then I read the logs, surely."),
            maya("No. That is the first trap. The container never started, so there are no logs at all. The failure happened to the platform, not to your code."),
            leo("So where did the platform write it down?"),
            maya("In its own diary, which you read with kubectl describe pod and the pod's name. The last section is Events: everything Kubernetes did to that pod, in order."),
            leo("And what did the diary say?"),
            maya("No image with that tag. The pipeline built version three point one, and the file we deployed asked for v thirty one. One missing full stop."),
            leo("[sigh] A full stop. Is it always a typo?"),
            maya("Two causes, and the exam likes both. Either the tag is wrong, or the puller has no permission. Zaiqa pulls with a managed identity, a badge Azure gives the app instead of a password."),
            leo("And how do I check the badge quickly?"),
            maya("One command for that: az aks check-acr. It tries the pull from the cluster and names which it was: the name, the tag or the permission."),
            leo("We fixed the tag. Then?"),
            maya("Then the pods came up, and went straight into CrashLoopBackOff. The container starts, dies, and Kubernetes restarts it, waiting longer each time. Five seconds, ten, twenty, up to five minutes."),
            leo("Now there are logs, at least."),
            maya("Now there are logs, and here is the second trap. Plain kubectl logs shows the container starting right now, which has printed nothing yet. You want the one that just died."),
            leo("How do I ask for a dead one?"),
            maya("Add dash dash previous. Kubectl logs, the pod name, dash dash previous. The most useful flag in this episode, and the exam asks it almost word for word."),
            leo("And what had died?"),
            maya("A dot NET startup error: no connection string for Azure SQL, which holds Zaiqa's orders. Someone added that setting on the Container Apps side, and never on the cluster."),
            leo("So a missing app setting looks exactly like broken code."),
            maya("It does from outside, which is why you read the crash before you theorise. For example, a missing setting, a wrong password and a bad port all look the same."),
            leo("Fine. Third act?"),
            maya("Third act is the quiet one. The pods were Running, no restarts at all, and ready said zero of one. And the app still showed nothing."),
            leo("Running but not ready. What is the difference?"),
            maya("That is probes. A probe is a small check the platform runs against your container, on a schedule. Three kinds, and the exam cares about telling them apart."),
            leo("Give me the three."),
            maya("A liveness probe asks, are you alive? Fail it, and the container is restarted. A readiness probe asks, can you take work? Fail it, and it stays running, but out of the traffic."),
            leo("And the third?"),
            maya("A startup probe, for a slow starter. It holds the other two back while the app wakes up, so a service that needs forty seconds is not killed at ten."),
            leo("So Running, zero of one, no restarts means what?"),
            maya("Readiness is failing and liveness is not. Ours checked Azure SQL from a page called ready, and the cluster's firewall rule was missing. The check honestly said no."),
            leo("How does traffic even know to skip it?"),
            maya("On the cluster a Service is a stable address in front of your pods, with a list behind it of the ready ones. Kubectl get endpoints prints that list."),
            leo("Picture a guest list with nobody on it. That is bleak."),
            maya("[chuckle] It is. And one mistake is worth saying out loud: never point a liveness probe at a check that calls the database. A slow database then restarts everything."),
            leo("Noted. Now the Container Apps side. There is no kubectl there."),
            maya("No, and you do not need it. Start with az containerapp revision list. A revision is just one frozen version of your app and its settings; every change makes another."),
            leo("And what does the list tell me?"),
            maya("Two columns matter: provisioning status and running status. Degraded, with zero of one replicas ready, is the one you will see most. A replica is just one running copy."),
            leo("And Degraded usually means?"),
            maya("A probe failing, or the port being wrong. Which was Friday's last gift. Our API listens on eight thousand and eighty, and the ingress target port said eighty."),
            leo("So the front door knocks on the wrong door inside."),
            maya("Exactly, and with ingress on, the platform's default check uses that target port too. Nothing answers on eighty, so no replica becomes ready and the revision stays Degraded."),
            leo("Where do I see that happening?"),
            maya("Two log streams, and they differ. System logs are the platform talking: pulls, probes, revisions. Console logs are your own code's output."),
            leo("And I say which one I want?"),
            maya("With az containerapp logs show, and dash dash type system, or dash dash type console. Add dash dash follow, and it scrolls live as things happen."),
            leo("And if I want to poke around inside the container?"),
            maya("Az containerapp exec, which opens a shell inside a running replica. From in there you call your own app on localhost, and that settles the port argument in ten seconds."),
            leo("Say you're building the same thing on the cluster instead. Same trick?"),
            maya("Same idea, two commands. Kubectl exec gets you a shell in a pod, and kubectl port-forward wires a port on your laptop to the pod, skipping the Service. If that works, it is readiness."),
            leo("One thing bothers me. All of this is live. What about yesterday?"),
            maya("Then the stream is no use; it only shows now. You need Log Analytics, the store that keeps the history, asked in KQL, which is a query language that reads almost like sentences."),
            leo("And on the cluster side?"),
            maya("Turn on Container insights, which is Azure Monitor collecting container logs and Kubernetes events into that same store. Then yesterday's crash is a query away."),
            leo("So what do I remember for the exam?"),
            maya("ImagePullBackOff: read describe pod, not the logs, and check the tag and the pull permission. CrashLoopBackOff: kubectl logs, dash dash previous. Running but zero of one ready: the readiness probe."),
            leo("And the Container Apps ones?"),
            maya("A revision Degraded with no replica ready: match the target port to the port your app listens on. Pending: look for FailedScheduling. And yesterday's errors: Log Analytics, never the live stream."),
            leo("And something to try today?"),
            maya("Deploy a revision with the wrong target port and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, call the app on localhost, and query the console logs in Log Analytics for the last hour."),
            leo("Status, then events, then the logs of the one that died. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Release That Would Not Start. Five on a Friday, Zaiqa ships a new version, and within four minutes the Android app is showing an empty restaurant list in three cities."),
            leo("Detective Leo, reporting. [chuckle] Friday at five. Who signs these things off? Right. Evidence. Where does Zaiqa's API actually live these days?"),
            maya("In two places, which is half the case. The Zaiqa API, which is the dot NET service the app calls for everything, runs on Container Apps behind ingress, the front door that lets outside calls in."),
            leo("And the rest?"),
            maya("The order worker moved to AKS last month, which is Azure Kubernetes Service: a cluster of our own machines. A cluster is just a group of computers working as one."),
            leo("Two crime scenes. My first suspect is the code. Somebody pushed bad code on a Friday."),
            maya("Hold that thought and look at the body first. On the cluster, kubectl get pods. Kubectl is the Kubernetes command line, and a pod is the little wrapper one container runs inside."),
            leo("And it says?"),
            maya("Every pod reads ImagePullBackOff. The machine tried to download the image, failed, and is waiting before it tries again."),
            leo("Then I want the logs. Bad code prints a trace."),
            maya("There are no logs, detective. The container never ran. That is the thing a junior gets wrong here, and the exam loves it: no start, no output."),
            leo("[sigh] Then where is my witness?"),
            maya("The platform's own diary. Kubectl describe pod, then the name, and the last section is Events: everything done to that pod, in order, with times."),
            leo("Read me the entry."),
            maya("No image with that tag. Our pipeline built version three point one. The file we deployed asked for v thirty one. One missing full stop, three cities dark."),
            leo("So the code was innocent. Is a typo the only way this happens?"),
            maya("No, and this is the exam's favourite pair. Either the tag is wrong, or the puller has no permission. Zaiqa pulls with a managed identity, a badge Azure gives the app instead of a password, holding the pull role on the registry."),
            leo("And is there a quick way to tell them apart?"),
            maya("One command: az aks check-acr. It attempts the pull from inside the cluster and tells you whether it was the name, the tag or the permission."),
            leo("Tag fixed. Case closed?"),
            maya("Case reopened. The pods came up and fell straight into CrashLoopBackOff: start, die, restart, with Kubernetes waiting longer every round, up to five minutes."),
            leo("Now I get my logs."),
            maya("Now you get a trap. Plain kubectl logs shows the container starting right now, which has printed nothing yet. The one with the story is the one that just died."),
            leo("And I ask for the dead one how?"),
            maya("Dash dash previous. Kubectl logs, the pod name, dash dash previous. Write that one on your hand."),
            leo("And the dying words were?"),
            maya("A dot NET startup error: no connection string for Azure SQL, where Zaiqa keeps the orders. Someone added that setting to the Container Apps copy on Wednesday and never added it to the cluster."),
            leo("A missing app setting. It looked exactly like broken code."),
            maya("From the outside they always do. For example, a missing setting, a wrong password and a bad port all wear the same mask, until the previous log takes it off."),
            leo("Third body?"),
            maya("Quieter. The pods were Running, zero restarts, and ready said zero of one. And the app still showed nothing at all."),
            leo("Alive but useless. Why?"),
            maya("Probes. A probe is a small check the platform runs against your container on a schedule. Three kinds, and telling them apart is half the exam's troubleshooting questions."),
            leo("Name them."),
            maya("A liveness probe asks, are you alive? Fail it, and the container is restarted. A readiness probe asks, can you take work? Fail it, and the container keeps running, but gets no traffic."),
            leo("And the third?"),
            maya("A startup probe, which holds the other two off while a slow app wakes up. Without it, an app that needs forty seconds is killed at ten, forever."),
            leo("So zero restarts plus zero of one ready means readiness, not liveness."),
            maya("You are getting good at this. Ours checked Azure SQL from a page called ready, and the cluster's firewall rule was missing. The check said no, honestly, every single time."),
            leo("And how does traffic know to skip a pod?"),
            maya("A Service is a stable address in front of your pods, and it keeps a list of the ready ones. Kubectl get endpoints, then the name, prints that list. Empty means nobody qualified."),
            leo("Bleak. And the other crime scene, Container Apps? No kubectl there."),
            maya("Different tools, same three questions. Az containerapp revision list first. A revision is just one frozen version of your app and its settings, and every change makes a new one."),
            leo("And what was the verdict?"),
            maya("Degraded, zero of one replicas ready. A replica is just one running copy. Degraded nearly always means a probe failing, or a wrong port."),
            leo("And which was it?"),
            maya("The port. Our API listens on eight thousand and eighty. The ingress target port said eighty. With ingress on, the platform's own default check uses that target port, so nothing ever answered."),
            leo("The front door knocking on a wall."),
            maya("[chuckle] For an hour. And the evidence sat in the system logs the whole time: pulls, probes, revisions. Your own code's output is the console logs, and those were silent, because the app was fine."),
            leo("Two streams. How do I ask for each?"),
            maya("Az containerapp logs show, dash dash type system, or dash dash type console. Add dash dash follow to watch it live."),
            leo("And if I want to stand inside the container myself?"),
            maya("Az containerapp exec opens a shell in a running replica. From there, call your own app on localhost and see which port answers. Ten seconds, argument over."),
            leo("Last question, detective work aside. Yesterday's evidence?"),
            maya("The stream only shows now, so for yesterday you need Log Analytics, the store that keeps the history. You question it in KQL, which is a query language that reads almost like plain sentences."),
            leo("And on the cluster?"),
            maya("Turn on Container insights, which is Azure Monitor gathering container logs and Kubernetes events into that same store. Without it, a crash from last Tuesday is simply gone."),
            leo("So, the case summary for the exam?"),
            maya("ImagePullBackOff: read describe pod and check the tag and the pull permission, never the logs. CrashLoopBackOff: kubectl logs with dash dash previous. Running, zero of one ready: the readiness probe."),
            leo("And the Container Apps verdicts?"),
            maya("A revision Degraded with no replica ready: match the target port to the port your app listens on. Pending: look for FailedScheduling. And yesterday's errors: Log Analytics, not the live stream."),
            leo("Case closed. Anything to try today?"),
            maya("Deploy a revision with the wrong target port and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, call the app on localhost, and query the console logs in Log Analytics for the last hour."),
            leo("Status, events, and the logs of the one that died. Case closed. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Did you hear what happened to the Zaiqa release on Friday? Five o'clock, new version out, and by six the app was showing empty restaurant lists in three cities. Sit down, there are four separate stories."),
            leo("Four? Start with the map. Where does the Zaiqa API live now?"),
            maya("Two homes, which is why this took so long. The Zaiqa API, which is the dot NET service the app calls for everything, sits on Container Apps behind ingress, the front door outside calls come through."),
            leo("And the worker?"),
            maya("On AKS since last month, which is Azure Kubernetes Service: a cluster we run ourselves. A cluster is just a group of computers working as one."),
            leo("Right. Story one."),
            maya("Tom. Tom deployed, then went home. Kubectl get pods, which is the Kubernetes command line, showed every pod stuck on ImagePullBackOff. A pod is the little wrapper one container runs inside."),
            leo("Which means?"),
            maya("The machine tried to download the image, failed, and is waiting before trying again. And Dana, bless her, spent twenty minutes reading kubectl logs."),
            leo("[chuckle] And found nothing."),
            maya("Nothing at all, because the container never started. No start, no output. The answer was in the platform's diary all along: kubectl describe pod, and the Events section at the bottom."),
            leo("What did the diary say?"),
            maya("No image with that tag. The pipeline built version three point one. The deployment file asked for v thirty one. The intern had typed it on Thursday and nobody looked."),
            leo("A full stop took down three cities."),
            maya("It did. And it is not always a typo. The other half of the time the puller has no permission. Zaiqa pulls with a managed identity, a badge Azure gives the app so there is no password, with the pull role on the registry."),
            leo("How do you tell those apart without guessing?"),
            maya("Az aks check-acr. It tries the pull from the cluster and names the problem. Marcus ran it on Saturday, which was a little late to be useful."),
            leo("[laugh] Saturday. Story two."),
            maya("Priya fixed the tag, the pods came up, and went straight into CrashLoopBackOff. Start, die, restart, and Kubernetes waits longer each round, up to five minutes."),
            leo("At least there are logs now."),
            maya("There are, and Priya read the wrong ones. Plain kubectl logs shows the container that is starting now, which has printed nothing. The story is in the one that just died."),
            leo("And you get that how?"),
            maya("Dash dash previous. Kubectl logs, the pod name, dash dash previous. Priya has it written on a sticky note now."),
            leo("And what did the dead one say?"),
            maya("No connection string for Azure SQL, where Zaiqa keeps its orders. Dana had added that setting to the Container Apps copy on Wednesday and never added it to the cluster."),
            leo("So a missing app setting looked like broken code."),
            maya("They always do from the outside. For example, a missing setting, a wrong password and a wrong port all look identical, until the previous log says otherwise."),
            leo("Story three, please."),
            maya("The quiet one. Pods Running, zero restarts, ready zero of one, and the app still blank. Marcus declared it a network problem and opened a ticket with the network team."),
            leo("And it was not."),
            maya("It was probes. A probe is a small check the platform runs against your container on a schedule, and there are three kinds of them."),
            leo("Go on then."),
            maya("A liveness probe asks, are you alive? Fail it, the container restarts. A readiness probe asks, can you take work? Fail it, the container stays up but gets no traffic. And a startup probe holds both off while a slow app wakes."),
            leo("So zero restarts and zero of one ready points at readiness."),
            maya("Which Marcus could have read in thirty seconds. Ours checked Azure SQL from a page called ready, and the cluster's firewall rule was missing, so the check honestly said no."),
            leo("And why does the traffic skip it?"),
            maya("A Service is a stable address in front of your pods, keeping a list of the ready ones. Kubectl get endpoints, then the name, prints that list. Marcus's was empty."),
            leo("Picture a guest list with nobody on it."),
            maya("[chuckle] That was the week. Story four, and this one is the Container Apps side, where there is no kubectl at all."),
            leo("What do you start with there?"),
            maya("Az containerapp revision list. A revision is just one frozen version of your app and its settings, and every change makes a new one."),
            leo("And the verdict?"),
            maya("Degraded, zero of one replicas ready. A replica is just one running copy. Degraded almost always means a probe failing, or the port being wrong."),
            leo("And which, this time?"),
            maya("The port. The API listens on eight thousand and eighty, and the intern set the ingress target port to eighty, because eighty is what websites use."),
            leo("Reasonable, and wrong."),
            maya("Completely wrong here. With ingress on, the platform's own default check knocks on that target port too, so no replica ever became ready and the revision stayed Degraded all evening."),
            leo("And nobody saw it?"),
            maya("It was written in the system logs the whole time: pulls, probes, revisions. Everyone was staring at the console logs, which are your own code's output, and those were perfectly quiet."),
            leo("How do you ask for one or the other?"),
            maya("Az containerapp logs show, dash dash type system, or dash dash type console, and dash dash follow to watch it live."),
            leo("And could anyone have just looked inside?"),
            maya("Az containerapp exec opens a shell in a running replica. Call your own app on localhost from in there, and the port question is settled in ten seconds. Priya did it, on Saturday."),
            leo("And on Monday, when everyone wanted the timeline?"),
            maya("That is the last lesson. The live stream only shows now. Monday's timeline came out of Log Analytics, the store that keeps the history, queried in KQL, which is a query language that reads almost like plain sentences."),
            leo("And the cluster's half?"),
            maya("Container insights, which is Azure Monitor collecting container logs and Kubernetes events into that same store. It had been off on the cluster, so Tuesday's crashes are simply gone."),
            leo("So, the gossip I should remember for the exam?"),
            maya("ImagePullBackOff: describe pod, check the tag and the pull permission, not the logs. CrashLoopBackOff: kubectl logs with dash dash previous. Running but zero of one ready: the readiness probe."),
            leo("And the rest?"),
            maya("A revision Degraded with no replica ready: match the target port to the app's own port. Pending: look for FailedScheduling. Yesterday's errors: Log Analytics, never the live stream."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Deploy a revision with the wrong target port and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, call the app on localhost, and query the console logs in Log Analytics for the last hour."),
            leo("Status, events, and the logs of the one that died. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-container-troubleshoot",
        listOf(
            nova("Hey, you. It is late, the kitchen light is still on, and somewhere a release is refusing to start. Tonight we go looking for it together."),
            ivy("A Friday release that will not come up. My favourite horror story. Where are we looking, Nova?"),
            nova("Two places. The Zaiqa API, which is the dot NET service the app calls for everything, lives on Container Apps behind ingress, the front door that lets outside calls reach it."),
            ivy("And the heavy lifting moved out last month, did it not?"),
            nova("To AKS, which is Azure Kubernetes Service: a cluster of our own. A cluster is just a group of computers working as one. [pause] Two homes, two sets of tools, one way of thinking."),
            ivy("Give me the one way of thinking. It is late, and I would like something I can hold."),
            nova("Three questions, always in this order. Did it start? Why did it stop? Can anything reach it? [pause] Everything tonight is one of those three."),
            ivy("Question one, then. Did it start?"),
            nova("On the cluster you ask with kubectl get pods. Kubectl is the Kubernetes command line, and a pod is the little wrapper one container runs inside. It prints a status, a ready count and a restart count."),
            ivy("And on Friday it printed something unfriendly."),
            nova("ImagePullBackOff. The machine tried to fetch the image, could not, and is waiting a little before trying again. [pause] And here is where people lose an hour."),
            ivy("They go and read the logs."),
            nova("They go and read the logs. There are none, love. The container never started, so it never printed a word. The failure belongs to the platform, not to your code."),
            ivy("Then what does the platform have?"),
            nova("A diary. Kubectl describe pod, then the name, and at the bottom a section called Events: everything done to that pod, in order."),
            ivy("And Friday's entry?"),
            nova("No image with that tag. The pipeline built version three point one, and the file asked for v thirty one. [pause] A full stop, and three cities in the dark."),
            ivy("[sigh] So small. Is it always a typo?"),
            nova("Half the time. The other half, the thing doing the pulling has no permission. Zaiqa pulls with a managed identity, a badge Azure hands the app so nobody needs a password, carrying the pull role on the registry."),
            ivy("And is there a lazy way to tell which?"),
            nova("There is, and lazy is right at this hour: az aks check-acr. It tries the pull from the cluster and tells you whether it was the name, the tag or the permission."),
            ivy("Tag fixed. Does the night end there?"),
            nova("[chuckle] It does not. The pods came up and dropped into CrashLoopBackOff: start, die, restart, with Kubernetes waiting longer each round, up to five minutes."),
            ivy("And now there really are logs."),
            nova("Now there are, and now the second trap. Plain kubectl logs shows the container starting this second, which has printed nothing. You want the one that just died."),
            ivy("How do I ask for a ghost?"),
            nova("Dash dash previous. [pause] Kubectl logs, the pod name, dash dash previous. If you remember one thing tonight, make it that one."),
            ivy("And what did the ghost say?"),
            nova("That there was no connection string for Azure SQL, where Zaiqa keeps its orders. Someone added that setting on the Container Apps side and never added it on the cluster."),
            ivy("A missing app setting, wearing a crash's clothes."),
            nova("They all do. A missing setting, a wrong password, a bad port: identical from outside, until the previous log takes the mask off. [pause] Question three, then. Can anything reach it?"),
            ivy("This is the quiet one, is it not."),
            nova("The quiet one. Pods Running, no restarts, ready zero of one, and still nothing on the phone. That is probes. A probe is a small check the platform runs against your container on a schedule."),
            ivy("Three kinds, I am told."),
            nova("Three. A liveness probe asks, are you alive? Fail it, and you are restarted. A readiness probe asks, can you take work? Fail it, and you stay up, but nobody sends you any."),
            ivy("That second one sounds like a quiet evening in."),
            nova("[chuckle] It rather does. And a startup probe holds the other two off while a slow app wakes up, so forty seconds of loading is not a death sentence."),
            ivy("So no restarts, zero of one ready, means readiness."),
            nova("Every time. Ours checked Azure SQL from a page called ready, and the firewall rule for the cluster was missing, so the check kept answering honestly: no."),
            ivy("And how does the traffic even know?"),
            nova("A Service is a stable address in front of your pods, with a list behind it of the ones that are ready. Kubectl get endpoints, then the name, prints that list. [pause] Empty, that night."),
            ivy("And on the other side, Container Apps. No kubectl there."),
            nova("No, and nothing to miss. Az containerapp revision list. A revision is just one frozen version of your app and its settings, and every change makes another."),
            ivy("And what was it showing?"),
            nova("Degraded, zero of one replicas ready. A replica is just one running copy. Degraded almost always means a probe failing, or a port that does not match."),
            ivy("Say the port one slowly, for the person on the sofa."),
            nova("The API listens on eight thousand and eighty. Ingress was told the target port is eighty. With ingress on, the platform's own check knocks on that target port too, so no replica ever became ready."),
            ivy("A front door knocking at a wall, all evening."),
            nova("All evening. And it was written down the whole time, in the system logs, which are the platform talking: pulls, probes, revisions. Your own code's output is the console logs, and those were quiet, because the app was fine."),
            ivy("How do I ask for each?"),
            nova("Az containerapp logs show, dash dash type system, or dash dash type console. Add dash dash follow, and it scrolls live while you watch."),
            ivy("And if I want to stand inside the thing?"),
            nova("Az containerapp exec opens a shell in a running replica. [pause] From in there, call your own app on localhost, and the port argument is over in ten seconds."),
            ivy("And tomorrow morning, when somebody wants the timeline?"),
            nova("The live stream only shows now, so tomorrow belongs to Log Analytics, the store that keeps the history. You ask it in KQL, which is a query language that reads almost like plain sentences."),
            ivy("And the cluster's half of the history?"),
            nova("Container insights, which is Azure Monitor collecting your container logs and Kubernetes events into that same store. [pause] Leave it off, and last Tuesday is simply gone."),
            ivy("[sigh] Before I fall asleep on you. What do I keep for the exam?"),
            nova("ImagePullBackOff: describe pod, check the tag and the pull permission, not the logs. CrashLoopBackOff: kubectl logs with dash dash previous. Running but zero of one ready: the readiness probe."),
            ivy("And the Container Apps ones?"),
            nova("A revision Degraded with no replica ready: match the target port to the port the app listens on. Pending: look for FailedScheduling. And yesterday's errors: Log Analytics, never the live stream."),
            ivy("And something small to try today, before I sleep?"),
            nova("Deploy a revision with the wrong target port and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, call the app on localhost, and query the console logs in Log Analytics for the last hour."),
            ivy("Status, events, and the logs of the one that died. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-container-troubleshoot",
        listOf(
            nova("Date night, darling. Wine poured, phone face down, and a release that would not start to pick over together."),
            rafe("The most romantic Friday I can imagine. Remind me where Zaiqa lives these days."),
            nova("Two homes, love. The Zaiqa API, which is the dot NET service the app calls for everything, is on Container Apps behind ingress, the front door outside calls come through."),
            rafe("And the worker moved out last month."),
            nova("To AKS, which is Azure Kubernetes Service: a cluster of our own. A cluster is just a group of computers working as one. Two homes, one way of thinking."),
            rafe("Then quiz me on the one way of thinking, and I will earn my dinner."),
            nova("Three questions, in order. Did it start, why did it stop, and can anything reach it? Friday gave us all three, so let us take them one at a time."),
            rafe("Question one. On the cluster, kubectl get pods, which is the Kubernetes command line. A pod is the little wrapper one container runs inside, and the line shows its status, its ready count and its restarts."),
            nova("Right answer, and you did not even look at the menu. [kiss] It said ImagePullBackOff: the machine tried to fetch the image, failed, and is waiting before it tries again."),
            rafe("So I open the logs and find the stack trace."),
            nova("[chuckle] No kiss for that one, handsome. The container never started, so there are no logs at all. The failure is the platform's, not your code's."),
            rafe("Then the platform must have written it down somewhere."),
            nova("In its diary. Kubectl describe pod, then the name, and the last section is Events: everything done to that pod, in order. Friday's entry said no image with that tag."),
            rafe("Let me guess. Three point one against thirty one."),
            nova("A missing full stop, and three cities dark. Exactly that. And it is not always a typo, love. Half the time the puller simply has no permission."),
            rafe("That would be the managed identity, which is the badge Azure gives the app instead of a password, carrying the pull role on the registry."),
            nova("Exactly right, and said beautifully. [kiss] And az aks check-acr will tell you which of the two it was, from inside the cluster."),
            rafe("My turn to ask, then. The tag is fixed, the pods are up, and now they are in CrashLoopBackOff. What is that, and what do you run?"),
            nova("Start, die, restart, with Kubernetes waiting longer each round. And I run kubectl logs with the pod name and dash dash previous, because plain logs show the container starting now, which has printed nothing yet."),
            rafe("Right answer, and the flag nobody remembers. [kiss] Ours said there was no connection string for Azure SQL, where Zaiqa keeps the orders."),
            nova("A missing app setting, added on the Container Apps side and never on the cluster. They always look like broken code from outside, until the previous log takes the mask off."),
            rafe("[chuckle] Question three, then. Can anything reach it?"),
            nova("The quiet one. Pods Running, no restarts, ready zero of one, and still nothing on the phone. That is probes: a probe is a small check the platform runs against your container on a schedule."),
            rafe("Three of them, and I want to see whether you can keep them straight."),
            nova("A liveness probe asks, are you alive? Fail it and you are restarted. A readiness probe asks, can you take work? Fail it and you stay up, but nobody sends you any. And a startup probe holds both off while a slow app wakes."),
            rafe("Perfect, all three. [kiss] So no restarts and zero of one ready points at which?"),
            nova("Readiness, every time. Ours checked Azure SQL from a page called ready, and the firewall rule for the cluster was missing, so it kept answering no, honestly."),
            rafe("And the traffic knows how?"),
            nova("A Service is a stable address in front of your pods, with a list behind it of the ready ones. Kubectl get endpoints prints that list, and ours was empty."),
            rafe("Now the other home. Container Apps, where there is no kubectl."),
            nova("Az containerapp revision list first. A revision is just one frozen version of your app and its settings, and every change makes another."),
            rafe("And Friday's said Degraded, zero of one replicas ready."),
            nova("A replica is just one running copy, and Degraded nearly always means a probe failing, or a port that does not match. Quiz for you, darling. Our API listens on eight thousand and eighty. Ingress was told the target port is eighty. What happens?"),
            rafe("Nothing answers on eighty, so the default check fails, no replica ever becomes ready, and the revision stays Degraded. The fix is to match the target port to the port the app actually listens on."),
            nova("Right answer, and that is dinner earned. [kiss] And it was written down all evening, in the system logs, which are the platform talking: pulls, probes, revisions."),
            rafe("And my own code's output?"),
            nova("The console logs, which were perfectly quiet, because the app was fine. Az containerapp logs show, dash dash type system or dash dash type console, and dash dash follow to watch live."),
            rafe("And if I want to go inside and look around?"),
            nova("Az containerapp exec opens a shell in a running replica. Call your own app on localhost from in there, and the port argument is over in ten seconds."),
            rafe("One more from me, and it is the one people get wrong. It is Monday, and I want Friday evening's errors. Where are they?"),
            nova("Not in the live stream, which only shows now. They are in Log Analytics, the store that keeps the history, asked for in KQL, which is a query language that reads almost like plain sentences."),
            rafe("Right answer, and nobody ever gets that one. [kiss] And the cluster's half of the history?"),
            nova("Container insights, which is Azure Monitor collecting container logs and Kubernetes events into that same store. Leave it off, and last Tuesday is simply gone."),
            rafe("So before pudding. What do we keep for the exam?"),
            nova("ImagePullBackOff: describe pod, check the tag and the pull permission, not the logs. CrashLoopBackOff: kubectl logs with dash dash previous. Running but zero of one ready: the readiness probe."),
            rafe("And the Container Apps two?"),
            nova("A revision Degraded with no replica ready: match the target port. Pending: look for FailedScheduling. And yesterday's errors: Log Analytics, never the live stream."),
            rafe("Five for five, darling. [kiss] And something to try today?"),
            nova("Deploy a revision with the wrong target port and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, call the app on localhost, and query the console logs in Log Analytics for the last hour."),
            rafe("Status, events, and the logs of the one that died. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun places() = DiagramSpec(
        "Zaiqa's API runs on Container Apps and the order worker on AKS: two places to look when the app goes quiet.",
        nodes = listOf(
            DNode("phone", "Phone", 0.15f, 0.5f, w = 0.24f, h = 0.2f, kind = NodeKind.USER),
            DNode("aca", "Container Apps", 0.65f, 0.2f, w = 0.5f, h = 0.22f, tone = 1, sub = "the Zaiqa API"),
            DNode("aks", "AKS", 0.65f, 0.78f, w = 0.5f, h = 0.22f, sub = "the order worker"),
        ),
        edges = listOf(DEdge("phone", "aca", "calls")),
        aspect = 0.8f,
    )

    private fun questions() = DiagramSpec(
        "Three questions, asked in order: did it start, why did it stop, and can traffic reach it.",
        nodes = listOf(
            DNode("q1", "Did it start?", 0.5f, 0.16f, w = 0.8f, h = 0.2f, kind = NodeKind.PILL, sub = "status and events"),
            DNode("q2", "Why did it stop?", 0.5f, 0.5f, w = 0.8f, h = 0.2f, kind = NodeKind.PILL, sub = "its own logs"),
            DNode("q3", "Can traffic reach it?", 0.5f, 0.84f, w = 0.8f, h = 0.2f, kind = NodeKind.PILL, sub = "probes, ingress, Service"),
        ),
        edges = listOf(DEdge("q1", "q2"), DEdge("q2", "q3")),
        aspect = 0.8f,
    )

    private fun aksLadder() = DiagramSpec(
        "On AKS: get pods for the status, describe for the events, logs for the code, and Container insights for history.",
        nodes = listOf(
            DNode("get", "kubectl get pods", 0.5f, 0.12f, w = 0.8f, h = 0.17f, sub = "status, ready, restarts"),
            DNode("describe", "kubectl describe pod", 0.5f, 0.38f, w = 0.8f, h = 0.17f, sub = "the Events diary"),
            DNode("logs", "kubectl logs", 0.5f, 0.64f, w = 0.8f, h = 0.17f, sub = "what the code printed"),
            DNode("ci", "Container insights", 0.5f, 0.88f, w = 0.8f, h = 0.17f, kind = NodeKind.CYLINDER, tone = 2, sub = "kept for later"),
        ),
        edges = listOf(DEdge("get", "describe"), DEdge("describe", "logs"), DEdge("logs", "ci")),
        aspect = 0.8f,
    )

    private fun probes() = DiagramSpec(
        "The platform checks the container's pulse three ways: liveness restarts it, readiness keeps traffic away, startup waits.",
        nodes = listOf(
            DNode("platform", "Platform", 0.5f, 0.16f, w = 0.5f, h = 0.2f, tone = 1, sub = "checks the pulse"),
            DNode("live", "Liveness", 0.17f, 0.72f, w = 0.3f, h = 0.26f, sub = "restarts it"),
            DNode("ready", "Readiness", 0.5f, 0.72f, w = 0.3f, h = 0.26f, sub = "no traffic"),
            DNode("start", "Startup", 0.83f, 0.72f, w = 0.3f, h = 0.26f, sub = "slow starts"),
        ),
        edges = listOf(
            DEdge("platform", "live"),
            DEdge("platform", "ready"),
            DEdge("platform", "start"),
        ),
        aspect = 0.8f,
    )

    private fun aca() = DiagramSpec(
        "In Container Apps: the revision's health, system logs from the platform, console logs from the app, and history in Log Analytics.",
        nodes = listOf(
            DNode("rev", "Revision", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "Degraded, 0/1 ready"),
            DNode("sys", "System logs", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "pulls, probes"),
            DNode("con", "Console logs", 0.25f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "what the app prints"),
            DNode("la", "Log Analytics", 0.75f, 0.76f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "history, KQL"),
        ),
        edges = listOf(
            DEdge("rev", "sys"),
            DEdge("sys", "la"),
            DEdge("con", "la"),
        ),
        aspect = 0.8f,
    )

    private fun ingress() = DiagramSpec(
        "Ingress passes callers to its target port; if the app listens on 8080 and ingress targets 80, nobody answers.",
        nodes = listOf(
            DNode("caller", "Caller", 0.15f, 0.2f, w = 0.24f, h = 0.2f, kind = NodeKind.USER),
            DNode("ing", "Ingress", 0.65f, 0.2f, w = 0.5f, h = 0.2f, tone = 1, sub = "target port 80"),
            DNode("app", "Zaiqa API", 0.65f, 0.78f, w = 0.5f, h = 0.22f, sub = "listens on 8080"),
            DNode("con", "Console", 0.2f, 0.78f, w = 0.34f, h = 0.22f, kind = NodeKind.NOTE, sub = "exec, localhost"),
        ),
        edges = listOf(
            DEdge("caller", "ing"),
            DEdge("ing", "app", "80, not 8080", dashed = true),
            DEdge("con", "app", "localhost"),
        ),
        aspect = 0.8f,
    )

    private fun endpoints() = DiagramSpec(
        "A Service sends calls only to the ready pods on its endpoints list; a pod failing readiness is taken off it.",
        nodes = listOf(
            DNode("svc", "Service", 0.5f, 0.16f, w = 0.6f, h = 0.2f, tone = 1, sub = "stable address"),
            DNode("ep", "Endpoints", 0.5f, 0.5f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "ready pods only"),
            DNode("pod", "Pod, 0/1 ready", 0.5f, 0.84f, w = 0.6f, h = 0.2f, tone = 2, sub = "off the list"),
        ),
        edges = listOf(
            DEdge("svc", "ep"),
            DEdge("ep", "pod", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's troubleshooting questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("pull", "ImagePullBackOff", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "describe pod, not logs"),
            DNode("crash", "CrashLoopBackOff", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "logs, previous"),
            DNode("ready", "Running, 0/1 ready", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "readiness probe"),
            DNode("port", "Degraded revision", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "fix the target port"),
            DNode("old", "Yesterday's errors", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Log Analytics"),
            DNode("pending", "Pending", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "FailedScheduling"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("pull", "crash", "ready", "port", "old", "pending")
    private val describeCode = BoardCard("Shell", "kubectl describe pod zaiqa-api-7d9f8c6b5-x2kqp", code = true)
    private val previousCode = BoardCard("Shell", "kubectl logs zaiqa-api-7d9f8c6b5-x2kqp --previous", code = true)
    private val revisionCode = BoardCard("Shell", "az containerapp revision list -n zaiqa-api -g zaiqa-rg -o table", code = true)
    private val execCode = BoardCard("Shell", "az containerapp exec -n zaiqa-api -g zaiqa-rg --command /bin/sh", code = true)
    private val today = BoardCard("Today", "A wrong target port, the system logs, fix it, then exec and call localhost.")
    private val threeQuestions = BoardCard("Three questions", "Did it start? Why did it stop? Can anything reach it?")
    private val imagePull = BoardCard("ImagePullBackOff", "The image could not be downloaded; it waits, then tries again.")
    private val noLogs = BoardCard("No logs to read", "The container never started, so it printed nothing at all.")
    private val theEvent = BoardCard("The event", "No image with that tag: the file asked for v31, the build made v3.1.")
    private val twoCauses = BoardCard("Two causes", "The tag is wrong, or the puller has no permission to pull it.")
    private val checkAcr = BoardCard("az aks check-acr", "Tries the pull from the cluster and names the real problem.")
    private val crashLoop = BoardCard("CrashLoopBackOff", "Start, die, restart, waiting longer each round, up to five minutes.")
    private val secondTrap = BoardCard("The second trap", "Plain logs show the container starting now, which printed nothing.")
    private val whatDied = BoardCard("What had died", "A missing connection string for Azure SQL, where the orders live.")
    private val aProbe = BoardCard("A probe", "A small check the platform runs against your container, on a schedule.")
    private val liveReady = BoardCard("Liveness and readiness", "Liveness restarts it. Readiness leaves it up, but out of the traffic.")
    private val startupProbe = BoardCard("Startup probe", "Holds the other two back while a slow app finishes waking up.")
    private val theVerdict = BoardCard("Which one was it?", "Zero restarts, zero of one ready: readiness is failing, not liveness.")
    private val endpointsCard = BoardCard("kubectl get endpoints", "The ready pods behind a Service. Empty means none qualified.")
    private val degraded = BoardCard("Degraded, zero of one", "A revision is one frozen version; a replica is one running copy.")
    private val wrongPort = BoardCard("The wrong port", "The API listens on 8080; ingress was told the target port is 80.")
    private val whyNotReady = BoardCard("Why nothing is ready", "With ingress on, the default check uses that target port too.")
    private val twoStreams = BoardCard("System and console logs", "System: pulls, probes, revisions. Console: your own code's output.")
    private val logsShow = BoardCard("logs show --type", "Add --type system or --type console, and --follow to watch live.")
    private val logAnalytics = BoardCard("Log Analytics", "The stream only shows now; the history is queried there, in KQL.")
    private val insights = BoardCard("Container insights", "Azure Monitor collects container logs and Kubernetes events.")
    private val phrase = BoardCard("Listen for the phrase", "ImagePullBackOff, CrashLoopBackOff, zero of one ready: three answers.")
    private val theRest = BoardCard("And the rest", "Degraded: the target port. Pending: FailedScheduling. Yesterday: history.")

    private fun classicBoard() = PodcastBoard(
        "ai-container-troubleshoot",
        listOf(
            // 0: "Hi, and welcome back. Today is the Friday release that will not start..."
            Scene(
                0, "Where Zaiqa runs now", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), light = setOf("aca"), card = BoardCard("The Zaiqa API", "Its image is on the shelf in our container registry.")),
                    Beat(2, light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps and ingress", "The front door that lets calls from the outside reach your app.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Azure Kubernetes Service: a cluster of our own machines.")),
                    Beat(6, light = setOf("aca", "aks"), card = BoardCard("The release", "Shipped at five on Friday. Three cities, empty restaurant lists.")),
                ),
            ),
            // 8: "Three questions, always in this order. Did it start? Why did it stop?..."
            Scene(
                8, "Three questions, in order", questions(),
                beats = listOf(
                    Beat(8, show = setOf("q1", "q2", "q3"), light = setOf("q1"), card = threeQuestions),
                ),
            ),
            // 10: "On the cluster, with kubectl get pods. Kubectl is the command line fo..."
            Scene(
                10, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(10, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "One line per pod: status, the ready count, and the restarts.")),
                    Beat(14, light = setOf("get"), card = imagePull),
                    Beat(16, light = setOf("get"), card = noLogs),
                    Beat(18, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = describeCode),
                    Beat(20, light = setOf("describe"), card = theEvent),
                    Beat(22, light = setOf("describe"), card = twoCauses),
                    Beat(24, light = setOf("describe"), card = checkAcr),
                ),
            ),
            // 26: "Then the pods came up, and went straight into CrashLoopBackOff..."
            Scene(
                26, "Why did it stop?", aksLadder(),
                beats = listOf(
                    Beat(26, show = setOf("get", "describe", "logs"), light = setOf("logs"), pulse = "describe" to "logs", card = crashLoop),
                    Beat(28, light = setOf("logs"), card = secondTrap),
                    Beat(30, light = setOf("logs"), card = previousCode),
                    Beat(32, light = setOf("logs"), card = whatDied),
                    Beat(34, light = setOf("logs"), card = BoardCard("The same mask", "A setting, a password or a port all look like a crash loop.")),
                ),
            ),
            // 36: "Third act is the quiet one. The pods were Running, no restarts at all..."
            Scene(
                36, "Running, but not ready", probes(),
                beats = listOf(
                    Beat(36, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Running, zero of one ready", "No restarts at all, and the app still shows nothing.")),
                    Beat(38, light = setOf("platform"), card = aProbe),
                    Beat(40, show = setOf("live", "ready"), light = setOf("live", "ready"), pulse = "platform" to "live", card = liveReady),
                    Beat(42, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = startupProbe),
                    Beat(44, light = setOf("ready"), card = theVerdict),
                ),
            ),
            // 46: "On the cluster a Service is a stable address in front of your pods..."
            Scene(
                46, "The ready list", endpoints(),
                beats = listOf(
                    Beat(46, show = setOf("svc", "ep", "pod"), light = setOf("ep"), pulse = "svc" to "ep", card = endpointsCard),
                    Beat(48, light = setOf("pod"), card = BoardCard("Never probe the database", "A liveness probe that calls the database restarts every pod at once.")),
                ),
            ),
            // 50: "No, and you do not need it. Start with az containerapp revision list..."
            Scene(
                50, "The Container Apps side", aca(),
                beats = listOf(
                    Beat(50, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(52, show = setOf("sys"), light = setOf("rev"), card = degraded),
                    Beat(54, light = setOf("rev"), card = wrongPort),
                    Beat(56, light = setOf("sys"), card = whyNotReady),
                    Beat(58, show = setOf("con"), light = setOf("sys", "con"), card = twoStreams),
                    Beat(60, light = setOf("sys", "con"), card = logsShow),
                ),
            ),
            // 62: "Az containerapp exec, which opens a shell inside a running replica..."
            Scene(
                62, "Inside the container", ingress(),
                beats = listOf(
                    Beat(62, show = setOf("caller", "ing", "app", "con"), light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                    Beat(64, light = setOf("app"), card = BoardCard("On the cluster", "kubectl exec for a shell; port-forward straight to the pod.")),
                ),
            ),
            // 66: "Then the stream is no use, because it only shows now..."
            Scene(
                66, "Keeping the history", aca(),
                beats = listOf(
                    Beat(66, show = setOf("rev", "sys", "con", "la"), light = setOf("la"), pulse = "sys" to "la", card = logAnalytics),
                    Beat(68, light = setOf("la"), card = insights),
                ),
            ),
            // 69: "So what do I remember for the exam?"
            Scene(
                69, "For the exam", exam(),
                beats = listOf(
                    Beat(70, show = setOf("pull", "crash", "ready"), light = setOf("pull", "crash", "ready"), card = phrase),
                    Beat(72, show = setOf("port", "old", "pending"), light = all6, card = theRest),
                    Beat(74, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-container-troubleshoot--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Release That Would Not Sta..."
            Scene(
                0, "The case", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), light = setOf("aca"), card = BoardCard("The case", "A Friday release at five, and three cities see empty lists.")),
                    Beat(2, light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps and ingress", "The front door that lets calls from the outside reach your app.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Azure Kubernetes Service: a cluster of our own machines.")),
                ),
            ),
            // 6: "Hold that thought and look at the body first. On the cluster, kubectl..."
            Scene(
                6, "Look at the body first", aksLadder(),
                beats = listOf(
                    Beat(6, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "One line per pod: status, the ready count, and the restarts.")),
                    Beat(8, light = setOf("get"), card = imagePull),
                    Beat(10, light = setOf("get"), card = BoardCard("Suspect one: the code", "No logs at all: the container never ran, so it printed nothing.")),
                    Beat(12, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = describeCode),
                    Beat(14, light = setOf("describe"), card = theEvent),
                    Beat(16, light = setOf("describe"), card = twoCauses),
                    Beat(18, light = setOf("describe"), card = checkAcr),
                ),
            ),
            // 20: "Case reopened. The pods came up and fell straight into CrashLoopBackO..."
            Scene(
                20, "Case reopened", aksLadder(),
                beats = listOf(
                    Beat(20, show = setOf("get", "describe", "logs"), light = setOf("logs"), pulse = "describe" to "logs", card = crashLoop),
                    Beat(22, light = setOf("logs"), card = secondTrap),
                    Beat(24, light = setOf("logs"), card = previousCode),
                    Beat(26, light = setOf("logs"), card = BoardCard("The dying words", "No connection string for Azure SQL, where Zaiqa keeps the orders.")),
                ),
            ),
            // 30: "Quieter. The pods were Running, zero restarts, and ready said zero of..."
            Scene(
                30, "Alive but useless", probes(),
                beats = listOf(
                    Beat(30, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Zero restarts, 0/1 ready", "Alive, and still serving nobody at all.")),
                    Beat(32, light = setOf("platform"), card = aProbe),
                    Beat(34, show = setOf("live", "ready"), light = setOf("live", "ready"), pulse = "platform" to "ready", card = liveReady),
                    Beat(36, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = startupProbe),
                    Beat(38, light = setOf("ready"), card = theVerdict),
                ),
            ),
            // 40: "A Service is a stable address in front of your pods, and it keeps a l..."
            Scene(
                40, "The ready list", endpoints(),
                beats = listOf(
                    Beat(40, show = setOf("svc", "ep", "pod"), light = setOf("ep"), pulse = "svc" to "ep", card = endpointsCard),
                ),
            ),
            // 42: "Different tools, same three questions. Az containerapp revision list..."
            Scene(
                42, "The other crime scene", aca(),
                beats = listOf(
                    Beat(42, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(44, show = setOf("sys"), light = setOf("rev"), card = degraded),
                    Beat(46, light = setOf("sys"), card = wrongPort),
                    Beat(48, show = setOf("con"), light = setOf("sys", "con"), card = twoStreams),
                    Beat(50, light = setOf("sys", "con"), card = logsShow),
                    Beat(52, light = setOf("con"), card = execCode),
                    Beat(54, show = setOf("la"), light = setOf("la"), pulse = "sys" to "la", card = logAnalytics),
                    Beat(56, light = setOf("la"), card = insights),
                ),
            ),
            // 57: "So, the case summary for the exam?"
            Scene(
                57, "Case closed", exam(),
                beats = listOf(
                    Beat(58, show = setOf("pull", "crash", "ready"), light = setOf("pull", "crash", "ready"), card = phrase),
                    Beat(60, show = setOf("port", "old", "pending"), light = all6, card = theRest),
                    Beat(62, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-container-troubleshoot--gossip",
        listOf(
            // 0: "Did you hear what happened to the Zaiqa release on Friday?..."
            Scene(
                0, "What the team did", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), light = setOf("aca"), card = BoardCard("The week", "A Friday release at five, and three cities see empty lists.")),
                    Beat(2, light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps and ingress", "The front door that lets calls from the outside reach your app.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Azure Kubernetes Service: a cluster of our own machines.")),
                ),
            ),
            // 6: "Tom. Tom deployed, then went home. Kubectl get pods..."
            Scene(
                6, "Tom: it would not pull", aksLadder(),
                beats = listOf(
                    Beat(6, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "One line per pod: status, the ready count, and the restarts.")),
                    Beat(8, light = setOf("get"), card = imagePull),
                    Beat(10, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("No logs, read the diary", "The container never started. describe pod ends with Events.")),
                    Beat(12, light = setOf("describe"), card = theEvent),
                    Beat(14, light = setOf("describe"), card = twoCauses),
                    Beat(16, light = setOf("describe"), card = checkAcr),
                ),
            ),
            // 18: "Priya fixed the tag, the pods came up, and went straight into CrashLo..."
            Scene(
                18, "Priya: the crash loop", aksLadder(),
                beats = listOf(
                    Beat(18, show = setOf("get", "describe", "logs"), light = setOf("logs"), pulse = "describe" to "logs", card = crashLoop),
                    Beat(20, light = setOf("logs"), card = secondTrap),
                    Beat(22, light = setOf("logs"), card = previousCode),
                    Beat(24, light = setOf("logs"), card = BoardCard("The dying words", "No connection string for Azure SQL, where Zaiqa keeps the orders.")),
                ),
            ),
            // 28: "The quiet one. Pods Running, zero restarts, ready zero of one..."
            Scene(
                28, "Marcus: alive, not ready", probes(),
                beats = listOf(
                    Beat(28, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Zero restarts, 0/1 ready", "Marcus called the network team. It was not the network.")),
                    Beat(30, light = setOf("platform"), card = aProbe),
                    Beat(32, show = setOf("live", "ready", "start"), light = setOf("live", "ready"), pulse = "platform" to "ready", card = BoardCard("Three probes", "Liveness restarts, readiness holds traffic back, startup waits.")),
                    Beat(34, light = setOf("ready"), card = theVerdict),
                ),
            ),
            // 36: "A Service is a stable address in front of your pods, keeping a list..."
            Scene(
                36, "The ready list", endpoints(),
                beats = listOf(
                    Beat(36, show = setOf("svc", "ep", "pod"), light = setOf("ep"), pulse = "svc" to "ep", card = endpointsCard),
                ),
            ),
            // 38: "That was the week. Story four, and this one is the Container Apps sid..."
            Scene(
                38, "The intern: the port", aca(),
                beats = listOf(
                    Beat(40, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(42, show = setOf("sys"), light = setOf("rev"), card = degraded),
                    Beat(44, light = setOf("rev"), card = wrongPort),
                    Beat(46, light = setOf("sys"), card = whyNotReady),
                    Beat(48, show = setOf("con"), light = setOf("sys", "con"), card = twoStreams),
                    Beat(50, light = setOf("sys", "con"), card = logsShow),
                    Beat(52, light = setOf("con"), card = execCode),
                    Beat(54, show = setOf("la"), light = setOf("la"), pulse = "sys" to "la", card = logAnalytics),
                    Beat(56, light = setOf("la"), card = insights),
                ),
            ),
            // 57: "So, the gossip I should remember for the exam?"
            Scene(
                57, "For the exam", exam(),
                beats = listOf(
                    Beat(58, show = setOf("pull", "crash", "ready"), light = setOf("pull", "crash", "ready"), card = phrase),
                    Beat(60, show = setOf("port", "old", "pending"), light = all6, card = theRest),
                    Beat(62, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-container-troubleshoot--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen light is still on..."
            Scene(
                0, "Tonight: it will not start", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), card = BoardCard("Tonight", "A Friday release that would not come up, and where to look.")),
                    Beat(2, light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps and ingress", "The front door that lets calls from the outside reach your app.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Azure Kubernetes Service: a cluster of our own machines.")),
                ),
            ),
            // 6: "Three questions, always in this order..."
            Scene(
                6, "Three questions, in order", questions(),
                beats = listOf(
                    Beat(6, show = setOf("q1", "q2", "q3"), light = setOf("q1"), card = threeQuestions),
                ),
            ),
            // 8: "On the cluster you ask with kubectl get pods..."
            Scene(
                8, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(8, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "A status, a ready count and a restart count, one line per pod.")),
                    Beat(10, light = setOf("get"), card = imagePull),
                    Beat(12, light = setOf("get"), card = noLogs),
                    Beat(14, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = describeCode),
                    Beat(16, light = setOf("describe"), card = theEvent),
                    Beat(18, light = setOf("describe"), card = twoCauses),
                    Beat(20, light = setOf("describe"), card = checkAcr),
                ),
            ),
            // 22: "It does not. The pods came up and dropped into CrashLoopBackOff..."
            Scene(
                22, "Why did it stop?", aksLadder(),
                beats = listOf(
                    Beat(22, show = setOf("get", "describe", "logs"), light = setOf("logs"), pulse = "describe" to "logs", card = crashLoop),
                    Beat(24, light = setOf("logs"), card = secondTrap),
                    Beat(26, light = setOf("logs"), card = previousCode),
                    Beat(28, light = setOf("logs"), card = whatDied),
                ),
            ),
            // 30: "They all do... Question three, then. Can anything reach it?"
            Scene(
                30, "Can anything reach it?", probes(),
                beats = listOf(
                    Beat(32, show = setOf("platform"), light = setOf("platform"), card = aProbe),
                    Beat(34, show = setOf("live", "ready"), light = setOf("live", "ready"), pulse = "platform" to "ready", card = liveReady),
                    Beat(36, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = startupProbe),
                    Beat(38, light = setOf("ready"), card = theVerdict),
                ),
            ),
            // 40: "A Service is a stable address in front of your pods..."
            Scene(
                40, "The ready list", endpoints(),
                beats = listOf(
                    Beat(40, show = setOf("svc", "ep", "pod"), light = setOf("ep"), pulse = "svc" to "ep", card = endpointsCard),
                ),
            ),
            // 42: "No, and nothing to miss. Az containerapp revision list..."
            Scene(
                42, "The other home", aca(),
                beats = listOf(
                    Beat(42, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(44, show = setOf("sys"), light = setOf("rev"), card = degraded),
                    Beat(46, light = setOf("sys"), card = wrongPort),
                    Beat(48, show = setOf("con"), light = setOf("sys", "con"), card = twoStreams),
                    Beat(50, light = setOf("sys", "con"), card = logsShow),
                    Beat(52, light = setOf("con"), card = execCode),
                    Beat(54, show = setOf("la"), light = setOf("la"), pulse = "sys" to "la", card = logAnalytics),
                    Beat(56, light = setOf("la"), card = insights),
                ),
            ),
            // 57: "Before I fall asleep on you. What do I keep for the exam?"
            Scene(
                57, "For the exam", exam(),
                beats = listOf(
                    Beat(58, show = setOf("pull", "crash", "ready"), light = setOf("pull", "crash", "ready"), card = phrase),
                    Beat(60, show = setOf("port", "old", "pending"), light = all6, card = theRest),
                    Beat(62, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-container-troubleshoot--couples",
        listOf(
            // 0: "Date night, darling. Wine poured, phone face down..."
            Scene(
                0, "Date night", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), card = BoardCard("Date night", "A Friday release that would not start, picked over together.")),
                    Beat(2, light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps and ingress", "The front door that lets calls from the outside reach your app.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Azure Kubernetes Service: a cluster of our own machines.")),
                ),
            ),
            // 6: "Three questions, in order. Did it start, why did it stop..."
            Scene(
                6, "Three questions, in order", questions(),
                beats = listOf(
                    Beat(6, show = setOf("q1", "q2", "q3"), light = setOf("q1"), card = threeQuestions),
                ),
            ),
            // 7: "Question one. On the cluster, kubectl get pods..."
            Scene(
                7, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(7, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "A status, a ready count and a restart count, one line per pod.")),
                    Beat(8, light = setOf("get"), card = imagePull),
                    Beat(10, light = setOf("get"), card = noLogs),
                    Beat(12, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = describeCode),
                    Beat(14, light = setOf("describe"), card = theEvent),
                    Beat(15, light = setOf("describe"), card = BoardCard("Managed identity", "The badge the app pulls with; it needs the pull role on the registry.")),
                    Beat(16, light = setOf("describe"), card = checkAcr),
                ),
            ),
            // 17: "My turn to ask, then. The tag is fixed, the pods are up..."
            Scene(
                17, "Why did it stop?", aksLadder(),
                beats = listOf(
                    Beat(17, show = setOf("get", "describe", "logs"), light = setOf("logs"), pulse = "describe" to "logs", card = crashLoop),
                    Beat(18, light = setOf("logs"), card = previousCode),
                    Beat(19, light = setOf("logs"), card = whatDied),
                    Beat(20, light = setOf("logs"), card = BoardCard("A missing app setting", "Added on one side and never the other. It looks like broken code.")),
                ),
            ),
            // 22: "The quiet one. Pods Running, no restarts, ready zero of one..."
            Scene(
                22, "Can anything reach it?", probes(),
                beats = listOf(
                    Beat(22, show = setOf("platform"), light = setOf("platform"), card = aProbe),
                    Beat(24, show = setOf("live", "ready", "start"), light = setOf("live", "ready"), pulse = "platform" to "ready", card = BoardCard("Three probes", "Liveness restarts, readiness holds traffic back, startup waits.")),
                    Beat(26, light = setOf("ready"), card = theVerdict),
                ),
            ),
            // 28: "A Service is a stable address in front of your pods..."
            Scene(
                28, "The ready list", endpoints(),
                beats = listOf(
                    Beat(28, show = setOf("svc", "ep", "pod"), light = setOf("ep"), pulse = "svc" to "ep", card = endpointsCard),
                ),
            ),
            // 30: "Az containerapp revision list first..."
            Scene(
                30, "The other home", aca(),
                beats = listOf(
                    Beat(30, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(32, show = setOf("sys"), light = setOf("rev"), card = BoardCard("Quiz: 8080 against 80", "The app listens on 8080; ingress was told the target port is 80.")),
                    Beat(33, light = setOf("sys"), card = BoardCard("Right answer", "Nothing answers, no replica is ready, the revision stays Degraded.")),
                    Beat(34, light = setOf("sys"), card = BoardCard("System logs", "The platform talking: pulls, probes, revisions.")),
                    Beat(36, show = setOf("con"), light = setOf("con"), card = BoardCard("Console logs", "Your own code's output, which was perfectly quiet.")),
                    Beat(38, light = setOf("con"), card = execCode),
                    Beat(40, show = setOf("la"), light = setOf("la"), pulse = "sys" to "la", card = logAnalytics),
                    Beat(42, light = setOf("la"), card = insights),
                ),
            ),
            // 43: "So before pudding. What do we keep for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = setOf("pull", "crash", "ready"), light = setOf("pull", "crash", "ready"), card = phrase),
                    Beat(46, show = setOf("port", "old", "pending"), light = all6, card = theRest),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
