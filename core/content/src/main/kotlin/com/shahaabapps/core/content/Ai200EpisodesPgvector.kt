package com.shahaabapps.core.content

/** pgvector on PostgreSQL: vector search and RAG: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesPgvector {
    private val classic = Podcast(
        "ai-pgvector",
        listOf(
            maya("Hi, and welcome back. Zaiqa is in good shape. The Zaiqa API, the backend our phone app talks to, runs on Container Apps, orders and payments are safe in Azure SQL, and the search that understands, something like dal makhani but vegan, is live on Cosmos DB."),
            leo("So the meaning search is finished. Why are we opening it again?"),
            maya("Because PostgreSQL can do the same search, and the exam wants you to know when to pick which. Postgres already holds Zaiqa's payouts ledger, the table of what each restaurant is owed. Today we build dish search a second time, right beside it."),
            leo("Hold on. Remind me what a meaning search actually stores."),
            maya("A list of numbers for every dish. Picture a town map: three steps east, four steps north, and your spot is three, four. A list of numbers like that is the vector. It is a spot on a map, nothing cleverer."),
            leo("And who writes the numbers?"),
            maya("An AI model whose only job is that. The list it writes for a piece of text is called an embedding, so two dishes that mean the same thing land close together, even in different words."),
            leo("Right. Can Postgres hold a list like that in one column?"),
            maya("Not on its own. It needs an extension, an add-on, like a plug-in for your browser. The one we want is pgvector. It adds a column type called vector, and ways to measure how far apart two lists are."),
            leo("And on Azure I run create extension pgvector?"),
            maya("[chuckle] Two traps in one line. The extension is named vector; pgvector is the project. And Azure's Postgres keeps a guest list of allowed add-ons, a setting called azure.extensions, empty on a new server. Put VECTOR on the list first, then run create extension vector."),
            leo("Then the table."),
            maya("A dishes table with the usual columns, plus one of type vector holding 1,536 numbers, the same count the model writes. And that is the whole argument for doing it here: it sits in the same database as the restaurant rows we already join to."),
            leo("And searching is a normal query?"),
            maya("Almost. Instead of equals you use a distance operator, written as a little arrow between the column and the question's numbers. There are three, one for each way of measuring."),
            leo("Give me the three."),
            maya("Euclidean is a ruler laid between two pins on the map. From the middle to the spot three, four, the ruler reads five. A real distance, so smaller means closer."),
            leo("That is one."),
            maya("Cosine distance. Picture an arrow from the middle of the map to each spot, and ask whether the two arrows point the same way, whatever their length. Same way is no distance at all. For text, this is the usual pick."),
            leo("And the third?"),
            maya("Inner product, which means multiply the matching numbers and add them up. Two, three against four, one is eight plus three, so eleven. With lists already scaled to length one, as OpenAI's are, it ranks exactly like cosine and costs the least to work out."),
            leo("So I order by the distance and keep the top five."),
            maya("Order by it, limit five. And add a where clause on the city, so only dishes a diner could actually order are in the race."),
            leo("Done. [pause] Is that it?"),
            maya("With four hundred thousand dishes, no. [pause] With nothing to help it, Postgres works out the distance to every single row. Explain analyze shows that as a sequential scan, which just means reading the table top to bottom. Nine hundred milliseconds a question."),
            leo("So I add an index."),
            maya("You do. An index is like the one at the back of a book: jump near the answer instead of reading every page. But a vector index gives something up. It is approximate, which means very close, not guaranteed the closest."),
            leo("How much does it miss?"),
            maya("You measure that, and the measure is called recall: the share of the truly closest rows a search actually returns. Recall of 0.93 means ninety-three in every hundred. You test it on a set of real questions, not by feel."),
            leo("And which index do I build?"),
            maya("Two come with pgvector. The first is HNSW. In plain words it is a web of dishes: each dish joined to a few near ones, in layers, so a search hops from dish to closer dish instead of knocking on every door."),
            leo("And what does that web cost?"),
            maya("It is slow to build and hungry for memory. In return it gives the best speed against recall of anything here, and there is no training step, so you can build it on an empty table and fill the table afterwards."),
            leo("And the second?"),
            maya("IVFFlat, which means the rows are sorted into groups first, like shelves by subject, and a search opens only the nearest shelves. It builds much faster and takes less memory. But the shelves are worked out from the rows already in the table."),
            leo("So an empty table breaks it."),
            maya("It would train its shelves on nothing. Load the dishes first, then build, with roughly one shelf per thousand rows, up to about a million rows."),
            leo("And at query time, can I buy back the misses?"),
            maya("One knob each. For HNSW it is ef search, the size of the shortlist it keeps while hopping, forty by default. For IVFFlat it is probes, the number of shelves one search opens, one by default. Raise either and you trade time for recall."),
            leo("And for a truly enormous table?"),
            maya("DiskANN, which Azure adds as pg diskann. It is a graph index, meaning the same web idea, dots joined by lines, built for tables of many millions of rows, with high recall and quick builds."),
            leo("Is there a trap waiting for me?"),
            maya("One that catches everybody. An index only serves the operator it was built for. Build it for cosine, then order by the ruler operator, and Postgres quietly ignores it and reads every row. Explain analyze says sequential scan again, and nobody knows why."),
            leo("How else do I keep it quick?"),
            maya("Three habits. Filter in the where clause so fewer rows compete, for example this city, this restaurant. Keep the top few small: five good dishes beat fifty. And give the index room to live in memory, so memory optimized compute for big sets, never burstable in production."),
            leo("And what the diner actually reads?"),
            maya("The same ending as in Cosmos: RAG, short for retrieval augmented generation. You fetch the five closest dishes and paste their descriptions into the message you send the chat model, which is told to answer only from those."),
            leo("Which brings me to the real question. Cosmos or Postgres?"),
            maya("Honestly, both work, and the exam asks it exactly that way. [pause] Pick Cosmos when the vectors sit beside documents you already keep there and you want copies close to diners in every region. Pick Postgres when they belong beside rows you are already joining to."),
            leo("And Zaiqa keeps which?"),
            maya("Both, on purpose. Dish search stays in Cosmos, beside the order feed. The payouts team's search over restaurant notes goes in Postgres, because every answer is joined to a ledger row. Say you're building one small app with one database: use the one you already have."),
            leo("Fair. So what do I remember for the exam?"),
            maya("Five phrases. Create extension refused on a new server: the azure.extensions guest list, and the name is vector. A sequential scan with an index sitting right there: the query's operator does not match the index. An empty table filled overnight, high recall wanted: HNSW."),
            leo("And the other two?"),
            maya("Faster build, less memory, data already loaded: IVFFlat. Recall too low with time to spare: raise ef search, or probes on IVFFlat. And an answer quoting another city's dishes: the filter belongs in the where clause."),
            leo("And anything to try today?"),
            maya("Put VECTOR on the azure.extensions list and run create extension vector. Make a small table with a three-number vector column and insert five rows. Order by each of the three operators and compare the top three. Then build an HNSW cosine index and confirm the index scan with explain."),
            leo("Vectors beside the feed, or vectors beside the ledger. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-pgvector",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Search That Read The Whole Menu. Zaiqa's payouts team got a search over restaurant notes in PostgreSQL last month. On Monday it took nine hundred milliseconds a question. On Tuesday it quoted a restaurant in another city."),
            leo("Detective Leo, notebook out. Start with what was built, because I thought search by meaning lived in Cosmos DB."),
            maya("Dish search does, beside the order feed, and it stays there. This is a second one. Postgres already holds Zaiqa's payouts ledger, the table of what each restaurant is owed, so the team put their notes in the same database."),
            leo("Notes searched by meaning. Which means storing what, exactly?"),
            maya("A list of numbers for each note. Picture a town map: three steps east, four north, and your spot is three, four. A list of numbers like that is the vector. An AI model writes one for any text, and that list is called an embedding."),
            leo("So notes that mean the same thing sit near each other. Can Postgres hold that in a column?"),
            maya("Only with an extension, an add-on, like a plug-in for your browser. This one is pgvector: a column type called vector, and operators that measure how far apart two lists are."),
            leo("Clue one, then. Where did the trouble start?"),
            maya("Before any of it. Marcus ran create extension pgvector, and Postgres refused point blank. Two things were wrong at once, and both are exam favourites."),
            leo("My guess: the server wanted a restart."),
            maya("No. The extension is named vector; pgvector is the project's name. And Azure's Postgres keeps a guest list of allowed add-ons, a setting called azure.extensions, empty on a new server. Put VECTOR on it, then run create extension vector."),
            leo("Noted. Clue two. How does the query even ask for the closest note?"),
            maya("With a distance operator, a little arrow written between the column and the question's numbers. Three to choose from. Euclidean is a ruler laid between two pins on the map: from the middle to the spot three, four, it reads five."),
            leo("And the other two?"),
            maya("Cosine distance. Picture an arrow from the middle out to each spot, and ask whether two arrows point the same way, whatever their length. Same way is no distance. For text it is the usual pick."),
            leo("Third?"),
            maya("Inner product, which means multiply the matching numbers and add them up. With lists scaled to length one, as OpenAI's are, it ranks like cosine and costs least. For all three, smaller is closer, so you order by it and limit five."),
            leo("Good. Now the nine hundred milliseconds."),
            maya("Four hundred thousand notes, and nothing to help, so Postgres measured against every single row. [pause] Explain analyze called it a sequential scan, which just means reading the table top to bottom."),
            leo("Then Dana adds an index and we all go home."),
            maya("Dana did, built for cosine distance. The query stayed at nine hundred milliseconds. [sigh] An index is like the one at the back of a book, and this one was never opened."),
            leo("Sabotage. Somebody dropped it in the night."),
            maya("Nothing so exciting. The query ordered by the ruler operator, not the cosine one, and an index only serves the operator it was built for. Match the two and explain analyze says index scan instead."),
            leo("Clue three. Tuesday's wrong restaurant."),
            maya("That query took the five closest notes from the whole table. No filter at all, and then the code tidied up afterwards in Python."),
            leo("So we ask for more rows and filter harder?"),
            maya("The opposite. [pause] A where clause on the city and the restaurant, so only rows that caller may see are in the race. Fewer rows compete, it is cheaper, and nobody reads another restaurant's notes."),
            leo("Case nearly closed. Was the index even the right kind?"),
            maya("That is the last file. Two kinds come with pgvector, and they cost different things. HNSW first: in plain words a web of notes, each joined to a few near ones in layers, so a search hops from note to closer note."),
            leo("Slow to build, I assume."),
            maya("Slow, and hungry for memory. In return it gives the best speed against recall there is, and recall means the share of the truly closest rows a search actually returns. No training step either, so an empty table is fine."),
            leo("And the other kind?"),
            maya("IVFFlat, which means the rows are sorted into groups first, like shelves by subject, so a search opens only the nearest shelves. It builds far faster and takes less memory. But the shelves are worked out from the rows already loaded."),
            leo("And on an empty table?"),
            maya("It trains on nothing, and the answers are poor. That was Priya's week: she built it before the night load, then rebuilt it after, with about one shelf per thousand rows."),
            leo("And when recall is simply short?"),
            maya("One knob each. For HNSW, ef search, the size of the shortlist it keeps while hopping, forty by default. For IVFFlat, probes, the number of shelves a search opens, one by default. Raise either and you pay in time."),
            leo("Anything for a table of many millions?"),
            maya("DiskANN, which Azure adds as pg diskann. It is a graph index, meaning the same web idea, dots joined by lines, built for very large tables, with high recall and quick builds."),
            leo("And the answer the payouts team reads?"),
            maya("RAG, short for retrieval augmented generation, the same ending as the Cosmos episode. Fetch the five closest notes, paste them into the message to a chat model, and tell it to answer only from those."),
            leo("One thing still bothers me. Why Postgres at all, when Cosmos already does this?"),
            maya("Because every one of these notes is joined to a ledger row the team already queries. Cosmos earns it when the vectors sit beside documents you keep there anyway, and you want copies close to diners in every region."),
            leo("Case closed. What goes in the report for the exam?"),
            maya("Create extension refused: the azure.extensions guest list, and the name is vector. A sequential scan with an index right there: the operator does not match. An empty table and high recall: HNSW. Faster build with data already loaded: IVFFlat."),
            leo("And the last two?"),
            maya("Recall too low with time to spare: raise ef search, or probes on IVFFlat. And someone else's rows in the answer: the filter belongs in the where clause, not in Python afterwards."),
            leo("And something to try today?"),
            maya("Allow VECTOR in azure.extensions and run create extension vector. Make a table with a three-number vector column and insert five rows. Order by each of the three operators and compare the top three. Then build an HNSW cosine index and confirm the index scan with explain."),
            leo("Case closed, and the index finally used. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-pgvector",
        listOf(
            maya("Okay, did you hear what the payouts team did to PostgreSQL this week? Sit down. They decided Zaiqa needed a second search by meaning, over restaurant notes, in the same database as the payouts ledger. Five days, five disasters."),
            leo("I heard somebody shouting the words sequential scan across the office. But first: why a second search at all? Cosmos DB already does dish search."),
            maya("And it keeps it. This one is different. Every restaurant note the team searches is joined to a ledger row, the line that says what that restaurant is owed, and those rows live in Postgres."),
            leo("Fine. So what are they storing?"),
            maya("A list of numbers for each note. Picture a town map: three steps east, four north, and your spot is three, four. A list like that is the vector. An AI model writes one for any text, and that list is called an embedding."),
            leo("Right. Monday."),
            maya("Tom. Tom ran create extension pgvector and got refused, and spent the morning convinced the server needed a restart."),
            leo("[chuckle] And it did not."),
            maya("Two things were wrong. The extension is named vector; pgvector is the project. And Azure's Postgres keeps a guest list of allowed add-ons, a setting called azure.extensions, empty on a new server. Put VECTOR on it, then create extension vector."),
            leo("Tuesday. Who is next?"),
            maya("Dana, and Dana's was the expensive one. [pause] Four hundred thousand notes, no help of any kind, so Postgres measured the distance to every single row. Nine hundred milliseconds a question."),
            leo("Measured how? What does a distance even look like here?"),
            maya("A little arrow written between the column and the question's numbers, and there are three. Euclidean is a ruler laid between two pins on the map: from the middle to three, four, it reads five."),
            leo("Go on, the other two."),
            maya("Cosine distance. Picture an arrow from the middle out to each spot and ask whether two arrows point the same way, whatever their length. Same way is no distance, and for text it is what everybody uses."),
            leo("And the third?"),
            maya("Inner product, which means multiply the matching numbers and add them up. With lists scaled to length one, as OpenAI's are, it ranks like cosine and is the cheapest to work out. Smaller is closer for all three."),
            leo("So Dana's query was fine, it just read everything."),
            maya("Explain analyze called it a sequential scan, which just means reading the table top to bottom. So on Wednesday Priya built an index, and that is where it gets good."),
            leo("An index should have fixed it."),
            maya("An index is like the one at the back of a book: jump near the answer instead of reading every page. Priya built hers for cosine distance. The query still took nine hundred milliseconds."),
            leo("[laugh] Why?"),
            maya("Because the query ordered by the ruler operator, not the cosine one, and an index only serves the operator it was built for. Postgres ignored it and read every row, politely, without a word of complaint."),
            leo("Thursday. Please tell me Thursday was calm."),
            maya("Thursday was Marcus, and Marcus had read about the other kind. Two come with pgvector. HNSW, which in plain words is a web of notes, each joined to a few near ones in layers, so a search hops from note to closer note."),
            leo("And the other?"),
            maya("IVFFlat, which means the rows are sorted into groups first, like shelves by subject, and a search opens only the nearest shelves. Faster to build, lighter on memory. The catch is that the shelves are worked out from the rows already loaded."),
            leo("And Marcus built it on?"),
            maya("An empty table, the night before the big load. It trained its shelves on nothing. [sigh] HNSW has no training step, which is exactly why it is the safe pick for a table that fills later."),
            leo("What did that cost in quality, measurably?"),
            maya("Recall fell through the floor, and recall means the share of the truly closest rows a search actually returns. His was about 0.4: four in every ten. After a rebuild, with about one shelf per thousand rows, it came back."),
            leo("And if it is merely a bit low?"),
            maya("One knob each. For HNSW, ef search, the size of the shortlist it keeps while hopping, forty by default. For IVFFlat, probes, the number of shelves a search opens, one by default. Raise either and you trade time for recall."),
            leo("Friday. Last one, and I can tell it is the embarrassing one."),
            maya("The intern. [pause] The search returned the five closest notes from the whole table, with no filter, and an answer quoted a restaurant in another city to the payouts team."),
            leo("Which they presumably noticed."),
            maya("Within a minute. The fix is a where clause on the city and the restaurant, so only rows that caller may see are in the race. Filtering afterwards in Python is the mistake the exam loves most."),
            leo("Did anything go right all week?"),
            maya("Plenty. The ending is RAG, short for retrieval augmented generation, the same as in the Cosmos episode: fetch the five closest notes and paste them into the message to a chat model, told to answer only from those."),
            leo("And did anyone ask the obvious question? Why not just use Cosmos?"),
            maya("Priya did, on Friday, and she is right to. Cosmos when the vectors sit beside documents you already keep there and you want copies close to diners in every region. Postgres when they belong beside rows you are already joining to."),
            leo("And if the table grows to many millions of notes?"),
            maya("Then DiskANN, which Azure adds as pg diskann. It is a graph index, meaning the same web idea, dots joined by lines, built for very large tables, with high recall and quick builds."),
            leo("So what is the gossip I should remember for the exam?"),
            maya("Create extension refused: the azure.extensions guest list, and the name is vector. A sequential scan with an index right there: the operator does not match. An empty table filled overnight: HNSW, not Marcus's choice."),
            leo("And Dana's and the intern's?"),
            maya("Faster build with the data already loaded: IVFFlat. Recall too low with time to spare: raise ef search, or probes. And someone else's rows in an answer: the filter belongs in the where clause."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Allow VECTOR in azure.extensions and run create extension vector. Make a table with a three-number vector column and insert five rows. Order by each of the three operators and compare the top three. Then build an HNSW cosine index and confirm the index scan with explain."),
            leo("No sequential scans, and no shelves built on an empty table. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-pgvector",
        listOf(
            nova("Hey, you. It is late, the kitchen is shut, and it is just the three of us. Me, Ivy, and you, still awake. [pause] Tonight: the same search Zaiqa already has, built a second time, somewhere else."),
            ivy("Somewhere else being PostgreSQL. Why would anyone do it twice, Nova?"),
            nova("Because the exam will ask you to choose, and because Postgres already holds Zaiqa's payouts ledger, the table of what each restaurant is owed. [pause] Numbers like these prefer to live near the rows they belong to."),
            ivy("Start me from the beginning. What is actually stored?"),
            nova("A list of numbers for every dish. Picture a town map: three steps east, four steps north, and your spot is three, four. A list of numbers like that is the vector. Just a spot on a map."),
            ivy("And the numbers come from somewhere clever, I assume."),
            nova("An AI model whose whole job is that. The list it writes for a piece of text is called an embedding. [pause] Two dishes that mean the same thing land close together, even in different words."),
            ivy("And Postgres can hold such a thing?"),
            nova("Not on its own. It needs an extension, an add-on, like a plug-in for your browser, and ours is pgvector. It brings a column type called vector, and ways to measure how far apart two lists are."),
            ivy("Then I switch it on and go to bed."),
            nova("Not quite. The extension is named vector, not pgvector, and Azure's Postgres keeps a guest list of allowed add-ons, a setting called azure.extensions, empty on a new server. [pause] Put VECTOR on the list, then run create extension vector."),
            ivy("Mm. And the search itself?"),
            nova("A normal query, with a distance operator instead of equals: a little arrow between the column and the question's numbers. Three of them, and I will give you one at a time."),
            ivy("Slowly, please. It is late."),
            nova("Euclidean is a ruler laid between two pins on the map. From the middle to the spot three, four, the ruler reads five. [pause] A real distance, so smaller means closer."),
            ivy("One. Next."),
            nova("Cosine distance. Picture an arrow from the middle of the map out to each spot, and ask whether the two arrows point the same way, whatever their length. Same way is no distance at all. For text, this is the one you want."),
            ivy("And the third, before I drift off?"),
            nova("Inner product, which means multiply the matching numbers and add them up. With lists already scaled to length one, as OpenAI's are, it ranks exactly like cosine and is the cheapest. Order by the distance, limit five, and filter by city."),
            ivy("So that is the whole thing working."),
            nova("Working, and slow. Four hundred thousand dishes and nothing to help, so Postgres measures against every row. [pause] Explain analyze calls it a sequential scan, which just means reading the table top to bottom. Nine hundred milliseconds a question."),
            ivy("Then give it an index and let us both sleep."),
            nova("An index is like the one at the back of a book: jump near the answer instead of reading every page. But a vector index is approximate, which means very close, not guaranteed the closest."),
            ivy("[sigh] And how badly does it cheat?"),
            nova("You measure it, and the measure is called recall: the share of the truly closest rows a search actually returns. Recall of 0.93 is ninety-three in every hundred. Test it on real questions, never by feel."),
            ivy("Which index, then?"),
            nova("Two come with pgvector. HNSW, which in plain words is a web of dishes: each one joined to a few near ones, in layers, so a search hops from dish to closer dish instead of knocking on every door."),
            ivy("Sounds expensive to build."),
            nova("Slow, and hungry for memory. In return, the best speed against recall of anything here, and no training step. [pause] You can build it on an empty table tonight and fill the table tomorrow."),
            ivy("And its rival?"),
            nova("IVFFlat, which means the rows are sorted into groups first, like shelves by subject, so a search opens only the nearest shelves. Builds far faster, takes less memory. But its shelves are worked out from the rows already loaded."),
            ivy("So never on an empty table."),
            nova("Never. It would train on nothing. Load first, then build, about one shelf per thousand rows. [pause] That is the trade, and the exam loves to put it to you as a table filled overnight."),
            ivy("And if the answers are still a little thin?"),
            nova("One knob each. For HNSW, ef search, the size of the shortlist it keeps while hopping, forty by default. For IVFFlat, probes, the number of shelves a search opens, one by default. Raise either and you pay in time."),
            ivy("And where does all this stop working?"),
            nova("Three places. An index only serves the operator it was built for, so a cosine index with a ruler query is ignored. Past many millions of rows there is DiskANN: a graph index, meaning the same web idea, dots joined by lines, that Azure adds as pg diskann."),
            ivy("And the third?"),
            nova("Memory. The index should fit in it, so memory optimized compute for big sets, never burstable in production. [pause] Otherwise it waits on disk all evening, like the rest of us."),
            ivy("And the poor tired diner gets what?"),
            nova("An answer. RAG, short for retrieval augmented generation, the same ending as the Cosmos night: fetch the five closest dishes, paste them into the message to a chat model, and tell it to answer only from those."),
            ivy("Then the question I have been saving. Cosmos or Postgres?"),
            nova("Both are right answers in different rooms. Cosmos when the vectors sit beside documents you already keep there and you want copies close to diners in every region. Postgres when they belong beside rows you are already joining to, like the ledger."),
            ivy("Mm. Before I give in. What do I remember for the exam?"),
            nova("Create extension refused on a new server: the azure.extensions guest list, and the name is vector. A sequential scan with an index right there: the operator does not match. An empty table filled overnight, high recall: HNSW."),
            ivy("And two more, quietly."),
            nova("Faster build with the data already loaded: IVFFlat. Recall a little low with time to spare: raise ef search, or probes. And another city's dishes in an answer: the filter belongs in the where clause."),
            ivy("And something to try today, before the light goes off?"),
            nova("Allow VECTOR in azure.extensions and run create extension vector. Make a table with a three-number vector column and insert five rows. Order by each of the three operators and compare the top three. Then build an HNSW cosine index and confirm the index scan with explain."),
            ivy("Vectors beside the feed, or vectors beside the ledger. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-pgvector",
        listOf(
            nova("Tonight is date night, darling, and I have brought a database to the table. PostgreSQL, since you ask. Zaiqa already searches dishes by meaning in Cosmos DB, and tonight we do the whole thing again, in Postgres, beside the payouts ledger."),
            rafe("You know how to spoil me. The ledger being the table of what each restaurant is owed."),
            nova("That one. And the rule of the evening is the usual: I ask, you answer, and a right answer gets paid for. First, warm up. What does a search by meaning actually store?"),
            rafe("A list of numbers for every dish. Picture a town map: three steps east, four north, and your spot is three, four. A list like that is the vector, and an AI model writes one for any text. That list is called an embedding."),
            nova("Right answer, and beautifully said. [kiss]"),
            rafe("Then my turn. Postgres holds no such column. What do we do about it?"),
            nova("We add an extension, an add-on, like a plug-in for your browser. Ours is pgvector: a column type called vector, and operators that measure how far apart two lists are."),
            rafe("Good. Now the exam version, and I will be strict. On a brand new Azure flexible server, create extension pgvector is refused. Two things are wrong. Name them both."),
            nova("One: the extension is named vector; pgvector is only the project's name. Two: Azure's Postgres keeps a guest list of allowed add-ons, a setting called azure.extensions, and it starts empty. Put VECTOR on it, then create extension vector."),
            rafe("Two for two. [kiss] Now, how does the query ask for something close?"),
            nova("With a distance operator, a little arrow between the column and the question's numbers. Three to pick from, and here is where you earn your dinner. Describe Euclidean."),
            rafe("A ruler laid between two pins on the map. From the middle to the spot three, four, it reads five. Smaller means closer."),
            nova("Mm, correct. And cosine distance?"),
            rafe("Picture an arrow from the middle out to each spot, and ask whether two arrows point the same way, whatever their length. Same way is no distance. It is the usual pick for text."),
            nova("Right answer, twice over. [kiss] And the third, love?"),
            rafe("Inner product, which means multiply the matching numbers and add them up. With lists scaled to length one, as OpenAI's are, it ranks like cosine and is the cheapest. Order by the distance, limit five, filter by city."),
            nova("Lovely. Now something harder. Four hundred thousand dishes, no help at all, and the search takes nine hundred milliseconds. What does explain analyze say, and why?"),
            rafe("Sequential scan, which just means reading the table top to bottom, measuring the distance to every single row. Exact, and far too slow for a hungry diner."),
            nova("And the cure?"),
            rafe("An index. Which is like the one at the back of a book: jump near the answer instead of reading every page."),
            nova("Right answer, and quick with it. [kiss] But a vector index asks something of you in return."),
            rafe("It is approximate, which means very close, not guaranteed the closest. And you measure what it misses."),
            nova("With what, exactly? Give me the word and the meaning."),
            rafe("Recall: the share of the truly closest rows a search actually returns. Recall of 0.93 is ninety-three in every hundred, tested on real questions."),
            nova("Mm, good. Now the real exam question of the evening. A new table, filled overnight by a loading job, and high recall matters. Which index, and why?"),
            rafe("IVFFlat, because it is cheaper to build."),
            nova("[sigh] No kiss for that one. IVFFlat means the rows are sorted into groups first, like shelves by subject, and the shelves are worked out from the rows already loaded. On an empty table it trains on nothing."),
            rafe("Then HNSW. Which is, in plain words?"),
            nova("A web of dishes: each one joined to a few near ones, in layers, so a search hops from dish to closer dish instead of knocking on every door. Slow to build, hungry for memory, best speed against recall, and no training step."),
            rafe("So an empty table is fine, and I have learned my lesson. Let me win one back. Recall is low and there is time to spare. What do we turn?"),
            nova("Say it properly and it is yours."),
            rafe("Ef search for HNSW, the size of the shortlist it keeps while hopping, forty by default. Probes for IVFFlat, the number of shelves a search opens, one by default. Either way, time for recall."),
            nova("Right answer. [kiss] And if the table ever reaches many millions of rows?"),
            rafe("DiskANN, which Azure adds as pg diskann. It is a graph index, meaning the same web idea, dots joined by lines, built for very large tables, with high recall and quick builds."),
            nova("One more trap, and it catches everybody. The index exists, and explain analyze still says sequential scan."),
            rafe("The query ordered by the wrong operator. An index only serves the operator it was built for, so a cosine index wants the cosine arrow, not the ruler."),
            nova("Right answer, darling. [kiss] And an answer that quoted a restaurant in another city?"),
            rafe("No filter. A where clause on the city and the restaurant, so only rows that caller may see are in the race. Never tidy it up afterwards in Python."),
            nova("Perfect. And the ending the diner sees is the one from our Cosmos date: RAG, short for retrieval augmented generation, where the five closest dishes go into the message to a chat model, told to answer only from those."),
            rafe("Then the question we have been circling all evening. Cosmos, or Postgres?"),
            nova("Both, and knowing which is the whole point. Cosmos when the vectors sit beside documents you already keep there and you want copies close to diners in every region. Postgres when they belong beside rows you are already joining to."),
            rafe("Like the ledger. So what do we take to the exam, love?"),
            nova("Create extension refused: the azure.extensions guest list, and the name is vector. A sequential scan with an index right there: the operator does not match. An empty table filled overnight: HNSW."),
            rafe("And the last two?"),
            nova("Faster build with the data already loaded: IVFFlat. Recall low with time to spare: raise ef search, or probes. And another city's dishes in an answer: the filter belongs in the where clause."),
            rafe("Five for five, between us. And something to try today?"),
            nova("Allow VECTOR in azure.extensions and run create extension vector. Make a table with a three-number vector column and insert five rows. Order by each of the three operators and compare the top three. Then build an HNSW cosine index and confirm the index scan with explain."),
            rafe("Vectors beside the ledger, and the washing up beside me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun setup() = DiagramSpec(
        "Put VECTOR on the server's guest list, run CREATE EXTENSION vector, then store embeddings in a vector column.",
        nodes = listOf(
            DNode("srv", "Azure PostgreSQL", 0.25f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "run for you"),
            DNode("allow", "Guest list", 0.75f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "azure.extensions"),
            DNode("ext", "CREATE EXTENSION", 0.25f, 0.55f, w = 0.44f, h = 0.2f, sub = "vector, not pgvector"),
            DNode("table", "Dishes table", 0.75f, 0.58f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "vector column"),
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
        "The query filters on the city in WHERE, takes the five closest dishes, and a chat model answers only from them.",
        nodes = listOf(
            DNode("user", "Diner", 0.1f, 0.2f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("api", "Taste service", 0.42f, 0.2f, w = 0.34f, h = 0.2f, tone = 1, sub = "Python"),
            DNode("where", "WHERE city", 0.8f, 0.2f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "and restaurant"),
            DNode("pg", "PostgreSQL", 0.42f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "top 5 dishes"),
            DNode("chat", "Chat model", 0.84f, 0.72f, w = 0.3f, h = 0.2f, tone = 2, sub = "answers"),
        ),
        edges = listOf(
            DEdge("user", "api", "question"),
            DEdge("api", "where", "filter"),
            DEdge("api", "pg", "query"),
            DEdge("where", "pg"),
            DEdge("pg", "chat", "5 dishes"),
        ),
        aspect = 0.8f,
    )

    private fun choose() = DiagramSpec(
        "The exam's choice: Cosmos DB beside the documents you already keep there, Postgres beside the rows you join to.",
        nodes = listOf(
            DNode("cosmos", "Cosmos DB", 0.26f, 0.2f, w = 0.46f, h = 0.22f, tone = 1, sub = "beside the order feed"),
            DNode("pg", "PostgreSQL", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 2, sub = "beside the ledger"),
            DNode("global", "Copies everywhere", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "pick Cosmos"),
            DNode("join", "Joined to rows", 0.76f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "pick Postgres"),
        ),
        edges = listOf(
            DEdge("cosmos", "global"),
            DEdge("pg", "join"),
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
            DNode("city", "Another city's dish", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "WHERE city"),
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
        "HNSW as a web: each stored dish is joined to a few near dishes, and a search hops from dot to closer dot.",
        nodes = listOf(
            DNode("start", "Start dish", 0.2f, 0.16f, w = 0.34f, h = 0.2f),
            DNode("far", "Far dish", 0.2f, 0.52f, w = 0.34f, h = 0.2f),
            DNode("a", "Neighbour", 0.6f, 0.16f, w = 0.34f, h = 0.2f),
            DNode("b", "Closer dish", 0.6f, 0.52f, w = 0.34f, h = 0.2f),
            DNode("q", "Question", 0.2f, 0.86f, w = 0.34f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("best", "Closest dish", 0.66f, 0.86f, w = 0.4f, h = 0.2f, tone = 1),
        ),
        edges = listOf(
            DEdge("start", "far"),
            DEdge("start", "a", "hop"),
            DEdge("a", "b", "hop"),
            DEdge("b", "best", "hop"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("ext", "seq", "hnsw", "ivf", "recall", "city")
    private val today = BoardCard("Today", "Allow VECTOR, a three-number table, three operators, an HNSW index, EXPLAIN.")
    private val vectorCard = BoardCard("Vector", "A list of numbers: 3 east, 4 north is the vector (3, 4), a spot on a map.")
    private val embeddingCard = BoardCard("Embedding", "The list a model writes for a text. Similar meanings land close together.")
    private val extensionCard = BoardCard("The name is vector", "pgvector is the project. The extension you create is called vector.")
    private val allowCard = BoardCard("azure.extensions", "Azure's guest list of allowed add-ons, empty on a new server.")
    private val rulerCard = BoardCard("Euclidean: a ruler", "From (0, 0) to (3, 4) the ruler says 5. Smaller means closer.")
    private val arrowsCard = BoardCard("Cosine: the arrows", "Do they point the same way, whatever their length? The usual for text.")
    private val innerCard = BoardCard("Inner product", "Multiply the matching numbers and add them up. The cheapest of the three.")
    private val scanCard = BoardCard("Sequential scan", "No index: every row measured. 400,000 dishes, nine hundred milliseconds.")
    private val indexCard = BoardCard("An index", "Like the one at the back of a book: jump near, do not read every page.")
    private val recallCard = BoardCard("Approximate, and recall", "Very close, not guaranteed. Recall 0.93: 93 of every 100 found.")
    private val hnswCard = BoardCard("HNSW", "A web in layers. Slow build, more memory, and no training step.")
    private val ivfCard = BoardCard("IVFFlat", "Shelves worked out from loaded rows. Fast build, less memory.")
    private val knobsCard = BoardCard("Two knobs", "ef search for HNSW, probes for IVFFlat. Time bought back as recall.")
    private val mismatchCard = BoardCard("Match the operator", "A cosine index serves cosine queries. A ruler query reads every row.")
    private val filterCard = BoardCard("Filter in WHERE", "City and restaurant, so fewer rows compete and nobody sees another's.")
    private val ragCard = BoardCard("RAG", "Retrieval augmented generation: five dishes into the message, answer from those.")
    private val chooseCard = BoardCard("Cosmos or Postgres", "Beside documents and copies everywhere, or beside rows you join to.")

    private fun classicBoard() = PodcastBoard(
        "ai-pgvector",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is in good shape..."
            Scene(
                0, "The same search, elsewhere", choose(),
                beats = listOf(
                    Beat(0, show = setOf("cosmos"), light = setOf("cosmos"), card = BoardCard("Today", "Dish search again, in PostgreSQL, beside the payouts ledger.")),
                    Beat(2, show = setOf("pg", "join"), light = setOf("pg"), pulse = "pg" to "join", card = BoardCard("Postgres is already here", "It holds the payouts ledger: what each restaurant is owed.")),
                ),
            ),
            // 4: "A list of numbers for every dish. Picture a town map..."
            Scene(
                4, "A spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), pulse = "mid" to "p", card = vectorCard),
                    Beat(6, show = setOf("r"), light = setOf("r"), card = embeddingCard),
                ),
            ),
            // 8: "Not on its own. It needs an extension..."
            Scene(
                8, "Turning it on", setup(),
                beats = listOf(
                    Beat(8, show = setOf("srv", "ext"), light = setOf("ext"), card = BoardCard("An extension", "An add-on for Postgres, like a plug-in for your browser.")),
                    Beat(10, show = setOf("allow"), light = setOf("allow"), pulse = "srv" to "allow", card = extensionCard),
                    Beat(11, light = setOf("allow"), card = allowCard),
                    Beat(12, show = setOf("table"), light = setOf("table"), pulse = "ext" to "table", card = BoardCard("The dishes table", "A vector column of 1,536 numbers, beside the rows you join to.")),
                ),
            ),
            // 14: "Almost. Instead of equals you use a distance operator..."
            Scene(
                14, "Three ways to measure", operators(),
                beats = listOf(
                    Beat(14, show = setOf("q", "l2", "cos", "ip"), light = setOf("q"), card = BoardCard("A distance operator", "A little arrow between the column and the question's numbers.")),
                    Beat(16, light = setOf("l2"), pulse = "q" to "l2", card = rulerCard),
                    Beat(18, light = setOf("cos"), pulse = "q" to "cos", card = arrowsCard),
                    Beat(20, light = setOf("ip"), pulse = "q" to "ip", card = innerCard),
                    Beat(22, light = setOf("cos"), card = BoardCard("Order by, limit five", "Smaller is closer. And a WHERE on the city before anything is ranked.")),
                ),
            ),
            // 24: "With four hundred thousand dishes, no..."
            Scene(
                24, "Every row, exactly", indexes(),
                beats = listOf(
                    Beat(24, show = setOf("scan"), light = setOf("scan"), card = scanCard),
                    Beat(26, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = indexCard),
                    Beat(28, light = setOf("hnsw"), card = recallCard),
                ),
            ),
            // 30: "Two come with pgvector. The first is HNSW..."
            Scene(
                30, "HNSW: a web in layers", graphWeb(),
                beats = listOf(
                    Beat(30, show = setOf("start", "a", "b", "best"), light = setOf("start", "a"), pulse = "start" to "a", card = hnswCard),
                    Beat(32, show = setOf("far", "q"), light = setOf("b", "best"), pulse = "b" to "best", card = BoardCard("What the web costs", "Slow build, more memory. Best speed against recall, and no training.")),
                ),
            ),
            // 34: "IVFFlat, which means the rows are sorted into groups first..."
            Scene(
                34, "IVFFlat: shelves by subject", indexes(),
                beats = listOf(
                    Beat(34, show = setOf("scan", "ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = ivfCard),
                    Beat(36, light = setOf("ivf"), card = BoardCard("Load first, then build", "Its shelves are worked out from the rows already in the table.")),
                    Beat(38, show = setOf("hnsw", "efs", "probes"), light = setOf("efs", "probes"), pulse = "hnsw" to "efs", card = knobsCard),
                    Beat(40, light = setOf("scan"), card = BoardCard("DiskANN", "Azure adds pg diskann: the same web idea, for many millions of rows.")),
                ),
            ),
            // 42: "One that catches everybody. An index only serves the operator..."
            Scene(
                42, "Match the operator", match(),
                beats = listOf(
                    Beat(42, show = setOf("idx", "qry", "seq", "iscan"), light = setOf("idx", "qry"), pulse = "idx" to "qry", card = mismatchCard),
                    Beat(44, light = setOf("iscan"), card = BoardCard("Three habits", "Filter in WHERE, keep the top few small, give the index memory.")),
                ),
            ),
            // 45: "And what the diner actually reads?"
            Scene(
                45, "The answer the diner reads", filter(),
                beats = listOf(
                    Beat(46, show = setOf("user", "api", "where", "pg", "chat"), light = setOf("pg", "chat"), pulse = "pg" to "chat", card = ragCard),
                ),
            ),
            // 47: "Which brings me to the real question. Cosmos or Postgres?"
            Scene(
                47, "Cosmos or Postgres", choose(),
                beats = listOf(
                    Beat(48, show = setOf("cosmos", "pg", "global", "join"), light = setOf("cosmos", "global"), pulse = "cosmos" to "global", card = chooseCard),
                    Beat(50, light = setOf("pg", "join"), pulse = "pg" to "join", card = BoardCard("Zaiqa keeps both", "Dish search in Cosmos; the payouts team's notes in Postgres.")),
                ),
            ),
            // 51: "Fair. So what do I remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("ext", "seq", "hnsw"), card = BoardCard("Listen for the phrase", "Refused install, a sequential scan, an empty table: one answer each.")),
                    Beat(54, light = setOf("ivf", "recall", "city"), card = BoardCard("And three more", "IVFFlat, raise ef search, and the filter in the WHERE clause.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-pgvector--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Search That Read The Whole Menu..."
            Scene(
                0, "The case", filter(),
                beats = listOf(
                    Beat(0, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("The case", "Nine hundred milliseconds a question, then another city's restaurant.")),
                    Beat(2, show = setOf("where"), light = setOf("api"), card = BoardCard("Why Postgres at all", "These notes are joined to the payouts ledger rows already there.")),
                ),
            ),
            // 4: "A list of numbers for each note. Picture a town map..."
            Scene(
                4, "A note as a spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), pulse = "mid" to "p", card = vectorCard),
                    Beat(6, show = setOf("r"), light = setOf("r"), card = embeddingCard),
                ),
            ),
            // 8: "Before any of it. Marcus ran create extension pgvector..."
            Scene(
                8, "Clue one: refused", setup(),
                beats = listOf(
                    Beat(8, show = setOf("srv", "ext"), light = setOf("ext"), card = BoardCard("Clue one", "Create extension pgvector, refused point blank. Two reasons at once.")),
                    Beat(10, show = setOf("allow"), light = setOf("allow"), pulse = "srv" to "allow", card = extensionCard),
                    Beat(11, light = setOf("allow"), card = allowCard),
                ),
            ),
            // 12: "With a distance operator, a little arrow..."
            Scene(
                12, "Three operators", operators(),
                beats = listOf(
                    Beat(12, show = setOf("q", "l2", "cos", "ip"), light = setOf("l2"), pulse = "q" to "l2", card = rulerCard),
                    Beat(14, light = setOf("cos"), pulse = "q" to "cos", card = arrowsCard),
                    Beat(16, light = setOf("ip"), pulse = "q" to "ip", card = innerCard),
                ),
            ),
            // 18: "Four hundred thousand notes, and nothing to help..."
            Scene(
                18, "Clue two: every row", indexes(),
                beats = listOf(
                    Beat(18, show = setOf("scan"), light = setOf("scan"), card = scanCard),
                    Beat(20, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = indexCard),
                ),
            ),
            // 22: "Nothing so exciting. The query ordered by the ruler operator..."
            Scene(
                22, "The index never opened", match(),
                beats = listOf(
                    Beat(22, show = setOf("idx", "qry", "seq", "iscan"), light = setOf("idx", "qry"), pulse = "idx" to "qry", card = mismatchCard),
                ),
            ),
            // 24: "That query took the five closest notes from the whole table..."
            Scene(
                24, "Clue three: no filter", filter(),
                beats = listOf(
                    Beat(24, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Clue three", "Five closest notes from the whole table, tidied up in Python after.")),
                    Beat(26, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = filterCard),
                ),
            ),
            // 28: "That is the last file. Two kinds come with pgvector..."
            Scene(
                28, "Two indexes, two costs", indexes(),
                beats = listOf(
                    Beat(28, show = setOf("scan", "hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = hnswCard),
                    Beat(30, light = setOf("hnsw"), card = recallCard),
                    Beat(32, show = setOf("ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = ivfCard),
                    Beat(34, light = setOf("ivf"), card = BoardCard("Trained on nothing", "Built before the night load, its shelves learned from an empty table.")),
                    Beat(36, show = setOf("efs", "probes"), light = setOf("efs", "probes"), pulse = "ivf" to "probes", card = knobsCard),
                ),
            ),
            // 38: "DiskANN, which Azure adds as pg diskann..."
            Scene(
                38, "Where it stops", limits("Cosmos DB episode", "the same idea"),
                beats = listOf(
                    Beat(38, show = setOf("miss", "disk", "mem"), light = setOf("disk"), card = BoardCard("DiskANN", "Azure adds pg diskann: the same web idea, for very large tables.")),
                    Beat(40, show = setOf("link"), light = setOf("link"), card = ragCard),
                ),
            ),
            // 41: "One thing still bothers me. Why Postgres at all...?"
            Scene(
                41, "Why Postgres, not Cosmos", choose(),
                beats = listOf(
                    Beat(42, show = setOf("cosmos", "pg", "global", "join"), light = setOf("pg", "join"), pulse = "pg" to "join", card = chooseCard),
                ),
            ),
            // 43: "Case closed. What goes in the report for the exam?"
            Scene(
                43, "Case closed", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("ext", "seq"), card = BoardCard("Listen for the phrase", "A refused install, and a sequential scan with the index sitting there.")),
                    Beat(46, light = setOf("hnsw", "ivf", "recall", "city"), card = BoardCard("And four more", "HNSW, IVFFlat, raise ef search, and the filter in the WHERE clause.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-pgvector--gossip",
        listOf(
            // 0: "Okay, did you hear what the payouts team did to PostgreSQL this week?"
            Scene(
                0, "Did you hear", choose(),
                beats = listOf(
                    Beat(0, show = setOf("pg"), light = setOf("pg"), card = BoardCard("The week", "A second search by meaning, in Postgres, beside the payouts ledger.")),
                    Beat(2, show = setOf("join"), light = setOf("join"), pulse = "pg" to "join", card = BoardCard("Why there", "Every note is joined to a ledger row the team already queries.")),
                ),
            ),
            // 4: "A list of numbers for each note. Picture a town map..."
            Scene(
                4, "What they stored", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), pulse = "mid" to "p", card = vectorCard),
                    Beat(5, show = setOf("r"), light = setOf("r"), card = embeddingCard),
                ),
            ),
            // 6: "Tom. Tom ran create extension pgvector and got refused..."
            Scene(
                6, "Monday: Tom", setup(),
                beats = listOf(
                    Beat(6, show = setOf("srv", "ext"), light = setOf("ext"), card = BoardCard("Monday: Tom", "Create extension pgvector, refused, and a morning lost to restarts.")),
                    Beat(8, show = setOf("allow"), light = setOf("allow"), pulse = "srv" to "allow", card = extensionCard),
                    Beat(9, light = setOf("allow"), card = allowCard),
                ),
            ),
            // 10: "Dana, and Dana's was the expensive one..."
            Scene(
                10, "Tuesday: Dana", indexes(),
                beats = listOf(
                    Beat(10, show = setOf("scan"), light = setOf("scan"), card = scanCard),
                ),
            ),
            // 12: "A little arrow written between the column and the question's numbers..."
            Scene(
                12, "Three operators", operators(),
                beats = listOf(
                    Beat(12, show = setOf("q", "l2", "cos", "ip"), light = setOf("l2"), pulse = "q" to "l2", card = rulerCard),
                    Beat(14, light = setOf("cos"), pulse = "q" to "cos", card = arrowsCard),
                    Beat(16, light = setOf("ip"), pulse = "q" to "ip", card = innerCard),
                ),
            ),
            // 18: "Explain analyze called it a sequential scan..."
            Scene(
                18, "Wednesday: the quiet index", match(),
                beats = listOf(
                    Beat(18, show = setOf("seq"), light = setOf("seq"), card = BoardCard("Sequential scan", "Top to bottom, every row: nine hundred milliseconds a question.")),
                    Beat(20, show = setOf("idx", "qry", "iscan"), light = setOf("idx"), card = indexCard),
                    Beat(22, light = setOf("qry", "seq"), pulse = "idx" to "qry", card = mismatchCard),
                ),
            ),
            // 24: "Thursday was Marcus, and Marcus had read about the other kind..."
            Scene(
                24, "Thursday: Marcus", indexes(),
                beats = listOf(
                    Beat(24, show = setOf("scan", "hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = hnswCard),
                    Beat(26, show = setOf("ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = ivfCard),
                    Beat(28, light = setOf("ivf"), card = BoardCard("Built on an empty table", "Its shelves trained on nothing. HNSW has no training step at all.")),
                    Beat(30, light = setOf("hnsw"), card = recallCard),
                    Beat(32, show = setOf("efs", "probes"), light = setOf("efs", "probes"), pulse = "hnsw" to "efs", card = knobsCard),
                ),
            ),
            // 33: "Friday. Last one, and I can tell it is the embarrassing one."
            Scene(
                33, "Friday: the intern", filter(),
                beats = listOf(
                    Beat(34, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Friday: the intern", "Five closest notes from the whole table, and another city in the answer.")),
                    Beat(36, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = filterCard),
                    Beat(38, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = ragCard),
                ),
            ),
            // 39: "And did anyone ask the obvious question? Why not just use Cosmos?"
            Scene(
                39, "Why not Cosmos", choose(),
                beats = listOf(
                    Beat(40, show = setOf("cosmos", "pg", "global", "join"), light = setOf("cosmos", "global"), pulse = "cosmos" to "global", card = chooseCard),
                    Beat(42, light = setOf("pg"), card = BoardCard("DiskANN", "Azure adds pg diskann: the same web idea, for many millions of rows.")),
                ),
            ),
            // 43: "So what is the gossip I should remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("ext", "seq", "hnsw"), card = BoardCard("Listen for the phrase", "Refused install, a sequential scan, an empty table: one answer each.")),
                    Beat(46, light = setOf("ivf", "recall", "city"), card = BoardCard("And three more", "IVFFlat, raise ef search, and the filter in the WHERE clause.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-pgvector--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen is shut..."
            Scene(
                0, "Tonight: the other store", choose(),
                beats = listOf(
                    Beat(0, show = setOf("pg"), light = setOf("pg"), card = BoardCard("Tonight", "The same search again, in PostgreSQL, beside the payouts ledger.")),
                    Beat(2, show = setOf("join"), light = setOf("join"), pulse = "pg" to "join", card = BoardCard("Near its own rows", "Numbers prefer to live beside the rows they are joined to.")),
                ),
            ),
            // 4: "A list of numbers for every dish. Picture a town map..."
            Scene(
                4, "A spot on a map", mapSpot(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "p"), light = setOf("p"), pulse = "mid" to "p", card = vectorCard),
                    Beat(6, show = setOf("r"), light = setOf("r"), card = embeddingCard),
                ),
            ),
            // 8: "Not on its own. It needs an extension..."
            Scene(
                8, "Switching it on", setup(),
                beats = listOf(
                    Beat(8, show = setOf("srv", "ext"), light = setOf("ext"), card = BoardCard("An extension", "An add-on for Postgres, like a plug-in for your browser.")),
                    Beat(10, show = setOf("allow"), light = setOf("allow"), pulse = "srv" to "allow", card = extensionCard),
                    Beat(11, light = setOf("allow"), card = allowCard),
                ),
            ),
            // 12: "A normal query, with a distance operator instead of equals..."
            Scene(
                12, "Three ways to measure", operators(),
                beats = listOf(
                    Beat(12, show = setOf("q", "l2", "cos", "ip"), light = setOf("q"), card = BoardCard("A distance operator", "A little arrow between the column and the question's numbers.")),
                    Beat(14, light = setOf("l2"), pulse = "q" to "l2", card = rulerCard),
                    Beat(16, light = setOf("cos"), pulse = "q" to "cos", card = arrowsCard),
                    Beat(18, light = setOf("ip"), pulse = "q" to "ip", card = innerCard),
                ),
            ),
            // 20: "Working, and slow. Four hundred thousand dishes..."
            Scene(
                20, "Every row, exactly", indexes(),
                beats = listOf(
                    Beat(20, show = setOf("scan"), light = setOf("scan"), card = scanCard),
                    Beat(22, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = indexCard),
                    Beat(24, light = setOf("hnsw"), card = recallCard),
                ),
            ),
            // 26: "Two come with pgvector. HNSW, which in plain words is a web of dishes..."
            Scene(
                26, "HNSW: a web in layers", graphWeb(),
                beats = listOf(
                    Beat(26, show = setOf("start", "a", "b", "best"), light = setOf("start", "a"), pulse = "start" to "a", card = hnswCard),
                    Beat(28, show = setOf("far", "q"), light = setOf("best"), pulse = "b" to "best", card = BoardCard("What it costs", "Slow build, more memory, best speed against recall, no training.")),
                ),
            ),
            // 30: "IVFFlat, which means the rows are sorted into groups first..."
            Scene(
                30, "IVFFlat: shelves by subject", indexes(),
                beats = listOf(
                    Beat(30, show = setOf("scan", "ivf"), light = setOf("ivf"), pulse = "scan" to "ivf", card = ivfCard),
                    Beat(32, light = setOf("ivf"), card = BoardCard("Load first, then build", "Its shelves are worked out from the rows already there.")),
                    Beat(34, show = setOf("hnsw", "efs", "probes"), light = setOf("efs", "probes"), pulse = "hnsw" to "efs", card = knobsCard),
                ),
            ),
            // 35: "And where does all this stop working?"
            Scene(
                35, "Where it stops", limits("Cosmos DB night", "the same idea"),
                beats = listOf(
                    Beat(36, show = setOf("miss", "disk"), light = setOf("disk"), card = BoardCard("DiskANN", "Azure adds pg diskann: the same web idea, for many millions of rows.")),
                    Beat(38, show = setOf("mem", "link"), light = setOf("mem"), card = BoardCard("Keep it in memory", "Memory optimized for big sets. Never burstable in production.")),
                ),
            ),
            // 39: "And the poor tired diner gets what?"
            Scene(
                39, "The answer", filter(),
                beats = listOf(
                    Beat(40, show = setOf("user", "api", "where", "pg", "chat"), light = setOf("pg", "chat"), pulse = "pg" to "chat", card = ragCard),
                ),
            ),
            // 41: "Then the question I have been saving. Cosmos or Postgres?"
            Scene(
                41, "Cosmos or Postgres", choose(),
                beats = listOf(
                    Beat(42, show = setOf("cosmos", "pg", "global", "join"), light = setOf("cosmos", "global"), pulse = "cosmos" to "global", card = chooseCard),
                ),
            ),
            // 43: "Mm. Before I give in. What do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("ext", "seq", "hnsw"), card = BoardCard("Listen for the phrase", "Refused install, a sequential scan, an empty table: one answer each.")),
                    Beat(46, light = setOf("ivf", "recall", "city"), card = BoardCard("And three more", "IVFFlat, raise ef search, and the filter in the WHERE clause.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-pgvector--couples",
        listOf(
            // 0: "Tonight is date night, darling, and I have brought a database to the table..."
            Scene(
                0, "Date night, with a database", choose(),
                beats = listOf(
                    Beat(0, show = setOf("pg"), light = setOf("pg"), card = BoardCard("Date night", "The same search again, in PostgreSQL, beside the payouts ledger.")),
                    Beat(2, show = setOf("join"), light = setOf("join"), pulse = "pg" to "join", card = BoardCard("Why here", "These notes are joined to the ledger rows we already query.")),
                ),
            ),
            // 3: "A list of numbers for every dish. Picture a town map..."
            Scene(
                3, "A spot on a map", mapSpot(),
                beats = listOf(
                    Beat(3, show = setOf("mid", "p"), light = setOf("p"), pulse = "mid" to "p", card = vectorCard),
                    Beat(4, show = setOf("r"), light = setOf("r"), card = embeddingCard),
                ),
            ),
            // 5: "Then my turn. Postgres holds no such column..."
            Scene(
                5, "Quiz: the refused install", setup(),
                beats = listOf(
                    Beat(6, show = setOf("srv", "ext"), light = setOf("ext"), card = BoardCard("An extension", "An add-on for Postgres: pgvector adds the vector column type.")),
                    Beat(7, show = setOf("allow"), light = setOf("allow"), card = BoardCard("Quiz: refused", "Create extension pgvector on a new server. Name two things wrong.")),
                    Beat(8, light = setOf("allow"), pulse = "srv" to "allow", card = extensionCard),
                    Beat(9, light = setOf("ext"), card = allowCard),
                ),
            ),
            // 10: "With a distance operator, a little arrow..."
            Scene(
                10, "Three operators", operators(),
                beats = listOf(
                    Beat(10, show = setOf("q", "l2", "cos", "ip"), light = setOf("q"), card = BoardCard("A distance operator", "A little arrow between the column and the question's numbers.")),
                    Beat(11, light = setOf("l2"), pulse = "q" to "l2", card = rulerCard),
                    Beat(13, light = setOf("cos"), pulse = "q" to "cos", card = arrowsCard),
                    Beat(15, light = setOf("ip"), pulse = "q" to "ip", card = innerCard),
                ),
            ),
            // 16: "Lovely. Now something harder. Four hundred thousand dishes..."
            Scene(
                16, "Quiz: nine hundred ms", indexes(),
                beats = listOf(
                    Beat(16, show = setOf("scan"), light = setOf("scan"), card = BoardCard("Quiz: nine hundred ms", "400,000 dishes and no help. What does explain analyze say, and why?")),
                    Beat(17, light = setOf("scan"), card = scanCard),
                    Beat(19, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = indexCard),
                    Beat(21, light = setOf("hnsw"), card = BoardCard("Approximate", "Very close, not guaranteed the closest. So you measure what it misses.")),
                    Beat(23, light = setOf("scan"), card = recallCard),
                ),
            ),
            // 24: "Mm, good. Now the real exam question of the evening..."
            Scene(
                24, "Quiz: the empty table", indexes(),
                beats = listOf(
                    Beat(24, show = setOf("scan", "ivf"), light = setOf("ivf"), card = BoardCard("Quiz: an empty table", "Filled overnight, and high recall matters. Which index, and why?")),
                    Beat(26, light = setOf("ivf"), pulse = "scan" to "ivf", card = ivfCard),
                    Beat(28, show = setOf("hnsw"), light = setOf("hnsw"), pulse = "scan" to "hnsw", card = hnswCard),
                    Beat(31, show = setOf("efs", "probes"), light = setOf("efs", "probes"), pulse = "hnsw" to "efs", card = knobsCard),
                    Beat(33, light = setOf("scan"), card = BoardCard("DiskANN", "Azure adds pg diskann: the same web idea, for many millions of rows.")),
                ),
            ),
            // 34: "One more trap, and it catches everybody..."
            Scene(
                34, "Quiz: still a Seq Scan", match(),
                beats = listOf(
                    Beat(34, show = setOf("idx", "qry", "seq", "iscan"), light = setOf("seq"), card = BoardCard("Quiz: still a Seq Scan", "The index is there and every row is still read. What went wrong?")),
                    Beat(35, light = setOf("idx", "qry"), pulse = "idx" to "qry", card = mismatchCard),
                ),
            ),
            // 36: "Right answer, darling. And an answer that quoted a restaurant in another city?"
            Scene(
                36, "Quiz: another city", filter(),
                beats = listOf(
                    Beat(36, show = setOf("user", "api", "pg"), light = setOf("pg"), pulse = "api" to "pg", card = BoardCard("Quiz: another city", "The answer quoted a restaurant in another city. What was missing?")),
                    Beat(37, show = setOf("where"), light = setOf("where"), pulse = "api" to "where", card = filterCard),
                    Beat(38, show = setOf("chat"), light = setOf("chat"), pulse = "pg" to "chat", card = ragCard),
                ),
            ),
            // 39: "Then the question we have been circling all evening. Cosmos, or Postgres?"
            Scene(
                39, "Cosmos or Postgres", choose(),
                beats = listOf(
                    Beat(40, show = setOf("cosmos", "pg", "global", "join"), light = setOf("cosmos", "global"), pulse = "cosmos" to "global", card = chooseCard),
                ),
            ),
            // 41: "Like the ledger. So what do we take to the exam, love?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("ext", "seq", "hnsw"), card = BoardCard("Listen for the phrase", "Refused install, a sequential scan, an empty table: one answer each.")),
                    Beat(44, light = setOf("ivf", "recall", "city"), card = BoardCard("And three more", "IVFFlat, raise ef search, and the filter in the WHERE clause.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
