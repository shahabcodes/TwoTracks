package com.shahaabapps.core.content

/** The Compute module's Durable Functions and containers topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledCompute2 {
    private val durableCase = Podcast(
        "az-durable",
        listOf(
            maya("Welcome to Case Files. Today's case: The Ride That Charged Three Times. A small bike rental company has an Android app. When a rider ends a ride, the backend, built on Azure Functions, runs a workflow. Check the bike, charge the card, email a receipt. On Monday morning the support inbox is on fire. Some riders were charged three times for one ride."),
            leo("[chuckle] Three rides' worth of money for one wobbly bike. Detective Leo reporting. My first suspect is the rider. They tapped End Ride three times."),
            maya("Good guess, but the app's logs say one tap each. And here's the odd part. The extra charges didn't come at the same moment. They came minutes apart, each time the workflow woke up after a wait."),
            leo("Woke up? Back up. What kind of workflow is this?"),
            maya("A Durable Function. A plain function wakes up, does one short job and forgets everything. Durable Functions let you write a whole workflow that runs for minutes or days, remembers where it is, and survives a restart. The Azure Functions episode covers the plain kind."),
            leo("So a function with a memory. What are its parts?"),
            maya("Three kinds of function. The client function starts the workflow and gets back an instance id, like a ticket number. The orchestrator is the workflow itself, a foreman with a clipboard. It decides the order and waits for each step. And activities do the real work, like charging a card or sending an email."),
            leo("And where does the foreman keep the clipboard?"),
            maya("In the task hub. Under the picture, it's tables and queues in an Azure storage account. Every step is written there as an event, like activity scheduled, or activity finished with this result. That list of events is called the history."),
            leo("Okay. So why would a workflow charge someone three times?"),
            maya("Here's the mechanism, and it's the key to the case. When the orchestrator waits, it doesn't sit in memory. It's unloaded. When the answer arrives, a queue message wakes it, and it replays. It runs its code again from line one, and for each finished step it reads the result from the history instead of doing the work again."),
            leo("[pause] Unless the step isn't an activity. Did someone put the charge straight into the orchestrator?"),
            maya("That's clue one. The developer called the payment service directly inside the orchestrator, because it was quicker to write. A direct call isn't in the history. So every replay called the payment service again. One ride, three wake-ups, three charges."),
            leo("So the rule is, anything that touches the outside world goes in an activity."),
            maya("Exactly. Calls to other services, writing rows, sending emails. All of it in activities. The orchestrator only decides and waits. But the case file has a second page. Some rides didn't overcharge. They crashed, with a non-determinism error."),
            leo("Non-determinism. That sounds like a word you'd use to scare juniors."),
            maya("[laugh] It just means the code made a different decision on replay than the first time. An orchestrator must be deterministic: the same path on every replay. And this one named each receipt with Guid.NewGuid, a brand-new random id, and read DateTime.Now to decide on a late fee."),
            leo("So on replay, a new id and a new time. The history says one thing and the code says another."),
            maya("And the framework stops rather than guess. The fix is the context, the object your orchestrator is handed. It has CurrentUtcDateTime, a clock that gives the same answer on every replay, and its own NewGuid that does the same."),
            leo("Any more suspects? I'm on a roll now."),
            maya("One more. Thirty minutes after a ride, the app asks the rider to rate the bike. The developer wrote Task.Delay for that wait. Those requests arrived late, or twice."),
            leo("Let me guess. Task.Delay isn't in the history either."),
            maya("Right. Thread.Sleep and Task.Delay aren't recorded, so on replay the wait starts all over again, and Thread.Sleep blocks the host besides. The only safe way to wait is a durable timer, made from the context with CreateTimer. It's written in the history, so a replay knows it's already done."),
            leo("So the charge moves into an activity, the clock comes from the context, the wait is a durable timer. Case closed?"),
            maya("Almost. Even an activity can run more than once. Activities run at least once, so if the host restarts mid-charge, the activity can run again. So make it idempotent, meaning running it twice does no extra harm. Check whether this rental was already charged before charging it."),
            leo("Got it. And the rating question waits for the rider. Does the workflow just wait for ever?"),
            maya("That's the human interaction pattern. The orchestrator waits for an external event, a named signal sent in from outside, like Rated. And it races that event against a durable timer. If the rider answers first, save the rating. If two days pass first, the timer wins and the workflow closes."),
            leo("How does the event get in?"),
            maya("The client function hands back management addresses. The backend sends a POST to the send-event address with the event's name. And the status address shows the workflow Running, then Completed."),
            leo("Are there other patterns the exam likes?"),
            maya("Five in all. The ride is function chaining: check, charge, email, each using the last result. Fan-out, fan-in starts many activities at once and waits for all with Task.WhenAll, like checking every bike's battery at night. Async HTTP API gives a caller an answer at once and a status address to poll. And monitor loops with a durable timer until a job is done."),
            leo("And where does all this stop working?"),
            maya("Two edges. A workflow that loops for ever keeps growing its history, so it should restart itself fresh with ContinueAsNew. And if one function just hands a message to another, with no waiting and no state, you don't need an orchestrator at all. A queue does it. The Service Bus episode covers that."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If the question says wait for a person with a time-out, the answer is an external event raced against a durable timer. If it says many items in parallel and combine, fan-out, fan-in with Task.WhenAll. If the orchestrator needs the time, CurrentUtcDateTime, never DateTime.Now. And if a step calls a payment service, it belongs in an activity, made idempotent."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate it on your laptop with Azurite, a local stand-in for storage. Use func new with the Durable Functions orchestration template, dotnet-isolated. Run func start and POST to the HttpStart endpoint. Open the statusQueryGetUri from the response. Then add a 30-second durable timer, and watch runtimeStatus stay Running, then Completed."),
            leo("No riders charged, not even once. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val durableGossip = Podcast(
        "az-durable",
        listOf(
            maya("Did you hear what Sam did on the habit tracker team? Their Android app nudges you when you skip a habit, and the backend runs on Azure Functions. Sam built the nudge as a Durable Functions workflow. And on Tuesday it fell over with a non-determinism error."),
            leo("[laugh] I heard there was shouting near the coffee machine. Start at the beginning. What's a Durable Function?"),
            maya("A plain function wakes up, does one short job and forgets. Durable Functions let you write a whole workflow, step by step, that runs for hours or days, remembers where it is and survives restarts. The Azure Functions episode covers the plain ones."),
            leo("And what did Sam's workflow look like?"),
            maya("Three parts. A client function starts it when you plan a habit, and gets back an instance id. The orchestrator is the workflow, a stage manager calling the cues. And activities do the real work: read your habit log, send a push notification. Under the picture, each one is an ordinary function, with a special trigger that tells the framework which part it plays."),
            leo("So what broke?"),
            maya("Sam's orchestrator read DateTime.Now to decide whether the habit's time slot had ended. Seems harmless, and on a quiet test day it even worked. But an orchestrator doesn't stay in memory while it waits. It's unloaded, and when it wakes up, it replays from the very first line."),
            leo("Replays? It runs the whole thing again?"),
            maya("It runs the code again, but for every finished step it reads the result from the history instead of redoing it. Under the picture, the history lives in the task hub, tables and queues in a storage account, where each step is written down as an event."),
            leo("And DateTime.Now gives a new answer on every replay."),
            maya("So the code took a different path than the history said, and the framework stopped it. That's all non-determinism means. Priya sat with Sam and fixed it in a minute: read the time from the context, with CurrentUtcDateTime. Same answer on every replay. And for a random id, the context's NewGuid, never Guid.NewGuid."),
            leo("Poor Sam. Who's next?"),
            maya("Aisha. Her workflow waited five minutes between two nudges, using Task.Delay. Users got the second nudge late, or twice. She had only wanted the app to feel less naggy."),
            leo("[pause] Let me guess. Task.Delay isn't in the history."),
            maya("You're learning the office ways. Neither Task.Delay nor Thread.Sleep is recorded, so on replay the wait starts over, and Sleep blocks the host too. Priya showed her the durable timer, made from the context with CreateTimer. It's the only safe way for an orchestrator to wait."),
            leo("How does a durable timer work, if nothing stays in memory?"),
            maya("It's written to the task hub as a scheduled message, due at the right time. When it comes due, it wakes the orchestrator, which replays, finds the timer finished in its history, and carries on. No thread sits there waiting."),
            leo("And Wednesday?"),
            maya("Wednesday was Ben. Ben wanted to save the streak score, and writing to the database straight from the orchestrator was quicker. One less function to write. So the write ran again on every replay. One user's streak went from five days to forty."),
            leo("[chuckle] Honestly, I'd keep that streak."),
            maya("Dana fixed it. Anything with a side effect, a database write, an email, a call to another service, goes in an activity. The orchestrator calls it, the result goes into the history, and on replay it's read back, not written again. Then came Thursday, and the intern."),
            leo("It's always the intern. What did they do?"),
            maya("Bless them. They moved the streak update into an activity, perfect. But activities run at least once, not exactly once. A host restarted mid-write, the activity ran again, and the bonus points were added twice. Dana explained idempotent: running it twice does no extra harm. Set the streak to a value, or check first. Don't just add."),
            leo("Did anything good happen this week?"),
            maya("Marcus used the human interaction pattern, nicely. The app lets a friend approve your cheat day. His orchestrator waits for an external event, a named signal from outside called Approved, raced against a durable timer. If the friend answers first, cheat day granted. If a day passes first, the timer wins, and no cheat day."),
            leo("Harsh but fair. Any other patterns I should know?"),
            maya("Five, by shape. Function chaining runs A, then B, then C. Fan-out, fan-in starts many activities in parallel and waits for all with Task.WhenAll, like building every user's weekly chart at once. Async HTTP API answers a caller at once, with a status address to poll. And monitor loops with a durable timer until a condition holds."),
            leo("And when is Durable Functions the wrong tool?"),
            maya("When there's no waiting and no state. If one service just hands a message to another, a queue does it, and the Service Bus episode covers that. And a workflow that loops for ever should restart itself fresh with ContinueAsNew, or its history keeps growing. Durable Functions earns its place when one step has to wait for another."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If the question says wait for a person with a time-out, the answer is an external event raced against a durable timer. If it says many in parallel and combine, fan-out, fan-in with Task.WhenAll. If the orchestrator needs the time, CurrentUtcDateTime. And if it needs to wait, a durable timer, never Thread.Sleep or Task.Delay. Payments and emails live in activities."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("It runs locally with Azurite, no Azure needed. Use func new with the Durable Functions orchestration template, dotnet-isolated. Run func start and POST to the HttpStart endpoint. Open the statusQueryGetUri from the response. Then add a 30-second durable timer, and watch runtimeStatus stay Running, then Completed."),
            leo("Thirty seconds. Even Aisha's nudge can wait that long. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val containersCase = Podcast(
        "az-containers",
        listOf(
            maya("Welcome to Case Files. Today's case: The Eight O'Clock Freeze. A small trivia company has an Android quiz app. Every morning at eight, the daily quiz goes live. The app calls an API that runs as a container on Azure. And every morning, the first players stare at a spinner for fifteen seconds. Some give up."),
            leo("[chuckle] A quiz where the first question is, how long can you wait? Detective Leo on the case. But first, a container. What's that, in plain words?"),
            maya("Your app packed up with everything it needs, so it runs the same on a laptop and in the cloud. The packed-up blueprint is an image. Under the picture, a container isn't a whole virtual machine. It's an ordinary process, walled off from its neighbours, sharing the host's operating system. That's why it starts in seconds."),
            leo("And where does this quiz API live?"),
            maya("Two services. The image sits in Azure Container Registry, ACR, a private shelf for images. You push images up, and Azure services pull them down to run. An ACR Task builds each new image in the cloud on every push, so nobody needs Docker on their laptop. And the API runs in Azure Container Apps, a managed home for containers with Kubernetes hidden underneath."),
            leo("Then my first suspect is the registry. It's on the Basic tier, it's slow, and the image comes from far away. We need geo-replication."),
            maya("[pause] Bold, and expensive. Geo-replication, copies of your images in several regions, is Premium only. But the players and the registry are in the same region. And a pull happens when a replica starts, not on every request. So why is a replica starting at eight every morning?"),
            leo("Because there wasn't one running before eight? Everyone's asleep at that hour, including the app."),
            maya("That's clue one. Container Apps scales with KEDA, the scaler underneath. It adds replicas, extra copies of your app, from rules like HTTP traffic, CPU, memory, or a queue's length. And someone set the minimum replicas to zero, to save money overnight."),
            leo("And zero means nobody's home at eight."),
            maya("Exactly. The first request after a quiet spell waits while a replica starts. That's a cold start. Zero is great for a hobby app. For a latency-sensitive API with a rush at eight, set the minimum to one. One replica idling all night is a small cost next to a morning of angry players."),
            leo("How does KEDA actually decide, though?"),
            maya("It watches a number, like requests in flight per replica, or messages waiting in a Service Bus queue. When the number passes your rule, it tells Kubernetes to add replicas, up to your maximum. When it falls, it removes them, down to your minimum."),
            leo("Minimum one. Case closed?"),
            maya("Not yet. When they changed the minimum, the new revision failed to start. The log said it couldn't pull the image. Unauthorized. And nobody had touched the image."),
            leo("Revision?"),
            maya("Each deployment in Container Apps makes a revision, a frozen snapshot of that version that never changes. And this one couldn't pull, because the app used the registry's admin password, stored as a secret. Last week, someone rotated that password."),
            leo("[sigh] So the password changed, and the app still had the old one."),
            maya("The fix is no password at all. Give the container app a managed identity, an account Azure manages for it, with the AcrPull role on the registry. It pulls with a short-lived token, and there's nothing to rotate. The Key Vault episode goes deeper on managed identities."),
            leo("Any more clues in the file?"),
            maya("One from the night shift. Every night a results export runs in Azure Container Instances, ACI. It starts a container the moment you ask, billed by the second, with no cluster. The export writes the day's scores to a file. And the next morning, the file is gone."),
            leo("Did someone delete it?"),
            maya("Nobody. A container's own disk is thrown away when it stops. To keep files, mount an Azure Files share, a shared network folder. And the restart policy was set to Always, so after the export finished, it started again, and again."),
            leo("What should it be?"),
            maya("OnFailure. It retries after a crash, but stops after success. Always is for a service that should keep running. Never is for a strict one-shot task. And if the export needed a helper beside it, a container group runs several containers sharing one host, network and storage."),
            leo("So with the fixes in, how do they ship the next version safely?"),
            maya("Multiple revision mode. Keep the old revision and the new one side by side, and send ten percent of traffic to the new one. If it misbehaves, point traffic back at the old revision. The image is still in the registry, so a rollback is a routing change, not a build. App Service does this with slots, as the Deployment slots and scaling episode explains."),
            leo("And where does Container Apps stop working?"),
            maya("If the question demands full Kubernetes control, your own cluster and custom operators, that's AKS. If it's one container, now, with no scaling or ingress rules, that's ACI. Container Apps sits between: microservices, scale to zero, revisions, HTTPS ingress and Dapr, with no cluster to manage. And for one simple web app, App Service is still a fine home."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If it says run this container now, billed by the second, the answer is Container Instances. If it says revisions, a traffic split or scale to zero, Container Apps. If it says build an image without Docker, an ACR Task. If it says no cold starts, minimum replicas one. And if it says geo-replication, the Premium registry."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the night shift. Create a Basic ACR. Run az acr build with a hello-world Dockerfile into it. Then az container create from that image, with the restart policy Never. Run az container logs, and the output shows. Then delete the resource group, so nothing keeps billing."),
            leo("A container that runs once and stops. Unlike that export. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val containersGossip = Podcast(
        "az-containers",
        listOf(
            maya("Did you hear what Dana did to the gym app on Monday? The Android app books fitness classes, and the booking API runs as a container on Azure. On Monday morning, nobody could book a spin class for two hours. Members queued at the front desk with their phones out."),
            leo("[laugh] I heard the spin instructor cycled to the office to complain. Very on brand. What happened?"),
            maya("First, the pieces. A container is your app packed with everything it needs, so it runs the same anywhere. The packed blueprint is an image. Under the picture, a running container is just a process walled off from its neighbours, sharing the host's operating system, so it starts in seconds."),
            leo("And where do the gym's images live?"),
            maya("In Azure Container Registry, ACR, a private shelf for images. You push images up to it, and Azure services pull them down to run. An ACR Task builds each image in the cloud whenever someone pushes code. And the API runs in Azure Container Apps, the managed home for microservices, with Kubernetes hidden underneath."),
            leo("So what did Dana do?"),
            maya("Dana set up the pull from the registry with the registry's admin password, stored as a secret in the container app. On Sunday, security rotated the password. On Monday, a new deployment couldn't pull its image. It had worked for months, so nobody had thought about it."),
            leo("Ouch. What's the right way?"),
            maya("Priya fixed it with a managed identity for the container app, an account Azure manages, given the AcrPull role on the registry. The app gets a short-lived token and pulls. No password stored, nothing to rotate. The Key Vault episode covers how those identities work."),
            leo("And what was being deployed on Monday anyway?"),
            maya("A new version of the booking API. Each deployment in Container Apps makes a revision, a frozen snapshot that never changes. Priya switched on multiple revision mode, kept the old revision running, and sent ten percent of bookings to the new one. That lets real members try it before everyone moves."),
            leo("And if the new one's broken? Do you rebuild the old image?"),
            maya("Point traffic back at the old revision. The image is still in the registry, so a rollback is a routing change, not a build. It's the job App Service does with deployment slots, which the Deployment slots and scaling episode covers."),
            leo("[pause] Okay, Dana's forgiven. Who's next?"),
            maya("Marcus. He wanted to save money, so he set the API's minimum replicas to zero. Replicas are copies of your app, each one a running container. Zero means nothing runs while it's quiet, and nothing is billed for it. And at six in the morning, when the early birds book their classes, the first ones stared at a spinner."),
            leo("Because nothing was running yet. The app had to wake up first."),
            maya("A cold start. The first request after idle pays the start-up. Zero suits a hobby app, but a latency-sensitive API gets a minimum of one. Aisha explained the scaler, KEDA. It watches a number, like requests in flight or the length of a Service Bus queue, and adds replicas up to the maximum, or removes them down to the minimum."),
            leo("And Wednesday?"),
            maya("Wednesday was the intern. The gym sends a monthly attendance report, built by a container in Azure Container Instances, ACI. It runs a container the moment you ask, billed by the second, no cluster. The intern's job wrote the report to the container's own disk. Then the container stopped, and the report vanished."),
            leo("[sigh] Poor intern. Where should it have gone?"),
            maya("Into a mounted Azure Files share, a shared network folder. A container's disk is thrown away when it stops. And Tom spotted one more thing. The restart policy was Always, so the report job started again after every success. Seven reports by lunch. And the job's API key now comes in as a secure environment variable, never baked into the image."),
            leo("So which policy should a report job have?"),
            maya("OnFailure: retry only after a crash. Always is for services that should keep running, Never for a strict one-shot task. And if the job needed a helper beside it, a container group runs several containers sharing one host, network and storage."),
            leo("Was there a Friday?"),
            maya("There's always a Friday. Ben opened a second gym in another country and wanted the images copied there. That's geo-replication. But he'd started the registry on Basic, and geo-replication is Premium only. Basic has ten gigabytes, Standard a hundred, and Premium five hundred, with geo-replication."),
            leo("And when is Container Apps the wrong choice?"),
            maya("If the team demands full Kubernetes control, their own cluster and custom operators, that's AKS. If it's one container right now, with no scaling or ingress rules, that's ACI. Container Apps is the middle: microservices, scale to zero, revisions, HTTPS ingress and Dapr. And for one plain web app, App Service is still fine; the App Service episode covers it."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. If the question says run this container now, billed by the second, pick Container Instances. Revisions, a traffic split or scale to zero, Container Apps. Build an image without Docker, an ACR Task. Retry a job only when it crashes, OnFailure. And geo-replication means the Premium registry."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("It costs a few cents. Create a Basic ACR. Use az acr build to put a hello-world Dockerfile into it. Then az container create from that image with the restart policy Never, and check that az container logs shows the output. Then delete the resource group."),
            leo("And no more spinners. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(durableCase, durableGossip, containersCase, containersGossip)

    private fun durableCaseBoard() = PodcastBoard(
        "az-durable--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Ride That Charged Three Times..."
            Scene(
                0, "Three charges, one ride",
                DiagramSpec(
                    "The bike rental app ends a ride, the backend's workflow charges the card, and some riders pay three times.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "End Ride"),
                        DNode("wf", "Workflow", 0.22f, 0.7f, w = 0.34f, h = 0.2f, sub = "Azure Functions"),
                        DNode("err", "Charged three times", 0.74f, 0.7f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("phone", "wf", "ride ends"),
                        DEdge("wf", "err", "charge", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "wf", "err"), light = setOf("err"), pulse = "phone" to "wf", card = BoardCard("The case", "Check the bike, charge the card, email a receipt. Some paid three times.")),
                    Beat(2, light = setOf("wf"), card = BoardCard("Suspect one: cleared", "One tap each. The charges came minutes apart, after each wake-up.")),
                ),
            ),
            // 3: "Woke up? Back up. What kind of workflow is this?"
            Scene(
                3, "Three kinds of function",
                DiagramSpec(
                    "The client starts the workflow, the orchestrator decides the order, and activities do the real work.",
                    nodes = listOf(
                        DNode("client", "Client", 0.18f, 0.2f, w = 0.3f, h = 0.22f, tone = 1, sub = "starts it"),
                        DNode("orch", "Orchestrator", 0.64f, 0.2f, w = 0.4f, h = 0.22f, sub = "the foreman"),
                        DNode("a1", "Check bike", 0.18f, 0.72f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("a2", "Charge card", 0.5f, 0.72f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("a3", "Email receipt", 0.82f, 0.72f, w = 0.3f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("client", "orch", "start"),
                        DEdge("orch", "a1"),
                        DEdge("orch", "a2"),
                        DEdge("orch", "a3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("client"), card = BoardCard("Durable Functions", "A workflow that runs for days, remembers its place, survives restarts.")),
                    Beat(6, show = setOf("orch", "a1", "a2", "a3"), light = setOf("orch", "a1", "a2", "a3"), pulse = "client" to "orch", card = BoardCard("Orchestrator and activities", "The orchestrator decides and waits. Activities do the real work.")),
                ),
            ),
            // 7: "And where does the foreman keep the clipboard?"
            Scene(
                7, "The clipboard",
                DiagramSpec(
                    "The task hub records every step as an event; on replay the orchestrator reads finished steps back from that history.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.2f),
                        DNode("hub", "Task hub", 0.8f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1, sub = "the history"),
                        DNode("replay", "Replay: run again from line one", 0.5f, 0.8f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("orch", "hub", "events", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("orch")),
                    Beat(8, show = setOf("hub"), light = setOf("hub"), pulse = "orch" to "hub", card = BoardCard("Under the picture", "Tables and queues in a storage account. Each step is an event.")),
                    Beat(10, show = setOf("replay"), light = setOf("replay"), pulse = "hub" to "orch", card = BoardCard("How it works", "Unloaded while it waits. A queue message wakes it, and it replays.")),
                ),
            ),
            // 11: "Unless the step isn't an activity..."
            Scene(
                11, "Clue one: a direct call",
                DiagramSpec(
                    "A payment call made straight from the orchestrator is not in the history, so every replay makes it again.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.22f, sub = "replays"),
                        DNode("pay", "Payment service", 0.78f, 0.3f, w = 0.38f, h = 0.22f, tone = 2),
                        DNode("act", "Charge activity", 0.5f, 0.8f, w = 0.4f, h = 0.2f, tone = 1, sub = "in the history"),
                    ),
                    edges = listOf(
                        DEdge("orch", "pay", "direct", dashed = true),
                        DEdge("orch", "act", "call"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("orch", "pay")),
                    Beat(12, light = setOf("pay"), pulse = "orch" to "pay", card = BoardCard("Clue one", "The charge was called from the orchestrator. Three replays, three charges.")),
                    Beat(14, show = setOf("act"), light = setOf("act"), pulse = "orch" to "act", card = BoardCard("The rule", "Service calls, rows, emails: all in activities. The orchestrator decides.")),
                ),
            ),
            // 15: "Non-determinism. That sounds like a word you'd use to scare juniors."
            Scene(
                15, "Same path every replay",
                DiagramSpec(
                    "An orchestrator must be deterministic: the context gives it a safe clock, a safe id and a safe way to wait.",
                    nodes = listOf(
                        DNode("bad1", "DateTime.Now", 0.25f, 0.28f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "differs each replay"),
                        DNode("good1", "CurrentUtcDateTime", 0.75f, 0.28f, w = 0.46f, h = 0.22f, sub = "the context clock"),
                        DNode("bad2", "Task.Delay", 0.25f, 0.74f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "not in the history"),
                        DNode("good2", "Durable timer", 0.75f, 0.74f, w = 0.46f, h = 0.22f, sub = "CreateTimer"),
                    ),
                    edges = listOf(
                        DEdge("bad1", "good1", "instead"),
                        DEdge("bad2", "good2", "instead"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("bad1"), light = setOf("bad1"), card = BoardCard("Clue two: non-determinism", "Guid.NewGuid and DateTime.Now gave new answers on replay.")),
                    Beat(18, show = setOf("good1"), light = setOf("good1"), pulse = "bad1" to "good1", card = BoardCard("C#", "var now = ctx.CurrentUtcDateTime;", code = true)),
                    Beat(20, show = setOf("bad2"), light = setOf("bad2"), card = BoardCard("Clue three", "A Task.Delay before asking for a rating. Late, or twice.")),
                    Beat(22, show = setOf("good2"), light = setOf("good2"), pulse = "bad2" to "good2", card = BoardCard("C#", "await ctx.CreateTimer(now.AddMinutes(30), token);", code = true)),
                ),
            ),
            // 24: "Almost. Even an activity can run more than once..."
            Scene(
                24, "Safe to run twice",
                DiagramSpec(
                    "Activities run at least once, so a charge must be idempotent: running it twice does no extra harm.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.22f, tone = 1),
                        DNode("act", "Charge card", 0.78f, 0.3f, w = 0.36f, h = 0.22f, sub = "an activity"),
                        DNode("idem", "Already charged? Then stop", 0.5f, 0.8f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("orch", "act", "at least once"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("orch", "act", "idem"), light = setOf("act", "idem"), pulse = "orch" to "act", card = BoardCard("Idempotent", "A host restart can run the activity again. Twice must do no harm.")),
                ),
            ),
            // 25: "Got it. And the rating question waits for the rider..."
            Scene(
                25, "Waiting for the rider",
                DiagramSpec(
                    "Human interaction: an external event raced against a durable timer, so the wait cannot last for ever.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.5f, 0.13f, w = 0.42f, h = 0.18f),
                        DNode("event", "External event", 0.23f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "Rated"),
                        DNode("timer", "Durable timer", 0.77f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "two days"),
                        DNode("save", "Save rating", 0.23f, 0.87f, w = 0.34f),
                        DNode("close", "Close", 0.77f, 0.87f, w = 0.3f),
                    ),
                    edges = listOf(
                        DEdge("event", "orch"),
                        DEdge("timer", "orch"),
                        DEdge("event", "save", "first"),
                        DEdge("timer", "close", "first"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("orch", "event", "timer", "save", "close"), light = setOf("event", "timer"), pulse = "event" to "orch", card = BoardCard("Human interaction", "An external event raced against a durable timer.")),
                    Beat(28, light = setOf("event", "save"), pulse = "event" to "save", card = BoardCard("Sending the event", "The backend POSTs to the send-event address the client handed back.")),
                ),
            ),
            // 29: "Are there other patterns the exam likes?"
            Scene(
                29, "Five shapes",
                DiagramSpec(
                    "Chaining runs steps in turn; fan-out, fan-in starts many at once and waits for all with Task.WhenAll.",
                    nodes = listOf(
                        DNode("o", "Orchestrator", 0.5f, 0.12f, w = 0.42f, h = 0.18f),
                        DNode("r1", "Battery", 0.18f, 0.5f, tone = 1),
                        DNode("r2", "Battery", 0.5f, 0.5f, tone = 1),
                        DNode("r3", "Battery", 0.82f, 0.5f, tone = 1),
                        DNode("sum", "Night report", 0.5f, 0.87f, w = 0.38f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("o", "r1"),
                        DEdge("o", "r2"),
                        DEdge("o", "r3"),
                        DEdge("r1", "sum"),
                        DEdge("r2", "sum"),
                        DEdge("r3", "sum"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("o")),
                    Beat(30, show = setOf("r1", "r2", "r3", "sum"), light = setOf("r1", "r2", "r3"), pulse = "o" to "r2", card = BoardCard("Chaining, fan-out, async, monitor", "Plus human interaction: five patterns, told apart by their shape.")),
                ),
            ),
            // 31: "And where does all this stop working?"
            Scene(
                31, "Where it stops",
                DiagramSpec(
                    "A loop for ever restarts itself with ContinueAsNew; a simple hand-off with no state needs only a queue.",
                    nodes = listOf(
                        DNode("loop", "Loops for ever", 0.26f, 0.35f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "ContinueAsNew"),
                        DNode("hand", "Just a hand-off", 0.76f, 0.35f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "a queue"),
                        DNode("sb", "Service Bus episode", 0.5f, 0.8f, w = 0.56f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("hand", "sb", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("loop", "hand", "sb"), light = setOf("hand", "sb"), pulse = "hand" to "sb", card = BoardCard("Past the edge", "No waiting, no state: no orchestrator. A queue does it.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed",
                DiagramSpec(
                    "The exam's Durable Functions questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("person", "Wait for a person", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "event vs timer"),
                        DNode("many", "Many in parallel", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "fan-out, fan-in"),
                        DNode("time", "Needs the time", 0.5f, 0.46f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "CurrentUtcDateTime"),
                        DNode("side", "Calls a payment service", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "an idempotent activity"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("person", "many", "time", "side"), light = setOf("side"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Real work only in activities.")),
                    Beat(36, card = BoardCard("Today", "Template, POST to HttpStart, read the status, add a 30-second timer.")),
                ),
            ),
        ),
    )

    private fun durableGossipBoard() = PodcastBoard(
        "az-durable--gossip",
        listOf(
            // 0: "Did you hear what Sam did on the habit tracker team?..."
            Scene(
                0, "What Sam did",
                DiagramSpec(
                    "The habit tracker's nudge workflow read DateTime.Now in its orchestrator and failed with a non-determinism error.",
                    nodes = listOf(
                        DNode("phone", "Habit app", 0.22f, 0.25f, w = 0.34f, h = 0.2f, sub = "Android"),
                        DNode("orch", "Sam's orchestrator", 0.72f, 0.25f, w = 0.46f, h = 0.2f, sub = "DateTime.Now"),
                        DNode("err", "Non-determinism error", 0.6f, 0.78f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("phone", "orch", "nudge"),
                        DEdge("orch", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "orch", "err"), light = setOf("err"), pulse = "phone" to "orch", card = BoardCard("Tuesday", "Sam's nudge workflow fell over with a non-determinism error.")),
                ),
            ),
            // 2: "A plain function wakes up, does one short job and forgets..."
            Scene(
                2, "Three kinds of function",
                DiagramSpec(
                    "The client starts the workflow, the orchestrator calls the cues, and activities do the real work.",
                    nodes = listOf(
                        DNode("client", "Client", 0.2f, 0.2f, w = 0.32f, h = 0.22f, tone = 1, sub = "instance id"),
                        DNode("orch", "Orchestrator", 0.68f, 0.2f, w = 0.4f, h = 0.22f, sub = "stage manager"),
                        DNode("a1", "Read habit log", 0.3f, 0.72f, w = 0.4f, h = 0.18f, tone = 1),
                        DNode("a2", "Send nudge", 0.76f, 0.72f, w = 0.36f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("client", "orch", "start"),
                        DEdge("orch", "a1"),
                        DEdge("orch", "a2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("client"), card = BoardCard("Durable Functions", "A workflow that runs for hours or days and survives restarts.")),
                    Beat(4, show = setOf("orch", "a1", "a2"), light = setOf("orch"), pulse = "client" to "orch", card = BoardCard("Activities", "The real work: read the habit log, send a push notification.")),
                ),
            ),
            // 5: "So what broke?"
            Scene(
                5, "Replay",
                DiagramSpec(
                    "The orchestrator is unloaded while it waits and replays from line one, reading finished steps from the task hub's history.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.2f),
                        DNode("hub", "Task hub", 0.8f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1, sub = "the history"),
                        DNode("replay", "Replay: run again from line one", 0.5f, 0.8f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("orch", "hub", "events", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("orch", "replay"), light = setOf("replay"), card = BoardCard("Not in memory", "Unloaded while it waits; on waking it replays from the top.")),
                    Beat(8, show = setOf("hub"), light = setOf("hub"), pulse = "hub" to "orch", card = BoardCard("Under the picture", "Tables and queues in a storage account. Each step is an event.")),
                    Beat(10, light = setOf("orch"), card = BoardCard("C#", "var now = ctx.CurrentUtcDateTime;", code = true)),
                ),
            ),
            // 11: "Poor Sam. Who's next?"
            Scene(
                11, "Aisha's five minutes",
                DiagramSpec(
                    "Task.Delay is not recorded, so replay waits again; a durable timer is a scheduled message in the task hub.",
                    nodes = listOf(
                        DNode("bad", "Task.Delay", 0.25f, 0.28f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "not in the history"),
                        DNode("good", "Durable timer", 0.75f, 0.28f, w = 0.44f, h = 0.22f, sub = "CreateTimer"),
                        DNode("msg", "Scheduled message", 0.75f, 0.78f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "wakes it"),
                    ),
                    edges = listOf(
                        DEdge("bad", "good", "instead"),
                        DEdge("good", "msg", "written as"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("bad"), light = setOf("bad"), card = BoardCard("Aisha's wait", "Five minutes with Task.Delay. The nudge came late, or twice.")),
                    Beat(14, show = setOf("good"), light = setOf("good"), pulse = "bad" to "good", card = BoardCard("C#", "await ctx.CreateTimer(now.AddMinutes(5), token);", code = true)),
                    Beat(16, show = setOf("msg"), light = setOf("msg"), pulse = "good" to "msg", card = BoardCard("How it works", "Due, it wakes the orchestrator, which replays and finds it done.")),
                ),
            ),
            // 17: "And Wednesday?"
            Scene(
                17, "Ben and the intern",
                DiagramSpec(
                    "Side effects belong in activities, and activities run at least once, so they must be idempotent.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.22f, 0.3f, w = 0.38f, h = 0.22f, tone = 1, sub = "replays"),
                        DNode("act", "Save streak", 0.78f, 0.3f, w = 0.36f, h = 0.22f, sub = "an activity"),
                        DNode("idem", "Idempotent: twice does no harm", 0.5f, 0.8f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("orch", "act", "call"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("orch"), light = setOf("orch"), card = BoardCard("Ben's database write", "Straight from the orchestrator, so every replay wrote again.")),
                    Beat(20, show = setOf("act"), light = setOf("act"), pulse = "orch" to "act", card = BoardCard("Dana's fix", "Writes, emails and service calls go in activities.")),
                    Beat(22, show = setOf("idem"), light = setOf("idem"), card = BoardCard("The intern's bonus points", "Activities run at least once. Set a value, or check first.")),
                ),
            ),
            // 23: "Did anything good happen this week?"
            Scene(
                23, "Marcus's cheat day",
                DiagramSpec(
                    "Human interaction: an external event raced against a durable timer, so the wait cannot last for ever.",
                    nodes = listOf(
                        DNode("orch", "Orchestrator", 0.5f, 0.13f, w = 0.42f, h = 0.18f),
                        DNode("event", "External event", 0.23f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "Approved"),
                        DNode("timer", "Durable timer", 0.77f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "one day"),
                        DNode("yes", "Cheat day", 0.23f, 0.87f, w = 0.32f),
                        DNode("no", "No cheat day", 0.77f, 0.87f, w = 0.34f),
                    ),
                    edges = listOf(
                        DEdge("event", "orch"),
                        DEdge("timer", "orch"),
                        DEdge("event", "yes", "first"),
                        DEdge("timer", "no", "first"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("orch", "event", "timer", "yes", "no"), light = setOf("event", "timer"), pulse = "event" to "yes", card = BoardCard("Human interaction", "A friend's Approved event, raced against a one-day durable timer.")),
                ),
            ),
            // 25: "Harsh but fair. Any other patterns I should know?"
            Scene(
                25, "Five shapes",
                DiagramSpec(
                    "Fan-out, fan-in starts many activities at once and waits for all of them with Task.WhenAll.",
                    nodes = listOf(
                        DNode("o", "Orchestrator", 0.5f, 0.12f, w = 0.42f, h = 0.18f),
                        DNode("r1", "Chart", 0.18f, 0.5f, tone = 1),
                        DNode("r2", "Chart", 0.5f, 0.5f, tone = 1),
                        DNode("r3", "Chart", 0.82f, 0.5f, tone = 1),
                        DNode("all", "Weekly charts", 0.5f, 0.87f, w = 0.4f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("o", "r1"),
                        DEdge("o", "r2"),
                        DEdge("o", "r3"),
                        DEdge("r1", "all"),
                        DEdge("r2", "all"),
                        DEdge("r3", "all"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("o", "r1", "r2", "r3", "all"), light = setOf("r1", "r2", "r3"), pulse = "o" to "r2", card = BoardCard("Chaining, fan-out, async, monitor", "Plus human interaction: five patterns, told apart by their shape.")),
                ),
            ),
            // 27: "And when is Durable Functions the wrong tool?"
            Scene(
                27, "The wrong tool",
                DiagramSpec(
                    "A simple hand-off with no state needs only a queue; a loop for ever restarts itself with ContinueAsNew.",
                    nodes = listOf(
                        DNode("hand", "Just a hand-off", 0.26f, 0.35f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "a queue"),
                        DNode("loop", "Loops for ever", 0.76f, 0.35f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "ContinueAsNew"),
                        DNode("sb", "Service Bus episode", 0.5f, 0.8f, w = 0.56f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("hand", "sb", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("hand", "loop", "sb"), light = setOf("hand", "sb"), pulse = "hand" to "sb", card = BoardCard("Past the edge", "No waiting, no state: no orchestrator. A queue does it.")),
                ),
            ),
            // 29: "So, the gossip I should remember for the exam?"
            Scene(
                29, "For the exam",
                DiagramSpec(
                    "The exam's Durable Functions questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("person", "Wait for a person", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "event vs timer"),
                        DNode("many", "Many in parallel", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "fan-out, fan-in"),
                        DNode("time", "Needs the time", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "CurrentUtcDateTime"),
                        DNode("wait", "Needs to wait", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "durable timer"),
                        DNode("side", "Payment or email", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "an activity"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("person", "many", "time", "wait", "side"), light = setOf("side"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one pattern or one safe call.")),
                    Beat(32, card = BoardCard("Today", "Template, POST to HttpStart, read the status, add a 30-second timer.")),
                ),
            ),
        ),
    )

    private fun containersCaseBoard() = PodcastBoard(
        "az-containers--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Eight O'Clock Freeze..."
            Scene(
                0, "The Eight O'Clock Freeze",
                DiagramSpec(
                    "The quiz app calls its container API at eight, and the first players wait fifteen seconds.",
                    nodes = listOf(
                        DNode("phone", "Quiz app", 0.22f, 0.25f, w = 0.34f, h = 0.2f, sub = "Android"),
                        DNode("api", "Quiz API", 0.76f, 0.25f, w = 0.36f, h = 0.2f, sub = "a container"),
                        DNode("spin", "15-second spinner", 0.5f, 0.78f, w = 0.56f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "8 a.m."),
                        DEdge("api", "spin", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "spin"), light = setOf("spin"), pulse = "phone" to "api", card = BoardCard("The case", "Every morning at eight, the first players stare at a spinner.")),
                ),
            ),
            // 1: "A quiz where the first question is, how long can you wait?..."
            Scene(
                1, "Image and container",
                DiagramSpec(
                    "An image is the packed-up app; each running copy is a container, a walled-off process on a shared host.",
                    nodes = listOf(
                        DNode("img", "Image", 0.5f, 0.2f, w = 0.4f, h = 0.22f, kind = NodeKind.NOTE, sub = "the blueprint"),
                        DNode("c1", "Container", 0.25f, 0.72f, w = 0.36f, h = 0.2f, tone = 1, sub = "a process"),
                        DNode("c2", "Container", 0.75f, 0.72f, w = 0.36f, h = 0.2f, tone = 1, sub = "a process"),
                    ),
                    edges = listOf(
                        DEdge("img", "c1", "run"),
                        DEdge("img", "c2", "run"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("img", "c1", "c2"), light = setOf("c1", "c2"), pulse = "img" to "c1", card = BoardCard("Under the picture", "Not a virtual machine: a walled-off process. It starts in seconds.")),
                ),
            ),
            // 3: "And where does this quiz API live?"
            Scene(
                3, "Registry and app",
                DiagramSpec(
                    "An ACR Task builds the image into the registry, and Container Apps pulls it to run the API.",
                    nodes = listOf(
                        DNode("task", "ACR Task", 0.2f, 0.22f, w = 0.32f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("acr", "Container Registry", 0.72f, 0.22f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1, sub = "ACR"),
                        DNode("aca", "Container Apps", 0.72f, 0.75f, w = 0.44f, h = 0.22f, sub = "Kubernetes hidden"),
                    ),
                    edges = listOf(
                        DEdge("task", "acr", "image"),
                        DEdge("acr", "aca", "pull"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("task", "acr", "aca"), light = setOf("task"), pulse = "task" to "acr", card = BoardCard("ACR and ACR Tasks", "A private shelf for images. An ACR Task builds one with no Docker.")),
                ),
            ),
            // 5: "Then my first suspect is the registry..."
            Scene(
                5, "Suspect: the registry",
                DiagramSpec(
                    "Geo-replication is Premium only, and the image is pulled when a replica starts, not on every request.",
                    nodes = listOf(
                        DNode("acr", "Basic registry", 0.24f, 0.3f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("geo", "Geo-replication", 0.76f, 0.3f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "Premium only"),
                        DNode("pull", "Pulled when a replica starts", 0.5f, 0.8f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("acr", "geo", "upgrade?", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("acr", "geo"), light = setOf("geo"), card = BoardCard("Suspect one", "A slow Basic registry far away. Buy geo-replication?")),
                    Beat(6, show = setOf("pull"), light = setOf("pull"), card = BoardCard("Suspect one: cleared", "Same region. So why does a replica start at eight?")),
                ),
            ),
            // 7: "Because there wasn't one running before eight?"
            Scene(
                7, "Clue one: nobody home",
                DiagramSpec(
                    "KEDA watches traffic or a queue and adds or removes replicas between your minimum and maximum.",
                    nodes = listOf(
                        DNode("rules", "HTTP, CPU, memory or a queue", 0.5f, 0.1f, w = 0.74f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("keda", "KEDA", 0.5f, 0.4f, w = 0.36f, h = 0.18f, sub = "the scaler"),
                        DNode("r1", "Replica", 0.18f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r2", "Replica", 0.5f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r3", "Replica", 0.82f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("min", "Minimum: 0 or 1", 0.5f, 0.92f, w = 0.5f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("rules", "keda"),
                        DEdge("keda", "r1"),
                        DEdge("keda", "r2"),
                        DEdge("keda", "r3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("rules", "keda", "r1", "r2", "r3"), light = setOf("keda"), pulse = "keda" to "r2", card = BoardCard("KEDA", "Adds replicas, extra copies of your app, from traffic or a queue.")),
                    Beat(10, show = setOf("min"), light = setOf("min"), card = BoardCard("A cold start", "Minimum zero: nobody home at eight. A busy API keeps a minimum of one.")),
                    Beat(12, light = setOf("rules", "keda"), pulse = "rules" to "keda", card = BoardCard("How it decides", "Past the rule, it asks Kubernetes for more replicas, up to the maximum.")),
                ),
            ),
            // 13: "Minimum one. Case closed?"
            Scene(
                13, "Clue two: Unauthorized",
                DiagramSpec(
                    "The app pulled with the registry's admin password; a managed identity with AcrPull needs no password at all.",
                    nodes = listOf(
                        DNode("app", "Container App", 0.2f, 0.3f, w = 0.34f, h = 0.2f, sub = "new revision"),
                        DNode("acr", "Registry", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("secret", "Admin password", 0.24f, 0.8f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "rotated"),
                        DNode("id", "Managed identity", 0.74f, 0.8f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, sub = "AcrPull"),
                    ),
                    edges = listOf(
                        DEdge("app", "acr", "pull"),
                        DEdge("app", "secret", dashed = true),
                        DEdge("id", "acr", "token"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("app", "acr"), light = setOf("app"), pulse = "app" to "acr", card = BoardCard("Clue two", "The new revision couldn't pull its image. Unauthorized.")),
                    Beat(16, show = setOf("secret"), light = setOf("secret"), card = BoardCard("Revision", "A frozen snapshot of one deployment. This one had an old password.")),
                    Beat(18, show = setOf("id"), light = setOf("id", "acr"), pulse = "id" to "acr", card = BoardCard("No password at all", "A managed identity with AcrPull. Key Vault episode: identities.")),
                ),
            ),
            // 19: "Any more clues in the file?"
            Scene(
                19, "The night shift",
                DiagramSpec(
                    "ACI runs the export by the second; its disk is thrown away, so files go to an Azure Files share.",
                    nodes = listOf(
                        DNode("aci", "Container Instances", 0.26f, 0.2f, w = 0.46f, h = 0.24f, sub = "by the second"),
                        DNode("files", "Azure Files share", 0.76f, 0.2f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("always", "Always", 0.18f, 0.75f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                        DNode("onf", "OnFailure", 0.5f, 0.75f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("never", "Never", 0.82f, 0.75f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("aci", "files", "mount"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("aci"), light = setOf("aci"), card = BoardCard("The missing scores", "The export wrote to its own disk. By morning, the file was gone.")),
                    Beat(22, show = setOf("files", "always"), light = setOf("files", "always"), pulse = "aci" to "files", card = BoardCard("Keep files, stop looping", "Mount Azure Files. And Always restarted it after every success.")),
                    Beat(24, show = setOf("onf", "never"), light = setOf("onf"), card = BoardCard("Restart policy", "OnFailure retries a crash. Container group: several share one host.")),
                ),
            ),
            // 25: "So with the fixes in, how do they ship the next version safely?"
            Scene(
                25, "Ten percent",
                DiagramSpec(
                    "In multiple revision mode, traffic is split across revisions by percentage, and a rollback is a routing change.",
                    nodes = listOf(
                        DNode("users", "Players", 0.14f, 0.45f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("r1", "Old revision", 0.72f, 0.22f, w = 0.4f, h = 0.22f, sub = "90 percent"),
                        DNode("r2", "New revision", 0.72f, 0.7f, w = 0.4f, h = 0.22f, tone = 1, sub = "10 percent"),
                    ),
                    edges = listOf(
                        DEdge("users", "r1"),
                        DEdge("users", "r2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("users", "r1", "r2"), light = setOf("r2"), pulse = "users" to "r2", card = BoardCard("Rollback", "Point traffic back at the old revision. Slots do this on App Service.")),
                ),
            ),
            // 27: "And where does Container Apps stop working?"
            Scene(
                27, "Where each fits",
                DiagramSpec(
                    "ACI runs one container now; Container Apps runs microservices without a cluster; AKS is full Kubernetes control.",
                    nodes = listOf(
                        DNode("aci", "ACI", 0.18f, 0.4f, w = 0.3f, h = 0.3f, tone = 1, sub = "one, now"),
                        DNode("aca", "Container Apps", 0.5f, 0.4f, w = 0.3f, h = 0.3f, sub = "the middle"),
                        DNode("aks", "AKS", 0.82f, 0.4f, w = 0.3f, h = 0.3f, tone = 2, sub = "full control"),
                        DNode("note", "Scale to zero, revisions, Dapr", 0.5f, 0.82f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("aca", "note", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("aci", "aca", "aks", "note"), light = setOf("aca", "note"), pulse = "aca" to "note", card = BoardCard("Past the edge", "Your own cluster and operators: AKS. One container, no rules: ACI.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's container questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("now", "Run it now", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Instances"),
                        DNode("micro", "Revisions, scale to 0", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Apps"),
                        DNode("build", "Build, no Docker", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "ACR Task"),
                        DNode("cold", "No cold starts", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "min replicas 1"),
                        DNode("geo", "Geo-replication", 0.5f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium registry"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("now", "micro", "build", "cold", "geo"), light = setOf("cold"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one service or setting.")),
                    Beat(32, card = BoardCard("Today", "Basic registry, build a Dockerfile, run it with Never, read logs, delete.")),
                ),
            ),
        ),
    )

    private fun containersGossipBoard() = PodcastBoard(
        "az-containers--gossip",
        listOf(
            // 0: "Did you hear what Dana did to the gym app on Monday?..."
            Scene(
                0, "What Dana did",
                DiagramSpec(
                    "The gym's Android app books classes through a container API, and on Monday nobody could book.",
                    nodes = listOf(
                        DNode("phone", "Gym app", 0.22f, 0.25f, w = 0.34f, h = 0.2f, sub = "Android"),
                        DNode("api", "Booking API", 0.76f, 0.25f, w = 0.38f, h = 0.2f, sub = "a container"),
                        DNode("err", "No bookings for two hours", 0.5f, 0.78f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "book"),
                        DEdge("api", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "err"), light = setOf("err"), pulse = "phone" to "api", card = BoardCard("Monday", "Nobody could book a spin class for two hours.")),
                ),
            ),
            // 2: "First, the pieces..."
            Scene(
                2, "Image, registry, app",
                DiagramSpec(
                    "An ACR Task builds the image into the registry, and Container Apps pulls it to run the API.",
                    nodes = listOf(
                        DNode("img", "Image", 0.2f, 0.22f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE, sub = "the blueprint"),
                        DNode("acr", "Container Registry", 0.72f, 0.22f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1, sub = "ACR"),
                        DNode("task", "ACR Task", 0.2f, 0.75f, w = 0.32f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("aca", "Container Apps", 0.72f, 0.75f, w = 0.44f, h = 0.22f, sub = "Kubernetes hidden"),
                    ),
                    edges = listOf(
                        DEdge("task", "acr", "builds"),
                        DEdge("acr", "aca", "pull"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("img"), light = setOf("img"), card = BoardCard("Under the picture", "A container is a walled-off process on a shared host. Fast to start.")),
                    Beat(4, show = setOf("acr", "task", "aca"), light = setOf("task", "acr"), pulse = "task" to "acr", card = BoardCard("ACR Task", "Builds the image in the cloud on every push. No Docker needed.")),
                ),
            ),
            // 5: "So what did Dana do?"
            Scene(
                5, "Dana's password",
                DiagramSpec(
                    "The app pulled with the registry's admin password; a managed identity with AcrPull needs no password at all.",
                    nodes = listOf(
                        DNode("app", "Container App", 0.2f, 0.3f, w = 0.34f, h = 0.2f),
                        DNode("acr", "Registry", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("secret", "Admin password", 0.24f, 0.8f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "rotated Sunday"),
                        DNode("id", "Managed identity", 0.74f, 0.8f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, sub = "AcrPull"),
                    ),
                    edges = listOf(
                        DEdge("app", "acr", "pull"),
                        DEdge("app", "secret", dashed = true),
                        DEdge("id", "acr", "token"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("app", "acr", "secret"), light = setOf("secret"), pulse = "app" to "secret", card = BoardCard("Dana's secret", "The registry's admin password, stored in the app. Then it was rotated.")),
                    Beat(8, show = setOf("id"), light = setOf("id", "acr"), pulse = "id" to "acr", card = BoardCard("Priya's fix", "A managed identity with AcrPull. Nothing stored, nothing to rotate.")),
                ),
            ),
            // 9: "And what was being deployed on Monday anyway?"
            Scene(
                9, "Ten percent",
                DiagramSpec(
                    "Each deployment is a revision that never changes; traffic is split across revisions by percentage.",
                    nodes = listOf(
                        DNode("users", "Members", 0.14f, 0.45f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("r1", "Old revision", 0.72f, 0.22f, w = 0.4f, h = 0.22f, sub = "90 percent"),
                        DNode("r2", "New revision", 0.72f, 0.7f, w = 0.4f, h = 0.22f, tone = 1, sub = "10 percent"),
                    ),
                    edges = listOf(
                        DEdge("users", "r1"),
                        DEdge("users", "r2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("users", "r1", "r2"), light = setOf("r2"), pulse = "users" to "r2", card = BoardCard("Revision", "A frozen snapshot. Multiple revision mode splits the traffic.")),
                    Beat(12, light = setOf("r1"), pulse = "users" to "r1", card = BoardCard("Rollback", "Point traffic back at the old revision. Slots do this on App Service.")),
                ),
            ),
            // 13: "Okay, Dana's forgiven. Who's next?"
            Scene(
                13, "Marcus at six",
                DiagramSpec(
                    "KEDA watches traffic or a queue and adds or removes replicas between your minimum and maximum.",
                    nodes = listOf(
                        DNode("rules", "HTTP, CPU, memory or a queue", 0.5f, 0.1f, w = 0.74f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("keda", "KEDA", 0.5f, 0.4f, w = 0.36f, h = 0.18f, sub = "the scaler"),
                        DNode("r1", "Replica", 0.18f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r2", "Replica", 0.5f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r3", "Replica", 0.82f, 0.7f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("min", "Minimum: 0 or 1", 0.5f, 0.92f, w = 0.5f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("rules", "keda"),
                        DEdge("keda", "r1"),
                        DEdge("keda", "r2"),
                        DEdge("keda", "r3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("min"), light = setOf("min"), card = BoardCard("Minimum zero", "Nothing runs while it's quiet. The six o'clock bookers waited.")),
                    Beat(16, show = setOf("rules", "keda", "r1", "r2", "r3"), light = setOf("keda"), pulse = "rules" to "keda", card = BoardCard("KEDA", "Watches a number and adds replicas. A busy API keeps a minimum of one.")),
                ),
            ),
            // 17: "And Wednesday?"
            Scene(
                17, "The intern's report",
                DiagramSpec(
                    "ACI runs the report job by the second; its disk is thrown away, so files go to an Azure Files share.",
                    nodes = listOf(
                        DNode("aci", "Container Instances", 0.26f, 0.2f, w = 0.46f, h = 0.24f, sub = "by the second"),
                        DNode("files", "Azure Files share", 0.76f, 0.2f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("always", "Always", 0.18f, 0.75f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                        DNode("onf", "OnFailure", 0.5f, 0.75f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("never", "Never", 0.82f, 0.75f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("aci", "files", "mount"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("aci"), light = setOf("aci"), card = BoardCard("The vanished report", "Written to the container's own disk, thrown away when it stopped.")),
                    Beat(20, show = setOf("files", "always"), light = setOf("files", "always"), pulse = "aci" to "files", card = BoardCard("Tom's catch", "Mount Azure Files. And Always meant seven reports by lunch.")),
                    Beat(22, show = setOf("onf", "never"), light = setOf("onf"), card = BoardCard("Restart policy", "OnFailure retries a crash. Container group: several share one host.")),
                ),
            ),
            // 23: "Was there a Friday?"
            Scene(
                23, "Ben's Friday",
                DiagramSpec(
                    "The registry tiers: Basic 10 GB, Standard 100 GB, and Premium 500 GB with geo-replication.",
                    nodes = listOf(
                        DNode("basic", "Basic", 0.18f, 0.35f, w = 0.3f, h = 0.24f, tone = 2, sub = "10 GB"),
                        DNode("std", "Standard", 0.5f, 0.35f, w = 0.3f, h = 0.24f, tone = 1, sub = "100 GB"),
                        DNode("prem", "Premium", 0.82f, 0.35f, w = 0.3f, h = 0.24f, sub = "500 GB"),
                        DNode("geo", "Geo-replication", 0.82f, 0.8f, w = 0.34f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("prem", "geo"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("basic", "std", "prem", "geo"), light = setOf("prem", "geo"), pulse = "prem" to "geo", card = BoardCard("A second gym abroad", "Images copied to another region: geo-replication, Premium only.")),
                ),
            ),
            // 25: "And when is Container Apps the wrong choice?"
            Scene(
                25, "Where each fits",
                DiagramSpec(
                    "ACI runs one container now; Container Apps runs microservices without a cluster; AKS is full Kubernetes control.",
                    nodes = listOf(
                        DNode("aci", "ACI", 0.18f, 0.4f, w = 0.3f, h = 0.3f, tone = 1, sub = "one, now"),
                        DNode("aca", "Container Apps", 0.5f, 0.4f, w = 0.3f, h = 0.3f, sub = "the middle"),
                        DNode("aks", "AKS", 0.82f, 0.4f, w = 0.3f, h = 0.3f, tone = 2, sub = "full control"),
                        DNode("note", "Scale to zero, revisions, Dapr", 0.5f, 0.82f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("aca", "note", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("aci", "aca", "aks", "note"), light = setOf("aca", "note"), pulse = "aca" to "note", card = BoardCard("Past the edge", "Your own cluster and operators: AKS. One container, no rules: ACI.")),
                ),
            ),
            // 27: "So, the gossip I should remember for the exam?"
            Scene(
                27, "For the exam",
                DiagramSpec(
                    "The exam's container questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("now", "Run it now", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Instances"),
                        DNode("micro", "Revisions, scale to 0", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Container Apps"),
                        DNode("build", "Build, no Docker", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "ACR Task"),
                        DNode("retry", "Retry on a crash", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "OnFailure"),
                        DNode("geo", "Geo-replication", 0.5f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium registry"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("now", "micro", "build", "retry", "geo"), light = setOf("retry"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one service or setting.")),
                    Beat(30, card = BoardCard("Today", "Basic registry, build a Dockerfile, run it with Never, read logs, delete.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(durableCaseBoard(), durableGossipBoard(), containersCaseBoard(), containersGossipBoard()) }
}
