package com.shahaabapps.core.content

/** The Monitor module's Redis and Front Door topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledMonitor2 {
    private val redisCase = Podcast(
        "az-redis",
        listOf(
            maya("Welcome to Case Files. Today's case: The Seven O'Clock Freeze. A small gym, Iron Lane, has an Android app for booking classes. The app calls a small backend on Azure App Service, which keeps the class timetable in Azure Cache for Redis. Every Monday at seven, when everyone books at once, the app freezes, and the logs fill with Redis timeouts."),
            leo("[chuckle] A gym that can't handle a rush. Detective Leo reporting. First, what is Redis, in plain words?"),
            maya("A notepad kept in memory, right next to your app. It's a key-value store: you save a value under a name, called the key, and you get it back by that name. Because it lives in memory and not on disk, reads take under a millisecond."),
            leo("And under the picture? Is it just a big notepad in the sky?"),
            maya("Under the picture, Redis is a server program that holds all its data in memory. Azure Cache for Redis is that server, run for you on Azure's machines. Your code sends it short commands, like GET and SET, over a network connection, and it answers almost at once because it never has to read from a disk."),
            leo("Okay. My first suspect: the cache is full. Too much stuff in it, so it chokes."),
            maya("[pause] Good thought, and it's a real mistake, keys without an expiry. But a full cache doesn't freeze. When memory runs out, the eviction policy picks keys to throw away. The default, volatile LRU, drops the least recently used keys that have a TTL first. You'd see more misses, not timeouts. And the memory chart shows the cache half empty."),
            leo("Wait. What's a TTL?"),
            maya("Time to live. A best-before date on a key. After, say, ten minutes, the key disappears, and the next read fetches a fresh copy. Every key should have one, or memory fills and eviction picks for you."),
            leo("Suspect two, then. They're on the cheap tier, and it falls over under load."),
            maya("Another real mistake, choosing Basic for production. Basic is one node with no SLA, which is the uptime promise, so it's for testing only. But Iron Lane is on Standard: two copies, with a ninety-nine point nine percent SLA. And a tier problem wouldn't come back every Monday at seven sharp."),
            leo("So what does happen every Monday at seven? Lots of requests."),
            maya("That's the clue. Look at the connected clients chart. Normally it sits at a handful. At seven it climbs into the thousands, one new connection for every booking request. So the question is, who opens a connection for every request?"),
            leo("Ooh. The code does."),
            maya("Exactly. In the dot net client, you connect through an object called the ConnectionMultiplexer. Think of it as the phone line to the cache. Last month a developer moved that line inside the booking handler. So every request dialled a new line, then hung up."),
            leo("Is dialling a new line so bad? It sounds tidy."),
            maya("It's expensive. Each one opens a network connection and does a secure handshake before a single command is sent. The multiplexer is built to be shared: one line carries the commands of every request at once. Create it per request, and at seven on a Monday the cache runs out of connections. Create one, as a singleton, a single shared copy for the life of the app."),
            leo("[laugh] So the gym was dialling the cache thousands of times a minute. Case closed?"),
            maya("Not yet. There's a second witness. A member called Nadia opened My Bookings and saw a stranger's classes."),
            leo("[sigh] That sounds worse than a freeze."),
            maya("It is. The backend uses the usual pattern, cache-aside. It looks in the cache first. If the value is there, that's a hit. If not, a miss: it reads the real database, saves the value in the cache with a TTL, and returns it. The bug was the key. Every member's bookings went under one shared key, called my-bookings."),
            leo("So the first member to miss filled it, and everyone after got their copy."),
            maya("Right. Per-user data needs the user in the key, like bookings, then the member's ID. Never one user's data under a shared key."),
            leo("Any more clues?"),
            maya("One more. A cancelled class kept showing for ten minutes. The fix is to invalidate on write. When a booking changes, delete the key, so the next read rebuilds it. The TTL is the safety net, not the plan."),
            leo("And where does this stop working? Could they just keep everything in Redis?"),
            maya("That's the edge. Redis is not a database. Assume anything in it can disappear, after a restart or an eviction, and always keep the slow path that reads the real store. Iron Lane's bookings live in Cosmos DB, and the Cosmos DB episode covers that. And big files for users far away are a job for the Front Door and CDN episode, not Redis."),
            leo("The gym also wanted a monthly leaderboard of who came most. Redis for that too?"),
            maya("A sorted set. It keeps members in order by a score. Each member is an entry, their visits are the score, and the top ten comes back instantly. Not a string per member, and not a list, which only keeps arrival order. A hash, by the way, holds one object's fields, like a cached member profile."),
            leo("And if they someday need the cache to survive a restart, or sit in a private network?"),
            maya("Then it's Premium, which adds saving to disk, private networks, clustering and geo-replication. Enterprise adds modules like RediSearch, and active geo-replication, writes in several regions at once. And keep access keys out of code: managed identity where it's supported, or the connection string in Key Vault, as the Key Vault episode shows."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If the question asks how many connection objects, it's one shared ConnectionMultiplexer, never one per request. If it says leaderboard, it's a sorted set. If it says read the cache first and fill it on a miss, it's cache-aside, with a TTL, and delete the key on write. If it must persist to disk or live in a private network, Premium at least, and Basic is never production."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the clues. Create a Basic C0 cache, a few cents an hour, and open the portal Console. Run SET greeting hello with a sixty second expiry, then GET greeting. Wait a minute and GET again. Then ZADD two streaks to a sorted set, and read them back highest first with ZREVRANGE, WITHSCORES."),
            leo("A greeting that vanishes. My kind of evidence. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val redisGossip = Podcast(
        "az-redis",
        listOf(
            maya("Did you hear what Sam did to the plant app last week? The whole team is still talking about it."),
            leo("[laugh] I heard the words Redis and outage in the same sentence. What's the plant app?"),
            maya("Sprout. A little Android app that reminds you to water your plants. Its backend runs on Azure App Service, and the watering schedules are cached in Azure Cache for Redis, so the home screen opens instantly."),
            leo("Hold on. Redis, in plain words, for those of us who aren't on the plant team?"),
            maya("A notepad kept in memory, right next to your app. It's a key-value store: you save a value under a name, called the key, and get it back by that name. It lives in memory, not on disk, so reads take under a millisecond."),
            leo("And under the notepad, what is it really?"),
            maya("A server program that keeps all its data in memory, which Azure runs for you on its own machines. Your backend sends it short commands, like GET and SET, over a network connection. Because it never has to read from a disk, it answers almost at once."),
            leo("Okay. So what did Sam do?"),
            maya("Sam wrote the caching code. In the dot net client, you talk to Redis through one object, the ConnectionMultiplexer. Think of it as the phone line to the cache. Sam created a new one inside every request."),
            leo("Which sounds tidy, honestly."),
            maya("That's what Sam said. But each new one opens a network connection and does a secure handshake before anything is sent. The multiplexer is built so one line carries everyone's commands. On Sunday evening, when everyone waters their plants, thousands of requests each dialled a new line, and the cache ran out of connections."),
            leo("[sigh] Poor Sam. Who fixed it?"),
            maya("Priya. She made one multiplexer, as a singleton, a single shared copy for the life of the app, and every request borrows it. Connections dropped from thousands to a handful. Sam bought her coffee for a week."),
            leo("Fair price. Was that the only drama?"),
            maya("Oh no. Week two was Aisha. Aisha cached the details of every plant photo, and set no expiry on any of them."),
            leo("Expiry, like a best-before date?"),
            maya("Exactly. It's called a TTL, time to live. After, say, ten minutes, the key disappears and the next read fetches a fresh copy. Aisha's keys never expired, so memory slowly filled up. And when Redis is full, the eviction policy decides which keys to throw out."),
            leo("So Redis just picks? That doesn't sound like a plan."),
            maya("It isn't. The default, volatile LRU, drops the least recently used keys that have a TTL first. Aisha's keys had none, so the few keys that did, the watering schedules, were the ones thrown out. Dana put an expiry on every key, and the schedules stayed."),
            leo("[chuckle] Evicted by photos. Week three?"),
            maya("Ben. Ben added a My Plants screen, using cache-aside. That's the usual pattern. Look in the cache first. On a hit, done. On a miss, read the real database, save the value with a TTL, and return it. Except Ben saved everyone's plants under one shared key, called my-plants."),
            leo("Oh no. So somebody saw somebody else's cactus."),
            maya("Somebody saw a stranger's entire balcony. Dana put the user in the key, plants, then the user's ID. And she added the other half of the pattern: invalidate on write. When someone adds a plant, delete their key, so the next read rebuilds it. The TTL is the safety net, not the plan."),
            leo("And the intern? There's always an intern."),
            maya("Bless them. The intern set up the production cache on Basic, because it was cheapest. Basic is a single node with no SLA, which is the uptime promise. During maintenance it restarted, and the whole cache came back empty."),
            leo("Did the app go down?"),
            maya("No, and that's the good news. The backend still had the slow path, reading Cosmos DB, so it was just slower for a while. That's the edge of the idea: Redis is not a database. Assume anything can disappear. The real data lives in Cosmos DB, which the Cosmos DB episode covers. And the team moved to Standard, two copies with a ninety-nine point nine percent SLA."),
            leo("What if they want the cache to survive a restart?"),
            maya("Premium. It adds saving to disk, private networks, clustering and geo-replication. Enterprise adds modules like RediSearch, and writes in several regions at once. And for the plant photos themselves, sent to phones around the world, Redis is the wrong tool: that's Blob Storage behind Front Door, in the Front Door and CDN episode."),
            leo("Did anything good happen this month?"),
            maya("Priya built a watering streak leaderboard. She used a sorted set, which keeps members in order by a score. Each user is a member, their streak is the score, and the top ten comes back instantly. A hash would hold one user's fields, and a list just keeps arrival order. Oh, and she put the connection string in Key Vault, not in code."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If the question asks how many connection objects, pick one shared ConnectionMultiplexer, never one per request. If it says leaderboard, it's a sorted set. If it says read the cache first and fill it on a miss, that's cache-aside, with a TTL, and delete on write. And if it must persist to disk or sit in a private network, Premium at least. Basic is never production."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Basic C0 cache, and delete it after. Open the portal Console and run SET greeting hello with a sixty second expiry, then GET greeting. Wait a minute and GET again. Then ZADD two streaks to a sorted set, and read them back highest first with ZREVRANGE, WITHSCORES."),
            leo("One multiplexer, and an expiry on everything. Got it, Sam. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val cdnCase = Podcast(
        "az-cdn",
        listOf(
            maya("Welcome to Case Files. Today's case: The Borrowed Bike. A small bike rental company, Spoke, has an Android app. Riders open it to see their rides and their bill. Last week the team put Azure Front Door in front of everything, to make the app faster. On Tuesday, a rider in Lahore opened My Rides and saw a stranger's rides."),
            leo("[chuckle] Somebody else's bike trips on my phone. Detective Leo is on it. But first, what's Front Door?"),
            maya("Azure's content delivery network, CDN for short, and the global front entrance to your site. Picture a chain of corner shops. The factory is far away, but each shop keeps stock near you. Front Door keeps copies of your files on servers close to your users, called edges, or points of presence."),
            leo("And the factory?"),
            maya("The origin, where your real content lives, like a storage account or your backend. When an edge doesn't have a file yet, that's a miss, and it fetches it from the origin once. Everyone nearby after that gets the edge's copy. That's a hit."),
            leo("Is it really just shops, under the picture?"),
            maya("Under the picture, Front Door is a worldwide network of Microsoft servers that stands between your users and your origin. Every request goes to it first. Each edge keeps responses in a cache, filed by their web address, so it answers repeats itself and only forwards what it doesn't have."),
            leo("How does my phone end up at the nearest one?"),
            maya("Front Door's addresses are announced from every edge at once, so the internet's own routing carries each request to the closest edge. A rider in Lahore lands near Lahore, and the app doesn't need to know anything about it."),
            leo("Back to the crime. My first suspect: the database mixed up two riders."),
            maya("[pause] Good instinct, wrong suspect. The backend logs show only one request for those rides, from the first rider. The second rider's request never reached the backend at all."),
            leo("Never reached it? Then who answered?"),
            maya("That's the clue. Every Front Door response carries a header, a little label, called X-Cache. The stranger's rides came back marked as a hit. The edge answered from its own copy."),
            leo("So the edge kept the first rider's page and gave it to the next one. [pause] Oh. The API had caching switched on."),
            maya("Detective Leo, finally. When the team added Front Door, they switched caching on for every route, the API too. My Rides is one address for every rider, and the backend sent nothing saying, don't keep this. So the edge kept it, and served it to the next rider nearby."),
            leo("So what's the fix?"),
            maya("Caching rules. There are three. Honor origin obeys the Cache-Control header your origin sends, and it's the default. Override sets your own TTL, the time to live, for files that arrive with no header."),
            leo("But My Rides came with no header at all. Wouldn't override just keep it for longer?"),
            maya("Exactly, which is why personal answers get the third rule, bypass cache, which never keeps a copy. Spoke moved the API onto its own route, with caching bypassed."),
            leo("Wait, the TTL. How long does an edge keep a copy anyway?"),
            maya("As long as the Cache-Control header says. It's a best-before date on the file. With no header, Front Door uses a default, a day or a few days depending on the product, unless a caching rule overrides it."),
            leo("Case closed?"),
            maya("Not quite. Second clue. The same week, Spoke changed its bike map picture, and riders kept seeing the old one. The developer purged it, refreshed at once, still old, and purged again. Five times."),
            leo("[laugh] Purge harder. So purging is broken?"),
            maya("No. A purge throws away the cached copy before its TTL ends, by path or with a wildcard. But it takes a few minutes, it's per path, and it's a request to every edge. The better habit is a versioned file name, with a content hash in it. A new name is a new address, and no edge holds a copy of an address it has never seen."),
            leo("Why not set the TTL to one second and never worry?"),
            maya("Then the edge goes back to the origin almost every time, and you've thrown away the point of a CDN. Third clue. Spoke's help site, a static website in a storage account, showed errors on its home page through Front Door."),
            leo("My suspect: the files were never uploaded."),
            maya("They were there. The origin was pointed at the plain blob address of the storage account, not its static website address. Only the static website address serves index pages the way a website expects. The Blob Storage episode covers both addresses."),
            leo("Any last clue?"),
            maya("Query strings, the bit after the question mark in a web address. Query string caching decides whether each different one gets its own copy. Spoke's map pictures carried a tracking tag that never changed the file, and the route was set to use query strings. So the edges filled up with identical copies. Ignore that one. Keep use for parameters that really change the file, like a map's size."),
            leo("So where does Front Door stop being the answer?"),
            maya("It keeps copies of web responses that are the same for everyone, never anything personal. Data your backend reads over and over, like a rider's current rental, belongs in a cache next to the backend: the Redis cache episode. Front Door Standard is the current CDN. Premium adds managed firewall rules and Private Link, a private road to your origin. Classic CDN profiles still appear on the exam."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If users still see an old file, purge the path, and use versioned names next time. If different users need different content at one address, bypass the cache, or vary on the query string. If it asks what sets an edge's TTL, Cache-Control from the origin, unless a rule overrides it. And the current CDN and global entry point is Front Door."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case. Enable a static website on a storage account, and create a Front Door Standard profile pointing at it. Load the page twice and check the X-Cache header, a miss and then a hit. Then change the file, see the old one, purge, and see the new one. Front Door has an hourly fee, so delete the profile after."),
            leo("A miss, then a hit, then a purge. Very satisfying. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val cdnGossip = Podcast(
        "az-cdn",
        listOf(
            maya("Did you hear what Dana did to the habit tracker? Apparently half the office found out about somebody's secret yoga streak."),
            leo("[laugh] I heard yoga and leak in the same sentence, and I need the rest. What happened?"),
            maya("So, Streakly. A little Android app for tracking habits, with a backend API on App Service and a help site. Its users are all over the world, so Dana put Azure Front Door in front of the lot, to make it fast for everyone."),
            leo("Front Door, in plain words?"),
            maya("Azure's content delivery network, CDN for short, and the global front entrance to your site. Picture a chain of corner shops. The factory is far away, but each shop keeps stock near you. Front Door keeps copies of your files on servers near your users, called edges, or points of presence."),
            leo("And the factory is?"),
            maya("The origin, where the real content lives. When an edge doesn't have something yet, that's a miss, and it fetches it from the origin once. Everyone nearby after that gets the edge's copy, a hit. Under the picture, it's a worldwide network of Microsoft servers standing between your users and your origin. Every request goes there first."),
            leo("How does a phone in Doha find the edge in Doha?"),
            maya("Front Door's addresses are announced from every edge at once, so the internet's own routing carries each request to the closest one. Then the edge looks up the web address in its cache. If it has a fresh copy, it answers itself. If not, it asks the origin."),
            leo("So what did Dana do?"),
            maya("Dana switched caching on for every route, the API too. The My Habits screen is one address for every user. So the first user's answer was kept at the edge, and the next user nearby got it. Yoga streak and all."),
            leo("[sigh] Oh, Dana. Who fixed it?"),
            maya("Marcus. He explained the caching rules. Honor origin obeys the Cache-Control header the origin sends, and it's the default. Override sets your own TTL for files that arrive with no header."),
            leo("Neither of those stops the edge keeping someone's yoga streak, though."),
            maya("No, that's the third, bypass cache, which never keeps a copy. The API got its own route, with caching bypassed. Anything personal never sits at the edge."),
            leo("TTL I know from Redis. Time to live?"),
            maya("Right. How long an edge keeps its copy. It comes from the origin's Cache-Control header, a best-before date on the file, unless a rule overrides it. With no header, Front Door uses a default of a day or a few days, depending on the product."),
            leo("Okay. Week two?"),
            maya("Tom. Tom updated the app's logo on the help site, and users kept seeing the old one. So Tom purged it. Refreshed. Still old. Purged again. Seven times, and then he raised a support ticket."),
            leo("[chuckle] Seven purges. What's a purge, exactly?"),
            maya("Throwing away the cached copy before its TTL ends, by path, or with a wildcard. It works, but it takes a few minutes, it's per path, and it's a request to every edge. Marcus showed him the better habit: a versioned file name, with a content hash in it. A new name is a new address, and no edge has a copy of an address it has never seen."),
            leo("Why not a TTL of one second, so it's always fresh?"),
            maya("That's what Tom asked. Then the edge goes back to the origin almost every time, and there's no point having a CDN at all. Anyway. Week three was Priya."),
            leo("Priya? She never makes mistakes."),
            maya("Everyone has a week. Priya moved the help site to a static website in a storage account, and pointed Front Door's origin at the plain blob address instead of the static website address. The home page broke, because only the static website address serves index pages the way a site expects. Fixed by lunch. The Blob Storage episode covers both addresses."),
            leo("And the intern? I sense an intern."),
            maya("The intern added a tracking tag to every image link, as a query string. That's the bit after the question mark in a web address. The route was set to use query strings, so each different tag made its own copy at the edge. Same picture, thousands of copies."),
            leo("So what should it have been?"),
            maya("Query string caching has choices. Ignore them, and there's one copy no matter what. Use them, and every unique address gets a copy. Or include or exclude certain ones. A chart that really changes with its size needs use. A tracking tag that never changes the file? Ignore it."),
            leo("Is Front Door the answer to every slow thing, then?"),
            maya("No, and that's its edge. It keeps copies of web responses that are the same for everyone. Data your backend reads over and over, like each user's streaks, belongs in a cache next to the backend, and the Redis cache episode covers that. Front Door Standard is the current CDN, Premium adds managed firewall rules and Private Link to the origin, and classic CDN profiles still appear on the exam."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If users still see an old file, purge the path, and use versioned names next time. If different users need different content at one address, bypass the cache, or vary on the query string. If it asks what sets an edge's TTL, Cache-Control from the origin, unless a rule overrides it. And the current CDN and global entry point is Front Door."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Enable a static website on a storage account, and create a Front Door Standard profile pointing at it. Load the page twice and check the X-Cache header, a miss and then a hit. Then change the file, see the old one, purge, and see the new one. Delete the profile after, because it has an hourly fee."),
            leo("Bypass the API, and nobody hears about my yoga. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(redisCase, redisGossip, cdnCase, cdnGossip)

    private fun redisCaseBoard() = PodcastBoard(
        "az-redis--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Seven O'Clock Freeze..."
            Scene(
                0, "The Seven O'Clock Freeze",
                DiagramSpec(
                    "The gym's Android app calls its backend, which reads the timetable from Redis, and at seven every Monday it times out.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "Timeouts at seven", 0.76f, 0.2f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("redis", "Redis", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "in memory"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "books"),
                        DEdge("app", "redis", "GET, SET"),
                        DEdge("redis", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "err"), light = setOf("err"), pulse = "phone" to "app", card = BoardCard("The case", "Every Monday at seven, the gym's booking app freezes.")),
                    Beat(2, show = setOf("redis"), light = setOf("redis"), pulse = "app" to "redis", card = BoardCard("Redis", "A notepad in memory: save a value under a key, get it back by name.")),
                    Beat(4, light = setOf("redis"), card = BoardCard("Under the picture", "A server holding all its data in memory, run for you on Azure.")),
                ),
            ),
            // 5: "Okay. My first suspect: the cache is full..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "A full cache evicts keys and a Basic cache has no replica, but neither explains a freeze every Monday at seven.",
                    nodes = listOf(
                        DNode("full", "Cache full?", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "half empty"),
                        DNode("basic", "Basic tier?", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "it's Standard"),
                        DNode("evict", "Eviction policy", 0.5f, 0.74f, w = 0.66f, h = 0.22f, sub = "volatile LRU"),
                    ),
                    edges = listOf(
                        DEdge("full", "evict", "drops keys"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("full"), light = setOf("full")),
                    Beat(6, show = setOf("evict"), light = setOf("evict"), pulse = "full" to "evict", card = BoardCard("Suspect one: cleared", "A full cache evicts keys: more misses, not timeouts.")),
                    Beat(8, light = setOf("evict"), card = BoardCard("TTL: time to live", "A best-before date on a key. Every key needs one.")),
                    Beat(9, show = setOf("basic"), light = setOf("basic")),
                    Beat(10, card = BoardCard("Suspect two: cleared", "Basic: one node, no SLA, testing only. Iron Lane is on Standard.")),
                ),
            ),
            // 11: "So what does happen every Monday at seven? Lots of requests."
            Scene(
                11, "One phone line",
                DiagramSpec(
                    "Every request shares one ConnectionMultiplexer, created once for the life of the app.",
                    nodes = listOf(
                        DNode("r1", "Request", 0.16f, 0.14f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r2", "Request", 0.16f, 0.4f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r3", "Request", 0.16f, 0.66f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("mux", "Connection Multiplexer", 0.52f, 0.4f, w = 0.34f, h = 0.24f, sub = "just one"),
                        DNode("redis", "Redis", 0.86f, 0.4f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("bad", "One per request: out of connections", 0.5f, 0.9f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("r1", "mux"),
                        DEdge("r2", "mux"),
                        DEdge("r3", "mux"),
                        DEdge("mux", "redis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("r1", "r2", "r3", "redis"), light = setOf("r1", "r2", "r3"), card = BoardCard("The clue", "Connected clients: a handful, then thousands at seven.")),
                    Beat(14, show = setOf("mux"), light = setOf("mux"), pulse = "mux" to "redis", card = BoardCard("ConnectionMultiplexer", "The phone line to the cache. Moved inside the booking handler.")),
                    Beat(16, show = setOf("bad"), light = setOf("bad", "mux"), pulse = "r2" to "mux", card = BoardCard("C#", "services.AddSingleton(ConnectionMultiplexer.Connect(conn));", code = true)),
                ),
            ),
            // 18: "Not yet. There's a second witness..."
            Scene(
                18, "The second witness",
                DiagramSpec(
                    "Cache-aside looks in the cache, reads the database on a miss and saves the value with a TTL; per-user data needs the user in the key.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.18f, 0.46f, w = 0.3f, h = 0.22f),
                        DNode("redis", "Redis", 0.76f, 0.18f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "TTL 10 min"),
                        DNode("db", "Database", 0.76f, 0.6f, w = 0.32f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("key", "my-bookings: one shared key", 0.5f, 0.9f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "redis", "1 look"),
                        DEdge("app", "db", "2 miss: read"),
                        DEdge("db", "redis", "3 save", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("app"), card = BoardCard("The second witness", "A member opened My Bookings and saw a stranger's classes.")),
                    Beat(20, show = setOf("redis", "db"), light = setOf("redis"), pulse = "app" to "db", card = BoardCard("Cache-aside", "Look in the cache first. On a miss, read the database and save it.")),
                    Beat(22, show = setOf("key"), light = setOf("key"), card = BoardCard("Put the user in the key", "Never one member's data under a shared key.")),
                    Beat(24, light = setOf("redis"), pulse = "app" to "redis", card = BoardCard("Invalidate on write", "A booking changes: delete its key. The TTL is the safety net.")),
                ),
            ),
            // 25: "And where does this stop working?..."
            Scene(
                25, "Not a database",
                DiagramSpec(
                    "Anything in Redis can vanish, so the slow path to the real store stays; big files for far-away users belong to Front Door.",
                    nodes = listOf(
                        DNode("redis", "Redis", 0.24f, 0.26f, w = 0.36f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "can vanish"),
                        DNode("db", "Cosmos DB", 0.76f, 0.26f, w = 0.36f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "the real store"),
                        DNode("fd", "Big files far away", 0.5f, 0.8f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE, sub = "Front Door and CDN"),
                    ),
                    edges = listOf(
                        DEdge("db", "redis", "slow path"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("redis")),
                    Beat(26, show = setOf("db", "fd"), light = setOf("db"), pulse = "db" to "redis", card = BoardCard("Redis is not a database", "Anything can disappear. Always keep the slow path.")),
                ),
            ),
            // 27: "The gym also wanted a monthly leaderboard..."
            Scene(
                27, "What you can store",
                DiagramSpec(
                    "Strings, counters, hashes, lists and sorted sets each fit a pattern: values, limits, objects, queues, leaderboards.",
                    nodes = listOf(
                        DNode("str", "String", 0.26f, 0.13f, w = 0.46f, h = 0.18f, tone = 1, sub = "a value with expiry"),
                        DNode("cnt", "Counter", 0.76f, 0.13f, w = 0.44f, h = 0.18f, tone = 1, sub = "rate limiting"),
                        DNode("hash", "Hash", 0.26f, 0.43f, w = 0.46f, h = 0.18f, tone = 1, sub = "an object's fields"),
                        DNode("list", "List", 0.76f, 0.43f, w = 0.44f, h = 0.18f, tone = 1, sub = "a simple queue"),
                        DNode("zset", "Sorted set", 0.5f, 0.8f, w = 0.6f, h = 0.24f, sub = "members in order by score"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("str", "cnt", "hash", "list", "zset"), light = setOf("zset"), card = BoardCard("The gym's leaderboard", "Members ranked by visits as the score: the top ten comes back fast.")),
                ),
            ),
            // 29: "And if they someday need the cache to survive a restart..."
            Scene(
                29, "Tiers",
                DiagramSpec(
                    "Basic for testing, Standard for production, Premium for disk and private networks, Enterprise for modules.",
                    nodes = listOf(
                        DNode("basic", "Basic", 0.26f, 0.16f, w = 0.46f, h = 0.22f, tone = 2, sub = "no SLA: testing"),
                        DNode("std", "Standard", 0.76f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "two copies: production"),
                        DNode("prem", "Premium", 0.26f, 0.52f, w = 0.46f, h = 0.22f, sub = "disk, private network"),
                        DNode("ent", "Enterprise", 0.76f, 0.52f, w = 0.44f, h = 0.22f, sub = "modules, multi-region"),
                        DNode("kv", "Keys: managed identity", 0.5f, 0.86f, w = 0.66f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "or Key Vault"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("basic", "std", "prem", "ent", "kv"), light = setOf("prem", "ent"), card = BoardCard("Disk or private network", "Premium at least. RediSearch or writes in many regions: Enterprise.")),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed",
                DiagramSpec(
                    "The exam's Redis questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("conn", "How many connections", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one, shared"),
                        DNode("board", "Leaderboard", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sorted set"),
                        DNode("aside", "Cache first, fill on miss", 0.5f, 0.45f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "cache-aside, TTL"),
                        DNode("prem", "Disk or private network", 0.26f, 0.82f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium"),
                        DNode("basic", "Production", 0.76f, 0.82f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "never Basic"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("conn", "board", "aside", "prem", "basic"), light = setOf("conn"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Today", "SET greeting hello EX 60, GET it, wait, GET again; then ZADD and ZREVRANGE.", code = true)),
                ),
            ),
        ),
    )

    private fun redisGossipBoard() = PodcastBoard(
        "az-redis--gossip",
        listOf(
            // 0: "Did you hear what Sam did to the plant app last week?..."
            Scene(
                0, "What Sam did",
                DiagramSpec(
                    "Sprout's Android app calls its backend, which caches the watering schedules in Azure Cache for Redis.",
                    nodes = listOf(
                        DNode("phone", "Sprout app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("app", "Backend", 0.22f, 0.7f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("redis", "Redis", 0.76f, 0.7f, w = 0.36f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "in memory"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app"),
                        DEdge("app", "redis", "under 1 ms"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone")),
                    Beat(2, show = setOf("app", "redis"), light = setOf("redis"), pulse = "phone" to "app", card = BoardCard("Sprout", "Watering schedules cached in Redis, so the home screen opens fast.")),
                    Beat(4, light = setOf("redis"), pulse = "app" to "redis", card = BoardCard("Redis", "A notepad in memory: save a value under a key, get it back by name.")),
                    Beat(6, card = BoardCard("Under the notepad", "A server keeping all its data in memory. GET and SET over the network.")),
                ),
            ),
            // 7: "Okay. So what did Sam do?"
            Scene(
                7, "One phone line",
                DiagramSpec(
                    "Every request shares one ConnectionMultiplexer, created once for the life of the app.",
                    nodes = listOf(
                        DNode("r1", "Request", 0.16f, 0.14f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r2", "Request", 0.16f, 0.4f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("r3", "Request", 0.16f, 0.66f, w = 0.28f, h = 0.14f, tone = 1),
                        DNode("mux", "Connection Multiplexer", 0.52f, 0.4f, w = 0.34f, h = 0.24f, sub = "just one"),
                        DNode("redis", "Redis", 0.86f, 0.4f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("bad", "One per request: out of connections", 0.5f, 0.9f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("r1", "mux"),
                        DEdge("r2", "mux"),
                        DEdge("r3", "mux"),
                        DEdge("mux", "redis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("r1", "r2", "r3", "mux", "redis"), light = setOf("mux"), card = BoardCard("ConnectionMultiplexer", "The phone line to the cache. Sam made a new one in every request.")),
                    Beat(10, show = setOf("bad"), light = setOf("bad"), pulse = "r1" to "mux", card = BoardCard("Sunday evening", "Thousands of new lines at once: the cache ran out of connections.")),
                    Beat(12, light = setOf("mux"), pulse = "mux" to "redis", card = BoardCard("C#", "services.AddSingleton(ConnectionMultiplexer.Connect(conn));", code = true)),
                ),
            ),
            // 14: "Oh no. Week two was Aisha..."
            Scene(
                14, "Week two: Aisha",
                DiagramSpec(
                    "When memory fills, the eviction policy drops keys that have a TTL first, so keys without one push the others out.",
                    nodes = listOf(
                        DNode("redis", "Redis: full", 0.24f, 0.26f, w = 0.4f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 2),
                        DNode("evict", "Eviction policy", 0.74f, 0.26f, w = 0.44f, h = 0.22f, sub = "volatile LRU"),
                        DNode("photos", "Aisha's keys: no TTL", 0.5f, 0.8f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("redis", "evict", "drops keys"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("redis", "photos"), light = setOf("photos")),
                    Beat(16, show = setOf("evict"), light = setOf("evict"), pulse = "redis" to "evict", card = BoardCard("TTL: time to live", "A best-before date on a key. Then the next read gets a fresh copy.")),
                    Beat(18, light = setOf("evict"), card = BoardCard("Volatile LRU", "Keys with a TTL go first, so the schedules went. An expiry on every key.")),
                ),
            ),
            // 20: "Ben. Ben added a My Plants screen, using cache-aside..."
            Scene(
                20, "Week three: Ben",
                DiagramSpec(
                    "Cache-aside looks in the cache, reads the database on a miss and saves the value with a TTL; per-user data needs the user in the key.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.18f, 0.46f, w = 0.3f, h = 0.22f),
                        DNode("redis", "Redis", 0.76f, 0.18f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "TTL 10 min"),
                        DNode("db", "Database", 0.76f, 0.6f, w = 0.32f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("key", "my-plants: one shared key", 0.5f, 0.9f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "redis", "1 look"),
                        DEdge("app", "db", "2 miss: read"),
                        DEdge("db", "redis", "3 save", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("app", "redis", "db"), light = setOf("redis"), pulse = "app" to "db", card = BoardCard("Cache-aside", "Look in the cache first. On a miss, read the database and save it.")),
                    Beat(22, show = setOf("key"), light = setOf("key"), pulse = "app" to "redis", card = BoardCard("Plants, then the user's ID", "The user goes in the key. And delete the key on write.")),
                ),
            ),
            // 23: "And the intern? There's always an intern."
            Scene(
                23, "The intern's tier",
                DiagramSpec(
                    "Basic for testing, Standard for production, Premium for disk and private networks, Enterprise for modules.",
                    nodes = listOf(
                        DNode("basic", "Basic", 0.26f, 0.16f, w = 0.46f, h = 0.22f, tone = 2, sub = "no SLA: testing"),
                        DNode("std", "Standard", 0.76f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "two copies: production"),
                        DNode("prem", "Premium", 0.26f, 0.52f, w = 0.46f, h = 0.22f, sub = "disk, private network"),
                        DNode("ent", "Enterprise", 0.76f, 0.52f, w = 0.44f, h = 0.22f, sub = "modules, multi-region"),
                        DNode("slow", "Slow path to Cosmos DB", 0.5f, 0.86f, w = 0.66f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("basic"), light = setOf("basic"), card = BoardCard("Basic in production", "One node, no SLA. A restart emptied the whole cache.")),
                    Beat(26, show = setOf("std", "slow"), light = setOf("std", "slow"), card = BoardCard("Redis is not a database", "The slow path kept the app up. Standard for production.")),
                    Beat(28, show = setOf("prem", "ent"), light = setOf("prem", "ent"), card = BoardCard("Survive a restart?", "Premium: saving to disk, private networks. Enterprise: modules.")),
                ),
            ),
            // 29: "Did anything good happen this month?"
            Scene(
                29, "Priya's leaderboard",
                DiagramSpec(
                    "Strings, counters, hashes, lists and sorted sets each fit a pattern: values, limits, objects, queues, leaderboards.",
                    nodes = listOf(
                        DNode("str", "String", 0.26f, 0.13f, w = 0.46f, h = 0.18f, tone = 1, sub = "a value with expiry"),
                        DNode("cnt", "Counter", 0.76f, 0.13f, w = 0.44f, h = 0.18f, tone = 1, sub = "rate limiting"),
                        DNode("hash", "Hash", 0.26f, 0.43f, w = 0.46f, h = 0.18f, tone = 1, sub = "an object's fields"),
                        DNode("list", "List", 0.76f, 0.43f, w = 0.44f, h = 0.18f, tone = 1, sub = "a simple queue"),
                        DNode("zset", "Sorted set", 0.5f, 0.8f, w = 0.6f, h = 0.24f, sub = "members in order by score"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("str", "cnt", "hash", "list", "zset"), light = setOf("zset"), card = BoardCard("A watering streak leaderboard", "Users ranked by streak as the score. The top ten, instantly.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's Redis questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("conn", "How many connections", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one, shared"),
                        DNode("board", "Leaderboard", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sorted set"),
                        DNode("aside", "Cache first, fill on miss", 0.5f, 0.45f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "cache-aside, TTL"),
                        DNode("prem", "Disk or private network", 0.26f, 0.82f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium"),
                        DNode("basic", "Production", 0.76f, 0.82f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "never Basic"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("conn", "board", "aside", "prem", "basic"), light = setOf("conn"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Today", "SET greeting hello EX 60, GET it, wait, GET again; then ZADD and ZREVRANGE.", code = true)),
                ),
            ),
        ),
    )

    private fun cdnCaseBoard() = PodcastBoard(
        "az-cdn--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Borrowed Bike..."
            Scene(
                0, "The Borrowed Bike",
                DiagramSpec(
                    "The bike app's requests go through Front Door to the backend, and a rider sees a stranger's rides.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("err", "A stranger's rides", 0.76f, 0.2f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("fd", "Front Door", 0.24f, 0.66f, w = 0.4f, h = 0.26f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("api", "Backend API", 0.78f, 0.66f, w = 0.34f, h = 0.2f, sub = "App Service"),
                    ),
                    edges = listOf(
                        DEdge("phone", "fd", "My Rides"),
                        DEdge("fd", "api"),
                        DEdge("fd", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "fd", "api", "err"), light = setOf("err"), pulse = "phone" to "fd", card = BoardCard("The case", "Front Door goes in. A rider opens My Rides and sees a stranger's.")),
                    Beat(2, light = setOf("fd"), card = BoardCard("Front Door", "Azure's CDN and global front entrance: copies near your users.")),
                ),
            ),
            // 3: "And the factory?"
            Scene(
                3, "Edges and the origin",
                DiagramSpec(
                    "Users hit the nearest edge. A miss goes to the origin once; after that the edge serves hits.",
                    nodes = listOf(
                        DNode("u1", "Doha", 0.12f, 0.25f, w = 0.2f, h = 0.18f, kind = NodeKind.USER, tone = 1),
                        DNode("u2", "Lahore", 0.12f, 0.75f, w = 0.2f, h = 0.18f, kind = NodeKind.USER, tone = 1),
                        DNode("e1", "Edge", 0.45f, 0.25f, w = 0.3f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("e2", "Edge", 0.45f, 0.75f, w = 0.3f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("origin", "Origin", 0.84f, 0.5f, w = 0.28f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "your real files"),
                    ),
                    edges = listOf(
                        DEdge("u1", "e1"),
                        DEdge("u2", "e2"),
                        DEdge("e1", "origin", "miss"),
                        DEdge("e2", "origin", "miss"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("u1", "u2", "e1", "e2"), light = setOf("e1", "e2"), card = BoardCard("Edges", "Points of presence: servers close to your users, holding copies.")),
                    Beat(4, show = setOf("origin"), light = setOf("origin"), pulse = "e1" to "origin", card = BoardCard("Origin, miss and hit", "A miss fetches from the origin once. Everyone after gets a hit.")),
                    Beat(6, light = setOf("e1", "e2"), card = BoardCard("Under the picture", "Microsoft servers between users and origin, each cache filed by web address.")),
                    Beat(8, light = setOf("u2", "e2"), pulse = "u2" to "e2", card = BoardCard("The nearest edge", "Addresses announced from every edge: routing finds the closest.")),
                ),
            ),
            // 9: "Back to the crime. My first suspect: the database mixed up two riders."
            Scene(
                9, "The suspects",
                DiagramSpec(
                    "The backend saw one request only; the edge answered the second rider from its own copy of the API.",
                    nodes = listOf(
                        DNode("db", "Database mix-up?", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "one request only"),
                        DNode("hit", "X-Cache: a hit", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the edge answered"),
                        DNode("route", "API route: caching on", 0.5f, 0.74f, w = 0.66f, h = 0.22f, tone = 2, sub = "My Rides kept at the edge"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("db"), light = setOf("db")),
                    Beat(10, card = BoardCard("Suspect one: cleared", "The second rider's request never reached the backend.")),
                    Beat(12, show = setOf("hit"), light = setOf("hit"), card = BoardCard("The clue", "The X-Cache header says hit: the edge answered from its copy.")),
                    Beat(14, show = setOf("route"), light = setOf("route"), card = BoardCard("Caching on every route", "The API too. My Rides is one address for every rider.")),
                ),
            ),
            // 15: "So what's the fix?"
            Scene(
                15, "Three caching rules",
                DiagramSpec(
                    "The origin's Cache-Control header sets the TTL, unless a caching rule overrides it; personal answers bypass the cache.",
                    nodes = listOf(
                        DNode("origin", "Origin", 0.18f, 0.22f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("edge", "Edge", 0.8f, 0.22f, w = 0.3f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("cc", "Cache-Control", 0.5f, 0.5f, w = 0.4f, h = 0.14f, kind = NodeKind.NOTE, sub = "the best-before date"),
                        DNode("honor", "Honor origin", 0.17f, 0.84f, w = 0.3f, kind = NodeKind.PILL, sub = "default"),
                        DNode("over", "Override", 0.5f, 0.84f, w = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "your own TTL"),
                        DNode("bypass", "Bypass cache", 0.83f, 0.84f, w = 0.3f, kind = NodeKind.PILL, tone = 2, sub = "never keep"),
                    ),
                    edges = listOf(
                        DEdge("origin", "edge", "file + label"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("origin", "edge")),
                    Beat(18, show = setOf("honor", "over", "bypass"), light = setOf("bypass"), card = BoardCard("Bypass cache", "Personal answers, like a rider's rides or bill, never at the edge.")),
                    Beat(20, show = setOf("cc"), light = setOf("cc"), pulse = "origin" to "edge", card = BoardCard("TTL: time to live", "How long an edge keeps its copy. Set by Cache-Control or a rule.")),
                ),
            ),
            // 21: "Case closed?"
            Scene(
                21, "The map that stayed old",
                DiagramSpec(
                    "Purge removes the cached copy before its TTL; a versioned name gives a new URL that cannot be stale.",
                    nodes = listOf(
                        DNode("old", "Old copy", 0.5f, 0.15f, w = 0.5f, h = 0.2f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("purge", "Purge", 0.22f, 0.55f, w = 0.36f, h = 0.2f, sub = "emergency, per path"),
                        DNode("hash", "New file name", 0.78f, 0.55f, w = 0.36f, h = 0.2f, sub = "content hash"),
                        DNode("ttl1", "TTL of one second: no", 0.5f, 0.88f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("purge", "old", "throws away"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("old"), light = setOf("old"), card = BoardCard("Second clue", "A new bike map; riders still see the old one. Purged five times.")),
                    Beat(23, show = setOf("purge"), light = setOf("purge"), pulse = "purge" to "old"),
                    Beat(24, show = setOf("hash"), light = setOf("hash"), card = BoardCard("Purge, or a versioned name", "Purge: minutes, per path, every edge. A new name can't be stale.")),
                    Beat(25, show = setOf("ttl1"), light = setOf("ttl1"), card = BoardCard("A one-second TTL?", "Back to the origin almost every time: no point having a CDN.")),
                ),
            ),
            // 26: "Then the edge goes back to the origin almost every time, and you've th..."
            Scene(
                26, "The wrong origin address",
                DiagramSpec(
                    "Front Door's origin must be the storage account's static website address, which serves index pages, not the blob address.",
                    nodes = listOf(
                        DNode("fd", "Front Door", 0.5f, 0.16f, w = 0.4f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("blob", "Blob address", 0.24f, 0.54f, w = 0.42f, h = 0.2f, tone = 2, sub = "no index pages"),
                        DNode("web", "Static website", 0.76f, 0.54f, w = 0.42f, h = 0.2f, tone = 1, sub = "serves index pages"),
                        DNode("sa", "Storage account", 0.5f, 0.86f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("fd", "blob", "was"),
                        DEdge("fd", "web", "should be", dashed = true),
                        DEdge("blob", "sa"),
                        DEdge("web", "sa"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("fd", "sa"), card = BoardCard("Third clue", "The help site's home page fails through Front Door.")),
                    Beat(28, show = setOf("blob", "web"), light = setOf("web"), pulse = "fd" to "web", card = BoardCard("The static website address", "It serves index pages. The Blob Storage episode covers both.")),
                ),
            ),
            // 29: "Any last clue?"
            Scene(
                29, "Query strings",
                DiagramSpec(
                    "Query string caching decides whether different URL parameters make separate cached copies.",
                    nodes = listOf(
                        DNode("small", "map?size=small", 0.25f, 0.2f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("large", "map?size=large", 0.75f, 0.2f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("use", "Use: two copies", 0.5f, 0.5f, w = 0.46f, h = 0.18f),
                        DNode("ignore", "Ignore: one copy", 0.5f, 0.84f, w = 0.46f, h = 0.18f, tone = 2, sub = "for tracking tags"),
                    ),
                    edges = listOf(
                        DEdge("small", "use"),
                        DEdge("large", "use"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("small", "large", "use", "ignore"), light = setOf("ignore"), card = BoardCard("A tracking tag", "Never changes the file: ignore it. A map's size really differs: use.")),
                ),
            ),
            // 31: "So where does Front Door stop being the answer?"
            Scene(
                31, "Where it stops",
                DiagramSpec(
                    "Front Door keeps copies of responses that are the same for everyone; per-user data the backend reads belongs in Redis.",
                    nodes = listOf(
                        DNode("fd", "Front Door", 0.26f, 0.22f, w = 0.42f, h = 0.22f, sub = "same for everyone"),
                        DNode("redis", "Redis cache", 0.76f, 0.22f, w = 0.38f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "backend data"),
                        DNode("prem", "Premium", 0.5f, 0.76f, w = 0.6f, h = 0.22f, tone = 1, sub = "managed WAF, Private Link"),
                    ),
                    edges = listOf(
                        DEdge("fd", "prem"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("fd"), light = setOf("fd")),
                    Beat(32, show = setOf("redis", "prem"), light = setOf("redis", "prem"), card = BoardCard("Not for personal data", "Data the backend reads often: the Redis cache episode.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed",
                DiagramSpec(
                    "The exam's caching questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("stale", "Users see the old file", 0.26f, 0.15f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "purge, versioned names"),
                        DNode("diff", "Different content per user", 0.76f, 0.15f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "bypass cache"),
                        DNode("ttl", "What sets the TTL", 0.26f, 0.5f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "Cache-Control or a rule"),
                        DNode("fd", "Current CDN", 0.76f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "Front Door"),
                        DNode("prem", "Managed WAF, Private Link", 0.5f, 0.85f, w = 0.7f, h = 0.2f, sub = "Front Door Premium"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("stale", "diff", "ttl", "fd", "prem"), light = setOf("stale", "diff"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, card = BoardCard("Today", "Static website, Front Door profile, a miss then a hit, then purge.")),
                ),
            ),
        ),
    )

    private fun cdnGossipBoard() = PodcastBoard(
        "az-cdn--gossip",
        listOf(
            // 0: "Did you hear what Dana did to the habit tracker?..."
            Scene(
                0, "What Dana did",
                DiagramSpec(
                    "Streakly's Android app and help site sit behind Front Door, with the backend API behind it too.",
                    nodes = listOf(
                        DNode("phone", "Streakly app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("help", "Help site", 0.78f, 0.2f, w = 0.32f, h = 0.18f),
                        DNode("fd", "Front Door", 0.5f, 0.56f, w = 0.4f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("api", "Backend API", 0.5f, 0.88f, w = 0.4f, h = 0.18f, sub = "App Service"),
                    ),
                    edges = listOf(
                        DEdge("phone", "fd"),
                        DEdge("fd", "help"),
                        DEdge("fd", "api"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone")),
                    Beat(2, show = setOf("fd", "help", "api"), light = setOf("fd"), pulse = "phone" to "fd", card = BoardCard("Streakly", "An Android habit tracker: an API, a help site, Front Door in front.")),
                ),
            ),
            // 3: "Front Door, in plain words?"
            Scene(
                3, "Corner shops",
                DiagramSpec(
                    "Users hit the nearest edge. A miss goes to the origin once; after that the edge serves hits.",
                    nodes = listOf(
                        DNode("u1", "Doha", 0.12f, 0.25f, w = 0.2f, h = 0.18f, kind = NodeKind.USER, tone = 1),
                        DNode("u2", "Lahore", 0.12f, 0.75f, w = 0.2f, h = 0.18f, kind = NodeKind.USER, tone = 1),
                        DNode("e1", "Edge", 0.45f, 0.25f, w = 0.3f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("e2", "Edge", 0.45f, 0.75f, w = 0.3f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("origin", "Origin", 0.84f, 0.5f, w = 0.28f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "your real files"),
                    ),
                    edges = listOf(
                        DEdge("u1", "e1"),
                        DEdge("u2", "e2"),
                        DEdge("e1", "origin", "miss"),
                        DEdge("e2", "origin", "miss"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("u1", "u2", "e1", "e2"), light = setOf("e1", "e2"), card = BoardCard("CDN: corner shops", "Edges, or points of presence: copies close to your users.")),
                    Beat(6, show = setOf("origin"), light = setOf("origin"), pulse = "e1" to "origin", card = BoardCard("Origin, miss and hit", "A miss fetches from the origin once. Everyone after gets a hit.")),
                    Beat(8, light = setOf("u1", "e1"), pulse = "u1" to "e1", card = BoardCard("The nearest edge", "Routing finds the closest edge; it looks the address up in its cache.")),
                ),
            ),
            // 9: "So what did Dana do?"
            Scene(
                9, "Caching on every route",
                DiagramSpec(
                    "Shared pages are cached at the edge; personal screens bypass the cache so nobody sees another user's data.",
                    nodes = listOf(
                        DNode("pages", "Help pages", 0.25f, 0.25f, w = 0.4f, h = 0.2f, sub = "same for everyone"),
                        DNode("habits", "My Habits", 0.75f, 0.25f, w = 0.4f, h = 0.2f, tone = 1, sub = "different per user"),
                        DNode("cache", "Cache it", 0.25f, 0.7f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("bypass", "Bypass cache", 0.75f, 0.7f, w = 0.34f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("pages", "cache"),
                        DEdge("habits", "bypass"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("pages", "habits", "cache"), light = setOf("habits"), card = BoardCard("Dana's week", "Caching on every route: one user's habits served to the next.")),
                    Beat(14, show = setOf("bypass"), light = setOf("habits", "bypass"), pulse = "habits" to "bypass", card = BoardCard("Bypass cache", "Marcus gave the API its own route. Anything personal skips the edge.")),
                ),
            ),
            // 15: "TTL I know from Redis. Time to live?"
            Scene(
                15, "How long a copy lasts",
                DiagramSpec(
                    "The origin's Cache-Control header sets the TTL, unless a caching rule overrides it.",
                    nodes = listOf(
                        DNode("origin", "Origin", 0.18f, 0.22f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("edge", "Edge", 0.8f, 0.22f, w = 0.3f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("cc", "Cache-Control", 0.5f, 0.5f, w = 0.4f, h = 0.14f, kind = NodeKind.NOTE, sub = "the best-before date"),
                        DNode("honor", "Honor origin", 0.17f, 0.84f, w = 0.3f, kind = NodeKind.PILL, sub = "default"),
                        DNode("over", "Override", 0.5f, 0.84f, w = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "your own TTL"),
                        DNode("bypass", "Bypass cache", 0.83f, 0.84f, w = 0.3f, kind = NodeKind.PILL, tone = 2, sub = "never keep"),
                    ),
                    edges = listOf(
                        DEdge("origin", "edge", "file + label"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("origin", "edge")),
                    Beat(16, show = setOf("cc", "honor", "over", "bypass"), light = setOf("cc"), pulse = "origin" to "edge", card = BoardCard("TTL: time to live", "How long an edge keeps its copy. Set by Cache-Control or a rule.")),
                ),
            ),
            // 17: "Okay. Week two?"
            Scene(
                17, "Week two: Tom",
                DiagramSpec(
                    "Purge removes the cached copy before its TTL; a versioned name gives a new URL that cannot be stale.",
                    nodes = listOf(
                        DNode("old", "Old copy", 0.5f, 0.15f, w = 0.5f, h = 0.2f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("purge", "Purge", 0.22f, 0.55f, w = 0.36f, h = 0.2f, sub = "emergency, per path"),
                        DNode("hash", "New file name", 0.78f, 0.55f, w = 0.36f, h = 0.2f, sub = "content hash"),
                        DNode("ttl1", "TTL of one second: no", 0.5f, 0.88f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("purge", "old", "throws away"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("old"), light = setOf("old"), card = BoardCard("Seven purges", "Tom's new logo; users still saw the old one.")),
                    Beat(20, show = setOf("purge", "hash"), light = setOf("hash"), pulse = "purge" to "old", card = BoardCard("Purge, or a versioned name", "Purge: minutes, per path, every edge. A content hash: a new URL.")),
                    Beat(22, show = setOf("ttl1"), light = setOf("ttl1"), card = BoardCard("A one-second TTL?", "Back to the origin almost every time: no point having a CDN.")),
                ),
            ),
            // 23: "Priya? She never makes mistakes."
            Scene(
                23, "Week three: Priya",
                DiagramSpec(
                    "Front Door's origin must be the storage account's static website address, which serves index pages, not the blob address.",
                    nodes = listOf(
                        DNode("fd", "Front Door", 0.5f, 0.16f, w = 0.4f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("blob", "Blob address", 0.24f, 0.54f, w = 0.42f, h = 0.2f, tone = 2, sub = "no index pages"),
                        DNode("web", "Static website", 0.76f, 0.54f, w = 0.42f, h = 0.2f, tone = 1, sub = "serves index pages"),
                        DNode("sa", "Storage account", 0.5f, 0.86f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("fd", "blob", "was"),
                        DEdge("fd", "web", "should be", dashed = true),
                        DEdge("blob", "sa"),
                        DEdge("web", "sa"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("fd", "sa")),
                    Beat(24, show = setOf("blob", "web"), light = setOf("blob", "web"), pulse = "fd" to "web", card = BoardCard("The static website address", "It serves index pages. The Blob Storage episode covers both.")),
                ),
            ),
            // 25: "And the intern? I sense an intern."
            Scene(
                25, "The intern's tags",
                DiagramSpec(
                    "Query string caching decides whether different URL parameters make separate cached copies.",
                    nodes = listOf(
                        DNode("small", "chart?size=small", 0.25f, 0.2f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("large", "chart?size=large", 0.75f, 0.2f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("use", "Use: two copies", 0.5f, 0.5f, w = 0.46f, h = 0.18f),
                        DNode("ignore", "Ignore: one copy", 0.5f, 0.84f, w = 0.46f, h = 0.18f, tone = 2, sub = "for tracking tags"),
                    ),
                    edges = listOf(
                        DEdge("small", "use"),
                        DEdge("large", "use"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("small", "large"), card = BoardCard("Query string", "The bit after the question mark in a web address.")),
                    Beat(28, show = setOf("use", "ignore"), light = setOf("ignore"), card = BoardCard("A tracking tag", "Never changes the file: ignore it. A chart's size really differs: use.")),
                ),
            ),
            // 29: "Is Front Door the answer to every slow thing, then?"
            Scene(
                29, "Where it stops",
                DiagramSpec(
                    "Front Door keeps copies of responses that are the same for everyone; per-user data the backend reads belongs in Redis.",
                    nodes = listOf(
                        DNode("fd", "Front Door", 0.26f, 0.22f, w = 0.42f, h = 0.22f, sub = "same for everyone"),
                        DNode("redis", "Redis cache", 0.76f, 0.22f, w = 0.38f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "backend data"),
                        DNode("prem", "Premium", 0.5f, 0.76f, w = 0.6f, h = 0.22f, tone = 1, sub = "managed WAF, Private Link"),
                    ),
                    edges = listOf(
                        DEdge("fd", "prem"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("fd", "redis", "prem"), light = setOf("redis"), card = BoardCard("Not for personal data", "Data the backend reads often: the Redis cache episode.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's caching questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("stale", "Users see the old file", 0.26f, 0.15f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "purge, versioned names"),
                        DNode("diff", "Different content per user", 0.76f, 0.15f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "bypass cache"),
                        DNode("ttl", "What sets the TTL", 0.26f, 0.5f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "Cache-Control or a rule"),
                        DNode("fd", "Current CDN", 0.76f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "Front Door"),
                        DNode("prem", "Managed WAF, Private Link", 0.5f, 0.85f, w = 0.7f, h = 0.2f, sub = "Front Door Premium"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("stale", "diff", "ttl", "fd", "prem"), light = setOf("stale", "diff"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Today", "Static website, Front Door profile, a miss then a hit, then purge.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(redisCaseBoard(), redisGossipBoard(), cdnCaseBoard(), cdnGossipBoard()) }
}
