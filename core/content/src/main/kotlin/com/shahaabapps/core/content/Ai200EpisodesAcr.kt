package com.shahaabapps.core.content

/** Azure Container Registry: build, store and version images: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAcr {
    private val classic = Podcast(
        "ai-acr",
        listOf(
            maya("Hi, and welcome back. Today is about where your app waits before it runs. Let's start with the box itself. A container image is a packed-up copy of your app and everything it needs to run: the code, the right Python, the libraries. Like a lunchbox packed the night before, ready to open anywhere."),
            leo("A lunchbox for code. I like it already. So where do I keep the lunchboxes?"),
            maya("In a container registry. That's a private warehouse for images. You upload an image there once, and every server that needs it downloads it from there. Uploading an image is called a push, and downloading it is called a pull."),
            leo("Push up, pull down. And Azure has its own warehouse?"),
            maya("Azure Container Registry, or ACR for short. It's a private registry that Azure looks after for you, so you never run the warehouse yourself. Think of a private version of Docker Hub, the big public warehouse that most people know."),
            leo("And who builds the lunchboxes in the first place?"),
            maya("Usually a tool called Docker, the popular program for building and running images. But hold that thought. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. The backend is packed as an image called coach-api, and it lives in a registry called ttcoach."),
            leo("And inside the warehouse, is it just one big pile of lunchboxes?"),
            maya("[chuckle] No, there are shelves. A repository is one shelf: all the versions of one image. So coach-api is a repository, and every new build of the backend goes on that shelf."),
            leo("How do I tell the versions on the shelf apart?"),
            maya("Two ways. The first is a tag: a friendly label you stick on one version, like v1 or v2. A tag can move. Push a new image with the same tag, and the label simply moves onto the new one."),
            leo("So the label can wander off. What's the second way?"),
            maya("The digest. It's a long code worked out from every byte of the image, like a fingerprint. Change a single byte, and you get a different fingerprint. So a digest never moves. It always means exactly one image."),
            leo("And the famous tag called latest? Everybody uses latest."),
            maya("Latest is just a tag that means whatever was pushed last. Fine on your laptop, risky in production, which is the live app your real users touch. Picture a small app that runs coach-api latest. On Tuesday someone pushes a test build as latest, and the next restart quietly runs that instead."),
            leo("[sigh] Real users on a test build. Lovely. So what should I put live?"),
            maya("A unique tag for every build, like a version number or the build's own id. Or the digest itself. Then a later push can't change what's running."),
            leo("Got it. Now, does every registry cost the same?"),
            maya("There are three tiers, which are just price levels. Basic is for learning and small projects. Standard suits most real apps. Premium is the top one, and it has a feature the exam loves: geo-replication."),
            leo("Geo-replication. Plain words, please."),
            maya("Copies of your registry in other parts of the world. Azure calls each part a region, like West Europe or East US. You keep one registry name, Azure keeps a copy in each region, and every download goes to the nearest copy."),
            leo("So if the Coach runs in Europe and in America, both pull from nearby."),
            maya("Right, and that needs Premium. Basic and Standard sit in one region only. Premium can also keep the registry on a private network, away from the public internet."),
            leo("Okay. How does an image get into the registry? Do I need Docker on my laptop?"),
            maya("You don't. A feature called ACR Tasks builds images for you, inside Azure. The simplest is a quick task: one command, az acr build. Az is Azure's command line, where you type instructions. It sends your folder up, builds the image there, and pushes it into the registry."),
            leo("Build in the cloud, no Docker needed. What else can a task do?"),
            maya("It can run by itself. That's a triggered task. It starts when something happens: a new commit, meaning a saved change to your code, or a timer, or an update to the base image."),
            leo("Base image? Another kind of lunchbox?"),
            maya("The one yours is built on top of. Your image starts from a ready-made one, like the official slim Python image. That's the base, like the pizza base under your toppings. When the base gets a security fix, the task rebuilds your image on the fixed base, with nobody remembering to do it."),
            leo("Nice. Any other kind?"),
            maya("A multi-step task. A short file lists the steps: build the image, run the tests inside it, and push only if they pass. For example, the Coach team never pushes a build that fails its tests."),
            leo("Okay, it's built and pushed. Now the Coach's server must pull it. Does it need a password?"),
            maya("It shouldn't. It uses a managed identity. That's a badge Azure gives your app, so the app can prove who it is without any password stored anywhere."),
            leo("And which doors does the badge open?"),
            maya("Whichever doors you give it a role for. A role is a named set of permissions. For pulling images, the role is AcrPull: it lets you download images and nothing more. Its sibling, AcrPush, is for whatever builds and uploads them."),
            leo("Pull for the app, push for the builder. And me, a person at my keyboard?"),
            maya("You type az acr login. It uses your normal Azure sign-in, and hands Docker a short-lived pass. No password to remember there either."),
            leo("I spotted something called the admin user. What's that?"),
            maya("One shared username and password for the whole registry, with both push and pull rights. Like a master key under the doormat. It's off by default, so keep it off, except for a quick test."),
            leo("[laugh] No keys under the doormat. What are the usual mistakes?"),
            maya("Four. Running latest, so a later push quietly changes production. Turning on the admin user because it's quicker. Giving an app AcrPush, or even Owner, the strongest role, when it only pulls. And expecting geo-replication on Basic or Standard. Where the image runs next has its own episodes: App Service, and Container Apps."),
            leo("So what do I remember for the exam?"),
            maya("If the question says one registry name, pulled locally in two regions, the answer is Premium with geo-replication. If images must rebuild when the base image is patched, a task with a base image trigger. If there's no Docker on the machine, az acr build. If an app pulls with no password, a managed identity with AcrPull. And if a push must never change what runs, a unique tag."),
            leo("And anything to try today?"),
            maya("It takes about fifteen minutes. Create a Basic registry and check the admin user is off. Build a small Python image with az acr build and a unique tag. List its tags and its digest. Then push the same tag again, and watch the digest change while the tag stays."),
            leo("A lunchbox with a fingerprint. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-acr",
        listOf(
            maya("Welcome to Case Files. Today's case: The Bus That Said Hello World. A small Android app called Bus Buddy shows when the next bus is due. Its backend, a small Python service on Azure, runs from a container image. On Tuesday morning, every rider opened the app and saw one line: hello from the test build."),
            leo("[chuckle] Detective Leo, reporting. Nobody wants a greeting, they want a bus. First, in plain words, what's a container image?"),
            maya("A packed-up copy of the app and everything it needs to run, like a lunchbox packed the night before. Under the picture, an image is a stack of layers, each one a bundle of files, plus a short list that names the layers in order. Bus Buddy's image is called bus-api."),
            leo("And where does the lunchbox wait before it runs?"),
            maya("In a container registry, a private warehouse for images. Bus Buddy uses Azure Container Registry, ACR for short, which Azure runs for you. Uploading an image is a push. Downloading it is a pull. Inside, each image has its own shelf, called a repository, holding all its versions."),
            leo("Then my first suspect is obvious. Someone put a new version live on Monday night."),
            maya("Good thought, but the log of releases is empty. Nobody released anything. Here's the first clue. The app's settings say it runs bus-api colon latest."),
            leo("Latest. As in, the newest one. That sounds safe."),
            maya("It sounds safe, and that's the trap. Latest is just a tag, a movable label stuck on one version. Whoever pushes next with the same tag moves the label. On Monday night, a developer pushed a test build as latest. On Tuesday the server restarted, pulled latest again, and got the test build."),
            leo("[sigh] So the label wandered off to the wrong lunchbox."),
            maya("Exactly. The fix is a unique tag for every build, like the build's own id, and putting that exact tag live. Or better still, the digest."),
            leo("Digest. Plain words?"),
            maya("A long code worked out from the image's content, like a fingerprint. Under the picture, it's worked out from that short list of layers, and the list names each layer by its own fingerprint, so change one byte anywhere and the fingerprint changes. A tag can move. A digest never can."),
            leo("Right. Suspect two, then. The registry was hacked."),
            maya("Closer than you think. Second clue: the server pulls with the admin user. That's one shared username and password for the whole registry, with push and pull rights, sitting in a settings file. A master key under the doormat. Anyone who finds it can push."),
            leo("So the fix is a stronger password."),
            maya("No. The fix is no password at all. Give the server a managed identity, a badge Azure gives the app. Under the hood, Azure answers the app's request for a short-lived pass on a private address only that machine can reach. Then switch the admin user off."),
            leo("And the badge gets Owner, so it never hits a locked door."),
            maya("[pause] And a third case opens. Owner is the strongest role there is. A role is just a named set of permissions, and the server only downloads. So it gets AcrPull: pull images, and nothing more. AcrPush is for whatever builds the images."),
            leo("Fine. Then who builds them? The developer's laptop, with Docker?"),
            maya("That's how the test build slipped in. Docker is the usual tool for building images, but the team now builds in Azure with ACR Tasks. A quick task is one command, az acr build, typed on Azure's command line. It sends the folder up, builds the image there, and pushes it."),
            leo("And how do they stop a broken build sneaking in again?"),
            maya("A multi-step task. A short file lists the steps: build the image, run the tests inside it, and push only if they pass. Bus Buddy's hello build would have stopped right there, at the tests."),
            leo("And the developers themselves, at their keyboards? Do they share a password?"),
            maya("Not any more. Each one types az acr login. It uses their own Azure sign-in, and hands Docker a short-lived pass. So every push now has a real name on it, which is handy for a detective."),
            leo("Is the case closed?"),
            maya("One more witness. Bus Buddy opened in a second country, and riders there waited ages for the server to start. It was pulling the image from a registry on another continent."),
            leo("Easy. Make a second Basic registry over there, with the same name."),
            maya("Can't. Registry names are unique across Azure, and two registries are two things to keep in step. The answer is geo-replication: copies of one registry in several regions, one name. Azure copies each push to every region, and each pull goes to the nearest copy. It needs the Premium tier, the top price level."),
            leo("And the security report on my desk? It says the image has an old hole in it."),
            maya("Last clue. Bus Buddy's image is built on a ready-made base image, the official slim Python one. The base was patched weeks ago, but nobody rebuilt. A triggered task fixes that: a task with a base image trigger rebuilds bus-api every time the base is updated."),
            leo("And where does this stop working?"),
            maya("Here. A unique tag can still be pushed over by mistake. To make one version truly fixed, lock it, or put the digest live. And a phone never pulls images at all. The app calls the backend, and the backend's host pulls. App Service has its own episode for that side."),
            leo("So, the case summary for the exam?"),
            maya("If a later push quietly changed production, deploy a unique tag or the digest, not latest. If an app must pull with no password, a managed identity with AcrPull, and the admin user off. If one registry name must serve two regions, Premium with geo-replication. And if images must rebuild when the base is patched, a task with a base image trigger."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Basic registry and check the admin user is off. Build a small Python image with az acr build and a unique tag. List its tags and its digest. Then push the same tag again, and see the digest change while the tag stays."),
            leo("The tag stays, the fingerprint tells the truth. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-acr",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Sit down. They moved the Two Tracks Coach backend into its own container registry. The Coach is the study assistant in the Android app, with a Python backend on Azure. And it was quite a week."),
            leo("[laugh] I heard someone called the registry a haunted warehouse. Start at the start. What's a container image?"),
            maya("A packed-up copy of the app and everything it needs to run, like a lunchbox packed the night before. Under the picture, it's a stack of layers, each a bundle of files, plus a short list naming those layers. The Coach's backend image is called coach-api."),
            leo("And the registry is where the lunchboxes wait."),
            maya("A private warehouse for images. Theirs is Azure Container Registry, ACR, named ttcoach. Uploading is a push, downloading is a pull. Each image gets a shelf, called a repository, with all its versions on it."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. Tom set the live server to run coach-api latest. Latest is a tag, a movable label on one version, and it just means whatever was pushed last."),
            leo("I can feel this going wrong."),
            maya("Monday afternoon, the intern pushed a test build as latest. The server restarted that evening, pulled latest, and real learners got a Coach that answered everything with, testing, testing."),
            leo("[chuckle] Testing, testing. What should Tom have done?"),
            maya("Put a unique tag live, like the build's own id. Or the digest: a code worked out from the image's content, like a fingerprint. Under the picture, it's worked out from that short list of layers, which names each layer by its own fingerprint. A tag can move to another image. A digest never can."),
            leo("Tuesday?"),
            maya("Tuesday was Dana. The new server couldn't pull, and Dana was in a hurry. So she switched on the admin user. That's one shared username and password for the whole registry, with push and pull rights. A master key under the doormat."),
            leo("And it worked."),
            maya("It worked, and that's the problem. The password sat in a settings file, and anyone who found it could push. The right way is a managed identity: a badge Azure gives the server. Azure hands the server short-lived passes on a private address nobody outside can reach, so there's no password to leak. Dana switched the admin user off by lunch."),
            leo("And people? Do the developers share that password too?"),
            maya("Not any more. They type az acr login. It uses their own Azure sign-in, and hands Docker, the usual tool for building and running images, a short-lived pass. Dana says she feels lighter."),
            leo("Wednesday. Go."),
            maya("Marcus. Marcus gave the server's badge the Owner role, so it would, quote, never hit a locked door."),
            leo("What's a role, in plain words?"),
            maya("A named set of permissions. Owner can do anything at all, including deleting the registry. The server only ever downloads, so it needs AcrPull: pull images, and nothing more. AcrPush is for whatever builds and uploads them. Marcus took it well. Mostly."),
            leo("Thursday. I sense money."),
            maya("The intern again. The Coach launched in America too, and the American servers were slow to start, pulling images across the ocean. The intern asked for geo-replication on their Standard registry, the middle price level."),
            leo("Geo-replication meaning what?"),
            maya("Copies of one registry in several regions, the parts of the world Azure runs in. One name, a copy in each region, and every pull goes to the nearest copy. Azure copies each push across for you. But it needs the Premium tier, the top price level. Basic and Standard live in one region only."),
            leo("Remind me what the three tiers are actually for."),
            maya("Basic is for learning and small projects. Standard suits most real apps in one region. Premium is for many regions, private networking away from the public internet, and the most speed. The intern had picked Standard to save a little money, which, to be fair, is usually sensible."),
            leo("So the intern was told to upgrade."),
            maya("To Premium, yes. And Friday was the big one. Priya."),
            leo("Priya? Priya never gets it wrong."),
            maya("Which is why everyone's still talking. A security scan found an old hole in coach-api. The image is built on the official slim Python image, its base image, the pizza base under the toppings. The base had been fixed a month ago. Priya rebuilt by hand once a month, and forgot."),
            leo("So how does she stop forgetting?"),
            maya("ACR Tasks, which builds images inside Azure, no Docker, the usual building tool, needed on anyone's laptop. A quick task is one command, az acr build. But Priya set up a triggered task with a base image trigger, so every time the base is patched, coach-api rebuilds itself. For the record, the triggered tasks may not run on free trial accounts."),
            leo("And the testing, testing build from Monday? Could a task have stopped that?"),
            maya("A multi-step task could. A short file of steps: build the image, run the tests inside it, and push only if they pass. Priya added one by Friday evening. Nobody was surprised."),
            leo("Did anything else go wrong?"),
            maya("Only where all this stops. A unique tag can still be pushed over by mistake, so to fix a version for good, lock it, or run the digest. And the Android app itself never pulls images; the backend's host does. The Container Apps episode covers that host."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If a later push changed production, the answer is a unique tag or the digest, not latest. If the app needs a password to pull, no: a managed identity with AcrPull, admin user off. If it's one registry name in two regions, Premium with geo-replication. And if images must rebuild when the base is patched, a base image trigger."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Basic registry and check the admin user is off. Build a small Python image with az acr build and a unique tag. List its tags and its digest. Then push the same tag again, and see the digest change while the tag stays."),
            leo("No testing, testing in production. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-acr",
        listOf(
            nova("Hey, you. It's late, the kettle's warm, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about where your app waits before it runs."),
            ivy("Waiting in the dark. Sounds like me at a bus stop. Go on, Nova."),
            nova("Picture your app packed up for a trip. That's a container image: a copy of your app and everything it needs to run, the code, the right Python, the libraries. Like a lunchbox packed the night before, ready to open anywhere."),
            ivy("A lunchbox. Tell me what it really is, under the picture."),
            nova("A stack of layers, each one a bundle of files, plus a short list that names the layers in order. [pause] And the lunchboxes need somewhere to wait. That's a container registry, a private warehouse for images."),
            ivy("And Azure has one of those."),
            nova("Azure Container Registry, ACR, which Azure runs for you. Say you're building the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. The backend image is coach-api, and it lives in a registry called ttcoach. Uploading an image is a push. Downloading it is a pull."),
            ivy("Push and pull. You're already flirting. How does the warehouse keep things tidy?"),
            nova("With shelves. A repository is one shelf, all the versions of one image. Coach-api is one repository. Each version on it carries a tag, a friendly label, like v1 or v2."),
            ivy("And a label can be peeled off, I suppose."),
            nova("[pause] It can. A tag moves. Push a new image with the same tag, and the label slides onto the new one. That's why the tag called latest is a little dangerous. It just means whatever was pushed last."),
            ivy("Whatever was pushed last. Sounds like my taste in music. So how do I hold on to one exact version?"),
            nova("With the digest. A long code worked out from the image's content, like a fingerprint. Under the picture, it's worked out from that short list of layers, which names each layer by its own fingerprint, so change one byte and the fingerprint changes. A digest never moves."),
            ivy("A fingerprint that never lies. I like that. So what goes live?"),
            nova("A unique tag for every build, or the digest itself. Never latest in production, the live app your users touch. Otherwise a test push at midnight quietly becomes what everyone runs at breakfast."),
            ivy("Midnight pushes. We've all been there. Does every warehouse cost the same?"),
            nova("Three tiers, three price levels. Basic for learning. Standard for most real apps. And Premium, with geo-replication: copies of one registry in several regions, the parts of the world Azure runs in. One name, a copy near each of your servers."),
            ivy("How does a pull know which copy to use?"),
            nova("Azure copies each push to every region for you, and the registry's one name sends each pull to the nearest copy. [pause] Basic and Standard sit in one region only. So, two regions, one name, means Premium."),
            ivy("And who packs the lunchbox? I don't even have Docker on this laptop."),
            nova("Docker is the usual tool for building images, but you don't need it. ACR Tasks builds inside Azure. A quick task is one command on Azure's command line, az acr build. It sends your folder up, a build machine in Azure builds it, and pushes the image into the registry."),
            ivy("And if I'd rather it happened while I sleep?"),
            nova("A triggered task. It starts by itself, on a commit, a saved change to your code, or on a timer, or when the base image is updated. The base is the ready-made image yours is built on, like the slim Python one. Patch the base, and your image rebuilds."),
            ivy("Mm. Fixes arriving while I sleep. Is there more?"),
            nova("A multi-step task. A short file of steps: build, run the tests inside the image, and push only if they pass."),
            ivy("Now the shy part. How does the Coach's server get in, to pull? A password?"),
            nova("No password. A managed identity, a badge Azure gives your app. Azure hands the server short-lived passes, on a private address no one outside can reach. And the badge gets a role, a named set of permissions. For pulling, that role is AcrPull. Download, and nothing more."),
            ivy("Nothing more. How disciplined. And the admin user I keep seeing?"),
            nova("One shared username and password for the whole registry, a master key under the doormat. Off by default. [pause] Leave it off, except for a quick test. And never give an app AcrPush or Owner when all it does is pull."),
            ivy("And me, at my keyboard, at this hour? How do I get in?"),
            nova("You type az acr login. It uses your own Azure sign-in, and hands Docker a short-lived pass. [pause] No password to whisper to anyone."),
            ivy("Give me the whole Coach, then, put together."),
            nova("One Premium registry, ttcoach, with copies in two regions. Images built by a task on every commit and every base image update, each tagged with the build's own id. And pulled by servers with managed identities that hold AcrPull, and nothing more."),
            ivy("Where does all this stop working?"),
            nova("A unique tag can still be pushed over by mistake, so lock the version, or run the digest. Triggered tasks may not run on free trial accounts. And the Android app never pulls images itself; its backend's host does. The App Service containers episode is your next late night for that."),
            ivy("[sigh] Mm. Before I fall asleep on you. What do I remember for the exam?"),
            nova("If one registry name must serve two regions, the answer is Premium with geo-replication. If images must rebuild when the base is patched, a task with a base image trigger. If there's no Docker, az acr build. If an app pulls with no password, a managed identity with AcrPull. And if a push must never change what runs, a unique tag or the digest."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Basic registry and check the admin user is off. Build a small Python image with az acr build and a unique tag. List its tags and its digest. [pause] Then push the same tag again, and see the digest change while the tag stays."),
            ivy("The label moves, the fingerprint stays. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-acr",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about where your app waits before it goes out."),
            rafe("Waiting to go out. I know that feeling. You take hours."),
            nova("[laugh] Worth every minute. Tonight it's Azure Container Registry. First, the thing it stores. A container image is a packed-up copy of your app and everything it needs, like an overnight bag. Under the picture, it's a stack of layers, bundles of files, plus a short list naming them."),
            rafe("An overnight bag. Now you're talking. And the registry?"),
            nova("A private warehouse for those bags. ACR is the one Azure runs for you. Uploading an image is a push, downloading is a pull. Picture the Two Tracks Coach, the study assistant in the Android app, with its Python backend packed as an image called coach-api."),
            rafe("Push and pull. You do know how to talk to me. How is the warehouse arranged?"),
            nova("By shelves. A repository is one shelf, every version of one image. And each version can wear a tag, a friendly label, like v1 or v2."),
            rafe("Then let me test you, darling. Exam style. Production runs coach-api latest. Someone pushes a test build as latest, and after a restart the live app runs it. Why?"),
            nova("Because latest is only a tag, and a tag moves. A new push with the same tag slides the label onto the new image. So the restart pulled the test build."),
            rafe("Right answer. A label is easy to move; a heart, not so much. [kiss]"),
            nova("Mm. Smooth. So what should we put live?"),
            rafe("A unique tag for every build. Or the digest, a long code worked out from the image's content, like a fingerprint. It's worked out from that short list of layers. Change one byte, and the fingerprint changes."),
            nova("So you can tag me however you like, but my digest never changes."),
            rafe("[chuckle] I'd know you anywhere. Your turn to be quizzed. The Coach runs in West Europe and East US, and both must pull locally from one registry name. What do we need?"),
            nova("Geo-replication: copies of one registry in several regions, the parts of the world Azure runs in. Azure copies every push across, and each pull goes to the nearest copy. And it needs Premium, the top tier, or price level."),
            rafe("Right answer. Only the best for you. [kiss]"),
            nova("Basic and Standard stay in one region, like you on a Sunday. Now, who packs the bags? Do we need Docker on the laptop?"),
            rafe("Docker's the usual tool for building images, but no. ACR Tasks builds inside Azure. A quick task is one command on Azure's command line, az acr build. It sends the folder up, builds there, and pushes into the registry."),
            nova("And if the base image gets a security patch? The base is the ready-made image ours is built on, like the slim Python one. What rebuilds coach-api automatically?"),
            rafe("A triggered task with a base image trigger. It also starts on a commit, a saved change to the code, or on a timer. A multi-step task can build, run the tests, and push only if they pass."),
            nova("Right answer, and thorough with it. [kiss]"),
            rafe("Now the intimate part. How does the Coach's server get into the warehouse? I like to know who has a key."),
            nova("No key at all. A managed identity, a badge Azure gives the app. Azure hands the server short-lived passes on a private address nobody outside can reach. Then the badge gets a role, a named set of permissions."),
            rafe("Which role? Quiz time. The server only ever downloads images."),
            nova("Owner. Then it can do anything it likes."),
            rafe("[sigh] No kiss for that one. Owner can even delete the registry. The server gets AcrPull: pull images, and nothing more. AcrPush is for whatever builds. It's all about knowing your role."),
            nova("Knowing my role. Point taken. And the admin user?"),
            rafe("One shared username and password for the whole registry, with push and pull rights. A spare key under the doormat. It's off by default. Keep it off, except for a quick test."),
            nova("Quiz for you, then. Why is the admin user a poor choice for the Coach's server?"),
            rafe("Because it's one shared password, with push and pull rights to the whole registry, sitting somewhere it can leak. A managed identity with AcrPull can only pull, leaves a record of who did what, and has no secret to lose or change."),
            nova("Right answer. Some doors stay private. [kiss]"),
            rafe("And me, at my keyboard? How do I get in, without a password?"),
            nova("You type az acr login. It uses your own Azure sign-in, and hands Docker a short-lived pass. Your own way in, darling, no shared secrets."),
            rafe("So the whole Coach, put together?"),
            nova("One Premium registry, ttcoach, with copies in two regions. Images built by a task on every commit and every base image update, each tagged with its build's own id. And pulled by managed identities holding AcrPull, and nothing more. A tidy household."),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("A unique tag can still be pushed over, so lock that version, or run the digest. Triggered tasks may not run on a free trial account. And the phone never pulls images; its backend's host does. The Container Apps episode has that host, and the App Service one too."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("If a later push changed production, the answer is a unique tag or the digest, not latest. If the app must pull with no password, a managed identity with AcrPull, admin user off. If it's one registry name in two regions, Premium with geo-replication. And if images must rebuild when the base is patched, a base image trigger."),
            rafe("Four for four. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, my love?"),
            rafe("Create a Basic registry and check the admin user is off. Build a small Python image with az acr build and a unique tag. List its tags and its digest. Then push the same tag again, and see the digest change while the tag stays."),
            nova("Labels come and go. Fingerprints stay. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "An image is built from source, pushed into the registry, and pulled by the hosts that run it.",
        nodes = listOf(
            DNode("src", "Source code", 0.2f, 0.15f, w = 0.34f, h = 0.18f, kind = NodeKind.NOTE, sub = "the app + recipe"),
            DNode("task", "ACR Tasks build", 0.75f, 0.15f, w = 0.4f, h = 0.18f, tone = 1, sub = "inside Azure"),
            DNode("acr", "Registry", 0.5f, 0.5f, w = 0.44f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "coach-api images"),
            DNode("app", "App Service", 0.25f, 0.85f, w = 0.4f, h = 0.18f, tone = 1, sub = "pulls and runs"),
            DNode("aca", "Container Apps", 0.75f, 0.85f, w = 0.4f, h = 0.18f, tone = 1, sub = "pulls and runs"),
        ),
        edges = listOf(
            DEdge("src", "task", "build"),
            DEdge("task", "acr", "push"),
            DEdge("acr", "app", "pull"),
            DEdge("acr", "aca", "pull"),
        ),
        aspect = 0.8f,
    )

    private fun names() = DiagramSpec(
        "A repository holds one image's versions; a tag is a movable label, a digest the content's fixed fingerprint.",
        nodes = listOf(
            DNode("repo", "Repository: coach-api", 0.5f, 0.14f, w = 0.8f, h = 0.2f, sub = "every version of one image"),
            DNode("tag", "Tag", 0.25f, 0.5f, w = 0.42f, h = 0.22f, sub = "movable label: v1"),
            DNode("digest", "Digest", 0.75f, 0.5f, w = 0.42f, h = 0.22f, tone = 2, sub = "fingerprint, never moves"),
            DNode("image", "One exact image", 0.5f, 0.85f, w = 0.6f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "its layers and list"),
        ),
        edges = listOf(
            DEdge("repo", "tag"),
            DEdge("repo", "digest"),
            DEdge("tag", "image", "points at, for now"),
            DEdge("digest", "image", "always this one"),
        ),
        aspect = 0.8f,
    )

    private fun tiers() = DiagramSpec(
        "Basic and Standard sit in one region; Premium geo-replicates one registry name to copies in other regions.",
        nodes = listOf(
            DNode("basic", "Basic", 0.17f, 0.25f, w = 0.3f, h = 0.26f, sub = "learning"),
            DNode("std", "Standard", 0.5f, 0.25f, w = 0.3f, h = 0.26f, sub = "most apps"),
            DNode("prem", "Premium", 0.83f, 0.25f, w = 0.3f, h = 0.26f, tone = 1, sub = "geo-replication"),
            DNode("west", "West Europe copy", 0.25f, 0.8f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "nearest pull"),
            DNode("east", "East US copy", 0.75f, 0.8f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "nearest pull"),
        ),
        edges = listOf(
            DEdge("prem", "west", "replica"),
            DEdge("prem", "east", "replica"),
        ),
        aspect = 0.8f,
    )

    private fun tasks() = DiagramSpec(
        "ACR Tasks build in Azure: a quick task once, a triggered task on a commit or base image update, a multi-step task with tests.",
        nodes = listOf(
            DNode("quick", "Quick task", 0.25f, 0.15f, w = 0.42f, h = 0.2f, sub = "az acr build, once"),
            DNode("trig", "Triggered task", 0.75f, 0.15f, w = 0.42f, h = 0.2f, sub = "commit, timer, base"),
            DNode("multi", "Multi-step task", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "build, test, push"),
            DNode("base", "Base image patched", 0.75f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "official slim Python"),
            DNode("acr", "Registry", 0.5f, 0.84f, w = 0.44f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "new image pushed"),
        ),
        edges = listOf(
            DEdge("base", "trig", "triggers"),
            DEdge("quick", "acr", "push"),
            DEdge("trig", "acr", "push"),
            DEdge("multi", "acr", "push if tests pass"),
        ),
        aspect = 0.8f,
    )

    private fun access() = DiagramSpec(
        "People sign in with az acr login; apps pull with a managed identity holding AcrPull; the admin user stays off.",
        nodes = listOf(
            DNode("you", "You", 0.12f, 0.18f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("login", "az acr login", 0.5f, 0.18f, w = 0.4f, h = 0.2f, sub = "your Azure sign-in"),
            DNode("acr", "Registry", 0.5f, 0.52f, w = 0.4f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "ttcoach"),
            DNode("admin", "Admin user", 0.85f, 0.52f, w = 0.28f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "keep it off"),
            DNode("mi", "Managed identity", 0.24f, 0.86f, w = 0.44f, h = 0.2f, tone = 1, sub = "the app's badge"),
            DNode("role", "AcrPull role", 0.76f, 0.86f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "pull, nothing more"),
        ),
        edges = listOf(
            DEdge("you", "login"),
            DEdge("login", "acr", "short-lived pass"),
            DEdge("mi", "acr", "pull"),
            DEdge("role", "mi", "granted to"),
            DEdge("admin", "acr", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where it stops: a tag can still be pushed over, the phone never pulls, and trial accounts may not run tasks.",
        nodes = listOf(
            DNode("lock", "Lock the version", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "or run the digest"),
            DNode("phone", "The phone never pulls", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "its backend's host does"),
            DNode("trial", "Free trial account", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "tasks may not run"),
            DNode("next", "Other episodes", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "App Service, Container Apps"),
        ),
        edges = listOf(DEdge("phone", "next", "the host")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's registry questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("regions", "Two regions, one name", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium, geo-replication"),
            DNode("base", "Base image patched", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "base image trigger"),
            DNode("nodocker", "No Docker on the laptop", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "az acr build"),
            DNode("nopass", "Pull with no password", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "identity with AcrPull"),
            DNode("latest", "A push changed prod", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "unique tag or digest"),
            DNode("admin", "Admin user for an app", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "no: keep it off"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("regions", "base", "nodocker", "nopass", "latest", "admin")
    private val today = BoardCard("Today", "A Basic registry, admin off; az acr build; tags and digest; push the tag again.")
    private fun quickCode(registry: String, image: String) = BoardCard("Quick task", "az acr build --registry $registry --image $image .", code = true)

    private fun classicBoard() = PodcastBoard(
        "ai-acr",
        listOf(
            // 0: "Hi, and welcome back. Today is about where your app waits before it runs..."
            Scene(
                0, "Images and registries", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src"), light = setOf("src"), card = BoardCard("Container image", "A packed-up copy of your app and all it needs, like a lunchbox.")),
                    Beat(2, show = setOf("acr"), light = setOf("acr"), card = BoardCard("Container registry", "A private warehouse for images. Upload is a push; download a pull.")),
                    Beat(4, show = setOf("task"), light = setOf("acr"), card = BoardCard("ACR", "Azure Container Registry: a private registry Azure runs for you.")),
                    Beat(6, show = setOf("app", "aca"), light = setOf("acr", "app"), pulse = "acr" to "app", card = BoardCard("Two Tracks Coach", "The backend image coach-api, in a registry called ttcoach.")),
                ),
            ),
            // 7: "And inside the warehouse, is it just one big pile of lunchboxes?"
            Scene(
                7, "Shelves, tags, digests", names(),
                beats = listOf(
                    Beat(8, show = setOf("repo"), light = setOf("repo"), card = BoardCard("Repository", "One shelf: every version of one image, such as coach-api.")),
                    Beat(10, show = setOf("tag", "image"), light = setOf("tag"), pulse = "tag" to "image", card = BoardCard("Tag", "A movable label, like v1. A new push with the same tag moves it.")),
                    Beat(12, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "A fingerprint worked out from the content. It never moves.")),
                    Beat(14, light = setOf("tag"), card = BoardCard("latest", "Just a tag: whatever was pushed last. Risky in production.")),
                    Beat(16, light = setOf("digest", "tag"), card = BoardCard("What to put live", "A unique tag for every build, or the digest itself.")),
                ),
            ),
            // 17: "Got it. Now, does every registry cost the same?"
            Scene(
                17, "Three tiers", tiers(),
                beats = listOf(
                    Beat(18, show = setOf("basic", "std", "prem"), light = setOf("prem"), card = BoardCard("Tiers", "Basic to learn, Standard for most apps, Premium for many regions.")),
                    Beat(20, show = setOf("west", "east"), light = setOf("west", "east"), pulse = "prem" to "west", card = BoardCard("Geo-replication", "One registry name, a copy in each region; pulls go to the nearest.")),
                    Beat(22, light = setOf("prem"), pulse = "prem" to "east", card = BoardCard("Premium only", "Basic and Standard sit in one region. Premium adds private networking.")),
                ),
            ),
            // 23: "Okay. How does an image get into the registry?..."
            Scene(
                23, "Build in Azure", tasks(),
                beats = listOf(
                    Beat(24, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode("ttcoach", "coach-api:v1")),
                    Beat(26, show = setOf("trig"), light = setOf("trig"), pulse = "trig" to "acr", card = BoardCard("Triggered task", "Starts by itself: a commit, a timer, or a base image update.")),
                    Beat(28, show = setOf("base"), light = setOf("base", "trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "The base gets a security fix, and your image rebuilds by itself.")),
                    Beat(30, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests inside the image, push only if they pass.")),
                ),
            ),
            // 31: "Okay, it's built and pushed. Now the Coach's server must pull it..."
            Scene(
                31, "Pulling without a password", access(),
                beats = listOf(
                    Beat(31, show = setOf("acr")),
                    Beat(32, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "A badge Azure gives your app: no password stored anywhere.")),
                    Beat(34, show = setOf("role"), light = setOf("role", "mi"), pulse = "role" to "mi", card = BoardCard("AcrPull", "Download images and nothing more. AcrPush is for the builder.")),
                    Beat(36, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "Your own Azure sign-in, and a short-lived pass for Docker.")),
                    Beat(38, show = setOf("admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "One shared password for the whole registry. Keep it off.")),
                ),
            ),
            // 39: "No keys under the doormat. What are the usual mistakes?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = setOf("latest", "admin"), light = setOf("latest", "admin"), card = BoardCard("The usual mistakes", "latest live, admin user on, Owner for an app, Basic for regions.")),
                    Beat(42, show = setOf("regions", "base", "nodocker", "nopass"), light = all6, card = BoardCard("Listen for the phrase", "Each phrase points to one answer: Premium, a trigger, AcrPull, a tag.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-acr--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Bus That Said Hello World..."
            Scene(
                0, "The Bus That Said Hello World",
                DiagramSpec(
                    "Bus Buddy's backend pulled bus-api:latest after a restart, and latest now pointed at a test build.",
                    nodes = listOf(
                        DNode("phone", "Bus Buddy app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Android"),
                        DNode("test", "Test build", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "hello from test"),
                        DNode("api", "Python backend", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "runs latest"),
                        DNode("acr", "Registry", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "bus-api"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "calls"),
                        DEdge("api", "acr", "pull latest"),
                        DEdge("acr", "test", "latest moved", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "test"), light = setOf("test"), card = BoardCard("The case", "Every rider opens Bus Buddy and sees: hello from the test build.")),
                    Beat(2, show = setOf("api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Container image", "The app packed with all it needs: layers of files, and a list.")),
                    Beat(4, show = setOf("acr"), light = setOf("acr"), pulse = "api" to "acr", card = BoardCard("Container registry", "A private warehouse for images: push to upload, pull to download.")),
                    Beat(6, light = setOf("api"), card = BoardCard("Clue one", "The backend's settings say: run bus-api:latest.")),
                    Beat(8, light = setOf("acr", "test"), pulse = "acr" to "test", card = BoardCard("latest moved", "A test build was pushed as latest; the restart pulled it.")),
                ),
            ),
            // 9: "So the label wandered off to the wrong lunchbox."
            Scene(
                9, "Tags and digests", names(),
                beats = listOf(
                    Beat(10, show = setOf("repo", "tag", "image"), light = setOf("tag"), pulse = "tag" to "image", card = BoardCard("Unique tag", "A unique tag for every build, like the build's own id.")),
                    Beat(12, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "The content's fingerprint. A tag can move; a digest never can.")),
                ),
            ),
            // 13: "Right. Suspect two, then. The registry was hacked."
            Scene(
                13, "Suspect two", access(),
                beats = listOf(
                    Beat(14, show = setOf("acr", "admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "One shared password with push and pull, left in a settings file.")),
                    Beat(16, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "Azure hands the server short-lived passes. No password at all.")),
                    Beat(18, show = setOf("role"), light = setOf("role"), pulse = "role" to "mi", card = BoardCard("AcrPull, not Owner", "A role is a set of permissions. Pull images, nothing more.")),
                ),
            ),
            // 19: "Fine. Then who builds them? The developer's laptop, with Docker?"
            Scene(
                19, "Who builds it", tasks(),
                beats = listOf(
                    Beat(20, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode("busbuddy", "bus-api:run42")),
                    Beat(22, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests, push only if they pass.")),
                    Beat(24, card = BoardCard("az acr login", "Each developer signs in as themselves; every push has a name.")),
                ),
            ),
            // 25: "Is the case closed?"
            Scene(
                25, "The far-away riders", tiers(),
                beats = listOf(
                    Beat(27, show = setOf("basic", "std"), light = setOf("basic"), card = BoardCard("Two Basic registries?", "Registry names are unique across Azure, and two would drift apart.")),
                    Beat(28, show = setOf("prem", "west", "east"), light = setOf("prem", "east"), pulse = "prem" to "east", card = BoardCard("Geo-replication", "One name, a copy in each region, the nearest pull. Premium only.")),
                ),
            ),
            // 29: "And the security report on my desk?..."
            Scene(
                29, "The old hole", tasks(),
                beats = listOf(
                    Beat(30, show = setOf("quick", "trig", "multi", "base", "acr"), light = setOf("base", "trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "The base gets patched, and a triggered task rebuilds bus-api.")),
                ),
            ),
            // 31: "And where does this stop working?"
            Scene(
                31, "Where it stops", limits(),
                beats = listOf(
                    Beat(32, show = setOf("lock", "phone", "trial", "next"), light = setOf("lock", "phone"), pulse = "phone" to "next", card = BoardCard("Where it stops", "Lock a version, or run the digest. The host pulls, not the phone.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("latest", "nopass", "regions", "base"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer: a tag, AcrPull, Premium, a trigger.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-acr--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "What the Coach team did", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src", "task"), light = setOf("src"), pulse = "src" to "task", card = BoardCard("The week", "The Coach backend moved into its own container registry.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Container image", "A lunchbox: layers of files, plus a short list naming them.")),
                    Beat(4, show = setOf("acr", "app", "aca"), light = setOf("acr"), pulse = "task" to "acr", card = BoardCard("ACR: ttcoach", "A private warehouse. Push to upload, pull to download.")),
                ),
            ),
            // 5: "Okay. Who went first?"
            Scene(
                5, "Monday: Tom", names(),
                beats = listOf(
                    Beat(6, show = setOf("repo", "tag", "image"), light = setOf("tag"), pulse = "tag" to "image", card = BoardCard("latest", "A movable tag that means whatever was pushed last.")),
                    Beat(8, light = setOf("tag"), card = BoardCard("Testing, testing", "The intern pushed a test build as latest; a restart pulled it.")),
                    Beat(10, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Unique tag or digest", "A digest is the content's fingerprint. It never moves.")),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Dana", access(),
                beats = listOf(
                    Beat(12, show = setOf("acr", "admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "One shared password, push and pull, for the whole registry.")),
                    Beat(14, show = setOf("mi", "role"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "Short-lived passes from Azure on a private address. Nothing to leak.")),
                    Beat(16, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "People sign in as themselves; Docker gets a short-lived pass.")),
                ),
            ),
            // 17: "Wednesday. Go."
            Scene(
                17, "Wednesday: Marcus", access(),
                beats = listOf(
                    Beat(18, show = setOf("you", "login", "acr", "admin", "mi"), light = setOf("mi"), card = BoardCard("Owner", "Can do anything at all, even delete the registry.")),
                    Beat(20, show = setOf("role"), light = setOf("role"), pulse = "role" to "mi", card = BoardCard("AcrPull", "Pull images, nothing more. AcrPush is for whatever builds.")),
                ),
            ),
            // 21: "Thursday. I sense money."
            Scene(
                21, "Thursday: the intern", tiers(),
                beats = listOf(
                    Beat(22, show = setOf("basic", "std", "prem"), light = setOf("std"), card = BoardCard("Standard", "One region only. American servers pulled across the ocean.")),
                    Beat(24, show = setOf("west", "east"), light = setOf("prem", "east"), pulse = "prem" to "east", card = BoardCard("Geo-replication", "One name, a copy in each region, the nearest pull. Premium only.")),
                    Beat(26, light = setOf("basic", "std", "prem"), card = BoardCard("The three tiers", "Basic to learn, Standard for most apps, Premium for regions.")),
                ),
            ),
            // 27: "So the intern was told to upgrade."
            Scene(
                27, "Friday: Priya", tasks(),
                beats = listOf(
                    Beat(30, show = setOf("base", "acr"), light = setOf("base"), card = BoardCard("An old hole", "The base image was patched a month ago, and nobody rebuilt.")),
                    Beat(32, show = setOf("quick", "trig"), light = setOf("trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "Every patch to the base rebuilds coach-api by itself.")),
                    Beat(34, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests, and push only if they pass.")),
                ),
            ),
            // 35: "Did anything else go wrong?"
            Scene(
                35, "Where it stops", limits(),
                beats = listOf(
                    Beat(36, show = setOf("lock", "phone", "trial", "next"), light = setOf("lock", "phone"), pulse = "phone" to "next", card = BoardCard("Where it stops", "Lock a version, or run the digest. The host pulls, not the phone.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("latest", "nopass", "regions", "base"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-acr--late-night",
        listOf(
            // 0: "Hey, you. It's late, the kettle's warm..."
            Scene(
                0, "Where your app waits", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src"), card = BoardCard("Tonight", "Where your app waits before it runs.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Container image", "A copy of your app and all it needs: a lunchbox, packed.")),
                    Beat(4, show = setOf("task", "acr"), light = setOf("acr"), card = BoardCard("Container registry", "Layers of files plus a list, kept in a private warehouse.")),
                    Beat(6, show = setOf("app", "aca"), light = setOf("acr", "app"), pulse = "acr" to "app", card = BoardCard("ACR: ttcoach", "The Coach's image coach-api. Push to upload, pull to download.")),
                ),
            ),
            // 7: "Push and pull. You're already flirting..."
            Scene(
                7, "Shelves and labels", names(),
                beats = listOf(
                    Beat(8, show = setOf("repo", "tag", "image"), light = setOf("repo", "tag"), pulse = "tag" to "image", card = BoardCard("Repository and tag", "One shelf per image; a friendly label on each version.")),
                    Beat(10, light = setOf("tag"), card = BoardCard("latest", "A tag that slides to whatever was pushed last.")),
                    Beat(12, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "The content's fingerprint. It never moves.")),
                    Beat(14, light = setOf("digest", "tag"), card = BoardCard("What goes live", "A unique tag for every build, or the digest. Never latest.")),
                ),
            ),
            // 15: "Midnight pushes. We've all been there..."
            Scene(
                15, "Three tiers", tiers(),
                beats = listOf(
                    Beat(16, show = setOf("basic", "std", "prem", "west", "east"), light = setOf("prem"), pulse = "prem" to "west", card = BoardCard("Geo-replication", "Premium: one name, a copy near each of your servers.")),
                    Beat(18, light = setOf("west", "east"), pulse = "prem" to "east", card = BoardCard("The nearest copy", "Azure copies each push; each pull goes to the closest region.")),
                ),
            ),
            // 19: "And who packs the lunchbox?..."
            Scene(
                19, "Built while you sleep", tasks(),
                beats = listOf(
                    Beat(20, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode("ttcoach", "coach-api:v1")),
                    Beat(22, show = setOf("trig", "base"), light = setOf("trig", "base"), pulse = "base" to "trig", card = BoardCard("Triggered task", "A commit, a timer, or a base image update starts it.")),
                    Beat(24, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests, push only if they pass.")),
                ),
            ),
            // 25: "Now the shy part..."
            Scene(
                25, "Getting in", access(),
                beats = listOf(
                    Beat(26, show = setOf("acr", "mi", "role"), light = setOf("mi", "role"), pulse = "mi" to "acr", card = BoardCard("Managed identity, AcrPull", "A badge with one permission: download images.")),
                    Beat(28, show = setOf("admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "A master key under the doormat. Leave it off.")),
                    Beat(30, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "Your own Azure sign-in, and a short-lived pass for Docker.")),
                    Beat(32, light = setOf("acr", "mi", "role"), card = BoardCard("The whole Coach", "Premium, two regions, task builds, build-id tags, AcrPull.")),
                ),
            ),
            // 33: "Where does all this stop working?"
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, show = setOf("lock", "phone", "trial", "next"), light = setOf("lock", "trial", "next"), pulse = "phone" to "next", card = BoardCard("Your next late night", "The App Service containers episode: the host that pulls.")),
                ),
            ),
            // 35: "Before I fall asleep on you. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("regions", "base", "nodocker", "nopass", "latest"), card = BoardCard("Listen for the phrase", "Premium, a base trigger, az acr build, AcrPull, a unique tag.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-acr--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src"), card = BoardCard("Date night", "Where your app waits before it goes out: ACR.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Container image", "An overnight bag: layers of files, plus a list naming them.")),
                    Beat(4, show = setOf("task", "acr", "app", "aca"), light = setOf("acr"), pulse = "acr" to "app", card = BoardCard("Container registry", "A private warehouse. Push to upload, pull to download.")),
                ),
            ),
            // 5: "Push and pull. You do know how to talk to me..."
            Scene(
                5, "Labels and fingerprints", names(),
                beats = listOf(
                    Beat(6, show = setOf("repo", "tag", "image"), light = setOf("repo", "tag"), pulse = "tag" to "image", card = BoardCard("Repository and tag", "One shelf per image; a label on each version.")),
                    Beat(7, light = setOf("tag"), card = BoardCard("Quiz: latest", "A test build pushed as latest reaches production. Why?")),
                    Beat(9, light = setOf("tag"), card = BoardCard("Right answer", "A tag moves. A new push slides the label onto the new image.")),
                    Beat(11, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "A fingerprint of the content. Change one byte, and it changes.")),
                ),
            ),
            // 13: "I'd know you anywhere. Your turn to be quizzed..."
            Scene(
                13, "A copy in every city", tiers(),
                beats = listOf(
                    Beat(13, show = setOf("basic", "std", "prem"), light = setOf("prem"), card = BoardCard("Quiz: two regions", "One registry name, local pulls in two regions. What do we need?")),
                    Beat(14, show = setOf("west", "east"), light = setOf("west", "east"), pulse = "prem" to "west", card = BoardCard("Geo-replication", "A copy in each region, the nearest pull. Premium only.")),
                ),
            ),
            // 16: "Basic and Standard stay in one region... who packs the bags?"
            Scene(
                16, "Who packs the bags", tasks(),
                beats = listOf(
                    Beat(17, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode("ttcoach", "coach-api:v1")),
                    Beat(18, show = setOf("base", "trig"), light = setOf("base"), pulse = "base" to "trig", card = BoardCard("Quiz: the base is patched", "What rebuilds coach-api when the base gets a fix?")),
                    Beat(19, show = setOf("multi"), light = setOf("trig", "multi"), pulse = "multi" to "acr", card = BoardCard("Right answer", "A triggered task with a base image trigger.")),
                ),
            ),
            // 21: "Now the intimate part..."
            Scene(
                21, "Knowing your role", access(),
                beats = listOf(
                    Beat(22, show = setOf("acr", "mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "A badge from Azure: short-lived passes, no key.")),
                    Beat(23, light = setOf("mi"), card = BoardCard("Quiz: it only downloads", "The server only pulls images. Which role?")),
                    Beat(25, show = setOf("role"), light = setOf("role"), pulse = "role" to "mi", card = BoardCard("No kiss: it's AcrPull", "Owner can delete the registry. AcrPull pulls, nothing more.")),
                    Beat(27, show = setOf("admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "A spare key under the doormat. Off by default; keep it off.")),
                    Beat(32, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "Your own Azure sign-in, and a short pass for Docker.")),
                    Beat(34, light = setOf("acr", "mi", "role"), card = BoardCard("The whole Coach", "Premium, two regions, task builds, build-id tags, AcrPull.")),
                ),
            ),
            // 35: "And where does it all stop working? Every romance has its limits."
            Scene(
                35, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(36, show = setOf("lock", "phone", "trial", "next"), light = setOf("lock", "phone"), pulse = "phone" to "next", card = BoardCard("Where it stops", "Lock a version, or run the digest. The host pulls, not the phone.")),
                ),
            ),
            // 37: "Last question, for the exam. What do we remember?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("latest", "nopass", "regions", "base"), card = BoardCard("Listen for the phrase", "A unique tag, AcrPull, Premium, a base trigger. Four for four.")),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
