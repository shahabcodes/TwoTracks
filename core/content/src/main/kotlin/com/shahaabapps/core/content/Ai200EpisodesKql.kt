package com.shahaabapps.core.content

/** KQL: asking your logs and metrics questions: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesKql {
    private val classic = Podcast(
        "ai-kql",
        listOf(
            maya("Hi, and welcome back. Zaiqa is all there now: the API, the service the phone talks to, and the order worker, both in Container Apps, orders in Azure SQL, the event feed in Cosmos DB. Today it is eight on a Friday, orders are failing, and we find out why."),
            leo("Failing how? Diners are telling us?"),
            maya("Support has forty messages saying payment failed, and our dashboard says the average request takes under a second. One of them is wrong, and today you learn the language that settles it."),
            leo("Which language, and asking what?"),
            maya("KQL, the Kusto Query Language. Kusto was Microsoft's internal name for the engine. Everything our services report about themselves goes to Application Insights, and that writes it into a Log Analytics workspace: a store of tables Azure keeps for you."),
            leo("Tables of what?"),
            maya("Each row is one thing that happened, with a time on it. Four tables you will live in. AppRequests: every call the Zaiqa API received, one per checkout, with Name, DurationMs and Success. AppDependencies: every call out, to Azure SQL, to Cosmos, to Redis."),
            leo("And when the API throws?"),
            maya("AppExceptions, one row per error, with the type and the message. AppTraces holds the plain log lines our code writes. And what the containers print lands in Container App Console Logs, with C L on the end."),
            leo("Right. How do I ask one of them something?"),
            maya("A query starts with the table's name, then passes its rows through steps, like an assembly line. Each step after the first begins with a pipe, a straight upright line that means: hand the rows on."),
            leo("Like chaining filter and map on a Kotlin list?"),
            maya("Exactly that shape. The first step is almost always a where, which keeps only the rows that match, like a sieve. And the rule is: filter on time first, always."),
            leo("Why time first?"),
            maya("Because the workspace holds months. Every row has a TimeGenerated column, and ago gives a time counted back from now. Keep TimeGenerated newer than ago four hours, and you read Friday evening instead of the summer."),
            leo("Fine. Now narrow it to checkout."),
            maya("Two ways, and the exam loves the difference. Where Name has checkout, or where Name contains checkout. Has looks for a whole word, and the workspace keeps an index: a list of the words it has seen, so it skips whole blocks that cannot match."),
            leo("And contains?"),
            maya("Contains looks for that text anywhere, even in the middle of another word, so it reads every character. Same answer, far more work. Reach for has first, and keep contains for a fragment."),
            leo("Got it. Now count the failures."),
            maya("That is summarize: it groups rows and works something out for each group. Summarize count by ResultCode, the number the server sends back, and you get one row per code. Ours said two thousand two hundreds, and three hundred and ten five hundreds."),
            leo("Three hundred and ten real diners. [pause] When, though?"),
            maya("For when, you need buckets, and that is bin. Bin rounds each time down to the start of its slice, like sorting letters into five-minute pigeonholes. Summarize the count by bin of TimeGenerated, five minutes, then render timechart, which draws the result as a line. Ours was flat until eight, then a wall."),
            leo("So why did the dashboard say everything was fine?"),
            maya("Because it charted the average duration, and an average hides the slow tail: the few very slow calls at the back of the queue. Use a percentile instead. The 95th, p95 for short, is the time that ninety five of every hundred calls beat."),
            leo("Give me that with small numbers."),
            maya("For example, line up twenty checkouts, fastest to slowest. Eighteen take half a second; the last two take six. The average is about one second, which looks lovely. The p95 is the nineteenth in the line: six seconds. That is what the unlucky diner in every twenty felt."),
            leo("And that diner writes to support."),
            maya("Every time. So: AppRequests, the last four hours, Name has checkout, summarize the 95th percentile of DurationMs by bin of TimeGenerated, render timechart. The line went from under a second to six, at eight exactly."),
            leo("Good. But the line still does not say what was slow."),
            maya("No. For that you join: it matches rows from two tables that share a value, and the value is OperationId. Picture a parcel's tracking number, given to each request when it arrives and carried by every row it causes: its dependency calls, its traces, its errors."),
            leo("So join the slow checkouts to AppDependencies, and the guilty step was?"),
            maya("The write to the Cosmos order events container. Five point eight of the six seconds, and Cosmos was telling us to slow down at dinner rush. Not the API, and not Azure SQL, which took the payment in forty milliseconds."),
            leo("Could I join on ResultCode instead? Slow rows, same code."),
            maya("[sigh] Please do not. Thousands of unrelated rows share a two hundred, so you would match everything with everything and wait a long time for nonsense. OperationId, always. The same key joins failed requests to AppExceptions for the error text."),
            leo("One more thing I want: which restaurant. Can the logs tell me that?"),
            maya("They can, because our API attaches the restaurant id to every request as a custom dimension: an extra labelled value you add yourself, beyond the built-in columns. They all land together in one column called Properties."),
            leo("One column holding many values? How do I get one out?"),
            maya("You read it by name, then convert it with tostring, because the column is a loose bag and summarize wants a proper value. Extend adds the result as a new column and keeps all the others."),
            leo("And why extend, not project?"),
            maya("Because project picks the columns you want and throws away the rest. Say you're building that query in a hurry: project the restaurant, and you have lost DurationMs, the thing you came for. That trap is on the exam."),
            leo("Fine. Extend the restaurant, then?"),
            maya("Then summarize count by restaurant. One place in Bandra was two thirds of the failures: the hot restaurant, every Friday. Now we have the when, the step and the who, in three queries."),
            leo("And next Friday I would still be asleep."),
            maya("Which is why you save the query as a log search alert rule. Azure runs it on a schedule, say every five minutes, over the last five minutes, and compares the result with a threshold, a line you draw: ten failures."),
            leo("And when it crosses, somebody still has to be watching?"),
            maya("No. An action group fires: a list of who to tell and how, like an email or a text message. The alert comes to you."),
            leo("Is that the only kind of alert?"),
            maya("No, and the comparison is worth knowing. A metric alert watches a ready-made number, like processor use, and fires in well under a minute, cheaply. A log search alert rule can ask anything KQL can ask, but only as often as its schedule."),
            leo("So, cheap and fast, or clever and a bit late."),
            maya("That is the trade. And three edges to remember. KQL only reads, so it tells you the problem and never fixes it. Rows land a minute or two after they happen, so no query is quite live. And in the workspace it is AppRequests and TimeGenerated, not the classic requests and timestamp, which only the Logs page of Application Insights still accepts."),
            leo("So what do I remember for the exam?"),
            maya("If the question says per five-minute slice, summarize by bin. If it asks what slow feels like, a percentile, not an average. If you need the dependency or the exception behind a request, join on OperationId."),
            leo("Keep going."),
            maya("If a whole word must be found fast in a big text column, has, not contains. If a new column must keep the others, extend. And if someone must be emailed when failures pass a number, a log search alert rule with a threshold and an action group."),
            leo("And anything to try today?"),
            maya("Fifteen minutes on any workspace with a little data. Open Logs and run AppRequests for the last hour, take ten. Count requests by ResultCode with summarize. Chart p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule with a threshold."),
            leo("Our logs, finally answering back on a Friday night. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-kql",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Eight O'Clock Checkout. Friday, Zaiqa, three hundred and ten failed orders between eight and nine, and a dashboard that says the evening was perfectly normal."),
            leo("Detective Leo, on the case. Three hundred angry diners and a happy dashboard. Somebody is lying to us."),
            maya("Nobody is lying. They are answering different questions. Everything our services report goes to Application Insights, which writes it into a Log Analytics workspace: a store of tables Azure keeps for you."),
            leo("Where each row is one event. What is in them?"),
            maya("AppRequests, one row per call into the Zaiqa API, the service the phone talks to, with Name, DurationMs and Success. AppDependencies, one row per call out, to Azure SQL, Cosmos or Redis. AppExceptions for errors, AppTraces for log lines."),
            leo("And the containers' own printing?"),
            maya("Container App Console Logs, with C L on the end. Its column names carry a suffix too: write ContainerAppName underscore s, or the query says there is no such column."),
            leo("Noted. First suspect: the dashboard. Show me how it was built."),
            maya("It started from AppRequests and used summarize, which groups rows and works something out for each group. It worked out the average of DurationMs per hour. Under a second, all evening."),
            leo("Then the average is innocent and the diners exaggerate. Case closed."),
            maya("[pause] Wrong suspect. An average hides the slow tail: a handful of very slow calls vanish among thousands of quick ones."),
            leo("Prove it with small numbers. I am a detective, not a statistician."),
            maya("Line up twenty checkouts, fastest to slowest. Eighteen take half a second; the last two take six. The average is about one second. The 95th percentile, p95, is the nineteenth in the line: six seconds."),
            leo("The unlucky diner in every twenty. And over the whole evening?"),
            maya("We asked again with a percentile, in buckets. Bin rounds each time down to the start of its slice, like five-minute pigeonholes, and render timechart draws the result as a line. It went from under a second to six, at eight exactly."),
            leo("So the diners were right. Second suspect: the intern's query that ran for four minutes yesterday."),
            maya("Good eye, wrong crime. That one had no time filter, so it trawled months. Every row has a TimeGenerated column, and ago gives a time counted back from now. Filter on time first, always."),
            leo("And the one that crawled over the console logs?"),
            maya("Clue two. It used contains, which looks for text anywhere, even inside other words, so it reads every character. Has looks for a whole word, and the workspace keeps an index: a list of the words it has seen, so it skips whole blocks."),
            leo("Same answer, less digging. Noted. Now: why eight o'clock?"),
            maya("For why, you join: match rows from two tables that share a value. The value is OperationId. Picture a parcel's tracking number, given to each request when it arrives and carried by every row it causes."),
            leo("Then I join the slow checkouts to the dependencies on ResultCode. Same code, same trouble."),
            maya("[sigh] That is how a detective loses an afternoon. Thousands of unrelated rows share a two hundred, so everything matches everything. OperationId, always."),
            leo("Fine. On OperationId, then. What came back?"),
            maya("Each step inside one checkout, with its own DurationMs. Five point eight of the six seconds was one write to the Cosmos order events container, and Cosmos was telling us to slow down at dinner rush."),
            leo("So the API was never the culprit."),
            maya("Never. Azure SQL took the payment in forty milliseconds. The episode on request units, the price Cosmos charges for every read and write, had warned us about Friday, and nobody had wired that warning to an alarm."),
            leo("One more question for the file: which restaurant?"),
            maya("Our API attaches the restaurant id to every request as a custom dimension: an extra labelled value you add yourself. They all land in one column called Properties, a loose bag of values."),
            leo("And you pull one out how?"),
            maya("By name, with tostring around it so that summarize gets a proper value. Extend adds that as a new column and keeps all the others."),
            leo("Why not project it? Fewer columns, tidier report."),
            maya("And there goes DurationMs, the thing you came for. Project picks what to keep and throws the rest away; extend keeps everything. That trap is on the exam."),
            leo("Extend, then summarize count by restaurant. The answer?"),
            maya("One place in Bandra, two thirds of the failures. The hot restaurant, every Friday."),
            leo("Case nearly closed. But I do not want to be woken by support next week."),
            maya("Then you save the query as a log search alert rule. Azure runs it on a schedule, say every five minutes over the last five minutes, and compares the count with a threshold: ten failures."),
            leo("And someone watches for the crossing?"),
            maya("No. An action group fires: a list of who to tell and how, like an email or a text message."),
            leo("Is there a cheaper alarm?"),
            maya("There is a different one. A metric alert watches a ready-made number, like processor use, and fires in well under a minute. A log search alert rule can ask anything KQL can ask, but only on its schedule."),
            leo("Any loose ends in the file?"),
            maya("Three. KQL only reads, so it names the problem and never fixes it. Rows land a minute or two late, so no query is quite live. And in the workspace it is AppRequests and TimeGenerated."),
            leo("As opposed to the classic names?"),
            maya("Requests and timestamp, which the Logs page of Application Insights still accepts. Same data, two spellings. Write the classic ones against the workspace and the query fails outright."),
            leo("So what is the case summary for the exam?"),
            maya("If it says per five-minute slice, summarize by bin. If it asks what slow feels like, a percentile, not an average. If you need the dependency or exception behind a request, join on OperationId."),
            leo("And the rest of the file?"),
            maya("A whole word in a big text column: has, rather than contains. A new column that must keep the others: extend. And an email when failures pass a number: a log search alert rule with a threshold and an action group."),
            leo("Case closed. Anything to try today?"),
            maya("Fifteen minutes. Open Logs, run AppRequests for the last hour and take ten. Count requests by ResultCode with summarize. Chart p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule with a threshold."),
            leo("Eight o'clock, solved before it happens next time. See you then."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-kql",
        listOf(
            maya("Okay, did you hear what happened to Zaiqa on Friday night? Three hundred and ten failed orders between eight and nine, and a dashboard that swore the evening was fine. Sit down, this one has five culprits."),
            leo("I heard shouting about a percentile. Start at the beginning. Where does any of this get written down?"),
            maya("Everything our services report goes to Application Insights, which writes it into a Log Analytics workspace: a store of tables Azure keeps. AppRequests for calls in, AppDependencies for calls out, AppExceptions and AppTraces."),
            leo("And you ask them things with?"),
            maya("KQL, the Kusto Query Language. You start with the table, then pass the rows through steps, each after a pipe, like an assembly line. Now. Culprit one: Tom."),
            leo("[chuckle] Of course it is Tom. What did Tom build?"),
            maya("The dashboard. Tom charted the average of DurationMs per hour, and the average sat happily under a second all evening, because an average hides the slow tail."),
            leo("Meaning the few very slow calls at the back."),
            maya("Exactly. Priya redid it with the 95th percentile, p95: the time ninety five of every hundred calls beat. Line up twenty checkouts; eighteen take half a second and two take six. The average says one second, the p95 says six."),
            leo("And the real line?"),
            maya("Steady until eight, then a wall at six seconds. She grouped it with bin, which rounds each time down into five-minute pigeonholes, then render timechart to draw it."),
            leo("Right. Culprit two?"),
            maya("The intern. He ran a query with no time filter, and it chewed through months of data for four minutes before anybody noticed."),
            leo("What should he have written?"),
            maya("A where on TimeGenerated, with ago, which gives a time counted back from now. The last four hours, and it comes back at once. Filter on time first, always."),
            leo("Culprit three? Please say Dana."),
            maya("Dana. She wanted the slow step inside a checkout, so she joined AppRequests to AppDependencies, which is right, and joined them on ResultCode, which is not."),
            leo("Why not? Same code, same trouble."),
            maya("Thousands of unrelated rows share a two hundred, so everything matched everything. [sigh] It ran for an age and came back nonsense. The key is OperationId."),
            leo("Which is what, in plain words?"),
            maya("A parcel's tracking number. Each request gets one when it arrives, and every row it causes, its dependencies, its traces, its errors, carries the same one."),
            leo("And once she joined on that?"),
            maya("Five point eight of the six seconds was one write to the Cosmos order events container, with Cosmos telling us to slow down at dinner rush. Azure SQL had taken the payment in forty milliseconds."),
            leo("So the API, the service every order goes through, was innocent all along. Culprit four?"),
            maya("Marcus, on the container console logs. He searched them with contains, which looks for text anywhere, even inside another word, so it reads every character."),
            leo("And the quick way?"),
            maya("Has. It looks for a whole word, and the workspace keeps an index: a list of the words it has seen, so it skips blocks that cannot match. Same answer, a fraction of the work."),
            leo("Noted forever. Culprit five, and then I want the ending."),
            maya("Priya again, I am afraid. She wanted the failures per restaurant. Our API attaches the restaurant id to every request as a custom dimension: an extra labelled value you add yourself, all landing in one column called Properties."),
            leo("And pulling one out of that bag?"),
            maya("By name, with tostring around it, so summarize gets a proper value. She used project to pick it, and lost DurationMs, which was the whole point."),
            leo("[laugh] And the right operator?"),
            maya("Extend. It adds the calculated column and keeps every other one. Then summarize count by restaurant: one place in Bandra, two thirds of the failures."),
            leo("The hot restaurant again. So how does the story end?"),
            maya("With nobody on call. They saved the failures query as a log search alert rule: Azure runs it every five minutes over the last five minutes, and compares the count with a threshold of ten."),
            leo("And then it rings whose phone?"),
            maya("An action group's. That is a list of who to tell and how: an email, a text message."),
            leo("Why not a plain metric alert? Those are cheap, I heard."),
            maya("Cheap and fast, under a minute, but a metric alert only watches a ready-made number like processor use. A log search alert rule asks anything KQL can ask, on its schedule. That is the trade."),
            leo("Any last gossip?"),
            maya("Only the small print. KQL only reads, so it names the problem and never fixes it. Rows land a minute or two late. And the intern keeps writing requests and timestamp."),
            leo("Which are wrong where?"),
            maya("In the workspace, where it is AppRequests and TimeGenerated. The Logs page of Application Insights still takes the classic names. Same data, two spellings."),
            leo("So what should I remember for the exam?"),
            maya("Per five-minute slice is summarize by bin. What slow feels like is a percentile, not an average. The dependency or exception behind a request is a join on OperationId."),
            leo("And the other three?"),
            maya("A whole word in a big text column is has, not contains. A new column that keeps the others is extend. And an email when failures pass a number is a log search alert rule with a threshold and an action group."),
            leo("And anything to try today?"),
            maya("Fifteen minutes. Open Logs and run AppRequests for the last hour, take ten. Count requests by ResultCode with summarize. Chart p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule with a threshold."),
            leo("Five culprits and one alert rule. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-kql",
        listOf(
            nova("Hey, you. It is late, the city has eaten, and the kitchens are quiet. [pause] Tonight: the Friday that went wrong at eight, and how we found out why."),
            ivy("Zaiqa, failing at dinner rush. Go on, Nova. Slowly."),
            nova("Three hundred and ten orders failed between eight and nine. And the dashboard, bless it, said the evening was perfectly normal. [pause] Somebody had asked the wrong question."),
            ivy("Where do the right questions get asked?"),
            nova("In a Log Analytics workspace. Everything our services report goes to Application Insights, and that writes it there, as tables. One row is one thing that happened, with a time on it."),
            ivy("Which tables, for us?"),
            nova("AppRequests: every call into the Zaiqa API, the service the phone talks to, with Name, DurationMs and Success. AppDependencies: every call out, to Azure SQL, to Cosmos, to Redis. AppExceptions for errors. AppTraces for the plain log lines. [pause]"),
            ivy("And the language?"),
            nova("KQL, the Kusto Query Language. You name a table, then pass its rows through steps, one after another. Each step after the first begins with a pipe, which just means: hand the rows on."),
            ivy("Like a conveyor belt at this hour. What goes first?"),
            nova("A where, which keeps only the rows that match. And the first thing you filter on is time, always, because the workspace holds months. TimeGenerated is the column, and ago is a time counted back from now."),
            ivy("Then you narrowed it to checkout, I assume."),
            nova("Where Name has checkout. [pause] And there is a lovely little exam question hiding inside that one word."),
            ivy("Tell me, since I am up anyway."),
            nova("Has looks for a whole word, and the workspace keeps an index: a list of the words it has seen, so it skips blocks that cannot match. Contains looks for your text anywhere, even inside another word, and reads every character."),
            ivy("Same answer, more work. And then you counted them?"),
            nova("Summarize count by ResultCode, the number the server sends back. Two thousand two hundreds, three hundred and ten five hundreds. Then by bin of TimeGenerated, five minutes, and render timechart to draw it."),
            ivy("Bin being the pigeonholes."),
            nova("Mm. It rounds each time down to the start of its slice. [pause] And the line was steady until eight, then a wall."),
            ivy("So why did the dashboard sleep through it?"),
            nova("It charted the average. An average hides the slow tail, the handful of very slow calls at the back of the queue. Twenty checkouts: eighteen at half a second, two at six."),
            ivy("And the average says?"),
            nova("About one second. Lovely. The 95th percentile, p95, is the nineteenth in that line: six seconds. That is what the unlucky diner in every twenty felt, and that diner writes to support. [pause]"),
            ivy("Percentiles, not averages. I will remember that at this hour. But what was actually slow?"),
            nova("For that you join: match rows from two tables that share a value. OperationId, which is a parcel's tracking number. Each request gets one when it arrives, and every row it causes carries it."),
            ivy("And never on ResultCode, I imagine."),
            nova("Never. Thousands of unrelated rows share a two hundred, so everything matches everything and you wait a long time for nonsense."),
            ivy("So, the guilty step?"),
            nova("Five point eight of the six seconds was one write to the Cosmos order events container, with Cosmos telling us to slow down at dinner rush. Azure SQL took the payment in forty milliseconds."),
            ivy("Request units, the price Cosmos charges for every read and write. That episode is coming back to haunt you."),
            nova("It did warn us. [pause] One more question, though: which restaurant?"),
            ivy("Can the logs even know that?"),
            nova("Ours can. The API attaches the restaurant id to every request as a custom dimension: an extra labelled value you add yourself. They all land in one column called Properties, a loose bag."),
            ivy("And you lift one out by name."),
            nova("By name, with tostring around it, so summarize gets a proper value. Extend adds it as a new column and keeps all the others. Project would pick that one and throw DurationMs away."),
            ivy("Which is the thing you came for. [pause] And the answer?"),
            nova("One place in Bandra. Two thirds of the failures. The hot restaurant, every Friday."),
            ivy("Lovely. Now tell me you do not do this at eight every week."),
            nova("We do not, because the query became a log search alert rule. Azure runs it every five minutes over the last five minutes, and compares the count with a threshold: ten failures."),
            ivy("And who gets woken?"),
            nova("An action group decides: a list of who to tell and how. An email, a text message. [pause] Not you, tonight."),
            ivy("Good. Is there a quicker kind?"),
            nova("A metric alert, on a ready-made number like processor use, in well under a minute. But it cannot ask a question. A log search alert rule can ask anything KQL can, on its schedule."),
            ivy("Any small print before I close my laptop?"),
            nova("Three things. KQL only reads, so it names the problem and never fixes it. Rows land a minute or two late. And in the workspace it is AppRequests and TimeGenerated, not the classic requests and timestamp."),
            ivy("So what would the exam want from tonight?"),
            nova("Per five-minute slice: summarize by bin. What slow feels like: a percentile, not an average. The dependency or exception behind a request: join on OperationId."),
            ivy("Three more, and then I am going to bed."),
            nova("A whole word in a big text column: has, not contains. A new column that keeps the others: extend. An email when failures pass a number: a log search alert rule with a threshold and an action group."),
            ivy("And what should they try today?"),
            nova("Fifteen quiet minutes. Open Logs and run AppRequests for the last hour, take ten. Count requests by ResultCode with summarize. Chart p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule with a threshold."),
            ivy("Goodnight, you. Sleep before eight o'clock on Friday. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-kql",
        listOf(
            nova("Tonight is date night, love. Candles, something with far too much chilli in it, and the two of us picking apart the Friday that Zaiqa fell over at eight."),
            rafe("Three hundred and ten failed orders, and a dashboard that slept straight through it. Very romantic."),
            nova("It is, if you enjoy being right. Everything our services report goes to Application Insights, which writes it into a Log Analytics workspace. So, quiz me, darling. What is in there?"),
            rafe("Tables. AppRequests for calls into the API, the service the phone talks to, AppDependencies for calls it made out, AppExceptions for errors, AppTraces for log lines. One row, one thing that happened."),
            nova("Right answer, and quickly too. [kiss]"),
            rafe("And the language is KQL, the Kusto Query Language. A table, then steps, each after a pipe."),
            nova("Each step hands its rows to the next. Now my turn to be strict. Exam style. The dashboard charted the average duration per hour and saw nothing. What should it have charted, and why?"),
            rafe("A percentile. An average hides the slow tail. Twenty checkouts, eighteen at half a second and two at six: the average is one second, the p95 is six."),
            nova("The time ninety five of every hundred calls beat. Right answer. [kiss]"),
            rafe("And the unlucky diner in every twenty is the one who writes to support."),
            nova("Always. The real line was steady until eight, then a wall. We grouped it with bin, which rounds each time down to the start of its five-minute slice, and drew it with render timechart."),
            rafe("Lovely. Now let me be difficult, since you are enjoying yourself. The first step of any query: what is it, and what goes wrong without it?"),
            nova("A where on time. TimeGenerated newer than ago four hours, because the workspace holds months. Leave it out and you trawl the summer and wait four minutes."),
            rafe("Right answer, and no kiss until you tell me how you narrowed it down to checkout."),
            nova("Where Name has checkout. Has looks for a whole word, and the workspace keeps an index: a list of the words it has seen, so it skips blocks that cannot match. Contains reads every character, even inside other words."),
            rafe("That one was worth waiting for. [kiss]"),
            nova("Then summarize count by ResultCode: two thousand two hundreds, three hundred and ten five hundreds. [pause] But a count never tells you what was slow."),
            rafe("No. For that you join two tables on something they share."),
            nova("On what, exactly? Be careful, love. People get this wrong in the exam and in production."),
            rafe("OperationId. A parcel's tracking number: each request gets one when it arrives, and every row it causes carries it. Never ResultCode, or everything matches everything."),
            nova("Right answer, and said like someone who has suffered. [kiss]"),
            rafe("I have. So what did the join say?"),
            nova("Five point eight of the six seconds was one write to the Cosmos order events container, with Cosmos telling us to slow down at dinner rush. Azure SQL took the payment in forty milliseconds."),
            rafe("Innocent API, guilty request units. And which restaurant?"),
            nova("Now that is my favourite part. The API attaches the restaurant id to every request as a custom dimension: an extra labelled value you add yourself. They all land in one column called Properties."),
            rafe("A bag of values. So you lift the restaurant out by name, with tostring around it."),
            nova("And then, darling, the question everybody fails. Extend or project?"),
            rafe("Extend. It adds the new column and keeps every other one. Project picks and throws the rest away, including DurationMs, which is what you came for."),
            nova("Right answer. [kiss] One place in Bandra, two thirds of the failures."),
            rafe("The hot restaurant. Every Friday. And next week, are we eating, or watching a chart?"),
            nova("Eating. The query became a log search alert rule: Azure runs it every five minutes over the last five minutes, and compares the count with a threshold of ten failures."),
            rafe("And it tells whom?"),
            nova("An action group: a list of who to tell and how, an email or a text message. Not us, over dessert."),
            rafe("One more from me, and then pudding. Why not a metric alert? They are cheap, and they fire in well under a minute."),
            nova("Because a metric alert only watches a ready-made number, like processor use. It cannot ask a question. A log search alert rule asks anything KQL can ask, but only on its schedule."),
            rafe("Cheap and fast, or clever and a little late. [pause] Any small print?"),
            nova("Three bits. KQL only reads, so it names the problem and never fixes it. Rows land a minute or two late. And in the workspace it is AppRequests and TimeGenerated."),
            rafe("Not requests and timestamp, which only the Logs page of Application Insights still takes. Same data, two spellings."),
            nova("Right answer, and that is the last of them. [kiss]"),
            rafe("Then give me the exam list, and I will listen beautifully."),
            nova("Per five-minute slice: summarize by bin. What slow feels like: a percentile, not an average. The dependency or exception behind a request: join on OperationId."),
            rafe("And three more."),
            nova("A whole word in a big text column: has, not contains. A new column that must keep the others: extend. An email when failures pass a number: a log search alert rule with a threshold and an action group."),
            rafe("Perfect. And what should they try today, before Friday comes round again?"),
            nova("Fifteen minutes. Open Logs and run AppRequests for the last hour, take ten. Count requests by ResultCode with summarize. Chart p95 of DurationMs by bin of TimeGenerated with render timechart. Then turn a failures query into a log search alert rule with a threshold."),
            rafe("Dinner, then logs. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun telemetry() = DiagramSpec(
        "Everything Zaiqa's services report goes to Application Insights, which writes it into a Log Analytics workspace.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.22f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "Container Apps"),
            DNode("worker", "Order worker", 0.22f, 0.5f, w = 0.4f, h = 0.2f, sub = "to the kitchen"),
            DNode("taste", "Taste service", 0.22f, 0.82f, w = 0.4f, h = 0.2f, sub = "Python"),
            DNode("ai", "Application Insights", 0.72f, 0.3f, w = 0.46f, h = 0.22f, tone = 2, sub = "collects it all"),
            DNode("ws", "Workspace", 0.72f, 0.72f, w = 0.46f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "Log Analytics tables"),
        ),
        edges = listOf(
            DEdge("api", "ai"),
            DEdge("worker", "ai"),
            DEdge("taste", "ai"),
            DEdge("ai", "ws", "writes"),
        ),
        aspect = 0.8f,
    )

    private fun tables() = DiagramSpec(
        "The tables a Zaiqa question starts from: calls in, calls out, errors, log lines, and what the container printed.",
        nodes = listOf(
            DNode("req", "AppRequests", 0.26f, 0.14f, w = 0.46f, h = 0.2f, tone = 1, sub = "calls in, one per checkout"),
            DNode("dep", "AppDependencies", 0.74f, 0.14f, w = 0.44f, h = 0.2f, sub = "calls out"),
            DNode("exc", "AppExceptions", 0.26f, 0.45f, w = 0.46f, h = 0.2f, sub = "errors"),
            DNode("tra", "AppTraces", 0.74f, 0.45f, w = 0.44f, h = 0.2f, sub = "log lines"),
            DNode("con", "Console Logs, C L", 0.5f, 0.8f, w = 0.66f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "what the container printed"),
        ),
        aspect = 0.8f,
    )

    private fun pipeline() = DiagramSpec(
        "A KQL query is a pipeline: rows leave the table and pass through each step, left to right, and come out as a chart.",
        nodes = listOf(
            DNode("t", "AppRequests", 0.13f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "rows in"),
            DNode("w", "where", 0.38f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, sub = "last 4 hours"),
            DNode("s", "summarize", 0.63f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, sub = "p95 per 5 min"),
            DNode("r", "render", 0.88f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, tone = 2, sub = "timechart"),
            DNode("n", "Each pipe hands the rows on", 0.5f, 0.8f, w = 0.6f, h = 0.25f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(DEdge("t", "w"), DEdge("w", "s"), DEdge("s", "r")),
        aspect = 0.4f,
    )

    private fun terms() = DiagramSpec(
        "On a big text column, has looks for a whole word through the term index, and contains reads every character.",
        nodes = listOf(
            DNode("col", "A big text column", 0.5f, 0.14f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "Name, or a log line"),
            DNode("has", "has", 0.26f, 0.55f, w = 0.46f, h = 0.22f, tone = 1, sub = "a whole word"),
            DNode("con", "contains", 0.74f, 0.55f, w = 0.44f, h = 0.22f, tone = 2, sub = "any text, read it all"),
            DNode("idx", "The term index", 0.5f, 0.86f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "skips what cannot match"),
        ),
        edges = listOf(DEdge("col", "has"), DEdge("col", "con"), DEdge("has", "idx")),
        aspect = 0.8f,
    )

    private fun percentile() = DiagramSpec(
        "An average hides the slow tail; the 95th percentile says what the slowest one checkout in twenty felt like.",
        nodes = listOf(
            DNode("calls", "Twenty checkouts", 0.5f, 0.14f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "eighteen quick, two slow"),
            DNode("avg", "Average", 0.26f, 0.52f, w = 0.46f, h = 0.22f, tone = 2, sub = "one second: looks fine"),
            DNode("p95", "p95", 0.74f, 0.52f, w = 0.44f, h = 0.22f, tone = 1, sub = "six seconds: the real feel"),
            DNode("diner", "One diner in twenty", 0.5f, 0.85f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "the one who writes in"),
        ),
        edges = listOf(DEdge("calls", "avg"), DEdge("calls", "p95"), DEdge("p95", "diner")),
        aspect = 0.8f,
    )

    private fun joinSpec() = DiagramSpec(
        "Joining the slow checkouts to their dependency calls on OperationId shows which step inside one checkout was slow.",
        nodes = listOf(
            DNode("req", "AppRequests", 0.24f, 0.16f, w = 0.42f, h = 0.2f, tone = 1, sub = "the slow checkouts"),
            DNode("dep", "AppDependencies", 0.76f, 0.16f, w = 0.42f, h = 0.2f, sub = "each step inside"),
            DNode("op", "OperationId", 0.5f, 0.5f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "a tracking number"),
            DNode("cosmos", "Cosmos write", 0.24f, 0.84f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2, sub = "most of six seconds"),
            DNode("sql", "Azure SQL", 0.76f, 0.84f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "forty milliseconds"),
        ),
        edges = listOf(
            DEdge("req", "op"),
            DEdge("dep", "op"),
            DEdge("op", "cosmos", "slow"),
            DEdge("op", "sql"),
        ),
        aspect = 0.8f,
    )

    private fun dims() = DiagramSpec(
        "A custom dimension lands in the Properties column; extend adds it as a column and keeps the others, project does not.",
        nodes = listOf(
            DNode("props", "Properties", 0.5f, 0.14f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "a bag of custom values"),
            DNode("ext", "extend", 0.24f, 0.52f, w = 0.42f, h = 0.22f, tone = 1, sub = "adds, keeps the rest"),
            DNode("proj", "project", 0.76f, 0.52f, w = 0.42f, h = 0.22f, tone = 2, sub = "picks, drops the rest"),
            DNode("byrest", "Count by restaurant", 0.5f, 0.86f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "Bandra: two thirds"),
        ),
        edges = listOf(DEdge("props", "ext"), DEdge("props", "proj"), DEdge("ext", "byrest")),
        aspect = 0.8f,
    )

    private fun alert() = DiagramSpec(
        "A log search alert rule runs the query on a schedule, compares the count with a threshold and tells an action group.",
        nodes = listOf(
            DNode("q", "The failures query", 0.24f, 0.16f, w = 0.42f, h = 0.2f, tone = 1, sub = "every five minutes"),
            DNode("th", "Threshold", 0.76f, 0.16f, w = 0.42f, h = 0.2f, sub = "ten failures"),
            DNode("ag", "Action group", 0.76f, 0.52f, w = 0.42f, h = 0.2f, tone = 2, sub = "email, text message"),
            DNode("you", "You", 0.24f, 0.52f, w = 0.42f, h = 0.2f, kind = NodeKind.USER),
            DNode("met", "Metric alert", 0.5f, 0.86f, w = 0.66f, h = 0.2f, kind = NodeKind.NOTE, sub = "a ready-made number, faster"),
        ),
        edges = listOf(DEdge("q", "th"), DEdge("th", "ag"), DEdge("ag", "you", "tells")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's KQL questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("slice", "Per five-minute slice", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "summarize by bin"),
            DNode("slow", "What slow feels like", 0.74f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a percentile"),
            DNode("behind", "The step behind it", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "join on OperationId"),
            DNode("word", "A whole word, fast", 0.74f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "has, not contains"),
            DNode("keep", "Keep the other columns", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "extend, not project"),
            DNode("email", "Email me past ten", 0.74f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "log search alert rule"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("slice", "slow", "behind", "word", "keep", "email")
    private val today = BoardCard("Today", "Last hour, count by ResultCode, chart p95 with render, then an alert rule.")
    private val lastHour = BoardCard("KQL", "AppRequests | where TimeGenerated > ago(4h) | take 10", code = true)
    private val hasCode = BoardCard("KQL", "| where Name has \"checkout\"", code = true)
    private val byCode = BoardCard("KQL", "| summarize count() by ResultCode", code = true)
    private val p95Code = BoardCard("KQL", "| summarize percentile(DurationMs, 95) by bin(TimeGenerated, 5m) | render timechart", code = true)
    private val joinCode = BoardCard("KQL", "| join kind=inner (AppDependencies) on OperationId", code = true)
    private val dimCode = BoardCard("KQL", "| extend restaurant = tostring(Properties[\"zaiqa.restaurant\"])", code = true)
    private val alertCode = BoardCard("KQL", "| where Success == false | summarize failures = count() by bin(TimeGenerated, 5m)", code = true)
    private val opCard = BoardCard("OperationId", "A parcel's tracking number, carried by every row a request causes.")
    private val notCodeCard = BoardCard("Never on ResultCode", "Thousands of unrelated rows share a two hundred: everything matches.")
    private val dimCard = BoardCard("A custom dimension", "An extra labelled value you add; they all land in Properties.")
    private val agCard = BoardCard("Action group", "A list of who to tell and how: an email, a text message.")
    private val metCard = BoardCard("Metric alert", "A ready-made number, under a minute, but it cannot ask a question.")
    private val threeMore = BoardCard("And three more", "has not contains, extend not project, a log search alert rule.")
    private val phraseCard = BoardCard("Listen for the phrase", "Per five-minute slice, what slow feels like, the step behind it.")
    private val containsCard = BoardCard("contains", "Looks anywhere, even inside another word, so it reads every character.")
    private val wsCard = BoardCard("The workspace", "Application Insights writes it all into a Log Analytics workspace.")
    private val fourTables = BoardCard("Four tables", "Calls in, calls out, errors, log lines. One row, one event.")
    private val guiltyCard = BoardCard("The guilty step", "One write to the Cosmos order events took most of the six seconds.")
    private val binCard = BoardCard("bin and render", "Five-minute pigeonholes, drawn as a line with time along the bottom.")
    private val byRestCard = BoardCard("Count by restaurant", "One place in Bandra: two thirds of the failures, every Friday.")

    private fun classicBoard() = PodcastBoard(
        "ai-kql",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is all there now..."
            Scene(
                0, "Where the logs land", telemetry(),
                beats = listOf(
                    Beat(0, show = setOf("api", "worker"), light = setOf("api"), card = BoardCard("Friday, eight o'clock", "Orders are failing, and it is already written down somewhere.")),
                    Beat(2, light = setOf("api"), card = BoardCard("Two answers", "Support says payment failed. The dashboard says under a second.")),
                    Beat(4, show = setOf("taste", "ai", "ws"), light = setOf("ai", "ws"), pulse = "ai" to "ws", card = wsCard),
                ),
            ),
            // 5: "Tables of what?"
            Scene(
                5, "The tables", tables(),
                beats = listOf(
                    Beat(6, show = setOf("req", "dep"), light = setOf("req", "dep"), card = BoardCard("Calls in, calls out", "One row per checkout; one row per call to SQL, Cosmos or Redis.")),
                    Beat(8, show = setOf("exc", "tra", "con"), light = setOf("exc", "tra", "con"), card = BoardCard("Errors, traces, console", "AppExceptions, AppTraces, and what the container printed.")),
                ),
            ),
            // 9: "Right. How do I ask one of them something?"
            Scene(
                9, "A query is a pipeline", pipeline(),
                beats = listOf(
                    Beat(10, show = setOf("t", "n"), light = setOf("t"), card = BoardCard("Start with the table", "Then each step after a pipe hands its rows to the next.")),
                    Beat(12, show = setOf("w"), light = setOf("w"), pulse = "t" to "w", card = BoardCard("where", "Keeps only the rows that match, like a sieve. Time first, always.")),
                    Beat(14, light = setOf("w"), card = lastHour),
                ),
            ),
            // 15: "Fine. Now narrow it to checkout."
            Scene(
                15, "has, or contains", terms(),
                beats = listOf(
                    Beat(16, show = setOf("col", "has", "idx"), light = setOf("has", "idx"), pulse = "has" to "idx", card = hasCode),
                    Beat(18, show = setOf("con"), light = setOf("con"), card = containsCard),
                ),
            ),
            // 19: "Got it. Now count the failures."
            Scene(
                19, "Counting them", pipeline(),
                beats = listOf(
                    Beat(20, show = setOf("t", "w", "s", "n"), light = setOf("s"), pulse = "w" to "s", card = byCode),
                    Beat(22, show = setOf("r"), light = setOf("r"), pulse = "s" to "r", card = binCard),
                ),
            ),
            // 23: "So why did the dashboard say everything was fine?"
            Scene(
                23, "The average hides it", percentile(),
                beats = listOf(
                    Beat(24, show = setOf("calls", "avg"), light = setOf("avg"), card = BoardCard("The average", "About one second, which looks lovely, and hides the slow tail.")),
                    Beat(26, show = setOf("p95", "diner"), light = setOf("p95"), card = BoardCard("p95", "The nineteenth of twenty: six seconds, and that diner writes in.")),
                    Beat(28, light = setOf("diner"), card = p95Code),
                ),
            ),
            // 29: "Good. But the line still does not say what was slow."
            Scene(
                29, "Which step was slow", joinSpec(),
                beats = listOf(
                    Beat(30, show = setOf("req", "dep", "op"), light = setOf("op"), pulse = "req" to "op", card = opCard),
                    Beat(31, light = setOf("dep"), pulse = "dep" to "op", card = joinCode),
                    Beat(32, show = setOf("cosmos", "sql"), light = setOf("cosmos"), pulse = "op" to "cosmos", card = guiltyCard),
                    Beat(34, light = setOf("req"), card = notCodeCard),
                ),
            ),
            // 35: "One more thing I want: which restaurant..."
            Scene(
                35, "Which restaurant", dims(),
                beats = listOf(
                    Beat(36, show = setOf("props"), light = setOf("props"), card = dimCard),
                    Beat(38, show = setOf("ext"), light = setOf("ext"), pulse = "props" to "ext", card = dimCode),
                    Beat(40, show = setOf("proj"), light = setOf("proj"), card = BoardCard("Not project", "It drops DurationMs, the thing you came for. Extend keeps it all.")),
                    Beat(42, show = setOf("byrest"), light = setOf("byrest"), card = byRestCard),
                ),
            ),
            // 43: "And next Friday I would still be asleep."
            Scene(
                43, "Being told next time", alert(),
                beats = listOf(
                    Beat(44, show = setOf("q", "th"), light = setOf("q", "th"), pulse = "q" to "th", card = alertCode),
                    Beat(46, show = setOf("ag", "you"), light = setOf("ag"), pulse = "th" to "ag", card = agCard),
                    Beat(48, show = setOf("met"), light = setOf("met"), card = metCard),
                ),
            ),
            // 51: "So what do I remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("slice", "slow", "behind"), card = phraseCard),
                    Beat(54, light = setOf("word", "keep", "email"), card = threeMore),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-kql--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Eight O'Clock Checkout..."
            Scene(
                0, "The case", telemetry(),
                beats = listOf(
                    Beat(0, show = setOf("api", "worker"), light = setOf("api"), card = BoardCard("The Eight O'Clock Checkout", "Three hundred and ten failed orders, and a dashboard that saw nothing.")),
                    Beat(2, show = setOf("taste", "ai", "ws"), light = setOf("ai", "ws"), pulse = "ai" to "ws", card = wsCard),
                ),
            ),
            // 3: "Where each row is one event. What is in them?"
            Scene(
                3, "The evidence", tables(),
                beats = listOf(
                    Beat(4, show = setOf("req", "dep", "exc", "tra"), light = setOf("req", "dep"), card = fourTables),
                    Beat(6, show = setOf("con"), light = setOf("con"), card = BoardCard("Console Logs, C L", "Its columns carry a suffix: ContainerAppName underscore s.")),
                ),
            ),
            // 7: "Noted. First suspect: the dashboard..."
            Scene(
                7, "Suspect one: the average", percentile(),
                beats = listOf(
                    Beat(8, show = setOf("calls", "avg"), light = setOf("avg"), card = BoardCard("The dashboard", "It charted the average of DurationMs per hour. Under a second.")),
                    Beat(10, light = setOf("calls"), card = BoardCard("Wrong suspect", "An average hides the slow tail: a few slow calls among thousands.")),
                    Beat(12, show = setOf("p95", "diner"), light = setOf("p95"), card = BoardCard("p95", "The nineteenth of twenty: six seconds, at eight exactly.")),
                    Beat(14, light = setOf("diner"), card = p95Code),
                ),
            ),
            // 15: "So the diners were right. Second suspect..."
            Scene(
                15, "Suspect two: no time filter", pipeline(),
                beats = listOf(
                    Beat(16, show = setOf("t", "w", "n"), light = setOf("w"), pulse = "t" to "w", card = lastHour),
                ),
            ),
            // 17: "And the one that crawled over the console logs?"
            Scene(
                17, "Suspect three: contains", terms(),
                beats = listOf(
                    Beat(18, show = setOf("col", "has", "con", "idx"), light = setOf("con"), card = containsCard),
                    Beat(19, light = setOf("has", "idx"), pulse = "has" to "idx", card = hasCode),
                ),
            ),
            // 20: "For why, you join..."
            Scene(
                20, "The join", joinSpec(),
                beats = listOf(
                    Beat(20, show = setOf("req", "dep", "op"), light = setOf("op"), pulse = "req" to "op", card = opCard),
                    Beat(22, light = setOf("req"), card = notCodeCard),
                    Beat(24, show = setOf("cosmos", "sql"), light = setOf("cosmos"), pulse = "op" to "cosmos", card = joinCode),
                    Beat(26, light = setOf("sql"), card = BoardCard("Innocent", "Azure SQL took the payment in forty milliseconds.")),
                ),
            ),
            // 27: "One more question for the file: which restaurant?"
            Scene(
                27, "Which restaurant", dims(),
                beats = listOf(
                    Beat(28, show = setOf("props"), light = setOf("props"), card = dimCard),
                    Beat(30, show = setOf("ext"), light = setOf("ext"), pulse = "props" to "ext", card = dimCode),
                    Beat(32, show = setOf("proj"), light = setOf("proj"), card = BoardCard("Not project", "It drops DurationMs, the thing you came for. Extend keeps it all.")),
                    Beat(34, show = setOf("byrest"), light = setOf("byrest"), card = byRestCard),
                ),
            ),
            // 35: "Case nearly closed. But I do not want to be woken..."
            Scene(
                35, "No more waiting up", alert(),
                beats = listOf(
                    Beat(36, show = setOf("q", "th"), light = setOf("q", "th"), pulse = "q" to "th", card = alertCode),
                    Beat(38, show = setOf("ag", "you"), light = setOf("ag"), pulse = "th" to "ag", card = agCard),
                    Beat(40, show = setOf("met"), light = setOf("met"), card = metCard),
                ),
            ),
            // 45: "So what is the case summary for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("slice", "slow", "behind"), card = BoardCard("The case summary", "Per five-minute slice, what slow feels like, the step behind it.")),
                    Beat(48, light = setOf("word", "keep", "email"), card = threeMore),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-kql--gossip",
        listOf(
            // 0: "Okay, did you hear what happened to Zaiqa on Friday night?..."
            Scene(
                0, "Friday night at Zaiqa", telemetry(),
                beats = listOf(
                    Beat(0, show = setOf("api", "worker"), light = setOf("api"), card = BoardCard("Five culprits", "Three hundred and ten failed orders, and a dashboard that saw nothing.")),
                    Beat(2, show = setOf("taste", "ai", "ws"), light = setOf("ws"), pulse = "ai" to "ws", card = wsCard),
                ),
            ),
            // 4: "KQL, the Kusto Query Language..."
            Scene(
                4, "The pipeline", pipeline(),
                beats = listOf(
                    Beat(4, show = setOf("t", "w", "n"), light = setOf("t"), card = BoardCard("KQL", "A table, then steps, each after a pipe, like an assembly line.")),
                ),
            ),
            // 6: "The dashboard. Tom charted the average..."
            Scene(
                6, "Culprit one: Tom", percentile(),
                beats = listOf(
                    Beat(6, show = setOf("calls", "avg"), light = setOf("avg"), card = BoardCard("Tom's average", "Under a second all evening, because an average hides the slow tail.")),
                    Beat(8, show = setOf("p95", "diner"), light = setOf("p95"), card = BoardCard("Priya's p95", "Eighteen at half a second, two at six: the p95 says six.")),
                    Beat(10, light = setOf("diner"), card = p95Code),
                ),
            ),
            // 12: "The intern. He ran a query with no time filter..."
            Scene(
                12, "Culprit two: the intern", pipeline(),
                beats = listOf(
                    Beat(12, show = setOf("t", "w", "n"), light = setOf("w"), pulse = "t" to "w", card = BoardCard("No time filter", "Four minutes through months of data. Filter on time first, always.")),
                    Beat(14, light = setOf("w"), card = lastHour),
                ),
            ),
            // 16: "Dana. She wanted the slow step inside a checkout..."
            Scene(
                16, "Culprit three: Dana", joinSpec(),
                beats = listOf(
                    Beat(16, show = setOf("req", "dep", "op"), light = setOf("req", "dep"), card = BoardCard("Joined on ResultCode", "Thousands of unrelated rows share a two hundred: everything matched.")),
                    Beat(18, light = setOf("op"), pulse = "req" to "op", card = opCard),
                    Beat(20, light = setOf("op"), card = joinCode),
                    Beat(22, show = setOf("cosmos", "sql"), light = setOf("cosmos"), pulse = "op" to "cosmos", card = guiltyCard),
                ),
            ),
            // 24: "Marcus, on the container console logs..."
            Scene(
                24, "Culprit four: Marcus", terms(),
                beats = listOf(
                    Beat(24, show = setOf("col", "con"), light = setOf("con"), card = containsCard),
                    Beat(26, show = setOf("has", "idx"), light = setOf("has", "idx"), pulse = "has" to "idx", card = hasCode),
                ),
            ),
            // 28: "Priya again, I am afraid..."
            Scene(
                28, "Culprit five: Priya", dims(),
                beats = listOf(
                    Beat(28, show = setOf("props"), light = setOf("props"), card = dimCard),
                    Beat(30, show = setOf("proj"), light = setOf("proj"), card = BoardCard("project lost it", "She picked the restaurant and dropped DurationMs, the whole point.")),
                    Beat(32, show = setOf("ext", "byrest"), light = setOf("ext"), pulse = "props" to "ext", card = dimCode),
                ),
            ),
            // 34: "With nobody on call..."
            Scene(
                34, "Nobody on call", alert(),
                beats = listOf(
                    Beat(34, show = setOf("q", "th"), light = setOf("q", "th"), pulse = "q" to "th", card = alertCode),
                    Beat(36, show = setOf("ag", "you"), light = setOf("ag"), pulse = "th" to "ag", card = agCard),
                    Beat(38, show = setOf("met"), light = setOf("met"), card = metCard),
                ),
            ),
            // 43: "So what should I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("slice", "slow", "behind"), card = phraseCard),
                    Beat(46, light = setOf("word", "keep", "email"), card = threeMore),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-kql--late-night",
        listOf(
            // 0: "Hey, you. It is late, the city has eaten..."
            Scene(
                0, "Tonight at eight", telemetry(),
                beats = listOf(
                    Beat(0, show = setOf("api", "worker"), light = setOf("api"), card = BoardCard("The Friday that went wrong", "Three hundred and ten orders failed between eight and nine.")),
                    Beat(4, show = setOf("taste", "ai", "ws"), light = setOf("ws"), pulse = "ai" to "ws", card = wsCard),
                ),
            ),
            // 6: "AppRequests: every call the Zaiqa API took..."
            Scene(
                6, "The tables", tables(),
                beats = listOf(
                    Beat(6, show = setOf("req", "dep", "exc", "tra", "con"), light = setOf("req", "dep"), card = fourTables),
                ),
            ),
            // 8: "KQL, the Kusto Query Language..."
            Scene(
                8, "A query is a pipeline", pipeline(),
                beats = listOf(
                    Beat(8, show = setOf("t", "n"), light = setOf("t"), card = BoardCard("Start with the table", "Then each step after a pipe hands its rows to the next.")),
                    Beat(10, show = setOf("w"), light = setOf("w"), pulse = "t" to "w", card = lastHour),
                ),
            ),
            // 12: "Where Name has checkout..."
            Scene(
                12, "has, or contains", terms(),
                beats = listOf(
                    Beat(12, show = setOf("col", "has"), light = setOf("has"), card = hasCode),
                    Beat(14, show = setOf("con", "idx"), light = setOf("con", "idx"), card = containsCard),
                ),
            ),
            // 16: "Summarize count by ResultCode..."
            Scene(
                16, "Counting, then drawing", pipeline(),
                beats = listOf(
                    Beat(16, show = setOf("t", "w", "s", "r", "n"), light = setOf("s", "r"), pulse = "s" to "r", card = byCode),
                    Beat(18, light = setOf("r"), card = binCard),
                ),
            ),
            // 20: "So why did the dashboard sleep through it?"
            Scene(
                20, "The average slept", percentile(),
                beats = listOf(
                    Beat(20, show = setOf("calls", "avg"), light = setOf("avg"), card = BoardCard("The average", "About one second, and it hides the slow tail at the back.")),
                    Beat(22, show = setOf("p95", "diner"), light = setOf("p95", "diner"), card = p95Code),
                ),
            ),
            // 24: "For that you join..."
            Scene(
                24, "What was actually slow", joinSpec(),
                beats = listOf(
                    Beat(24, show = setOf("req", "dep", "op"), light = setOf("op"), pulse = "req" to "op", card = opCard),
                    Beat(26, light = setOf("req"), card = notCodeCard),
                    Beat(28, show = setOf("cosmos", "sql"), light = setOf("cosmos"), pulse = "op" to "cosmos", card = joinCode),
                ),
            ),
            // 30: "It did warn us. One more question: which restaurant?"
            Scene(
                30, "Which restaurant", dims(),
                beats = listOf(
                    Beat(32, show = setOf("props"), light = setOf("props"), card = dimCard),
                    Beat(34, show = setOf("ext", "proj"), light = setOf("ext"), pulse = "props" to "ext", card = dimCode),
                    Beat(36, show = setOf("byrest"), light = setOf("byrest"), card = byRestCard),
                ),
            ),
            // 38: "We do not, because the query became a log search alert rule..."
            Scene(
                38, "Nobody wakes you", alert(),
                beats = listOf(
                    Beat(38, show = setOf("q", "th"), light = setOf("q", "th"), pulse = "q" to "th", card = alertCode),
                    Beat(40, show = setOf("ag", "you"), light = setOf("ag"), pulse = "th" to "ag", card = agCard),
                    Beat(42, show = setOf("met"), light = setOf("met"), card = metCard),
                ),
            ),
            // 45: "So what would the exam want from tonight?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("slice", "slow", "behind"), card = phraseCard),
                    Beat(48, light = setOf("word", "keep", "email"), card = threeMore),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-kql--couples",
        listOf(
            // 0: "Tonight is date night, love..."
            Scene(
                0, "Date night", telemetry(),
                beats = listOf(
                    Beat(0, show = setOf("api", "worker"), light = setOf("api"), card = BoardCard("Friday, eight o'clock", "Three hundred and ten failed orders, and a dashboard that slept.")),
                    Beat(2, show = setOf("taste", "ai", "ws"), light = setOf("ws"), pulse = "ai" to "ws", card = wsCard),
                ),
            ),
            // 3: "Tables. AppRequests for calls the API took..."
            Scene(
                3, "The tables", tables(),
                beats = listOf(
                    Beat(4, show = setOf("req", "dep", "exc", "tra"), light = setOf("req", "dep"), card = fourTables),
                ),
            ),
            // 6: "Now my turn to be strict. Exam style..."
            Scene(
                6, "Average, or percentile", percentile(),
                beats = listOf(
                    Beat(6, show = setOf("calls", "avg"), light = setOf("avg"), card = BoardCard("The exam question", "The dashboard charted the average per hour and saw nothing.")),
                    Beat(8, show = setOf("p95", "diner"), light = setOf("p95"), card = BoardCard("p95", "Eighteen at half a second, two at six: the p95 says six seconds.")),
                    Beat(10, light = setOf("diner"), card = p95Code),
                ),
            ),
            // 11: "The first step of any query: what is it?"
            Scene(
                11, "Filter on time first", pipeline(),
                beats = listOf(
                    Beat(12, show = setOf("t", "w", "n"), light = setOf("w"), pulse = "t" to "w", card = lastHour),
                ),
            ),
            // 14: "Where Name has checkout..."
            Scene(
                14, "has, or contains", terms(),
                beats = listOf(
                    Beat(14, show = setOf("col", "has", "con", "idx"), light = setOf("has", "idx"), pulse = "has" to "idx", card = hasCode),
                    Beat(16, light = setOf("con"), card = byCode),
                ),
            ),
            // 17: "No. For that you join two tables on something they share."
            Scene(
                17, "Join on what", joinSpec(),
                beats = listOf(
                    Beat(18, show = setOf("req", "dep", "op"), light = setOf("op"), pulse = "req" to "op", card = opCard),
                    Beat(20, light = setOf("req"), card = joinCode),
                    Beat(22, show = setOf("cosmos", "sql"), light = setOf("cosmos"), pulse = "op" to "cosmos", card = guiltyCard),
                ),
            ),
            // 24: "Now that is my favourite part..."
            Scene(
                24, "Extend, or project", dims(),
                beats = listOf(
                    Beat(24, show = setOf("props"), light = setOf("props"), card = dimCard),
                    Beat(26, show = setOf("proj"), light = setOf("proj"), card = BoardCard("Not project", "It drops DurationMs, the thing you came for.")),
                    Beat(28, show = setOf("ext", "byrest"), light = setOf("ext", "byrest"), pulse = "props" to "ext", card = dimCode),
                ),
            ),
            // 30: "Eating. The query became a log search alert rule..."
            Scene(
                30, "Dinner, not a chart", alert(),
                beats = listOf(
                    Beat(30, show = setOf("q", "th"), light = setOf("q", "th"), pulse = "q" to "th", card = alertCode),
                    Beat(32, show = setOf("ag", "you"), light = setOf("ag"), pulse = "th" to "ag", card = agCard),
                    Beat(34, show = setOf("met"), light = setOf("met"), card = metCard),
                ),
            ),
            // 39: "Then give me the exam list..."
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("slice", "slow", "behind"), card = BoardCard("The exam list", "Per five-minute slice, what slow feels like, the step behind it.")),
                    Beat(42, light = setOf("word", "keep", "email"), card = threeMore),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
