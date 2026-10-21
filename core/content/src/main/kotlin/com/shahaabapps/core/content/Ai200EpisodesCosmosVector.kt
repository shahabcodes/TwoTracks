package com.shahaabapps.core.content

/** Vector search in Cosmos DB: embeddings and semantic retrieval: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosVector {
    private val classic = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Hi, and welcome back. Today is about finding things by meaning. Here's a puzzle to start. A learner types into their study app, how do I stop throttling. The best note they ever wrote says, handle 429 with autoscale. Hardly a word in common. So how could a database find that note?"),
            leo("A search on the word throttling? [pause] No, that word isn't even in the note. I'd find nothing."),
            maya("Exactly. Matching words fails here, and so would an exact match on the title. What we want is to compare what the two texts mean. And the trick is to turn meaning into numbers. That's called an embedding."),
            leo("What is an embedding, in plain words?"),
            maya("A list of numbers that an AI model makes from a piece of text. Often 1,536 of them. Think of each list as an arrow pointing somewhere in a huge space. Texts with similar meaning get arrows pointing in similar directions. Throttling and too many requests end up pointing nearly the same way."),
            leo("So the database compares arrows instead of words."),
            maya("That's vector search. A vector is just that list of numbers. You store each note's embedding next to the note, in Azure Cosmos DB. When a question comes in, you turn it into an embedding too, and ask for the notes whose vectors are closest. It finds notes by meaning, not by matching words."),
            leo("Where do the numbers come from? Do I make them up?"),
            maya("[chuckle] No. An embedding model makes them. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. You deploy an Azure OpenAI embedding model, such as text-embedding-3-small, in your own resource, and the backend signs in to it with Entra ID, like everything else."),
            leo("And I send it a note, and it sends back 1,536 numbers?"),
            maya("Right. And here's the rule people trip on. Use the same model for the stored notes and for the question. Each model places meaning in its own space, often with a different number of dimensions. Mix two models, and the results are nonsense."),
            leo("Like reading a map of London to find your way round Paris. Same paper, wrong city."),
            maya("Lovely. Now, Cosmos DB doesn't do this out of the box. First you enable vector search on the account, once. It's a capability called EnableNoSQLVectorSearch, and it can take about fifteen minutes to switch on."),
            leo("And then any container can do it?"),
            maya("No, and that's the next trap. Vector settings go on a new container, when you create it. You can't switch them on for an existing container. And vector search isn't supported on a database with shared throughput."),
            leo("What settings, exactly?"),
            maya("Two. First, the vector embedding policy. For each path that holds a vector, it says the data type, usually float32, the dimensions, which must match the model, so 1,536 for text-embedding-3-small, and the distance function."),
            leo("Distance function. That sounds like maths homework."),
            maya("It's just how closeness is measured. Cosine is the default, and it runs from minus one to plus one: higher, toward plus one, means more similar. Dot product is also higher for more similar. Euclidean is the odd one out. It's a real distance, from zero up, so lower means closer."),
            leo("Higher is closer, except when lower is closer. Got it. [sigh] Mostly. What's the second setting?"),
            maya("The indexing policy. Two things in it. Exclude the vector path from the normal index, or every insert pays to index 1,536 numbers one by one. And add a vector index. There are three kinds."),
            leo("Of course there are three. Go on."),
            maya("Flat compares the question with every vector, brute force, so it's exact, a hundred percent recall. But it only takes vectors up to 505 dimensions. Then quantizedFlat. It compresses the vectors, then still compares them all. Up to 4,096 dimensions, with a slight loss of accuracy. It suits up to about 50,000 vectors in scope, often after a filter."),
            leo("And the third?"),
            maya("DiskANN. An approximate nearest neighbour index, for more than about 50,000 vectors in scope. Up to 4,096 dimensions, and the lowest latency and RU cost at scale. One more detail. QuantizedFlat and diskANN need about a thousand vectors before they help. Below that, a full scan runs anyway."),
            leo("So with 1,536 dimensions, flat is out straight away."),
            maya("Straight away. For example, the Coach holds 300,000 notes. But every search is filtered to one learner, so only a few hundred vectors are in scope each time. That's well under 50,000, so the team picks quantizedFlat, with cosine distance. Now picture a shared library of two million public study cards, searched with no filter at all. That's diskANN."),
            leo("Okay. The container's ready. How do I actually search?"),
            maya("With a query that orders by a function called VectorDistance. It scores how similar each note's vector is to the question's vector. You select TOP 5, order by VectorDistance, and add a WHERE on the learner's id. That filter keeps the search inside one learner's notes, and it's also a single-partition query, which is cheap."),
            leo("Why TOP 5? Why not just give me everything, ranked?"),
            maya("Because without TOP N the engine works out a score for far more items than you need, and the RU charge climbs. That's a classic exam question: good matches, but far too many RUs. The missing piece is TOP N."),
            leo("And then the Coach shows the learner five notes?"),
            maya("It can, but usually it goes one step further. That's RAG, retrieval augmented generation. Retrieve the most relevant notes with vector search, put their text into the prompt, and ask a chat model to answer only from them. The answer is grounded in the learner's own notes, and the app can show which ones it used."),
            leo("So the chat model isn't making things up. It's reading my own notes back to me, nicely."),
            maya("[laugh] Nicely put. Now, the usual mistakes. Trying to add a vector policy to an existing container. Leaving the vector path in the normal index. Picking flat for 1,536 dimensions. Forgetting TOP N. And embedding the notes and the questions with different models."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If the question says 1,536 dimensions and a flat index, that's the trap: flat stops at 505. If far more than 50,000 vectors are in scope, the answer is diskANN. If a filter leaves a small set, quantizedFlat. If a vector query costs too many RUs, add TOP N. And if the results are nonsense, use the same embedding model for both."),
            leo("And anything to try today?"),
            maya("It takes about fifteen minutes. Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Embed and upsert five notes on different topics. Then ask a question in different words from any note, and see which note ranks first."),
            leo("A search that understands me. Better than most people. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Welcome to Case Files. Today's case: The Answer About Pancakes. A small Android app called Pocket Tutor helps students revise, with a Python backend on Azure. Its new feature: ask a question, and it finds your own notes that answer it. On launch day, a student asks, how do I stop throttling. And the app proudly shows a note about pancake recipes."),
            leo("[chuckle] Pancakes. Detective Leo is hungry, and on the case. How was it meant to find the right note, in plain words?"),
            maya("By meaning, not by words. Each note is turned into an embedding: a list of numbers, 1,536 of them, made by an AI model. Texts that mean similar things get lists that point in similar directions. The note that says handle 429 with autoscale should sit right beside the question, though they share hardly a word."),
            leo("Lists that point? Numbers don't point."),
            maya("Under the picture, they do. Each list is a vector, a point in a space with 1,536 directions instead of three. Every number is one coordinate. The notes and their vectors live together in Azure Cosmos DB, and the search asks for the notes whose vectors are closest to the question's. That's vector search."),
            leo("Then my first suspect is the distance function. They picked the wrong one. Switch cosine to euclidean, and the pancakes go away."),
            maya("[pause] Good instinct, wrong suspect. Any of the three works, as long as both vectors come from the same model. Cosine, the default, runs from minus one to plus one, and higher is closer. Dot product, higher is closer. Euclidean is a real distance, so lower is closer. Changing it wouldn't turn pancakes into throttling."),
            leo("Suspect two. The database read stale data. Turn the consistency up to strong."),
            maya("Consistency has nothing to do with how vectors are compared. Strong would only make reads cost more. Here's the clue that cracks it. The notes were embedded last month with text-embedding-3-small, an Azure OpenAI model. On launch day, the developer embedded the questions with a different, newer model."),
            leo("So the question and the notes speak different languages."),
            maya("Exactly. Each model places meaning in its own space, often with different dimensions. Numbers from two models are not comparable. The same model for the stored notes and for the question, always. And if you ever change models, every stored note must be embedded again."),
            leo("Fix the model, pancakes gone. Case closed?"),
            maya("Not quite. There's a second witness: the finance team. The Cosmos DB bill tripled in the first week. Every search cost far more request units, RUs, than anyone expected."),
            leo("Suspect: it needs a composite index on the embedding. Indexes make queries cheap."),
            maya("Wrong kind of index. Vectors use a vector index, not a composite one. Look at the query itself. SELECT the id from the notes, ORDER BY VectorDistance of the embedding and the question. VectorDistance is the function that scores how similar two vectors are. What's missing?"),
            leo("[pause] A limit? It ranks every note, and the app only shows a few."),
            maya("Detective Leo, finally. Without TOP N, the engine works out a score for far more items than you need, and the RU charge climbs. So, SELECT TOP 5. And a WHERE on the student's id, so the search stays inside one student's notes. That's a metadata filter and a single-partition query in one."),
            leo("And the inserts? Finance said writes were pricey too."),
            maya("Third clue. The vector path was still in the normal indexing policy, so every insert paid to index 1,536 numbers one by one. Exclude that path from the normal index, and give it a vector index instead. The Cosmos performance episode covers indexing policies and RUs in depth."),
            leo("Easy. Edit the existing container, add the vector settings, done by lunch."),
            maya("That's how you open the next case. Vector policies go on a new container, when you create it. You can't switch them on for an existing one. So the team made a new container and moved the notes in. And before any of that, the account itself needs vector search enabled, a capability that can take about fifteen minutes."),
            leo("What goes on that new container?"),
            maya("A vector embedding policy. For the embedding path, it gives the data type, float32, the dimensions, 1,536 to match the model, and the distance function, cosine. Then the vector index. And one warning: vector search isn't supported on a database with shared throughput."),
            leo("Which index, then? I'll say flat. Simple, exact, a hundred percent recall."),
            maya("[sigh] And a fourth case opens. Flat is exact, brute force, but it stops at 505 dimensions. 1,536 doesn't fit. It's the exam's favourite trap."),
            leo("Fine. Then the fancy one. DiskANN."),
            maya("Closer, but look at the scope. Pocket Tutor holds 300,000 notes, but each search is filtered to one student, so only a few hundred vectors are in scope. Well under 50,000. That's quantizedFlat. It keeps compressed copies of the vectors and still compares them all, up to 4,096 dimensions, with a slight loss of accuracy."),
            leo("So when is diskANN the right answer?"),
            maya("When far more than 50,000 vectors are in scope, say a shared library of two million public study cards, searched with no filter. DiskANN is an approximate nearest neighbour index. It builds a graph, each vector linked to some of its near neighbours, and a search hops along those links toward the question, checking only a small part. Lowest latency and RU cost at scale."),
            leo("And the student sees five notes. That's it?"),
            maya("Then comes RAG, retrieval augmented generation. The top notes go into the prompt, and a chat model answers only from them. The answer is grounded in the student's own notes, and the app can show which ones it used."),
            leo("Any loose ends in the file?"),
            maya("One, and it's where vector search stops working. It always returns the closest notes, even when nothing is about the question. Ask Pocket Tutor about tax returns, and it will still find five notes. Closest isn't the same as relevant. So check the scores, and tell the chat model to say when the notes don't answer it."),
            leo("And when a student edits a note?"),
            maya("Its embedding is out of date, so it must be made again. The change feed episode shows how to react to new and updated items. And the pgvector episode does this same search on PostgreSQL, if you're curious."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If the results are nonsense, the question and the notes need the same embedding model. If a vector query costs too many RUs, add TOP N. If the question says 1,536 dimensions with a flat index, that's the trap: flat stops at 505. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. And a vector policy needs a new container."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, without the pancakes. Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Embed and upsert five notes on different topics. Then ask a question in different words from any note, and see which note ranks first."),
            leo("Pancakes for breakfast, throttling after lunch. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They shipped smart search for the Two Tracks Coach, the study assistant in the Android app, with its Python backend on Azure. And let's just say, it was a week."),
            leo("[laugh] I heard there was a meeting with the word sourdough in the title. What were they building?"),
            maya("A search that finds a learner's notes by meaning, not by words. Ask, how do I stop throttling, and it finds the note that says, handle 429 with autoscale. No words in common, same meaning."),
            leo("How does a database know what anything means?"),
            maya("It doesn't, really. An AI model turns each note into an embedding, a list of numbers, usually 1,536 of them. Under the picture, it's a point in a space with 1,536 directions, and notes that mean similar things land near each other. You store that list on the note in Cosmos DB, embed the question the same way, and ask for the closest notes. That's vector search."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday morning. Tom had last year's notes container, full of data, and he thought he'd just switch vector search on for it. Add the settings, done by coffee."),
            leo("And?"),
            maya("And you can't. Vector policies go on a new container, when you create it. You can't add them to an existing one. Oh, and the account needs vector search enabled first, a capability that can take about fifteen minutes. Tom spent his Monday making a new container and copying the notes across."),
            leo("[sigh] Poor Tom. What settings did the new container need?"),
            maya("A vector embedding policy. For the embedding path, it gives the data type, float32, the dimensions, 1,536 to match the model, and the distance function. Cosine is the default, from minus one to plus one, and higher is closer. Dot product, higher is closer too. Euclidean is a real distance, so lower is closer."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. Dana loaded the notes, and the write costs went through the roof. Every insert cost far more request units than before."),
            leo("Why? It's just a note."),
            maya("A note with 1,536 numbers on it. Dana had left the vector path in the normal indexing policy, so every insert paid to index all those numbers one by one. Exclude the vector path from the normal index, and give it a vector index instead. Dana fixed it before lunch, to be fair. The Cosmos performance episode has the rest of the indexing story."),
            leo("Which vector index? I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. Marcus picked flat. Flat compares the question with every vector, brute force. Exact, a hundred percent recall."),
            leo("Sounds perfect. What's the catch?"),
            maya("Flat only takes vectors up to 505 dimensions. Marcus had 1,536. The container just said no, and the whole floor heard about it."),
            leo("So what should Marcus have picked?"),
            maya("It depends how many vectors a search looks at. The Coach holds 300,000 notes, but each search is filtered to one learner, so a few hundred are in scope. Under about 50,000 in scope, it's quantizedFlat. It keeps compressed copies of the vectors and still compares them all. Up to 4,096 dimensions, with a slight loss of accuracy."),
            leo("And the big one? Everyone keeps saying diskANN."),
            maya("DiskANN is for more than about 50,000 vectors in scope, like a public library of two million study cards searched with no filter. It's an approximate nearest neighbour index. Under the hood, it builds a graph where each vector links to some of its near neighbours, and a search hops along those links toward the question, checking only a small part. Lowest latency and RU cost at scale."),
            leo("And if you've only got fifty notes, like me?"),
            maya("Then it hardly matters. QuantizedFlat and diskANN need about a thousand vectors before they help. Below that, a full scan runs anyway."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. Their search worked beautifully. Great matches. And the RU bill for that one day was enormous."),
            leo("Let me guess. They asked for everything."),
            maya("They did. SELECT, ORDER BY VectorDistance, the function that scores how similar two vectors are, but no TOP. Without TOP N, the engine scores far more items than you need. SELECT TOP 5, with a WHERE on the learner's id, and it's cheap again. That filter is a metadata filter and a single-partition query in one."),
            leo("And Friday? There's always a Friday."),
            maya("The Friday. Priya. Priya never makes mistakes, which is why everyone's still talking about it. She tried a newer embedding model for the questions, but the notes were still embedded with text-embedding-3-small. And the Coach answered a question about throttling with a note about sourdough starters."),
            leo("[laugh] The sourdough meeting. But why sourdough?"),
            maya("Because each model places meaning in its own space, often with different dimensions. Vectors from two models aren't comparable. The same model for notes and questions, always, and if you change models, every note gets embedded again. Priya re-embedded the lot by Monday. The change feed episode shows how to react when notes are added or edited."),
            leo("So once it all worked, what did the Coach do with its five notes?"),
            maya("RAG. Retrieval augmented generation. The top notes go into the prompt, and a chat model answers only from them. The answer is grounded in the learner's own notes, and the app shows which ones it used."),
            leo("Did anything still go wrong?"),
            maya("Only where vector search stops working. It always returns the closest notes, even if none is really about the question. Closest isn't relevant. So the team checks the scores, and lets the chat model say it doesn't know. And vector search isn't supported on a database with shared throughput. Nobody tried that one this week. Give it time."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. A vector policy on an existing container? No, a new one. 1,536 dimensions with a flat index is the trap: flat stops at 505. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. Too many RUs on a vector query, add TOP N. And nonsense results mean the same embedding model for both."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Embed and upsert five notes on different topics. Then ask a question in different words from any note, and see which note ranks first."),
            leo("No sourdough. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-vector",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about finding things by what they mean."),
            ivy("Mm. Meaning, at this hour? You're ambitious tonight, Nova."),
            nova("Only a little. Picture this. A learner is revising late, like you. They type, how do I stop throttling. And somewhere in their notes is the perfect answer: handle 429 with autoscale. [pause] Barely a word in common."),
            ivy("So a search for the word throttling finds nothing. How lonely."),
            nova("Lonely. Unless the database compares meaning instead of words. And the way to do that is an embedding. A list of numbers an AI model makes from a piece of text, often 1,536 of them. Texts with similar meaning get lists that point in similar directions."),
            ivy("Numbers that point. Tell me what that really is."),
            nova("Under the picture, each list is a vector, a point in a space with 1,536 directions instead of the three we live in. Every number is one coordinate. Two notes about the same idea sit close together in that space, [pause] even if they never share a word."),
            ivy("Close together, in the dark. I like that. Where do they live?"),
            nova("Right next to the notes, in Azure Cosmos DB. Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. Each note keeps its embedding beside its text. The question gets embedded too, and the database looks for the notes whose vectors are closest. That's vector search."),
            ivy("And who makes the numbers?"),
            nova("An embedding model. An Azure OpenAI deployment of text-embedding-3-small, say, in your own resource. The backend signs in to it with Entra ID, like everything else. And there's one promise you have to keep. The same model for the notes and for the question."),
            ivy("Or what?"),
            nova("Or nonsense. Each model has its own space, often with its own number of dimensions. Numbers from two models simply don't compare. It's like whispering in one language and listening in another."),
            ivy("[chuckle] I've been on dates like that. So how close is close?"),
            nova("That's the distance function. Cosine is the default. It looks at the angle between two vectors, and runs from minus one to plus one. Higher, toward plus one, is more similar. Dot product, higher is closer too. And euclidean is a true distance, from zero up, so there, lower means closer."),
            ivy("And Cosmos DB? Does it just do this, out of the box?"),
            nova("Not quite. First you enable vector search on the account, once. It's a capability, and it can take about fifteen minutes. [pause] Then, and this part matters, the vector settings go on a new container, when you create it. You can't add them to an existing container."),
            ivy("No changing your mind later. Strict."),
            nova("Strict. And not on a database with shared throughput, either. The container gets a vector embedding policy. For the path that holds the vector, it says the data type, float32 usually, the dimensions, 1,536 to match the model, and the distance function."),
            ivy("And the index? I can hear an index coming."),
            nova("Two things in the indexing policy. Exclude the vector path from the normal index, or every insert pays to index 1,536 numbers, one by one. And add a vector index. There are three, and the exam loves them."),
            ivy("Tell me slowly."),
            nova("Flat. It compares the question with every vector, brute force. Exact, a hundred percent recall. But only up to 505 dimensions. [pause] Then quantizedFlat. It keeps compressed copies of the vectors, and still compares them all. Up to 4,096 dimensions, with a slight loss of accuracy. Good for up to about 50,000 vectors in scope, often after a filter."),
            ivy("And the third one. The one everyone whispers about."),
            nova("DiskANN. For more than about 50,000 vectors in scope. It's an approximate nearest neighbour index. It builds a graph, each vector holding hands with a few of its near neighbours, and a search walks along those links toward the question, visiting only a small part of the crowd. Lowest latency and RU cost at scale."),
            ivy("Nearest neighbours, holding hands. You're making indexes romantic."),
            nova("[pause] Someone has to. One more thing. QuantizedFlat and diskANN need about a thousand vectors before they help; below that, a full scan runs anyway. So for the Coach: 300,000 notes, but each search filtered to one learner, a few hundred in scope. Flat can't take 1,536 dimensions. So, quantizedFlat, with cosine."),
            ivy("And the search itself? What do I actually write?"),
            nova("A query with VectorDistance, the function that scores how similar two vectors are. SELECT TOP 5, ORDER BY VectorDistance, with a WHERE on the learner's id. That keeps the search inside one learner's notes, in one partition. And always the TOP. Without it, the engine scores far more items than you need, and the RU charge climbs."),
            ivy("Five notes, then. What does the Coach do with them?"),
            nova("RAG. Retrieval augmented generation. It puts their text into the prompt and asks a chat model to answer only from them. So the answer is grounded in your own notes, and the app can show you which ones it used."),
            ivy("Now the part I've been waiting for. Where does all this stop working?"),
            nova("Here. [pause] Vector search always brings back the closest notes, even when none of them is really about the question. Closest isn't the same as relevant. So look at the scores, and let the chat model say, I don't know. And when a note changes, its embedding must be made again. The change feed episode shows how to catch those changes."),
            ivy("And if I'd rather do all this in PostgreSQL?"),
            nova("Then the pgvector episode is your next late night. The same idea, in a different database."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. If it's 1,536 dimensions with a flat index, that's the trap: flat stops at 505. If far more than 50,000 vectors are in scope, diskANN. If a filter leaves a small set, quantizedFlat. If a vector query costs too many RUs, add TOP N. And if the results are nonsense, use the same embedding model for notes and questions."),
            ivy("And something to try today, before I sleep?"),
            nova("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Embed and upsert five notes on different topics. [pause] Then ask a question in different words from any note, and see which note ranks first."),
            ivy("Close in meaning, if not in words. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-vector",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about the most romantic thing in the cloud."),
            rafe("Let me guess. Finding someone close to you."),
            nova("[laugh] Vector search in Cosmos DB. Which is, in fact, all about finding what's close to you. Not what uses the same words. What means the same thing."),
            rafe("Like us. We never say the same thing, and somehow we always mean it."),
            nova("Smooth. So, picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. A learner asks, how do I stop throttling. And the perfect note says, handle 429 with autoscale. Hardly a word in common."),
            rafe("But a perfect match. So how does the database see it?"),
            nova("With an embedding. An AI model turns each piece of text into a list of numbers, often 1,536 of them. Texts with similar meaning get lists that point in similar directions."),
            rafe("1,536 dimensions. [pause] I always knew you were complicated, but that's a lot of dimensions."),
            nova("And you love every one of them. Under the picture, each list is a vector, a point in a space with 1,536 directions. Every number is one coordinate. Two notes that mean the same thing sit close together there, a small distance apart."),
            rafe("A small distance apart. Exactly where I'd like to be. So where do these vectors live?"),
            nova("Right beside the notes, in Cosmos DB. The question gets embedded too, and the database finds the notes whose vectors are closest. That's vector search. The numbers come from an Azure OpenAI embedding model, like text-embedding-3-small, and the backend signs in to it with Entra ID."),
            rafe("Then let me test you, darling. Exam style. The notes were embedded with text-embedding-3-small, but the questions with a different model. The results are nonsense. Why?"),
            nova("Because vectors from different models aren't comparable. Each model places meaning in its own space, often with different dimensions. The same model for the notes and the question, always."),
            rafe("Right answer. Two people have to be on the same wavelength. [kiss]"),
            nova("Mm. My turn. How does a database measure similarity? Ours is obvious."),
            rafe("With the distance function. It's how closeness is measured. Cosine is the default, from minus one to plus one, and higher means more similar. Dot product, higher is closer as well. Euclidean is a true distance, from zero up, so lower is closer."),
            nova("And cosine only looks at the angle between two vectors, not how long they are. It's about which way you're facing, Rafe, not how big you are."),
            rafe("[chuckle] I'll take that as a compliment. Now, Cosmos DB. Can I just switch this on for any container I like?"),
            nova("Oh, no. First you enable vector search on the account, once, a capability that can take about fifteen minutes. Then the vector settings go on a new container, when you create it. You can't add them to an existing one. It's commitment from the very start. And never on a database with shared throughput."),
            rafe("Committed from day one. I approve. What settings?"),
            nova("A vector embedding policy. For the path holding the vector, it says the data type, float32 usually, the dimensions, which must match the model, and the distance function. Then the indexing policy, and here's your question. Every insert is suddenly very expensive. What did we forget?"),
            rafe("We left the vector path in the normal indexing policy, so every insert pays to index 1,536 numbers one by one. Exclude it from the normal index, and give it a vector index instead."),
            nova("Right answer. Some things shouldn't be indexed in public. [kiss]"),
            rafe("Now the vector indexes. Flat, quantizedFlat and diskANN. Shall I quiz you, or will you just tell me?"),
            nova("Tell first, quiz after. Flat compares with every vector, brute force. Exact, a hundred percent recall, but only up to 505 dimensions. QuantizedFlat keeps compressed copies and still compares them all, up to 4,096 dimensions, with a slight loss of accuracy. It suits up to about 50,000 vectors in scope."),
            rafe("And diskANN?"),
            nova("An approximate nearest neighbour index, for more than about 50,000 vectors in scope, up to 4,096 dimensions. Under the hood it builds a graph, each vector linked to a few near neighbours, and a search hops along those links toward the question, visiting only a small part. Lowest latency and RU cost at scale."),
            rafe("Nearest neighbours. The ones you can always lean over the fence to. All right, my question. Notes with 1,536 dimensions, and someone picks a flat index. Good idea?"),
            nova("No. Flat stops at 505 dimensions, and 1,536 doesn't fit. It's the exam's favourite trap."),
            rafe("Right answer, and a dangerous one to get wrong. [kiss]"),
            nova("My turn. The Coach holds 300,000 notes, but every search is filtered to one learner, so only a few hundred vectors are in scope. Which index?"),
            rafe("DiskANN. It's the biggest, so it's the best."),
            nova("[sigh] No kiss for that one. Under 50,000 vectors in scope means quantizedFlat. DiskANN is for far more than that, like two million public study cards searched with no filter. And below about a thousand vectors, neither helps; a full scan runs anyway."),
            rafe("Bigger isn't always better. Noted. Let me win one back. How do I actually search?"),
            nova("With VectorDistance, the function that scores how similar two vectors are. SELECT TOP 5, ORDER BY VectorDistance, with a WHERE on the learner's id, so it stays in one learner's notes, in one partition. So, darling. A vector query returns good matches, but costs far too many RUs. What's missing?"),
            rafe("TOP N. Without it the engine scores far more items than you need, and the RU charge climbs. I only want my top five. Well. My top one."),
            nova("Right answer, and charming with it. [kiss]"),
            rafe("My question, then. The Coach must answer only from the learner's own notes, and show which ones it used. What's that pattern called?"),
            nova("RAG, retrieval augmented generation. Retrieve the closest notes with vector search, put their text into the prompt, and ask a chat model to answer only from them. The answer is grounded, and it can cite its notes."),
            rafe("Right answer. Grounded, like a good relationship. [kiss]"),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("Vector search always brings back the closest notes, even when none is really about the question. Closest isn't always the one for you. So check the scores, and let the chat model say it doesn't know. When a note changes, embed it again; the change feed episode covers that. And the pgvector episode does the same search on PostgreSQL."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("If the results are nonsense, the same embedding model for both. If a vector query costs too many RUs, add TOP N. If it's 1,536 dimensions with flat, that's the trap. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. And a vector policy needs a new container."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Embed and upsert five notes on different topics. Then ask a question in different words from any note, and see which note ranks first."),
            rafe("And I know who ranks first with me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun meaning() = DiagramSpec(
        "Two texts with hardly a word in common get nearby vectors from the same embedding model.",
        nodes = listOf(
            DNode("q", "Question", 0.24f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "stop throttling?"),
            DNode("note", "Note", 0.24f, 0.78f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "handle 429"),
            DNode("model", "Embedding model", 0.76f, 0.18f, w = 0.4f, h = 0.2f, tone = 2, sub = "same for both"),
            DNode("vec", "Nearby vectors", 0.76f, 0.78f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "1,536 numbers"),
        ),
        edges = listOf(
            DEdge("q", "model", "embed"),
            DEdge("note", "model", "embed"),
            DEdge("model", "vec"),
        ),
        aspect = 0.8f,
    )

    private fun coach() = DiagramSpec(
        "Retrieval for the Coach: embed the question, find the closest notes with VectorDistance, then a chat model answers.",
        nodes = listOf(
            DNode("user", "Learner", 0.1f, 0.2f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("api", "Coach API", 0.4f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "Python"),
            DNode("emb", "Embedding model", 0.8f, 0.2f, w = 0.34f, h = 0.2f, tone = 2, sub = "Azure OpenAI"),
            DNode("db", "Cosmos DB notes", 0.4f, 0.7f, w = 0.34f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "TOP 5"),
            DNode("chat", "Chat model", 0.8f, 0.7f, w = 0.34f, h = 0.2f, tone = 2, sub = "answers from notes"),
        ),
        edges = listOf(
            DEdge("user", "api", "question"),
            DEdge("api", "emb", "text", bidirectional = true),
            DEdge("api", "db", "query vector"),
            DEdge("db", "chat", "top notes"),
        ),
        aspect = 0.8f,
    )

    private fun container() = DiagramSpec(
        "Enable vector search on the account, then create a new container with a vector embedding policy and a vector index.",
        nodes = listOf(
            DNode("acct", "Account", 0.25f, 0.14f, w = 0.44f, h = 0.2f, tone = 1, sub = "vector search on"),
            DNode("cont", "New container", 0.25f, 0.5f, w = 0.44f, h = 0.22f, sub = "set at creation"),
            DNode("policy", "Vector embedding policy", 0.75f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "type, dimensions, distance"),
            DNode("index", "Vector index", 0.75f, 0.5f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "quantizedFlat or diskANN"),
            DNode("excl", "Exclude the vector path", 0.5f, 0.85f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "from the normal index"),
        ),
        edges = listOf(
            DEdge("acct", "cont", "then"),
            DEdge("cont", "policy"),
            DEdge("cont", "index"),
            DEdge("cont", "excl"),
        ),
        aspect = 0.8f,
    )

    private fun distance() = DiagramSpec(
        "Cosine, the default, and dot product rank higher as more similar; euclidean is a distance, so lower is closer.",
        nodes = listOf(
            DNode("q", "Question", 0.2f, 0.5f, w = 0.32f, h = 0.2f, tone = 1),
            DNode("cos", "cosine (default)", 0.7f, 0.16f, w = 0.52f, h = 0.2f, sub = "higher is closer"),
            DNode("dot", "dotproduct", 0.7f, 0.5f, w = 0.52f, h = 0.2f, sub = "higher is closer"),
            DNode("euc", "euclidean", 0.7f, 0.84f, w = 0.52f, h = 0.2f, tone = 2, sub = "lower is closer"),
        ),
        edges = listOf(DEdge("q", "cos"), DEdge("q", "dot"), DEdge("q", "euc")),
        aspect = 0.8f,
    )

    private fun indexes() = DiagramSpec(
        "flat stops at 505 dimensions; quantizedFlat suits up to about 50,000 vectors in scope; diskANN goes past that.",
        nodes = listOf(
            DNode("flat", "flat", 0.17f, 0.25f, w = 0.3f, h = 0.26f, tone = 2, sub = "up to 505 dims"),
            DNode("qf", "quantizedFlat", 0.5f, 0.25f, w = 0.3f, h = 0.26f, sub = "under 50,000"),
            DNode("disk", "diskANN", 0.83f, 0.25f, w = 0.3f, h = 0.26f, sub = "past 50,000"),
            DNode("dims", "1,536 dimensions", 0.5f, 0.78f, w = 0.56f, h = 0.2f, kind = NodeKind.PILL, sub = "how many in scope?"),
        ),
        edges = listOf(
            DEdge("dims", "flat", dashed = true),
            DEdge("dims", "qf"),
            DEdge("dims", "disk"),
        ),
        aspect = 0.8f,
    )

    private fun query() = DiagramSpec(
        "SELECT TOP 5 ordered by VectorDistance, filtered to one learner: cheap, and inside one partition.",
        nodes = listOf(
            DNode("q", "Question vector", 0.25f, 0.16f, w = 0.44f, h = 0.2f, tone = 1, sub = "same model"),
            DNode("filter", "WHERE userId", 0.75f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "one partition"),
            DNode("rank", "ORDER BY VectorDistance", 0.5f, 0.5f, w = 0.8f, h = 0.2f, sub = "closest first"),
            DNode("top", "TOP 5", 0.5f, 0.84f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, sub = "keeps RUs low"),
        ),
        edges = listOf(
            DEdge("q", "rank"),
            DEdge("filter", "rank"),
            DEdge("rank", "top"),
        ),
        aspect = 0.8f,
    )

    private fun rag() = DiagramSpec(
        "RAG: the top notes go into the prompt, and a chat model answers only from them, citing what it used.",
        nodes = listOf(
            DNode("notes", "Top 5 notes", 0.24f, 0.2f, w = 0.4f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "by VectorDistance"),
            DNode("prompt", "Prompt", 0.76f, 0.2f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "answer only from these"),
            DNode("chat", "Chat model", 0.76f, 0.78f, w = 0.4f, h = 0.2f, tone = 2, sub = "Azure OpenAI"),
            DNode("ans", "Grounded answer", 0.24f, 0.78f, w = 0.4f, h = 0.2f, sub = "cites its notes"),
        ),
        edges = listOf(
            DEdge("notes", "prompt"),
            DEdge("prompt", "chat"),
            DEdge("chat", "ans"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where vector search stops: closest is not always relevant, and a changed note or model means embedding again.",
        nodes = listOf(
            DNode("close", "Closest, not relevant", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "check the scores"),
            DNode("again", "Embed again", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "note or model changed"),
            DNode("shared", "Shared throughput", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "not supported"),
            DNode("feed", "Change feed", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "another episode"),
        ),
        edges = listOf(DEdge("again", "feed", "reacts to edits")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's vector search questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("models", "Nonsense results", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "same embedding model"),
            DNode("rus", "Too many RUs", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "add TOP N"),
            DNode("flat", "1,536 with flat", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "flat stops at 505"),
            DNode("big", "Over 50,000 in scope", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "diskANN"),
            DNode("small", "Small set, filtered", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "quantizedFlat"),
            DNode("new", "Existing container", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "no: a new one"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("models", "rus", "flat", "big", "small", "new")
    private val sql = BoardCard("Query", "SELECT TOP 5 c.id FROM c WHERE c.userId = @u ORDER BY VectorDistance(c.embedding, @q)", code = true)
    private val policyCode = BoardCard("Python", "{\"path\": \"/embedding\", \"dataType\": \"float32\", \"dimensions\": 1536}", code = true)

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-vector",
        listOf(
            // 0: "Hi, and welcome back. Today is about finding things by meaning..."
            Scene(
                0, "Meaning, not words", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q", "note"), light = setOf("q", "note"), card = BoardCard("The puzzle", "Stop throttling? The best note says: handle 429 with autoscale.")),
                    Beat(2, light = setOf("note"), card = BoardCard("Matching words fails", "Hardly a word in common. Compare what the texts mean instead.")),
                    Beat(4, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "A list of numbers from an AI model, often 1,536. Similar meaning, similar direction.")),
                ),
            ),
            // 6: "That's vector search. A vector is just that list of numbers..."
            Scene(
                6, "The Coach's search", coach(),
                beats = listOf(
                    Beat(6, show = setOf("user", "api", "db"), light = setOf("api", "db"), pulse = "api" to "db", card = BoardCard("Vector search", "Store each note's embedding; find the notes closest to the question.")),
                    Beat(8, show = setOf("emb"), light = setOf("emb"), pulse = "api" to "emb", card = BoardCard("Embedding model", "Azure OpenAI text-embedding-3-small, signed in with Entra ID.")),
                    Beat(10, light = setOf("emb", "db"), card = BoardCard("Same model for both", "Notes and question from one model, or the numbers don't compare.")),
                ),
            ),
            // 12: "Lovely. Now, Cosmos DB doesn't do this out of the box..."
            Scene(
                12, "Turn it on", container(),
                beats = listOf(
                    Beat(12, show = setOf("acct"), light = setOf("acct"), card = BoardCard("EnableNoSQLVectorSearch", "Switched on for the account once; it can take about 15 minutes.")),
                    Beat(14, show = setOf("cont"), light = setOf("cont"), pulse = "acct" to "cont", card = BoardCard("A new container", "Vector policies are set at creation. Not on shared throughput.")),
                    Beat(16, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Vector embedding policy", "Per path: data type, dimensions to match the model, distance function.")),
                ),
            ),
            // 17: "Distance function. That sounds like maths homework."
            Scene(
                17, "Distance functions", distance(),
                beats = listOf(
                    Beat(17, show = setOf("q"), card = BoardCard("Distance function", "How closeness is measured: cosine, dot product or euclidean.")),
                    Beat(18, show = setOf("cos", "dot", "euc"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("cosine (default)", "From minus one to plus one: higher is more similar.")),
                    Beat(19, light = setOf("euc"), pulse = "q" to "euc", card = BoardCard("euclidean", "A real distance from zero up, so lower is closer.")),
                ),
            ),
            // 20: "The indexing policy. Two things in it..."
            Scene(
                20, "The indexing policy", container(),
                beats = listOf(
                    Beat(20, light = setOf("excl", "index"), pulse = "cont" to "excl", card = BoardCard("Indexing policy", "Exclude the vector path from the normal index, and add a vector index.")),
                ),
            ),
            // 22: "Flat compares the question with every vector..."
            Scene(
                22, "Which vector index?", indexes(),
                beats = listOf(
                    Beat(22, show = setOf("flat", "qf"), light = setOf("flat", "qf"), card = BoardCard("flat and quantizedFlat", "flat: exact, up to 505 dims. quantizedFlat: compressed, to about 50,000.")),
                    Beat(24, show = setOf("disk"), light = setOf("disk"), card = BoardCard("diskANN", "Approximate nearest neighbour, past 50,000 in scope; lowest RU at scale.")),
                    Beat(25, show = setOf("dims"), light = setOf("dims", "flat"), pulse = "dims" to "flat", card = BoardCard("1,536 dimensions", "flat is out straight away: it stops at 505.")),
                    Beat(26, light = setOf("qf"), pulse = "dims" to "qf", card = BoardCard("The Coach: quantizedFlat", "300,000 notes, filtered to one learner: a few hundred in scope.")),
                ),
            ),
            // 27: "Okay. The container's ready. How do I actually search?"
            Scene(
                27, "The query", query(),
                beats = listOf(
                    Beat(27, show = setOf("q", "rank")),
                    Beat(28, show = setOf("filter"), light = setOf("rank", "filter"), pulse = "filter" to "rank", card = sql),
                    Beat(30, show = setOf("top"), light = setOf("top"), pulse = "rank" to "top", card = BoardCard("TOP N", "Without it, far more items are scored and the RU charge climbs.")),
                ),
            ),
            // 31: "And then the Coach shows the learner five notes?"
            Scene(
                31, "From retrieval to RAG", rag(),
                beats = listOf(
                    Beat(31, show = setOf("notes")),
                    Beat(32, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = BoardCard("RAG", "Retrieve the closest notes, put them in the prompt, answer only from them.")),
                    Beat(34, light = setOf("notes"), card = BoardCard("The usual mistakes", "Old container, vector path indexed, flat at 1,536, no TOP N, two models.")),
                ),
            ),
            // 35: "So what do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("flat", "big", "small"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: TOP N, diskANN, quantizedFlat, one model.")),
                    Beat(38, card = BoardCard("Today", "Enable vector search, a quantizedFlat container, five notes, one question.")),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-vector--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Answer About Pancakes..."
            Scene(
                0, "The Answer About Pancakes",
                DiagramSpec(
                    "Pocket Tutor's question about throttling comes back with a note about pancakes: the search found the wrong neighbour.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Pocket Tutor"),
                        DNode("api", "Python backend", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "on Azure"),
                        DNode("bad", "Pancake recipes", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "the top result"),
                        DNode("db", "Cosmos DB notes", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "question"),
                        DEdge("api", "db", "vector search"),
                        DEdge("db", "bad", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "bad"), light = setOf("bad"), pulse = "phone" to "api", card = BoardCard("The case", "Ask how to stop throttling; get a note about pancake recipes.")),
                    Beat(2, show = setOf("db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("Embedding", "1,536 numbers from an AI model; similar meaning points the same way.")),
                ),
            ),
            // 3: "Lists that point? Numbers don't point."
            Scene(
                3, "Under the picture", meaning(),
                beats = listOf(
                    Beat(3, show = setOf("q", "note"), light = setOf("q", "note")),
                    Beat(4, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("A vector", "A point in a space with 1,536 directions; every number a coordinate.")),
                ),
            ),
            // 5: "Then my first suspect is the distance function..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Changing the distance function or the consistency does not fix vectors made by two different models.",
                    nodes = listOf(
                        DNode("fn", "Distance function", 0.26f, 0.18f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "any of three works"),
                        DNode("cons", "Strong consistency", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "only costs more"),
                        DNode("m1", "Notes: one model", 0.26f, 0.74f, w = 0.46f, h = 0.2f, tone = 1, sub = "text-embedding-3-small"),
                        DNode("m2", "Questions: another", 0.76f, 0.74f, w = 0.44f, h = 0.2f, tone = 2, sub = "a newer model"),
                    ),
                    edges = listOf(DEdge("m1", "m2", "not comparable", dashed = true)),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("fn"), light = setOf("fn")),
                    Beat(6, card = BoardCard("Suspect one: cleared", "Cosine, dot product, euclidean: any works if both vectors share a model.")),
                    Beat(7, show = setOf("cons"), light = setOf("cons")),
                    Beat(8, show = setOf("m1", "m2"), light = setOf("m1", "m2"), pulse = "m1" to "m2", card = BoardCard("The clue", "Notes from text-embedding-3-small; questions from a newer model.")),
                    Beat(10, light = setOf("m2"), card = BoardCard("Not comparable", "Same model for notes and question. Change models, embed everything again.")),
                ),
            ),
            // 12: "Not quite. There's a second witness: the finance team..."
            Scene(
                12, "The second witness", query(),
                beats = listOf(
                    Beat(12, show = setOf("q", "rank"), light = setOf("rank"), card = BoardCard("The bill tripled", "Every search cost far more RUs than anyone expected.")),
                    Beat(13, card = BoardCard("Suspect: a composite index", "Wrong kind. Vectors use a vector index, not a composite one.")),
                    Beat(14, light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("VectorDistance", "The function that scores how similar two vectors are.")),
                    Beat(16, show = setOf("filter", "top"), light = setOf("top", "filter"), pulse = "rank" to "top", card = sql),
                ),
            ),
            // 17: "And the inserts? Finance said writes were pricey too."
            Scene(
                17, "The third clue", container(),
                beats = listOf(
                    Beat(17, show = setOf("cont")),
                    Beat(18, show = setOf("excl"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Vector path in the normal index", "Every insert paid to index 1,536 numbers. Exclude it.")),
                    Beat(19, light = setOf("cont"), card = BoardCard("Edit the existing container?", "No: vector policies go on a new container, set at creation.")),
                    Beat(20, show = setOf("acct"), light = setOf("acct", "cont"), pulse = "acct" to "cont", card = BoardCard("EnableNoSQLVectorSearch", "Enabled on the account first; it can take about 15 minutes.")),
                    Beat(22, show = setOf("policy", "index"), light = setOf("policy"), pulse = "cont" to "policy", card = policyCode),
                ),
            ),
            // 23: "Which index, then? I'll say flat..."
            Scene(
                23, "Which index?", indexes(),
                beats = listOf(
                    Beat(23, show = setOf("flat"), light = setOf("flat")),
                    Beat(24, show = setOf("dims"), light = setOf("flat", "dims"), pulse = "dims" to "flat", card = BoardCard("A fourth case", "flat stops at 505 dimensions: 1,536 doesn't fit.")),
                    Beat(25, show = setOf("disk"), light = setOf("disk")),
                    Beat(26, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = BoardCard("quantizedFlat", "A few hundred in scope after the filter: compressed, compares them all.")),
                    Beat(28, light = setOf("disk"), pulse = "dims" to "disk", card = BoardCard("diskANN", "Past 50,000 in scope: a graph of near neighbours, approximate.")),
                ),
            ),
            // 29: "And the student sees five notes. That's it?"
            Scene(
                29, "Then comes RAG", rag(),
                beats = listOf(
                    Beat(29, show = setOf("notes")),
                    Beat(30, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = BoardCard("RAG", "The top notes go into the prompt; the chat model answers only from them.")),
                ),
            ),
            // 31: "Any loose ends in the file?"
            Scene(
                31, "Loose ends", limits(),
                beats = listOf(
                    Beat(32, show = setOf("close"), light = setOf("close"), card = BoardCard("Closest isn't relevant", "It always returns five notes, even about tax returns. Check the scores.")),
                    Beat(34, show = setOf("again", "feed", "shared"), light = setOf("again", "feed"), pulse = "again" to "feed", card = BoardCard("Other episodes", "Change feed: react to edited notes. pgvector: the same on PostgreSQL.")),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("models", "rus"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Same model, TOP N, the right index.")),
                    Beat(38, card = BoardCard("Today", "Recreate it: enable, a quantizedFlat container, five notes, one question.")),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-vector--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "What the Coach team built", coach(),
                beats = listOf(
                    Beat(0, show = setOf("user", "api"), light = setOf("api"), pulse = "user" to "api", card = BoardCard("Smart search", "The Coach finds a learner's notes by meaning, not by words.")),
                    Beat(2, show = setOf("db"), light = setOf("db"), pulse = "api" to "db"),
                    Beat(4, show = setOf("emb"), light = setOf("emb"), pulse = "api" to "emb", card = BoardCard("Embedding", "1,536 numbers per note; notes that mean similar things land nearby.")),
                ),
            ),
            // 5: "Okay. So who went first?"
            Scene(
                5, "Monday: Tom", container(),
                beats = listOf(
                    Beat(6, show = setOf("cont"), light = setOf("cont"), card = BoardCard("An existing container?", "Tom wanted to switch vector search on for last year's notes.")),
                    Beat(8, show = setOf("acct"), light = setOf("acct", "cont"), pulse = "acct" to "cont", card = BoardCard("A new container", "Vector policies are set at creation. Enable the account first.")),
                    Beat(10, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Vector embedding policy", "float32, 1,536 dimensions, a distance function: cosine by default.")),
                ),
            ),
            // 11: "And Tuesday?"
            Scene(
                11, "Tuesday: Dana", container(),
                beats = listOf(
                    Beat(12, show = setOf("cont"), light = setOf("cont"), card = BoardCard("Inserts through the roof", "Every write cost far more request units than before.")),
                    Beat(14, show = setOf("excl", "index"), light = setOf("excl", "index"), pulse = "cont" to "excl", card = BoardCard("Exclude the vector path", "From the normal index, and give it a vector index instead.")),
                ),
            ),
            // 15: "Which vector index? I sense a Wednesday."
            Scene(
                15, "Wednesday: Marcus", indexes(),
                beats = listOf(
                    Beat(16, show = setOf("flat"), light = setOf("flat"), card = BoardCard("flat", "Brute force, exact, 100% recall.")),
                    Beat(18, show = setOf("dims"), light = setOf("dims", "flat"), pulse = "dims" to "flat", card = BoardCard("1,536 dimensions", "flat stops at 505. The container said no.")),
                    Beat(20, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = BoardCard("quantizedFlat", "A few hundred in scope after the filter: under about 50,000.")),
                    Beat(22, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk", card = BoardCard("diskANN", "Past 50,000 in scope: a graph of near neighbours, approximate.")),
                    Beat(24, light = setOf("qf", "disk"), card = BoardCard("Fifty notes?", "Under about 1,000 vectors, a full scan runs anyway.")),
                ),
            ),
            // 25: "Thursday. Go."
            Scene(
                25, "Thursday: the intern", query(),
                beats = listOf(
                    Beat(26, show = setOf("q", "rank"), light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("Great matches, huge bill", "One day's RU charge was enormous.")),
                    Beat(28, show = setOf("filter", "top"), light = setOf("top", "filter"), pulse = "rank" to "top", card = sql),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya",
                DiagramSpec(
                    "Questions embedded with a newer model than the notes cannot be compared, so the top result is nonsense.",
                    nodes = listOf(
                        DNode("m1", "Notes", 0.26f, 0.2f, w = 0.46f, h = 0.2f, tone = 1, sub = "text-embedding-3-small"),
                        DNode("m2", "Questions", 0.76f, 0.2f, w = 0.42f, h = 0.2f, tone = 2, sub = "a newer model"),
                        DNode("bad", "A sourdough note", 0.5f, 0.76f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "the top result"),
                    ),
                    edges = listOf(
                        DEdge("m1", "m2", "not comparable", dashed = true),
                        DEdge("m2", "bad"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("m1", "m2"), light = setOf("m2"), pulse = "m1" to "m2", card = BoardCard("Two models", "Questions from a newer model; notes from text-embedding-3-small.")),
                    Beat(31, show = setOf("bad"), light = setOf("bad"), pulse = "m2" to "bad"),
                    Beat(32, light = setOf("m1", "m2"), card = BoardCard("Same model, always", "Change models, embed every note again. Change feed: another episode.")),
                ),
            ),
            // 33: "So once it all worked, what did the Coach do with its five notes?"
            Scene(
                33, "What the Coach did next", rag(),
                beats = listOf(
                    Beat(33, show = setOf("notes")),
                    Beat(34, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = BoardCard("RAG", "The top notes go into the prompt; the chat model answers only from them.")),
                ),
            ),
            // 35: "Did anything still go wrong?"
            Scene(
                35, "Where it stops", limits(),
                beats = listOf(
                    Beat(36, show = setOf("close", "shared"), light = setOf("close", "shared"), card = BoardCard("Closest isn't relevant", "Check the scores; let the chat model say it doesn't know.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("new", "flat"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(40, card = BoardCard("Today", "Enable vector search, a quantizedFlat container, five notes, one question.")),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-vector--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Finding things by meaning", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q"), card = BoardCard("Tonight", "Finding things by what they mean, not by the words they use.")),
                    Beat(2, show = setOf("note"), light = setOf("q", "note"), card = BoardCard("The perfect answer", "Handle 429 with autoscale. Barely a word in common.")),
                    Beat(4, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "Numbers from an AI model, often 1,536. Similar meaning, similar direction.")),
                    Beat(6, light = setOf("vec"), card = BoardCard("Under the picture", "A point in a space with 1,536 directions; every number a coordinate.")),
                ),
            ),
            // 7: "Close together, in the dark. I like that. Where do they live?"
            Scene(
                7, "The Coach", coach(),
                beats = listOf(
                    Beat(8, show = setOf("user", "api", "db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("Vector search", "The question is embedded too; the closest vectors come back.")),
                    Beat(10, show = setOf("emb"), light = setOf("emb"), pulse = "api" to "emb", card = BoardCard("Embedding model", "Azure OpenAI text-embedding-3-small, signed in with Entra ID.")),
                    Beat(12, light = setOf("emb", "db"), card = BoardCard("One promise", "The same model for the notes and the question, or nonsense.")),
                ),
            ),
            // 13: "I've been on dates like that. So how close is close?"
            Scene(
                13, "How close is close", distance(),
                beats = listOf(
                    Beat(13, show = setOf("q")),
                    Beat(14, show = setOf("cos", "dot", "euc"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Distance function", "Cosine: the angle, higher is closer. Euclidean: lower is closer.")),
                ),
            ),
            // 15: "And Cosmos DB? Does it just do this, out of the box?"
            Scene(
                15, "Setting it up", container(),
                beats = listOf(
                    Beat(16, show = setOf("acct", "cont"), light = setOf("acct", "cont"), pulse = "acct" to "cont", card = BoardCard("Account, then a new container", "Enable vector search on the account; vector policies only at creation.")),
                    Beat(18, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Vector embedding policy", "float32, 1,536 dimensions, a distance function. Not on shared throughput.")),
                    Beat(20, show = setOf("excl", "index"), light = setOf("excl", "index"), pulse = "cont" to "excl", card = BoardCard("Indexing policy", "Exclude the vector path from the normal index; add a vector index.")),
                ),
            ),
            // 21: "Tell me slowly."
            Scene(
                21, "Three vector indexes", indexes(),
                beats = listOf(
                    Beat(22, show = setOf("flat", "qf"), light = setOf("flat", "qf"), card = BoardCard("flat and quantizedFlat", "flat: exact, up to 505 dims. quantizedFlat: compressed, to about 50,000.")),
                    Beat(24, show = setOf("disk"), light = setOf("disk"), card = BoardCard("diskANN", "Near neighbours holding hands: a graph. Approximate, fastest at scale.")),
                    Beat(26, show = setOf("dims"), light = setOf("qf", "dims"), pulse = "dims" to "qf", card = BoardCard("The Coach: quantizedFlat", "A few hundred in scope; flat can't take 1,536 dimensions.")),
                ),
            ),
            // 27: "And the search itself? What do I actually write?"
            Scene(
                27, "The search", query(),
                beats = listOf(
                    Beat(27, show = setOf("q", "rank"), light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("VectorDistance", "The function that scores how similar two vectors are.")),
                    Beat(28, show = setOf("filter", "top"), light = setOf("top", "filter"), pulse = "rank" to "top", card = sql),
                ),
            ),
            // 29: "Five notes, then. What does the Coach do with them?"
            Scene(
                29, "Grounded answers", rag(),
                beats = listOf(
                    Beat(29, show = setOf("notes")),
                    Beat(30, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = BoardCard("RAG", "Retrieval augmented generation: answer only from your own notes.")),
                ),
            ),
            // 31: "Now the part I've been waiting for. Where does all this stop working?"
            Scene(
                31, "Where it stops", limits(),
                beats = listOf(
                    Beat(32, show = setOf("close", "again", "feed", "shared"), light = setOf("close", "again"), pulse = "again" to "feed", card = BoardCard("Closest isn't relevant", "Look at the scores. A changed note is embedded again.")),
                    Beat(34, light = setOf("feed"), card = BoardCard("Your next late night", "The pgvector episode: the same idea on PostgreSQL.")),
                ),
            ),
            // 35: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("flat", "big", "small"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: the index, TOP N, one model.")),
                    Beat(38, card = BoardCard("Today", "Enable vector search, a quantizedFlat container, five notes, one question.")),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-vector--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q"), card = BoardCard("Date night", "Finding someone close to you: vector search in Cosmos DB.")),
                    Beat(4, show = setOf("note"), light = setOf("q", "note"), card = BoardCard("A perfect match", "Stop throttling? Handle 429 with autoscale. Hardly a word in common.")),
                    Beat(6, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "Numbers from an AI model, often 1,536. Similar meaning, similar direction.")),
                    Beat(8, light = setOf("vec"), card = BoardCard("A small distance apart", "Each list is a vector: a point with 1,536 dimensions. Close means similar.")),
                ),
            ),
            // 9: "A small distance apart. Exactly where I'd like to be..."
            Scene(
                9, "Where they live", coach(),
                beats = listOf(
                    Beat(10, show = setOf("user", "api", "emb", "db"), light = setOf("emb", "db"), pulse = "api" to "db", card = BoardCard("Vector search", "Embed the question with the same model; find the closest notes.")),
                    Beat(11, light = setOf("emb"), card = BoardCard("Quiz: two models", "Notes and questions from different models. Nonsense results. Why?")),
                    Beat(13, light = setOf("emb"), pulse = "api" to "emb", card = BoardCard("Right answer", "Vectors from different models aren't comparable. Same model, always.")),
                ),
            ),
            // 14: "Mm. My turn. How does a database measure similarity?..."
            Scene(
                14, "Similarity", distance(),
                beats = listOf(
                    Beat(14, show = setOf("q")),
                    Beat(15, show = setOf("cos", "dot", "euc"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Distance function", "Cosine and dot product: higher is closer. Euclidean: lower is closer.")),
                    Beat(16, light = setOf("cos"), card = BoardCard("Cosine is the angle", "Which way two vectors face, not how long they are.")),
                ),
            ),
            // 17: "I'll take that as a compliment. Now, Cosmos DB..."
            Scene(
                17, "Commitment", container(),
                beats = listOf(
                    Beat(18, show = setOf("acct", "cont"), light = setOf("cont"), pulse = "acct" to "cont", card = BoardCard("A new container", "Enable the account first; vector policies are set at creation.")),
                    Beat(20, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Quiz: expensive inserts", "Every insert suddenly costs far more. What was forgotten?")),
                    Beat(21, show = setOf("excl", "index"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Exclude the vector path", "From the normal index, and give it a vector index instead.")),
                ),
            ),
            // 23: "Now the vector indexes..."
            Scene(
                23, "Nearest neighbours", indexes(),
                beats = listOf(
                    Beat(24, show = setOf("flat", "qf"), light = setOf("flat", "qf"), card = BoardCard("flat and quantizedFlat", "flat: exact, up to 505 dims. quantizedFlat: compressed, to about 50,000.")),
                    Beat(26, show = setOf("disk"), light = setOf("disk"), card = BoardCard("diskANN", "A graph of near neighbours: approximate, fastest at scale.")),
                    Beat(27, show = setOf("dims"), light = setOf("dims", "flat"), pulse = "dims" to "flat", card = BoardCard("Quiz: 1,536 with flat", "Notes with 1,536 dimensions and a flat index. Good idea?")),
                    Beat(28, light = setOf("flat"), card = BoardCard("Right answer", "flat stops at 505 dimensions. The exam's favourite trap.")),
                    Beat(30, light = setOf("dims"), card = BoardCard("Quiz: a few hundred in scope", "300,000 notes, every search filtered to one learner. Which index?")),
                    Beat(32, light = setOf("qf"), pulse = "dims" to "qf", card = BoardCard("No kiss: it's quantizedFlat", "Under 50,000 in scope. diskANN is for far more than that.")),
                ),
            ),
            // 33: "Bigger isn't always better. Noted..."
            Scene(
                33, "My top one", query(),
                beats = listOf(
                    Beat(33, show = setOf("q", "rank")),
                    Beat(34, show = setOf("filter"), light = setOf("rank", "filter"), pulse = "filter" to "rank", card = sql),
                    Beat(35, show = setOf("top"), light = setOf("top"), pulse = "rank" to "top", card = BoardCard("TOP N", "Without it, far more items are scored and the RU charge climbs.")),
                ),
            ),
            // 37: "My question, then. The Coach must answer only from the learner's own notes..."
            Scene(
                37, "Grounded", rag(),
                beats = listOf(
                    Beat(37, show = setOf("notes"), card = BoardCard("Quiz: only from the notes", "Answer only from the learner's notes, and cite them. The pattern?")),
                    Beat(38, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = BoardCard("RAG", "Retrieval augmented generation: retrieve, then answer only from it.")),
                ),
            ),
            // 40: "And where does it all stop working, Rafe?..."
            Scene(
                40, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(41, show = setOf("close", "again", "feed", "shared"), light = setOf("close"), pulse = "again" to "feed", card = BoardCard("Closest isn't the one", "Check the scores. Change feed for edits; pgvector for PostgreSQL.")),
                ),
            ),
            // 42: "Last question, for the exam. What do we remember?"
            Scene(
                42, "For the exam", exam(),
                beats = listOf(
                    Beat(43, show = all6, light = setOf("models", "rus", "new"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(46, card = BoardCard("Today", "Enable vector search, a quantizedFlat container, five notes, one question.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
