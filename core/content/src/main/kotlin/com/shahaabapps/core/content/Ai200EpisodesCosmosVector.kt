package com.shahaabapps.core.content

/** Vector search in Cosmos DB: embeddings and semantic retrieval: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosVector {
    private val classic = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Hi, and welcome back. Today: finding things by meaning. Say you're building the Two Tracks Coach, a study assistant in an Android app. A learner asks, why is my app being told to slow down? And their best note says, when the database gets busy, let it grow by itself. Hardly a word in common."),
            leo("So a search on their words finds nothing. [pause] How could anything find it?"),
            maya("By turning meaning into numbers. Keep one picture all episode: a town map. From the middle, walk 3 steps east and 4 steps north. Your spot is two numbers, 3 and 4. A list of numbers like that is called a vector: a spot on the map."),
            leo("So my spot is the vector 3, 4. And a dimension?"),
            maya("A dimension means one number in the list. A map spot has two. Ours have 1,536, so 1,536 dimensions. Nobody can picture that, and you don't need to: the maths works just as on paper."),
            leo("And who writes 1,536 numbers? Not me, I hope."),
            maya("[chuckle] No. An embedding model, an AI whose only job is this. The list it writes for a piece of text is called an embedding: that text's spot on a huge map. Texts that mean similar things get nearby spots, so the question and that note land close together."),
            leo("Where does the Coach get that model?"),
            maya("From Azure OpenAI, Microsoft's home for OpenAI's models, called by the Coach's Python backend. One rule people trip on: the same model for the stored notes and for the question. Each model draws its own map. Mix two, and the results are nonsense."),
            leo("Like using a map of London to get round Paris. Same paper, wrong city."),
            maya("Lovely. Finding notes by their nearby spots is called vector search, and we'll do it in Azure Cosmos DB, Microsoft's cloud database. First you enable vector search on the account, the top-level home for your databases. It can take fifteen minutes."),
            leo("Then I store notes in a container. Which is, what, a table?"),
            maya("Pretty much, a box of documents. And here's the trap: the vector settings go on when you create the container. You can't add them later."),
            leo("So I can't just switch it on for the notes container my app already uses?"),
            maya("No. You make a new one and copy the notes across. The first setting is the vector embedding policy: which field holds the numbers, what kind of number, how many, 1,536 to match the model, and the distance function."),
            leo("Distance function. That sounds like maths homework."),
            maya("It's just the rule for how close two spots are. There are three, and we'll take them one at a time. The default is cosine. Picture an arrow from the middle of the map out to each spot."),
            leo("Two arrows. Then what?"),
            maya("Cosine asks: do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite ways minus 1. So higher means more similar. Arrows to 3, 4 and to 6, 8 point the same way, so they score 1, though one is twice as long."),
            leo("Got it. The second?"),
            maya("Dot product. In plain words: multiply the matching numbers, then add them up. Take 2, 3 and 4, 1. Two times four is eight, three times one is three, and eight plus three is eleven. A bigger answer means the lists agree more, so higher is closer again."),
            leo("Multiply and add. Even I can do that. And the third?"),
            maya("Euclidean. Picture a ruler laid between two pins on the map. From the middle to the spot 3, 4, the ruler reads 5. It's a real distance, so this time lower means closer."),
            leo("Higher is closer, except when lower is closer. [sigh] Mostly got it. What's the second setting?"),
            maya("The indexing policy. An index is like the one at the back of a book: the database jumps to the right page instead of reading everything. By default, Cosmos DB indexes every field you save."),
            leo("Every field? Including my 1,536 numbers?"),
            maya("That's rule one: leave the vector field out of the normal index, or every new note pays to index all of them. Rule two: add a vector index, built for finding the nearest spots."),
            leo("Let me guess. There are three kinds."),
            maya("Three, one at a time. The first is called flat. Picture measuring the distance to every single café in the city, then picking the closest. Flat compares the question with every stored note. It never misses, but with many notes it's slow and costly, and it only takes lists of up to 505 numbers."),
            leo("And ours have 1,536. So flat is out straight away."),
            maya("Straight away. The second is quantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo. It still compares with every note, losing only a little accuracy. It suits up to about 50,000 notes in scope, meaning the ones a search actually looks at, often after a filter."),
            leo("And the Coach?"),
            maya("300,000 notes, but every search is filtered to one learner, so only a few hundred are in scope. QuantizedFlat. For example, two million public study cards searched with no filter would need the third kind: diskANN."),
            leo("DiskANN. What's that, in plain words?"),
            maya("It links the notes into a web. Each node, meaning one stored note, is joined to a few of its near neighbours. Picture dots on a map joined by lines, like a chart of who knows whom. That web is called a graph."),
            leo("And a search walks the web?"),
            maya("It hops. Start at a dot, look at its neighbours, jump to whichever is closer to the question, and repeat. Like finding someone through friends of friends, instead of knocking on every door. Its answer is the nearest neighbour, the stored note closest to the question."),
            leo("Guaranteed the very closest?"),
            maya("Not quite. ANN stands for approximate nearest neighbour: very close, not guaranteed perfect. That's the trade for speed. Past about 50,000 notes in scope, it's the fastest and cheapest of the three."),
            leo("Okay, container ready. How do I search?"),
            maya("Picture the moment in your app. The learner types a question, the phone sends it to your backend, and the backend turns it into an embedding with the same model. Then it asks Cosmos DB with a query in SQL, the language for asking a database questions."),
            leo("And the query says, what, find me the close ones?"),
            maya("A function called VectorDistance gives each note a closeness score against the question. You sort by it, which SQL calls ORDER BY, keep the best five, TOP 5, and add a WHERE, SQL's filter, on the learner's id."),
            leo("Why filter by learner?"),
            maya("The Coach keeps notes in drawers by learner, and each drawer is called a partition. So the search opens just one drawer. Cheap."),
            leo("And why TOP 5? Why not everything, ranked?"),
            maya("Cosmos DB bills in RUs, request units: the price tag on every call. Without TOP N, meaning the top few, the engine scores far more notes than you need, and the RU charge climbs. Good matches but too many RUs is a classic exam question. TOP N is the answer."),
            leo("And then the learner sees five notes?"),
            maya("Usually it goes further, with RAG, short for retrieval augmented generation. The backend retrieves the closest notes and pastes their text into the prompt, the message it sends to a chat model."),
            leo("The chat model being the AI that writes the reply?"),
            maya("Yes. And the prompt says: answer only from these notes. So the app can show which notes it used."),
            leo("So the chat model isn't making things up. It reads my own notes back to me, nicely."),
            maya("[laugh] Nicely put."),
            leo("So what do I remember for the exam?"),
            maya("Five things. 1,536 dimensions with a flat index is the trap: flat stops at 505. Far more than 50,000 vectors in scope: diskANN. If a filter leaves a small set, quantizedFlat. If a vector query costs too many RUs, add TOP N. And if the results are nonsense, use the same embedding model for both."),
            leo("And anything to try today?"),
            maya("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five notes on different topics into embeddings, and upsert them, meaning save them. Then ask a question in different words from any note, and see which note ranks first."),
            leo("A search that understands me. Better than most people. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Welcome to Case Files. Today's case: The Answer About Pancakes. A small Android app called Pocket Tutor finds a student's own notes that answer their question. On launch day, a student asks, why is my app being told to slow down? And the app proudly shows a note about pancake recipes."),
            leo("[chuckle] Pancakes. Detective Leo is hungry, and on the case. How was it meant to find the right note?"),
            maya("By meaning, not by words. The right note said, when the database gets busy, let it grow by itself. Hardly a shared word. So each note is turned into numbers. Picture a town map: 3 steps east and 4 steps north is a spot you can write as 3, 4. A list of numbers like that is called a vector."),
            leo("A spot on a map. Fine. But a note isn't a map."),
            maya("No, but an AI model can give it a spot on a huge one. That list is called an embedding, and texts that mean similar things get nearby spots."),
            leo("How huge? Two numbers, like the town?"),
            maya("1,536 numbers per note, so 1,536 dimensions, where a dimension means one number in the list. A town map has two."),
            leo("1,536 directions. I can't picture that."),
            maya("Nobody can, and you don't need to: the maths works just as on paper. The notes and their vectors live together in Azure Cosmos DB, Microsoft's cloud database, and the search asks for the notes nearest the question. That's vector search."),
            leo("Then my first suspect is the distance function, the rule for what counts as near. They picked the wrong one, I bet."),
            maya("[pause] Good instinct, wrong suspect, but let's question all three, one at a time. Cosine, the default. Picture arrows from the middle of the map to two spots. Do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher means more similar."),
            leo("So arrows to 3, 4 and 6, 8, same direction, score 1?"),
            maya("Exactly, though one arrow is twice as long. Second, dot product. In plain words: multiply the matching numbers and add them up. Take 2, 3 and 4, 1: two times four, plus three times one, is eleven. A bigger answer means the lists agree more."),
            leo("And the third suspect's euclidean."),
            maya("Picture a ruler laid between two pins on the map. From the middle to 3, 4, it reads 5, so lower is closer. Any of the three works. So the suspect isn't the rule."),
            leo("Then what is it? The notes and the question are both just lists of numbers."),
            maya("Here's the clue. The notes were embedded last month with one embedding model. On launch day, the backend switched to a newer model for the questions."),
            leo("So the question and the notes are on different maps."),
            maya("Exactly. Each model draws its own map, often with a different number of dimensions. Spots from two maps don't compare. The same model for notes and questions, always, and if you change models, embed every note again."),
            leo("Fix the model, pancakes gone. Case closed?"),
            maya("Not quite. Second witness: the finance team. The bill tripled in week one. Every search cost far more RUs, request units, the price tag Cosmos DB puts on every call."),
            leo("Suspect: there's no index. An index makes queries cheap, like the one at the back of a book."),
            maya("Close, but look at the query, written in SQL, the language for asking a database questions. It sorts the notes by VectorDistance, a function that scores how near each note is to the question. SQL calls sorting ORDER BY. What's missing?"),
            leo("[pause] A limit? It ranks every note, and the app only shows five."),
            maya("Detective Leo, finally. TOP N means only the top few. Without it, the engine scores far more notes than you need, and the RU charge climbs. So, TOP 5."),
            leo("And the student's id? The app already knows whose phone it is."),
            maya("So the backend adds a WHERE, SQL's filter, on that id. Notes sit in drawers by student, each drawer called a partition, so the search opens one drawer instead of every one."),
            leo("And the writes? Finance said those were pricey too."),
            maya("Third clue. The vector field was still in the indexing policy, the list of what goes in the normal index. So every insert paid to index 1,536 numbers one by one. Leave it out, and give it a vector index instead, one built for finding the nearest spots. The Cosmos performance episode covers indexing and RUs in depth."),
            leo("Easy. Edit the existing container, add the vector settings, done by lunch."),
            maya("That's how you open the next case. A container is Cosmos DB's box of documents, and its vector settings go on when you create it, never later."),
            leo("So they had to build a new container and copy every note in? [pause] What did it need?"),
            maya("First, vector search switched on for the account, which can take fifteen minutes. Then a vector embedding policy: the kind of number, 1,536 of them, and cosine."),
            leo("Which vector index, then? I'll say flat. It sounds simple."),
            maya("[sigh] And a fourth case opens. Picture measuring the distance to every café in the city, then picking the closest. That's flat: it compares the question with every note. Never misses, slow with many notes, and it stops at lists of 505 numbers. 1,536 doesn't fit. The exam's favourite trap."),
            leo("Fine. What did they pick?"),
            maya("QuantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo, then still compared with every note. It suits up to about 50,000 in scope, the notes one search looks at. Pocket Tutor holds 300,000, but each search is filtered to one student: a few hundred in scope."),
            leo("And when is diskANN the answer?"),
            maya("Far more than 50,000 in scope, say two million public cards with no filter. It links the notes into a graph, meaning a web of dots joined by lines. Each node, one stored note, is joined to its near neighbours, and a search hops from dot to closer dot, like friends of friends instead of knocking on every door."),
            leo("So it doesn't check every note. Can it miss?"),
            maya("Slightly. ANN stands for approximate nearest neighbour. The nearest neighbour is the stored note closest to the question, and approximate means very close, not guaranteed perfect. At that size, it's the fastest and cheapest."),
            leo("And the student sees five notes. That's it?"),
            maya("Then comes RAG, short for retrieval augmented generation. Retrieve the top notes, add them to the prompt, the message sent to a chat model, an AI that writes replies. It answers only from them, and the app can show which notes it used."),
            leo("Any loose ends in the file?"),
            maya("One, where vector search stops working. It always returns the nearest notes, even when nothing is about the question. Ask about tax returns, and you still get five. Nearest isn't relevant. So check the scores, and let the chat model say it doesn't know. A changed note is embedded again; the change feed episode shows how. And the pgvector episode does all this on PostgreSQL."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If the results are nonsense, the question and the notes need the same embedding model. If a vector query costs too many RUs, add TOP N. If the question says 1,536 dimensions with a flat index, that's the trap: flat stops at 505. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. And a vector policy needs a new container."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, without the pancakes. Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five notes on different topics into embeddings, and upsert them, meaning save them. Then ask a question in different words from any note, and see which note ranks first."),
            leo("Pancakes for breakfast, slow-downs after lunch. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They shipped smart search for the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. And let's just say, it was a week."),
            leo("[laugh] I heard there was a meeting with sourdough in the title. What were they building?"),
            maya("A search that finds a learner's notes by meaning, not words. Ask, why is my app being told to slow down, and it finds the note that says, when the database gets busy, let it grow by itself. No shared words, same meaning."),
            leo("How does a database know what anything means?"),
            maya("It doesn't, really. It compares numbers. Picture a town map: 3 steps east and 4 steps north is a spot you can write as 3, 4. That list is called a vector."),
            leo("Okay, a spot on a map. But a note about databases isn't on any map."),
            maya("It is once an AI model gives it one, on a huge map. That list of numbers is called an embedding. Similar meanings, nearby spots."),
            leo("A huge map. How huge?"),
            maya("1,536 numbers per note, so 1,536 dimensions. A dimension means one number in the list; a town map has two. Nobody can picture 1,536, but the maths is the same. The notes and their vectors live in Cosmos DB, Microsoft's cloud database, and vector search asks for the nearest spots."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday morning. Tom had last year's notes container. A container is Cosmos DB's box of documents, like a table. He thought he'd just switch vector search on for it. Done by coffee."),
            leo("And?"),
            maya("And you can't. Vector settings go on a new container, when you create it. And the account, the top-level home for your databases, needs vector search switched on first, which can take fifteen minutes. Tom spent his Monday copying notes into a new container."),
            leo("[sigh] Poor Tom. What settings did it need?"),
            maya("A vector embedding policy, a short description of the vectors: the kind of number, how many, 1,536 to match the model, and the distance function, the rule for how near two spots are. There are three rules, and Tom had to learn all three before lunch."),
            leo("Go on then, one at a time."),
            maya("Cosine, the default. Picture arrows from the middle of the map to two spots. Do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher means more similar."),
            leo("And the second?"),
            maya("Dot product. In plain words: multiply the matching numbers, then add them up. Take 2, 3 and 4, 1: two times four is eight, three times one is three, eleven in all. A bigger answer means the lists agree more."),
            leo("And the third?"),
            maya("Euclidean. Picture a ruler laid between two pins on the map. From the middle to 3, 4, it reads 5. A real distance, so this one's backwards: lower means closer."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. Dana loaded the notes, and every insert cost far more RUs. RUs, request units, are the price tag Cosmos DB puts on every call."),
            leo("Why? It's just a note."),
            maya("A note with 1,536 numbers on it. An index is like the one at the back of a book, and by default Cosmos DB indexes every field you save."),
            leo("Oh no. Including the 1,536 numbers?"),
            maya("All of them, one by one, on every insert. Dana should have left the vector field out of the indexing policy, the list of what goes in the normal index, and given it a vector index instead."),
            leo("Which vector index? I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. Marcus picked flat. Picture measuring the distance to every single café in the city, then picking the closest. That's flat: it compares the question with every note. It never misses."),
            leo("Sounds perfect. What's the catch?"),
            maya("Slow and costly with many notes, and it only takes lists of up to 505 numbers. Marcus had 1,536. The container said no, and the whole floor heard about it."),
            leo("So what should Marcus have picked?"),
            maya("It depends on the notes in scope, the ones one search looks at. Each Coach search is filtered to one learner, so a few hundred are in scope, out of 300,000. Under about 50,000 in scope, it's quantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo, then still compared with every note."),
            leo("And the big one? Everyone keeps saying diskANN."),
            maya("For far more than 50,000 in scope, like two million public cards with no filter. It's a graph, which is a web of dots joined by lines. Each node, meaning one stored note, is joined to its near neighbours. A search hops from dot to closer dot, like friends of friends, instead of knocking on every door."),
            leo("So it might miss one?"),
            maya("Just about. ANN stands for approximate nearest neighbour. The nearest neighbour is the note closest to the question, and approximate means very close, not guaranteed perfect. Fastest and cheapest at that size."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. Great matches, and an enormous RU bill."),
            leo("What did their query look like?"),
            maya("It was in SQL, the language for asking a database questions. It sorted every note by VectorDistance, a function that scores how near each note is. SQL calls that ORDER BY. But there was no TOP."),
            leo("And TOP is what?"),
            maya("TOP N means only the top few. Without it, the engine scores far more notes than you need. TOP 5, with a WHERE, SQL's filter, on the learner's id, and it's cheap again. Notes sit in drawers by learner, each drawer called a partition, so the search opens one drawer."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She tried a newer embedding model for the questions, but the notes were embedded with the old one. And the Coach answered a question about slowing down with a note about sourdough starters."),
            leo("[laugh] The sourdough meeting. But why?"),
            maya("Each model draws its own map, often with different dimensions. Spots from two maps don't compare. The same model for notes and questions, always, and if you change models, embed every note again. The change feed episode shows how to react when notes are added or edited."),
            leo("Once it worked, what did the Coach do with its five notes?"),
            maya("RAG, which stands for retrieval augmented generation. Retrieve the top notes, add them to the prompt, the message sent to a chat model, an AI that writes replies. It answers only from them, and the app shows which notes it used."),
            leo("Did anything still go wrong?"),
            maya("Only where vector search stops working. It always returns the nearest notes, even when none is about the question. Nearest isn't relevant. So the team checks the scores, and lets the chat model say it doesn't know. And the pgvector episode does all this on PostgreSQL, if your team prefers it."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. A vector policy on an existing container? No, a new one. 1,536 dimensions with a flat index is the trap: flat stops at 505. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. Too many RUs on a vector query, add TOP N. And nonsense results mean the same embedding model for both."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five notes on different topics into embeddings, and upsert them, meaning save them. Then ask a question in different words from any note, and see which note ranks first."),
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
            nova("Only a little. A learner is revising late, like you. They type, why is my app being told to slow down? And somewhere in their notes is the perfect answer: when the database gets busy, let it grow by itself. [pause] Barely a word in common."),
            ivy("So a search for their words finds nothing. How lonely."),
            nova("Lonely. Unless we compare meaning, and for that we turn text into numbers. Close your eyes and picture a town map. Walk 3 steps east, 4 steps north. Your spot is two numbers, 3 and 4. A list of numbers like that is called a vector. [pause] A spot on the map."),
            ivy("A spot on the map. I can see it. And a dimension?"),
            nova("A dimension means one number in the list. A map spot has two. The lists tonight have 1,536. Nobody can picture that many directions, not even at this hour. You don't need to. The maths works just the same."),
            ivy("And who writes 1,536 numbers for a note?"),
            nova("An embedding model, an AI whose only job is that. The list it writes is called an embedding: the spot a text gets on its huge map. Texts with similar meaning land close together, [pause] even if they never share a word."),
            ivy("Close together, in the dark. I like that. Where do they live?"),
            nova("Right beside the notes, in Azure Cosmos DB, Microsoft's cloud database. Take the Two Tracks Coach, the study assistant in the Android app. Each note keeps its embedding. The question gets one too, from the same model, and the database finds the nearest spots. That's vector search."),
            ivy("The same model. Is that a rule?"),
            nova("A promise. Each model draws its own map. Spots from two maps simply don't compare. It's like whispering in one language and listening in another."),
            ivy("[chuckle] I've been on dates like that. So how near is near?"),
            nova("That's the distance function: the rule for how close two spots are. There are three. Slowly, one at a time. The default is cosine. Picture an arrow from the middle of the map to each spot."),
            ivy("Two arrows, in the dark. Then?"),
            nova("Cosine asks, do they point the same way, whatever their length? Same way, 1. A right angle, 0. Opposite ways, minus 1. Higher means more similar. Arrows to 3, 4 and to 6, 8 point the same way, so they score 1. [pause] Length doesn't matter. Only direction."),
            ivy("Only direction. Romantic. The second?"),
            nova("Dot product. In plain words, multiply the matching numbers, then add them up. Take 2, 3 and 4, 1. Two times four is eight. Three times one is three. Eight plus three, eleven. A bigger answer means the lists agree more."),
            ivy("Multiply and add. Even at this hour. And the third?"),
            nova("Euclidean. Picture a ruler laid between two pins on the map. From the middle to 3, 4, the ruler reads 5. A real distance, so this time, lower means closer."),
            ivy("And Cosmos DB? Does it just do this, out of the box?"),
            nova("Not quite. First you enable vector search on the account, the top-level home for your databases. It can take fifteen minutes. [pause] Then the vector settings go on a new container, Cosmos DB's box of documents, when you create it. Never an existing one."),
            ivy("No changing your mind later. Strict. What settings?"),
            nova("A vector embedding policy: the kind of number, how many, 1,536 to match the model, and the distance function. Then the indexing policy. An index is like the one at the back of a book, and by default every field goes in it."),
            ivy("Every field. Even all 1,536 of my numbers?"),
            nova("Even those, on every insert, [pause] unless you leave the vector field out of the normal index. Then you give it a vector index instead."),
            ivy("And there's more than one kind. I can hear it."),
            nova("Three, and the exam loves them. The first is flat. Picture measuring the distance to every single café in the city, then picking the closest. Flat compares the question with every note. It never misses. But it's slow with many notes, and it stops at lists of 505 numbers."),
            ivy("And ours have 1,536. Mm. The second?"),
            nova("QuantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo. Then it still compares with every note, a little less exactly. [pause] Good for up to about 50,000 notes in scope, the ones a search actually looks at, often after a filter."),
            ivy("And the third, diskANN. The one everyone whispers about."),
            nova("It's for far more than 50,000 in scope. It links the notes into a graph, which is a web of dots joined by lines. Each node, meaning one stored note, holds hands with its near neighbours. A search hops from dot to closer dot, like friends of friends, instead of knocking on every door."),
            ivy("Holding hands. You're making indexes romantic. Does it ever miss?"),
            nova("[pause] A little. ANN stands for approximate nearest neighbour. The nearest neighbour is the note closest to the question; approximate means very close, not guaranteed perfect. At that size, it's the fastest and cheapest."),
            ivy("So which one does the Coach pick? It has a lot of notes."),
            nova("300,000. But each search is filtered to one learner, so only a few hundred are in scope. [pause] QuantizedFlat."),
            ivy("And the search itself? What do I write?"),
            nova("Picture the learner's phone, sending the question to the backend. The backend embeds it with the same model, then asks Cosmos DB with a query in SQL, the language for asking a database questions. VectorDistance is a function that scores how near each note is."),
            ivy("And then?"),
            nova("Sort by it, ORDER BY. Keep the best five, TOP 5. [pause] And add a WHERE, SQL's filter, on the learner's id. Notes sit in drawers by learner, each called a partition, so the search opens one."),
            ivy("Why only five?"),
            nova("Because without TOP N, the top few, the engine scores far more notes than you need, and the RU charge climbs. RUs, request units, are the price tag Cosmos DB puts on every call."),
            ivy("Five notes, then. What does the Coach do with them?"),
            nova("RAG, which stands for retrieval augmented generation. It retrieves those notes, adds their text to the prompt, the message sent to a chat model, an AI that writes replies, and asks it to answer only from them. So the answer rests on your own notes."),
            ivy("Now the part I've been waiting for. Where does it stop working?"),
            nova("Here. [pause] It always brings back the nearest notes, even when none is about the question. Nearest isn't relevant. So look at the scores, and let the chat model say, I don't know. When a note changes, embed it again; the change feed episode shows how. And the pgvector episode is this same idea in PostgreSQL."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. If it's 1,536 dimensions with a flat index, that's the trap: flat stops at 505. If far more than 50,000 vectors are in scope, diskANN. If a filter leaves a small set, quantizedFlat. If a vector query costs too many RUs, add TOP N. And if the results are nonsense, use the same embedding model for notes and questions."),
            ivy("And something to try today, before I sleep?"),
            nova("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five notes on different topics into embeddings, and upsert them, meaning save them. [pause] Then ask a question in different words from any note, and see which note ranks first."),
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
            nova("[laugh] Search by meaning, in Cosmos DB, Microsoft's cloud database. All about finding what's close. A learner asks the Two Tracks Coach, our study app, why is my app being told to slow down? The perfect note says, when the database gets busy, let it grow. Hardly a word in common."),
            rafe("But a perfect match. How does a database see that?"),
            nova("With numbers. Picture a town map. Three steps east, four steps north, and your spot is 3, 4. A list of numbers like that is a vector. And a dimension is one number in it, so a map spot has two dimensions."),
            rafe("And how many dimensions do you have, darling?"),
            nova("For notes, 1,536. An embedding model, an AI, writes that many numbers for a text, and the list is called an embedding: the text's spot on a huge map. Similar meanings, nearby spots. Nobody can picture 1,536 directions, but the maths is the same."),
            rafe("I always knew you were complicated. So two notes that mean the same sit close?"),
            nova("A small distance apart. Under the picture, each number is one coordinate. The question gets a spot too, and the database finds the nearest notes. That's vector search. The model comes from Azure OpenAI, Microsoft's service for OpenAI's models."),
            rafe("A small distance apart. Exactly where I'd like to be. Exam style, darling: notes embedded with one model, questions with another. Nonsense results. Why?"),
            nova("Each model draws its own map. Spots from two maps don't compare. The same model for notes and questions, always."),
            rafe("Right answer. Two people have to be on the same map. [kiss]"),
            nova("Mm. My turn. How close are two spots? That's the distance function, and there are three. Tell me the default first."),
            rafe("Cosine. Picture an arrow from the middle of the map to each spot. Do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher is more similar."),
            nova("Right answer. It's about which way you're facing, Rafe, not how big you are. [kiss]"),
            rafe("[chuckle] I'll take that as a compliment. The second is dot product. Multiply me out."),
            nova("In plain words: multiply the matching numbers, then add them up. Take 2, 3 and 4, 1: two times four is eight, three times one is three, eleven in all. A bigger answer means the lists agree more."),
            rafe("We agree on plenty. And the third, euclidean?"),
            nova("Picture a ruler laid between two pins on the map. From the middle to 3, 4, it reads 5. A real distance, so lower means closer. The one time smaller is better."),
            rafe("Noted. Can I switch this on for any container I like?"),
            nova("Oh, no. First you enable vector search on the account, the top-level home for your databases. Then the vector settings go on a new container, Cosmos DB's box of documents, when you create it. Never an existing one. Commitment from the start."),
            rafe("Committed from day one. What settings?"),
            nova("A vector embedding policy: the kind of number, how many, 1,536 to match the model, and the distance function. Then the indexing policy. An index is like the one at the back of a book. Quiz: every insert is suddenly very expensive. What did we forget?"),
            rafe("We left the vector field in the normal index, so every insert indexes 1,536 numbers. Leave it out, and give it a vector index instead, one built for finding the nearest spots."),
            nova("Right answer. Some things shouldn't be indexed in public. [kiss]"),
            rafe("Now the vector indexes. Tell me slowly."),
            nova("The first is flat. Picture measuring the distance to every single café in the city, then picking the closest. Flat compares the question with every note. Never misses, but slow with many notes, and only lists of up to 505 numbers."),
            rafe("Leaning over every fence in town. And the second?"),
            nova("QuantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo, then still compared with every note. Up to about 50,000 notes in scope, meaning the ones a search actually looks at."),
            rafe("And the third, diskANN?"),
            nova("For far more than 50,000 in scope. It's a graph, which is a web of dots joined by lines. Each node, meaning one stored note, is joined to its near neighbours. A search hops from dot to closer dot, like friends of friends, not knocking on every door."),
            rafe("Friends of friends. How we met. Does it find the very closest?"),
            nova("Almost. ANN stands for approximate nearest neighbour. The nearest neighbour is the note closest to the question; approximate means very close, not guaranteed perfect. The fastest at that size."),
            rafe("My question, then. Notes with 1,536 dimensions, and someone picks flat. Good idea?"),
            nova("No. Flat stops at 505, and 1,536 doesn't fit. The exam's favourite trap."),
            rafe("Right answer, and a dangerous one to get wrong. [kiss]"),
            nova("My turn. The Coach holds 300,000 notes, but each search is filtered to one learner, so a few hundred are in scope. Which index?"),
            rafe("DiskANN. It's the biggest, so it's the best."),
            nova("[sigh] No kiss for that one. Under 50,000 in scope means quantizedFlat. DiskANN is for far more, like two million public cards with no filter."),
            rafe("Bigger isn't always better. Noted. How do I search?"),
            nova("Your phone sends the question to the backend, the backend embeds it, then asks Cosmos DB in SQL, the language for asking a database questions. VectorDistance is a function that scores how near each note is."),
            rafe("Then I sort by that score, and take the best?"),
            nova("ORDER BY it, keep the best five with TOP 5, and add a WHERE, SQL's filter, on the learner's id, so it opens one drawer of notes, called a partition."),
            rafe("And what does it cost me?"),
            nova("RUs, request units, are the price tag Cosmos DB puts on every call. So, darling. A vector query gives good matches, but far too many RUs. What's missing?"),
            rafe("TOP N, only the top few. Without it the engine scores far more notes than you need. I only want my top five. Well. My top one."),
            nova("Right answer, and charming with it. [kiss]"),
            rafe("My question. The Coach must answer only from the learner's notes, and show which it used. The pattern?"),
            nova("RAG, which stands for retrieval augmented generation. Retrieve the nearest notes, put their text in the prompt, the message sent to a chat model, an AI that writes replies, and ask it to answer only from them."),
            rafe("Right answer. Grounded, like a good relationship. [kiss]"),
            nova("And where does it stop working, Rafe? Every romance has its limits."),
            rafe("It always brings back the nearest notes, even when none is about the question. Nearest isn't always the one. So check the scores, and let the chat model say it doesn't know. A changed note gets embedded again; the change feed episode shows how, and the pgvector episode does it all on PostgreSQL."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("If the results are nonsense, the same embedding model for both. If a vector query costs too many RUs, add TOP N. If it's 1,536 dimensions with flat, that's the trap. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. And a vector policy needs a new container."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five notes on different topics into embeddings, and upsert them, meaning save them. Then ask a question in different words from any note, and see which note ranks first."),
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

    private fun mapPicture() = DiagramSpec(
        "On a town map, 3 steps east and 4 north is the vector (3, 4); an embedding is a text's spot on a far bigger map.",
        nodes = listOf(
            DNode("mid", "Middle", 0.2f, 0.8f, w = 0.28f, h = 0.16f, kind = NodeKind.PILL, sub = "(0, 0)"),
            DNode("a", "Question", 0.4f, 0.2f, w = 0.34f, h = 0.2f, tone = 1, sub = "(3, 4)"),
            DNode("b", "Note", 0.8f, 0.44f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, sub = "(4, 3): nearby"),
            DNode("dims", "1,536 dimensions", 0.66f, 0.8f, w = 0.56f, h = 0.16f, tone = 2, sub = "one number each"),
        ),
        edges = listOf(
            DEdge("mid", "a", "3 east, 4 north"),
            DEdge("mid", "b"),
            DEdge("a", "b", "close", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun web() = DiagramSpec(
        "diskANN: each node is one stored note joined to its near neighbours, and a search hops toward the question.",
        nodes = listOf(
            DNode("q", "Question", 0.22f, 0.15f, w = 0.36f, h = 0.16f, tone = 1),
            DNode("a", "Note", 0.75f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE, sub = "start here"),
            DNode("b", "Note", 0.5f, 0.47f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE, sub = "a neighbour"),
            DNode("c", "Note", 0.2f, 0.8f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE),
            DNode("d", "Nearest note", 0.75f, 0.8f, w = 0.4f, h = 0.16f, kind = NodeKind.NOTE, sub = "the answer"),
        ),
        edges = listOf(
            DEdge("q", "a", "start", dashed = true),
            DEdge("a", "b", "hop"),
            DEdge("b", "d", "hop"),
            DEdge("b", "c"),
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
            DNode("say", "Say I don't know", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "the chat model may"),
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


    private val vectorCard = BoardCard("Vector", "3 steps east, 4 north: the vector (3, 4), a spot on the map.")
    private val dimensionCard = BoardCard("Dimension", "How many numbers in the list. A map spot has 2; these embeddings 1,536.")
    private val embeddingCard = BoardCard("Embedding", "The spot an AI model gives a text. Similar meaning, nearby spots.")
    private val cosineCard = BoardCard("cosine (default)", "Arrows from the middle: same way 1, right angle 0, opposite minus 1.")
    private val dotCard = BoardCard("dot product", "(2, 3) and (4, 1): 2 times 4 plus 3 times 1 is 11. Bigger agrees more.")
    private val euclideanCard = BoardCard("euclidean", "A ruler between two pins: middle to (3, 4) reads 5. Lower is closer.")
    private val flatCard = BoardCard("flat", "Measure to every café in town: never misses, but only up to 505 numbers.")
    private val quantizedCard = BoardCard("quantizedFlat", "Each list squeezed like a compressed photo; up to about 50,000 in scope.")
    private val graphCard = BoardCard("Node and graph", "A node is one stored note, joined to near neighbours. The web is a graph.")
    private val annCard = BoardCard("Approximate nearest neighbour", "Hop dot to closer dot: very close, not guaranteed perfect. Past 50,000.")
    private val ragCard = BoardCard("RAG", "Retrieve the closest notes, put them in the prompt, answer only from them.")
    private val todayCard = BoardCard("Today", "Enable vector search, a quantizedFlat container, five notes, one question.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-vector",
        listOf(
            // 0: "Hi, and welcome back. Today is about finding things by meaning..."
            Scene(
                0, "Meaning, not words", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q", "note"), light = setOf("q", "note"), card = BoardCard("The puzzle", "Why is my app told to slow down? The note: let the database grow.")),
                ),
            ),
            // 2: "By turning meaning into numbers. Let me give you one picture..."
            Scene(
                2, "A spot on the map", mapPicture(),
                beats = listOf(
                    Beat(2, show = setOf("mid", "a"), light = setOf("a"), pulse = "mid" to "a", card = vectorCard),
                    Beat(4, show = setOf("dims"), light = setOf("dims"), card = dimensionCard),
                    Beat(6, show = setOf("b"), light = setOf("a", "b"), pulse = "a" to "b", card = embeddingCard),
                ),
            ),
            // 8: "From Azure OpenAI, Microsoft's home for OpenAI's models..."
            Scene(
                8, "Same model, same map", coach(),
                beats = listOf(
                    Beat(8, show = setOf("user", "api", "emb"), light = setOf("emb"), pulse = "api" to "emb", card = BoardCard("Same model for both", "Each model draws its own map. Mix two, and the results are nonsense.")),
                    Beat(10, show = setOf("db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("Vector search", "Find the notes whose spots sit nearest the question's, in Cosmos DB.")),
                ),
            ),
            // 11: "Then I store notes in a container. Which is, what, a table?"
            Scene(
                11, "A new container", container(),
                beats = listOf(
                    Beat(11, show = setOf("cont"), light = setOf("acct", "cont"), card = BoardCard("A new container", "A box of documents. Vector settings go on when you create it.")),
                    Beat(12, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Vector embedding policy", "Per field: kind of number, how many to match the model, distance function.")),
                ),
            ),
            // 15: "Distance function. That sounds like maths homework."
            Scene(
                15, "Three distance functions", distance(),
                beats = listOf(
                    Beat(15, show = setOf("q"), card = BoardCard("Distance function", "The rule for how close two spots are. Three, one at a time.")),
                    Beat(16, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(20, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(22, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 24: "The indexing policy. An index is like the one at the back of a book..."
            Scene(
                24, "The indexing policy", container(),
                beats = listOf(
                    Beat(24, show = setOf("excl", "index"), light = setOf("excl", "index"), pulse = "cont" to "excl", card = BoardCard("Two rules", "Leave the vector field out of the normal index, and add a vector index.")),
                ),
            ),
            // 28: "Three, one at a time. The first is called flat..."
            Scene(
                28, "Which vector index?", indexes(),
                beats = listOf(
                    Beat(28, show = setOf("flat"), light = setOf("flat"), card = flatCard),
                    Beat(29, show = setOf("dims"), light = setOf("dims", "flat"), pulse = "dims" to "flat", card = BoardCard("1,536 dimensions", "flat is out straight away: it stops at 505.")),
                    Beat(30, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(32, show = setOf("disk"), light = setOf("qf"), card = BoardCard("The Coach: quantizedFlat", "300,000 notes, filtered to one learner: a few hundred in scope.")),
                ),
            ),
            // 33: "DiskANN. What's that, in plain words?"
            Scene(
                33, "diskANN: a web of notes", web(),
                beats = listOf(
                    Beat(34, show = setOf("a", "b", "c", "d"), light = setOf("b"), card = graphCard),
                    Beat(36, show = setOf("q"), light = setOf("b", "d"), pulse = "b" to "d", card = BoardCard("Friends of friends", "Hop to whichever neighbour is closer, instead of knocking on every door.")),
                    Beat(38, light = setOf("d"), card = annCard),
                ),
            ),
            // 39: "Okay, container ready. How do I search?"
            Scene(
                39, "The query", query(),
                beats = listOf(
                    Beat(39, show = setOf("q")),
                    Beat(42, show = setOf("rank"), light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("VectorDistance", "A closeness score for each note; sort by it and keep the top five.")),
                    Beat(43, show = setOf("filter"), light = setOf("rank", "filter"), pulse = "filter" to "rank", card = sql),
                    Beat(44, light = setOf("filter"), card = BoardCard("Partition", "A drawer of notes for one learner: the search opens just one.")),
                    Beat(46, show = setOf("top"), light = setOf("top"), pulse = "rank" to "top", card = BoardCard("TOP N", "RUs are the price tag on every call. Without TOP N, far more is scored.")),
                ),
            ),
            // 47: "And then the learner sees five notes?"
            Scene(
                47, "From retrieval to RAG", rag(),
                beats = listOf(
                    Beat(47, show = setOf("notes")),
                    Beat(48, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = ragCard),
                    Beat(50, light = setOf("notes"), card = BoardCard("Answer only from these notes", "The prompt tells the chat model to use only the retrieved notes.")),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("flat", "big", "small"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: TOP N, diskANN, quantizedFlat, one model.")),
                    Beat(56, card = todayCard),
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
                    "Pocket Tutor's question about slowing down comes back with a note about pancakes: the search found the wrong neighbour.",
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
                    Beat(0, show = setOf("phone", "api", "bad"), light = setOf("bad"), pulse = "phone" to "api", card = BoardCard("The case", "Ask why the app is told to slow down; get a note about pancakes.")),
                    Beat(1, show = setOf("db"), light = setOf("db"), pulse = "api" to "db"),
                ),
            ),
            // 2: "By meaning, not by words..."
            Scene(
                2, "A spot on the map", mapPicture(),
                beats = listOf(
                    Beat(2, show = setOf("mid", "a"), light = setOf("a"), pulse = "mid" to "a", card = vectorCard),
                    Beat(4, show = setOf("b", "dims"), light = setOf("a", "b"), pulse = "a" to "b", card = embeddingCard),
                    Beat(8, light = setOf("dims"), card = BoardCard("Under the picture", "Each number is one coordinate; 1,536 of them, the maths as on paper.")),
                ),
            ),
            // 9: "Then my first suspect is the distance function..."
            Scene(
                9, "Suspect one: distance", distance(),
                beats = listOf(
                    Beat(9, show = setOf("q"), card = BoardCard("Distance function", "The rule for what counts as near. Three suspects, one at a time.")),
                    Beat(10, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(12, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(14, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 17: "So the question and the notes are on different maps."
            Scene(
                17, "The real culprit",
                DiagramSpec(
                    "Any distance function works, but vectors made by two different models are on different maps.",
                    nodes = listOf(
                        DNode("fn", "Distance function", 0.5f, 0.18f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "any of three works"),
                        DNode("m1", "Notes: one model", 0.26f, 0.74f, w = 0.46f, h = 0.2f, tone = 1, sub = "text-embedding-3-small"),
                        DNode("m2", "Questions: another", 0.76f, 0.74f, w = 0.44f, h = 0.2f, tone = 2, sub = "a newer model"),
                    ),
                    edges = listOf(DEdge("m1", "m2", "not comparable", dashed = true)),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("fn", "m1", "m2"), light = setOf("m1", "m2"), pulse = "m1" to "m2", card = BoardCard("The clue", "Notes from one embedding model; questions from a newer one.")),
                    Beat(18, light = setOf("m2"), card = BoardCard("Different maps", "Same model for notes and question. Change models, embed everything again.")),
                ),
            ),
            // 20: "Not quite. Second witness: the finance team..."
            Scene(
                20, "The second witness", query(),
                beats = listOf(
                    Beat(20, show = setOf("q", "rank"), light = setOf("rank"), card = BoardCard("The bill tripled", "Every search cost far more RUs, the price tag on every call.")),
                    Beat(21, card = BoardCard("Suspect: no index", "An index is like a book's. But the culprit is in the query.")),
                    Beat(22, light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("VectorDistance", "The function that scores how near each note is to the question.")),
                    Beat(24, show = setOf("filter", "top"), light = setOf("top", "filter"), pulse = "rank" to "top", card = sql),
                ),
            ),
            // 27: "And the writes? Finance said those were pricey too."
            Scene(
                27, "The third clue", container(),
                beats = listOf(
                    Beat(27, show = setOf("cont")),
                    Beat(28, show = setOf("excl", "index"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Vector field in the normal index", "Every insert paid to index 1,536 numbers. Leave it out.")),
                    Beat(29, light = setOf("cont"), card = BoardCard("Edit the existing container?", "No: vector settings go on a new container, at creation.")),
                    Beat(32, show = setOf("acct", "policy"), light = setOf("acct", "policy"), pulse = "acct" to "cont", card = policyCode),
                ),
            ),
            // 33: "Which vector index, then? I'll say flat."
            Scene(
                33, "Which index?", indexes(),
                beats = listOf(
                    Beat(33, show = setOf("flat"), light = setOf("flat")),
                    Beat(34, show = setOf("dims"), light = setOf("flat", "dims"), pulse = "dims" to "flat", card = BoardCard("A fourth case", "Every café in town: exact, but flat stops at 505. 1,536 doesn't fit.")),
                    Beat(36, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(37, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk"),
                ),
            ),
            // 38: "Far more than 50,000 in scope... It links the notes into a graph"
            Scene(
                38, "diskANN: friends of friends", web(),
                beats = listOf(
                    Beat(38, show = setOf("q", "a", "b", "c", "d"), light = setOf("b"), pulse = "a" to "b", card = graphCard),
                    Beat(40, light = setOf("d"), pulse = "b" to "d", card = annCard),
                ),
            ),
            // 41: "And the student sees five notes. That's it?"
            Scene(
                41, "Then comes RAG", rag(),
                beats = listOf(
                    Beat(41, show = setOf("notes")),
                    Beat(42, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = ragCard),
                ),
            ),
            // 43: "Any loose ends in the file?"
            Scene(
                43, "Loose ends", limits(),
                beats = listOf(
                    Beat(44, show = setOf("close", "say", "again", "feed"), light = setOf("close", "say"), pulse = "again" to "feed", card = BoardCard("Nearest isn't relevant", "Five notes even about tax returns. Check the scores. Change feed, pgvector.")),
                ),
            ),
            // 45: "So, the case summary for the exam?"
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("models", "rus"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Same model, TOP N, the right index.")),
                    Beat(48, card = BoardCard("Today", "Recreate it: enable, a quantizedFlat container, five notes, one question.")),
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
                    Beat(2, show = setOf("db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("No shared words", "Told to slow down? The note: let the database grow by itself.")),
                ),
            ),
            // 4: "It doesn't, really. It compares numbers. Picture a town map..."
            Scene(
                4, "A spot on the map", mapPicture(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "a", "b"), light = setOf("a", "b"), pulse = "mid" to "a", card = BoardCard("Vector and embedding", "(3, 4) is a spot on a map. An embedding is a text's spot on a huge one.")),
                    Beat(8, show = setOf("dims"), light = setOf("dims"), card = dimensionCard),
                ),
            ),
            // 9: "Okay. So who went first?"
            Scene(
                9, "Monday: Tom", container(),
                beats = listOf(
                    Beat(10, show = setOf("cont"), light = setOf("cont"), card = BoardCard("An existing container?", "A container is a box of documents. Tom's was last year's notes.")),
                    Beat(12, show = setOf("acct"), light = setOf("acct", "cont"), pulse = "acct" to "cont", card = BoardCard("A new container", "Vector settings go on at creation. Switch on the account first.")),
                    Beat(14, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Vector embedding policy", "Kind of number, 1,536 of them, and a distance function.")),
                ),
            ),
            // 15: "Go on then, one at a time."
            Scene(
                15, "Three distance rules", distance(),
                beats = listOf(
                    Beat(15, show = setOf("q")),
                    Beat(16, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(18, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(20, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 21: "And Tuesday?"
            Scene(
                21, "Tuesday: Dana", container(),
                beats = listOf(
                    Beat(22, show = setOf("cont"), light = setOf("cont"), card = BoardCard("Inserts through the roof", "Every write cost far more RUs, the price tag on every call.")),
                    Beat(26, show = setOf("excl", "index"), light = setOf("excl", "index"), pulse = "cont" to "excl", card = BoardCard("Leave the vector field out", "Out of the normal index, and give it a vector index instead.")),
                ),
            ),
            // 27: "Which vector index? I sense a Wednesday."
            Scene(
                27, "Wednesday: Marcus", indexes(),
                beats = listOf(
                    Beat(28, show = setOf("flat"), light = setOf("flat"), card = flatCard),
                    Beat(30, show = setOf("dims"), light = setOf("dims", "flat"), pulse = "dims" to "flat", card = BoardCard("1,536 dimensions", "flat stops at 505. The container said no.")),
                    Beat(32, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(33, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk"),
                ),
            ),
            // 34: "For far more than 50,000 in scope... It's a graph"
            Scene(
                34, "diskANN: a web of notes", web(),
                beats = listOf(
                    Beat(34, show = setOf("q", "a", "b", "c", "d"), light = setOf("b"), pulse = "a" to "b", card = graphCard),
                    Beat(36, light = setOf("d"), pulse = "b" to "d", card = annCard),
                ),
            ),
            // 37: "Thursday. Go."
            Scene(
                37, "Thursday: the intern", query(),
                beats = listOf(
                    Beat(40, show = setOf("q", "rank"), light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("VectorDistance", "Scores how near each note is; ORDER BY sorts. No TOP: a huge RU bill.")),
                    Beat(42, show = setOf("filter", "top"), light = setOf("top", "filter"), pulse = "rank" to "top", card = sql),
                ),
            ),
            // 43: "And Friday? There's always a Friday."
            Scene(
                43, "Friday: Priya",
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
                    Beat(44, show = setOf("m1", "m2"), light = setOf("m2"), pulse = "m1" to "m2", card = BoardCard("Two models", "Questions from a newer model; notes from the old one.")),
                    Beat(45, show = setOf("bad"), light = setOf("bad"), pulse = "m2" to "bad"),
                    Beat(46, light = setOf("m1", "m2"), card = BoardCard("Two maps", "Same model, always. Change models, embed every note again.")),
                ),
            ),
            // 47: "Once it worked, what did the Coach do with its five notes?"
            Scene(
                47, "What the Coach did next", rag(),
                beats = listOf(
                    Beat(47, show = setOf("notes")),
                    Beat(48, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = ragCard),
                    Beat(50, light = setOf("notes"), card = BoardCard("Nearest isn't relevant", "Check the scores; let the chat model say it doesn't know.")),
                ),
            ),
            // 51: "So, the gossip I should remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("new", "flat"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(54, card = todayCard),
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
                    Beat(2, show = setOf("note"), light = setOf("q", "note"), card = BoardCard("The perfect answer", "Let the database grow by itself. Barely a word in common.")),
                ),
            ),
            // 4: "Lonely. Unless we compare meaning... picture a town map."
            Scene(
                4, "A spot on the map", mapPicture(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "a"), light = setOf("a"), pulse = "mid" to "a", card = vectorCard),
                    Beat(6, show = setOf("dims"), light = setOf("dims"), card = dimensionCard),
                    Beat(8, show = setOf("b"), light = setOf("a", "b"), pulse = "a" to "b", card = embeddingCard),
                ),
            ),
            // 9: "Close together, in the dark. I like that. Where do they live?"
            Scene(
                9, "The Coach", coach(),
                beats = listOf(
                    Beat(10, show = setOf("user", "api", "db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("Vector search", "The question gets a spot too; the nearest notes come back.")),
                    Beat(12, show = setOf("emb"), light = setOf("emb"), pulse = "api" to "emb", card = BoardCard("One promise", "Each model draws its own map. The same model for notes and question.")),
                ),
            ),
            // 13: "I've been on dates like that. So how near is near?"
            Scene(
                13, "How near is near", distance(),
                beats = listOf(
                    Beat(13, show = setOf("q")),
                    Beat(14, show = setOf("cos"), light = setOf("cos"), card = BoardCard("Distance function", "The rule for how close two spots are. Three of them, slowly.")),
                    Beat(16, light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(18, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(20, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 21: "And Cosmos DB? Does it just do this, out of the box?"
            Scene(
                21, "Setting it up", container(),
                beats = listOf(
                    Beat(22, show = setOf("acct", "cont"), light = setOf("acct", "cont"), pulse = "acct" to "cont", card = BoardCard("Account, then a new container", "Switch on vector search for the account; vector settings only at creation.")),
                    Beat(24, show = setOf("policy", "excl", "index"), light = setOf("policy", "excl"), pulse = "cont" to "excl", card = BoardCard("Policy and index", "Kind, count and distance. Leave the vector field out; add a vector index.")),
                ),
            ),
            // 27: "And there's more than one kind. I can hear it."
            Scene(
                27, "Three vector indexes", indexes(),
                beats = listOf(
                    Beat(28, show = setOf("flat"), light = setOf("flat"), card = flatCard),
                    Beat(30, show = setOf("qf"), light = setOf("qf"), card = quantizedCard),
                    Beat(31, show = setOf("disk"), light = setOf("disk")),
                ),
            ),
            // 32: "It's for far more than 50,000 in scope. It links the notes into a graph..."
            Scene(
                32, "Holding hands", web(),
                beats = listOf(
                    Beat(32, show = setOf("q", "a", "b", "c", "d"), light = setOf("b"), pulse = "a" to "b", card = graphCard),
                    Beat(34, light = setOf("d"), pulse = "b" to "d", card = annCard),
                ),
            ),
            // 37: "And the search itself? What do I write?"
            Scene(
                37, "The search", query(),
                beats = listOf(
                    Beat(40, show = setOf("q", "rank", "filter"), light = setOf("rank", "filter"), pulse = "q" to "rank", card = sql),
                    Beat(42, show = setOf("top"), light = setOf("top"), pulse = "rank" to "top", card = BoardCard("TOP N", "RUs are the price tag on every call. Without TOP N, far more is scored.")),
                ),
            ),
            // 43: "Five notes, then. What does the Coach do with them?"
            Scene(
                43, "Grounded answers", rag(),
                beats = listOf(
                    Beat(43, show = setOf("notes")),
                    Beat(44, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = ragCard),
                ),
            ),
            // 45: "Now the part I've been waiting for. Where does it stop working?"
            Scene(
                45, "Where it stops", limits(),
                beats = listOf(
                    Beat(46, show = setOf("close", "say", "again", "feed"), light = setOf("close", "again"), pulse = "again" to "feed", card = BoardCard("Nearest isn't relevant", "Look at the scores. A changed note is embedded again. pgvector: next.")),
                ),
            ),
            // 47: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("flat", "big", "small"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: the index, TOP N, one model.")),
                    Beat(50, card = todayCard),
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
                    Beat(0, show = setOf("q"), card = BoardCard("Date night", "Finding what's close to you: search by meaning in Cosmos DB.")),
                    Beat(2, show = setOf("note"), light = setOf("q", "note"), card = BoardCard("A perfect match", "Told to slow down? Let the database grow. Hardly a word in common.")),
                ),
            ),
            // 4: "With numbers. Picture a town map..."
            Scene(
                4, "A spot on the map", mapPicture(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "a"), light = setOf("a"), pulse = "mid" to "a", card = BoardCard("Vector and dimension", "(3, 4) is a spot on a map; each number in it is one dimension.")),
                    Beat(6, show = setOf("b", "dims"), light = setOf("a", "b"), pulse = "a" to "b", card = embeddingCard),
                    Beat(9, light = setOf("a", "b"), card = BoardCard("Quiz: two models", "Notes and questions from different models. Nonsense results. Why?")),
                    Beat(10, light = setOf("dims"), card = BoardCard("Right answer", "Each model draws its own map. Same model, always.")),
                ),
            ),
            // 12: "Mm. My turn. How close are two spots?..."
            Scene(
                12, "Similarity", distance(),
                beats = listOf(
                    Beat(12, show = setOf("q"), card = BoardCard("Distance function", "The rule for how close two spots are. Three of them.")),
                    Beat(13, show = setOf("cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(16, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(18, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 19: "Noted. Can I switch this on for any container I like?"
            Scene(
                19, "Commitment", container(),
                beats = listOf(
                    Beat(20, show = setOf("acct", "cont"), light = setOf("cont"), pulse = "acct" to "cont", card = BoardCard("A new container", "Switch on the account first; vector settings go on at creation.")),
                    Beat(22, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = BoardCard("Quiz: expensive inserts", "Every insert suddenly costs far more. What was forgotten?")),
                    Beat(23, show = setOf("excl", "index"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Leave the vector field out", "Out of the normal index, and give it a vector index instead.")),
                ),
            ),
            // 25: "Now the vector indexes. Tell me slowly."
            Scene(
                25, "Three indexes", indexes(),
                beats = listOf(
                    Beat(26, show = setOf("flat"), light = setOf("flat"), card = flatCard),
                    Beat(28, show = setOf("qf"), light = setOf("qf"), card = quantizedCard),
                    Beat(29, show = setOf("disk"), light = setOf("disk")),
                ),
            ),
            // 30: "For far more than 50,000 in scope. It's a graph..."
            Scene(
                30, "Friends of friends", web(),
                beats = listOf(
                    Beat(30, show = setOf("q", "a", "b", "c", "d"), light = setOf("b"), pulse = "a" to "b", card = graphCard),
                    Beat(32, light = setOf("d"), pulse = "b" to "d", card = annCard),
                ),
            ),
            // 33: "My question, then. Notes with 1,536 dimensions, and someone picks flat."
            Scene(
                33, "Quiz night", indexes(),
                beats = listOf(
                    Beat(33, show = setOf("dims"), light = setOf("dims", "flat"), pulse = "dims" to "flat", card = BoardCard("Quiz: 1,536 with flat", "Notes with 1,536 dimensions and a flat index. Good idea?")),
                    Beat(34, light = setOf("flat"), card = BoardCard("Right answer", "flat stops at 505 dimensions. The exam's favourite trap.")),
                    Beat(36, light = setOf("dims"), card = BoardCard("Quiz: a few hundred in scope", "300,000 notes, every search filtered to one learner. Which index?")),
                    Beat(38, light = setOf("qf"), pulse = "dims" to "qf", card = BoardCard("No kiss: it's quantizedFlat", "Under 50,000 in scope. diskANN is for far more than that.")),
                ),
            ),
            // 39: "Bigger isn't always better. Noted. How do I search?"
            Scene(
                39, "My top one", query(),
                beats = listOf(
                    Beat(39, show = setOf("q", "rank")),
                    Beat(42, show = setOf("filter"), light = setOf("rank", "filter"), pulse = "filter" to "rank", card = sql),
                    Beat(44, light = setOf("rank"), card = BoardCard("Quiz: too many RUs", "RUs, request units, are the price of every call. What's missing?")),
                    Beat(45, show = setOf("top"), light = setOf("top"), pulse = "rank" to "top", card = BoardCard("TOP N", "Without it, far more notes are scored and the RU charge climbs.")),
                ),
            ),
            // 47: "My question. The Coach must answer only from the learner's notes..."
            Scene(
                47, "Grounded", rag(),
                beats = listOf(
                    Beat(47, show = setOf("notes"), card = BoardCard("Quiz: only from the notes", "Answer only from the learner's notes, and cite them. The pattern?")),
                    Beat(48, show = setOf("prompt", "chat", "ans"), light = setOf("prompt", "chat"), pulse = "notes" to "prompt", card = ragCard),
                ),
            ),
            // 50: "And where does it stop working, Rafe?..."
            Scene(
                50, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(51, show = setOf("close", "say", "again", "feed"), light = setOf("close"), pulse = "again" to "feed", card = BoardCard("Nearest isn't the one", "Check the scores. A changed note is embedded again: see the change feed.")),
                ),
            ),
            // 52: "Last question, for the exam. What do we remember?"
            Scene(
                52, "For the exam", exam(),
                beats = listOf(
                    Beat(53, show = all6, light = setOf("models", "rus", "new"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(56, card = todayCard),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
