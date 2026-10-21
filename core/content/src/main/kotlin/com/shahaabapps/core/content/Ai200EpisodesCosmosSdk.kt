package com.shahaabapps.core.content

/** Cosmos DB for NoSQL in Python: connect and query: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosSdk {
    private val classic = Podcast(
        "ai-cosmos-sdk",
        listOf(
            maya("Hi, and welcome back. Today is talking to a database from Python. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. Every learner saves study notes. Where do the notes live, and how does the backend fetch them cheaply?"),
            leo("In a database, I'm guessing. [chuckle] But which one?"),
            maya("Azure Cosmos DB for NoSQL. It's a database that stores documents instead of rows in fixed tables. Each document is JSON, a plain-text format of labelled values, like a filled-in form: id, user, topic, text."),
            leo("And one of those documents is called what?"),
            maya("An item. One study note is one item. Items live in a container, containers live in a database, and the database lives in an account. Picture a building, a room, a filing cabinet, and the cards inside it."),
            leo("Building, room, cabinet, card. What's special about the cabinet?"),
            maya("Every container has a partition key. That's one property, such as userId, whose value decides which slice of storage an item lives in. Picture one drawer per learner. All of learner 42's notes sit in learner 42's drawer."),
            leo("Why should I care which drawer?"),
            maya("Because going to one drawer is cheap, and opening every drawer is not. In Cosmos DB, every call is priced in request units, RUs: the price tag on each operation. The Cosmos performance episode goes deep on RUs. For today, fewer drawers opened means fewer RUs."),
            leo("So how do I pick a good partition key?"),
            maya("Pick a property with many different values, that most of your queries filter on. For the Coach, userId: thousands of learners, and each one reads their own notes. A classic mistake is topic. There are only a few topics, so a few drawers take all the load."),
            leo("Got it. How does my Python code actually connect?"),
            maya("Through the azure cosmos package, Microsoft's Python library for Cosmos DB. You make a CosmosClient, the object that talks to your account. And here's the important part: you sign in with Entra ID, Microsoft's sign-in service, not with a key."),
            leo("What's wrong with a key?"),
            maya("An account key is a master password for the whole account. If it leaks, anyone can read everything. So instead you pass DefaultAzureCredential, a helper that finds whatever sign-in is around. On your laptop it uses your az login, from the Azure command-line tool; in Azure, the app's managed identity, a login Azure gives your app, like a staff badge."),
            leo("Badge in hand. Can I do anything I like now?"),
            maya("Only what its role allows. A role is a set of permissions, and the identity needs a data-plane role, such as Cosmos DB Built-in Data Contributor. Data plane means the data itself: reading and writing items. It's a key to the filing cabinets, not to the builders' yard."),
            leo("Meaning?"),
            maya("It can't create databases or containers. A classic exam question: the app's start-up code calls create database if not exists, and fails with 403, which means forbidden. The fix isn't a key. Create the database and container ahead of time, with az, the Azure command-line tool, or a template, a file that describes what to build."),
            leo("Makes sense. Now let me save a note."),
            maya("Call upsert item, with the note as a Python dictionary. Upsert means update or insert: it inserts the note, or replaces it if one with the same id and partition key is already there."),
            leo("Same id and partition key. Why both?"),
            maya("Because an item's identity is the pair. The same id can exist in two different drawers. Note one for learner 42 and note one for learner 7 are different items."),
            leo("And to read a note back?"),
            maya("If you know both halves, the id and the partition key value, do a point read, with read item. It goes straight to the right drawer and the right card, no searching. For a small note, about one RU. It's the cheapest thing Cosmos DB offers."),
            leo("And if I don't know the id?"),
            maya("Then you query. Cosmos DB speaks SQL, the classic language for asking a database questions: select these fields, from here, where this is true. You call query items. It costs more than a point read, but it can filter and sort."),
            leo("So I write, where the user equals, and paste in the learner's id?"),
            maya("Never paste. That's the mistake of building SQL with an f-string, Python's way of dropping values into text. If a learner types something sneaky into a search box, it becomes part of your query. That's called injection. Instead, use parameters: blanks in the query, filled in separately, like fields on a form."),
            leo("Blanks on a form. Anything else about queries?"),
            maya("Give the partition key when you can. Then it's a single-partition query: one drawer. Leave it off, filtering on something else, and it's a cross-partition query, which fans out to every drawer and costs more. For example, the Coach's nightly topic report is its one cross-partition query."),
            leo("And if a learner has five hundred notes? I don't want them all at once."),
            maya("Then you page: ask for, say, fifty at a time. Each page comes with a continuation token, a bookmark. Hand it to the app, and when the learner scrolls, pass the bookmark back. The query resumes exactly where it stopped."),
            leo("Why not just remember the last note's id?"),
            maya("Because an id alone doesn't tell the query engine where it was, especially across drawers. The token does. One last call: patch item. It changes a few fields without sending the whole note back, like adding one to a view counter."),
            leo("So what do I remember for the exam?"),
            maya("If the question says the cheapest way to get one item whose id and key you know, a point read. If a data role gets 403 creating a database, data-plane roles can't create; use az or a template. If user text goes into a query, parameters, not f-strings. If the next page comes later, the continuation token. A filter off the partition key means cross-partition, which costs more."),
            leo("And anything to try today?"),
            maya("Create a serverless account, which bills only the RUs you use, then the coach database and a notes container on userId. Give yourself Built-in Data Contributor, and sign in with az login. Upsert three notes for two users, then point-read one. Then run a parameterized single-partition query and a cross-partition one, and compare the results."),
            leo("One drawer, not every drawer. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-sdk",
        listOf(
            maya("Welcome to Case Files. Today's case: The Bill That Ate the Budget. A small Android app called Pocket Quiz lets students save quiz cards. Its Python backend on Azure keeps the cards in Cosmos DB. Launch week went badly. First it wouldn't start. Then it was slow. Then the bill arrived."),
            leo("[chuckle] Three crimes in one week. Detective Leo, on the case. First, what's Cosmos DB, in plain words?"),
            maya("Azure Cosmos DB for NoSQL is a database that stores documents, not rows in fixed tables. Each document is JSON, a plain-text format of labelled values, like a filled-in form. One quiz card is one document, called an item."),
            leo("And where do the items live?"),
            maya("In a container, inside a database, inside an account. Picture a building, a room, a filing cabinet, and the cards inside. The backend talks to it through the azure cosmos package, Microsoft's Python library, using an object called CosmosClient."),
            leo("So, crime one. It wouldn't start."),
            maya("The start-up code called create database if not exists, and got 403, which means forbidden. The backend signs in with DefaultAzureCredential, a helper that finds whatever sign-in is around. In Azure, that's the app's managed identity, a login Azure gives the app, like a staff badge."),
            leo("First suspect: the badge isn't enough. Give it the account key as well."),
            maya("[pause] Wrong suspect. An account key is a master password for the whole account. Adding one puts back the very secret the team removed. And the badge worked fine for reading cards. Only creating things was refused."),
            leo("Second suspect: managed identities just can't use Cosmos DB."),
            maya("They can. Look at the role, the set of permissions on the badge. It was Cosmos DB Built-in Data Contributor, a data-plane role. Data plane means the data itself: reading and writing items. It's a key to the cabinets, not to the builders' yard. It can't create databases or containers."),
            leo("So how do you create them?"),
            maya("Ahead of time, with az, the Azure command-line tool, or a template, a file that describes what to build. The start-up code just uses what's there. Under the picture, Entra ID, Microsoft's sign-in service, hands the badge a short-lived token, and Cosmos DB checks that token's role on every call. Crime one, closed."),
            leo("Crime two. It was slow."),
            maya("Every container has a partition key: one property whose value decides which slice of storage an item lives in. Picture drawers. The team chose topic. Pocket Quiz has about six topics, and half the students revise maths."),
            leo("So one drawer did all the work."),
            maya("Exactly. Under the hood, Cosmos DB scrambles the key's value into a number, and that number picks a physical partition, a real slice of storage. Few values, few slices, and the busy ones get hot. A good key has many values that most queries filter on. Like userId: thousands of students, each reading their own cards."),
            leo("Can they just change the key?"),
            maya("Not on the same container. The partition key is set when a container is made. They made a new container on userId and copied the cards across. Which brings us to crime three. The bill."),
            leo("Suspect: too many students. Success is expensive."),
            maya("Not this time. Every call is priced in request units, RUs, the price tag on each operation. And the most common call, opening one card, was a query, a question written in SQL, the classic database language: select from the cards where id equals this id. The Cosmos performance episode has the full price list."),
            leo("What's wrong with a query? It found the card."),
            maya("The app already knew both halves of the card's identity: its id, and the student's id, the partition key value. With both, you do a point read, with read item. Straight to the drawer and the card, no query engine at all. About one RU for a small card. The cheapest call there is."),
            leo("And the queries that are left?"),
            maya("Give the partition key when you can, and it's a single-partition query, one drawer. Leave it off, filtering on something else, and it's a cross-partition query, which fans out to every drawer and costs more. That's where a partition key stops helping."),
            leo("Any loose ends in the file?"),
            maya("Two. The search box. The developer built the query text with an f-string, Python's way of dropping values into text. Type something sneaky, and it becomes part of the query. That's injection."),
            leo("Suspect: base64-encode the input first."),
            maya("That hides the text, but it's still treated as part of the query. The fix is parameters: blanks in the query, filled in separately, like fields on a form. The values never touch the query text."),
            leo("And the second loose end?"),
            maya("The load more button. The app remembered the last card's id and started again from there, and cards went missing. An id alone doesn't tell the engine where it was. Each page comes with a continuation token, a bookmark. Hand that back, and the query resumes exactly where it stopped."),
            leo("And the phone app? Should it just talk to Cosmos DB directly?"),
            maya("Never with a key. A phone can't keep a secret. It calls your backend, and the backend, with its badge, talks to Cosmos DB. The change feed episode shows how to react when cards are added or changed."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If the question says one item whose id and key you know, the answer is a point read. If a data role gets 403 creating a database, data-plane roles can't create; use az or a template. If user text goes into a query, parameters. If the next page comes later, the continuation token. And a filter off the partition key is cross-partition, which costs more."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, cheaply. Create a serverless account, which bills only the RUs you use, then the coach database and a notes container on userId. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one. Run a parameterized single-partition query and a cross-partition one, and compare the results."),
            leo("One drawer at a time. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-sdk",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They moved the Two Tracks Coach's notes, for the study assistant in the Android app, into Cosmos DB, with a Python backend. And the finance team now knows everyone's name."),
            leo("[laugh] I heard the word invoice said very slowly in a meeting. Start at the start. What's Cosmos DB?"),
            maya("Azure Cosmos DB for NoSQL is a database that stores documents instead of rows in fixed tables. Each document is JSON, a plain-text format of labelled values, like a filled-in form. One note is one document, called an item."),
            leo("And where do items live?"),
            maya("In a container, inside a database, inside an account. Picture a filing cabinet in a room in a building. Every container has a partition key, one property, like userId, whose value decides which slice of storage an item lives in. One drawer per learner."),
            leo("And how does the backend reach the cabinet?"),
            maya("Through the azure cosmos package, Microsoft's Python library, with an object called CosmosClient. It signs in with DefaultAzureCredential, a helper that finds whatever sign-in is around: your az login, from the Azure command-line tool, on a laptop, or the app's managed identity in Azure, a login Azure gives your app, like a staff badge. No key."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Every time a learner opened a note, Tom's code ran a query, a question written in SQL, the classic database language: select from notes where the id equals this id."),
            leo("That sounds reasonable."),
            maya("It works. But every call in Cosmos DB costs request units, RUs, the price tag on each operation. And Tom already knew the note's id and the learner's id, the partition key value. With both halves, you do a point read, read item. Straight to the right card. About one RU for a small note, far cheaper than any query."),
            leo("Why does it need both ids?"),
            maya("Because an item's identity is the pair: the id plus the partition key value. The same id can exist in two different drawers. Saving works the same way. Upsert item, which means update or insert, puts the note in, or replaces the one with the same id and key."),
            leo("[sigh] Poor Tom. And Tuesday?"),
            maya("Tuesday was Dana. Dana built the search box. She dropped the learner's text straight into the query with an f-string, Python's way of putting values into text."),
            leo("And someone typed something sneaky."),
            maya("The intern did, to be fair, on purpose. That's injection: user text that becomes part of the query. The fix is parameters, blanks in the query that are filled in separately, like fields on a form. Dana fixed it by lunch, and bought the intern a muffin."),
            leo("I sense a Wednesday."),
            maya("[chuckle] Wednesday was Marcus. Marcus gave the backend's badge a role, a set of permissions: Cosmos DB Built-in Data Contributor. Then his start-up code called create container if not exists. And got 403. Forbidden."),
            leo("He had the contributor role, though."),
            maya("A data-plane role. Data plane means the data itself, reading and writing items. It can't create databases or containers. Under the hood, Entra ID, Microsoft's sign-in service, gives the badge a token, and Cosmos DB checks its role on every call. Create containers ahead of time, with az, the Azure command-line tool, or a template, a file that describes what to build."),
            leo("And Marcus tried the account key, didn't he."),
            maya("He did. An account key is a master password for everything. Security had words. Thursday, the intern again, bless them, made a test container with topic as the partition key."),
            leo("Is that bad?"),
            maya("There are six topics, and half the notes are about one of them. So one drawer did half the work. A good partition key has many different values that most queries filter on, like userId. Under the hood, the key's value is scrambled into a number that picks a physical slice of storage. Few values, few slices."),
            leo("Can't the intern just change the key?"),
            maya("Not on that container. The partition key is set when a container is made. So it's a new container on userId, and copy the notes across. Luckily it was only a test."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. The note list. She filtered on the learner but forgot to pass the partition key, so it ran as a cross-partition query. That fans out to every drawer, and costs more. Give the key, and it's a single-partition query, one drawer."),
            leo("Did anything go right?"),
            maya("Priya's paging, actually. The list loads fifty notes at a time. Each page comes with a continuation token, a bookmark. The app hands it back when the learner scrolls, and the query resumes exactly where it stopped. And patch item updates one field, like a view counter, without sending the whole note."),
            leo("Where did Finance land on all this?"),
            maya("Happier. The Cosmos performance episode has the rest of the RU story. And where it all stops: the phone app never talks to Cosmos DB with a key. It calls the backend, and the backend uses its badge. And the vector search episode shows the same notes found by meaning."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. If the question says one item whose id and key you know, a point read. If a data role gets 403 creating a database, data-plane roles can't create; use az or a template. If user text goes into a query, parameters, not f-strings. If the next page comes later, the continuation token. And a filter off the partition key is cross-partition, and costs more."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a serverless account, which bills only the RUs you use, then the coach database and a notes container on userId. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one. Run a parameterized single-partition query and a cross-partition one, and compare the results."),
            leo("Point reads and parameters. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-sdk",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about where your app keeps its memories."),
            ivy("Mm. Memories. At this hour that sounds dangerous, Nova."),
            nova("Only a little. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. Every learner saves study notes. They live in Azure Cosmos DB for NoSQL, a database that keeps documents, not rows in fixed tables."),
            ivy("Documents. Like letters in a drawer?"),
            nova("Close. Each one is JSON, a plain-text format of labelled values, like a filled-in form. One note is one document, called an item. Items live in a container, in a database, in an account. [pause] A card, in a filing cabinet, in a room, in a building."),
            ivy("And how does the cabinet know where to put each card?"),
            nova("The partition key. One property, like userId, whose value decides which slice of storage an item lives in. Think of one drawer per learner. All your notes, tucked into your drawer."),
            ivy("Tell me what's really happening there."),
            nova("Under the picture, Cosmos DB scrambles the key's value into a number, and that number picks a physical partition, a real slice of storage. So pick a key with many different values, that most queries filter on. Like userId. Pick topic, with only a few values, and a few drawers do all the work."),
            ivy("And why does it matter which drawer I open?"),
            nova("Because every call has a price. Request units, RUs, the price tag on each operation. One drawer is cheap. Every drawer is not. [pause] The Cosmos performance episode is another long night on RUs."),
            ivy("So how does my Python code get in? Quietly, I hope."),
            nova("Through the azure cosmos package, Microsoft's Python library, with a CosmosClient, the object that talks to your account. And no key. An account key is a master password for everything. Instead, DefaultAzureCredential, a helper that finds whatever sign-in is around."),
            ivy("Whatever sign-in? Which one?"),
            nova("On your laptop, your az login, from the Azure command-line tool. In Azure, the app's managed identity, a login Azure gives your app, like a staff badge with no password. Entra ID, Microsoft's sign-in service, gives the badge a short-lived token, and Cosmos DB checks it on every call."),
            ivy("And what does the badge let me do?"),
            nova("Only what its role says. A role is a set of permissions. You give it a data-plane role, such as Cosmos DB Built-in Data Contributor. Data plane means the data itself, reading and writing items. [pause] It opens the cabinets. It doesn't build new rooms."),
            ivy("And if my start-up code tries to build a room anyway?"),
            nova("Then create database if not exists comes back 403. Forbidden. That's where a data-plane role stops working. Create databases and containers ahead of time, with az, the Azure command-line tool, or a template, a file that describes what to build. Your code just uses them."),
            ivy("Now, saving a note. Gently."),
            nova("Upsert item. Upsert means update or insert. It puts the note in, or replaces it if one with the same id and partition key is already there. Because an item's identity is the pair. The same id can live in two different drawers."),
            ivy("And reading it back? Under the covers?"),
            nova("If you know both halves, the id and the partition key value, a point read, with read item. [pause] Straight to the drawer, straight to the card. About one RU for a small note. The cheapest thing Cosmos DB offers."),
            ivy("And why is a point read so cheap, really?"),
            nova("Because there's nothing to search. The key's number names the slice of storage, and the id names the card inside it. [pause] No query engine involved, so nothing to plan and nothing to scan."),
            ivy("And when I don't know the id?"),
            nova("You query. Cosmos DB speaks SQL, the classic language for asking a database questions: select this, from there, where that. Give the partition key, and it's a single-partition query, one drawer. Leave it off, and it's a cross-partition query. It fans out to every drawer, and costs more."),
            ivy("And if a stranger types into my search box?"),
            nova("Never drop their words into the query with an f-string, Python's way of pasting values into text. That invites injection, where their text becomes part of your query. Use parameters instead: blanks in the query, filled in separately, like fields on a form."),
            ivy("And when there are too many notes for one screen?"),
            nova("You page. Fifty at a time. Each page carries a continuation token, a bookmark. Hand it back later, and the query picks up exactly where it stopped. [pause] Like a book left open on the pillow."),
            ivy("And small changes? Just one little detail?"),
            nova("Patch item. It changes a few fields without sending the whole note, like adding one to a view counter. And one last edge: the phone app never talks to Cosmos DB with a key. It calls your backend, and the backend wears the badge. The change feed episode is for the night you want to react the moment a note changes."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. One item, id and key known: a point read. A data role, 403 creating a database: data-plane roles can't create, so az or a template. User text in a query: parameters, not f-strings. The next page later: the continuation token. A filter off the partition key: cross-partition, which costs more."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a serverless account, which bills only the RUs you use, then the coach database and a notes container on userId. Give yourself Built-in Data Contributor and sign in with az login. [pause] Upsert three notes for two users, then point-read one. Run a parameterized single-partition query and a cross-partition one, and compare the results."),
            ivy("Your own drawer, and a bookmark. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-sdk",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about where an app keeps its secrets. And its notes."),
            rafe("Secrets on a first date? I like where this is going."),
            nova("[laugh] Cosmos DB. Azure Cosmos DB for NoSQL is a database that stores documents, not rows in fixed tables. Each one is JSON, a plain-text format of labelled values, like a filled-in form. One document is called an item."),
            rafe("And our app is the Two Tracks Coach, the study assistant in the Android app, with a Python backend. One study note, one item. Where do they all live?"),
            nova("In a container, in a database, in an account. A card, in a filing cabinet, in a room, in a building. And every container has a partition key, one property whose value decides which slice of storage an item lives in. One drawer per learner."),
            rafe("I've always wanted my own drawer at your place."),
            nova("Then choose the key well. Many different values, that most queries filter on. Like userId. Under the hood, the value is scrambled into a number that picks a physical slice of storage. Few values, and a few drawers do all the work."),
            rafe("Then let me test you, darling. Exam style. The backend signs in with a managed identity, a login Azure gives the app, like a badge, holding Cosmos DB Built-in Data Contributor. Start-up calls create database if not exists, and gets 403, forbidden. Why?"),
            nova("Because Data Contributor is a data-plane role. A role is a set of permissions, and data plane means the data itself, reading and writing items. It opens the cabinets; it can't build new rooms. Create databases and containers ahead of time, with az, the Azure command-line tool, or a template, a file that describes what to build."),
            rafe("Right answer. Knowing your role is everything. [kiss]"),
            nova("Mm. And the badge itself. DefaultAzureCredential, a helper that finds whatever sign-in is around: your az login, from the Azure command-line tool, at home, the managed identity in Azure. Entra ID, Microsoft's sign-in service, gives it a short-lived token. No account key, the master password for everything."),
            rafe("No keys under the doormat. Agreed. My turn to be tested."),
            nova("You know a note's id and the learner's id, which is the partition key value. What's the cheapest way to fetch it?"),
            rafe("A query. Select the note where the id matches."),
            nova("No kiss for that one. A query, a question in SQL, the classic database language, works, but it costs more. Every call is priced in request units, RUs, the price tag on each operation. With both halves of the identity, you do a point read, read item. Straight to the drawer, straight to the card, about one RU."),
            rafe("Straight to the point. I should have known. Why both halves, though?"),
            nova("Because an item's identity is the id plus the partition key value. The same id can sit in two different drawers. And saving is upsert item: update or insert. In it goes, or it replaces the one already there."),
            rafe("Update or insert. Like moving in. If there's already a toothbrush in the drawer, mine replaces it."),
            nova("[chuckle] Exactly that. Same id, same drawer, replaced. And patch item is gentler. It changes one field, like a view counter, without sending the whole note."),
            rafe("So, darling. A search box puts the learner's text into a query. How should the code include it?"),
            nova("As a parameter: a blank in the query, filled in separately, like a field on a form. Never an f-string, Python's way of pasting values into text. That invites injection, where their text becomes part of your query."),
            rafe("Right answer. Never let a stranger finish your sentences. [kiss]"),
            nova("Then here's yours. The note list filters on the learner but leaves off the partition key. What happens?"),
            rafe("It becomes a cross-partition query. It fans out to every drawer and costs more. Pass the key, and it's a single-partition query, one drawer. Same answer, smaller bill."),
            nova("Right answer. One drawer at a time, Rafe. [kiss]"),
            rafe("Now, a long list. Too many notes for one screen. The app fetches one page now, and the next page later, in a new request. What does the backend hand back?"),
            nova("The continuation token. A bookmark. Hand it back, and the query resumes exactly where it stopped. Not the last note's id; an id alone doesn't tell the engine where it was."),
            rafe("Right answer. I always know where we left off. [kiss]"),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("The phone app never talks to Cosmos DB with a key. A phone can't keep a secret. It calls the backend, and the backend wears the badge. The Cosmos performance episode has the rest of the RU story."),
            nova("Smooth. And if I want the app to react the moment a note changes?"),
            rafe("Then the change feed episode, darling. Cosmos DB keeps an ordered record of every change, and your code can follow along, like reading over someone's shoulder."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("One item, id and key known: a point read. A data role, 403 creating a database: data-plane roles can't create; az or a template. User text in a query: parameters, not f-strings. The next page later: the continuation token. A filter off the partition key: cross-partition, and it costs more."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a serverless account, which bills only the RUs you use, then the coach database and a notes container on userId. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one. Run a parameterized single-partition query and a cross-partition one, and compare the results."),
            rafe("My own drawer, and a bookmark. Perfect night. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun hierarchy() = DiagramSpec(
        "The Coach API signs in with Entra ID, then works down from account to database to container, which holds the items.",
        nodes = listOf(
            DNode("app", "Coach API", 0.22f, 0.18f, w = 0.38f, h = 0.2f, tone = 1, sub = "CosmosClient"),
            DNode("acct", "Account", 0.72f, 0.14f, w = 0.46f, h = 0.18f, tone = 2, sub = "the building"),
            DNode("db", "Database: coach", 0.72f, 0.42f, w = 0.46f, h = 0.18f, sub = "the room"),
            DNode("cont", "Container: notes", 0.72f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "key: /userId"),
            DNode("item", "Item", 0.22f, 0.74f, w = 0.38f, h = 0.24f, kind = NodeKind.NOTE, sub = "one note as JSON"),
        ),
        edges = listOf(
            DEdge("app", "acct", "Entra token"),
            DEdge("acct", "db"),
            DEdge("db", "cont"),
            DEdge("cont", "item", "holds"),
        ),
        aspect = 0.8f,
    )

    private fun drawers() = DiagramSpec(
        "The partition key's value picks the drawer an item lives in: with userId, each learner has a drawer of their own.",
        nodes = listOf(
            DNode("key", "Partition key", 0.5f, 0.16f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "userId"),
            DNode("d1", "Drawer u-42", 0.17f, 0.72f, w = 0.3f, h = 0.26f, sub = "one learner"),
            DNode("d2", "Drawer u-7", 0.5f, 0.72f, w = 0.3f, h = 0.26f, sub = "one learner"),
            DNode("d3", "Drawer u-9", 0.83f, 0.72f, w = 0.3f, h = 0.26f, sub = "one learner"),
        ),
        edges = listOf(
            DEdge("key", "d1"),
            DEdge("key", "d2"),
            DEdge("key", "d3"),
        ),
        aspect = 0.8f,
    )

    private fun signin() = DiagramSpec(
        "No account key: DefaultAzureCredential uses the managed identity, Entra ID gives it a token, Cosmos DB checks its role.",
        nodes = listOf(
            DNode("app", "Backend", 0.26f, 0.16f, w = 0.46f, h = 0.2f, tone = 1, sub = "DefaultAzureCredential"),
            DNode("entra", "Entra ID", 0.76f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "short-lived token"),
            DNode("mi", "Managed identity", 0.26f, 0.5f, w = 0.46f, h = 0.2f, sub = "a badge, no password"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.5f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "checks the role"),
            DNode("key", "Account key", 0.5f, 0.86f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "a master password: not used"),
        ),
        edges = listOf(
            DEdge("app", "mi", "uses"),
            DEdge("mi", "entra", "token"),
            DEdge("app", "cosmos", "with the token"),
        ),
        aspect = 0.8f,
    )

    private fun roles() = DiagramSpec(
        "A data-plane role reads and writes items but cannot create databases or containers; create those ahead of time.",
        nodes = listOf(
            DNode("dp", "Data-plane role", 0.26f, 0.18f, w = 0.46f, h = 0.2f, tone = 1, sub = "Data Contributor"),
            DNode("items", "Items", 0.76f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "read and write"),
            DNode("create", "Create database", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "403, forbidden"),
            DNode("cli", "az or a template", 0.76f, 0.76f, w = 0.42f, h = 0.2f, sub = "ahead of time"),
        ),
        edges = listOf(
            DEdge("dp", "items", "allowed"),
            DEdge("dp", "create", "refused", dashed = true),
            DEdge("cli", "create", "does it"),
        ),
        aspect = 0.8f,
    )

    private fun reads() = DiagramSpec(
        "A point read goes straight to one item; a query in one partition is cheaper than one that fans out to every partition.",
        nodes = listOf(
            DNode("cont", "Container", 0.5f, 0.14f, w = 0.5f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "notes"),
            DNode("pr", "Point read", 0.26f, 0.5f, w = 0.46f, h = 0.2f, sub = "id + key, about 1 RU"),
            DNode("q1", "Single-partition", 0.76f, 0.5f, w = 0.44f, h = 0.2f, sub = "one drawer"),
            DNode("qx", "Cross-partition query", 0.5f, 0.85f, w = 0.6f, h = 0.2f, tone = 2, sub = "every drawer, more RUs"),
        ),
        edges = listOf(
            DEdge("cont", "pr"),
            DEdge("cont", "q1"),
            DEdge("cont", "qx"),
        ),
        aspect = 0.8f,
    )

    private fun query() = DiagramSpec(
        "User text goes into a query as a parameter, filled in separately, never pasted in with an f-string.",
        nodes = listOf(
            DNode("search", "Search box", 0.26f, 0.16f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the learner's text"),
            DNode("fstr", "f-string", 0.76f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "injection"),
            DNode("params", "Parameters", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 1, sub = "filled in separately"),
            DNode("sql", "Query text", 0.76f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "stays the same"),
        ),
        edges = listOf(
            DEdge("search", "params"),
            DEdge("search", "fstr", dashed = true),
            DEdge("params", "sql"),
        ),
        aspect = 0.8f,
    )

    private fun paging() = DiagramSpec(
        "Each page comes with a continuation token; the app hands it back later and the query resumes where it stopped.",
        nodes = listOf(
            DNode("page1", "Page 1", 0.26f, 0.18f, w = 0.46f, h = 0.2f, sub = "fifty notes"),
            DNode("token", "Continuation token", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "a bookmark"),
            DNode("app", "The app", 0.76f, 0.76f, w = 0.44f, h = 0.2f, kind = NodeKind.USER),
            DNode("page2", "Page 2, later", 0.26f, 0.76f, w = 0.46f, h = 0.2f, sub = "resumes there"),
        ),
        edges = listOf(
            DEdge("page1", "token"),
            DEdge("token", "app", "kept"),
            DEdge("app", "page2", "hands it back"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Cosmos DB SDK questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("pr", "Id and key known", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a point read"),
            DNode("role", "403 creating a database", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "az or a template"),
            DNode("prm", "User text in a query", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "parameters"),
            DNode("tok", "Next page, later", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "continuation token"),
            DNode("cross", "Filter off the key", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "cross-partition"),
            DNode("pk", "Few key values", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "pick userId"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("pr", "role", "prm", "tok", "cross", "pk")
    private val clientCode = BoardCard("Python", "CosmosClient(url, credential=DefaultAzureCredential())", code = true)
    private val upsertCode = BoardCard("Python", "container.upsert_item(note)", code = true)
    private val readCode = BoardCard("Python", "container.read_item(item=\"n-101\", partition_key=\"u-42\")", code = true)
    private val queryCode = BoardCard("Python", "WHERE c.userId = @user, with parameters=[{\"name\": \"@user\", ...}]", code = true)
    private val pageCode = BoardCard("Python", "pages = container.query_items(...).by_page(token)", code = true)
    private val today = BoardCard("Today", "Serverless account, Data Contributor, upsert three, point-read, two queries.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-sdk",
        listOf(
            // 0: "Hi, and welcome back. Today is talking to a database from Python..."
            Scene(
                0, "Where the notes live", hierarchy(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("The question", "Every learner saves notes. Where do they live, and how are they fetched?")),
                    Beat(2, show = setOf("item"), light = setOf("item"), card = BoardCard("Cosmos DB for NoSQL", "Stores documents, not table rows. Each is JSON, like a filled-in form.")),
                    Beat(4, show = setOf("acct", "db", "cont"), light = setOf("cont"), pulse = "db" to "cont", card = BoardCard("Account, database, container", "A building, a room, a filing cabinet. One note is one item.")),
                ),
            ),
            // 6: "Every container has a partition key..."
            Scene(
                6, "One drawer per learner", drawers(),
                beats = listOf(
                    Beat(6, show = setOf("key", "d1"), light = setOf("key", "d1"), pulse = "key" to "d1", card = BoardCard("Partition key", "One property whose value decides the drawer an item lives in.")),
                    Beat(8, show = setOf("d2", "d3"), light = setOf("d1"), card = BoardCard("RUs", "Request units: the price tag on every call. One drawer is cheap.")),
                    Beat(10, light = setOf("key"), card = BoardCard("A good key", "Many values, used by most queries: userId. Not topic.")),
                ),
            ),
            // 11: "Got it. How does my Python code actually connect?"
            Scene(
                11, "Signing in", signin(),
                beats = listOf(
                    Beat(12, show = setOf("app"), light = setOf("app"), card = clientCode),
                    Beat(14, show = setOf("key", "mi", "entra"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("No account key", "DefaultAzureCredential: az login on a laptop, managed identity in Azure.")),
                ),
            ),
            // 15: "Badge in hand. Can I do anything I like now?"
            Scene(
                15, "What the role allows", roles(),
                beats = listOf(
                    Beat(16, show = setOf("dp", "items"), light = setOf("dp", "items"), pulse = "dp" to "items", card = BoardCard("Data-plane role", "Built-in Data Contributor: read and write items.")),
                    Beat(18, show = setOf("create", "cli"), light = setOf("create", "cli"), pulse = "dp" to "create", card = BoardCard("403 at start-up", "Data-plane roles can't create. Use az or a template, ahead of time.")),
                ),
            ),
            // 19: "Makes sense. Now let me save a note."
            Scene(
                19, "Save and read", reads(),
                beats = listOf(
                    Beat(20, light = setOf("cont"), card = upsertCode),
                    Beat(22, card = BoardCard("Id plus partition key", "An item's identity. The same id can sit in two drawers.")),
                    Beat(24, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = readCode),
                    Beat(26, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("Query", "SQL: select, from, where. Costs more, but can filter and sort.")),
                ),
            ),
            // 27: "So I write, where the user equals, and paste in the learner's id?"
            Scene(
                27, "Never paste", query(),
                beats = listOf(
                    Beat(28, show = setOf("search", "fstr", "params", "sql"), light = setOf("params"), pulse = "search" to "params", card = queryCode),
                ),
            ),
            // 29: "Blanks on a form. Anything else about queries?"
            Scene(
                29, "One drawer or all", reads(),
                beats = listOf(
                    Beat(30, show = setOf("q1", "qx"), light = setOf("q1", "qx"), pulse = "cont" to "qx", card = BoardCard("Cross-partition query", "No partition key: it fans out to every drawer and costs more.")),
                ),
            ),
            // 31: "And if a learner has five hundred notes?..."
            Scene(
                31, "A bookmark", paging(),
                beats = listOf(
                    Beat(32, show = setOf("page1", "token"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark with each page. Pass it back to resume.")),
                    Beat(34, show = setOf("app", "page2"), light = setOf("page2"), pulse = "app" to "page2", card = BoardCard("And patch item", "Changes a few fields, like a view counter, without the whole note.")),
                ),
            ),
            // 35: "So what do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("pr", "role", "prm", "tok", "cross"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: point read, parameters, token.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-sdk--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Bill That Ate the Budget..."
            Scene(
                0, "The Bill That Ate the Budget", hierarchy(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("The case", "Pocket Quiz: first it wouldn't start, then slow, then the bill.")),
                    Beat(2, show = setOf("item"), light = setOf("item"), card = BoardCard("An item", "One quiz card: a JSON document, like a filled-in form.")),
                    Beat(4, show = setOf("acct", "db", "cont"), light = setOf("acct", "db", "cont"), pulse = "acct" to "db", card = clientCode),
                ),
            ),
            // 5: "So, crime one. It wouldn't start."
            Scene(
                5, "Crime one: 403", signin(),
                beats = listOf(
                    Beat(6, show = setOf("app", "mi"), light = setOf("app", "mi"), pulse = "app" to "mi", card = BoardCard("403 at start-up", "Create database if not exists: forbidden. The badge is a managed identity.")),
                    Beat(7, show = setOf("key"), light = setOf("key"), card = BoardCard("Suspect: the account key", "Cleared. A master password puts back the secret the team removed.")),
                    Beat(9, show = setOf("cosmos"), light = setOf("cosmos"), card = BoardCard("Suspect: identities can't", "Cleared. Reading cards worked fine. Only creating was refused.")),
                ),
            ),
            // 10: "They can. Look at the role..."
            Scene(
                10, "The role", roles(),
                beats = listOf(
                    Beat(10, show = setOf("dp", "items"), light = setOf("dp"), pulse = "dp" to "items", card = BoardCard("Data-plane role", "Data Contributor reads and writes items. It can't build new rooms.")),
                    Beat(12, show = setOf("create", "cli"), light = setOf("cli"), pulse = "cli" to "create", card = BoardCard("Create ahead of time", "With az or a template. Cosmos DB checks the token's role every call.")),
                ),
            ),
            // 13: "Crime two. It was slow."
            Scene(
                13, "Crime two: slow", drawers(),
                beats = listOf(
                    Beat(14, show = setOf("key", "d1", "d2", "d3"), light = setOf("key"), card = BoardCard("Partition key: topic", "Six topics, half the students on maths. One drawer did the work.")),
                    Beat(16, light = setOf("d1"), pulse = "key" to "d1", card = BoardCard("Under the hood", "The key's value becomes a number that picks a physical slice.")),
                    Beat(18, light = setOf("key"), card = BoardCard("A new container", "The key is set at creation. New container on userId, cards copied.")),
                ),
            ),
            // 19: "Suspect: too many students. Success is expensive."
            Scene(
                19, "Crime three: the bill", reads(),
                beats = listOf(
                    Beat(20, light = setOf("cont"), card = BoardCard("A query by id", "Every card opened with a query: where id equals this id. RUs climbed.")),
                    Beat(22, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = readCode),
                    Beat(24, show = setOf("q1", "qx"), light = setOf("q1", "qx"), pulse = "cont" to "qx", card = BoardCard("One drawer or every drawer", "With the key: single-partition. Without: cross-partition.")),
                ),
            ),
            // 25: "Any loose ends in the file?"
            Scene(
                25, "Loose ends", query(),
                beats = listOf(
                    Beat(26, show = setOf("search", "fstr"), light = setOf("fstr"), pulse = "search" to "fstr", card = BoardCard("An f-string", "Type something sneaky and it becomes part of the query: injection.")),
                    Beat(27, card = BoardCard("Suspect: base64", "Hides the text, but it's still treated as part of the query.")),
                    Beat(28, show = setOf("params", "sql"), light = setOf("params"), pulse = "search" to "params", card = queryCode),
                ),
            ),
            // 29: "And the second loose end?"
            Scene(
                29, "The load more button", paging(),
                beats = listOf(
                    Beat(30, show = setOf("page1", "token", "app", "page2"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "Not the last id: a bookmark that resumes exactly where it stopped.")),
                    Beat(32, light = setOf("app"), card = BoardCard("The phone app", "Never a key on a phone. It calls the backend, which wears the badge.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("pr", "role", "prm", "tok", "cross"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. One drawer at a time.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-sdk--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "The Coach team's week", hierarchy(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Notes into Cosmos DB", "And the finance team now knows everyone's name.")),
                    Beat(2, show = setOf("item"), light = setOf("item"), card = BoardCard("An item", "One note: a JSON document, like a filled-in form.")),
                    Beat(4, show = setOf("acct", "db", "cont"), light = setOf("cont"), pulse = "db" to "cont", card = BoardCard("Partition key", "One property, like userId, decides the drawer. One per learner.")),
                    Beat(6, light = setOf("app", "acct"), pulse = "app" to "acct", card = clientCode),
                ),
            ),
            // 7: "Okay. So who went first?"
            Scene(
                7, "Monday: Tom", reads(),
                beats = listOf(
                    Beat(8, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("A query by id", "Every note opened with a query: where id equals this id.")),
                    Beat(10, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = readCode),
                    Beat(12, card = upsertCode),
                ),
            ),
            // 13: "Poor Tom. And Tuesday?"
            Scene(
                13, "Tuesday: Dana", query(),
                beats = listOf(
                    Beat(14, show = setOf("search", "fstr"), light = setOf("fstr"), pulse = "search" to "fstr", card = BoardCard("An f-string", "The learner's text pasted straight into the query.")),
                    Beat(16, show = setOf("params", "sql"), light = setOf("params"), pulse = "search" to "params", card = queryCode),
                ),
            ),
            // 17: "I sense a Wednesday."
            Scene(
                17, "Wednesday: Marcus", roles(),
                beats = listOf(
                    Beat(18, show = setOf("dp", "create"), light = setOf("create"), pulse = "dp" to "create", card = BoardCard("403 at start-up", "Create container if not exists, with Data Contributor: forbidden.")),
                    Beat(20, show = setOf("items", "cli"), light = setOf("dp", "items"), pulse = "dp" to "items", card = BoardCard("Data-plane role", "Reads and writes items only. Create containers with az or a template.")),
                    Beat(22, light = setOf("cli"), card = BoardCard("Not the account key", "A master password for everything. Security had words.")),
                ),
            ),
            // 23: "Is that bad?"
            Scene(
                23, "Thursday: the intern", drawers(),
                beats = listOf(
                    Beat(24, show = setOf("key", "d1", "d2", "d3"), light = setOf("d1"), pulse = "key" to "d1", card = BoardCard("Partition key: topic", "Six topics, half the notes in one. One drawer did half the work.")),
                    Beat(26, light = setOf("key"), card = BoardCard("A new container", "The key is set at creation: a new container on userId.")),
                ),
            ),
            // 27: "And Friday? There's always a Friday."
            Scene(
                27, "Friday: Priya", reads(),
                beats = listOf(
                    Beat(28, show = setOf("q1", "qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("No partition key passed", "A cross-partition query: every drawer, more RUs.")),
                ),
            ),
            // 29: "Did anything go right?"
            Scene(
                29, "What went right", paging(),
                beats = listOf(
                    Beat(30, show = setOf("page1", "token", "app", "page2"), light = setOf("token"), pulse = "page1" to "token", card = pageCode),
                    Beat(32, light = setOf("app"), card = BoardCard("The phone app", "Never a key on a phone. It calls the backend, which has the badge.")),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("pr", "role", "prm", "tok", "cross"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be the story.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-sdk--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Where memories live", hierarchy(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Tonight", "Where your app keeps its memories.")),
                    Beat(2, show = setOf("item"), light = setOf("item"), card = BoardCard("Cosmos DB for NoSQL", "Keeps documents, not table rows. Each one is JSON.")),
                    Beat(4, show = setOf("acct", "db", "cont"), light = setOf("cont"), pulse = "db" to "cont", card = BoardCard("A card in a cabinet", "Item, container, database, account: card, cabinet, room, building.")),
                ),
            ),
            // 5: "And how does the cabinet know where to put each card?"
            Scene(
                5, "Your own drawer", drawers(),
                beats = listOf(
                    Beat(6, show = setOf("key", "d1"), light = setOf("key", "d1"), pulse = "key" to "d1", card = BoardCard("Partition key", "One property, like userId, decides which drawer an item lives in.")),
                    Beat(8, show = setOf("d2", "d3"), light = setOf("key"), card = BoardCard("Under the picture", "The value becomes a number that picks a physical slice of storage.")),
                    Beat(10, light = setOf("d1"), card = BoardCard("RUs", "Request units: the price tag on every call. One drawer is cheap.")),
                ),
            ),
            // 11: "So how does my Python code get in? Quietly, I hope."
            Scene(
                11, "Getting in quietly", signin(),
                beats = listOf(
                    Beat(12, show = setOf("app", "key"), light = setOf("app", "key"), card = clientCode),
                    Beat(14, show = setOf("mi", "entra"), light = setOf("mi", "entra"), pulse = "mi" to "entra", card = BoardCard("Managed identity", "A badge, no password. Entra ID gives it a short-lived token.")),
                ),
            ),
            // 15: "And what does the badge let me do?"
            Scene(
                15, "What the badge allows", roles(),
                beats = listOf(
                    Beat(16, show = setOf("dp", "items"), light = setOf("dp", "items"), pulse = "dp" to "items", card = BoardCard("Data-plane role", "Opens the cabinets: read and write items.")),
                    Beat(18, show = setOf("create", "cli"), light = setOf("create"), pulse = "dp" to "create", card = BoardCard("403, forbidden", "It doesn't build rooms. Create them ahead with az or a template.")),
                ),
            ),
            // 19: "Now, saving a note. Gently."
            Scene(
                19, "Saving and reading", reads(),
                beats = listOf(
                    Beat(20, light = setOf("cont"), card = upsertCode),
                    Beat(22, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = readCode),
                    Beat(24, card = BoardCard("Why so cheap", "The key names the slice, the id names the card. Nothing to scan.")),
                    Beat(26, show = setOf("q1", "qx"), light = setOf("q1", "qx"), pulse = "cont" to "qx", card = BoardCard("One drawer or every drawer", "With the key: single-partition. Without: cross-partition, more RUs.")),
                ),
            ),
            // 27: "And if a stranger types into my search box?"
            Scene(
                27, "Strangers at the door", query(),
                beats = listOf(
                    Beat(28, show = setOf("search", "fstr", "params", "sql"), light = setOf("params"), pulse = "search" to "params", card = queryCode),
                ),
            ),
            // 29: "And when there are too many notes for one screen?"
            Scene(
                29, "A book left open", paging(),
                beats = listOf(
                    Beat(30, show = setOf("page1", "token", "app", "page2"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark. Hand it back later; the query resumes there.")),
                    Beat(32, light = setOf("app"), card = BoardCard("Patch, and the phone", "Patch one field. The phone never holds a key; the backend does.")),
                ),
            ),
            // 33: "Mm. Before you send me off to bed..."
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("pr", "role", "prm", "tok", "cross"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-sdk--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", hierarchy(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Date night", "Where an app keeps its secrets. And its notes.")),
                    Beat(2, show = setOf("item"), light = setOf("item"), card = BoardCard("Cosmos DB for NoSQL", "Stores JSON documents, like filled-in forms. One is an item.")),
                    Beat(4, show = setOf("acct", "db", "cont"), light = setOf("cont"), pulse = "db" to "cont", card = BoardCard("A card in a cabinet", "Item, container, database, account. One drawer per learner.")),
                ),
            ),
            // 5: "I've always wanted my own drawer at your place."
            Scene(
                5, "My own drawer", drawers(),
                beats = listOf(
                    Beat(6, light = setOf("key", "d1"), pulse = "key" to "d1", card = BoardCard("Choose the key well", "Many values, used by most queries: userId. Few values, hot drawers.")),
                ),
            ),
            // 7: "Then let me test you, darling..."
            Scene(
                7, "Knowing your role", roles(),
                beats = listOf(
                    Beat(7, show = setOf("dp", "create"), light = setOf("create"), card = BoardCard("Quiz: 403 at start-up", "Data Contributor calls create database if not exists. Why 403?")),
                    Beat(8, show = setOf("items", "cli"), light = setOf("dp", "cli"), pulse = "dp" to "create", card = BoardCard("Right answer", "A data-plane role reads and writes items. Create with az or a template.")),
                ),
            ),
            // 10: "Mm. And the badge itself..."
            Scene(
                10, "The badge", signin(),
                beats = listOf(
                    Beat(10, light = setOf("mi", "entra"), pulse = "mi" to "entra", card = BoardCard("DefaultAzureCredential", "az login at home, managed identity in Azure. No account key.")),
                ),
            ),
            // 12: "You know a note's id and the learner's id..."
            Scene(
                12, "Straight to the point", reads(),
                beats = listOf(
                    Beat(12, light = setOf("cont"), card = BoardCard("Quiz: id and key known", "What is the cheapest way to fetch the note?")),
                    Beat(13, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("A query?", "It works, but it costs more RUs.")),
                    Beat(14, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = readCode),
                    Beat(16, card = upsertCode),
                    Beat(18, card = BoardCard("Patch item", "Changes one field, like a view counter, without the whole note.")),
                ),
            ),
            // 19: "So, darling. A search box puts the learner's text into a query..."
            Scene(
                19, "Finishing sentences", query(),
                beats = listOf(
                    Beat(19, show = setOf("search"), light = setOf("search"), card = BoardCard("Quiz: a search box", "How should the learner's text get into the query?")),
                    Beat(20, show = setOf("fstr", "params", "sql"), light = setOf("params"), pulse = "search" to "params", card = queryCode),
                ),
            ),
            // 22: "Then here's yours. The note list filters on the learner..."
            Scene(
                22, "One drawer at a time", reads(),
                beats = listOf(
                    Beat(22, show = setOf("qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("Quiz: no partition key", "The note list leaves off the key. What happens?")),
                    Beat(23, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("Right answer", "Cross-partition: every drawer. Pass the key: one drawer.")),
                ),
            ),
            // 25: "Now, a long list..."
            Scene(
                25, "Where we left off", paging(),
                beats = listOf(
                    Beat(25, show = setOf("page1"), light = setOf("page1"), card = BoardCard("Quiz: the next page, later", "What does the backend hand back to the app?")),
                    Beat(26, show = setOf("token", "app", "page2"), light = setOf("token"), pulse = "page1" to "token", card = pageCode),
                ),
            ),
            // 28: "And where does it all stop working, Rafe?..."
            Scene(
                28, "Every romance has limits", signin(),
                beats = listOf(
                    Beat(29, light = setOf("key"), card = BoardCard("No key on a phone", "The phone calls the backend; the backend wears the badge.")),
                    Beat(31, light = setOf("cosmos"), card = BoardCard("The change feed", "An ordered record of every change. Another episode.")),
                ),
            ),
            // 32: "Last question, for the exam. What do we remember?"
            Scene(
                32, "For the exam", exam(),
                beats = listOf(
                    Beat(33, show = all6, light = setOf("pr", "role", "prm", "tok", "cross"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
