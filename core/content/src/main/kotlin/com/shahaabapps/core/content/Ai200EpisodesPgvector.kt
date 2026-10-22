package com.shahaabapps.core.content

/** pgvector on PostgreSQL: vector search and RAG: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesPgvector {
    private val classic = Podcast(
        "ai-pgvector",
        listOf(
            maya("Hi, and welcome back. Today is about searching by meaning. Say you're building the Two Tracks Coach, a study helper in an Android app, with a Python server on Azure, Microsoft's cloud. A learner asks, what is a dead-letter queue? The answer is in their notes, but in different words."),
            leo("So a search for the words finds nothing. [pause] How do we find it?"),
            maya("We compare meanings, and for that we turn text into numbers. Start with a map. Three steps east, four steps north: you can write that spot as two numbers, three and four. A list of numbers like that is called a vector. It's just a spot on the map."),
            leo("A spot on a map. And what has that got to do with meaning?"),
            maya("An AI model reads a piece of text and writes a list of numbers for it, so texts that mean similar things get nearby spots. That list is called an embedding. The question and the note that answers it land close together, even in different words."),
            leo("Is it really just two numbers, east and north?"),
            maya("No. How many numbers are in the list is called the dimension. A map spot has two. The usual model, text-embedding-3-small, writes 1,536 for every text. Nobody can picture that, but the maths works the same."),
            leo("Okay. So where do these lists live?"),
            maya("In PostgreSQL, a popular free database that keeps data in tables of rows and columns, like a very strict spreadsheet. The Coach already keeps its notes there, so each note's embedding can sit beside it."),
            leo("Can PostgreSQL even hold a list of numbers in one column?"),
            maya("Not on its own. It needs an extension, an add-on, like a plug-in for your browser. This one is called pgvector. It adds a new kind of column, called vector, and ways to measure how far apart two lists are."),
            leo("And on Azure, I just tick a box?"),
            maya("Two steps. Azure's PostgreSQL keeps a guest list of allowed add-ons, a setting called azure.extensions, empty on a new server. Step one: put VECTOR on it."),
            leo("And step two? I'd type CREATE EXTENSION pgvector."),
            maya("Close, but no: CREATE EXTENSION vector, in each database. The add-on's real name is just vector."),
            leo("[chuckle] Famous, but not under its own name. Then a table?"),
            maya("A notes table with a vector column of 1,536 numbers, to match the model. So which notes are close to the question? pgvector gives three ways to measure. Each one is a small symbol you put in the query, called an operator."),
            leo("Three ways. One at a time, please."),
            maya("The first is Euclidean distance: a ruler laid between two pins on the map. From the middle of the map to the spot three east, four north, the ruler says five. Smaller means closer."),
            leo("That's the one I'd have guessed. The second?"),
            maya("Cosine. Picture an arrow from the middle of the map to each spot. Cosine asks only: do the two arrows point the same way, whatever their length? Same way scores 1, a right angle 0, opposite ways minus 1."),
            leo("Give me an example."),
            maya("Three east, four north, against six east, eight north. The second arrow is twice as long, but it points the same way, so cosine says 1. pgvector turns that into a distance, one minus the score, so the same way gives 0. Smaller means closer again. It's the usual choice for text."),
            leo("And the third?"),
            maya("Inner product, also called the dot product. Multiply the matching numbers and add them up. Two and three, against four and one: two times four is eight, three times one is three, and eight plus three is eleven. Bigger means the lists agree more."),
            leo("Bigger is better? But you said smaller means closer."),
            maya("Well spotted. So pgvector's operator is the negative inner product: minus eleven, and smaller means closer again."),
            leo("Why bother with it, if cosine exists?"),
            maya("With the usual models every arrow has the same length, so it ranks notes just like cosine, only cheaper to work out."),
            leo("So the search is: sort by distance, closest first, and keep a few?"),
            maya("That's it. In SQL, the language you talk to the database in, you write ORDER BY the distance to the question, then LIMIT 5. The catch: with no help, it measures the question against every row."),
            leo("Which sounds fine, until the table is big."),
            maya("For example, the Coach has 400,000 notes, and each question took nearly a second. The fix is an index. Like the index at the back of a book, it lets the database jump near the answer instead of reading every page."),
            leo("Does it still find the very closest notes?"),
            maya("Nearly always. These indexes are approximate: very close, but not guaranteed perfect. How much they find is called recall. A recall of 0.93 means 93 of every 100 notes that reading every row would have found."),
            leo("And which index do I pick?"),
            maya("Two you must know. The first is HNSW, short for hierarchical navigable small world. It's a graph: dots joined by lines, like a map of who knows whom."),
            leo("And what is each dot?"),
            maya("A node: one stored note, joined to a few of its nearest notes. A search hops along the lines, like asking friends of friends instead of knocking on every door."),
            leo("Hopping sounds slow, with 400,000 notes."),
            maya("So it has layers. The top layer has a few notes far apart, like motorways; lower layers are denser, like streets. Start on the motorway, then drop to the streets."),
            leo("What's it good at?"),
            maya("The best balance of speed and recall. It's slower to build and uses more memory. And it needs no training, so you can create it on an empty table and fill it later."),
            leo("Training? Indexes go to the gym now?"),
            maya("[laugh] One of them does. IVFFlat sorts the vectors into groups, like a library putting books on shelves by subject. A search checks only the shelves nearest the question. It builds faster and uses less memory. But it learns its shelves from the data already there, so you build it after loading."),
            leo("So IVFFlat on an empty table is a library with shelves for nothing."),
            maya("Perfect. Each index has a dial for recall. For HNSW it's ef search: how many candidates it keeps on its shortlist while it hops, 40 by default. Turn it up, and you find more true neighbours, a little slower."),
            leo("And the dial on IVFFlat?"),
            maya("It's called probes: how many shelves a search checks, one by default. More shelves, better recall, a little slower."),
            leo("And if my table grows to many millions of notes?"),
            maya("Then name DiskANN, an add-on only Azure offers, for very large vector tables. Now the trap the exam loves."),
            leo("I'm listening. What's the trap?"),
            maya("Each index is built for one operator. Build a cosine index, then sort by the ruler distance, and the database quietly ignores the index and reads every row."),
            leo("How would I even notice?"),
            maya("Put EXPLAIN in front of the query. It shows the database's plan. Index Scan means your index is used. Seq Scan, a sequential scan, means it's reading every row."),
            leo("One more worry. Could my search find someone else's notes?"),
            maya("Unless you stop it. Filter in the WHERE part of the query, on the user id and the course, so only this learner's notes compete. Don't fetch everyone's top five and filter afterwards in Python."),
            leo("And then the Coach answers from those five notes?"),
            maya("Yes, and that has a name: RAG, which stands for retrieval augmented generation. Retrieve the closest notes, hand them to a chat model with the question, and ask it to answer only from them."),
            leo("So what do I remember for the exam?"),
            maya("If CREATE EXTENSION is refused, put VECTOR in azure.extensions, and call it vector. If the index is ignored, match the query's operator to the index. Best recall, or an empty table: HNSW. Faster build with the data loaded first: IVFFlat. Low recall: raise ef search, or probes. Another learner's notes: filter on the user id in WHERE."),
            leo("Great. Anything to try today?"),
            maya("Allow VECTOR in azure.extensions and run CREATE EXTENSION vector. Create a table with a vector column of just three numbers, and insert five rows. Order by each of the three operators and compare the top three rows. Then build an HNSW cosine index and confirm the index scan with EXPLAIN."),
            leo("A database that reads between the lines. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-pgvector",
        listOf(
            maya("Welcome to Case Files. Today's case: The Borrowed Notes. A small Android app called Flashcard Fox helps students revise, with a Python server on Azure, Microsoft's cloud. A student asks, what is a dead-letter queue? And the answer quotes notes she never wrote. Someone else's notes."),
            leo("[pause] Detective Leo is on the case. But first, how was it meant to find her notes, in plain words?"),
            maya("By meaning. An AI model turns each note into an embedding: a list of numbers it writes for the text, so notes that mean similar things get nearby spots, like pins on a map. The question gets a list too, and the app looks for the closest notes."),
            leo("A list of numbers is a spot on a map? Show me."),
            maya("On a paper map, three steps east and four north is the spot three, four. That list of two numbers is a vector. An embedding is the same idea with 1,536 numbers instead of two. Nobody can picture that, but the maths works the same."),
            leo("And where do those lists live?"),
            maya("In PostgreSQL, a popular free database of tables, rows and columns, like a very strict spreadsheet. The team added pgvector, an extension, which is an add-on, like a plug-in for your browser, so PostgreSQL can store those lists and compare them."),
            leo("Is it really just a plug-in? Under the picture, I mean."),
            maya("Under the picture, it's code loaded into the database server itself, adding a column type called vector that packs the numbers side by side."),
            leo("Then my first suspect: they installed it wrong. Somebody typed CREATE EXTENSION pgvector."),
            maya("That did happen, on day one. The add-on's real name is just vector."),
            leo("So they typed vector, and it worked?"),
            maya("Not yet. Azure's PostgreSQL keeps a guest list of allowed add-ons, a setting called azure.extensions, empty at the start, so VECTOR had to go on it first. But that was fixed weeks ago. Wrong suspect."),
            leo("Suspect two. They measured closeness the wrong way, so it picks the wrong notes."),
            maya("How you measure changes the order of the notes, not who they belong to. The clue is in the code. The server asked for the five closest notes from the whole table, every student's. The plan was to filter by student afterwards, in Python, and the new feature skipped that step."),
            leo("So the filter lived in the wrong place."),
            maya("Exactly. It belongs in the query itself, in its WHERE part, on the user id and the course."),
            leo("Isn't filtering in Python just as good, if nobody forgets?"),
            maya("Even then, it's worse. Fetch everyone's top five, and maybe none are hers. A metadata filter in WHERE, on facts about each note like whose it is, puts only her notes in the race. And fewer rows competing is faster."),
            leo("Faster. Good, because the second complaint says every question takes almost a second."),
            maya("Second witness. Flashcard Fox has 400,000 notes, and with no help the database measures the question against every single one. So the team had built an index. Like the index at the back of a book, it lets the database jump near the answer instead of reading every page."),
            leo("So the index is broken. Suspect: the table needs a clean-up. VACUUM, or whatever it's called."),
            maya("[chuckle] VACUUM tidies away old deleted rows. Not the cause. The clue: they put EXPLAIN in front of the query, which shows the database's plan. It said Seq Scan, a sequential scan: reading every row, one after another. The index was being ignored."),
            leo("Why would it ignore a perfectly good index?"),
            maya("Because each index is built for one operator, a small symbol in the query that says how to measure. pgvector has three."),
            leo("Start with the one I'd guess."),
            maya("Euclidean distance: a ruler laid between two pins on the map. From the middle of the map to the spot three, four, the ruler says five. Smaller is closer."),
            leo("And the second?"),
            maya("Cosine. Picture an arrow from the middle of the map to each pin. Cosine only asks whether two arrows point the same way, whatever their length: same way 1, right angle 0, opposite ways minus 1."),
            leo("Give me an example."),
            maya("Three, four against six, eight. The second arrow is twice as long, but it points the same way, so cosine says 1. pgvector makes that a distance, one minus the score, so 0, and smaller is closer. It's the usual choice for text."),
            leo("And the third?"),
            maya("Inner product, also called the dot product: multiply the matching numbers and add them up. Two, three against four, one: two times four plus three times one is eleven. Bigger means more alike, so pgvector uses minus eleven, and smaller is closer for all three."),
            leo("And the team mixed them up."),
            maya("They built the index for cosine, then sorted the query by the ruler. An index only serves its own operator. They switched the query to cosine, EXPLAIN said Index Scan, and each question fell from 900 milliseconds to 15."),
            leo("Which index was it, by the way?"),
            maya("HNSW, short for hierarchical navigable small world. It's a graph: dots joined by lines, like a map of who knows whom."),
            leo("And what are the dots?"),
            maya("Each dot is a node: one stored note, joined to a few of its nearest notes. A search hops from dot to closer dot, like asking friends of friends instead of knocking on every door."),
            leo("It can't be that simple. There must be a trick."),
            maya("Layers. The top layer has a few notes far apart, like motorways; lower layers are denser, like streets. A search starts on the motorway and drops down. On the way it keeps a shortlist of candidates, and its size is called ef search, 40 by default."),
            leo("Does it always find the very closest notes?"),
            maya("Not always. It's approximate: very close, not guaranteed perfect. How much it finds is called recall. Theirs was 0.93: 93 of every 100 right notes. They set ef search to 80, a longer shortlist, and recall rose to 0.98, a little slower."),
            leo("So, case closed?"),
            maya("Not yet. A new table, three million flashcards, created empty and filled during the night. Recall the next morning was dreadful."),
            leo("Easy. Raise probes."),
            maya("[pause] Probes is the dial of the other index, IVFFlat, and that's what they'd built."),
            leo("Another index. What does that one do?"),
            maya("It sorts vectors into groups, like a library putting books on shelves by subject, and searches only the nearest shelves; probes says how many. But it learns its shelves from the data already there, and the table was empty."),
            leo("Shelves for nothing. So, build it after loading?"),
            maya("Or use HNSW, which needs no training and can be built on an empty table. They rebuilt with HNSW. IVFFlat is the pick for a faster build and less memory, with the data already loaded."),
            leo("Any loose ends in the file?"),
            maya("Where it stops working. Approximate means it can miss. At many millions of rows, Azure offers DiskANN, an add-on for very large vector tables. The index should fit in the server's memory. And the Cosmos DB vector search episode shows the same idea in another database."),
            leo("And once the right notes come back?"),
            maya("That's RAG, which stands for retrieval augmented generation. The five notes go to a chat model with the question, and it answers only from them. So the answer is built on her own notes. Truly her own, now."),
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
            maya("A search that finds a learner's notes by meaning, not by words. An AI model turns each note into an embedding: a list of numbers it writes for the text, so notes that mean similar things get nearby spots, like pins on a map."),
            leo("Numbers as spots on a map? Explain that like I'm new."),
            maya("On a paper map, three steps east and four north is the spot three, four. A list of numbers like that is called a vector. An embedding is a vector with 1,536 numbers instead of two. Nobody can picture that many directions, but the maths works the same."),
            leo("And they kept those lists where?"),
            maya("In PostgreSQL, the database the Coach already uses. It's popular and free, and keeps data in tables of rows and columns, like a very strict spreadsheet."),
            leo("A strict spreadsheet that understands meaning? Since when?"),
            maya("Since pgvector. It's an extension, an add-on, like a plug-in for your browser. Under the picture, it's code loaded into the database server, adding a column type called vector that packs the numbers side by side. Okay. Monday. Ben."),
            leo("What did Ben do?"),
            maya("Ben opened the database and typed CREATE EXTENSION pgvector, the command that installs an add-on. Refused. He typed it again, louder, somehow. Refused."),
            leo("[pause] Why?"),
            maya("Two reasons. Azure's PostgreSQL service keeps a guest list of allowed add-ons, a setting called azure.extensions, and on a new server it's empty."),
            leo("So Ben's add-on wasn't even on the list."),
            maya("Not even close. VECTOR goes on the list first. And its real name is just vector, so it's CREATE EXTENSION vector, in every database that needs it."),
            leo("Poor Ben. Tuesday?"),
            maya("Tuesday was Aisha. Searches took nearly a second each, because the database measured the question against every single note. So Aisha built an index. Like the index at the back of a book, it lets the database jump near the answer instead of reading every page."),
            leo("That sounds like a win."),
            maya("It would have been. But pgvector has three ways to measure closeness, each its own operator, a small symbol in the query."),
            leo("Three? Give me the obvious one first."),
            maya("Euclidean distance: a ruler laid between two pins on the map. From the middle of the map to the spot three, four, the ruler says five."),
            leo("Sensible. And the second?"),
            maya("Cosine. Picture an arrow from the middle of the map to each pin. Cosine asks only whether two arrows point the same way, whatever their length: same way 1, right angle 0, opposite ways minus 1."),
            leo("An example, for the people at the back."),
            maya("Three, four against six, eight. Twice as long, same way, so cosine says 1. pgvector turns it into a distance, one minus that, so 0. The usual one for text."),
            leo("You said three."),
            maya("The third is inner product, also called the dot product: multiply the matching numbers and add them up. Two, three against four, one: two times four plus three times one, eleven."),
            leo("So here, bigger means closer?"),
            maya("Bigger means more alike, so pgvector flips it to minus eleven. For all three, smaller means closer."),
            leo("And Aisha's mistake?"),
            maya("She built her index for cosine, but the query still sorted by the ruler. An index only helps its own operator, so the database ignored it completely."),
            leo("How did she even find out?"),
            maya("She put EXPLAIN in front of the query, which shows the database's plan. It said Seq Scan, a sequential scan, reading every row. She switched to the cosine operator, and it said Index Scan. 900 milliseconds became 15."),
            leo("Wednesday. Let me guess. The shelves."),
            maya("Wednesday was Marcus. A new table, three million flashcards, created empty, to be filled overnight. Marcus built an IVFFlat index on it straight away. IVFFlat sorts vectors into groups, like a library putting books on shelves by subject, and searches only the nearest shelves."),
            leo("So where did it go wrong?"),
            maya("It works out its shelves from the data already in the table. And the table was empty. Shelves for nothing, hence the whiteboard. Build IVFFlat after loading, or use the other index, which needs no training at all."),
            leo("The other index. Is that a person or a thing?"),
            maya("[chuckle] A thing, called HNSW, short for hierarchical navigable small world. It's a graph: dots joined by lines, like a map of who knows whom."),
            leo("And the dots are the notes?"),
            maya("Yes. Each dot is a node, one stored note, joined to a few of its nearest notes. A search hops from dot to closer dot, like asking friends of friends instead of knocking on every door."),
            leo("Doesn't all that hopping take forever?"),
            maya("Layers. Top layers are sparse, like motorways; lower ones are streets. Best balance of speed and recall."),
            leo("Recall. That's the word everyone used in the Thursday meeting."),
            maya("Because Thursday was Priya. These indexes are approximate, which means very close, not guaranteed perfect. Recall is how many of the true closest notes a search actually finds. Priya measured 0.93, 93 of every 100."),
            leo("And Priya, being Priya, fixed it how?"),
            maya("HNSW keeps a shortlist of candidates while it hops. Its size is called ef search, 40 by default. She raised it to 80 and got 0.98, for a slightly slower search. On IVFFlat the dial is probes, how many shelves it checks."),
            leo("So Priya did nothing wrong? I'm disappointed."),
            maya("Priya never does. Friday did, though. The intern. Their search fetched the five closest notes from every learner, then filtered by learner afterwards, in Python."),
            leo("Risky, but fine if you never forget the filter."),
            maya("And one code path forgot. A learner's answer quoted somebody else's notes."),
            leo("Oof. What's the fix?"),
            maya("A metadata filter, a filter on facts about each note, like whose it is. It goes in the WHERE part of the query, on the user id and the course. Then only this learner's notes compete at all. It's safer, and faster, too."),
            leo("And after all that, what does the Coach do with the five notes?"),
            maya("That's RAG, which stands for retrieval augmented generation. The notes go to a chat model with the question, and it answers only from them. So every answer is built on the learner's own notes."),
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
            nova("By meaning. And for that, we need a map. Three steps east, four steps north: you can write that spot as two numbers, three and four. A list of numbers like that is called a vector. [pause] Just a spot on the map."),
            ivy("A spot on a map. And where does meaning come in?"),
            nova("An AI model reads each note and writes a list of numbers for it, so notes that mean similar things get nearby spots. That list is an embedding. Only, it isn't two numbers. It's 1,536. Nobody can picture that many directions, [pause] but the maths works just the same."),
            ivy("Pins on a map, in the dark. And where do the pins live?"),
            nova("Tonight, in PostgreSQL. A popular free database that keeps data in tables of rows and columns, like a very strict spreadsheet. The Two Tracks Coach, the study helper in the Android app, already keeps its notes there, behind a Python server on Azure, Microsoft's cloud."),
            ivy("And a strict spreadsheet just understands meaning now?"),
            nova("Not on its own. It needs an extension, an add-on, like a plug-in for your browser. This one's called pgvector. Under the picture, it's code loaded into the database server itself, adding a column type called vector that holds the numbers side by side."),
            ivy("And on Azure, I just switch it on?"),
            nova("Two gentle steps. Azure's PostgreSQL keeps a guest list of add-ons it allows, a setting called azure.extensions. On a new server it's empty, so VECTOR goes on the list."),
            ivy("And then I type CREATE EXTENSION pgvector?"),
            nova("[pause] Vector, not pgvector. CREATE EXTENSION vector, in each database. The add-on goes by a shorter name than its fame."),
            ivy("[chuckle] Don't we all. So how does it measure closeness?"),
            nova("Three ways, each a small symbol in the query, called an operator. The first is Euclidean distance: a ruler laid between two pins. From the middle of the map to the spot three, four, the ruler says five. Smaller means closer."),
            ivy("A ruler. So practical. And the second?"),
            nova("Cosine. [pause] Picture an arrow from the middle of the map to each pin. Cosine only asks: do the two arrows point the same way, whatever their length? Same way 1, a right angle 0, opposite ways minus 1."),
            ivy("Which way you're facing. I like that one. Show me."),
            nova("Three, four, and six, eight. One arrow is twice as long, but they face the same way, so cosine says 1. pgvector makes it a distance, one minus that, so 0. That's the usual one for text."),
            ivy("And the third?"),
            nova("Inner product, also called the dot product. Multiply the matching numbers, and add them up. Two, three against four, one: two times four is eight, three times one is three. Eleven. [pause] Bigger means the lists agree more."),
            ivy("Bigger is better? You said smaller means closer."),
            nova("Sharp, as ever. So pgvector uses the negative inner product, minus eleven, and smaller is closer again. It's the cheapest of the three."),
            ivy("And then? I ask, it measures, and?"),
            nova("It sorts the notes by distance, closest first, and keeps five. In SQL, the database's language, that's ORDER BY the distance, then LIMIT 5."),
            ivy("Sort every note, every time? That sounds like a long night."),
            nova("It is, without help. With 400,000 notes, it took almost a second a question. So you build an index. Like the index at the back of a book, it lets the database jump near the answer instead of turning every page."),
            ivy("Which index, Nova? There's always more than one."),
            nova("Two to know. HNSW, short for hierarchical navigable small world. It's a graph: dots joined by lines, like a map of who knows whom."),
            ivy("And the dots?"),
            nova("Each dot is a node, one stored note, holding hands with a few of its nearest notes. A search hops from dot to closer dot, like friends of friends, instead of knocking on every door."),
            ivy("Hop, hop, hop. Through every note?"),
            nova("No. Top layers are sparse, like motorways; lower ones are streets. [pause] It keeps a shortlist on the way, and its size is ef search, 40 by default."),
            ivy("And the other index?"),
            nova("IVFFlat. It sorts vectors into groups, like a library shelving books by subject, and checks only the nearest shelves. Faster to build, less memory."),
            ivy("There's a but. I can hear it."),
            nova("It learns its shelves from the data already there, so it's built after loading. HNSW needs no training, so it's fine on an empty table."),
            ivy("Hopping, shelves. Do they ever miss?"),
            nova("Sometimes. They're approximate, which means very close, not guaranteed perfect. How much they find is called recall. A recall of 0.93 means 93 of every 100 true closest notes. The Coach raised ef search to 80, and recall went to 0.98."),
            ivy("And the dial on IVFFlat?"),
            nova("It's called probes: how many shelves a search checks, one by default. [pause] More shelves, better recall, a little slower."),
            ivy("And now the trap. I can feel one coming."),
            nova("An index serves only its own operator. Build it for cosine, then sort by the ruler, and the database ignores your index and reads every row."),
            ivy("Silently? How would I ever know?"),
            nova("Put EXPLAIN in front of the query to see its plan. Seq Scan, a sequential scan, every row. Index Scan, you're fine."),
            ivy("Mm. And whose notes come back? Only mine, I hope."),
            nova("Only yours, if the query says so. A filter in its WHERE part, on your user id and the course. Never fetch the top five from everyone and filter later in Python. That's how a stranger's notes slip into your answer."),
            ivy("Then the Coach answers me from my own notes?"),
            nova("That's RAG, which stands for retrieval augmented generation. Your five notes go to a chat model with your question, and it answers only from them. [pause] Built on your notes, not its imagination."),
            ivy("Now tell me where it all stops working."),
            nova("Approximate can miss, so check your recall. At many millions of rows, Azure offers DiskANN, an add-on made for huge vector tables. And the index should fit in the server's memory. The Cosmos DB vector search episode does this same dance in another database, if you're not sleepy yet."),
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
            nova("Exactly. So the app compares meaning, on a map. Three steps east, four steps north: that spot is the list of numbers three, four. A list of numbers like that is called a vector. A spot on the map."),
            rafe("And meaning becomes a spot how?"),
            nova("An AI model writes a list of numbers for each note, so notes that mean similar things get nearby spots. That's an embedding. Not two numbers, though: 1,536. Nobody can picture that, but the maths works the same."),
            rafe("Close together on a map. I like where this is going. Where do the pins live?"),
            nova("In PostgreSQL, a popular free database that keeps data in tables of rows and columns, like a very strict spreadsheet."),
            rafe("Strict. And it understands meaning?"),
            nova("Not alone. It needs an extension, an add-on, like a plug-in for your browser. That's pgvector: code inside the database server, adding a column type called vector."),
            rafe("A strict spreadsheet with a plug-in. My type. Then let me test you, darling. Exam style. On a brand-new Azure server, you run CREATE EXTENSION vector, the command that installs the add-on, and it's refused. Why?"),
            nova("Because Azure's PostgreSQL service keeps a guest list of add-ons it allows, a setting called azure.extensions, and on a new server it's empty. Put VECTOR on the list first. And the add-on is named vector, never pgvector."),
            rafe("Right answer. You always know how to get on the list. [kiss]"),
            nova("Mm. Now, measuring closeness. pgvector has three small symbols for it, called operators. The first is Euclidean distance: a ruler laid between two pins. From the middle of the map to three, four, the ruler says five. Smaller is closer."),
            rafe("A ruler. How very romantic. And the second?"),
            nova("Cosine. Picture an arrow from the middle of the map to each pin. Do two arrows point the same way, whatever their length? Same way 1, right angle 0, opposite ways minus 1."),
            rafe("Which way you're facing, not how tall you are. Thank goodness. Example?"),
            nova("Three, four, and six, eight. Twice as long, same way: cosine says 1. pgvector makes it a distance, one minus that, so 0. The usual choice for text."),
            rafe("My turn to teach the third. Inner product, also called the dot product. Multiply the matching numbers and add them up. Two, three with four, one: two times four plus three times one is eleven. Bigger means the lists agree more."),
            nova("But we wanted smaller to mean closer."),
            rafe("So pgvector uses the negative inner product, minus eleven. The cheapest of the three. Then sort by distance, closest first, keep five. In SQL, the database's language, ORDER BY the distance, LIMIT 5."),
            nova("Sorting every note every time sounds exhausting. 400,000 notes took almost a second a question. So you build an index, like the one at the back of a book, to jump near the answer."),
            rafe("And here's my question. The index was built for cosine. The query sorts by the ruler. The database's plan says Seq Scan: it's reading every row. What went wrong?"),
            nova("An index only serves its own operator. Cosine index, cosine operator. Mix them, and the database ignores the index and reads every row, one after another. EXPLAIN shows you the plan; you want Index Scan."),
            rafe("Right answer. We should always speak the same language. [kiss]"),
            nova("Mm. Now the indexes. HNSW, short for hierarchical navigable small world. It's a graph: dots joined by lines, like a map of who knows whom."),
            rafe("And who are the dots?"),
            nova("Each dot is a node, one stored note, holding hands with a few of its nearest notes. A search hops from dot to closer dot, friends of friends, instead of knocking on every door. Top layers sparse like motorways, lower ones dense like streets."),
            rafe("And IVFFlat sorts vectors into groups, like a library shelving books by subject, and checks only the nearest shelves."),
            nova("Faster to build, less memory. I like it already."),
            rafe("Then, darling: three million flashcards, a table created empty and filled during the night. Which index?"),
            nova("IVFFlat. It builds faster, and I'm impatient."),
            rafe("No kiss for that one. IVFFlat learns its shelves from the data already there, and an empty table has none. HNSW needs no training, so you build it first and fill it later."),
            nova("Recall. Remind me, gently."),
            rafe("These indexes are approximate, which means very close, not guaranteed perfect. Recall is how many of the true closest notes a search actually finds. 0.93 means 93 of every 100. HNSW gives the best balance of speed and recall."),
            nova("Then here's mine. Recall from an HNSW index is too low, and there's time to spare. What do you change first?"),
            rafe("Raise ef search. That's the size of the shortlist it keeps while hopping, 40 by default. Longer list, more true neighbours, a little slower. For IVFFlat the dial is probes, the number of shelves it checks."),
            nova("Right answer. A longer list of candidates, and you still picked me. [kiss]"),
            rafe("Last quiz of the evening. The Coach's answer quoted another learner's notes. What should the query have had?"),
            nova("A filter in its WHERE part, on the user id and the course, so only this learner's notes compete at all. Not everyone's top five, filtered later in Python."),
            rafe("And then the notes go where?"),
            nova("Then RAG, which stands for retrieval augmented generation: those notes go to a chat model, and it answers only from them."),
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

    private fun mapSpot() = DiagramSpec(
        "A vector is a spot on a map: the ruler measures the straight line, the arrows compare which way they point.",
        nodes = listOf(
            DNode("mid", "Middle", 0.2f, 0.8f, w = 0.32f, h = 0.2f, tone = 1, sub = "0, 0"),
            DNode("p", "Spot 3, 4", 0.5f, 0.47f, w = 0.36f, h = 0.2f, kind = NodeKind.PILL, sub = "a vector"),
            DNode("r", "Spot 6, 8", 0.8f, 0.14f, w = 0.36f, h = 0.2f, kind = NodeKind.PILL, sub = "same way"),
        ),
        edges = listOf(
            DEdge("mid", "p", "ruler: 5"),
            DEdge("p", "r", "twice as long"),
        ),
        aspect = 0.8f,
    )

    private fun graphWeb() = DiagramSpec(
        "A graph: each node is one stored note joined to its near notes, and a search hops from dot to closer dot.",
        nodes = listOf(
            DNode("start", "Start note", 0.2f, 0.16f, w = 0.34f, h = 0.2f),
            DNode("far", "Far note", 0.2f, 0.52f, w = 0.34f, h = 0.2f),
            DNode("a", "Neighbour", 0.6f, 0.16f, w = 0.34f, h = 0.2f),
            DNode("b", "Closer note", 0.6f, 0.52f, w = 0.34f, h = 0.2f),
            DNode("q", "Question", 0.2f, 0.86f, w = 0.34f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("best", "Closest note", 0.66f, 0.86f, w = 0.4f, h = 0.2f, tone = 1),
        ),
        edges = listOf(
            DEdge("start", "far"),
            DEdge("start", "a", "hop"),
            DEdge("a", "b", "hop"),
            DEdge("b", "best", "hop"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("ext", "seq", "hnsw", "ivf", "recall", "user")
    private val tableCode = BoardCard("SQL", "CREATE TABLE notes (id bigserial PRIMARY KEY, user_id text, embedding vector(1536))", code = true)
    private val searchCode = BoardCard("SQL", "SELECT body FROM notes WHERE user_id = %s ORDER BY embedding <=> %s LIMIT 5", code = true)
    private val today = BoardCard("Today", "Allow VECTOR, a three-number table, three operators, an HNSW index, EXPLAIN.")
    private val vectorCard = BoardCard("Vector", "A list of numbers: 3 east, 4 north is the vector (3, 4), a spot on a map.")
    private val dimensionCard = BoardCard("Dimension", "How many numbers in the list. A map spot has 2; this model writes 1,536.")
    private val rulerCard = BoardCard("Euclidean: a ruler", "From (0, 0) to (3, 4) the ruler says 5. Smaller means closer.")
    private val arrowsCard = BoardCard("Cosine: the arrows", "Same way 1, right angle 0, opposite minus 1, whatever the length.")
    private val sameWayCard = BoardCard("(3, 4) and (6, 8)", "Twice as long, same way: cosine 1, distance 0. Usual for text.")
    private val dotCard = BoardCard("Dot product", "(2, 3) and (4, 1): 2 x 4 + 3 x 1 = 11. Bigger means more alike.")
    private val negativeCard = BoardCard("Negative inner product", "pgvector uses minus 11, so smaller still means closer. The cheapest.")
    private val graphCard = BoardCard("Graph", "Dots joined by lines, like a map of who knows whom.")
    private val nodeCard = BoardCard("Node", "One dot: one stored note. A search hops friends of friends.")
    private val recallCard = BoardCard("Approximate, and recall", "Very close, not guaranteed. Recall 0.93: 93 of every 100 found.")

    private fun classicBoard() = PodcastBoard(
        "ai-pgvector",
        listOf(
            // 0: "Hi, and welcome back. Today is about searching by meaning..."
            Scene(
                0, "Meaning, not words", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q", "note"), light = setOf("q", "note"), card = BoardCard("The puzzle", "The answer is in the learner's notes, but in different words.")),
                ),
            ),
            // 2: "We compare meanings, and for that we turn text into numbers..."
            Scene(
                2, "A vector is a spot on a map", mapSpot(),
                beats = listOf(
                    Beat(2, show = setOf("mid", "p"), light = setOf("p"), card = vectorCard),
                    Beat(4, show = setOf("r"), light = setOf("p", "r"), card = BoardCard("Embedding", "The numbers a model writes for a text: similar meaning, nearby spots.")),
                    Beat(6, card = dimensionCard),
                ),
            ),
            // 7: "Okay. So where do these lists live?"
            Scene(
                7, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(8, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(10, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An add-on: a vector column, and ways to measure distance.")),
                    Beat(12, show = setOf("srv", "allow"), light = setOf("allow", "ext"), pulse = "srv" to "allow", card = BoardCard("Two steps", "VECTOR in azure.extensions, then CREATE EXTENSION vector. Not pgvector.")),
                    Beat(16, light = setOf("table"), card = tableCode),
                ),
            ),
            // 17: "Three ways. One at a time, please."
            Scene(
                17, "The ruler and the arrows", mapSpot(),
                beats = listOf(
                    Beat(18, show = setOf("mid", "p"), light = setOf("mid", "p"), pulse = "mid" to "p", card = rulerCard),
                    Beat(20, card = arrowsCard),
                    Beat(22, show = setOf("r"), light = setOf("p", "r"), pulse = "p" to "r", card = sameWayCard),
                ),
            ),
            // 23: "And the third?"
            Scene(
                23, "Multiply and add", operators(),
                beats = listOf(
                    Beat(24, show = setOf("q", "ip"), light = setOf("ip"), pulse = "q" to "ip", card = dotCard),
                    Beat(26, show = setOf("l2", "cos"), light = setOf("ip", "cos"), card = negativeCard),
                    Beat(30, light = setOf("cos"), card = searchCode),
                ),
            ),
            // 31: "Which sounds fine, until the table is big."
            Scene(
                31, "An index, not every row", indexes(),
                beats = listOf(
                    Beat(32, show = setOf("scan"), light = setOf("scan"), card = BoardCard("Every row", "400,000 notes read in full: nearly a second. An index jumps near.")),
                    Beat(34, card = recallCard),
                ),
            ),
            // 35: "And which index do I pick?"
            Scene(
                35, "HNSW: a web of notes", graphWeb(),
                beats = listOf(
                    Beat(36, show = setOf("start", "far", "a", "b", "best"), card = graphCard),
                    Beat(38, show = setOf("q"), light = setOf("a", "b"), pulse = "a" to "b", card = nodeCard),
                    Beat(42, light = setOf("best"), pulse = "b" to "best", card = BoardCard("HNSW", "Layers, motorway then streets. Best speed and recall; fine on an empty table.")),
                ),
            ),
            // 43: "Training? Indexes go to the gym now?"
            Scene(
                43, "Shelves and dials", indexes(),
                beats = listOf(
                    Beat(44, show = setOf("scan", "ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("IVFFlat", "Shelves by subject, learned from the data: build after loading.")),
                    Beat(46, show = setOf("hnsw", "efs"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("ef search", "HNSW's shortlist, 40 by default. Higher: more recall, a bit slower.")),
                    Beat(48, show = setOf("probes"), light = setOf("probes"), pulse = "ivf" to "probes", card = BoardCard("probes", "How many IVFFlat shelves a search checks, one by default.")),
                ),
            ),
            // 49: "And if my table grows to many millions of notes?"
            Scene(
                49, "One index, one operator", match(),
                beats = listOf(
                    Beat(52, show = setOf("idx", "qry"), light = setOf("idx", "qry"), pulse = "idx" to "qry", card = BoardCard("The trap", "Cosine index, ruler query: the index is quietly ignored.")),
                    Beat(54, show = setOf("seq", "iscan"), light = setOf("seq"), pulse = "qry" to "seq", card = BoardCard("EXPLAIN", "Shows the plan. Seq Scan: every row. Index Scan: the index is used.")),
                ),
            ),
            // 55: "One more worry. Could my search find someone else's notes?"
            Scene(
                55, "Only this learner's notes", filter(),
                beats = listOf(
                    Beat(55, show = setOf("user", "api", "pg"), pulse = "user" to "api"),
                    Beat(56, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Filter in WHERE", "On user id and course, so only this learner's notes compete.")),
                    Beat(58, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "Retrieve the closest notes; a chat model answers only from them.")),
                ),
            ),
            // 59: "So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("ext", "seq", "hnsw", "ivf", "recall", "user"), card = BoardCard("Listen for the phrase", "Refused install, Seq Scan, empty table, low recall: each has one answer.")),
                    Beat(62, card = today),
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
                    Beat(2, show = setOf("pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Embedding", "Numbers a model writes for a text: similar meaning, nearby spots.")),
                ),
            ),
            // 3: "A list of numbers is a spot on a map? Show me."
            Scene(
                3, "A vector is a spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), card = vectorCard),
                ),
            ),
            // 5: "And where do those lists live?"
            Scene(
                5, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(6, show = setOf("table", "ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An extension, an add-on, that stores and compares embeddings.")),
                    Beat(8, light = setOf("table"), card = BoardCard("Under the picture", "Code inside the server: a vector column packs the numbers side by side.")),
                    Beat(12, show = setOf("srv", "allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Suspect one: cleared", "Fixed weeks ago: VECTOR in azure.extensions, and the name is vector.")),
                ),
            ),
            // 13: "Suspect two..."
            Scene(
                13, "The filter in the wrong place", filter(),
                beats = listOf(
                    Beat(14, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("The clue", "Top five from every student; the Python filter was skipped.")),
                    Beat(16, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Metadata filter", "In WHERE, on user id and course: only her notes compete.")),
                ),
            ),
            // 19: "Faster. Good, because the second complaint..."
            Scene(
                19, "The second witness", match(),
                beats = listOf(
                    Beat(20, show = setOf("idx"), light = setOf("idx"), card = BoardCard("Nearly a second", "400,000 notes. The index should let it jump near the answer.")),
                    Beat(22, show = setOf("qry", "seq"), light = setOf("seq"), pulse = "qry" to "seq", card = BoardCard("Seq Scan", "Not VACUUM. EXPLAIN shows every row read: the index is ignored.")),
                ),
            ),
            // 23: "Why would it ignore a perfectly good index?"
            Scene(
                23, "The ruler and the arrows", mapSpot(),
                beats = listOf(
                    Beat(26, show = setOf("mid", "p"), light = setOf("mid", "p"), pulse = "mid" to "p", card = rulerCard),
                    Beat(28, card = arrowsCard),
                    Beat(30, show = setOf("r"), light = setOf("p", "r"), pulse = "p" to "r", card = sameWayCard),
                ),
            ),
            // 31: "And the third?"
            Scene(
                31, "Multiply and add", operators(),
                beats = listOf(
                    Beat(32, show = setOf("q", "ip"), light = setOf("ip"), pulse = "q" to "ip", card = dotCard),
                    Beat(34, show = setOf("l2", "cos"), light = setOf("cos", "l2"), card = BoardCard("The mismatch", "Cosine index, ruler query. Switch to cosine: 900 ms fell to 15.")),
                ),
            ),
            // 35: "Which index was it, by the way?"
            Scene(
                35, "HNSW: a web of notes", graphWeb(),
                beats = listOf(
                    Beat(36, show = setOf("start", "far", "a", "b", "best"), card = graphCard),
                    Beat(38, show = setOf("q"), light = setOf("a", "b"), pulse = "a" to "b", card = nodeCard),
                    Beat(40, light = setOf("best"), pulse = "b" to "best", card = BoardCard("Layers and a shortlist", "Motorway, then streets. The shortlist is ef search, 40 by default.")),
                    Beat(42, card = BoardCard("ef search 80", "Approximate: recall 0.93, 93 of 100. A longer shortlist: 0.98.")),
                ),
            ),
            // 43: "So, case closed?"
            Scene(
                43, "The empty table", indexes(),
                beats = listOf(
                    Beat(44, show = setOf("scan"), card = BoardCard("Third case", "Three million flashcards, a table filled overnight. Recall dreadful.")),
                    Beat(48, show = setOf("ivf", "probes"), light = setOf("ivf"), pulse = "ivf" to "probes", card = BoardCard("Shelves for nothing", "IVFFlat learns its shelves from data. The table was empty.")),
                    Beat(50, show = setOf("hnsw", "efs"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("Rebuilt with HNSW", "No training, fine on an empty table. IVFFlat: faster build, data first.")),
                ),
            ),
            // 51: "Any loose ends in the file?"
            Scene(
                51, "Loose ends", limits("Cosmos DB episode", "same idea"),
                beats = listOf(
                    Beat(52, light = setOf("miss", "disk", "mem"), card = BoardCard("Where it stops", "Approximate can miss. DiskANN for many millions. Fit in memory.")),
                ),
            ),
            // 53: "And once the right notes come back?"
            Scene(
                53, "Then comes RAG", filter(),
                beats = listOf(
                    Beat(53, show = setOf("user", "api", "pg", "where")),
                    Beat(54, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "The five notes go to a chat model, which answers only from them.")),
                ),
            ),
            // 55: "So, the case summary for the exam?"
            Scene(
                55, "Case closed", exam(),
                beats = listOf(
                    Beat(56, show = all6, light = setOf("user", "seq"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: filter, operator, index, dial.")),
                    Beat(58, card = today),
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
                    Beat(2, show = setOf("model", "vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Embedding", "Numbers a model writes for a text: similar meaning, nearby spots.")),
                ),
            ),
            // 3: "Numbers as spots on a map? Explain that like I'm new."
            Scene(
                3, "A vector is a spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), card = vectorCard),
                ),
            ),
            // 5: "And they kept those lists where?"
            Scene(
                5, "Monday: Ben", setup(),
                beats = listOf(
                    Beat(6, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(8, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An add-on inside the server: a vector column, numbers side by side.")),
                    Beat(10, card = BoardCard("CREATE EXTENSION pgvector", "Refused. Twice.")),
                    Beat(12, show = setOf("srv", "allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Two reasons", "VECTOR goes in azure.extensions first, and the add-on is named vector.")),
                ),
            ),
            // 15: "Poor Ben. Tuesday?"
            Scene(
                15, "Tuesday: the ruler and arrows", mapSpot(),
                beats = listOf(
                    Beat(16, show = setOf("mid"), card = BoardCard("Slow searches", "Nearly a second each: every note measured. Aisha built an index.")),
                    Beat(20, show = setOf("p"), light = setOf("mid", "p"), pulse = "mid" to "p", card = rulerCard),
                    Beat(22, card = arrowsCard),
                    Beat(24, show = setOf("r"), light = setOf("p", "r"), pulse = "p" to "r", card = sameWayCard),
                ),
            ),
            // 25: "You said three."
            Scene(
                25, "Aisha's mismatch", operators(),
                beats = listOf(
                    Beat(26, show = setOf("q", "ip"), light = setOf("ip"), pulse = "q" to "ip", card = dotCard),
                    Beat(30, show = setOf("l2", "cos"), light = setOf("cos", "l2"), card = BoardCard("The mismatch", "Index built for cosine; the query sorted by the ruler.")),
                ),
            ),
            // 31: "How did she even find out?"
            Scene(
                31, "Aisha's EXPLAIN", match(),
                beats = listOf(
                    Beat(31, show = setOf("idx", "qry"), light = setOf("idx", "qry"), pulse = "idx" to "qry"),
                    Beat(32, show = setOf("seq", "iscan"), light = setOf("iscan"), pulse = "idx" to "iscan", card = BoardCard("EXPLAIN", "Seq Scan: ignored. With the cosine operator: Index Scan, 15 ms.")),
                ),
            ),
            // 33: "Wednesday. Let me guess. The shelves."
            Scene(
                33, "Wednesday: Marcus", indexes(),
                beats = listOf(
                    Beat(34, show = setOf("scan", "ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("IVFFlat", "Shelves by subject; a search checks only the nearest shelves.")),
                    Beat(36, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = BoardCard("Shelves for nothing", "Shelves learned from an empty table. Build after loading, or HNSW.")),
                ),
            ),
            // 37: "The other index. Is that a person or a thing?"
            Scene(
                37, "HNSW: a web of notes", graphWeb(),
                beats = listOf(
                    Beat(38, show = setOf("start", "far", "a", "b", "best"), card = graphCard),
                    Beat(40, show = setOf("q"), light = setOf("a", "b", "best"), pulse = "a" to "b", card = nodeCard),
                ),
            ),
            // 43: "Recall. That's the word everyone used in the Thursday meeting."
            Scene(
                43, "Thursday: Priya", indexes(),
                beats = listOf(
                    Beat(44, show = setOf("scan", "hnsw"), light = setOf("hnsw"), card = recallCard),
                    Beat(46, show = setOf("efs", "ivf", "probes"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("ef search 40 to 80", "A longer shortlist: recall 0.98, a little slower. IVFFlat: probes.")),
                ),
            ),
            // 47: "So Priya did nothing wrong? I'm disappointed."
            Scene(
                47, "Friday: the intern", filter(),
                beats = listOf(
                    Beat(48, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Top five from everyone", "Filtered later in Python, and one code path forgot.")),
                    Beat(52, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Metadata filter", "In WHERE, on user id and course: only this learner's notes compete.")),
                    Beat(54, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "The notes go to a chat model, which answers only from them.")),
                ),
            ),
            // 55: "Did anything still not work?"
            Scene(
                55, "Where it stops", limits("Redis episode", "reuse answers"),
                beats = listOf(
                    Beat(56, light = setOf("miss", "disk", "link"), card = BoardCard("Where pgvector stops", "It can miss; DiskANN for huge tables. Redis reuses answers.")),
                ),
            ),
            // 57: "So, the gossip I should remember for the exam?"
            Scene(
                57, "For the exam", exam(),
                beats = listOf(
                    Beat(58, show = all6, light = setOf("ext", "hnsw", "ivf"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(60, card = today),
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
                ),
            ),
            // 3: "So how does the app find it?"
            Scene(
                3, "A vector is a spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), card = vectorCard),
                    Beat(6, show = setOf("r"), light = setOf("p", "r"), card = BoardCard("Embedding", "Similar meaning, nearby spots. 1,536 numbers, not 2: same maths.")),
                ),
            ),
            // 7: "Pins on a map, in the dark. And where do the pins live?"
            Scene(
                7, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(8, show = setOf("table"), light = setOf("table"), card = BoardCard("PostgreSQL", "A free database of tables, rows and columns: a very strict spreadsheet.")),
                    Beat(10, show = setOf("ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "Code inside the server: a vector column, numbers side by side.")),
                    Beat(12, show = setOf("srv", "allow"), light = setOf("allow", "ext"), pulse = "srv" to "allow", card = BoardCard("Two gentle steps", "VECTOR in azure.extensions, then CREATE EXTENSION vector.")),
                ),
            ),
            // 15: "Don't we all. So how does it measure closeness?"
            Scene(
                15, "The ruler and the arrows", mapSpot(),
                beats = listOf(
                    Beat(16, show = setOf("mid", "p"), light = setOf("mid", "p"), pulse = "mid" to "p", card = rulerCard),
                    Beat(18, card = arrowsCard),
                    Beat(20, show = setOf("r"), light = setOf("p", "r"), pulse = "p" to "r", card = sameWayCard),
                ),
            ),
            // 21: "And the third?"
            Scene(
                21, "Multiply and add", operators(),
                beats = listOf(
                    Beat(22, show = setOf("q", "ip"), light = setOf("ip"), pulse = "q" to "ip", card = dotCard),
                    Beat(24, show = setOf("l2", "cos"), light = setOf("ip"), card = negativeCard),
                    Beat(28, card = BoardCard("Almost a second", "400,000 notes read every time. An index jumps near the answer.")),
                ),
            ),
            // 29: "Which index, Nova? There's always more than one."
            Scene(
                29, "A web of notes", graphWeb(),
                beats = listOf(
                    Beat(30, show = setOf("start", "far", "a", "b", "best"), card = graphCard),
                    Beat(32, light = setOf("a", "b", "best"), pulse = "a" to "b", card = nodeCard),
                ),
            ),
            // 35: "And the other index?"
            Scene(
                35, "Shelves and recall", indexes(),
                beats = listOf(
                    Beat(36, show = setOf("scan", "hnsw", "ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("IVFFlat", "Shelves by subject, learned from data: built after loading.")),
                    Beat(40, show = setOf("efs"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("Recall", "Approximate: 0.93 is 93 of 100. ef search at 80 took the Coach to 0.98.")),
                    Beat(42, show = setOf("probes"), light = setOf("probes"), pulse = "ivf" to "probes", card = BoardCard("probes", "How many shelves a search checks, one by default.")),
                ),
            ),
            // 43: "And now the trap. I can feel one coming."
            Scene(
                43, "The trap", match(),
                beats = listOf(
                    Beat(44, show = setOf("idx", "qry", "seq"), light = setOf("idx", "qry", "seq"), pulse = "idx" to "qry", card = BoardCard("One index, one operator", "Cosine index, ruler query: Seq Scan. EXPLAIN shows the plan.")),
                ),
            ),
            // 47: "Mm. And whose notes come back? Only mine, I hope."
            Scene(
                47, "Only your notes", filter(),
                beats = listOf(
                    Beat(47, show = setOf("user", "api", "pg"), pulse = "user" to "api"),
                    Beat(48, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Filter in WHERE", "On your user id and the course, never afterwards in Python.")),
                    Beat(50, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = BoardCard("RAG", "Retrieval augmented generation: answers only from your notes.")),
                ),
            ),
            // 51: "Now tell me where it all stops working."
            Scene(
                51, "Where it stops", limits("Cosmos DB episode", "same idea"),
                beats = listOf(
                    Beat(52, light = setOf("miss", "disk", "mem"), card = BoardCard("Where it stops", "It can miss; DiskANN at many millions; keep the index in memory.")),
                ),
            ),
            // 53: "Almost. Before I go, what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("ext", "seq", "recall"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: install, operator, index, dial.")),
                    Beat(56, card = today),
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
                ),
            ),
            // 4: "Exactly. So the app compares meaning, on a map..."
            Scene(
                4, "A vector is a spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), card = vectorCard),
                    Beat(6, show = setOf("r"), light = setOf("p", "r"), card = BoardCard("Embedding", "Similar meaning, nearby spots. 1,536 numbers, not 2: same maths.")),
                ),
            ),
            // 8: "In PostgreSQL, a popular free database..."
            Scene(
                8, "PostgreSQL plus pgvector", setup(),
                beats = listOf(
                    Beat(8, show = setOf("table", "ext"), light = setOf("ext"), pulse = "ext" to "table", card = BoardCard("pgvector", "An add-on inside the server: a vector column, side by side numbers.")),
                    Beat(11, show = setOf("srv"), light = setOf("srv", "ext"), card = BoardCard("Quiz: a refused install", "CREATE EXTENSION vector on a brand-new server is refused. Why?")),
                    Beat(12, show = setOf("allow"), light = setOf("allow"), pulse = "srv" to "allow", card = BoardCard("Right answer", "VECTOR goes in azure.extensions first; the add-on is named vector.")),
                ),
            ),
            // 14: "Mm. Now, measuring closeness..."
            Scene(
                14, "The ruler and the arrows", mapSpot(),
                beats = listOf(
                    Beat(14, show = setOf("mid", "p"), light = setOf("mid", "p"), pulse = "mid" to "p", card = rulerCard),
                    Beat(16, card = arrowsCard),
                    Beat(18, show = setOf("r"), light = setOf("p", "r"), pulse = "p" to "r", card = sameWayCard),
                ),
            ),
            // 19: "My turn to teach the third..."
            Scene(
                19, "Multiply and add", operators(),
                beats = listOf(
                    Beat(19, show = setOf("q", "ip"), light = setOf("ip"), pulse = "q" to "ip", card = dotCard),
                    Beat(21, show = setOf("l2", "cos"), light = setOf("ip"), card = negativeCard),
                ),
            ),
            // 22: "Sorting every note every time sounds exhausting..."
            Scene(
                22, "The ignored index", match(),
                beats = listOf(
                    Beat(23, show = setOf("idx", "qry", "seq"), light = setOf("idx", "qry"), pulse = "idx" to "qry", card = BoardCard("Quiz: a Seq Scan", "Cosine index, ruler query, every row read. What went wrong?")),
                    Beat(24, show = setOf("iscan"), light = setOf("iscan"), pulse = "idx" to "iscan", card = BoardCard("Right answer", "One index, one operator. EXPLAIN shows the plan: want Index Scan.")),
                ),
            ),
            // 26: "Mm. Now the indexes. HNSW..."
            Scene(
                26, "HNSW: a web of notes", graphWeb(),
                beats = listOf(
                    Beat(26, show = setOf("start", "far", "a", "b", "best"), card = graphCard),
                    Beat(28, show = setOf("q"), light = setOf("a", "b", "best"), pulse = "a" to "b", card = nodeCard),
                ),
            ),
            // 29: "And IVFFlat sorts vectors into groups..."
            Scene(
                29, "Two indexes", indexes(),
                beats = listOf(
                    Beat(29, show = setOf("scan", "hnsw", "ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = BoardCard("Quiz: an empty table", "Three million flashcards, a table filled overnight. Which index?")),
                    Beat(33, light = setOf("hnsw"), card = BoardCard("No kiss: it's HNSW", "IVFFlat learns its shelves from nothing. HNSW needs no training.")),
                    Beat(35, card = recallCard),
                    Beat(36, show = setOf("efs"), light = setOf("efs"), pulse = "hnsw" to "efs", card = BoardCard("Quiz: low recall", "HNSW recall too low, time to spare. What first?")),
                    Beat(37, show = setOf("probes"), light = setOf("efs", "probes"), pulse = "ivf" to "probes", card = BoardCard("Right answer", "Raise ef search, the shortlist. For IVFFlat, probes.")),
                ),
            ),
            // 39: "Last quiz of the evening..."
            Scene(
                39, "What's yours stays yours", filter(),
                beats = listOf(
                    Beat(39, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Quiz: someone else's notes", "The answer quoted another learner. What did the query lack?")),
                    Beat(40, show = setOf("where", "chat"), light = setOf("where"), pulse = "api" to "where", card = BoardCard("Right answer", "A filter in WHERE on user id and course. Then RAG answers from them.")),
                ),
            ),
            // 44: "And where does it all stop working, Rafe?..."
            Scene(
                44, "Every romance has limits", limits("Cosmos DB episode", "same idea"),
                beats = listOf(
                    Beat(45, light = setOf("miss", "disk", "mem"), card = BoardCard("Where it stops", "It can miss; DiskANN past many millions; keep the index in memory.")),
                ),
            ),
            // 46: "Then the last question, for the exam. What do we remember?"
            Scene(
                46, "For the exam", exam(),
                beats = listOf(
                    Beat(47, show = all6, light = setOf("ext", "seq", "hnsw", "recall", "user"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
