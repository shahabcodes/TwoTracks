package com.shahaabapps.core.content

/** Tracing with OpenTelemetry: following a request across services: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesOpenTelemetry {
    private val classic = Podcast(
        "ai-opentelemetry",
        listOf(
            maya("Hi, and welcome back. Zaiqa is running. The Zaiqa API, the .NET service the phone talks to, lives in Container Apps. Orders sit in Azure SQL, and the order worker takes paid orders off the queue. Today: watching one order cross all of it."),
            leo("Watching it how? I can already read the logs from the API."),
            maya("You can, and that is the problem. A diner taps Pay, and eleven seconds later the screen says ordered. The API's log says it was fine. The worker's log says it was fine. Everything was fine, and it still took eleven seconds."),
            leo("So one of them is lying."),
            maya("Nobody is lying. Each program only sees its own small part. There is an open standard for stitching the parts together, and it is called OpenTelemetry. What it gives you for one order is called a trace."),
            leo("A trace being?"),
            maya("Everything that happened for that one order, in order, from the tap on the phone to the restaurant's tablet. Picture a parcel with one tracking number, scanned at every depot. Same parcel, many scans, one printout."),
            leo("And the tracking number?"),
            maya("A trace id: a long random number made when the request first arrives, and carried to every program it touches. Each program writes it down with its own work, so Azure can put the pieces back into one story."),
            leo("And each scan on that printout is one piece of work?"),
            maya("Yes, and the word for it is a span: one timed piece of work, with a name, a start, an end and a parent. Handling the pay call is a span. The insert into Azure SQL is a span. The worker's work is a span."),
            leo("Why does a span need a parent?"),
            maya("Because spans make a tree, not a list. The pay call is the parent; the SQL insert and the queue send are its children. That is how you see the API sitting idle while something underneath it was slow."),
            leo("Fine. How does the next program learn the trace id?"),
            maya("Over HTTP it travels in a header called traceparent, which is a web standard, so every language agrees on it. One line of text: a version, the trace id, the id of the span doing the calling, and a flag saying whether this trace is being kept."),
            leo("And something writes that header for me?"),
            maya("The HTTP library does, once it is instrumented. The caller writes the header, the receiver reads it and carries on the same trace instead of starting a fresh one. That is all anyone means by context propagation."),
            leo("But the worker never gets an HTTP call. It takes a message off the orders queue, maybe a minute later."),
            maya("Good catch, and this is the part people get wrong. A queue message carries labels beside its body, and the trace context rides in one of them. The sending library writes it, the receiving library reads it, and the worker's work ties back to that send."),
            leo("So it lands in the same trace, a minute late."),
            maya("A minute late, and the gap on the picture is real. Our eleven seconds were nine spent waiting in the queue at dinner rush, and two spent working. Three separate logs would never have shown that."),
            leo("[sigh] I have lost evenings to worse. How much of this do I write myself?"),
            maya("Almost none. Microsoft ships a distro: one package that bundles the standard, the part that sends your data to Azure, and a pile of ready-made instrumentation. In Python you install azure-monitor-opentelemetry and call configure azure monitor once, before the app starts serving."),
            leo("Our API is .NET, though."),
            maya("Then it is the same idea with a different name: add the Azure Monitor package and call use Azure Monitor while the app is being built. Zaiqa's Taste service, the Python one, calls configure azure monitor. Both send to the same Application Insights, where Azure keeps all this and draws it."),
            leo("And what gets instrumented without me asking?"),
            maya("The web framework, so every incoming call becomes a span. The outgoing HTTP client, so every call to another service becomes a span with traceparent attached. And the Azure libraries, so the queue send and the Cosmos read turn up as well."),
            leo("What about our Azure SQL queries? That is where the orders live."),
            maya("Turn on the SQL client instrumentation and each query becomes its own span, with the statement on it. For example, our slow checkout turned out to be one query reading an entire menu table for a restaurant with nine hundred dishes."),
            leo("And when I want to time my own code?"),
            maya("You write a custom span. Ask the library for a tracer, wrap the part you care about, name it something like price the basket, and hang attributes on it: the restaurant id, the surge fee, the city. An attribute is just a label you can search for later."),
            leo("Why bother? The framework already times the whole call."),
            maya("Because the whole call tells you nothing. Say you're building a checkout that takes two seconds: was it the pricing, the payment provider, or the save? One custom span around each, and the answer is on the screen."),
            leo("What about my own log lines? I have hundreds of them."),
            maya("They come along too. The distro sends three kinds of telemetry, which just means measurements a program publishes about itself: traces, metrics and logs. A log line written while a span is open carries that trace id, so your message sits in the timeline beside the database call."),
            leo("And metrics are numbers?"),
            maya("Numbers over time: calls a minute, how long they took, how much memory. You can add your own counter, say orders paid, so the number you care about lives beside the traces."),
            leo("This must make a frightening amount of data."),
            maya("It does, and you pay by the gigabyte you send. Which brings us to sampling: keeping only some traces and throwing the rest away. Recent versions of the distro keep about five traces a second by default, and they keep whole traces, never half a journey."),
            leo("And if I turn it down to one in ten?"),
            maya("Your bill falls to roughly a tenth, and the portal knows the rate, so counts and failure rates are scaled back up and still read correctly. What you lose is the particular one. The angry diner's order at ten past eight may simply not be there."),
            leo("So what sampling costs me is the trace I most wanted."),
            maya("That is the trade. Keep more while you are hunting something, less when it is quiet. [pause] And before you turn it down, throw away the rubbish instead."),
            leo("What rubbish?"),
            maya("Health checks. Container Apps calls a small health URL on every copy of the API every few seconds, all day, and not one of those traces will ever be read. There is an excluded URLs setting: name that path and the instrumentation skips it."),
            leo("Better than sampling, then."),
            maya("Much better, because it is aimed. You drop what is worthless and keep every real order. Sampling cuts blindly. [pause] One last thing: the map Application Insights draws, a box for each of your services with arrows between them."),
            leo("How does it know which box is which?"),
            maya("By the cloud role name: the name a service calls itself. You set it once per service, with a setting named OTEL service name: zaiqa api, order worker, taste service. Leave it at the default and all three collapse into one confused box."),
            leo("Right. So what do I remember for the exam?"),
            maya("If the next service's work shows up as a separate trace, the traceparent header did not travel. If two services are one blob on the map, set the service name on each, which becomes the cloud role name."),
            leo("Keep going."),
            maya("If one call sets up traces, metrics and logs in Python, that is configure azure monitor, and the connection string comes from an environment variable, never the source. If the bill is too high, that is sampling. And to time one function, a custom span with an attribute."),
            leo("And something to try today?"),
            maya("Create a workspace-based Application Insights resource and copy its connection string. Install the distro in a tiny FastAPI app, call configure azure monitor, and add one custom span with an attribute. Hit it a few times, then open transaction search and read the end-to-end view of one request. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-opentelemetry",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Eleven Second Order. A diner taps Pay in the Zaiqa app, the screen sits there for eleven seconds, and then it says ordered. Every log in the system swears nothing was wrong."),
            leo("Detective Leo, reporting. The Zaiqa API, the .NET service the phone talks to, said it was fine?"),
            maya("Fine, and fast. Forty milliseconds. The order worker, which takes paid orders off the queue, also says fine. Azure SQL says fine. Three witnesses, three alibis, and eleven seconds nobody will admit to."),
            leo("Then somebody is lying, and I say it is the database."),
            maya("That is the junior's answer, and it is wrong. Nobody is lying: each program only sees its own small part. The case needs one record of the whole journey, which the open standard called OpenTelemetry gives you, and which is called a trace."),
            leo("Define a trace for the file."),
            maya("Everything that happened for one order, in order, from the tap on the phone to the restaurant's tablet. Picture a parcel with one tracking number, scanned at every depot. Same parcel, many scans, one printout."),
            leo("And the tracking number is evidence bag one."),
            maya("The trace id: a long random number made when the request arrives, carried to every program it touches. Each one writes it down beside its own work, so Azure can put the pieces back into one story."),
            leo("And each scan on the printout is?"),
            maya("A span: one timed piece of work, with a name, a start, an end and a parent. The pay call is a span. The insert into Azure SQL is a span. The worker's handling of the message is a span."),
            leo("Why would a span need a parent?"),
            maya("Because spans make a tree, not a list. The pay call is the parent; the SQL insert and the queue send are its children. Which is how we saw, at last, that the API was idle and something under it was slow."),
            leo("Then let me see the printout."),
            maya("That is clue one. There was no printout. The API's spans were one trace and the worker's were another, with nothing joining them. Over HTTP the trace id travels in a header called traceparent, a web standard: a version, the trace id, the calling span's id, and a flag."),
            leo("So somebody stripped the header."),
            maya("Over HTTP nobody did. But the worker is not called over HTTP. It takes a message off the orders queue, and a queue message carries labels beside its body, where the trace context should ride."),
            leo("And it was not riding."),
            maya("Our first version built the message by hand with a helper nobody had instrumented, so no label was written. The receiving library found nothing to read, and started a fresh trace. Two islands instead of one journey."),
            leo("[sigh] And with the label put back?"),
            maya("One picture, and the answer in it. Nine of the eleven seconds were the message sitting in the queue at dinner rush. Two were real work. No log could have shown that, because waiting is nobody's log line."),
            leo("Case nearly closed. But there are two more files on the desk."),
            maya("There are. The second: the map. Application Insights draws a box for each of your services with arrows between them, and ours had one box holding all three."),
            leo("Three services pretending to be one. Why?"),
            maya("Because none of them had been named. The cloud role name is the name a service calls itself, and you set it once per service with a setting named OTEL service name. Zaiqa api, order worker, taste service, and the map came apart properly."),
            leo("Noted. And the third file?"),
            maya("The one I like least. An angry diner rang about an order at ten past eight, we went looking for its trace, and it was not there at all."),
            leo("Deleted?"),
            maya("Never stored. That is sampling: keeping only some traces and throwing the rest away, to keep the bill down. You pay by the gigabyte you send, and recent versions of the distro keep about five traces a second by default."),
            leo("The distro being?"),
            maya("One package that bundles the standard, the part that sends your data to Azure, and a pile of ready-made instrumentation. In Python you install it and call configure azure monitor; in .NET you add the Azure Monitor package and call use Azure Monitor."),
            leo("And sampling throws away whole requests?"),
            maya("Whole traces, decided once at the start and carried in that flag on the traceparent header. You never get half a journey with a hole in the middle. But the one trace you want may be the one that went."),
            leo("Then we turn sampling off."),
            maya("And triple the bill. Throw away the rubbish first. Container Apps calls a small health URL on every copy of the API every few seconds, all day, and nobody will ever read those traces. An excluded URLs setting drops them by name."),
            leo("Aimed, rather than blind. What else is on the picture now?"),
            maya("Every Azure SQL query, once the SQL client instrumentation is on, with the statement attached. That is how we found a second problem: one query reading an entire menu table for a restaurant with nine hundred dishes."),
            leo("And the parts that are only our code? The pricing?"),
            maya("A custom span. Ask the library for a tracer, wrap the part you care about, name it price the basket, and hang attributes on it: the restaurant id, the surge fee, the city. An attribute is a label you can search for later."),
            leo("Say you're building a two-second checkout. That tells you which second."),
            maya("Exactly that. And your own log lines come along too, because the distro sends three kinds of telemetry, meaning measurements a program publishes about itself: traces, metrics and logs."),
            leo("Metrics being numbers over time?"),
            maya("Calls a minute, how long they took, how much memory. A log line written while a span is open carries that trace id, so your own message sits in the timeline beside the database call."),
            leo("[chuckle] That would have saved me a week of printing timestamps."),
            maya("It saves everybody a week eventually. One picture, every hop, with the waiting drawn as waiting."),
            leo("Case closed. What goes in the summary for the exam?"),
            maya("If the next service's work shows up as a separate trace, the traceparent header did not travel, and over a queue that means the context label. If two services are one blob on the map, set the service name, which becomes the cloud role name."),
            leo("And the rest of the file?"),
            maya("If one call sets up traces, metrics and logs in Python, that is configure azure monitor, and the connection string comes from an environment variable, never the source. If the bill is too high, that is sampling. To time one function, a custom span with an attribute."),
            leo("And something to try today, before the next case?"),
            maya("Create a workspace-based Application Insights resource and copy its connection string. Install the distro in a tiny FastAPI app, call configure azure monitor, and add one custom span with an attribute. Hit it a few times, then open transaction search and read the end-to-end view of one request. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-opentelemetry",
        listOf(
            maya("Okay, did you hear what the Zaiqa team did to the checkout this week? Sit down. They spent four days arguing about which service was slow, and none of them could prove a thing. Eleven seconds from Pay to ordered, and every log saying fine."),
            leo("I heard shouting about a header. Start at the beginning. Why could nobody prove it?"),
            maya("Because each program only sees its own part. The Zaiqa API, the .NET service the phone talks to, saw forty milliseconds. The order worker saw two seconds. Azure SQL saw nothing worth mentioning."),
            leo("And the other nine seconds?"),
            maya("Nobody's. That is the whole problem, and the fix is an open standard called OpenTelemetry, which gives you one record of the whole journey. That record is called a trace."),
            leo("Which holds what, exactly?"),
            maya("Everything that happened for one order, in order, from the tap on the phone to the restaurant's tablet. Picture a parcel with one tracking number, scanned at every depot. Same parcel, many scans, one printout."),
            leo("Right. And Monday?"),
            maya("Monday was Tom. Tom added the distro to the API: one package that bundles the standard, the part that sends your data to Azure, and a pile of ready-made instrumentation. He called configure azure monitor at the bottom of the file."),
            leo("[chuckle] After the app was already serving."),
            maya("After it was already serving. So the first few hundred requests had no spans at all. A span, the word for one timed piece of work, with a name, a start, an end and a parent."),
            leo("And Tom thought the first requests were simply fast."),
            maya("He did, for a morning. The call goes before the app starts serving, and spans make a tree: the pay call is the parent, the SQL insert and the queue send are its children. That is how you see the API idle while something under it is slow."),
            leo("Tuesday. Go on."),
            maya("Tuesday was the intern, and this is the famous one. The API's work and the worker's work came out as two separate traces, with nothing joining them."),
            leo("Because?"),
            maya("Over HTTP the trace id travels in a header called traceparent, a web standard: a version, the trace id, the calling span's id, and a flag saying whether this trace is kept. The intern's outgoing call went through a helper nobody had instrumented, so no header was written."),
            leo("And the receiving side starts a fresh trace."),
            maya("Every time. Same story on the queue, which is where it actually bit. A queue message carries labels beside its body, the trace context rides in one of them, and the intern's hand-built message had none."),
            leo("So that is where the nine seconds were hiding."),
            maya("That is exactly where. Once the label was written, the picture showed nine seconds of the message simply sitting in the orders queue at dinner rush. Waiting is nobody's log line, which is why four days of reading logs found nothing."),
            leo("Wednesday, then."),
            maya("Dana. Dana opened the map, the picture Application Insights draws with a box for each of your services and arrows between them, and found one box with all three services inside it."),
            leo("One blob. Why?"),
            maya("Nobody had named anything. The cloud role name is the name a service calls itself, set once per service with a setting named OTEL service name. Zaiqa api, order worker, taste service, and by lunch the map made sense."),
            leo("Thursday. I can feel it getting expensive."),
            maya("Marcus. Marcus got the whole thing working on his laptop by pasting the Application Insights connection string straight into the source, and pushing it."),
            leo("[sigh] Into the repository."),
            maya("Into the repository. It reads from an environment variable now, which is what production should always do, and the old string has been rotated. Marcus bought biscuits."),
            leo("And Friday? Friday is always the good one."),
            maya("Friday was Priya, and an angry diner who rang about an order at ten past eight. Priya went looking for its trace and it was not there at all."),
            leo("Someone deleted it?"),
            maya("Never stored. Sampling: keeping only some traces and throwing the rest away, because you pay by the gigabyte you send. Recent versions of the distro keep about five traces a second by default, and nobody had read that line."),
            leo("Whole traces, or odd spans here and there?"),
            maya("Whole traces, decided once at the start and carried in that flag on the traceparent header. Never half a journey with a hole in it. But the one trace you want may be the one that went."),
            leo("And Priya's answer was to turn it off."),
            maya("For about an hour, until the bill was estimated. Then she did the sensible thing: Container Apps calls a small health URL on every copy of the API every few seconds, all day, and an excluded URLs setting drops those by name. Aimed, not blind."),
            leo("Did anything actually go right this week?"),
            maya("Plenty. The SQL client instrumentation went on, so every Azure SQL query is now its own span with the statement attached, and it caught a query reading an entire menu table for a restaurant with nine hundred dishes."),
            leo("And the parts that are only our code?"),
            maya("Dana's custom spans. Ask the library for a tracer, wrap the part you care about, name it price the basket, and hang attributes on it: the restaurant id, the surge fee, the city. An attribute is a label you can search for later."),
            leo("Say you're building a two-second checkout, and you want to know which second."),
            maya("That is the one. And the log lines came along free, because the distro sends three kinds of telemetry, meaning measurements a program publishes about itself: traces, metrics and logs."),
            leo("Metrics being?"),
            maya("Numbers over time: calls a minute, how long they took, how much memory. A log line written while a span is open carries the trace id, so Dana's own messages sit in the timeline beside the database call."),
            leo("So the week ended well."),
            maya("The week ended with one picture of one order, nine seconds of it drawn as waiting. [pause] Tom has framed it."),
            leo("So what is the gossip I should remember for the exam?"),
            maya("If the next service's work shows up as a separate trace, the traceparent header did not travel, and over a queue that means the context label. If services are one blob on the map, set the service name, which becomes the cloud role name."),
            leo("And what about Tom, then Marcus, then Priya?"),
            maya("Set up before the app serves. The connection string comes from an environment variable, never the source. One call for traces, metrics and logs in Python is configure azure monitor. A missing trace with the bill under control is sampling."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a workspace-based Application Insights resource and copy its connection string. Install the distro in a tiny FastAPI app, call configure azure monitor, and add one custom span with an attribute. Hit it a few times, then open transaction search and read the end-to-end view of one request. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-opentelemetry",
        listOf(
            nova("Hey, you. It is late, the kitchen has shut, and somewhere out there one Zaiqa order is still crawling through the system. [pause] Tonight we follow it, all the way, and nobody is allowed to say it was fine."),
            ivy("Eleven seconds, was it not? From Pay to ordered."),
            nova("Eleven. And the Zaiqa API, the .NET service the phone talks to, swears it took forty milliseconds. The order worker swears it took two seconds. Azure SQL has nothing to confess. [pause] And yet."),
            ivy("Somebody is being economical with the truth."),
            nova("Nobody is. Each program only sees its own small part. What we need is one record of the whole journey, and there is an open standard for it, called OpenTelemetry. The record it makes for one order is called a trace."),
            ivy("Say that slowly. A trace is?"),
            nova("Everything that happened for that one order, in order, from the tap on the phone to the restaurant's tablet. [pause] Picture a parcel with one tracking number, scanned at every depot. Same parcel, many scans, one printout."),
            ivy("And the tracking number travels with it."),
            nova("The trace id: a long random number made when the request first arrives, carried to every program it touches. Each one writes it beside its own work, so Azure can put the story back together afterwards."),
            ivy("And one scan on that printout is one little piece of work."),
            nova("It is called a span: one timed piece of work, with a name, a start, an end and a parent. The pay call is a span. The insert into Azure SQL is a span. The worker's work is a span."),
            ivy("Why does it want a parent? It is late for family trees."),
            nova("[chuckle] Because spans make a tree, not a list. The pay call is the parent; the SQL insert and the queue send are its children. That is how you catch the API sitting idle while something underneath it takes its time."),
            ivy("And how does the next program hear about the trace id?"),
            nova("Over HTTP it rides in a header called traceparent, which is a web standard, so every language agrees on it. A version, the trace id, the id of the span doing the calling, and a flag saying whether this trace is being kept."),
            ivy("Who writes that header? Please do not say me."),
            nova("The HTTP library, once it is instrumented. The caller writes it, the receiver reads it and carries on the same trace instead of starting a new one. [pause] That is all anybody means by context propagation."),
            ivy("Mm. But your worker does not get a call. It takes a message off a queue, long after everyone has gone home."),
            nova("Which is the lovely part. A queue message carries labels beside its body, and the trace context rides in one of them. The sending library writes it, the receiving library reads it, and the worker's work ties back to that send."),
            ivy("So it joins the same trace, a minute late."),
            nova("A minute late, and the gap is drawn as a gap. Nine of our eleven seconds were the message waiting in the orders queue at dinner rush. [pause] Waiting is nobody's log line. That is why four days of reading logs found nothing."),
            ivy("And how much of this do you write yourself, at this hour?"),
            nova("Almost none. There is a distro: one package that bundles the standard, the part that sends your data to Azure, and a pile of ready-made instrumentation. In Python you install it and call configure azure monitor, once, before the app starts serving."),
            ivy("And the .NET side of the house?"),
            nova("Add the Azure Monitor package and call use Azure Monitor while the app is being built. Both land in the same Application Insights, where Azure keeps all this and draws it for you."),
            ivy("What does it instrument while I am not looking?"),
            nova("The web framework, so every incoming call is a span. The outgoing HTTP client, so every call out is a span with traceparent attached. And the Azure libraries, so the queue send and the Cosmos read appear too."),
            ivy("And the database? Zaiqa's orders are all in Azure SQL."),
            nova("Turn on the SQL client instrumentation and each query is its own span, statement and all. [pause] Ours confessed to reading an entire menu table for a restaurant with nine hundred dishes."),
            ivy("Nine hundred dishes. And the pieces that are only yours?"),
            nova("A custom span. Ask the library for a tracer, wrap the part you care about, name it price the basket, and hang attributes on it: the restaurant id, the surge fee, the city. An attribute is just a label you can search for later."),
            ivy("Why, if the framework already times the whole call?"),
            nova("Because the whole call tells you nothing. Say you're building a checkout that takes two seconds: the pricing, the payment provider, or the save? One custom span around each, and the screen answers you."),
            ivy("And my own log lines, the ones I write at midnight?"),
            nova("They come along. The distro sends three kinds of telemetry, which means measurements a program publishes about itself: traces, metrics and logs. A log line written while a span is open carries that trace id, and sits in the timeline beside the database call."),
            ivy("Metrics meaning numbers over time, I assume."),
            nova("Calls a minute, how long they took, how much memory. [pause] And you can add your own counter, say orders paid, so the number you care about lives beside the traces."),
            ivy("This all sounds expensive."),
            nova("You pay by the gigabyte you send, so there is sampling: keeping only some traces and throwing the rest away. Recent versions keep about five traces a second by default, and they keep whole traces, never half a journey."),
            ivy("And at one in ten?"),
            nova("Your bill falls to about a tenth, and the portal knows the rate, so the counts and failure rates still read correctly. What you lose is the particular one. The angry diner's order at ten past eight may simply not be there."),
            ivy("[sigh] Which is always the one somebody rings about."),
            nova("Always. So before you turn sampling down, throw away the rubbish. Container Apps calls a small health URL on every copy of the API every few seconds, all night, and nobody will ever read those. An excluded URLs setting drops them by name."),
            ivy("Aimed, not blind. And the map you keep mentioning?"),
            nova("A box for each of your services, with arrows between them. It knows which box is which from the cloud role name, the name a service calls itself, set with a setting named OTEL service name. Leave it at the default and all three become one confused box."),
            ivy("Mm. Before I fall asleep on you. What do I remember for the exam?"),
            nova("If the next service's work shows up as a separate trace, the traceparent header did not travel, and over a queue that means the context label. If two services are one blob on the map, set the service name, which becomes the cloud role name."),
            ivy("And the gentler ones?"),
            nova("One call for traces, metrics and logs in Python is configure azure monitor. The connection string comes from an environment variable, never the source. A bill too high is sampling. And to time one function, a custom span with an attribute."),
            ivy("And something to try today, before bed?"),
            nova("Create a workspace-based Application Insights resource and copy its connection string. Install the distro in a tiny FastAPI app, call configure azure monitor, and add one custom span with an attribute. Hit it a few times, then open transaction search and read the end-to-end view of one request. Goodnight, you."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-opentelemetry",
        listOf(
            nova("Tonight is date night, darling, and I have brought you a mystery. One Zaiqa order, eleven seconds from Pay to ordered, and three services all swearing they were quick about it."),
            rafe("The Zaiqa API, the .NET service the phone talks to, says forty milliseconds. The worker says two seconds. Somebody is holding something back."),
            nova("Nobody is holding anything back, love. Each of them only sees its own small part. So quiz me, and be strict: what gives you the whole journey?"),
            rafe("An open standard called OpenTelemetry, and what it makes for one order is called a trace. Everything that happened, in order, from the tap on the phone to the restaurant's tablet."),
            nova("Right answer, and beautifully put. [kiss]"),
            rafe("Picture a parcel with one tracking number, scanned at every depot. Same parcel, many scans."),
            nova("And the tracking number is the trace id: a long random number made when the request arrives, carried to every program it touches. Each one writes it down beside its own work."),
            rafe("And one scan is one piece of work. What is the word?"),
            nova("A span: one timed piece of work, with a name, a start, an end and a parent. The pay call is a span, the insert into Azure SQL is a span, the worker's work is a span."),
            rafe("Parents. On a date night."),
            nova("[chuckle] Spans make a tree, not a list. The pay call is the parent, the SQL insert and the queue send are its children, and that is how you catch the API lying idle while something underneath takes its time."),
            rafe("Then my turn to be strict. Exam style, darling. The API's work is one trace and the worker's is another, with nothing joining them. What is missing, and where does it live?"),
            nova("Over HTTP, the trace id rides in a header called traceparent, a web standard: a version, the trace id, the calling span's id, and a flag. If it is missing, the receiver starts a fresh trace instead of carrying on."),
            rafe("Right answer, and quick with it. [kiss]"),
            nova("Though you only asked me half the question, and the other half is the interesting one. Our worker is never called over HTTP."),
            rafe("It takes a message off the orders queue. A minute later, sometimes."),
            nova("So where does the trace context ride then, clever one?"),
            rafe("In a label beside the message body. The sending library writes it, the receiving library reads it, and the worker's work ties back to that send instead of floating off alone."),
            nova("Right answer. Always knowing where you came from. [kiss]"),
            rafe("And that is where your nine seconds were."),
            nova("Nine of the eleven, the message simply sitting in the queue at dinner rush. Waiting is nobody's log line, which is why four days of reading logs found nothing at all."),
            rafe("So how much of this do we write ourselves? Honestly."),
            nova("Almost none. There is a distro: one package that bundles the standard, the part that sends your data to Azure, and a pile of ready-made instrumentation. In Python you install it and call configure azure monitor, before the app starts serving."),
            rafe("And our .NET side?"),
            nova("Add the Azure Monitor package and call use Azure Monitor while the app is built. Both send to the same Application Insights, where Azure keeps all this and draws it. Now, what does it instrument for you?"),
            rafe("The web framework for calls coming in, the HTTP client for calls going out, with traceparent attached, and the Azure libraries, so the queue send and the Cosmos read appear."),
            nova("Right answer, and you did not even pause. [kiss]"),
            rafe("And Zaiqa's orders are in Azure SQL. Do the queries show?"),
            nova("Once the SQL client instrumentation is on, each query is its own span with the statement on it. Ours confessed to reading an entire menu table for a restaurant with nine hundred dishes."),
            rafe("And the parts that are only ours? The pricing, the surge fee?"),
            nova("A custom span. Ask the library for a tracer, wrap the part you care about, name it price the basket, and hang attributes on it: the restaurant id, the city. An attribute is just a label you can search for later."),
            rafe("Say you're building a checkout that takes two seconds. One span each, and you know which second was yours."),
            nova("Exactly. And your own log lines come too, because the distro sends three kinds of telemetry, meaning measurements a program publishes about itself: traces, metrics and logs."),
            rafe("Metrics being numbers over time. Calls a minute, how long they took."),
            nova("And a log line written while a span is open carries that trace id, so your message sits in the timeline beside the database call. Now a hard one, love: the bill."),
            rafe("You pay by the gigabyte you send. So there is sampling: keeping only some traces and throwing the rest away. Recent versions keep about five traces a second by default."),
            nova("Right answer. Economical, like you. [kiss]"),
            rafe("Half a journey, though? Spans going missing from the middle?"),
            nova("Never. Whole traces, decided once at the start and carried in that flag on the traceparent header. What you lose at one in ten is the particular one, and the portal still scales the counts back up."),
            rafe("So the angry diner at ten past eight may not be there."),
            nova("Which is why you throw away the rubbish first. Container Apps calls a small health URL on every copy of the API every few seconds, all day. Those traces are worthless. What drops them?"),
            rafe("Sampling, turned right down."),
            nova("[sigh] No kiss for that one. Sampling cuts blindly. An excluded URLs setting names that path and the instrumentation skips it: aimed, not blind, and every real order kept."),
            rafe("Aimed, not blind. Let me earn it back, then. On the map Application Insights draws, three services came out as one box. The fix is the cloud role name, the name a service calls itself, set with a setting named OTEL service name."),
            nova("Right answer, and a lovely recovery. [kiss]"),
            rafe("Then the whole household, put together."),
            nova("One trace for one order: the tap, the API, the Azure SQL insert, the queue send, the worker a minute later. Custom spans where we care, logs and metrics beside them, health checks excluded, and every service named."),
            rafe("And what do we remember for the exam, darling?"),
            nova("A separate trace for the next service means the traceparent header did not travel, and over a queue that means the context label. One blob on the map means the service name, which is the cloud role name. One call in Python is configure azure monitor."),
            rafe("And the two that caught us?"),
            nova("The connection string comes from an environment variable, never the source. And a trace that was never stored is sampling. [pause] Now, something to try today?"),
            rafe("Create a workspace-based Application Insights resource and copy its connection string. Install the distro in a tiny FastAPI app, call configure azure monitor, and add one custom span with an attribute. Hit it a few times, then open transaction search and read the end-to-end view of one request. Goodnight, everyone."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun journey() = DiagramSpec(
        "One order crosses the phone, the Zaiqa API, Azure SQL, the orders queue and the worker: one trace holds the lot.",
        nodes = listOf(
            DNode("phone", "Zaiqa app", 0.22f, 0.16f, w = 0.38f, h = 0.2f, sub = "the diner taps Pay"),
            DNode("api", "Zaiqa API", 0.76f, 0.16f, w = 0.4f, h = 0.2f, tone = 1, sub = "Container Apps"),
            DNode("queue", "Orders queue", 0.5f, 0.48f, w = 0.42f, h = 0.16f, kind = NodeKind.PILL, tone = 2, sub = "nine seconds"),
            DNode("sql", "Azure SQL", 0.22f, 0.8f, w = 0.38f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "the order row"),
            DNode("worker", "Order worker", 0.76f, 0.8f, w = 0.4f, h = 0.2f, sub = "a minute later"),
        ),
        edges = listOf(
            DEdge("phone", "api", "pay"),
            DEdge("api", "sql", "insert"),
            DEdge("api", "queue", "send"),
            DEdge("queue", "worker", "receive"),
        ),
        aspect = 0.8f,
    )

    private fun spans() = DiagramSpec(
        "The spans of one trace make a tree: the pay call is the parent, and the work underneath it are its children.",
        nodes = listOf(
            DNode("trace", "One trace", 0.5f, 0.14f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "one order, end to end"),
            DNode("s1", "Pay call", 0.17f, 0.5f, w = 0.3f, h = 0.2f, sub = "the parent"),
            DNode("s2", "SQL insert", 0.5f, 0.5f, w = 0.3f, h = 0.2f, sub = "child span"),
            DNode("s3", "Queue send", 0.83f, 0.5f, w = 0.3f, h = 0.2f, sub = "child span"),
            DNode("s4", "Worker span", 0.5f, 0.84f, w = 0.4f, h = 0.2f, tone = 2, sub = "same trace id"),
        ),
        edges = listOf(
            DEdge("trace", "s1"),
            DEdge("s1", "s2"),
            DEdge("s1", "s3"),
            DEdge("s3", "s4"),
        ),
        aspect = 0.8f,
    )

    private fun propagate() = DiagramSpec(
        "The trace context travels in the traceparent header over HTTP, and in a label on the queue message to the worker.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.24f, 0.16f, w = 0.4f, h = 0.2f, tone = 1, sub = "starts the span"),
            DNode("hdr", "traceparent", 0.74f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "trace id, span id"),
            DNode("msg", "Queue message", 0.24f, 0.76f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "a label by the body"),
            DNode("worker", "Order worker", 0.74f, 0.76f, w = 0.44f, h = 0.2f, tone = 2, sub = "joins the trace"),
        ),
        edges = listOf(
            DEdge("api", "hdr", "adds"),
            DEdge("api", "msg", "sends"),
            DEdge("msg", "worker", "reads it"),
        ),
        aspect = 0.8f,
    )

    private fun distro() = DiagramSpec(
        "One call at start-up brings the instrumentation and sends traces, metrics and logs to Application Insights.",
        nodes = listOf(
            DNode("call", "One call at start-up", 0.5f, 0.13f, w = 0.62f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "the distro"),
            DNode("web", "Web framework", 0.18f, 0.48f, w = 0.32f, h = 0.22f, sub = "calls in"),
            DNode("http", "HTTP client", 0.5f, 0.48f, w = 0.28f, h = 0.22f, sub = "calls out"),
            DNode("azure", "Azure libraries", 0.82f, 0.48f, w = 0.32f, h = 0.22f, sub = "queue, Cosmos"),
            DNode("appi", "App Insights", 0.5f, 0.85f, w = 0.56f, h = 0.2f, tone = 2, sub = "traces, metrics, logs"),
        ),
        edges = listOf(
            DEdge("call", "web"),
            DEdge("call", "http"),
            DEdge("call", "azure"),
            DEdge("call", "appi", "sends"),
        ),
        aspect = 0.8f,
    )

    private fun custom() = DiagramSpec(
        "A custom span times the part of the code you care about, and attributes hang searchable labels on it.",
        nodes = listOf(
            DNode("req", "Pay request", 0.5f, 0.14f, w = 0.5f, h = 0.2f, tone = 1, sub = "the server span"),
            DNode("price", "Price the basket", 0.24f, 0.5f, w = 0.42f, h = 0.2f, sub = "custom span"),
            DNode("save", "Save the order", 0.74f, 0.5f, w = 0.42f, h = 0.2f, sub = "custom span"),
            DNode("attr", "Attributes", 0.5f, 0.85f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "restaurant id, city"),
        ),
        edges = listOf(
            DEdge("req", "price"),
            DEdge("req", "save"),
            DEdge("price", "attr"),
        ),
        aspect = 0.8f,
    )

    private fun sampling() = DiagramSpec(
        "Sampling keeps whole traces and drops the rest; excluding the health check URL drops what was worthless anyway.",
        nodes = listOf(
            DNode("all", "Every trace", 0.26f, 0.15f, w = 0.46f, h = 0.2f, sub = "all of them"),
            DNode("health", "Health checks", 0.76f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "excluded by URL"),
            DNode("keep", "Kept", 0.26f, 0.53f, w = 0.46f, h = 0.2f, tone = 1, sub = "one in ten, whole"),
            DNode("drop", "Dropped", 0.76f, 0.53f, w = 0.42f, h = 0.2f, sub = "the rest"),
            DNode("bill", "The bill", 0.5f, 0.87f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "by the gigabyte"),
        ),
        edges = listOf(
            DEdge("all", "keep"),
            DEdge("all", "drop"),
            DEdge("health", "drop"),
            DEdge("keep", "bill"),
        ),
        aspect = 0.8f,
    )

    private fun rolemap() = DiagramSpec(
        "The application map draws one box per cloud role name, so each service needs a name of its own.",
        nodes = listOf(
            DNode("api", "zaiqa api", 0.24f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "cloud role name"),
            DNode("worker", "order worker", 0.76f, 0.18f, w = 0.4f, h = 0.2f, sub = "cloud role name"),
            DNode("taste", "taste service", 0.24f, 0.7f, w = 0.4f, h = 0.2f, tone = 2, sub = "cloud role name"),
            DNode("blob", "One unnamed box", 0.76f, 0.7f, w = 0.4f, h = 0.24f, kind = NodeKind.NOTE, sub = "all three, if you forget"),
        ),
        edges = listOf(
            DEdge("api", "worker", "calls"),
            DEdge("api", "taste", "calls"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's OpenTelemetry questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("hdr", "A separate trace", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "traceparent missing"),
            DNode("role", "One blob on the map", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "cloud role name"),
            DNode("one", "One call in Python", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the distro's setup"),
            DNode("conn", "Connection string", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "an environment variable"),
            DNode("samp", "Bill too high", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sampling, whole traces"),
            DNode("cust", "Time one function", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a custom span"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("hdr", "role", "one", "conn", "samp", "cust")
    private val today = BoardCard("Today", "App Insights, the distro, one custom span, then transaction search.")
    private val spanCard = BoardCard("Span", "One timed piece of work: a name, a start, an end and a parent.")
    private val queueCard = BoardCard("Across the queue", "The context rides in a label beside the message body.")
    private val customCard = BoardCard("Custom span", "A tracer, a name like price the basket, and attributes on it.")
    private val sampleCard = BoardCard("Sampling", "Whole traces kept or dropped, decided once, carried in the flag.")
    private val roleCard = BoardCard("Cloud role name", "The name a service calls itself, from the OTEL service name setting.")
    private val telemetryCard = BoardCard("Three kinds of telemetry", "Traces, metrics and logs. A log in a span carries the trace id.")
    private val phraseCard = BoardCard("Listen for the phrase", "A separate trace, and one blob on the map: one answer each.")
    private val fourCard = BoardCard("And four more", "One call, the environment variable, sampling, and a custom span.")
    private val treeCard = BoardCard("Parent and children", "The pay call is the parent; the insert and the send are its children.")
    private val alibiCard = BoardCard("Three alibis", "Forty milliseconds, two seconds, and nothing from Azure SQL.")
    private val nineCard = BoardCard("Nine of the eleven", "The message sat in the queue. Waiting is nobody's log line.")
    private val tenthCard = BoardCard("One in ten", "A tenth of the bill, and the counts scale back up. You lose the one.")
    private val distroCard = BoardCard("The distro", "One package: the standard, the exporter and ready-made instrumentation.")
    private val sqlCard = BoardCard("Every query a span", "The SQL client instrumentation brings the statement with it.")
    private val otelCard = BoardCard("OpenTelemetry", "One record of the whole journey. That record is a trace.")
    private val traceIdCard = BoardCard("Trace id", "A long random number, carried to every program it touches.")
    private val oneTraceCard = BoardCard("One trace", "The tap on the phone to the restaurant's tablet, in order.")

    private fun classicBoard() = PodcastBoard(
        "ai-opentelemetry",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is running..."
            Scene(
                0, "One order, every hop", journey(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("Today", "Watching one order cross the app, the API, SQL, the queue and the worker.")),
                    Beat(2, show = setOf("sql"), light = setOf("api", "sql"), pulse = "phone" to "api", card = BoardCard("Eleven seconds", "Every log says fine, and it still took eleven seconds.")),
                    Beat(4, light = setOf("api"), card = otelCard),
                    Beat(6, show = setOf("queue", "worker"), light = setOf("queue", "worker"), card = oneTraceCard),
                    Beat(8, light = setOf("phone", "api", "queue", "worker"), card = traceIdCard),
                ),
            ),
            // 10: "Yes, and the word for it is a span..."
            Scene(
                10, "Spans make a tree", spans(),
                beats = listOf(
                    Beat(10, show = setOf("trace", "s1"), light = setOf("s1"), pulse = "trace" to "s1", card = spanCard),
                    Beat(12, show = setOf("s2", "s3", "s4"), light = setOf("s2", "s3"), pulse = "s1" to "s2", card = treeCard),
                ),
            ),
            // 14: "Over HTTP it travels in a header called traceparent..."
            Scene(
                14, "How the id travels", propagate(),
                beats = listOf(
                    Beat(14, show = setOf("api", "hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = BoardCard("traceparent", "Version, trace id, the calling span's id, and a kept-or-not flag.")),
                    Beat(16, light = setOf("api", "hdr"), card = BoardCard("Context propagation", "The caller writes it, the receiver carries on the same trace.")),
                    Beat(18, show = setOf("msg", "worker"), light = setOf("msg", "worker"), pulse = "msg" to "worker", card = queueCard),
                    Beat(20, light = setOf("worker"), card = BoardCard("Nine seconds queued", "The gap is real waiting, which no log line would have shown.")),
                ),
            ),
            // 22: "Almost none. Microsoft ships a distro..."
            Scene(
                22, "One call at start-up", distro(),
                beats = listOf(
                    Beat(22, show = setOf("call"), light = setOf("call"), card = distroCard),
                    Beat(24, show = setOf("appi"), light = setOf("appi"), pulse = "call" to "appi", card = BoardCard("Python and .NET", "Configure azure monitor, or use Azure Monitor. One place at the end.")),
                    Beat(26, show = setOf("web", "http", "azure"), light = setOf("web", "http", "azure"), card = BoardCard("What it instruments", "Calls in, calls out with traceparent, and the Azure libraries.")),
                    Beat(28, light = setOf("web"), card = sqlCard),
                ),
            ),
            // 30: "You write a custom span..."
            Scene(
                30, "Timing your own code", custom(),
                beats = listOf(
                    Beat(30, show = setOf("price", "attr"), light = setOf("price", "attr"), pulse = "price" to "attr", card = customCard),
                    Beat(32, light = setOf("price", "save"), pulse = "req" to "save", card = BoardCard("Which two seconds", "Pricing, the payment provider, or the save? One span around each.")),
                ),
            ),
            // 34: "They come along too. The distro sends three kinds..."
            Scene(
                34, "Logs and metrics too", distro(),
                beats = listOf(
                    Beat(34, light = setOf("appi"), pulse = "call" to "appi", card = telemetryCard),
                    Beat(36, light = setOf("appi"), card = BoardCard("Metrics", "Calls a minute, how long they took, and a counter of your own.")),
                ),
            ),
            // 38: "It does, and you pay by the gigabyte you send..."
            Scene(
                38, "What it costs", sampling(),
                beats = listOf(
                    Beat(38, show = setOf("all", "keep", "drop", "bill"), light = setOf("keep", "drop"), pulse = "all" to "keep", card = sampleCard),
                    Beat(40, light = setOf("bill"), pulse = "keep" to "bill", card = tenthCard),
                    Beat(44, show = setOf("health"), light = setOf("health"), pulse = "health" to "drop", card = BoardCard("Excluded URLs", "Health checks all day: name the path and skip them. Aimed, not blind.")),
                ),
            ),
            // 46: "Much better, because it is aimed... one last thing: the map."
            Scene(
                46, "One box per service", rolemap(),
                beats = listOf(
                    Beat(48, light = setOf("api", "worker", "taste"), card = roleCard),
                ),
            ),
            // 49: "Right. So what do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("hdr", "role"), card = phraseCard),
                    Beat(52, light = setOf("one", "conn", "samp", "cust"), card = fourCard),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-opentelemetry--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Eleven Second Order..."
            Scene(
                0, "The Eleven Second Order", journey(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("The case", "Pay to ordered in eleven seconds, and every log swearing it was fine.")),
                    Beat(2, show = setOf("sql", "queue", "worker"), light = setOf("api", "sql", "worker"), pulse = "phone" to "api", card = alibiCard),
                    Beat(4, light = setOf("api"), card = otelCard),
                    Beat(6, light = setOf("phone", "worker"), card = oneTraceCard),
                    Beat(8, light = setOf("queue"), card = traceIdCard),
                ),
            ),
            // 10: "A span: one timed piece of work..."
            Scene(
                10, "Evidence: the spans", spans(),
                beats = listOf(
                    Beat(10, show = setOf("trace", "s1"), light = setOf("s1"), pulse = "trace" to "s1", card = spanCard),
                    Beat(12, show = setOf("s2", "s3", "s4"), light = setOf("s2", "s3"), pulse = "s1" to "s2", card = treeCard),
                ),
            ),
            // 14: "That is clue one. There was no printout..."
            Scene(
                14, "Clue one: two islands", propagate(),
                beats = listOf(
                    Beat(14, show = setOf("api", "hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = BoardCard("Clue one", "Two traces, nothing joining them. Over HTTP that is traceparent.")),
                    Beat(16, show = setOf("msg"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("But not over HTTP", "The worker takes a message off the orders queue instead.")),
                    Beat(18, show = setOf("worker"), light = setOf("msg", "worker"), pulse = "msg" to "worker", card = queueCard),
                    Beat(20, light = setOf("worker"), card = nineCard),
                ),
            ),
            // 22: "There are. The second: the map..."
            Scene(
                22, "The second file: the map", rolemap(),
                beats = listOf(
                    Beat(22, show = setOf("blob"), light = setOf("blob"), card = BoardCard("One box, three services", "The map draws a box per service. Ours had all three in one.")),
                    Beat(24, show = setOf("api", "worker", "taste"), light = setOf("api", "worker", "taste"), pulse = "api" to "worker", card = roleCard),
                ),
            ),
            // 26: "Noted. And the third file?"
            Scene(
                26, "A missing trace", sampling(),
                beats = listOf(
                    Beat(28, show = setOf("all", "keep", "drop", "bill"), light = setOf("drop"), pulse = "all" to "drop", card = BoardCard("Never stored", "The angry diner's order at ten past eight was simply not there.")),
                    Beat(32, light = setOf("keep"), pulse = "all" to "keep", card = sampleCard),
                    Beat(34, show = setOf("health"), light = setOf("health"), pulse = "health" to "drop", card = BoardCard("Throw away the rubbish", "Health checks all day: an excluded URLs setting drops them by name.")),
                ),
            ),
            // 35: "Every Azure SQL query, once the instrumentation is on..."
            Scene(
                35, "What the picture shows now", custom(),
                beats = listOf(
                    Beat(36, show = setOf("req"), light = setOf("req"), card = sqlCard),
                    Beat(38, show = setOf("price", "save", "attr"), light = setOf("price", "attr"), pulse = "price" to "attr", card = customCard),
                ),
            ),
            // 40: "Exactly that. And your own log lines come along too..."
            Scene(
                40, "Logs and metrics too", distro(),
                beats = listOf(
                    Beat(42, light = setOf("appi"), pulse = "call" to "appi", card = telemetryCard),
                    Beat(44, light = setOf("call"), card = distroCard),
                ),
            ),
            // 45: "Case closed. What goes in the summary for the exam?"
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("hdr", "role"), card = phraseCard),
                    Beat(48, light = setOf("one", "conn", "samp", "cust"), card = fourCard),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-opentelemetry--gossip",
        listOf(
            // 0: "Okay, did you hear what the Zaiqa team did to the checkout this week?"
            Scene(
                0, "Four days of arguing", journey(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone", "api"), card = BoardCard("The week", "Eleven seconds from Pay to ordered, and every log saying fine.")),
                    Beat(2, show = setOf("sql", "worker"), light = setOf("api", "worker"), pulse = "phone" to "api", card = alibiCard),
                    Beat(4, light = setOf("api"), card = otelCard),
                    Beat(6, show = setOf("queue"), light = setOf("queue"), card = oneTraceCard),
                ),
            ),
            // 8: "Monday was Tom..."
            Scene(
                8, "Monday: Tom", distro(),
                beats = listOf(
                    Beat(8, show = setOf("call"), light = setOf("call"), card = BoardCard("Monday: Tom", "He set the distro up at the bottom of the file, after it served.")),
                    Beat(10, show = setOf("web", "http", "azure", "appi"), light = setOf("web"), pulse = "call" to "web", card = spanCard),
                    Beat(12, light = setOf("call"), card = BoardCard("Before it serves", "Set up first, or the first few hundred requests have no spans.")),
                ),
            ),
            // 14: "Tuesday was the intern, and this is the famous one..."
            Scene(
                14, "Tuesday: the intern", propagate(),
                beats = listOf(
                    Beat(14, show = setOf("api", "hdr"), light = setOf("api"), card = BoardCard("Tuesday: the intern", "The API's work and the worker's came out as two traces.")),
                    Beat(16, light = setOf("hdr"), pulse = "api" to "hdr", card = BoardCard("traceparent", "Version, trace id, the calling span's id, and a kept-or-not flag.")),
                    Beat(18, show = setOf("msg", "worker"), light = setOf("msg", "worker"), pulse = "msg" to "worker", card = queueCard),
                    Beat(20, light = setOf("worker"), card = nineCard),
                ),
            ),
            // 22: "Dana. Dana opened the map..."
            Scene(
                22, "Wednesday: Dana", rolemap(),
                beats = listOf(
                    Beat(22, show = setOf("blob"), light = setOf("blob"), card = BoardCard("Wednesday: Dana", "One box on the map, with all three services inside it.")),
                    Beat(24, show = setOf("api", "worker", "taste"), light = setOf("api", "worker", "taste"), pulse = "api" to "taste", card = roleCard),
                ),
            ),
            // 26: "Marcus. Marcus got the whole thing working on his laptop..."
            Scene(
                26, "Thursday: Marcus", exam(),
                beats = listOf(
                    Beat(26, show = setOf("conn"), light = setOf("conn"), card = BoardCard("Thursday: Marcus", "He pasted the connection string into the source and pushed it.")),
                    Beat(28, light = setOf("conn"), card = BoardCard("An environment variable", "What production should always do. The old string was rotated.")),
                ),
            ),
            // 30: "Friday was Priya, and an angry diner..."
            Scene(
                30, "Friday: Priya", sampling(),
                beats = listOf(
                    Beat(30, show = setOf("all", "keep", "drop", "bill"), light = setOf("drop"), pulse = "all" to "drop", card = BoardCard("Friday: Priya", "The angry diner's trace at ten past eight was simply not there.")),
                    Beat(32, light = setOf("keep"), pulse = "all" to "keep", card = sampleCard),
                    Beat(34, light = setOf("bill"), pulse = "keep" to "bill", card = tenthCard),
                    Beat(36, show = setOf("health"), light = setOf("health"), pulse = "health" to "drop", card = BoardCard("Excluded URLs", "Health checks all day: name the path and skip them. Aimed, not blind.")),
                ),
            ),
            // 38: "Plenty. The SQL client instrumentation went on..."
            Scene(
                38, "What went right", custom(),
                beats = listOf(
                    Beat(38, show = setOf("req"), light = setOf("req"), card = sqlCard),
                    Beat(40, show = setOf("price", "attr"), light = setOf("price", "attr"), pulse = "price" to "attr", card = customCard),
                    Beat(42, show = setOf("save"), light = setOf("save"), pulse = "req" to "save", card = telemetryCard),
                ),
            ),
            // 45: "So the week ended well." Then: what to remember for the exam.
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("hdr", "role"), card = phraseCard),
                    Beat(50, light = setOf("one", "conn", "samp", "cust"), card = fourCard),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-opentelemetry--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen has shut..."
            Scene(
                0, "One order, still crawling", journey(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("Tonight", "We follow one order all the way, and nobody may say it was fine.")),
                    Beat(2, show = setOf("sql", "worker"), light = setOf("api", "worker"), pulse = "phone" to "api", card = alibiCard),
                    Beat(4, light = setOf("api"), card = otelCard),
                    Beat(6, show = setOf("queue"), light = setOf("queue"), card = oneTraceCard),
                    Beat(8, light = setOf("phone", "worker"), card = traceIdCard),
                ),
            ),
            // 10: "It is called a span..."
            Scene(
                10, "A family tree, at midnight", spans(),
                beats = listOf(
                    Beat(10, show = setOf("trace", "s1"), light = setOf("s1"), pulse = "trace" to "s1", card = spanCard),
                    Beat(12, show = setOf("s2", "s3", "s4"), light = setOf("s2", "s3"), pulse = "s1" to "s2", card = treeCard),
                ),
            ),
            // 14: "Over HTTP it rides in a header called traceparent..."
            Scene(
                14, "How the id travels", propagate(),
                beats = listOf(
                    Beat(14, show = setOf("api", "hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = BoardCard("traceparent", "Version, trace id, the calling span's id, and a kept-or-not flag.")),
                    Beat(16, light = setOf("api", "hdr"), card = BoardCard("Context propagation", "The caller writes it, the receiver carries on the same trace.")),
                    Beat(18, show = setOf("msg", "worker"), light = setOf("msg", "worker"), pulse = "msg" to "worker", card = queueCard),
                    Beat(20, light = setOf("worker"), card = nineCard),
                ),
            ),
            // 22: "Almost none. There is a distro..."
            Scene(
                22, "One call at start-up", distro(),
                beats = listOf(
                    Beat(22, show = setOf("call"), light = setOf("call"), card = distroCard),
                    Beat(24, show = setOf("appi"), light = setOf("appi"), pulse = "call" to "appi", card = BoardCard("Python and .NET", "Configure azure monitor, or use Azure Monitor. One place at the end.")),
                    Beat(26, show = setOf("web", "http", "azure"), light = setOf("web", "http", "azure"), card = BoardCard("What it instruments", "Calls in, calls out with traceparent, and the Azure libraries.")),
                    Beat(28, light = setOf("web"), card = sqlCard),
                ),
            ),
            // 30: "A custom span. Ask the library for a tracer..."
            Scene(
                30, "Timing your own code", custom(),
                beats = listOf(
                    Beat(30, show = setOf("price", "attr"), light = setOf("price", "attr"), pulse = "price" to "attr", card = customCard),
                    Beat(32, light = setOf("price", "save"), pulse = "req" to "save", card = BoardCard("Which two seconds", "Pricing, the payment provider, or the save? One span around each.")),
                ),
            ),
            // 34: "They come along. The distro sends three kinds of telemetry..."
            Scene(
                34, "Logs and metrics too", distro(),
                beats = listOf(
                    Beat(34, light = setOf("appi"), pulse = "call" to "appi", card = telemetryCard),
                    Beat(36, light = setOf("appi"), card = BoardCard("Metrics", "Calls a minute, how long they took, and a counter of your own.")),
                ),
            ),
            // 38: "You pay by the gigabyte you send, so there is sampling..."
            Scene(
                38, "What it costs", sampling(),
                beats = listOf(
                    Beat(38, show = setOf("all", "keep", "drop", "bill"), light = setOf("keep", "drop"), pulse = "all" to "keep", card = sampleCard),
                    Beat(40, light = setOf("bill"), pulse = "keep" to "bill", card = tenthCard),
                    Beat(42, show = setOf("health"), light = setOf("health"), pulse = "health" to "drop", card = BoardCard("Excluded URLs", "Health checks all night: name the path and skip them. Aimed, not blind.")),
                ),
            ),
            // 43: "Aimed, not blind. And the map you keep mentioning?"
            Scene(
                43, "One box per service", rolemap(),
                beats = listOf(
                    Beat(44, light = setOf("api", "worker", "taste"), card = roleCard),
                ),
            ),
            // 45: "Mm. Before I fall asleep on you. What do I remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("hdr", "role"), card = phraseCard),
                    Beat(48, light = setOf("one", "conn", "samp", "cust"), card = fourCard),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-opentelemetry--couples",
        listOf(
            // 0: "Tonight is date night, darling, and I have brought you a mystery..."
            Scene(
                0, "Date night, one mystery", journey(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("Date night", "One order, eleven seconds, three services all swearing they were quick.")),
                    Beat(2, show = setOf("sql", "worker"), light = setOf("api", "worker"), pulse = "phone" to "api", card = alibiCard),
                    Beat(4, light = setOf("api"), card = BoardCard("Right answer", "OpenTelemetry, and what it gives you for one order is a trace.")),
                    Beat(6, show = setOf("queue"), light = setOf("queue"), card = traceIdCard),
                ),
            ),
            // 8: "And one scan is one piece of work. What is the word?"
            Scene(
                8, "What is the word", spans(),
                beats = listOf(
                    Beat(8, show = setOf("trace", "s1"), light = setOf("s1"), pulse = "trace" to "s1", card = spanCard),
                    Beat(10, show = setOf("s2", "s3", "s4"), light = setOf("s2", "s3"), pulse = "s1" to "s2", card = treeCard),
                ),
            ),
            // 11: "Then my turn to be strict. Exam style, darling..."
            Scene(
                11, "What is missing", propagate(),
                beats = listOf(
                    Beat(12, show = setOf("api", "hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = BoardCard("Right answer", "Over HTTP the trace id rides in the traceparent header.")),
                    Beat(16, show = setOf("msg"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("But not over HTTP", "The worker takes a message off the orders queue instead.")),
                    Beat(18, show = setOf("worker"), light = setOf("msg", "worker"), pulse = "msg" to "worker", card = queueCard),
                    Beat(20, light = setOf("worker"), card = nineCard),
                ),
            ),
            // 22: "Almost none. There is a distro..."
            Scene(
                22, "One call at start-up", distro(),
                beats = listOf(
                    Beat(22, show = setOf("call"), light = setOf("call"), card = distroCard),
                    Beat(24, show = setOf("appi"), light = setOf("appi"), pulse = "call" to "appi", card = BoardCard("Python and .NET", "Configure azure monitor, or use Azure Monitor. One place at the end.")),
                    Beat(26, show = setOf("web", "http", "azure"), light = setOf("web", "http", "azure"), card = BoardCard("Right answer", "Calls in, calls out with traceparent, and the Azure libraries.")),
                ),
            ),
            // 28: "And Zaiqa's orders are in Azure SQL. Do the queries show?"
            Scene(
                28, "Ours, and only ours", custom(),
                beats = listOf(
                    Beat(28, show = setOf("req"), light = setOf("req"), card = sqlCard),
                    Beat(30, show = setOf("price", "attr"), light = setOf("price", "attr"), pulse = "price" to "attr", card = customCard),
                    Beat(32, show = setOf("save"), light = setOf("save"), pulse = "req" to "save", card = BoardCard("Which second was yours", "One span around the pricing, one around the save.")),
                    Beat(34, light = setOf("attr"), card = telemetryCard),
                ),
            ),
            // 36: "You pay by the gigabyte you send. So there is sampling..."
            Scene(
                36, "The bill", sampling(),
                beats = listOf(
                    Beat(36, show = setOf("all", "keep", "drop", "bill"), light = setOf("keep", "drop"), pulse = "all" to "keep", card = sampleCard),
                    Beat(38, light = setOf("bill"), pulse = "keep" to "bill", card = tenthCard),
                    Beat(42, show = setOf("health"), light = setOf("health"), pulse = "health" to "drop", card = BoardCard("No kiss for that one", "Sampling cuts blindly. Excluded URLs drop the health checks by name.")),
                ),
            ),
            // 43: "Aimed, not blind. Let me earn it back, then..."
            Scene(
                43, "One box per service", rolemap(),
                beats = listOf(
                    Beat(43, show = setOf("blob"), light = setOf("blob"), card = BoardCard("One box, three services", "The map draws a box per service. Theirs had all three in one.")),
                    Beat(44, show = setOf("api", "worker", "taste"), light = setOf("api", "worker", "taste"), pulse = "api" to "worker", card = roleCard),
                ),
            ),
            // 46: "One trace for one order: the tap, the API..."
            Scene(
                46, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("hdr"), card = BoardCard("The whole household", "The tap, the API, the insert, the send, the worker a minute later.")),
                    Beat(48, light = setOf("hdr", "role", "one"), card = BoardCard("Listen for the phrase", "A separate trace, one blob on the map, and one call in Python.")),
                    Beat(51, light = setOf("conn", "samp", "cust"), card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
