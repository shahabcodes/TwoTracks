package com.shahaabapps.core.content

/** Azure Database for PostgreSQL: connect, model and index: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesPostgres {
    private val classic = Podcast(
        "ai-postgres",
        listOf(
            maya("Hi, and welcome back. Today is PostgreSQL on Azure. PostgreSQL is a popular free database that keeps data in tables, with rows and columns, like the SQLite database inside your Android app, but on a server, shared by every user. You talk to it in SQL, the standard language for asking a database for data."),
            leo("So it's like Room on my phone, only bigger and shared. Where does Azure come in?"),
            maya("Azure runs it for you. The product is Azure Database for PostgreSQL flexible server: ordinary PostgreSQL, where Azure handles the patches and the backups."),
            leo("And if the machine under it dies at three in the morning?"),
            maya("Azure keeps it up, or brings it back on another machine. You just pick the size."),
            leo("And my code talks to it how?"),
            maya("Say you're building the Two Tracks Coach, a study assistant in an Android app, with a Python backend on Azure. The backend uses psycopg, the standard PostgreSQL driver for Python. A driver is the library that carries your queries, your requests in SQL, to the database and brings the rows back."),
            leo("And it signs in with a user name and password?"),
            maya("It can, but then the password has to live somewhere safe, ideally Key Vault, Azure's safe for secrets. The better way is Microsoft Entra authentication. Entra ID is Azure's sign-in service, like the front desk at a festival: it knows who everyone is, and hands out wristbands."),
            leo("So with Entra, what does my app show at the gate?"),
            maya("A token. The backend runs with a managed identity, an identity Azure gives the app itself, like a staff badge."),
            leo("Hang on. Does my Android app get a badge too?"),
            maya("Never. The phone calls your backend, and only the backend, running in Azure, has the badge. With it, it asks Entra ID for a token: a short-lived signed pass, like a festival wristband, and sends that in the password field. Nothing secret is stored anywhere."),
            leo("A wristband. So it runs out?"),
            maya("After about an hour. So get a fresh token whenever you open new connections, new open lines to the database. In Python, DefaultAzureCredential does the asking. It's a helper from Azure's identity library: it uses the managed identity when running in Azure, or your own sign-in on your laptop."),
            leo("Okay, I'm in. What does the Coach store?"),
            maya("A notes table: columns for an id, the learner, the title, the text, and when it was made. And one flexible column called meta, of type jsonb."),
            leo("jsonb. What's that, in plain words?"),
            maya("JSON is a common text format for nested data, like a little form of labels and values: module, containers; source, lecture; tags, exam and tricky. jsonb stores it in a packed binary form, so PostgreSQL can search inside it quickly."),
            leo("And how do I make searches fast?"),
            maya("With indexes. An index is like the index at the back of a book: it finds rows without reading the whole table. The default kind is the B-tree index."),
            leo("B-tree. Picture it for me."),
            maya("A phone book. To find Patel, you open near P and narrow down, instead of reading from A. And everyone from Patel to Price sits together, already in order."),
            leo("So a B-tree finds one value, a range, and keeps things sorted. When do I reach for it?"),
            maya("To filter or sort on a plain column. For example, the notes screen shows one learner's notes, newest first. So, a B-tree on two columns: the learner's id first, then the created date. Like a phone book sorted by surname, then first name: all of Asha's notes together, already newest first."),
            leo("And for that jsonb column?"),
            maya("A GIN index. A B-tree compares whole values, like a phone book of whole sentences: it can't tell you which ones mention a word."),
            leo("And GIN can?"),
            maya("GIN indexes the parts inside a value: each key in jsonb, each item in a list, each word in a text. Like a book index listing every page each word is on."),
            leo("Give me an example."),
            maya("The GIN index on meta keeps an entry per tag: exam, notes 3, 17 and 42; tricky, notes 8 and 17. A search for notes tagged exam uses the contains operator, which asks: does this jsonb hold these keys and values? GIN reads one entry and has the answer. The small price: each save writes a few more entries."),
            leo("How do I know whether my index is even being used?"),
            maya("EXPLAIN. Put it before a query, and PostgreSQL shows its plan, like a sat-nav showing the route before you drive. EXPLAIN ANALYZE goes further: it really drives the route, and reports the real times and row counts."),
            leo("What am I looking for in the plan?"),
            maya("A Seq Scan, short for sequential scan, means it read the whole table, row by row, like reading a book cover to cover to find one name."),
            leo("Is that always bad?"),
            maya("For a filter that picks a few rows, it usually means a missing index. After the B-tree, you'd see an Index Scan, and the time fall from about 200 milliseconds to under one."),
            leo("Anything dangerous about it?"),
            maya("One thing. ANALYZE really runs the statement. Analyze an UPDATE or a DELETE, the commands that change or remove rows, and it really changes your data. So wrap it in a transaction, a group of changes you can cancel, like a shopping basket before you pay, and roll it back."),
            leo("Okay. Now, I heard connections cause trouble."),
            maya("They can. A connection is an open line between your app and the database. In PostgreSQL, every one is a whole separate program on the server."),
            leo("A whole program? In my code, opening a connection feels free."),
            maya("It isn't. Each costs a handshake, encryption and a sign-in, and thousands of idle ones eat the memory queries need. Picture the Coach backend scaled to forty copies in a study rush, each opening its own."),
            leo("And the server runs out."),
            maya("It hits its connection limit. The fix is pooling: sharing a few real connections among many users. Picture an office with three hundred staff and twenty phone lines. Nobody owns a line; you pick up a free one, make your call, and hang up. Most of the time, twenty is plenty."),
            leo("And for the Coach?"),
            maya("Flexible server has PgBouncer built in, a connection pooler: the switchboard for those phone lines."),
            leo("Do the sums for me. Forty copies of my backend."),
            maya("Forty copies times ten connections is four hundred. PgBouncer keeps maybe fifty real ones to PostgreSQL, and lends one out for each transaction, a few milliseconds, then takes it back for the next caller."),
            leo("How do I switch it on?"),
            maya("Set the server setting pgbouncer dot enabled to true; no restart needed. Then connect to the same host, the server's address, on port 6432 instead of 5432. A port is like a door number on the server. 5432 goes straight to PostgreSQL; 6432 goes through PgBouncer."),
            leo("[chuckle] Same building, different door. And inside my app?"),
            maya("Keep a small pool in the app too, instead of connecting for every request. One catch: built-in PgBouncer isn't available on Burstable, the cheap tier for testing and light loads. You need General Purpose, for most real apps, or Memory Optimized, for heavy queries."),
            leo("So what do I remember for the exam?"),
            maya("If the app has a managed identity, it sends an Entra token as the password. If built-in PgBouncer is on, connect to the same host on port 6432. If the search looks inside jsonb, a GIN index; a plain filter or sort, a B-tree. To see if an index is used, EXPLAIN ANALYZE. And no PgBouncer on Burstable."),
            leo("And anything to try today?"),
            maya("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. Create the notes table, insert a few thousand rows, and EXPLAIN ANALYZE a filter before and after a B-tree index. Then add a GIN index on the jsonb column, and check the contains search uses it."),
            leo("Pooled, indexed, and no passwords. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-postgres",
        listOf(
            maya("Welcome to Case Files. Today's case: The Exam Week Crawl. Study Lantern is a small Android revision app, with a Python backend on Azure. Its data lives in PostgreSQL, a popular free database that keeps data in tables of rows and columns. In exam week, the app slowed to a crawl, and then started failing."),
            leo("Detective Leo, on the case. What did the errors say?"),
            maya("Too many connections. A connection is an open line between the backend and the database, like a phone line. The backend runs as forty copies at peak, and each copy opened a new connection for every single request."),
            leo("Then my first suspect: the disk is too small. Buy a bigger disk."),
            maya("[pause] Wrong suspect. Every PostgreSQL connection is a whole separate program running on the server. Opening one costs a handshake, encryption and a sign-in. Thousands of them eat the memory the queries need. A bigger disk changes none of that."),
            leo("So, fewer connections. How?"),
            maya("Pooling: sharing a few real connections among many users. Picture an office with three hundred staff and twenty phone lines. Nobody owns a line; you pick up a free one, make your call, and hang up. Most of the time, twenty is plenty."),
            leo("And where's the switchboard?"),
            maya("Built in. The database is Azure Database for PostgreSQL flexible server, ordinary PostgreSQL that Azure runs for you: patches, backups and staying up are handled. And flexible server has PgBouncer built in."),
            leo("PgBouncer. Sounds like a nightclub. What is it?"),
            maya("[chuckle] Close. PgBouncer is a connection pooler: a small program on the same server that holds a few real connections to PostgreSQL and lends them out."),
            leo("How few? They had forty copies of the backend."),
            maya("Forty copies times ten connections is four hundred. PgBouncer serves them with maybe fifty real ones, each lent for one transaction, a group of changes, then handed straight back."),
            leo("Then switch it on. Case closed."),
            maya("The team did switch it on, with the server setting pgbouncer dot enabled. Nothing changed. That's our next clue."),
            leo("Suspect: it needs a restart."),
            maya("No restart is needed. Look at the app's connection string. It still says port 5432."),
            leo("And a port is what, exactly?"),
            maya("A door number on the server. 5432 goes straight to PostgreSQL. PgBouncer listens on 6432, on the same host, the same server address. Change that one number, and the requests finally go through the pool."),
            leo("And did that fix it?"),
            maya("Not on their tier. Built-in PgBouncer isn't available on Burstable, the cheap tier for testing and light loads."),
            leo("So they switched on a bouncer who never turned up for work."),
            maya("Exactly. They moved to General Purpose, for most real apps; Memory Optimized is the third, for heavy queries. And each copy of the app also got a small pool of its own, instead of connecting per request."),
            leo("Connections stable. What else is in the file?"),
            maya("Two slow screens. First, the notes list: one student's notes, newest first. To see why, they used EXPLAIN ANALYZE. Put it before a query, a request for data, and PostgreSQL runs it and shows its plan, like a sat-nav showing the route it took and the real journey time."),
            leo("And what did the plan say?"),
            maya("Seq Scan, short for sequential scan: it read the whole table, row by row, half a million rows to find twenty. Like reading a book cover to cover to find one name. That points to a missing index. An index is like the index at the back of a book: it finds rows without reading everything."),
            leo("Which index?"),
            maya("A B-tree index, the default kind. Picture a phone book: to find Patel, you open near P and narrow down, and a range like Patel to Price sits together."),
            leo("But that screen filters on two things: the student, and the date."),
            maya("So the B-tree goes on two columns: the student's id, then the created date, like surname, then first name. The plan became an Index Scan, and 200 milliseconds fell to under one."),
            leo("Second slow screen?"),
            maya("The tag filter. Each note has a column called meta, of type jsonb. JSON is a text format for nested data, like a form of labels and values; jsonb stores it packed, so PostgreSQL can search inside it. The screen runs a contains search: does meta hold this tag?"),
            leo("Easy. Another B-tree, on meta."),
            maya("[sigh] A B-tree compares whole values, like a phone book of whole sentences; it can't tell you which ones mention a word."),
            leo("Then what can look inside?"),
            maya("A GIN index. It indexes the parts inside a value, like a book index listing every page each word is on: tag exam, notes 3, 17 and 42. The catch: each save writes a few more entries."),
            leo("Any other suspects?"),
            maya("One that wasn't slow, just risky. The app kept the database password in an app setting. They switched to Microsoft Entra authentication. Entra ID is Azure's sign-in service, like the front desk at a festival that knows who everyone is."),
            leo("And what does the app show at the desk?"),
            maya("Its managed identity, an identity Azure gives the app itself, like a staff badge. With it, the app gets a token: a short-lived signed pass, like a festival wristband, sent as the password."),
            leo("And PostgreSQL just believes the wristband?"),
            maya("It checks Entra's signature on it, and that the name inside matches the user you connect as. No password left lying in a setting."),
            leo("And in Python?"),
            maya("psycopg, the standard PostgreSQL driver, sends it, and DefaultAzureCredential, a helper from Azure's identity library, fetches it. Wristbands expire after about an hour, so fetch a fresh one for new connections."),
            leo("Any loose ends?"),
            maya("Where this stops working. A phone app never connects straight to the database; it calls the backend, which does. And for searching notes by meaning, the pgvector episode takes PostgreSQL further. The Cosmos SDK episode shows the other database the Coach uses."),
            leo("So, the case summary for the exam?"),
            maya("If the app has a managed identity, it sends an Entra token as the password. If built-in PgBouncer is on, connect to the same host on port 6432. If the search looks inside jsonb, a GIN index; a plain filter or sort, a B-tree. To see if an index is used, EXPLAIN ANALYZE. And no PgBouncer on Burstable."),
            leo("Case closed. Anything to try today?"),
            maya("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. Create the notes table, insert a few thousand rows, and EXPLAIN ANALYZE a filter before and after a B-tree index. Then add a GIN index on the jsonb column, and check the contains search uses it."),
            leo("Exam week, survived. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-postgres",
        listOf(
            maya("Okay, did you hear what happened when the Coach team moved notes to PostgreSQL? Pull up a chair. The Two Tracks Coach, the study assistant in the Android app, with its Python backend on Azure, now keeps notes in PostgreSQL, a popular free database of tables, rows and columns. And it was a week."),
            leo("[laugh] I heard someone say the word bouncer in a meeting. Where does PostgreSQL even run?"),
            maya("On Azure Database for PostgreSQL flexible server. That's ordinary PostgreSQL that Azure runs for you: patches, backups and staying up are handled, and you pick the size. The backend talks to it with psycopg, the standard PostgreSQL driver for Python, the library that carries queries, requests for data, over and brings rows back."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom's code opened a brand-new connection for every request. A connection is an open line to the database, like a phone line. And by lunch, during the study rush, the server hit its connection limit."),
            leo("Is a connection that expensive?"),
            maya("In PostgreSQL, yes. Each one is a whole separate program on the server, costly to open and hungry for memory."),
            leo("Huh. In my own code I'd never think twice about opening one."),
            maya("Neither did Tom. The fix is pooling: sharing a few real connections, like an office with three hundred staff and twenty phone lines. Pick up a free one, call, hang up. Tom now keeps a small pool in each copy of the app."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. Dana switched on PgBouncer, the connection pooler built into flexible server: a small program on the same server that holds a few real connections and lends them out."),
            leo("Four hundred app connections, squeezed into how many real ones?"),
            maya("Maybe fifty. She set pgbouncer dot enabled to true. And nothing changed."),
            leo("Why not?"),
            maya("Because the app still connected to port 5432. A port is like a door number on the server. 5432 goes straight to PostgreSQL. PgBouncer waits behind door 6432, on the same host, the same server address. Dana changed one number, and the connections flattened out."),
            leo("[chuckle] One number. Wednesday?"),
            maya("Wednesday, Marcus. The tag filter was slow. Notes keep their tags in a column called meta, of type jsonb. JSON is a text format for nested data, labels and values; jsonb stores it packed, so PostgreSQL can search inside it. The filter is a contains search: does meta hold this tag?"),
            leo("And what did Marcus do?"),
            maya("Added a B-tree index on meta. An index is like a book's index: it finds rows without reading everything."),
            leo("And a B-tree is the phone-book one?"),
            maya("The default kind, yes: open near P for Patel, narrow down, and everyone from Patel to Price sits together. Great for an equals filter, a range or a sort."),
            leo("So why didn't it work?"),
            maya("A B-tree compares whole values, like a phone book of whole sentences; it can't say which ones mention a word. The answer was a GIN index: like a book index listing every page each word is on. Tag exam: notes 3, 17 and 42. One entry read, answer found."),
            leo("And what was the B-tree good for, then?"),
            maya("The notes list: one learner's notes, newest first. A B-tree on two columns, the learner's id, then the created date, like surname, then first name. And GIN has a small price: each save has a few more index entries to write."),
            leo("How did he find out it wasn't working?"),
            maya("EXPLAIN ANALYZE. Put it before a query, and PostgreSQL runs it and shows its plan, like a sat-nav showing the route it took and the real journey time."),
            leo("And what did the sat-nav say?"),
            maya("Seq Scan, short for sequential scan: the whole table, row by row, cover to cover. After the GIN index, the plan used the index, and the screen was instant."),
            leo("Thursday. I can feel it."),
            maya("Thursday, the intern. Bless them. The search box built its SQL, the database's query language, by pasting what the user typed straight into the query text. Someone typed a quote mark and some SQL of their own, and the app ran it."),
            leo("Oh no. SQL injection."),
            maya("SQL injection: an attacker slipping their own SQL into yours. Say someone types a quote mark, then delete every note. Pasted in, that's a command. The fix is parameters, placeholders in the query that psycopg fills in safely, always as plain data. The intern now has it on a mug."),
            leo("And Friday? There's always a Friday."),
            maya("The Friday. Priya. Priya never makes mistakes, so it's still the talk of the kitchen. She'd moved the app to Microsoft Entra authentication. Entra ID is Azure's sign-in service, like the front desk at a festival that knows who everyone is."),
            leo("And what does the app show at the desk?"),
            maya("Its managed identity, an identity Azure gives the app itself, like a staff badge. With it, the app gets a token: a short-lived signed pass, like a festival wristband, sent as the password."),
            leo("So no password sitting in the app settings at all?"),
            maya("None. Entra signs the wristband, and PostgreSQL checks the signature and the name inside."),
            leo("So what went wrong?"),
            maya("She fetched one wristband when the app started, and reused it. About an hour later, new connections started failing. Tokens expire, so you get a fresh one when you open new connections. In Python, DefaultAzureCredential, a helper from Azure's identity library, fetches it for you. Priya fixed it by teatime."),
            leo("Anything else this week?"),
            maya("One thing, and it's where PgBouncer stops working. The test server was on Burstable, the cheap tier for testing and light loads, and built-in PgBouncer isn't available there. Production uses General Purpose, the tier for most real apps. And for searching notes by meaning in PostgreSQL, the pgvector episode is next."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the app has a managed identity, it sends an Entra token as the password. If built-in PgBouncer is on, connect to the same host on port 6432. If the search looks inside jsonb, a GIN index; a plain filter or sort, a B-tree. To see if an index is used, EXPLAIN ANALYZE. And no PgBouncer on Burstable."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. Create the notes table, insert a few thousand rows, and EXPLAIN ANALYZE a filter before and after a B-tree index. Then add a GIN index on the jsonb column, and check the contains search uses it."),
            leo("Door 6432. Got it, Dana. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-postgres",
        listOf(
            nova("Hey, you. It's late, the lamp's low, and it's just the three of us. Me, Ivy, and you, still up. [pause] Tonight, a database with good manners: PostgreSQL."),
            ivy("Good manners, at this hour. Introduce us, Nova."),
            nova("PostgreSQL is a popular free database that keeps data in tables, with rows and columns. You talk to it in SQL, the standard language for asking a database for data. And on Azure, it's Azure Database for PostgreSQL flexible server."),
            ivy("Flexible. I like the sound of that. What does it mean?"),
            nova("It's ordinary PostgreSQL that Azure runs for you. The patches, the backups, staying up if a machine fails, all handled. [pause] You just pick the size."),
            ivy("And who's talking to it, this late?"),
            nova("The Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. The backend uses psycopg, the standard PostgreSQL driver for Python: the library that carries your SQL over, and brings the rows home."),
            ivy("And it whispers a password at the door?"),
            nova("It could. Better is Microsoft Entra authentication. Entra ID is Azure's sign-in service, like the front desk at a festival that knows everyone. And the backend has a managed identity, an identity Azure gives the app itself, like a staff badge."),
            ivy("And if I really want a password?"),
            nova("Then keep it in Key Vault, Azure's safe for secrets, never in your code. But tonight, let's do it without one."),
            ivy("So what does the app say at the door?"),
            nova("It shows a token: a short-lived signed pass from Entra ID, like a festival wristband, sent in the password field. [pause] PostgreSQL checks Entra's signature on it, and that the name inside matches the user connecting. Nothing secret is stored anywhere."),
            ivy("Short-lived. Like most things after midnight."),
            nova("[chuckle] About an hour. So fetch a fresh one when you open new connections. In Python, DefaultAzureCredential does that, a helper from Azure's identity library. It's the managed identity in Azure, and your own sign-in on your laptop."),
            ivy("And what does the Coach keep in there?"),
            nova("A notes table. An id, the learner, the title, the text, when it was made. And a flexible column called meta, of type jsonb."),
            ivy("jsonb. Say it slowly."),
            nova("JSON is a text format for nested data, labels and values, the same kind your Android app already reads from your backend. jsonb stores it packed, so PostgreSQL can search inside it."),
            ivy("Tell me how to find things fast. Slowly."),
            nova("With an index. Like the index at the back of a book: it finds rows without reading every one. The default is the B-tree index. Picture a phone book. To find Patel, you open near P and narrow down. [pause] And everyone from Patel to Price sits together, already in order."),
            ivy("So for one learner's notes, newest first?"),
            nova("A B-tree on two columns. The learner's id first, then the created date, like a phone book sorted by surname, then first name. All of one learner's notes together, newest first. [pause] Order matters, just like at a dinner party."),
            ivy("And searching inside that jsonb?"),
            nova("That's where the B-tree stops working. It compares whole values, like a phone book of whole sentences; it can't say which ones mention a word. So it can't answer a contains search: does this jsonb hold that key? You want a GIN index."),
            ivy("GIN. Tell me what it does, gently."),
            nova("It indexes the parts inside a value: every key in jsonb, every item in a list, every word in a text. Like a book index listing each page a word is on. Tag exam: notes 3, 17 and 42. [pause] The price: each save writes a few more entries, so saves get a little slower."),
            ivy("How do I know the index is even working? I like proof."),
            nova("EXPLAIN ANALYZE. Put it before a query, and PostgreSQL runs it and shows its plan, like a sat-nav showing the route it took and the real time."),
            ivy("And what am I hoping to see?"),
            nova("Index Scan: it used the index. A Seq Scan, short for sequential scan, read the whole table. [pause] Careful, though: it really runs even an UPDATE, so wrap it in a transaction, a group of changes you can cancel."),
            ivy("And when the whole class logs in at once?"),
            nova("Connections, the open lines to the database. Each one is a whole separate program on the server, costly to open, hungry for memory."),
            ivy("And my app opens one for every request, doesn't it?"),
            nova("If you let it. So you pool them: share a few real connections among many users. Picture an office with three hundred staff and twenty phone lines. [pause] You pick up a free one, call, and hang up."),
            ivy("A shared line. How very old-fashioned."),
            nova("And flexible server has one built in: PgBouncer, a connection pooler. Forty copies of the app times ten connections is four hundred. PgBouncer serves them with maybe fifty real ones, lending each out for one transaction, then taking it back."),
            ivy("A bouncer. At last, someone on the door."),
            nova("Turn on pgbouncer dot enabled, no restart. Then connect to the same host, on port 6432, instead of 5432. A port is a door number. [pause] 5432 goes straight in; 6432 goes past the bouncer."),
            ivy("And inside the app itself?"),
            nova("Keep a small pool there too, rather than a new connection for every request. [pause] And send your values as parameters, placeholders psycopg fills in safely. Never paste them into the SQL text; that invites SQL injection, a stranger slipping their own SQL into yours."),
            ivy("Any door it won't open?"),
            nova("Burstable. The cheap tier for testing and light loads has no built-in PgBouncer. General Purpose, for most real apps, or Memory Optimized, for heavy queries, do. And the pgvector episode takes PostgreSQL further, into searching by meaning."),
            ivy("[sigh] Mm. Before I drift off. What do I remember for the exam?"),
            nova("If the app has a managed identity, it sends an Entra token as the password. If built-in PgBouncer is on, connect to the same host on port 6432. If the search looks inside jsonb, a GIN index; a plain filter or sort, a B-tree. To see if an index is used, EXPLAIN ANALYZE. And no PgBouncer on Burstable."),
            ivy("And something to try today, before bed?"),
            nova("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. [pause] Create the notes table, insert a few thousand rows, and EXPLAIN ANALYZE a filter before and after a B-tree index. Then add a GIN index on the jsonb column, and check the contains search uses it."),
            ivy("Door 6432, and a good night's sleep. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-postgres",
        listOf(
            nova("Tonight is date night, darling. And tonight, Rafe, it's a classic. PostgreSQL."),
            rafe("Old-fashioned. Reliable. Always there for me. I see why you chose it."),
            nova("[laugh] PostgreSQL is a popular free database that keeps data in tables, with rows and columns. You talk to it in SQL, the standard language for asking a database for data. On Azure, it's Azure Database for PostgreSQL flexible server."),
            rafe("Flexible. My favourite quality in a partner. What does it mean here?"),
            nova("Ordinary PostgreSQL that Azure runs for you. Patches, backups, staying up if a machine fails, all handled; you pick the size. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. It keeps its notes there."),
            rafe("And how does the backend get in?"),
            nova("With psycopg, the standard PostgreSQL driver for Python, the library that carries your SQL over. And it signs in with Microsoft Entra authentication. Entra ID is Azure's sign-in service, like the front desk at a festival that knows everyone."),
            rafe("Then a quiz, darling. The Coach backend has a managed identity, an identity Azure gives the app itself, like a staff badge, and uses Entra authentication. What does it send as the password?"),
            nova("A token from Entra ID: a short-lived signed pass, like a festival wristband, sent in the password field. PostgreSQL checks Entra's signature and the name inside. No secret stored anywhere."),
            rafe("Right answer. No secrets between us. [kiss]"),
            nova("Mm. And wristbands don't last the night, about an hour, so you fetch a fresh one for new connections. In Python, DefaultAzureCredential does it, a helper from Azure's identity library."),
            rafe("Now, what do we keep in this table of ours?"),
            nova("Notes. An id, the learner, the title, the text, when it was made. And a column called meta, of type jsonb. JSON is a text format for nested data, labels and values. jsonb stores it packed, so PostgreSQL can search inside."),
            rafe("Searching inside. Intimate. How do we make it fast?"),
            nova("With an index, like the index at the back of a book. The default is the B-tree index. Picture a phone book: to find Patel, you open near P and narrow down, and everyone from Patel to Price sits together. Good for an equals filter, a range or a sort."),
            rafe("And for one learner's notes, newest first?"),
            nova("A B-tree on two columns, like a phone book by surname, then first name. The learner's id first, then the created date. Order matters, Rafe. You never skip the first course."),
            rafe("My question, then. Notes are filtered on what's inside meta, and it's slow. Which index?"),
            nova("A B-tree on meta?"),
            rafe("No kiss for that one. A B-tree compares whole values, like a phone book of whole sentences; it can't say which ones mention a word. A GIN index indexes the parts inside, like a book index listing every page a word is on. Tag exam: notes 3, 17 and 42. It makes saves a little slower, though."),
            nova("Fine. I'll win it back. How do you know an index is really being used?"),
            rafe("EXPLAIN ANALYZE. Put it before a query, and PostgreSQL runs it and shows its plan, like a sat-nav showing the route it took and the real time. Seq Scan, short for sequential scan, means it read every row. Index Scan means the index did its job."),
            nova("Right answer. You never fake it. [kiss]"),
            rafe("And never run it on an UPDATE, a command that changes data, without a transaction you roll back, a group of changes you can cancel. Because ANALYZE really runs it."),
            nova("And while we're being careful: send values as parameters, placeholders psycopg fills in safely. Paste them into the SQL text, and you invite SQL injection, a stranger slipping their own SQL into yours."),
            rafe("No strangers in our queries. Noted."),
            nova("Now, connections. A connection is an open line to the database, and in PostgreSQL each one is a whole separate program on the server, costly to open, hungry for memory."),
            rafe("Then why does everyone open so many?"),
            nova("Because forty copies of the app each connect per request, and the server hits its limit."),
            rafe("Too many suitors. What do we do?"),
            nova("Pooling: share a few real connections among many users. Picture an office with three hundred staff and twenty phone lines: pick up a free one, call, hang up."),
            rafe("Sharing. I'm not sure I approve."),
            nova("Only the lines, darling. Flexible server has PgBouncer built in, a connection pooler. Four hundred app connections share maybe fifty real ones, each lent for one transaction."),
            rafe("Then my question. Built-in PgBouncer is switched on. What must the app change?"),
            nova("Its port. A port is a door number on the server. Same host, the same server address, but port 6432 instead of 5432. 5432 goes straight to PostgreSQL."),
            rafe("Right answer. Same house, back door. [kiss]"),
            nova("[chuckle] Cheeky. My turn. The server is on Burstable. Can it use built-in PgBouncer?"),
            rafe("No. Burstable is the cheap tier for testing and light loads, and it has no built-in PgBouncer. You need General Purpose, for most real apps, or Memory Optimized, for heavy queries."),
            nova("Right answer. Some things need commitment. [kiss]"),
            rafe("And where does it all stop working, my love?"),
            nova("A B-tree can't see inside jsonb. PgBouncer isn't on Burstable. A wristband reused after it expires fails."),
            rafe("And my phone app? Can it skip the backend and go straight to the database?"),
            nova("Never. The phone calls the backend, and the backend connects. The pgvector episode takes PostgreSQL into searching by meaning."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("If the app has a managed identity, it sends an Entra token as the password. If built-in PgBouncer is on, connect to the same host on port 6432. If the search looks inside jsonb, a GIN index; a plain filter or sort, a B-tree. To see if an index is used, EXPLAIN ANALYZE. And no PgBouncer on Burstable."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. Create the notes table, insert a few thousand rows, and EXPLAIN ANALYZE a filter before and after a B-tree index. Then add a GIN index on the jsonb column, and check the contains search uses it."),
            nova("A classic never goes out of style. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun connect() = DiagramSpec(
        "The API gets an Entra token, connects through PgBouncer on port 6432, and PgBouncer shares a few server connections.",
        nodes = listOf(
            DNode("entra", "Entra ID", 0.16f, 0.16f, w = 0.28f, h = 0.2f, tone = 2, sub = "token as password"),
            DNode("api", "Coach API", 0.16f, 0.5f, w = 0.28f, h = 0.22f, tone = 1, sub = "psycopg"),
            DNode("pgb", "PgBouncer", 0.5f, 0.5f, w = 0.28f, h = 0.22f, sub = "port 6432"),
            DNode("pg", "PostgreSQL", 0.84f, 0.5f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "port 5432"),
            DNode("note", "Many app connections, few real ones", 0.5f, 0.86f, w = 0.7f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("api", "entra", "get token"),
            DEdge("api", "pgb", "connect"),
            DEdge("pgb", "pg", "pooled"),
        ),
        aspect = 0.8f,
    )

    private fun tableIdx() = DiagramSpec(
        "The notes table: a B-tree index serves filters and sorting, a GIN index searches inside jsonb, and EXPLAIN ANALYZE proves it.",
        nodes = listOf(
            DNode("table", "notes table", 0.5f, 0.18f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "meta is jsonb"),
            DNode("btree", "B-tree index", 0.26f, 0.62f, w = 0.46f, h = 0.24f, tone = 1, sub = "user_id, created_at"),
            DNode("gin", "GIN index", 0.76f, 0.62f, w = 0.44f, h = 0.24f, tone = 2, sub = "inside meta"),
            DNode("plan", "EXPLAIN ANALYZE: is it used?", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("table", "btree", "filter, sort"),
            DEdge("table", "gin", "contains"),
            DEdge("plan", "btree", dashed = true),
            DEdge("plan", "gin", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun plan() = DiagramSpec(
        "EXPLAIN ANALYZE runs the query and shows its plan: a Seq Scan reads every row, an Index Scan uses the index.",
        nodes = listOf(
            DNode("query", "EXPLAIN ANALYZE", 0.5f, 0.15f, w = 0.7f, h = 0.2f, tone = 1, sub = "runs it, shows the plan"),
            DNode("seq", "Seq Scan", 0.26f, 0.56f, w = 0.46f, h = 0.26f, tone = 2, sub = "every row: about 200 ms"),
            DNode("idx", "Index Scan", 0.76f, 0.56f, w = 0.44f, h = 0.26f, sub = "under 1 ms"),
            DNode("txn", "UPDATE or DELETE? Roll it back", 0.5f, 0.9f, w = 0.76f, h = 0.14f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("query", "seq", "before"),
            DEdge("query", "idx", "after"),
        ),
        aspect = 0.8f,
    )

    private fun pool() = DiagramSpec(
        "Many app copies connect to PgBouncer on port 6432, which shares a few real connections; port 5432 goes straight past it.",
        nodes = listOf(
            DNode("apps", "40 app copies", 0.2f, 0.5f, w = 0.34f, h = 0.3f, tone = 1, sub = "small pool each"),
            DNode("direct", "Port 5432", 0.55f, 0.14f, w = 0.28f, h = 0.18f, kind = NodeKind.PILL, sub = "straight in"),
            DNode("pgb", "PgBouncer", 0.55f, 0.5f, w = 0.28f, h = 0.22f, sub = "port 6432"),
            DNode("pg", "Server", 0.86f, 0.5f, w = 0.24f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "few connections"),
            DNode("tier", "Not on Burstable", 0.5f, 0.88f, w = 0.8f, h = 0.16f, kind = NodeKind.NOTE, tone = 2, sub = "General Purpose or Memory Optimized"),
        ),
        edges = listOf(
            DEdge("apps", "pgb", "6432"),
            DEdge("pgb", "pg", "pooled"),
            DEdge("apps", "direct", dashed = true),
            DEdge("direct", "pg", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's PostgreSQL questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("mi", "Managed identity", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Entra token as password"),
            DNode("bouncer", "PgBouncer is on", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "same host, port 6432"),
            DNode("inside", "Search inside jsonb", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "GIN index"),
            DNode("plain", "Filter or sort", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "B-tree index"),
            DNode("used", "Is the index used?", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "EXPLAIN ANALYZE"),
            DNode("burst", "Burstable tier", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "no built-in PgBouncer"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("mi", "bouncer", "inside", "plain", "used", "burst")
    private val connectCode = BoardCard("Python", "cred = DefaultAzureCredential(); password=cred.get_token(SCOPE).token", code = true)
    private val btreeCode = BoardCard("SQL", "CREATE INDEX notes_user_created ON notes (user_id, created_at DESC);", code = true)
    private val ginCode = BoardCard("SQL", "CREATE INDEX notes_meta ON notes USING GIN (meta);", code = true)
    private val containsCode = BoardCard("SQL", "SELECT id, title FROM notes WHERE meta @> '{\"module\": \"containers\"}';", code = true)
    private val paramCode = BoardCard("Python", "conn.execute(\"SELECT id, title FROM notes WHERE user_id = %s\", (user_id,))", code = true)
    private val today = BoardCard("Today", "Entra sign-in, psycopg with a token, a B-tree before and after, a GIN index.")

    // The shared pictures, one card each.
    private val frontDeskCard = BoardCard("Entra ID: the front desk", "Azure's sign-in service: knows who everyone is, hands out wristbands.")
    private val wristbandCard = BoardCard("Token: a festival wristband", "A short-lived signed pass from Entra ID, sent in the password field.")
    private val jsonbCard = BoardCard("jsonb", "JSON stored packed, so PostgreSQL can search inside it: module, source, tags.")
    private val phoneBookCard = BoardCard("B-tree: a phone book", "Open near P for Patel; Patel to Price sit together. Equals, ranges, sorting.")
    private val twoColumnCard = BoardCard("Two columns", "Learner, then date: like surname, then first name. One learner, newest first.")
    private val ginCard = BoardCard("GIN: every page a word is on", "Tag exam: notes 3, 17, 42. Tag tricky: 8, 17. One entry read, answer found.")
    private val satNavCard = BoardCard("EXPLAIN ANALYZE: a sat-nav", "Runs the query and shows the route it took, with the real times.")
    private val linesCard = BoardCard("Pooling: shared phone lines", "300 staff, 20 lines: pick up a free one, make the call, hang up.")
    private val bouncerCard = BoardCard("PgBouncer", "400 app connections share about 50 real ones, each lent for one transaction.")
    private val portCard = BoardCard("Port 6432", "Same host, different door: 5432 goes straight in, 6432 through PgBouncer.")

    private fun classicBoard() = PodcastBoard(
        "ai-postgres",
        listOf(
            // 0: "Hi, and welcome back. Today is PostgreSQL on Azure..."
            Scene(
                0, "PostgreSQL on Azure", connect(),
                beats = listOf(
                    Beat(0, show = setOf("pg"), light = setOf("pg"), card = BoardCard("PostgreSQL", "A popular free database: tables of rows and columns, queried with SQL.")),
                    Beat(2, light = setOf("pg"), card = BoardCard("Flexible server", "Ordinary PostgreSQL that Azure runs: patches, backups, staying up.")),
                    Beat(6, show = setOf("api"), light = setOf("api"), card = BoardCard("psycopg", "The standard PostgreSQL driver for Python.")),
                    Beat(8, show = setOf("entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(10, light = setOf("api", "entra"), pulse = "api" to "entra", card = wristbandCard),
                    Beat(14, card = connectCode),
                ),
            ),
            // 15: "Okay, I'm in. What does the Coach store?"
            Scene(
                15, "The notes table", tableIdx(),
                beats = listOf(
                    Beat(16, show = setOf("table"), light = setOf("table"), card = BoardCard("A notes table", "Id, learner, title, text, created date, and a meta column.")),
                    Beat(18, card = jsonbCard),
                    Beat(20, show = setOf("btree"), light = setOf("btree"), pulse = "table" to "btree", card = phoneBookCard),
                    Beat(24, light = setOf("btree"), card = btreeCode),
                    Beat(26, show = setOf("gin"), light = setOf("gin"), pulse = "table" to "gin", card = BoardCard("GIN index", "Indexes the parts inside a value: jsonb keys, list items, words.")),
                    Beat(30, light = setOf("gin"), card = ginCard),
                ),
            ),
            // 31: "How do I know whether my index is even being used?"
            Scene(
                31, "Is the index used?", plan(),
                beats = listOf(
                    Beat(32, show = setOf("query"), light = setOf("query"), card = satNavCard),
                    Beat(34, show = setOf("seq", "idx"), light = setOf("seq", "idx"), pulse = "query" to "idx", card = BoardCard("Seq Scan, then Index Scan", "Cover to cover; after the B-tree, about 200 ms down to under 1.")),
                    Beat(38, show = setOf("txn"), light = setOf("txn"), card = BoardCard("It really runs", "Wrap an UPDATE or DELETE in a transaction and roll it back.")),
                ),
            ),
            // 39: "Okay. Now, I heard connections cause trouble."
            Scene(
                39, "Connections", pool(),
                beats = listOf(
                    Beat(40, show = setOf("apps"), light = setOf("apps"), card = BoardCard("Each connection: a program", "Costly to open, hungry for memory. Forty copies hit the limit.")),
                    Beat(44, show = setOf("pg"), light = setOf("pg"), card = linesCard),
                    Beat(46, show = setOf("pgb"), light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(50, show = setOf("direct"), light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(52, show = setOf("tier"), light = setOf("tier"), card = BoardCard("Not on Burstable", "General Purpose or Memory Optimized. A small pool in the app too.")),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("mi", "bouncer", "inside"), card = BoardCard("Listen for the phrase", "Token as password. Port 6432. GIN inside jsonb. EXPLAIN ANALYZE.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-postgres--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Exam Week Crawl..."
            Scene(
                0, "The Exam Week Crawl",
                DiagramSpec(
                    "Study Lantern's forty backend copies each open a new connection per request, and PostgreSQL runs out.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Study Lantern"),
                        DNode("api", "Python backend", 0.22f, 0.72f, w = 0.38f, h = 0.2f, tone = 1, sub = "40 copies"),
                        DNode("err", "Too many connections", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "exam week"),
                        DNode("db", "PostgreSQL", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "on Azure"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "calls"),
                        DEdge("api", "db", "new connections"),
                        DEdge("db", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "db"), light = setOf("db"), pulse = "phone" to "api", card = BoardCard("The case", "In exam week the app slowed to a crawl, then started failing.")),
                    Beat(2, show = setOf("err"), light = setOf("err"), pulse = "db" to "err", card = BoardCard("Too many connections", "Forty copies, a new phone line for every request.")),
                ),
            ),
            // 3: "Then my first suspect: the disk is too small..."
            Scene(
                3, "The suspects", pool(),
                beats = listOf(
                    Beat(3, show = setOf("apps"), light = setOf("apps"), card = BoardCard("Suspect one: the disk", "A bigger disk changes nothing.")),
                    Beat(4, card = BoardCard("Each connection: a program", "Handshake, encryption, sign-in; thousands eat memory.")),
                    Beat(6, show = setOf("pg"), light = setOf("pg"), card = linesCard),
                    Beat(8, show = setOf("pgb"), light = setOf("pgb"), card = BoardCard("Flexible server", "Ordinary PostgreSQL that Azure runs, with PgBouncer built in.")),
                    Beat(10, light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(14, show = setOf("direct"), light = setOf("direct"), pulse = "apps" to "direct", card = BoardCard("Switched on: no change", "pgbouncer.enabled was set, but the app still used port 5432.")),
                    Beat(16, light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(20, show = setOf("tier"), light = setOf("tier"), card = BoardCard("Not on Burstable", "Moved to General Purpose; a small pool in each app copy too.")),
                ),
            ),
            // 23: "Connections stable. What else is in the file?"
            Scene(
                23, "Two slow screens", plan(),
                beats = listOf(
                    Beat(24, show = setOf("query"), light = setOf("query"), card = satNavCard),
                    Beat(26, show = setOf("seq"), light = setOf("seq"), pulse = "query" to "seq", card = BoardCard("Seq Scan", "Half a million rows read to find twenty: cover to cover.")),
                    Beat(28, show = setOf("idx", "txn"), light = setOf("idx"), pulse = "query" to "idx", card = phoneBookCard),
                ),
            ),
            // 31: "Second slow screen?"
            Scene(
                31, "The tag filter", tableIdx(),
                beats = listOf(
                    Beat(32, show = setOf("table"), light = setOf("table"), card = containsCode),
                    Beat(33, show = setOf("btree"), light = setOf("btree"), pulse = "table" to "btree", card = BoardCard("Suspect: a B-tree on meta", "A phone book of whole sentences: it can't see inside.")),
                    Beat(34, show = setOf("gin"), light = setOf("gin"), pulse = "table" to "gin", card = ginCard),
                ),
            ),
            // 37: "Any other suspects?"
            Scene(
                37, "The risky one", connect(),
                beats = listOf(
                    Beat(38, show = setOf("api", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(40, light = setOf("api", "entra"), pulse = "api" to "entra", card = wristbandCard),
                    Beat(44, card = connectCode),
                ),
            ),
            // 45: "Any loose ends?"
            Scene(
                45, "Loose ends", connect(),
                beats = listOf(
                    Beat(46, light = setOf("api"), card = BoardCard("Phones call the backend", "Never the database directly. pgvector: searching by meaning.")),
                ),
            ),
            // 47: "So, the case summary for the exam?"
            Scene(
                47, "Case closed", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("bouncer", "burst", "inside"), card = BoardCard("Listen for the phrase", "Port 6432, not on Burstable, GIN inside jsonb, a token as password.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-postgres--gossip",
        listOf(
            // 0: "Okay, did you hear what happened when the Coach team moved notes to PostgreSQL?..."
            Scene(
                0, "The move to PostgreSQL", connect(),
                beats = listOf(
                    Beat(0, show = setOf("api", "pg"), light = setOf("pg"), card = BoardCard("PostgreSQL", "A popular free database of tables, rows and columns.")),
                    Beat(2, light = setOf("api", "pg"), card = BoardCard("Flexible server", "Ordinary PostgreSQL that Azure runs. psycopg is the Python driver.")),
                ),
            ),
            // 3: "Okay. So who went first?"
            Scene(
                3, "Monday: Tom", pool(),
                beats = listOf(
                    Beat(4, show = setOf("apps"), light = setOf("apps"), card = BoardCard("A connection per request", "By lunch, the server hit its connection limit.")),
                    Beat(6, show = setOf("pg"), light = setOf("pg"), card = linesCard),
                ),
            ),
            // 9: "And Tuesday?"
            Scene(
                9, "Tuesday: Dana", pool(),
                beats = listOf(
                    Beat(10, show = setOf("apps", "pgb", "pg"), light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(14, show = setOf("direct"), light = setOf("direct"), pulse = "apps" to "direct", card = portCard),
                ),
            ),
            // 15: "One number. Wednesday?"
            Scene(
                15, "Wednesday: Marcus", tableIdx(),
                beats = listOf(
                    Beat(16, show = setOf("table"), light = setOf("table"), card = jsonbCard),
                    Beat(18, show = setOf("btree"), light = setOf("btree"), pulse = "table" to "btree", card = phoneBookCard),
                    Beat(22, show = setOf("gin"), light = setOf("gin"), pulse = "table" to "gin", card = ginCard),
                    Beat(24, light = setOf("btree"), card = btreeCode),
                ),
            ),
            // 25: "How did he find out it wasn't working?"
            Scene(
                25, "How he found out", plan(),
                beats = listOf(
                    Beat(26, show = setOf("query", "seq", "idx"), light = setOf("seq"), pulse = "query" to "seq", card = satNavCard),
                ),
            ),
            // 29: "Thursday. I can feel it."
            Scene(
                29, "Thursday: the intern",
                DiagramSpec(
                    "Pasting what a user typed into the SQL lets them run their own; parameters let psycopg fill values in safely.",
                    nodes = listOf(
                        DNode("box", "Search box", 0.25f, 0.25f, w = 0.42f, h = 0.22f, tone = 1, sub = "what the user typed"),
                        DNode("pasted", "Pasted into SQL", 0.75f, 0.25f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "SQL injection"),
                        DNode("params", "Parameters", 0.5f, 0.75f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, sub = "placeholders filled safely"),
                    ),
                    edges = listOf(
                        DEdge("box", "pasted", dashed = true),
                        DEdge("box", "params"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("box", "pasted"), light = setOf("pasted"), pulse = "box" to "pasted", card = BoardCard("Pasted SQL", "A quote mark and some SQL of their own, and the app ran it.")),
                    Beat(32, show = setOf("params"), light = setOf("params"), pulse = "box" to "params", card = paramCode),
                ),
            ),
            // 33: "And Friday? There's always a Friday."
            Scene(
                33, "Friday: Priya", connect(),
                beats = listOf(
                    Beat(34, show = setOf("api", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(36, light = setOf("api", "entra"), pulse = "api" to "entra", card = wristbandCard),
                    Beat(40, light = setOf("entra", "pg"), card = BoardCard("An hour later", "New connections failed. Fetch a fresh wristband for new connections.")),
                ),
            ),
            // 41: "Anything else this week?"
            Scene(
                41, "Where PgBouncer stops", pool(),
                beats = listOf(
                    Beat(42, light = setOf("tier"), card = BoardCard("Not on Burstable", "Production uses General Purpose. pgvector: the next episode.")),
                ),
            ),
            // 43: "So, the gossip I should remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("bouncer", "inside", "mi"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Door 6432, as Dana learned.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-postgres--late-night",
        listOf(
            // 0: "Hey, you. It's late, the lamp's low..."
            Scene(
                0, "A database with manners", connect(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "PostgreSQL on Azure: sign in, model, index, pool.")),
                    Beat(2, show = setOf("pg"), light = setOf("pg"), card = BoardCard("PostgreSQL", "A popular free database: tables of rows and columns, queried with SQL.")),
                    Beat(4, card = BoardCard("Flexible server", "Ordinary PostgreSQL that Azure runs: patches, backups, staying up.")),
                    Beat(6, show = setOf("api"), light = setOf("api"), card = BoardCard("psycopg", "The standard PostgreSQL driver for Python.")),
                    Beat(8, show = setOf("entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(10, card = BoardCard("A password?", "Then keep it in Key Vault, never in the code.")),
                    Beat(12, light = setOf("api", "entra"), pulse = "api" to "entra", card = wristbandCard),
                    Beat(14, card = connectCode),
                ),
            ),
            // 15: "And what does the Coach keep in there?"
            Scene(
                15, "The notes table", tableIdx(),
                beats = listOf(
                    Beat(16, show = setOf("table"), light = setOf("table"), card = jsonbCard),
                    Beat(20, show = setOf("btree"), light = setOf("btree"), pulse = "table" to "btree", card = phoneBookCard),
                    Beat(22, light = setOf("btree"), card = twoColumnCard),
                    Beat(24, light = setOf("btree"), card = BoardCard("Where the B-tree stops", "A phone book of whole sentences: it can't see inside jsonb.")),
                    Beat(26, show = setOf("gin"), light = setOf("gin"), pulse = "table" to "gin", card = ginCard),
                ),
            ),
            // 27: "How do I know the index is even working? I like proof."
            Scene(
                27, "Proof", plan(),
                beats = listOf(
                    Beat(28, show = setOf("query", "seq", "idx", "txn"), light = setOf("query"), pulse = "query" to "idx", card = satNavCard),
                ),
            ),
            // 31: "And when the whole class logs in at once?"
            Scene(
                31, "The whole class at once", pool(),
                beats = listOf(
                    Beat(34, show = setOf("apps", "pg"), light = setOf("apps"), card = linesCard),
                    Beat(36, show = setOf("pgb"), light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(38, show = setOf("direct"), light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(40, light = setOf("apps"), card = BoardCard("In the app", "A small pool, and parameters, never values pasted into SQL.")),
                    Beat(42, show = setOf("tier"), light = setOf("tier"), card = BoardCard("Not on Burstable", "General Purpose or Memory Optimized. pgvector: another night.")),
                ),
            ),
            // 43: "Mm. Before I drift off. What do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("mi", "bouncer", "inside"), card = BoardCard("Listen for the phrase", "Token as password. Port 6432. GIN inside jsonb. EXPLAIN ANALYZE.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-postgres--couples",
        listOf(
            // 0: "Tonight is date night, darling. And tonight, Rafe, it's a classic. PostgreSQL."
            Scene(
                0, "Date night: a classic", connect(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "A classic: PostgreSQL on Azure.")),
                    Beat(2, show = setOf("pg"), light = setOf("pg"), card = BoardCard("PostgreSQL", "A popular free database: tables of rows and columns, queried with SQL.")),
                    Beat(4, card = BoardCard("Flexible server", "Ordinary PostgreSQL that Azure runs: patches, backups, staying up.")),
                    Beat(6, show = setOf("api", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(7, light = setOf("api"), card = BoardCard("Quiz: the password", "A managed identity and Entra authentication. What's the password?")),
                    Beat(8, light = setOf("api", "entra"), pulse = "api" to "entra", card = wristbandCard),
                    Beat(10, card = connectCode),
                ),
            ),
            // 11: "Now, what do we keep in this table of ours?"
            Scene(
                11, "What's inside", tableIdx(),
                beats = listOf(
                    Beat(12, show = setOf("table"), light = setOf("table"), card = jsonbCard),
                    Beat(14, show = setOf("btree"), light = setOf("btree"), pulse = "table" to "btree", card = phoneBookCard),
                    Beat(16, light = setOf("btree"), card = twoColumnCard),
                    Beat(17, light = setOf("table"), card = BoardCard("Quiz: inside meta", "Notes filtered on what's inside meta are slow. Which index?")),
                    Beat(19, show = setOf("gin"), light = setOf("gin"), pulse = "table" to "gin", card = ginCard),
                ),
            ),
            // 20: "Fine. I'll win it back. How do you know an index is really being used?"
            Scene(
                20, "Never fake it", plan(),
                beats = listOf(
                    Beat(20, show = setOf("query"), light = setOf("query"), card = BoardCard("Quiz: is the index used?", "How do you know for sure?")),
                    Beat(21, show = setOf("seq", "idx"), light = setOf("seq", "idx"), pulse = "query" to "idx", card = satNavCard),
                    Beat(23, show = setOf("txn"), light = setOf("txn"), card = BoardCard("It really runs", "Wrap an UPDATE in a transaction you roll back.")),
                    Beat(24, card = paramCode),
                ),
            ),
            // 26: "Now, connections..."
            Scene(
                26, "Too many suitors", pool(),
                beats = listOf(
                    Beat(26, show = setOf("apps"), light = setOf("apps"), card = BoardCard("Each connection: a program", "Costly to open, hungry for memory. Forty copies hit the limit.")),
                    Beat(30, show = setOf("pgb", "pg"), light = setOf("pgb"), pulse = "pgb" to "pg", card = linesCard),
                    Beat(33, card = BoardCard("Quiz: PgBouncer is on", "What must the app change?")),
                    Beat(34, show = setOf("direct"), light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(36, show = setOf("tier"), light = setOf("tier"), card = BoardCard("Quiz: Burstable?", "Can it use built-in PgBouncer?")),
                    Beat(37, card = BoardCard("Right answer: no", "General Purpose, or Memory Optimized for heavy queries.")),
                    Beat(40, light = setOf("apps", "tier"), card = BoardCard("Where it stops", "No B-tree inside jsonb, no PgBouncer on Burstable, phones call the API.")),
                ),
            ),
            // 43: "Last question, for the exam. What do we remember?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("mi", "bouncer", "burst"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Five for five.")),
                    Beat(47, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
