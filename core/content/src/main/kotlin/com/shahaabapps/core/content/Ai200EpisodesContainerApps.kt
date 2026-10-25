package com.shahaabapps.core.content

/** Azure Container Apps: environments, revisions and KEDA scaling: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesContainerApps {
    private val classic = Podcast(
        "ai-container-apps",
        listOf(
            maya("Hi, and welcome back. Zaiqa is our own food-delivery app, and so far we have drawn the map and filled the shelf. The Zaiqa API, the dot NET service that answers everything the phone asks for, sits in Azure Container Registry as an image."),
            leo("And last time we tried running it on App Service, Azure's managed web hosting. It worked. It pulled the image, handed the container its settings, and swapped a staging copy into production."),
            maya("It did. But two things hurt. At four in the morning nobody orders dinner, and we still paid for a machine that was awake. And the order worker, the small service that passes a paid order on to the restaurant, takes its work from a queue, not from web requests."),
            leo("So App Service could not shrink to nothing, and could not count a queue."),
            maya("Right on both. Today, where the Zaiqa API really lives: Azure Container Apps. You hand it an image, and Azure runs copies of it, adds more at dinner time, and takes them all away when nobody is ordering."),
            leo("Copies. Does a copy have a name?"),
            maya("A copy is called a replica: one running instance of your container, with its own memory. Picture a counter at a takeaway: one replica is one person serving."),
            leo("And where do the replicas live? Do I pick a machine?"),
            maya("You never see a machine. You make an environment first: a fence around a group of container apps. Inside it they share one private network and one log workspace, and they can call each other by name. Ours is called zaiqa-env."),
            leo("So the API and the order worker go inside the same fence."),
            maya("They do, and the taste service later. Now, how does the phone get in? That is ingress: the front door Container Apps puts in front of an app, with a web address, a certificate, and requests arriving over HTTPS."),
            leo("And the order worker gets a front door too?"),
            maya("No, and that is the point. Ingress is external, internal, or off. External means the internet may knock, which the Zaiqa API needs. The order worker has none: its work arrives in the orders queue."),
            leo("What is internal for, then?"),
            maya("Only apps in the same fence may call it. For example, say you're building a small shop: the web app is external, and the stock service behind it is internal. Nobody outside ever reaches the stock service."),
            leo("And the door needs to know which port?"),
            maya("You set the target port: the number your container is listening on. Our dot NET image listens on 8080, so the target port is 8080. Name the wrong one, and every request times out while a healthy container sits there."),
            leo("Now the awkward one. The API needs the payment provider's key, and the database. Do I type those into a settings box?"),
            maya("Never a plain settings box. Container Apps has secrets: values stored on the app, kept apart from ordinary settings. Your code reads environment variables, named values handed to it at start, so you point one at a secret with the word secretref and the secret's name."),
            leo("And the key itself still sits in Key Vault?"),
            maya("It can. A secret can be a reference to Key Vault, fetched by the app's managed identity, which is a badge Azure gives the app so it carries no password of its own. For the Azure SQL database, the same badge, and no connection string anywhere."),
            leo("Good. Now the release. On Friday I push a new build of the API. What happens?"),
            maya("You get a new revision. A revision is a frozen snapshot of the app's template: the image, the environment variables and the scale rules, exactly as they were. Change any of those, and Container Apps makes a new one rather than editing the old."),
            leo("And a changed secret? Is that a new revision too?"),
            maya("No, and this one catches people. Secrets, ingress and traffic weights are settings, not the template. Change a secret and nothing new is made, and the replicas already running keep the old value, because they read it when they started. Restart the revision and they see the new one."),
            leo("Fine. Can I put the new build in front of ten percent of diners first?"),
            maya("Only in multiple revision mode. The default is single mode: one revision active, and the newest ready one takes all the traffic. Switch the app to multiple mode, and old and new run side by side."),
            leo("And then I decide who sees which?"),
            maya("That is traffic splitting. You give each revision a percentage, and ingress divides the requests that way. Ninety to version seven, ten to version eight. Watch the failures for twenty minutes, then move to nought and a hundred."),
            leo("And if version eight is bad at eight on a Friday?"),
            maya("Move the weights back. No rebuild, no deploy, seconds. The cheapest rollback you will ever write."),
            leo("Can our testers see the new one on purpose, before any diner does?"),
            maya("Give it a label: a name stuck to one revision, which gets its own fixed web address. Testers go there and always land on version eight, even while it is at zero percent of real traffic."),
            leo("Now the dinner rush. Who decides to add replicas at eight on a Friday?"),
            maya("A scale rule, run by KEDA, which stands for Kubernetes Event-driven Autoscaling: the engine that watches something and sets the replica count. For the API, the rule watches concurrent requests, meaning requests being handled at the same time by one replica."),
            leo("So I say how busy one replica is allowed to be."),
            maya("Exactly. Say a hundred at once. Two thousand diners is then about twenty replicas. Set the minimum to two, so somebody is always at the counter, and the maximum to thirty."),
            leo("And the order worker, with no web requests at all?"),
            maya("A custom rule of type azure-servicebus, watching the orders queue. Twenty messages per replica. Four hundred waiting means twenty workers. Minimum replicas nought, so at four in the morning the worker costs nothing."),
            leo("Nothing at all? It just disappears?"),
            maya("It does, and that is scale to zero. KEDA checks the queue about every thirty seconds, and after five quiet minutes it removes the last replica. The first order after that waits a few seconds while one starts, which an order can afford."),
            leo("Could I scale it on the processor instead? That sounds simpler."),
            maya("It is simpler, and it is the trap. A processor or memory rule can never reach zero, so you pay all night for an idle worker. And an app with no ingress and no scale rule, at a minimum of nought, never wakes up at all."),
            leo("[sigh] A worker asleep for ever. What about the nightly payouts run? It starts, it finishes, it goes away."),
            maya("Then it should not be an app. Make it a Container Apps job: a container that runs to completion and then ends. Jobs come three ways: started by hand, on a schedule, or one run per batch of queue messages."),
            leo("So Zaiqa's nightly menu refresh is a scheduled job, not a service pretending to be one."),
            maya("Exactly. And where does all this stop? Waking from zero takes a moment, so anything a diner waits on keeps a minimum of one. If you want the Kubernetes knobs yourself, that is the next episode. And the image still comes off the shelf, pulled with that same badge."),
            leo("So what do I remember for the exam?"),
            maya("If it says send twenty percent to the new version, that is multiple revision mode with traffic splitting. If testers need a stable address, a label. If a changed secret is not seen, restart the revision."),
            leo("And the money ones?"),
            maya("If it must cost nothing on an empty queue, minimum replicas nought with a Service Bus rule, never the processor. A new image makes a new revision; a new secret does not. And work that runs and ends is a job."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create an environment and a container app from a sample image, with external ingress. Change an environment variable and list the revisions. Switch to multiple revision mode and split the traffic fifty fifty. Then set minimum replicas to zero, wait, and watch the replica count drop."),
            leo("Counters that open at dinner and close at dawn. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-container-apps",
        listOf(
            maya("Welcome to Case Files. Today's case: The Friday Nobody Cooked. Zaiqa is our own food-delivery app. At eight on Friday, nine hundred diners paid. Every one of them saw the order accepted. Not one restaurant kitchen ever heard about it."),
            leo("[chuckle] Detective Leo, hungry and on the scene. Nine hundred dinners that nobody cooked. Before the clues, remind me where Zaiqa's code was running by then."),
            maya("Two container apps, side by side. The Zaiqa API, the dot NET service that answers everything the phone asks for, and the order worker, which passes a paid order on to the restaurant. Both on Azure Container Apps, both from images we built last time."),
            leo("And Container Apps is?"),
            maya("Azure runs your containers and you never touch a machine. You hand it an image, and it runs copies of it. A copy is called a replica: one running instance of your container. More at dinner time, fewer at dawn, and sometimes none at all."),
            leo("Sometimes none. Hold that thought, it smells like a motive. First suspect: the queue broke."),
            maya("It did not. The orders queue in Service Bus held nine hundred messages, all waiting politely. So the orders were taken. Nobody came to collect them."),
            leo("Second suspect, then. The worker crashed."),
            maya("No crash in the logs. In fact, no logs at all from the worker that night. Its replica count was nought from six in the evening until the next morning."),
            leo("Nought. So somebody switched it off."),
            maya("Nobody did. Here is the first real clue. An app scales between a minimum and a maximum number of replicas, and the worker's minimum was nought. Nought is allowed, and it is usually the thing you want."),
            leo("Why would I ever want nought?"),
            maya("Because at four in the morning nobody orders dinner. That is scale to zero: no copies running, nothing to pay for. Say you're building a small photo app that tidies uploads overnight. It should cost you nothing while nobody uploads."),
            leo("Fine. But then who wakes it up?"),
            maya("A scale rule. And that, detective, is the hole in this case. The worker had no scale rule at all."),
            leo("So it went to sleep, and nothing ever said good morning."),
            maya("Nothing. An app with no front door and no scale rule, at a minimum of nought, is a lamp nobody is standing next to. It stays off for ever."),
            leo("No front door. Meaning?"),
            maya("Meaning no ingress. Ingress is the front door Container Apps puts in front of an app: a web address, a certificate, and requests arriving over HTTPS. The Zaiqa API has external ingress, so the internet may knock. The worker has none, on purpose."),
            leo("Because the worker's work arrives in the queue, not from a diner."),
            maya("Exactly. And a request arriving is the thing that would otherwise have woken it. So the fix is a scale rule that watches the queue instead."),
            leo("And the engine that does the watching?"),
            maya("KEDA, which stands for Kubernetes Event-driven Autoscaling: the part of Container Apps that watches something and decides the replica count. We added a custom rule of type azure-servicebus on the orders queue, twenty messages per replica."),
            leo("So nine hundred waiting would have meant?"),
            maya("Thirty workers, if the maximum allowed it, and nought again once the queue emptied. KEDA checks about every thirty seconds, and after five quiet minutes it removes the last one."),
            leo("Case closed, then."),
            maya("[pause] Not quite. Read the second complaint. At nine that night, every single diner got the new version of the API, when the plan was ten percent."),
            leo("Easy. Somebody typed a hundred instead of ten."),
            maya("Somebody typed ten. The clue is in the app's mode. A revision is a frozen snapshot of the app's template: its image, its environment variables and its scale rules, as they were when you changed them. Every new build makes one."),
            leo("And the app had two of those, one old and one new."),
            maya("It had two, but only one was active, because the app was in single revision mode. In single mode the newest ready revision takes all the traffic and the old one is retired. Traffic splitting needs multiple revision mode, where both stay alive."),
            leo("So the percentage was typed, and quietly ignored."),
            maya("It was. Switch to multiple mode, set the weights to ninety and ten, and ingress divides the requests for you. And if the new one misbehaves, move the weights back. That rollback takes seconds, with no rebuild."),
            leo("And our testers? They would like to poke the new one before diners do."),
            maya("Give it a label: a name stuck to one revision, with its own fixed web address. They land on that revision whatever the percentages say."),
            leo("Right. Third complaint, and the one that worries me. Finance says the payment provider's key was rotated at noon, and the API kept using the old one all afternoon."),
            maya("That is the third clue, and the one the exam loves. Secrets live on the app, apart from ordinary settings. An environment variable, a named value your code reads at start, points at one with the word secretref."),
            leo("So changing the secret should change what the code reads."),
            maya("At start. Those replicas were already running, and they had read the old value when they started. A secret is a setting, not part of the template, so nothing new is made and nothing restarts."),
            leo("[sigh] So the fix is to restart the revision."),
            maya("Restart it, or ship a new one. Better still, keep the key in Key Vault and make the secret a reference to it, fetched with the app's managed identity, which is a badge Azure gives the app so it carries no password of its own."),
            leo("Can I see the whole picture? What should Friday have looked like?"),
            maya("One environment, which is the fence around both apps: shared private network, shared logs, and they call each other by name. The API external, with a rule on concurrent requests and a minimum of two. The worker with no ingress, a queue rule, and a minimum of nought."),
            leo("And the nightly payouts run that finance keeps asking about?"),
            maya("Not an app at all. A Container Apps job: a container that runs to completion and then ends, started by hand, on a schedule, or once per batch of queue messages. Picture a small shop that totals its till at midnight. That is a scheduled job."),
            leo("Case closed. So what do I take to the exam?"),
            maya("Costs nothing on an empty queue means minimum nought with a Service Bus rule, never the processor, because a processor rule can never reach zero. Twenty percent to the new version means multiple mode with traffic splitting. A stable test address means a label."),
            leo("And the secret?"),
            maya("A changed secret is not seen until you restart the revision. A new image makes a new revision; a new secret does not. And work that runs and ends is a job, not an app."),
            leo("And what do I do today?"),
            maya("Fifteen minutes. Create an environment and a container app from a sample image, with external ingress. Change an environment variable and list the revisions. Switch to multiple revision mode and split the traffic fifty fifty. Then set minimum replicas to zero, wait, and watch the replica count drop."),
            leo("Nine hundred dinners, one missing scale rule. Case closed."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-container-apps",
        listOf(
            maya("Come in, shut the door. Did you hear what happened to Zaiqa this week? Four people, four different ways to upset a container app, and all of it inside five working days."),
            leo("[chuckle] I heard something about Friday, and nothing else. Nobody tells me anything. Start from Monday."),
            maya("Monday, Priya. You know Zaiqa runs on Azure Container Apps now: you hand Azure an image, and it runs copies of it, more at dinner, fewer at dawn. A copy is called a replica: one running instance of your container."),
            leo("I know that much. What did Priya do?"),
            maya("She set up the order worker, the service that passes a paid order on to the restaurant. It reads the orders queue in Service Bus. And she pasted the queue's connection string, name and password in one line, straight into a plain setting."),
            leo("In plain words, where anyone with read access can see it?"),
            maya("Anyone. Container Apps has secrets for exactly this: values stored on the app, apart from ordinary settings. Your code reads environment variables, named values handed to it at start, and one of them points at a secret with the word secretref."),
            leo("And nobody noticed for how long?"),
            maya("Until Tom did the security review, and then Tom had his own week. [chuckle] Because the real answer was no string at all: give the worker a managed identity, which is a badge Azure gives the app so it needs no password, and let the scale rule use the badge too."),
            leo("What did Tom do?"),
            maya("Tuesday. Tom was told the worker should cost nothing overnight, so he added a scale rule on the processor: add copies when the processor is busy. Very sensible, and quite wrong."),
            leo("Why wrong? The processor is quiet at night."),
            maya("Because a processor or memory rule can never take an app down to zero replicas. The lowest it reaches is one. Tom's worker sat there all night, idle, billing."),
            leo("So what should he have used?"),
            maya("A custom rule of type azure-servicebus, watching the queue length: twenty messages per replica. That one can reach nought, which is called scale to zero. The engine behind all of them is KEDA, which stands for Kubernetes Event-driven Autoscaling."),
            leo("And it wakes the worker again when a message lands?"),
            maya("It does. KEDA looks about every thirty seconds, and after five quiet minutes it drops the last replica. Four hundred orders waiting means twenty workers, and by two in the morning, nobody."),
            leo("Right. Wednesday?"),
            maya("Wednesday was Dana, and honestly I feel for her. She rotated the payment provider's key at noon, as the policy says, and went to lunch. The Zaiqa API, the dot NET service the phone talks to, kept charging cards with the old key all afternoon."),
            leo("She changed the value, though. Did it not save?"),
            maya("It saved. But the copies already running read their secrets when they started. A secret is a setting, not the template, so changing it creates no new revision. A revision is a frozen snapshot of the image, the variables and the scale rules, and a secret is none of those."),
            leo("So she had to restart?"),
            maya("Restart the revision, and they pick up the new key. Or keep the key in Key Vault, make the secret a reference to it, and let the badge fetch it. Dana does both now, and she tells everyone about it, loudly."),
            leo("[laugh] Thursday, then. Who is left?"),
            maya("Marcus. Marcus had the big release: a new version of the API, and the plan was ten percent of diners first. He set the weights, ninety and ten, and told the whole floor at stand-up."),
            leo("And?"),
            maya("And every single diner got the new version at nine that evening. Because the app was still in single revision mode: one revision active, and the newest ready one takes all the traffic."),
            leo("So the percentages were ignored."),
            maya("Completely. Traffic splitting needs multiple revision mode, where old and new both stay alive, and ingress divides the requests by weight. Ingress is the front door Container Apps gives an app: a web address and a certificate, over HTTPS."),
            leo("Did anything break?"),
            maya("Nothing serious, and the rollback was one command moving the weights back. But Marcus wanted his testers on the new one first, and for that he needed a label: a name stuck to one revision, with its own fixed address."),
            leo("And Friday? Please tell me Friday was quiet."),
            maya("Friday was the intern. Sweet, careful, read the documentation twice. He deployed a small app to tidy up old dish photos. No front door, no scale rule, minimum replicas nought."),
            leo("No front door, no rule, minimum nought. That app never ran, did it?"),
            maya("Not once. Nothing calls it, nothing watches anything for it, so nothing ever starts it. He checked the logs every day. Picture a shop with no door and no bell. Nobody is coming."),
            leo("What should it have been?"),
            maya("A Container Apps job: a container that runs to completion and then ends. On a schedule, every night at two. Jobs can also be started by hand, or run once per batch of queue messages."),
            leo("Does all of this sit in one place, or is it scattered?"),
            maya("One environment, the fence around a group of container apps: a shared private network, one log workspace, and they call each other by name. Ours is zaiqa-env, and every one of this week's stories happened inside it."),
            leo("And where does Container Apps stop being the answer?"),
            maya("When you want the Kubernetes knobs yourself, which is a later episode. Waking from nought takes a moment, so anything a diner waits on keeps a minimum of one. And the images still come off the shelf we filled in the registry episode."),
            leo("Before anyone else hears us. What goes on the exam?"),
            maya("Nothing on an empty queue means minimum nought with a Service Bus rule, never the processor. Twenty percent to the new version means multiple mode with traffic splitting. A stable test address means a label. A changed secret means restart the revision."),
            leo("And Priya's string?"),
            maya("A secret, or better, a managed identity. And a new image makes a new revision; a new secret does not."),
            leo("Anything to try today, before Monday starts all over again?"),
            maya("Fifteen minutes. Create an environment and a container app from a sample image, with external ingress. Change an environment variable and list the revisions. Switch to multiple revision mode and split the traffic fifty fifty. Then set minimum replicas to zero, wait, and watch the replica count drop."),
            leo("Four colleagues, one fence. I heard nothing."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-container-apps",
        listOf(
            nova("It's late, and you're still up, so let's take this gently. Tonight: where Zaiqa's code actually lives. [pause] Zaiqa is our own food-delivery app, and the part that answers your phone is the Zaiqa API, the dot NET service behind every tap you make."),
            ivy("And last time we had it on App Service, and it worked."),
            nova("It did, and it still would. But at four in the morning nobody is ordering biryani, and we were paying for a machine that was wide awake. [pause] That is the thought that keeps a finance team up, not you."),
            ivy("So tonight we move it."),
            nova("To Azure Container Apps. You hand Azure the image, the box with your code and everything it needs, and Azure runs copies of it. A copy is called a replica: one running instance, with its own memory. [pause] More of them when the city is hungry."),
            ivy("And fewer when the city sleeps."),
            nova("And sometimes none. Hold onto that one. It is the loveliest part, and we will come back to it when you are nearly asleep."),
            ivy("Where do they all live? Do I choose machines?"),
            nova("You never see a machine. You make an environment: a fence around a group of container apps. Inside it they share one private network and one log workspace, and they call each other by name. Ours is zaiqa-env."),
            ivy("And the phone? How does it get inside the fence?"),
            nova("Through ingress: the front door Container Apps puts in front of an app, with a web address, a certificate, and requests arriving over HTTPS. [pause] External ingress means the whole internet may knock. That is what the Zaiqa API wants."),
            ivy("And the order worker, the one that tells the restaurant? Does it get a door?"),
            nova("No door at all, and it is happier that way. Its work arrives in the orders queue, so nothing outside ever needs to reach it. Internal is the third choice: only apps inside the same fence may call."),
            ivy("What does a door need to know?"),
            nova("The target port, the number your container is listening on. Ours listens on 8080. Name the wrong one, and every request waits in the dark while a perfectly healthy container sits two doors down."),
            ivy("[pause] And the payment key? The one we must never say out loud."),
            nova("Never out loud, and never in a plain setting. Container Apps keeps secrets: values stored on the app, apart from the ordinary ones. Your code reads environment variables, named values handed to it at start, and one of them points at a secret with the word secretref."),
            ivy("And the key itself can live in the vault?"),
            nova("It can. The secret becomes a reference to Key Vault, fetched with the app's managed identity, which is a badge Azure gives your app so it carries no password of its own. For the Azure SQL database, the same badge, and no connection string anywhere."),
            ivy("Now. Friday. We ship a new version at nine, with the city watching."),
            nova("Then you make a revision. A revision is a frozen snapshot of the app's template: the image, the variables, the scale rules, exactly as they were. Change any of them, and a new one is born beside the old."),
            ivy("All at once? Everybody on the new one?"),
            nova("Only if you are in single revision mode, which is the default: the newest ready revision takes everything. [pause] Switch to multiple revision mode, and the two live together for a while."),
            ivy("And I decide who meets who."),
            nova("That is traffic splitting. Ninety percent on the old, ten on the new, and ingress divides the requests. Watch the errors for twenty minutes. If you don't like what you see, move the weights back, and it is over in seconds."),
            ivy("And if I want someone to meet the new one on purpose?"),
            nova("Give it a label: a name stuck to one revision, with its own fixed address. Your testers go straight there, while the city carries on with the old one."),
            ivy("[pause] Good. Now the part you promised. Eight on a Friday."),
            nova("Eight on a Friday, and two thousand people want dinner at once. A scale rule decides how many replicas there are, and the engine that runs it is KEDA, which stands for Kubernetes Event-driven Autoscaling."),
            ivy("And what does it watch, for the API?"),
            nova("Concurrent requests: how many are being handled at the same time by one replica. Say a hundred. Two thousand at once, and you get about twenty copies. A minimum of two, so somebody is always at the counter. A maximum of thirty, so Friday cannot bankrupt you."),
            ivy("And the worker with no door?"),
            nova("A rule of type azure-servicebus, on the orders queue. Twenty messages per replica. Four hundred waiting is twenty workers, and an empty queue is none at all. [pause] That is scale to zero, and it is the thing App Service could never do for us."),
            ivy("None at all. Nothing running, nothing to pay."),
            nova("Nothing. KEDA looks about every thirty seconds, and after five quiet minutes it lets the last one go. The first order after that waits a few seconds while a copy starts. An order can wait a few seconds."),
            ivy("Could we just watch the processor instead?"),
            nova("You could, and you would never reach zero. A processor rule stops at one replica, always. [pause] And an app with no door and no rule, at a minimum of nought, is a lamp nobody will ever switch on."),
            ivy("And the nightly payouts run, the one that finishes and goes away?"),
            nova("That is a Container Apps job: a container that runs to completion and then ends. By hand, on a schedule, or once per batch of queue messages. Zaiqa's menu refresh runs at two, and by five past it does not exist."),
            ivy("Where does all this stop, before I fall asleep believing in it?"),
            nova("Waking from nought takes a moment, so keep a minimum of one for anything a diner waits on. Want the Kubernetes knobs yourself, and you want the next episode. And the image still comes off the shelf we filled in the registry episode."),
            ivy("One more before the lights. What does the exam want?"),
            nova("Twenty percent to the new version is multiple mode with traffic splitting. A stable test address is a label. A changed secret that nobody sees is a revision waiting to be restarted."),
            ivy("And the money?"),
            nova("Nothing on an empty queue is a minimum of nought and a Service Bus rule, never the processor. A new image makes a revision; a new secret does not. And work that runs and ends is a job."),
            ivy("And if I'm still awake, what do I do today?"),
            nova("Fifteen minutes, no more. Create an environment and a container app from a sample image, with external ingress. Change an environment variable and list the revisions. Switch to multiple revision mode and split the traffic fifty fifty. Then set minimum replicas to zero, wait, and watch the replica count drop."),
            ivy("Copies that sleep when we do. Goodnight."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-container-apps",
        listOf(
            nova("Tonight is date night, darling, and I have chosen the venue: a place where nobody has to stay late. Zaiqa, our food-delivery app, has moved its kitchen. The Zaiqa API, the dot NET service that answers every tap on the phone, now lives on Azure Container Apps."),
            rafe("Out of App Service at last. It was a lovely flat, but it never knew when to go home."),
            nova("That is exactly it. Container Apps takes your image and runs copies of it: more when the city is hungry, none at all when it isn't. A copy is called a replica: one running instance, with its own memory."),
            rafe("None at all. [chuckle] I do like something that knows when to leave."),
            nova("Then you will love the rest. First question of the night. Where do our two apps live together, sharing a network and a log workspace, able to call each other by name?"),
            rafe("In one environment. A fence around a group of container apps. Ours is zaiqa-env, and the order worker lives in it too."),
            nova("Right answer. [kiss] And how does a hungry diner get in?"),
            rafe("Through ingress: the front door Container Apps puts in front of an app, with its own web address and a certificate, over HTTPS. External for the API, because the whole world may knock."),
            nova("Mm, and warm with it. And the order worker, the one that whispers to the restaurant?"),
            rafe("No door. It takes its work from the orders queue, so nothing outside should ever reach it. Internal would mean only our own apps may call."),
            nova("Clever. Now something delicate. The payment provider's key. Where does a careful person keep it?"),
            rafe("Not in a plain setting. Container Apps has secrets, kept apart from the ordinary values, and an environment variable points at one with the word secretref. Better still, in Key Vault."),
            nova("Fetched how?"),
            rafe("With the app's managed identity: a badge Azure gives the app, so it carries no password of its own. The same badge opens the Azure SQL database."),
            nova("Right answer, and no passwords between us. [kiss] Now, Friday. You ship a new version at nine. What have you just made?"),
            rafe("A new revision. A revision is a frozen snapshot of the app's template: image, variables, scale rules, as they were at that moment."),
            nova("And if you only change a secret?"),
            rafe("Then a new revision, obviously. It changed, so it must be new."),
            nova("No kiss for that one, love. Secrets, ingress and traffic are settings, not the template. Nothing new is made, and the copies already running keep the old value, because they read it when they started."),
            rafe("[sigh] So I restart the revision."),
            nova("You restart the revision. Try again, and I will be generous. Ten percent of diners on the new version, ninety on the old. What do you need?"),
            rafe("Multiple revision mode, so both stay alive, and then traffic splitting: ingress divides the requests ninety and ten. In single mode, the newest one simply takes everybody."),
            nova("Right answer. [kiss] And if the new one misbehaves at the worst hour?"),
            rafe("Move the weights back. Seconds, no rebuild. And if the testers want it to themselves beforehand, a label: a name on one revision, with its own fixed address."),
            nova("You have been reading ahead. Now my favourite part. Eight on a Friday, and two thousand people want dinner at once."),
            rafe("Then somebody has to add replicas. A scale rule, run by KEDA, which stands for Kubernetes Event-driven Autoscaling."),
            nova("And what does the rule watch, for an app that people call?"),
            rafe("Concurrent requests: how many one replica is handling at the same time. Say a hundred, and two thousand at once gives about twenty copies. A minimum of two, so nobody waits while one starts."),
            nova("Right answer. [kiss] Now make the worker cost nothing when nothing is happening."),
            rafe("A rule of type azure-servicebus on the orders queue, twenty messages a replica, and a minimum of nought. Empty queue, nobody on duty. Scale to zero."),
            nova("That is the whole evening in one line. [kiss] Tell me why the processor would not do."),
            rafe("Because a processor rule never reaches zero. It stops at one replica, awake all night, waiting for somebody who is not coming."),
            nova("[chuckle] A story I know. And the app with no door, no rule, minimum nought?"),
            rafe("Never wakes at all. Nothing calls it, nothing watches for it. You give it a rule, or a minimum of one."),
            nova("Good. Last one before pudding. The nightly payouts run that starts, finishes and leaves."),
            rafe("A Container Apps job. A container that runs to completion and then ends: by hand, on a schedule, or once per batch of queue messages."),
            nova("Right answer. [kiss] And where does all this stop, before you get too comfortable?"),
            rafe("Waking from nought takes a moment, so anything a diner waits on keeps a minimum of one. And if I want the Kubernetes knobs myself, that is another episode."),
            nova("And the image?"),
            rafe("Still off the shelf in the registry, pulled with the same badge. Nothing about this evening is a password."),
            nova("Then one last question, for the exam. Four phrases, four answers. Go."),
            rafe("Twenty percent to the new version: multiple mode with traffic splitting. A stable test address: a label. A changed secret nobody sees: restart the revision. Nothing on an empty queue: a minimum of nought and a Service Bus rule."),
            nova("Every one of them. [kiss] And a new image makes a new revision; a new secret does not."),
            rafe("And what are we doing today, before the plates are cleared?"),
            nova("Fifteen minutes, darling. Create an environment and a container app from a sample image, with external ingress. Change an environment variable and list the revisions. Switch to multiple revision mode and split the traffic fifty fifty. Then set minimum replicas to zero, wait, and watch the replica count drop."),
            rafe("A counter that opens for the rush and closes for the night. More wine?"),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun apps(api: String, worker: String) = DiagramSpec(
        "One environment: the Zaiqa API behind external ingress, and the order worker with no ingress, reading the orders queue.",
        nodes = listOf(
            DNode("env", "Environment", 0.5f, 0.1f, w = 0.9f, h = 0.16f, tone = 2, sub = "shared network and logs"),
            DNode("api", api, 0.25f, 0.42f, w = 0.42f, h = 0.2f, tone = 1, sub = "external ingress"),
            DNode("worker", worker, 0.75f, 0.42f, w = 0.42f, h = 0.2f, sub = "no ingress"),
            DNode("phone", "Phone", 0.12f, 0.8f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("sb", "Service Bus queue", 0.66f, 0.8f, w = 0.5f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "orders waiting"),
        ),
        edges = listOf(
            DEdge("phone", "api", "port 8080"),
            DEdge("api", "sb", "orders"),
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
            DNode("r1", "Revision v7", 0.25f, 0.5f, w = 0.42f, h = 0.22f, sub = "90 percent"),
            DNode("r2", "Revision v8", 0.75f, 0.5f, w = 0.42f, h = 0.22f, sub = "10 percent"),
            DNode("mode", "Multiple mode", 0.25f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "several active"),
            DNode("label", "Label: beta", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "its own address"),
        ),
        edges = listOf(
            DEdge("ing", "r1", "90%"),
            DEdge("ing", "r2", "10%"),
            DEdge("mode", "r1", "both active", dashed = true),
            DEdge("label", "r2", "testers"),
        ),
        aspect = 0.8f,
    )

    private fun scaling() = DiagramSpec(
        "KEDA polls the queue and adds or removes replicas, down to zero; a processor rule never reaches zero, and no rule never wakes.",
        nodes = listOf(
            DNode("sb", "Service Bus queue", 0.25f, 0.16f, w = 0.44f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "orders waiting"),
            DNode("keda", "KEDA scale rule", 0.75f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "checks the queue"),
            DNode("reps", "Replicas", 0.5f, 0.52f, w = 0.6f, h = 0.2f, tone = 1, sub = "0 when empty, more when busy"),
            DNode("cpu", "Processor rule", 0.25f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "never reaches zero"),
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
            DNode("wake", "Waking takes a moment", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "the first order waits"),
            DNode("conn", "No plain connection string", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "a secret or a badge"),
            DNode("job", "Runs, then ends?", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "make it a job"),
            DNode("next", "Other episodes", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "registry, Kubernetes"),
        ),
        edges = listOf(DEdge("conn", "next", "a badge, not a password")),
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
    private val limitNodes = setOf("wake", "conn", "job", "next")
    private val today = BoardCard("Today", "An environment with ingress; change a variable; multiple mode 50/50; min 0.")
    private val secretRef = BoardCard("Environment variable", "PAYMENTS-KEY=secretref:payments-key", code = true)
    private val trafficCode = BoardCard("Azure CLI", "az containerapp ingress traffic set --revision-weight zaiqa-api--v7=90 zaiqa-api--v8=10", code = true)
    private val queueRule = BoardCard("The queue rule", "Checks about every 30 seconds; after 5 quiet minutes, back to zero.")
    private val wholeZaiqa = BoardCard("The whole of Zaiqa", "External API, a worker from nought, Key Vault secrets, a nightly job.")
    private val whereItStops = BoardCard("Where it stops", "Waking takes a moment. No plain strings. Run-once work is a job.")
    private val kedaCard = BoardCard("KEDA", "Kubernetes Event-driven Autoscaling: it sets the replica count.")
    private val revisionCard = BoardCard("Revision", "A frozen snapshot: the image, the variables and the scale rules.")
    private val labelCard = BoardCard("Label", "A name on one revision, with its own fixed address, for testers.")
    private val jobCard = BoardCard("Container Apps job", "Runs, then ends: by hand, on a schedule, or per queue batch.")
    private val replicaCard = BoardCard("Replica", "One running copy of your container, with its own memory.")
    private val envCard = BoardCard("Environment", "A fence: shared network, shared logs, apps call each other by name.")
    private val queueRuleCard = BoardCard("Queue rule", "Type azure-servicebus, 20 messages a replica, minimum nought.")
    private val httpRuleCard = BoardCard("HTTP rule", "100 at once per replica: 2,000 requests means about 20 replicas.")
    private val trapsCard = BoardCard("Two traps", "A processor rule never reaches zero. No door and no rule never wakes.")

    private fun classicBoard() = PodcastBoard(
        "ai-container-apps",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is our own food-delivery app..."
            Scene(
                0, "Containers, no servers", apps("zaiqa-api", "order-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Zaiqa so far", "The API image is on the shelf. App Service could never sleep.")),
                    Beat(4, light = setOf("api"), card = BoardCard("Container Apps", "Hand over an image; Azure runs the copies, more or fewer, even none.")),
                    Beat(6, card = replicaCard),
                    Beat(8, show = setOf("env"), light = setOf("env"), card = envCard),
                    Beat(10, show = setOf("phone"), light = setOf("api", "phone"), pulse = "phone" to "api", card = BoardCard("Ingress", "The front door: a web address, a certificate, requests over HTTPS.")),
                    Beat(12, show = setOf("worker", "sb"), light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("No ingress for the worker", "Its work arrives in the orders queue, not from the internet.")),
                    Beat(16, light = setOf("api"), card = BoardCard("Target port", "The port the container listens on. Our dot NET image uses 8080.")),
                ),
            ),
            // 17: "Now the awkward one. The API needs the payment provider's key..."
            Scene(
                17, "Secrets", secrets(),
                beats = listOf(
                    Beat(18, show = setOf("sec", "env"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(20, show = setOf("kv", "cont"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Key Vault reference", "Fetched by the app's badge, its managed identity. No password kept.")),
                ),
            ),
            // 21: "Good. Now the release. On Friday I push a new build of the API."
            Scene(
                21, "What makes a revision", snapshot(),
                beats = listOf(
                    Beat(22, show = setOf("template", "newrev"), light = setOf("newrev"), pulse = "template" to "newrev", card = revisionCard),
                    Beat(24, show = setOf("same", "restart"), light = setOf("same", "restart"), pulse = "same" to "restart", card = BoardCard("A changed secret", "Nothing new is made. Restart, so replicas read the new value.")),
                ),
            ),
            // 25: "Fine. Can I put the new build in front of ten percent of diners first?"
            Scene(
                25, "Releasing on a Friday", splitting(),
                beats = listOf(
                    Beat(26, show = setOf("ing", "r1", "r2", "mode"), light = setOf("mode"), pulse = "mode" to "r1", card = BoardCard("Multiple revision mode", "Old and new both active. Single mode gives the newest everything.")),
                    Beat(28, light = setOf("r1", "r2"), pulse = "ing" to "r2", card = trafficCode),
                    Beat(30, light = setOf("r1"), pulse = "ing" to "r1", card = BoardCard("Rollback", "Move the weights back. Seconds, and no rebuild.")),
                    Beat(32, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = labelCard),
                ),
            ),
            // 33: "Now the dinner rush. Who decides to add replicas at eight on a Friday?"
            Scene(
                33, "The dinner rush", scaling(),
                beats = listOf(
                    Beat(34, show = setOf("keda", "reps"), light = setOf("keda"), pulse = "keda" to "reps", card = kedaCard),
                    Beat(36, light = setOf("reps"), card = httpRuleCard),
                    Beat(38, show = setOf("sb"), light = setOf("sb", "keda"), pulse = "keda" to "sb", card = queueRuleCard),
                    Beat(40, light = setOf("reps"), card = queueRule),
                    Beat(42, show = setOf("cpu", "none"), light = setOf("cpu", "none"), pulse = "cpu" to "reps", card = trapsCard),
                ),
            ),
            // 43: "A worker asleep for ever. What about the nightly payouts run?"
            Scene(
                43, "Work that ends", jobs("Nightly menu refresh"),
                beats = listOf(
                    Beat(44, light = setOf("sched", "work"), pulse = "work" to "sched", card = jobCard),
                    Beat(46, light = setOf("work"), card = wholeZaiqa),
                ),
            ),
            // 47: "So what do I remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, light = setOf("split", "label", "secret"), card = BoardCard("Listen for the phrase", "Multiple mode and splitting, a label, a restart for a secret.")),
                    Beat(50, light = all6, card = BoardCard("And the money ones", "Nought with a queue rule; a new image; work that ends is a job.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-container-apps--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Friday Nobody Cooked."
            Scene(
                0, "The Friday Nobody Cooked", apps("zaiqa-api", "order-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api", "phone"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The case", "900 diners paid. Not one kitchen ever heard about it.")),
                    Beat(4, light = setOf("api"), card = replicaCard),
                    Beat(6, show = setOf("sb"), light = setOf("sb"), card = BoardCard("Clue one", "The orders queue held 900 messages. Nobody came to collect them.")),
                    Beat(8, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Replica count: nought", "From six in the evening until morning. No logs at all.")),
                    Beat(10, light = setOf("worker"), card = BoardCard("Minimum nought", "Allowed, and usually what you want. Nothing runs, nothing is paid.")),
                    Beat(18, show = setOf("env"), light = setOf("api", "phone"), pulse = "phone" to "api", card = BoardCard("Ingress", "The front door. External for the API; the worker has none.")),
                ),
            ),
            // 20: "And a request arriving is the thing that would have woken it."
            Scene(
                20, "Who wakes the worker", scaling(),
                beats = listOf(
                    Beat(20, show = setOf("reps"), light = setOf("reps"), card = BoardCard("The hole in the case", "No scale rule at all. Nothing was watching the queue for it.")),
                    Beat(22, show = setOf("keda", "sb"), light = setOf("keda"), pulse = "keda" to "sb", card = kedaCard),
                    Beat(24, show = setOf("cpu", "none"), light = setOf("keda", "reps"), pulse = "keda" to "reps", card = queueRule),
                ),
            ),
            // 26: "Not quite. At nine that night, every diner got the new version."
            Scene(
                26, "Everyone got the new one", snapshot(),
                beats = listOf(
                    Beat(28, show = setOf("template", "newrev"), light = setOf("newrev"), pulse = "template" to "newrev", card = revisionCard),
                    Beat(30, show = setOf("single"), light = setOf("single"), pulse = "newrev" to "single", card = BoardCard("Single revision mode", "The newest ready revision takes all the traffic. Weights ignored.")),
                ),
            ),
            // 31: "So the percentage was typed, and quietly ignored."
            Scene(
                31, "Splitting it properly", splitting(),
                beats = listOf(
                    Beat(32, show = setOf("ing", "r1", "r2", "mode"), light = setOf("mode", "r1", "r2"), pulse = "ing" to "r2", card = trafficCode),
                    Beat(34, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = labelCard),
                ),
            ),
            // 35: "Third complaint. The payment key was rotated at noon."
            Scene(
                35, "The key that would not change", secrets(),
                beats = listOf(
                    Beat(36, show = setOf("sec", "env"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(38, show = setOf("cont"), light = setOf("cont"), pulse = "env" to "cont", card = BoardCard("Read at start", "Running replicas keep the old value. A secret makes no revision.")),
                    Beat(40, show = setOf("kv"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Key Vault reference", "Fetched with the app's badge, its managed identity. No password.")),
                ),
            ),
            // 41: "Can I see the whole picture? What should Friday have looked like?"
            Scene(
                41, "What Friday should have been", apps("zaiqa-api", "order-worker"),
                beats = listOf(
                    Beat(42, light = setOf("env", "api", "worker"), card = BoardCard("One environment", "API external, an HTTP rule, minimum two; the worker at nought.")),
                    Beat(44, light = setOf("worker"), card = BoardCard("And the nightly run", "Not an app. A job: it runs to completion and then ends.")),
                ),
            ),
            // 45: "Case closed. So what do I take to the exam?"
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, light = setOf("zero", "split", "label"), card = BoardCard("Listen for the phrase", "Nought with a queue rule, multiple mode and splitting, a label.")),
                    Beat(48, light = all6, card = BoardCard("And the rest", "A restart for a secret, a new image for a revision, a job that ends.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-container-apps--gossip",
        listOf(
            // 0: "Did you hear what happened to Zaiqa this week?"
            Scene(
                0, "Monday: a string in the open", apps("zaiqa-api", "order-worker"),
                beats = listOf(
                    Beat(0, light = setOf("env"), card = BoardCard("Four colleagues, one week", "Four ways to upset a container app, in five working days.")),
                    Beat(2, light = setOf("api"), card = replicaCard),
                    Beat(4, light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("Priya's connection string", "Pasted into a plain setting. Anyone with read access can see it.")),
                ),
            ),
            // 6: "Container Apps has secrets for exactly this."
            Scene(
                6, "Secrets, and a badge", secrets(),
                beats = listOf(
                    Beat(6, show = setOf("sec", "env"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(8, show = setOf("kv", "cont"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Better: no string at all", "A managed identity, the app's badge, and the rule uses it too.")),
                ),
            ),
            // 10: "Tuesday. Tom added a scale rule on the processor."
            Scene(
                10, "Tuesday: the processor rule", scaling(),
                beats = listOf(
                    Beat(10, show = setOf("reps", "cpu"), light = setOf("cpu"), pulse = "cpu" to "reps", card = BoardCard("Tom's processor rule", "A processor or memory rule stops at one replica. Never zero.")),
                    Beat(14, show = setOf("keda", "sb"), light = setOf("keda"), pulse = "keda" to "sb", card = queueRuleCard),
                    Beat(16, show = setOf("none"), light = setOf("reps"), pulse = "keda" to "reps", card = queueRule),
                ),
            ),
            // 17: "Right. Wednesday?"
            Scene(
                17, "Wednesday: the old key", snapshot(),
                beats = listOf(
                    Beat(18, show = setOf("template", "same", "restart"), light = setOf("same"), pulse = "template" to "same", card = BoardCard("Dana's rotated key", "Saved at noon, and the API charged with the old one all afternoon.")),
                    Beat(20, light = setOf("same", "restart"), pulse = "same" to "restart", card = BoardCard("A secret is a setting", "No new revision, and replicas read secrets when they start.")),
                    Beat(22, light = setOf("restart"), card = BoardCard("The fix", "Restart the revision, or keep the key in Key Vault.")),
                ),
            ),
            // 23: "Thursday, then. Who is left?"
            Scene(
                23, "Thursday: everyone at once", splitting(),
                beats = listOf(
                    Beat(24, show = setOf("ing", "r1", "r2"), light = setOf("r2"), pulse = "ing" to "r2", card = BoardCard("Marcus set ten percent", "And every diner got the new version at nine that evening.")),
                    Beat(26, show = setOf("mode"), light = setOf("mode"), pulse = "mode" to "r1", card = BoardCard("Single revision mode", "The newest ready revision takes everything. Weights ignored.")),
                    Beat(28, light = setOf("r1", "r2"), pulse = "ing" to "r1", card = trafficCode),
                    Beat(30, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = labelCard),
                ),
            ),
            // 31: "And Friday? Please tell me Friday was quiet."
            Scene(
                31, "Friday: the intern's app", scaling(),
                beats = listOf(
                    Beat(32, show = setOf("none", "reps"), light = setOf("none"), pulse = "none" to "reps", card = BoardCard("No door, no rule, nought", "Nothing calls it, nothing watches for it. It never ran once.")),
                    Beat(34, light = setOf("none"), card = BoardCard("A shop with no door", "And no bell. Nobody is coming.")),
                ),
            ),
            // 35: "What should it have been?"
            Scene(
                35, "What it should have been", jobs("Nightly photo tidy"),
                beats = listOf(
                    Beat(36, light = setOf("sched", "work"), pulse = "work" to "sched", card = jobCard),
                ),
            ),
            // 37: "Does all of this sit in one place, or is it scattered?"
            Scene(
                37, "One fence around it all", limits(),
                beats = listOf(
                    Beat(38, light = setOf("wake", "conn"), card = envCard),
                    Beat(40, light = limitNodes, pulse = "conn" to "next", card = whereItStops),
                ),
            ),
            // 41: "Before anyone else hears us. What goes on the exam?"
            Scene(
                41, "Before anyone else hears", exam(),
                beats = listOf(
                    Beat(42, light = setOf("zero", "split", "label", "secret"), card = BoardCard("Listen for the phrase", "Nought and a queue rule, multiple mode, a label, a restart.")),
                    Beat(44, light = all6, card = BoardCard("And the rest", "A string belongs in a secret. A new image makes a new revision.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-container-apps--late-night",
        listOf(
            // 0: "It's late, and you're still up. Tonight: where Zaiqa's code lives."
            Scene(
                0, "Still up", apps("zaiqa-api", "order-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api", "phone"), light = setOf("api"), card = BoardCard("Tonight", "Where Zaiqa's code lives, and when it is allowed to sleep.")),
                    Beat(4, light = setOf("api"), card = replicaCard),
                    Beat(8, show = setOf("env"), light = setOf("env"), card = envCard),
                    Beat(10, light = setOf("api", "phone"), pulse = "phone" to "api", card = BoardCard("Ingress", "The front door: a web address, a certificate, requests over HTTPS.")),
                    Beat(12, show = setOf("worker", "sb"), light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("No door for the worker", "Work arrives in the orders queue. Internal is the third choice.")),
                    Beat(14, light = setOf("api"), card = BoardCard("Target port", "The port the container is listening on. Ours is 8080.")),
                ),
            ),
            // 15: "And the payment key? The one we must never say out loud."
            Scene(
                15, "Things we never say aloud", secrets(),
                beats = listOf(
                    Beat(16, show = setOf("sec", "env"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(18, show = setOf("kv", "cont"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Key Vault reference", "Fetched with the app's badge, its managed identity. No password.")),
                ),
            ),
            // 19: "Now. Friday. We ship a new version at nine."
            Scene(
                19, "Shipping at nine", snapshot(),
                beats = listOf(
                    Beat(20, show = setOf("template", "newrev"), light = setOf("newrev"), pulse = "template" to "newrev", card = revisionCard),
                    Beat(22, show = setOf("single"), light = setOf("single"), pulse = "newrev" to "single", card = BoardCard("Single mode", "The default: the newest ready revision takes everything.")),
                ),
            ),
            // 23: "And I decide who meets who."
            Scene(
                23, "Who meets who", splitting(),
                beats = listOf(
                    Beat(24, show = setOf("ing", "r1", "r2", "mode"), light = setOf("r1", "r2"), pulse = "ing" to "r2", card = trafficCode),
                    Beat(26, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = labelCard),
                ),
            ),
            // 27: "Good. Now the part you promised. Eight on a Friday."
            Scene(
                27, "Eight on a Friday", scaling(),
                beats = listOf(
                    Beat(28, show = setOf("keda", "reps"), light = setOf("keda"), pulse = "keda" to "reps", card = kedaCard),
                    Beat(30, light = setOf("reps"), card = httpRuleCard),
                    Beat(32, show = setOf("sb"), light = setOf("sb"), pulse = "keda" to "sb", card = queueRuleCard),
                    Beat(34, light = setOf("reps"), card = queueRule),
                    Beat(36, show = setOf("cpu", "none"), light = setOf("cpu", "none"), pulse = "cpu" to "reps", card = trapsCard),
                ),
            ),
            // 37: "And the nightly payouts run, the one that finishes and goes away?"
            Scene(
                37, "Runs, then ends", jobs("Nightly menu refresh"),
                beats = listOf(
                    Beat(38, light = setOf("sched", "work"), pulse = "work" to "sched", card = jobCard),
                ),
            ),
            // 39: "Where does all this stop, before I fall asleep believing in it?"
            Scene(
                39, "Where it stops", limits(),
                beats = listOf(
                    Beat(40, light = limitNodes, pulse = "conn" to "next", card = whereItStops),
                ),
            ),
            // 41: "One more before the lights. What does the exam want?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, light = setOf("split", "label", "secret"), card = BoardCard("Listen for the phrase", "Multiple mode and splitting, a label, a restart for a secret.")),
                    Beat(44, light = all6, card = BoardCard("And the money ones", "Nought with a queue rule; a new image; work that ends is a job.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-container-apps--couples",
        listOf(
            // 0: "Tonight is date night, darling. Zaiqa has moved its kitchen."
            Scene(
                0, "Date night", apps("zaiqa-api", "order-worker"),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Date night", "Somewhere nobody has to stay late. Zaiqa has moved its kitchen.")),
                    Beat(2, light = setOf("api"), card = replicaCard),
                    Beat(4, show = setOf("env"), light = setOf("env"), card = BoardCard("Quiz: where do they live?", "Shared network, shared logs, calling each other by name.")),
                    Beat(6, show = setOf("phone"), light = setOf("api", "phone"), pulse = "phone" to "api", card = BoardCard("Right answer", "One environment: the fence around a group of container apps.")),
                    Beat(8, show = setOf("worker", "sb"), light = setOf("worker", "sb"), pulse = "sb" to "worker", card = BoardCard("Ingress", "The front door. External for the API; the worker wants none.")),
                ),
            ),
            // 10: "Clever. Now something delicate. The payment provider's key."
            Scene(
                10, "Something delicate", secrets(),
                beats = listOf(
                    Beat(11, show = setOf("sec", "env"), light = setOf("env"), pulse = "sec" to "env", card = secretRef),
                    Beat(13, show = setOf("kv", "cont"), light = setOf("kv"), pulse = "kv" to "sec", card = BoardCard("Right answer", "A managed identity: the app's badge. No password between us.")),
                ),
            ),
            // 14: "Now, Friday. You ship a new version at nine."
            Scene(
                14, "What have you just made", snapshot(),
                beats = listOf(
                    Beat(15, show = setOf("template", "newrev"), light = setOf("newrev"), pulse = "template" to "newrev", card = revisionCard),
                    Beat(18, show = setOf("same", "restart"), light = setOf("same"), pulse = "template" to "same", card = BoardCard("No kiss for that one", "A secret is a setting, not the template. Nothing new is made.")),
                    Beat(19, light = setOf("restart"), pulse = "same" to "restart", card = BoardCard("Restart the revision", "Running replicas read their secrets when they started.")),
                ),
            ),
            // 20: "Try again. Ten percent of diners on the new version."
            Scene(
                20, "Ten percent of diners", splitting(),
                beats = listOf(
                    Beat(21, show = setOf("ing", "r1", "r2", "mode"), light = setOf("mode"), pulse = "ing" to "r2", card = BoardCard("Right answer", "Multiple mode, then traffic splitting: ingress divides by weight.")),
                    Beat(23, show = setOf("label"), light = setOf("label"), pulse = "label" to "r2", card = trafficCode),
                ),
            ),
            // 24: "Now my favourite part. Eight on a Friday, two thousand at once."
            Scene(
                24, "Two thousand at once", scaling(),
                beats = listOf(
                    Beat(25, show = setOf("keda", "reps"), light = setOf("keda"), pulse = "keda" to "reps", card = kedaCard),
                    Beat(27, light = setOf("reps"), card = BoardCard("Right answer", "100 at once per replica, minimum two. Nobody waits for a start.")),
                    Beat(29, show = setOf("sb"), light = setOf("sb"), pulse = "keda" to "sb", card = BoardCard("Right answer", "An azure-servicebus rule, 20 a replica, minimum nought.")),
                    Beat(31, show = setOf("cpu"), light = setOf("cpu"), pulse = "cpu" to "reps", card = BoardCard("Why not the processor", "It never reaches zero. One replica, awake all night.")),
                    Beat(33, show = setOf("none"), light = setOf("none"), pulse = "none" to "reps", card = BoardCard("No door, no rule, nought", "It never wakes. Give it a rule, or a minimum of one.")),
                ),
            ),
            // 34: "Good. Last one before pudding. The nightly payouts run."
            Scene(
                34, "Before pudding", jobs("Nightly payouts run"),
                beats = listOf(
                    Beat(35, light = setOf("sched", "work"), pulse = "work" to "sched", card = jobCard),
                ),
            ),
            // 36: "Right answer. And where does all this stop?"
            Scene(
                36, "Where it stops", limits(),
                beats = listOf(
                    Beat(37, light = setOf("wake", "job"), card = whereItStops),
                    Beat(39, light = limitNodes, pulse = "conn" to "next", card = BoardCard("Still off the shelf", "The image comes from the registry, pulled with the same badge.")),
                ),
            ),
            // 40: "Then one last question, for the exam."
            Scene(
                40, "For the exam", exam(),
                beats = listOf(
                    Beat(41, light = all6, card = BoardCard("Four phrases, four answers", "Multiple mode and splitting, a label, a restart, nought and a rule.")),
                    Beat(43, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
