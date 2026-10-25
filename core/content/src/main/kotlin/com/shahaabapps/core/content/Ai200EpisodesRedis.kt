package com.shahaabapps.core.content

/** Azure Managed Redis: caching and vector search: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesRedis {
    private val classic = Podcast(
        "ai-redis",
        listOf(
            maya("Hi, and welcome back. Zaiqa's orders sit in Azure SQL, the order events and rider pings in Cosmos DB, the payouts ledger in PostgreSQL. Our API, the .NET service the phone talks to, runs in Container Apps. Today we add a fourth store."),
            leo("A fourth one? What is left to keep?"),
            maya("What is true right now, read thousands of times a second. SQL holds what must be correct, Cosmos what must be fast and huge. Two things today: the hot restaurant list on the home screen, and the rider's current position, the dot on the tracking map."),
            leo("The list comes out of SQL, surely. It is only restaurants."),
            maya("It does, and that query is expensive. It joins restaurants, orders and ratings for the last hour, then ranks them. Two hundred milliseconds every time, and the same answer for everybody for the next minute."),
            leo("And at eight on a Friday, everybody opens the app at once."),
            maya("Thirty thousand phones a minute asking Azure SQL the same question. So we keep the answer somewhere much faster. That place is Azure Managed Redis."),
            leo("Which is what, in plain words?"),
            maya("An in-memory store. Values live in the machine's working memory rather than on a disk, so a read comes back in well under a millisecond. You store a value under a name, called a key, and ask for it back by name."),
            leo("Like a HashMap in my Kotlin code, but one the whole backend shares?"),
            maya("That is the picture. Azure Managed Redis is Microsoft's own, built on Redis Enterprise, and where Microsoft points new work. It listens on port 10000, with encryption always on. The older Azure Cache for Redis answers on 6380, and the exam tells them apart by exactly that, and by the command line: az redisenterprise rather than az redis."),
            leo("So how does the API use it for the hot list?"),
            maya("With the cache-aside pattern. The API asks Redis first; if the list is there, that is a hit and it goes straight back to the phone. On a miss, and this is where the name comes from, your own code runs the SQL query, stores the answer in Redis, and returns it. The cache only sits aside."),
            leo("For how long?"),
            maya("You decide, with a TTL, which stands for time to live: an expiry on the key, like a use-by date on milk. When it passes, Redis throws the copy away. For example, the hot list gets sixty seconds."),
            leo("Sixty seconds? Why not an hour?"),
            maya("Because that list is about right now. A restaurant that stops taking orders at ten past eight should be off the screen by nine past. Picture a menu page: that barely changes, so an hour is fine."),
            leo("And when one closes, do I wait the minute out?"),
            maya("No, you take the copy away yourself. That is invalidation. And the order matters: update Azure SQL first, then delete the cached key. The next read finds nothing and rebuilds it."),
            leo("Delete first sounds tidier. Why is it wrong?"),
            maya("Because a reader slips in between. You delete the key, a reader misses, reads a database that still says open, and writes the old list back into Redis. Then your update lands, and the cache holds yesterday."),
            leo("[chuckle] And the TTL saves me from my own code."),
            maya("That is what it is for. One day a code path forgets. With no expiry that stale copy lives forever; with one, it is wrong for sixty seconds at most."),
            leo("Right. Now the rider's position. Why is that not in Cosmos with the pings?"),
            maya("The history is in Cosmos and stays there. But the tracking screen wants one value: where rider four-two-one is now, overwritten every three seconds. Reading the newest ping would mean paying a database every time for a number stale three seconds later. In Redis it is one key, one write, one very cheap read."),
            leo("And the sceptical question. What happens when Redis falls over?"),
            maya("Say it with me: losing the cache must never lose data. [pause] If it vanishes, the hot list rebuilds from Azure SQL on the next request, the ping history is still in Cosmos, and the dot is three seconds behind."),
            leo("And nothing may live only in Redis."),
            maya("Nothing. Put something there that exists nowhere else and you have made a database out of a cache, and a cache is allowed to disappear."),
            leo("Understood. Now, what happens when the memory fills up?"),
            maya("One setting decides: the eviction policy, which says which keys Redis throws out to make room. [pause] Broadly three choices. Whatever was used least recently, across every key. Only keys that already carry an expiry. Or nothing at all, which is called no-eviction."),
            leo("And no-eviction means?"),
            maya("Writes start failing with an out-of-memory error, while reads carry on perfectly. That catches people, because one of the add-ons requires it."),
            leo("Which add-on?"),
            maya("RediSearch, the one that finds an answer by meaning rather than by exact key. Zaiqa's Taste service will use it for a semantic cache later. You tick it when you create the cache: add-ons cannot be added afterwards."),
            leo("And with no-eviction forced on, I must be careful."),
            maya("You do. Every key gets a TTL, and you watch how full the memory is. For example, a slow drip of keys with no expiry fills it over a quiet fortnight, and then Friday's writes are refused."),
            leo("Can I simply make it bigger?"),
            maya("Two ways. A bigger tier: Memory Optimized when you hold a lot, Compute Optimized when you make a lot of calls. Or scale out over shards, a shard being one slice of the cache with its own memory."),
            leo("Does my code have to know which shard a key is on?"),
            maya("No. With the Enterprise clustering policy you talk to one address and Managed Redis routes it. The catch: one command touching several keys only works when those keys land on the same shard."),
            leo("Fair. Now a different use. You said rate limiting."),
            maya("A counter, one key per diner per minute. The API adds one on every request and sets a sixty-second expiry the first time. Past two hundred it answers 429, which means too many requests. And not a counter in the API's own memory: twenty copies run at dinner rush, each counting alone, so your limit quietly becomes four thousand."),
            leo("Good. Last thing: how does the API get in?"),
            maya("Not with an access key. A new Managed Redis cache has Microsoft Entra turned on by default, which is Microsoft's sign-in service, so the API signs in with its managed identity, the badge Azure gives your app."),
            leo("And that is enough?"),
            maya("Almost. One step everybody misses: on the cache's Authentication page you add that identity as a Redis user. A role on the Azure resource alone does not let it run commands. Badge in hand, not on the door list."),
            leo("And in code?"),
            maya("In Python it is the redis-entraid credential provider with DefaultAzureCredential. The library fetches a token, which is a short-lived pass, and refreshes it. The .NET client does the same."),
            leo("And is it out on the public internet?"),
            maya("It does not have to be. Give it a private endpoint, which is a private address for the cache inside your own network, and turn public access off. Then only Container Apps can reach it."),
            leo("So what do I remember for the exam?"),
            maya("Port 10000 with Entra on by default means Azure Managed Redis; 6380 means the older Azure Cache for Redis. Stale after an edit means update the database first, then delete the key, with a TTL as the net."),
            leo("Keep going."),
            maya("Writes refused while reads work means memory full and the no-eviction policy. Wanting search later means ticking the add-on at creation. And only the API may reach the cache means a private endpoint with public access off."),
            leo("And one more for the exam, on the sign-in?"),
            maya("Commands refused after you granted a role means the identity was never added as a Redis user on the Authentication page."),
            leo("And anything to try today?"),
            maya("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run ping. Then write a key with a 60-second TTL and watch it count down."),
            leo("Correct in SQL, fast and huge in Cosmos, true right now in Redis. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-redis",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Restaurants That Were Not There. Eight on a Friday, and Zaiqa's home screen was offering fifteen places in your area. Four of them had closed for the night an hour earlier."),
            leo("Detective Leo, reporting. Our API, the .NET service the phone asks, reads that list out of Azure SQL. Was SQL wrong?"),
            maya("Azure SQL was perfect. Every one of those four had its shutter down in the database by ten past eight. The screen was reading something else."),
            leo("Then something was keeping a copy. What?"),
            maya("Azure Managed Redis. We put it in two months ago: an in-memory store, where values live in the machine's working memory rather than on a disk, so a read comes back in well under a millisecond."),
            leo("And why does Zaiqa need one at all?"),
            maya("Because that list is expensive. It joins restaurants, orders and ratings for the last hour and ranks them. Two hundred milliseconds of work, and thirty thousand phones a minute asking the same question at dinner rush."),
            leo("So we answer them from the copy. What is the pattern called?"),
            maya("It is called cache-aside. The API asks Redis first, and a hit goes straight back. On a miss its own code runs the SQL query, stores the answer in Redis, and returns it. Redis never fetches anything itself."),
            leo("Evidence bag one, then. How long was that copy meant to live?"),
            maya("Sixty seconds, with a TTL, which stands for time to live: an expiry on the key, like a use-by date on milk. And here is clue one. The key had none."),
            leo("Somebody forgot the expiry."),
            maya("Somebody wrote the line that stores the list and left the expiry off. On its own that is survivable, because we also do invalidation: when a restaurant closes, we delete the key."),
            leo("So the delete should have saved us. Unless it never ran."),
            maya("It ran. Clue two is the order it ran in. The code deleted the cached key first, and then wrote the closure into Azure SQL."),
            leo("[sigh] And in between, somebody read."),
            maya("A phone read, missed, ran the query against a database that still said open, and wrote the old list straight back into Redis. Then our update landed. The cache held a lie, and with no expiry nothing was ever going to clear it."),
            leo("So the rule is?"),
            maya("Update Azure SQL first, then delete the key. And give every key a TTL anyway, because one day a code path forgets, and sixty seconds of wrong is a bad evening rather than a bad fortnight."),
            leo("Case one closed. There is a second file on the desk."),
            maya("The same Friday, later. The rider's dot on the tracking map stopped moving, and the API began refusing writes while reads carried on perfectly."),
            leo("Now that is odd. Where does the dot live?"),
            maya("In Redis, one key per order, overwritten every three seconds. The ping history goes to Cosmos DB as always. This is only the one value the tracking screen wants: where rider four-two-one is now."),
            leo("Why not read the newest ping out of Cosmos?"),
            maya("You would pay a database every three seconds for a number that is stale three seconds later. One key, one write, one very cheap read."),
            leo("Fine. So why were writes refused?"),
            maya("The memory filled. And what Redis does when it fills is one setting: the eviction policy, which says which keys it throws out to make room."),
            leo("And ours threw out?"),
            maya("Nothing. Ours was no-eviction, so writes fail with an out-of-memory error and reads keep working. Which is exactly the shape of the symptom."),
            leo("Why on earth would anybody choose that?"),
            maya("Nobody chose it by hand. We created the cache with RediSearch, the add-on that finds an answer by meaning rather than by exact key, for the Taste service's semantic cache later on. RediSearch requires no-eviction."),
            leo("So the add-on set the policy, and the missing expiries filled the memory."),
            maya("The two clues are one clue. Keys with no TTL, in a cache that may never throw anything away. It took eleven days to fill and one Friday to hurt."),
            leo("And the repair?"),
            maya("Every key gets an expiry. Then more room: a bigger tier, Memory Optimized when you hold a lot, Compute Optimized when you make a lot of calls. Or scale out over shards, each shard one slice of the cache with its own memory."),
            leo("Does the API have to know which shard a key is on?"),
            maya("No. With the Enterprise clustering policy you talk to one address and Managed Redis routes it. Only a command touching several keys cares, and then they must land on the same shard."),
            leo("One more thing in the file. A note about the rate limit."),
            maya("That one worked, and it is worth saying why. A counter, one key per diner per minute: add one on every request, a sixty-second expiry on the first, and past two hundred the API answers 429, which means too many requests."),
            leo("Why not count inside the API?"),
            maya("Because twenty copies of the API run at dinner rush, each with its own count, and the real limit becomes twenty times what you meant. Redis is the one place all twenty agree."),
            leo("And the last page: how does the API get in? Tell me nobody left a password in the settings."),
            maya("Nobody did. A new Managed Redis cache has Microsoft Entra on by default, which is Microsoft's sign-in service, so the API signs in with its managed identity, the badge Azure gives your app."),
            leo("Then we were never going to find an access key in evidence."),
            maya("You would have found one complaint, though, from the week we turned it on. Every command came back refused, with the role granted and everything."),
            leo("Because?"),
            maya("Granting a role on the Azure resource is not enough. On the cache's Authentication page you add that identity as a Redis user. Badge in hand, but not on the list for that room."),
            leo("And in code?"),
            maya("In Python, the redis-entraid credential provider with DefaultAzureCredential: it fetches a token, which is a short-lived pass, and refreshes it before it runs out."),
            leo("Last question on the case. Could any of this have lost us an order?"),
            maya("Not one, and that is the design. Losing the cache must never lose data. The hot list rebuilds from Azure SQL, the ping history is in Cosmos, the orders and payments are in SQL. Put the only copy of something in Redis and you have made a database out of a cache."),
            leo("Case closed. And for the exam?"),
            maya("Port 10000 with Entra on by default is Azure Managed Redis; 6380 is the older Azure Cache for Redis. Stale after an edit means update the database first, then delete the key. Writes refused while reads work means the memory is full and the policy is no-eviction."),
            leo("Two more for the exam?"),
            maya("Wanting search later means ticking the add-on when you create the cache, because it cannot be added after. And commands refused after a role was granted means the identity was never added as a Redis user."),
            leo("And if only the API may reach it?"),
            maya("A private endpoint, which is a private address for the cache inside your own network, with public access turned off."),
            leo("Anything to try today?"),
            maya("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run ping. Then write a key with a 60-second TTL and watch it count down."),
            leo("Correct in SQL, fast and huge in Cosmos, true right now in Redis. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-redis",
        listOf(
            maya("Okay, did you hear what happened to the Zaiqa home screen this week? Sit down. They finally put a cache in front of the hot restaurant list, and I have five days of material."),
            leo("I heard somebody shouting about memory on Thursday. Start on Monday. Why a cache at all?"),
            maya("Because that list is expensive. It joins restaurants, orders and ratings for the last hour and ranks them. Two hundred milliseconds, thirty thousand phones a minute at dinner rush, and the same answer for everybody."),
            leo("And the cache is?"),
            maya("Azure Managed Redis. An in-memory store: values live in the machine's working memory rather than on a disk, so a read comes back in well under a millisecond. Port 10000, encryption always on, and the command line is az redisenterprise."),
            leo("Right. Monday."),
            maya("Tom. Tom wired up cache-aside in an afternoon, and honestly it was good work. The API, the .NET service the phone asks, checks Redis first, and on a miss its own code runs the SQL query, stores the answer, and returns it."),
            leo("I hear a but coming."),
            maya("Tom stored it with no TTL. Time to live: an expiry on the key, like a use-by date on milk. Without one, a copy sits there until somebody removes it."),
            leo("And nobody did."),
            maya("On Friday the home screen was still offering four restaurants that had closed at eight. [chuckle] Tom now puts an expiry on everything, including his lunch."),
            leo("Tuesday."),
            maya("Dana, and this one is subtler. Dana wrote the invalidation: when a restaurant closes, delete the cached key. Right instinct, wrong order. She deleted the key first, then wrote the closure into Azure SQL."),
            leo("And in between?"),
            maya("A phone read, missed, ran the query against a database that still said open, and put the old list straight back. Then Dana's update landed. The cache held yesterday, and with Tom's missing expiry, forever."),
            leo("So the rule is database first, then delete."),
            maya("Database first, then delete, with an expiry as the safety net underneath. Dana wrote that on the whiteboard and it is still there."),
            leo("Wednesday. Please tell me Wednesday was quiet."),
            maya("Wednesday was Marcus, and Marcus was ambitious. He moved the rider's current position into Redis, one key per order, overwritten every three seconds. That part was exactly right."),
            leo("Because paying a database every three seconds for a number that goes stale in three seconds is silly."),
            maya("Precisely. The ping history still goes to Cosmos DB, as it always has. What Marcus did next is the problem. He also moved the rider's end-of-shift summary in there, and nowhere else."),
            leo("The only copy, in a cache."),
            maya("Thursday morning the cache was emptied during a resize, and nine hundred summaries were gone. Say it with me: losing the cache must never lose data. Put the only copy in Redis and you have made a database out of a cache."),
            leo("Thursday. That was the shouting."),
            maya("Thursday was the memory. Writes started failing with an out-of-memory error while reads carried on perfectly, which is a very specific symptom."),
            leo("Meaning?"),
            maya("Meaning the memory was full and the eviction policy was no-eviction. The eviction policy says which keys Redis throws out to make room: the least recently used, or only ones that carry an expiry, or, in our case, nothing at all."),
            leo("Who picked nothing at all?"),
            maya("Nobody. Priya created the cache in January with RediSearch, the add-on that finds an answer by meaning rather than by exact key, for the Taste service's semantic cache. RediSearch requires no-eviction, and add-ons are ticked at creation or never."),
            leo("So Priya was right, and Tom's missing expiries filled it."),
            maya("The two stories are one story. Keys that never expire, in a cache that may never throw anything away. Eleven days to fill, one Friday to hurt."),
            leo("And the fix?"),
            maya("Expiries everywhere, and more room: a bigger tier, Memory Optimized when you hold a lot, Compute Optimized when you make a lot of calls. Or scale out over shards, each shard a slice of the cache with its own memory."),
            leo("Does the code have to know the shards?"),
            maya("No. With the Enterprise clustering policy you talk to one address and Managed Redis routes it. Only a command touching several keys cares, and then those keys must land on the same shard."),
            leo("Friday."),
            maya("Friday was the intern, and I am going to defend the intern. The rate limit: a counter, one key per diner per minute, add one per request, a sixty-second expiry on the first, and past two hundred the API answers 429, which means too many requests."),
            leo("That sounds right to me."),
            maya("It is right. The intern's first version kept the counter inside the API, in its own memory, which at dinner rush is twenty copies of the API each counting to two hundred."),
            leo("[laugh] A limit of four thousand."),
            maya("Fixed in twenty minutes, once somebody said it out loud. Redis is the one place all twenty agree."),
            leo("And did anybody try to put a password in the app settings?"),
            maya("The intern asked where the access key went, and Priya nearly wept with joy at the question. There is no key. Microsoft Entra is on by default on a new cache, which is Microsoft's sign-in service, so the API uses its managed identity, the badge Azure gives your app."),
            leo("And nobody fought it?"),
            maya("January fought it. Every command came back refused, with the role granted, and two people lost a morning. On the cache's Authentication page you must add that identity as a Redis user. Badge in hand, not on the list for that room."),
            leo("And in code?"),
            maya("Python, the redis-entraid credential provider with DefaultAzureCredential: it fetches a token, which is a short-lived pass, and refreshes it before it runs out."),
            leo("Anything good I have not heard?"),
            maya("One. The cache has a private endpoint, which is a private address inside our own network, and public access is off. Nobody outside Container Apps can even knock."),
            leo("So what is the gossip I should remember for the exam?"),
            maya("Port 10000 with Entra on by default is Azure Managed Redis; 6380 is the older Azure Cache for Redis. Stale after an edit is Dana's: database first, then delete the key. Writes refused while reads work is Thursday: memory full, no-eviction."),
            leo("And the other two?"),
            maya("Wanting search later means ticking the add-on at creation, because you cannot add it afterwards. And commands refused after a role was granted means nobody added the identity as a Redis user."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run ping. Then write a key with a 60-second TTL and watch it count down."),
            leo("An expiry on everything, and never the only copy. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-redis",
        listOf(
            nova("Hey, you. It is late, the kitchen is shut, and there is one light still on inside Zaiqa. [pause] Tonight: the little store that only ever holds what is true right now."),
            ivy("Only right now. Nothing older?"),
            nova("Nothing it would miss. Orders and payments are in Azure SQL, the order events and rider pings are in Cosmos DB, the payouts ledger is in PostgreSQL. Tonight we add Azure Managed Redis."),
            ivy("And what does it keep?"),
            nova("Two things. The hot restaurant list on the home screen. And the rider's position, the dot moving on your tracking map while you wait. [pause] Both read thousands of times a second."),
            ivy("Tell me about the list first."),
            nova("It costs two hundred milliseconds in Azure SQL, every time: a join across restaurants, orders and ratings for the last hour, then a ranking. And at eight on a Friday, thirty thousand phones a minute want the same fifteen names."),
            ivy("So you answer them all from one answer."),
            nova("From one answer, kept somewhere very fast. Azure Managed Redis is an in-memory store: values live in the machine's working memory rather than on a disk, so a read comes back in well under a millisecond."),
            ivy("Mm. And how does our API get at it?"),
            nova("Your API, which is the .NET service the phone talks to, asks Redis first. That is a hit, and it goes straight back. On a miss, your own code runs the SQL query, stores the answer, and returns it. [pause] That pattern is cache-aside."),
            ivy("Aside, because the cache never goes and fetches."),
            nova("Never. It holds what you put in it, for as long as you say. Which brings us to the TTL, which stands for time to live: an expiry on the key, like a use-by date on milk."),
            ivy("And how long does a restaurant list stay fresh, at this hour?"),
            nova("Sixty seconds. A place that stops taking orders at ten past eight should be off the screen by nine past. [pause] Longer for a menu page, which barely changes. Short for what the evening keeps rewriting."),
            ivy("And when one closes, do we wait the minute out?"),
            nova("No, we take the copy away ourselves. That is invalidation. And the order matters more than anything else I will say tonight: update Azure SQL first, then delete the key."),
            ivy("Why that way round?"),
            nova("Because if you delete first, a reader slips in, misses, reads a database that still says open, and writes the old list back. Then your update lands, and the cache is holding yesterday. [pause] Quietly, all night."),
            ivy("[sigh] And the expiry catches it anyway."),
            nova("Sixty seconds of wrong instead of a fortnight of wrong. The expiry is the net under the invalidation, never a replacement for it."),
            ivy("Now the dot on the map. Why is that not with the pings in Cosmos?"),
            nova("The history stays in Cosmos; that is its job. But the screen wants one number: where rider four-two-one is now. Overwritten every three seconds, read by everyone watching that order."),
            ivy("And paying a database for it each time would be?"),
            nova("Slow, and a little silly, for a value that is stale three seconds later. One key, one write, one read that costs almost nothing."),
            ivy("So Redis is a little database now?"),
            nova("[pause] No, and this is the line to take to bed. Losing the cache must never lose data. If it vanished tonight, the hot list rebuilds from Azure SQL, the ping history is in Cosmos, the orders are in SQL, and the dot is three seconds behind."),
            ivy("And if something lived only there?"),
            nova("Then you have made a database out of a cache, and a cache is allowed to disappear. Never the only copy."),
            ivy("Mm. What happens when it runs out of room?"),
            nova("One setting decides: the eviction policy, which says which keys Redis throws out to make space. The least recently used. Or only ones that already carry an expiry. Or nothing at all."),
            ivy("And nothing at all means?"),
            nova("Writes start failing with an out-of-memory error while reads carry on perfectly. [pause] A very distinctive symptom at two in the morning."),
            ivy("Who would choose it?"),
            nova("You choose it without noticing. RediSearch, the add-on that finds an answer by meaning rather than by exact key, requires it. Zaiqa ticked it for the Taste service's semantic cache later on, and add-ons are ticked at creation or never."),
            ivy("So with that on, every key needs its expiry."),
            nova("Every key, and an eye on how full it is. Otherwise a quiet fortnight of keys with no expiry fills it, and Friday's writes are refused."),
            ivy("And when it is genuinely too small?"),
            nova("A bigger tier: Memory Optimized when you hold a lot, Compute Optimized when you make a lot of calls. Or you scale out over shards, each shard one slice of the cache with its own memory."),
            ivy("Does the code have to know which shard?"),
            nova("No. With the Enterprise clustering policy you talk to one address and it routes for you. [pause] Only a command touching several keys cares, and then they must land on the same shard."),
            ivy("One more use before I fade. You mentioned limits."),
            nova("Rate limiting. A counter, one key per diner per minute: add one on each request, set a sixty-second expiry the first time, and past two hundred your API answers 429, which means too many requests."),
            ivy("And counting inside the API would be?"),
            nova("Twenty copies of the API at dinner rush, each counting to two hundred by itself. Your limit quietly becomes four thousand. Redis is the one place all twenty agree."),
            ivy("Last thing. How does it get in, at this hour, without a password?"),
            nova("A new cache has Microsoft Entra on by default, which is Microsoft's sign-in service, so your API signs in with its managed identity, the badge Azure gives your app. No access key anywhere."),
            ivy("And it simply works?"),
            nova("One more step, and it is the one people lose a morning to. On the cache's Authentication page you add that identity as a Redis user. A role on the resource alone is a badge with no name on the door list."),
            ivy("And in code, softly."),
            nova("In Python, the redis-entraid credential provider with DefaultAzureCredential: it fetches a token, which is a short-lived pass, and refreshes it before it runs out. [pause] Nothing to store, nothing to leak."),
            ivy("And can anyone out there reach it?"),
            nova("Not if you give it a private endpoint, which is a private address inside your own network, and turn public access off. Then only Container Apps can knock."),
            ivy("Before I go. What do I remember for the exam?"),
            nova("Port 10000 with Entra on by default is Azure Managed Redis; 6380 is the older Azure Cache for Redis. Stale after an edit: database first, then delete the key. Writes refused while reads work: memory full, no-eviction."),
            ivy("Two more?"),
            nova("Search wanted later means the add-on had to be ticked at creation. And commands refused after a role was granted means nobody added the identity as a Redis user."),
            ivy("And something to try today, before the light goes off?"),
            nova("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run ping. [pause] Then write a key with a 60-second TTL and watch it count down."),
            ivy("True right now, and never the only copy. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-redis",
        listOf(
            nova("Tonight is date night, darling. Candles, something with far too much chilli in it, and the one store in Zaiqa that keeps only what is true this very minute."),
            rafe("I do like a thing that lives in the moment. Azure SQL has the orders, Cosmos has the pings. What is left?"),
            nova("The hot restaurant list on the home screen, and the rider's dot on your tracking map. Both read thousands of times a second, both wrong within the minute. So quiz me, and be strict: where do they go?"),
            rafe("Azure Managed Redis. An in-memory store: the values live in working memory rather than on a disk, so a read comes back in well under a millisecond."),
            nova("Right answer, and quick with it. [kiss]"),
            rafe("And the port, since the exam does love a port."),
            nova("10000, with encryption always on. The older Azure Cache for Redis answers on 6380. Now, your turn to be useful. The home screen asks for fifteen restaurants. Walk me through it."),
            rafe("Your API, which is the .NET service the phone talks to, asks Redis first. A hit goes straight back. On a miss its own code runs the SQL query, stores the answer, and returns it. That is cache-aside."),
            nova("Right answer. Aside, because the cache never fetches for you. [kiss]"),
            rafe("Now be strict with me. How long do I keep it?"),
            nova("You set a TTL, which stands for time to live: an expiry on the key, like a use-by date on milk. Sixty seconds for a list the evening keeps rewriting. An hour for a menu page that barely moves."),
            rafe("Exam style, then, love. A restaurant closes at ten past eight and I must not wait the minute out. What do I do, and in which order?"),
            nova("Update Azure SQL first, then delete the cached key. That is invalidation. Delete first and a reader slips in, misses, reads a database that still says open, and puts the old list back on top of your update."),
            rafe("Right answer, and in the right order, which is the whole marriage. [kiss]"),
            nova("Mm. And the expiry stays on anyway, as the net underneath. Now the dot. Why does the rider's position belong here and not in a database?"),
            rafe("Because it is one number, overwritten every three seconds, and stale three seconds later. The history is already in Cosmos DB. Paying a database for the current value every time buys you nothing."),
            nova("Right answer. Fast, and faithless. [kiss]"),
            rafe("Then the cruel one. What happens the night Redis falls over?"),
            nova("Nothing we cannot live without, darling, because losing the cache must never lose data. The list rebuilds from Azure SQL, the pings are in Cosmos, the orders and payments are in SQL, and the dot is three seconds behind."),
            rafe("And if somebody keeps the only copy of something in there?"),
            nova("Then they have made a database out of a cache, and a cache is allowed to disappear. Never the only copy."),
            rafe("Next. The memory fills up. What decides who gets thrown out?"),
            nova("The eviction policy. The least recently used key, or only the ones carrying an expiry, or nothing at all, which is no-eviction. With that last one, writes fail with an out-of-memory error while reads carry on."),
            rafe("Right answer, and a very good description of a bad evening. [kiss]"),
            nova("Now a trap for you. Nobody chose no-eviction by hand. So why do we have it?"),
            rafe("RediSearch. The add-on that finds an answer by meaning rather than by exact key, which we ticked for the Taste service's semantic cache. It requires no-eviction, and add-ons are ticked at creation or never."),
            nova("Right answer, and you remembered the cruel half of it."),
            rafe("So every key gets an expiry, and we watch how full it is. And if it is simply too small?"),
            nova("A bigger tier: Memory Optimized to hold a lot, Compute Optimized to serve a lot. Or scale out over shards, each shard a slice of the cache with its own memory."),
            rafe("And my code has to know which shard?"),
            nova("No. With the Enterprise clustering policy you talk to one address and it routes for you. Only a command touching several keys cares, and then they must land on the same shard."),
            rafe("One more from your list. Rate limiting, which sounds like something we should discuss separately."),
            nova("[chuckle] A counter, darling. One key per diner per minute: add one on each request, a sixty-second expiry on the first, and past two hundred your API answers 429, which means too many requests."),
            rafe("And why not count inside the API?"),
            nova("Because twenty copies of it run at dinner rush, each counting to two hundred alone, and your limit is quietly four thousand. Redis is the one place they all agree. Now, my last strict question: how does the API get in?"),
            rafe("With no access key at all. A new cache has Microsoft Entra on by default, which is Microsoft's sign-in service, so the API uses its managed identity, the badge Azure gives your app."),
            nova("Right answer, and no secrets between us. [kiss]"),
            rafe("Although I remember a morning when that was not enough."),
            nova("Granting a role on the Azure resource is not the same as being allowed to speak. On the cache's Authentication page you add that identity as a Redis user. In Python it is the redis-entraid provider with DefaultAzureCredential, fetching a token, which is a short-lived pass."),
            rafe("And nobody outside can knock?"),
            nova("Not with a private endpoint, which is a private address for the cache inside our own network, and public access turned off. Only Container Apps may come to the door."),
            rafe("Then the last one, love, and it is for the exam. Five phrases, five answers."),
            nova("Port 10000 with Entra by default is Azure Managed Redis; 6380 is the older one. Stale after an edit is database first, then delete. Writes refused while reads work is memory full with no-eviction."),
            rafe("And the two that cost us mornings?"),
            nova("Search wanted later means the add-on had to be ticked at creation. And commands refused after a role was granted means nobody added the identity as a Redis user."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run ping. Then write a key with a 60-second TTL and watch it count down."),
            nova("Correct in SQL, fast and huge in Cosmos, true right now in Redis. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun stores() = DiagramSpec(
        "Each store holds a different kind of truth, and Redis holds the one that is true right now.",
        nodes = listOf(
            DNode("sql", "Azure SQL", 0.26f, 0.18f, w = 0.46f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "orders, payments"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "events, pings"),
            DNode("redis", "Managed Redis", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "true right now"),
            DNode("hot", "Hot list, and the dot", 0.76f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "thousands a second"),
        ),
        edges = listOf(
            DEdge("redis", "hot", "holds"),
        ),
        aspect = 0.8f,
    )

    private fun aside() = DiagramSpec(
        "Cache-aside: the API asks Redis first, and on a miss runs the SQL query itself and stores the answer back.",
        nodes = listOf(
            DNode("phone", "Zaiqa app", 0.18f, 0.15f, w = 0.32f, h = 0.18f, kind = NodeKind.USER),
            DNode("api", "Zaiqa API", 0.62f, 0.15f, w = 0.48f, h = 0.18f, tone = 1, sub = "Container Apps"),
            DNode("redis", "Managed Redis", 0.22f, 0.55f, w = 0.4f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "a hit, in no time"),
            DNode("sql", "Azure SQL", 0.74f, 0.55f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "a miss, 200 milliseconds"),
            DNode("ttl", "Store it back", 0.5f, 0.88f, w = 0.6f, h = 0.18f, kind = NodeKind.PILL, sub = "with a sixty-second expiry"),
        ),
        edges = listOf(
            DEdge("phone", "api"),
            DEdge("api", "redis", "1 ask"),
            DEdge("api", "sql", "2 on a miss"),
            DEdge("api", "ttl", "3 store"),
        ),
        aspect = 0.8f,
    )

    private fun order() = DiagramSpec(
        "An edit updates Azure SQL first and deletes the key after; the other way round, a reader puts the old value back.",
        nodes = listOf(
            DNode("write", "The edit", 0.5f, 0.12f, w = 0.56f, h = 0.18f, kind = NodeKind.NOTE, sub = "a restaurant closes"),
            DNode("db", "Azure SQL", 0.24f, 0.45f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "1 update first"),
            DNode("del", "Invalidation", 0.74f, 0.45f, w = 0.44f, h = 0.2f, sub = "2 delete the key"),
            DNode("stale", "The wrong order", 0.5f, 0.82f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "delete first: a reader refills it"),
        ),
        edges = listOf(
            DEdge("write", "db"),
            DEdge("db", "del"),
            DEdge("del", "stale", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun expiry() = DiagramSpec(
        "Every cached key carries an expiry, so a delete that never runs cannot leave the copy wrong forever.",
        nodes = listOf(
            DNode("key", "The cached key", 0.26f, 0.18f, w = 0.46f, h = 0.2f, sub = "fifteen restaurants"),
            DNode("ttl", "TTL", 0.76f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "sixty seconds"),
            DNode("gone", "Redis drops it", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "when the time passes"),
            DNode("net", "The safety net", 0.76f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "if a delete is forgotten"),
        ),
        edges = listOf(
            DEdge("key", "ttl"),
            DEdge("ttl", "gone"),
            DEdge("ttl", "net"),
        ),
        aspect = 0.8f,
    )

    private fun rider() = DiagramSpec(
        "The rider's current position is one key in Redis, overwritten every three seconds; the history goes to Cosmos.",
        nodes = listOf(
            DNode("rider", "Rider", 0.18f, 0.15f, w = 0.32f, h = 0.18f, kind = NodeKind.USER),
            DNode("api", "Zaiqa API", 0.64f, 0.15f, w = 0.46f, h = 0.18f, tone = 1, sub = "every three seconds"),
            DNode("redis", "One key in Redis", 0.26f, 0.56f, w = 0.46f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "where he is now"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.56f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2, sub = "the ping history"),
            DNode("phone", "The tracking map", 0.5f, 0.88f, w = 0.5f, h = 0.18f, kind = NodeKind.USER),
        ),
        edges = listOf(
            DEdge("rider", "api"),
            DEdge("api", "redis", "overwrite"),
            DEdge("api", "cosmos", "append"),
            DEdge("redis", "phone", "read"),
        ),
        aspect = 0.8f,
    )

    private fun memory() = DiagramSpec(
        "When the memory fills, the eviction policy decides what goes; with no-eviction nothing goes and writes are refused.",
        nodes = listOf(
            DNode("full", "Memory full", 0.5f, 0.14f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "a fortnight of keys"),
            DNode("lru", "Least used", 0.18f, 0.5f, w = 0.3f, h = 0.2f, sub = "every key"),
            DNode("vol", "Only expiring", 0.5f, 0.5f, w = 0.3f, h = 0.2f, sub = "keys with a TTL"),
            DNode("none", "No-eviction", 0.82f, 0.5f, w = 0.3f, h = 0.2f, tone = 2, sub = "nothing at all"),
            DNode("refused", "Writes refused", 0.5f, 0.85f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "out of memory; reads work"),
        ),
        edges = listOf(
            DEdge("full", "lru"),
            DEdge("full", "vol"),
            DEdge("full", "none"),
            DEdge("none", "refused"),
        ),
        aspect = 0.8f,
    )

    private fun scale() = DiagramSpec(
        "More room is a bigger tier or more shards, and the Enterprise clustering policy keeps it one address to talk to.",
        nodes = listOf(
            DNode("cache", "Managed Redis", 0.5f, 0.14f, w = 0.56f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "one address"),
            DNode("tier", "A bigger tier", 0.24f, 0.5f, w = 0.42f, h = 0.22f, sub = "Memory or Compute"),
            DNode("shards", "More shards", 0.74f, 0.5f, w = 0.44f, h = 0.22f, sub = "a slice each, with memory"),
            DNode("policy", "Enterprise clustering", 0.5f, 0.85f, w = 0.62f, h = 0.2f, kind = NodeKind.PILL, sub = "keys used together, together"),
        ),
        edges = listOf(
            DEdge("cache", "tier"),
            DEdge("cache", "shards"),
            DEdge("cache", "policy"),
        ),
        aspect = 0.8f,
    )

    private fun limit() = DiagramSpec(
        "Rate limiting is one counter key a diner a minute, shared by every copy of the API, with an expiry on the first.",
        nodes = listOf(
            DNode("user", "One diner", 0.18f, 0.16f, w = 0.3f, h = 0.18f, kind = NodeKind.USER),
            DNode("counter", "One key a minute", 0.64f, 0.16f, w = 0.46f, h = 0.2f, kind = NodeKind.PILL, sub = "add one per request"),
            DNode("many", "Twenty copies", 0.26f, 0.6f, w = 0.46f, h = 0.22f, sub = "all agree on one count"),
            DNode("over", "Past two hundred", 0.76f, 0.6f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "429, too many requests"),
            DNode("exp", "A sixty-second expiry", 0.5f, 0.9f, w = 0.56f, h = 0.16f, kind = NodeKind.PILL, sub = "set on the first one"),
        ),
        edges = listOf(
            DEdge("user", "counter"),
            DEdge("counter", "many"),
            DEdge("counter", "over"),
            DEdge("counter", "exp"),
        ),
        aspect = 0.8f,
    )

    private fun signin() = DiagramSpec(
        "No access key: the API signs in with its managed identity, which must also be added as a Redis user on the cache.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.26f, 0.14f, w = 0.46f, h = 0.2f, tone = 1, sub = "no access key"),
            DNode("entra", "Microsoft Entra", 0.76f, 0.14f, w = 0.42f, h = 0.2f, tone = 2, sub = "on by default"),
            DNode("mi", "Managed identity", 0.26f, 0.46f, w = 0.46f, h = 0.2f, sub = "a badge, no password"),
            DNode("user", "Redis user", 0.76f, 0.46f, w = 0.42f, h = 0.2f, sub = "on Authentication"),
            DNode("cache", "Managed Redis", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "port 10000"),
            DNode("pe", "Private endpoint", 0.76f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "public access off"),
        ),
        edges = listOf(
            DEdge("api", "mi", "uses"),
            DEdge("mi", "entra", "signs in"),
            DEdge("mi", "user", "added as"),
            DEdge("api", "cache", "commands"),
            DEdge("user", "cache", "allowed"),
            DEdge("cache", "pe", "reached by"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Managed Redis questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("port", "Port 10000", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Azure Managed Redis"),
            DNode("order", "Stale after an edit", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "database first, then delete"),
            DNode("mem", "Writes refused", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "memory full, no-eviction"),
            DNode("mods", "Search wanted later", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ticked at creation"),
            DNode("usr", "Refused, role granted", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "add it as a Redis user"),
            DNode("net", "Only the API reaches it", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a private endpoint"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("port", "order", "mem", "mods", "usr", "net")
    private val today = BoardCard("Today", "A cache with RediSearch, redis-entraid, and a TTL you watch run out.")
    private val asideCard = BoardCard("Cache-aside", "Ask Redis first; on a miss your own code loads SQL and stores it back.")
    private val ttlCard = BoardCard("TTL", "Time to live: an expiry on the key, like a use-by date on milk.")
    private val orderCard = BoardCard("Database first, then delete", "Delete first and a reader puts the old list back on top of your update.")
    private val netCard = BoardCard("The expiry is the net", "One day a code path forgets. Sixty seconds of wrong, not a fortnight.")
    private val evictCard = BoardCard("Eviction policy", "Least recently used, only keys with an expiry, or nothing at all.")
    private val noEvictCard = BoardCard("No-eviction", "Writes fail with out of memory while reads carry on perfectly.")
    private val searchCard = BoardCard("RediSearch requires it", "The add-on is ticked at creation or never, and it forces no-eviction.")
    private val onlyCopyCard = BoardCard("Never the only copy", "Put it only in Redis and you have made a database out of a cache.")
    private val clusterCard = BoardCard("Enterprise clustering", "One address, and keys used in one command on the same shard.")
    private val limitCard = BoardCard("A counter a diner a minute", "Add one per request, expiry on the first, 429 past two hundred.")
    private val sharedCard = BoardCard("Twenty copies of the API", "Counting in memory makes the limit twenty times what you meant.")
    private val noKeyCard = BoardCard("No access key", "Entra on by default; the API signs in with its managed identity.")
    private val redisUserCard = BoardCard("Add it as a Redis user", "A role on the resource alone does not let it run commands.")
    private val entraidCard = BoardCard("redis-entraid", "It fetches a short-lived pass and refreshes it. Nothing to store.")
    private val peCard = BoardCard("Private endpoint", "A private address inside your network, with public access off.")
    private val phraseCard = BoardCard("Listen for the phrase", "Port 10000, stale after an edit, writes refused: one answer each.")

    private fun classicBoard() = PodcastBoard(
        "ai-redis",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa's orders sit in Azure SQL..."
            Scene(
                0, "The fourth store", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql", "cosmos"), light = setOf("sql"), card = BoardCard("Today", "SQL, Cosmos and PostgreSQL are in. Today we add a fourth store.")),
                    Beat(2, show = setOf("redis", "hot"), light = setOf("redis", "hot"), pulse = "redis" to "hot", card = BoardCard("What is true right now", "The hot restaurant list, and the rider's dot on the tracking map.")),
                    Beat(4, light = setOf("sql"), card = BoardCard("The expensive query", "Restaurants, orders and ratings joined and ranked: 200 milliseconds.")),
                    Beat(6, light = setOf("redis"), card = BoardCard("Azure Managed Redis", "Thirty thousand phones a minute, and one answer for all of them.")),
                ),
            ),
            // 7: "Which is what, in plain words?"
            Scene(
                7, "Values in memory", aside(),
                beats = listOf(
                    Beat(8, show = setOf("phone", "api", "redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("In-memory store", "Values in working memory, not on a disk: under a millisecond.")),
                    Beat(10, light = setOf("redis"), card = BoardCard("Port 10000", "Built on Redis Enterprise. The older Azure Cache for Redis: 6380.")),
                ),
            ),
            // 11: "So how does the API use it for the hot list?"
            Scene(
                11, "Asking Redis first", aside(),
                beats = listOf(
                    Beat(12, show = setOf("phone", "api", "redis", "sql"), light = setOf("redis", "sql"), pulse = "api" to "sql", card = asideCard),
                    Beat(14, show = setOf("ttl"), light = setOf("ttl"), pulse = "api" to "ttl", card = ttlCard),
                    Beat(16, light = setOf("ttl"), card = BoardCard("Sixty seconds", "Short for what the evening rewrites; an hour for a menu page.")),
                ),
            ),
            // 17: "And when one closes, do I wait the minute out?"
            Scene(
                17, "The order matters", order(),
                beats = listOf(
                    Beat(18, show = setOf("write", "db", "del"), light = setOf("db", "del"), pulse = "db" to "del", card = orderCard),
                    Beat(20, show = setOf("stale"), light = setOf("stale"), pulse = "del" to "stale", card = BoardCard("Delete first", "A reader misses, reads a database that still says open, and refills it.")),
                    Beat(22, light = setOf("del"), card = netCard),
                ),
            ),
            // 23: "Right. Now the rider's position..."
            Scene(
                23, "Where the dot lives", rider(),
                beats = listOf(
                    Beat(24, show = setOf("rider", "api", "redis", "cosmos"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("One key per order", "Overwritten every three seconds; the history goes to Cosmos.")),
                    Beat(26, show = setOf("phone"), light = setOf("cosmos"), card = BoardCard("Losing the cache", "The list rebuilds from SQL, the pings are in Cosmos, orders in SQL.")),
                    Beat(28, light = setOf("redis"), card = onlyCopyCard),
                ),
            ),
            // 29: "Understood. Now, what happens when the memory fills up?"
            Scene(
                29, "When memory fills", memory(),
                beats = listOf(
                    Beat(30, show = setOf("full", "lru", "vol", "none"), light = setOf("lru", "vol", "none"), pulse = "full" to "none", card = evictCard),
                    Beat(32, show = setOf("refused"), light = setOf("refused"), pulse = "none" to "refused", card = noEvictCard),
                    Beat(34, light = setOf("none"), card = searchCard),
                    Beat(36, light = setOf("full"), card = BoardCard("Every key gets a TTL", "A slow drip of keys with no expiry fills it over a quiet fortnight.")),
                ),
            ),
            // 37: "Can I simply make it bigger?"
            Scene(
                37, "Making it bigger", scale(),
                beats = listOf(
                    Beat(38, show = setOf("cache", "tier", "shards"), light = setOf("tier", "shards"), pulse = "cache" to "shards", card = BoardCard("Two ways", "A bigger tier, or shards: one slice of the cache with its own memory.")),
                    Beat(40, show = setOf("policy"), light = setOf("policy"), pulse = "cache" to "policy", card = clusterCard),
                ),
            ),
            // 41: "Fair. Now a different use. You said rate limiting."
            Scene(
                41, "The rate limit", limit(),
                beats = listOf(
                    Beat(42, show = setOf("user", "counter", "many", "over", "exp"), light = setOf("counter", "over"), pulse = "counter" to "over", card = limitCard),
                ),
            ),
            // 43: "Good. Last thing: how does the API get in?"
            Scene(
                43, "Getting in", signin(),
                beats = listOf(
                    Beat(44, show = setOf("api", "mi", "entra"), light = setOf("mi"), pulse = "api" to "mi", card = noKeyCard),
                    Beat(46, show = setOf("user", "cache"), light = setOf("user"), pulse = "mi" to "user", card = redisUserCard),
                    Beat(48, light = setOf("api"), card = entraidCard),
                    Beat(50, show = setOf("pe"), light = setOf("pe"), pulse = "cache" to "pe", card = peCard),
                ),
            ),
            // 51: "So what do I remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("port", "order"), card = phraseCard),
                    Beat(54, light = setOf("mem", "mods", "net"), card = BoardCard("And three more", "Memory full, the add-on at creation, and a private endpoint.")),
                    Beat(56, light = setOf("usr"), card = BoardCard("Refused, role granted", "Nobody added the identity as a Redis user on Authentication.")),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-redis--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Restaurants That Were Not There..."
            Scene(
                0, "The restaurants not there", aside(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("The case", "Four restaurants on the home screen had closed an hour earlier.")),
                    Beat(2, show = setOf("sql"), light = setOf("sql"), pulse = "api" to "sql", card = BoardCard("Azure SQL was right", "Every shutter was down in the database by ten past eight.")),
                    Beat(4, show = setOf("redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Something kept a copy", "Managed Redis: values in working memory, under a millisecond.")),
                    Beat(6, light = setOf("sql"), card = BoardCard("Why the copy exists", "A 200 millisecond query, thirty thousand phones a minute.")),
                    Beat(8, show = setOf("ttl"), light = setOf("redis"), pulse = "api" to "redis", card = asideCard),
                ),
            ),
            // 9: "Evidence bag one, then. How long was that copy meant to live?"
            Scene(
                9, "Clue one: no expiry", expiry(),
                beats = listOf(
                    Beat(10, show = setOf("key", "ttl"), light = setOf("ttl"), pulse = "key" to "ttl", card = ttlCard),
                    Beat(12, show = setOf("net"), light = setOf("net"), pulse = "ttl" to "net", card = BoardCard("Clue one", "The key had no expiry at all, so only a delete could clear it.")),
                ),
            ),
            // 13: "So the delete should have saved us. Unless it never ran."
            Scene(
                13, "Clue two: the order", order(),
                beats = listOf(
                    Beat(14, show = setOf("write", "db", "del"), light = setOf("del"), card = BoardCard("Clue two", "It deleted the cached key first, then wrote the closure to SQL.")),
                    Beat(16, show = setOf("stale"), light = setOf("stale"), pulse = "del" to "stale", card = BoardCard("A reader refilled it", "The old list went back in, and nothing was going to clear it.")),
                    Beat(18, light = setOf("db"), pulse = "db" to "del", card = orderCard),
                ),
            ),
            // 19: "Case one closed. There is a second file on the desk."
            Scene(
                19, "The second file", rider(),
                beats = listOf(
                    Beat(20, show = setOf("rider", "api", "redis"), light = setOf("redis"), card = BoardCard("The dot stopped", "And the API began refusing writes while reads carried on.")),
                    Beat(22, show = setOf("cosmos", "phone"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("One key per order", "Overwritten every three seconds; the history goes to Cosmos.")),
                    Beat(24, light = setOf("cosmos"), card = BoardCard("Not the newest ping", "Paying a database for a number that is stale three seconds later.")),
                ),
            ),
            // 25: "Fine. So why were writes refused?"
            Scene(
                25, "The memory filled", memory(),
                beats = listOf(
                    Beat(26, show = setOf("full", "lru", "vol", "none"), light = setOf("full"), card = evictCard),
                    Beat(28, show = setOf("refused"), light = setOf("refused"), pulse = "none" to "refused", card = noEvictCard),
                    Beat(30, light = setOf("none"), card = searchCard),
                    Beat(32, light = setOf("full"), card = BoardCard("Two clues, one clue", "Keys with no TTL, in a cache that may never throw anything away.")),
                ),
            ),
            // 33: "And the repair?"
            Scene(
                33, "The repair", scale(),
                beats = listOf(
                    Beat(34, show = setOf("cache", "tier", "shards"), light = setOf("tier", "shards"), pulse = "cache" to "shards", card = BoardCard("More room", "A bigger tier, or shards: one slice of the cache with its own memory.")),
                    Beat(36, show = setOf("policy"), light = setOf("policy"), pulse = "cache" to "policy", card = clusterCard),
                ),
            ),
            // 37: "One more thing in the file. A note about the rate limit."
            Scene(
                37, "The rate limit", limit(),
                beats = listOf(
                    Beat(38, show = setOf("user", "counter", "over", "exp"), light = setOf("counter", "over"), pulse = "counter" to "over", card = limitCard),
                    Beat(40, show = setOf("many"), light = setOf("many"), pulse = "counter" to "many", card = sharedCard),
                ),
            ),
            // 41: "And the last page: how does the API get in?"
            Scene(
                41, "How it gets in", signin(),
                beats = listOf(
                    Beat(42, show = setOf("api", "mi", "entra"), light = setOf("mi"), pulse = "api" to "mi", card = noKeyCard),
                    Beat(44, show = setOf("user"), light = setOf("user"), card = BoardCard("Refused, role granted", "Every command came back refused the week it was turned on.")),
                    Beat(46, show = setOf("cache"), light = setOf("user"), pulse = "mi" to "user", card = redisUserCard),
                    Beat(48, light = setOf("api"), card = entraidCard),
                ),
            ),
            // 49: "Last question on the case. Could any of this have lost us an order?"
            Scene(
                49, "Case closed", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("port"), card = onlyCopyCard),
                    Beat(52, light = setOf("port", "order", "mem"), card = phraseCard),
                    Beat(54, light = setOf("mods", "usr"), card = BoardCard("And two more", "The add-on at creation, and the identity as a Redis user.")),
                    Beat(56, light = setOf("net"), card = peCard),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-redis--gossip",
        listOf(
            // 0: "Okay, did you hear what happened to the Zaiqa home screen this week?"
            Scene(
                0, "The week they cached it", aside(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "sql"), light = setOf("sql"), card = BoardCard("The week", "A cache went in front of the hot restaurant list. Five days of it.")),
                    Beat(2, light = setOf("sql"), card = BoardCard("Why a cache", "A 200 millisecond query, thirty thousand phones a minute.")),
                    Beat(4, show = setOf("redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Azure Managed Redis", "Working memory, port 10000, and the az redisenterprise command.")),
                    Beat(6, light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Monday: Tom", "He wired up cache-aside in an afternoon, and it was good work.")),
                    Beat(8, show = setOf("ttl"), light = setOf("ttl"), pulse = "api" to "ttl", card = ttlCard),
                ),
            ),
            // 9: "And nobody did."
            Scene(
                9, "Monday's mistake", expiry(),
                beats = listOf(
                    Beat(10, show = setOf("key", "ttl", "net"), light = setOf("net"), pulse = "ttl" to "net", card = BoardCard("No expiry at all", "On Friday the screen still offered four restaurants closed at eight.")),
                ),
            ),
            // 11: "Tuesday."
            Scene(
                11, "Tuesday: Dana", order(),
                beats = listOf(
                    Beat(12, show = setOf("write", "db", "del"), light = setOf("del"), card = BoardCard("Tuesday: Dana", "Right instinct, wrong order: she deleted the key before the write.")),
                    Beat(14, show = setOf("stale"), light = setOf("stale"), pulse = "del" to "stale", card = BoardCard("The old list came back", "A reader missed, read a database that said open, and refilled it.")),
                    Beat(16, light = setOf("db"), pulse = "db" to "del", card = orderCard),
                ),
            ),
            // 17: "Wednesday. Please tell me Wednesday was quiet."
            Scene(
                17, "Wednesday: Marcus", rider(),
                beats = listOf(
                    Beat(18, show = setOf("rider", "api", "redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Wednesday: Marcus", "The rider's position into Redis, overwritten every three seconds.")),
                    Beat(20, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "api" to "cosmos", card = BoardCard("The history stays", "The pings still go to Cosmos DB, as they always have.")),
                    Beat(22, show = setOf("phone"), light = setOf("redis"), card = onlyCopyCard),
                ),
            ),
            // 23: "Thursday. That was the shouting."
            Scene(
                23, "Thursday: the memory", memory(),
                beats = listOf(
                    Beat(24, show = setOf("full", "lru", "vol", "none", "refused"), light = setOf("refused"), pulse = "none" to "refused", card = noEvictCard),
                    Beat(26, light = setOf("lru", "vol", "none"), card = evictCard),
                    Beat(28, light = setOf("none"), card = searchCard),
                    Beat(30, light = setOf("full"), card = BoardCard("Two stories, one story", "Keys that never expire, in a cache that never throws anything away.")),
                ),
            ),
            // 31: "And the fix?"
            Scene(
                31, "The fix", scale(),
                beats = listOf(
                    Beat(32, show = setOf("cache", "tier", "shards"), light = setOf("tier", "shards"), pulse = "cache" to "shards", card = BoardCard("More room", "A bigger tier, or shards: one slice of the cache with its own memory.")),
                    Beat(34, show = setOf("policy"), light = setOf("policy"), pulse = "cache" to "policy", card = clusterCard),
                ),
            ),
            // 35: "Friday."
            Scene(
                35, "Friday: the intern", limit(),
                beats = listOf(
                    Beat(36, show = setOf("user", "counter", "over", "exp"), light = setOf("counter", "over"), pulse = "counter" to "over", card = limitCard),
                    Beat(38, show = setOf("many"), light = setOf("many"), pulse = "counter" to "many", card = sharedCard),
                    Beat(40, light = setOf("counter"), card = BoardCard("Fixed in twenty minutes", "Redis is the one place all twenty copies agree on the count.")),
                ),
            ),
            // 41: "And did anybody try to put a password in the app settings?"
            Scene(
                41, "No password at all", signin(),
                beats = listOf(
                    Beat(42, show = setOf("api", "mi", "entra"), light = setOf("mi"), pulse = "api" to "mi", card = noKeyCard),
                    Beat(44, show = setOf("user", "cache"), light = setOf("user"), pulse = "mi" to "user", card = redisUserCard),
                    Beat(46, light = setOf("api"), card = entraidCard),
                    Beat(48, show = setOf("pe"), light = setOf("pe"), pulse = "cache" to "pe", card = peCard),
                ),
            ),
            // 49: "So what is the gossip I should remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("port", "order", "mem"), card = phraseCard),
                    Beat(52, light = setOf("mods", "usr", "net"), card = BoardCard("And three more", "The add-on at creation, the Redis user, the private endpoint.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-redis--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen is shut..."
            Scene(
                0, "The light still on", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql", "cosmos"), light = setOf("sql"), card = BoardCard("Tonight", "The little store that only holds what is true right now.")),
                    Beat(2, show = setOf("redis"), light = setOf("redis"), card = BoardCard("Azure Managed Redis", "Orders in SQL, events and pings in Cosmos, the ledger in Postgres.")),
                    Beat(4, show = setOf("hot"), light = setOf("hot"), pulse = "redis" to "hot", card = BoardCard("Two things", "The hot restaurant list, and the dot on your tracking map.")),
                ),
            ),
            // 5: "Tell me about the list first."
            Scene(
                5, "The expensive list", aside(),
                beats = listOf(
                    Beat(6, show = setOf("phone", "api", "sql"), light = setOf("sql"), pulse = "api" to "sql", card = BoardCard("Two hundred milliseconds", "Restaurants, orders and ratings joined and ranked, every time.")),
                    Beat(8, show = setOf("redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("In-memory store", "Values in working memory, not on a disk: under a millisecond.")),
                    Beat(10, light = setOf("redis"), card = asideCard),
                ),
            ),
            // 11: "Aside, because the cache never goes and fetches."
            Scene(
                11, "How long it keeps", expiry(),
                beats = listOf(
                    Beat(12, show = setOf("key", "ttl"), light = setOf("ttl"), pulse = "key" to "ttl", card = ttlCard),
                    Beat(14, show = setOf("gone"), light = setOf("gone"), pulse = "ttl" to "gone", card = BoardCard("Sixty seconds", "Short for what the evening rewrites; longer for a menu page.")),
                ),
            ),
            // 15: "And when one closes, do we wait the minute out?"
            Scene(
                15, "The order matters", order(),
                beats = listOf(
                    Beat(16, show = setOf("write", "db", "del"), light = setOf("db", "del"), pulse = "db" to "del", card = orderCard),
                    Beat(18, show = setOf("stale"), light = setOf("stale"), pulse = "del" to "stale", card = BoardCard("Delete first", "A reader misses, reads a database that still says open, and refills it.")),
                    Beat(20, light = setOf("del"), card = netCard),
                ),
            ),
            // 21: "Now the dot on the map..."
            Scene(
                21, "The dot on the map", rider(),
                beats = listOf(
                    Beat(22, show = setOf("rider", "api", "redis", "cosmos"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("One number", "Where rider four-two-one is now, overwritten every three seconds.")),
                    Beat(24, light = setOf("redis"), card = BoardCard("One key, one write", "And a read that costs almost nothing, however many are watching.")),
                    Beat(26, show = setOf("phone"), light = setOf("cosmos"), card = BoardCard("Losing the cache", "The list rebuilds from SQL, the pings are in Cosmos, orders in SQL.")),
                    Beat(28, light = setOf("redis"), card = onlyCopyCard),
                ),
            ),
            // 29: "Mm. What happens when it runs out of room?"
            Scene(
                29, "Running out of room", memory(),
                beats = listOf(
                    Beat(30, show = setOf("full", "lru", "vol", "none"), light = setOf("lru", "vol", "none"), pulse = "full" to "none", card = evictCard),
                    Beat(32, show = setOf("refused"), light = setOf("refused"), pulse = "none" to "refused", card = noEvictCard),
                    Beat(34, light = setOf("none"), card = searchCard),
                    Beat(36, light = setOf("full"), card = BoardCard("Every key, and an eye", "A quiet fortnight of keys with no expiry fills it on its own.")),
                ),
            ),
            // 37: "And when it is genuinely too small?"
            Scene(
                37, "When it is too small", scale(),
                beats = listOf(
                    Beat(38, show = setOf("cache", "tier", "shards"), light = setOf("tier", "shards"), pulse = "cache" to "tier", card = BoardCard("A bigger tier", "Memory Optimized to hold a lot, Compute Optimized to serve a lot.")),
                    Beat(40, show = setOf("policy"), light = setOf("policy"), pulse = "cache" to "policy", card = clusterCard),
                ),
            ),
            // 41: "One more use before I fade. You mentioned limits."
            Scene(
                41, "The limit", limit(),
                beats = listOf(
                    Beat(42, show = setOf("user", "counter", "over", "exp"), light = setOf("counter", "over"), pulse = "counter" to "over", card = limitCard),
                    Beat(44, show = setOf("many"), light = setOf("many"), pulse = "counter" to "many", card = sharedCard),
                ),
            ),
            // 45: "Last thing. How does it get in, at this hour, without a password?"
            Scene(
                45, "Getting in", signin(),
                beats = listOf(
                    Beat(46, show = setOf("api", "mi", "entra"), light = setOf("mi"), pulse = "api" to "mi", card = noKeyCard),
                    Beat(48, show = setOf("user", "cache"), light = setOf("user"), pulse = "mi" to "user", card = redisUserCard),
                    Beat(50, light = setOf("api"), card = entraidCard),
                    Beat(52, show = setOf("pe"), light = setOf("pe"), pulse = "cache" to "pe", card = peCard),
                ),
            ),
            // 53: "Before I go. What do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("port", "order", "mem"), card = phraseCard),
                    Beat(56, light = setOf("mods", "usr", "net"), card = BoardCard("And three more", "The add-on at creation, the Redis user, the private endpoint.")),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-redis--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql", "cosmos"), light = setOf("sql"), card = BoardCard("Date night", "The one store in Zaiqa that keeps only what is true this minute.")),
                    Beat(2, show = setOf("hot"), light = setOf("hot"), card = BoardCard("Quiz: where do they go?", "The hot restaurant list, and the dot on your tracking map.")),
                    Beat(4, show = setOf("redis"), light = setOf("redis"), pulse = "redis" to "hot", card = BoardCard("Right answer", "Azure Managed Redis: values in working memory, not on a disk.")),
                ),
            ),
            // 5: "And the port, since the exam does love a port."
            Scene(
                5, "Walk me through it", aside(),
                beats = listOf(
                    Beat(6, show = setOf("phone", "api", "redis", "sql"), light = setOf("redis"), card = BoardCard("Port 10000", "The older Azure Cache for Redis answers on 6380.")),
                    Beat(7, light = setOf("redis", "sql"), pulse = "api" to "sql", card = asideCard),
                    Beat(8, show = setOf("ttl"), light = setOf("ttl"), pulse = "api" to "ttl", card = BoardCard("Right answer", "Aside, because the cache never fetches anything for you.")),
                ),
            ),
            // 9: "Now be strict with me. How long do I keep it?"
            Scene(
                9, "How long do I keep it", expiry(),
                beats = listOf(
                    Beat(10, show = setOf("key", "ttl", "gone"), light = setOf("ttl"), pulse = "key" to "ttl", card = ttlCard),
                ),
            ),
            // 11: "Exam style, then, love. A restaurant closes at ten past eight..."
            Scene(
                11, "In which order", order(),
                beats = listOf(
                    Beat(12, show = setOf("write", "db", "del", "stale"), light = setOf("db", "del"), pulse = "db" to "del", card = BoardCard("Right answer", "Update Azure SQL first, then delete the cached key.")),
                    Beat(13, light = setOf("stale"), pulse = "del" to "stale", card = BoardCard("Delete first", "A reader puts the old list back on top of your update.")),
                ),
            ),
            // 14: "Mm. And the expiry stays on anyway. Now the dot."
            Scene(
                14, "Why it belongs here", rider(),
                beats = listOf(
                    Beat(15, show = setOf("rider", "api", "redis", "cosmos"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Right answer", "One number, overwritten every three seconds, stale three later.")),
                    Beat(16, light = setOf("cosmos"), pulse = "api" to "cosmos", card = BoardCard("The history is in Cosmos", "Paying a database for the current value buys you nothing.")),
                    Beat(18, show = setOf("phone"), light = setOf("redis"), card = BoardCard("Losing the cache", "The list rebuilds from SQL, the pings are in Cosmos, orders in SQL.")),
                    Beat(20, light = setOf("redis"), card = onlyCopyCard),
                ),
            ),
            // 21: "Next. The memory fills up. What decides who gets thrown out?"
            Scene(
                21, "Who gets thrown out", memory(),
                beats = listOf(
                    Beat(22, show = setOf("full", "lru", "vol", "none", "refused"), light = setOf("lru", "vol", "none"), card = BoardCard("Right answer", "Least used, only keys with an expiry, or nothing at all.")),
                    Beat(25, light = setOf("none"), card = searchCard),
                    Beat(26, light = setOf("refused"), pulse = "none" to "refused", card = noEvictCard),
                ),
            ),
            // 27: "So every key gets an expiry. And if it is simply too small?"
            Scene(
                27, "If it is too small", scale(),
                beats = listOf(
                    Beat(28, show = setOf("cache", "tier", "shards"), light = setOf("tier", "shards"), pulse = "cache" to "shards", card = BoardCard("A bigger tier", "Memory Optimized to hold a lot, Compute Optimized to serve a lot.")),
                    Beat(30, show = setOf("policy"), light = setOf("policy"), pulse = "cache" to "policy", card = clusterCard),
                ),
            ),
            // 31: "One more from your list. Rate limiting..."
            Scene(
                31, "Rate limiting", limit(),
                beats = listOf(
                    Beat(32, show = setOf("user", "counter", "over", "exp"), light = setOf("counter", "over"), pulse = "counter" to "over", card = limitCard),
                    Beat(34, show = setOf("many"), light = setOf("many"), pulse = "counter" to "many", card = sharedCard),
                ),
            ),
            // 35: "With no access key at all..."
            Scene(
                35, "How does it get in", signin(),
                beats = listOf(
                    Beat(35, show = setOf("api", "mi", "entra"), light = setOf("mi"), pulse = "api" to "mi", card = BoardCard("Right answer", "Entra on by default; the API signs in with its managed identity.")),
                    Beat(38, show = setOf("user", "cache"), light = setOf("user"), pulse = "mi" to "user", card = redisUserCard),
                    Beat(40, show = setOf("pe"), light = setOf("pe"), pulse = "cache" to "pe", card = peCard),
                ),
            ),
            // 41: "Then the last one, love, and it is for the exam."
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("port", "order", "mem"), card = phraseCard),
                    Beat(44, light = setOf("mods", "usr", "net"), card = BoardCard("And three more", "The add-on at creation, the Redis user, the private endpoint.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
