package com.shahaabapps.core.content

/** Azure Database for PostgreSQL: connect, model and index: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesPostgres {
    private val classic = Podcast(
        "ai-postgres",
        listOf(
            maya("Hi, and welcome back. The Zaiqa API, the .NET service the phone talks to, lives in Container Apps, orders and payments sit in Azure SQL, and the change feed wakes the payout worker when an order is delivered. Today: where that payout is written down."),
            leo("Written down where? We already have a relational database. Why not one more table in Azure SQL?"),
            maya("That is the honest question. Zaiqa pays its restaurants every Sunday, and that ledger, the running record of what we owe each one, is going into Azure Database for PostgreSQL."),
            leo("A second relational database. Is that not the thing everybody warns you about?"),
            maya("It is a real cost: two sets of backups, two things to patch, two sign-ins. On technical grounds alone, payout tables in Azure SQL would have done."),
            leo("So why PostgreSQL?"),
            maya("Two reasons, and only one is technical. The payouts team runs PostgreSQL and arrived with years of reconciliation queries. And the exam tests PostgreSQL. [pause] A team decision, not a default."),
            leo("Fine. Which PostgreSQL am I buying?"),
            maya("Azure Database for PostgreSQL flexible server: ordinary PostgreSQL, where Azure does the patching, the backups and the staying up. There used to be a single server shape. It is retired, so in an exam answer it is the wrong one."),
            leo("Good. And how big?"),
            maya("Compute and storage, separately. Burstable for playing, General Purpose for most real work, Memory Optimized for heavy reporting. Compute moves up and down with a restart; the disk only ever grows, so start modest."),
            leo("Go on, then. What happens when the machine under my ledger dies on a Sunday?"),
            maya("You pay for a standby: a second copy of the server, kept in step with the first, in another availability zone, which is a separate building in the same region. Every write lands on both before we call it saved."),
            leo("And when the first one goes?"),
            maya("Azure fails over, the server name does not change, and the worker reconnects. It costs roughly double, because you are renting two machines."),
            leo("Then let me run the finance report on the spare. It is sitting there doing nothing."),
            maya("You cannot, and that is the exam's favourite trap. [pause] The standby is warm and silent. Nobody reads from it."),
            leo("Then finance is stuck. They want a Monday report over the whole quarter."),
            maya("They want the other thing: a read replica. A replica is a read-only copy of the server that keeps itself up to date from the original, and you can read from it all day without the ledger feeling it."),
            leo("What is the catch?"),
            maya("It copies over in the background, so it is a little behind, usually by seconds. For last week's payouts, fine. For have we already paid this invoice, go to the real server."),
            leo("So standby and replica are not the same animal."),
            maya("That is the pair the exam loves. Survive a zone failing: a standby. Keep heavy reads off the ledger: a read replica, which can sit in another region and later be promoted into a server of its own."),
            leo("Right. Now, the payout worker. It wakes, writes a row, and goes away."),
            maya("Hundreds do, at once. For example, Sunday at nine: the change feed hands over a few thousand delivered orders, Container Apps scales the worker to forty copies, and each opens its own connections."),
            leo("And a connection is expensive here?"),
            maya("Here it is literal. In PostgreSQL every connection is a whole separate program on the server: a handshake, encryption, a sign-in, and memory held while it is open. A few hundred idle ones eat the memory the queries need."),
            leo("So the server falls over at the exact moment we are paying people."),
            maya("It hits its connection limit and starts refusing. The answer is pooling. Picture a sales floor with three hundred staff and twenty phone lines: you pick up a free one, make your call, and hang up."),
            leo("And who holds the twenty lines?"),
            maya("PgBouncer, a connection pooler that flexible server already has built in. Turn on the setting pgbouncer dot enabled, no restart, then connect to the same host on port 6432 instead of 5432. A port is a door number."),
            leo("Same building, different door. Does each copy get a line of its own?"),
            maya("Better: one per transaction. Forty copies times ten connections is four hundred, and PgBouncer serves them with maybe fifty real ones. Keep a small pool inside the worker too."),
            leo("And let me guess. Not on Burstable."),
            maya("[chuckle] Not on Burstable. Built-in PgBouncer needs General Purpose or Memory Optimized. People switch it on, see no change, and lose an afternoon to it."),
            leo("Now the part I always get wrong. What password does the worker use?"),
            maya("None. The worker has a managed identity, an identity Azure gives the app itself, like a staff badge, and the server has Microsoft Entra authentication switched on. Entra ID is Azure's sign-in service, like the front desk at a festival."),
            leo("And at the front desk it is handed?"),
            maya("A token: a short-lived signed pass, like a festival wristband. The worker sends that in the password field, and PostgreSQL checks the signature and that the name matches the user it connects as. Nothing secret is stored anywhere."),
            leo("How short is short-lived?"),
            maya("About an hour. So fetch a fresh one whenever you open new connections, never once at start-up. Our worker is .NET; in Python the pair is psycopg, the standard PostgreSQL driver, and DefaultAzureCredential."),
            leo("Back to finance for a moment. Their report takes two minutes, even on the replica."),
            maya("Put EXPLAIN ANALYZE in front of the query. It runs the query and shows the route it took, like a sat-nav reporting the real journey afterwards. Theirs said Seq Scan, short for sequential scan: every row, cover to cover."),
            leo("Which smells like a missing index."),
            maya("It is. An index is like the index at the back of a book: it finds the rows without reading everything. They wanted a B-tree index, the default kind, on the restaurant id and the week. Picture a phone book, where Patel to Price sit together. Two minutes became under a second."),
            leo("Next worry. Somebody runs a bad correction script and wipes a week of payouts."),
            maya("Backups are automatic, and you choose how far back you can reach: seven days up to thirty-five. Then point-in-time restore brings the data back as it was at a moment you name, down to the minute."),
            leo("So it rewinds my server."),
            maya("No, and this one catches people. Restore builds a new server at that moment; the broken one is still sitting there. You restore, check the totals, and point the worker at the new name."),
            leo("Last worry. Is this thing on the public internet?"),
            maya("That is a choice you make when you create it, and it is hard to undo. Public access gives the server a public address with a firewall list of who may knock. Private access, virtual network integration, puts it inside your own network with no public address at all."),
            leo("And the worker finds it how? It is over in Container Apps."),
            maya("By the same host name. A private DNS zone resolves it inside the network, and nothing outside can even look it up."),
            leo("And if I change my mind afterwards?"),
            maya("A new server and a restore, one quiet Sunday. [sigh] Choose it properly the first time."),
            leo("So what do I remember for the exam?"),
            maya("If the app has a managed identity, it sends an Entra token as the password. If a zone must be able to fail, high availability with a standby, and nobody reads from it. If reports must not slow the ledger, a read replica, slightly behind."),
            leo("Keep going."),
            maya("If hundreds of short connections are exhausting the server, built-in PgBouncer, same host, port 6432, and never on Burstable. If you must get back to just before the bad script, point-in-time restore, and it makes a new server. And no public address means private access, chosen when you create it."),
            leo("And anything to try today?"),
            maya("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. Then create a table, insert a few thousand rows, and run EXPLAIN ANALYZE on a filter before and after a B-tree index."),
            leo("Ledger in PostgreSQL, badge at the door, reports off to one side. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-postgres",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Sunday That Paid Nobody. Zaiqa pays its restaurants every Sunday, and the ledger, the running record of what we owe each one, now lives in PostgreSQL. Last Sunday, nine restaurants out of two thousand were paid, and then everything stopped."),
            leo("Detective Leo, on the case. First, though: orders and payments are in Azure SQL. Why is there a second database at all?"),
            maya("A fair question, and the answer is not purely technical. The payouts team runs PostgreSQL and came with years of reconciliation queries, and the exam tests PostgreSQL. Two sets of backups is a real cost. We chose to pay it."),
            leo("Noted. First suspect: the disk filled up."),
            maya("[pause] Wrong suspect. The log said too many connections. The payout worker, the .NET job that reads the change feed and writes the ledger, had scaled to forty copies, and each copy opened its own."),
            leo("Forty copies. Is one connection really that expensive?"),
            maya("In PostgreSQL, yes. Every connection is a whole separate program on the server: a handshake, encryption, a sign-in, and memory held while it is open. Hundreds of idle ones eat the memory the queries need."),
            leo("So, fewer of them. How?"),
            maya("Pooling. Picture a sales floor with three hundred staff and twenty phone lines: nobody owns a line, you pick up a free one, make the call, and hang up. Our server has a pooler built in."),
            leo("Our server being?"),
            maya("Azure Database for PostgreSQL flexible server: ordinary PostgreSQL that Azure patches, backs up and keeps running. There was an older single server shape. It is retired, so in an exam answer it is the wrong one."),
            leo("And the pooler has a name, I take it."),
            maya("PgBouncer, a connection pooler sitting on the same machine. Turn on the setting pgbouncer dot enabled, no restart, then connect on port 6432 instead of 5432. A port is a door number on the server."),
            leo("They switched it on and the case closed itself."),
            maya("They switched it on. Nothing changed. Clue two."),
            leo("Suspect: it needed a restart."),
            maya("It does not. Look at the connection string. Still port 5432, same host, wrong door, so nothing went through the pool."),
            leo("And after they changed the number?"),
            maya("Still nothing, and that is clue three. The server was on Burstable, the cheap tier for testing and light loads, and built-in PgBouncer is not there at all."),
            leo("How many tiers are there?"),
            maya("Three. Burstable for playing, General Purpose for most real work, Memory Optimized for heavy reporting. They moved to General Purpose. Compute goes up and down with a restart; the disk only ever grows, so buy it modestly."),
            leo("Connections fixed. But what if the machine itself had died mid-run?"),
            maya("Then you want a standby: a second copy of the server kept in step with the first, in another availability zone, a separate building in the same region. Azure fails over, the server name does not change, and you pay about double."),
            leo("Can finance read their Monday report off that spare?"),
            maya("Never. The standby is warm and silent. That is the trap the exam sets."),
            leo("Speaking of finance. What else is in the file?"),
            maya("Two complaints. The Monday report took two minutes, and while it ran, writes to the ledger crawled."),
            leo("Suspect: a bigger server."),
            maya("[sigh] Money again. The real answer is a read replica: a read-only copy of the server that keeps itself up to date from the original. Point the report at that, and the ledger never feels it."),
            leo("Is the copy exactly current?"),
            maya("It copies over in the background, so it lags, usually by seconds. Fine for last week's totals; not for have we already paid this invoice."),
            leo("And the second complaint?"),
            maya("Even on the replica, two minutes. So they ran EXPLAIN ANALYZE, which runs the query and shows the route it took, like a sat-nav reporting the real journey afterwards. The plan said Seq Scan: every row, cover to cover."),
            leo("Missing index, I suspect."),
            maya("You suspect right. An index is like the index at the back of a book: it finds rows without reading everything. They added a B-tree index, the default kind, on the restaurant id and the week: a phone book, where Patel to Price sit together."),
            leo("Two minutes became?"),
            maya("Under a second. And you create it on the real server; it copies across to the replica by itself."),
            leo("Any suspects still at large?"),
            maya("Two, and they frighten me more than the first. Last month somebody ran a correction script that removed a week of rows. And the server was created with a public address, so it could be reached from the internet at all."),
            leo("The wiped week first. Was there a backup?"),
            maya("Automatic, and you choose how far back you keep them: seven days up to thirty-five. Point-in-time restore brings the data back as it was at a moment you name, down to the minute."),
            leo("So they rewound the server."),
            maya("That is what they tried. Restore builds a new server at that moment, and the broken one stays where it is. They restored, checked the totals, and moved the worker to the new name."),
            leo("And the public address?"),
            maya("Still there, because it is chosen when you create the server and cannot be flipped afterwards. Private access, virtual network integration, puts the server inside your own network with no public address. It will take a new server and a restore."),
            leo("Then how would the worker find it?"),
            maya("By the same host name. A private DNS zone resolves that name inside the network, and nothing outside can even look it up."),
            leo("One last thing bothers me. Where is the database password kept?"),
            maya("Nowhere, now. The worker has a managed identity, an identity Azure gives the app itself, like a staff badge, and the server has Microsoft Entra authentication on. Entra ID is Azure's sign-in service, like the front desk at a festival."),
            leo("It shows the badge and is handed?"),
            maya("A token: a short-lived signed pass, like a festival wristband, sent in the password field. It lasts about an hour, so fetch a fresh one whenever you open new connections. The pgvector episode takes this same server further."),
            leo("So, the case summary for the exam?"),
            maya("A managed identity sends an Entra token as the password. A zone that must be able to fail means high availability with a standby nobody reads from. Reports that slow the ledger mean a read replica, slightly behind."),
            leo("Keep going."),
            maya("Hundreds of short connections mean built-in PgBouncer, same host, port 6432, never on Burstable. Back to just before the bad script means point-in-time restore, and it makes a new server. No public address means private access, chosen at creation."),
            leo("Case closed. Anything to try today?"),
            maya("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg, the standard driver, and a token from DefaultAzureCredential. Then insert a few thousand rows and run EXPLAIN ANALYZE on a filter before and after a B-tree index."),
            leo("Paid, pooled and private. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-postgres",
        listOf(
            maya("Okay, did you hear what Sunday night did to the payouts team? Sit down. Zaiqa pays its restaurants from a ledger that now lives in PostgreSQL, a second relational database beside Azure SQL, and the first month was a parade."),
            leo("[laugh] I heard somebody shout the word bouncer across the kitchen. Why a second database at all?"),
            maya("Because the payouts team runs PostgreSQL and came with years of reconciliation queries, and because the exam tests PostgreSQL. Orders and payments stay in Azure SQL. Nobody pretends it is free: two sets of backups, two things to patch."),
            leo("Fair. Who went first?"),
            maya("Tom. Monday. Tom chose the tier, and he chose Burstable, the cheap one for testing, because the ledger is only a few tables."),
            leo("And what did that cost him?"),
            maya("Everything he wanted later. Burstable has no standby, no built-in pooler, and none of the tricks the rest of this story needs. He is on General Purpose now. And the disk he over-bought cannot shrink: storage only grows."),
            leo("What are the other tiers?"),
            maya("General Purpose for most real work, Memory Optimized for heavy reporting. Compute goes up and down with a restart. All of it on Azure Database for PostgreSQL flexible server, which is ordinary PostgreSQL that Azure patches and backs up for you."),
            leo("Tuesday?"),
            maya("Dana, and the Sunday run. Forty copies of the payout worker, each opening its own connections, and PostgreSQL stopped taking them. Every connection there is a whole separate program on the server, costly to open and hungry for memory."),
            leo("Huh. In my own code I open connections without thinking."),
            maya("So did Dana. The answer is pooling: sharing a few real ones. Picture a sales floor with three hundred staff and twenty phone lines. You pick up a free one, make the call, hang up."),
            leo("And the switchboard?"),
            maya("PgBouncer, a connection pooler built into flexible server. Dana set pgbouncer dot enabled to true. And nothing happened."),
            leo("Why not?"),
            maya("Because the worker still connected to port 5432. A port is a door number on the server, and PgBouncer waits behind 6432, on the same host. One number, and four hundred app connections became fifty real ones."),
            leo("[chuckle] One number. Wednesday?"),
            maya("Marcus, and the Monday finance report. Two minutes to run, and while it ran the ledger writes crawled. [pause] Marcus asked for a bigger server."),
            leo("And got?"),
            maya("A read replica. A replica is a read-only copy of the server that keeps itself up to date from the original, so the report runs over there and never touches the ledger."),
            leo("Is it exactly up to date?"),
            maya("It lags, usually by seconds, because it copies over in the background. Fine for last week's totals. Not for have we already paid this invoice."),
            leo("And is that the same as the standby everybody keeps mentioning?"),
            maya("No, and that pair is the exam's favourite. A standby is for surviving a zone going down: a second copy in a separate building in the same region, every write on both, and nobody reads from it. A replica is for reads, and it lags."),
            leo("Thursday. There is always a Thursday."),
            maya("The intern. Bless them. They ran a correction script against the live ledger and removed a week of rows."),
            leo("[sigh] Oh no."),
            maya("Oh yes, and it was survivable. Backups are automatic, you choose seven days up to thirty-five, and point-in-time restore brings the data back as it was at a moment you name, down to the minute. They picked two minutes before the script."),
            leo("So they rewound the server."),
            maya("That is the bit they got wrong first. Restore builds a new server at that moment; the broken one stays. They restored, checked the totals, and pointed the worker at the new name."),
            leo("And Friday? There is always a Friday."),
            maya("Priya. Priya never makes mistakes, so it is still the talk of the kitchen. She had moved the worker to Microsoft Entra authentication. Entra ID is Azure's sign-in service, like the front desk at a festival that knows everyone."),
            leo("So no password anywhere?"),
            maya("None. The worker has a managed identity, an identity Azure gives the app itself, like a staff badge, and it sends a token in the password field: a short-lived signed pass, like a wristband."),
            leo("Then what went wrong?"),
            maya("She fetched one wristband when the worker started, and kept it. About an hour later, new connections began failing. You fetch a fresh one each time you open new connections."),
            leo("Anything else this week?"),
            maya("The quiet one. The server was created with a public address and a firewall list, and the auditor asked why a payouts ledger can be reached from the internet at all."),
            leo("And the answer?"),
            maya("Private access, virtual network integration: the server sits inside your own network with no public address, and a private DNS zone resolves the same host name inside it. The catch is that you choose that when you create the server."),
            leo("So they cannot flip a switch."),
            maya("A new server and a restore, one quiet Sunday, and nobody is looking forward to it."),
            leo("And the report? Two minutes even on the replica, you said."),
            maya("Marcus ran EXPLAIN ANALYZE, which runs the query and shows the route it took, like a sat-nav reporting the real journey afterwards. It said Seq Scan: every row, cover to cover."),
            leo("Missing index?"),
            maya("A missing B-tree index on the restaurant id and the week. An index is like the index at the back of a book, and a B-tree is a phone book, where Patel to Price sit together. Two minutes became under a second."),
            leo("So, the gossip I should remember for the exam?"),
            maya("A managed identity sends an Entra token as the password. A zone that must be able to fail means a standby nobody reads from. Reports that slow the ledger mean a read replica, slightly behind."),
            leo("And the rest of the week?"),
            maya("Too many short connections mean built-in PgBouncer, same host, port 6432, never on Burstable. Back to just before a bad script means point-in-time restore, and it makes a new server. No public address means private access, chosen at creation."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a flexible server with Microsoft Entra authentication and add yourself as admin. Connect from Python with psycopg, the standard driver, and a token from DefaultAzureCredential. Then insert a few thousand rows and run EXPLAIN ANALYZE on a filter before and after a B-tree index."),
            leo("Door 6432, and nothing public. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-postgres",
        listOf(
            nova("Hey, you. It is late, the kitchen has closed, and the riders have gone home. [pause] Tonight, the quietest job Zaiqa does: paying the restaurants."),
            ivy("Sunday money. Go on, Nova. Where does it live?"),
            nova("Zaiqa's orders and payments are in Azure SQL, and they stay there. But the payouts ledger, the running record of what we owe each restaurant, goes somewhere new: Azure Database for PostgreSQL flexible server."),
            ivy("A second database. Is that not a small scandal?"),
            nova("It is a real cost, and I will not dress it up. Two sets of backups, two things to patch. [pause] The payouts team runs PostgreSQL and came with years of reconciliation queries, and the exam tests PostgreSQL. A decision, not a default."),
            ivy("And flexible server means?"),
            nova("Ordinary PostgreSQL that Azure runs for you: the patching, the backups, the staying up. There was an older shape called single server, and it is retired. [pause] If an exam answer offers it, walk past."),
            ivy("Tell me what I am buying, then."),
            nova("Compute and storage, separately. Burstable for playing, General Purpose for most real work, Memory Optimized for heavy reporting. The disk you choose only ever grows, so start modest."),
            ivy("And Burstable is the one you are about to warn me about."),
            nova("[chuckle] It is. The cheap tier for testing. Almost everything I am going to tell you tonight is missing there."),
            ivy("Start with the thing that frightens me. The machine dies at two in the morning."),
            nova("Then you have a standby: a second copy of the server kept in step with the first, in another availability zone, a separate building in the same region. Every write lands on both. [pause] Azure fails over, the name stays the same, and you pay about double."),
            ivy("And I read my reports from the spare, surely."),
            nova("Nobody reads from the standby. It sits there warm and silent. [pause] That one is on the exam, in those exact words."),
            ivy("Then finance gets nothing."),
            nova("Finance gets a read replica. A replica is a read-only copy of the server that keeps itself up to date from the original, and the Monday report runs over there while the ledger carries on."),
            ivy("Is it exactly current?"),
            nova("It is a little behind, usually seconds, because it copies over in the background. Last week's totals, fine. Have we already paid this invoice, ask the real server."),
            ivy("Standby for failing, replica for reading. Mm. Now, who is writing to this thing?"),
            nova("The payout worker, in Container Apps, woken by the change feed when an order is delivered. On a Sunday it scales to forty copies, and every copy opens its own connections."),
            ivy("And connections here are not free."),
            nova("Every one is a whole separate program on the server. A handshake, encryption, a sign-in, and memory held for as long as it stays open. [pause] A few hundred idle ones eat the memory the queries need."),
            ivy("So the server starts saying no."),
            nova("At the worst possible hour. The answer is pooling. Picture a sales floor with three hundred staff and twenty phone lines: you pick up a free one, make your call, hang up."),
            ivy("And someone minds the lines."),
            nova("PgBouncer, a connection pooler, already built into flexible server. Turn on pgbouncer dot enabled, no restart, and connect to the same host on port 6432 instead of 5432. [pause] A port is a door number."),
            ivy("Same house, back door. How many real lines?"),
            nova("Four hundred app connections, maybe fifty real ones, each lent out for a single transaction. Keep a small pool inside the worker too. And none of this exists on Burstable."),
            ivy("And the password it whispers at the door?"),
            nova("There is no password. The worker has a managed identity, an identity Azure gives the app itself, like a staff badge, and the server has Microsoft Entra authentication on. Entra ID is Azure's sign-in service, like the front desk at a festival."),
            ivy("So it shows the badge, and the desk hands it?"),
            nova("A token: a short-lived signed pass, like a festival wristband, sent in the password field. PostgreSQL checks the signature and the name inside. [pause] Nothing secret is stored anywhere."),
            ivy("Short-lived. Like most things after midnight."),
            nova("About an hour. So fetch a fresh one whenever you open new connections. Our worker is .NET; in Python it is psycopg, the standard PostgreSQL driver, with DefaultAzureCredential doing the asking."),
            ivy("Finance again. Two minutes for their report, even on that replica."),
            nova("Then put EXPLAIN ANALYZE in front of the query. It runs the query and shows the route it took, like a sat-nav reporting the real journey afterwards. Theirs said Seq Scan: every row, cover to cover."),
            ivy("Something is missing."),
            nova("An index. Like the index at the back of a book: it finds the rows without reading everything. A B-tree index, the default kind, on the restaurant id and the week. [pause] A phone book, where Patel to Price sit together."),
            ivy("And it goes on the real server, not the copy."),
            nova("On the real one, and it travels across by itself. Two minutes became under a second."),
            ivy("Now frighten me again. Somebody deletes a week of payouts."),
            nova("Backups are automatic, and you choose how far back you can reach: seven days up to thirty-five. Then point-in-time restore brings the data back as it was at a moment you name, to the minute."),
            ivy("Rewinding the server. How satisfying."),
            nova("It does not rewind it. [pause] Restore builds a new server at that moment, and the broken one stays where it is. You restore, check the totals, and move the worker to the new name."),
            ivy("One more before I fade. Is it out there on the internet?"),
            nova("Only if you chose that. Public access gives it a public address with a firewall list of who may knock. Private access, virtual network integration, puts it inside your own network with no public address, and a private DNS zone resolves the same name inside."),
            ivy("And I can change my mind later?"),
            nova("You cannot. It is chosen when the server is created. [pause] A new server and a restore, some quiet Sunday. And the pgvector episode takes this same server further, another night."),
            ivy("[sigh] Mm. Before I drift off. What do I remember for the exam?"),
            nova("A managed identity sends an Entra token as the password. A zone that must be able to fail means a standby nobody reads from. Reports that must not slow the ledger mean a read replica, slightly behind."),
            ivy("And the ones that catch people?"),
            nova("Too many short connections mean built-in PgBouncer, same host, port 6432, never on Burstable. Back to just before the bad script means point-in-time restore, and it makes a new server. No public address means private access, chosen at creation."),
            ivy("And something to try today, before bed?"),
            nova("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. [pause] Then insert a few thousand rows and run EXPLAIN ANALYZE on a filter before and after a B-tree index."),
            ivy("Paid, pooled and private. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-postgres",
        listOf(
            nova("Tonight is date night, darling, and tonight we are talking about money. Specifically, paying two thousand restaurants every Sunday."),
            rafe("Romance. And here I was expecting a candle."),
            nova("The candle is lit. Zaiqa's orders and payments stay in Azure SQL, love, but the payouts ledger, the running record of what we owe each restaurant, is moving to Azure Database for PostgreSQL flexible server."),
            rafe("A second relational database. Bold, in front of the neighbours."),
            nova("It is a real cost, and I will say so: two sets of backups, two things to patch. The payouts team runs PostgreSQL and came with years of reconciliation queries, and the exam tests PostgreSQL."),
            rafe("Flexible server, though. Define it for me, slowly."),
            nova("Ordinary PostgreSQL that Azure runs for you: patching, backups, staying up. The older single server shape is retired, so in an exam answer it is never the right one."),
            rafe("Then a question, and be strict with yourself. The machine must survive its building losing power, and we can lose nothing. What do we buy?"),
            nova("High availability with a standby: a second copy of the server kept in step with the first, in another availability zone, which is a separate building in the same region. Every write lands on both, and Azure fails over without changing the name."),
            rafe("Right answer. Someone who turns up when the lights go out. [kiss]"),
            nova("Mm. And it costs about double, because we are renting two machines."),
            rafe("Then let finance run their Monday report on the spare. It is just sitting there."),
            nova("No kiss for that one, darling. Nobody reads from the standby. It is warm and silent, and the exam loves asking exactly that."),
            rafe("Then where does the report go?"),
            nova("To a read replica. A replica is a read-only copy of the server that keeps itself up to date from the original. The report runs there, the ledger never feels it, and the copy lags by seconds."),
            rafe("Seconds. So not for have we already paid this invoice."),
            nova("Never for that. Now, my turn to be difficult. Sunday night, the change feed hands the payout worker a few thousand delivered orders, Container Apps scales it to forty copies, and the server starts refusing. Why?"),
            rafe("Connections. In PostgreSQL each one is a whole separate program on the server, costly to open and hungry for memory, and forty copies opening their own hit the limit."),
            nova("Right answer, and quick with it. [kiss]"),
            rafe("So we share. Pooling, is it?"),
            nova("Pooling. Picture a sales floor with three hundred staff and twenty phone lines: nobody owns a line, you pick a free one, make the call, hang up."),
            rafe("And who keeps the lines?"),
            nova("PgBouncer, a connection pooler built into flexible server. Four hundred app connections, maybe fifty real ones, each lent out for a single transaction."),
            rafe("Then here is a real exam question, love. Built-in PgBouncer is switched on and nothing changes. What did they forget?"),
            nova("The port. A port is a door number on the server: PgBouncer waits behind 6432, and the app is still knocking at 5432, on the same host."),
            rafe("Right answer. Same house, back door. [kiss]"),
            nova("And the one underneath it: none of this exists on Burstable, the cheap tier for testing. You want General Purpose, or Memory Optimized for heavy reporting."),
            rafe("Noted. Now, what does our worker whisper at the door to get in?"),
            nova("Nothing secret. It has a managed identity, an identity Azure gives the app itself, like a staff badge, and the server has Microsoft Entra authentication on. Entra ID is Azure's sign-in service, like the front desk at a festival."),
            rafe("And the desk hands it a token: a short-lived signed pass, like a wristband, which goes in the password field."),
            nova("Right answer, and no secrets between us. [kiss]"),
            rafe("Wristbands do not last the night, though."),
            nova("About an hour. So fetch a fresh one whenever you open new connections. In Python that is psycopg, the standard PostgreSQL driver, with DefaultAzureCredential asking on your behalf."),
            rafe("My question again. The Monday report takes two minutes even on the replica. How do I find out why, before I spend money?"),
            nova("EXPLAIN ANALYZE in front of the query. It runs the query and shows the route it took, like a sat-nav reporting the real journey afterwards. If it says Seq Scan, it read every row, cover to cover."),
            rafe("Right answer. You always check the route. [kiss]"),
            nova("And the cure was a B-tree index on the restaurant id and the week. An index is like the index at the back of a book, and a B-tree is a phone book, where Patel to Price sit together."),
            rafe("Two minutes to?"),
            nova("Under a second. Now be strict with me. The intern removes a week of payout rows on a Tuesday morning. What saves us?"),
            rafe("Point-in-time restore. Backups are automatic, you keep seven days up to thirty-five, and it brings the data back as it was at a moment you name, to the minute. And it builds a new server; it does not rewind the old one."),
            nova("Right answer, and you even said the hard half. [kiss]"),
            rafe("One more for the evening. The auditor asks why a payouts ledger is reachable from the internet."),
            nova("Because somebody chose public access, which gives it a public address with a firewall list. Private access, virtual network integration, puts the server inside our own network with no public address, and a private DNS zone resolves the same name inside."),
            rafe("And we can switch afterwards?"),
            nova("We cannot. It is chosen when the server is created, so a change means a new server and a restore. The pgvector episode takes this same server further, another date."),
            rafe("Then the last question, for the exam. What do we remember?"),
            nova("A managed identity sends an Entra token as the password. A zone that must be able to fail means a standby nobody reads from. Reports that slow the ledger mean a read replica, slightly behind."),
            rafe("That is three. The two that caught us?"),
            nova("Too many short connections mean built-in PgBouncer, same host, port 6432, never on Burstable. And no public address means private access, chosen when the server is created."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a flexible server with Microsoft Entra authentication, and add yourself as admin. Connect from Python with psycopg and a token from DefaultAzureCredential. Then insert a few thousand rows and run EXPLAIN ANALYZE on a filter before and after a B-tree index."),
            nova("Paid, pooled and private. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun stores() = DiagramSpec(
        "Orders and payments stay in Azure SQL; the payouts ledger, written by the payout worker, goes into PostgreSQL.",
        nodes = listOf(
            DNode("sql", "Azure SQL", 0.26f, 0.16f, w = 0.46f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "orders, payments"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.16f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "events, pings"),
            DNode("pg", "PostgreSQL", 0.26f, 0.54f, w = 0.46f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "the payouts ledger"),
            DNode("worker", "Payout worker", 0.76f, 0.54f, w = 0.42f, h = 0.24f, sub = "the change feed"),
            DNode("why", "A second relational database", 0.5f, 0.86f, w = 0.8f, h = 0.16f, kind = NodeKind.NOTE, sub = "a decision, not a default"),
        ),
        edges = listOf(
            DEdge("cosmos", "worker", "delivered"),
            DEdge("worker", "pg", "writes"),
            DEdge("sql", "worker", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun tiers() = DiagramSpec(
        "Compute comes in three tiers and moves up or down with a restart; storage only ever grows, and Burstable lacks the extras.",
        nodes = listOf(
            DNode("compute", "Compute", 0.5f, 0.12f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, tone = 1, sub = "up or down"),
            DNode("burst", "Burstable", 0.17f, 0.46f, w = 0.3f, h = 0.24f, tone = 2, sub = "testing only"),
            DNode("gp", "General Purpose", 0.5f, 0.46f, w = 0.32f, h = 0.24f, sub = "most real work"),
            DNode("mo", "Memory Optimized", 0.84f, 0.46f, w = 0.3f, h = 0.24f, sub = "heavy reports"),
            DNode("disk", "Storage", 0.22f, 0.82f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "only grows"),
            DNode("note", "Burstable has none of this", 0.72f, 0.82f, w = 0.5f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "no standby, no pooler"),
        ),
        edges = listOf(
            DEdge("compute", "burst"),
            DEdge("compute", "gp"),
            DEdge("compute", "mo"),
            DEdge("burst", "note", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun ha() = DiagramSpec(
        "A standby in another zone takes over when the first fails; a read replica serves the finance report, a little behind.",
        nodes = listOf(
            DNode("primary", "Primary server", 0.22f, 0.3f, w = 0.4f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 1, sub = "the ledger"),
            DNode("standby", "Standby", 0.74f, 0.3f, w = 0.4f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "another zone"),
            DNode("replica", "Read replica", 0.74f, 0.72f, w = 0.4f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "a little behind"),
            DNode("report", "Finance report", 0.22f, 0.72f, w = 0.4f, h = 0.2f, sub = "Monday"),
        ),
        edges = listOf(
            DEdge("primary", "standby", "every write"),
            DEdge("primary", "replica", "copies over"),
            DEdge("report", "replica", "reads"),
        ),
        aspect = 0.8f,
    )

    private fun pool() = DiagramSpec(
        "Forty copies of the payout worker connect through PgBouncer on port 6432, which shares a few real connections.",
        nodes = listOf(
            DNode("apps", "40 worker copies", 0.2f, 0.5f, w = 0.34f, h = 0.3f, tone = 1, sub = "small pool each"),
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

    private fun signin() = DiagramSpec(
        "The worker shows its managed identity, Entra ID hands back a token, and the token goes in the password field.",
        nodes = listOf(
            DNode("entra", "Entra ID", 0.16f, 0.16f, w = 0.28f, h = 0.2f, tone = 2, sub = "token as password"),
            DNode("worker", "Payout worker", 0.16f, 0.5f, w = 0.28f, h = 0.22f, tone = 1, sub = "a badge"),
            DNode("pgb", "PgBouncer", 0.5f, 0.5f, w = 0.28f, h = 0.22f, sub = "port 6432"),
            DNode("pg", "PostgreSQL", 0.84f, 0.5f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "the signature"),
            DNode("note", "Nothing secret is stored", 0.5f, 0.86f, w = 0.7f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("worker", "entra", "get token"),
            DEdge("worker", "pgb", "connect"),
            DEdge("pgb", "pg", "pooled"),
        ),
        aspect = 0.8f,
    )

    private fun plan() = DiagramSpec(
        "EXPLAIN ANALYZE runs the query and shows its plan: a Seq Scan reads every row, an Index Scan uses the index.",
        nodes = listOf(
            DNode("query", "EXPLAIN ANALYZE", 0.5f, 0.15f, w = 0.7f, h = 0.2f, tone = 1, sub = "runs it, shows the plan"),
            DNode("seq", "Seq Scan", 0.26f, 0.56f, w = 0.46f, h = 0.26f, tone = 2, sub = "every row: two minutes"),
            DNode("idx", "Index Scan", 0.76f, 0.56f, w = 0.44f, h = 0.26f, sub = "under a second"),
            DNode("note", "B-tree on restaurant and week", 0.5f, 0.9f, w = 0.76f, h = 0.14f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("query", "seq", "before"),
            DEdge("query", "idx", "after"),
        ),
        aspect = 0.8f,
    )

    private fun restore() = DiagramSpec(
        "Backups run on their own, and a point-in-time restore builds a new server as it was, leaving the broken one alone.",
        nodes = listOf(
            DNode("backups", "Automatic backups", 0.5f, 0.14f, w = 0.6f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "7 to 35 days"),
            DNode("bad", "The bad script", 0.24f, 0.48f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "a week of rows gone"),
            DNode("old", "The old server", 0.24f, 0.82f, w = 0.44f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "still there"),
            DNode("new", "A new server", 0.76f, 0.65f, w = 0.42f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "to the minute"),
        ),
        edges = listOf(
            DEdge("backups", "new", "restore"),
            DEdge("bad", "old"),
            DEdge("old", "new", "point in time", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun network() = DiagramSpec(
        "Public access gives the server an address on the internet; private access puts it inside your own network instead.",
        nodes = listOf(
            DNode("public", "Public access", 0.25f, 0.18f, w = 0.44f, h = 0.22f, tone = 2, sub = "a firewall list"),
            DNode("private", "Private access", 0.75f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "no public address"),
            DNode("worker", "Payout worker", 0.25f, 0.56f, w = 0.44f, h = 0.2f, sub = "inside the network"),
            DNode("dns", "Private DNS zone", 0.75f, 0.56f, w = 0.44f, h = 0.2f, sub = "the same host name"),
            DNode("note", "Chosen when you create the server", 0.5f, 0.86f, w = 0.8f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("worker", "dns", "looks up"),
            DEdge("dns", "private", "resolves"),
            DEdge("public", "private", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's PostgreSQL questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("mi", "Managed identity", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Entra token as password"),
            DNode("zone", "A zone could fail", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a standby, never read"),
            DNode("reports", "Reports slow the ledger", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a read replica"),
            DNode("conns", "Too many connections", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "PgBouncer, port 6432"),
            DNode("oops", "Undo a bad script", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "point-in-time restore"),
            DNode("net", "No public address", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "private access"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("mi", "zone", "reports", "conns", "oops", "net")

    // The pictures the tellings share, one card each.
    private val todayCard = BoardCard("Today", "Entra sign-in, psycopg with a token, EXPLAIN ANALYZE before and after a B-tree.")
    private val ledgerCard = BoardCard("The payouts ledger", "What we owe each restaurant, paid every Sunday, in Azure Database for PostgreSQL.")
    private val costCard = BoardCard("A real cost", "Two sets of backups, two things to patch, two sign-ins to get right.")
    private val decisionCard = BoardCard("A decision, not a default", "The payouts team runs PostgreSQL, and the exam tests it. Orders stay in Azure SQL.")
    private val flexCard = BoardCard("Flexible server", "Ordinary PostgreSQL that Azure patches, backs up and keeps running.")
    private val tiersCard = BoardCard("Compute and storage", "Burstable, General Purpose, Memory Optimized. The disk only ever grows.")
    private val burstCard = BoardCard("Not on Burstable", "No standby, no read replica, no built-in PgBouncer. Pick General Purpose.")
    private val burstPickCard = BoardCard("Monday: Tom picked Burstable", "The cheap tier for testing, because the ledger is only a few tables.")
    private val standbyCard = BoardCard("High availability: a standby", "A second copy in another zone. Every write lands on both, at roughly double the cost.")
    private val failoverCard = BoardCard("Failover", "Azure switches over, the server name stays, and the worker reconnects.")
    private val silentCard = BoardCard("Nobody reads the standby", "It sits there warm and silent. The exam asks this one in those words.")
    private val replicaCard = BoardCard("Read replica", "A read-only copy, kept up to date, for the Monday report. It can sit in another region.")
    private val lagCard = BoardCard("A little behind", "Seconds, usually. Fine for last week's totals, not for have we paid this.")
    private val pairCard = BoardCard("The pair the exam loves", "A zone could fail: a standby. Reports slowing the ledger: a read replica.")
    private val reportCard = BoardCard("The Monday report", "Two minutes to run, and the ledger writes crawled while it ran.")
    private val fortyCard = BoardCard("Sunday at nine", "The change feed hands over thousands of orders; the worker scales to forty copies.")
    private val processCard = BoardCard("A connection is a program", "Handshake, encryption, sign-in, and memory held while it stays open.")
    private val linesCard = BoardCard("Pooling: shared phone lines", "300 staff, 20 lines: pick up a free one, make the call, hang up.")
    private val bouncerCard = BoardCard("PgBouncer", "400 app connections share about 50 real ones, each lent for one transaction.")
    private val portCard = BoardCard("Port 6432", "Same host, different door: 5432 goes straight in, 6432 through PgBouncer.")
    private val wrongDoorCard = BoardCard("Switched on, nothing changed", "Still connecting to 5432: same host, wrong door, nothing through the pool.")
    private val frontDeskCard = BoardCard("Entra ID: the front desk", "Azure's sign-in service: it knows who everyone is and hands out wristbands.")
    private val wristbandCard = BoardCard("Token: a festival wristband", "The managed identity's short-lived pass, sent in the password field.")
    private val freshCard = BoardCard("About an hour", "Fetch a fresh token whenever you open new connections, never once at start-up.")
    private val satNavCard = BoardCard("EXPLAIN ANALYZE: a sat-nav", "Runs the query and shows the route it took, with the real times.")
    private val btreeCard = BoardCard("B-tree: a phone book", "On the restaurant id and the week. Patel to Price sit together. Under a second.")
    private val onRealCard = BoardCard("Build it on the real server", "The index copies across to the read replica by itself.")
    private val backupCard = BoardCard("Backups, 7 to 35 days", "Automatic, and you choose how far back you can reach.")
    private val pitrCard = BoardCard("Point-in-time restore", "It builds a new server as it was, to the minute. The broken one stays.")
    private val scriptCard = BoardCard("The correction script", "A week of payout rows removed, against the live ledger.")
    private val publicCard = BoardCard("Public access", "A public address with a firewall list of who may knock.")
    private val privateCard = BoardCard("Private access", "Virtual network integration: inside your own network, with no public address.")
    private val dnsCard = BoardCard("Private DNS zone", "The same host name, resolved inside the network. Nothing outside can look it up.")
    private val atCreateCard = BoardCard("Chosen at creation", "You cannot flip it later: changing means a new server and a restore.")
    private val phraseCard = BoardCard("Listen for the phrase", "Token as password. A zone could fail. Reports slowing the ledger.")
    private val phrase2Card = BoardCard("And three more", "Too many connections, back to before the script, no public address.")

    private fun classicBoard() = PodcastBoard(
        "ai-postgres",
        listOf(
            // 0: "Hi, and welcome back. The Zaiqa API..."
            Scene(
                0, "A second database, on purpose", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql", "cosmos"), light = setOf("sql"), card = BoardCard("Where Zaiqa keeps things", "Orders and payments in Azure SQL; events and rider pings in Cosmos DB.")),
                    Beat(2, show = setOf("pg", "worker"), light = setOf("pg"), pulse = "worker" to "pg", card = ledgerCard),
                    Beat(4, light = setOf("sql", "pg"), card = costCard),
                    Beat(6, show = setOf("why"), light = setOf("why"), card = decisionCard),
                    Beat(8, light = setOf("pg"), card = flexCard),
                ),
            ),
            // 9: "Good. And how big?"
            Scene(
                9, "Compute and storage", tiers(),
                beats = listOf(
                    Beat(10, show = setOf("compute", "burst", "gp", "mo", "disk", "note"), light = setOf("burst", "gp", "mo"), pulse = "compute" to "gp", card = tiersCard),
                ),
            ),
            // 11: "What happens when the machine under my ledger dies on a Sunday?"
            Scene(
                11, "When the machine dies", ha(),
                beats = listOf(
                    Beat(12, show = setOf("primary", "standby"), light = setOf("standby"), pulse = "primary" to "standby", card = standbyCard),
                    Beat(14, light = setOf("standby"), card = failoverCard),
                    Beat(16, light = setOf("standby"), card = silentCard),
                    Beat(18, show = setOf("replica", "report"), light = setOf("replica"), pulse = "report" to "replica", card = replicaCard),
                    Beat(20, light = setOf("replica"), card = lagCard),
                    Beat(22, light = setOf("standby", "replica"), card = pairCard),
                ),
            ),
            // 23: "Right. Now, the payout worker."
            Scene(
                23, "Sunday night connections", pool(),
                beats = listOf(
                    Beat(24, show = setOf("apps", "pg"), light = setOf("apps"), card = fortyCard),
                    Beat(26, light = setOf("apps"), card = processCard),
                    Beat(28, light = setOf("pg"), card = linesCard),
                    Beat(30, show = setOf("pgb", "direct"), light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(32, light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(34, show = setOf("tier"), light = setOf("tier"), card = burstCard),
                ),
            ),
            // 35: "What password does the worker use?"
            Scene(
                35, "No password at the door", signin(),
                beats = listOf(
                    Beat(36, show = setOf("worker", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(38, light = setOf("worker", "entra"), pulse = "worker" to "entra", card = wristbandCard),
                    Beat(40, show = setOf("note"), light = setOf("note"), card = freshCard),
                ),
            ),
            // 41: "Their report takes two minutes, even on the replica."
            Scene(
                41, "Why the report is slow", plan(),
                beats = listOf(
                    Beat(42, show = setOf("query", "seq"), light = setOf("seq"), pulse = "query" to "seq", card = satNavCard),
                    Beat(44, show = setOf("idx"), light = setOf("idx"), pulse = "query" to "idx", card = btreeCard),
                ),
            ),
            // 45: "Somebody runs a bad correction script."
            Scene(
                45, "The bad script", restore(),
                beats = listOf(
                    Beat(46, show = setOf("backups"), light = setOf("backups"), card = backupCard),
                    Beat(48, show = setOf("bad", "old", "new"), light = setOf("new"), pulse = "old" to "new", card = pitrCard),
                ),
            ),
            // 49: "Is this thing on the public internet?"
            Scene(
                49, "Public or private", network(),
                beats = listOf(
                    Beat(50, show = setOf("public", "private"), light = setOf("private"), pulse = "public" to "private", card = privateCard),
                    Beat(52, show = setOf("worker", "dns"), light = setOf("dns"), pulse = "worker" to "dns", card = dnsCard),
                    Beat(54, show = setOf("note"), light = setOf("note"), card = atCreateCard),
                ),
            ),
            // 55: "So what do I remember for the exam?"
            Scene(
                55, "For the exam", exam(),
                beats = listOf(
                    Beat(56, show = all6, light = setOf("mi", "zone", "reports"), card = phraseCard),
                    Beat(58, light = setOf("conns", "oops", "net"), card = phrase2Card),
                    Beat(60, card = todayCard),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-postgres--case-files",
        listOf(
            // 0: "Tonight's case: The Sunday That Paid Nobody."
            Scene(
                0, "The Sunday That Paid Nobody",
                DiagramSpec(
                    "The change feed wakes forty copies of the payout worker, and PostgreSQL runs out of connections.",
                    nodes = listOf(
                        DNode("feed", "Change feed", 0.22f, 0.18f, w = 0.4f, h = 0.2f, sub = "delivered orders"),
                        DNode("err", "Too many connections", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "Sunday, nine"),
                        DNode("worker", "Payout worker", 0.22f, 0.58f, w = 0.4f, h = 0.22f, tone = 1, sub = "40 copies"),
                        DNode("pg", "PostgreSQL", 0.76f, 0.58f, w = 0.4f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "the ledger"),
                    ),
                    edges = listOf(
                        DEdge("feed", "worker", "wakes"),
                        DEdge("worker", "pg", "writes"),
                        DEdge("pg", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("feed", "worker", "pg"), light = setOf("pg"), pulse = "worker" to "pg", card = BoardCard("The case", "Nine restaurants of two thousand paid, and then everything stopped.")),
                    Beat(2, light = setOf("pg"), card = decisionCard),
                    Beat(4, show = setOf("err"), light = setOf("err"), pulse = "pg" to "err", card = BoardCard("Too many connections", "Forty copies of the worker, each opening its own.")),
                ),
            ),
            // 5: "Is one connection really that expensive?"
            Scene(
                5, "The suspects", pool(),
                beats = listOf(
                    Beat(6, show = setOf("apps", "pg"), light = setOf("apps"), card = processCard),
                    Beat(8, light = setOf("pg"), card = linesCard),
                    Beat(10, light = setOf("pg"), card = flexCard),
                    Beat(12, show = setOf("pgb"), light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(16, show = setOf("direct"), light = setOf("direct"), pulse = "apps" to "direct", card = wrongDoorCard),
                    Beat(18, show = setOf("tier"), light = setOf("tier"), card = burstCard),
                ),
            ),
            // 19: "How many tiers are there?"
            Scene(
                19, "Three tiers", tiers(),
                beats = listOf(
                    Beat(20, show = setOf("compute", "burst", "gp", "mo", "disk", "note"), light = setOf("gp"), pulse = "compute" to "gp", card = tiersCard),
                ),
            ),
            // 21: "But what if the machine itself had died mid-run?"
            Scene(
                21, "The machine that held", ha(),
                beats = listOf(
                    Beat(22, show = setOf("primary", "standby"), light = setOf("standby"), pulse = "primary" to "standby", card = standbyCard),
                    Beat(24, light = setOf("standby"), card = silentCard),
                    Beat(26, show = setOf("report"), light = setOf("report"), card = reportCard),
                    Beat(28, show = setOf("replica"), light = setOf("replica"), pulse = "report" to "replica", card = replicaCard),
                    Beat(30, light = setOf("replica"), card = lagCard),
                ),
            ),
            // 31: "And the second complaint?"
            Scene(
                31, "Two minutes on the report", plan(),
                beats = listOf(
                    Beat(32, show = setOf("query", "seq"), light = setOf("seq"), pulse = "query" to "seq", card = satNavCard),
                    Beat(34, show = setOf("idx"), light = setOf("idx"), pulse = "query" to "idx", card = btreeCard),
                    Beat(36, light = setOf("idx"), card = onRealCard),
                ),
            ),
            // 37: "Any suspects still at large?"
            Scene(
                37, "Still at large", restore(),
                beats = listOf(
                    Beat(38, show = setOf("bad"), light = setOf("bad"), card = scriptCard),
                    Beat(40, show = setOf("backups"), light = setOf("backups"), card = backupCard),
                    Beat(42, show = setOf("old", "new"), light = setOf("new"), pulse = "old" to "new", card = pitrCard),
                ),
            ),
            // 43: "And the public address?"
            Scene(
                43, "The public address", network(),
                beats = listOf(
                    Beat(44, show = setOf("public", "private", "note"), light = setOf("private"), pulse = "public" to "private", card = atCreateCard),
                    Beat(46, show = setOf("worker", "dns"), light = setOf("dns"), pulse = "worker" to "dns", card = dnsCard),
                ),
            ),
            // 47: "Where is the database password kept?"
            Scene(
                47, "Where is the password", signin(),
                beats = listOf(
                    Beat(48, show = setOf("worker", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(50, light = setOf("worker", "entra"), pulse = "worker" to "entra", card = wristbandCard),
                ),
            ),
            // 51: "So, the case summary for the exam?"
            Scene(
                51, "Case closed", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("mi", "zone", "reports"), card = phraseCard),
                    Beat(54, light = setOf("conns", "oops", "net"), card = phrase2Card),
                    Beat(56, card = todayCard),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-postgres--gossip",
        listOf(
            // 0: "Did you hear what Sunday night did to the payouts team?"
            Scene(
                0, "The move to PostgreSQL", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql", "pg", "worker"), light = setOf("pg"), pulse = "worker" to "pg", card = ledgerCard),
                    Beat(2, show = setOf("why"), light = setOf("why"), card = decisionCard),
                ),
            ),
            // 3: "Fair. Who went first?"
            Scene(
                3, "Monday: Tom", tiers(),
                beats = listOf(
                    Beat(4, show = setOf("compute", "burst", "gp", "mo"), light = setOf("burst"), pulse = "compute" to "burst", card = burstPickCard),
                    Beat(6, show = setOf("note", "disk"), light = setOf("note"), card = burstCard),
                    Beat(8, light = setOf("gp", "mo"), card = tiersCard),
                ),
            ),
            // 9: "Tuesday?"
            Scene(
                9, "Tuesday: Dana", pool(),
                beats = listOf(
                    Beat(10, show = setOf("apps", "pg"), light = setOf("apps"), card = processCard),
                    Beat(12, light = setOf("pg"), card = linesCard),
                    Beat(14, show = setOf("pgb"), light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(16, show = setOf("direct"), light = setOf("direct"), pulse = "apps" to "pgb", card = portCard),
                ),
            ),
            // 17: "One number. Wednesday?"
            Scene(
                17, "Wednesday: Marcus", ha(),
                beats = listOf(
                    Beat(18, show = setOf("report", "primary"), light = setOf("report"), card = reportCard),
                    Beat(20, show = setOf("replica"), light = setOf("replica"), pulse = "report" to "replica", card = replicaCard),
                    Beat(22, light = setOf("replica"), card = lagCard),
                    Beat(24, show = setOf("standby"), light = setOf("standby"), pulse = "primary" to "standby", card = pairCard),
                ),
            ),
            // 25: "Thursday. There is always a Thursday."
            Scene(
                25, "Thursday: the intern", restore(),
                beats = listOf(
                    Beat(26, show = setOf("bad"), light = setOf("bad"), card = scriptCard),
                    Beat(28, show = setOf("backups"), light = setOf("backups"), card = backupCard),
                    Beat(30, show = setOf("old", "new"), light = setOf("new"), pulse = "old" to "new", card = pitrCard),
                ),
            ),
            // 31: "And Friday? There is always a Friday."
            Scene(
                31, "Friday: Priya", signin(),
                beats = listOf(
                    Beat(32, show = setOf("worker", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(34, light = setOf("worker", "entra"), pulse = "worker" to "entra", card = wristbandCard),
                    Beat(36, show = setOf("note"), light = setOf("note"), card = freshCard),
                ),
            ),
            // 37: "Anything else this week?"
            Scene(
                37, "The quiet one", network(),
                beats = listOf(
                    Beat(38, show = setOf("public"), light = setOf("public"), card = publicCard),
                    Beat(40, show = setOf("private", "worker", "dns"), light = setOf("private"), pulse = "worker" to "dns", card = privateCard),
                    Beat(42, show = setOf("note"), light = setOf("note"), card = atCreateCard),
                ),
            ),
            // 43: "And the report? Two minutes even on the replica."
            Scene(
                43, "The slow report", plan(),
                beats = listOf(
                    Beat(44, show = setOf("query", "seq"), light = setOf("seq"), pulse = "query" to "seq", card = satNavCard),
                    Beat(46, show = setOf("idx"), light = setOf("idx"), pulse = "query" to "idx", card = btreeCard),
                ),
            ),
            // 47: "So, the gossip I should remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("mi", "zone", "reports"), card = phraseCard),
                    Beat(50, light = setOf("conns", "oops", "net"), card = phrase2Card),
                    Beat(52, card = todayCard),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-postgres--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen has closed..."
            Scene(
                0, "Tonight: Sunday money", stores(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The quietest job Zaiqa does: paying the restaurants on a Sunday.")),
                    Beat(2, show = setOf("pg", "worker"), light = setOf("pg"), pulse = "worker" to "pg", card = ledgerCard),
                    Beat(4, show = setOf("why"), light = setOf("why"), card = decisionCard),
                    Beat(6, light = setOf("pg"), card = flexCard),
                ),
            ),
            // 7: "Tell me what I am buying, then."
            Scene(
                7, "What you are buying", tiers(),
                beats = listOf(
                    Beat(8, show = setOf("compute", "burst", "gp", "mo", "disk"), light = setOf("burst", "gp", "mo"), pulse = "compute" to "gp", card = tiersCard),
                    Beat(10, show = setOf("note"), light = setOf("note"), card = burstCard),
                ),
            ),
            // 11: "The machine dies at two in the morning."
            Scene(
                11, "If the machine dies", ha(),
                beats = listOf(
                    Beat(12, show = setOf("primary", "standby"), light = setOf("standby"), pulse = "primary" to "standby", card = standbyCard),
                    Beat(14, light = setOf("standby"), card = silentCard),
                    Beat(16, show = setOf("replica", "report"), light = setOf("replica"), pulse = "report" to "replica", card = replicaCard),
                    Beat(18, light = setOf("replica"), card = lagCard),
                ),
            ),
            // 19: "Now, who is writing to this thing?"
            Scene(
                19, "Who is writing tonight", pool(),
                beats = listOf(
                    Beat(20, show = setOf("apps", "pg"), light = setOf("apps"), card = fortyCard),
                    Beat(22, light = setOf("apps"), card = processCard),
                    Beat(24, light = setOf("pg"), card = linesCard),
                    Beat(26, show = setOf("pgb", "direct"), light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(28, show = setOf("tier"), light = setOf("tier"), card = bouncerCard),
                ),
            ),
            // 29: "And the password it whispers at the door?"
            Scene(
                29, "No password at the door", signin(),
                beats = listOf(
                    Beat(30, show = setOf("worker", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(32, light = setOf("worker", "entra"), pulse = "worker" to "entra", card = wristbandCard),
                    Beat(34, show = setOf("note"), light = setOf("note"), card = freshCard),
                ),
            ),
            // 35: "Two minutes for their report, even on that replica."
            Scene(
                35, "Two minutes, still", plan(),
                beats = listOf(
                    Beat(36, show = setOf("query", "seq"), light = setOf("seq"), pulse = "query" to "seq", card = satNavCard),
                    Beat(38, show = setOf("idx"), light = setOf("idx"), pulse = "query" to "idx", card = btreeCard),
                    Beat(40, light = setOf("idx"), card = onRealCard),
                ),
            ),
            // 41: "Somebody deletes a week of payouts."
            Scene(
                41, "A week of payouts, gone", restore(),
                beats = listOf(
                    Beat(42, show = setOf("backups", "bad"), light = setOf("backups"), card = backupCard),
                    Beat(44, show = setOf("old", "new"), light = setOf("new"), pulse = "old" to "new", card = pitrCard),
                ),
            ),
            // 45: "Is it out there on the internet?"
            Scene(
                45, "Out there, or not", network(),
                beats = listOf(
                    Beat(46, show = setOf("public", "private"), light = setOf("private"), pulse = "public" to "private", card = privateCard),
                    Beat(48, show = setOf("note", "worker", "dns"), light = setOf("note"), card = atCreateCard),
                ),
            ),
            // 49: "Before I drift off. What do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("mi", "zone", "reports"), card = phraseCard),
                    Beat(52, light = setOf("conns", "oops", "net"), card = phrase2Card),
                    Beat(54, card = todayCard),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-postgres--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night: Sunday money", stores(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Paying two thousand restaurants, every Sunday.")),
                    Beat(2, show = setOf("pg", "worker"), light = setOf("pg"), pulse = "worker" to "pg", card = ledgerCard),
                    Beat(4, show = setOf("why"), light = setOf("why"), card = decisionCard),
                    Beat(6, light = setOf("pg"), card = flexCard),
                ),
            ),
            // 7: "The machine must survive its building losing power."
            Scene(
                7, "Quiz: the lights go out", ha(),
                beats = listOf(
                    Beat(7, show = setOf("primary"), light = setOf("primary"), card = BoardCard("Quiz: the building goes dark", "We can lose nothing. What do we buy?")),
                    Beat(8, show = setOf("standby"), light = setOf("standby"), pulse = "primary" to "standby", card = standbyCard),
                    Beat(11, light = setOf("standby"), card = BoardCard("Quiz: read from the spare?", "Finance wants the Monday report off the standby.")),
                    Beat(12, light = setOf("standby"), card = silentCard),
                    Beat(14, show = setOf("replica", "report"), light = setOf("replica"), pulse = "report" to "replica", card = replicaCard),
                ),
            ),
            // 16: "Sunday night, and the server starts refusing. Why?"
            Scene(
                16, "Quiz: Sunday at nine", pool(),
                beats = listOf(
                    Beat(16, show = setOf("apps", "pg"), light = setOf("apps"), card = BoardCard("Quiz: the server refuses", "Forty copies on Sunday night. Why?")),
                    Beat(17, light = setOf("apps"), card = processCard),
                    Beat(20, light = setOf("pg"), card = linesCard),
                    Beat(22, show = setOf("pgb"), light = setOf("pgb"), pulse = "pgb" to "pg", card = bouncerCard),
                    Beat(23, show = setOf("direct"), light = setOf("direct"), card = BoardCard("Quiz: PgBouncer is on", "Nothing changed. What did they forget?")),
                    Beat(24, light = setOf("pgb"), pulse = "apps" to "pgb", card = portCard),
                    Beat(26, show = setOf("tier"), light = setOf("tier"), card = burstCard),
                ),
            ),
            // 27: "What does our worker whisper at the door?"
            Scene(
                27, "What the worker whispers", signin(),
                beats = listOf(
                    Beat(28, show = setOf("worker", "entra"), light = setOf("entra"), card = frontDeskCard),
                    Beat(29, light = setOf("worker", "entra"), pulse = "worker" to "entra", card = wristbandCard),
                    Beat(32, show = setOf("note"), light = setOf("note"), card = freshCard),
                ),
            ),
            // 33: "The Monday report takes two minutes even on the replica."
            Scene(
                33, "Quiz: find out why", plan(),
                beats = listOf(
                    Beat(33, show = setOf("query"), light = setOf("query"), card = BoardCard("Quiz: two minutes", "How do you find out why, before spending money?")),
                    Beat(34, show = setOf("seq"), light = setOf("seq"), pulse = "query" to "seq", card = satNavCard),
                    Beat(36, show = setOf("idx"), light = setOf("idx"), pulse = "query" to "idx", card = btreeCard),
                ),
            ),
            // 38: "The intern removes a week of payout rows."
            Scene(
                38, "Quiz: the intern's Tuesday", restore(),
                beats = listOf(
                    Beat(38, show = setOf("bad"), light = setOf("bad"), card = BoardCard("Quiz: a week of rows gone", "Tuesday morning, the live ledger. What saves us?")),
                    Beat(39, show = setOf("backups", "old", "new"), light = setOf("new"), pulse = "old" to "new", card = pitrCard),
                ),
            ),
            // 41: "The auditor asks why a payouts ledger is reachable."
            Scene(
                41, "The auditor's question", network(),
                beats = listOf(
                    Beat(41, show = setOf("public"), light = setOf("public"), card = publicCard),
                    Beat(42, show = setOf("private", "worker", "dns"), light = setOf("private"), pulse = "worker" to "dns", card = privateCard),
                    Beat(44, show = setOf("note"), light = setOf("note"), card = atCreateCard),
                ),
            ),
            // 45: "Then the last question, for the exam."
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("mi", "zone", "reports"), card = phraseCard),
                    Beat(48, light = setOf("conns", "net"), card = phrase2Card),
                    Beat(51, card = todayCard),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
