package com.shahaabapps.core.content

/** Azure Managed Redis: caching and vector search: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesRedis {
    private val classic = Podcast(
        "ai-redis",
        listOf(
            maya("Hi, and welcome back. Today is about a fast memory that saves you time and money. Say you're building the Two Tracks Coach, a study helper in an Android app, with a small Python server on Azure, Microsoft's cloud. Every time a learner opens a note, the server builds a summary of it. Slowly. Again and again."),
            leo("Same note, same summary, same wait. [pause] Sounds wasteful."),
            maya("It is. So we keep a copy of the result somewhere very fast, close to the app. That copy is called a cache. Think of a sticky note on your desk, instead of walking to the filing cabinet down the hall every time."),
            leo("And where does the sticky note live?"),
            maya("In Redis. Redis is a store that keeps everything in the computer's fast working memory, not on its slower disk. So a read takes well under a thousandth of a second. You store a value under a name, called a key, and ask for it back by that name."),
            leo("A giant dictionary that lives in memory. And on Azure?"),
            maya("Azure Managed Redis. It's Redis that Microsoft runs for you, built on Redis Enterprise, the paid edition from the company behind Redis. It's where Microsoft points all new work."),
            leo("Is there an older one I might bump into?"),
            maya("Yes, Azure Cache for Redis. The exam likes to tell them apart by port. A port is like a door number on the server. The old service answers on door 6380. Azure Managed Redis answers on door 10000."),
            leo("Door 10000. Noted. How does my server get in?"),
            maya("By signing in with Entra ID, Microsoft's sign-in system, like a staff badge. It's on by default for a new cache, so you never copy a shared password into your app's settings."),
            leo("So the badge opens the door, and that's it?"),
            maya("One more step people miss. On the cache's Authentication page, you add your app's identity as a Redis user. A role on the Azure resource alone doesn't let it run commands. It's like having a staff badge, but not being on the list for that room."),
            leo("Okay, I'm in. How does the Coach actually use the cache?"),
            maya("With a pattern called cache-aside. Look in the cache first. If the summary's there, that's a hit: return it. If not, that's a miss: build it from the database, store a copy in the cache, then return it. The app does the loading; the cache just sits aside."),
            leo("And the copy stays forever?"),
            maya("It shouldn't. Every copy gets a TTL, time to live: an expiry, like the use-by date on milk. When it passes, Redis throws the copy away, and the next read builds a fresh one. For example, the Coach keeps summaries for one hour."),
            leo("But what if the learner edits the note five minutes in? The summary's wrong for 55 minutes."),
            maya("Then you remove the copy yourself. That's called invalidation. And the order matters. Update the database first, then delete the cached key. The next read finds nothing, and builds a fresh summary."),
            leo("Why that order? Delete first sounds tidier."),
            maya("Because a reader can sneak in between. You delete the key, a reader finds nothing, reads the old note from the database, and puts the old summary back. Then you update the database. Now the cache holds the old version, and nothing will remove it."),
            leo("[chuckle] Sneaky reader. So if I always delete after writing, why the TTL as well?"),
            maya("It's the safety net. One day a code path forgets to delete. With no expiry, that stale copy lives forever. With a TTL, it's wrong for an hour at most. Short TTLs for things that change often, longer ones for stable content. And never cache secrets."),
            leo("Got it. Now, you said this was an AI topic."),
            maya("Here it comes. The Coach pays a chat model for every answer. And learners ask the same things in different words. What is a DLQ? What is a dead-letter queue? Same question. A normal cache looks up the exact words, so it misses."),
            leo("So we need a cache that understands meaning."),
            maya("Exactly, a semantic cache. Semantic just means about meaning. It reuses a stored answer when a new question means the same thing, whatever the words."),
            leo("But how can a cache tell what a question means?"),
            maya("An AI model turns each question into an embedding, a list of numbers that captures what it means. Two questions that mean the same thing get lists that sit close together, like pins near each other on a map."),
            leo("And Redis can search those pins?"),
            maya("With an add-on, called a module. The one you need is RediSearch. It indexes data in Redis, including vector fields that hold those lists of numbers. And here's the big exam fact. In Azure Managed Redis, modules are chosen when you create the cache. You can't add them later."),
            leo("So if I forgot to tick RediSearch last month?"),
            maya("You create a new cache with RediSearch, and move across. There's no switch on the old one. RediSearch also needs one more setting at creation, the NoEviction policy. Eviction is Redis throwing keys out to make room when memory is full. NoEviction says never do that."),
            leo("And the vector field itself. Anything to choose?"),
            maya("Its search method. FLAT checks every stored vector, one by one. Exact, and fine for small sets."),
            leo("And for big sets?"),
            maya("HNSW. It's a web of links between near neighbours, so a search hops along the links and checks only a few. Approximate, but much faster on large sets."),
            leo("And how do I ask for the closest question?"),
            maya("With a KNN query. KNN means k nearest neighbours: give me the k closest stored vectors, where k is how many you want. For a semantic cache, k is one. You can add a normal filter too, such as a tag for the course."),
            leo("And if the closest one isn't that close?"),
            maya("Then it's a miss. You reuse the stored answer only when the distance is small, under 0.1 for the Coach, where lower means closer. Start strict, and loosen only after testing. Picture a small app where too loose a cache answers a different question. Not good."),
            leo("And could one learner get another learner's answer?"),
            maya("Only if you let them. Keep each entry on a TTL, and split the cache by user and course, and by chat model version. Then one person's answer never reaches another. The Coach turned a third of its questions into cache hits that way."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If vector search is needed on a cache made without modules, create a new one with RediSearch. If stale data could last forever, put a TTL on every key. On a write, update the database first, then delete the key. Exact by checking every vector is FLAT; fast on large sets is HNSW."),
            leo("And the fifth?"),
            maya("If the same question comes in other words, that's a semantic cache with a KNN query, filtered per user."),
            leo("Perfect. Anything to try today?"),
            maya("About fifteen minutes. Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid, the library that handles the sign-in, and run PING, which just answers PONG. Write a key with a 60-second TTL and watch TTL count down. Then create a small vector index and run a KNN query with two stored answers."),
            leo("A sticky note that throws itself away. Finally. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-redis",
        listOf(
            maya("Welcome to Case Files. Today's case: The Summary That Wouldn't Die. A small Android app called Quiz Quokka helps students revise, with a Python server on Azure, Microsoft's cloud. A student fixes a typo in her note. The app keeps showing the old summary, typo and all. For three days."),
            leo("[pause] Detective Leo is on the case. Where does the app keep its summaries?"),
            maya("Two places. The real notes live in PostgreSQL, a database on disk. That's the source of truth. And a copy of each summary sits in a cache, a fast store close to the app, like a sticky note on your desk instead of a trip to the filing cabinet."),
            leo("And the cache is what, exactly?"),
            maya("Azure Managed Redis. Redis keeps everything in the computer's fast working memory, so a read takes well under a thousandth of a second. Azure Managed Redis is Redis that Microsoft runs for you, built on Redis Enterprise, the paid edition."),
            leo("And under the picture? What is it really?"),
            maya("A giant dictionary held in memory: names, called keys, each pointing to a value. On Azure it's a small cluster of servers that Microsoft looks after. The app asks for summary 42, and gets the text back by that key."),
            leo("Then my first suspect: the cache is broken. Restart it."),
            maya("The cache did exactly what it was told. Look at how the app uses it. It's a pattern called cache-aside. Read the cache first. On a hit, return the copy. On a miss, build the summary from PostgreSQL, store a copy in Redis, and return it. The app does the loading."),
            leo("So where's the bug?"),
            maya("In the editing code. When the student saved her fix, the app first deleted the cached summary, then updated the database. Removing a stale copy is called invalidation. But in between those two steps, another request came in."),
            leo("[pause] Found nothing in the cache, read the old note, and put the old summary back."),
            maya("Detective Leo, finally. Then the database was updated, and the cache kept the old copy. The fix is the order: update the database first, then delete the key. The next read loads the new note."),
            leo("But three days? Even a bad copy should go off eventually."),
            maya("Second clue. The copy was stored with no expiry. Every key should get a TTL, time to live, like the use-by date on milk. Under the hood, Redis records an expiry time on the key and throws it away once the time passes. Here there was none, so the stale copy would have lived forever."),
            leo("So the TTL is the safety net when invalidation fails."),
            maya("Exactly. Quiz Quokka now keeps summaries for one hour. Even if one code path forgets to delete, the worst is an hour. And one rule while we're here: never cache secrets."),
            leo("Case closed?"),
            maya("A second case, same app. Quiz Quokka pays a chat model for every answer. Students ask, what is a DLQ, and what is a dead-letter queue. Same question, different words. A normal cache looks up the exact words and misses. So the team wanted a semantic cache, one that matches by meaning."),
            leo("How does a cache match by meaning?"),
            maya("An AI model turns each question into an embedding, a list of numbers that captures what it means. Questions that mean the same thing get lists that sit close together. The cache finds the closest stored question and reuses its answer, if it's close enough."),
            leo("And Redis can do that?"),
            maya("With a module, an add-on, called RediSearch. It indexes Redis data, including vector fields that hold those lists. So the developer opened the existing cache to switch RediSearch on. And couldn't."),
            leo("Suspect: wrong port. Switch to 6380."),
            maya("[chuckle] A port is a door number on the server. 6380 is the door of the older service, Azure Cache for Redis. Azure Managed Redis uses door 10000. Nothing to do with modules."),
            leo("Then what's the real clue?"),
            maya("In the docs. Modules are chosen when the cache is created, and can't be added later. RediSearch also needs a setting called NoEviction, chosen at creation too."),
            leo("NoEviction meaning?"),
            maya("Eviction is Redis throwing keys out to make room when memory is full. NoEviction says never. So the team created a new cache with RediSearch, and moved across."),
            leo("And how did the app sign in to the new one? I read ahead: permission errors."),
            maya("Good detective. It signs in with Entra ID, Microsoft's sign-in system, like a staff badge, not a shared password in its settings. But the app's identity only had a role on the Azure resource. It was never added as a Redis user on the cache's Authentication page, and a role alone doesn't let it run commands."),
            leo("And the search itself? How does it find the closest question?"),
            maya("With a KNN query. KNN means k nearest neighbours: return the k closest stored vectors. For a cache, k is one, the single closest question."),
            leo("And how does it search? Every vector?"),
            maya("That's FLAT, which checks every stored vector: exact, and fine for small sets. Quiz Quokka chose HNSW, a web of links between near neighbours, so a search checks only a few. Approximate, but faster on large sets."),
            leo("Any loose ends in the file?"),
            maya("One, and it's where a semantic cache stops working. A student once got another student's answer. The cache was shared with no filter. Split it by user and course, with a filter on the search, and by chat model version. And keep the closeness threshold strict, a distance under 0.1 here, where lower is closer, or it answers the wrong question."),
            leo("So the cache isn't a database."),
            maya("Never. PostgreSQL is the truth; Redis is the sticky note. If you want vector search in the database itself, the pgvector episode covers that."),
            leo("So, the case summary for the exam?"),
            maya("On a write, update the database first, then delete the key. Stale for days means no TTL. Vector search on a cache without modules means a new cache with RediSearch. Exact by checking every vector is FLAT; large and fast is HNSW. Same question, other words, is a semantic cache with a KNN query, split per user."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid, the library that handles the sign-in, and run PING, which just answers PONG. Write a key with a 60-second TTL and watch TTL count down. Then create a small vector index and run a KNN query with two stored answers."),
            leo("No more summaries from beyond the grave. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-redis",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? They added a cache to the Two Tracks Coach, the study helper in the Android app, with its Python server on Azure, Microsoft's cloud. Five days, five stories."),
            leo("[laugh] I heard someone called a summary a zombie in standup. What were they caching?"),
            maya("Two things. Note summaries, which are slow to build. And answers from the chat model, which cost money every time. A cache keeps a copy of a result somewhere fast, close to the app. Like a sticky note on your desk, instead of the filing cabinet down the hall."),
            leo("And the sticky note is Redis?"),
            maya("Azure Managed Redis. Redis keeps everything in the computer's fast working memory, so reads take well under a thousandth of a second. Azure Managed Redis is Redis that Microsoft runs for you, built on Redis Enterprise, the paid edition."),
            leo("And what is it really, under the picture?"),
            maya("A giant dictionary held in memory: names, called keys, each pointing to a value. Summary 42, say, and its text. Microsoft runs it on a small cluster of servers. Okay. Monday. Tom."),
            leo("Go on."),
            maya("Tom wrote the summary code with cache-aside. Look in the cache first. On a hit, return the copy. On a miss, build the summary from the database, store a copy, return it. Textbook. Then he wrote the edit code."),
            leo("Uh oh."),
            maya("When a note changed, Tom deleted the cached copy first, then updated the database. Removing a stale copy is called invalidation. But a reader slipped in between, found nothing, read the old note, and cached the old summary again."),
            leo("So the fix is the other order."),
            maya("Database first, then delete the key. Which brings us to Tuesday, and Dana. Dana found Tom's stale summary. Still there on Tuesday. Because nothing ever expires it."),
            leo("Things don't expire by themselves?"),
            maya("Only if you give them a TTL, time to live, like the use-by date on milk. Redis records an expiry time on the key and throws it away once the time passes. Tom's keys had none. Dana put a one-hour TTL on every summary. Now even a missed delete is wrong for an hour at most."),
            leo("Dana, saving the day. Wednesday?"),
            maya("Wednesday was Leila, and the chat answers. Learners ask, what is a DLQ, and what is a dead-letter queue. Same question, different words. A normal cache matches exact words and misses. Leila wanted a semantic cache, one that matches by meaning."),
            leo("How does a cache know meaning?"),
            maya("An AI model turns each question into an embedding, a list of numbers that captures what it means. Questions that mean the same land close together, like pins near each other on a map. Redis finds the nearest stored question, and if it's close enough, reuses its answer."),
            leo("I sense a but."),
            maya("To search those lists of numbers, called vectors, Redis needs a module, an add-on, called RediSearch. Leila went to switch it on. And in Azure Managed Redis, modules are chosen when the cache is created. You can't add them later."),
            leo("[sigh] So, a whole new cache."),
            maya("A whole new cache, with RediSearch ticked, and the NoEviction policy it needs. Eviction is Redis throwing keys out when memory is full. NoEviction says never; it refuses new writes instead. That's also where Redis stops working as a store. If memory fills, writes fail. So TTLs matter twice."),
            leo("Thursday. Let me guess. The intern."),
            maya("The intern. Bless them. They couldn't connect, so they copied the cache's access key, a shared password, into the app settings."),
            leo("Isn't that how you connect?"),
            maya("Not any more. Entra ID, Microsoft's sign-in system, like a staff badge, is the default on a new cache. No shared passwords needed."),
            leo("Then why couldn't they connect?"),
            maya("Because the app's identity had a role on the Azure resource, but was never added as a Redis user on the cache's Authentication page. A role alone doesn't let it run commands. Add it there, and a library called redis-entraid handles the sign-in from Python. The key went back in the drawer."),
            leo("And Friday? There's always a Friday."),
            maya("Friday was Sam. Sam built the search: a KNN query. KNN means k nearest neighbours: give me the k closest stored vectors, here just the one closest question."),
            leo("And how does it search? Every single vector?"),
            maya("That's FLAT: it checks every stored vector, exact, fine for small sets. Sam picked HNSW, a web of links between near neighbours, so a search checks only a few. Fast, and approximate. All fine so far."),
            leo("So what went wrong?"),
            maya("Two things. Sam set the closeness threshold very loose, so a question about queues got an answer about topics. Start strict, a distance under 0.1, where lower is closer. And the cache was shared across learners with no filter. One learner saw another's answer."),
            leo("Ouch. The fix?"),
            maya("Split the cache by user and course, with a filter on the search, and by chat model version, with a TTL on every entry. By Friday evening, a third of questions were cache hits. If you want vector search in the database itself, the pgvector episode does that."),
            leo("So, the gossip I should remember for the exam?"),
            maya("On a write, database first, then delete the key. Stale forever means no TTL. Vector search on a cache without modules means a new cache with RediSearch. Exact by checking every vector is FLAT; fast on large sets is HNSW. Same question, other words, is a semantic cache with a KNN query, filtered per user."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run PING, which just answers PONG. Write a key with a 60-second TTL and watch TTL count down. Then create a small vector index and run a KNN query with two stored answers."),
            leo("No zombies in my cache. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-redis",
        listOf(
            nova("Hey, you. It's late, and you're still here, still learning. [pause] Tonight, Ivy and I are talking about memory. The fast kind. The kind that forgets on purpose."),
            ivy("Forgets on purpose. Mysterious, Nova. Go on."),
            nova("Picture the Two Tracks Coach, the study helper in the Android app, with a Python server on Azure, Microsoft's cloud. A learner opens a note, and the server slowly builds a summary. They open it again. [pause] It builds the same summary, again."),
            ivy("That's no way to treat a learner at this hour."),
            nova("So we keep a copy. A cache: a copy of a result kept somewhere fast, close to the app. Like a sticky note on your bedside table, instead of the filing cabinet downstairs."),
            ivy("And where does the sticky note live?"),
            nova("In Redis. It keeps everything in the computer's fast working memory, so a read takes well under a thousandth of a second. Azure Managed Redis is Redis that Microsoft runs for you, built on Redis Enterprise, the paid edition."),
            ivy("Tell me what it really is, under the picture."),
            nova("A giant dictionary held in memory. Names, called keys, each pointing to a value. Summary 42, and its text. [pause] On Azure, a small cluster of servers Microsoft looks after, so you don't have to."),
            ivy("And how does the Coach use it?"),
            nova("A pattern called cache-aside. Look in the cache first. If the copy is there, a hit, return it. If not, a miss: build it from the database, store a copy, return it. The app does the loading. The cache waits to one side."),
            ivy("And the copy stays forever? Some things should."),
            nova("Not this. Every copy gets a TTL, time to live, like the use-by date on milk. Redis notes an expiry time on the key, and once it passes, quietly lets it go. The Coach keeps summaries for an hour."),
            ivy("And if the learner edits the note in the middle of that hour?"),
            nova("Then you remove the copy yourself. That's invalidation. Update the database first. Then delete the key. [pause] The other way round, a reader can slip in between, find nothing, read the old note, and put the old summary back."),
            ivy("[sigh] Old feelings, cached again. So why a TTL as well?"),
            nova("A safety net. One day, a code path forgets to delete. With a TTL, the stale copy fades within the hour. Without one, it lingers forever. And never cache secrets."),
            ivy("Now the part with AI in it. I can feel it."),
            nova("The Coach pays a chat model for every answer. And learners ask the same thing in different words. What is a DLQ? What is a dead-letter queue? A normal cache matches the exact words, so it misses."),
            ivy("So you want a cache that understands what I mean, not what I say."),
            nova("A semantic cache. Semantic means about meaning. It reuses a stored answer when a new question means the same thing, [pause] whatever words you whisper."),
            ivy("And how does it know what I mean?"),
            nova("An AI model turns each question into an embedding, a list of numbers that captures what it means. Questions that mean the same thing sit close together, like two pins near each other on a map."),
            ivy("And Redis searches the pins?"),
            nova("With a module, an add-on, called RediSearch. It indexes data in Redis, including vector fields, which hold those lists of numbers. But listen. [pause] In Azure Managed Redis, modules are chosen when you create the cache. You can't add them later."),
            ivy("No changing your mind afterwards. Strict."),
            nova("Strict. Forget it, and you create a new cache and move across. RediSearch also needs the NoEviction policy. Eviction is Redis throwing keys out when memory is full. NoEviction says never, and refuses new writes instead. That's where it stops working: a full cache stops accepting. Another reason for TTLs."),
            ivy("And how does it find the nearest pin?"),
            nova("A KNN query. KNN means k nearest neighbours: give me the k closest stored vectors. For a cache, just one, the single closest question."),
            ivy("And does it look at every pin to find it?"),
            nova("It can. That's FLAT: it checks every stored vector, exact, fine for small sets. Or HNSW, a web of links between near neighbours. A search hops along the links, [pause] checking only a few. Approximate, and faster on large sets."),
            ivy("And if the closest isn't close enough?"),
            nova("Then it's a miss, and you ask the chat model. Reuse an answer only when the distance is small, under 0.1 here, where lower is closer. Start strict. [pause] And split the cache by user and course, and by chat model version, so one person's answer never reaches another."),
            ivy("Mm. And how do I get in, at this hour?"),
            nova("With Entra ID, Microsoft's sign-in system, like a staff badge. It's on by default, so there's no shared password in your settings."),
            ivy("A badge. And the badge opens every door?"),
            nova("Not quite. Add your app's identity as a Redis user on the cache's Authentication page; a role on the Azure resource alone doesn't let it run commands. [pause] From Python, a library called redis-entraid handles the sign-in."),
            ivy("And if I want vectors that stay, not ones that fade?"),
            nova("Then the pgvector episode is your next late night. Vector search in the database itself. The cache is only ever the sticky note."),
            ivy("Before you send me to bed. What do I remember for the exam?"),
            nova("On a write, the database first, then delete the key. Stale forever means no TTL. Vector search on a cache without modules: a new cache with RediSearch. Exact by checking every vector is FLAT; fast on large sets is HNSW. Same question, other words: a semantic cache with a KNN query, filtered per user."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run PING, which answers PONG. [pause] Write a key with a 60-second TTL and watch TTL count down. Then create a small vector index and run a KNN query with two stored answers."),
            ivy("Sixty seconds, and then it lets go. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-redis",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and a topic all about memory. Azure Managed Redis."),
            rafe("Memory. Do you remember our first date?"),
            nova("[laugh] Every detail, cached. Picture the Two Tracks Coach, the study helper in the Android app, with a Python server on Azure, Microsoft's cloud. Opening a note means building a summary, slowly, every single time."),
            rafe("So keep a copy close. I know the feeling."),
            nova("That's a cache: a copy of a result kept somewhere fast, close to the app. Like a sticky note on the fridge, instead of the filing cabinet in the attic."),
            rafe("And Redis is the fridge?"),
            nova("Redis keeps everything in the computer's fast working memory, so a read takes well under a thousandth of a second. Under the picture, it's a giant dictionary: names, called keys, each pointing to a value."),
            rafe("A dictionary that lives in memory. And on Azure?"),
            nova("Azure Managed Redis. It's Redis that Microsoft runs for you, built on Redis Enterprise, the paid edition. A small cluster of servers someone else looks after. Very low maintenance."),
            rafe("Unlike some people. So how does the Coach use it?"),
            nova("A pattern called cache-aside. Look in the cache first. If it's there, a hit: return it. If not, a miss: build it from the database, store a copy, return it. The app does the loading; the cache waits to one side."),
            rafe("And the copy stays forever?"),
            nova("No. Every copy gets a TTL, time to live, like a use-by date. Redis notes when it expires, and throws it away after."),
            rafe("Then let me test you, darling. Exam style. A learner edits a note. What do you do, and in which order?"),
            nova("Update the database first, then delete the cached key. That removal is called invalidation. Delete first, and a reader can sneak in between and cache the old version all over again."),
            rafe("Right answer. Timing is everything. [kiss]"),
            nova("Mm. My turn. If you always delete after writing, why give every key an expiry as well?"),
            rafe("Because one day a code path forgets to delete. With no TTL, the stale copy lasts forever. With one, it's wrong for an hour at most. A safety net."),
            nova("Right answer. Nothing should overstay its welcome. [kiss]"),
            rafe("Except me. Now, the AI part. The Coach pays a chat model for each answer. And learners say, what is a DLQ, or what is a dead-letter queue. Same question. A normal cache misses."),
            nova("So you want a semantic cache. Semantic means about meaning: it reuses a stored answer when a new question means the same thing, whatever the words."),
            rafe("Whatever the words. So how does it know what I mean?"),
            nova("An AI model turns each question into an embedding, a list of numbers that captures what it means. Questions that mean the same thing sit close together, like two pins near each other on a map."),
            rafe("Close together. My favourite place. And Redis finds the close ones how?"),
            nova("With a module, an add-on, called RediSearch, which indexes vector fields, the ones holding those lists of numbers. So, my question, Rafe. You made a cache last month without modules. Now you want vector search. What do you do?"),
            rafe("Switch RediSearch on. Easy."),
            nova("No kiss for that one. In Azure Managed Redis, modules are chosen when you create the cache. You can't add them later. You create a new cache with RediSearch and move across. It also needs the NoEviction policy, so Redis never throws keys out to make room."),
            rafe("Commitment from the very start. I can respect that. My question, then. Which vector search method gives exact results by checking every stored vector?"),
            nova("FLAT. It checks every stored vector, one by one: exact, and fine for small sets."),
            rafe("Right answer. You never skip anyone. [kiss]"),
            nova("And for big sets, HNSW: a web of links between near neighbours, so a search hops along and checks only a few. Approximate, but faster."),
            rafe("Fast and approximate. Like my cooking. And how do you ask for the nearest question?"),
            nova("A KNN query. KNN means k nearest neighbours: the k closest stored vectors. For a cache, just one. And it only counts as a hit when the distance is small, under 0.1 here, where lower is closer."),
            rafe("Here's a hard one, then. Learners start seeing each other's answers. What's missing?"),
            nova("A filter. Split the cache by user and course, with a filter on the search, and by chat model version, with a TTL on every entry. Then one person's answer never reaches another."),
            rafe("Right answer. Some things are just between us. [kiss]"),
            nova("Last practical one. How does your server sign in to the cache, without a password in its settings?"),
            rafe("With Entra ID, Microsoft's sign-in system, like a staff badge. It's on by default, so no shared password sits in the settings."),
            nova("And is the badge enough on its own?"),
            rafe("No. The app's identity must be added as a Redis user on the cache's Authentication page; a role on the Azure resource alone can't run commands. In Python, a library called redis-entraid handles the sign-in."),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("A cache is never the truth; the database is. With NoEviction, a full cache refuses new writes, so TTLs matter. A loose threshold answers the wrong question. And for vectors that stay, the pgvector episode does vector search in the database itself."),
            nova("Then the last question, for the exam. What do we remember?"),
            rafe("On a write, database first, then delete the key. Stale forever means no TTL. Vector search on a cache without modules: a new cache with RediSearch. Exact by checking every vector is FLAT; fast on large sets is HNSW. Same question, other words: a semantic cache with a KNN query, filtered per user."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a Managed Redis cache with RediSearch and add yourself as a Redis user. Connect from Python with redis-entraid and run PING, which answers PONG. Write a key with a 60-second TTL and watch TTL count down. Then create a small vector index and run a KNN query with two stored answers."),
            rafe("Sixty seconds? I'd never let you expire. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun desk() = DiagramSpec(
        "A cache is the sticky note on your desk: a fast copy in memory, instead of a trip to the slow filing cabinet.",
        nodes = listOf(
            DNode("app", "Coach API", 0.25f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "Python"),
            DNode("note", "Sticky note", 0.75f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.PILL, sub = "Redis: memory"),
            DNode("cabinet", "Filing cabinet", 0.75f, 0.75f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 2, sub = "PostgreSQL: disk"),
        ),
        edges = listOf(
            DEdge("app", "note", "close, fast"),
            DEdge("app", "cabinet", "far, slow"),
        ),
        aspect = 0.8f,
    )

    private fun cacheAside() = DiagramSpec(
        "Cache-aside: look in Redis first, build from the database on a miss, and store the copy with a TTL.",
        nodes = listOf(
            DNode("api", "Coach API", 0.25f, 0.2f, w = 0.4f, h = 0.2f, tone = 1, sub = "Python"),
            DNode("redis", "Managed Redis", 0.75f, 0.22f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "in memory"),
            DNode("ttl", "TTL", 0.25f, 0.75f, w = 0.36f, h = 0.2f, kind = NodeKind.PILL, sub = "use-by date"),
            DNode("db", "PostgreSQL", 0.75f, 0.75f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 2, sub = "source of truth"),
        ),
        edges = listOf(
            DEdge("api", "redis", "1 look first"),
            DEdge("api", "db", "2 on a miss"),
            DEdge("ttl", "redis", "expires"),
        ),
        aspect = 0.8f,
    )

    private fun invalidation() = DiagramSpec(
        "On an edit, update the database first, then delete the key; the other order lets a reader cache the old copy.",
        nodes = listOf(
            DNode("write", "Edit the note", 0.25f, 0.15f, w = 0.44f, h = 0.2f, tone = 1),
            DNode("db", "Update database", 0.25f, 0.5f, w = 0.44f, h = 0.2f, sub = "first"),
            DNode("del", "Delete the key", 0.25f, 0.85f, w = 0.44f, h = 0.2f, sub = "then"),
            DNode("reader", "A sneaky reader", 0.75f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "the wrong order"),
            DNode("redis", "Cached copy", 0.75f, 0.85f, w = 0.44f, h = 0.24f, kind = NodeKind.CYLINDER),
        ),
        edges = listOf(
            DEdge("write", "db", "first"),
            DEdge("db", "del", "then"),
            DEdge("del", "redis", "removes"),
            DEdge("reader", "redis", "old copy back", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun signin() = DiagramSpec(
        "The API signs in with Entra ID, and its identity is added as a Redis user on the cache's Authentication page.",
        nodes = listOf(
            DNode("app", "Coach API", 0.25f, 0.2f, w = 0.4f, h = 0.2f, tone = 1, sub = "Python"),
            DNode("entra", "Entra ID", 0.75f, 0.2f, w = 0.4f, h = 0.2f, tone = 2, sub = "staff badge"),
            DNode("user", "Redis user", 0.25f, 0.75f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "Authentication page"),
            DNode("cache", "Managed Redis", 0.75f, 0.75f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "port 10000"),
        ),
        edges = listOf(
            DEdge("app", "entra", "token"),
            DEdge("app", "cache", "redis-entraid"),
            DEdge("user", "cache", "allowed"),
        ),
        aspect = 0.8f,
    )

    private fun meaningR() = DiagramSpec(
        "Two questions in different words get nearby embeddings, so a semantic cache can reuse the stored answer.",
        nodes = listOf(
            DNode("q", "What is a DLQ?", 0.25f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "new question"),
            DNode("q2", "Dead-letter queue?", 0.25f, 0.78f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "stored question"),
            DNode("emb", "Embedding model", 0.75f, 0.18f, w = 0.44f, h = 0.2f, tone = 2, sub = "text to numbers"),
            DNode("pins", "Nearby pins", 0.75f, 0.78f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "same meaning"),
        ),
        edges = listOf(
            DEdge("q", "emb", "embed"),
            DEdge("q2", "emb", "embed"),
            DEdge("emb", "pins"),
        ),
        aspect = 0.8f,
    )

    private fun modules() = DiagramSpec(
        "Modules are chosen when the cache is created: forget RediSearch, and you move to a new cache.",
        nodes = listOf(
            DNode("old", "Old cache", 0.25f, 0.2f, w = 0.44f, h = 0.22f, tone = 2, sub = "no modules"),
            DNode("new", "New cache", 0.75f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "chosen at creation"),
            DNode("rs", "RediSearch", 0.25f, 0.75f, w = 0.44f, h = 0.22f, sub = "vector search"),
            DNode("ne", "NoEviction", 0.75f, 0.75f, w = 0.44f, h = 0.22f, sub = "never throw keys out"),
        ),
        edges = listOf(
            DEdge("old", "new", "move across"),
            DEdge("new", "rs"),
            DEdge("new", "ne"),
        ),
        aspect = 0.8f,
    )

    private fun methods() = DiagramSpec(
        "A vector field searches with FLAT, exact over every vector, or HNSW, approximate and faster on large sets.",
        nodes = listOf(
            DNode("field", "Vector field", 0.5f, 0.2f, w = 0.6f, h = 0.22f, tone = 1, sub = "in RediSearch"),
            DNode("flat", "FLAT", 0.25f, 0.72f, w = 0.44f, h = 0.24f, sub = "checks every one"),
            DNode("hnsw", "HNSW", 0.75f, 0.72f, w = 0.44f, h = 0.24f, sub = "web of neighbours"),
        ),
        edges = listOf(DEdge("field", "flat"), DEdge("field", "hnsw")),
        aspect = 0.8f,
    )

    private fun knnSearch() = DiagramSpec(
        "A KNN query finds the closest stored question; reuse its answer only when the distance is small.",
        nodes = listOf(
            DNode("qv", "Question vector", 0.25f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "embedded"),
            DNode("idx", "RediSearch index", 0.75f, 0.18f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "vector field"),
            DNode("knn", "KNN query", 0.5f, 0.5f, w = 0.5f, h = 0.2f, sub = "k = 1, with a filter"),
            DNode("hit", "Close: reuse", 0.25f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "distance under 0.1"),
            DNode("miss", "Far: ask the model", 0.75f, 0.84f, w = 0.44f, h = 0.2f, tone = 2, sub = "then store it"),
        ),
        edges = listOf(
            DEdge("qv", "knn"),
            DEdge("knn", "idx", "search"),
            DEdge("knn", "hit"),
            DEdge("knn", "miss"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where a cache stops: it is never the truth, a full NoEviction cache refuses writes, and a loose threshold misleads.",
        nodes = listOf(
            DNode("truth", "Not the truth", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "the database is"),
            DNode("full", "NoEviction", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 2, sub = "full: writes refused"),
            DNode("loose", "Loose threshold", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "wrong answer"),
            DNode("link", "pgvector episode", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "vectors that stay"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Redis questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("order", "On a write", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "database, then delete"),
            DNode("ttl", "Stale forever", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TTL on every key"),
            DNode("mods", "No modules", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "new cache, RediSearch"),
            DNode("flat", "Every vector, exact", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "FLAT"),
            DNode("fast", "Large and fast", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "HNSW"),
            DNode("sem", "Other words", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "semantic cache, KNN"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("order", "ttl", "mods", "flat", "fast", "sem")
    private val asideCode = BoardCard("Python", "v = r.get(key)  # on a miss: v = load(); r.set(key, v, ex=3600)", code = true)
    private val writeCode = BoardCard("Python", "save_note(note_id, body)  # database first\nr.delete(key)  # then the cache", code = true)
    private val knnCode = BoardCard("Query", "(@course:{ai200})=>[KNN 1 @embedding \$vec AS score]", code = true)
    private val today = BoardCard("Today", "A cache with RediSearch, redis-entraid and PING, a 60-second TTL, a KNN query.")

    private fun classicBoard() = PodcastBoard(
        "ai-redis",
        listOf(
            // 0: "Hi, and welcome back. Today is about a fast memory..."
            Scene(
                0, "The sticky note", desk(),
                beats = listOf(
                    Beat(0, show = setOf("app", "cabinet"), light = setOf("cabinet"), pulse = "app" to "cabinet", card = BoardCard("Slow summaries", "Built from the database every time a note is opened.")),
                    Beat(2, show = setOf("note"), light = setOf("note"), pulse = "app" to "note", card = BoardCard("Cache", "A copy kept close and fast: the sticky note on your desk.")),
                    Beat(4, light = setOf("note"), card = BoardCard("Redis", "Everything in fast memory; values stored under names called keys.")),
                    Beat(6, card = BoardCard("Azure Managed Redis", "Redis that Microsoft runs for you, built on Redis Enterprise.")),
                ),
            ),
            // 7: "Is there an older one I might bump into?"
            Scene(
                7, "Getting in", signin(),
                beats = listOf(
                    Beat(8, show = setOf("cache"), light = setOf("cache"), card = BoardCard("Port 10000", "A port is a door number. The older Azure Cache for Redis uses 6380.")),
                    Beat(10, show = setOf("app", "entra"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Entra ID", "Microsoft's sign-in, like a staff badge. No shared password.")),
                    Beat(12, show = setOf("user"), light = setOf("user"), pulse = "user" to "cache", card = BoardCard("Redis user", "Add the identity on the Authentication page; a role alone can't.")),
                ),
            ),
            // 13: "Okay, I'm in. How does the Coach actually use the cache?"
            Scene(
                13, "Cache-aside", cacheAside(),
                beats = listOf(
                    Beat(13, show = setOf("api", "redis", "db")),
                    Beat(14, light = setOf("redis", "db"), pulse = "api" to "redis", card = asideCode),
                    Beat(16, show = setOf("ttl"), light = setOf("ttl"), pulse = "ttl" to "redis", card = BoardCard("TTL", "Time to live: a use-by date. Redis throws the copy away after.")),
                ),
            ),
            // 17: "But what if the learner edits the note five minutes in?..."
            Scene(
                17, "Invalidation", invalidation(),
                beats = listOf(
                    Beat(17, show = setOf("write")),
                    Beat(18, show = setOf("db", "del"), light = setOf("db", "del"), pulse = "db" to "del", card = writeCode),
                    Beat(20, show = setOf("reader", "redis"), light = setOf("reader"), pulse = "reader" to "redis", card = BoardCard("The other order", "Delete first, and a reader slips in and caches the old summary.")),
                    Beat(22, light = setOf("del"), card = BoardCard("TTL as safety net", "A missed delete is stale for an hour, not forever. Never cache secrets.")),
                ),
            ),
            // 23: "Got it. Now, you said this was an AI topic."
            Scene(
                23, "Same question, other words", meaningR(),
                beats = listOf(
                    Beat(24, show = setOf("q", "q2"), light = setOf("q", "q2"), card = BoardCard("A DLQ? A dead-letter queue?", "Same question, other words. An exact-word cache misses.")),
                    Beat(26, card = BoardCard("Semantic cache", "Reuse a stored answer when a new question means the same thing.")),
                    Beat(28, show = setOf("emb", "pins"), light = setOf("pins"), pulse = "emb" to "pins", card = BoardCard("Embedding", "Numbers that capture meaning; the same meaning lands close.")),
                ),
            ),
            // 29: "And Redis can search those pins?"
            Scene(
                29, "Chosen at creation", modules(),
                beats = listOf(
                    Beat(30, show = setOf("new", "rs"), light = setOf("rs"), pulse = "new" to "rs", card = BoardCard("RediSearch", "The module that indexes vector fields. Chosen at creation only.")),
                    Beat(32, show = setOf("old", "ne"), light = setOf("old", "ne"), pulse = "old" to "new", card = BoardCard("No switch later", "Forgot it? A new cache. NoEviction: never throw keys out.")),
                ),
            ),
            // 33: "And the vector field itself. Anything to choose?"
            Scene(
                33, "FLAT or HNSW", methods(),
                beats = listOf(
                    Beat(34, show = setOf("field", "flat"), light = setOf("flat"), pulse = "field" to "flat", card = BoardCard("FLAT", "Checks every stored vector: exact, fine for small sets.")),
                    Beat(36, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "field" to "hnsw", card = BoardCard("HNSW", "A web of near neighbours: approximate, faster on large sets.")),
                ),
            ),
            // 37: "And how do I ask for the closest question?"
            Scene(
                37, "The KNN query", knnSearch(),
                beats = listOf(
                    Beat(38, show = setOf("qv", "knn", "idx"), light = setOf("knn"), pulse = "knn" to "idx", card = knnCode),
                    Beat(40, show = setOf("hit", "miss"), light = setOf("hit", "miss"), pulse = "knn" to "hit", card = BoardCard("Close enough?", "Reuse only under a distance of 0.1; lower is closer. Start strict.")),
                    Beat(42, light = setOf("knn"), card = BoardCard("Split it", "By user, course and chat model version, each entry on a TTL.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("mods", "ttl", "order", "flat", "fast"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: new cache, TTL, order, method.")),
                    Beat(46, light = setOf("sem"), card = BoardCard("Other words", "Same question, other words: a semantic cache with a KNN query.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-redis--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Summary That Wouldn't Die..."
            Scene(
                0, "The Summary That Wouldn't Die",
                DiagramSpec(
                    "Quiz Quokka keeps showing an old summary from its cache, three days after the note was fixed.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Quiz Quokka"),
                        DNode("api", "Python server", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "on Azure"),
                        DNode("stale", "Old summary", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "for three days"),
                        DNode("redis", "Managed Redis", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "open note"),
                        DEdge("api", "redis", "cache"),
                        DEdge("redis", "stale", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "stale"), light = setOf("stale"), pulse = "phone" to "api", card = BoardCard("The case", "A typo fixed, and the old summary still shows. For three days.")),
                    Beat(2, show = setOf("redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Cache", "A fast copy close to the app; PostgreSQL holds the truth.")),
                ),
            ),
            // 3: "And the cache is what, exactly?"
            Scene(
                3, "Azure Managed Redis", desk(),
                beats = listOf(
                    Beat(4, show = setOf("app", "note"), light = setOf("note"), pulse = "app" to "note", card = BoardCard("Azure Managed Redis", "Redis run by Microsoft: everything in fast memory.")),
                    Beat(6, light = setOf("note"), card = BoardCard("Under the picture", "A giant dictionary of keys and values, on servers Microsoft runs.")),
                    Beat(8, show = setOf("cabinet"), light = setOf("note", "cabinet"), pulse = "app" to "cabinet", card = asideCode),
                ),
            ),
            // 9: "So where's the bug?"
            Scene(
                9, "The editing code", invalidation(),
                beats = listOf(
                    Beat(10, show = setOf("write", "del"), light = setOf("del"), card = BoardCard("Delete first", "The app deleted the cached copy, then updated the database.")),
                    Beat(11, show = setOf("reader", "redis"), light = setOf("reader"), pulse = "reader" to "redis", card = BoardCard("The sneaky reader", "Found nothing, read the old note, cached the old summary.")),
                    Beat(12, show = setOf("db"), light = setOf("db", "del"), pulse = "db" to "del", card = writeCode),
                ),
            ),
            // 13: "But three days? Even a bad copy should go off eventually."
            Scene(
                13, "The second clue", cacheAside(),
                beats = listOf(
                    Beat(14, show = setOf("ttl"), light = setOf("ttl"), pulse = "ttl" to "redis", card = BoardCard("No expiry", "Every key needs a TTL: Redis drops it once the time passes.")),
                    Beat(16, light = setOf("ttl", "db"), card = BoardCard("One hour, at most", "A missed delete is stale for an hour. Never cache secrets.")),
                ),
            ),
            // 17: "Case closed?"
            Scene(
                17, "The second case", meaningR(),
                beats = listOf(
                    Beat(18, show = setOf("q", "q2"), light = setOf("q", "q2"), card = BoardCard("Semantic cache", "Same question, other words: match by meaning, not exact words.")),
                    Beat(20, show = setOf("emb", "pins"), light = setOf("pins"), pulse = "emb" to "pins", card = BoardCard("Embedding", "Numbers that capture meaning; the same meaning lands close.")),
                ),
            ),
            // 21: "And Redis can do that?"
            Scene(
                21, "Suspect: the wrong port", modules(),
                beats = listOf(
                    Beat(22, show = setOf("old"), light = setOf("old"), card = BoardCard("RediSearch", "A module that indexes vector fields. The old cache had none.")),
                    Beat(24, card = BoardCard("Suspect: port 6380", "That's the older Azure Cache for Redis. Managed Redis uses 10000.")),
                    Beat(26, show = setOf("new", "rs"), light = setOf("rs"), pulse = "new" to "rs", card = BoardCard("The clue", "Modules are chosen at creation and can't be added later.")),
                    Beat(28, show = setOf("ne"), light = setOf("ne", "new"), pulse = "old" to "new", card = BoardCard("NoEviction", "Never throw keys out to make room. Then move to the new cache.")),
                ),
            ),
            // 29: "And how did the app sign in to the new one?..."
            Scene(
                29, "Permission errors", signin(),
                beats = listOf(
                    Beat(29, show = setOf("app", "cache"), pulse = "app" to "cache"),
                    Beat(30, show = setOf("entra", "user"), light = setOf("user"), pulse = "user" to "cache", card = BoardCard("Redis user", "Entra ID signs it in; the Authentication page lets it run commands.")),
                ),
            ),
            // 31: "And the search itself? How does it find the closest question?"
            Scene(
                31, "The KNN query", knnSearch(),
                beats = listOf(
                    Beat(32, show = setOf("qv", "knn", "idx"), light = setOf("knn"), pulse = "knn" to "idx", card = BoardCard("KNN query", "k nearest neighbours: the k closest stored vectors. Here, k is one.")),
                ),
            ),
            // 33: "And how does it search? Every vector?"
            Scene(
                33, "FLAT or HNSW", methods(),
                beats = listOf(
                    Beat(34, show = setOf("field", "flat", "hnsw"), light = setOf("hnsw"), pulse = "field" to "hnsw", card = BoardCard("FLAT or HNSW", "FLAT checks every vector, exactly. HNSW: near neighbours, faster.")),
                ),
            ),
            // 35: "Any loose ends in the file?"
            Scene(
                35, "Loose ends", limits(),
                beats = listOf(
                    Beat(36, show = setOf("loose", "full"), light = setOf("loose"), card = BoardCard("Another student's answer", "Split by user, course and model version. Keep the threshold strict.")),
                    Beat(38, show = setOf("truth", "link"), light = setOf("truth", "link"), card = BoardCard("Not a database", "PostgreSQL is the truth. The pgvector episode: vectors that stay.")),
                ),
            ),
            // 39: "So, the case summary for the exam?"
            Scene(
                39, "Case closed", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("order", "ttl", "mods"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: order, TTL, new cache, method.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-redis--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "What the Coach team cached", desk(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Five days, five stories", "The Coach team added a cache. It was a week.")),
                    Beat(2, show = setOf("note", "cabinet"), light = setOf("note"), pulse = "app" to "note", card = BoardCard("Cache", "A copy kept close and fast: the sticky note on your desk.")),
                    Beat(4, light = setOf("note"), card = BoardCard("Azure Managed Redis", "Redis run by Microsoft: everything in fast memory.")),
                    Beat(6, card = BoardCard("Under the picture", "A giant dictionary of keys and values, on servers Microsoft runs.")),
                ),
            ),
            // 7: "Go on."
            Scene(
                7, "Monday: Tom", invalidation(),
                beats = listOf(
                    Beat(8, show = setOf("write"), card = BoardCard("Cache-aside", "Cache first; on a miss, build from the database and store a copy.")),
                    Beat(10, show = setOf("del", "reader", "redis"), light = setOf("reader"), pulse = "reader" to "redis", card = BoardCard("Delete first", "A reader slipped in between and cached the old summary again.")),
                    Beat(12, show = setOf("db"), light = setOf("db", "del"), pulse = "db" to "del", card = writeCode),
                ),
            ),
            // 13: "Things don't expire by themselves?"
            Scene(
                13, "Tuesday: Dana", cacheAside(),
                beats = listOf(
                    Beat(14, light = setOf("ttl"), pulse = "ttl" to "redis", card = BoardCard("TTL", "A use-by date: Redis drops the key once the time passes.")),
                ),
            ),
            // 15: "Dana, saving the day. Wednesday?"
            Scene(
                15, "Wednesday: Leila", meaningR(),
                beats = listOf(
                    Beat(16, show = setOf("q", "q2"), light = setOf("q", "q2"), card = BoardCard("Semantic cache", "Same question, other words: match by meaning, not exact words.")),
                    Beat(18, show = setOf("emb", "pins"), light = setOf("pins"), pulse = "emb" to "pins", card = BoardCard("Embedding", "Numbers that capture meaning; the same meaning lands close.")),
                ),
            ),
            // 19: "I sense a but."
            Scene(
                19, "A whole new cache", modules(),
                beats = listOf(
                    Beat(20, show = setOf("old", "rs"), light = setOf("rs"), card = BoardCard("RediSearch", "The module for vectors. Chosen at creation; never added later.")),
                    Beat(22, show = setOf("new", "ne"), light = setOf("new", "ne"), pulse = "old" to "new", card = BoardCard("Where it stops", "NoEviction: when memory is full, new writes are refused.")),
                ),
            ),
            // 23: "Thursday. Let me guess. The intern."
            Scene(
                23, "Thursday: the intern", signin(),
                beats = listOf(
                    Beat(24, show = setOf("app", "cache"), light = setOf("app"), card = BoardCard("An access key", "A shared password pasted into settings. Not needed.")),
                    Beat(26, show = setOf("entra"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Entra ID", "Microsoft's sign-in, like a staff badge: on by default.")),
                    Beat(28, show = setOf("user"), light = setOf("user"), pulse = "user" to "cache", card = BoardCard("Redis user", "Added on the Authentication page; redis-entraid signs in.")),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Sam", knnSearch(),
                beats = listOf(
                    Beat(30, show = setOf("qv", "knn", "idx"), light = setOf("knn"), pulse = "knn" to "idx", card = knnCode),
                ),
            ),
            // 31: "And how does it search? Every single vector?"
            Scene(
                31, "FLAT or HNSW", methods(),
                beats = listOf(
                    Beat(32, show = setOf("field", "flat", "hnsw"), light = setOf("hnsw"), pulse = "field" to "hnsw", card = BoardCard("FLAT or HNSW", "FLAT checks every vector, exactly. HNSW: near neighbours, faster.")),
                ),
            ),
            // 33: "So what went wrong?"
            Scene(
                33, "Too loose, too shared", knnSearch(),
                beats = listOf(
                    Beat(34, light = setOf("hit", "miss"), pulse = "knn" to "hit", card = BoardCard("Too loose, and shared", "Queues answered with topics; one learner saw another's answer.")),
                    Beat(36, light = setOf("knn"), card = BoardCard("The fix", "Under 0.1, split by user, course and model; pgvector for the rest.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("order", "ttl", "mods", "sem"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-redis--late-night",
        listOf(
            // 0: "Hey, you. It's late, and you're still here..."
            Scene(
                0, "Memory that forgets", desk(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Tonight", "Memory: the fast kind, the kind that forgets on purpose.")),
                    Beat(2, show = setOf("cabinet"), light = setOf("cabinet"), pulse = "app" to "cabinet", card = BoardCard("Slow summaries", "The same summary, built again every time a note opens.")),
                    Beat(4, show = setOf("note"), light = setOf("note"), pulse = "app" to "note", card = BoardCard("Cache", "A copy kept close and fast: a sticky note by the bed.")),
                    Beat(6, card = BoardCard("Azure Managed Redis", "Redis run by Microsoft: everything in fast memory.")),
                    Beat(8, card = BoardCard("Under the picture", "A giant dictionary of keys and values, on servers Microsoft runs.")),
                ),
            ),
            // 9: "And how does the Coach use it?"
            Scene(
                9, "Cache-aside", cacheAside(),
                beats = listOf(
                    Beat(9, show = setOf("api", "redis", "db")),
                    Beat(10, light = setOf("redis", "db"), pulse = "api" to "redis", card = asideCode),
                    Beat(12, show = setOf("ttl"), light = setOf("ttl"), pulse = "ttl" to "redis", card = BoardCard("TTL", "Time to live: once it passes, Redis quietly lets the key go.")),
                ),
            ),
            // 13: "And if the learner edits the note in the middle of that hour?"
            Scene(
                13, "Letting go", invalidation(),
                beats = listOf(
                    Beat(14, show = setOf("write", "db", "del"), light = setOf("db", "del"), pulse = "db" to "del", card = writeCode),
                    Beat(15, show = setOf("reader", "redis"), light = setOf("reader"), pulse = "reader" to "redis", card = BoardCard("The other way round", "A reader slips in between and caches the old summary again.")),
                    Beat(16, light = setOf("del"), card = BoardCard("A safety net", "With a TTL, a missed delete fades within the hour.")),
                ),
            ),
            // 17: "Now the part with AI in it. I can feel it."
            Scene(
                17, "What I mean, not what I say", meaningR(),
                beats = listOf(
                    Beat(18, show = setOf("q", "q2"), light = setOf("q", "q2"), card = BoardCard("A DLQ? A dead-letter queue?", "Same question, other words. An exact-word cache misses.")),
                    Beat(20, card = BoardCard("Semantic cache", "Reuse a stored answer when a new question means the same thing.")),
                    Beat(22, show = setOf("emb", "pins"), light = setOf("pins"), pulse = "emb" to "pins", card = BoardCard("Embedding", "Numbers that capture meaning; the same meaning lands close.")),
                ),
            ),
            // 23: "And Redis searches the pins?"
            Scene(
                23, "Chosen at creation", modules(),
                beats = listOf(
                    Beat(24, show = setOf("new", "rs"), light = setOf("rs"), pulse = "new" to "rs", card = BoardCard("RediSearch", "The module for vector fields. Chosen at creation; never added later.")),
                    Beat(26, show = setOf("old", "ne"), light = setOf("ne"), pulse = "old" to "new", card = BoardCard("Where it stops", "NoEviction: a full cache refuses new writes. Another reason for TTLs.")),
                ),
            ),
            // 27: "And how does it find the nearest pin?"
            Scene(
                27, "The nearest pin", knnSearch(),
                beats = listOf(
                    Beat(28, show = setOf("qv", "knn", "idx"), light = setOf("knn"), pulse = "knn" to "idx", card = knnCode),
                ),
            ),
            // 29: "And does it look at every pin to find it?"
            Scene(
                29, "FLAT or HNSW", methods(),
                beats = listOf(
                    Beat(30, show = setOf("field", "flat", "hnsw"), light = setOf("flat", "hnsw"), pulse = "field" to "hnsw", card = BoardCard("FLAT or HNSW", "FLAT checks every vector, exactly. HNSW hops between neighbours.")),
                ),
            ),
            // 31: "And if the closest isn't close enough?"
            Scene(
                31, "Close enough?", knnSearch(),
                beats = listOf(
                    Beat(32, light = setOf("hit", "miss"), pulse = "knn" to "miss", card = BoardCard("Start strict", "Reuse under 0.1; split by user, course and chat model version.")),
                ),
            ),
            // 33: "Mm. And how do I get in, at this hour?"
            Scene(
                33, "Getting in", signin(),
                beats = listOf(
                    Beat(34, show = setOf("app", "entra", "cache"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Entra ID", "Microsoft's sign-in, like a staff badge. No shared password.")),
                    Beat(36, show = setOf("user"), light = setOf("user"), pulse = "user" to "cache", card = BoardCard("Redis user", "Added on the Authentication page; redis-entraid signs in.")),
                ),
            ),
            // 37: "And if I want vectors that stay, not ones that fade?"
            Scene(
                37, "Vectors that stay", limits(),
                beats = listOf(
                    Beat(38, light = setOf("link", "truth"), card = BoardCard("Your next late night", "The pgvector episode: vector search in the database itself.")),
                ),
            ),
            // 39: "Before you send me to bed. What do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("order", "ttl", "mods", "sem"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: order, TTL, new cache, KNN.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-redis--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", desk(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Date night", "A topic all about memory: Azure Managed Redis.")),
                    Beat(2, show = setOf("cabinet"), light = setOf("cabinet"), pulse = "app" to "cabinet", card = BoardCard("Slow summaries", "Built again, slowly, every single time a note opens.")),
                    Beat(4, show = setOf("note"), light = setOf("note"), pulse = "app" to "note", card = BoardCard("Cache", "A copy kept close and fast: a sticky note on the fridge.")),
                    Beat(6, card = BoardCard("Under the picture", "A giant dictionary of keys and values, in fast memory.")),
                    Beat(8, card = BoardCard("Azure Managed Redis", "Redis that Microsoft runs for you, built on Redis Enterprise.")),
                ),
            ),
            // 9: "Unlike some people. So how does the Coach use it?"
            Scene(
                9, "Cache-aside", cacheAside(),
                beats = listOf(
                    Beat(9, show = setOf("api", "redis", "db")),
                    Beat(10, light = setOf("redis", "db"), pulse = "api" to "redis", card = asideCode),
                    Beat(12, show = setOf("ttl"), light = setOf("ttl"), pulse = "ttl" to "redis", card = BoardCard("TTL", "Time to live, a use-by date. Redis throws the copy away after.")),
                ),
            ),
            // 13: "Then let me test you, darling. Exam style..."
            Scene(
                13, "Timing is everything", invalidation(),
                beats = listOf(
                    Beat(13, show = setOf("write"), card = BoardCard("Quiz: an edited note", "The database and the cache: which first?")),
                    Beat(14, show = setOf("db", "del", "reader", "redis"), light = setOf("db", "del"), pulse = "db" to "del", card = BoardCard("Right answer", "Database first, then delete the key: invalidation.")),
                    Beat(16, light = setOf("del"), card = BoardCard("Quiz: why a TTL too?", "If you always delete after writing, why an expiry?")),
                    Beat(17, light = setOf("redis"), card = BoardCard("Right answer", "A missed delete is stale for an hour, not forever.")),
                ),
            ),
            // 19: "Except me. Now, the AI part..."
            Scene(
                19, "Same question, other words", meaningR(),
                beats = listOf(
                    Beat(19, show = setOf("q", "q2"), light = setOf("q", "q2"), card = BoardCard("A DLQ? A dead-letter queue?", "Same question, other words. An exact-word cache misses.")),
                    Beat(20, card = BoardCard("Semantic cache", "Reuse a stored answer when a new question means the same thing.")),
                    Beat(22, show = setOf("emb", "pins"), light = setOf("pins"), pulse = "emb" to "pins", card = BoardCard("Embedding", "Numbers that capture meaning; the same meaning lands close.")),
                ),
            ),
            // 23: "Close together. My favourite place..."
            Scene(
                23, "Commitment", modules(),
                beats = listOf(
                    Beat(24, show = setOf("old"), light = setOf("old"), card = BoardCard("Quiz: no modules", "A cache made without modules now needs vector search. What then?")),
                    Beat(26, show = setOf("new", "rs", "ne"), light = setOf("new", "rs"), pulse = "old" to "new", card = BoardCard("No kiss: a new cache", "Modules are chosen at creation. RediSearch, with NoEviction.")),
                ),
            ),
            // 27: "Commitment from the very start..."
            Scene(
                27, "Search methods", methods(),
                beats = listOf(
                    Beat(27, show = setOf("field"), card = BoardCard("Quiz: exact search", "Which method checks every stored vector?")),
                    Beat(28, show = setOf("flat"), light = setOf("flat"), pulse = "field" to "flat", card = BoardCard("Right answer: FLAT", "Exact, every vector, fine for small sets.")),
                    Beat(30, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "field" to "hnsw", card = BoardCard("HNSW", "A web of near neighbours: approximate, faster on large sets.")),
                ),
            ),
            // 31: "Fast and approximate. Like my cooking..."
            Scene(
                31, "Just between us", knnSearch(),
                beats = listOf(
                    Beat(32, show = setOf("qv", "knn", "idx", "hit", "miss"), light = setOf("knn", "hit"), pulse = "knn" to "idx", card = BoardCard("KNN query", "The k closest stored vectors; a hit only under a distance of 0.1.")),
                    Beat(33, light = setOf("hit"), card = BoardCard("Quiz: shared answers", "Learners see each other's answers. What's missing?")),
                    Beat(34, light = setOf("knn"), card = BoardCard("Right answer", "Split by user, course and chat model version; a TTL on each.")),
                ),
            ),
            // 36: "Last practical one. How does your server sign in..."
            Scene(
                36, "No password", signin(),
                beats = listOf(
                    Beat(36, show = setOf("app", "cache"), card = BoardCard("Quiz: no password", "How does the server sign in, with nothing in its settings?")),
                    Beat(37, show = setOf("entra"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Entra ID", "Microsoft's sign-in, like a staff badge: on by default.")),
                    Beat(39, show = setOf("user"), light = setOf("user"), pulse = "user" to "cache", card = BoardCard("Redis user", "Added on the Authentication page; redis-entraid signs in.")),
                ),
            ),
            // 40: "And where does it all stop working, Rafe?..."
            Scene(
                40, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(41, light = setOf("truth", "full", "loose"), card = BoardCard("Where it stops", "Not the truth; full means refused writes; loose means wrong answers.")),
                ),
            ),
            // 42: "Then the last question, for the exam. What do we remember?"
            Scene(
                42, "For the exam", exam(),
                beats = listOf(
                    Beat(43, show = all6, light = setOf("order", "ttl", "mods", "flat", "sem"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
