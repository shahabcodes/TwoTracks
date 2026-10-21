package com.shahaabapps.core.content

/** KQL: asking your logs and metrics questions: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesKql {
    private val classic = Podcast(
        "ai-kql",
        listOf(
            maya("Hi, and welcome back. Today is about asking questions of your app's logs. When was it slow? What failed, and why? The answers are already written down somewhere. Today you learn the language for asking."),
            leo("Written down where, exactly?"),
            maya("Say you're building the Two Tracks Coach, a study assistant in an Android app. The phone calls the Coach API, a Python program on Azure, Microsoft's cloud. API means the part other programs call. Everything the API reports about itself, every request and every error, goes to Application Insights, Azure's service for collecting that."),
            leo("And Application Insights keeps it all?"),
            maya("It stores it in a Log Analytics workspace. Picture a giant spreadsheet workbook. Each sheet is a table, each row is one event, like one request, and each column is one fact about it, like how long it took. Every row has a time."),
            leo("So which sheets are in the workbook?"),
            maya("The one you'll use most is AppRequests: the calls your app received, one row per question a student asked. Its columns include Name, the address that was called, DurationMs, how many milliseconds it took, and Success, true or false."),
            leo("And the calls my app makes out, to other things?"),
            maya("Those go in AppDependencies: every call to a database or another program. Errors go in AppExceptions. And the plain log lines your code printed go in AppTraces."),
            leo("Okay, I have a workbook. How do I ask it something?"),
            maya("With KQL, the Kusto Query Language. Kusto was Microsoft's internal name for the engine. A query is just a question written in that language. And KQL reads like an assembly line: you start with a table, and pass its rows through steps, one after another."),
            leo("Not like SQL, the usual database language, where you start with select?"),
            maya("No. You start with the table's name, AppRequests. On the page, each step after it begins with a pipe, a straight upright line that means: hand the rows on. Each step takes the table from the step before, and hands a new one on. For example, keep some rows, then count them."),
            leo("What's the first step, usually?"),
            maya("A where. Where keeps only the rows that match a rule, like a sieve. And the first rule should almost always be about time, using the TimeGenerated column: the time each row was written."),
            leo("And how do I say, the last hour?"),
            maya("With ago. Ago means a time counted back from now, so ago one hour is one hour before now. Keep the rows where TimeGenerated is later than that. Forget the time filter, and the query trawls through weeks of data, slowly."),
            leo("Then what?"),
            maya("Then shape the result. Project picks the columns you want to see, like Name and DurationMs, and drops the rest."),
            leo("And if I want to add a column of my own?"),
            maya("That's extend. It adds a new column you calculate, and keeps every other column. For example, a column called slow, true when DurationMs is over two thousand milliseconds, which is two seconds. Project there would throw the other columns away, a classic exam trap."),
            leo("Now the counting."),
            maya("That's summarize. It groups rows and works something out for each group: a count, an average, a percentile. Count requests by ResultCode, the number a server sends back to say how it went, and you get one row per code, like 200 for fine or 500 for a server error."),
            leo("And by time? Requests per five minutes?"),
            maya("That needs bin. Bin rounds each time down to the start of its bucket, like sorting letters into five-minute pigeonholes. Summarize the count by bin of TimeGenerated, five minutes, and you get one row per five-minute slice."),
            leo("And a picture would be nice."),
            maya("Render timechart. Render draws the result, and a timechart is a line chart with time along the bottom. Add it as the last step, and the numbers become a line you can read at a glance."),
            leo("Okay. The Coach is slow in the evenings. Do I chart the average time?"),
            maya("No, and that's the most useful idea today. An average hides the slow tail. Use a percentile. The 95th percentile, p95 for short, is the time that 95 of every 100 requests beat. It tells you what the slowest one in twenty felt like. That's the request students complain about."),
            leo("So, the Coach's evening question."),
            maya("Start from AppRequests. Keep the last seven days, where the name has ask in it. Summarize the 95th percentile of DurationMs by bin of TimeGenerated, one hour. Render a timechart. On the Coach, the line jumped from under a second to six seconds, every evening at eight."),
            leo("[pause] But the line doesn't say why."),
            maya("For why, you join. Join matches rows from two tables that share a value. The right shared value is OperationId: the trace id that every row of one request carries, its tracking number. Join the slow requests to AppDependencies on OperationId, and you see the time went into the database, not the API."),
            leo("Could I join on something else? ResultCode, say?"),
            maya("[sigh] Please don't. Thousands of unrelated requests share a 500, so you'd match everything with everything: a huge pile of nonsense. OperationId, always. The same goes for joining failed requests to AppExceptions, to see the error behind each one."),
            leo("And the last ten rows, newest first?"),
            maya("Order by TimeGenerated, descending, then take ten. Take returns that many rows. It knows nothing about time, so take one hour isn't a thing. That's what where and ago are for."),
            leo("One more. I've seen tables called requests, with a column called timestamp. Same thing?"),
            maya("Same data, older spelling. In the workspace it's AppRequests and TimeGenerated. The Logs page of Application Insights still accepts the classic names, requests and timestamp. Write the classic names in the workspace, and the query fails."),
            leo("Last thing. I don't want to sit staring at charts. Can it tell me?"),
            maya("Yes, with a log search alert rule. It runs your query on a schedule, say every five minutes, and compares the result with a threshold, a line you draw, like ten failures. Cross it, and an action group fires: a list of who to tell and how, like an email."),
            leo("So what do I remember for the exam?"),
            maya("If the question says count per five-minute slice, the answer is summarize by bin. If it's about how slow requests feel, a percentile, not an average. If you need the exception behind a failed request, join on OperationId. If a new column must keep all the others, extend. And to email someone when failures pass a number, a log search alert rule."),
            leo("And anything to try today?"),
            maya("About fifteen minutes, on any workspace with a little data. Open Logs, the query page on Azure's website, and run AppRequests, where TimeGenerated is newer than ago one hour, take ten. Count requests by ResultCode with summarize. Chart the p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule, with a threshold."),
            leo("My logs, finally answering back. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-kql",
        listOf(
            maya("Welcome to Case Files. Today's case: The Eight O'Clock Slowdown. PlantPal is a small Android app that tells you what's wrong with your houseplant. Behind it runs a Python program on Azure, Microsoft's cloud, that answers each photo. Every evening at eight, users say it crawls. And the team's chart says everything is fine."),
            leo("Detective Leo, on the case. A chart that says fine, and users that say slow. Somebody's lying."),
            maya("Nobody's lying. They're asking different questions. Everything the PlantPal backend reports about itself goes to Application Insights, Azure's service for collecting that. And it's stored in a Log Analytics workspace. Picture a giant spreadsheet workbook: each sheet a table, each row one event, each column one fact about it."),
            leo("Under the picture, what is a workspace, really?"),
            maya("A store of tables that Azure keeps for you, where each row is one thing that happened, with a time on it. Incoming calls land in a table called AppRequests: one row per photo a user sent, with columns like DurationMs, how many milliseconds it took."),
            leo("And how do you question the workbook?"),
            maya("With KQL, the Kusto Query Language. A query is a question written in it. It reads like an assembly line: start with a table, and pass the rows through steps. Each step takes the table the last step made and hands a new table on. KQL only ever reads; it can't change a single row."),
            leo("My first suspect, then: the team's chart. Show me the query."),
            maya("It started from AppRequests, used summarize, the step that groups rows and works something out for each group, and worked out the average of DurationMs, per hour. The average said under a second, all evening."),
            leo("So the average is innocent. Case closed: users exaggerate."),
            maya("[pause] Wrong suspect. An average hides the slow tail. A few very slow requests vanish among thousands of quick ones. Clue one: use a percentile. The 95th percentile, p95, is the time 95 of every 100 requests beat: what the slowest one in twenty felt like."),
            leo("And with p95?"),
            maya("The line jumped from under a second to six seconds, every evening at eight. The users were right. To draw it per hour, they used bin, which rounds each time down to the start of its bucket, like sorting letters into hourly pigeonholes."),
            leo("Then my second suspect: the query that timed out yesterday. I saw it spin for minutes."),
            maya("Good catch, wrong crime. That one had no time filter. Without a where on TimeGenerated, the column holding each row's time, it trawls through weeks of data. Where keeps only rows that match, like a sieve. Keep TimeGenerated later than ago seven days, and it's quick. Ago counts back from now."),
            leo("And the query that failed outright? Table not found?"),
            maya("Clue two. The intern wrote requests, with a column called timestamp. Those are the classic names, which the Logs page of Application Insights still accepts. In the workspace, it's AppRequests and TimeGenerated. Same data, two spellings."),
            leo("Fine. We know when. Now, why eight o'clock?"),
            maya("For why, you join: match rows from two tables that share a value. The slow requests went into a join with AppDependencies, the table of calls the backend makes out, to its database for example."),
            leo("Join them on ResultCode. Slow requests, same code."),
            maya("[sigh] That's how a query eats an afternoon. ResultCode is the number a server sends back, like 200 for fine. Thousands of unrelated rows share a 200, so everything matches everything. Join on OperationId: the trace id every row of one request carries, its tracking number."),
            leo("And the joined result?"),
            maya("Almost all of the six seconds was one call to the database. Under-provisioned, too small for the evening rush. Not the Python code at all. The OpenTelemetry episode explains how OperationId gets onto every row in the first place."),
            leo("Any other witnesses?"),
            maya("One. The backend's own printed output. The program runs in a container, a packaged copy of the app, and what it prints lands in a table called Container App Console Logs, with C L at the end. Its column names end in an underscore and a letter, so it's ContainerAppName underscore s. Leave that off, and the query finds nothing."),
            leo("So how do they shape a result for the report?"),
            maya("Project picks the columns to keep. Extend adds a calculated one and keeps the rest, like a column called slow, true when DurationMs is over two thousand. Order by DurationMs, descending, then take ten, for the ten worst. And render timechart draws a line over time."),
            leo("And next time? I don't want to wait for users to complain."),
            maya("A log search alert rule. Azure runs your query on a schedule, say every five minutes, over the last five minutes, and compares the result with a threshold, like ten failures. Cross it, and an action group fires: a list of who to tell and how, like an email."),
            leo("Any loose ends in the file?"),
            maya("Where KQL stops working. It only reads, so it reports the problem; fixing the database is up to you. Rows arrive a minute or two after they happen, so a query is never quite live. And an alert checks on its schedule, not the instant something breaks."),
            leo("So, the case summary for the exam?"),
            maya("If the question is about how slow requests feel, the answer is a percentile, not an average. If it says per five-minute slice, summarize by bin. If you need the dependency or exception behind a request, join on OperationId. If a new column must keep the others, extend. And to be emailed when failures pass a number, a log search alert rule."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, without the slowdown. Open Logs, the query page on Azure's website, and run AppRequests, where TimeGenerated is newer than ago one hour, take ten. Count requests by ResultCode with summarize. Chart the p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule, with a threshold."),
            leo("Eight o'clock, solved by ten past. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-kql",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They spent the whole week querying the logs of the Two Tracks Coach, the study assistant in the Android app, with its Python program on Azure, Microsoft's cloud. And let's just say, it was a week."),
            leo("[laugh] I heard someone's query ran for so long they went to lunch. Querying what, exactly?"),
            maya("Everything the Coach reports about itself lands in Application Insights, Azure's service for collecting that. It's kept in a Log Analytics workspace. Picture a giant spreadsheet workbook: each sheet a table, each row one event, each column one fact. Requests the Coach received go in a table called AppRequests."),
            leo("And how do you ask the workbook something?"),
            maya("With KQL, the Kusto Query Language. A query is a question in that language, and it reads like an assembly line: start with a table, then pass the rows through steps. Under the picture, each step takes the table the last one made and hands a new table on. And it only reads. It can't change a thing."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. The lunch query was Tom's. He started from AppRequests and went straight to counting. No time filter."),
            leo("And?"),
            maya("And it trawled through weeks of data. The first step should be a where, which keeps only the rows that match, like a sieve. Where TimeGenerated, the time each row was written, is later than ago one hour. Ago counts back from now. Tom's query now takes two seconds."),
            leo("[sigh] Poor Tom. Tuesday?"),
            maya("Dana. Students said the Coach was slow in the evenings, and Dana proudly showed a chart. Summarize, the step that groups rows and works something out per group, of the average DurationMs, per hour. Flat. Lovely. Everything fine."),
            leo("But students said it was slow."),
            maya("Because an average hides the slow tail. Priya changed one word. The 95th percentile, p95: the time that 95 of every 100 requests beat, so what the slowest one in twenty felt like. Six seconds, every evening at eight. Dana went very quiet."),
            leo("How did they get it per hour?"),
            maya("Bin. It rounds each time down to the start of its bucket, like sorting letters into hourly pigeonholes. Summarize the p95 by bin of TimeGenerated, one hour, then render timechart, which draws a line with time along the bottom."),
            leo("Wednesday. Go."),
            maya("Marcus. Marcus copied a query from an old blog post. Table called requests, column called timestamp. And in the workspace, it just failed."),
            leo("Why?"),
            maya("Those are the classic names. The Logs page of Application Insights still accepts them, but in the workspace it's AppRequests and TimeGenerated. Same data, two spellings. Marcus now has a sticky note on his screen."),
            leo("[chuckle] Thursday?"),
            maya("The intern. Bless them. They wanted to know why the evenings were slow, so they joined the slow requests to AppDependencies, the table of calls the Coach makes out, to its database for example. A join matches rows from two tables that share a value."),
            leo("Sounds right. What did they join on?"),
            maya("ResultCode. The number a server sends back to say how it went, like 200 for fine. Thousands of unrelated rows share a 200, so everything matched everything. The result was enormous and meant nothing."),
            leo("What should they have joined on?"),
            maya("OperationId: the trace id every row of one request carries, its tracking number. Join on that, and each slow request lines up with its own database call. Almost all of the six seconds was the database. The OpenTelemetry episode explains where that id comes from."),
            leo("And the intern's other famous query? I heard take one hour."),
            maya("Oh, that too. Take returns a number of rows, like take ten. It knows nothing about time. For the last hour you need where and ago. And for the ten slowest, order by DurationMs, descending, then take ten."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya saved everyone from ever staring at charts again. She built a log search alert rule. Azure runs her failures query on a schedule, every five minutes, and compares the count with a threshold, ten. Cross it, and an action group fires: a list of who to tell and how. An email to the whole team."),
            leo("Did anything still go wrong?"),
            maya("Only where KQL stops. It reads; it never fixes. The database still needed a bigger size, and that was a different job. Rows arrive a minute or two late, so a query is never quite live. And Priya's alert checks every five minutes, not the instant something breaks."),
            leo("And the extend versus project argument? I heard voices."),
            maya("Dana and Marcus. Project keeps only the columns you list. Extend adds a calculated column, like slow, true when DurationMs is over two thousand, and keeps all the others. Dana was right. Extend."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the question is about how slow requests feel, the answer is a percentile, not Dana's average. If it says per five-minute slice, summarize by bin. If you need the dependency behind a request, join on OperationId, not ResultCode. And to email the team when failures pass ten, a log search alert rule, like Priya's."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Open Logs, the query page on Azure's website, and run AppRequests, where TimeGenerated is newer than ago one hour, take ten. Count requests by ResultCode with summarize. Chart the p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule, with a threshold."),
            leo("A time filter first, Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-kql",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's ask your app's logs a few gentle questions, and see what they confess."),
            ivy("Confessions, at this hour. Where do these logs live?"),
            nova("Take the Two Tracks Coach, the study assistant in the Android app. Behind it, the Coach API, a Python program on Azure, Microsoft's cloud. API just means the part the phone calls. Everything it reports about itself goes to Application Insights, Azure's service for collecting that."),
            ivy("And where does Application Insights keep it all?"),
            nova("In a Log Analytics workspace. Picture a giant spreadsheet workbook, open on the desk. Each sheet is a table. Each row is one event, like one request. Each column is one fact about it, like how long it took. [pause] And every row has a time."),
            ivy("Which sheet do I open first?"),
            nova("AppRequests. The calls the Coach received, one row per question, with columns like DurationMs, how many milliseconds it took, and Success, true or false. Calls the Coach made out, to its database, sit in AppDependencies. And errors in AppExceptions."),
            ivy("And how do I ask the workbook something?"),
            nova("In KQL, the Kusto Query Language. A query is a question written in it. It reads like an assembly line: you start with a table, and pass its rows through steps. Under the picture, each step takes the table the last one made, and hands a new table on. And it only ever reads. It never changes a thing."),
            ivy("Gentle. What's the first step?"),
            nova("A where. Where keeps only the rows that match a rule, like a sieve. And the first rule is almost always time: TimeGenerated, the column holding when each row was written, later than ago one hour."),
            ivy("Ago. Say that one slowly."),
            nova("Ago means a time counted back from now. Ago one hour is one hour before now. [pause] Forget the time filter, and your query wanders through weeks of data, and keeps you up even later."),
            ivy("And after the sieve?"),
            nova("Shape what's left. Project keeps only the columns you name. Extend adds a column you calculate, and keeps all the rest, like a column called slow, true when DurationMs is over two thousand."),
            ivy("And counting? I like counting, at night."),
            nova("Then you'll like summarize. It groups rows and works something out for each group: a count, an average, a percentile. Count requests by ResultCode, the number a server sends back to say how it went, and you get one row per code."),
            ivy("And counting by time? Every five minutes?"),
            nova("That's bin. Bin rounds each time down to the start of its bucket, like sorting letters into five-minute pigeonholes. Summarize by bin of TimeGenerated, five minutes. Then render timechart, which draws the result as a line, with time along the bottom."),
            ivy("Now. The Coach feels slow in the evenings. Do I look at the average?"),
            nova("[pause] No. An average hides the slow tail. Use a percentile. The 95th percentile, p95, is the time 95 of every 100 requests beat: what the slowest one in twenty felt like. On the Coach, p95 jumped from under a second to six seconds, every evening at eight."),
            ivy("But why eight o'clock?"),
            nova("For why, you join. Join matches rows from two tables that share a value. The value to use is OperationId, the trace id every row of one request carries, like a tracking number. Join the slow requests to AppDependencies on it, and the six seconds were all in the database."),
            ivy("Could I join on something easier? ResultCode?"),
            nova("Never. Thousands of strangers share a 200. Everything would match everything. OperationId, always. The OpenTelemetry episode tells you how that id gets onto every row."),
            ivy("And if I've seen requests and timestamp, written in old notes?"),
            nova("The classic names. The Logs page of Application Insights still accepts them. But in the workspace, it's AppRequests and TimeGenerated. Same data, two spellings, and only one works there."),
            ivy("And can the logs whisper to me, so I don't have to keep watching?"),
            nova("A log search alert rule. Azure runs your query on a schedule, every five minutes say, and compares the result with a threshold, like ten failures. Cross it, and an action group fires: a list of who to tell, and how. An email, in the middle of the night."),
            ivy("Now the part I've been waiting for. Where does all this stop working?"),
            nova("Here. [pause] KQL only reads. It tells you the database is slow; it can't make it faster. Rows arrive a minute or two after they happen, so it's never quite live. And an alert checks on its schedule, not the instant something breaks."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question is about how slow requests feel, a percentile, not an average. If it says per five-minute slice, summarize by bin. If you need the dependency or the exception behind a request, join on OperationId. If a new column must keep the others, extend. And to be emailed when failures pass a number, a log search alert rule."),
            ivy("And something to try today, before I sleep?"),
            nova("Open Logs, the query page on Azure's website, and run AppRequests, where TimeGenerated is newer than ago one hour, take ten. Count requests by ResultCode with summarize. [pause] Chart the p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule, with a threshold."),
            ivy("Ask gently, and the logs will tell. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-kql",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about the most curious thing in the cloud."),
            rafe("Let me guess. Something that asks a lot of questions."),
            nova("[laugh] KQL, the Kusto Query Language. It's how you ask your app's logs questions. When was it slow? What failed, and why? A query is one of those questions, written down."),
            rafe("You do ask a lot of questions. Where are these logs kept?"),
            nova("Picture the Two Tracks Coach, the study assistant in the Android app, with its Python program on Azure, Microsoft's cloud. Everything it reports about itself goes to Application Insights, Azure's service for collecting that, and lands in a Log Analytics workspace."),
            rafe("Workspace. Under the picture, what is it?"),
            nova("A giant spreadsheet workbook Azure keeps for you. Each sheet a table, each row one event with a time on it, each column one fact. The requests the Coach received sit in a table called AppRequests, with DurationMs, how many milliseconds each took."),
            rafe("And the calls the Coach makes out, to its database? Where do those go?"),
            nova("AppDependencies. Errors go in AppExceptions, and the lines the code printed go in AppTraces. Four sheets, four kinds of confession. You'll want to read them all."),
            rafe("And KQL reads like what, a love letter?"),
            nova("Like an assembly line. Start with a table, then pass its rows through steps. Each step takes the table the last one made, and hands a new one on. And it only ever reads; it never changes a row. Very respectful."),
            rafe("Respectful. Quiz time, darling, exam style. You want only the last hour of requests. What do you write?"),
            nova("Start from AppRequests, then a where: keep rows where TimeGenerated, the time each row was written, is later than ago one hour. Ago counts back from now. Where is the sieve."),
            rafe("Right answer. You always know where to start. [kiss]"),
            nova("Mm. My turn. You want a count of requests for every five-minute slice. Which part does the slicing?"),
            rafe("Take five. Five of everything."),
            nova("[sigh] No kiss for that one. Take just returns that many rows; it knows nothing about time. The slicing is bin: it rounds each time down to the start of its bucket, like five-minute pigeonholes. And summarize does the counting: it groups rows and works something out for each group."),
            rafe("Summarize by bin. I'll remember. Some things should come in five-minute slices."),
            nova("And render timechart draws it, a line with time along the bottom. Now, the Coach is slow in the evenings. Would you chart the average?"),
            rafe("No. An average hides the slow tail. I'd use a percentile: the 95th, p95, the time 95 of every 100 requests beat. What the slowest one in twenty felt like."),
            nova("Right answer. You never settle for average. [kiss]"),
            rafe("And p95 said?"),
            nova("Under a second all day, then six seconds every evening at eight. So, Rafe. You need the database call behind each slow request. Which column do you join on?"),
            rafe("OperationId. The trace id every row of one request carries, its tracking number. A join matches rows from two tables that share a value, and that's the one that belongs together. Never ResultCode; thousands of strangers share a 200."),
            nova("Right answer. Only the ones that truly belong together. [kiss]"),
            rafe("The OpenTelemetry episode explains how that id gets on every row, by the way. My question. You want a column called slow, true when DurationMs is over two thousand, and you must keep every other column. Which step?"),
            nova("Extend. It adds a calculated column and keeps all the rest. Project would keep only the columns I name, and throw the others away."),
            rafe("Right answer. Nobody gets thrown away tonight. [kiss]"),
            nova("Quick one about names. Old notes say requests and timestamp. In the workspace?"),
            rafe("AppRequests and TimeGenerated. The classic names only work on the Logs page of Application Insights. Same data, two spellings."),
            nova("Lovely. Last question from me. The team wants an email when more than ten requests fail in five minutes. What do you create?"),
            rafe("A log search alert rule. Azure runs the failures query on a schedule, every five minutes, compares it with a threshold of ten, and if it's crossed, an action group fires: who to tell, and how."),
            nova("Right answer. I do love a man who calls when something's wrong. [kiss]"),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("KQL only reads. It tells you the database is slow; fixing it is another job. Rows arrive a minute or two late, so it's never quite live. And an alert checks on its schedule, not the instant something breaks."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("If the question is about how slow requests feel, a percentile, not an average. Per five-minute slice, summarize by bin. The exception or dependency behind a request, join on OperationId. A new column that keeps the others, extend. And an email when failures pass a number, a log search alert rule."),
            rafe("Five for five. That deserves a long one. [kiss]"),
            nova("Mm. Now, something to try today, darling."),
            rafe("Tell me, and I'll try it today."),
            nova("Open Logs, the query page on Azure's website, and run AppRequests, where TimeGenerated is newer than ago one hour, take ten. Count requests by ResultCode with summarize. Chart the p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule, with a threshold."),
            rafe("And I'll keep asking you questions. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun store(api: String) = DiagramSpec(
        "The API's telemetry goes to Application Insights, which stores it as tables in a Log Analytics workspace.",
        nodes = listOf(
            DNode("api", api, 0.24f, 0.11f, w = 0.4f, h = 0.18f, tone = 1, sub = "Python on Azure"),
            DNode("appi", "Application Insights", 0.76f, 0.11f, w = 0.42f, h = 0.18f),
            DNode("ws", "Log Analytics workspace", 0.5f, 0.42f, w = 0.7f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
            DNode("tReq", "AppRequests", 0.26f, 0.71f, w = 0.44f, h = 0.15f, kind = NodeKind.PILL, sub = "calls received"),
            DNode("tDep", "AppDependencies", 0.76f, 0.71f, w = 0.44f, h = 0.15f, kind = NodeKind.PILL, sub = "calls made out"),
            DNode("tExc", "AppExceptions", 0.26f, 0.9f, w = 0.44f, h = 0.15f, kind = NodeKind.PILL, sub = "errors"),
            DNode("tTr", "AppTraces", 0.76f, 0.9f, w = 0.44f, h = 0.15f, kind = NodeKind.PILL, sub = "printed lines"),
        ),
        edges = listOf(
            DEdge("api", "appi", "telemetry"),
            DEdge("appi", "ws", "stores in"),
            DEdge("ws", "tReq"),
            DEdge("ws", "tDep"),
        ),
        aspect = 0.8f,
    )

    private fun pipeline() = DiagramSpec(
        "A KQL query starts with a table and passes its rows through steps, each one handing a new table to the next.",
        nodes = listOf(
            DNode("t", "AppRequests", 0.25f, 0.1f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, tone = 1, sub = "start: a table"),
            DNode("w", "where", 0.25f, 0.33f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, sub = "keep matching rows"),
            DNode("p", "project, extend", 0.25f, 0.56f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, sub = "shape the columns"),
            DNode("s", "summarize", 0.25f, 0.79f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, sub = "group, count, by bin"),
            DNode("pipe", "Each | hands the rows on", 0.75f, 0.1f, w = 0.46f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
            DNode("ago", "ago(1h)", 0.75f, 0.33f, w = 0.46f, h = 0.16f, kind = NodeKind.NOTE, sub = "an hour before now"),
            DNode("take", "order by, take", 0.75f, 0.56f, w = 0.46f, h = 0.16f, kind = NodeKind.PILL, sub = "sort, then limit rows"),
            DNode("r", "render", 0.75f, 0.79f, w = 0.46f, h = 0.16f, kind = NodeKind.PILL, tone = 1, sub = "timechart"),
        ),
        edges = listOf(
            DEdge("t", "w"),
            DEdge("w", "p"),
            DEdge("p", "s"),
            DEdge("s", "r"),
            DEdge("w", "ago", dashed = true),
            DEdge("p", "take", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun percentile() = DiagramSpec(
        "Per hour, the average looked fine while the 95th percentile showed six seconds every evening at eight.",
        nodes = listOf(
            DNode("avg", "Average", 0.26f, 0.18f, w = 0.44f, h = 0.22f, tone = 2, sub = "under 1 s: looks fine"),
            DNode("p95", "p95", 0.76f, 0.18f, w = 0.42f, h = 0.22f, tone = 1, sub = "6 s at eight"),
            DNode("bin", "bin(TimeGenerated, 1h)", 0.5f, 0.54f, w = 0.8f, h = 0.2f, kind = NodeKind.PILL, sub = "hourly buckets"),
            DNode("chart", "render timechart", 0.5f, 0.85f, w = 0.6f, h = 0.18f, sub = "a line over time"),
        ),
        edges = listOf(
            DEdge("avg", "bin"),
            DEdge("p95", "bin"),
            DEdge("bin", "chart"),
        ),
        aspect = 0.8f,
    )

    private fun join() = DiagramSpec(
        "Join the slow requests to their dependencies on OperationId, the trace id they share, never on ResultCode.",
        nodes = listOf(
            DNode("req", "AppRequests", 0.24f, 0.18f, w = 0.42f, h = 0.22f, tone = 1, sub = "slow requests"),
            DNode("dep", "AppDependencies", 0.76f, 0.18f, w = 0.42f, h = 0.22f, sub = "database calls"),
            DNode("op", "OperationId", 0.5f, 0.55f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "the shared trace id"),
            DNode("rc", "Not ResultCode", 0.24f, 0.86f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, sub = "everything matches"),
            DNode("ans", "Database: 6 s", 0.76f, 0.86f, w = 0.42f, h = 0.18f, sub = "the real cause"),
        ),
        edges = listOf(
            DEdge("req", "op"),
            DEdge("dep", "op"),
            DEdge("op", "ans"),
            DEdge("rc", "op", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun names() = DiagramSpec(
        "The same data has two spellings: classic names on the Logs page of Application Insights, App names in the workspace.",
        nodes = listOf(
            DNode("classic", "requests, timestamp", 0.26f, 0.28f, w = 0.46f, h = 0.24f, tone = 2, sub = "classic names"),
            DNode("ws", "AppRequests, TimeGenerated", 0.76f, 0.28f, w = 0.44f, h = 0.24f, tone = 1, sub = "workspace names"),
            DNode("logs", "App Insights Logs", 0.26f, 0.76f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "accepts both"),
            DNode("wsq", "Workspace query", 0.76f, 0.76f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "App names only"),
        ),
        edges = listOf(
            DEdge("classic", "logs"),
            DEdge("ws", "wsq"),
        ),
        aspect = 0.8f,
    )

    private fun alert() = DiagramSpec(
        "A log search alert rule runs a query on a schedule and fires an action group when the result crosses a threshold.",
        nodes = listOf(
            DNode("q", "Failures query", 0.24f, 0.17f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "Success == false"),
            DNode("sched", "Every 5 minutes", 0.76f, 0.17f, w = 0.42f, h = 0.2f, sub = "on a schedule"),
            DNode("thr", "Threshold: 10", 0.5f, 0.5f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "crossed?"),
            DNode("ag", "Action group", 0.5f, 0.83f, w = 0.5f, h = 0.2f, tone = 1, sub = "email the team"),
        ),
        edges = listOf(
            DEdge("q", "thr"),
            DEdge("sched", "thr"),
            DEdge("thr", "ag", "fires"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where KQL stops: it only reads, rows arrive a little late, and an alert checks on its schedule, not instantly.",
        nodes = listOf(
            DNode("reads", "Only reads", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "can't fix the database"),
            DNode("late", "Rows arrive late", 0.76f, 0.2f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "a minute or two"),
            DNode("sched", "Alerts on a schedule", 0.26f, 0.76f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "not the instant"),
            DNode("otel", "OpenTelemetry episode", 0.76f, 0.76f, w = 0.44f, h = 0.22f, tone = 1, sub = "where the ids come from"),
        ),
        edges = listOf(DEdge("late", "otel", dashed = true)),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's KQL questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("slice", "Per five-minute slice", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "summarize by bin"),
            DNode("slow", "How slow it feels", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "percentile, not average"),
            DNode("behind", "The cause behind it", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "join on OperationId"),
            DNode("keep", "Keep the other columns", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "extend"),
            DNode("email", "Email on failures", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "log search alert rule"),
            DNode("hour", "Only the last hour", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "where with ago(1h)"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("slice", "slow", "behind", "keep", "email", "hour")
    private val lastHour = BoardCard("KQL", "AppRequests | where TimeGenerated > ago(1h) | take 10", code = true)
    private val byCode = BoardCard("KQL", "AppRequests | summarize count() by ResultCode", code = true)
    private val p95Code = BoardCard("KQL", "summarize percentile(DurationMs, 95) by bin(TimeGenerated, 1h) | render timechart", code = true)
    private val joinCode = BoardCard("KQL", "| join kind=inner (AppDependencies) on OperationId", code = true)
    private val extendCode = BoardCard("KQL", "| extend slow = DurationMs > 2000", code = true)
    private val alertCode = BoardCard("KQL", "| where Success == false | summarize failures = count() by bin(TimeGenerated, 5m)", code = true)
    private val today = BoardCard("Today", "Last hour, count by ResultCode, chart p95, then an alert rule.")

    private fun classicBoard() = PodcastBoard(
        "ai-kql",
        listOf(
            // 0: "Hi, and welcome back. Today is about asking questions of your app's logs..."
            Scene(
                0, "Where the logs live", store("Coach API"),
                beats = listOf(
                    Beat(0, card = BoardCard("Asking your logs", "When was it slow? What failed, and why? It's all written down.")),
                    Beat(2, show = setOf("api", "appi"), light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("Application Insights", "Collects everything the Coach API reports about itself.")),
                    Beat(4, show = setOf("ws"), light = setOf("ws"), pulse = "appi" to "ws", card = BoardCard("Log Analytics workspace", "A workbook of tables: a row per event, a column per fact.")),
                    Beat(6, show = setOf("tReq"), light = setOf("tReq"), pulse = "ws" to "tReq", card = BoardCard("AppRequests", "Calls received: Name, DurationMs, Success, ResultCode.")),
                    Beat(8, show = setOf("tDep", "tExc", "tTr"), light = setOf("tDep", "tExc", "tTr"), card = BoardCard("The other tables", "Calls made out, errors, and the lines your code printed.")),
                ),
            ),
            // 9: "Okay, I have a workbook. How do I ask it something?"
            Scene(
                9, "An assembly line", pipeline(),
                beats = listOf(
                    Beat(10, show = setOf("t"), light = setOf("t"), card = BoardCard("KQL", "Kusto Query Language: start with a table, then steps.")),
                    Beat(12, show = setOf("pipe"), light = setOf("pipe"), card = BoardCard("The pipe", "Each step takes the last step's table and hands a new one on.")),
                    Beat(14, show = setOf("w"), light = setOf("w"), pulse = "t" to "w", card = BoardCard("where", "Keeps only the rows that match. Filter on TimeGenerated first.")),
                    Beat(16, show = setOf("ago"), light = setOf("ago"), pulse = "w" to "ago", card = lastHour),
                    Beat(18, show = setOf("p"), light = setOf("p"), pulse = "w" to "p", card = BoardCard("project", "Keeps only the columns you name, and drops the rest.")),
                    Beat(20, light = setOf("p"), card = extendCode),
                ),
            ),
            // 21: "Now the counting."
            Scene(
                21, "Counting and charting", pipeline(),
                beats = listOf(
                    Beat(22, show = setOf("s"), light = setOf("s"), pulse = "p" to "s", card = byCode),
                    Beat(24, light = setOf("s"), card = BoardCard("bin", "Rounds each time down to its bucket: five-minute pigeonholes.")),
                    Beat(26, show = setOf("r"), light = setOf("r"), pulse = "s" to "r", card = BoardCard("render timechart", "A line chart with time along the bottom.")),
                ),
            ),
            // 27: "Okay. The Coach is slow in the evenings. Do I chart the average time?"
            Scene(
                27, "Percentiles, not averages", percentile(),
                beats = listOf(
                    Beat(28, show = setOf("avg", "p95"), light = setOf("p95"), card = BoardCard("p95", "The time 95 of every 100 requests beat: the slowest one in twenty.")),
                    Beat(30, show = setOf("bin", "chart"), light = setOf("p95", "chart"), pulse = "bin" to "chart", card = p95Code),
                ),
            ),
            // 31: "But the line doesn't say why."
            Scene(
                31, "Why eight o'clock?", join(),
                beats = listOf(
                    Beat(32, show = setOf("req", "dep", "op", "ans"), light = setOf("op", "ans"), pulse = "op" to "ans", card = joinCode),
                    Beat(34, show = setOf("rc"), light = setOf("rc"), pulse = "rc" to "op", card = BoardCard("Not ResultCode", "Thousands share a 500, so everything matches everything.")),
                    Beat(36, light = setOf("req"), card = BoardCard("order by, then take", "Newest first, then ten rows. take knows nothing about time.")),
                ),
            ),
            // 37: "One more. I've seen tables called requests..."
            Scene(
                37, "Two spellings", names(),
                beats = listOf(
                    Beat(38, light = setOf("classic", "ws"), pulse = "ws" to "wsq", card = BoardCard("Same data, older spelling", "In the workspace: AppRequests and TimeGenerated.")),
                ),
            ),
            // 39: "Last thing. I don't want to sit staring at charts..."
            Scene(
                39, "Telling you next time", alert(),
                beats = listOf(
                    Beat(40, light = setOf("thr", "ag"), pulse = "thr" to "ag", card = BoardCard("Log search alert rule", "A query on a schedule, a threshold, and an action group to tell.")),
                ),
            ),
            // 41: "So what do I remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("slice", "slow", "behind", "keep", "email"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-kql--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Eight O'Clock Slowdown..."
            Scene(
                0, "The Eight O'Clock Slowdown", store("PlantPal API"),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("The case", "Users say it crawls at eight. The team's chart says fine.")),
                    Beat(2, show = setOf("appi", "ws"), light = setOf("ws"), pulse = "appi" to "ws", card = BoardCard("Log Analytics workspace", "A workbook of tables: a row per event, a column per fact.")),
                    Beat(4, show = setOf("tReq"), light = setOf("tReq"), pulse = "ws" to "tReq", card = BoardCard("Under the picture", "A store of tables; each row one thing that happened, with a time.")),
                ),
            ),
            // 5: "And how do you question the workbook?"
            Scene(
                5, "The assembly line", pipeline(),
                beats = listOf(
                    Beat(6, show = setOf("t", "pipe"), light = setOf("pipe"), card = BoardCard("KQL", "Start with a table, then steps. It only reads; it changes nothing.")),
                ),
            ),
            // 7: "My first suspect, then: the team's chart..."
            Scene(
                7, "Suspect one: the average", percentile(),
                beats = listOf(
                    Beat(8, show = setOf("avg"), light = setOf("avg"), card = BoardCard("The team's chart", "summarize the average DurationMs per hour: under a second.")),
                    Beat(10, show = setOf("p95"), light = setOf("p95"), card = BoardCard("Clue one: a percentile", "p95: the time 95 of every 100 requests beat.")),
                    Beat(12, show = setOf("bin", "chart"), light = setOf("p95", "bin"), pulse = "p95" to "bin", card = p95Code),
                ),
            ),
            // 13: "Then my second suspect: the query that timed out yesterday..."
            Scene(
                13, "The query that spun", pipeline(),
                beats = listOf(
                    Beat(14, show = setOf("w", "ago"), light = setOf("w", "ago"), pulse = "w" to "ago", card = lastHour),
                ),
            ),
            // 15: "And the query that failed outright? Table not found?"
            Scene(
                15, "Clue two: two spellings", names(),
                beats = listOf(
                    Beat(16, light = setOf("classic", "ws"), pulse = "classic" to "logs", card = BoardCard("Classic names", "requests and timestamp work on the Logs page, not in the workspace.")),
                ),
            ),
            // 17: "Fine. We know when. Now, why eight o'clock?"
            Scene(
                17, "Why eight o'clock?", join(),
                beats = listOf(
                    Beat(18, show = setOf("req", "dep"), light = setOf("req", "dep"), card = BoardCard("join", "Match rows from two tables that share a value.")),
                    Beat(20, show = setOf("op", "rc"), light = setOf("rc", "op"), pulse = "rc" to "op", card = BoardCard("Not ResultCode", "Thousands share a 200. Join on OperationId, the trace id.")),
                    Beat(22, show = setOf("ans"), light = setOf("op", "ans"), pulse = "op" to "ans", card = joinCode),
                ),
            ),
            // 23: "Any other witnesses?"
            Scene(
                23, "Another witness", store("PlantPal API"),
                beats = listOf(
                    Beat(24, light = setOf("api", "ws"), pulse = "appi" to "ws", card = BoardCard("KQL", "ContainerAppConsoleLogs_CL | where ContainerAppName_s == \"plantpal\"", code = true)),
                ),
            ),
            // 25: "So how do they shape a result for the report?"
            Scene(
                25, "Shaping the report", pipeline(),
                beats = listOf(
                    Beat(26, light = setOf("p", "take", "r"), pulse = "p" to "take", card = extendCode),
                ),
            ),
            // 27: "And next time? I don't want to wait for users to complain."
            Scene(
                27, "Next time", alert(),
                beats = listOf(
                    Beat(28, light = setOf("thr", "ag"), pulse = "thr" to "ag", card = alertCode),
                ),
            ),
            // 29: "Any loose ends in the file?"
            Scene(
                29, "Loose ends", limits(),
                beats = listOf(
                    Beat(30, light = setOf("reads", "late", "sched"), pulse = "late" to "otel", card = BoardCard("Where KQL stops", "It reads, never fixes; rows land late; alerts check on a schedule.")),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("slow", "slice", "behind", "keep", "email"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer.")),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-kql--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "Query week", store("Coach API"),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Query week", "The Coach team spent the week asking the logs questions.")),
                    Beat(2, show = setOf("appi", "ws", "tReq"), light = setOf("ws", "tReq"), pulse = "appi" to "ws", card = BoardCard("Log Analytics workspace", "A workbook of tables. Requests go in AppRequests.")),
                ),
            ),
            // 3: "And how do you ask the workbook something?"
            Scene(
                3, "An assembly line", pipeline(),
                beats = listOf(
                    Beat(4, show = setOf("t", "pipe"), light = setOf("pipe"), card = BoardCard("KQL", "Start with a table, then steps. It only reads.")),
                ),
            ),
            // 5: "Okay. Who went first?"
            Scene(
                5, "Monday: Tom", pipeline(),
                beats = listOf(
                    Beat(6, light = setOf("t"), card = BoardCard("No time filter", "Tom's query trawled through weeks of data. Lunch.")),
                    Beat(8, show = setOf("w", "ago"), light = setOf("w", "ago"), pulse = "w" to "ago", card = lastHour),
                ),
            ),
            // 9: "Poor Tom. Tuesday?"
            Scene(
                9, "Tuesday: Dana", percentile(),
                beats = listOf(
                    Beat(10, show = setOf("avg"), light = setOf("avg"), card = BoardCard("Dana's chart", "The average DurationMs per hour: flat and lovely.")),
                    Beat(12, show = setOf("p95"), light = setOf("p95"), card = BoardCard("Priya's one word", "p95: six seconds, every evening at eight.")),
                    Beat(14, show = setOf("bin", "chart"), light = setOf("bin", "chart"), pulse = "bin" to "chart", card = p95Code),
                ),
            ),
            // 15: "Wednesday. Go."
            Scene(
                15, "Wednesday: Marcus", names(),
                beats = listOf(
                    Beat(16, light = setOf("classic"), card = BoardCard("An old blog post", "requests and timestamp, pasted into a workspace query.")),
                    Beat(18, light = setOf("ws", "wsq"), pulse = "ws" to "wsq", card = BoardCard("Two spellings", "In the workspace: AppRequests and TimeGenerated.")),
                ),
            ),
            // 19: "Thursday?"
            Scene(
                19, "Thursday: the intern", join(),
                beats = listOf(
                    Beat(20, show = setOf("req", "dep"), light = setOf("req", "dep"), card = BoardCard("join", "Match rows from two tables that share a value.")),
                    Beat(22, show = setOf("op", "rc"), light = setOf("rc"), pulse = "rc" to "op", card = BoardCard("Joined on ResultCode", "Thousands share a 200: an enormous result that meant nothing.")),
                    Beat(24, show = setOf("ans"), light = setOf("op", "ans"), pulse = "op" to "ans", card = joinCode),
                    Beat(26, card = BoardCard("take one hour?", "take returns rows; it knows nothing about time. Use where and ago.")),
                ),
            ),
            // 27: "And Friday? There's always a Friday."
            Scene(
                27, "Friday: Priya", alert(),
                beats = listOf(
                    Beat(28, light = setOf("thr", "ag"), pulse = "thr" to "ag", card = alertCode),
                ),
            ),
            // 29: "Did anything still go wrong?"
            Scene(
                29, "Where it stops", limits(),
                beats = listOf(
                    Beat(30, light = setOf("reads", "late", "sched"), pulse = "late" to "otel", card = BoardCard("Where KQL stops", "It reads, never fixes; rows land late; alerts check on a schedule.")),
                ),
            ),
            // 31: "And the extend versus project argument? I heard voices."
            Scene(
                31, "Extend or project", pipeline(),
                beats = listOf(
                    Beat(32, light = setOf("p"), card = extendCode),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("slow", "slice", "behind", "email"), card = BoardCard("Listen for the phrase", "Dana, the intern and Priya: one answer each.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-kql--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Gentle questions", store("Coach API"),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Asking your app's logs a few gentle questions.")),
                    Beat(2, show = setOf("api", "appi"), light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("Application Insights", "Collects everything the Coach API reports about itself.")),
                    Beat(4, show = setOf("ws"), light = setOf("ws"), pulse = "appi" to "ws", card = BoardCard("Log Analytics workspace", "A workbook of tables: a row per event, a column per fact.")),
                    Beat(6, show = setOf("tReq", "tDep", "tExc", "tTr"), light = setOf("tReq"), pulse = "ws" to "tReq", card = BoardCard("AppRequests", "Calls received, with DurationMs and Success.")),
                ),
            ),
            // 7: "And how do I ask the workbook something?"
            Scene(
                7, "An assembly line", pipeline(),
                beats = listOf(
                    Beat(8, show = setOf("t", "pipe"), light = setOf("pipe"), card = BoardCard("KQL", "A table, then steps; each hands a new table on. It only reads.")),
                    Beat(10, show = setOf("w"), light = setOf("w"), pulse = "t" to "w", card = BoardCard("where", "A sieve: keep only rows that match. Time first.")),
                    Beat(12, show = setOf("ago"), light = setOf("ago"), pulse = "w" to "ago", card = lastHour),
                    Beat(14, show = setOf("p"), light = setOf("p"), pulse = "w" to "p", card = extendCode),
                ),
            ),
            // 15: "And counting? I like counting, at night."
            Scene(
                15, "Counting at night", pipeline(),
                beats = listOf(
                    Beat(16, show = setOf("s"), light = setOf("s"), pulse = "p" to "s", card = byCode),
                    Beat(18, show = setOf("r"), light = setOf("r"), pulse = "s" to "r", card = BoardCard("bin and render", "Five-minute pigeonholes, drawn as a line over time.")),
                ),
            ),
            // 19: "Now. The Coach feels slow in the evenings..."
            Scene(
                19, "Not the average", percentile(),
                beats = listOf(
                    Beat(20, show = setOf("avg", "p95", "bin", "chart"), light = setOf("p95"), pulse = "bin" to "chart", card = p95Code),
                ),
            ),
            // 21: "But why eight o'clock?"
            Scene(
                21, "Why eight o'clock?", join(),
                beats = listOf(
                    Beat(22, show = setOf("req", "dep", "op", "ans"), light = setOf("op", "ans"), pulse = "op" to "ans", card = joinCode),
                    Beat(24, show = setOf("rc"), light = setOf("rc"), pulse = "rc" to "op", card = BoardCard("Never ResultCode", "Thousands of strangers share a 200.")),
                ),
            ),
            // 25: "And if I've seen requests and timestamp, written in old notes?"
            Scene(
                25, "Two spellings", names(),
                beats = listOf(
                    Beat(26, light = setOf("classic", "ws"), pulse = "ws" to "wsq", card = BoardCard("Same data, two spellings", "In the workspace: AppRequests and TimeGenerated.")),
                ),
            ),
            // 27: "And can the logs whisper to me, so I don't have to keep watching?"
            Scene(
                27, "A whisper in the night", alert(),
                beats = listOf(
                    Beat(28, light = setOf("thr", "ag"), pulse = "thr" to "ag", card = BoardCard("Log search alert rule", "A query on a schedule, a threshold, and an action group.")),
                ),
            ),
            // 29: "Now the part I've been waiting for. Where does all this stop working?"
            Scene(
                29, "Where it stops", limits(),
                beats = listOf(
                    Beat(30, light = setOf("reads", "late", "sched"), pulse = "late" to "otel", card = BoardCard("Where KQL stops", "It reads, never fixes; rows land late; alerts check on a schedule.")),
                ),
            ),
            // 31: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("slow", "slice", "behind", "keep", "email"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer.")),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-kql--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", store("Coach API"),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "The most curious thing in the cloud.")),
                    Beat(2, card = BoardCard("KQL", "Kusto Query Language: how you ask your app's logs questions.")),
                    Beat(4, show = setOf("api", "appi", "ws"), light = setOf("ws"), pulse = "appi" to "ws", card = BoardCard("Log Analytics workspace", "Where Application Insights keeps what the Coach reports.")),
                    Beat(6, show = setOf("tReq"), light = setOf("tReq"), pulse = "ws" to "tReq", card = BoardCard("AppRequests", "Requests received, with DurationMs in milliseconds.")),
                    Beat(8, show = setOf("tDep", "tExc", "tTr"), light = setOf("tDep", "tExc", "tTr"), card = BoardCard("Four sheets", "Calls made out, errors, and the lines the code printed.")),
                ),
            ),
            // 9: "And KQL reads like what, a love letter?"
            Scene(
                9, "An assembly line", pipeline(),
                beats = listOf(
                    Beat(10, show = setOf("t", "pipe"), light = setOf("pipe"), card = BoardCard("Like an assembly line", "Each step hands a new table on. It only ever reads.")),
                    Beat(11, light = setOf("t"), card = BoardCard("Quiz: the last hour", "Only the last hour of requests. What do you write?")),
                    Beat(12, show = setOf("w", "ago"), light = setOf("w", "ago"), pulse = "w" to "ago", card = lastHour),
                    Beat(14, show = setOf("s"), light = setOf("s"), card = BoardCard("Quiz: five-minute slices", "A count of requests for every five minutes. What slices it?")),
                    Beat(16, light = setOf("s"), card = BoardCard("No kiss: it's bin", "take only returns rows. bin makes buckets; summarize counts.")),
                    Beat(18, show = setOf("r"), light = setOf("r"), pulse = "s" to "r", card = BoardCard("render timechart", "A line with time along the bottom.")),
                ),
            ),
            // 19: "No. An average hides the slow tail..."
            Scene(
                19, "Never average", percentile(),
                beats = listOf(
                    Beat(19, show = setOf("avg", "p95"), light = setOf("p95"), card = BoardCard("Right answer: a percentile", "p95: what the slowest one in twenty felt like.")),
                    Beat(21, show = setOf("bin", "chart"), light = setOf("p95", "chart"), pulse = "bin" to "chart", card = p95Code),
                ),
            ),
            // 22: "Under a second all day, then six seconds every evening at eight..."
            Scene(
                22, "Truly belong together", join(),
                beats = listOf(
                    Beat(22, show = setOf("req", "dep"), light = setOf("req", "dep"), card = BoardCard("Quiz: which column?", "The database call behind each slow request. Join on what?")),
                    Beat(23, show = setOf("op", "rc"), light = setOf("op"), pulse = "req" to "op", card = BoardCard("Right answer: OperationId", "The shared trace id. Never ResultCode.")),
                    Beat(24, show = setOf("ans"), light = setOf("ans"), pulse = "op" to "ans", card = joinCode),
                ),
            ),
            // 25: "The OpenTelemetry episode explains how that id gets on every row..."
            Scene(
                25, "Nobody thrown away", pipeline(),
                beats = listOf(
                    Beat(25, light = setOf("p"), card = BoardCard("Quiz: keep every column", "Add a column called slow, and keep all the others.")),
                    Beat(26, light = setOf("p"), card = extendCode),
                ),
            ),
            // 28: "Quick one about names..."
            Scene(
                28, "Two spellings", names(),
                beats = listOf(
                    Beat(28, light = setOf("classic"), card = BoardCard("Old notes", "requests and timestamp. In the workspace?")),
                    Beat(29, light = setOf("ws", "wsq"), pulse = "ws" to "wsq", card = BoardCard("AppRequests, TimeGenerated", "Classic names only work on the Logs page of App Insights.")),
                ),
            ),
            // 30: "Lovely. Last question from me..."
            Scene(
                30, "Calls when it's wrong", alert(),
                beats = listOf(
                    Beat(30, light = setOf("q"), card = BoardCard("Quiz: an email", "More than ten failures in five minutes. What do you create?")),
                    Beat(31, light = setOf("thr", "ag"), pulse = "thr" to "ag", card = BoardCard("Right answer", "A log search alert rule: schedule, threshold, action group.")),
                ),
            ),
            // 33: "And where does it all stop working?..."
            Scene(
                33, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(34, light = setOf("reads", "late", "sched"), pulse = "late" to "otel", card = BoardCard("Where KQL stops", "It reads, never fixes; rows land late; alerts check on a schedule.")),
                ),
            ),
            // 35: "Last question, for the exam. What do we remember?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("slow", "slice", "behind", "keep", "email"), card = BoardCard("Listen for the phrase", "Five for five.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
