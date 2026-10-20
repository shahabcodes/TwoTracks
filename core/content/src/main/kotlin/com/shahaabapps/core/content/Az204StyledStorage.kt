package com.shahaabapps.core.content

/** The Storage module's Blob Storage topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledStorage {
    private val blobBasicsCase = Podcast(
        "az-blob-basics",
        listOf(
            maya("Welcome to Case Files. Today's case: The Receipt That Would Not Open. A small bike rental company rents city bikes through an Android app. Its backend runs on Azure App Service, and every ride receipt is saved as a file in Blob Storage. A customer disputes a ride from two years ago. Support clicks download on the receipt. And the download fails."),
            leo("[chuckle] A cold case, two years cold. Detective Leo reporting. First, what is Blob Storage, in plain words?"),
            maya("A place in the cloud to keep files of any size. Photos, backups, logs, receipts. Blob just means a file. You pay for what you store, and for how often you touch it. It's organised in three layers, like a building with rooms and boxes."),
            leo("Walk me through the building."),
            maya("The storage account is the building. A container is a room inside it, a folder-like group of blobs with its own access level, like private. The blob is the box, the file itself. The bike company has one account, with a container called receipts and another called ride logs."),
            leo("And under the picture? What is a blob, really?"),
            maya("A name and a pile of bytes, with its own web address, reached over HTTPS. The account name becomes part of that address, which is why it must be unique across Azure. And a container isn't a real folder. A slash in a blob's name only looks like a folder. Underneath, it's one flat list of names."),
            leo("Fine. My first suspect. Someone deleted the receipt."),
            maya("[pause] Good instinct, wrong suspect. Support can still see it in the list. Same name, same size, two years old. A deleted blob wouldn't be listed. The receipt is still there."),
            leo("Then suspect two. Azure lost it. A disk died, and the file went with it."),
            maya("Azure keeps several copies, so one dead disk loses nothing. The letters tell you how many, and how far apart. LRS, locally redundant, is three copies in one datacenter. ZRS, zone redundant, spreads three copies across separate buildings in one region. GRS, geo redundant, adds three more in a paired region far away."),
            leo("So a dead disk can't be it. Suspect three. The file is old and slow, and I just have to wait longer."),
            maya("Now you're warm, but wrong in an important way. Look at the clue on the receipt's details page. Its access tier says Archive."),
            leo("Access tier. Which is what, in plain words?"),
            maya("A price plan on each blob. You pay either for keeping a file or for reading it, never cheaply for both. Hot costs the most to keep and the least to read. Cool is cheaper to keep and dearer to read, with a thirty day minimum. Cold is cheaper again, ninety days. Archive is the cheapest to keep, with a hundred and eighty days."),
            leo("And the receipt is in Archive. So it's just slow to read?"),
            maya("No, and that's the twist. An Archive blob is offline. Not slow, not dearer. A read simply fails, however long you wait. Under the hood, its bytes sit in offline storage, so Azure has to copy them back to an online tier before anything can read them."),
            leo("So how does support get the receipt back?"),
            maya("They rehydrate it, like adding water to dried food. You set its tier back to Hot or Cool, and Azure brings it online. On standard priority, that can take up to fifteen hours. So support tells the customer, tomorrow morning."),
            leo("[sigh] Fifteen hours. But who put it in Archive? Nobody here touches two year old receipts."),
            maya("Exactly, nobody did. A lifecycle rule did. It's a policy on the account that moves or deletes blobs by age. Theirs says Cool after thirty days, Archive after a year, deleted after six years. Once a day it compares each blob's last change against the rule, and moves whatever qualifies."),
            leo("So the rule did its job perfectly. The real crime is that nobody remembered it."),
            maya("[chuckle] Mystery solved. But the file has more clues. Last month the finance team moved a pile of test receipts to Cool, and deleted them a week later. The bill came with an early deletion fee."),
            leo("Because Cool wants thirty days, and they left after seven?"),
            maya("Right. You pay as if they'd stayed the full thirty. Same idea with Cold at ninety, and Archive at a hundred and eighty. Files that only live a few days belong in Hot."),
            leo("Any other clues?"),
            maya("Two. A developer made a new test account to try tiers, and the tier setting was missing. They had picked an old classic style account from an old script. Tiers need a general-purpose v2 account, called StorageV2."),
            leo("And the second?"),
            maya("The boss wants receipts to survive a whole region going down, and to be readable from the second region at any time, without waiting for a failover, which is the big switch-over. Someone picked GRS. But plain GRS only lets you read that far copy after a failover. Reading it any time is RA-GRS, read access GRS. And GZRS mixes zones with geo copies."),
            leo("What about the ride logs? Same kind of blob as the receipts?"),
            maya("No. Receipts are block blobs, the default, for files you upload whole. The ride log gets a line every second, and old lines never change, so it's an append blob, a diary written in pen. The third kind, the page blob, sits behind virtual machine disks."),
            leo("Where does Blob Storage stop being the right tool?"),
            maya("Three edges. It keeps files, it can't search inside them. To ask for every ride by one customer, keep those records in a database, which the Cosmos DB episode covers. Redundancy protects you from broken hardware, not from your own delete, because every copy goes at once. And to serve files fast to the whole world, put Front Door and CDN in front."),
            leo("And the code side? Uploading receipts from the backend?"),
            maya("That's the Blobs in code episode: the SDK, metadata, and links with limits. This case was about the building itself."),
            leo("So, case closed. What goes in the file for the exam?"),
            maya("Four rules. If a blob is in Archive, it must be rehydrated first, and that takes hours. If data must survive losing a region, GRS, and if it must be readable from the second region without failover, RA-GRS. If it's the cheapest with zone resilience, ZRS. And if files must move or be deleted by age, a lifecycle rule, on a StorageV2 account."),
            leo("Anything to try today?"),
            maya("Recreate the case, small. Create a StorageV2 account with LRS. Upload a file to a private container. Change its tier to Cool in the portal. Add a lifecycle rule that deletes after one day. And when you're done, delete the resource group, so nothing keeps costing you."),
            leo("A crime scene that cleans itself up. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val blobBasicsGossip = Podcast(
        "az-blob-basics",
        listOf(
            maya("Did you hear what Sam did with the gym app? Sam's team builds a little Android app for a local gym, with a backend on App Service. Members film their squats, and the videos go into Blob Storage. Well. This week Sam tried to play a video from three years ago. During a demo. To the gym owner."),
            leo("[laugh] I heard there was a spinning wheel and a very quiet room. What happened?"),
            maya("Let's start with what Blob Storage is, because Sam needed the refresher too. It's a place in the cloud for files of any size. Videos, backups, logs. Blob just means a file. You pay to store it, and you pay each time you touch it."),
            leo("And how is it organised? For those of us who are not Sam."),
            maya("Three layers. The storage account is the building. A container is a room in it, a folder-like group with its own access level, like private. The blob is the file. The gym app has one account with two containers, videos and receipts. And under the picture, every blob is just a name and its bytes, with its own web address. Folders are only slashes in names."),
            leo("So why didn't Sam's video play?"),
            maya("Because it was in the Archive tier. Every blob has an access tier, a price plan. Hot costs the most to keep and the least to read. Cool is cheaper to keep and dearer to read, with a thirty day minimum stay. Cold is cheaper again, ninety days. Archive is the cheapest of all, a hundred and eighty days."),
            leo("So Archive is slow. Sam should have waited."),
            maya("That's what Sam thought, for ten long minutes. But Archive isn't slow, it's offline. The bytes sit in offline storage, and a read simply fails. Priya rescued the demo with a newer video, then started a rehydrate on the old one. You set its tier back to Hot or Cool, and Azure copies it back online. Up to fifteen hours on standard priority."),
            leo("[sigh] Fifteen hours. And how did the video end up in Archive? Did Sam move it?"),
            maya("No, a lifecycle rule did. That's a policy on the account that moves or deletes blobs by age. Once a day it looks at each blob's last change and acts. Theirs says Cool after thirty days, Archive after a year. Nobody touches it by hand, which is the point, and also why nobody remembered."),
            leo("Poor Sam. Who's next?"),
            maya("Aisha. Aisha wanted to save money, so she moved last week's upload test videos to Cool. Then she tidied up and deleted them, a week later. She even posted a cheerful note in the channel about how much she'd saved."),
            leo("Tidy sounds good. What's wrong with tidy?"),
            maya("Cool has a thirty day minimum stay. Leave after seven, and you pay an early deletion fee, as if they'd stayed thirty. Dana spotted it on the bill and explained it kindly. Files that live a few days belong in Hot. Cool is for things you keep a month or more and rarely read, like monthly reports."),
            leo("Noted. Hot for short stays. Week three?"),
            maya("Ben. The gym owner said the videos must survive a whole region going down, and the far copy must be readable any time, even before a failover, the big switch-over. Ben picked GRS."),
            leo("GRS is the geo one, isn't it? Sounds right."),
            maya("Half right. First the letters. LRS, locally redundant, is three copies in one datacenter. ZRS spreads three copies across zones, separate buildings in one region. GRS adds three more copies in a paired region far away. But with plain GRS you can only read that far copy after a failover. Reading it any time is RA-GRS. Dana switched it over in five minutes."),
            leo("And GZRS? I've seen that one on the list too."),
            maya("Zones in the main region, plus the geo copies. So, cheapest with zone resilience is ZRS. Surviving a region is GRS. Reading the secondary without a failover is RA-GRS."),
            leo("And the intern? There's always the intern."),
            maya("[chuckle] The intern made a fresh account for a tiers experiment, from an old script. No tier setting anywhere. They'd made a classic style account, and it took most of a morning to notice. Priya showed them the fix: tiers need a general-purpose v2 account, StorageV2. New script, new account, and the tiers appeared."),
            leo("Did the gym's logs survive all this?"),
            maya("The logs were the one calm thing. The backend writes a line every second, and old lines never change, so it's an append blob, like a diary in pen. The videos are block blobs, the default for whole files. Page blobs are the third kind, behind virtual machine disks."),
            leo("So could the gym keep everything in Blob Storage? Member records, bookings?"),
            maya("That's where it stops. Blob Storage keeps files, it doesn't search inside them. Bookings by member is a job for a database, which the Cosmos DB episode covers. Redundancy also isn't a backup of your mistakes: delete a blob and every copy goes. And for videos that load fast for members everywhere, Front Door and CDN sit in front."),
            leo("And uploading straight from the phone?"),
            maya("Never with the storage account key inside the app. The Blobs in code episode covers the safe way, with short links."),
            leo("Okay, the office has taught me a lot. What do I remember for the exam?"),
            maya("Four things. If a blob is in Archive, it must be rehydrated before anyone reads it, and that takes hours. If data must survive a region, GRS, and readable from the secondary without failover, RA-GRS. If it's add-only logs, an append blob. And if files move or get deleted by age, a lifecycle rule, which needs StorageV2."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a StorageV2 account with LRS. Upload a file to a private container. Change its tier to Cool in the portal. Add a lifecycle rule that deletes after one day. Then delete the resource group, so nothing keeps costing you."),
            leo("No Archive in demos. Got it, Sam. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val blobSdkCase = Podcast(
        "az-blob-sdk",
        listOf(
            maya("Welcome to Case Files. Today's case: The Garden Anyone Could Enter. A small plant watering app for Android reminds people to water their plants, and lets them upload a photo of each one. The backend runs on App Service, and the photos live in Blob Storage, in a private container. On Monday, a user writes in. They can open other people's plant photos. And delete them."),
            leo("[chuckle] Someone's been trampling the flowerbeds. Detective Leo on the case. First suspect: the container was made public."),
            maya("Checked first. The container is private, with no anonymous access. A stranger without credentials gets turned away. Whoever is opening these photos is proving they're allowed."),
            leo("Suspect two, then. The backend's check is broken. It hands anyone any photo."),
            maya("[pause] Good thought, but look where the traffic goes. These reads never touched the backend at all. The storage logs show requests going straight from phones to storage, each one signed with the account key. The master key to the whole account."),
            leo("How would a phone have the account key?"),
            maya("That's the clue. Three months ago, to make uploads quick, someone put the account key inside the Android app. Anyone can take an app apart in minutes and read what's inside. One curious user did. With the master key, you can do anything to any file."),
            leo("[sigh] So the key's been out for three months. What's the fix?"),
            maya("Two parts. First, rotate the account key, so the leaked one stops working. Then never ship a key again. The app asks the backend, and the backend checks that this user owns this plant. Storage doesn't decide that, your code does. Then the backend hands back a SAS."),
            leo("A SAS. In plain words?"),
            maya("A shared access signature. A link with limits, like a hotel key card. It opens one door, does only what it allows, like read, and stops working at checkout time. Under the picture, it's the blob's address plus a few settings: the permissions, the expiry, and a signature. The signature is a code computed from those settings with a secret key."),
            leo("So what stops me changing the expiry to next year?"),
            maya("The signature. Storage recomputes it from the settings in the link. Change one letter, and the codes no longer match, so storage answers 403, forbidden. But note the edge. A SAS is like cash: whoever holds the link can use it. So keep it short."),
            leo("Which secret key signs it, though? Not the account key again?"),
            maya("There are three kinds. A service SAS covers one service, like blobs. An account SAS covers several. Both are signed with the account key, and the only way to cancel them is to rotate that key for everything, unless a service SAS uses a stored access policy. The third is the user delegation SAS. It covers blobs, and it's signed with a user delegation key."),
            leo("Which is what?"),
            maya("A key the backend gets from storage by signing in with Entra ID, using its managed identity. No account key anywhere. It lasts at most seven days, and revoking it cancels every link it signed. The code uses DefaultAzureCredential, the same helper as in the Key Vault episode."),
            leo("So the backend issues user delegation links. Case closed?"),
            maya("Not yet. The new links went out on Tuesday, and on Wednesday a user still deleted someone else's photo. Look at the link. The developer had allowed read, write and delete. Only read was needed."),
            leo("So the key card opened the minibar too."),
            maya("[laugh] Exactly. Give a SAS the smallest permissions that do the job, read for viewing, and keep the expiry short, thirty minutes say. When it expires, storage answers 403, and the app asks the backend for a fresh one. For uploads, the backend can upload the photo itself, or issue a short link that only creates one new blob."),
            leo("Any more clues?"),
            maya("One about labels. The app stores who uploaded each photo as metadata, little name and value pairs on the blob, like a luggage tag. Up to eight kilobytes, returned with every read. Then support asked for every photo from one user, across the account, and someone tried to search the metadata."),
            leo("And metadata can't be searched?"),
            maya("Not across blobs. You read it one blob at a time. For a search, you use blob index tags, also key-value pairs, but Azure keeps an index of them, like a library catalogue. Then you can ask for every blob where owner is this user. For richer questions about users and plants, the Cosmos DB episode is the place."),
            leo("Last clue?"),
            maya("The watering schedule. It's one shared file, and three copies of the backend update it. Two wrote at once, and a change vanished. So they took a lease, a write lock on a blob, like the engaged sign on a bathroom door. Fifteen to sixty seconds, or forever until released."),
            leo("And then everything worked?"),
            maya("Then the backend's own writes failed. Here's how a lease works. Taking it returns a lease id. Every write must carry that id, and storage refuses any write without it, even from the code that took the lease. They forgot to pass it. And the edge: a lease guards one blob. To share out lots of work between servers, a queue like Service Bus fits better."),
            leo("And the code itself? Is there a lot of it?"),
            maya("Three objects in the SDK, the ready-made library. A service client for the account, a container client for one container, and a blob client for one blob. Start at the building, walk into a room, pick up a box. The Blob Storage episode covers the building itself."),
            leo("So, case closed. What's the summary for the exam?"),
            maya("Four rules. If it says store the uploader's name with the file, metadata. If it says search blobs by a key across the account, index tags. If it says give a browser short access to one private blob, a short user delegation SAS from your API. And if it says one writer at a time, a lease. Never an account key in a mobile app."),
            leo("Anything to try today?"),
            maya("Recreate the case, safely. With the Azure CLI and the data contributor role, upload a text blob using your own login. Set metadata on it with the metadata update command. Generate a ten-minute read SAS as a user, so it's a user delegation SAS. Open the URL in a browser, then open it again after it expires."),
            leo("A key card that checks itself out. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val blobSdkGossip = Podcast(
        "az-blob-sdk",
        listOf(
            maya("Did you hear what Ben did with the quiz app's leaderboard? Ben's team runs a little trivia app for Android. The backend runs on App Service, three copies of it, and the weekly leaderboard is one file in Blob Storage. On Thursday, scores started vanishing."),
            leo("[laugh] I heard someone answered forty questions and ended up with zero. What happened?"),
            maya("Two copies of the backend read the leaderboard, added a score each, and wrote it back at the same moment. The second write wiped out the first. So Ben added a lease. A lease is a write lock on a blob, like the engaged sign on a bathroom door. Fifteen to sixty seconds, or forever until released."),
            leo("Sounds like the right fix. So where's the gossip?"),
            maya("Right after that, every write from the backend failed. Including the copy holding the lease. Ben had locked himself out of his own bathroom. He spent a whole afternoon sure that storage was broken, and posted three worried messages in the team chat."),
            leo("How? He had the lock."),
            maya("Here's how it works. Taking a lease gives you back a lease id. Every write after that must carry the id, and storage refuses any write without it, even from you. Priya found the one line that forgot to pass it. And she pointed out the edge: a lease guards one blob. Sharing lots of work between servers is a queue's job, like Service Bus."),
            leo("Poor Ben. Who's next?"),
            maya("Week two, the intern. Players can upload a profile picture. To make uploads quick, the intern put the storage account key inside the Android app. It worked beautifully in testing, which is always the worrying part."),
            leo("Oh no. The master key, on every phone?"),
            maya("Every phone. Anything inside an app can be taken apart in minutes, and that key opens every file in the account. Dana caught it in review, and rotated the key the same afternoon, just in case. Then she showed the intern the safe way. The app asks the backend, the backend checks the player, and hands back a SAS."),
            leo("Which is what, in plain words?"),
            maya("A shared access signature. A link with limits, like a hotel key card: one door, only what it allows, and it stops at checkout time. Under the picture, it's the blob's address plus the permissions, the expiry, and a signature, a code computed from those settings with a secret key. Change one letter, and storage says 403, forbidden."),
            leo("Which secret key, though? Not the account key again?"),
            maya("That's exactly what the intern asked. A service SAS and an account SAS are both signed with the account key, and you can only cancel them by rotating it, unless a service SAS sits on a stored access policy. The better one is a user delegation SAS. The backend signs in with Entra ID, using its managed identity, and gets a user delegation key from storage."),
            leo("So no account key anywhere."),
            maya("None. The user delegation key lasts at most seven days, and revoking it cancels every link it signed. The backend uses DefaultAzureCredential, same as in the Key Vault episode. And the phone side, signing the player in, is the MSAL episode."),
            leo("Week three?"),
            maya("Marcus. Marcus made the share-your-score links. Each one was a SAS with read, write and delete, valid for a month. He wanted friends to be able to fix typos on a shared score card, which is a kind thought with a big hole in it."),
            leo("So anyone with the link could change someone's score card?"),
            maya("Or delete it. A SAS is like cash: whoever holds it can spend it. Dana sat with Marcus and trimmed it. Read only, because that's all a viewer needs. Thirty minutes, not a month. When it expires, storage answers 403, and the app simply asks the backend for a fresh link."),
            leo("[chuckle] Dana is having a busy month. Anyone else?"),
            maya("Aisha. Aisha stored the quiz author's name on each question pack as metadata. Little name and value labels on the blob, like a luggage tag, up to eight kilobytes, returned with every read. Perfect. Then she was asked for every pack by one author, across the account, and tried to search the metadata."),
            leo("And metadata can't be searched?"),
            maya("Not across blobs, it's only read one at a time. Priya showed her blob index tags, also key-value pairs, but Azure keeps an index of them, like a library catalogue. Now she asks for every blob where author is Sam. For richer questions about players and scores, a database fits better, which the Cosmos DB episode covers."),
            leo("And the code under all this? Is it complicated?"),
            maya("Three objects in the SDK, the ready-made library. A service client for the account, a container client for one container, a blob client for one blob. Building, room, box. They're cheap to create, and none of them holds a connection until you call it. The Blob Storage episode covers the building itself."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If it says store the uploader's name with the file, metadata. If it says search blobs by a key across the account, index tags. If it says give a browser short access to one private blob, a short user delegation SAS from your API. And if it says one writer at a time, a lease. Never the account key in a mobile app."),
            leo("And something to try today, so I'm not the next story?"),
            maya("With the Azure CLI and the data contributor role, upload a text blob using your own login. Set metadata on it with the metadata update command. Generate a ten-minute read SAS as a user. Open the URL in a browser, then open it again after it expires."),
            leo("Leases, keys and luggage tags. Got it, Ben. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(blobBasicsCase, blobBasicsGossip, blobSdkCase, blobSdkGossip)

    private fun blobBasicsCaseBoard() = PodcastBoard(
        "az-blob-basics--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Receipt That Would Not Open..."
            Scene(
                0, "The receipt that won't open",
                DiagramSpec(
                    "The bike rental app's backend asks Blob Storage for a two-year-old receipt, and the download fails.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "Download fails", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("store", "Blob Storage", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "support"),
                        DEdge("app", "store", "download"),
                        DEdge("store", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "err"), light = setOf("err"), pulse = "phone" to "app", card = BoardCard("The case", "A receipt from two years ago. Support clicks download, and it fails.")),
                    Beat(2, show = setOf("store"), light = setOf("store"), pulse = "app" to "store", card = BoardCard("Blob Storage", "Files of any size in the cloud. Pay for storing, and for touching.")),
                ),
            ),
            // 3: "Walk me through the building."
            Scene(
                3, "Account, container, blob",
                DiagramSpec(
                    "A storage account holds containers, a container holds blobs, and a blob is a file with its own address.",
                    nodes = listOf(
                        DNode("acct", "Storage account", 0.5f, 0.16f, w = 0.46f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "the building"),
                        DNode("c1", "receipts", 0.25f, 0.52f, w = 0.38f, tone = 1, sub = "container"),
                        DNode("c2", "ride-logs", 0.75f, 0.52f, w = 0.38f, tone = 1, sub = "container"),
                        DNode("b1", "ride-0412.pdf", 0.25f, 0.85f, w = 0.34f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("b2", "today.log", 0.75f, 0.85f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("acct", "c1"),
                        DEdge("acct", "c2"),
                        DEdge("c1", "b1"),
                        DEdge("c2", "b2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("acct")),
                    Beat(4, show = setOf("c1", "c2", "b1", "b2"), light = setOf("c1", "c2"), pulse = "acct" to "c1", card = BoardCard("Container", "A room in the account: a folder-like group with its own access level.")),
                    Beat(6, light = setOf("b1"), pulse = "c1" to "b1", card = BoardCard("Under the picture", "A name and its bytes, with its own HTTPS address. Folders are just slashes.")),
                ),
            ),
            // 7: "Fine. My first suspect. Someone deleted the receipt."
            Scene(
                7, "The suspects",
                DiagramSpec(
                    "The receipt is still listed, a dead disk loses nothing, and waiting longer will not help.",
                    nodes = listOf(
                        DNode("del", "Deleted?", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "still listed"),
                        DNode("disk", "A dead disk?", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "other copies"),
                        DNode("slow", "Old, so slow?", 0.5f, 0.72f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "wait longer"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("del"), light = setOf("del")),
                    Beat(8, card = BoardCard("Suspect one: cleared", "Same name, same size, still listed. The receipt is still there.")),
                    Beat(9, show = setOf("disk"), light = setOf("disk")),
                    Beat(10, card = BoardCard("Suspect two: cleared", "LRS: 3 copies, one datacenter. ZRS: across zones. GRS: a second region too.")),
                    Beat(11, show = setOf("slow"), light = setOf("slow")),
                ),
            ),
            // 12: "Now you're warm, but wrong in an important way..."
            Scene(
                12, "The clue: Archive",
                DiagramSpec(
                    "Each access tier trades the price of keeping a file against the price of reading it; Archive is offline.",
                    nodes = listOf(
                        DNode("hot", "Hot", 0.26f, 0.2f, w = 0.42f, h = 0.22f, sub = "daily use"),
                        DNode("cool", "Cool", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "30 days minimum"),
                        DNode("cold", "Cold", 0.26f, 0.62f, w = 0.42f, h = 0.22f, tone = 1, sub = "90 days minimum"),
                        DNode("arch", "Archive", 0.76f, 0.62f, w = 0.42f, h = 0.22f, tone = 2, sub = "180 days, offline"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("arch"), light = setOf("arch"), card = BoardCard("The clue", "The receipt's access tier says Archive.")),
                    Beat(14, show = setOf("hot", "cool", "cold"), light = setOf("hot", "cool", "cold"), card = BoardCard("Access tier", "Pay to keep or pay to read, never cheaply for both.")),
                    Beat(16, light = setOf("arch"), card = BoardCard("Archive is offline", "Not slow. A read fails, however long you wait.")),
                ),
            ),
            // 17: "So how does support get the receipt back?"
            Scene(
                17, "Rehydrate first",
                DiagramSpec(
                    "An Archive blob must be rehydrated to Hot or Cool, which can take hours, before anything reads it.",
                    nodes = listOf(
                        DNode("arch", "Archive", 0.2f, 0.35f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2),
                        DNode("hot", "Hot or Cool", 0.8f, 0.35f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("time", "Up to 15 hours", 0.5f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "standard priority"),
                    ),
                    edges = listOf(
                        DEdge("arch", "hot", "rehydrate"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("arch")),
                    Beat(18, show = setOf("hot", "time"), light = setOf("time"), pulse = "arch" to "hot", card = BoardCard("Rehydrate", "Set the tier back to Hot or Cool, and Azure brings the blob online.")),
                ),
            ),
            // 19: "Fifteen hours. But who put it in Archive?..."
            Scene(
                19, "Who moved it?",
                DiagramSpec(
                    "A lifecycle rule moves each receipt from Hot to Cool to Archive by age, then deletes it, with nobody involved.",
                    nodes = listOf(
                        DNode("hot", "Hot", 0.17f, 0.2f, w = 0.28f, h = 0.18f, sub = "this month"),
                        DNode("cool", "Cool", 0.5f, 0.2f, w = 0.28f, h = 0.18f, tone = 1, sub = "after 30 days"),
                        DNode("arch", "Archive", 0.83f, 0.2f, w = 0.28f, h = 0.18f, tone = 2, sub = "after a year"),
                        DNode("gone", "Deleted", 0.83f, 0.58f, w = 0.28f, kind = NodeKind.PILL, tone = 2),
                        DNode("rule", "Lifecycle rule", 0.3f, 0.75f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "runs once a day"),
                    ),
                    edges = listOf(
                        DEdge("hot", "cool"),
                        DEdge("cool", "arch"),
                        DEdge("arch", "gone", "6 years"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("hot", "cool", "arch"), light = setOf("arch")),
                    Beat(20, show = setOf("gone", "rule"), light = setOf("rule"), pulse = "cool" to "arch", card = BoardCard("Lifecycle rule", "A policy on the account. Once a day it moves or deletes blobs by age.")),
                ),
            ),
            // 22: "Mystery solved. But the file has more clues..."
            Scene(
                22, "More clues",
                DiagramSpec(
                    "Three more mistakes in the file: an early deletion fee, a classic account, and GRS where RA-GRS was needed.",
                    nodes = listOf(
                        DNode("fee", "Cool, deleted in a week", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("v1", "A classic account", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("grs", "GRS, read any time?", 0.5f, 0.72f, w = 0.6f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("fee"), light = setOf("fee"), card = BoardCard("Early deletion fee", "Cool wants 30 days. Leave after 7, and you pay for 30.")),
                    Beat(26, show = setOf("v1"), light = setOf("v1"), card = BoardCard("Where are the tiers?", "Tiers need a general-purpose v2 account: StorageV2.")),
                    Beat(28, show = setOf("grs"), light = setOf("grs"), card = BoardCard("Read the far copy any time", "Plain GRS reads only after failover. Any time is RA-GRS.")),
                ),
            ),
            // 29: "What about the ride logs? Same kind of blob as the receipts?"
            Scene(
                29, "Three kinds of blob",
                DiagramSpec(
                    "Block blobs for whole files, append blobs for add-only logs, page blobs for disks.",
                    nodes = listOf(
                        DNode("block", "Block blob", 0.5f, 0.15f, w = 0.6f, h = 0.2f, sub = "receipts: whole files"),
                        DNode("append", "Append blob", 0.5f, 0.48f, w = 0.6f, h = 0.2f, tone = 1, sub = "the ride log"),
                        DNode("page", "Page blob", 0.5f, 0.81f, w = 0.6f, h = 0.2f, tone = 2, sub = "virtual machine disks"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("block"), light = setOf("block")),
                    Beat(30, show = setOf("append", "page"), light = setOf("append"), card = BoardCard("A line every second", "Add to the end, never edit: an append blob, a diary in pen.")),
                ),
            ),
            // 31: "Where does Blob Storage stop being the right tool?"
            Scene(
                31, "Where it stops",
                DiagramSpec(
                    "Blob Storage keeps files: it cannot search inside them, redundancy does not undo a delete, and it is not a CDN.",
                    nodes = listOf(
                        DNode("blob", "Blob Storage", 0.5f, 0.18f, w = 0.34f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("search", "Search records", 0.17f, 0.64f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "Cosmos DB"),
                        DNode("del", "Your own delete", 0.5f, 0.64f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "every copy goes"),
                        DNode("world", "Worldwide speed", 0.83f, 0.64f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "Front Door, CDN"),
                    ),
                    edges = listOf(
                        DEdge("blob", "search", dashed = true),
                        DEdge("blob", "del", dashed = true),
                        DEdge("blob", "world", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("blob")),
                    Beat(32, show = setOf("search", "del", "world"), light = setOf("search", "world"), card = BoardCard("Past the edge", "Every ride by one customer: Cosmos DB. Fast worldwide: Front Door and CDN.")),
                    Beat(34, light = setOf("blob"), card = BoardCard("Next: Blobs in code", "The SDK, metadata, and links with limits.")),
                ),
            ),
            // 35: "So, case closed. What goes in the file for the exam?"
            Scene(
                35, "Case closed",
                DiagramSpec(
                    "The exam's Blob Storage questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("arch", "Reading Archive", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "rehydrate, hours"),
                        DNode("region", "Lose a region", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "GRS"),
                        DNode("ra", "Read without failover", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "RA-GRS"),
                        DNode("zone", "Cheapest zone-safe", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "ZRS"),
                        DNode("age", "Move or delete by age", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "lifecycle rule, StorageV2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("arch", "region", "ra", "zone", "age"), light = setOf("arch"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Archive is offline, not slow.")),
                    Beat(38, card = BoardCard("Today", "StorageV2 with LRS, a private container, Cool tier, a one-day lifecycle rule.")),
                ),
            ),
        ),
    )

    private fun blobBasicsGossipBoard() = PodcastBoard(
        "az-blob-basics--gossip",
        listOf(
            // 0: "Did you hear what Sam did with the gym app?..."
            Scene(
                0, "What Sam did",
                DiagramSpec(
                    "The gym app's backend asks Blob Storage for a three-year-old video in the middle of a demo, and nothing plays.",
                    nodes = listOf(
                        DNode("phone", "Gym app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "Spinning wheel", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("store", "Blob Storage", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "play"),
                        DEdge("app", "store", "the video?"),
                        DEdge("store", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "err"), light = setOf("err"), pulse = "phone" to "app", card = BoardCard("The demo", "A three-year-old squat video, in front of the gym owner.")),
                    Beat(2, show = setOf("store"), light = setOf("store"), pulse = "app" to "store", card = BoardCard("Blob Storage", "Files of any size in the cloud. Pay for storing, and for touching.")),
                ),
            ),
            // 3: "And how is it organised? For those of us who are not Sam."
            Scene(
                3, "Account, container, blob",
                DiagramSpec(
                    "A storage account holds containers, a container holds blobs, and a blob is a file with its own address.",
                    nodes = listOf(
                        DNode("acct", "Storage account", 0.5f, 0.16f, w = 0.46f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "the building"),
                        DNode("c1", "videos", 0.25f, 0.52f, w = 0.38f, tone = 1, sub = "container"),
                        DNode("c2", "receipts", 0.75f, 0.52f, w = 0.38f, tone = 1, sub = "container"),
                        DNode("b1", "squat.mp4", 0.25f, 0.85f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("b2", "may.pdf", 0.75f, 0.85f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("acct", "c1"),
                        DEdge("acct", "c2"),
                        DEdge("c1", "b1"),
                        DEdge("c2", "b2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("acct")),
                    Beat(4, show = setOf("c1", "c2", "b1", "b2"), light = setOf("c1", "c2"), pulse = "acct" to "c1", card = BoardCard("Container", "A room with its own access level. Folders are only slashes in names.")),
                ),
            ),
            // 5: "So why didn't Sam's video play?"
            Scene(
                5, "Access tiers",
                DiagramSpec(
                    "Each access tier trades the price of keeping a file against the price of reading it.",
                    nodes = listOf(
                        DNode("hot", "Hot", 0.26f, 0.2f, w = 0.42f, h = 0.22f, sub = "daily use"),
                        DNode("cool", "Cool", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "30 days minimum"),
                        DNode("cold", "Cold", 0.26f, 0.62f, w = 0.42f, h = 0.22f, tone = 1, sub = "90 days minimum"),
                        DNode("arch", "Archive", 0.76f, 0.62f, w = 0.42f, h = 0.22f, tone = 2, sub = "180 days, offline"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("hot", "cool", "cold", "arch"), light = setOf("arch"), card = BoardCard("Access tier", "Pay to keep or pay to read, never cheaply for both.")),
                ),
            ),
            // 7: "So Archive is slow. Sam should have waited."
            Scene(
                7, "Offline, not slow",
                DiagramSpec(
                    "An Archive blob must be rehydrated to Hot or Cool, which can take hours, before anything reads it.",
                    nodes = listOf(
                        DNode("arch", "Archive", 0.2f, 0.35f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2),
                        DNode("hot", "Hot or Cool", 0.8f, 0.35f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("time", "Up to 15 hours", 0.5f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "standard priority"),
                    ),
                    edges = listOf(
                        DEdge("arch", "hot", "rehydrate"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("arch"), light = setOf("arch")),
                    Beat(8, show = setOf("hot", "time"), light = setOf("time"), pulse = "arch" to "hot", card = BoardCard("Priya's rehydrate", "Set the tier back to Hot or Cool, and Azure copies it back online.")),
                ),
            ),
            // 9: "Fifteen hours. And how did the video end up in Archive?..."
            Scene(
                9, "Who moved it?",
                DiagramSpec(
                    "A lifecycle rule moves each video from Hot to Cool to Archive by age, with nobody involved.",
                    nodes = listOf(
                        DNode("hot", "Hot", 0.17f, 0.25f, w = 0.28f, h = 0.18f, sub = "this month"),
                        DNode("cool", "Cool", 0.5f, 0.25f, w = 0.28f, h = 0.18f, tone = 1, sub = "after 30 days"),
                        DNode("arch", "Archive", 0.83f, 0.25f, w = 0.28f, h = 0.18f, tone = 2, sub = "after a year"),
                        DNode("rule", "Lifecycle rule", 0.5f, 0.72f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "runs once a day"),
                    ),
                    edges = listOf(
                        DEdge("hot", "cool"),
                        DEdge("cool", "arch"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("hot", "cool", "arch"), light = setOf("arch")),
                    Beat(10, show = setOf("rule"), light = setOf("rule"), pulse = "cool" to "arch", card = BoardCard("Lifecycle rule", "A policy that moves or deletes blobs by age. Nobody does it by hand.")),
                ),
            ),
            // 11: "Poor Sam. Who's next?"
            Scene(
                11, "Aisha's tidy week",
                DiagramSpec(
                    "Cool has a thirty day minimum stay, so deleting after a week brings an early deletion fee.",
                    nodes = listOf(
                        DNode("cool", "Cool", 0.24f, 0.3f, w = 0.4f, h = 0.22f, tone = 1, sub = "30 days minimum"),
                        DNode("week", "Deleted after 7", 0.76f, 0.3f, w = 0.4f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "billed for 30"),
                        DNode("hot", "Hot", 0.5f, 0.78f, w = 0.4f, h = 0.22f, sub = "short stays"),
                    ),
                    edges = listOf(
                        DEdge("cool", "week", "a week later", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("cool", "week"), light = setOf("week"), pulse = "cool" to "week", card = BoardCard("Aisha's clean-up", "Test videos moved to Cool, then deleted a week later.")),
                    Beat(14, show = setOf("hot"), light = setOf("hot"), card = BoardCard("Early deletion fee", "You pay as if they stayed 30 days. Short stays belong in Hot.")),
                ),
            ),
            // 15: "Noted. Hot for short stays. Week three?"
            Scene(
                15, "Ben's copies",
                DiagramSpec(
                    "LRS keeps copies in one building, ZRS across zones in a region, GRS in a second region too.",
                    nodes = listOf(
                        DNode("lrs", "LRS", 0.17f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "one building"),
                        DNode("zrs", "ZRS", 0.5f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "across zones"),
                        DNode("grs", "GRS", 0.83f, 0.2f, w = 0.3f, h = 0.2f, sub = "second region"),
                        DNode("ra", "RA-GRS", 0.83f, 0.62f, w = 0.3f, h = 0.2f, sub = "read it any time"),
                        DNode("gz", "GZRS: zones and geo", 0.33f, 0.62f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("lrs", "zrs"),
                        DEdge("zrs", "grs"),
                        DEdge("grs", "ra"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("grs"), light = setOf("grs"), card = BoardCard("Ben's pick: GRS", "Survive a region, and read the far copy any time?")),
                    Beat(18, show = setOf("lrs", "zrs", "ra"), light = setOf("grs", "ra"), pulse = "grs" to "ra", card = BoardCard("RA-GRS", "Plain GRS reads the far copy only after failover. Any time is RA-GRS.")),
                    Beat(20, show = setOf("gz"), light = setOf("zrs", "gz"), card = BoardCard("Cheapest zone-safe", "ZRS. Survive a region: GRS. Read it without failover: RA-GRS.")),
                ),
            ),
            // 21: "And the intern? There's always the intern."
            Scene(
                21, "The intern's account",
                DiagramSpec(
                    "A classic account has no tiers; a general-purpose v2 account, StorageV2, has them.",
                    nodes = listOf(
                        DNode("v1", "Classic account", 0.24f, 0.35f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "no tiers"),
                        DNode("v2", "StorageV2", 0.76f, 0.35f, w = 0.4f, h = 0.22f, tone = 1, sub = "tiers appear"),
                    ),
                    edges = listOf(
                        DEdge("v1", "v2", "new script"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("v1"), light = setOf("v1")),
                    Beat(22, show = setOf("v2"), light = setOf("v2"), pulse = "v1" to "v2", card = BoardCard("Priya's fix", "Tiers need a general-purpose v2 account: StorageV2.")),
                ),
            ),
            // 23: "Did the gym's logs survive all this?"
            Scene(
                23, "Three kinds of blob",
                DiagramSpec(
                    "Block blobs for whole files, append blobs for add-only logs, page blobs for disks.",
                    nodes = listOf(
                        DNode("block", "Block blob", 0.5f, 0.15f, w = 0.6f, h = 0.2f, sub = "videos: whole files"),
                        DNode("append", "Append blob", 0.5f, 0.48f, w = 0.6f, h = 0.2f, tone = 1, sub = "the log, add to the end"),
                        DNode("page", "Page blob", 0.5f, 0.81f, w = 0.6f, h = 0.2f, tone = 2, sub = "virtual machine disks"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("block", "append", "page"), light = setOf("append"), card = BoardCard("A line every second", "Old lines never change: an append blob, a diary in pen.")),
                ),
            ),
            // 25: "So could the gym keep everything in Blob Storage?..."
            Scene(
                25, "Where it stops",
                DiagramSpec(
                    "Blob Storage keeps files: it cannot search inside them, redundancy does not undo a delete, and it is not a CDN.",
                    nodes = listOf(
                        DNode("blob", "Blob Storage", 0.5f, 0.18f, w = 0.34f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("search", "Bookings", 0.17f, 0.64f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "Cosmos DB"),
                        DNode("del", "Your own delete", 0.5f, 0.64f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "every copy goes"),
                        DNode("world", "Worldwide speed", 0.83f, 0.64f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "Front Door, CDN"),
                    ),
                    edges = listOf(
                        DEdge("blob", "search", dashed = true),
                        DEdge("blob", "del", dashed = true),
                        DEdge("blob", "world", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("blob")),
                    Beat(26, show = setOf("search", "del", "world"), light = setOf("search", "del", "world"), card = BoardCard("Past the edge", "Bookings by member: Cosmos DB. Fast worldwide: Front Door and CDN.")),
                    Beat(28, light = setOf("blob"), card = BoardCard("Next: Blobs in code", "Never the account key in the app. Short links instead.")),
                ),
            ),
            // 29: "Okay, the office has taught me a lot. What do I remember for the exam?"
            Scene(
                29, "For the exam",
                DiagramSpec(
                    "The exam's Blob Storage questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("arch", "Reading Archive", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "rehydrate, hours"),
                        DNode("region", "Lose a region", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "GRS"),
                        DNode("ra", "Read without failover", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "RA-GRS"),
                        DNode("logs", "Add-only logs", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "append blob"),
                        DNode("age", "Move or delete by age", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "lifecycle rule, StorageV2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("arch", "region", "ra", "logs", "age"), light = setOf("arch"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Archive is offline, not slow.")),
                    Beat(32, card = BoardCard("Today", "StorageV2 with LRS, a private container, Cool tier, a one-day lifecycle rule.")),
                ),
            ),
        ),
    )

    private fun blobSdkCaseBoard() = PodcastBoard(
        "az-blob-sdk--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Garden Anyone Could Enter..."
            Scene(
                0, "The garden anyone could enter",
                DiagramSpec(
                    "The plant watering app keeps photos in a private container, yet users can open and delete each other's photos.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "Others' photos open", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("store", "Plant photos", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "uploads"),
                        DEdge("app", "store"),
                        DEdge("err", "phone", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "store", "err"), light = setOf("err"), pulse = "phone" to "app", card = BoardCard("The case", "Users can open, and delete, other people's plant photos.")),
                    Beat(2, light = setOf("store"), card = BoardCard("Suspect one: cleared", "The container is private, with no anonymous access.")),
                ),
            ),
            // 3: "Suspect two, then. The backend's check is broken..."
            Scene(
                3, "Who is reading?",
                DiagramSpec(
                    "The reads skip the backend entirely: phones go straight to storage, signed with the account key.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.2f, 0.18f, w = 0.32f, h = 0.18f),
                        DNode("app", "Backend", 0.2f, 0.75f, w = 0.32f, h = 0.2f),
                        DNode("key", "Account key", 0.58f, 0.18f, w = 0.32f, kind = NodeKind.PILL, tone = 2),
                        DNode("store", "Storage", 0.8f, 0.6f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app"),
                        DEdge("app", "store"),
                        DEdge("phone", "store", "signed with the key"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("phone", "app", "store"), light = setOf("app"), card = BoardCard("Suspect two: the backend?", "Maybe its owner check hands out any photo.")),
                    Beat(4, light = setOf("phone", "store"), pulse = "phone" to "store", card = BoardCard("Suspect two: cleared", "The reads skip the backend, each signed with the account key.")),
                    Beat(6, show = setOf("key"), light = setOf("key"), card = BoardCard("The clue", "The account key inside the Android app. Apps come apart in minutes.")),
                ),
            ),
            // 7: "So the key's been out for three months. What's the fix?"
            Scene(
                7, "Ask the backend",
                DiagramSpec(
                    "The app asks the backend, the backend checks the owner and issues a SAS, and the phone reads straight from storage.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.2f, 0.2f, w = 0.34f, h = 0.18f),
                        DNode("api", "Backend", 0.2f, 0.72f, w = 0.34f, h = 0.2f, sub = "checks the owner"),
                        DNode("sas", "SAS link", 0.74f, 0.2f, w = 0.4f, kind = NodeKind.PILL, sub = "read, 30 minutes"),
                        DNode("store", "Storage", 0.8f, 0.68f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "asks"),
                        DEdge("api", "sas", "issues"),
                        DEdge("phone", "store", "reads"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("phone")),
                    Beat(8, show = setOf("api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Rotate, then ask", "Rotate the key. The backend checks this user owns this plant.")),
                    Beat(10, show = setOf("sas", "store"), light = setOf("sas"), pulse = "api" to "sas", card = BoardCard("SAS: shared access signature", "A link with limits: one blob, only what it allows, until it expires.")),
                ),
            ),
            // 11: "So what stops me changing the expiry to next year?"
            Scene(
                11, "What's in the link",
                DiagramSpec(
                    "A SAS is the blob's address plus permissions, an expiry and a signature computed from them with a secret key.",
                    nodes = listOf(
                        DNode("addr", "Blob address", 0.26f, 0.2f, w = 0.42f, h = 0.18f),
                        DNode("perms", "Permissions", 0.74f, 0.2f, w = 0.42f, h = 0.18f, tone = 1, sub = "read"),
                        DNode("exp", "Expiry", 0.26f, 0.5f, w = 0.42f, h = 0.18f, tone = 1, sub = "30 minutes"),
                        DNode("sig", "Signature", 0.74f, 0.5f, w = 0.42f, h = 0.18f, sub = "a code from a key"),
                        DNode("tamper", "Changed? 403", 0.5f, 0.84f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("perms", "sig"),
                        DEdge("exp", "sig"),
                        DEdge("sig", "tamper", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("addr", "perms", "exp"), light = setOf("exp")),
                    Beat(12, show = setOf("sig", "tamper"), light = setOf("sig", "tamper"), pulse = "exp" to "sig", card = BoardCard("Change one letter", "The codes no longer match: 403. And whoever holds the link can use it.")),
                ),
            ),
            // 13: "Which secret key signs it, though? Not the account key again?"
            Scene(
                13, "Three kinds of SAS",
                DiagramSpec(
                    "Three kinds of SAS; only the user delegation SAS is signed without the account key.",
                    nodes = listOf(
                        DNode("sas", "SAS link", 0.5f, 0.13f, w = 0.4f, kind = NodeKind.PILL, sub = "a hotel key card"),
                        DNode("ud", "User delegation", 0.18f, 0.52f, w = 0.34f, h = 0.22f, sub = "Entra ID key"),
                        DNode("svc", "Service SAS", 0.52f, 0.52f, w = 0.3f, h = 0.22f, tone = 2, sub = "account key"),
                        DNode("acct", "Account SAS", 0.84f, 0.52f, w = 0.3f, h = 0.22f, tone = 2, sub = "account key"),
                        DNode("pol", "Stored access policy", 0.6f, 0.87f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("sas", "ud"),
                        DEdge("sas", "svc"),
                        DEdge("sas", "acct"),
                        DEdge("pol", "svc", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("sas")),
                    Beat(14, show = setOf("ud", "svc", "acct", "pol"), light = setOf("svc", "acct", "pol"), pulse = "pol" to "svc", card = BoardCard("Service and account SAS", "Signed with the account key. Cancel by rotating it, or a stored access policy.")),
                    Beat(16, light = setOf("ud"), pulse = "sas" to "ud", card = BoardCard("User delegation key", "From Entra ID via the managed identity. Seven days at most. Revoke to cancel.")),
                ),
            ),
            // 17: "So the backend issues user delegation links. Case closed?"
            Scene(
                17, "Too many permissions",
                DiagramSpec(
                    "Wednesday's link allowed read, write and delete; viewing a photo only ever needs read.",
                    nodes = listOf(
                        DNode("link", "SAS link", 0.5f, 0.16f, w = 0.4f, kind = NodeKind.PILL),
                        DNode("r", "Read", 0.18f, 0.56f, w = 0.28f, h = 0.18f, tone = 1),
                        DNode("w", "Write", 0.5f, 0.56f, w = 0.28f, h = 0.18f, tone = 2),
                        DNode("d", "Delete", 0.82f, 0.56f, w = 0.28f, h = 0.18f, tone = 2),
                        DNode("short", "Read only, 30 minutes", 0.5f, 0.86f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("link", "r"),
                        DEdge("link", "w"),
                        DEdge("link", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("link", "r", "w", "d"), light = setOf("w", "d"), card = BoardCard("Wednesday's link", "Read, write and delete. Only read was needed.")),
                    Beat(20, show = setOf("short"), light = setOf("r", "short"), pulse = "link" to "r", card = BoardCard("Smallest permissions", "Read only, short expiry. Then a 403, and the app asks for a fresh link.")),
                ),
            ),
            // 21: "Any more clues?"
            Scene(
                21, "Labels on a photo",
                DiagramSpec(
                    "Metadata travels with one blob and cannot be searched; index tags can be searched across the account.",
                    nodes = listOf(
                        DNode("blob", "A photo", 0.2f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("props", "System properties", 0.7f, 0.2f, w = 0.46f, h = 0.2f, tone = 2, sub = "type, last changed"),
                        DNode("meta", "Metadata", 0.2f, 0.55f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE, sub = "a luggage tag"),
                        DNode("tags", "Index tags", 0.7f, 0.55f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "a catalogue"),
                        DNode("query", "Every photo by one user", 0.5f, 0.87f, w = 0.7f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("blob", "props"),
                        DEdge("blob", "meta"),
                        DEdge("query", "tags", "search"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("blob", "props", "meta"), light = setOf("meta"), pulse = "blob" to "meta", card = BoardCard("Metadata", "Who uploaded the photo. Up to 8 KB, read one blob at a time.")),
                    Beat(24, show = setOf("query", "tags"), light = setOf("tags"), pulse = "query" to "tags", card = BoardCard("Index tags", "Searchable across the account. Richer questions: Cosmos DB.")),
                ),
            ),
            // 25: "Last clue?"
            Scene(
                25, "One writer at a time",
                DiagramSpec(
                    "A lease locks the schedule file for writes, so the backend copies take turns, and each write carries the lease id.",
                    nodes = listOf(
                        DNode("s1", "Backend 1", 0.16f, 0.18f, tone = 1),
                        DNode("s2", "Backend 2", 0.16f, 0.48f, tone = 1),
                        DNode("s3", "Backend 3", 0.16f, 0.78f, tone = 1),
                        DNode("file", "Schedule file", 0.74f, 0.48f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("lease", "Lease", 0.74f, 0.13f, w = 0.4f, kind = NodeKind.PILL, sub = "15 to 60 seconds"),
                    ),
                    edges = listOf(
                        DEdge("s1", "file", "lease id"),
                        DEdge("s2", "file", dashed = true),
                        DEdge("s3", "file", dashed = true),
                        DEdge("lease", "file"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("s1", "s2", "s3", "file", "lease"), light = setOf("lease"), card = BoardCard("Lease", "A write lock on one blob: 15 to 60 seconds, or until released.")),
                    Beat(28, light = setOf("s1", "file"), pulse = "s1" to "file", card = BoardCard("Pass the lease id", "Every write needs it, even yours. Sharing out work: Service Bus.")),
                ),
            ),
            // 29: "And the code itself? Is there a lot of it?"
            Scene(
                29, "Building, room, box",
                DiagramSpec(
                    "The SDK has three client objects: the account, one container in it, and one blob in that.",
                    nodes = listOf(
                        DNode("svc", "Service client", 0.18f, 0.42f, w = 0.32f, h = 0.26f, tone = 1, sub = "the building"),
                        DNode("cont", "Container client", 0.52f, 0.42f, w = 0.32f, h = 0.26f, sub = "one room"),
                        DNode("blob", "Blob client", 0.85f, 0.42f, w = 0.28f, h = 0.26f, tone = 2, sub = "one box"),
                        DNode("sdk", "SDK: a ready-made library", 0.5f, 0.84f, w = 0.6f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("svc", "cont"),
                        DEdge("cont", "blob"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("svc", "cont", "blob", "sdk"), light = setOf("svc", "cont", "blob"), pulse = "svc" to "cont", card = BoardCard("C#", "service.GetBlobContainerClient(\"photos\").GetBlobClient(\"fern.jpg\")", code = true)),
                ),
            ),
            // 31: "So, case closed. What's the summary for the exam?"
            Scene(
                31, "Case closed",
                DiagramSpec(
                    "The exam's blob code questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("meta", "Uploader's name", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "metadata"),
                        DNode("tags", "Search by a key", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "index tags"),
                        DNode("sas", "Short private link", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "user delegation SAS"),
                        DNode("lease", "One writer", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a lease"),
                        DNode("none", "No key in the app", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "the API issues a SAS"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("meta", "tags", "sas", "lease", "none"), light = setOf("none"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Never an account key in an app.")),
                    Beat(34, card = BoardCard("Today", "Upload a blob, set metadata, make a 10-minute user SAS, open it twice.")),
                ),
            ),
        ),
    )

    private fun blobSdkGossipBoard() = PodcastBoard(
        "az-blob-sdk--gossip",
        listOf(
            // 0: "Did you hear what Ben did with the quiz app's leaderboard?..."
            Scene(
                0, "What Ben did",
                DiagramSpec(
                    "Three backend copies update one leaderboard file; two wrote at once, so Ben added a lease.",
                    nodes = listOf(
                        DNode("s1", "Backend 1", 0.16f, 0.18f, tone = 1),
                        DNode("s2", "Backend 2", 0.16f, 0.48f, tone = 1),
                        DNode("s3", "Backend 3", 0.16f, 0.78f, tone = 1),
                        DNode("file", "Leaderboard", 0.74f, 0.48f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("lease", "Lease", 0.74f, 0.13f, w = 0.4f, kind = NodeKind.PILL, sub = "15 to 60 seconds"),
                    ),
                    edges = listOf(
                        DEdge("s1", "file"),
                        DEdge("s2", "file"),
                        DEdge("s3", "file"),
                        DEdge("lease", "file"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("s1", "s2", "s3", "file"), light = setOf("file"), card = BoardCard("Vanishing scores", "A trivia app's weekly leaderboard: one file, three backends.")),
                    Beat(2, show = setOf("lease"), light = setOf("s1", "s2", "lease"), pulse = "s2" to "file", card = BoardCard("Lease", "A write lock on a blob: the engaged sign on the bathroom door.")),
                ),
            ),
            // 3: "Sounds like the right fix. So where's the gossip?"
            Scene(
                3, "Locked out",
                DiagramSpec(
                    "Taking a lease returns a lease id, and storage refuses any write that does not carry it, even from the holder.",
                    nodes = listOf(
                        DNode("ben", "Backend", 0.2f, 0.3f, w = 0.32f, h = 0.2f, sub = "holds the lease"),
                        DNode("file", "Leaderboard", 0.8f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("lid", "Lease id", 0.5f, 0.8f, w = 0.4f, kind = NodeKind.PILL, sub = "on every write"),
                    ),
                    edges = listOf(
                        DEdge("ben", "file", "writes"),
                        DEdge("ben", "lid", "takes"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("ben", "file"), light = setOf("file"), pulse = "ben" to "file", card = BoardCard("Every write failed", "Even from the backend holding the lease.")),
                    Beat(6, show = setOf("lid"), light = setOf("lid"), pulse = "ben" to "lid", card = BoardCard("Pass the lease id", "Every write must carry it. Sharing out lots of work: Service Bus.")),
                ),
            ),
            // 7: "Poor Ben. Who's next?"
            Scene(
                7, "The intern's key",
                DiagramSpec(
                    "The account key inside an app opens every file; the safe way is to ask the backend, which hands back a SAS.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("key", "Account key", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the master key"),
                        DNode("api", "Backend", 0.22f, 0.72f, w = 0.34f, h = 0.2f, sub = "checks the player"),
                        DNode("store", "Storage", 0.78f, 0.72f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "asks"),
                        DEdge("api", "store", "a SAS"),
                        DEdge("key", "phone", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("phone", "key"), light = setOf("key"), pulse = "key" to "phone", card = BoardCard("Week two: the intern", "The storage account key inside the Android app, for quick uploads.")),
                    Beat(10, show = setOf("api", "store"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Dana's fix", "Rotate the key. The app asks the backend, which hands back a SAS.")),
                ),
            ),
            // 11: "Which is what, in plain words?"
            Scene(
                11, "What's in the link",
                DiagramSpec(
                    "A SAS is the blob's address plus permissions, an expiry and a signature computed from them with a secret key.",
                    nodes = listOf(
                        DNode("addr", "Blob address", 0.26f, 0.2f, w = 0.42f, h = 0.18f),
                        DNode("perms", "Permissions", 0.74f, 0.2f, w = 0.42f, h = 0.18f, tone = 1, sub = "read"),
                        DNode("exp", "Expiry", 0.26f, 0.5f, w = 0.42f, h = 0.18f, tone = 1, sub = "checkout time"),
                        DNode("sig", "Signature", 0.74f, 0.5f, w = 0.42f, h = 0.18f, sub = "a code from a key"),
                        DNode("tamper", "Changed? 403", 0.5f, 0.84f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("perms", "sig"),
                        DEdge("exp", "sig"),
                        DEdge("sig", "tamper", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("addr", "perms", "exp", "sig", "tamper"), light = setOf("sig"), pulse = "perms" to "sig", card = BoardCard("SAS: shared access signature", "A link with limits, like a hotel key card. Change one letter: 403.")),
                ),
            ),
            // 13: "Which secret key, though? Not the account key again?"
            Scene(
                13, "Three kinds of SAS",
                DiagramSpec(
                    "Three kinds of SAS; only the user delegation SAS is signed without the account key.",
                    nodes = listOf(
                        DNode("sas", "SAS link", 0.5f, 0.13f, w = 0.4f, kind = NodeKind.PILL, sub = "a hotel key card"),
                        DNode("ud", "User delegation", 0.18f, 0.52f, w = 0.34f, h = 0.22f, sub = "Entra ID key"),
                        DNode("svc", "Service SAS", 0.52f, 0.52f, w = 0.3f, h = 0.22f, tone = 2, sub = "account key"),
                        DNode("acct", "Account SAS", 0.84f, 0.52f, w = 0.3f, h = 0.22f, tone = 2, sub = "account key"),
                        DNode("pol", "Stored access policy", 0.6f, 0.87f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("sas", "ud"),
                        DEdge("sas", "svc"),
                        DEdge("sas", "acct"),
                        DEdge("pol", "svc", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("sas", "ud", "svc", "acct", "pol"), light = setOf("ud"), pulse = "sas" to "ud", card = BoardCard("User delegation key", "From Entra ID via the managed identity. No account key anywhere.")),
                    Beat(16, light = setOf("ud", "sas"), card = BoardCard("Seven days at most", "Revoke the key to cancel every link it signed. MSAL: the phone side.")),
                ),
            ),
            // 17: "Week three?"
            Scene(
                17, "Marcus's links",
                DiagramSpec(
                    "Marcus's share links allowed read, write and delete for a month; a viewer only ever needs read.",
                    nodes = listOf(
                        DNode("link", "SAS link", 0.5f, 0.16f, w = 0.4f, kind = NodeKind.PILL, sub = "valid a month"),
                        DNode("r", "Read", 0.18f, 0.56f, w = 0.28f, h = 0.18f, tone = 1),
                        DNode("w", "Write", 0.5f, 0.56f, w = 0.28f, h = 0.18f, tone = 2),
                        DNode("d", "Delete", 0.82f, 0.56f, w = 0.28f, h = 0.18f, tone = 2),
                        DNode("short", "Read only, 30 minutes", 0.5f, 0.86f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("link", "r"),
                        DEdge("link", "w"),
                        DEdge("link", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("link", "r", "w", "d"), light = setOf("w", "d"), card = BoardCard("Week three: Marcus", "Share-your-score links with read, write and delete, for a month.")),
                    Beat(20, show = setOf("short"), light = setOf("r", "short"), pulse = "link" to "r", card = BoardCard("A SAS is like cash", "Whoever holds it can spend it. Read only, 30 minutes, then a fresh link.")),
                ),
            ),
            // 21: "Dana is having a busy month. Anyone else?"
            Scene(
                21, "Aisha's labels",
                DiagramSpec(
                    "Metadata travels with one blob and cannot be searched; index tags can be searched across the account.",
                    nodes = listOf(
                        DNode("blob", "A quiz pack", 0.2f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("props", "System properties", 0.7f, 0.2f, w = 0.46f, h = 0.2f, tone = 2, sub = "type, last changed"),
                        DNode("meta", "Metadata", 0.2f, 0.55f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE, sub = "a luggage tag"),
                        DNode("tags", "Index tags", 0.7f, 0.55f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "a catalogue"),
                        DNode("query", "Every pack by one author", 0.5f, 0.87f, w = 0.7f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("blob", "props"),
                        DEdge("blob", "meta"),
                        DEdge("query", "tags", "search"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("blob", "props", "meta"), light = setOf("meta"), pulse = "blob" to "meta", card = BoardCard("Metadata", "The author's name on each pack. Up to 8 KB, read one blob at a time.")),
                    Beat(24, show = setOf("query", "tags"), light = setOf("tags"), pulse = "query" to "tags", card = BoardCard("Index tags", "Searchable across the account. Richer questions: Cosmos DB.")),
                ),
            ),
            // 25: "And the code under all this? Is it complicated?"
            Scene(
                25, "Building, room, box",
                DiagramSpec(
                    "The SDK has three client objects: the account, one container in it, and one blob in that.",
                    nodes = listOf(
                        DNode("svc", "Service client", 0.18f, 0.42f, w = 0.32f, h = 0.26f, tone = 1, sub = "the building"),
                        DNode("cont", "Container client", 0.52f, 0.42f, w = 0.32f, h = 0.26f, sub = "one room"),
                        DNode("blob", "Blob client", 0.85f, 0.42f, w = 0.28f, h = 0.26f, tone = 2, sub = "one box"),
                        DNode("sdk", "SDK: a ready-made library", 0.5f, 0.84f, w = 0.6f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("svc", "cont"),
                        DEdge("cont", "blob"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("svc", "cont", "blob", "sdk"), light = setOf("svc", "cont", "blob"), pulse = "cont" to "blob", card = BoardCard("C#", "new BlobServiceClient(uri, new DefaultAzureCredential())", code = true)),
                ),
            ),
            // 27: "So, the gossip I should remember for the exam?"
            Scene(
                27, "For the exam",
                DiagramSpec(
                    "The exam's blob code questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("meta", "Uploader's name", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "metadata"),
                        DNode("tags", "Search by a key", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "index tags"),
                        DNode("sas", "Short private link", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "user delegation SAS"),
                        DNode("lease", "One writer", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a lease"),
                        DNode("none", "No key in the app", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "the API issues a SAS"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("meta", "tags", "sas", "lease", "none"), light = setOf("none"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Never an account key in an app.")),
                    Beat(30, card = BoardCard("Today", "Upload a blob, set metadata, make a 10-minute user SAS, open it twice.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy {
        listOf(blobBasicsCaseBoard(), blobBasicsGossipBoard(), blobSdkCaseBoard(), blobSdkGossipBoard())
    }
}
