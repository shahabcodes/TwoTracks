package com.shahaabapps.core.content

/** Troubleshooting AKS and Container Apps: logs, events and connectivity: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesContainerTroubleshoot {
    private val classic = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Hi, and welcome back. Today is detective work: finding out why a container won't serve. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend. You ship a new version, and the app goes quiet. Where do you look?"),
            leo("Usually? Everywhere, in a panic. [chuckle] Is there a better way?"),
            maya("There is. First, the words. A container image packs your code, its Python and libraries, plus a slim copy of an operating system's files. A container is one running copy of that image."),
            leo("A running copy. So when it breaks, it's just a program on some machine?"),
            maya("A walled-off one, sharing the machine's kernel, the core of the operating system. That's why its logs are simply what your code prints. The Coach runs its backend on Azure Container Apps, a service that runs containers for you."),
            leo("Just the one place?"),
            maya("Two. Its nightly worker runs on AKS, Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines, and keeps them running, like a restaurant manager keeping enough cooks on shift. The AKS episode covers setting it up."),
            leo("So what's better than panic?"),
            maya("Three questions, in order. Did it start? Why did it stop? Can traffic reach it? Each question has its own place to look. Ask them in that order, and most problems name themselves."),
            leo("Start with AKS, then. Did it start? How do I know?"),
            maya("With kubectl, the command-line tool that talks to Kubernetes, like a remote control you type into. You type kubectl get pods, and it lists your pods."),
            leo("Pods. Remind me what a pod is, in plain words?"),
            maya("The smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers. One pod is one running copy of your app. For each pod, the list shows its status, how many containers are ready, and how often it has restarted."),
            leo("And what does the status tell me?"),
            maya("Which question to ask next. Then kubectl describe pod, with the pod's name, gives you its details, ending with a list called Events. Events are the platform's own diary: found a machine, pulled the image, a health check failed, stopped the container."),
            leo("Okay. What statuses will I actually see?"),
            maya("The big one first. ImagePullBackOff means the machine can't pull your image from the registry, so it's backing off, waiting a while before it tries again."),
            leo("Then I read the logs, to see why?"),
            maya("That's the classic mistake. Logs are what your own code prints, and this container never started, so it has no logs."),
            leo("Then where's the error?"),
            maya("The pull error is a platform event, so kubectl describe pod shows it, word for word. Usually a wrong tag, the image's version label, or no permission to read your private image shelf."),
            leo("Got an example?"),
            maya("For example, the event says version 31 not found, and the team had built version 3.1. The tag in the file was missing a dot. Fix the tag, apply the file again, and the pod is running."),
            leo("[laugh] One missing dot. And the other big status?"),
            maya("CrashLoopBackOff. The container starts, crashes, and Kubernetes keeps restarting it, waiting longer each time. Here your code did run, so this time the logs have the answer."),
            leo("So, kubectl logs."),
            maya("With one flag that matters: previous. Plain kubectl logs shows the container that's starting right now, which may have printed nothing yet. With the previous flag, you see the one that just died, with its stack trace, the error report Python prints when it crashes."),
            leo("Any other statuses?"),
            maya("One more. Pending means no machine has room for the pod yet. Describe shows an event called FailedScheduling, which is Kubernetes for, I couldn't find a place to put it."),
            leo("Now the sneaky one. The pods are running and never restart, but callers get nothing. Ready shows zero of one."),
            maya("That's a probe. A probe is a check the platform runs against your container, like a nurse checking a pulse. There are three kinds, and the exam loves the difference."),
            leo("Go on, nurse. Number one?"),
            maya("A liveness probe asks, are you alive? If it fails, the container is stuck, like a frozen screen, so the platform restarts it."),
            leo("Number two?"),
            maya("A readiness probe asks, are you ready for customers? If it fails, the container keeps running but gets no traffic, like a shop with the lights on and the door locked."),
            leo("And three?"),
            maya("A startup probe gives a slow starter time to wake up, before the other two begin checking. Like not taking someone's pulse while they're still getting out of bed."),
            leo("So running, never restarting, zero of one ready..."),
            maya("Is a failing readiness probe. A failing liveness probe would restart it, and these never restart. Usually the probe has the wrong path or the wrong port. A port is the numbered door your app listens at. Point the probe at the wrong door, and nobody answers."),
            leo("What should the probes check, then?"),
            maya("Cheap things. The Coach has two tiny web paths: healthz, which just says I'm alive, and ready, which says whether its AI model has loaded. A classic mistake: a liveness probe that calls the database. Then one slow database restarts every pod at once."),
            leo("Ouch. Now Container Apps. Same three questions?"),
            maya("Same questions, different tools. In Container Apps, each deploy makes a revision, a saved version of your app's image and settings, like a numbered draft. You list them with az, the Azure command-line tool."),
            leo("And what am I looking for in that list?"),
            maya("Health. A good revision is Running. A sick one says Degraded, with zero of one replicas ready. A replica is one running copy of your app, like a photocopy. That usually means a probe or the port is wrong."),
            leo("And the logs, in Container Apps?"),
            maya("Two kinds. System logs come from the platform: image pulls, probe results, revision events. Console logs are your own app's output, whatever your code prints. You can stream either one live."),
            leo("Live is nice. What about yesterday's errors?"),
            maya("The log stream is like live radio: once it's gone, it's gone. For history, you use Log Analytics, Azure's filing room for logs. You search it with KQL, which is its query language: a way to ask, show me yesterday's errors. The KQL episode teaches the queries."),
            leo("And on AKS? Does history just appear there?"),
            maya("Only if you switch on Container insights. That's Azure Monitor collecting your container logs and Kubernetes events into Log Analytics, so they're kept."),
            leo("And the third question? Can traffic reach it?"),
            maya("In Container Apps, check the ingress. Ingress is the front door that lets traffic in from outside. It hands each caller to a target port, the numbered door inside where your app should be listening."),
            leo("And how does that go wrong?"),
            maya("The exam's favourite: the app listens on 8000, but ingress targets 80. The default probes knock on 80 too, nobody answers, and the revision goes Degraded."),
            leo("And on AKS?"),
            maya("kubectl get endpoints, with your Service's name. A Service is the stable address in front of your pods, like a shop's phone number. Its endpoints are the list of ready pods behind it, the cashiers on shift right now. Empty means no ready pod matches."),
            leo("And if I want to poke around myself?"),
            maya("In both worlds you can open a console, a command line inside the running container, and test from where the app sits. Calling localhost there means the container calling itself."),
            leo("So what do I remember for the exam?"),
            maya("If the question says ImagePullBackOff, the answer is kubectl describe pod, not logs. If it says CrashLoopBackOff, kubectl logs with previous. If pods run, never restart, and show zero of one ready, the readiness probe."),
            leo("And on Container Apps?"),
            maya("If a revision is Degraded and the ports differ, fix the target port. And for yesterday's errors, Log Analytics, not the log stream."),
            leo("And anything to try today?"),
            maya("Deploy a revision with a wrong target port, and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port, and confirm the revision becomes healthy. Then open a console with az containerapp exec, and call the app on localhost."),
            leo("Get, describe, logs with previous. My new panic plan. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Welcome to Case Files. Today's case: The Friday Release. A small Android app called Plant Pal tells you what's wrong with your houseplants. Its Python backend runs in containers on Azure. On Friday afternoon the team shipped a new version of everything. By Friday evening, the app answered nobody."),
            leo("[chuckle] Friday releases. The oldest crime there is. Detective Leo, on the case. Where does the backend live?"),
            maya("In two places. But first, the evidence itself. A container image packs the app's code, Python, libraries and a slim copy of an operating system's files. A container is one running copy of it. The main backend runs on Azure Container Apps, a service that runs containers for you."),
            leo("So a running container is basically a program on a server?"),
            maya("A walled-off program, sharing the server's kernel, the core of its operating system. Remember that, Detective: its logs are only what the code prints."),
            leo("And the second place?"),
            maya("The photo worker runs on AKS, Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running, like a restaurant manager keeping enough cooks on shift."),
            leo("Two crime scenes. Which first?"),
            maya("The worker on AKS. We ask three questions, in order. Did it start? Why did it stop? Can traffic reach it? First question: kubectl get pods. kubectl is the command-line tool for Kubernetes, like a remote control you type into."),
            leo("And a pod is what, exactly?"),
            maya("The smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers. One pod is one running copy of the worker."),
            leo("And what did it show?"),
            maya("Status: ImagePullBackOff. That means the machine can't pull the image down from the registry, so it's backing off, waiting longer between tries."),
            leo("Easy. First suspect: the code. I'll read the logs."),
            maya("[pause] Wrong place. Logs are what the code prints, and this container never started. Empty logs, every time. The pull error is a platform event. kubectl describe pod ends with a list called Events, the platform's own diary. And the diary said: version 31 not found."),
            leo("And the team had built..."),
            maya("Version 3.1. A missing dot in the tag, the image's version label. Under the picture, events are short-lived records Kubernetes writes as it works, kept only about an hour by default, so read them early."),
            leo("And if the tag had been right?"),
            maya("Then the next suspect is permission to read the private image shelf. The build pushed fine, but the machines may not be allowed to pull. The AKS episode covers that fix."),
            leo("Dot fixed. Case closed?"),
            maya("The pods started, and then the second clue. Status: CrashLoopBackOff. The container starts, crashes, and Kubernetes restarts it, waiting longer each time. How it works: the wait roughly doubles after each crash, up to about five minutes."),
            leo("This time the code ran, so I read the logs. Nothing there."),
            maya("Because plain kubectl logs shows the container that's starting right now, which hasn't printed anything yet. You want the one that just died. kubectl logs with the previous flag. And there was the stack trace, the error report Python prints when it crashes."),
            leo("What did it say?"),
            maya("The database address setting was missing. The new version expected it, and nobody had added it to the list of plain settings handed to the pods. They added it, applied, and the worker came up. Events tell you about the platform. Logs tell you about your code."),
            leo("Now the main backend. Container Apps."),
            maya("Same three questions, different tools. Each deploy there makes a revision, a saved version of the app's image and settings, like a numbered draft. They listed them with az, the Azure command-line tool."),
            leo("And what did the list say?"),
            maya("The new revision said Degraded, zero of one replicas ready. A replica is just one running copy of the app, like a photocopy. So not one copy was ready."),
            leo("Suspect: the image is missing from the registry."),
            maya("Then it would fail at the pull, and the system logs would say so. System logs are the platform's messages: pulls, health checks, revisions. Console logs are what the app prints. The system log said something else: the probe, a health check the platform runs, got no answer on port 80."),
            leo("Port 80. And what does the app listen on?"),
            maya("8000. A port is the numbered door an app listens at. Plant Pal was listening at door 8000."),
            leo("So who sent everyone to door 80?"),
            maya("The ingress. Ingress is the front door that lets traffic in from outside, and it hands callers to a target port. Theirs was set to 80. The default probes knock on the target port too. Nobody answers at 80, so no replica is ever ready."),
            leo("Hang on. What's a probe, exactly?"),
            maya("A check the platform runs against your container, like a nurse checking a pulse. A liveness probe asks, are you alive? Failing means stuck, so the container is restarted."),
            leo("And the other kinds?"),
            maya("A readiness probe asks, are you ready for customers? Failing means not now: no traffic, but the container keeps running. And a startup probe gives a slow starter time before the other two begin."),
            leo("How could they prove it was the port?"),
            maya("From the inside. az containerapp exec opens a console, a command line inside the running container. They called the app on localhost, the container calling itself, on 8000, and it answered. The app was fine all along. The door was wrong."),
            leo("Fix the target port, then. Case closed?"),
            maya("Port fixed, revision Running. But on Saturday, a third witness. The AKS worker had a second copy behind a Service, the stable address in front of pods, like a shop's phone number. Those pods were running, never restarted, and showed zero of one ready."),
            leo("Suspect: the liveness probe."),
            maya("A failing liveness probe would restart them, and these never restarted. So it's readiness. A Service's endpoints are just its list of ready pods, the cashiers on shift right now. kubectl get endpoints showed none."),
            leo("And the cause?"),
            maya("The readiness probe pointed at the wrong path. And never point a liveness probe at the database, or one slow database restarts every pod."),
            leo("And the boss wants to know what broke on Friday. Can we scroll back the log stream?"),
            maya("No. The log stream is live, like radio. History lives in Log Analytics, Azure's filing room for logs. You search it with KQL, which is its query language; the KQL episode teaches it."),
            leo("And the AKS worker's history?"),
            maya("Switch on Container insights. That's Azure Monitor collecting container logs and Kubernetes events into Log Analytics, so Friday is still there on Monday."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If the question says ImagePullBackOff, the answer is kubectl describe pod, not logs. If CrashLoopBackOff, kubectl logs with previous. If pods run, never restart, and show zero of one ready, the readiness probe."),
            leo("And the Friday revision?"),
            maya("If a revision is Degraded and the ports differ, fix the target port. And for yesterday's errors, Log Analytics."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Deploy a revision with a wrong target port, and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, and call the app on localhost."),
            leo("No more Friday releases. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Okay, did you hear about on-call week on the Coach team? Grab a coffee. Every day, something in the Two Tracks Coach broke, the study assistant in the Android app with the Python backend. And every day, someone looked in exactly the wrong place."),
            leo("[laugh] I heard there's a new rule on the wall. Where does the Coach even run?"),
            maya("In containers. A container image packs the code, Python, libraries and a slim copy of an operating system's files, and a container is one running copy of it. The main backend runs on Azure Container Apps, a service that runs containers for you, no machines to manage."),
            leo("And when one breaks, what am I actually looking at?"),
            maya("A walled-off program on a shared kernel, the core of the machine's operating system. Which is why its logs are just whatever the code prints. Remember that for Monday."),
            leo("And the rest?"),
            maya("The nightly worker runs on AKS, Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines and keeps them running, like a restaurant manager keeping enough cooks on shift."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. The worker's pods said ImagePullBackOff. A pod is the smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers."),
            leo("And ImagePullBackOff, in plain words?"),
            maya("The machine can't pull the image down from the registry, so it's backing off, waiting longer between tries."),
            leo("And Tom did what?"),
            maya("Read the logs. With kubectl, the command-line tool for Kubernetes, like a remote control you type into. For an hour. And they were empty."),
            leo("Why empty?"),
            maya("Logs are what your code prints, and the container never started. The pull error is a platform event. kubectl describe pod ends with a list called Events, the platform's own diary, and it said, version 31 not found. Someone had dropped a dot from version 3.1."),
            leo("So what was the actual fix?"),
            maya("The tag, the image's version label. Put the dot back, apply the file again, done. If the tag had been right, the next suspect is permission to read the team's private image shelf. The AKS episode covers that one."),
            leo("[sigh] Poor Tom. And Tuesday?"),
            maya("Tuesday was Dana. The worker came up and fell over, again and again. Status: CrashLoopBackOff. The container starts, crashes, and Kubernetes restarts it, waiting longer each time. Under the hood, the wait roughly doubles, up to about five minutes."),
            leo("So this time the logs help."),
            maya("They do, if you ask the right container. Dana ran plain kubectl logs, which shows the one starting right now. It hadn't printed anything yet."),
            leo("So which one should she have asked?"),
            maya("The one that just died. The previous flag shows it, with its stack trace, Python's crash report. Dana found it after lunch: a missing setting."),
            leo("I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. Marcus had read about probes. A probe is a check the platform runs against your container, like a nurse checking a pulse."),
            leo("That sounds sensible. What went wrong?"),
            maya("A liveness probe asks, are you alive? Fail it, and the container is restarted. Marcus made his liveness probe call the database, to be thorough. On Wednesday the database was slow for a minute. Every probe failed, and every single pod restarted at once."),
            leo("Oof. So what should liveness check?"),
            maya("Something cheap, like a tiny healthz path that just says, I'm alive. Then there's the readiness probe, which asks, are you ready for customers? Fail it, and the container keeps running, but gets no traffic, like a shop with the lights on and the door locked."),
            leo("Is there a third nurse?"),
            maya("The startup probe. It gives a slow starter time to wake up before the other two begin checking. No pulse-taking while someone's still getting out of bed."),
            leo("And how does readiness actually keep the traffic away?"),
            maya("Under the hood, a Service, the stable address in front of your pods, like a shop's phone number, keeps a list of the ready ones, called its endpoints: the cashiers on shift. Fail readiness, and you're taken off the list. Pass again, and you're back on."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. They deployed a new revision of the Container Apps backend. A revision is a saved version of the app's image and settings, like a numbered draft."),
            leo("And how did it go?"),
            maya("It went Degraded, zero of one replicas ready. A replica is just one running copy, like a photocopy. Not one copy was ready."),
            leo("And the culprit?"),
            maya("The ingress, the front door that lets traffic in from outside. It hands callers to a target port, the numbered door where the app should be listening. Theirs was 80. The app listens on 8000. The default probes knocked on 80, nobody answered, so no replica was ever ready."),
            leo("How did they find it?"),
            maya("The system logs. In Container Apps, system logs are the platform's messages: image pulls, probe results, revisions. Console logs are what your app prints. They streamed the system logs with az, the Azure command-line tool, and there it was: probe failed on port 80."),
            leo("Could they have tested it from inside?"),
            maya("Yes. az containerapp exec opens a console, a command line inside the running container. From there you call the app on localhost, which means the container calling itself. On 8000 it answered fine. The app was healthy; the door was wrong."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. The boss asked what broke on Tuesday night. Priya opened the log stream and scrolled. And scrolled. Nothing from Tuesday."),
            leo("Because the stream is live."),
            maya("Exactly. It's like live radio. History lives in Log Analytics, Azure's filing room for logs. You search it with KQL, which is its query language. The KQL episode is Priya's weekend reading."),
            leo("And the AKS worker's history?"),
            maya("That needs Container insights switched on: Azure Monitor collecting container logs and Kubernetes events into Log Analytics. And events fade on their own: by default they're kept only about an hour."),
            leo("Did anything go right this week?"),
            maya("One thing. Running pods that showed zero of one ready, and never restarted. The new hire said, that's the readiness probe, and kubectl get endpoints, the list of ready pods behind a Service, was empty. Wrong path in the probe. Fixed in five minutes. Everyone's still annoyed."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. If the question says ImagePullBackOff, kubectl describe pod, not logs. If CrashLoopBackOff, kubectl logs with previous. If pods run, never restart, and show zero of one ready, the readiness probe."),
            leo("And Thursday and Friday?"),
            maya("If a revision is Degraded and the ports differ, fix the target port. And for yesterday's errors, Log Analytics."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Deploy a revision with a wrong target port, and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, and call the app on localhost."),
            leo("Get, describe, logs with previous. It's going on my wall too. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-container-troubleshoot",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about the other reason people are awake at this hour. Something's broken, and nobody knows why."),
            ivy("Mm. The three in the morning phone call. Tell me there's a gentler way, Nova."),
            nova("There is. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend. Its image packs the code, Python, libraries and a slim copy of an operating system's files. Each running copy is a container."),
            ivy("And at three in the morning, what is a container, really?"),
            nova("[pause] Just a walled-off program, sharing the machine's kernel, the core of its operating system. Its logs are only what your code says out loud. The backend lives on Azure Container Apps, a service that runs containers for you, no machines to manage."),
            ivy("And anywhere else?"),
            nova("A nightly worker, on AKS, which is Azure Kubernetes Service. Kubernetes runs lots of containers across lots of machines, and keeps them running, like a night manager keeping staff on shift. [pause] Tonight, the worker isn't working."),
            ivy("So where do I start? Gently."),
            nova("With three questions, slowly, in order. Did it start? Why did it stop? Can traffic reach it? Each one has its own place to look."),
            ivy("Did it start, then."),
            nova("You ask with kubectl, the command-line tool for Kubernetes, like a remote control you type into. kubectl get pods lists your pods."),
            ivy("Pods. Say it slowly for me."),
            nova("A pod is the smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers. [pause] The list shows each pod's status, how many are ready, and how often they've restarted."),
            ivy("And if the status is something I don't recognise?"),
            nova("Then kubectl describe pod. At the bottom is a list called Events, [pause] the platform's own diary. It found a machine, it pulled the image, a health check failed. Under the picture, events are short-lived records Kubernetes keeps for about an hour by default. Read them while they're fresh."),
            ivy("Tell me the statuses that keep people up."),
            nova("ImagePullBackOff. The machine can't pull your image down from the registry, so it's backing off between tries. And here's the trap. People read the logs."),
            ivy("And the logs are empty."),
            nova("Empty. Logs are what your code prints, and this container never started. The answer is in the events, in describe. A tag, the image's version label, typed wrong. Or no permission to read the private image shelf; the AKS episode covers that."),
            ivy("And the other one? I can hear another one."),
            nova("CrashLoopBackOff. The container starts, crashes, and Kubernetes keeps restarting it. Each time it waits a little longer, [pause] doubling, up to about five minutes."),
            ivy("And this time the code ran. So logs."),
            nova("Logs, but the right ones. Plain kubectl logs shows the container that's just waking up, which hasn't said a word. kubectl logs with the previous flag shows the one that just died, with its stack trace, Python's crash report. [pause] That's where the truth is."),
            ivy("Any others that keep people waiting?"),
            nova("Pending. No machine has room for the pod yet, so it waits in the hall. Describe shows an event called FailedScheduling, which is Kubernetes for, I couldn't find you a seat."),
            ivy("And if they're running, and quiet, and nobody can reach them?"),
            nova("Then it's a probe. A probe is a check the platform runs on your container, like a nurse checking a pulse in the night. A liveness probe asks, are you alive? Fail it, and the container is restarted."),
            ivy("And the others?"),
            nova("A readiness probe asks, are you ready for company? Fail it, and the container keeps running, but no traffic comes, [pause] like a house with the lights on and the door locked."),
            ivy("And a third, I suspect."),
            nova("A startup probe gives a slow riser time before the others begin. So running, never restarting, zero of one ready: [pause] readiness."),
            ivy("Ready for company. You make health checks sound personal."),
            nova("They are. And where it stops working: a liveness probe that calls the database. One slow database, and every pod is restarted at once. Keep liveness cheap, like a tiny healthz path that just says, I'm here."),
            ivy("And how does readiness keep the callers away, really?"),
            nova("A Service is the stable address in front of your pods, like one phone number for the whole house. It keeps a list of the ready ones, called its endpoints."),
            ivy("And if a pod isn't ready?"),
            nova("It's quietly taken off the list. [pause] kubectl get endpoints shows it. Empty means nobody's ready to answer."),
            ivy("Now the backend. Container Apps. Same questions?"),
            nova("Same questions, softer tools. Each deploy makes a revision, a saved version of the app's image and settings, like a numbered draft of a letter. You list them with az, the Azure command-line tool."),
            ivy("And a sick one looks like what?"),
            nova("It says Degraded, zero of one replicas ready. A replica is just one running copy, like a photocopy. [pause] Zero ready means not one copy is answering."),
            ivy("And the logs there?"),
            nova("Two voices. System logs are the platform talking: image pulls, probe results, revisions. Console logs are your app talking, whatever it prints. You can stream either, live, [pause] like late-night radio."),
            ivy("And when the show is over? Last night's errors?"),
            nova("Then Log Analytics, Azure's filing room for logs. You search it with KQL, which is its query language. The KQL episode is a good companion for another night."),
            ivy("And the worker on AKS? Does it keep a diary too?"),
            nova("Only if you switch on Container insights. That's Azure Monitor collecting your container logs and events into Log Analytics, so they're kept."),
            ivy("And the third question. Can traffic reach it?"),
            nova("In Container Apps, look at the ingress. Ingress is the front door that lets traffic in from outside. It hands each caller to a target port, the numbered door inside where your app should be listening."),
            ivy("And if the doors don't line up?"),
            nova("If the app listens on 8000 and ingress targets 80, the default probes knock on 80, [pause] nobody answers, and the revision goes Degraded."),
            ivy("And if I want to look around inside, myself?"),
            nova("Then az containerapp exec opens a console, a command line inside the running container. Call the app on localhost, the container calling itself. If it answers there, [pause] the problem is the door, not the app."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. ImagePullBackOff: kubectl describe pod, not logs. CrashLoopBackOff: kubectl logs with previous. Running, never restarting, zero of one ready: the readiness probe. A Degraded revision with different ports: fix the target port. And yesterday's errors: Log Analytics, not the stream."),
            ivy("And something to try today, before I sleep?"),
            nova("Deploy a revision with a wrong target port, and watch it go Degraded. Stream the system logs and find the probe failure. [pause] Fix the port, and confirm the revision becomes healthy. Then open a console with az containerapp exec, and call the app on localhost."),
            ivy("Get, describe, and the previous one. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-container-troubleshoot",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about what to do when things stop working."),
            rafe("On a date? Bold. Is this about us?"),
            nova("[laugh] It's about containers. A container image packs your code, Python, libraries and a slim copy of an operating system's files. The Two Tracks Coach, the study assistant in the Android app, runs its Python backend that way, on Azure Container Apps, a service that runs containers for you."),
            rafe("Quiz for you, then. A running container: is it a little virtual machine with its own kernel?"),
            nova("No. It's one running copy of the image, a walled-off program sharing the host's kernel, the core of the operating system. So its logs are just what the code prints."),
            rafe("Right answer. Nothing to hide. [kiss]"),
            nova("Not from you. And the rest of the Coach?"),
            rafe("And the nightly worker runs on AKS, which is Azure Kubernetes Service. Kubernetes keeps lots of containers running on lots of machines, like a manager keeping cooks on shift. I do listen, you know."),
            nova("You do. So, three questions, in order, when something breaks. Did it start? Why did it stop? Can traffic reach it?"),
            rafe("Did it start. On AKS, I'd ask kubectl, the command-line tool for Kubernetes, like a remote control you type into. kubectl get pods."),
            nova("And a pod, my love, is what?"),
            rafe("The smallest thing Kubernetes runs. Picture a pea pod: a little wrapper round one or more containers. Two peas in a pod, like us."),
            nova("And the list shows each pod's status, how many are ready, and how often they've restarted. Then kubectl describe pod gives the details, ending with Events, the platform's own diary."),
            rafe("The platform's diary. Do I get to read yours?"),
            nova("Only the events. Under the picture, they're short-lived records Kubernetes writes as it works, kept about an hour by default. So read them while they're warm."),
            rafe("Then let me test you, darling. Exam style. A pod shows ImagePullBackOff. Which command shows you the exact error?"),
            nova("ImagePullBackOff means the machine can't pull the image down from the registry, so it waits between tries. The container never started, so it has no logs. kubectl describe pod. The pull error is in the Events."),
            rafe("Right answer. Some things you only learn from the diary. [kiss]"),
            nova("Mm. My turn. A pod is in CrashLoopBackOff. It starts, crashes, and Kubernetes keeps restarting it, waiting longer each time. Where do you look?"),
            rafe("kubectl logs. Logs are what the code prints."),
            nova("No kiss for that one. Plain logs show the container that's just starting, which hasn't said anything yet. You need the previous flag, for the one that just died."),
            rafe("So you do want me to bring up the previous one."),
            nova("[chuckle] Only for its stack trace, Python's crash report. That's where the reason is. Under the hood, the wait between restarts roughly doubles, up to about five minutes."),
            rafe("Fine. Let me win one back. What's a probe?"),
            nova("A check the platform runs against your container, like a nurse checking your pulse. A liveness probe asks, are you alive? Fail it, and you're restarted."),
            rafe("And the one that asks if I'm ready?"),
            nova("The readiness probe. Fail it, and you keep running, but get no traffic, like a house with the lights on and the door locked."),
            rafe("Are you ready? I always am."),
            nova("Then here's your question. The pods are running, never restart, and show zero of one ready. Callers get nothing. What's failing?"),
            rafe("The readiness probe. A failing liveness probe would restart them, and these never restart. Running, but not taking visitors."),
            nova("Right answer. Ready when you are. [kiss]"),
            rafe("And a startup probe gives a slow starter time first, before the other two check. Now, how does readiness keep the callers away?"),
            nova("A Service is the stable address in front of the pods, like one phone number for the house. It keeps a list of the ready pods, called its endpoints. Fail readiness, and you're taken off the list, so no calls reach you."),
            rafe("Off the list. Harsh. And where does it all stop working?"),
            nova("When someone points a liveness probe at the database. One slow database, and every pod restarts at once. Keep liveness light, like a tiny healthz path that just says, I'm alive. Some checks shouldn't be so demanding."),
            rafe("One more for you. A pod just sits there, Pending. What's going on?"),
            nova("No machine has room for it yet. Describe shows an event called FailedScheduling, Kubernetes for, I couldn't find it a seat. Some pods just can't get a table."),
            rafe("Noted. Now Container Apps. Each deploy makes a revision, a saved version of the app's image and settings, like a numbered draft. You list them with az, the Azure command-line tool."),
            nova("And a sick revision says Degraded, zero of one replicas ready. A replica is just one running copy, like a photocopy. So, darling. The app listens on port 8000, but ingress targets 80. What's wrong?"),
            rafe("Ingress is the front door that lets traffic in. It hands callers to the target port, the numbered door where the app should listen. The default probes knock on 80, and nobody's home. Fix the target port to 8000."),
            nova("Right answer. Always knock on the right door. [kiss]"),
            rafe("And how would you have spotted it, darling?"),
            nova("The system logs, the platform's messages: pulls, probes, revisions. The console logs are what the app itself prints. I'd stream the system logs, and see the probe failing on 80."),
            rafe("And if I want to see for myself, from the inside?"),
            nova("az containerapp exec opens a console, a command line inside the running container. Call the app on localhost, the container calling itself. If it answers there, the trouble is at the door, not inside."),
            rafe("My turn to ask. The boss wants last Tuesday's errors. The log stream shows nothing. Where do you look?"),
            nova("Log Analytics, Azure's filing room for logs. The log stream is live, like a phone call; Log Analytics keeps the history. You search it with KQL, which is its query language, and the KQL episode teaches it."),
            rafe("And on AKS?"),
            nova("Switch on Container insights. That's Azure Monitor collecting container logs and events into Log Analytics, so last Tuesday is still there."),
            rafe("Right answer. Some of us remember every Tuesday. [kiss]"),
            nova("Last question, for the exam. What do we remember?"),
            rafe("ImagePullBackOff: kubectl describe pod, not logs. CrashLoopBackOff: kubectl logs with previous. Running, never restarting, zero of one ready: readiness probe. A Degraded revision with mismatched ports: fix the target port. Yesterday's errors: Log Analytics, not the stream."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Deploy a revision with a wrong target port, and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port and confirm the revision becomes healthy. Then open a console with az containerapp exec, and call the app on localhost."),
            rafe("Alive, ready, and right at the door. Perfect night. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun places() = DiagramSpec(
        "The Coach's backend runs on Container Apps and its nightly worker on AKS: two places to look when it goes quiet.",
        nodes = listOf(
            DNode("phone", "Phone", 0.15f, 0.5f, w = 0.24f, h = 0.2f, kind = NodeKind.USER),
            DNode("aca", "Container Apps", 0.65f, 0.2f, w = 0.5f, h = 0.22f, tone = 1, sub = "the backend"),
            DNode("aks", "AKS", 0.65f, 0.78f, w = 0.5f, h = 0.22f, sub = "the nightly worker"),
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

    private fun statuses() = DiagramSpec(
        "Each pod status points to its first move: describe for a pull, previous logs for a crash, the probe for not ready.",
        nodes = listOf(
            DNode("pull", "ImagePullBackOff", 0.25f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "read describe"),
            DNode("crash", "CrashLoopBackOff", 0.75f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "logs, previous"),
            DNode("pod", "Pod", 0.5f, 0.5f, w = 0.3f, h = 0.18f, tone = 1),
            DNode("pending", "Pending", 0.25f, 0.86f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "FailedScheduling"),
            DNode("ready", "Running, 0/1 ready", 0.75f, 0.86f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "readiness probe"),
        ),
        edges = listOf(
            DEdge("pod", "pull"),
            DEdge("pod", "crash"),
            DEdge("pod", "pending"),
            DEdge("pod", "ready"),
        ),
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
        "Ingress passes callers to its target port; if the app listens on 8000 and ingress targets 80, nobody answers.",
        nodes = listOf(
            DNode("caller", "Caller", 0.15f, 0.2f, w = 0.24f, h = 0.2f, kind = NodeKind.USER),
            DNode("ing", "Ingress", 0.65f, 0.2f, w = 0.5f, h = 0.2f, tone = 1, sub = "target port 80"),
            DNode("app", "App", 0.65f, 0.78f, w = 0.5f, h = 0.22f, sub = "listens on 8000"),
            DNode("con", "Console", 0.2f, 0.78f, w = 0.34f, h = 0.22f, kind = NodeKind.NOTE, sub = "exec, localhost"),
        ),
        edges = listOf(
            DEdge("caller", "ing"),
            DEdge("ing", "app", "80, not 8000", dashed = true),
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
    private val describeCode = BoardCard("Shell", "kubectl describe pod coach-api-7d9f8c6b5-x2kqp", code = true)
    private val previousCode = BoardCard("Shell", "kubectl logs coach-api-7d9f8c6b5-x2kqp --previous", code = true)
    private val revisionCode = BoardCard("Shell", "az containerapp revision list -n coach-api -g tt-rg -o table", code = true)
    private val execCode = BoardCard("Shell", "az containerapp exec -n coach-api -g tt-rg --command /bin/sh", code = true)
    private val today = BoardCard("Today", "A wrong target port, the system logs, fix it, then exec and call localhost.")

    private fun classicBoard() = PodcastBoard(
        "ai-container-troubleshoot",
        listOf(
            // 0: "Hi, and welcome back. Today is detective work: finding out why a conta..."
            Scene(
                0, "Why won't it serve?", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), light = setOf("phone"), card = BoardCard("The app goes quiet", "A new version ships, and nothing answers. Where do you look?")),
                    Beat(2, card = BoardCard("Image and container", "Code, Python, libraries, slim OS files. A container: one running copy.")),
                    Beat(4, show = setOf("aca"), light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps", "Runs your containers for you: walled-off processes on a shared kernel.")),
                    Beat(6, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Kubernetes: many containers on many machines, kept running.")),
                ),
            ),
            // 7: "So what's better than panic?"
            Scene(
                7, "Three questions", questions(),
                beats = listOf(
                    Beat(8, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("Ask them in order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 9: "Start with AKS, then. Did it start? How do I know?"
            Scene(
                9, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(10, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "Each pod's status, ready count and restarts.")),
                    Beat(12, card = BoardCard("Pod", "Like a pea pod: a wrapper round containers. One running copy of your app.")),
                    Beat(14, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("Events", "The platform's own diary, at the end of kubectl describe pod.")),
                ),
            ),
            // 15: "Okay. What statuses will I actually see?"
            Scene(
                15, "The statuses", statuses(),
                beats = listOf(
                    Beat(16, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image; waiting longer between tries.")),
                    Beat(18, card = describeCode),
                    Beat(22, card = BoardCard("One missing dot", "The event said version 31 not found; the team built 3.1.")),
                    Beat(24, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts, waiting longer each time.")),
                    Beat(26, card = previousCode),
                    Beat(28, show = setOf("pending"), light = setOf("pending"), pulse = "pod" to "pending", card = BoardCard("Pending", "No machine has room yet. Describe shows FailedScheduling.")),
                    Beat(29, show = setOf("ready"), light = setOf("ready"), pulse = "pod" to "ready", card = BoardCard("Running, 0/1 ready", "Never restarts, gets no traffic. Something is checking it.")),
                ),
            ),
            // 30: "That's a probe. A probe is a check the platform runs against your cont..."
            Scene(
                30, "Probes", probes(),
                beats = listOf(
                    Beat(30, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Probe", "A check the platform runs on your container, like a nurse.")),
                    Beat(32, show = setOf("live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness", "Are you alive? Failing means stuck, like a frozen screen: restart it.")),
                    Beat(34, show = setOf("ready"), light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness", "Ready for customers? If not: still running, no traffic. Door locked.")),
                    Beat(36, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = BoardCard("Startup", "Gives a slow starter time before the other two begin checking.")),
                    Beat(38, light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness, failing", "Usually the wrong path or port: the wrong numbered door.")),
                    Beat(40, light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Keep liveness cheap", "A tiny healthz path. Never the database, or all pods restart.")),
                ),
            ),
            // 41: "Ouch. Now Container Apps. Same three questions?"
            Scene(
                41, "Container Apps", aca(),
                beats = listOf(
                    Beat(42, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(44, card = BoardCard("Degraded, 0/1 ready", "A sick revision. A replica is one running copy of the app.")),
                    Beat(46, show = setOf("sys", "con"), light = setOf("sys", "con"), pulse = "rev" to "sys", card = BoardCard("System and console logs", "The platform's messages, and what your own code prints.")),
                    Beat(48, show = setOf("la"), light = setOf("la"), pulse = "sys" to "la", card = BoardCard("Log Analytics", "The stream is live radio; history is queried here with KQL.")),
                    Beat(50, card = BoardCard("Container insights", "Azure Monitor keeping AKS container logs and events in Log Analytics.")),
                ),
            ),
            // 51: "And the third question? Can traffic reach it?"
            Scene(
                51, "Can traffic reach it?", ingress(),
                beats = listOf(
                    Beat(52, show = setOf("caller", "ing"), light = setOf("ing"), pulse = "caller" to "ing", card = BoardCard("Ingress", "The front door. It hands callers to a target port, a numbered door.")),
                    Beat(54, show = setOf("app"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("The target port", "App on 8000, ingress on 80: the probes knock, nobody answers.")),
                ),
            ),
            // 55: "And on AKS?"
            Scene(
                55, "On AKS", endpoints(),
                beats = listOf(
                    Beat(56, light = setOf("ep"), pulse = "svc" to "ep", card = BoardCard("kubectl get endpoints", "The ready pods behind a Service. Empty: no ready pod matches.")),
                    Beat(58, card = BoardCard("Console", "A command line inside the running container; localhost is itself.")),
                ),
            ),
            // 59: "So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: describe, previous, readiness.")),
                    Beat(64, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-container-troubleshoot--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Friday Release. A small Andro..."
            Scene(
                0, "The Friday Release", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca", "aks"), light = setOf("phone"), card = BoardCard("The case", "Plant Pal shipped everything on Friday. By evening, nobody answered.")),
                    Beat(2, light = setOf("aca"), card = BoardCard("The evidence", "An image: code, Python, libraries, slim OS files. Running: a container.")),
                    Beat(4, light = setOf("aca"), card = BoardCard("A walled-off program", "It shares the host's kernel. Its logs are only what the code prints.")),
                    Beat(6, light = setOf("aca", "aks"), card = BoardCard("Two crime scenes", "The backend on Container Apps; the photo worker on AKS.")),
                ),
            ),
            // 7: "Two crime scenes. Which first?"
            Scene(
                7, "Three questions", questions(),
                beats = listOf(
                    Beat(8, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("Ask them in order", "Did it start? Why did it stop? Can traffic reach it?")),
                    Beat(10, card = BoardCard("Pod", "Like a pea pod: a wrapper round containers. One copy of the worker.")),
                ),
            ),
            // 11: "And what did it show?"
            Scene(
                11, "Clue one: the worker", statuses(),
                beats = listOf(
                    Beat(12, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image; backing off between tries.")),
                    Beat(13, card = BoardCard("Suspect: the code", "Wrong place. The container never started, so its logs are empty.")),
                    Beat(14, card = BoardCard("The Events diary", "kubectl describe pod said: version 31 not found.")),
                    Beat(16, card = BoardCard("A missing dot", "The team built 3.1. Events are kept about an hour: read them early.")),
                    Beat(20, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts; the wait doubles, up to about five minutes.")),
                    Beat(22, card = previousCode),
                    Beat(24, card = BoardCard("A missing setting", "Added to the settings. Events: the platform. Logs: your code.")),
                ),
            ),
            // 25: "Now the main backend. Container Apps."
            Scene(
                25, "Clue two: the backend", aca(),
                beats = listOf(
                    Beat(26, show = setOf("rev"), light = setOf("rev"), card = BoardCard("Revision", "A saved version of the app's image and settings, like a numbered draft.")),
                    Beat(28, card = BoardCard("Degraded, 0/1 ready", "A replica is one running copy, like a photocopy. None was ready.")),
                    Beat(29, card = BoardCard("Suspect: a missing image", "It would fail at the pull, and the system logs would say so.")),
                    Beat(30, show = setOf("sys"), light = setOf("sys"), pulse = "rev" to "sys", card = BoardCard("System logs", "The platform's messages said: the probe got no answer on port 80.")),
                ),
            ),
            // 31: "Port 80. And what does the app listen on?"
            Scene(
                31, "The wrong door", ingress(),
                beats = listOf(
                    Beat(32, show = setOf("app"), light = setOf("app"), card = BoardCard("Port", "The numbered door an app listens at. Plant Pal listened at 8000.")),
                    Beat(34, show = setOf("caller", "ing"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("Ingress, target port 80", "The front door sent callers to door 80. Nobody answered there.")),
                ),
            ),
            // 35: "Hang on. What's a probe, exactly?"
            Scene(
                35, "Checking the pulse", probes(),
                beats = listOf(
                    Beat(36, show = setOf("live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness", "Are you alive? Failing means stuck, so the container is restarted.")),
                    Beat(38, show = setOf("ready", "start"), light = setOf("ready", "start"), pulse = "platform" to "ready", card = BoardCard("Readiness and startup", "Readiness: no traffic until ready. Startup: time for a slow starter.")),
                ),
            ),
            // 39: "How could they prove it was the port?"
            Scene(
                39, "Proof from the inside", ingress(),
                beats = listOf(
                    Beat(40, light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                ),
            ),
            // 41: "Fix the target port, then. Case closed?"
            Scene(
                41, "Saturday's witness", endpoints(),
                beats = listOf(
                    Beat(42, light = setOf("pod"), card = BoardCard("Running, 0/1 ready", "Never restarted, and no calls reached them.")),
                    Beat(43, card = BoardCard("Suspect: liveness", "Cleared. A failing liveness probe would have restarted them.")),
                    Beat(44, light = setOf("ep", "pod"), pulse = "ep" to "pod", card = BoardCard("Readiness, wrong path", "Endpoints empty. And never point liveness at the database.")),
                ),
            ),
            // 47: "And the boss wants to know what broke on Friday. Can we scroll back th..."
            Scene(
                47, "Friday's history", aca(),
                beats = listOf(
                    Beat(48, show = setOf("con", "la"), light = setOf("la"), pulse = "con" to "la", card = BoardCard("Log Analytics", "The stream is live radio. History is queried with KQL.")),
                    Beat(50, card = BoardCard("Container insights", "Azure Monitor keeping AKS container logs and events in Log Analytics.")),
                ),
            ),
            // 51: "So, the case summary for the exam?"
            Scene(
                51, "Case closed", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. No more Friday releases.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-container-troubleshoot--gossip",
        listOf(
            // 0: "Okay, did you hear about on-call week on the Coach team? Grab a coffee..."
            Scene(
                0, "On-call week", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), light = setOf("phone"), card = BoardCard("Every day, a break", "And every day, someone looked in exactly the wrong place.")),
                    Beat(2, light = setOf("aca"), card = BoardCard("Image and container", "Code, Python, libraries, slim OS files. A container: one running copy.")),
                    Beat(4, light = setOf("aca"), card = BoardCard("A walled-off program", "On a shared kernel. Its logs are just whatever the code prints.")),
                    Beat(6, show = setOf("aks"), light = setOf("aca", "aks"), card = BoardCard("Where the Coach runs", "The backend on Container Apps; the nightly worker on AKS.")),
                ),
            ),
            // 7: "Okay. So who went first?"
            Scene(
                7, "Monday: Tom", statuses(),
                beats = listOf(
                    Beat(8, show = setOf("pod"), light = setOf("pod"), card = BoardCard("Pod", "Like a pea pod: a wrapper round one or more containers.")),
                    Beat(10, show = setOf("pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image; waiting longer between tries.")),
                    Beat(12, card = BoardCard("Tom read the logs", "For an hour. They were empty: the container never started.")),
                    Beat(14, card = describeCode),
                    Beat(16, card = BoardCard("Fix the tag", "Version 31 should have been 3.1. Apply the file again.")),
                ),
            ),
            // 17: "Poor Tom. And Tuesday?"
            Scene(
                17, "Tuesday: Dana", statuses(),
                beats = listOf(
                    Beat(18, show = setOf("pod", "crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts; the wait doubles, up to five minutes.")),
                    Beat(20, card = previousCode),
                ),
            ),
            // 23: "I sense a Wednesday."
            Scene(
                23, "Wednesday: Marcus", probes(),
                beats = listOf(
                    Beat(24, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Probe", "A check the platform runs on your container, like a nurse.")),
                    Beat(26, show = setOf("live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness called the database", "A slow minute, every probe failed, every pod restarted.")),
                    Beat(28, show = setOf("ready"), light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness", "Ready for customers? If not: still running, no traffic. Door locked.")),
                    Beat(30, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = BoardCard("Startup", "Gives a slow starter time to wake before the other two check.")),
                ),
            ),
            // 31: "And how does readiness actually keep the traffic away?"
            Scene(
                31, "Off the list", endpoints(),
                beats = listOf(
                    Beat(32, light = setOf("ep", "pod"), pulse = "ep" to "pod", card = BoardCard("Endpoints", "The Service's list of ready pods. Fail readiness, you're off it.")),
                ),
            ),
            // 33: "Thursday. Go."
            Scene(
                33, "Thursday: the intern", ingress(),
                beats = listOf(
                    Beat(34, show = setOf("app"), light = setOf("app"), card = BoardCard("Revision", "A saved version of the app's image and settings, like a numbered draft.")),
                    Beat(36, card = BoardCard("Degraded, 0/1 ready", "A replica is one running copy, like a photocopy. None was ready.")),
                    Beat(38, show = setOf("caller", "ing"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("Ingress, target port 80", "The front door hands callers to door 80. The app listens on 8000.")),
                    Beat(40, light = setOf("ing"), card = BoardCard("System logs", "The platform's messages said: probe failed on port 80.")),
                    Beat(42, show = setOf("con"), light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                ),
            ),
            // 43: "And Friday? There's always a Friday."
            Scene(
                43, "Friday: Priya", aca(),
                beats = listOf(
                    Beat(44, show = setOf("con"), light = setOf("con"), card = BoardCard("Scrolling the stream", "Nothing from Tuesday. The log stream is live radio.")),
                    Beat(46, show = setOf("la"), light = setOf("la"), pulse = "con" to "la", card = BoardCard("Log Analytics", "History, queried with KQL. Container insights for AKS.")),
                    Beat(48, card = BoardCard("Container insights", "Azure Monitor keeping AKS container logs and events in Log Analytics.")),
                ),
            ),
            // 49: "Did anything go right this week?"
            Scene(
                49, "What went right", statuses(),
                beats = listOf(
                    Beat(50, show = setOf("pod", "ready"), light = setOf("ready"), pulse = "pod" to "ready", card = BoardCard("Running, 0/1 ready", "The readiness probe, wrong path. Fixed in five minutes.")),
                ),
            ),
            // 51: "So, the gossip I should remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be the story.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-container-troubleshoot--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet, and it's just the three of us..."
            Scene(
                0, "Something's broken", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), card = BoardCard("Tonight", "Something's broken, and nobody knows why.")),
                    Beat(2, card = BoardCard("Image and container", "Code, Python, libraries, slim OS files. Each running copy: a container.")),
                    Beat(4, show = setOf("aca"), light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps", "Walled-off programs on a shared kernel, run for you. No machines.")),
                    Beat(6, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "The nightly worker. Tonight, it isn't working.")),
                ),
            ),
            // 7: "So where do I start? Gently."
            Scene(
                7, "Three questions", questions(),
                beats = listOf(
                    Beat(8, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("Slowly, in order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 9: "Did it start, then."
            Scene(
                9, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(10, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "Each pod's status, how many are ready, how often restarted.")),
                    Beat(12, card = BoardCard("Pod", "Like a pea pod: a wrapper round one or more containers.")),
                    Beat(14, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("Events", "The platform's diary, kept about an hour. Read it while it's fresh.")),
                ),
            ),
            // 15: "Tell me the statuses that keep people up."
            Scene(
                15, "What keeps people up", statuses(),
                beats = listOf(
                    Beat(16, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image. The logs are empty; read describe.")),
                    Beat(20, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts; each wait doubles, to about five minutes.")),
                    Beat(22, card = previousCode),
                    Beat(24, show = setOf("pending"), light = setOf("pending"), pulse = "pod" to "pending", card = BoardCard("Pending", "No machine has room yet. Describe shows FailedScheduling.")),
                    Beat(25, show = setOf("ready"), light = setOf("ready"), pulse = "pod" to "ready", card = BoardCard("Running, and quiet", "Never restarting, zero of one ready. Nobody can reach them.")),
                ),
            ),
            // 26: "Then it's a probe. A probe is a check the platform runs on your contai..."
            Scene(
                26, "A pulse in the night", probes(),
                beats = listOf(
                    Beat(26, show = setOf("platform", "live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness", "Are you alive? Fail it, and the container is restarted.")),
                    Beat(28, show = setOf("ready"), light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness", "Ready for company? If not: still running, no traffic. Door locked.")),
                    Beat(30, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = BoardCard("Startup", "Gives a slow riser time before the others begin.")),
                    Beat(32, light = setOf("live"), card = BoardCard("Keep liveness cheap", "Never the database. A tiny healthz path that says, I'm here.")),
                ),
            ),
            // 33: "And how does readiness keep the callers away, really?"
            Scene(
                33, "Off the list", endpoints(),
                beats = listOf(
                    Beat(34, light = setOf("ep", "pod"), pulse = "ep" to "pod", card = BoardCard("kubectl get endpoints", "The ready pods behind a Service. Empty: nobody's ready to answer.")),
                ),
            ),
            // 37: "Now the backend. Container Apps. Same questions?"
            Scene(
                37, "Container Apps", aca(),
                beats = listOf(
                    Beat(38, show = setOf("rev"), light = setOf("rev"), card = BoardCard("Revision", "A saved version of the app's image and settings, like a numbered draft.")),
                    Beat(40, card = BoardCard("Degraded, 0/1 ready", "A replica is one running copy, like a photocopy. None answering.")),
                    Beat(42, show = setOf("sys", "con"), light = setOf("sys", "con"), pulse = "rev" to "sys", card = BoardCard("Two voices", "System logs: the platform. Console logs: your app.")),
                    Beat(44, show = setOf("la"), light = setOf("la"), pulse = "con" to "la", card = BoardCard("Log Analytics", "When the live show is over, history is queried with KQL.")),
                    Beat(46, card = BoardCard("Container insights", "Azure Monitor keeping AKS container logs and events in Log Analytics.")),
                ),
            ),
            // 47: "And the third question. Can traffic reach it?"
            Scene(
                47, "The front door", ingress(),
                beats = listOf(
                    Beat(48, show = setOf("caller", "ing"), light = setOf("ing"), pulse = "caller" to "ing", card = BoardCard("Ingress", "The front door. It hands callers to a target port, a numbered door.")),
                    Beat(50, show = setOf("app"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("Target port", "App on 8000, ingress on 80: the probes knock, nobody answers.")),
                    Beat(52, show = setOf("con"), light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                ),
            ),
            // 53: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-container-troubleshoot--couples",
        listOf(
            // 0: "Tonight is date night, darling. Candles, a little wine, and you and me..."
            Scene(
                0, "Date night", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), card = BoardCard("Date night", "What to do when things stop working.")),
                    Beat(2, show = setOf("aca"), light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps", "The Coach's backend, run for you, no machines to manage.")),
                    Beat(3, light = setOf("aca"), card = BoardCard("Quiz: its own kernel?", "No: a walled-off program on the host's kernel. Logs: what it prints.")),
                    Beat(7, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "The nightly worker, on Kubernetes.")),
                ),
            ),
            // 8: "You do. So, three questions, in order, when something breaks. Did it s..."
            Scene(
                8, "Three questions", questions(),
                beats = listOf(
                    Beat(8, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("In order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 9: "Did it start. On AKS, I'd ask kubectl, the command-line tool for Kuber..."
            Scene(
                9, "The diary", aksLadder(),
                beats = listOf(
                    Beat(9, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "Status, ready count, restarts.")),
                    Beat(11, card = BoardCard("Pod", "Like a pea pod: a wrapper round containers. Two peas, like us.")),
                    Beat(12, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("Events", "The platform's own diary, at the end of describe.")),
                    Beat(14, card = BoardCard("Read them while warm", "Short-lived records, kept about an hour by default.")),
                ),
            ),
            // 15: "Then let me test you, darling. Exam style. A pod shows ImagePullBackOf..."
            Scene(
                15, "Quiz: the statuses", statuses(),
                beats = listOf(
                    Beat(15, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("Quiz: ImagePullBackOff", "Which command shows the exact error?")),
                    Beat(16, card = BoardCard("Right answer: describe", "The container never started, so no logs. The error is in Events.")),
                    Beat(18, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("Quiz: CrashLoopBackOff", "Starts, crashes, restarts. Where do you look?")),
                    Beat(20, card = BoardCard("No kiss: use previous", "Plain logs show the new container. You need the one that died.")),
                    Beat(22, card = previousCode),
                ),
            ),
            // 23: "Fine. Let me win one back. What's a probe?"
            Scene(
                23, "Are you ready?", probes(),
                beats = listOf(
                    Beat(24, show = setOf("platform", "live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness probe", "A nurse's check: are you alive? Fail it, and you're restarted.")),
                    Beat(26, show = setOf("ready"), light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness", "Not ready: still running, no traffic. Lights on, door locked.")),
                    Beat(28, card = BoardCard("Quiz: 0/1 ready", "Running, never restarting, no callers. What's failing?")),
                    Beat(29, light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Right answer: readiness", "Liveness would restart them. These are running, just not open.")),
                    Beat(31, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = BoardCard("Startup", "Gives a slow starter time before the other two check.")),
                    Beat(32, card = BoardCard("Endpoints", "The Service's list of ready pods. Fail readiness, you're off it.")),
                    Beat(34, light = setOf("live"), card = BoardCard("Keep liveness light", "Never the database. A tiny healthz path is enough.")),
                ),
            ),
            // 35: "One more for you. A pod just sits there, Pending. What's going on?"
            Scene(
                35, "Can't get a table", statuses(),
                beats = listOf(
                    Beat(35, show = setOf("pod", "pending"), light = setOf("pending"), pulse = "pod" to "pending", card = BoardCard("Pending", "No machine has room yet. Describe shows FailedScheduling.")),
                ),
            ),
            // 37: "Noted. Now Container Apps. Each deploy makes a revision, a saved versi..."
            Scene(
                37, "The right door", ingress(),
                beats = listOf(
                    Beat(37, card = BoardCard("Revision", "A saved version of the app's image and settings, like a numbered draft.")),
                    Beat(38, show = setOf("caller", "ing", "app"), light = setOf("ing", "app"), card = BoardCard("Quiz: 8000 and 80", "The app listens on 8000; ingress targets 80. What's wrong?")),
                    Beat(39, pulse = "ing" to "app", card = BoardCard("Right answer: target port", "The default probes knock on 80, and nobody's home.")),
                ),
            ),
            // 41: "And how would you have spotted it, darling?"
            Scene(
                41, "Seeing for yourself", aca(),
                beats = listOf(
                    Beat(42, show = setOf("rev", "sys"), light = setOf("sys"), pulse = "rev" to "sys", card = BoardCard("System logs", "The platform's messages: the probe failing on port 80.")),
                    Beat(44, card = execCode),
                    Beat(45, show = setOf("con", "la"), light = setOf("con"), card = BoardCard("Quiz: last Tuesday", "The log stream shows nothing. Where do you look?")),
                    Beat(46, light = setOf("la"), pulse = "con" to "la", card = BoardCard("Right answer: Log Analytics", "The stream is live; history is queried with KQL.")),
                    Beat(48, card = BoardCard("Container insights", "Azure Monitor keeping AKS container logs and events in Log Analytics.")),
                ),
            ),
            // 50: "Last question, for the exam. What do we remember?"
            Scene(
                50, "For the exam", exam(),
                beats = listOf(
                    Beat(51, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
