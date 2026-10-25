package com.shahaabapps.core.content

/** Azure Container Registry: build, store and version images: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAcr {
    private val classic = Podcast(
        "ai-acr",
        listOf(
            maya("Hi, and welcome back. Last time we walked one order through Zaiqa: the Kotlin app, the dashboard, and the Zaiqa API, the dot NET 9 service that writes every order into Azure SQL. Today that API goes into a box."),
            leo("A box. You mean a container. Right now our API runs on my laptop, and on a test server someone set up by hand. Never quite the same."),
            maya("That is the problem. Someone installed the dot NET runtime there, the pieces our code needs in order to run, and copied the build across. One missing piece and the API will not start."),
            leo("Define image, in plain words."),
            maya("A box holding our compiled API and everything underneath it: the dot NET runtime, the system libraries, the lot. Like a tiffin packed the night before, ready to open anywhere."),
            leo("Is the whole operating system in the tiffin?"),
            maya("Part of it. The box starts from a base image: a slim copy of an operating system's files, a cut-down Linux with its shell and system libraries. What is left out is the kernel."),
            leo("The kernel being?"),
            maya("The core of the operating system, the part that drives the hardware: memory, disk, network. Every container on a server shares that server's one kernel. A virtual machine brings its own."),
            leo("So an image is megabytes, not gigabytes, and starts in seconds."),
            maya("Right. And you write the box as a recipe, a Dockerfile. FROM names the base image, then you copy things in and set the start command. For our API we write it in two stages."),
            leo("Two stages? Why would one box need two?"),
            maya("Because building dot NET code and running it want different things. Stage one uses the big build image, the one with the compiler, and runs dotnet publish. Stage two uses the small ASP dot NET Core runtime image, and copies only the published output across."),
            leo("So the compiler never ships."),
            maya("Never, and nor does our source. The finished zaiqa-api image is a few hundred megabytes instead of well over a gigabyte, with less in it for a security scan to complain about."),
            leo("Good. Now where does the box live, once it is built?"),
            maya("In a container registry: a private warehouse for images, holding whole ready-to-run boxes. Uploading one in is a push. A host downloading one to run is a pull."),
            leo("And Azure runs one of those."),
            maya("Azure Container Registry, or ACR, is the one Azure runs for you, a private cousin of Docker Hub, the big public warehouse. Ours is called zaiqa."),
            leo("[chuckle] And inside the warehouse, is it one big pile of boxes?"),
            maya("Shelves. A repository is one shelf: every version of one image. So zaiqa-api is a repository, and every build of the API goes on that shelf."),
            leo("How do I tell the versions on a shelf apart?"),
            maya("Two ways. A tag is a friendly label stuck on one version, like one point four. A tag can move: push a new image with the same tag, and the label slides onto it."),
            leo("And the second way?"),
            maya("The digest: a long code worked out from every byte of the image, like a fingerprint. Change one byte and you get a different one. A digest never moves."),
            leo("So what about the tag called latest? Everybody uses latest."),
            maya("It is just a tag meaning whatever was pushed last. Fine on a laptop, risky in production, the live app your real users touch. For example, someone pushes a test build as latest, and that evening Zaiqa quietly serves it."),
            leo("[sigh] Real orders on a test build. So what do we deploy?"),
            maya("A unique tag for every build, the build's own number, or the digest itself. Then a later push cannot change what is running tonight."),
            leo("Fine. Who builds the image? Do I need Docker on my laptop?"),
            maya("You do not. ACR Tasks builds images for you inside Azure, on Azure's own machines. The simplest kind is a quick task: one command, az acr build, typed on Azure's command line."),
            leo("And what does that command actually do?"),
            maya("It sends your folder up, builds the image there from your Dockerfile, and pushes it into the registry. Picture a laptop with no Docker on it at all. It still works."),
            leo("Nice. What else can a task do?"),
            maya("It can start by itself. That is a triggered task: it runs on a commit, meaning a saved change to our code, or on a timer, or when the base image is updated."),
            leo("The base image. The dot NET one we build on?"),
            maya("That one. Microsoft patches it regularly. A task with a base image trigger notices, rebuilds zaiqa-api on the patched base, and pushes it, with nobody remembering anything."),
            leo("That is the one I would forget. Any other kind?"),
            maya("A multi-step task. A short file lists the steps: build the image, run the API's tests inside it, and push only if they pass. For example, Zaiqa never ships a build whose order tests failed."),
            leo("Now the registry itself. Does it all cost the same?"),
            maya("Three tiers, which are just price levels. Basic for learning. Standard for most real apps in one region. Premium is the top one, and it carries two things the exam loves."),
            leo("Which two?"),
            maya("Geo-replication, and private endpoints. Geo-replication means copies of one registry in several regions, the parts of the world Azure runs in. One name, and every pull goes to the nearest copy."),
            leo("So if Zaiqa opens in a second country too..."),
            maya("Both sides pull locally from the same registry name, and Azure keeps the copies in step. Basic and Standard live in one region."),
            leo("And the other one?"),
            maya("A private endpoint is a private address for the registry inside your own network, so pulls never cross the public internet. Also Premium."),
            leo("Different question. The taste service starts from a ready-made Python image in a public registry. Do we rebuild it ourselves?"),
            maya("No, you copy it in, with az acr import. It copies an image straight from another registry into yours, inside Azure, with no Docker and nothing downloaded to your laptop."),
            leo("And the shelves must fill up. Do old images cost us?"),
            maya("They do, you pay for the storage. So a purge task on a timer deletes images past a chosen age, and the untagged leftovers nobody can name."),
            leo("Last piece. The API runs on Container Apps, and that host has to pull. A password?"),
            maya("It should not. It uses a managed identity: a badge Azure gives your app, so it proves who it is with no password anywhere. The badge then gets a role, a named set of permissions."),
            leo("And which role, for a host that only downloads?"),
            maya("AcrPull. Pull images, and nothing more. The build pipeline that uploads them gets AcrPush. Push for the builder, pull for the runner."),
            leo("And me, a person at my keyboard?"),
            maya("You type az acr login. It uses your own Azure sign-in and hands Docker a short-lived pass. No shared password anywhere."),
            leo("I saw something called the admin user in the portal."),
            maya("One shared username and password for the whole registry, with push and pull rights. A master key under the doormat. It is off by default; keep it off."),
            leo("And when the role is missing, what do I actually see?"),
            maya("The release never starts, and the host reports ImagePullBackOff: it tried to download the image, failed, and backed off before trying again. Usually the badge is missing AcrPull."),
            leo("Not a broken Dockerfile, then."),
            maya("Rarely. A wrong image name and a missing AcrPull look identical from outside, so check the role first. Crash loops are the troubleshooting episode; where the API runs is the Container Apps one."),
            leo("So what do I remember for the exam?"),
            maya("One registry name serving two regions with local pulls: Premium with geo-replication. Images that must rebuild when the base image is patched: a base image trigger. No Docker on the machine: az acr build. An app that pulls with no password: a managed identity holding AcrPull. And a push that must never change what runs: a unique tag."),
            leo("And something to try today?"),
            maya("Create a Basic registry and check the admin user is off. Build a small image with az acr build and a unique tag. List its tags and its digest. Then push that tag again, and watch the digest change while the tag stays."),
            leo("The label moves, the fingerprint tells the truth. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-acr",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Night Zaiqa Sold Dinner At Last Month's Prices. The Zaiqa API, the dot NET 9 service behind the app, restarted at nine on Friday and came back with a fortnight-old menu. Nobody had deployed anything."),
            leo("[chuckle] Detective Leo, reporting. A cheap dinner for the customers, an awkward morning for finance. First, the evidence. We packed the API into a container image last month. Remind me exactly what that is."),
            maya("A box holding the compiled API and everything underneath it: the dot NET runtime, the system libraries, the lot. Like a tiffin packed the night before, so the server needs nothing installed by hand."),
            leo("And the operating system is in the tiffin?"),
            maya("Only a slim copy of its files, the base image: a cut-down Linux with its shell and system libraries. No kernel, the core that drives the hardware. Every container shares the server's one kernel, while a virtual machine brings its own."),
            leo("Which is why it is megabytes and starts in seconds. And our recipe?"),
            maya("A Dockerfile, in two stages. Stage one starts from the big dot NET build image, the one with the compiler, and runs dotnet publish. Stage two starts from the small ASP dot NET Core runtime image and copies only the published output over."),
            leo("So the compiler and our source never leave the building."),
            maya("Exactly, and what ships is a few hundred megabytes rather than well over a gigabyte. Now, where was it waiting? In a container registry, a private warehouse for images. Azure Container Registry, or ACR, is the one Azure runs for you, and ours is called zaiqa."),
            leo("And the boxes inside it?"),
            maya("On shelves. A repository is one shelf, every version of one image: zaiqa-api. Uploading a new version into the registry is a push, and the host downloading it to run is a pull."),
            leo("Then my first suspect is obvious. Somebody deployed on Friday night."),
            maya("The release log is empty. Nobody deployed. Here is clue one: Container Apps was told to run zaiqa-api latest."),
            leo("Latest. As in the newest build. That sounds safe."),
            maya("It sounds safe, and that is the trap. Latest is only a tag, a friendly label stuck on one version, and a tag moves. Whoever pushes next with that tag drags the label onto their image."),
            leo("So who pushed?"),
            maya("A developer, a fortnight earlier, rerunning an old build to check something. It landed as latest and sat there. Friday's restart pulled latest, and got a fortnight-old Zaiqa."),
            leo("[sigh] The label wandered off. What stops that?"),
            maya("A unique tag for every build, the build's own number, deployed by name. Or better, the digest: a long code worked out from every byte of the image, like a fingerprint. A tag moves; a digest never can."),
            leo("Right. Suspect two. Somebody broke into the registry."),
            maya("Closer than you think, though not this time. Clue two: the admin user was switched on. One shared username and password for the whole registry, with push and pull rights, sitting in a settings file."),
            leo("So we change the password."),
            maya("We remove it. Container Apps gets a managed identity, a badge Azure gives the app, and Azure answers it with short-lived passes on a private address nobody outside can reach. Then the admin user goes off."),
            leo("And the badge gets Owner, so it never hits a locked door."),
            maya("[pause] And a third case opens. Owner can delete the registry. A role is a named set of permissions, and this host only ever downloads, so it gets AcrPull and nothing else. AcrPush belongs to whatever builds."),
            leo("Noted. Then who does build? A laptop with Docker?"),
            maya("Not any more. ACR Tasks builds inside Azure, on Azure's own machines. A quick task is one command on Azure's command line: az acr build, which sends the folder up, builds it there, and pushes the image in."),
            leo("And how do we stop a stale build going out again?"),
            maya("A multi-step task. A short file of steps: build the image, run the API's tests inside it, push only if they pass. Every push carries the build number as its tag."),
            leo("Is the case closed?"),
            maya("Two more witnesses. Zaiqa opened in a second region last month, and the host there took an age to start, pulling the image across the sea."),
            leo("Easy. A second registry over there with the same name."),
            maya("Can't be done. Registry names are unique across Azure, and two registries are two things to keep in step. The answer is geo-replication: copies of one registry in several regions, under one name, each pull served by the nearest copy."),
            leo("And that comes free?"),
            maya("It is Premium, the top tier, which is just the top price level. Basic and Standard sit in one region. Premium also brings private endpoints, a private address for the registry inside your own network, so pulls skip the public internet."),
            leo("And the last witness?"),
            maya("The security report. Our base image, the ASP dot NET Core runtime, was patched six weeks ago and nobody rebuilt. A triggered task fixes that: give it a base image trigger, and zaiqa-api rebuilds itself the day the base changes."),
            leo("Anything else on those shelves?"),
            maya("Housekeeping. Old versions cost storage, so a purge task on a timer clears images past a chosen age and the untagged leftovers. And the taste service's ready-made Python image came in with az acr import, copied registry to registry inside Azure, with no Docker anywhere."),
            leo("One loose end. When a host cannot pull at all, what do we actually see?"),
            maya("The new release never starts, and the host reports ImagePullBackOff: it tried to download the image, failed, and backed off before trying again. Usually the identity is missing AcrPull, or that tag does not exist."),
            leo("So not a broken Dockerfile."),
            maya("Rarely. Crash loops and start-up checks are the troubleshooting episode, and where the API actually runs is the Container Apps one."),
            leo("So, the case summary for the exam?"),
            maya("A later push quietly changed production: deploy a unique tag or the digest, never latest. An app that must pull with no password: a managed identity holding AcrPull, and the admin user off."),
            leo("And the slow region, and the old hole?"),
            maya("One registry name serving two regions: Premium with geo-replication. Images that must rebuild when the base image is patched: a task with a base image trigger. And no Docker on the machine: az acr build."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Basic registry and check the admin user is off. Build a small image with az acr build and a unique tag. List its tags and its digest. Then push that same tag again, and watch the digest change while the tag stays."),
            leo("The label moves, the fingerprint tells the truth. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-acr",
        listOf(
            maya("Sit down. Did you hear what happened on the Zaiqa team this week? They finally put the Zaiqa API, the dot NET 9 service behind the app, into its own container registry. Five people, five mistakes, one week."),
            leo("[laugh] I heard somebody called it the haunted warehouse. Start at the start. What actually went in there?"),
            maya("A container image of the API: a box with our compiled code, the dot NET runtime and the system libraries, like a tiffin packed the night before. Underneath it a base image, a slim copy of a cut-down Linux."),
            leo("Is the whole operating system in the tiffin? Marcus says it is basically a tiny virtual machine."),
            maya("Marcus is wrong, and Priya nearly dropped her chai. There is no kernel inside, the core that drives the hardware. Every container shares the server's one kernel. A virtual machine carries its own, which is why it is gigabytes."),
            leo("And how do they build the box?"),
            maya("A Dockerfile in two stages. Stage one uses the big dot NET build image with the compiler and runs dotnet publish. Stage two copies only the published output onto the small ASP dot NET Core runtime image. The compiler never ships."),
            leo("Sensible. And the registry itself?"),
            maya("Azure Container Registry, or ACR, is the private warehouse Azure runs for you, and theirs is called zaiqa. A repository is one shelf, all the versions of one image: zaiqa-api. Pushing uploads a version in, pulling downloads it to run."),
            leo("Right. Who went first?"),
            maya("Tom. Monday. Tom pointed production at zaiqa-api latest, and latest is only a tag, a friendly label meaning whatever was pushed last."),
            leo("I can feel this going wrong."),
            maya("Monday evening, the intern pushed a half-finished build as latest. Zaiqa restarted at the dinner rush, pulled latest, and every restaurant's menu came back empty."),
            leo("[chuckle] Empty menus at eight on a Monday. What should Tom have done?"),
            maya("Deployed a unique tag, the build's own number. Or the digest: a long code worked out from every byte of the image, like a fingerprint. Change one byte and it changes. A tag moves; a digest never can."),
            leo("Tuesday?"),
            maya("Dana. A new host could not pull, Dana was in a hurry, so she switched on the admin user: one shared username and password for the whole registry, with push and pull rights. A master key under the doormat."),
            leo("And it worked."),
            maya("It worked, and that is the problem. The password sat in a settings file, and anyone who found it could push. By lunch she had replaced it with a managed identity, a badge Azure gives the app, and switched the admin user off."),
            leo("And people? Do they share that password too?"),
            maya("Not now. They type az acr login. It uses their own Azure sign-in and hands Docker a short-lived pass. Every push has a name on it."),
            leo("Wednesday. Go."),
            maya("Marcus again. Marcus gave the API's badge the Owner role so it would, quote, never hit a locked door. A role is just a named set of permissions, and Owner can delete the registry."),
            leo("And what did it actually need?"),
            maya("AcrPull. Download images, nothing more. The build pipeline gets AcrPush. Push for the builder, pull for the runner."),
            leo("Thursday. I sense money."),
            maya("The intern. Zaiqa opened in a second region, pulls crawled across the sea, and the intern asked for geo-replication on their Standard registry, the middle price level."),
            leo("Geo-replication meaning?"),
            maya("Copies of one registry in several regions, the parts of the world Azure runs in. One name, a copy in each region, every pull served by the nearest. Azure keeps the copies in step."),
            leo("And Standard cannot do that?"),
            maya("Basic and Standard live in one region only. It is Premium, the top tier, which also brings private endpoints, a private address for the registry inside your own network."),
            leo("Friday, then. Tell me Friday was quiet."),
            maya("Friday was Priya, which is why people are still talking. A scan found an old hole in zaiqa-api. The base image, the ASP dot NET Core runtime, had been patched a month before. Priya rebuilt by hand once a month, and forgot."),
            leo("So how does she stop forgetting?"),
            maya("ACR Tasks. It builds inside Azure, on Azure's own machines, so nobody needs Docker on a laptop. A quick task is one command, az acr build, which sends the folder up, builds it there and pushes it in."),
            leo("But someone still has to run it."),
            maya("Which is why she set up a triggered task with a base image trigger. Every patch to the dot NET base rebuilds zaiqa-api by itself. For the record, triggered tasks may not run on free trial accounts."),
            leo("And the empty-menu build from Monday?"),
            maya("A multi-step task would have caught it: build the image, run the API's tests inside it, push only if they pass. She added one by Friday evening. Nobody was surprised."),
            leo("Anything else I should have heard?"),
            maya("Two tidy ones. The taste service's ready-made Python image came in with az acr import, copied registry to registry inside Azure, no Docker. And a purge task on a timer now clears old and untagged images, because storage is not free."),
            leo("And the new release that would not start on Wednesday?"),
            maya("ImagePullBackOff, which just means the host tried to download the image, failed, and backed off before trying again. Nine times in ten it is the missing AcrPull, or a tag that does not exist."),
            leo("So, the gossip I should remember for the exam?"),
            maya("A later push changed production: a unique tag or the digest, not latest. An app that must pull with no password: a managed identity with AcrPull, and the admin user off."),
            leo("And Thursday and Friday?"),
            maya("One registry name in two regions: Premium with geo-replication. Images that must rebuild when the base is patched: a base image trigger. And no Docker on the machine: az acr build."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a Basic registry and check the admin user is off. Build a small image with az acr build and a unique tag. List its tags and its digest. Then push that same tag again, and see the digest change while the tag stays."),
            leo("No half-finished builds at the dinner rush. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-acr",
        listOf(
            nova("Hey, you. It is late, the kettle is on, and it is just the three of us. Me, Ivy, and you, still up and still learning. [pause] Tonight, where the Zaiqa API, the dot NET 9 service behind the app, waits before it runs."),
            ivy("Waiting in the dark before service. Sounds like me at a bus stop. Go on, Nova."),
            nova("Last time we followed one order: the Kotlin app, the API, the order safe in Azure SQL. All of that was code on somebody's machine. [pause] Tonight we pack it into a box, and put the box on a shelf."),
            ivy("A box. Say it plainly for me."),
            nova("A container image: your compiled API, the dot NET runtime it needs and the system libraries, all packed together. Like a tiffin packed the night before, ready to open anywhere."),
            ivy("And the operating system? Is that in the tiffin too?"),
            nova("[pause] Only a slim copy of its files: a base image, a cut-down Linux with its shell and system libraries. Not the kernel, the core that drives the hardware."),
            ivy("Then where is the kernel?"),
            nova("On the server. Every container there shares the server's one kernel, while a virtual machine carries a whole system of its own. So an image is megabytes, and wakes in seconds."),
            ivy("Mm. And who writes the recipe?"),
            nova("You do, in a Dockerfile, and for the Zaiqa API we write it in two stages. The first starts from the big dot NET build image, the one with the compiler, and runs dotnet publish."),
            ivy("And the second?"),
            nova("Starts from the small ASP dot NET Core runtime image, and copies only the published output across. [pause] The compiler stays behind. What goes out is a few hundred megabytes, not well over a gigabyte."),
            ivy("Travelling light. And where does the box sleep until it is wanted?"),
            nova("In a container registry: a private warehouse where images wait between being built and being run. Azure's is Azure Container Registry, ACR, and ours is called zaiqa."),
            ivy("And how do boxes come and go?"),
            nova("Your build uploads an image into the registry: that is a push. A host downloads it to run: that is a pull. Inside, a repository is one shelf, every version of one image. Ours is zaiqa-api."),
            ivy("Shelves. How do you tell one box from another?"),
            nova("By a tag, a friendly label like one point four. [pause] Though a label peels off. Push a new image with the same tag, and the label slides onto the new one."),
            ivy("So the tag called latest is..."),
            nova("Whatever was pushed last. Lovely on a laptop, risky in production, the live app your users touch. Someone pushes a test build at midnight, and Zaiqa quietly serves it at breakfast."),
            ivy("Then how do I hold on to one exact box?"),
            nova("The digest: a long code worked out from every byte of the image, like a fingerprint. Change a byte, change the fingerprint. A tag moves; a digest never does. So deploy a unique tag, or the digest."),
            ivy("And who packs the box? I do not even have Docker on this laptop."),
            nova("You do not need it. ACR Tasks builds inside Azure, on Azure's own machines. A quick task is one command on Azure's command line, the text window where you type instructions: az acr build."),
            ivy("And it does what, exactly?"),
            nova("Sends your folder up, builds the image there from your Dockerfile, streams the log back to you, and pushes the finished image into the registry. [pause] Hotel wifi and no Docker, and it still works."),
            ivy("And if I would rather it happened while I sleep?"),
            nova("A triggered task. It starts by itself: on a commit, a saved change to your code, on a timer, or when the base image is updated."),
            ivy("The base image being our dot NET one."),
            nova("The one our Dockerfile starts from. Microsoft patches it, and a task with a base image trigger rebuilds zaiqa-api on the patched base and pushes it. [pause] Security fixes arriving while you sleep."),
            ivy("Mm. Anything else a task can do?"),
            nova("A multi-step task: build the image, run the API's tests inside it, push only if they pass. And a purge task on a timer, clearing old and untagged images, because the shelves cost money."),
            ivy("And the taste service's ready-made Python image? Do we rebuild that ourselves?"),
            nova("No. One command, az acr import, copies it straight from the public registry into ours, inside Azure, with no Docker and nothing downloaded to your laptop."),
            ivy("Now the shy part. How does Container Apps get in, to pull? A password?"),
            nova("No password. A managed identity: a badge Azure gives your app, and Azure answers it with short-lived passes on a private address nobody outside can reach."),
            ivy("And which doors does the badge open?"),
            nova("Only what its role allows, and a role is a named set of permissions. For a host that downloads, that role is AcrPull. Pull images, nothing more. The build pipeline gets AcrPush."),
            ivy("And the admin user I keep seeing in the portal?"),
            nova("One shared username and password for the whole registry, a master key under the doormat. Off by default. [pause] Leave it off, except for a quick test."),
            ivy("And me, at my keyboard, at this hour?"),
            nova("You type az acr login. It uses your own Azure sign-in and hands Docker a short-lived pass. No secret to whisper to anybody."),
            ivy("And when the role is missing, what do I see at two in the morning?"),
            nova("ImagePullBackOff. The host tried to download the image, failed, and backed off before trying again. [pause] Almost always a badge with no AcrPull, or a tag that is not there."),
            ivy("Does the registry cost the same whatever we do?"),
            nova("Three tiers, three price levels. Basic for learning, Standard for most apps in one region, and Premium for geo-replication and for private endpoints, meaning a private address for the registry inside your own network."),
            ivy("And geo-replication, slowly?"),
            nova("Copies of one registry in several regions, the parts of the world Azure runs in. One name, and each pull served by the nearest copy. [pause] Basic and Standard sit in one region only."),
            ivy("And where does all this stop working?"),
            nova("A unique tag can still be pushed over by mistake, so lock the version, making it read-only, or deploy the digest. [pause] And triggered tasks may not run on free trial accounts."),
            ivy("And my phone? Does the Zaiqa app pull the image?"),
            nova("Never. Its host does. App Service comes first, and then the Container Apps episode, which is where the API actually lives."),
            ivy("[sigh] Mm. Before I fall asleep on you. What do I remember for the exam?"),
            nova("One registry name serving two regions: Premium with geo-replication. Images that must rebuild when the base image is patched: a task with a base image trigger. No Docker on the machine: az acr build."),
            ivy("And the password question?"),
            nova("An app that pulls with no password: a managed identity holding AcrPull, admin user off. And a push that must never change what runs: a unique tag, or the digest."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Basic registry and check the admin user is off. Build a small image with az acr build and a unique tag. List its tags and its digest. [pause] Then push that same tag again, and see the digest change while the tag stays."),
            ivy("The label moves, the fingerprint stays. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-acr",
        listOf(
            nova("Tonight is date night, darling. Candles, something with far too much chilli, and the Zaiqa API, the dot NET service behind the app, finally going into a box."),
            rafe("A box, on date night. You spoil me."),
            nova("[laugh] Pack it properly and it opens anywhere. A container image: our compiled API, the dot NET runtime and the system libraries, all in one overnight bag."),
            rafe("Quiz for you then, love. Is the operating system in the bag?"),
            nova("Half of it. A base image, a slim copy of a cut-down Linux, its shell and its system libraries. No kernel, the core that drives the hardware. Every container shares the host's kernel; a virtual machine packs its own."),
            rafe("Right answer. Travelling light: megabytes, not gigabytes. [kiss]"),
            nova("Mm. And the packing list is a Dockerfile, in two stages. Your turn. Why two?"),
            rafe("Because building and running want different things. Stage one takes the big dot NET build image with the compiler and runs dotnet publish. Stage two takes the small ASP dot NET Core runtime image and copies only the published output."),
            nova("Right answer, and the compiler never leaves home. [kiss]"),
            rafe("A few hundred megabytes instead of well over a gigabyte. Now, where does the bag wait?"),
            nova("In a container registry, a private warehouse for images. Azure Container Registry, or ACR, is the one Azure runs for you, and ours is called zaiqa. Uploading a version into it is a push; a host downloading it to run is a pull."),
            rafe("Push in, pull out. You do know how to talk to me. And inside?"),
            nova("Shelves. A repository is one shelf, every version of one image: zaiqa-api. And each version can wear a tag, a friendly label, like one point four."),
            rafe("Then let me test you, exam style. Production runs zaiqa-api latest. Someone pushes a test build as latest, and after a restart the live app serves it. Why?"),
            nova("Because latest is only a tag, and a tag moves. A new push with the same tag slides the label onto the new image, so the restart pulled the test build."),
            rafe("Right answer. A label is easy to move; a heart, not so much. [kiss]"),
            nova("Smooth. So what do we deploy instead?"),
            rafe("A unique tag for every build. Or the digest, a long code worked out from every byte of the image, like a fingerprint. Change one byte, and it changes."),
            nova("So you may tag me however you like, darling, but my digest never moves."),
            rafe("[chuckle] I would know you anywhere. My turn to be quizzed, then."),
            nova("Zaiqa serves two regions, and both must pull locally from one registry name. What do we need?"),
            rafe("Geo-replication: copies of one registry in several regions, one name, each pull served by the nearest copy. And that needs Premium, the top tier."),
            nova("Right answer. Only the best for you. [kiss]"),
            rafe("Premium also brings private endpoints, a private address for the registry inside our own network. Basic and Standard stay in one region, like you on a Sunday."),
            nova("Cheeky. Now, who packs the bags? Do we need Docker on the laptop?"),
            rafe("We do not. ACR Tasks builds inside Azure, on Azure's own machines. A quick task is one command on Azure's command line: az acr build, which sends the folder up, builds it there, and pushes it in."),
            nova("And when the base image, the ASP dot NET Core one ours is built on, gets a security patch? What rebuilds zaiqa-api with nobody remembering?"),
            rafe("A triggered task with a base image trigger. The same kind can start on a commit, a saved change to our code, or on a timer."),
            nova("Right answer, and thorough with it. [kiss]"),
            rafe("One more for free: a multi-step task builds the image, runs the API's tests inside it, and pushes only if they pass."),
            nova("Building, testing, and only then going out. Very sensible. And the housekeeping?"),
            rafe("A purge task on a timer clears old and untagged images, because shelf space is not free. And the taste service's ready-made Python image came in with az acr import, copied registry to registry inside Azure."),
            nova("No Docker, nothing downloaded. Now the intimate part. How does Container Apps get into our warehouse? I like to know who holds a key."),
            rafe("No key at all. A managed identity, a badge Azure gives the app, and Azure answers it with short-lived passes on a private address nobody outside can reach."),
            nova("And the badge carries a role, a named set of permissions. Quiz: our host only ever downloads images. Which role?"),
            rafe("Owner, surely. Then it never hits a locked door."),
            nova("[sigh] No kiss for that one. Owner can delete the whole registry. It gets AcrPull: pull images, and nothing more. AcrPush is for whatever builds. It is all about knowing your role."),
            rafe("Knowing my role. Point taken, darling. And the admin user?"),
            nova("One shared username and password for the whole registry, with push and pull rights. A spare key under the doormat. Off by default; keep it off, except for a quick test."),
            rafe("And me, at my keyboard? How do I get in?"),
            nova("You type az acr login. It uses your own Azure sign-in and hands Docker a short-lived pass. Your own way in, no shared secrets."),
            rafe("Last quiz for you. A new Zaiqa release will not start, and the host keeps saying ImagePullBackOff. What is that?"),
            nova("It tried to download the image, failed, and backed off before trying again. Almost always the identity is missing AcrPull, or the tag does not exist."),
            rafe("Right answer. You always know when I cannot get in. [kiss]"),
            nova("Mm. And where does all this stop? Every romance has its limits."),
            rafe("A unique tag can still be pushed over, so lock that version, making it read-only, or deploy the digest. And triggered tasks may not run on a free trial account."),
            nova("And the phone? Does the Zaiqa app pull the bag?"),
            rafe("Never. Its host does, and that is the Container Apps episode. Now, the exam. What do we remember?"),
            nova("A later push changed production: a unique tag or the digest, not latest. An app that must pull with no password: a managed identity holding AcrPull, and the admin user off."),
            rafe("That is two. Keep going, love."),
            nova("One registry name in two regions: Premium with geo-replication. Images that must rebuild when the base is patched: a base image trigger. And no Docker on the machine: az acr build."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, my love?"),
            rafe("Create a Basic registry and check the admin user is off. Build a small image with az acr build and a unique tag. List its tags and its digest. Then push that same tag again, and see the digest change while the tag stays."),
            nova("Labels come and go. Fingerprints stay. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "The Zaiqa API image is built from source, pushed into the registry, and pulled by the hosts that run it.",
        nodes = listOf(
            DNode("src", "Source code", 0.2f, 0.15f, w = 0.34f, h = 0.18f, kind = NodeKind.NOTE, sub = "the API + recipe"),
            DNode("task", "ACR Tasks build", 0.75f, 0.15f, w = 0.4f, h = 0.18f, tone = 1, sub = "inside Azure"),
            DNode("acr", "Registry", 0.5f, 0.5f, w = 0.44f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "zaiqa-api images"),
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

    private fun stages() = DiagramSpec(
        "A two-stage Dockerfile: build with the dot NET build image, then ship only the published output on the runtime image.",
        nodes = listOf(
            DNode("build", "Stage one: build", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "has the compiler"),
            DNode("publish", "dotnet publish", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the output only"),
            DNode("runtime", "Stage two: runtime", 0.25f, 0.55f, w = 0.42f, h = 0.2f, tone = 1, sub = "ASP dot NET Core"),
            DNode("image", "Final image", 0.6f, 0.85f, w = 0.5f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "small, no compiler"),
        ),
        edges = listOf(
            DEdge("build", "publish", "builds"),
            DEdge("publish", "runtime", "copied in"),
            DEdge("runtime", "image", "what ships"),
        ),
        aspect = 0.8f,
    )

    private fun names() = DiagramSpec(
        "A repository holds one image's versions; a tag is a movable label, a digest the content's fixed fingerprint.",
        nodes = listOf(
            DNode("repo", "Repository: zaiqa-api", 0.5f, 0.14f, w = 0.8f, h = 0.2f, sub = "every version of one image"),
            DNode("tag", "Tag", 0.25f, 0.5f, w = 0.42f, h = 0.22f, sub = "movable label: 1.4"),
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
            DNode("std", "Standard", 0.5f, 0.25f, w = 0.3f, h = 0.26f, sub = "one region"),
            DNode("prem", "Premium", 0.83f, 0.25f, w = 0.3f, h = 0.26f, tone = 1, sub = "geo-replication"),
            DNode("west", "Home region copy", 0.25f, 0.8f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "nearest pull"),
            DNode("east", "Second region copy", 0.75f, 0.8f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "nearest pull"),
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
            DNode("base", "Base image patched", 0.75f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "ASP dot NET Core"),
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

    private fun housekeeping() = DiagramSpec(
        "az acr import copies an image in registry to registry; a purge task on a timer clears old and untagged images.",
        nodes = listOf(
            DNode("hub", "Public registry", 0.2f, 0.18f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "ready-made image"),
            DNode("import", "az acr import", 0.65f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "copied in Azure"),
            DNode("acr", "Registry", 0.5f, 0.52f, w = 0.44f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "zaiqa"),
            DNode("purge", "Purge task", 0.25f, 0.85f, w = 0.42f, h = 0.2f, sub = "on a timer"),
            DNode("old", "Old untagged images", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "storage you pay for"),
        ),
        edges = listOf(
            DEdge("hub", "import", "copy"),
            DEdge("import", "acr", "into ours"),
            DEdge("purge", "acr", "deletes old"),
            DEdge("old", "purge", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun access() = DiagramSpec(
        "People sign in with az acr login; apps pull with a managed identity holding AcrPull; the admin user stays off.",
        nodes = listOf(
            DNode("you", "You", 0.12f, 0.18f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("login", "az acr login", 0.5f, 0.18f, w = 0.4f, h = 0.2f, sub = "your Azure sign-in"),
            DNode("acr", "Registry", 0.5f, 0.52f, w = 0.4f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "zaiqa"),
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
            DNode("lock", "Lock the version", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "or deploy the digest"),
            DNode("phone", "The phone never pulls", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "its host pulls instead"),
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
            DNode("backoff", "ImagePullBackOff", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "identity lacks AcrPull"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("regions", "base", "nodocker", "nopass", "latest", "backoff")
    private val today = BoardCard("Today", "A Basic registry, admin off; az acr build; tags and digest; push the tag again.")
    private fun quickCode() = BoardCard("Quick task", "az acr build --registry zaiqa --image zaiqa-api:482 .", code = true)

    private fun classicBoard() = PodcastBoard(
        "ai-acr",
        listOf(
            // 0: "Hi, and welcome back. Last time we walked one order through Zaiqa..."
            Scene(
                0, "Packing the Zaiqa API", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src"), light = setOf("src"), card = BoardCard("Where the API waits", "Last time: one order through Zaiqa. Today the API goes in a box.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Today, by hand", "Someone installs the dot NET runtime and copies the build across.")),
                    Beat(4, light = setOf("src"), card = BoardCard("Container image", "The compiled API, its runtime and libraries in one box: a tiffin.")),
                    Beat(6, light = setOf("src"), card = BoardCard("Base image", "A slim copy of an operating system's files, a cut-down Linux.")),
                    Beat(8, light = setOf("src"), card = BoardCard("No kernel inside", "Every container shares the server's kernel. A VM brings its own.")),
                    Beat(10, light = setOf("src"), card = BoardCard("Dockerfile", "FROM a base image, copy things in, set the start command.")),
                ),
            ),
            // 11: "Two stages? Why would one box need two?"
            Scene(
                11, "Two stages, one image", stages(),
                beats = listOf(
                    Beat(12, show = setOf("build", "publish"), light = setOf("build"), pulse = "build" to "publish", card = BoardCard("Stage one: build", "The big dot NET build image, with the compiler: dotnet publish.")),
                    Beat(14, show = setOf("runtime", "image"), light = setOf("runtime", "image"), pulse = "runtime" to "image", card = BoardCard("Stage two: runtime", "The small ASP dot NET Core image, with only the published output.")),
                ),
            ),
            // 15: "Good. Now where does the box live, once it is built?"
            Scene(
                15, "Shelves, tags, digests", names(),
                beats = listOf(
                    Beat(16, card = BoardCard("Container registry", "A private warehouse for images: push to upload, pull to download.")),
                    Beat(18, card = BoardCard("ACR: zaiqa", "Azure Container Registry, run by Azure: a private Docker Hub.")),
                    Beat(20, show = setOf("repo"), light = setOf("repo"), card = BoardCard("Repository", "One shelf: every version of one image, such as zaiqa-api.")),
                    Beat(22, show = setOf("tag", "image"), light = setOf("tag"), pulse = "tag" to "image", card = BoardCard("Tag", "A movable label. A new push with the same tag moves it.")),
                    Beat(24, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "A fingerprint worked out from the content. It never moves.")),
                    Beat(26, light = setOf("tag"), card = BoardCard("latest", "Just a tag: whatever was pushed last. Risky in production.")),
                    Beat(28, light = setOf("tag", "digest"), card = BoardCard("What to deploy", "A unique tag for every build, or the digest itself.")),
                ),
            ),
            // 29: "Fine. Who builds the image? Do I need Docker on my laptop?"
            Scene(
                29, "Build in Azure", tasks(),
                beats = listOf(
                    Beat(30, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = BoardCard("ACR Tasks", "Builds on Azure's own machines. No Docker on your laptop.")),
                    Beat(32, light = setOf("quick"), pulse = "quick" to "acr", card = quickCode()),
                    Beat(34, show = setOf("trig"), light = setOf("trig"), pulse = "trig" to "acr", card = BoardCard("Triggered task", "Starts by itself: a commit, a timer, or a base image update.")),
                    Beat(36, show = setOf("base"), light = setOf("base", "trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "Microsoft patches the dot NET base, and zaiqa-api rebuilds itself.")),
                    Beat(38, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the API's tests inside it, push only if they pass.")),
                ),
            ),
            // 39: "Good. Now the registry itself. Does it cost the same whatever we do?"
            Scene(
                39, "Three tiers", tiers(),
                beats = listOf(
                    Beat(40, show = setOf("basic", "std", "prem"), light = setOf("prem"), card = BoardCard("Tiers", "Basic to learn, Standard for one region, Premium for many.")),
                    Beat(42, show = setOf("west", "east"), light = setOf("west", "east"), pulse = "prem" to "west", card = BoardCard("Geo-replication", "One registry name, a copy in each region; pulls go to the nearest.")),
                    Beat(44, light = setOf("prem"), pulse = "prem" to "east", card = BoardCard("Premium only", "Basic and Standard sit in one region.")),
                    Beat(46, light = setOf("prem"), card = BoardCard("Private endpoint", "A private address for the registry inside your own network.")),
                ),
            ),
            // 47: "Now a different question. The taste service starts from a ready-made..."
            Scene(
                47, "Housekeeping", housekeeping(),
                beats = listOf(
                    Beat(48, show = setOf("hub", "import", "acr"), light = setOf("import"), pulse = "hub" to "import", card = BoardCard("az acr import", "Copies an image registry to registry inside Azure. No Docker.")),
                    Beat(50, show = setOf("purge", "old"), light = setOf("purge"), pulse = "purge" to "acr", card = BoardCard("Purge task", "On a timer: old images and untagged leftovers cost storage.")),
                ),
            ),
            // 51: "Right. Last piece. The API runs on Container Apps, and that host..."
            Scene(
                51, "Pulling without a password", access(),
                beats = listOf(
                    Beat(51, show = setOf("acr")),
                    Beat(52, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "A badge Azure gives your app: no password stored anywhere.")),
                    Beat(54, show = setOf("role"), light = setOf("role", "mi"), pulse = "role" to "mi", card = BoardCard("AcrPull", "Download images and nothing more. AcrPush is for the builder.")),
                    Beat(56, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "Your own Azure sign-in, and a short-lived pass for Docker.")),
                    Beat(58, show = setOf("admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "One shared password for the whole registry. Keep it off.")),
                    Beat(60, light = setOf("mi", "role"), card = BoardCard("ImagePullBackOff", "The host could not download the image. Usually a missing AcrPull.")),
                ),
            ),
            // 63: "So what do I remember for the exam?"
            Scene(
                63, "For the exam", exam(),
                beats = listOf(
                    Beat(64, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Premium, a base trigger, az acr build, AcrPull, a unique tag.")),
                    Beat(66, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-acr--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Night Zaiqa Sold Dinner..."
            Scene(
                0, "The night of the old menu",
                DiagramSpec(
                    "Zaiqa's host pulled zaiqa-api latest after a restart, and latest now pointed at a fortnight-old build.",
                    nodes = listOf(
                        DNode("phone", "Zaiqa app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Kotlin"),
                        DNode("old", "Old build", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "last month's prices"),
                        DNode("api", "Zaiqa API", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "runs latest"),
                        DNode("acr", "Registry", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "zaiqa-api"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "calls"),
                        DEdge("api", "acr", "pull latest"),
                        DEdge("acr", "old", "latest moved", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "old"), light = setOf("old"), card = BoardCard("The case", "Friday, nine o'clock: a fortnight-old menu. Nobody deployed.")),
                    Beat(2, show = setOf("api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Container image", "The compiled API, its runtime and libraries in one box: a tiffin.")),
                    Beat(4, light = setOf("api"), card = BoardCard("No kernel inside", "Containers share the server's kernel; a VM brings its own.")),
                    Beat(6, light = setOf("api"), card = BoardCard("Two-stage Dockerfile", "Build with the dot NET build image; ship on the runtime one.")),
                    Beat(8, show = setOf("acr"), light = setOf("acr"), pulse = "api" to "acr", card = BoardCard("Container registry", "A private warehouse: push to upload, pull to download.")),
                    Beat(12, light = setOf("api"), card = BoardCard("Clue one", "Container Apps was told to run zaiqa-api latest.")),
                    Beat(16, light = setOf("acr", "old"), pulse = "acr" to "old", card = BoardCard("latest moved", "An old build was pushed as latest; the restart pulled it.")),
                ),
            ),
            // 17: "The label wandered off. What stops that?"
            Scene(
                17, "Tags and digests", names(),
                beats = listOf(
                    Beat(18, show = setOf("repo", "tag", "digest", "image"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Unique tag or digest", "The content's fingerprint. A tag can move; a digest never can.")),
                ),
            ),
            // 19: "Right. Suspect two. Somebody broke into the registry."
            Scene(
                19, "Suspect two", access(),
                beats = listOf(
                    Beat(20, show = setOf("acr", "admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "One shared password with push and pull, left in a settings file.")),
                    Beat(22, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "Azure hands the app short-lived passes. No password at all.")),
                    Beat(24, show = setOf("role"), light = setOf("role"), pulse = "role" to "mi", card = BoardCard("AcrPull, not Owner", "A role is a set of permissions. Pull images, nothing more.")),
                ),
            ),
            // 25: "Noted. Then who does build? A laptop with Docker?"
            Scene(
                25, "Who builds it", tasks(),
                beats = listOf(
                    Beat(26, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode()),
                    Beat(28, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests inside the image, push only if they pass.")),
                ),
            ),
            // 29: "Is the case closed?"
            Scene(
                29, "The far-away region", tiers(),
                beats = listOf(
                    Beat(31, show = setOf("basic", "std"), light = setOf("basic"), card = BoardCard("Two registries?", "Registry names are unique across Azure, and two would drift apart.")),
                    Beat(32, show = setOf("prem", "west", "east"), light = setOf("prem", "east"), pulse = "prem" to "east", card = BoardCard("Geo-replication", "One name, a copy in each region, the nearest pull. Premium only.")),
                    Beat(34, light = setOf("prem"), card = BoardCard("Premium also", "Private endpoints: a private address inside your own network.")),
                ),
            ),
            // 35: "And the last witness?"
            Scene(
                35, "The old hole", tasks(),
                beats = listOf(
                    Beat(36, show = setOf("quick", "trig", "multi", "base", "acr"), light = setOf("base", "trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "The dot NET base is patched, and a task rebuilds zaiqa-api.")),
                ),
            ),
            // 37: "Anything else on those shelves?"
            Scene(
                37, "Housekeeping", housekeeping(),
                beats = listOf(
                    Beat(38, show = setOf("hub", "import", "acr", "purge", "old"), light = setOf("import", "purge"), pulse = "hub" to "import", card = BoardCard("Import and purge", "Copy an image in; clear old and untagged ones on a timer.")),
                ),
            ),
            // 39: "One loose end. When a host cannot pull at all, what do we see?"
            Scene(
                39, "Why it will not start", access(),
                beats = listOf(
                    Beat(40, show = setOf("acr", "mi", "role"), light = setOf("mi", "role"), card = BoardCard("ImagePullBackOff", "It could not download the image. Usually the badge lacks AcrPull.")),
                    Beat(42, light = setOf("role"), card = BoardCard("Not the Dockerfile", "Crash loops and start-up checks are the troubleshooting episode.")),
                ),
            ),
            // 43: "So, the case summary for the exam?"
            Scene(
                43, "Case closed", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("latest", "nopass", "regions", "base"), card = BoardCard("Listen for the phrase", "A unique tag, AcrPull, Premium, a base trigger, az acr build.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-acr--gossip",
        listOf(
            // 0: "Sit down. Did you hear what happened on the Zaiqa team this week?"
            Scene(
                0, "The Zaiqa team's week", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src", "task"), light = setOf("src"), pulse = "src" to "task", card = BoardCard("The week", "The Zaiqa API moved into its own container registry.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Container image", "The compiled API, its runtime and libraries in one box: a tiffin.")),
                    Beat(4, light = setOf("src"), card = BoardCard("Not a tiny VM", "A slim base of OS files, no kernel: containers share the host's.")),
                    Beat(6, light = setOf("src"), card = BoardCard("Two-stage Dockerfile", "Build with the compiler; ship only the published output.")),
                    Beat(8, show = setOf("acr", "app", "aca"), light = setOf("acr"), pulse = "task" to "acr", card = BoardCard("ACR: zaiqa", "A private warehouse. Push to upload, pull to download.")),
                ),
            ),
            // 9: "Right. Who went first?"
            Scene(
                9, "Monday: Tom", names(),
                beats = listOf(
                    Beat(10, show = setOf("repo", "tag", "image"), light = setOf("tag"), pulse = "tag" to "image", card = BoardCard("latest", "A movable tag that means whatever was pushed last.")),
                    Beat(12, light = setOf("tag"), card = BoardCard("Empty menus", "A half-finished build went up as latest; a restart pulled it.")),
                    Beat(14, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Unique tag or digest", "A digest is the content's fingerprint. It never moves.")),
                ),
            ),
            // 15: "Tuesday?"
            Scene(
                15, "Tuesday: Dana", access(),
                beats = listOf(
                    Beat(16, show = setOf("acr", "admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "One shared password, push and pull, for the whole registry.")),
                    Beat(18, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "Short-lived passes on a private address. Nothing to leak.")),
                    Beat(20, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "People sign in as themselves; Docker gets a short-lived pass.")),
                ),
            ),
            // 21: "Wednesday. Go."
            Scene(
                21, "Wednesday: Marcus", access(),
                beats = listOf(
                    Beat(22, show = setOf("acr", "mi"), light = setOf("mi"), card = BoardCard("Owner", "Can do anything at all, even delete the registry.")),
                    Beat(24, show = setOf("role"), light = setOf("role"), pulse = "role" to "mi", card = BoardCard("AcrPull", "Pull images, nothing more. AcrPush is for whatever builds.")),
                ),
            ),
            // 25: "Thursday. I sense money."
            Scene(
                25, "Thursday: the intern", tiers(),
                beats = listOf(
                    Beat(26, show = setOf("basic", "std", "prem"), light = setOf("std"), card = BoardCard("Standard", "One region only. The second region pulled across the sea.")),
                    Beat(28, show = setOf("west", "east"), light = setOf("prem", "east"), pulse = "prem" to "east", card = BoardCard("Geo-replication", "One name, a copy in each region, the nearest pull. Premium only.")),
                    Beat(30, light = setOf("prem"), card = BoardCard("Premium also", "Private endpoints: a private address inside your own network.")),
                ),
            ),
            // 31: "Friday, then. Tell me Friday was quiet."
            Scene(
                31, "Friday: Priya", tasks(),
                beats = listOf(
                    Beat(32, show = setOf("base", "acr"), light = setOf("base"), card = BoardCard("An old hole", "The dot NET base was patched a month ago, and nobody rebuilt.")),
                    Beat(34, show = setOf("quick"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode()),
                    Beat(36, show = setOf("trig"), light = setOf("trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "Every patch to the base rebuilds zaiqa-api by itself.")),
                    Beat(38, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests, and push only if they pass.")),
                ),
            ),
            // 39: "Anything else I should have heard?"
            Scene(
                39, "Housekeeping", housekeeping(),
                beats = listOf(
                    Beat(40, show = setOf("hub", "import", "acr", "purge", "old"), light = setOf("import", "purge"), pulse = "hub" to "import", card = BoardCard("Import and purge", "Copy an image in; clear old and untagged ones on a timer.")),
                ),
            ),
            // 41: "And the new release that would not start on Wednesday?"
            Scene(
                41, "The release that would not start", access(),
                beats = listOf(
                    Beat(42, show = setOf("acr", "mi", "role"), light = setOf("mi", "role"), card = BoardCard("ImagePullBackOff", "It could not download the image. Usually a missing AcrPull.")),
                ),
            ),
            // 43: "So, the gossip I should remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("latest", "nopass", "regions", "base"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-acr--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kettle is on..."
            Scene(
                0, "Where the API waits", flow(),
                beats = listOf(
                    Beat(0, show = setOf("src"), card = BoardCard("Tonight", "Where the Zaiqa API waits before it runs.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Last time", "One order through Zaiqa: the app, the API, the order safe in SQL.")),
                    Beat(4, light = setOf("src"), card = BoardCard("Container image", "Your compiled API, its runtime and libraries: a tiffin, packed.")),
                    Beat(6, light = setOf("src"), card = BoardCard("Base image", "A slim copy of an operating system's files. Not the kernel.")),
                    Beat(8, light = setOf("src"), card = BoardCard("The shared kernel", "Every container shares the host's kernel; a VM carries its own.")),
                ),
            ),
            // 9: "Mm. And who writes the recipe?"
            Scene(
                9, "Two stages", stages(),
                beats = listOf(
                    Beat(10, show = setOf("build", "publish"), light = setOf("build"), pulse = "build" to "publish", card = BoardCard("Stage one: build", "The big dot NET build image, the one with the compiler.")),
                    Beat(12, show = setOf("runtime", "image"), light = setOf("runtime", "image"), pulse = "runtime" to "image", card = BoardCard("Stage two: runtime", "Only the published output ships. The compiler stays behind.")),
                ),
            ),
            // 13: "Travelling light. And where does the box sleep until it is wanted?"
            Scene(
                13, "Shelves and labels", names(),
                beats = listOf(
                    Beat(14, card = BoardCard("Container registry", "A private warehouse where images wait to be run. ACR: zaiqa.")),
                    Beat(16, show = setOf("repo"), light = setOf("repo"), card = BoardCard("Repository", "One shelf: every version of one image, zaiqa-api.")),
                    Beat(18, show = setOf("tag", "image"), light = setOf("tag"), pulse = "tag" to "image", card = BoardCard("Tag", "A friendly label on one version, and labels peel off.")),
                    Beat(20, light = setOf("tag"), card = BoardCard("latest", "A tag that slides to whatever was pushed last.")),
                    Beat(22, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "The content's fingerprint. It never moves.")),
                ),
            ),
            // 23: "And who packs the box? I do not even have Docker on this laptop."
            Scene(
                23, "Built while you sleep", tasks(),
                beats = listOf(
                    Beat(24, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode()),
                    Beat(26, light = setOf("quick"), pulse = "quick" to "acr", card = BoardCard("What it does", "Sends your folder up, builds it in Azure, pushes the image in.")),
                    Beat(28, show = setOf("trig"), light = setOf("trig"), pulse = "trig" to "acr", card = BoardCard("Triggered task", "A commit, a timer, or a base image update starts it.")),
                    Beat(30, show = setOf("base"), light = setOf("base", "trig"), pulse = "base" to "trig", card = BoardCard("Base image trigger", "Microsoft patches the base, and zaiqa-api rebuilds itself.")),
                    Beat(32, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step, and purge", "Push only if the tests pass; a timer clears old images.")),
                ),
            ),
            // 33: "And the taste service's ready-made Python image?"
            Scene(
                33, "Copying one in", housekeeping(),
                beats = listOf(
                    Beat(34, show = setOf("hub", "import", "acr"), light = setOf("import"), pulse = "hub" to "import", card = BoardCard("az acr import", "Copies an image registry to registry inside Azure. No Docker.")),
                ),
            ),
            // 35: "Now the shy part. How does Container Apps get in, to pull?"
            Scene(
                35, "Getting in", access(),
                beats = listOf(
                    Beat(36, show = setOf("acr", "mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "A badge Azure gives your app: short-lived passes, no password.")),
                    Beat(38, show = setOf("role"), light = setOf("role", "mi"), pulse = "role" to "mi", card = BoardCard("AcrPull", "Download images and nothing more. AcrPush is for the builder.")),
                    Beat(40, show = setOf("admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "A master key under the doormat. Leave it off.")),
                    Beat(42, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "Your own Azure sign-in, and a short-lived pass for Docker.")),
                    Beat(44, light = setOf("mi", "role"), card = BoardCard("ImagePullBackOff", "It could not download the image. Usually a badge with no AcrPull.")),
                ),
            ),
            // 45: "Does the registry cost the same whatever we do?"
            Scene(
                45, "Three tiers", tiers(),
                beats = listOf(
                    Beat(46, show = setOf("basic", "std", "prem"), light = setOf("prem"), card = BoardCard("Tiers", "Basic to learn, Standard for one region, Premium for many.")),
                    Beat(48, show = setOf("west", "east"), light = setOf("west", "east"), pulse = "prem" to "west", card = BoardCard("Geo-replication", "One name, a copy near each host, and private endpoints too.")),
                ),
            ),
            // 49: "And where does all this stop working?"
            Scene(
                49, "Where it stops", limits(),
                beats = listOf(
                    Beat(50, show = setOf("lock", "phone", "trial", "next"), light = setOf("lock", "trial"), card = BoardCard("Where it stops", "Lock a version, or deploy the digest. Trials may not run tasks.")),
                    Beat(52, light = setOf("phone", "next"), pulse = "phone" to "next", card = BoardCard("Your next late night", "App Service first, then Container Apps: the hosts that pull.")),
                ),
            ),
            // 53: "Mm. Before I fall asleep on you. What do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("regions", "base", "nodocker"), card = BoardCard("Listen for the phrase", "Premium, a base image trigger, az acr build.")),
                    Beat(56, light = setOf("nopass", "latest"), card = BoardCard("And the password one", "A managed identity with AcrPull; a unique tag or the digest.")),
                    Beat(58, card = today),
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
                    Beat(0, show = setOf("src"), card = BoardCard("Date night", "The Zaiqa API goes into a box tonight.")),
                    Beat(2, light = setOf("src"), card = BoardCard("Container image", "An overnight bag: the compiled API, its runtime and libraries.")),
                    Beat(4, light = setOf("src"), card = BoardCard("Quiz: the OS in the bag?", "A slim base of OS files, no kernel. Containers share the host's.")),
                ),
            ),
            // 6: "Mm. And the packing list is a Dockerfile, in two stages."
            Scene(
                6, "Two stages", stages(),
                beats = listOf(
                    Beat(6, show = setOf("build", "publish"), light = setOf("build"), pulse = "build" to "publish", card = BoardCard("Quiz: why two stages?", "Building and running want different things. Why two?")),
                    Beat(7, show = setOf("runtime", "image"), light = setOf("runtime", "image"), pulse = "runtime" to "image", card = BoardCard("Right answer", "Build with the compiler; ship only the published output.")),
                ),
            ),
            // 9: "A few hundred megabytes. Now, where does the bag wait?"
            Scene(
                9, "Labels and fingerprints", names(),
                beats = listOf(
                    Beat(10, card = BoardCard("Container registry", "ACR: a private warehouse. Push to upload, pull to download.")),
                    Beat(12, show = setOf("repo", "tag", "image"), light = setOf("repo", "tag"), pulse = "tag" to "image", card = BoardCard("Repository and tag", "One shelf per image; a label on each version.")),
                    Beat(13, light = setOf("tag"), card = BoardCard("Quiz: latest in production", "A test build pushed as latest reaches the live app. Why?")),
                    Beat(14, light = setOf("tag"), card = BoardCard("Right answer", "A tag moves: a new push slides the label onto the new image.")),
                    Beat(17, show = setOf("digest"), light = setOf("digest"), pulse = "digest" to "image", card = BoardCard("Digest", "A fingerprint of the content. Change one byte, and it changes.")),
                ),
            ),
            // 19: "I would know you anywhere. My turn to be quizzed, then."
            Scene(
                19, "A copy in every city", tiers(),
                beats = listOf(
                    Beat(20, show = setOf("basic", "std", "prem"), light = setOf("prem"), card = BoardCard("Quiz: two regions", "One registry name, local pulls in two regions. What do we need?")),
                    Beat(21, show = setOf("west", "east"), light = setOf("west", "east"), pulse = "prem" to "west", card = BoardCard("Geo-replication", "A copy in each region, the nearest pull. Premium only.")),
                    Beat(23, light = setOf("prem"), card = BoardCard("Premium also", "Private endpoints: a private address inside our own network.")),
                ),
            ),
            // 24: "Cheeky. Now, who packs the bags? Do we need Docker on the laptop?"
            Scene(
                24, "Who packs the bags", tasks(),
                beats = listOf(
                    Beat(25, show = setOf("quick", "acr"), light = setOf("quick"), pulse = "quick" to "acr", card = quickCode()),
                    Beat(26, show = setOf("base", "trig"), light = setOf("base"), pulse = "base" to "trig", card = BoardCard("Quiz: the base is patched", "What rebuilds zaiqa-api when the dot NET base gets a fix?")),
                    Beat(27, light = setOf("trig"), pulse = "trig" to "acr", card = BoardCard("Right answer", "A triggered task with a base image trigger.")),
                    Beat(29, show = setOf("multi"), light = setOf("multi"), pulse = "multi" to "acr", card = BoardCard("Multi-step task", "Build, run the tests inside the image, push only if they pass.")),
                ),
            ),
            // 30: "Building, testing, and only then going out. And the housekeeping?"
            Scene(
                30, "Housekeeping", housekeeping(),
                beats = listOf(
                    Beat(31, show = setOf("hub", "import", "acr", "purge", "old"), light = setOf("purge", "import"), pulse = "hub" to "import", card = BoardCard("Import and purge", "Copy an image in; clear old and untagged ones on a timer.")),
                ),
            ),
            // 32: "Now the intimate part. How does Container Apps get into our warehouse?"
            Scene(
                32, "Knowing your role", access(),
                beats = listOf(
                    Beat(33, show = setOf("acr", "mi"), light = setOf("mi"), pulse = "mi" to "acr", card = BoardCard("Managed identity", "A badge from Azure: short-lived passes, no key.")),
                    Beat(34, light = setOf("mi"), card = BoardCard("Quiz: it only downloads", "The host only pulls images. Which role?")),
                    Beat(36, show = setOf("role"), light = setOf("role"), pulse = "role" to "mi", card = BoardCard("No kiss: it is AcrPull", "Owner can delete the registry. AcrPull pulls, nothing more.")),
                    Beat(38, show = setOf("admin"), light = setOf("admin"), pulse = "admin" to "acr", card = BoardCard("Admin user", "A spare key under the doormat. Off by default; keep it off.")),
                    Beat(40, show = setOf("you", "login"), light = setOf("login"), pulse = "you" to "login", card = BoardCard("az acr login", "Your own Azure sign-in, and a short pass for Docker.")),
                    Beat(42, light = setOf("mi", "role"), card = BoardCard("ImagePullBackOff", "It could not download the image. Usually a missing AcrPull.")),
                ),
            ),
            // 44: "And where does all this stop? Every romance has its limits."
            Scene(
                44, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(45, show = setOf("lock", "phone", "trial", "next"), light = setOf("lock", "trial"), card = BoardCard("Where it stops", "Lock a version, or deploy the digest. Trials may not run tasks.")),
                    Beat(47, light = setOf("phone", "next"), pulse = "phone" to "next", card = BoardCard("The phone never pulls", "Its host does, and that is the Container Apps episode.")),
                ),
            ),
            // 48: "A later push changed production: a unique tag or the digest."
            Scene(
                48, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("latest", "nopass"), card = BoardCard("Listen for the phrase", "A unique tag or the digest; a managed identity with AcrPull.")),
                    Beat(50, light = setOf("regions", "base", "nodocker"), card = BoardCard("And three more", "Premium with geo-replication, a base trigger, az acr build.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
