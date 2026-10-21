package com.shahaabapps.core.content

/** pgvector on PostgreSQL: vector search and RAG: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesPgvector {
    private val classic = Podcast(
        "ai-pgvector",
        listOf(
            maya("Hi, and welcome back. Today is about searching by meaning, inside a database you may already use. Say you're building the Two Tracks Coach, a study helper in an Android app, with a small Python server on Azure, Microsoft's cloud. A learner asks, what is a dead-letter queue? The answer is in their notes, but in different words. How do we find it?"),
            leo("Search for the words? [pause] No. If the note says it differently, a word search finds nothing."),
            maya("Exactly. So we compare meanings instead. An AI model reads a piece of text and gives back a list of numbers that captures what it means. That list is called an embedding. Two sentences that mean the same thing get lists that sit close together, like two pins near each other on a map."),
            leo("Pins on a map. I can picture that. So where do these lists live?"),
            maya("Today's answer: in PostgreSQL. PostgreSQL is a popular free database that keeps data in tables of rows and columns, a bit like a very strict spreadsheet. Plenty of apps already keep their data there. So why not keep each note's meaning right beside the note?"),
            leo("Can PostgreSQL even hold a list of numbers in one column?"),
            maya("Not on its own. It needs an extension, which is an add-on, like a plug-in for your browser. This add-on is called pgvector. It gives PostgreSQL a new kind of column, called vector, that holds one embedding, and new ways to measure how far apart two embeddings are."),
            leo("And on Azure, I just tick a box?"),
            maya("Two steps, and the first one trips everyone. Azure's PostgreSQL service, the one Microsoft runs for you, keeps a guest list of add-ons it will allow. It's a server setting called azure.extensions, and on a new server the list is empty. So step one: put VECTOR on the guest list."),
            leo("Like a bouncer with a clipboard. And step two?"),
            maya("In each database that needs it, you run the command that installs the add-on: CREATE EXTENSION vector. Notice the name. The project is called pgvector, but the add-on's real name is just vector. Type CREATE EXTENSION pgvector, and the database says it has never heard of it."),
            leo("[chuckle] Famous, but not under its own name. Then what?"),
            maya("Then a notes table with a vector column. You say how many numbers each vector holds, and that must match your embedding model. The usual model, text-embedding-3-small, gives 1,536 numbers for every text, so the column holds 1,536. Each number is one dimension, one coordinate on that map."),
            leo("Okay, the notes are stored. How does the database decide which ones are close to my question?"),
            maya("It measures a distance, and pgvector offers three. Each one is a small symbol you put in the query, called an operator. The first is Euclidean distance: the straight line between two pins, as if you measured it with a ruler."),
            leo("That's the one I'd have guessed. The second?"),
            maya("Cosine distance. It ignores how long two arrows are and looks only at the angle between them, at which way they point. Two notes facing the same way mean the same thing. It's the usual choice for text."),
            leo("And the third?"),
            maya("Negative inner product. Inner product is a quick multiply-and-add of the two lists. The usual embedding models give arrows that are all the same length, and then it ranks the notes just like cosine, but it's the cheapest to work out. With all three, smaller means closer."),
            leo("So the search is: sort by distance, closest first, and keep a few?"),
            maya("That's it. In SQL, the language you talk to the database in, you write ORDER BY the distance to the question, then LIMIT 5. Five good notes beat fifty. Now the catch. With no help, the database compares the question with every single row."),
            leo("Which sounds fine, until the table is big."),
            maya("For example, the Coach has 400,000 notes, and reading them all took 900 milliseconds, nearly a second, for every question. The fix is an index. Like the index at the back of a book, it lets the database jump near the answer instead of reading every page."),
            leo("Does it still find the very closest notes?"),
            maya("Nearly always. These indexes are approximate: they look at far fewer rows, and now and then miss a true neighbour. How much they find is called recall. A recall of 0.93 means it found 93 of every 100 notes that reading every row would have found."),
            leo("And which index do I pick?"),
            maya("Two you must know. HNSW stands for hierarchical navigable small world. In plain words, it's a web of links in layers: every note is linked to a few near neighbours. A search travels the way you drive: motorway first, then main roads, then your street."),
            leo("What's it good at?"),
            maya("The best balance of speed and recall, so it suits most apps that answer from notes. It's slower to build and uses more memory. And it needs no training, so you can create it on an empty table and fill the table later."),
            leo("Training? Indexes go to the gym now?"),
            maya("[laugh] One of them does. IVFFlat sorts the vectors into groups, like a library putting books on shelves by subject. A search only checks the shelves nearest the question. It builds faster and uses less memory. But it works out its shelves from the data already there, so you build it after loading."),
            leo("So IVFFlat on an empty table is a library with shelves for nothing."),
            maya("Perfect. Each index has a dial for recall. For HNSW it's ef search: how many candidates it keeps on its shortlist while searching, 40 by default. Turn it up, and you find more true neighbours, a little slower."),
            leo("And the dial on IVFFlat?"),
            maya("It's called probes: how many shelves a search checks, one by default. More shelves, better recall, a little slower. Same deal."),
            leo("And if my table grows to many millions of notes?"),
            maya("Then name DiskANN, an add-on only Azure offers, built for very large vector tables. Now the trap the exam loves. Each index is built for one operator. Build a cosine index, then sort by the ruler distance in your query, and the database quietly ignores the index and reads every row again."),
            leo("How would I even notice?"),
            maya("Put EXPLAIN in front of the query. It shows the database's plan. Index Scan means your index is used. Seq Scan, a sequential scan, means it's reading every row, one after another. Picture a small app whose search got slow: EXPLAIN is the first thing to try."),
            leo("One more worry. Could my search find someone else's notes?"),
            maya("It could, unless you stop it. Put a filter in the WHERE part of the query, on the user id and the course, so only this learner's notes compete at all. Don't fetch the top five from everyone and filter afterwards in Python. That's how an answer ends up quoting a stranger."),
            leo("And then the Coach answers from those five notes?"),
            maya("Yes, and that has a name: RAG, retrieval augmented generation. Retrieve the closest notes, hand them to a chat model with the question, and ask it to answer only from them. So the answer is built on the learner's own notes."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If CREATE EXTENSION is refused, VECTOR isn't in azure.extensions yet, and the name is vector. If the index isn't used, match the query's operator to the index. Best speed and recall, or an empty table: HNSW. Faster build, less memory, data loaded first: IVFFlat."),
            leo("And the other two?"),
            maya("If recall is too low, raise ef search, or probes for IVFFlat. And if an answer quotes another learner's notes, the query needs a filter on the user id in its WHERE part."),
            leo("Great. Anything to try today?"),
            maya("About fifteen minutes on a small practice server. Allow VECTOR in azure.extensions and run CREATE EXTENSION vector. Create a table with a vector column of just three numbers, and insert five rows. Order by each of the three operators and compare the top three rows. Then build an HNSW cosine index and confirm the index scan with EXPLAIN."),
            leo("A database that reads between the lines. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-pgvector",
        listOf(
            maya("Welcome to Case Files. Today's case: The Borrowed Notes. A small Android app called Flashcard Fox helps students revise, with a Python server on Azure, Microsoft's cloud. Its new feature answers questions from your own notes. A student asks, what is a dead-letter queue? And the answer quotes notes she never wrote. Someone else's notes."),
            leo("[pause] Detective Leo is on the case. But first, how was it meant to find her notes, in plain words?"),
            maya("By meaning. An AI model turns each note into an embedding: a list of numbers that captures what the text means. Notes that mean the same thing get lists that sit close together, like two pins near each other on a map. The question gets a list too, and the app looks for the closest notes."),
            leo("And where do those lists live?"),
            maya("In PostgreSQL, a popular free database that keeps data in tables of rows and columns, like a very strict spreadsheet. Flashcard Fox already kept its notes there."),
            leo("And a strict spreadsheet understands meaning, all by itself?"),
            maya("Not by itself. So the team added pgvector. That's an extension, an add-on, like a plug-in for your browser, which lets PostgreSQL store embeddings and compare them."),
            leo("Is it really just a plug-in? Under the picture, I mean."),
            maya("Under the picture, it's code loaded into the database server itself. It adds a new column type called vector, which packs the numbers tightly side by side. To the database, it looks as if it had always been there."),
            leo("Then my first suspect: they installed it wrong. Somebody typed CREATE EXTENSION pgvector."),
            maya("Good memory, and that did happen, on day one. The add-on's real name is just vector. And before even that works, Azure's PostgreSQL service keeps a guest list of allowed add-ons, a setting called azure.extensions, which starts empty. VECTOR had to go on the list. But all that was fixed weeks ago. Wrong suspect."),
            leo("Suspect two. The distance. They picked the wrong one, so the ranking picks the wrong notes."),
            maya("Changing the distance changes the order of the notes, not who they belong to. The clue is in the code. The server asked for the five closest notes from the whole table, every student's. The plan was to filter by student afterwards, in Python, and the new feature skipped that step."),
            leo("So the filter lived in the wrong place."),
            maya("Exactly. It belongs in the query itself, in its WHERE part, on the user id and the course. That's a metadata filter, a filter on facts about each note, like whose it is. It decides which rows may compete at all. Only her notes are in the race, so only her notes can win. And fewer rows competing makes the search faster, too."),
            leo("Faster. Good, because the second complaint in the file says every question takes almost a second."),
            maya("Second witness, then. Flashcard Fox has 400,000 notes. With no help, the database compares the question with every single row. Exact, but slow. So the team had built an index. Like the index at the back of a book, it lets the database jump near the answer instead of reading every page."),
            leo("So the index is broken. Suspect: the table needs a clean-up. VACUUM, or whatever it's called."),
            maya("[chuckle] VACUUM tidies away old deleted rows. It wouldn't change a thing. Here's the clue. They put EXPLAIN in front of the query. EXPLAIN shows the database's plan for a query, step by step."),
            leo("And what did the plan say?"),
            maya("Seq Scan, a sequential scan: reading every row, one after another. The index was being ignored."),
            leo("Why would it ignore a perfectly good index?"),
            maya("Because each index is built for one operator, a small symbol in the query that says how to measure distance. pgvector has three. The first is Euclidean distance: the straight line between two pins, as if you measured it with a ruler."),
            leo("And the second?"),
            maya("Cosine distance. It looks only at the angle, at which way two arrows point, and it's the usual choice for text."),
            leo("And the third?"),
            maya("Negative inner product, a quick multiply-and-add, the cheapest of the three. For all of them, smaller means closer."),
            leo("And the team mixed them up."),
            maya("They built the index for cosine, then sorted the query by the ruler distance. An index only serves its own operator. They switched the query to the cosine operator, EXPLAIN said Index Scan, and each question fell from 900 milliseconds to 15."),
            leo("Which index was it, by the way?"),
            maya("HNSW, which stands for hierarchical navigable small world. Picture a web of links in layers: every note linked to a few near neighbours."),
            leo("Does it always find the very closest notes?"),
            maya("Not always. It's approximate: it looks at far fewer rows, and now and then misses a true neighbour. How much it finds is called recall. Theirs was 0.93: 93 of every 100 right notes."),
            leo("How does it actually search the web, though? It can't be magic."),
            maya("It starts on the top layer, which holds only a few notes, far apart, and jumps to whichever is closest to the question. Then it drops a layer and does it again, nearer each time, like motorway, then main road, then your street. On the way it keeps a shortlist of candidates. That shortlist's size is called ef search, 40 by default."),
            leo("So a longer shortlist finds more?"),
            maya("Yes. They set ef search to 80, and recall rose to 0.98, a little slower. Now the third case. A new table, three million flashcards, created empty and filled during the night. Recall the next morning was dreadful."),
            leo("Easy. Raise probes."),
            maya("[pause] Probes is the dial of the other index, IVFFlat. And that's exactly what they'd built. IVFFlat sorts vectors into groups, like a library putting books on shelves by subject, and searches only the nearest shelves. Probes says how many. But it works out its shelves from the data already there, and the table was empty."),
            leo("Shelves for nothing. So, build it after loading?"),
            maya("Or use HNSW, which needs no training and can be built on an empty table. They rebuilt with HNSW. IVFFlat is the pick when you want a faster build and less memory, with the data already loaded."),
            leo("Any loose ends in the file?"),
            maya("Where it stops working. Approximate means it can miss. At many millions of rows, Azure offers DiskANN, an add-on built for very large vector tables. The index should fit in the server's memory, so pick a server size with plenty of it. And the Cosmos DB vector search episode shows the same idea in another database."),
            leo("And once the right notes come back?"),
            maya("That's RAG, retrieval augmented generation. The five notes go to a chat model with the question, and it answers only from them. So the answer is built on the student's own notes. Now, truly, her own."),
            leo("So, the case summary for the exam?"),
            maya("If an answer quotes another user's notes, filter on the user id in WHERE. If EXPLAIN shows a Seq Scan, match the operator to the index. Best speed and recall, or an empty table, is HNSW; faster build with data loaded is IVFFlat. Low recall: raise ef search. Refused install: VECTOR in azure.extensions, and the name is vector."),
            leo("Case closed. Anything to try today?"),
            maya("Allow VECTOR in azure.extensions and run CREATE EXTENSION vector. Create a table with a vector column of three numbers, and insert five rows. Order by each of the three operators and compare the top three rows. Then build an HNSW cosine index, and confirm the index scan with EXPLAIN."),
            leo("Everyone's notes back where they belong. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-pgvector",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? They added search by meaning to the Two Tracks Coach, the study helper in the Android app, with its Python server on Azure, Microsoft's cloud. And every day, someone made the week a little longer."),
            leo("[laugh] I heard there was a whiteboard titled shelves for nothing. What were they building?"),
            maya("A search that finds a learner's notes by meaning, not by words. An AI model turns each note into an embedding, a list of numbers that captures what it means. Notes that mean the same thing get lists that sit close together, like two pins near each other on a map."),
            leo("And they kept those lists where?"),
            maya("In PostgreSQL, the database the Coach already uses. It's popular and free, and keeps data in tables of rows and columns, like a very strict spreadsheet."),
            leo("A strict spreadsheet that understands meaning? Since when?"),
            maya("Since pgvector. It's an extension, an add-on, like a plug-in for your browser, that lets PostgreSQL store embeddings and compare them."),
            leo("What does the plug-in actually do, under the picture?"),
            maya("It's code loaded into the database server. It adds a column type called vector, which packs the numbers tightly side by side. Okay. Monday. Ben."),
            leo("What did Ben do?"),
            maya("Ben opened the database and typed CREATE EXTENSION pgvector, the command that installs an add-on. Refused. He typed it again, louder, somehow. Refused."),
            leo("[pause] Why?"),
            maya("Two reasons. Azure's PostgreSQL service keeps a guest list of allowed add-ons, a setting called azure.extensions, and on a new server it's empty. VECTOR goes on the list first. And the add-on's real name is just vector. So it's CREATE EXTENSION vector, in every database that needs it."),
            leo("Poor Ben. Tuesday?"),
            maya("Tuesday was Aisha. Searches were slow, nearly a second each, because the database compared the question with every single note. So Aisha built an index. Like the index at the back of a book, it lets the database jump near the answer instead of reading every page."),
            leo("That sounds like a win."),
            maya("It would have been. But pgvector has three ways to measure, each its own operator, a small symbol in the query. The first is Euclidean distance: the straight line between two pins, as if you used a ruler."),
            leo("Sensible. And the second?"),
            maya("Cosine distance. It looks only at the angle, at which way two arrows point. The usual one for text."),
            leo("You said three."),
            maya("The third is negative inner product, a quick multiply-and-add, the cheapest of them. For all three, smaller means closer. Aisha built her index for cosine. But the query still sorted by the ruler distance."),
            leo("And an index only helps its own operator?"),
            maya("Exactly. The database ignored it completely. She only found out because she put EXPLAIN in front of the query, which shows the database's plan. It said Seq Scan, a sequential scan, reading every row. She switched to the cosine operator, and it said Index Scan. 900 milliseconds became 15."),
            leo("Wednesday. Let me guess. The shelves."),
            maya("Wednesday was Marcus. A new table, three million flashcards, created empty, to be filled overnight. Marcus built an IVFFlat index on it straight away. IVFFlat sorts vectors into groups, like a library putting books on shelves by subject, and searches only the nearest shelves."),
            leo("So where did it go wrong?"),
            maya("It works out its shelves from the data already in the table. And the table was empty. Shelves for nothing, hence the whiteboard. Build IVFFlat after loading. Or use HNSW, which needs no training at all."),
            leo("HNSW. Is that a person or a thing?"),
            maya("[chuckle] A thing. It stands for hierarchical navigable small world: a web of links in layers, each note linked to a few near neighbours. A search starts on a sparse top layer, jumps to whatever is closest, drops a layer, and repeats, keeping a shortlist of candidates. Best balance of speed and recall, though slower to build."),
            leo("Recall. That's the word everyone used in the Thursday meeting."),
            maya("Because Thursday was Priya. Recall is how much of the true closest notes a search actually finds. These indexes are approximate, so they can miss. Priya measured 0.93, 93 of every 100."),
            leo("And Priya, being Priya, fixed it how?"),
            maya("She raised ef search, the size of that shortlist, from its default of 40 to 80, and got 0.98, for a slightly slower search. A longer shortlist finds more true neighbours."),
            leo("So Priya did nothing wrong? I'm disappointed."),
            maya("Priya never does. Friday did, though. The intern. Their search fetched the five closest notes from every learner, then filtered by learner afterwards, in Python. And one code path forgot to filter. A learner's answer quoted somebody else's notes."),
            leo("Oof. What's the fix?"),
            maya("A metadata filter, a filter on facts about each note, like whose it is. It goes in the WHERE part of the query, on the user id and the course. Then only this learner's notes compete at all. It's safer, and faster, too."),
            leo("And after all that, what does the Coach do with the five notes?"),
            maya("RAG, retrieval augmented generation. The notes go to a chat model with the question, and it answers only from them. So every answer is built on the learner's own notes."),
            leo("Did anything still not work?"),
            maya("Only where pgvector stops. Approximate indexes can still miss. At many millions of rows, Azure offers DiskANN, an add-on built for huge vector tables. The index should fit in memory. And the Redis episode shows how to reuse an answer when the next question means the same thing."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Refused install: put VECTOR in azure.extensions, and call it vector. Seq Scan with an index: match the operator. Best speed and recall, or an empty table: HNSW; faster build with data loaded: IVFFlat. Low recall: raise ef search. Someone else's notes: filter on the user id in WHERE."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Allow VECTOR in azure.extensions and run CREATE EXTENSION vector. Create a table with a vector column of three numbers, and insert five rows. Order by each of the three operators and compare the top three rows. Then build an HNSW cosine index and confirm the index scan with EXPLAIN."),
            leo("No empty shelves for me. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-pgvector",
        listOf(
            nova("Hey, you. It's late, the kettle's cooling, and you're still up, still learning. [pause] Tonight, Ivy and I want to teach an old database a new trick."),
            ivy("An old database, at this hour. You do know how to spoil a girl, Nova."),
            nova("Mm. Picture a learner, revising late, like you. They ask their study app, what is a dead-letter queue? The answer is in their notes. Just in different words. [pause] So a search for the words finds nothing."),
            ivy("So how does the app find it?"),
            nova("By meaning. An AI model reads each note and gives back a list of numbers that captures what it means. That's an embedding. Two notes that mean the same thing get lists that sit close together, like two pins near each other on a map."),
            ivy("Pins on a map, in the dark. And where do the pins live?"),
            nova("Tonight, in PostgreSQL. A popular free database that keeps data in tables of rows and columns, like a very strict spreadsheet. The Two Tracks Coach, the study helper in the Android app, already keeps its notes there, behind a Python server on Azure, Microsoft's cloud."),
            ivy("And a strict spreadsheet just understands meaning now?"),
            nova("Not on its own. It needs an extension, an add-on, like a plug-in for your browser. This one's called pgvector. It gives PostgreSQL a column type called vector, to hold one embedding, and new ways to measure how far apart two of them are."),
            ivy("Tell me what a plug-in really is. Under the picture."),
            nova("Under the picture, it's code loaded into the database server itself. The vector column packs its numbers tightly, side by side. [pause] To the database, it's as if it had always been there."),
            ivy("And on Azure, I just switch it on?"),
            nova("Two gentle steps. Azure's PostgreSQL service keeps a guest list of add-ons it allows, a setting called azure.extensions. On a new server it's empty. So VECTOR goes on the list. Then, in each database, CREATE EXTENSION vector. Vector, not pgvector. The add-on goes by a shorter name than its fame."),
            ivy("[chuckle] Don't we all. So how does it measure closeness?"),
            nova("Three ways, each a small symbol in the query, called an operator. The first is Euclidean distance: the straight line between two pins, as if you laid a ruler across the map."),
            ivy("A ruler. So practical. And the second?"),
            nova("Cosine distance. [pause] It looks only at the angle, at which way two arrows face. That's the usual one for text."),
            ivy("Which way you're facing. I like that one. And the third?"),
            nova("Negative inner product. A quick multiply-and-add, the cheapest to work out. With all three, smaller means closer. Then you sort the notes by distance to the question, closest first, and keep five. In SQL, the database's language, that's ORDER BY the distance, then LIMIT 5."),
            ivy("Sort every note, every time? That sounds like a long night."),
            nova("It is, without help. With 400,000 notes, it took almost a second a question. So you build an index. Like the index at the back of a book, it lets the database jump near the answer instead of turning every page."),
            ivy("Which index, Nova? There's always more than one."),
            nova("Two to know. HNSW stands for hierarchical navigable small world. A web of links in layers, each note holding hands with a few near neighbours. Best balance of speed and accuracy. And it needs no training, so you can build it on an empty table."),
            ivy("How does it actually find its way, in that web?"),
            nova("It starts on the top layer, where only a few notes live, far apart. It steps to whichever is nearest the question, then drops a layer and does it again. Like driving home: motorway, main road, your street. [pause] And it keeps a shortlist of candidates on the way. That shortlist's size is ef search, 40 by default."),
            ivy("And the other index?"),
            nova("IVFFlat. It sorts vectors into groups, like a library shelving books by subject, and checks only the nearest shelves. Faster to build, less memory. But it learns its shelves from the data already there, so it's built after loading. Never on an empty table."),
            ivy("Approximate, all of it. Do they ever miss?"),
            nova("Sometimes. How much they find is called recall. A recall of 0.93 means 93 of every 100 true closest notes. Want more? Raise ef search, and wait a breath longer. The Coach went from 0.93 to 0.98 with ef search at 80."),
            ivy("And the dial on IVFFlat?"),
            nova("Probes. How many shelves a search checks, one by default. [pause] More shelves, better recall, a little slower."),
            ivy("And now the trap. I can feel one coming."),
            nova("An index serves only its own operator. Build it for cosine, then sort by the ruler distance, and the database ignores your index and reads every row. Put EXPLAIN in front of the query to see its plan. Seq Scan, a sequential scan, every row. Index Scan, you're fine."),
            ivy("Mm. And whose notes come back? Only mine, I hope."),
            nova("Only yours, if the query says so. A filter in its WHERE part, on your user id and the course. Never fetch the top five from everyone and filter later in Python. That's how a stranger's notes slip into your answer."),
            ivy("Then the Coach answers me from my own notes?"),
            nova("That's RAG, retrieval augmented generation. Your five notes go to a chat model with your question, and it answers only from them. [pause] Built on your notes, not its imagination."),
            ivy("Now tell me where it all stops working."),
            nova("Approximate can miss, so check your recall. At many millions of rows, Azure offers DiskANN, an add-on made for huge vector tables. And the index should fit in the server's memory, or every search waits on the disk. The Cosmos DB vector search episode does this same dance in another database, if you're not sleepy yet."),
            ivy("[sigh] Almost. Before I go, what do I remember for the exam?"),
            nova("Refused install: VECTOR in azure.extensions, and the name is vector. A Seq Scan despite an index: match the operator. Best speed and recall, or an empty table: HNSW. Faster build, data loaded first: IVFFlat. Low recall: raise ef search. Someone else's notes: filter on the user id in WHERE."),
            ivy("And something to try today, before I sleep?"),
            nova("Allow VECTOR in azure.extensions and run CREATE EXTENSION vector. Create a table with a vector column of three numbers, and insert five rows. [pause] Order by each of the three operators and compare the top three rows. Then build an HNSW cosine index, and confirm the index scan with EXPLAIN."),
            ivy("Pins on a map, all facing the same way. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-pgvector",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about how to find your perfect match."),
            rafe("I already found mine. But go on, tell me how a database does it."),
            nova("[laugh] With pgvector, in PostgreSQL. Picture the Two Tracks Coach, the study helper in the Android app, with a Python server on Azure, Microsoft's cloud. A learner asks, what is a dead-letter queue? The answer's in their notes, in completely different words."),
            rafe("Different words, same meaning. The story of our first date."),
            nova("Exactly. So the app compares meaning. An AI model turns each note into a list of numbers that captures what it means. That's an embedding. Two notes that mean the same thing get lists that sit close together, like two pins near each other on a map."),
            rafe("Close together on a map. I like where this is going. Where do the pins live?"),
            nova("In PostgreSQL, a popular free database that keeps data in tables of rows and columns, like a very strict spreadsheet. The Coach keeps its notes there already."),
            rafe("A strict spreadsheet. My type. But can it hold a list of numbers?"),
            nova("Not alone. It needs an extension, an add-on, like a plug-in for your browser. That's pgvector. Under the picture, it's code loaded into the database server, adding a column type called vector that holds the numbers side by side."),
            rafe("Then let me test you, darling. Exam style. On a brand-new Azure server, you run CREATE EXTENSION vector, the command that installs the add-on, and it's refused. Why?"),
            nova("Because Azure's PostgreSQL service keeps a guest list of add-ons it allows, a setting called azure.extensions, and on a new server it's empty. Put VECTOR on the list first. And the add-on is named vector, never pgvector."),
            rafe("Right answer. You always know how to get on the list. [kiss]"),
            nova("Mm. Now, measuring closeness. pgvector has three small symbols for it, called operators. The first is Euclidean distance: the straight line between two pins, a ruler across the map."),
            rafe("A ruler. How very romantic. And the second?"),
            nova("Cosine distance. It looks only at the angle, at which way two arrows face. The usual choice for text."),
            rafe("Which way you're facing, not how tall you are. Thank goodness. And the third?"),
            nova("Negative inner product, a quick multiply-and-add, the cheapest of the three. For all of them, smaller means closer. Then you sort by distance, closest first, and keep five. In SQL, the database's language, ORDER BY the distance, LIMIT 5."),
            rafe("Sorting every note in the table every time sounds exhausting."),
            nova("It is. 400,000 notes took almost a second a question. So you build an index, like the one at the back of a book, to jump near the answer. Now, your question, Rafe. The index was built for cosine. The query sorts by the ruler distance. The database's plan says Seq Scan: it's reading every row. What went wrong?"),
            rafe("An index only serves its own operator. Cosine index, cosine operator. Mix them, and the database ignores the index and reads every row, one after another. EXPLAIN shows you the plan; you want Index Scan."),
            nova("Right answer. We should always use the same language. [kiss]"),
            rafe("My turn to teach. Two indexes. HNSW stands for hierarchical navigable small world. A web of links in layers, each note holding hands with a few near neighbours. A search starts at the top, where it's sparse, steps toward the question, and drops a layer each time. Motorway, main road, your street."),
            nova("And IVFFlat?"),
            rafe("IVFFlat sorts vectors into groups, like a library shelving books by subject, and checks only the nearest shelves. Faster to build, less memory. But it learns its shelves from the data already there. So, darling. Three million flashcards, a table created empty and filled during the night. Which index?"),
            nova("IVFFlat. It builds faster, and I'm impatient."),
            rafe("No kiss for that one. On an empty table, IVFFlat learns its shelves from nothing. HNSW needs no training, so you can build it first and fill it later. And it has the best balance of speed and recall."),
            nova("Recall. Remind me, gently."),
            rafe("These indexes are approximate: they look at far fewer rows, and now and then miss a true neighbour. Recall is how much of the true closest notes a search actually finds. 0.93 means 93 of every 100."),
            nova("Then here's mine. Recall from an HNSW index is too low, and there's time to spare. What do you change first?"),
            rafe("Raise ef search. That's the size of the shortlist it keeps while searching, 40 by default. Longer list, more true neighbours, a little slower. For IVFFlat the dial is probes, the number of shelves it checks."),
            nova("Right answer. A longer list of candidates, and you still picked me. [kiss]"),
            rafe("Last quiz of the evening. The Coach's answer quoted another learner's notes. What should the query have had?"),
            nova("A filter in its WHERE part, on the user id and the course, so only this learner's notes compete at all. Not the top five from everyone, filtered later in Python. And RAG, retrieval augmented generation, means those notes go to a chat model with the question, and it answers only from them."),
            rafe("Right answer. What's yours stays yours. [kiss]"),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("Approximate can miss, so measure recall. Past many millions of rows, Azure offers DiskANN, an add-on built for huge vector tables. Keep the index in the server's memory. And the Cosmos DB vector search episode shows the same idea in another database."),
            nova("Then the last question, for the exam. What do we remember?"),
            rafe("Refused install: VECTOR in azure.extensions, and call it vector. Seq Scan despite an index: match the operator. Best speed and recall, or an empty table: HNSW; faster build with data loaded: IVFFlat. Low recall: raise ef search. Someone else's notes: filter on the user id in WHERE."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Allow VECTOR in azure.extensions and run CREATE EXTENSION vector. Create a table with a vector column of three numbers, and insert five rows. Order by each of the three operators and compare the top three rows. Then build an HNSW cosine index, and confirm the index scan with EXPLAIN."),
            rafe("And I know who's closest to me, by every measure. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun meaning() = DiagramSpec(
        "Two texts in different words get nearby embeddings, so the note is found by meaning.",
        nodes = listOf(
            DNode("q", "Question", 0.24f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter queue?"),
            DNode("note", "Note", 0.24f, 0.78f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "other words"),
            DNode("model", "Embedding model", 0.76f, 0.18f, w = 0.4f, h = 0.2f, tone = 2, sub = "text to numbers"),
            DNode("vec", "Nearby pins", 0.76f, 0.78f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "similar meaning"),
        ),
        edges = listOf(
            DEdge("q", "model", "embed"),
            DEdge("note", "model", "embed"),
            DEdge("model", "vec"),
        ),
        aspect = 0.8f,
    )

    private fun setup() = DiagramSpec(
        "Put VECTOR on the server's guest list, run CREATE EXTENSION vector, then store embeddings in a vector column.",
        nodes = listOf(
            DNode("srv", "Azure PostgreSQL", 0.25f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "run for you"),
            DNode("allow", "Guest list", 0.75f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "azure.extensions"),
            DNode("ext", "CREATE EXTENSION", 0.25f, 0.55f, w = 0.44f, h = 0.2f, sub = "vector, not pgvector"),
            DNode("table", "Notes table", 0.75f, 0.58f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "vector column"),
        ),
        edges = listOf(
            DEdge("srv", "allow", "guest list"),
            DEdge("allow", "ext", "then"),
            DEdge("ext", "table", "installs"),
        ),
        aspect = 0.8f,
    )

    private fun operators() = DiagramSpec(
        "Three operators measure distance: the ruler, the angle and multiply-and-add. For all three, smaller is closer.",
        nodes = listOf(
            DNode("q", "Question", 0.2f, 0.5f, w = 0.32f, h = 0.2f, tone = 1),
            DNode("l2", "Euclidean", 0.7f, 0.16f, w = 0.52f, h = 0.2f, sub = "a ruler: straight line"),
            DNode("cos", "Cosine", 0.7f, 0.5f, w = 0.52f, h = 0.2f, sub = "the angle: usual for text"),
            DNode("ip", "Inner product", 0.7f, 0.84f, w = 0.52f, h = 0.2f, sub = "multiply and add"),
        ),
        edges = listOf(DEdge("q", "l2"), DEdge("q", "cos"), DEdge("q", "ip")),
        aspect = 0.8f,
    )

    private fun indexes() = DiagramSpec(
        "Instead of reading every row, an approximate index: HNSW tuned by ef search, or IVFFlat tuned by probes.",
        nodes = listOf(
            DNode("scan", "Every row", 0.5f, 0.14f, w = 0.6f, h = 0.18f, tone = 2, sub = "exact, but slow"),
            DNode("hnsw", "HNSW", 0.26f, 0.5f, w = 0.44f, h = 0.24f, sub = "web in layers"),
            DNode("ivf", "IVFFlat", 0.76f, 0.5f, w = 0.44f, h = 0.24f, sub = "shelves by subject"),
            DNode("efs", "ef search", 0.26f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "shortlist, 40"),
            DNode("probes", "probes", 0.76f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "shelves, 1"),
        ),
        edges = listOf(
            DEdge("scan", "hnsw"),
            DEdge("scan", "ivf"),
            DEdge("hnsw", "efs"),
            DEdge("ivf", "probes"),
        ),
        aspect = 0.8f,
    )

    private fun layers() = DiagramSpec(
        "HNSW starts on a sparse top layer, steps toward the question, drops a layer, and keeps a shortlist of candidates.",
        nodes = listOf(
            DNode("top", "Top layer", 0.3f, 0.15f, w = 0.5f, h = 0.2f, sub = "few notes, far apart"),
            DNode("mid", "Middle layer", 0.3f, 0.45f, w = 0.5f, h = 0.2f, sub = "closer"),
            DNode("low", "Bottom layer", 0.3f, 0.75f, w = 0.5f, h = 0.2f, sub = "every note"),
            DNode("q", "Question", 0.8f, 0.15f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("short", "Shortlist", 0.8f, 0.75f, w = 0.32f, h = 0.2f, kind = NodeKind.PILL, sub = "ef search"),
        ),
        edges = listOf(
            DEdge("q", "top", "start"),
            DEdge("top", "mid", "step down"),
            DEdge("mid", "low", "step down"),
            DEdge("low", "short", "keeps"),
        ),
        aspect = 0.8f,
    )

    private fun match() = DiagramSpec(
        "An index serves only its own operator: a cosine index with a ruler query falls back to reading every row.",
        nodes = listOf(
            DNode("idx", "Cosine index", 0.26f, 0.2f, w = 0.44f, h = 0.2f, sub = "built for cosine"),
            DNode("qry", "Ruler query", 0.76f, 0.2f, w = 0.44f, h = 0.2f, tone = 2, sub = "Euclidean operator"),
            DNode("seq", "Seq Scan", 0.26f, 0.76f, w = 0.44f, h = 0.2f, tone = 2, sub = "every row read"),
            DNode("iscan", "Index Scan", 0.76f, 0.76f, w = 0.44f, h = 0.2f, sub = "operators match"),
        ),
        edges = listOf(
            DEdge("idx", "qry", "mismatch", dashed = true),
            DEdge("qry", "seq"),
            DEdge("idx", "iscan", "cosine query"),
        ),
        aspect = 0.8f,
    )

    private fun filter() = DiagramSpec(
        "The query filters on the learner in WHERE, takes the five closest notes, and a chat model answers only from them.",
        nodes = listOf(
            DNode("user", "Learner", 0.1f, 0.2f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("api", "Coach API", 0.42f, 0.2f, w = 0.34f, h = 0.2f, tone = 1, sub = "Python"),
            DNode("where", "WHERE user id", 0.8f, 0.2f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "and course"),
            DNode("pg", "PostgreSQL", 0.42f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "top 5 notes"),
            DNode("chat", "Chat model", 0.84f, 0.72f, w = 0.3f, h = 0.2f, tone = 2, sub = "answers"),
        ),
        edges = listOf(
            DEdge("user", "api", "question"),
            DEdge("api", "where", "filter"),
            DEdge("api", "pg", "query"),
            DEdge("where", "pg"),
            DEdge("pg", "chat", "5 notes"),
        ),
        aspect = 0.8f,
    )

    private fun limits(link: String, linkSub: String) = DiagramSpec(
        "Where pgvector stops: approximate can miss, huge tables want DiskANN, and the index should fit in memory.",
        nodes = listOf(
            DNode("miss", "Can miss", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "check recall"),
            DNode("disk", "DiskANN", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 2, sub = "many millions"),
            DNode("mem", "Fit in memory", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "or wait on disk"),
            DNode("link", link, 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = linkSub),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's pgvector questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("ext", "Refused install", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "azure.extensions"),
            DNode("seq", "Seq Scan", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "match the operator"),
            DNode("hnsw", "Empty table", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "HNSW"),
            DNode("ivf", "Faster build", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "IVFFlat, data first"),
            DNode("recall", "Low recall", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "raise ef search"),
            DNode("user", "Others' notes", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "WHERE user id"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("ext", "seq", "hnsw", "ivf", "recall", "user")
    private val tableCode = BoardCard("SQL", "CREATE TABLE notes (id bigserial PRIMARY KEY, user_id text, embedding vector(1536))", code = true)
    private val searchCode = BoardCard("SQL", "SELECT body FROM notes WHERE user_id = %s ORDER BY embedding <=> %s LIMIT 5", code = true)
    private val today = BoardCard("Today", "Allow VECTOR, a three-number table, three operators, an HNSW index, EXPLAIN.")

    private fun classicBoard() = PodcastBoard(
        "ai-pgvector",
        listOf(
            // 0: "Hi, and welcome back. Today is about searching by meaning..."
            Scene(
                0, "Meaning, not words", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q", "note"), light = setOf("q", "note"), card = BoardCard("The puzzle", "The answer is in the learner's notes, but in different words.")),
                    Beat(2, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "A list of numbers that captures meaning: like pins close on a map.")),
                ),
            ),
            // 4: "Today's answer: in PostgreSQL..."
            Scene(
                4, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(4, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(6, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An add-on: a vector column, and ways to measure distance.")),
                    Beat(8, show = setOf("srv", "allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Step one: the guest list", "Put VECTOR in azure.extensions. On a new server the list is empty.")),
                    Beat(10, light = setOf("ext"), pulse = "allow" to "ext", card = BoardCard("Step two", "CREATE EXTENSION vector in each database. Not pgvector.")),
                    Beat(12, light = setOf("table"), card = tableCode),
                ),
            ),
            // 13: "Okay, the notes are stored. How does the database decide..."
            Scene(
                13, "Three ways to measure", operators(),
                beats = listOf(
                    Beat(13, show = setOf("q")),
                    Beat(14, show = setOf("l2"), light = setOf("l2"), pulse = "q" to "l2", card = BoardCard("Operators", "Small symbols in the query. Euclidean: a ruler's straight line.")),
                    Beat(16, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Cosine", "Only the angle: which way two arrows point. Usual for text.")),
                    Beat(18, show = setOf("ip"), light = setOf("ip"), pulse = "q" to "ip", card = BoardCard("Negative inner product", "Multiply and add; the cheapest. For all three, smaller is closer.")),
                    Beat(20, light = setOf("cos"), card = searchCode),
                ),
            ),
            // 21: "Which sounds fine, until the table is big."
            Scene(
                21, "An index, not every row", indexes(),
                beats = listOf(
                    Beat(22, show = setOf("scan"), light = setOf("scan"), card = BoardCard("Every row", "400,000 notes read in full: 900 ms a question. An index jumps near.")),
                    Beat(24, light = setOf("scan"), card = BoardCard("Recall", "The share of the true closest notes found: 0.93 is 93 of 100.")),
                    Beat(26, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("HNSW", "A web of links in layers: best speed and recall; fine on an empty table.")),
                    Beat(30, show = setOf("ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("IVFFlat", "Shelves by subject, learned from the data: build after loading.")),
                    Beat(32, show = setOf("efs"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("ef search", "HNSW's shortlist, 40 by default. Higher: more recall, a bit slower.")),
                    Beat(34, show = setOf("probes"), light = setOf("probes"), pulse = "ivf" to "probes", card = BoardCard("probes", "How many IVFFlat shelves a search checks, one by default.")),
                ),
            ),
            // 35: "And if my table grows to many millions of notes?"
            Scene(
                35, "One index, one operator", match(),
                beats = listOf(
                    Beat(36, show = setOf("idx", "qry"), light = setOf("idx", "qry"), pulse = "idx" to "qry", card = BoardCard("The trap", "Cosine index, ruler query: the index is quietly ignored.")),
                    Beat(38, show = setOf("seq", "iscan"), light = setOf("seq"), pulse = "qry" to "seq", card = BoardCard("EXPLAIN", "Shows the plan. Seq Scan: every row. Index Scan: the index is used.")),
                ),
            ),
            // 39: "One more worry. Could my search find someone else's notes?"
            Scene(
                39, "Only this learner's notes", filter(),
                beats = listOf(
                    Beat(39, show = setOf("user", "api", "pg"), pulse = "user" to "api"),
                    Beat(40, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Filter in WHERE", "On user id and course, so only this learner's notes compete.")),
                    Beat(42, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "Retrieve the closest notes; a chat model answers only from them.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("ext", "seq", "hnsw", "ivf"), card = BoardCard("Listen for the phrase", "Refused install, Seq Scan, empty table, faster build: each has one answer.")),
                    Beat(46, light = setOf("recall", "user"), card = BoardCard("Low recall, others' notes", "Raise ef search or probes. Filter on the user id in WHERE.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-pgvector--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Borrowed Notes..."
            Scene(
                0, "The Borrowed Notes",
                DiagramSpec(
                    "Flashcard Fox's answer quotes notes the student never wrote: the search reached someone else's rows.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Flashcard Fox"),
                        DNode("api", "Python server", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "on Azure"),
                        DNode("bad", "Someone's notes", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "in her answer"),
                        DNode("pg", "PostgreSQL", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "question"),
                        DEdge("api", "pg", "search"),
                        DEdge("pg", "bad", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "bad"), light = setOf("bad"), pulse = "phone" to "api", card = BoardCard("The case", "A student's answer quotes notes she never wrote.")),
                    Beat(2, show = setOf("pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Embedding", "Numbers that capture meaning; close numbers, similar meaning.")),
                ),
            ),
            // 3: "And where do those lists live?"
            Scene(
                3, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(4, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(6, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An extension, an add-on, that stores and compares embeddings.")),
                    Beat(8, light = setOf("table"), card = BoardCard("Under the picture", "Code inside the server: a vector column packs the numbers side by side.")),
                    Beat(10, show = setOf("srv", "allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Suspect one: cleared", "Fixed weeks ago: VECTOR in azure.extensions, and the name is vector.")),
                ),
            ),
            // 11: "Suspect two. The distance..."
            Scene(
                11, "The filter in the wrong place", filter(),
                beats = listOf(
                    Beat(12, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("The clue", "Top five from every student; the Python filter was skipped.")),
                    Beat(14, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Metadata filter", "In WHERE, on user id and course: only her notes compete.")),
                ),
            ),
            // 15: "Faster. Good, because the second complaint..."
            Scene(
                15, "The second witness", match(),
                beats = listOf(
                    Beat(16, show = setOf("idx"), light = setOf("idx"), card = BoardCard("900 ms a question", "400,000 notes. The index should let it jump near the answer.")),
                    Beat(18, card = BoardCard("Suspect: VACUUM", "It tidies old deleted rows. Not the cause.")),
                    Beat(20, show = setOf("qry", "seq"), light = setOf("seq"), pulse = "qry" to "seq", card = BoardCard("Seq Scan", "EXPLAIN shows every row read: the index is ignored.")),
                ),
            ),
            // 21: "Why would it ignore a perfectly good index?"
            Scene(
                21, "Three operators", operators(),
                beats = listOf(
                    Beat(22, show = setOf("q", "l2"), light = setOf("l2"), pulse = "q" to "l2", card = BoardCard("Operators", "A small symbol says how to measure. Euclidean: the ruler.")),
                    Beat(24, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Cosine", "Only the angle: which way two arrows point. Usual for text.")),
                    Beat(26, show = setOf("ip"), light = setOf("ip"), pulse = "q" to "ip", card = BoardCard("Negative inner product", "Multiply and add; the cheapest. Smaller is closer for all three.")),
                    Beat(28, light = setOf("cos", "l2"), card = BoardCard("The mismatch", "Cosine index, ruler query. Switch to cosine: 900 ms fell to 15.")),
                ),
            ),
            // 29: "Which index was it, by the way?"
            Scene(
                29, "How HNSW searches", layers(),
                beats = listOf(
                    Beat(30, light = setOf("top", "mid", "low"), card = BoardCard("HNSW", "Hierarchical navigable small world: a web of links in layers.")),
                    Beat(32, light = setOf("short"), card = BoardCard("Recall", "Approximate, so it can miss. 0.93: 93 of every 100 right notes.")),
                    Beat(34, light = setOf("q", "top"), pulse = "q" to "top", card = BoardCard("Not magic", "Start on top, step closer, drop a layer; keep a shortlist.")),
                    Beat(36, light = setOf("short"), pulse = "low" to "short", card = BoardCard("ef search 80", "A longer shortlist: recall rose from 0.93 to 0.98.")),
                ),
            ),
            // 37: "Easy. Raise probes."
            Scene(
                37, "The empty table", indexes(),
                beats = listOf(
                    Beat(38, show = setOf("ivf", "probes"), light = setOf("ivf"), pulse = "ivf" to "probes", card = BoardCard("Shelves for nothing", "IVFFlat learns its shelves from data. The table was empty.")),
                    Beat(40, show = setOf("hnsw", "efs"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("Rebuilt with HNSW", "No training, fine on an empty table. IVFFlat: faster build, data first.")),
                ),
            ),
            // 41: "Any loose ends in the file?"
            Scene(
                41, "Loose ends", limits("Cosmos DB episode", "same idea"),
                beats = listOf(
                    Beat(42, light = setOf("miss", "disk", "mem"), card = BoardCard("Where it stops", "Approximate can miss. DiskANN for many millions. Fit in memory.")),
                ),
            ),
            // 43: "And once the right notes come back?"
            Scene(
                43, "Then comes RAG", filter(),
                beats = listOf(
                    Beat(44, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "The five notes go to a chat model, which answers only from them.")),
                ),
            ),
            // 45: "So, the case summary for the exam?"
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("user", "seq"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: filter, operator, index, dial.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-pgvector--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "What the Coach team built", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q", "note"), card = BoardCard("Search by meaning", "The Coach finds a learner's notes by meaning, not by words.")),
                    Beat(2, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "A list of numbers that captures meaning: like pins close on a map.")),
                ),
            ),
            // 3: "And they kept those lists where?"
            Scene(
                3, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(4, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(6, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An extension, an add-on, that stores and compares embeddings.")),
                    Beat(8, light = setOf("table"), card = BoardCard("Under the picture", "Code inside the server: a vector column packs the numbers side by side.")),
                ),
            ),
            // 9: "What did Ben do?"
            Scene(
                9, "Monday: Ben", setup(),
                beats = listOf(
                    Beat(10, show = setOf("ext"), light = setOf("ext"), card = BoardCard("CREATE EXTENSION pgvector", "Refused. Twice.")),
                    Beat(12, show = setOf("srv", "allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Two reasons", "VECTOR goes in azure.extensions first, and the add-on is named vector.")),
                ),
            ),
            // 13: "Poor Ben. Tuesday?"
            Scene(
                13, "Tuesday: Aisha", operators(),
                beats = listOf(
                    Beat(14, show = setOf("q"), card = BoardCard("Slow searches", "Nearly a second each: every note compared. Aisha built an index.")),
                    Beat(16, show = setOf("l2"), light = setOf("l2"), pulse = "q" to "l2", card = BoardCard("Operators", "Small symbols in the query. Euclidean: a ruler's straight line.")),
                    Beat(18, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Cosine", "Only the angle: which way two arrows point. Usual for text.")),
                    Beat(20, show = setOf("ip"), light = setOf("cos", "l2"), pulse = "q" to "ip", card = BoardCard("The mismatch", "Index built for cosine; the query sorted by the ruler distance.")),
                ),
            ),
            // 21: "And an index only helps its own operator?"
            Scene(
                21, "Aisha's EXPLAIN", match(),
                beats = listOf(
                    Beat(21, show = setOf("idx", "qry"), light = setOf("idx", "qry"), pulse = "idx" to "qry"),
                    Beat(22, show = setOf("seq", "iscan"), light = setOf("iscan"), pulse = "idx" to "iscan", card = BoardCard("EXPLAIN", "Seq Scan: ignored. With the cosine operator: Index Scan, 15 ms.")),
                ),
            ),
            // 23: "Wednesday. Let me guess. The shelves."
            Scene(
                23, "Wednesday: Marcus", indexes(),
                beats = listOf(
                    Beat(24, show = setOf("ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("IVFFlat", "Shelves by subject; a search checks only the nearest shelves.")),
                    Beat(26, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("Shelves for nothing", "Shelves learned from an empty table. Build after loading, or HNSW.")),
                ),
            ),
            // 27: "HNSW. Is that a person or a thing?"
            Scene(
                27, "HNSW, then Priya", layers(),
                beats = listOf(
                    Beat(28, light = setOf("top", "mid"), pulse = "top" to "mid", card = BoardCard("HNSW", "A web of links in layers: start on top, step closer, drop a layer.")),
                    Beat(30, light = setOf("low"), card = BoardCard("Recall", "Approximate, so it can miss. Priya measured 0.93: 93 of 100.")),
                    Beat(32, light = setOf("short"), pulse = "low" to "short", card = BoardCard("ef search 40 to 80", "A longer shortlist: recall 0.98, a little slower.")),
                ),
            ),
            // 33: "So Priya did nothing wrong? I'm disappointed."
            Scene(
                33, "Friday: the intern", filter(),
                beats = listOf(
                    Beat(34, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Top five from everyone", "Filtered later in Python, and one code path forgot.")),
                    Beat(36, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Metadata filter", "In WHERE, on user id and course: only this learner's notes compete.")),
                    Beat(38, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "The notes go to a chat model, which answers only from them.")),
                ),
            ),
            // 39: "Did anything still not work?"
            Scene(
                39, "Where it stops", limits("Redis episode", "reuse answers"),
                beats = listOf(
                    Beat(40, light = setOf("miss", "disk", "link"), card = BoardCard("Where pgvector stops", "It can miss; DiskANN for huge tables. Redis reuses answers.")),
                ),
            ),
            // 41: "So, the gossip I should remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("ext", "hnsw", "ivf"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-pgvector--late-night",
        listOf(
            // 0: "Hey, you. It's late, the kettle's cooling..."
            Scene(
                0, "Finding things by meaning", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q"), card = BoardCard("Tonight", "Teaching an old database a new trick: search by meaning.")),
                    Beat(2, show = setOf("note"), light = setOf("q", "note"), card = BoardCard("Different words", "The answer is in the notes, so a word search finds nothing.")),
                    Beat(4, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "A list of numbers that captures meaning: like pins close on a map.")),
                ),
            ),
            // 5: "Pins on a map, in the dark. And where do the pins live?"
            Scene(
                5, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(6, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(8, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An add-on: a vector column, and ways to measure distance.")),
                    Beat(10, light = setOf("table"), card = BoardCard("Under the picture", "Code inside the server: a vector column packs the numbers side by side.")),
                    Beat(12, show = setOf("srv", "allow"), light = setOf("allow", "ext"), pulse = "srv" to "allow", card = BoardCard("Two gentle steps", "VECTOR in azure.extensions, then CREATE EXTENSION vector.")),
                ),
            ),
            // 13: "Don't we all. So how does it measure closeness?"
            Scene(
                13, "How close is close", operators(),
                beats = listOf(
                    Beat(14, show = setOf("q", "l2"), light = setOf("l2"), pulse = "q" to "l2", card = BoardCard("Operators", "Small symbols in the query. Euclidean: a ruler across the map.")),
                    Beat(16, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Cosine", "Which way two arrows face. The usual one for text.")),
                    Beat(18, show = setOf("ip"), light = setOf("ip"), pulse = "q" to "ip", card = searchCode),
                ),
            ),
            // 19: "Sort every note, every time? That sounds like a long night."
            Scene(
                19, "Two indexes", indexes(),
                beats = listOf(
                    Beat(20, show = setOf("scan"), light = setOf("scan"), card = BoardCard("Almost a second", "400,000 notes read every time. An index jumps near the answer.")),
                    Beat(22, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("HNSW", "Notes holding hands in layers. No training: fine on an empty table.")),
                ),
            ),
            // 23: "How does it actually find its way, in that web?"
            Scene(
                23, "Finding its way", layers(),
                beats = listOf(
                    Beat(24, light = setOf("top", "mid", "low"), pulse = "q" to "top", card = BoardCard("Motorway, main road, street", "Step toward the question, drop a layer; keep a shortlist: ef search.")),
                ),
            ),
            // 25: "And the other index?"
            Scene(
                25, "Shelves and recall", indexes(),
                beats = listOf(
                    Beat(26, show = setOf("ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("IVFFlat", "Shelves by subject, learned from data: built after loading.")),
                    Beat(28, show = setOf("efs"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("Recall", "0.93 is 93 of 100. ef search at 80 took the Coach to 0.98.")),
                    Beat(30, show = setOf("probes"), light = setOf("probes"), pulse = "ivf" to "probes", card = BoardCard("probes", "How many shelves a search checks, one by default.")),
                ),
            ),
            // 31: "And now the trap. I can feel one coming."
            Scene(
                31, "The trap", match(),
                beats = listOf(
                    Beat(32, light = setOf("idx", "qry", "seq"), pulse = "idx" to "qry", card = BoardCard("One index, one operator", "Cosine index, ruler query: Seq Scan. EXPLAIN shows the plan.")),
                ),
            ),
            // 33: "Mm. And whose notes come back? Only mine, I hope."
            Scene(
                33, "Only your notes", filter(),
                beats = listOf(
                    Beat(33, show = setOf("user", "api", "pg"), pulse = "user" to "api"),
                    Beat(34, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Filter in WHERE", "On your user id and the course, never afterwards in Python.")),
                    Beat(36, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "Retrieval augmented generation: answers only from your notes.")),
                ),
            ),
            // 37: "Now tell me where it all stops working."
            Scene(
                37, "Where it stops", limits("Cosmos DB episode", "same idea"),
                beats = listOf(
                    Beat(38, light = setOf("miss", "disk", "mem"), card = BoardCard("Where it stops", "It can miss; DiskANN at many millions; keep the index in memory.")),
                ),
            ),
            // 39: "Almost. Before I go, what do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("ext", "seq", "recall"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: install, operator, index, dial.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-pgvector--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q"), card = BoardCard("Date night", "How a database finds your perfect match: pgvector.")),
                    Beat(2, show = setOf("note"), light = setOf("q", "note"), card = BoardCard("Different words", "Same meaning, completely different words.")),
                    Beat(4, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "A list of numbers that captures meaning: like pins close on a map.")),
                ),
            ),
            // 6: "In PostgreSQL, a popular free database..."
            Scene(
                6, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(6, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(8, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An add-on inside the server: a vector column, side by side numbers.")),
                    Beat(9, show = setOf("srv"), light = setOf("srv", "ext"), card = BoardCard("Quiz: a refused install", "CREATE EXTENSION vector on a brand-new server is refused. Why?")),
                    Beat(10, show = setOf("allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Right answer", "VECTOR goes in azure.extensions first; the add-on is named vector.")),
                ),
            ),
            // 12: "Mm. Now, measuring closeness..."
            Scene(
                12, "Measuring closeness", operators(),
                beats = listOf(
                    Beat(12, show = setOf("q", "l2"), light = setOf("l2"), pulse = "q" to "l2", card = BoardCard("Operators", "Small symbols in the query. Euclidean: a ruler across the map.")),
                    Beat(14, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = BoardCard("Cosine", "Which way two arrows face. The usual choice for text.")),
                    Beat(16, show = setOf("ip"), light = setOf("ip"), pulse = "q" to "ip", card = searchCode),
                ),
            ),
            // 17: "Sorting every note in the table every time sounds exhausting."
            Scene(
                17, "The ignored index", match(),
                beats = listOf(
                    Beat(18, show = setOf("idx", "qry", "seq"), light = setOf("idx", "qry"), pulse = "idx" to "qry", card = BoardCard("Quiz: a Seq Scan", "Cosine index, ruler query, every row read. What went wrong?")),
                    Beat(19, show = setOf("iscan"), light = setOf("iscan"), pulse = "idx" to "iscan", card = BoardCard("Right answer", "One index, one operator. EXPLAIN shows the plan: want Index Scan.")),
                ),
            ),
            // 21: "My turn to teach. Two indexes..."
            Scene(
                21, "Two indexes", indexes(),
                beats = listOf(
                    Beat(21, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("HNSW", "Notes holding hands in layers: motorway, main road, your street.")),
                    Beat(23, show = setOf("ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("Quiz: an empty table", "Three million flashcards, a table filled overnight. Which index?")),
                    Beat(25, light = setOf("hnsw"), card = BoardCard("No kiss: it's HNSW", "IVFFlat learns its shelves from nothing. HNSW needs no training.")),
                    Beat(27, card = BoardCard("Recall", "Approximate, so it can miss. 0.93 is 93 of every 100.")),
                    Beat(28, show = setOf("efs"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("Quiz: low recall", "HNSW recall too low, time to spare. What first?")),
                    Beat(29, show = setOf("probes"), light = setOf("efs", "probes"), pulse = "ivf" to "probes", card = BoardCard("Right answer", "Raise ef search, the shortlist. For IVFFlat, probes.")),
                ),
            ),
            // 31: "Last quiz of the evening..."
            Scene(
                31, "What's yours stays yours", filter(),
                beats = listOf(
                    Beat(31, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Quiz: someone else's notes", "The answer quoted another learner. What did the query lack?")),
                    Beat(32, show = setOf("where", "chat"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Right answer", "A filter in WHERE on user id and course. Then RAG answers from them.")),
                ),
            ),
            // 34: "And where does it all stop working, Rafe?..."
            Scene(
                34, "Every romance has limits", limits("Cosmos DB episode", "same idea"),
                beats = listOf(
                    Beat(35, light = setOf("miss", "disk", "mem"), card = BoardCard("Where it stops", "It can miss; DiskANN past many millions; keep the index in memory.")),
                ),
            ),
            // 36: "Then the last question, for the exam. What do we remember?"
            Scene(
                36, "For the exam", exam(),
                beats = listOf(
                    Beat(37, show = all6, light = setOf("ext", "seq", "hnsw", "recall", "user"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
