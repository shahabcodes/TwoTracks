package com.shahaabapps.core.content

/** Troubleshooting AKS and Container Apps: logs, events and connectivity: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesContainerTroubleshoot {
    private val classic = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Hi, and welcome back. Today is detective work: finding out why a container won't serve. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. You ship a new version, and the app goes quiet. Where do you look?"),
            leo("Usually? Everywhere, in a panic. [chuckle] Is there a better way?"),
            maya("There is. First, the words. A container is your app packed in a box with everything it needs, so it runs the same anywhere. The Coach runs its backend on Azure Container Apps, a service that runs containers for you, with no machines to look after."),
            leo("Just the one place?"),
            maya("Two. Its nightly worker runs on AKS, Azure Kubernetes Service. Kubernetes is a system that runs lots of containers across lots of machines, and keeps them running. The AKS episode covers setting it up. Today is about when things go wrong."),
            leo("So what's better than panic?"),
            maya("Three questions, in order. Did it start? Why did it stop? Can traffic reach it? Each question has its own place to look. Ask them in that order, and most problems name themselves."),
            leo("Start with AKS, then. Did it start? How do I know?"),
            maya("With kubectl, the command-line tool that talks to Kubernetes. kubectl get pods lists your pods. A pod is the smallest thing Kubernetes runs, a wrapper round one or more containers. For each pod you see its status, a ready count, and how many times it has restarted."),
            leo("And what does the status tell me?"),
            maya("Which question to ask next. Then kubectl describe pod, with the pod's name, gives you its details, ending with a list called Events. Events are the platform's own diary: found a machine, pulled the image, a health check failed, stopped the container."),
            leo("Okay. What statuses will I actually see?"),
            maya("The big one first. ImagePullBackOff means the machine can't download your image, the ready-to-start copy of your app, so it's backing off, waiting a while before it tries again."),
            leo("Then I read the logs, to see why?"),
            maya("That's the classic mistake. Logs are what your own code prints, and this container never started, so it has no logs. The pull error is a platform event, so kubectl describe pod shows it, word for word. Usually a wrong tag, the image's version label, or no permission to read your private image shelf."),
            leo("Got an example?"),
            maya("For example, the event says version 31 not found, and the team had built version 3.1. The tag in the file was missing a dot. Fix the tag, apply the file again, and the pod is running."),
            leo("[laugh] One missing dot. And the other big status?"),
            maya("CrashLoopBackOff. The container starts, crashes, and Kubernetes keeps restarting it, waiting longer each time. Here your code did run, so this time the logs have the answer."),
            leo("So, kubectl logs."),
            maya("With one flag that matters: previous. Plain kubectl logs shows the container that's starting right now, which may have printed nothing yet. With the previous flag, you see the one that just died, with its stack trace, the error report Python prints when it crashes."),
            leo("Any other statuses?"),
            maya("One more worth knowing. Pending means no machine has room for the pod yet. Describe shows an event called FailedScheduling, which is Kubernetes for, I couldn't find a place to put it."),
            leo("Now the sneaky one. The pods are running and never restart, but callers get nothing. Ready shows zero of one."),
            maya("That's a probe. A probe is a check the platform runs against your container, like a nurse checking a pulse. There are three kinds, and the exam loves the difference."),
            leo("Go on, nurse."),
            maya("A liveness probe asks, are you alive? If it fails, the container is stuck, so it's restarted. A readiness probe asks, are you ready for customers? If it fails, the container keeps running but gets no traffic. A startup probe gives a slow starter time before the other two begin."),
            leo("So running, never restarting, zero of one ready..."),
            maya("Is a failing readiness probe. A failing liveness probe would restart it, and these never restart. Usually the probe has the wrong path or the wrong port. A port is the numbered door your app listens at. Point the probe at the wrong door, and nobody answers."),
            leo("What should the probes check, then?"),
            maya("Cheap things. The Coach has two tiny web paths: healthz, which just says I'm alive, and ready, which says whether its AI model has loaded. And another classic mistake: a liveness probe that calls the database. Then one slow database restarts every pod at once."),
            leo("Ouch. Now Container Apps. Same three questions?"),
            maya("Same questions, different tools. In Container Apps, each deploy makes a revision, a saved version of your app's image and settings. You list them with az, the Azure command-line tool: az containerapp revision list."),
            leo("And what am I looking for in that list?"),
            maya("Health. A good revision is Running. A sick one says Degraded, with zero of one replicas ready. A replica is one running copy of your app. That usually means a probe or the port is wrong."),
            leo("And the logs, in Container Apps?"),
            maya("Two kinds. System logs come from the platform: image pulls, probe results, revision events. Console logs are your own app's output, whatever your code prints. You can stream either one live, with az containerapp logs show."),
            leo("Live is nice. What about yesterday's errors?"),
            maya("The log stream is like live radio: once it's gone, it's gone. For history, you query Log Analytics, Azure's store for logs, with KQL, its query language. On AKS, that history needs Container insights switched on: Azure Monitor collecting your container logs and Kubernetes events. The KQL episode teaches the queries."),
            leo("And the third question? Can traffic reach it?"),
            maya("In Container Apps, check the ingress, the front door that lets traffic in. It has a target port, the door number it passes callers to. The exam's favourite: the app listens on 8000, but ingress targets 80. The default probes knock on 80, nobody answers, and the revision goes Degraded."),
            leo("And on AKS?"),
            maya("kubectl get endpoints, with your Service's name. A Service is the stable address in front of your pods, and endpoints are the ready pods behind it. Empty means no ready pod matches. And in both worlds you can open a console, a command line inside the running container, and test from where the app sits."),
            leo("So what do I remember for the exam?"),
            maya("If the question says ImagePullBackOff, the answer is kubectl describe pod, not logs. If it says CrashLoopBackOff, kubectl logs with previous. If pods run, never restart, and show zero of one ready, the readiness probe. If a revision is Degraded and the ports differ, fix the target port. And for yesterday's errors, Log Analytics, not the log stream."),
            leo("And anything to try today?"),
            maya("Deploy a revision with a wrong target port, and watch it go Degraded. Stream the system logs and find the probe failure. Fix the port, and confirm the revision becomes healthy. Then open a console with az containerapp exec, and call the app on localhost, the container calling itself."),
            leo("Get, describe, logs with previous. My new panic plan. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-container-troubleshoot",
        listOf(
            maya("Welcome to Case Files. Today's case: The Friday Release. A small Android app called Plant Pal tells you what's wrong with your houseplants. Its Python backend runs in containers on Azure. On Friday afternoon the team shipped a new version of everything. By Friday evening, the app answered nobody."),
            leo("[chuckle] Friday releases. The oldest crime there is. Detective Leo, on the case. Where does the backend live?"),
            maya("In two places. A container is an app packed in a box with everything it needs. The main backend runs on Azure Container Apps, a service that runs containers for you, with no machines to manage. The photo worker runs on AKS, Azure Kubernetes Service. Kubernetes runs lots of containers across lots of machines, and keeps them running."),
            leo("Two crime scenes. Which first?"),
            maya("The worker on AKS. We ask three questions, in order. Did it start? Why did it stop? Can traffic reach it? First question: kubectl get pods. kubectl is the command-line tool for Kubernetes, and a pod is the smallest thing it runs, a wrapper round one or more containers."),
            leo("And what did it show?"),
            maya("Status: ImagePullBackOff. That means the machine can't download the image, the ready-to-start copy of the app, so it's backing off, waiting longer between tries."),
            leo("Easy. First suspect: the code. I'll read the logs."),
            maya("[pause] Wrong place. Logs are what the code prints, and this container never started. Empty logs, every time. The pull error is a platform event. kubectl describe pod ends with a list called Events, the platform's own diary. And the diary said: version 31 not found."),
            leo("And the team had built..."),
            maya("Version 3.1. A missing dot in the tag, the image's version label. Under the picture, events are short-lived records the cluster writes as it works; Kubernetes keeps them only about an hour by default, so read them early. If the tag had been right, the next suspect is permission to read the private image shelf. The AKS episode covers that fix."),
            leo("Dot fixed. Case closed?"),
            maya("The pods started, and then the second clue. Status: CrashLoopBackOff. The container starts, crashes, and Kubernetes restarts it, waiting longer each time. How it works: the wait roughly doubles after each crash, up to about five minutes."),
            leo("This time the code ran, so I read the logs. Nothing there."),
            maya("Because plain kubectl logs shows the container that's starting right now, which hasn't printed anything yet. You want the one that just died. kubectl logs with the previous flag. And there was the stack trace, the error report Python prints when it crashes."),
            leo("What did it say?"),
            maya("The database address setting was missing. The new version expected it, and nobody had added it to the ConfigMap, the list of plain settings handed to the pods. They added it, applied, and the worker came up. Events tell you about pulls and probes. Logs tell you about your code."),
            leo("Now the main backend. Container Apps."),
            maya("Same three questions, different tools. Each deploy there makes a revision, a saved version of the app's image and settings. They listed them with az, the Azure command-line tool, using az containerapp revision list. The new revision said Degraded, zero of one replicas ready. A replica is one running copy."),
            leo("Suspect: the image is missing from the registry."),
            maya("Then it would fail at the pull, before any replica ran, and the system logs would say so. System logs are the platform's messages: pulls, probes, revisions. Console logs are what the app prints. The system log said something else. The probe got no answer on port 80."),
            leo("Port 80. And what does the app listen on?"),
            maya("8000. A port is the numbered door an app listens at. The ingress, the front door that lets traffic in, had its target port set to 80. With ingress on, the default probes knock on the target port. Nobody answers at 80, so no replica is ever ready."),
            leo("Hang on. What's a probe, exactly?"),
            maya("A check the platform runs against your container, like a nurse checking a pulse. A liveness probe failing means stuck, so it restarts the container. A readiness probe failing means not now, so it takes the container out of traffic but leaves it running. A startup probe gives a slow starter time first."),
            leo("How could they prove it was the port?"),
            maya("From the inside. az containerapp exec opens a console, a command line inside the running container. They called the app on localhost, the container calling itself, on 8000, and it answered. The app was fine all along. The door was wrong."),
            leo("Fix the target port, then. Case closed?"),
            maya("Port fixed, revision Running. But on Saturday, a third witness. The AKS worker had a second copy behind a Service, the stable address in front of pods. Those pods were running, never restarted, and showed zero of one ready."),
            leo("Suspect: the liveness probe."),
            maya("A failing liveness probe would restart them, and these never restarted. So it's readiness. kubectl get endpoints showed the Service's ready pods: none. The readiness probe pointed at the wrong path. And where this stops working: never point a liveness probe at the database, or one slow database restarts every pod."),
            leo("And the boss wants to know what broke on Friday. Can we scroll back the log stream?"),
            maya("No. The log stream is live, like radio. History lives in Log Analytics, Azure's log store, which you query with KQL; the KQL episode teaches it. For AKS, switch on Container insights, Azure Monitor collecting container logs and Kubernetes events into it."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If the question says ImagePullBackOff, the answer is kubectl describe pod, not logs. If CrashLoopBackOff, kubectl logs with previous. If pods run, never restart, and show zero of one ready, the readiness probe. If a revision is Degraded and the ports differ, fix the target port. And for yesterday's errors, Log Analytics."),
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
            maya("In containers. A container is an app packed in a box with everything it needs. The main backend runs on Azure Container Apps, a service that runs containers for you, no machines to manage. The nightly worker runs on AKS, Azure Kubernetes Service, a system that runs lots of containers across lots of machines and keeps them running."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. The worker's pods said ImagePullBackOff. A pod is the smallest thing Kubernetes runs, a wrapper round one or more containers. ImagePullBackOff means the machine can't download the image, the ready-to-start copy of the app, so it's waiting longer between tries."),
            leo("And Tom did what?"),
            maya("Read the logs. With kubectl, the command-line tool for Kubernetes. For an hour. And they were empty."),
            leo("Why empty?"),
            maya("Logs are what your code prints, and the container never started. The pull error is a platform event. kubectl describe pod ends with a list called Events, the platform's own diary, and it said, version 31 not found. Someone had dropped a dot from version 3.1."),
            leo("So what was the actual fix?"),
            maya("The tag, the image's version label. Put the dot back, apply the file again, done. If the tag had been right, the next suspect is permission to read the team's private image shelf. The AKS episode covers that one."),
            leo("[sigh] Poor Tom. And Tuesday?"),
            maya("Tuesday was Dana. The worker came up and fell over, again and again. Status: CrashLoopBackOff. The container starts, crashes, and Kubernetes restarts it, waiting longer each time. Under the hood, the wait roughly doubles, up to about five minutes."),
            leo("So this time the logs help."),
            maya("They do, if you ask the right container. Dana ran plain kubectl logs, which shows the one starting right now. It hadn't printed anything yet. You need the previous flag, for the one that just died, with its stack trace, Python's crash report. Dana found it after lunch: a missing setting."),
            leo("I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. Marcus had read about probes. A probe is a check the platform runs against your container, like a nurse checking a pulse."),
            leo("That sounds sensible. What went wrong?"),
            maya("A liveness probe asks, are you alive? Fail it, and the container is restarted. Marcus made his liveness probe call the database, to be thorough. On Wednesday the database was slow for a minute. Every probe failed, and every single pod restarted at once."),
            leo("Oof. So what should liveness check?"),
            maya("Something cheap, like a tiny healthz path that just says, I'm alive. Then there's the readiness probe, which asks, are you ready for customers? Fail it, and the container keeps running, but gets no traffic. And a startup probe gives slow starters time first."),
            leo("And how does readiness actually keep the traffic away?"),
            maya("Under the hood, a Service, the stable address in front of your pods, keeps a list of the ready ones, called endpoints. Fail readiness, and you're taken off the list, so the Service simply stops sending you calls. Pass again, and you're back on."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. They deployed a new revision of the Container Apps backend. A revision is a saved version of the app's image and settings. And it went Degraded, zero of one replicas ready. A replica is one running copy."),
            leo("And the culprit?"),
            maya("The ingress, the front door that lets traffic in. Its target port, the numbered door it passes callers to, was 80. The app listens on 8000. The default probes knocked on 80, nobody answered, so no replica was ever ready."),
            leo("How did they find it?"),
            maya("The system logs. In Container Apps, system logs are the platform's messages: image pulls, probe results, revisions. Console logs are what your app prints. They streamed the system logs with az, the Azure command-line tool, and there it was: probe failed on port 80."),
            leo("Could they have tested it from inside?"),
            maya("Yes. az containerapp exec opens a console, a command line inside the running container. From there you call the app on localhost, which means the container calling itself. On 8000 it answered fine. The app was healthy; the door was wrong."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. The boss asked what broke on Tuesday night. Priya opened the log stream and scrolled. And scrolled. Nothing from Tuesday."),
            leo("Because the stream is live."),
            maya("Exactly. It's like live radio. History lives in Log Analytics, Azure's store for logs, which you query with KQL, its query language. For AKS, that needs Container insights on, Azure Monitor collecting container logs and Kubernetes events. And Kubernetes events fade too: by default they're kept only about an hour. The KQL episode is Priya's weekend reading."),
            leo("Did anything go right this week?"),
            maya("One thing. Running pods that showed zero of one ready, and never restarted. The new hire said, that's the readiness probe, and kubectl get endpoints, the list of ready pods behind a Service, was empty. Wrong path in the probe. Fixed in five minutes. Everyone's still annoyed."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. If the question says ImagePullBackOff, kubectl describe pod, not logs. If CrashLoopBackOff, kubectl logs with previous. If pods run, never restart, and show zero of one ready, the readiness probe. If a revision is Degraded and the ports differ, fix the target port. And for yesterday's errors, Log Analytics."),
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
            nova("There is. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. It runs in containers: the app packed in a box with everything it needs. The backend lives on Azure Container Apps, a service that runs containers for you, no machines to manage."),
            ivy("And anywhere else?"),
            nova("A nightly worker, on AKS, Azure Kubernetes Service. Kubernetes runs lots of containers across lots of machines, and keeps them running. [pause] Tonight, the worker isn't working."),
            ivy("So where do I start? Gently."),
            nova("With three questions, slowly, in order. Did it start? Why did it stop? Can traffic reach it? Each one has its own place to look."),
            ivy("Did it start, then."),
            nova("You ask with kubectl, the command-line tool for Kubernetes. kubectl get pods. A pod is the smallest thing Kubernetes runs, a wrapper round one or more containers. You'll see each pod's status, how many are ready, and how often they've restarted."),
            ivy("And if the status is something I don't recognise?"),
            nova("Then kubectl describe pod. At the bottom is a list called Events, [pause] the platform's own diary. It found a machine, it pulled the image, a health check failed. Under the picture, events are short-lived records the cluster keeps for about an hour by default. Read them while they're fresh."),
            ivy("Tell me the statuses that keep people up."),
            nova("ImagePullBackOff. The machine can't download your image, the ready-to-start copy of the app, so it's backing off between tries. And here's the trap. People read the logs."),
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
            nova("A readiness probe asks, are you ready for company? Fail it, and the container keeps running, but no traffic comes. A startup probe gives a slow riser time before the others begin. So running, never restarting, zero of one ready: [pause] readiness."),
            ivy("Ready for company. You make health checks sound personal."),
            nova("They are. And where it stops working: a liveness probe that calls the database. One slow database, and every pod is restarted at once. Keep liveness cheap, like a tiny healthz path that just says, I'm here."),
            ivy("And how does readiness keep the callers away, really?"),
            nova("A Service, the stable address in front of your pods, keeps a list of the ready ones, called endpoints. Fail readiness, and you're quietly taken off the list. [pause] kubectl get endpoints shows it. Empty means nobody's ready to answer."),
            ivy("Now the backend. Container Apps. Same questions?"),
            nova("Same questions, softer tools. Each deploy makes a revision, a saved version of the app's image and settings. You list them with az, the Azure command-line tool. A sick revision says Degraded, zero of one replicas ready. A replica is one running copy."),
            ivy("And the logs there?"),
            nova("Two voices. System logs are the platform talking: image pulls, probe results, revisions. Console logs are your app talking, whatever it prints. You can stream either, live, [pause] like late-night radio."),
            ivy("And when the show is over? Last night's errors?"),
            nova("Then Log Analytics, Azure's store for logs, which you query with KQL, its query language. The KQL episode is a good companion for another night. On AKS, you switch on Container insights, Azure Monitor collecting your container logs and events, so they're kept."),
            ivy("And the third question. Can traffic reach it?"),
            nova("In Container Apps, look at the ingress, the front door that lets traffic in. It has a target port, the numbered door it passes callers to. If the app listens on 8000 and ingress targets 80, the default probes knock on 80, nobody answers, and the revision goes Degraded."),
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
            nova("[laugh] It's about containers. A container is an app packed in a box with everything it needs. The Two Tracks Coach, the study assistant in the Android app, runs its Python backend in containers on Azure Container Apps, a service that runs them for you, no machines to manage."),
            rafe("And the nightly worker runs on AKS, Azure Kubernetes Service. Kubernetes keeps lots of containers running on lots of machines. I do listen, you know."),
            nova("You do. So, three questions, in order, when something breaks. Did it start? Why did it stop? Can traffic reach it?"),
            rafe("Did it start. On AKS, I'd ask kubectl, the command-line tool for Kubernetes. kubectl get pods. A pod is the smallest thing it runs, a wrapper round containers."),
            nova("And it shows each pod's status, how many are ready, and how often they've restarted. Then kubectl describe pod gives the details, ending with Events, the platform's own diary."),
            rafe("The platform's diary. Do I get to read yours?"),
            nova("Only the events. Under the picture, they're short-lived records the cluster writes as it works, kept about an hour by default. So read them while they're warm."),
            rafe("Then let me test you, darling. Exam style. A pod shows ImagePullBackOff. Which command shows you the exact error?"),
            nova("ImagePullBackOff means the machine can't download the image, the ready-to-start copy of the app, so it waits between tries. The container never started, so it has no logs. kubectl describe pod. The pull error is in the Events."),
            rafe("Right answer. Some things you only learn from the diary. [kiss]"),
            nova("Mm. My turn. A pod is in CrashLoopBackOff. It starts, crashes, and Kubernetes keeps restarting it, waiting longer each time. Where do you look?"),
            rafe("kubectl logs. Logs are what the code prints."),
            nova("No kiss for that one. Plain logs show the container that's just starting, which hasn't said anything yet. You need the previous flag, for the one that just died."),
            rafe("So you do want me to bring up the previous one."),
            nova("[chuckle] Only for its stack trace, Python's crash report. That's where the reason is. Under the hood, the wait between restarts roughly doubles, up to about five minutes."),
            rafe("Fine. Let me win one back. What's a probe?"),
            nova("A check the platform runs against your container, like a nurse checking your pulse. A liveness probe asks, are you alive? Fail it, and you're restarted. A readiness probe asks, are you ready? Fail it, and you keep running, but get no traffic."),
            rafe("Are you ready? I always am."),
            nova("Then here's your question. The pods are running, never restart, and show zero of one ready. Callers get nothing. What's failing?"),
            rafe("The readiness probe. A failing liveness probe would restart them, and these never restart. Running, but not taking visitors."),
            nova("Right answer. Ready when you are. [kiss]"),
            rafe("And a startup probe gives a slow starter time first. Under the hood, a pod that fails readiness is taken off its Service's list of ready pods, the endpoints. The Service is the stable address in front of the pods, so no calls reach it. Now, where does it all stop working?"),
            nova("When someone points a liveness probe at the database. One slow database, and every pod restarts at once. Keep liveness light, like a tiny healthz path that just says, I'm alive. Some checks shouldn't be so demanding."),
            rafe("One more for you. A pod just sits there, Pending. What's going on?"),
            nova("No machine has room for it yet. Describe shows an event called FailedScheduling, Kubernetes for, I couldn't find it a seat. Some pods just can't get a table."),
            rafe("Noted. Now Container Apps. Each deploy makes a revision, a saved version of the app's image and settings. You list them with az, the Azure command-line tool."),
            nova("And a sick revision says Degraded, zero of one replicas ready. A replica is one running copy. So, darling. The app listens on port 8000, but ingress targets 80. What's wrong?"),
            rafe("Ingress is the front door that lets traffic in, and the target port is the numbered door it passes callers to. The default probes knock on 80, and nobody's home. Fix the target port to 8000."),
            nova("Right answer. Always knock on the right door. [kiss]"),
            rafe("And how would you have spotted it, darling?"),
            nova("The system logs, the platform's messages: pulls, probes, revisions. The console logs are what the app itself prints. I'd stream the system logs, and see the probe failing on 80."),
            rafe("And if I want to see for myself, from the inside?"),
            nova("az containerapp exec opens a console, a command line inside the running container. Call the app on localhost, the container calling itself. If it answers there, the trouble is at the door, not inside."),
            rafe("My turn to ask. The boss wants last Tuesday's errors. The log stream shows nothing. Where do you look?"),
            nova("Log Analytics, Azure's store for logs, queried with KQL, its query language. The log stream is live, like a phone call; Log Analytics keeps the history. On AKS, Container insights collects container logs and events into it. The KQL episode teaches the queries."),
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
            // 0: "Hi, and welcome back. Today is detective work..."
            Scene(
                0, "Why won't it serve?", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), light = setOf("phone"), card = BoardCard("The app goes quiet", "A new version ships, and nothing answers. Where do you look?")),
                    Beat(2, show = setOf("aca"), light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps", "Runs your containers for you, with no machines to look after.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "Kubernetes: many containers on many machines, kept running.")),
                ),
            ),
            // 5: "So what's better than panic?"
            Scene(
                5, "Three questions", questions(),
                beats = listOf(
                    Beat(6, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("Ask them in order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 7: "Start with AKS, then. Did it start?"
            Scene(
                7, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(8, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "Each pod's status, ready count and restarts.")),
                    Beat(10, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("Events", "The platform's own diary, at the end of kubectl describe pod.")),
                ),
            ),
            // 11: "Okay. What statuses will I actually see?"
            Scene(
                11, "The statuses", statuses(),
                beats = listOf(
                    Beat(12, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image; waiting longer between tries.")),
                    Beat(14, card = describeCode),
                    Beat(16, card = BoardCard("One missing dot", "The event said version 31 not found; the team built 3.1.")),
                    Beat(18, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts, waiting longer each time.")),
                    Beat(20, card = previousCode),
                    Beat(22, show = setOf("pending"), light = setOf("pending"), pulse = "pod" to "pending", card = BoardCard("Pending", "No machine has room yet. Describe shows FailedScheduling.")),
                    Beat(23, show = setOf("ready"), light = setOf("ready"), pulse = "pod" to "ready", card = BoardCard("Running, 0/1 ready", "Never restarts, gets no traffic. Something is checking it.")),
                ),
            ),
            // 24: "That's a probe..."
            Scene(
                24, "Probes", probes(),
                beats = listOf(
                    Beat(24, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Probe", "A check the platform runs on your container, like a nurse.")),
                    Beat(26, show = setOf("live", "ready", "start"), light = setOf("live", "ready", "start"), card = BoardCard("Three kinds", "Liveness: restart. Readiness: no traffic. Startup: time to wake.")),
                    Beat(28, light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness, failing", "Usually the wrong path or port: the wrong numbered door.")),
                    Beat(30, light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Keep liveness cheap", "A tiny healthz path. Never the database, or all pods restart.")),
                ),
            ),
            // 31: "Ouch. Now Container Apps. Same three questions?"
            Scene(
                31, "Container Apps", aca(),
                beats = listOf(
                    Beat(32, show = setOf("rev"), light = setOf("rev"), card = revisionCode),
                    Beat(34, card = BoardCard("Degraded, 0/1 ready", "A sick revision. A replica is one running copy of the app.")),
                    Beat(36, show = setOf("sys", "con"), light = setOf("sys", "con"), pulse = "rev" to "sys", card = BoardCard("System and console logs", "The platform's messages, and what your own code prints.")),
                    Beat(38, show = setOf("la"), light = setOf("la"), pulse = "sys" to "la", card = BoardCard("Log Analytics", "The stream is live radio; history is queried here with KQL.")),
                ),
            ),
            // 39: "And the third question? Can traffic reach it?"
            Scene(
                39, "Can traffic reach it?", ingress(),
                beats = listOf(
                    Beat(40, show = setOf("caller", "ing", "app"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("The target port", "App on 8000, ingress on 80: the probes knock, nobody answers.")),
                ),
            ),
            // 41: "And on AKS?"
            Scene(
                41, "On AKS", endpoints(),
                beats = listOf(
                    Beat(42, light = setOf("ep"), pulse = "svc" to "ep", card = BoardCard("kubectl get endpoints", "The ready pods behind a Service. Empty: no ready pod matches.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: describe, previous, readiness.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-container-troubleshoot--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Friday Release..."
            Scene(
                0, "The Friday Release", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca", "aks"), light = setOf("phone"), card = BoardCard("The case", "Plant Pal shipped everything on Friday. By evening, nobody answered.")),
                    Beat(2, light = setOf("aca", "aks"), card = BoardCard("Two crime scenes", "The backend on Container Apps; the photo worker on AKS.")),
                ),
            ),
            // 3: "Two crime scenes. Which first?"
            Scene(
                3, "Three questions", questions(),
                beats = listOf(
                    Beat(4, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("Ask them in order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 5: "And what did it show?"
            Scene(
                5, "Clue one: the worker", statuses(),
                beats = listOf(
                    Beat(6, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image; backing off between tries.")),
                    Beat(7, card = BoardCard("Suspect: the code", "Wrong place. The container never started, so its logs are empty.")),
                    Beat(8, card = BoardCard("The Events diary", "kubectl describe pod said: version 31 not found.")),
                    Beat(10, card = BoardCard("A missing dot", "The team built 3.1. Events are kept about an hour: read them early.")),
                    Beat(12, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts; the wait doubles, up to about five minutes.")),
                    Beat(14, card = previousCode),
                    Beat(16, card = BoardCard("A missing setting", "Added to the ConfigMap. Events: pulls and probes. Logs: your code.")),
                ),
            ),
            // 17: "Now the main backend. Container Apps."
            Scene(
                17, "Clue two: the backend", aca(),
                beats = listOf(
                    Beat(18, show = setOf("rev"), light = setOf("rev"), card = BoardCard("Degraded, 0/1 ready", "A revision is a saved version; a replica is one running copy.")),
                    Beat(19, card = BoardCard("Suspect: a missing image", "It would fail at the pull, and the system logs would say so.")),
                    Beat(20, show = setOf("sys"), light = setOf("sys"), pulse = "rev" to "sys", card = BoardCard("System logs", "The platform's messages said: the probe got no answer on port 80.")),
                ),
            ),
            // 21: "Port 80. And what does the app listen on?"
            Scene(
                21, "The wrong door", ingress(),
                beats = listOf(
                    Beat(22, show = setOf("caller", "ing", "app"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("Target port", "Ingress sends callers to 80; the app listens on 8000.")),
                ),
            ),
            // 23: "Hang on. What's a probe, exactly?"
            Scene(
                23, "Checking the pulse", probes(),
                beats = listOf(
                    Beat(24, show = setOf("live", "ready", "start"), light = setOf("live", "ready", "start"), pulse = "platform" to "ready", card = BoardCard("Three probes", "Liveness: restart. Readiness: no traffic. Startup: time first.")),
                ),
            ),
            // 25: "How could they prove it was the port?"
            Scene(
                25, "Proof from the inside", ingress(),
                beats = listOf(
                    Beat(26, light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                ),
            ),
            // 27: "Fix the target port, then. Case closed?"
            Scene(
                27, "Saturday's witness", endpoints(),
                beats = listOf(
                    Beat(28, light = setOf("pod"), card = BoardCard("Running, 0/1 ready", "Never restarted, and no calls reached them.")),
                    Beat(29, card = BoardCard("Suspect: liveness", "Cleared. A failing liveness probe would have restarted them.")),
                    Beat(30, light = setOf("ep", "pod"), pulse = "ep" to "pod", card = BoardCard("Readiness, wrong path", "Endpoints empty. And never point liveness at the database.")),
                ),
            ),
            // 31: "And the boss wants to know what broke on Friday..."
            Scene(
                31, "Friday's history", aca(),
                beats = listOf(
                    Beat(32, show = setOf("con", "la"), light = setOf("la"), pulse = "con" to "la", card = BoardCard("Log Analytics", "The stream is live radio. History is queried with KQL.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. No more Friday releases.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-container-troubleshoot--gossip",
        listOf(
            // 0: "Okay, did you hear about on-call week on the Coach team?..."
            Scene(
                0, "On-call week", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "aca"), light = setOf("phone"), card = BoardCard("Every day, a break", "And every day, someone looked in exactly the wrong place.")),
                    Beat(2, show = setOf("aks"), light = setOf("aca", "aks"), card = BoardCard("Where the Coach runs", "The backend on Container Apps; the nightly worker on AKS.")),
                ),
            ),
            // 3: "Okay. So who went first?"
            Scene(
                3, "Monday: Tom", statuses(),
                beats = listOf(
                    Beat(4, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image; waiting longer between tries.")),
                    Beat(6, card = BoardCard("Tom read the logs", "For an hour. They were empty: the container never started.")),
                    Beat(8, card = describeCode),
                    Beat(10, card = BoardCard("Fix the tag", "Version 31 should have been 3.1. Apply the file again.")),
                ),
            ),
            // 11: "Poor Tom. And Tuesday?"
            Scene(
                11, "Tuesday: Dana", statuses(),
                beats = listOf(
                    Beat(12, show = setOf("pod", "crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts; the wait doubles, up to five minutes.")),
                    Beat(14, card = previousCode),
                ),
            ),
            // 15: "I sense a Wednesday."
            Scene(
                15, "Wednesday: Marcus", probes(),
                beats = listOf(
                    Beat(16, show = setOf("platform"), light = setOf("platform"), card = BoardCard("Probe", "A check the platform runs on your container, like a nurse.")),
                    Beat(18, show = setOf("live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness called the database", "A slow minute, every probe failed, every pod restarted.")),
                    Beat(20, show = setOf("ready", "start"), light = setOf("ready", "start"), pulse = "platform" to "ready", card = BoardCard("Readiness and startup", "Readiness: no traffic until ready. Startup: time for slow starters.")),
                ),
            ),
            // 21: "And how does readiness actually keep the traffic away?"
            Scene(
                21, "Off the list", endpoints(),
                beats = listOf(
                    Beat(22, light = setOf("ep", "pod"), pulse = "ep" to "pod", card = BoardCard("Endpoints", "The Service's list of ready pods. Fail readiness, you're off it.")),
                ),
            ),
            // 23: "Thursday. Go."
            Scene(
                23, "Thursday: the intern", ingress(),
                beats = listOf(
                    Beat(24, show = setOf("app"), light = setOf("app"), card = BoardCard("Degraded, 0/1 ready", "A new revision, a saved version of the app, and no ready replica.")),
                    Beat(26, show = setOf("caller", "ing"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("Target port 80", "The app listens on 8000. The default probes knocked on 80.")),
                    Beat(28, light = setOf("ing"), card = BoardCard("System logs", "The platform's messages said: probe failed on port 80.")),
                    Beat(30, show = setOf("con"), light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                ),
            ),
            // 31: "And Friday? There's always a Friday."
            Scene(
                31, "Friday: Priya", aca(),
                beats = listOf(
                    Beat(32, show = setOf("con"), light = setOf("con"), card = BoardCard("Scrolling the stream", "Nothing from Tuesday. The log stream is live radio.")),
                    Beat(34, show = setOf("la"), light = setOf("la"), pulse = "con" to "la", card = BoardCard("Log Analytics", "History, queried with KQL. Container insights for AKS.")),
                ),
            ),
            // 35: "Did anything go right this week?"
            Scene(
                35, "What went right", statuses(),
                beats = listOf(
                    Beat(36, show = setOf("pod", "ready"), light = setOf("ready"), pulse = "pod" to "ready", card = BoardCard("Running, 0/1 ready", "The readiness probe, wrong path. Fixed in five minutes.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be the story.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-container-troubleshoot--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Something's broken", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), card = BoardCard("Tonight", "Something's broken, and nobody knows why.")),
                    Beat(2, show = setOf("aca"), light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps", "Runs the Coach's backend for you, no machines to manage.")),
                    Beat(4, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "The nightly worker. Tonight, it isn't working.")),
                ),
            ),
            // 5: "So where do I start? Gently."
            Scene(
                5, "Three questions", questions(),
                beats = listOf(
                    Beat(6, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("Slowly, in order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 7: "Did it start, then."
            Scene(
                7, "Did it start?", aksLadder(),
                beats = listOf(
                    Beat(8, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "Each pod's status, how many are ready, how often restarted.")),
                    Beat(10, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("Events", "The platform's diary, kept about an hour. Read it while it's fresh.")),
                ),
            ),
            // 11: "Tell me the statuses that keep people up."
            Scene(
                11, "What keeps people up", statuses(),
                beats = listOf(
                    Beat(12, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("ImagePullBackOff", "Can't download the image. The logs are empty; read describe.")),
                    Beat(16, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("CrashLoopBackOff", "Starts, crashes, restarts; each wait doubles, to about five minutes.")),
                    Beat(18, card = previousCode),
                    Beat(20, show = setOf("pending"), light = setOf("pending"), pulse = "pod" to "pending", card = BoardCard("Pending", "No machine has room yet. Describe shows FailedScheduling.")),
                    Beat(21, show = setOf("ready"), light = setOf("ready"), pulse = "pod" to "ready", card = BoardCard("Running, and quiet", "Never restarting, zero of one ready. Nobody can reach them.")),
                ),
            ),
            // 22: "Then it's a probe..."
            Scene(
                22, "A pulse in the night", probes(),
                beats = listOf(
                    Beat(22, show = setOf("platform", "live"), light = setOf("live"), pulse = "platform" to "live", card = BoardCard("Liveness", "Are you alive? Fail it, and the container is restarted.")),
                    Beat(24, show = setOf("ready", "start"), light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Readiness", "Ready for company? Fail it: still running, no traffic.")),
                    Beat(26, light = setOf("live"), card = BoardCard("Keep liveness cheap", "Never the database. A tiny healthz path that says, I'm here.")),
                ),
            ),
            // 27: "And how does readiness keep the callers away, really?"
            Scene(
                27, "Off the list", endpoints(),
                beats = listOf(
                    Beat(28, light = setOf("ep", "pod"), pulse = "ep" to "pod", card = BoardCard("kubectl get endpoints", "The ready pods behind a Service. Empty: nobody's ready to answer.")),
                ),
            ),
            // 29: "Now the backend. Container Apps. Same questions?"
            Scene(
                29, "Container Apps", aca(),
                beats = listOf(
                    Beat(30, show = setOf("rev"), light = setOf("rev"), card = BoardCard("Degraded, 0/1 ready", "A sick revision. A replica is one running copy.")),
                    Beat(32, show = setOf("sys", "con"), light = setOf("sys", "con"), pulse = "rev" to "sys", card = BoardCard("Two voices", "System logs: the platform. Console logs: your app.")),
                    Beat(34, show = setOf("la"), light = setOf("la"), pulse = "con" to "la", card = BoardCard("Log Analytics", "When the live show is over, history is queried with KQL.")),
                ),
            ),
            // 35: "And the third question. Can traffic reach it?"
            Scene(
                35, "The front door", ingress(),
                beats = listOf(
                    Beat(36, show = setOf("caller", "ing", "app"), light = setOf("ing", "app"), pulse = "ing" to "app", card = BoardCard("Target port", "App on 8000, ingress on 80. The probes knock; nobody answers.")),
                    Beat(38, show = setOf("con"), light = setOf("con", "app"), pulse = "con" to "app", card = execCode),
                ),
            ),
            // 39: "Mm. Before you send me off to bed..."
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-container-troubleshoot--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", places(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), card = BoardCard("Date night", "What to do when things stop working.")),
                    Beat(2, show = setOf("aca"), light = setOf("aca"), pulse = "phone" to "aca", card = BoardCard("Container Apps", "The Coach's backend, run for you, no machines to manage.")),
                    Beat(3, show = setOf("aks"), light = setOf("aks"), card = BoardCard("AKS", "The nightly worker, on Kubernetes.")),
                ),
            ),
            // 4: "You do. So, three questions..."
            Scene(
                4, "Three questions", questions(),
                beats = listOf(
                    Beat(4, light = setOf("q1", "q2", "q3"), pulse = "q1" to "q2", card = BoardCard("In order", "Did it start? Why did it stop? Can traffic reach it?")),
                ),
            ),
            // 5: "Did it start. On AKS, I'd ask kubectl..."
            Scene(
                5, "The diary", aksLadder(),
                beats = listOf(
                    Beat(5, show = setOf("get"), light = setOf("get"), card = BoardCard("kubectl get pods", "Status, ready count, restarts.")),
                    Beat(6, show = setOf("describe"), light = setOf("describe"), pulse = "get" to "describe", card = BoardCard("Events", "The platform's own diary, at the end of describe.")),
                    Beat(8, card = BoardCard("Read them while warm", "Short-lived records, kept about an hour by default.")),
                ),
            ),
            // 9: "Then let me test you, darling..."
            Scene(
                9, "Quiz: the statuses", statuses(),
                beats = listOf(
                    Beat(9, show = setOf("pod", "pull"), light = setOf("pull"), pulse = "pod" to "pull", card = BoardCard("Quiz: ImagePullBackOff", "Which command shows the exact error?")),
                    Beat(10, card = BoardCard("Right answer: describe", "The container never started, so no logs. The error is in Events.")),
                    Beat(12, show = setOf("crash"), light = setOf("crash"), pulse = "pod" to "crash", card = BoardCard("Quiz: CrashLoopBackOff", "Starts, crashes, restarts. Where do you look?")),
                    Beat(14, card = BoardCard("No kiss: use previous", "Plain logs show the new container. You need the one that died.")),
                    Beat(16, card = previousCode),
                ),
            ),
            // 17: "Fine. Let me win one back. What's a probe?"
            Scene(
                17, "Are you ready?", probes(),
                beats = listOf(
                    Beat(18, show = setOf("platform", "live", "ready"), light = setOf("live", "ready"), pulse = "platform" to "live", card = BoardCard("Probes", "Liveness: restart if stuck. Readiness: no traffic until ready.")),
                    Beat(20, card = BoardCard("Quiz: 0/1 ready", "Running, never restarting, no callers. What's failing?")),
                    Beat(21, light = setOf("ready"), pulse = "platform" to "ready", card = BoardCard("Right answer: readiness", "Liveness would restart them. These are running, just not open.")),
                    Beat(23, show = setOf("start"), light = setOf("start"), pulse = "platform" to "start", card = BoardCard("Startup, and endpoints", "Time for slow starters. Failing readiness: off the Service's list.")),
                    Beat(24, light = setOf("live"), card = BoardCard("Keep liveness light", "Never the database. A tiny healthz path is enough.")),
                ),
            ),
            // 25: "One more for you. A pod just sits there, Pending."
            Scene(
                25, "Can't get a table", statuses(),
                beats = listOf(
                    Beat(25, show = setOf("pod", "pending"), light = setOf("pending"), pulse = "pod" to "pending", card = BoardCard("Pending", "No machine has room yet. Describe shows FailedScheduling.")),
                ),
            ),
            // 27: "Noted. Now Container Apps..."
            Scene(
                27, "The right door", ingress(),
                beats = listOf(
                    Beat(28, show = setOf("caller", "ing", "app"), light = setOf("ing", "app"), card = BoardCard("Quiz: 8000 and 80", "The app listens on 8000; ingress targets 80. What's wrong?")),
                    Beat(29, pulse = "ing" to "app", card = BoardCard("Right answer: target port", "The default probes knock on 80, and nobody's home.")),
                ),
            ),
            // 31: "And how would you have spotted it, darling?"
            Scene(
                31, "Seeing for yourself", aca(),
                beats = listOf(
                    Beat(32, show = setOf("rev", "sys"), light = setOf("sys"), pulse = "rev" to "sys", card = BoardCard("System logs", "The platform's messages: the probe failing on port 80.")),
                    Beat(34, card = execCode),
                    Beat(35, show = setOf("con", "la"), light = setOf("con"), card = BoardCard("Quiz: last Tuesday", "The log stream shows nothing. Where do you look?")),
                    Beat(36, light = setOf("la"), pulse = "con" to "la", card = BoardCard("Right answer: Log Analytics", "The stream is live; history is queried with KQL.")),
                ),
            ),
            // 38: "Last question, for the exam. What do we remember?"
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(39, show = all6, light = setOf("pull", "crash", "ready", "port", "old"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
