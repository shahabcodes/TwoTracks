package com.shahaabapps.core.content

/** Azure Container Apps: environments, revisions and KEDA scaling: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesContainerApps {
    private val classic = Podcast(
        "ai-container-apps",
        listOf(
            maya("Hi, and welcome back. Today, a place to run your containers where you never look after a server. A container image, remember, is your app packed with everything it needs, like a lunchbox. Azure Container Apps runs those lunchboxes for you. You hand it an image, and it runs copies, adds more when it's busy, and removes them when it's quiet."),
            leo("Removes them all? So when nobody's using my app, it costs nothing?"),
            maya("It can go all the way down to none. That's called scale to zero. Under the hood, Container Apps uses Kubernetes, the big open-source system for running containers across many machines. Kubernetes is powerful, but running it yourself is a whole job. Container Apps runs it for you, and you never touch it."),
            leo("Like eating at a restaurant instead of running the kitchen."),
            maya("Exactly. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. It has two containers. Coach-api answers the phone app. Coach-worker does slow jobs in the background, like preparing a learner's notes for search."),
            leo("And where do those two live?"),
            maya("In an environment. That's the fence around a group of container apps. Inside it, they share a private network and one shared log book, and they can call each other by name. Like flats in one building, sharing a post room."),
            leo("And how does the phone get through the fence to coach-api?"),
            maya("Through ingress, the front door. External ingress opens it to the internet. Internal ingress opens it only to neighbours in the same environment. You also give it the target port, the numbered door your app listens behind, which is 8000 for the Coach."),
            leo("So coach-api gets external. And the worker?"),
            maya("No ingress at all. The worker never takes web requests. It takes jobs from a queue. Hold on to that, because it matters later, when we talk about scaling."),
            leo("Okay. What about secrets, like the key for Azure OpenAI, the AI service the Coach talks to?"),
            maya("Secrets are stored on the app, apart from the normal settings. Your code reads environment variables, named values it gets when it starts. So you make an environment variable that points at the secret, using the word secretref, then the secret's name. The code just sees the value."),
            leo("Can the secret live in Key Vault instead?"),
            maya("It can. Key Vault is Azure's safe for secrets. The app's secret becomes a reference to the safe, read with the app's managed identity, a badge Azure gives the app. So no password sits anywhere."),
            leo("And when I change a secret, the app picks it up?"),
            maya("Here's a trap. Changing a secret restarts nothing. Running containers keep the old value, because they only read it when they start. So after you change a secret, you restart the revision."),
            leo("Revision. Plain words, please."),
            maya("A revision is a frozen snapshot of your app: the image, the environment variables, the scale rules. Like a saved version of a document. Change any of those, and Container Apps makes a new revision. Change secrets, ingress or traffic, and no new revision is made; those apply to all of them."),
            leo("So a new image tag means a new revision, but a new secret doesn't."),
            maya("Exactly, and that's a classic exam question. Now, revision mode. Single mode is the default. One revision is active at a time. When a new one is ready, it gets all the traffic, so there's no downtime, no moment where the app can't be reached, and the old one is switched off for you."),
            leo("And the other mode?"),
            maya("Multiple mode. Several revisions stay active together. That's what makes traffic splitting possible: sending set percentages of users to each revision. For example, 80 percent to the old version and 20 percent to the new one, to see how it behaves before everyone gets it."),
            leo("A taste test. What if my testers want the new one on purpose?"),
            maya("Give it a label. A label is a stable web address for one particular revision, like a name on a door. Testers open the beta address and always reach the new revision, whatever the percentages say."),
            leo("And if the new one is bad?"),
            maya("Move the traffic back. Rolling back is just changing the percentages. When you're happy, you go to 100 percent, and switch the old revision off later."),
            leo("Now scaling. You keep saying copies. What's a copy called?"),
            maya("A replica: one running copy of a revision. Each revision scales between a minimum and a maximum number of replicas. Out of the box, that's zero and ten."),
            leo("And who decides when to add one?"),
            maya("KEDA. The letters stand for Kubernetes Event-driven Autoscaling. In plain words, it's a watcher. It looks at something, like web traffic or a queue, and adds replicas when things get busy. You tell it what to watch with a scale rule."),
            leo("What can a scale rule watch?"),
            maya("Web traffic: how many requests each replica handles at once. Or CPU and memory: how hard the machine is working. Or an event source, like a queue in Service Bus, Azure's message queue service. Picture a rail of order tickets in a kitchen, waiting for a cook."),
            leo("So the worker watches its queue."),
            maya("Right. The worker gets a Service Bus scale rule, with a minimum of zero replicas. At night the queue is empty: no replicas, no cost. When jobs arrive, KEDA wakes the worker, and adds replicas as the queue grows."),
            leo("Why not just scale on CPU? Sounds simpler."),
            maya("Because a CPU rule can never scale to zero. With nothing running, there's nothing to measure. That's a favourite trap. Another: an app with no ingress and no scale rule, at a minimum of zero, scales to zero and never wakes up. Give it a rule, or a minimum of one."),
            leo("[sigh] Asleep forever. Is there anything that's meant to stop?"),
            maya("Jobs. A Container Apps job is a container that runs and then ends: started by hand, on a schedule, or once per batch of queue messages. The Coach's nightly refresh of every note is a scheduled job, not an app."),
            leo("[chuckle] Nice. And the usual mistakes?"),
            maya("Expecting traffic splitting in single mode. Changing a secret and not restarting. Scaling a queue worker on CPU. Leaving an app with no ingress and no rule at zero. And a connection string, the address and password in one line, sitting in a plain variable. The App Service containers episode is the other home for these images."),
            leo("So what do I remember for the exam?"),
            maya("If the question says send 20 percent of users to a new version, the answer is multiple revision mode with traffic splitting. If testers need a stable address, a label. If a changed secret isn't seen, restart the revision. If a worker must cost nothing when the queue is empty, minimum zero with a Service Bus rule. And a new image makes a new revision; a new secret doesn't."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create an environment and a container app from a sample image, with external ingress. Change an environment variable, and list the revisions. Switch to multiple revision mode, and split traffic fifty fifty. Then set min replicas to zero, wait, and watch the replica count drop."),
            leo("Copies that sleep when I do. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-container-apps",
        listOf(
            maya("Welcome to Case Files. Today's case: The Gym That Never Closed. A small Android app called GymLog records workouts. Its Python backend runs on Azure Container Apps. Every Sunday a worker builds each member's weekly summary. And at the end of the first month, the bill said the worker had been running every hour of every night."),
            leo("[chuckle] Detective Leo, in trainers. A worker that never rests. First, plain words: what's Container Apps?"),
            maya("Azure runs your containers for you. A container image is your app packed with all it needs, like a lunchbox. You hand Container Apps the image, and it runs copies, more when busy, fewer when quiet, even none. Under the picture, it's Kubernetes, the big system for running containers on many machines, run by Azure so you never touch it."),
            leo("And the copies have a name?"),
            maya("Replicas. One replica is one running copy. GymLog has two apps: gym-api, which the phone talks to, and gym-worker, which reads jobs from a queue in Service Bus, Azure's message queue service. A queue is a waiting line of messages, like order tickets on a kitchen rail."),
            leo("And where do the two apps live?"),
            maya("In an environment, the fence around a group of apps. Inside, they share a private network and one log book, and call each other by name. Gym-api has external ingress, its front door open to the internet, on port 8000, the numbered door it listens behind."),
            leo("My first suspect: the worker has its front door open, and the whole internet keeps it awake."),
            maya("Good guess, but no. Unlike gym-api, the worker has no ingress at all. Here's the first clue. Its minimum is one replica, and it scales on CPU, how hard the machine is working."),
            leo("And that's a problem because?"),
            maya("Because a minimum of one keeps one replica running all night, every night. And a CPU rule can never reach zero, because with nothing running, there's nothing to measure. The worker should watch the queue instead."),
            leo("Watch it how?"),
            maya("With KEDA, which stands for Kubernetes Event-driven Autoscaling. It's a watcher. You give the worker a scale rule for the Service Bus queue, and a minimum of zero. Under the hood, KEDA checks the queue about every thirty seconds. Messages waiting, it wakes the worker and adds replicas. Queue empty for about five minutes, back to zero."),
            leo("Case closed. Cheap gym."),
            maya("Not yet. Second clue. Someone tidied up and deleted the scale rule entirely. Now the worker sits at zero, and Sunday's summaries never arrive."),
            leo("[sigh] The gym that never opened. So it's asleep, and nothing wakes it?"),
            maya("Exactly. No ingress and no scale rule, at a minimum of zero, means it scales to zero and never wakes. Put the queue rule back. Or, if you can't, a minimum of one."),
            leo("Third clue?"),
            maya("The team replaced the queue's connection string, the address and password in one line. It's stored as a secret on the worker, and an environment variable, a named value the code reads at start, points to it with the word secretref. But the running worker kept using the old one, and failed."),
            leo("Suspect: they needed a new revision. Change the image tag."),
            maya("[pause] Close, but a sledgehammer. A revision is a frozen snapshot of the app: image, environment variables and scale rules. Changing a secret makes no new revision, and running containers only read secrets when they start. So: restart the revision. Better still, give the worker a managed identity, a badge from Azure, and no connection string at all."),
            leo("And if a secret really must exist?"),
            maya("Keep it in Key Vault, Azure's safe for secrets. The app's secret becomes a Key Vault reference, read with that managed identity. Change it in the safe, restart the revision, and the new value arrives. The worker found its queue again by teatime."),
            leo("Fourth clue?"),
            maya("Release day. The team wanted ten percent of members on the new gym-api first. They pushed the new image, and every single member got it at once, bugs and all."),
            leo("Suspect: they forgot to set the percentages."),
            maya("They set them. They just couldn't take effect. The app was in single revision mode, the default, where the newest ready revision always gets all the traffic. Traffic splitting, sending set percentages to different revisions, needs multiple revision mode, where several revisions stay active together."),
            leo("And how did the testers reach the new one before members did?"),
            maya("A label: a stable web address for one particular revision. The testers opened the beta address and always got the new version. Rolling back was just moving the traffic back."),
            leo("And the Sunday summary itself? It runs once and stops."),
            maya("Final clue, and it's a design one. Work that runs and then ends shouldn't be an app at all. It's a Container Apps job, and this one is a scheduled job, on a timer every Sunday. Jobs can also be started by hand, or once per batch of queue messages."),
            leo("And where does all this stop working?"),
            maya("Waking from zero takes a few moments, so the first job after a quiet night waits a little. Each app sits in an environment, the fence around a group of apps that share a private network and logs. And where the image comes from is the registry episode."),
            leo("So, the case summary for the exam?"),
            maya("If a worker must cost nothing with an empty queue, the answer is minimum zero and a Service Bus scale rule, not CPU. If a changed secret isn't seen, restart the revision. If ten percent of users should get the new version, multiple revision mode and traffic splitting. If testers need a stable address, a label. And work that runs then ends is a job."),
            leo("Case closed. Anything to try today?"),
            maya("Create an environment and a container app from a sample image, with external ingress. Change an environment variable, and list the revisions. Switch to multiple revision mode, and split traffic fifty fifty. Then set min replicas to zero, wait, and watch the replica count drop."),
            leo("A gym that closes at night. Finally. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-container-apps",
        listOf(
            maya("Okay, did you hear what happened when the Coach team moved to Container Apps this week? Pull up a chair. The Two Tracks Coach, the study assistant in the Android app, with its Python backend. New home, new mistakes."),
            leo("[laugh] I heard someone's app has been asleep since Thursday and nobody can wake it. Start at the start. What's Container Apps?"),
            maya("Azure runs your containers for you. A container image is your app packed with what it needs, like a lunchbox. You hand Container Apps the image, and it runs copies, called replicas, adding more when busy and removing them when quiet, even down to none. Under the picture, it's Kubernetes, the big system for running containers on many machines, which Azure runs so you never touch it."),
            leo("And the Coach is what, exactly?"),
            maya("Two apps. Coach-api answers the phone, through ingress, the front door: external for the internet. Coach-worker has no front door; it takes jobs from a queue in Service Bus, Azure's message queue service. Both live in one environment, the fence around a group of apps, sharing a private network and a log book."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. Tom wanted to try the new coach-api on twenty percent of learners first. He set the traffic to eighty and twenty, deployed, and everyone got the new version."),
            leo("Everyone? Why?"),
            maya("Because the app was in single revision mode, the default. A revision is a frozen snapshot of the app: its image, environment variables and scale rules. In single mode, only one is active, and the newest ready one gets all the traffic."),
            leo("So how should Tom have done it?"),
            maya("Multiple revision mode, where several revisions stay active together. Then traffic splitting works: set percentages to each revision. And a label, a stable web address for one revision, so testers can reach the new one on purpose. Tom's twenty percent arrived on Tuesday."),
            leo("And when the new version had a bug?"),
            maya("Tom moved the traffic back. In multiple mode, rolling back is just changing the percentages, with nothing to deploy again. Tom says he's never moved so fast."),
            leo("Tuesday?"),
            maya("Dana. Dana replaced the OpenAI key. That's the key for Azure OpenAI, the AI service the Coach talks to. It's stored as a secret on the app, and an environment variable, a named value the code reads at start, points at it with secretref. Dana changed the secret, and the app kept using the old key all day."),
            leo("Let me guess. It needed a new revision."),
            maya("That's what Dana thought too. But changing a secret makes no revision, and running containers only read secrets when they start. You just restart the revision. And the secret can also be a Key Vault reference, read from Azure's safe with the app's managed identity, a badge Azure gives the app."),
            leo("Wednesday. Go."),
            maya("Marcus. Marcus set up scaling for the worker. Scaling is done by KEDA, Kubernetes Event-driven Autoscaling, which is a watcher: it looks at something and adds replicas when it's busy. You tell it what to watch with a scale rule. Marcus picked CPU, how hard the machine is working."),
            leo("Sounds reasonable."),
            maya("It sounds it. But a CPU rule can never scale to zero. With nothing running, there's nothing to measure. So the worker ran all night with an empty queue, and finance noticed. The right rule watches the Service Bus queue, with a minimum of zero replicas."),
            leo("How does the queue rule actually work?"),
            maya("Under the hood, KEDA checks the queue about every thirty seconds. When messages wait, it wakes the worker and adds replicas as the queue grows. When the queue has been empty for about five minutes, back to zero. Marcus has made his peace with it."),
            leo("Thursday. The sleeping app."),
            maya("The intern. The intern built a little reminder sender: no ingress, no scale rule, and a minimum of zero. It scaled to zero and never woke up. Nothing was watching it, and nothing could knock."),
            leo("[sigh] Poor thing. So how do you wake it?"),
            maya("Give it a scale rule for whatever it should react to, or a minimum of one. And honestly, a reminder sender that runs and then stops isn't an app at all. It's a Container Apps job: a container that runs to completion. By hand, on a schedule, or once per batch of queue messages."),
            leo("Did the intern take it well?"),
            maya("Better than Marcus. The reminder sender is now a scheduled job. It runs every morning at seven, sends its reminders, and ends. Nobody pays for it the rest of the day, and nobody has to wake it."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya set up the nightly refresh of every learner's notes, as a scheduled job. Perfect. But she put the Service Bus connection string, the address and password in one line, in a plain environment variable. Someone printed the variables in a log, and there it was."),
            leo("Priya! What should she have done?"),
            maya("Stored it as a secret and pointed the variable at it with secretref. Or better, no connection string at all: give the job a managed identity, and let the scale rule use it too. Priya rotated the string before lunch, and nobody's mentioned it since. Except us, now."),
            leo("Did anything else go wrong?"),
            maya("Only where it stops working. Waking from zero takes a few moments, so the first job after a quiet night waits a little. And where the images come from, and how the app pulls them with AcrPull, the role that only downloads, is the registry episode."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If twenty percent should get the new version, the answer is multiple revision mode with traffic splitting. If testers need a stable address, a label. If a changed secret isn't seen, restart the revision. If the worker must cost nothing with an empty queue, minimum zero and a Service Bus rule, never CPU. And work that runs then ends is a job."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create an environment and a container app from a sample image, with external ingress. Change an environment variable, and list the revisions. Switch to multiple revision mode, and split traffic fifty fifty. Then set min replicas to zero, wait, and watch the replica count drop."),
            leo("Wake the intern's app, please. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-container-apps",
        listOf(
            nova("Hey, you. It's late, the city's lights are going out one by one, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, apps that know when to sleep."),
            ivy("Apps that sleep. Unlike us. Tell me more, Nova."),
            nova("Azure Container Apps. It runs your containers for you. A container image is your app packed with all it needs, like a lunchbox. You hand over the image, and Container Apps runs copies of it. More when it's busy. Fewer when it's quiet. [pause] And when nobody needs it, none at all."),
            ivy("None at all. What's it really doing, under the picture?"),
            nova("It's Kubernetes, the big system for running containers across many machines. Usually you'd have to look after it yourself. Here, Azure runs it, and you never touch it. Each running copy of your app is called a replica."),
            ivy("Replicas. Little copies of you. Where do they live?"),
            nova("In an environment, a fence around a group of apps. Inside, they share a private network and one log book, and call each other by name. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend. Coach-api answers the phone. Coach-worker does slow jobs in the background."),
            ivy("And how does the phone get in, at this hour?"),
            nova("Through ingress, the front door. External opens it to the internet. Internal, only to neighbours inside the fence. You tell it the target port, the numbered door your app listens behind, 8000 for the Coach. The worker has no front door at all. [pause] It takes its jobs from a queue."),
            ivy("A queue. Like the one for the night bus."),
            nova("A queue in Service Bus, Azure's message queue service. A waiting line of messages, each one a job. Remember it. It's how the worker will sleep."),
            ivy("And secrets? You know I like secrets."),
            nova("Secrets live on the app, apart from the normal settings. Your code reads environment variables, named values it gets when it starts. One of them can point at a secret, with the word secretref and the secret's name. And a secret can be a Key Vault reference, read from Azure's safe with the app's managed identity, a badge from Azure."),
            ivy("So if I change a secret tonight, the app sees it right away?"),
            nova("[pause] No, and that's the gentle trap. Running containers read secrets only when they start. Changing one restarts nothing. So you restart the revision."),
            ivy("Revision. Say it slowly."),
            nova("A revision is a frozen snapshot of your app. The image, the environment variables, the scale rules. A saved version of a letter. Change any of those, and a new revision appears. Change secrets, ingress or traffic, and no new revision; those apply to every one."),
            ivy("And how many revisions can be awake at once?"),
            nova("In single mode, the default, just one. The newest ready one gets all the traffic, with no downtime, not a moment where the app is gone, and the old one is switched off. In multiple mode, several stay active together. [pause] And that lets you do traffic splitting."),
            ivy("Splitting. I'm listening."),
            nova("Sending set percentages of users to each revision. Eighty percent to the old Coach, twenty to the new one, just to see how it behaves. And if your testers want the new one on purpose, give it a label: a stable web address for that one revision."),
            ivy("And when the new one misbehaves, at three in the morning?"),
            nova("You move the traffic back. [pause] Rolling back is just changing the percentages. And when you're happy, the new one gets a hundred percent, and the old one is switched off later."),
            ivy("Now the sleeping part. Who decides when to wake up?"),
            nova("KEDA. Kubernetes Event-driven Autoscaling. A night watchman. It looks at something, web traffic, or a queue, and adds replicas when things get busy. You tell it what to watch with a scale rule. Each revision scales between a minimum and a maximum; by default, zero and ten."),
            ivy("And how does the watchman actually watch?"),
            nova("For the worker, it checks the Service Bus queue about every thirty seconds. Messages waiting, it wakes the worker and adds replicas as the line grows. After about five quiet minutes, it lets the last one go back to zero. [pause] So at night, with an empty queue, the worker costs nothing."),
            ivy("Couldn't it just watch how hard the machine works? CPU?"),
            nova("A CPU rule never reaches zero. With nothing running, there's nothing to measure. And one more, a sad one. An app with no ingress and no scale rule, at a minimum of zero, falls asleep and never wakes. Nothing watches it, nothing knocks. Give it a rule, or a minimum of one."),
            ivy("[sigh] That's the saddest thing you've said all night. Anything that's meant to stop?"),
            nova("Jobs. A Container Apps job runs, and then ends. Started by hand, on a schedule, or once per batch of queue messages. The Coach's nightly refresh of every note is a scheduled job. It does its work while you sleep, [pause] and then it sleeps too."),
            ivy("So the whole Coach, put together, for me?"),
            nova("Coach-api with external ingress on port 8000, scaling on web traffic. Coach-worker with no ingress, scaled from zero by a Service Bus rule using a managed identity. Secrets as Key Vault references. And a scheduled job for the nightly refresh. [pause] Everyone sleeps when they can."),
            ivy("Where does all this stop working?"),
            nova("Waking from zero takes a few moments, so the first job after a quiet night waits a little. A connection string, the address and password in one line, never belongs in a plain variable. And where the images come from is the registry episode, your next late night."),
            ivy("Mm. Before you send me to bed. What do I remember for the exam?"),
            nova("If twenty percent of users should get the new version, the answer is multiple revision mode with traffic splitting. If testers need a stable address, a label. If a changed secret isn't seen, restart the revision. If a worker must cost nothing with an empty queue, minimum zero with a Service Bus rule. And a new image makes a revision; a new secret doesn't."),
            ivy("And something to try today, before I sleep?"),
            nova("Create an environment and a container app from a sample image, with external ingress. Change an environment variable, and list the revisions. [pause] Switch to multiple revision mode, and split traffic fifty fifty. Then set min replicas to zero, wait, and watch the replica count drop."),
            ivy("Down to zero, like me in a minute. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-container-apps",
        listOf(
            nova("Tonight is date night, darling. Low lights, a little wine, and you and me, Rafe, talking about something that knows exactly when to turn up, and when to go home."),
            rafe("That's not me. I never know when to go home."),
            nova("[laugh] I know. Azure Container Apps. It runs your containers for you. A container image is your app packed with all it needs, like an overnight bag. You hand it the image, and it runs copies, called replicas. More when it's busy, fewer when it's quiet, and none when nobody needs it."),
            rafe("None at all? So I'm only around when I'm wanted."),
            nova("Exactly. Under the picture, it's Kubernetes, the big system for running containers on many machines, run by Azure so you never touch it. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend. Coach-api answers the phone. Coach-worker takes jobs from a queue in Service Bus, Azure's message queue service."),
            rafe("And they live together?"),
            nova("In an environment, a fence around a group of apps, with a shared private network and one log book. They can call each other by name. Coach-api has ingress, the front door, set to external so the phone can reach it. The worker keeps its door shut."),
            rafe("A worker with boundaries. I respect that. And secrets?"),
            nova("Stored on the app, apart from normal settings. An environment variable, a named value the code reads when it starts, points at a secret with the word secretref. Or the secret is a Key Vault reference, read from Azure's safe with a managed identity, a badge from Azure."),
            rafe("Then let me test you, darling. Exam style. You change a secret, and the running containers keep using the old value. Why?"),
            nova("Because they only read secrets when they start, and changing a secret restarts nothing and makes no new revision. So you restart the revision."),
            rafe("Right answer. Old habits die hard, but a restart helps. [kiss]"),
            nova("Mm. And a revision, for anyone listening, is a frozen snapshot of the app: image, environment variables, scale rules. Change one of those, and you get a new revision. My turn to quiz you. Which change to coach-api creates a new revision: a new image tag, a new secret value, or new traffic weights?"),
            rafe("The new image tag. The image is part of the snapshot. Secrets and traffic are settings across every revision."),
            nova("Right answer. You know what's part of you. [kiss]"),
            rafe("Now, a delicate one. We want twenty percent of learners to try the new coach-api first. We set the traffic, and everyone gets it. Why?"),
            nova("We're in single revision mode, the default, where the newest ready revision gets all the traffic. Traffic splitting, set percentages to each revision, needs multiple mode, where several revisions stay active together."),
            rafe("Right answer. Some of us like to keep more than one option open. [kiss]"),
            nova("[chuckle] Careful. And if the testers want the new one on purpose, a label: a stable web address for one revision."),
            rafe("And if the new one misbehaves?"),
            nova("We move the traffic back. Rolling back is just changing the percentages. No drama, nothing to deploy again, darling."),
            rafe("Now my favourite part. Who decides when to call in more replicas?"),
            nova("KEDA, Kubernetes Event-driven Autoscaling. A watcher. It looks at web traffic or a queue and adds replicas when things get busy. You tell it what to watch with a scale rule. It checks the queue about every thirty seconds, and after about five quiet minutes lets the last replica go."),
            rafe("Quiz, then. The worker should cost nothing while the queue is empty, and grow with the backlog. What do we set?"),
            nova("A CPU rule, and a minimum of one."),
            rafe("[sigh] No kiss for that one. A CPU rule can never reach zero, and a minimum of one pays all night. It's a minimum of zero, and a Service Bus scale rule. Sometimes the best thing is to go quiet and wait to be called."),
            nova("Point taken. I'll wait by the phone. Your question back: an app has no ingress and no scale rule, at a minimum of zero. What happens?"),
            rafe("It scales to zero and never wakes. Nothing watches it, and nobody can knock. Give it a rule, or a minimum of one."),
            nova("Right answer. Nobody should be left asleep forever. [kiss]"),
            rafe("And something that's meant to leave after the job's done?"),
            nova("A Container Apps job: a container that runs and then ends. By hand, on a schedule, or once per batch of queue messages. The Coach's nightly refresh of every note is a scheduled job. In and out, no lingering."),
            rafe("So the whole Coach, put together?"),
            nova("Coach-api with external ingress, scaling on web traffic. Coach-worker with no front door, scaled from zero by a Service Bus rule with a managed identity. Secrets as Key Vault references. And a scheduled job for the nightly refresh. Everyone sleeps when they can."),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("Waking from zero takes a few moments, so the first job after a quiet night waits a little. A connection string, the address and password in one line, never goes in a plain variable. And the registry episode covers where the images come from."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("If twenty percent should get the new version, the answer is multiple revision mode with traffic splitting. If testers need a stable address, a label. If a changed secret isn't seen, restart the revision. If the worker must cost nothing with an empty queue, minimum zero and a Service Bus rule. And a new image makes a revision; a new secret doesn't."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, my love?"),
            rafe("Create an environment and a container app from a sample image, with external ingress. Change an environment variable, and list the revisions. Switch to multiple revision mode, and split traffic fifty fifty. Then set min replicas to zero, wait, and watch the replica count drop."),
            nova("And I know who never scales to zero for me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun apps(api: String, worker: String) = DiagramSpec(
        "One environment: the API behind external ingress, and a worker with no ingress that reads a Service Bus queue.",
        nodes = listOf(
            DNode("env", "Environment", 0.5f, 0.1f, w = 0.9f, h = 0.16f, tone = 2, sub = "shared network and logs"),
            DNode("api", api, 0.25f, 0.42f, w = 0.42f, h = 0.2f, tone = 1, sub = "external ingress"),
            DNode("worker", worker, 0.75f, 0.42f, w = 0.42f, h = 0.2f, sub = "no ingress"),
            DNode("phone", "Phone", 0.12f, 0.8f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("sb", "Service Bus queue", 0.66f, 0.8f, w = 0.5f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "jobs waiting"),
        ),
        edges = listOf(
            DEdge("phone", "api", "port 8000"),
            DEdge("api", "sb", "jobs"),
            DEdge("sb", "worker", "reads"),
        ),
        aspect = 0.8f,
    )

    private fun secrets() = DiagramSpec(
        "A secret on the app, or a Key Vault reference, reaches the code through an environment variable with secretref.",
        nodes = listOf(
            DNode("sec", "Secret on the app", 0.25f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "stored apart"),
            DNode("kv", "Key Vault", 0.75f, 0.18f, w = 0.4f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "via managed identity"),
            DNode("env", "Environment variable", 0.5f, 0.55f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "secretref: the secret's name"),
            DNode("cont", "Running replicas", 0.5f, 0.86f, w = 0.5f, h = 0.18f, sub = "read it at start"),
        ),
        edges = listOf(
            DEdge("kv", "sec", "reference"),
            DEdge("sec", "env", "secretref"),
            DEdge("env", "cont", "at start"),
        ),
        aspect = 0.8f,
    )

    private fun snapshot() = DiagramSpec(
        "Changing the template makes a new revision; secrets, ingress and traffic make none, and a secret needs a restart.",
        nodes = listOf(
            DNode("template", "Revision template", 0.5f, 0.15f, w = 0.7f, h = 0.2f, tone = 1, sub = "image, variables, scale rules"),
            DNode("newrev", "New revision", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "image tag changed"),
            DNode("same", "No new revision", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "secret, ingress, traffic"),
            DNode("single", "Single mode", 0.25f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "newest gets all traffic"),
            DNode("restart", "Restart the revision", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "to see a new secret"),
        ),
        edges = listOf(
            DEdge("template", "newrev", "change"),
            DEdge("template", "same", "settings"),
            DEdge("newrev", "single"),
            DEdge("same", "restart", "secret changed"),
        ),
        aspect = 0.8f,
    )

    private fun splitting() = DiagramSpec(
        "In multiple revision mode, ingress splits traffic by weight, and a label gives one revision its own address.",
        nodes = listOf(
            DNode("ing", "Ingress", 0.5f, 0.14f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "traffic weights"),
            DNode("r1", "Revision v1", 0.25f, 0.5f, w = 0.42f, h = 0.22f, sub = "80 percent"),
            DNode("r2", "Revision v2", 0.75f, 0.5f, w = 0.42f, h = 0.22f, sub = "20 percent"),
            DNode("mode", "Multiple mode", 0.25f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "several active"),
            DNode("label", "Label: beta", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "its own address"),
        ),
        edges = listOf(
            DEdge("ing", "r1", "80%"),
            DEdge("ing", "r2", "20%"),
            DEdge("mode", "r1", "both active", dashed = true),
            DEdge("label", "r2", "testers"),
        ),
        aspect = 0.8f,
    )

    private fun scaling() = DiagramSpec(
        "KEDA polls the queue and adds or removes replicas, down to zero; a CPU rule never reaches zero, and no rule never wakes.",
        nodes = listOf(
            DNode("sb", "Service Bus queue", 0.25f, 0.16f, w = 0.44f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "jobs waiting"),
            DNode("keda", "KEDA scale rule", 0.75f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "checks the queue"),
            DNode("reps", "Replicas", 0.5f, 0.52f, w = 0.6f, h = 0.2f, tone = 1, sub = "0 when empty, more when busy"),
            DNode("cpu", "CPU rule", 0.25f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "never reaches zero"),
            DNode("none", "No ingress, no rule", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "at zero, never wakes"),
        ),
        edges = listOf(
            DEdge("keda", "sb", "polls"),
            DEdge("keda", "reps", "adds, removes"),
            DEdge("cpu", "reps", dashed = true),
            DEdge("none", "reps", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun jobs(work: String) = DiagramSpec(
        "A Container Apps job runs and then ends: started by hand, on a schedule, or per batch of queue messages.",
        nodes = listOf(
            DNode("manual", "Manual job", 0.17f, 0.25f, w = 0.3f, h = 0.26f, sub = "you start it"),
            DNode("sched", "Scheduled job", 0.5f, 0.25f, w = 0.3f, h = 0.26f, tone = 1, sub = "on a timer"),
            DNode("event", "Event job", 0.83f, 0.25f, w = 0.3f, h = 0.26f, sub = "per queue batch"),
            DNode("work", work, 0.5f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "runs, then ends"),
        ),
        edges = listOf(DEdge("work", "sched", "a scheduled job")),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where it stops: waking from zero takes a moment, strings stay out of plain variables, and run-once work is a job.",
        nodes = listOf(
            DNode("wake", "Waking takes a moment", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "the first job waits"),
            DNode("conn", "No plain connection string", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "a secret or an identity"),
            DNode("job", "Runs, then ends?", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "make it a job"),
            DNode("next", "Other episodes", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "ACR, App Service"),
        ),
        edges = listOf(DEdge("conn", "next", "identity and AcrPull")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Container Apps questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("split", "20 percent to new", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "multiple mode, split"),
            DNode("label", "Stable test address", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a label"),
            DNode("secret", "Changed secret unseen", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "restart the revision"),
            DNode("zero", "Free on an empty queue", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "min 0, Service Bus rule"),
            DNode("rev", "What makes a revision?", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a new image, not a secret"),
            DNode("job", "Runs, then ends", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a Container Apps job"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("split", "label", "secret", "zero", "rev", "job")
    private val jobKinds = setOf("manual", "sched", "event", "work")
    private val limitNodes = setOf("wake", "conn", "job", "next")
    private val today = BoardCard("Today", "An environment with ingress; change a variable; multiple mode 50/50; min 0.")
    private val secretRef = BoardCard("Environment variable", "OPENAI_KEY=secretref:openai-key", code = true)
    private val trafficCode = BoardCard("Azure CLI", "az containerapp ingress traffic set --revision-weight coach-api--v1=80 coach-api--v2=20", code = true)
    private val queueRule = BoardCard("The queue rule", "Checks about every 30 seconds; after 5 quiet minutes, back to zero.")
    private val wholeCoach = BoardCard("The whole Coach", "External api, a worker from zero, Key Vault secrets, a nightly job.")
    private val whereItStops = BoardCard("Where it stops", "Waking takes a moment. No plain strings. Run-once work is a job.")

    private fun classicBoard() = PodcastBoard(
        "ai-container-apps",
        listOf(
            // 0: "Hi, and welcome back. Today, a place to run your containers..."
            Scene(
                0, "Containers, no servers", apps("coach-api", "coach-worker"),
                beats = listOf(
                    Beat(0, card = BoardCard("Container Apps", "Hand over an image; Azure runs the copies, more or fewer, even none.")),
                    Beat(2, card = BoardCard("Scale to zero", "Nobody using it, nothing running. Kubernetes runs underneath.")),
                    Beat(4, show = setOf("api", "worker"), light = setOf("api", "worker"), card = BoardCard("Two Tracks Coach", "coach-api answers the phone; coach-worker does slow background jobs.")),
                    Beat(6, show = setOf("env"), light = setOf("env"), card = BoardCard("Environment", "The fence around a group of apps: one network, one log book.")),
                    Beat(8, show = setOf("phone"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Ingress", "The front door: external for the internet, target port 8000.")),
                    Beat(10, show = setOf("sb"), light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("No ingress", "The worker takes its jobs from a queue instead.")),
                ),
            ),
            // 11: "Okay. What about secrets..."
            Scene(
                11, "Secrets", secrets(),
                beats = listOf(
                    Beat(12, show = setOf("sec", "env"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(14, show = setOf("kv"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Key Vault reference", "The secret lives in Azure's safe, read with a managed identity.")),
                    Beat(16, show = setOf("cont"), light = setOf("cont"), pulse = "env" to "cont", card = BoardCard("Restart the revision", "A changed secret restarts nothing; containers read it at start.")),
                ),
            ),
            // 17: "Revision. Plain words, please."
            Scene(
                17, "Revisions", snapshot(),
                beats = listOf(
                    Beat(18, show = setOf("template", "newrev", "same"), light = setOf("template"), pulse = "template" to "newrev", card = BoardCard("Revision", "A frozen snapshot: image, environment variables, scale rules.")),
                    Beat(19, light = setOf("newrev", "same"), pulse = "template" to "same", card = BoardCard("New tag, new revision", "A new secret, ingress or traffic weights make none.")),
                    Beat(20, show = setOf("single", "restart"), light = setOf("single"), pulse = "newrev" to "single", card = BoardCard("Single mode", "The default: the newest ready revision gets all the traffic.")),
                ),
            ),
            // 21: "And the other mode?"
            Scene(
                21, "Splitting traffic", splitting(),
                beats = listOf(
                    Beat(22, show = setOf("ing", "r1", "r2", "mode"), light = setOf("mode", "r2"), pulse = "ing" to "r2", card = trafficCode),
                    Beat(24, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = BoardCard("Label", "A stable web address for one revision, for the testers.")),
                    Beat(26, light = setOf("r1"), pulse = "ing" to "r1", card = BoardCard("Roll back", "Move the traffic back: just change the percentages.")),
                ),
            ),
            // 27: "Now scaling. You keep saying copies..."
            Scene(
                27, "Replicas and KEDA", scaling(),
                beats = listOf(
                    Beat(28, show = setOf("reps"), light = setOf("reps"), card = BoardCard("Replica", "One running copy of a revision. By default, between 0 and 10.")),
                    Beat(30, show = setOf("keda"), light = setOf("keda"), pulse = "keda" to "reps", card = BoardCard("KEDA", "Kubernetes Event-driven Autoscaling: a watcher that adds replicas.")),
                    Beat(32, show = setOf("sb"), light = setOf("sb"), pulse = "keda" to "sb", card = BoardCard("Scale rules", "Web traffic, CPU or memory, or a queue like Service Bus.")),
                    Beat(34, light = setOf("reps", "sb"), pulse = "keda" to "reps", card = BoardCard("The worker", "A Service Bus rule, minimum zero: free while the queue is empty.")),
                    Beat(36, show = setOf("cpu", "none"), light = setOf("cpu", "none"), pulse = "cpu" to "reps", card = BoardCard("Two traps", "CPU never reaches zero. No ingress and no rule never wakes.")),
                ),
            ),
            // 37: "Asleep forever. Is there anything that's meant to stop?"
            Scene(
                37, "Jobs", jobs("Nightly note refresh"),
                beats = listOf(
                    Beat(38, show = jobKinds, light = setOf("sched", "work"), pulse = "work" to "sched", card = BoardCard("Container Apps job", "Runs, then ends: by hand, on a schedule, or per queue batch.")),
                ),
            ),
            // 39: "Nice. And the usual mistakes?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = setOf("split", "secret", "zero"), light = setOf("split", "secret", "zero"), card = BoardCard("The usual mistakes", "Splits in single mode, no restart, CPU workers, plain strings.")),
                    Beat(42, show = setOf("label", "rev", "job"), light = setOf("split", "label", "secret", "zero", "rev"), card = BoardCard("Listen for the phrase", "Multiple mode, a label, a restart, zero and a queue rule, the image.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-container-apps--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Gym That Never Closed..."
            Scene(
                0, "The Gym That Never Closed", apps("gym-api", "gym-worker"),
                beats = listOf(
                    Beat(0, show = setOf("worker"), light = setOf("worker"), card = BoardCard("The case", "GymLog's worker ran every hour of every night.")),
                    Beat(2, card = BoardCard("Container Apps", "Azure runs your containers: more copies when busy, none when quiet.")),
                    Beat(4, show = setOf("sb"), light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("Replicas and a queue", "Running copies. The worker reads a Service Bus queue.")),
                    Beat(6, show = setOf("env", "api", "phone"), light = setOf("env", "api"), pulse = "phone" to "api", card = BoardCard("Environment", "A fence with a shared network. gym-api has external ingress.")),
                ),
            ),
            // 7: "My first suspect: the worker has its front door open..."
            Scene(
                7, "The first clue", scaling(),
                beats = listOf(
                    Beat(8, show = setOf("reps", "cpu"), light = setOf("cpu"), pulse = "cpu" to "reps", card = BoardCard("Clue one", "No ingress at all. Minimum one replica, scaling on CPU.")),
                    Beat(10, light = setOf("cpu", "reps"), card = BoardCard("CPU never reaches zero", "Nothing running, nothing to measure. Min one pays all night.")),
                    Beat(12, show = setOf("sb", "keda"), light = setOf("keda", "sb"), pulse = "keda" to "sb", card = queueRule),
                ),
            ),
            // 13: "Case closed. Cheap gym."
            Scene(
                13, "The gym that never opened", scaling(),
                beats = listOf(
                    Beat(14, show = setOf("reps", "none"), light = setOf("none"), pulse = "none" to "reps", card = BoardCard("Clue two", "The scale rule was deleted. The summaries never arrived.")),
                    Beat(16, show = setOf("sb", "keda", "cpu"), light = setOf("keda"), pulse = "keda" to "reps", card = BoardCard("Never wakes", "No ingress, no rule, minimum zero. Put the rule back, or min one.")),
                ),
            ),
            // 17: "Third clue?"
            Scene(
                17, "The old connection string", secrets(),
                beats = listOf(
                    Beat(18, show = setOf("sec", "env", "cont"), light = setOf("env"), pulse = "sec" to "env", card = BoardCard("Environment variable", "SB_CONNECTION=secretref:sb-conn", code = true)),
                    Beat(20, light = setOf("cont"), pulse = "env" to "cont", card = BoardCard("Restart the revision", "A changed secret makes no revision; containers read it at start.")),
                    Beat(22, show = setOf("kv"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Key Vault reference", "Keep it in the safe, read with a managed identity.")),
                ),
            ),
            // 23: "Fourth clue?"
            Scene(
                23, "Release day", splitting(),
                beats = listOf(
                    Beat(24, show = setOf("ing", "r1", "r2"), light = setOf("r2"), pulse = "ing" to "r2", card = BoardCard("Everyone got it", "Ten percent was the plan. Single mode sent everyone.")),
                    Beat(26, show = setOf("mode"), light = setOf("mode"), pulse = "mode" to "r1", card = BoardCard("Multiple mode", "Several revisions active, so traffic splitting can work.")),
                    Beat(28, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = BoardCard("Label", "A stable address for testers. Roll back by moving the traffic.")),
                ),
            ),
            // 29: "And the Sunday summary itself? It runs once and stops."
            Scene(
                29, "The Sunday summary", jobs("Weekly summaries"),
                beats = listOf(
                    Beat(30, show = jobKinds, light = setOf("sched", "work"), pulse = "work" to "sched", card = BoardCard("Scheduled job", "Runs every Sunday, then ends. Not an app at all.")),
                ),
            ),
            // 31: "And where does all this stop working?"
            Scene(
                31, "Where it stops", limits(),
                beats = listOf(
                    Beat(32, show = limitNodes, light = setOf("wake", "next"), pulse = "conn" to "next", card = whereItStops),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("zero", "secret", "split", "label", "job"), card = BoardCard("Listen for the phrase", "A queue rule at zero, a restart, multiple mode, a label, a job.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-container-apps--gossip",
        listOf(
            // 0: "Okay, did you hear what happened when the Coach team moved..."
            Scene(
                0, "The move", apps("coach-api", "coach-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("The week", "The Coach moved to Container Apps. New home, new mistakes.")),
                    Beat(2, card = BoardCard("Container Apps", "Azure runs the copies, called replicas, even down to none.")),
                    Beat(4, show = setOf("env", "phone", "worker", "sb"), light = setOf("env"), pulse = "phone" to "api", card = BoardCard("Environment", "One fence: coach-api with external ingress, coach-worker with none.")),
                ),
            ),
            // 5: "Okay. Who went first?"
            Scene(
                5, "Monday: Tom", splitting(),
                beats = listOf(
                    Beat(6, show = setOf("ing", "r1", "r2"), light = setOf("r2"), pulse = "ing" to "r2", card = BoardCard("80 and 20?", "Tom set the weights. Everyone got the new version.")),
                    Beat(8, light = setOf("r1", "r2"), card = BoardCard("Single mode", "One active revision; the newest ready one takes all the traffic.")),
                    Beat(10, show = setOf("mode", "label"), light = setOf("mode", "label"), pulse = "label" to "r2", card = BoardCard("Multiple mode and a label", "Several active, split by percentage; a stable address for testers.")),
                    Beat(12, light = setOf("r1"), pulse = "ing" to "r1", card = BoardCard("Roll back", "Move the traffic back. Nothing to deploy again.")),
                ),
            ),
            // 13: "Tuesday?"
            Scene(
                13, "Tuesday: Dana", secrets(),
                beats = listOf(
                    Beat(14, show = setOf("sec", "env", "cont"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(16, show = setOf("kv"), light = setOf("cont", "kv"), pulse = "env" to "cont", card = BoardCard("Restart the revision", "A changed secret makes no revision. Or read it from Key Vault.")),
                ),
            ),
            // 17: "Wednesday. Go."
            Scene(
                17, "Wednesday: Marcus", scaling(),
                beats = listOf(
                    Beat(18, show = setOf("reps", "keda", "cpu"), light = setOf("cpu"), pulse = "cpu" to "reps", card = BoardCard("KEDA", "A watcher with a scale rule. Marcus picked CPU.")),
                    Beat(20, light = setOf("cpu"), card = BoardCard("CPU never reaches zero", "Nothing running, nothing to measure. The worker ran all night.")),
                    Beat(22, show = setOf("sb"), light = setOf("keda", "sb"), pulse = "keda" to "sb", card = queueRule),
                ),
            ),
            // 23: "Thursday. The sleeping app."
            Scene(
                23, "Thursday: the intern", scaling(),
                beats = listOf(
                    Beat(24, show = setOf("reps", "none"), light = setOf("none"), pulse = "none" to "reps", card = BoardCard("Asleep since Thursday", "No ingress, no rule, minimum zero: it never wakes.")),
                ),
            ),
            // 25: "Poor thing. So how do you wake it?"
            Scene(
                25, "Runs, then ends", jobs("Reminder sender"),
                beats = listOf(
                    Beat(26, show = jobKinds, light = setOf("work"), card = BoardCard("Wake it, or make it a job", "A scale rule or a minimum of one. Or a job that runs to the end.")),
                    Beat(28, light = setOf("sched", "work"), pulse = "work" to "sched", card = BoardCard("Every morning at seven", "Sends its reminders, then ends. Nobody pays the rest of the day.")),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", secrets(),
                beats = listOf(
                    Beat(30, show = setOf("env", "cont"), light = setOf("env"), card = BoardCard("A plain variable", "The connection string, printed in a log for all to see.")),
                    Beat(32, show = setOf("sec", "kv"), light = setOf("sec", "kv"), pulse = "sec" to "env", card = BoardCard("A secret or an identity", "A secret with secretref, or a managed identity and no string.")),
                ),
            ),
            // 33: "Did anything else go wrong?"
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, show = limitNodes, light = setOf("wake", "next"), pulse = "conn" to "next", card = whereItStops),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("split", "label", "secret", "zero", "job"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-container-apps--late-night",
        listOf(
            // 0: "Hey, you. It's late, the city's lights are going out..."
            Scene(
                0, "Apps that sleep", apps("coach-api", "coach-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api"), card = BoardCard("Tonight", "Apps that know when to sleep.")),
                    Beat(2, card = BoardCard("Container Apps", "Copies of your image: more when busy, none when quiet.")),
                    Beat(4, card = BoardCard("Under the picture", "Kubernetes, run by Azure. Each running copy is a replica.")),
                    Beat(6, show = setOf("env", "worker"), light = setOf("env"), card = BoardCard("Environment", "A fence: a shared network, one log book, names to call.")),
                    Beat(8, show = setOf("phone"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Ingress", "The front door. External, target port 8000. The worker has none.")),
                    Beat(10, show = setOf("sb"), light = setOf("sb"), pulse = "sb" to "worker", card = BoardCard("Service Bus queue", "A waiting line of jobs for the worker.")),
                ),
            ),
            // 11: "And secrets? You know I like secrets."
            Scene(
                11, "Secrets", secrets(),
                beats = listOf(
                    Beat(12, show = setOf("sec", "env", "kv"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(14, show = setOf("cont"), light = setOf("cont"), pulse = "env" to "cont", card = BoardCard("Restart the revision", "Containers read secrets at start. A change restarts nothing.")),
                ),
            ),
            // 15: "Revision. Say it slowly."
            Scene(
                15, "Frozen snapshots", snapshot(),
                beats = listOf(
                    Beat(16, show = setOf("template", "newrev", "same"), light = setOf("template"), pulse = "template" to "newrev", card = BoardCard("Revision", "Image, environment variables, scale rules. A saved letter.")),
                    Beat(18, show = setOf("single", "restart"), light = setOf("single"), pulse = "newrev" to "single", card = BoardCard("Single mode", "One awake; the newest ready one gets all the traffic.")),
                ),
            ),
            // 19: "Splitting. I'm listening."
            Scene(
                19, "Splitting", splitting(),
                beats = listOf(
                    Beat(20, show = setOf("ing", "r1", "r2", "mode", "label"), light = setOf("r2", "label"), pulse = "ing" to "r2", card = BoardCard("Traffic splitting", "Multiple mode: 80 to the old Coach, 20 to the new. A label for testers.")),
                    Beat(22, light = setOf("r1"), pulse = "ing" to "r1", card = BoardCard("Roll back", "Move the traffic back. Then 100 percent, and switch the old off.")),
                ),
            ),
            // 23: "Now the sleeping part. Who decides when to wake up?"
            Scene(
                23, "The night watchman", scaling(),
                beats = listOf(
                    Beat(24, show = setOf("keda", "reps"), light = setOf("keda"), pulse = "keda" to "reps", card = BoardCard("KEDA", "Kubernetes Event-driven Autoscaling. Between 0 and 10 by default.")),
                    Beat(26, show = setOf("sb"), light = setOf("sb"), pulse = "keda" to "sb", card = queueRule),
                    Beat(28, show = setOf("cpu", "none"), light = setOf("cpu", "none"), pulse = "cpu" to "reps", card = BoardCard("Two traps", "CPU never reaches zero. No rule, no ingress: never wakes.")),
                ),
            ),
            // 29: "That's the saddest thing you've said all night..."
            Scene(
                29, "Runs, then sleeps", jobs("Nightly note refresh"),
                beats = listOf(
                    Beat(30, show = jobKinds, light = setOf("sched", "work"), pulse = "work" to "sched", card = BoardCard("Container Apps job", "By hand, on a schedule, or per queue batch. Then it ends.")),
                    Beat(32, card = wholeCoach),
                ),
            ),
            // 33: "Where does all this stop working?"
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, show = limitNodes, light = setOf("wake", "conn", "next"), pulse = "conn" to "next", card = BoardCard("Your next late night", "The registry episode: where the images come from.")),
                ),
            ),
            // 35: "Mm. Before you send me to bed. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("split", "label", "secret", "zero", "rev"), card = BoardCard("Listen for the phrase", "Multiple mode, a label, a restart, zero and a queue rule, the image.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-container-apps--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", apps("coach-api", "coach-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api"), card = BoardCard("Date night", "Something that knows when to turn up, and when to go home.")),
                    Beat(2, card = BoardCard("Container Apps", "Copies called replicas: more when busy, none when nobody calls.")),
                    Beat(4, show = setOf("worker", "sb"), light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("Two Tracks Coach", "coach-api answers the phone; coach-worker takes jobs from a queue.")),
                    Beat(6, show = setOf("env", "phone"), light = setOf("env", "api"), pulse = "phone" to "api", card = BoardCard("Environment and ingress", "A fence with a shared network. The api's front door is external.")),
                ),
            ),
            // 8: "Stored on the app, apart from normal settings..."
            Scene(
                8, "Secrets", secrets(),
                beats = listOf(
                    Beat(8, show = setOf("sec", "env", "kv"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(9, show = setOf("cont"), light = setOf("cont"), card = BoardCard("Quiz: the old value", "A changed secret, and containers keep the old one. Why?")),
                    Beat(10, light = setOf("cont"), pulse = "env" to "cont", card = BoardCard("Right answer", "They read secrets at start. Restart the revision.")),
                ),
            ),
            // 12: "And a revision, for anyone listening, is a frozen snapshot..."
            Scene(
                12, "What's part of you", snapshot(),
                beats = listOf(
                    Beat(12, show = setOf("template", "newrev", "same"), light = setOf("template"), card = BoardCard("Quiz: a new revision", "A new image tag, a new secret, or new traffic weights?")),
                    Beat(13, light = setOf("newrev"), pulse = "template" to "newrev", card = BoardCard("Right answer", "The image tag. Secrets and traffic make no revision.")),
                ),
            ),
            // 15: "Now, a delicate one. We want twenty percent of learners..."
            Scene(
                15, "More than one option", splitting(),
                beats = listOf(
                    Beat(15, show = setOf("ing", "r1", "r2"), light = setOf("r2"), pulse = "ing" to "r2", card = BoardCard("Quiz: everyone got it", "Twenty percent planned; everyone got the new version. Why?")),
                    Beat(16, show = setOf("mode"), light = setOf("mode"), pulse = "mode" to "r1", card = BoardCard("Right answer", "Single mode. Traffic splitting needs multiple mode.")),
                    Beat(18, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = BoardCard("Label", "A stable web address for one revision, for the testers.")),
                    Beat(20, light = setOf("r1"), pulse = "ing" to "r1", card = BoardCard("Roll back", "Move the traffic back. No drama.")),
                ),
            ),
            // 21: "Now my favourite part. Who decides when to call in more replicas?"
            Scene(
                21, "Waiting to be called", scaling(),
                beats = listOf(
                    Beat(22, show = setOf("keda", "reps", "sb"), light = setOf("keda"), pulse = "keda" to "sb", card = queueRule),
                    Beat(23, light = setOf("reps"), card = BoardCard("Quiz: free when empty", "The worker costs nothing on an empty queue. What do we set?")),
                    Beat(25, show = setOf("cpu"), light = setOf("cpu"), pulse = "cpu" to "reps", card = BoardCard("No kiss: zero, a queue rule", "CPU never reaches zero, and a minimum of one pays all night.")),
                    Beat(26, show = setOf("none"), light = setOf("none"), pulse = "none" to "reps", card = BoardCard("Quiz: the sleeper", "No ingress, no rule, minimum zero. What happens?")),
                    Beat(27, light = setOf("none"), card = BoardCard("Right answer", "It never wakes. Give it a rule, or a minimum of one.")),
                ),
            ),
            // 29: "And something that's meant to leave after the job's done?"
            Scene(
                29, "In and out", jobs("Nightly note refresh"),
                beats = listOf(
                    Beat(30, show = jobKinds, light = setOf("sched", "work"), pulse = "work" to "sched", card = BoardCard("Container Apps job", "Runs, then ends: by hand, on a schedule, or per queue batch.")),
                    Beat(32, card = wholeCoach),
                ),
            ),
            // 33: "And where does it all stop working? Every romance has its limits."
            Scene(
                33, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(34, show = limitNodes, light = setOf("wake", "conn"), pulse = "conn" to "next", card = whereItStops),
                ),
            ),
            // 35: "Last question, for the exam. What do we remember?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("split", "label", "secret", "zero", "rev"), card = BoardCard("Listen for the phrase", "Multiple mode, a label, a restart, zero and a queue rule. Five for five.")),
                    Beat(39, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
