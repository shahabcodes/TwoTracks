package com.shahaabapps.core.content

/** Tracing with OpenTelemetry: following a request across services: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesOpenTelemetry {
    private val classic = Podcast(
        "ai-opentelemetry",
        listOf(
            maya("Hi, and welcome back. Today: following one request on its journey through several programs. Think of a parcel with a tracking number, scanned at every depot, so you can see the whole trip and where it sat waiting."),
            leo("My app is one program. Why would a request visit several?"),
            maya("Say you're building the Two Tracks Coach, a study assistant in an Android app. A student asks a question, and the phone sends it to the Coach API, a Python program on Azure, Microsoft's cloud. API just means the part other programs call. That API calls a second program to summarise the notes."),
            leo("What's the second program?"),
            maya("A function. In Azure Functions, a function is a small piece of code Azure runs only when it's called, like a taxi you hail instead of a car you own. The Coach's one is called Summarize, and it reads the notes from a database."),
            leo("And one evening, students say the Coach is slow."),
            maya("Each program keeps its own log, a diary like Logcat on your phone, and each diary says, I was fine. Matching them by the clock fails: a hundred students ask in the same second. You need something that travels with the request."),
            leo("The tracking number."),
            maya("That's distributed tracing: following one request through every program it touches. The tool for it is called OpenTelemetry."),
            leo("Another Azure product I have to learn?"),
            maya("No, an open standard: a shared rulebook many tools agree on, not tied to one company. Python, Java and Kotlin all speak it, and Azure is one place it can send to."),
            leo("Okay. What words does the rulebook use?"),
            maya("Two big ones. A trace is the whole journey of one request: here, one student question, end to end. It carries a trace id, a long random number: the tracking number itself."),
            leo("And the scans at each depot?"),
            maya("Those are spans. A span is one stop on the trip, stamped in and stamped out: in at nine, out at nine forty, forty minutes at that depot. Every span has a name, a start, an end, and the trip's trace id."),
            leo("And in the Coach, what are the stops?"),
            maya("Handling the question is one span. The call to Summarize is a second, inside it, and the database read inside the function is a third. Each span's parent is the step that started it, so they sit inside each other, like boxes inside boxes."),
            leo("So the spans make a family tree, all with the same surname."),
            maya("[chuckle] That's it. And a span can carry attributes: little labels, a name and a value, stuck on the span. For example, coach topic equals cosmos. Later you can find every slow question about one topic."),
            leo("Where do all these spans go?"),
            maya("To Application Insights, Azure's service that collects what your app reports about itself, and shows it to you. You create one in Azure; each thing you create there is called a resource."),
            leo("Reports about itself? Like Logcat, when my phone's plugged in?"),
            maya("Close, but nobody's plugged in: your server is in the cloud. That report is called telemetry. Picture a fitness tracker on your wrist, sending your steps and heartbeat to your phone. Your server sends its requests, how long each took, which failed, and its spans."),
            leo("And it works out which spans belong together?"),
            maya("By trace id. Application Insights stitches every span with the same number into one story."),
            leo("But how does the function know the API's trace id? They're two separate programs."),
            maya("That's the clever bit: passing the trace along. When the API calls the function over the web, it adds a header: a line of extra information on the outside of the request, like writing on an envelope. You've set headers with OkHttp. This one is called traceparent."),
            leo("And what's written on the envelope?"),
            maya("The trace id, and the id of the span that made the call, its parent. When the function receives the call, it reads traceparent and carries on the same trace, instead of starting a new one."),
            leo("Do I have to write that header myself?"),
            maya("No. That's instrumentation: ready-made code that records spans for you, wrapped around the tools you already use. Think of an OkHttp interceptor that stamps every call without you touching it. Going out, it adds traceparent; coming in, it reads it."),
            leo("And how do I get this instrumentation into my Python?"),
            maya("With one package from Microsoft: the Azure Monitor OpenTelemetry Distro. A distro is a bundle, like a meal kit: the OpenTelemetry parts, the instrumentations, and an exporter that sends your spans to Application Insights. You install it and call one function, configure azure monitor."),
            leo("Where in my code?"),
            maya("At the very top, before the app answers any request, like starting a crash reporter in your Android Application class. Call it later, and the first requests go untraced. It also needs the connection string: the postal address of your Application Insights."),
            leo("Do I paste the connection string into the code?"),
            maya("No. Hand it over as an environment variable: a setting given to a program from outside its code, like a note left on its desk. The distro finds it there. Pasting it into the source is a classic mistake."),
            leo("So what does it trace without me doing anything?"),
            maya("Incoming calls through FastAPI, the Python toolkit the Coach API is built with. That's the request your Android app sends in."),
            leo("And the calls my server makes out?"),
            maya("Through requests, a popular Python library for calling the web, and through the Azure SDK, short for software development kit: Microsoft's packages for its services. So database reads show up too."),
            leo("And if I want to time just one part of my own code?"),
            maya("A custom span. You get a tracer, the object that makes spans, and wrap the part you care about, say ranking the notes, in a with block. Then you set an attribute on it, like the topic."),
            leo("Okay. Where do I actually see all this?"),
            maya("Two places. Transaction search finds one request, by time or name. Open its end-to-end transaction details, and you see every span of that trace as a timeline: bars, one under another, showing where the time went."),
            leo("And the second place?"),
            maya("The application map: a drawing of your whole system, with arrows for the calls and how often they fail. Each box on it is a node, one dot in a network; here, one node is one program."),
            leo("How does it know which box is which?"),
            maya("By the cloud role name, set with the OTEL service name setting, OTEL being short for OpenTelemetry. Forget it, and two programs blur into one node."),
            leo("So what did the slow evening show?"),
            maya("One trace showed it: forty milliseconds in the API, nearly two seconds in Summarize, mostly one database query. Without tracing, they'd have tuned the wrong program."),
            leo("[pause] Does it keep every single request? That sounds expensive."),
            maya("Not by default. That's sampling: keeping only some traces, to control cost. Recent versions keep about five traces a second. For a fixed one in ten, set the sampling ratio to 0.1."),
            leo("Couldn't that cut a journey in half?"),
            maya("No. Whole traces are kept or dropped together, so you never get half a journey."),
            leo("What goes wrong most often?"),
            maya("Calling configure azure monitor too late, and pasting the connection string into the code. You've heard both."),
            leo("And the sneaky ones?"),
            maya("Every service left with the default name, so they blur on the map. A homemade call helper nobody instruments, so the trace breaks in two. And expecting every request kept while sampling is on."),
            leo("So what do I remember for the exam?"),
            maya("If the question says one call to send traces from Python, the answer is configure azure monitor. If a called service shows up as a separate trace, traceparent is missing. If two services appear as one node on the map, a different service name in each. To keep one trace in ten, a sampling ratio of 0.1. And for which service fails most, the application map."),
            leo("And anything to try today?"),
            maya("Create an Application Insights resource and copy its connection string. Install the distro with FastAPI, and call configure azure monitor in a tiny app. Add one custom span with an attribute, run the app and hit it a few times. Then open Transaction search, and the end-to-end view of one request."),
            leo("A tracking number for every question. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-opentelemetry",
        listOf(
            maya("Welcome to Case Files. Today's case: The Two Islands. QuizCraft is a small Android quiz app. Behind it, a Python program on Azure, Microsoft's cloud, marks each answer. We'll call it the quiz API: the part the phone calls. Students say marking takes forever. And every log says, not me."),
            leo("Detective Leo, on the case. Every log is innocent. Classic. Who else is involved?"),
            maya("The quiz API calls a second program, the grader, which checks the hard answers. The grader is a function: in Azure Functions, a small piece of code Azure runs only when it's called, like a taxi you hail. The grader reads past answers from a database."),
            leo("So the slow part is somewhere along a chain of three."),
            maya("And the logs can't tell us where, because two hundred students answer in the same minute. Matching by the clock is hopeless. The team had already added distributed tracing: following one request through every program it touches, like a parcel scanned at every depot."),
            leo("With what?"),
            maya("OpenTelemetry, the open standard for tracing. A standard is a shared rulebook that many tools agree on, like the rules of chess: any board, any player, the same moves."),
            leo("And the rulebook's first word?"),
            maya("The trace: the whole journey of one request, like a parcel's whole trip from the shop to your door. It has a trace id, a long random number that works like the parcel's tracking number."),
            leo("And the scans at each depot?"),
            maya("Spans. A span is one stop on the trip, stamped in and out: in at nine, out at nine forty, forty minutes there. Marking the answer is one span; the call to the grader, another."),
            leo("And under the picture? What's actually in a span?"),
            maya("A small record: a name, a start time, how long it took, the trace id, its own id, and its parent's id, the span that started it."),
            leo("And where do the records end up?"),
            maya("In Application Insights, Azure's service that collects an app's telemetry and shows it back to you."),
            leo("Telemetry. In plain words, for the file."),
            maya("What an app reports about itself. Think of a fitness tracker on your wrist, sending your steps and heartbeat to your phone. Your app sends its requests, timings, errors and spans, and Application Insights joins the spans into one story by trace id."),
            leo("That's the theory. What's the evidence?"),
            maya("The grader's spans showed up as a separate trace. Two islands, no bridge."),
            leo("Suspect one: the grader's managed identity. It's not allowed to join in."),
            maya("[pause] Wrong suspect. A managed identity is a badge that decides what a program may open. It carries nothing about tracing. Ask instead how the grader would know the trace id at all. It's a separate program."),
            leo("Someone has to tell it."),
            maya("Exactly. That's context propagation, passing the trace along. On every outgoing web call, a header is added. A header is a line of extra information on the outside of a request, like writing on an envelope. This one is traceparent. It carries the trace id and the caller's span id."),
            leo("So the grader reads the envelope and continues the same trace."),
            maya("If the envelope arrives. And who writes it? The instrumentation: ready-made code wrapped around the tools you already use, recording spans and adding traceparent for you. Like an OkHttp interceptor in your Android app."),
            leo("So nobody on the team typed that header by hand."),
            maya("Nobody. And here's clue one: the quiz API called the grader with a raw socket helper, a homemade shortcut. Nothing instruments it, so the envelope went out blank."),
            leo("And the fix?"),
            maya("Make the call with requests, the popular Python library for calling the web, which the distro does instrument. Traceparent is added, the grader reads it, and the two islands become one trace."),
            leo("Distro? Is that another suspect?"),
            maya("A witness. The Azure Monitor OpenTelemetry Distro is one Python package from Microsoft: a bundle, like a meal kit, of the OpenTelemetry parts, a sender to Application Insights, and the instrumentations. You install it and call configure azure monitor once, before the app starts answering requests."),
            leo("And the grader? It's a function, not a normal program."),
            maya("Functions take part too. In the function's settings file, host json, you set the telemetry mode to OpenTelemetry, add the distro, and keep the connection string. Then the function takes its trace id straight from the incoming traceparent."),
            leo("Connection string. Is that a clue?"),
            maya("Clue two, and a bad one. It's the address of your Application Insights, with an id on it, and someone had pasted it into the source code. It belongs in an environment variable, a setting handed to the program from outside its code. The distro reads it from there by itself."),
            leo("Case closed, then? One trace, everything joined."),
            maya("Not yet. Clue three: the application map. That's a drawing of your whole system, with arrows for the calls and how often they fail. Each box is a node, one dot in a network, and here one node is one program. QuizCraft's map showed one confused blob."),
            leo("Suspect: they should give each service its own Application Insights."),
            maya("[sigh] That splits the trace across two places, the opposite of what you want. The boxes are named by the cloud role name, and both services kept the default. Set the OTEL service name setting differently in each, quiz API and grader. OTEL is short for OpenTelemetry. Now the map shows quiz API pointing at grader."),
            leo("And what did the joined trace finally say?"),
            maya("Open transaction search, find one slow request, and open its end-to-end details: every span as a timeline. Forty milliseconds in the quiz API, nearly two seconds in the grader, most of it one database query. The culprit was the database read, not the API."),
            leo("Last loose end. Some slow requests weren't in there at all."),
            maya("That's sampling: keeping only some traces, to control cost. By default, recent versions keep about five traces a second. To keep a fixed one in ten, the sampling ratio is 0.1. Whole traces are kept or dropped together."),
            leo("So where does tracing stop working, for the record?"),
            maya("Wherever a call goes through a tool that isn't instrumented, and for any trace that sampling didn't keep."),
            leo("And if I want to time just the marking code?"),
            maya("A custom span. Ask for a tracer, the object that makes spans, wrap the marking in a with block, and set an attribute, a little label like the quiz topic. The KQL episode, the one about asking your logs questions, shows how to search those labels later."),
            leo("So, the case summary for the exam?"),
            maya("If the question says one call to send traces from Python, the answer is configure azure monitor. If a called service starts its own trace, traceparent is missing. If services blur into one node on the map, set a different service name in each. To keep one trace in ten, a sampling ratio of 0.1. And to time one part of your code, a custom span."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, without the slow grader. Create an Application Insights resource and copy its connection string. Install the distro with FastAPI, and call configure azure monitor in a tiny app. Add one custom span with an attribute, then hit it a few times. Then open Transaction search, and the end-to-end view of one request."),
            leo("Two islands, one bridge. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-opentelemetry",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They added tracing to the Two Tracks Coach, the study assistant in the Android app. Behind it run a few Python programs on Azure, Microsoft's cloud. And let's just say, it was a week."),
            leo("[laugh] I heard the word blob shouted across the office. What's tracing?"),
            maya("Following one request through every program it touches, like a parcel scanned at every depot. A student's question goes from the phone to the Coach API, the program the phone calls. Then on to Summarize, a small function Azure runs only when it's called, and then to a database."),
            leo("And they wanted to know which stop was slow."),
            maya("Right. They used OpenTelemetry, the open standard for tracing: a shared rulebook many tools agree on."),
            leo("Okay, but what does it actually record?"),
            maya("A trace: the whole journey of one question, from phone to database. It has a trace id, a long random number: the tracking number."),
            leo("And each stop along the way?"),
            maya("A span: one stop on the trip, stamped in and stamped out. In at nine, out at nine forty, so forty minutes at that depot."),
            leo("And in code terms? It's not a real stamp."),
            maya("No, a small record: a name, a duration, the trace id, its own id, and its parent's id, the step that started it."),
            leo("And where do all those stamps end up?"),
            maya("In Application Insights, Azure's service that collects an app's telemetry. That's what an app reports about itself, like a fitness tracker sending your steps and heartbeat to your phone. Here it's requests, timings, errors and spans."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. Tom installed the Azure Monitor OpenTelemetry Distro, Microsoft's bundle of everything you need, like a meal kit. You call one function, configure azure monitor, and it's all wired up."),
            leo("Sounds easy. What did Tom break?"),
            maya("He called it after the web app was already up and answering. So the first requests of every start went untraced. Configure azure monitor goes first, before the app serves anything."),
            leo("[sigh] Poor Tom. Tuesday?"),
            maya("Tuesday was Marcus. The distro needs a connection string, the address of your Application Insights with an id on it. Marcus pasted it straight into the source code, and pushed it for everyone to see."),
            leo("Where should it have gone?"),
            maya("Into an environment variable, a setting handed to a program from outside its code. The distro looks for the Application Insights connection string there by itself. Marcus moved it before lunch, to be fair."),
            leo("Wednesday. Go."),
            maya("The intern. Bless them. They wrote their own clever little helper to call Summarize, homemade from raw sockets, the bare plumbing of the internet. And from then on, Summarize's work showed up as its own separate trace. Two journeys instead of one."),
            leo("Why would a helper break tracing?"),
            maya("Here's how it really works. When the Coach calls another program over the web, it adds a header: a line of extra information on the outside of the request, like writing on an envelope."),
            leo("Like the auth header my Android app sends?"),
            maya("Same idea. This one's called traceparent, and it carries the trace id. The receiving side reads it and carries on the same trace."),
            leo("And the intern's helper didn't write the envelope."),
            maya("Nobody did. The header is added by instrumentation: ready-made code wrapped around common tools, like requests, the popular Python library for calling the web. The intern's homemade helper had no instrumentation. Switch to requests, and the header is back."),
            leo("So that's where tracing stops working."),
            maya("One of the places. Any call through a tool that isn't instrumented breaks the chain. FastAPI, requests, and the Azure SDK, short for software development kit, Microsoft's Python packages for its services, are covered for you."),
            leo("And Summarize? It's a function, not a web app."),
            maya("The team set telemetry mode to OpenTelemetry in the function's settings file, host json. Then it joins the same trace."),
            leo("Thursday. Who shouted blob?"),
            maya("Dana. Dana opened the application map, the drawing of the whole system, with arrows for the calls. Each box is a node, one dot in a network; here, one program. And the Coach API and Summarize appeared as one confused blob. She shouted. We all heard."),
            leo("[chuckle] Why one blob?"),
            maya("Both kept the default name. Each box is named by its cloud role name, set with the OTEL service name setting. OTEL is short for OpenTelemetry. Dana set it to coach api in one, summarize in the other, and the map split into two boxes with an arrow."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya was hunting one slow request from a student, in transaction search, which finds requests by time or name. And it wasn't there. She swore the tracing was broken."),
            leo("Was it?"),
            maya("No. That's sampling: keeping only some traces, to keep the cost down. Recent versions keep about five traces a second by default. Whole traces are kept or dropped together. For a fixed one in ten, the sampling ratio is 0.1. Priya's request just wasn't one of the lucky ones."),
            leo("Did they find what was slow, in the end?"),
            maya("They did. Priya added a custom span: she asked for a tracer, the object that makes spans, and wrapped the note ranking in a with block, with an attribute, a little label for the topic."),
            leo("And the verdict?"),
            maya("The end-to-end view showed nearly two seconds in one database query. The KQL episode, the one about asking your logs questions, shows how to search those labels."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the question says one call to send traces from Python, the answer is configure azure monitor, and Tom will tell you, call it first. If a called service starts its own trace, traceparent is missing. If services blur into one node, a different service name in each. To keep one in ten, a sampling ratio of 0.1."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create an Application Insights resource and copy its connection string. Install the distro with FastAPI, and call configure azure monitor in a tiny app. Add one custom span with an attribute, run the app and hit it a few times. Then open Transaction search, and the end-to-end view of one request."),
            leo("No blobs, no shouting. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-opentelemetry",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's follow one little request on its journey, all the way home."),
            ivy("A journey, at this hour. Where's it going?"),
            nova("Picture the Two Tracks Coach, the study assistant in the Android app. A student asks a question. It goes to the Coach API, a Python program on Azure, Microsoft's cloud. API just means the part other programs call. Then on to a second program, and then a database."),
            ivy("What's the second program?"),
            nova("Summarize. It's a function: in Azure Functions, a small piece of code Azure wakes only when it's called, like a night bus that comes when you wave. [pause] And one night, the Coach feels slow, and every program swears it wasn't them."),
            ivy("Of course they do. How do you catch them?"),
            nova("With distributed tracing: following one request through every program it touches. Like a parcel scanned at every depot, so you see the whole trip, and where it waited."),
            ivy("And who sets the rules for the scanning?"),
            nova("OpenTelemetry. It's an open standard, [pause] a shared set of rules many tools agree on, owned by no single company."),
            ivy("Rules. Tell me the first one."),
            nova("A trace is the whole journey of one request, like a parcel's whole trip from the shop to your door. It carries a trace id, a long random number, the parcel's tracking number. [pause] Every step of the journey wears that same number."),
            ivy("And each step has a name?"),
            nova("Each step is a span: one stop on the trip, stamped in and stamped out. In at nine, out at nine forty: forty minutes at that depot. [pause] Under the picture, a tiny record: a name, a start, a duration, the trace id, its own id, and its parent's, the step that began it."),
            ivy("A family tree of little records. Where do they all go to sleep?"),
            nova("Into Application Insights, Azure's service that collects what your app reports about itself, and stitches the spans back together by their trace id."),
            ivy("What it reports about itself. Say that in plain words."),
            nova("That report is called telemetry. Picture a fitness tracker on your wrist, counting steps and heartbeats through the night, and sending them to your phone. Your app sends how many requests came in, how long each took, which ones failed, and its spans."),
            ivy("But the function is a different program. How does it know the number?"),
            nova("Someone whispers it. When the Coach calls the function over the web, it adds a header: a line of extra information on the outside of the request, like writing on an envelope. This one is called traceparent. It carries the trace id and the caller's span id."),
            ivy("A secret note on the envelope. I like it."),
            nova("And the function reads it, and carries on the same trace instead of starting a new one. [pause] That's how it really works."),
            ivy("Do I write that note by hand? I'd forget."),
            nova("Nobody does. Instrumentation does: ready-made code wrapped around the tools you already use, like an interceptor on your Android app's web client, recording spans and adding the header."),
            ivy("And how does that get into my Python?"),
            nova("One package: the Azure Monitor OpenTelemetry Distro. A distro is a bundle, like a meal kit: the OpenTelemetry parts, a sender to Application Insights, and the instrumentations. You install it, and call one function, configure azure monitor, once, before the app starts answering."),
            ivy("Before. Not after."),
            nova("Before. Call it late, and the first requests slip by untraced. And it needs to know where to send things: the connection string, the address of your Application Insights, with an id on it."),
            ivy("Do I write that address in my code?"),
            nova("[pause] No. You hand it over as an environment variable, a setting given to a program from outside its code. The distro finds the Application Insights connection string there on its own."),
            ivy("What does it watch without being asked?"),
            nova("Quite a lot. FastAPI, the Python toolkit the Coach API is built with. Requests, the popular Python library for calling the web. And the Azure SDK, short for software development kit, Microsoft's Python packages for its services, so the database calls appear too."),
            ivy("And if I want to watch just one part of my own code? Closely."),
            nova("A custom span. You ask for a tracer, the object that makes spans, and wrap the part you care about, say ranking the notes, in a with block. Then you set an attribute on it, a little label, like the topic."),
            ivy("And later, I find it how?"),
            nova("The KQL episode, the one about asking your logs questions, shows you how to search those labels."),
            ivy("Now, where do I actually look?"),
            nova("Two places. Transaction search finds one request, by time or by name. Open its end-to-end transaction details, and every span lies there as a timeline, bar under bar, showing where the time went."),
            ivy("And the other place?"),
            nova("The application map. A drawing of your whole system, with arrows for the calls. Each box is a node, one dot in a network; here, one program."),
            ivy("And how does a box get its name?"),
            nova("From its cloud role name, which you set with the OTEL service name setting. OTEL is short for OpenTelemetry. [pause] Forget it, and two services blur into one."),
            ivy("Does it keep every request? Every single one?"),
            nova("Not by default. That's sampling: keeping only some traces, to keep the cost down. Recent versions keep about five a second. To keep a fixed one in ten, set the sampling ratio to 0.1. Whole traces are kept or dropped together. Never half a journey."),
            ivy("Now the part I've been waiting for. Where does all this stop working?"),
            nova("Here. [pause] Call out through a tool that isn't instrumented, a homemade helper, say, and the envelope goes out blank. The other side starts a new trace, and you get two islands. And a trace that sampling didn't keep, you'll never find, however hard you look."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says one call to send traces from Python, configure azure monitor. If a called service starts its own trace, traceparent is missing. If two services blur into one node, a different service name in each. To keep one in ten, a sampling ratio of 0.1. And for which service fails most, the application map."),
            ivy("And something to try today, before I sleep?"),
            nova("Create an Application Insights resource and copy its connection string. Install the distro with FastAPI, and call configure azure monitor in a tiny app. [pause] Add one custom span with an attribute, run it, and hit it a few times. Then open Transaction search, and the end-to-end view of one request."),
            ivy("Every journey, followed home. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-opentelemetry",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about the most devoted thing in the cloud."),
            rafe("Let me guess. Something that follows you everywhere you go."),
            nova("[laugh] Tracing, with OpenTelemetry. Following one request through every program it touches, like a parcel scanned at every depot. OpenTelemetry is the open standard for it, a shared rulebook many tools agree on."),
            rafe("Following someone everywhere. In some places that's frowned upon."),
            nova("Not in software. Picture the Two Tracks Coach, the study assistant in the Android app. A question goes from the phone to the Coach API, the Python program it calls, on Azure, Microsoft's cloud."),
            rafe("And the API does all the work itself?"),
            nova("No, it calls Summarize, a function: a small piece of code Azure runs only when it's called. And Summarize reads a database."),
            rafe("And when it's slow, everyone says it wasn't them."),
            nova("Exactly. So each request gets a trace: the whole journey, like a parcel's trip from the shop to your door, under one trace id, a long random number that works as the tracking number."),
            rafe("And each stop on the way? Every depot it visits?"),
            nova("Each stop is a span, stamped in and stamped out. In at nine, out at nine forty: forty minutes at that depot. Under the picture, a small record: a name, a start, a duration, the trace id, its own id, and its parent's, the step that started it."),
            rafe("A parent. So spans have a family. And where do all these records go?"),
            nova("To Application Insights, Azure's service that collects an app's telemetry. That's what an app reports about itself, like a fitness tracker sending your steps and heartbeat to your phone. It stitches the spans together by trace id."),
            rafe("Quiz time, darling, exam style. The Coach API calls Summarize, but Summarize's work shows up as a separate trace. What went missing?"),
            nova("The traceparent header. A header is a line of extra information on the outside of a web request, like writing on an envelope. Traceparent carries the trace id to the next program, so it carries on the same trace."),
            rafe("Right answer. You always carry my number with you. [kiss]"),
            nova("Mm. And who writes on the envelope? Instrumentation: ready-made code wrapped around the tools you already use, adding the header for you. Use a homemade helper nobody instruments, and the envelope goes out blank."),
            rafe("So how do I get instrumented, in Python?"),
            nova("The Azure Monitor OpenTelemetry Distro. A distro is a bundle, like a meal kit: the OpenTelemetry parts, a sender to Application Insights, and the instrumentations. My question: which one call sets it all up in Python?"),
            rafe("Configure azure monitor. Called once, at the very start, before the app answers a single request."),
            nova("Right answer. Timing is everything. [kiss]"),
            rafe("And it needs an address, I assume. Somewhere to send all those love letters."),
            nova("The connection string, the address of your Application Insights, with an id on it. And not in the code. It goes in an environment variable, a setting handed to a program from outside its code. The distro finds it by itself."),
            rafe("Discreet. What does it watch without being asked?"),
            nova("FastAPI, the Python toolkit the Coach API is built with: that's the request your phone sends in."),
            rafe("And the calls going out?"),
            nova("Requests, the popular library for calling the web. And the Azure SDK, short for software development kit, Microsoft's Python packages for its services, so database calls appear too."),
            rafe("And if I only want to watch one part of you closely?"),
            nova("[chuckle] A custom span. Ask for a tracer, the object that makes spans, and wrap the part you care about in a with block. Then add an attribute, a little label, like the topic. Your turn for a question."),
            rafe("Two services, one Application Insights, and on the application map they show up as one blob. How do we fix it?"),
            nova("Give each its own Application Insights."),
            rafe("No kiss for that one. That splits the trace in two, and we don't split. The application map draws each program as a node, one dot in a network, named by its cloud role name. Set the OTEL service name setting differently in each. OTEL is short for OpenTelemetry."),
            nova("Every relationship needs two names on the door. Fine. Where do I see one slow request, in detail?"),
            rafe("Transaction search, to find it. Then its end-to-end transaction details: every span as a timeline, bar under bar."),
            nova("Right answer. You do like the details. [kiss]"),
            rafe("Last one from me. The bill's too high, and the team is happy to keep one trace in ten. What do we set?"),
            nova("That's sampling, keeping only some traces. Set the sampling ratio to 0.1. Whole traces are kept or dropped together, never half a journey. And by default, recent versions keep about five traces a second."),
            rafe("Right answer. Though I'd keep every one of yours. [kiss]"),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("A call through a tool that isn't instrumented sends a blank envelope, and the other side starts a new trace. And a trace sampling didn't keep is gone for good. The KQL episode, the one about asking your logs questions, shows how to search the traces you did keep."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("If the question says one call to send traces from Python, configure azure monitor. If a called service starts its own trace, traceparent is missing. If services blur into one node, a different service name. To keep one in ten, a sampling ratio of 0.1. And for which service fails most, the application map."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create an Application Insights resource and copy its connection string. Install the distro with FastAPI, and call configure azure monitor in a tiny app. Add one custom span with an attribute, run it and hit it a few times. Then open Transaction search, and the end-to-end view of one request."),
            rafe("And I'll follow you anywhere. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun journey(api: String, func: String) = DiagramSpec(
        "One question travels from the phone through the API and a function to the database; every program sends its spans to Application Insights.",
        nodes = listOf(
            DNode("phone", "Android app", 0.24f, 0.13f, w = 0.4f, h = 0.18f),
            DNode("api", api, 0.76f, 0.13f, w = 0.4f, h = 0.18f, tone = 1, sub = "Python"),
            DNode("func", func, 0.76f, 0.48f, w = 0.4f, h = 0.2f, sub = "a function"),
            DNode("db", "Database", 0.76f, 0.83f, w = 0.4f, h = 0.26f, kind = NodeKind.CYLINDER),
            DNode("appi", "Application Insights", 0.24f, 0.62f, w = 0.4f, h = 0.26f, tone = 2, sub = "collects spans"),
        ),
        edges = listOf(
            DEdge("phone", "api", "question"),
            DEdge("api", "func", "call"),
            DEdge("func", "db"),
            DEdge("api", "appi", "spans", dashed = true),
            DEdge("func", "appi", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun spans() = DiagramSpec(
        "One trace, one trace id: each span is a timed step with a parent, and a custom span times your own code.",
        nodes = listOf(
            DNode("trace", "One trace id for every span", 0.5f, 0.1f, w = 0.76f, h = 0.15f, kind = NodeKind.NOTE, tone = 2),
            DNode("s1", "POST /ask", 0.5f, 0.33f, w = 0.9f, h = 0.16f, kind = NodeKind.PILL, tone = 1, sub = "server span"),
            DNode("s2", "rank-notes", 0.26f, 0.58f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, sub = "custom span"),
            DNode("s3", "Call Summarize", 0.75f, 0.58f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, sub = "client span"),
            DNode("s4", "Database read", 0.75f, 0.84f, w = 0.44f, h = 0.16f, kind = NodeKind.PILL, sub = "child of the call"),
        ),
        edges = listOf(
            DEdge("s1", "s2"),
            DEdge("s1", "s3"),
            DEdge("s3", "s4"),
        ),
        aspect = 0.8f,
    )

    private fun propagation(api: String, func: String) = DiagramSpec(
        "The caller's instrumentation adds the traceparent header; the receiver reads it and continues the same trace.",
        nodes = listOf(
            DNode("api", api, 0.2f, 0.28f, w = 0.36f, h = 0.24f, tone = 1, sub = "the caller"),
            DNode("func", func, 0.8f, 0.28f, w = 0.36f, h = 0.24f, sub = "the receiver"),
            DNode("hdr", "traceparent header", 0.5f, 0.76f, w = 0.62f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "trace id and parent id"),
        ),
        edges = listOf(
            DEdge("api", "func", "web call"),
            DEdge("api", "hdr", "adds"),
            DEdge("hdr", "func", "reads"),
        ),
        aspect = 0.8f,
    )

    private fun distro() = DiagramSpec(
        "The distro bundles OpenTelemetry, the instrumentations and an exporter; the connection string says where to send.",
        nodes = listOf(
            DNode("pkg", "The distro", 0.5f, 0.13f, w = 0.62f, h = 0.2f, tone = 1, sub = "azure-monitor-opentelemetry"),
            DNode("inst", "Instrumentations", 0.18f, 0.48f, w = 0.34f, h = 0.22f, sub = "FastAPI, requests, SDK"),
            DNode("otel", "OpenTelemetry", 0.5f, 0.48f, w = 0.28f, h = 0.22f, sub = "the SDK"),
            DNode("exp", "Exporter", 0.82f, 0.48f, w = 0.34f, h = 0.22f, sub = "sends spans"),
            DNode("conn", "Connection string", 0.27f, 0.84f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "environment variable"),
            DNode("appi", "Application Insights", 0.77f, 0.84f, w = 0.42f, h = 0.2f, tone = 2),
        ),
        edges = listOf(
            DEdge("pkg", "inst"),
            DEdge("pkg", "otel"),
            DEdge("pkg", "exp"),
            DEdge("exp", "appi"),
            DEdge("conn", "appi", "address"),
        ),
        aspect = 0.8f,
    )

    private fun views() = DiagramSpec(
        "Transaction search explains one request as a timeline; the application map shows the whole system, one box per cloud role.",
        nodes = listOf(
            DNode("appi", "Application Insights", 0.5f, 0.13f, w = 0.62f, h = 0.2f, tone = 1),
            DNode("search", "Transaction search", 0.25f, 0.5f, w = 0.44f, h = 0.22f, sub = "one request"),
            DNode("map", "Application map", 0.75f, 0.5f, w = 0.44f, h = 0.22f, sub = "the whole system"),
            DNode("timeline", "End-to-end details", 0.25f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "spans as a timeline"),
            DNode("role", "Cloud role name", 0.75f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "OTEL_SERVICE_NAME"),
        ),
        edges = listOf(
            DEdge("appi", "search"),
            DEdge("appi", "map"),
            DEdge("search", "timeline"),
            DEdge("map", "role", "box names"),
        ),
        aspect = 0.8f,
    )

    private fun sampling() = DiagramSpec(
        "Sampling keeps whole traces or drops them whole; the default keeps about five a second, a ratio keeps a fixed share.",
        nodes = listOf(
            DNode("all", "Every trace", 0.24f, 0.2f, w = 0.42f, h = 0.22f, sub = "all requests"),
            DNode("kept", "Kept, whole", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "about 5 a second"),
            DNode("dropped", "Dropped, whole", 0.24f, 0.64f, w = 0.42f, h = 0.22f, tone = 2, sub = "never found later"),
            DNode("ratio", "sampling_ratio=0.1", 0.76f, 0.64f, w = 0.42f, h = 0.22f, kind = NodeKind.PILL, sub = "one in ten"),
        ),
        edges = listOf(
            DEdge("all", "kept"),
            DEdge("all", "dropped"),
            DEdge("kept", "ratio", "or fixed"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where tracing stops: an uninstrumented call breaks the trace, a dropped trace is gone, and a late start misses requests.",
        nodes = listOf(
            DNode("blank", "Not instrumented", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "blank envelope"),
            DNode("dropped", "Not sampled", 0.76f, 0.2f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "gone for good"),
            DNode("late", "Configured late", 0.26f, 0.76f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "first requests missed"),
            DNode("kql", "KQL episode", 0.76f, 0.76f, w = 0.44f, h = 0.22f, tone = 1, sub = "search what you kept"),
        ),
        edges = listOf(DEdge("dropped", "kql", dashed = true)),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's tracing questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("one", "One call from Python", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "configure_azure_monitor"),
            DNode("split", "A separate trace", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "traceparent missing"),
            DNode("blob", "One blob on the map", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "OTEL_SERVICE_NAME"),
            DNode("tenth", "Keep one in ten", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sampling_ratio=0.1"),
            DNode("glance", "Fails most, at a glance", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "application map"),
            DNode("part", "Time one part", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "custom span"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("one", "split", "blob", "tenth", "glance", "part")
    private val configureCode = BoardCard("Python", "configure_azure_monitor(logger_name=\"coach\")  # before the app serves", code = true)
    private val spanCode = BoardCard("Python", "with tracer.start_as_current_span(\"rank-notes\") as s: s.set_attribute(\"topic\", t)", code = true)
    private val headerCode = BoardCard("Header", "traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01", code = true)
    private val envCode = BoardCard("Settings", "APPLICATIONINSIGHTS_CONNECTION_STRING, OTEL_SERVICE_NAME=coach-api", code = true)
    private val traced = BoardCard("Traced for you", "FastAPI coming in, requests going out, and the Azure SDK's calls.")
    private val today = BoardCard("Today", "Application Insights, the distro in FastAPI, a custom span, one timeline.")

    private fun classicBoard() = PodcastBoard(
        "ai-opentelemetry",
        listOf(
            // 0: "Hi, and welcome back. Today is about following one request..."
            Scene(
                0, "Following one request", journey("Coach API", "Summarize"),
                beats = listOf(
                    Beat(0, card = BoardCard("A tracking number", "Like a parcel scanned at every depot: the whole trip, stop by stop.")),
                    Beat(2, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The Two Tracks Coach", "The phone calls the Coach API, a Python program on Azure.")),
                    Beat(4, show = setOf("func", "db"), light = setOf("func"), pulse = "api" to "func", card = BoardCard("A function", "Code Azure runs only when it's called. Summarize reads the notes.")),
                    Beat(6, light = setOf("api", "func"), card = BoardCard("Every log says: I was fine", "Matching logs by the clock fails: a hundred students, one second.")),
                    Beat(10, show = setOf("appi"), light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("OpenTelemetry", "The open standard for distributed tracing: a shared rulebook.")),
                ),
            ),
            // 11: "Okay. What words does the rulebook use?"
            Scene(
                11, "Traces and spans", spans(),
                beats = listOf(
                    Beat(12, show = setOf("trace"), light = setOf("trace"), card = BoardCard("Trace", "The whole trip of one request, shop to door, under one trace id.")),
                    Beat(14, show = setOf("s1", "s3"), light = setOf("s3"), pulse = "s1" to "s3", card = BoardCard("Span", "One stop, stamped in and out: in at 9:00, out at 9:40, so 40 minutes.")),
                    Beat(16, show = setOf("s4"), light = setOf("s4"), pulse = "s3" to "s4", card = BoardCard("Boxes inside boxes", "Each span's parent started it: the question, Summarize, the read.")),
                    Beat(18, light = setOf("s1"), card = BoardCard("Attribute", "A name and a value on a span: coach.topic = cosmos.")),
                ),
            ),
            // 19: "Where do all these spans go?"
            Scene(
                19, "Where the spans go", journey("Coach API", "Summarize"),
                beats = listOf(
                    Beat(20, light = setOf("appi"), pulse = "func" to "appi", card = BoardCard("Application Insights", "Collects telemetry and stitches the spans back together by trace id.")),
                    Beat(22, light = setOf("appi", "api"), pulse = "api" to "appi", card = BoardCard("Telemetry", "What the app reports about itself, like a fitness tracker's readings.")),
                ),
            ),
            // 25: "But how does the function know the API's trace id?..."
            Scene(
                25, "Passing the trace along", propagation("Coach API", "Summarize"),
                beats = listOf(
                    Beat(25, show = setOf("api", "func"), light = setOf("func")),
                    Beat(26, show = setOf("hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = BoardCard("A header", "Extra information on the outside of a web call, like an envelope.")),
                    Beat(28, light = setOf("func"), pulse = "hdr" to "func", card = headerCode),
                    Beat(30, light = setOf("api", "func"), pulse = "api" to "func", card = BoardCard("Instrumentation", "Ready-made code around your tools: it adds and reads traceparent.")),
                ),
            ),
            // 31: "And how do I get this instrumentation into my Python?"
            Scene(
                31, "The distro", distro(),
                beats = listOf(
                    Beat(32, show = setOf("pkg", "inst", "otel", "exp"), light = setOf("pkg"), pulse = "pkg" to "inst", card = BoardCard("A meal kit", "OpenTelemetry, an exporter and the instrumentations, in one package.")),
                    Beat(34, light = setOf("pkg"), card = configureCode),
                    Beat(36, show = setOf("conn", "appi"), light = setOf("conn"), pulse = "conn" to "appi", card = envCode),
                    Beat(38, light = setOf("inst"), pulse = "pkg" to "inst", card = traced),
                ),
            ),
            // 41: "And if I want to time just one part of my own code?"
            Scene(
                41, "A custom span", spans(),
                beats = listOf(
                    Beat(42, show = setOf("s1", "s2"), light = setOf("s2"), pulse = "s1" to "s2", card = spanCode),
                ),
            ),
            // 43: "Okay. Where do I actually see all this?"
            Scene(
                43, "Where to look", views(),
                beats = listOf(
                    Beat(44, show = setOf("appi", "search", "timeline"), light = setOf("search", "timeline"), pulse = "search" to "timeline", card = BoardCard("Transaction search", "Find one request, then its end-to-end details as a timeline.")),
                    Beat(46, show = setOf("map", "role"), light = setOf("map", "role"), pulse = "map" to "role", card = BoardCard("Application map", "One node per program, named by OTEL_SERVICE_NAME.")),
                    Beat(50, light = setOf("timeline"), card = BoardCard("The slow evening", "40 ms in the API, nearly 2 s in Summarize: one database query.")),
                ),
            ),
            // 51: "Does it keep every single request? That sounds expensive."
            Scene(
                51, "Sampling", sampling(),
                beats = listOf(
                    Beat(52, show = setOf("all", "kept", "dropped", "ratio"), light = setOf("ratio"), pulse = "kept" to "ratio", card = BoardCard("Sampling", "Keep some traces, whole. For one in ten, sampling_ratio=0.1.")),
                    Beat(56, light = setOf("dropped"), card = BoardCard("The usual mistakes", "Configured late, default names, a pasted string, a blank envelope.")),
                ),
            ),
            // 59: "So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("one", "split", "blob", "tenth", "glance"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer.")),
                    Beat(62, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-opentelemetry--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Two Islands..."
            Scene(
                0, "The Two Islands", journey("Quiz API", "Grader"),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The case", "Marking takes forever, and every log says: not me.")),
                    Beat(2, show = setOf("func", "db"), light = setOf("func"), pulse = "api" to "func", card = BoardCard("The grader", "A function: code Azure runs only when it's called.")),
                    Beat(4, show = setOf("appi"), light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("Distributed tracing", "Follow one request through every program, like a scanned parcel.")),
                ),
            ),
            // 5: "With what?"
            Scene(
                5, "Traces and spans", spans(),
                beats = listOf(
                    Beat(6, card = BoardCard("OpenTelemetry", "The open standard for tracing: a shared rulebook, like chess rules.")),
                    Beat(8, show = setOf("trace"), light = setOf("trace"), card = BoardCard("Trace", "The whole journey of one request; its trace id is the tracking number.")),
                    Beat(10, show = setOf("s1", "s3", "s4"), light = setOf("s1", "s3"), pulse = "s1" to "s3", card = BoardCard("Span", "One stop, stamped in and out: in at 9:00, out at 9:40, so 40 minutes.")),
                ),
            ),
            // 13: "And where do the records end up?"
            Scene(
                13, "Two islands, no bridge", journey("Quiz API", "Grader"),
                beats = listOf(
                    Beat(14, light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("Telemetry", "What the app reports about itself, like a fitness tracker's readings.")),
                    Beat(18, light = setOf("appi", "func"), pulse = "func" to "appi", card = BoardCard("The evidence", "The grader's spans showed up as a separate trace.")),
                ),
            ),
            // 19: "Suspect one: the grader's managed identity..."
            Scene(
                19, "The blank envelope", propagation("Quiz API", "Grader"),
                beats = listOf(
                    Beat(19, show = setOf("api", "func"), light = setOf("func"), card = BoardCard("Suspect: managed identity", "A badge for access. It carries nothing about tracing.")),
                    Beat(22, show = setOf("hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = headerCode),
                    Beat(26, light = setOf("api"), card = BoardCard("Clue one: a raw socket helper", "Nothing instruments it, so the envelope went out blank.")),
                    Beat(28, light = setOf("api", "func"), pulse = "hdr" to "func", card = BoardCard("The fix: requests", "An instrumented library adds traceparent: one trace again.")),
                ),
            ),
            // 29: "Distro? Is that another suspect?"
            Scene(
                29, "The witness: the distro", distro(),
                beats = listOf(
                    Beat(30, show = setOf("pkg", "inst", "otel", "exp"), light = setOf("pkg"), pulse = "pkg" to "inst", card = configureCode),
                    Beat(32, light = setOf("inst"), card = BoardCard("Functions too", "In host.json, telemetryMode is OpenTelemetry; add the distro.")),
                    Beat(34, show = setOf("conn", "appi"), light = setOf("conn"), pulse = "conn" to "appi", card = BoardCard("Clue two: a pasted string", "The connection string belongs in an environment variable.")),
                ),
            ),
            // 35: "Case closed, then? One trace, everything joined."
            Scene(
                35, "Clue three: one blob", views(),
                beats = listOf(
                    Beat(36, show = setOf("appi", "map"), light = setOf("map"), pulse = "appi" to "map", card = BoardCard("Application map", "Each box a node, one program. QuizCraft's showed one blob.")),
                    Beat(38, show = setOf("role"), light = setOf("role"), pulse = "map" to "role", card = BoardCard("Cloud role name", "Not a second resource: set OTEL_SERVICE_NAME differently in each.")),
                    Beat(40, show = setOf("search", "timeline"), light = setOf("timeline"), pulse = "search" to "timeline", card = BoardCard("End-to-end details", "40 ms in the quiz API, nearly 2 s in the grader's database read.")),
                ),
            ),
            // 41: "Last loose end. Some slow requests weren't in there at all."
            Scene(
                41, "The missing requests", sampling(),
                beats = listOf(
                    Beat(42, show = setOf("all", "kept", "dropped", "ratio"), light = setOf("dropped", "ratio"), pulse = "all" to "dropped", card = BoardCard("Sampling", "About 5 traces a second by default; sampling_ratio=0.1 keeps one in ten.")),
                ),
            ),
            // 45: "And if I want to time just the marking code?"
            Scene(
                45, "A custom span", spans(),
                beats = listOf(
                    Beat(46, show = setOf("s1", "s2"), light = setOf("s2"), pulse = "s1" to "s2", card = spanCode),
                ),
            ),
            // 47: "So, the case summary for the exam?"
            Scene(
                47, "Case closed", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("one", "split", "blob", "tenth", "part"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-opentelemetry--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "Tracing week", journey("Coach API", "Summarize"),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Tracing week", "The Coach team added tracing to the Two Tracks Coach.")),
                    Beat(2, show = setOf("func", "db"), light = setOf("func"), pulse = "api" to "func", card = BoardCard("One question's journey", "Phone, Coach API, Summarize, then the database.")),
                    Beat(4, show = setOf("appi"), light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("OpenTelemetry", "The open standard: a trace per request, with one trace id.")),
                ),
            ),
            // 7: "And each stop along the way?"
            Scene(
                7, "Spans", spans(),
                beats = listOf(
                    Beat(8, show = setOf("trace", "s1", "s3", "s4"), light = setOf("s3"), pulse = "s1" to "s3", card = BoardCard("Span", "One stop, stamped in and out: in at 9:00, out at 9:40, so 40 minutes.")),
                    Beat(12, light = setOf("trace"), card = BoardCard("Telemetry", "What the app reports about itself, like a fitness tracker's readings.")),
                ),
            ),
            // 13: "Okay. Who went first?"
            Scene(
                13, "Monday: Tom", distro(),
                beats = listOf(
                    Beat(14, show = setOf("pkg", "inst", "otel", "exp"), light = setOf("pkg"), pulse = "pkg" to "exp", card = BoardCard("The distro", "Microsoft's bundle: OpenTelemetry, instrumentations, an exporter.")),
                    Beat(16, light = setOf("pkg"), card = configureCode),
                ),
            ),
            // 17: "Poor Tom. Tuesday?"
            Scene(
                17, "Tuesday: Marcus", distro(),
                beats = listOf(
                    Beat(18, show = setOf("conn", "appi"), light = setOf("conn"), pulse = "conn" to "appi", card = BoardCard("Pasted into the source", "The connection string, there for everyone to see.")),
                    Beat(20, light = setOf("conn"), card = envCode),
                ),
            ),
            // 21: "Wednesday. Go."
            Scene(
                21, "Wednesday: the intern", propagation("Coach API", "Summarize"),
                beats = listOf(
                    Beat(22, show = setOf("api", "func"), light = setOf("func"), card = BoardCard("Two journeys", "A homemade helper, and Summarize started its own trace.")),
                    Beat(26, show = setOf("hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = headerCode),
                    Beat(28, light = setOf("api"), pulse = "hdr" to "func", card = BoardCard("Instrumentation", "Wrapped around requests, it writes the envelope. The helper didn't.")),
                    Beat(30, light = setOf("api", "func"), card = traced),
                ),
            ),
            // 33: "Thursday. Who shouted blob?"
            Scene(
                33, "Thursday: Dana", views(),
                beats = listOf(
                    Beat(34, show = setOf("appi", "map"), light = setOf("map"), pulse = "appi" to "map", card = BoardCard("One confused blob", "Two programs drawn as one node, a single box.")),
                    Beat(36, show = setOf("role"), light = setOf("role"), pulse = "map" to "role", card = BoardCard("Cloud role name", "OTEL_SERVICE_NAME: coach-api in one, summarize in the other.")),
                ),
            ),
            // 37: "And Friday? There's always a Friday."
            Scene(
                37, "Friday: Priya", sampling(),
                beats = listOf(
                    Beat(38, show = setOf("all", "dropped"), light = setOf("dropped"), pulse = "all" to "dropped", card = BoardCard("A missing request", "Transaction search couldn't find it. Tracing wasn't broken.")),
                    Beat(40, show = setOf("kept", "ratio"), light = setOf("ratio"), pulse = "kept" to "ratio", card = BoardCard("Sampling", "About 5 traces a second by default; one in ten with a ratio of 0.1.")),
                ),
            ),
            // 41: "Did they find what was slow, in the end?"
            Scene(
                41, "What was slow", spans(),
                beats = listOf(
                    Beat(42, show = setOf("s1", "s2"), light = setOf("s2", "s4"), pulse = "s1" to "s2", card = spanCode),
                ),
            ),
            // 45: "So, the gossip I should remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("one", "split", "blob", "tenth"), card = BoardCard("Listen for the phrase", "Tom, the intern, Dana and Priya: one answer each.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-opentelemetry--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "One little journey", journey("Coach API", "Summarize"),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Following one little request on its journey, all the way home.")),
                    Beat(2, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The Two Tracks Coach", "The phone calls the Coach API, a Python program on Azure.")),
                    Beat(4, show = setOf("func", "db"), light = setOf("func"), pulse = "api" to "func", card = BoardCard("A function", "Summarize: code Azure wakes only when it's called.")),
                    Beat(6, light = setOf("api", "func", "db"), card = BoardCard("Distributed tracing", "One request, followed through every program. OpenTelemetry's rules.")),
                ),
            ),
            // 9: "Rules. Tell me the first one."
            Scene(
                9, "Traces and spans", spans(),
                beats = listOf(
                    Beat(10, show = setOf("trace"), light = setOf("trace"), card = BoardCard("Trace", "The whole journey, with a trace id: the tracking number.")),
                    Beat(12, show = setOf("s1", "s3", "s4"), light = setOf("s3"), pulse = "s1" to "s3", card = BoardCard("Span", "One stop, stamped in and out: in at 9:00, out at 9:40, so 40 minutes.")),
                ),
            ),
            // 13: "A family tree of little records. Where do they all go to sleep?"
            Scene(
                13, "Where spans sleep", journey("Coach API", "Summarize"),
                beats = listOf(
                    Beat(14, light = setOf("appi"), pulse = "api" to "appi", card = BoardCard("Application Insights", "Collects telemetry and stitches spans together by trace id.")),
                    Beat(16, light = setOf("appi", "api"), card = BoardCard("Telemetry", "What the app reports about itself, like a fitness tracker's readings.")),
                ),
            ),
            // 17: "But the function is a different program..."
            Scene(
                17, "A note on the envelope", propagation("Coach API", "Summarize"),
                beats = listOf(
                    Beat(18, show = setOf("api", "func", "hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = headerCode),
                    Beat(22, light = setOf("func"), pulse = "hdr" to "func", card = BoardCard("Instrumentation", "Ready-made code that records spans and writes the envelope.")),
                ),
            ),
            // 23: "And how does that get into my Python?"
            Scene(
                23, "The meal kit", distro(),
                beats = listOf(
                    Beat(24, show = setOf("pkg", "inst", "otel", "exp"), light = setOf("pkg"), pulse = "pkg" to "inst", card = configureCode),
                    Beat(26, show = setOf("conn", "appi"), light = setOf("conn"), pulse = "conn" to "appi", card = BoardCard("Connection string", "Where to send: the address of your Application Insights.")),
                    Beat(28, light = setOf("conn"), card = envCode),
                    Beat(30, light = setOf("inst"), pulse = "pkg" to "inst", card = traced),
                ),
            ),
            // 31: "And if I want to watch just one part of my own code? Closely."
            Scene(
                31, "Watching closely", spans(),
                beats = listOf(
                    Beat(32, show = setOf("s1", "s2"), light = setOf("s2"), pulse = "s1" to "s2", card = spanCode),
                ),
            ),
            // 35: "Now, where do I actually look?"
            Scene(
                35, "Where to look", views(),
                beats = listOf(
                    Beat(36, show = setOf("appi", "search", "timeline"), light = setOf("search", "timeline"), pulse = "search" to "timeline", card = BoardCard("Transaction search", "One request, then every span as a timeline, bar under bar.")),
                    Beat(38, show = setOf("map", "role"), light = setOf("map", "role"), pulse = "map" to "role", card = BoardCard("Application map", "One node per program, named by OTEL_SERVICE_NAME.")),
                ),
            ),
            // 41: "Does it keep every request? Every single one?"
            Scene(
                41, "Sampling", sampling(),
                beats = listOf(
                    Beat(42, show = setOf("all", "kept", "dropped", "ratio"), light = setOf("ratio"), pulse = "kept" to "ratio", card = BoardCard("Sampling", "Whole traces kept or dropped. One in ten: sampling_ratio=0.1.")),
                ),
            ),
            // 43: "Now the part I've been waiting for. Where does all this stop working?"
            Scene(
                43, "Where it stops", limits(),
                beats = listOf(
                    Beat(44, light = setOf("blank", "dropped"), pulse = "dropped" to "kql", card = BoardCard("Where tracing stops", "A blank envelope makes two islands; a dropped trace is gone.")),
                ),
            ),
            // 45: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("one", "split", "blob", "tenth", "glance"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-opentelemetry--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", journey("Coach API", "Summarize"),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "The most devoted thing in the cloud: tracing.")),
                    Beat(2, show = setOf("appi"), light = setOf("appi"), card = BoardCard("OpenTelemetry", "The open standard for following one request everywhere it goes.")),
                    Beat(4, show = setOf("phone", "api", "func", "db"), light = setOf("api", "func"), pulse = "api" to "func", card = BoardCard("The journey", "Phone, Coach API, Summarize, then the database.")),
                ),
            ),
            // 8: "Exactly. So each request gets a trace..."
            Scene(
                8, "A family of spans", spans(),
                beats = listOf(
                    Beat(8, show = setOf("trace"), light = setOf("trace"), card = BoardCard("Trace", "One journey, shop to door, under one trace id.")),
                    Beat(10, show = setOf("s1", "s3", "s4"), light = setOf("s3"), pulse = "s1" to "s3", card = BoardCard("Span", "One stop, stamped in and out: in at 9:00, out at 9:40, so 40 minutes.")),
                    Beat(12, light = setOf("trace"), card = BoardCard("Telemetry", "What the app reports about itself, like a fitness tracker's readings.")),
                ),
            ),
            // 13: "Quiz time, darling, exam style..."
            Scene(
                13, "Carrying your number", propagation("Coach API", "Summarize"),
                beats = listOf(
                    Beat(13, show = setOf("api", "func"), light = setOf("func"), card = BoardCard("Quiz: a separate trace", "Summarize's work shows up as its own trace. What went missing?")),
                    Beat(14, show = setOf("hdr"), light = setOf("hdr"), pulse = "api" to "hdr", card = headerCode),
                    Beat(15, light = setOf("hdr", "func"), pulse = "hdr" to "func", card = BoardCard("Right answer", "Traceparent carries the trace id to the next program.")),
                    Beat(16, light = setOf("api"), card = BoardCard("Instrumentation", "Ready-made code that writes the envelope. A homemade helper won't.")),
                ),
            ),
            // 17: "So how do I get instrumented, in Python?"
            Scene(
                17, "Timing is everything", distro(),
                beats = listOf(
                    Beat(18, show = setOf("pkg", "inst", "otel", "exp"), light = setOf("pkg"), pulse = "pkg" to "inst", card = BoardCard("Quiz: one call?", "Which one call sets up the distro in Python?")),
                    Beat(20, light = setOf("pkg"), card = configureCode),
                    Beat(22, show = setOf("conn", "appi"), light = setOf("conn"), pulse = "conn" to "appi", card = BoardCard("Connection string", "In an environment variable, never in the code.")),
                    Beat(24, light = setOf("inst"), pulse = "pkg" to "inst", card = traced),
                ),
            ),
            // 27: "And if I only want to watch one part of you closely?"
            Scene(
                27, "Watching closely", spans(),
                beats = listOf(
                    Beat(28, show = setOf("s1", "s2"), light = setOf("s2"), pulse = "s1" to "s2", card = spanCode),
                ),
            ),
            // 29: "Two services, one Application Insights..."
            Scene(
                29, "Two names on the door", views(),
                beats = listOf(
                    Beat(29, show = setOf("appi", "map"), light = setOf("map"), pulse = "appi" to "map", card = BoardCard("Quiz: one blob", "Two services show up on the application map as one blob.")),
                    Beat(31, show = setOf("role"), light = setOf("role"), pulse = "map" to "role", card = BoardCard("No kiss: set the name", "Not a second resource: OTEL_SERVICE_NAME differs in each.")),
                    Beat(33, show = setOf("search", "timeline"), light = setOf("search", "timeline"), pulse = "search" to "timeline", card = BoardCard("Right answer", "Transaction search, then the end-to-end details.")),
                ),
            ),
            // 35: "Last one from me. The bill's too high..."
            Scene(
                35, "Every one of yours", sampling(),
                beats = listOf(
                    Beat(35, show = setOf("all", "kept"), light = setOf("all"), pulse = "all" to "kept", card = BoardCard("Quiz: one in ten", "The bill's too high; keep one trace in ten. What do you set?")),
                    Beat(36, show = setOf("dropped", "ratio"), light = setOf("ratio"), pulse = "kept" to "ratio", card = BoardCard("Right answer", "sampling_ratio=0.1. Whole traces, never half a journey.")),
                ),
            ),
            // 38: "And where does it all stop working, Rafe?..."
            Scene(
                38, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(39, light = setOf("blank", "dropped", "kql"), pulse = "dropped" to "kql", card = BoardCard("Where tracing stops", "A blank envelope makes a new trace; a dropped trace is gone.")),
                ),
            ),
            // 40: "Last question, for the exam. What do we remember?"
            Scene(
                40, "For the exam", exam(),
                beats = listOf(
                    Beat(41, show = all6, light = setOf("one", "split", "blob", "tenth", "glance"), card = BoardCard("Listen for the phrase", "Five for five.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
