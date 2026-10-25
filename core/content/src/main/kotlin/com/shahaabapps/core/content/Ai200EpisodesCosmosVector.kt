package com.shahaabapps.core.content

/** Vector search in Cosmos DB for Zaiqa: embeddings, the vector policy, VectorDistance: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosVector {
    private val classic = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Hi, and welcome back. Zaiqa is real now: the Android app, the Zaiqa API, the backend that serves it, on Container Apps, restaurants and orders safe in Azure SQL, the order feed in Cosmos DB. Today, the search box that understands you."),
            leo("Understands me? Zaiqa's search already works. I type dal makhani, I get dal makhani."),
            maya("Now type: something like dal makhani, but vegan. The dish you want is on a menu as creamy black lentils, no dairy. Not one word in common. Say you're building that search. Matching words finds nothing."),
            leo("Then why not just add a clever column in Azure SQL? The menus are already there."),
            maya("SQL holds what must be correct: the price, the order. But searching words in SQL is still searching words. To search by meaning, meaning has to become numbers first."),
            leo("Numbers. Go on."),
            maya("Keep one picture all episode: a town map. From the middle, walk 3 steps east and 4 steps north. Your spot is two numbers, 3 and 4. A list of numbers like that is called a vector: a spot on the map."),
            leo("So my spot is the vector 3, 4. And a dimension?"),
            maya("A dimension means one number in the list. A map spot has two. Ours have 1,536. Nobody can picture that, and you don't need to. The maths works just as on paper."),
            leo("And who writes 1,536 numbers? Not me, I hope."),
            maya("[chuckle] An embedding model, an AI whose only job is this. The list it writes for a text is called an embedding: that text's spot on a huge map. Similar meanings land close, so vegan dal makhani and creamy black lentils sit near each other."),
            leo("Where does Zaiqa get that model?"),
            maya("Azure OpenAI, Microsoft's home for OpenAI's models. Our Taste service calls it: the small Python service behind the search box. One rule people trip on: the same model for the stored dishes and for what a diner types."),
            leo("Mix two, and it's a map of London for a walk round Paris."),
            maya("Exactly. Finding dishes by nearby spots is called vector search, and it goes in Cosmos DB, beside the order feed. First, enable vector search on the account, the top-level home for your databases. It takes fifteen minutes."),
            leo("Then dishes go in a container. Which is, what, a table?"),
            maya("Near enough: a box of documents. And here's the trap. The vector settings go on when you create the container. You cannot add them later."),
            leo("So I can't switch it on for the dish container Zaiqa already has?"),
            maya("No. New one, copy the dishes across. First setting: the vector embedding policy. Which field holds the numbers, what kind, how many, 1,536 to match the model, and the distance function."),
            leo("Distance function. That sounds like maths homework."),
            maya("It's just the rule for how close two spots are. Three of them, one at a time. The default is cosine. Picture an arrow from the middle of the map to each spot."),
            leo("Two arrows. Then what?"),
            maya("Cosine asks: do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher means more similar. Arrows to 3, 4 and to 6, 8 score 1, though one is twice as long."),
            leo("Got it. The second?"),
            maya("Dot product. In plain words: multiply the matching numbers, then add them up. Take 2, 3 and 4, 1. Two times four is eight, three times one is three, eleven in all. A bigger answer means the lists agree more."),
            leo("Multiply and add. Even I can do that. And the third?"),
            maya("Euclidean. Picture a ruler laid between two pins on the map. From the middle to the spot 3, 4, the ruler reads 5. A real distance, so this time lower means closer."),
            leo("Higher is closer, except when lower is closer. [sigh] What's the second setting?"),
            maya("The indexing policy. An index is like the one at the back of a book: the database jumps to the right page. By default Cosmos DB indexes every field you save."),
            leo("Every field? Including my 1,536 numbers?"),
            maya("That's rule one: leave the vector field out of the normal index, or every dish you save pays to index all 1,536, one by one. Rule two: add a vector index instead."),
            leo("Let me guess. Three kinds."),
            maya("Three. The first is called flat. Picture measuring the distance to every restaurant in the city, then picking the closest. Flat compares the search with every dish. Never misses, slow, and it only takes lists of up to 505 numbers."),
            leo("And ours have 1,536. So flat is out straight away."),
            maya("Straight away. Second is quantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo, then still compared with every dish. Good up to about 50,000 dishes in scope, meaning the ones one search looks at."),
            leo("And Zaiqa?"),
            maya("Four hundred thousand dishes, but every search is filtered to one city: a few thousand in scope. QuantizedFlat. For example, a nationwide search with no city filter, two million dishes, needs the third kind: diskANN."),
            leo("DiskANN. What's that, in plain words?"),
            maya("It links the dishes into a web. Each node, meaning one stored dish, is joined to a few near neighbours. Picture dots joined by lines, like a chart of who knows whom. That web is called a graph."),
            leo("And a search walks the web?"),
            maya("It hops. Start at a dot, look at its neighbours, jump to whichever is closer, repeat. Friends of friends, instead of knocking on every door. Its answer is the nearest neighbour, the dish closest to what was typed."),
            leo("Guaranteed the very closest?"),
            maya("Not quite. ANN stands for approximate nearest neighbour: very close, not guaranteed perfect. That's the trade for speed. Past about 50,000 in scope, it's the fastest and cheapest."),
            leo("Okay, container ready. How does a search actually run?"),
            maya("The diner types, something like dal makhani but vegan. The app sends it to the Zaiqa API, which hands it to the Taste service. That embeds it, then asks Cosmos DB in SQL, the language for asking a database questions."),
            leo("And the query says, find me the close ones?"),
            maya("A function called VectorDistance scores every dish against what was typed. You sort by it, which SQL calls ORDER BY, keep the best five, TOP 5, and add a WHERE, SQL's filter, on the city."),
            leo("Why filter by city?"),
            maya("Two reasons that are one. You can't eat a dish three hundred miles away. And dishes sit in drawers by city, each drawer called a partition, so the search opens one drawer."),
            leo("And why TOP 5? Why not everything, ranked?"),
            maya("RUs, request units, are the price tag Cosmos DB puts on every call. Without TOP N, meaning the top few, the engine scores far more dishes than you need. Good matches but too many RUs is a classic exam question."),
            leo("And the diner sees five dishes?"),
            maya("One step further, with RAG, short for retrieval augmented generation. The Taste service retrieves the closest dishes and pastes their descriptions into the prompt, the message it sends to a chat model."),
            leo("The chat model being the AI that writes the reply?"),
            maya("Yes, and the prompt says: answer only from these dishes. So Zaiqa can say why each one matched, and show the five it used."),
            leo("And when a restaurant adds a new dish? Does somebody remember all this?"),
            maya("Nobody has to. The photo function already wakes when a dish photo lands in Blob Storage and makes the thumbnail. Now it also asks the Taste service for a fresh embedding and saves it. An edited description does the same."),
            leo("So the map redraws itself as the menus change."),
            maya("[laugh] It does. And if you ever change model, every dish must be embedded again."),
            leo("Fair warning. So what do I remember for the exam?"),
            maya("Five things. 1,536 dimensions with a flat index is the trap: flat stops at 505. Over 50,000 vectors in scope: diskANN. A filter leaving a small set: quantizedFlat. Too many RUs: add TOP N. Nonsense results: the same embedding model for both."),
            leo("And anything to try today?"),
            maya("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five dishes from different menus into embeddings, and upsert them, meaning save them. Then search in words no menu uses, and see which dish ranks first."),
            leo("A search that knows what I mean. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Welcome to Case Files. Today's case: The Search That Loved Pancakes. Zaiqa's new search is meant to answer, something like dal makhani but vegan. On launch night it proudly returned a pancake stack, a milkshake, and a cheese toastie."),
            leo("[chuckle] Detective Leo, hungry and on the case. First, the groundwork. How was it meant to find the right dish at all?"),
            maya("By meaning, not words. The dish it wanted is on a menu as creamy black lentils, no dairy. Hardly a shared word. So every dish is turned into numbers. Picture a town map: 3 steps east, 4 north is a spot you write as 3, 4. A list like that is called a vector."),
            leo("A spot on a map. Fine. But a dish isn't a map."),
            maya("No, but an AI model gives it a spot on a huge one. That list is called an embedding, and texts that mean similar things get nearby spots."),
            leo("How huge? Two numbers, like the town?"),
            maya("1,536 numbers a dish, so 1,536 dimensions, where a dimension means one number in the list. A town map has two."),
            leo("1,536 directions. I can't picture that."),
            maya("Nobody can, and nobody needs to. The dishes and their vectors live in Cosmos DB, beside Zaiqa's order feed, while the menus themselves stay in Azure SQL. Searching for the nearest spots is vector search."),
            leo("Then my first suspect is the distance function, the rule for what counts as near. Wrong one picked, I bet."),
            maya("[pause] Good instinct, wrong suspect. Let's question all three. Cosine, the default. Picture arrows from the middle of the map to two spots. Do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher is more similar."),
            leo("So arrows to 3, 4 and 6, 8, same direction, score 1?"),
            maya("Exactly, though one is twice as long. Second, dot product. In plain words: multiply the matching numbers and add them up. Take 2, 3 and 4, 1: two times four, plus three times one, is eleven. Bigger means the lists agree more."),
            leo("And the third suspect is euclidean."),
            maya("Picture a ruler laid between two pins on the map. From the middle to 3, 4 it reads 5, so lower is closer. All three work. Your suspect has an alibi."),
            leo("Then what? The dishes and the search are both just lists of numbers."),
            maya("Here's the clue. The dishes were embedded in March with one embedding model. On launch night the Taste service, the Python service behind the search box, called a newer one for what diners type."),
            leo("So the search and the dishes are on different maps."),
            maya("Each model draws its own, often with a different number of dimensions. Spots from two maps don't compare. Same model for both, always, and if you change models, embed every dish again."),
            leo("Fix the model, pancakes gone. Case closed?"),
            maya("Not quite. Second witness: finance. The bill tripled in week one. Every search cost far more RUs, request units, the price tag Cosmos DB puts on every call."),
            leo("Suspect: no index. An index is like the one at the back of a book, and it makes queries cheap."),
            maya("Close. Look at the query, written in SQL, the language for asking a database questions. It sorts dishes by VectorDistance, a function that scores how near each dish is. SQL calls sorting ORDER BY. What's missing?"),
            leo("[pause] A limit. It ranks every dish in the country, and the app shows five."),
            maya("Detective Leo, at last. TOP N means only the top few. Without it the engine scores far more dishes than you need. So, TOP 5. And a WHERE, SQL's filter, on the diner's city."),
            leo("Because nobody orders dinner three hundred miles away."),
            maya("And because dishes sit in drawers by city, each drawer called a partition, so the search opens one drawer instead of all of them. One filter, two wins."),
            leo("And the writes? Finance said those were pricey too."),
            maya("Third clue. The vector field was still in the indexing policy, the list of what goes in the normal index. Every saved dish paid to index 1,536 numbers one by one. Leave it out, and give it a vector index instead. The Cosmos performance episode has the rest."),
            leo("Easy. Edit the container, add the vector settings, done by lunch."),
            maya("That's how you open the next case. A container is Cosmos DB's box of documents, and its vector settings go on when you create it, never later."),
            leo("So they built a new container and copied every dish in. [pause] What did it need?"),
            maya("Vector search switched on for the account first, which can take fifteen minutes. Then a vector embedding policy: the kind of number, 1,536 of them, and cosine."),
            leo("Which vector index? I'll say flat. It sounds simple."),
            maya("[sigh] And a fourth case opens. Picture measuring the distance to every restaurant in the city, then picking the closest. That's flat: it compares the search with every dish. Never misses, slow, and it stops at lists of 505 numbers. 1,536 doesn't fit. The exam's favourite trap."),
            leo("Fine. What did they pick?"),
            maya("QuantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo, then still compared with every dish. Good up to about 50,000 in scope, the dishes one search looks at. Zaiqa holds four hundred thousand, but each search is one city: a few thousand."),
            leo("And when is diskANN the answer?"),
            maya("Far more than 50,000 in scope, say a nationwide search with no city filter. It links the dishes into a graph, meaning a web of dots joined by lines. Each node, one stored dish, is joined to its near neighbours, and a search hops from dot to closer dot."),
            leo("So it doesn't check every dish. Can it miss?"),
            maya("Slightly. ANN stands for approximate nearest neighbour. The nearest neighbour is the dish closest to what was typed, and approximate means very close, not guaranteed perfect. At that size it's the fastest and cheapest."),
            leo("And the diner sees five dishes. That's it?"),
            maya("Then comes RAG, short for retrieval augmented generation. Retrieve the top dishes, put their descriptions in the prompt, the message sent to a chat model, an AI that writes replies. It answers only from them, so Zaiqa can show which five it used."),
            leo("Any loose ends in the file?"),
            maya("Two. New dishes: the photo function already runs when a photo lands in Blob Storage, so it now asks for an embedding too, and a changed menu row re-embeds that dish. And vector search always returns the nearest dishes, even when none fits. Nearest is not relevant: check the scores."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Nonsense results: the same embedding model for both. Too many RUs on a vector query: add TOP N. 1,536 dimensions with a flat index is the trap, flat stops at 505. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. And a vector policy needs a new container."),
            leo("Case closed. Anything to try today?"),
            maya("Run the case again without the pancakes. Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five dishes from different menus into embeddings, and upsert them, meaning save them. Then search in words no menu uses, and see which dish ranks first."),
            leo("Pancakes for breakfast, lentils for dinner. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-vector",
        listOf(
            maya("Okay, did you hear what happened on the Zaiqa team this week? Grab a coffee. They shipped the search that understands you, the one that answers, something like dal makhani but vegan. And let's just say, it was a week."),
            leo("[laugh] I heard there was a meeting with sourdough in the title. What were they actually building?"),
            maya("Search by meaning. The right dish is on a menu as creamy black lentils, no dairy, and nobody types that. The menus stay in Azure SQL; the search is new."),
            leo("How does a database know what anything means?"),
            maya("It doesn't. It compares numbers. Picture a town map: 3 steps east, 4 north is a spot you write as 3, 4. A list like that is called a vector."),
            leo("A spot on a map. But a plate of lentils isn't on any map."),
            maya("It is once an AI model gives it one, on a huge map. That list of numbers is called an embedding. Similar meanings, nearby spots."),
            leo("A huge map. How huge?"),
            maya("1,536 numbers a dish, so 1,536 dimensions, where a dimension means one number in the list. A town map has two. The dishes and their vectors go in Cosmos DB, beside the order feed, and searching for the nearest spots is vector search."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday morning. Tom had last year's dish container. A container is Cosmos DB's box of documents, like a table. He thought he'd switch vector search on for it. Done by coffee."),
            leo("And?"),
            maya("And you can't. Vector settings go on a new container, when you create it. The account, the top-level home for your databases, needs vector search switched on first, which can take fifteen minutes. Tom spent Monday copying dishes across."),
            leo("[sigh] Poor Tom. What settings did it need?"),
            maya("A vector embedding policy, a short description of the vectors: the kind of number, how many, 1,536 to match the model, and the distance function, the rule for how near two spots are. Three rules, and Tom learnt all three before lunch."),
            leo("Go on then, one at a time."),
            maya("Cosine, the default. Picture arrows from the middle of the map to two spots. Do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher means more similar."),
            leo("And the second?"),
            maya("Dot product. In plain words: multiply the matching numbers, then add them up. Take 2, 3 and 4, 1: two times four is eight, three times one is three, eleven in all. A bigger answer means the lists agree more."),
            leo("And the third?"),
            maya("Euclidean. Picture a ruler laid between two pins on the map. From the middle to 3, 4 it reads 5. A real distance, so this one's backwards: lower means closer."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. Dana loaded four hundred thousand dishes, and every single save cost far more RUs. RUs, request units, are the price tag Cosmos DB puts on every call."),
            leo("Why? It's just a dish."),
            maya("A dish with 1,536 numbers on it. An index is like the one at the back of a book, and by default Cosmos DB indexes every field you save."),
            leo("Oh no. Including the 1,536 numbers?"),
            maya("All of them, one by one, on every save. Dana should have left the vector field out of the indexing policy, the list of what goes in the normal index, and given it a vector index instead."),
            leo("Which vector index? I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. Marcus picked flat. Picture measuring the distance to every restaurant in the city, then picking the closest. That's flat: it compares the search with every dish. It never misses."),
            leo("Sounds perfect. What's the catch?"),
            maya("Slow and costly, and it only takes lists of up to 505 numbers. Marcus had 1,536. The container said no, and the whole floor heard about it."),
            leo("So what should Marcus have picked?"),
            maya("It depends on the dishes in scope, the ones one search looks at. Every Zaiqa search is filtered to one city, so a few thousand are in scope out of four hundred thousand. Under about 50,000, that's quantizedFlat: quantized means each list is squeezed smaller first, like a compressed photo."),
            leo("And the big one? Everyone keeps saying diskANN."),
            maya("For far more than 50,000 in scope, like a nationwide search with no city filter. It's a graph, which is a web of dots joined by lines. Each node, meaning one stored dish, is joined to its near neighbours, and a search hops from dot to closer dot, like friends of friends."),
            leo("So it might miss one?"),
            maya("Just about. ANN stands for approximate nearest neighbour. The nearest neighbour is the dish closest to what was typed, and approximate means very close, not guaranteed perfect. Fastest and cheapest at that size."),
            leo("Thursday. Go."),
            maya("Thursday, the intern. Bless them. Lovely matches, and a frightening bill."),
            leo("What did their query look like?"),
            maya("It was in SQL, the language for asking a database questions. It sorted every dish by VectorDistance, a function that scores how near each dish is. SQL calls that ORDER BY. But there was no TOP."),
            leo("And TOP is what?"),
            maya("TOP N means only the top few. Without it the engine scores far more dishes than you need. TOP 5, with a WHERE, SQL's filter, on the diner's city, and it's cheap again. Dishes sit in drawers by city, each drawer called a partition, so the search opens one."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She tried a newer embedding model for what diners type, but the dishes were embedded with the old one. Someone asked for vegan dal makhani and Zaiqa offered sourdough."),
            leo("[laugh] The sourdough meeting. But why?"),
            maya("Each model draws its own map, often with different dimensions. Spots from two maps don't compare. Same model for both, always, and if you change models, embed every dish again. Four hundred thousand of them."),
            leo("Once it worked, what did Zaiqa do with its five dishes?"),
            maya("RAG, which stands for retrieval augmented generation. Retrieve the top dishes, add their descriptions to the prompt, the message sent to a chat model, an AI that writes replies. It answers only from them, and the app shows the five it used."),
            leo("And who keeps all this up to date? Menus change daily."),
            maya("The photo function, which already wakes when a dish photo lands in Blob Storage. Now it asks the Taste service, the Python service behind the search, for a fresh embedding and saves it. An edited description in Azure SQL does the same."),
            leo("Did anything still go wrong?"),
            maya("Only where vector search stops working. It always returns the nearest dishes, even when none of them fits. Nearest is not relevant, so the team checks the scores. And the pgvector episode does all this in PostgreSQL, if your team prefers it."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. A vector policy on an existing container? No, a new one. 1,536 dimensions with a flat index is the trap: flat stops at 505. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. Too many RUs, add TOP N. Nonsense results, the same embedding model for both."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five dishes from different menus into embeddings, and upsert them, meaning save them. Then search in words no menu uses, and see which dish ranks first."),
            leo("No sourdough. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-vector",
        listOf(
            nova("Hey, you. It's late, the kitchen's shut, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, how Zaiqa learns to find food by what you mean."),
            ivy("Mm. Meaning, at this hour? You're ambitious tonight, Nova."),
            nova("Only a little. Picture someone hungry at midnight, typing: something like dal makhani, but vegan. And somewhere in the city is the dish, on a menu as creamy black lentils, no dairy. [pause] Barely a word in common."),
            ivy("So a search on their words finds nothing. How lonely."),
            nova("Lonely. Unless we compare meaning, and for that, text becomes numbers. Close your eyes and picture a town map. Walk 3 steps east, 4 steps north. Your spot is two numbers, 3 and 4. A list of numbers like that is called a vector. [pause] A spot on the map."),
            ivy("A spot on the map. I can see it. And a dimension?"),
            nova("A dimension means one number in the list. A map spot has two. Ours tonight have 1,536. Nobody can picture that many directions, not at this hour. You don't need to. The maths works just the same."),
            ivy("And who writes 1,536 numbers for a plate of lentils?"),
            nova("An embedding model, an AI whose only job is that. The list it writes is called an embedding: the spot a text gets on its huge map. Similar meanings land close together, [pause] even when they never share a word."),
            ivy("Close together, in the dark. Where do they live?"),
            nova("Right beside the dishes, in Cosmos DB, where Zaiqa's order feed already lives. The menus stay in Azure SQL, where money and orders belong. What the diner types gets a spot too, from the same model, and the database finds the nearest. That's vector search."),
            ivy("The same model. Is that a rule?"),
            nova("A promise. Each model draws its own map. Spots from two maps simply don't compare. It's like whispering in one language and listening in another."),
            ivy("[chuckle] I've been on dates like that. So how near is near?"),
            nova("That's the distance function: the rule for how close two spots are. Three of them. Slowly, one at a time. The default is cosine. Picture an arrow from the middle of the map to each spot."),
            ivy("Two arrows, in the dark. Then?"),
            nova("Cosine asks, do they point the same way, whatever their length? Same way, 1. A right angle, 0. Opposite, minus 1. Higher means more similar. Arrows to 3, 4 and to 6, 8 point the same way, so they score 1. [pause] Length doesn't matter. Only direction."),
            ivy("Only direction. Romantic. The second?"),
            nova("Dot product. In plain words, multiply the matching numbers, then add them up. Take 2, 3 and 4, 1. Two times four is eight. Three times one is three. Eleven in all. A bigger answer means the lists agree more."),
            ivy("Multiply and add. Even at this hour. And the third?"),
            nova("Euclidean. Picture a ruler laid between two pins on the map. From the middle to 3, 4, the ruler reads 5. A real distance, so this time, lower means closer."),
            ivy("And Cosmos DB does all this out of the box?"),
            nova("Not quite. First you enable vector search on the account, the top-level home for your databases. It can take fifteen minutes. [pause] Then the vector settings go on a new container, Cosmos DB's box of documents, when you create it. Never an existing one."),
            ivy("No changing your mind later. Strict. What settings?"),
            nova("A vector embedding policy: the kind of number, how many, 1,536 to match the model, and the distance function. Then the indexing policy. An index is like the one at the back of a book, and by default every field goes in it."),
            ivy("Every field. Even all 1,536 of my numbers?"),
            nova("Even those, on every dish you save, [pause] unless you leave the vector field out of the normal index. Then you give it a vector index instead."),
            ivy("And there's more than one kind. I can hear it."),
            nova("Three, and the exam loves them. The first is flat. Picture measuring the distance to every restaurant in the city, then picking the closest. Flat compares the search with every dish. It never misses. But it's slow, and it stops at lists of 505 numbers."),
            ivy("And ours have 1,536. Mm. The second?"),
            nova("QuantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo. Then it still compares with every dish, a little less exactly. [pause] Good up to about 50,000 dishes in scope, the ones one search actually looks at."),
            ivy("And the third, diskANN. The one everyone whispers about."),
            nova("For far more than 50,000 in scope. It links the dishes into a graph, which is a web of dots joined by lines. Each node, meaning one stored dish, holds hands with its near neighbours. A search hops from dot to closer dot, like friends of friends."),
            ivy("Holding hands. You're making indexes romantic. Does it ever miss?"),
            nova("[pause] A little. ANN stands for approximate nearest neighbour. The nearest neighbour is the dish closest to what was typed; approximate means very close, not guaranteed perfect. At that size, the fastest and cheapest."),
            ivy("So which does Zaiqa pick? That's a lot of dishes."),
            nova("Four hundred thousand. But nobody orders dinner three hundred miles away, so every search is filtered to one city: a few thousand in scope. [pause] QuantizedFlat."),
            ivy("And the search itself? What do I write?"),
            nova("The app sends what was typed to the Taste service, the small Python service behind the search box. It embeds it with the same model, then asks Cosmos DB in SQL, the language for asking a database questions. VectorDistance scores how near each dish is."),
            ivy("And then?"),
            nova("Sort by it, ORDER BY. Keep the best five, TOP 5. [pause] And a WHERE, SQL's filter, on the city. Dishes sit in drawers by city, each drawer called a partition, so the search opens one."),
            ivy("Why only five?"),
            nova("Because without TOP N, the top few, the engine scores far more dishes than you need, and the RU charge climbs. RUs, request units, are the price tag Cosmos DB puts on every call."),
            ivy("Five dishes, then. What does Zaiqa do with them?"),
            nova("RAG, which stands for retrieval augmented generation. It retrieves those dishes, adds their descriptions to the prompt, the message sent to a chat model, an AI that writes replies, and asks it to answer only from them."),
            ivy("And tomorrow, when every menu has changed?"),
            nova("The photo function wakes when a new dish photo lands in Blob Storage, makes the thumbnail, and now asks the Taste service for a fresh embedding too. An edited description in Azure SQL does the same. [pause] The map redraws itself overnight."),
            ivy("Now the part I wait for. Where does it stop working?"),
            nova("Here. [pause] It always brings back the nearest dishes, even when none of them fits what you asked. Nearest is not relevant. So look at the scores, and let the chat model say, I don't know. And the pgvector episode is this same idea in PostgreSQL."),
            ivy("[sigh] Mm. Before you send me to bed. What do I remember for the exam?"),
            nova("Five little things. 1,536 dimensions with a flat index is the trap: flat stops at 505. Far more than 50,000 vectors in scope, diskANN. A filter leaving a small set, quantizedFlat. Too many RUs on a vector query, add TOP N. Nonsense results, the same embedding model for both."),
            ivy("And something to try today, before I sleep?"),
            nova("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five dishes from different menus into embeddings, and upsert them, meaning save them. [pause] Then search in words no menu uses, and see which dish ranks first."),
            ivy("Close in meaning, if not in words. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-vector",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and the most romantic thing in Zaiqa."),
            rafe("Let me guess. Finding someone close to you."),
            nova("[laugh] Search by meaning. A diner types, something like dal makhani, but vegan. The dish they want is on a menu as creamy black lentils, no dairy. Hardly a word in common, and a perfect match."),
            rafe("A perfect match with nothing in common. Go on, how does a database see that?"),
            nova("With numbers. Picture a town map. Three steps east, four steps north, and your spot is 3, 4. A list of numbers like that is a vector. And a dimension is one number in it, so a map spot has two dimensions."),
            rafe("And how many dimensions do you have, darling?"),
            nova("For dishes, 1,536. An embedding model, an AI, writes that many numbers for a text, and the list is called an embedding: the text's spot on a huge map. Similar meanings, nearby spots."),
            rafe("I always knew you were complicated. So two dishes that mean the same sit close?"),
            nova("A small distance apart. What the diner types gets a spot too, and the database finds the nearest dishes. That's vector search, in Cosmos DB, beside Zaiqa's order feed. The menus stay in Azure SQL, where orders belong."),
            rafe("A small distance apart. Exactly where I'd like to be. Exam style, darling: dishes embedded with one model, searches with another. Nonsense results. Why?"),
            nova("Each model draws its own map. Spots from two maps don't compare. The same model for both, always."),
            rafe("Right answer. Two people have to be on the same map. [kiss]"),
            nova("Mm. My turn. How close are two spots? That's the distance function, and there are three. The default first."),
            rafe("Cosine. Picture an arrow from the middle of the map to each spot. Do they point the same way, whatever their length? Same way scores 1, a right angle 0, opposite minus 1. Higher is more similar."),
            nova("Right answer. It's which way you're facing, Rafe, not how big you are. [kiss]"),
            rafe("[chuckle] I'll take that as a compliment. The second is dot product. Multiply me out."),
            nova("In plain words: multiply the matching numbers, then add them up. Take 2, 3 and 4, 1: two times four is eight, three times one is three, eleven in all. A bigger answer means the lists agree more."),
            rafe("We agree on plenty. And the third, euclidean?"),
            nova("Picture a ruler laid between two pins on the map. From the middle to 3, 4, it reads 5. A real distance, so lower means closer. The one time smaller is better."),
            rafe("Noted. Can I switch this on for any container I like?"),
            nova("Oh no. First you enable vector search on the account, the top-level home for your databases. Then the vector settings go on a new container, Cosmos DB's box of documents, when you create it. Never an existing one. Commitment from the start."),
            rafe("Committed from day one. What settings?"),
            nova("A vector embedding policy: the kind of number, how many, 1,536 to match the model, and the distance function. Then the indexing policy. An index is like the one at the back of a book. Quiz: every saved dish is suddenly expensive. What did we forget?"),
            rafe("We left the vector field in the normal index, so every save indexes 1,536 numbers. Leave it out, and give it a vector index instead."),
            nova("Right answer. Some things shouldn't be indexed in public. [kiss]"),
            rafe("Now the vector indexes. Tell me slowly."),
            nova("The first is flat. Picture measuring the distance to every restaurant in the city, then picking the closest. Flat compares the search with every dish. Never misses, slow, and only lists of up to 505 numbers."),
            rafe("Leaning over every garden fence in town. And the second?"),
            nova("QuantizedFlat. Quantized means each list is squeezed smaller first, like a compressed photo, then still compared with every dish. Up to about 50,000 dishes in scope, meaning the ones one search actually looks at."),
            rafe("And the third, diskANN?"),
            nova("For far more than 50,000 in scope. It's a graph, which is a web of dots joined by lines. Each node, meaning one stored dish, is joined to its near neighbours. A search hops from dot to closer dot, like friends of friends."),
            rafe("Friends of friends. How we met. Does it find the very closest?"),
            nova("Almost. ANN stands for approximate nearest neighbour. The nearest neighbour is the dish closest to what was typed; approximate means very close, not guaranteed perfect."),
            rafe("My question, then. Dishes with 1,536 dimensions, and someone picks flat. Good idea?"),
            nova("No. Flat stops at 505, and 1,536 doesn't fit. The exam's favourite trap."),
            rafe("Right answer, and a dangerous one to get wrong. [kiss]"),
            nova("My turn. Zaiqa holds four hundred thousand dishes, but every search is filtered to one city, so a few thousand are in scope. Which index?"),
            rafe("DiskANN. It's the biggest, so it's the best."),
            nova("[sigh] No kiss for that one. Under 50,000 in scope means quantizedFlat. DiskANN is for far more, like a nationwide search with no city filter."),
            rafe("Bigger isn't always better. Noted. So how does a search run?"),
            nova("The app sends what was typed to the Taste service, our small Python service, which embeds it and asks Cosmos DB in SQL, the language for asking a database questions. VectorDistance scores how near each dish is."),
            rafe("Then I sort by that score, and take the best?"),
            nova("ORDER BY it, keep the best five with TOP 5, and add a WHERE, SQL's filter, on the city, so it opens one drawer of dishes, called a partition."),
            rafe("One city only. You never did like long distance."),
            nova("Never. And it costs less. RUs, request units, are the price tag Cosmos DB puts on every call. So, darling: a vector query gives lovely matches and far too many RUs. What's missing?"),
            rafe("TOP N, only the top few. Without it the engine scores far more dishes than you need. I only want my top five. Well. My top one."),
            nova("Right answer, and charming with it. [kiss]"),
            rafe("My question. Zaiqa must answer from the dishes it found, and show which it used. The pattern?"),
            nova("RAG, which stands for retrieval augmented generation. Retrieve the nearest dishes, put their descriptions in the prompt, the message sent to a chat model, an AI that writes replies, and ask it to answer only from them."),
            rafe("Right answer. Grounded, like a good relationship. [kiss]"),
            nova("And who keeps us fresh, darling? Menus change every day."),
            rafe("The photo function. It wakes when a dish photo lands in Blob Storage, makes the thumbnail, and now asks the Taste service for a new embedding. An edited description does the same."),
            nova("Mm. And where does it stop working? Every romance has its limits."),
            rafe("It always brings back the nearest dishes, even when none of them fits. Nearest is not always the one. So check the scores, and let the chat model say it doesn't know. The pgvector episode does it all in PostgreSQL."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("Nonsense results, the same embedding model for both. Too many RUs, add TOP N. 1,536 dimensions with flat is the trap. A small set after a filter is quantizedFlat; far more than 50,000 in scope is diskANN. And a vector policy needs a new container."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Enable vector search on the account. Create a container with a vector embedding policy and a quantizedFlat index. Turn five dishes from different menus into embeddings, and upsert them, meaning save them. Then search in words no menu uses, and see which dish ranks first."),
            rafe("And I know who ranks first with me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun openZaiqa() = DiagramSpec(
        "Zaiqa so far: the app and its API, orders and menus in Azure SQL, the order feed in Cosmos DB, and search still to come.",
        nodes = listOf(
            DNode("app", "Zaiqa app", 0.25f, 0.15f, w = 0.4f, h = 0.2f, sub = "Android"),
            DNode("api", "Zaiqa API", 0.75f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "Container Apps"),
            DNode("sql", "Azure SQL", 0.25f, 0.52f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "menus and orders"),
            DNode("cos", "Cosmos DB", 0.75f, 0.52f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "the order feed"),
            DNode("new", "Search by meaning", 0.5f, 0.86f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "today's job"),
        ),
        edges = listOf(
            DEdge("app", "api", "search"),
            DEdge("api", "sql", "menus"),
            DEdge("api", "cos", "events"),
            DEdge("api", "new", "today"),
        ),
        aspect = 0.8f,
    )

    private fun meaning() = DiagramSpec(
        "Two texts with hardly a word in common get nearby vectors from the same embedding model.",
        nodes = listOf(
            DNode("q", "Search", 0.24f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "dal makhani, vegan"),
            DNode("dish", "Dish", 0.24f, 0.78f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "black lentils"),
            DNode("model", "Embedding model", 0.76f, 0.18f, w = 0.4f, h = 0.2f, tone = 2, sub = "same for both"),
            DNode("vec", "Nearby vectors", 0.76f, 0.78f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "1,536 numbers"),
        ),
        edges = listOf(
            DEdge("q", "model", "embed"),
            DEdge("dish", "model", "embed"),
            DEdge("model", "vec"),
        ),
        aspect = 0.8f,
    )

    private fun mapPicture() = DiagramSpec(
        "On a town map, 3 steps east and 4 north is the vector (3, 4); an embedding is a text's spot on a far bigger map.",
        nodes = listOf(
            DNode("mid", "Middle", 0.2f, 0.8f, w = 0.28f, h = 0.16f, kind = NodeKind.PILL, sub = "(0, 0)"),
            DNode("a", "Search", 0.4f, 0.2f, w = 0.34f, h = 0.2f, tone = 1, sub = "(3, 4)"),
            DNode("b", "Dish", 0.8f, 0.44f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, sub = "(4, 3): nearby"),
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
        "diskANN: each node is one stored dish joined to its near neighbours, and a search hops toward what was typed.",
        nodes = listOf(
            DNode("q", "Search", 0.22f, 0.15f, w = 0.36f, h = 0.16f, tone = 1),
            DNode("a", "Dish", 0.75f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE, sub = "start here"),
            DNode("b", "Dish", 0.5f, 0.47f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE, sub = "a neighbour"),
            DNode("c", "Dish", 0.2f, 0.8f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE),
            DNode("d", "Nearest dish", 0.75f, 0.8f, w = 0.4f, h = 0.16f, kind = NodeKind.NOTE, sub = "the answer"),
        ),
        edges = listOf(
            DEdge("q", "a", "start", dashed = true),
            DEdge("a", "b", "hop"),
            DEdge("b", "d", "hop"),
            DEdge("b", "c"),
        ),
        aspect = 0.8f,
    )

    private fun zaiqaSearch() = DiagramSpec(
        "A search in Zaiqa: the Taste service embeds what was typed, Cosmos DB ranks by VectorDistance, a chat model answers.",
        nodes = listOf(
            DNode("user", "Diner", 0.1f, 0.2f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
            DNode("api", "Taste service", 0.4f, 0.2f, w = 0.34f, h = 0.2f, tone = 1, sub = "Python"),
            DNode("emb", "Embedding model", 0.8f, 0.2f, w = 0.34f, h = 0.2f, tone = 2, sub = "Azure OpenAI"),
            DNode("db", "Cosmos DB dishes", 0.4f, 0.7f, w = 0.34f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "TOP 5"),
            DNode("chat", "Chat model", 0.8f, 0.7f, w = 0.34f, h = 0.2f, tone = 2, sub = "answers from these"),
        ),
        edges = listOf(
            DEdge("user", "api", "typed words"),
            DEdge("api", "emb", "text", bidirectional = true),
            DEdge("api", "db", "query vector"),
            DEdge("db", "chat", "top dishes"),
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
            DNode("q", "Search", 0.2f, 0.5f, w = 0.32f, h = 0.2f, tone = 1),
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
        "SELECT TOP 5 ordered by VectorDistance, filtered to one city: cheap, and inside one partition.",
        nodes = listOf(
            DNode("q", "Search vector", 0.25f, 0.16f, w = 0.44f, h = 0.2f, tone = 1, sub = "same model"),
            DNode("filter", "WHERE city", 0.75f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "one partition"),
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
        "RAG: the top dishes go into the prompt, and a chat model answers only from them, saying which it used.",
        nodes = listOf(
            DNode("dishes", "Top 5 dishes", 0.24f, 0.2f, w = 0.4f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "by VectorDistance"),
            DNode("prompt", "Prompt", 0.76f, 0.2f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "answer only from these"),
            DNode("chat", "Chat model", 0.76f, 0.78f, w = 0.4f, h = 0.2f, tone = 2, sub = "Azure OpenAI"),
            DNode("ans", "Grounded answer", 0.24f, 0.78f, w = 0.4f, h = 0.2f, sub = "cites its dishes"),
        ),
        edges = listOf(
            DEdge("dishes", "prompt"),
            DEdge("prompt", "chat"),
            DEdge("chat", "ans"),
        ),
        aspect = 0.8f,
    )

    private fun fresh() = DiagramSpec(
        "A new photo or an edited menu wakes the photo function, which asks the Taste service for a fresh embedding.",
        nodes = listOf(
            DNode("photo", "New dish photo", 0.25f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Blob Storage"),
            DNode("fn", "Photo function", 0.75f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "makes the thumbnail"),
            DNode("sql", "Azure SQL menu", 0.25f, 0.52f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "menu edited"),
            DNode("taste", "Taste service", 0.75f, 0.52f, w = 0.44f, h = 0.2f, tone = 1, sub = "new embedding"),
            DNode("cos", "Cosmos DB dishes", 0.5f, 0.86f, w = 0.5f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "vector saved"),
        ),
        edges = listOf(
            DEdge("photo", "fn", "lands"),
            DEdge("fn", "taste", "embed this"),
            DEdge("sql", "taste", "edited"),
            DEdge("taste", "cos", "upsert"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where vector search stops: closest is not always relevant, and a changed dish or model means embedding again.",
        nodes = listOf(
            DNode("close", "Closest, not relevant", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "check the scores"),
            DNode("again", "Embed again", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "dish or model changed"),
            DNode("say", "Say I don't know", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "the chat model may"),
            DNode("pg", "pgvector episode", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "the same in Postgres"),
        ),
        edges = listOf(
            DEdge("close", "say", "so"),
            DEdge("again", "pg", "same idea"),
        ),
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
    private val sqlCard = BoardCard("Query", "SELECT TOP 5 c.id FROM c WHERE c.city = @city ORDER BY VectorDistance(c.embedding, @q)", code = true)
    private val policyCard = BoardCard("Python", "{\"path\": \"/embedding\", \"dataType\": \"float32\", \"dimensions\": 1536}", code = true)

    private val vectorCard = BoardCard("Vector", "3 steps east, 4 north: the vector (3, 4), a spot on the map.")
    private val dimensionCard = BoardCard("Dimension", "How many numbers in the list. A map spot has 2; these dishes 1,536.")
    private val embeddingCard = BoardCard("Embedding", "The spot an AI model gives a text. Similar meaning, nearby spots.")
    private val cosineCard = BoardCard("cosine (default)", "Arrows from the middle: same way 1, right angle 0, opposite minus 1.")
    private val dotCard = BoardCard("dot product", "(2, 3) and (4, 1): 2 times 4 plus 3 times 1 is 11. Bigger agrees more.")
    private val euclideanCard = BoardCard("euclidean", "A ruler between two pins: middle to (3, 4) reads 5. Lower is closer.")
    private val flatCard = BoardCard("flat", "Measure to every restaurant in town: never misses, but 505 numbers.")
    private val quantizedCard = BoardCard("quantizedFlat", "Each list squeezed like a compressed photo; up to about 50,000 in scope.")
    private val graphCard = BoardCard("Node and graph", "A node is one stored dish, joined to near neighbours. The web is a graph.")
    private val annCard = BoardCard("Approximate nearest neighbour", "Hop dot to closer dot: very close, not guaranteed perfect. Past 50,000.")
    private val ragCard = BoardCard("RAG", "Retrieve the closest dishes, put them in the prompt, answer only from them.")
    private val freshCard = BoardCard("Keeping it fresh", "The photo function asks the Taste service for a new embedding.")
    private val sameModelCard = BoardCard("Same model for both", "Each model draws its own map. Spots from two maps don't compare.")
    private val nearestCard = BoardCard("Nearest is not relevant", "It always returns five dishes. Check the scores.")
    private val todayCard = BoardCard("Today", "Enable vector search, a quantizedFlat container, five dishes, one search.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-vector",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is real now..."
            Scene(
                0, "Zaiqa so far", openZaiqa(),
                beats = listOf(
                    Beat(0, show = setOf("app", "api", "sql", "cos"), light = setOf("api"), card = BoardCard("Zaiqa so far", "App, API on Container Apps, Azure SQL for orders, Cosmos for the feed.")),
                    Beat(2, show = setOf("new"), light = setOf("new"), pulse = "api" to "new", card = BoardCard("Like dal makhani, but vegan", "The menu says creamy black lentils. Not one word in common.")),
                    Beat(4, light = setOf("sql"), pulse = "api" to "sql", card = BoardCard("Why not SQL?", "SQL holds what must be correct. Searching words there is still words.")),
                ),
            ),
            // 6: the town map
            Scene(
                6, "Meaning becomes numbers", mapPicture(),
                beats = listOf(
                    Beat(6, show = setOf("mid", "a"), light = setOf("a"), pulse = "mid" to "a", card = vectorCard),
                    Beat(8, show = setOf("dims"), light = setOf("dims"), card = dimensionCard),
                    Beat(10, show = setOf("b"), light = setOf("b"), pulse = "a" to "b", card = embeddingCard),
                    Beat(12, light = setOf("a", "b"), card = sameModelCard),
                ),
            ),
            // 14: vector search in Cosmos DB, and the account
            Scene(
                14, "A new container", container(),
                beats = listOf(
                    Beat(14, show = setOf("acct"), light = setOf("acct"), card = BoardCard("Enable it on the account", "Vector search is switched on once. It takes fifteen minutes.")),
                    Beat(16, show = setOf("cont"), light = setOf("cont"), pulse = "acct" to "cont", card = BoardCard("Set at creation", "The vector settings go on when you create it. Never later.")),
                    Beat(18, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = policyCard),
                ),
            ),
            // 19: "Distance function. That sounds like maths homework."
            Scene(
                19, "How close is close?", distance(),
                beats = listOf(
                    Beat(20, show = setOf("q", "cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(24, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(26, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 28: the indexing policy
            Scene(
                28, "Leave the vectors out", container(),
                beats = listOf(
                    Beat(28, show = setOf("cont", "excl"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Exclude the vector path", "Or every dish you save pays to index all 1,536, one by one.")),
                    Beat(30, show = setOf("index"), light = setOf("index"), pulse = "cont" to "index", card = BoardCard("Vector index", "A second index instead, built for finding the nearest spots.")),
                ),
            ),
            // 31: "Let me guess. Three kinds."
            Scene(
                31, "Which vector index?", indexes(),
                beats = listOf(
                    Beat(32, show = setOf("dims", "flat"), light = setOf("flat"), pulse = "dims" to "flat", card = flatCard),
                    Beat(34, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(36, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk", card = BoardCard("In scope", "The dishes one search looks at. Zaiqa filters to one city first.")),
                ),
            ),
            // 37: "DiskANN. What's that, in plain words?"
            Scene(
                37, "Hopping through a web", web(),
                beats = listOf(
                    Beat(38, show = setOf("a", "b", "c"), light = setOf("a", "b"), pulse = "a" to "b", card = graphCard),
                    Beat(40, show = setOf("q", "d"), light = setOf("d"), pulse = "b" to "d", card = BoardCard("Nearest neighbour", "The dish closest to what was typed. Hop, don't knock on doors.")),
                    Beat(42, light = setOf("d"), card = annCard),
                ),
            ),
            // 43: "Okay, container ready. How does a search actually run?"
            Scene(
                43, "The search itself", zaiqaSearch(),
                beats = listOf(
                    Beat(44, show = setOf("user", "api", "emb"), light = setOf("api"), pulse = "api" to "emb", card = BoardCard("Taste service", "The small Python service that embeds what the diner typed.")),
                    Beat(46, show = setOf("db"), light = setOf("db"), pulse = "api" to "db", card = sqlCard),
                    Beat(48, light = setOf("db"), card = BoardCard("Filter by city", "Dishes sit in drawers by city. One drawer, called a partition.")),
                    Beat(50, light = setOf("db"), card = BoardCard("TOP 5", "Without it the engine scores far more dishes than you need.")),
                    Beat(52, show = setOf("chat"), light = setOf("chat"), pulse = "db" to "chat", card = ragCard),
                ),
            ),
            // 55: "And when a restaurant adds a new dish?"
            Scene(
                55, "Keeping the vectors fresh", fresh(),
                beats = listOf(
                    Beat(56, show = setOf("photo", "fn", "taste", "cos"), light = setOf("fn"), pulse = "photo" to "fn", card = BoardCard("Photo function", "A photo lands in Blob Storage: thumbnail, then a new embedding.")),
                    Beat(58, show = setOf("sql"), light = setOf("taste", "cos"), pulse = "taste" to "cos", card = BoardCard("Change the model", "Then every dish must be embedded again, all four hundred thousand.")),
                ),
            ),
            // 59: "So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Each phrase points at one answer: diskANN, quantizedFlat, TOP N.")),
                    Beat(62, card = todayCard),
                ),
            ),
        ),
    )

    private fun caseDiagram() = DiagramSpec(
        "The case: dishes embedded in March with one model, searches on launch night with another, and a tripled bill.",
        nodes = listOf(
            DNode("typed", "Someone types", 0.25f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "dal makhani, vegan"),
            DNode("got", "Zaiqa returns", 0.75f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "pancakes"),
            DNode("mar", "March dishes", 0.25f, 0.6f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "the old model"),
            DNode("new", "Launch night model", 0.75f, 0.6f, w = 0.44f, h = 0.2f, tone = 1, sub = "another map"),
            DNode("bill", "The bill tripled", 0.5f, 0.9f, w = 0.5f, h = 0.16f, kind = NodeKind.NOTE, sub = "too many RUs"),
        ),
        edges = listOf(
            DEdge("typed", "new", "embedded by"),
            DEdge("new", "got", "nonsense", dashed = true),
            DEdge("mar", "got", "nearest"),
            DEdge("got", "bill", "every search"),
        ),
        aspect = 0.8f,
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-vector--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Search That Loved Pancakes."
            Scene(
                0, "The case", caseDiagram(),
                beats = listOf(
                    Beat(0, show = setOf("typed", "got"), light = setOf("typed", "got"), card = BoardCard("The case", "Something like dal makhani, but vegan. Zaiqa returns pancakes.")),
                    Beat(2, light = setOf("typed"), card = BoardCard("Meaning, not words", "The menu says creamy black lentils, no dairy. No shared word.")),
                ),
            ),
            // 4: the map and the embedding
            Scene(
                4, "Meaning becomes numbers", mapPicture(),
                beats = listOf(
                    Beat(4, show = setOf("mid", "a", "b"), light = setOf("b"), pulse = "mid" to "a", card = embeddingCard),
                    Beat(6, show = setOf("dims"), light = setOf("dims"), card = dimensionCard),
                    Beat(8, light = setOf("a", "b"), card = vectorCard),
                ),
            ),
            // 9: "Then my first suspect is the distance function"
            Scene(
                9, "Suspect one: the distance", distance(),
                beats = listOf(
                    Beat(10, show = setOf("q", "cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(12, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(14, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 16: "Here's the clue. The dishes were embedded in March..."
            Scene(
                16, "The clue: two maps", caseDiagram(),
                beats = listOf(
                    Beat(16, show = setOf("typed", "mar", "new"), light = setOf("mar", "new"), pulse = "typed" to "new", card = BoardCard("Two models, two maps", "March dishes on one map, launch-night searches on another.")),
                    Beat(18, show = setOf("got"), light = setOf("got"), pulse = "new" to "got", card = BoardCard("Same model for both", "Change models, and every dish must be embedded again.")),
                ),
            ),
            // 20: the bill
            Scene(
                20, "The bill", query(),
                beats = listOf(
                    Beat(20, show = setOf("q", "rank"), light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("Too many RUs", "Request units: the price tag Cosmos DB puts on every call.")),
                    Beat(22, light = setOf("rank"), card = BoardCard("ORDER BY VectorDistance", "It scores every dish in the country, then ranks them all.")),
                    Beat(24, show = setOf("top"), light = setOf("top"), pulse = "rank" to "top", card = BoardCard("TOP N", "Only the top few. Without it the engine scores far more.")),
                    Beat(26, show = setOf("filter"), light = setOf("filter"), pulse = "filter" to "rank", card = BoardCard("WHERE city", "One drawer of dishes, called a partition, not all of them.")),
                ),
            ),
            // 28: the indexing policy and a new container
            Scene(
                28, "A new container", container(),
                beats = listOf(
                    Beat(28, show = setOf("cont", "excl"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Exclude the vector path", "It was in the normal index, so every save cost far more.")),
                    Beat(30, light = setOf("cont"), card = BoardCard("Set at creation", "Vector settings go on a new container, never an existing one.")),
                    Beat(32, show = setOf("acct", "policy"), light = setOf("acct", "policy"), pulse = "acct" to "cont", card = policyCard),
                ),
            ),
            // 33: "Which vector index? I'll say flat."
            Scene(
                33, "Which vector index?", indexes(),
                beats = listOf(
                    Beat(34, show = setOf("dims", "flat"), light = setOf("flat"), pulse = "dims" to "flat", card = flatCard),
                    Beat(36, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(38, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk", card = BoardCard("diskANN", "Past 50,000 in scope: a nationwide search with no city filter.")),
                    Beat(40, light = setOf("disk"), card = annCard),
                ),
            ),
            // 41: "And the diner sees five dishes. That's it?"
            Scene(
                41, "What the diner gets", rag(),
                beats = listOf(
                    Beat(42, show = setOf("dishes", "prompt", "chat", "ans"), light = setOf("prompt"), pulse = "dishes" to "prompt", card = ragCard),
                    Beat(44, light = setOf("ans"), card = freshCard),
                ),
            ),
            // 45: "So, the case summary for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = all6, card = BoardCard("Case closed", "Nonsense results, too many RUs, 1,536 with flat, a new container.")),
                    Beat(48, card = todayCard),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-vector--gossip",
        listOf(
            // 0: "did you hear what happened on the Zaiqa team this week?"
            Scene(
                0, "The week on the Zaiqa team", meaning(),
                beats = listOf(
                    Beat(0, show = setOf("q", "dish"), light = setOf("q", "dish"), card = BoardCard("The week", "They shipped the search that answers, like dal makhani but vegan.")),
                    Beat(2, show = setOf("model"), light = setOf("model"), pulse = "q" to "model", card = BoardCard("Meaning, not words", "The menu says creamy black lentils. Nobody types that.")),
                    Beat(4, show = setOf("vec"), light = setOf("vec"), pulse = "model" to "vec", card = vectorCard),
                    Beat(6, light = setOf("vec"), card = embeddingCard),
                    Beat(8, light = setOf("vec"), card = dimensionCard),
                ),
            ),
            // 9: "So who went first?" Tom.
            Scene(
                9, "Monday: Tom", container(),
                beats = listOf(
                    Beat(10, show = setOf("cont"), light = setOf("cont"), card = BoardCard("Tom's Monday", "He tried to switch vector search on for last year's container.")),
                    Beat(12, show = setOf("acct"), light = setOf("acct"), pulse = "acct" to "cont", card = BoardCard("Account first, then a new one", "Enable it on the account, then set the vectors at creation.")),
                    Beat(14, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = policyCard),
                ),
            ),
            // 15: "Go on then, one at a time."
            Scene(
                15, "The three distances", distance(),
                beats = listOf(
                    Beat(16, show = setOf("q", "cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(18, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(20, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 21: "And Tuesday?" Dana.
            Scene(
                21, "Tuesday: Dana", container(),
                beats = listOf(
                    Beat(22, show = setOf("cont"), light = setOf("cont"), card = BoardCard("Dana's Tuesday", "Four hundred thousand dishes loaded, and every save cost more.")),
                    Beat(24, show = setOf("excl"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Exclude the vector path", "By default Cosmos DB indexes every field, all 1,536 numbers.")),
                    Beat(26, show = setOf("index"), light = setOf("index"), pulse = "cont" to "index", card = BoardCard("Vector index", "A second index instead, built for finding the nearest spots.")),
                ),
            ),
            // 27: "Which vector index? I sense a Wednesday."
            Scene(
                27, "Wednesday: Marcus", indexes(),
                beats = listOf(
                    Beat(28, show = setOf("dims", "flat"), light = setOf("flat"), pulse = "dims" to "flat", card = flatCard),
                    Beat(30, light = setOf("flat"), card = BoardCard("505 numbers", "Marcus had 1,536. The container said no, and the floor heard it.")),
                    Beat(32, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(34, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk", card = graphCard),
                    Beat(36, light = setOf("disk"), card = annCard),
                ),
            ),
            // 37: "Thursday. Go."
            Scene(
                37, "Thursday: the intern", query(),
                beats = listOf(
                    Beat(38, show = setOf("q", "rank"), light = setOf("rank"), pulse = "q" to "rank", card = BoardCard("Lovely matches, huge bill", "It sorted every dish by VectorDistance, with no TOP at all.")),
                    Beat(40, light = setOf("rank"), card = sqlCard),
                    Beat(42, show = setOf("top", "filter"), light = setOf("top", "filter"), pulse = "rank" to "top", card = BoardCard("TOP 5 and WHERE city", "Only the top few, in one drawer of dishes, called a partition.")),
                ),
            ),
            // 43: "And Friday? There's always a Friday."
            Scene(
                43, "Friday: Priya", meaning(),
                beats = listOf(
                    Beat(44, show = setOf("q", "dish", "model"), light = setOf("model"), pulse = "q" to "model", card = BoardCard("One model, not two", "A newer model for searches, the old one for dishes. Sourdough.")),
                    Beat(46, show = setOf("vec"), light = setOf("vec"), pulse = "model" to "vec", card = sameModelCard),
                    Beat(48, light = setOf("vec"), card = ragCard),
                    Beat(50, light = setOf("model"), card = freshCard),
                    Beat(52, light = setOf("dish"), card = nearestCard),
                ),
            ),
            // 53: "So, the gossip I should remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = all6, card = BoardCard("Listen for the phrase", "A new container, flat's 505, quantizedFlat, diskANN, TOP N.")),
                    Beat(56, card = todayCard),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-vector--late-night",
        listOf(
            // 0: "Hey, you. It's late, the kitchen's shut..."
            Scene(
                0, "Tonight: finding by meaning", mapPicture(),
                beats = listOf(
                    Beat(0, show = setOf("mid"), light = setOf("mid"), card = BoardCard("Tonight", "Someone hungry at midnight types, like dal makhani but vegan.")),
                    Beat(4, show = setOf("a"), light = setOf("a"), pulse = "mid" to "a", card = vectorCard),
                    Beat(6, show = setOf("dims"), light = setOf("dims"), card = dimensionCard),
                    Beat(8, show = setOf("b"), light = setOf("b"), pulse = "a" to "b", card = embeddingCard),
                ),
            ),
            // 10: "Right beside the dishes, in Cosmos DB..."
            Scene(
                10, "Where the vectors live", openZaiqa(),
                beats = listOf(
                    Beat(10, show = setOf("app", "api", "sql", "cos"), light = setOf("cos"), pulse = "api" to "cos", card = BoardCard("Beside the order feed", "Dishes and vectors in Cosmos DB; menus and money in Azure SQL.")),
                    Beat(12, light = setOf("cos"), card = sameModelCard),
                ),
            ),
            // 14: "That's the distance function..."
            Scene(
                14, "How near is near?", distance(),
                beats = listOf(
                    Beat(16, show = setOf("q", "cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(18, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(20, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 22: enable, then a new container
            Scene(
                22, "A new container", container(),
                beats = listOf(
                    Beat(22, show = setOf("acct", "cont"), light = setOf("acct"), pulse = "acct" to "cont", card = BoardCard("Account, then a container", "Fifteen minutes to enable; vector settings only at creation.")),
                    Beat(24, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = policyCard),
                    Beat(26, show = setOf("excl"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Exclude the vector path", "Or every dish you save pays to index all 1,536 numbers.")),
                ),
            ),
            // 27: "And there's more than one kind."
            Scene(
                27, "Three vector indexes", indexes(),
                beats = listOf(
                    Beat(28, show = setOf("dims", "flat"), light = setOf("flat"), pulse = "dims" to "flat", card = flatCard),
                    Beat(30, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(32, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk", card = graphCard),
                    Beat(34, light = setOf("disk"), card = annCard),
                    Beat(36, light = setOf("qf"), card = BoardCard("Zaiqa picks quantizedFlat", "Four hundred thousand dishes, a few thousand in scope per city.")),
                ),
            ),
            // 37: "And the search itself? What do I write?"
            Scene(
                37, "The query", query(),
                beats = listOf(
                    Beat(38, show = setOf("q", "rank"), light = setOf("q"), pulse = "q" to "rank", card = BoardCard("Taste service", "The small Python service that embeds what the diner typed.")),
                    Beat(40, show = setOf("filter", "top"), light = setOf("filter", "top"), pulse = "rank" to "top", card = sqlCard),
                    Beat(42, light = setOf("top"), card = BoardCard("TOP 5", "Without it the engine scores far more dishes, and RUs climb.")),
                ),
            ),
            // 43: RAG, then tomorrow's menus
            Scene(
                43, "Grounded, and kept fresh", rag(),
                beats = listOf(
                    Beat(44, show = setOf("dishes", "prompt", "chat", "ans"), light = setOf("prompt"), pulse = "prompt" to "chat", card = ragCard),
                    Beat(46, light = setOf("dishes"), card = freshCard),
                ),
            ),
            // 47: "Where does it stop working?"
            Scene(
                47, "Where it stops", limits(),
                beats = listOf(
                    Beat(48, show = setOf("close", "say"), light = setOf("close"), pulse = "close" to "say", card = nearestCard),
                ),
            ),
            // 49: "What do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Flat's 505, diskANN past 50,000, quantizedFlat, TOP N, one model.")),
                    Beat(52, card = todayCard),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-vector--couples",
        listOf(
            // 0: "Tonight is date night, darling."
            Scene(
                0, "Date night: what is close", mapPicture(),
                beats = listOf(
                    Beat(0, show = setOf("mid"), light = setOf("mid"), card = BoardCard("Date night", "The most romantic thing in Zaiqa: finding what is close.")),
                    Beat(2, show = setOf("a"), light = setOf("a"), pulse = "mid" to "a", card = BoardCard("Like dal makhani, but vegan", "The menu says creamy black lentils. Hardly a word in common.")),
                    Beat(4, show = setOf("dims"), light = setOf("dims"), card = vectorCard),
                    Beat(6, show = setOf("b"), light = setOf("b"), pulse = "a" to "b", card = embeddingCard),
                    Beat(8, light = setOf("dims"), card = dimensionCard),
                ),
            ),
            // 9: "Exam style, darling: two models."
            Scene(
                9, "Quiz: one model, or two?", meaning(),
                beats = listOf(
                    Beat(10, show = setOf("q", "dish", "model"), light = setOf("model"), pulse = "q" to "model", card = sameModelCard),
                    Beat(12, show = setOf("vec"), light = setOf("vec"), pulse = "model" to "vec", card = BoardCard("Right answer", "Two people have to be on the same map.")),
                ),
            ),
            // 13: cosine, dot product, euclidean
            Scene(
                13, "The three distances", distance(),
                beats = listOf(
                    Beat(13, show = setOf("q", "cos"), light = setOf("cos"), pulse = "q" to "cos", card = cosineCard),
                    Beat(16, show = setOf("dot"), light = setOf("dot"), pulse = "q" to "dot", card = dotCard),
                    Beat(18, show = setOf("euc"), light = setOf("euc"), pulse = "q" to "euc", card = euclideanCard),
                ),
            ),
            // 19: "Can I switch this on for any container I like?"
            Scene(
                19, "Commitment at creation", container(),
                beats = listOf(
                    Beat(20, show = setOf("acct", "cont"), light = setOf("cont"), pulse = "acct" to "cont", card = BoardCard("A new container", "Enable it on the account, then set the vectors at creation.")),
                    Beat(22, show = setOf("policy"), light = setOf("policy"), pulse = "cont" to "policy", card = policyCard),
                    Beat(23, show = setOf("excl"), light = setOf("excl"), pulse = "cont" to "excl", card = BoardCard("Quiz: saves got expensive", "The vector field was left in the normal index. All 1,536.")),
                ),
            ),
            // 25: "Now the vector indexes. Tell me slowly."
            Scene(
                25, "Which vector index?", indexes(),
                beats = listOf(
                    Beat(26, show = setOf("dims", "flat"), light = setOf("flat"), pulse = "dims" to "flat", card = flatCard),
                    Beat(28, show = setOf("qf"), light = setOf("qf"), pulse = "dims" to "qf", card = quantizedCard),
                    Beat(30, show = setOf("disk"), light = setOf("disk"), pulse = "dims" to "disk", card = graphCard),
                    Beat(32, light = setOf("disk"), card = annCard),
                    Beat(34, light = setOf("flat"), card = BoardCard("Quiz: 1,536 with flat", "No. Flat stops at 505. The exam's favourite trap.")),
                    Beat(36, light = setOf("qf"), card = BoardCard("Quiz: a few thousand in scope", "QuantizedFlat. DiskANN is for far more, with no city filter.")),
                ),
            ),
            // 39: "So how does a search run?"
            Scene(
                39, "The query", query(),
                beats = listOf(
                    Beat(40, show = setOf("q", "rank"), light = setOf("q"), pulse = "q" to "rank", card = BoardCard("Taste service", "Our small Python service embeds what the diner typed.")),
                    Beat(42, show = setOf("filter", "top"), light = setOf("filter", "top"), pulse = "filter" to "rank", card = sqlCard),
                    Beat(44, light = setOf("top"), card = BoardCard("Quiz: far too many RUs", "TOP N was missing. Without it the engine scores far more.")),
                ),
            ),
            // 47: "Zaiqa must answer from the dishes it found."
            Scene(
                47, "Grounded, and kept fresh", rag(),
                beats = listOf(
                    Beat(48, show = setOf("dishes", "prompt", "chat", "ans"), light = setOf("prompt"), pulse = "prompt" to "chat", card = ragCard),
                    Beat(51, light = setOf("dishes"), card = freshCard),
                ),
            ),
            // 52: "And where does it stop working?"
            Scene(
                52, "Where it stops", limits(),
                beats = listOf(
                    Beat(53, show = setOf("close", "say"), light = setOf("close"), pulse = "close" to "say", card = nearestCard),
                ),
            ),
            // 54: "Last question, for the exam."
            Scene(
                54, "For the exam", exam(),
                beats = listOf(
                    Beat(55, show = all6, light = all6, card = BoardCard("Listen for the phrase", "One model, TOP N, flat's 505, quantizedFlat, diskANN, new container.")),
                    Beat(58, card = todayCard),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
