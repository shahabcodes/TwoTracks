package com.shahaabapps.core.content

/** The Monitor module's Application Insights and availability topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledMonitor {
    private val appInsightsCase = Podcast(
        "az-appinsights",
        listOf(
            maya("Welcome to Case Files. Today's case: The Missing Exceptions. A small plant-watering app for Android, called Leafy, reminds people when to water their plants. Its backend runs on Azure App Service and keeps the schedules in a database. Since Tuesday, users say saving a schedule sometimes fails. But the exceptions chart is perfectly flat. Zero errors."),
            leo("[chuckle] A crime with no fingerprints. Zero errors, and a lot of thirsty plants. Where do we even look?"),
            maya("Where the team should have been looking all along. Application Insights. Picture a flight recorder for your app. It quietly writes down every request coming in, every call your app makes out, and every crash. Each recorded item is called telemetry, a fancy word for measurements sent back from far away."),
            leo("And what is it really, under the flight recorder?"),
            maya("It's part of Azure Monitor. Under the picture, every item becomes a row in a table, inside a Log Analytics workspace. Requests go in the requests table, the calls your app makes out go in dependencies, crashes in exceptions, and the log lines your code writes in traces. It's a database of what your app did."),
            leo("Then my first suspect is obvious. The telemetry isn't arriving at all. Somebody set it up wrong."),
            maya("[pause] Good instinct, wrong suspect. The requests table is full, and it shows the failures: saves answering 500, a server error. So the recorder is plugged in and sending. The team even used the connection string, the address label that tells the app where to send its telemetry, and not the old instrumentation key on its own, which new regions no longer accept."),
            leo("Suspect two, then. Sampling. I've heard it throws data away to save money. Maybe it threw away the exceptions."),
            maya("Adaptive sampling does keep only a share of the items when traffic is heavy. It's the default in the dot net SDK. But it keeps related items together, and it scales the counts back up, so your charts stay roughly right. It would thin the exceptions out. It would never wipe out every single one."),
            leo("So requests failed, and no exceptions were recorded. How do I see what one failed request actually did?"),
            maya("That's the clue that cracks it. Every item one request causes carries the same operation id, like a tracking number on a parcel. Open one failed save in the end-to-end transaction view, and you see its whole chain, tied together by that id."),
            leo("And how does the id get from one place to the next? Is that magic?"),
            maya("No magic. The SDK inside your backend stamps the id on everything it records, and adds it as a header on every call the backend makes out. If the next service is watched too, it reads that header and carries on with the same id. That's how one request can be followed through three services."),
            leo("So what does the chain of a failed save show?"),
            maya("The request. A dependency call to the database, which failed. And then one log line, save failed, with the error message. And there it is. The developer caught the error and wrote it as a plain log line, through ILogger, with only the message."),
            leo("[pause] Oh. So it's a trace, not an exception. The errors were in the wrong drawer all along."),
            maya("Exactly. Errors logged as plain lines land in traces, unless you pass the exception object itself. Pass it, and the error lands in exceptions, with its stack trace. The chart was flat because everyone was looking in the exceptions table."),
            leo("Then that's it. Pass the exception, done. Can I go home?"),
            maya("Not quite. There's a second witness. Read that log line again. It also has the user's email address and home town in it, written into a custom property so support could search by user."),
            leo("[sigh] Is that so bad? It's our own telemetry. Nobody outside sees it."),
            maya("Telemetry is kept for ninety days by default, and anyone with reader access can query it. That's a lot of colleagues reading a list of strangers' emails. So personal data stays out of custom properties. Leafy took it out the same afternoon."),
            leo("Any more clues in the file?"),
            maya("One from the week before. To see everything, someone switched sampling off during a busy spring weekend, when everyone was planting. That month, monitoring was the biggest line on the invoice. Turning sampling off under heavy load means paying for every single item."),
            leo("Fine. Sampling stays on. Now, how would I actually ask the workspace a question, like how many saves failed?"),
            maya("With KQL, the Kusto Query Language. You start with a table and add small steps. Where filters the rows. Summarize groups and counts them. Render draws a chart. So: requests, where success is false, summarize the count by name and result code. That's the query that first showed the 500s."),
            leo("And if Leafy wants to know which plants people schedule most?"),
            maya("That's a custom event, your own business moment. TrackEvent, named schedule saved, with the plant type as a property. Properties are text, for filtering. Measurements are numbers, for charts. And for one number you watch over time, like plants per user, there's TrackMetric."),
            leo("So do I need to write code for all of that?"),
            maya("For the basics, no. On App Service, Functions or AKS, auto-instrumentation is a switch in the portal: requests, dependencies and exceptions arrive with no code change. Custom events need the SDK, a small code library, plus the connection string."),
            leo("Where does this stop working? Can it watch the Android app itself?"),
            maya("That's the edge. Application Insights here watches your backend, from the moment a request arrives. A crash on the phone, before it ever calls you, never reaches it. And it records, but it doesn't wake anyone up at night. Being told when things break is the Availability tests and alerts episode."),
            leo("And when they deploy the fix?"),
            maya("They keep Live Metrics open. It shows requests, failures and CPU with about one second of delay. As the new version takes the traffic, the failure line should drop to zero. For a safer way to swap versions, the Deployment slots and scaling episode covers it."),
            leo("So, the case summary for the exam?"),
            maya("Case closed, with four rules. If the question says cut the cost but keep the counts accurate, the answer is sampling. If it says follow a slow request through several services, it's the operation id and the end-to-end view. If it says watch a deployment live, it's Live Metrics. And counting business actions by topic is TrackEvent with a property."),
            leo("And errors that seem to vanish?"),
            maya("Check traces. Errors logged as plain lines land there, not in exceptions. And remember, everything lives in a Log Analytics workspace that you query with KQL."),
            leo("Solved. Anything to try today?"),
            maya("Recreate the case. Create an Application Insights resource. Run any sample web app with its connection string. Make a few requests, and one that fails. Then, in Logs, run requests, summarize count, by result code, and find your failure. There's a five gigabyte free monthly allowance."),
            leo("A failure I cause on purpose. Much less stressful. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val appInsightsGossip = Podcast(
        "az-appinsights",
        listOf(
            maya("Did you hear what Sam did on Monday? Sam from the gym team. You know their Android app, the one for booking fitness classes, with a small backend on App Service? Sam wanted support to find members faster."),
            leo("[laugh] I heard there was a meeting with the word privacy in the title. What did Sam do?"),
            maya("Sam put every member's email and phone number into the custom properties of the backend's telemetry. Every class booked, with the member's details attached."),
            leo("Hold on. Telemetry? Custom properties? Plain words, please."),
            maya("Sure. The backend is watched by Application Insights. Picture a flight recorder for your app: it writes down every request coming in, every call going out, every crash. Each recorded item is telemetry. And a custom property is a label you attach to an item yourself."),
            leo("So what was wrong with Sam's labels?"),
            maya("Telemetry is kept for ninety days by default, and anyone with reader access can query it. So Sam's labels made a ninety-day phone book for half the office. Aisha from security cleaned it up that afternoon. No personal data in properties. Properties are for things like the class type, so you can filter by it."),
            leo("And under the flight recorder, what is the thing, actually?"),
            maya("It's part of Azure Monitor. Each item becomes a row in a table in a Log Analytics workspace. Requests in one table, dependencies, the calls your app makes out, in another, exceptions in another."),
            leo("So it's a database of what the app did. How do I ask it anything?"),
            maya("With KQL, the Kusto Query Language. Start with a table, then where to filter, summarize to group and count, and render to draw a chart."),
            leo("Give me one query I'd actually type."),
            maya("Requests, where success is false, summarize the count by result code. Table, filter, group. Now you can see how many bookings failed, and with which error. Add render at the end, and it's a chart for the Monday meeting."),
            leo("Okay. What happened on Tuesday?"),
            maya("Tuesday was Ben. The gym ran a new year sign-up rush, traffic went through the roof, and Ben decided he wanted every single item. So he switched sampling off."),
            leo("Sampling being what, exactly?"),
            maya("Keeping only a share of the items when traffic is heavy. Adaptive sampling is the default in the dot net SDK. Ben thought it made his counts wrong. And with sampling off under that load, the team paid for every single item. Finance noticed before Ben did."),
            leo("[sigh] But Ben had a point, surely? If you throw items away, your counts must drop."),
            maya("That's the surprise. Priya showed him. Adaptive sampling keeps related items together, so one request and its calls are kept or dropped as a group. And each kept item remembers how many it stands for, so the charts scale the counts back up. The numbers stay roughly right, and the bill shrinks."),
            leo("Nice. Wednesday?"),
            maya("Dana. Dana moved the backend to a new region and copied the settings over. Including the old instrumentation key, on its own. No telemetry arrived at all. Two quiet days, which Dana took as good news."),
            leo("[chuckle] Silence is golden. Until it isn't. What should Dana have used?"),
            maya("The connection string. It's the address label on the parcel: the setting that tells the SDK where to send its telemetry. New regions require it. The old key alone isn't enough any more."),
            leo("And the SDK is what, in plain words?"),
            maya("A small code library you add to your backend. Under the hood it gathers telemetry in your process and sends it to Azure in small batches. You need it for your own custom events. For the basics you don't: on App Service, Functions or AKS, auto-instrumentation is a switch in the portal, and requests, dependencies and exceptions arrive with no code change."),
            leo("Thursday. I know there was a Thursday."),
            maya("The intern. A member complained that booking a spin class kept failing. So the intern queried the exceptions table. Empty. They told everyone, with real pride, that the backend had no errors at all."),
            leo("And it did?"),
            maya("Plenty. The code caught each error and wrote it as a plain log line through ILogger, with just the message. Those land in traces, not exceptions, unless you pass the exception object itself. Priya found them in a minute, by following one failed booking through the end-to-end view."),
            leo("How did she find one failed booking among thousands?"),
            maya("Every item one request causes shares an operation id, a tracking number. The request, its database calls, its log lines, all tagged the same. And the id travels: the SDK adds it as a header on every outgoing call, so the next service, if it's watched too, carries on with the same id."),
            leo("So what did the team do on Friday, after all that?"),
            maya("Something nice, for once. They added a custom event, class booked, sent with TrackEvent, with the class type as a property. Properties are text, for filtering. Measurements are numbers, for charts. Now there's a chart of bookings per hour, by class type. Then they shipped it with Live Metrics open, a view of requests, failures and CPU about one second behind."),
            leo("Why watch it live? Couldn't they just check the charts the next morning?"),
            maya("By the next morning, every member who tried to book a class has met the bug. With Live Metrics open, Priya saw failures, requests and CPU as the new version took the traffic, and could roll back within minutes if the failure line jumped. It's for watching, as it happens, not for digging into last week."),
            leo("Is there anything it can't do? I bet the intern wants to know."),
            maya("It watches the backend, not the phone. A crash on the phone, before it calls you, never reaches it. And it records, but it doesn't wake anyone at night. For that you want alerts, which is the Availability tests and alerts episode. And if the slow part is the same database read again and again, the Redis cache episode shows how to skip it."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If the question says cut the cost but keep the counts accurate, the answer is sampling. If it says follow a slow request through several services, it's the operation id and the end-to-end view. If it says watch a deployment live, it's Live Metrics. And counting business actions, split by something, is TrackEvent with a property."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create an Application Insights resource. Run any sample web app with its connection string. Make a few requests, and one that fails. Then, in Logs, run requests, summarize count, by result code. The workspace has a five gigabyte free monthly allowance."),
            leo("No phone numbers in my properties. Got it, Sam. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val availabilityCase = Podcast(
        "az-availability",
        listOf(
            maya("Welcome to Case Files. Today's case: The Alert Nobody Read. A small bike rental company runs an Android app. You scan a bike, the app calls a backend on Azure App Service, and the lock opens. Last Saturday, from nine until noon, no bike would unlock. Three hours. And the team found out from a one-star review."),
            leo("[chuckle] A flat tyre of a morning. Detective Leo reporting. My first suspect: nobody was watching. No tests, no alerts."),
            maya("[pause] The obvious suspect, and wrong. There was an availability test. Picture a robot customer: on a schedule, it visits your app from different places in the world, and checks that it answers properly."),
            leo("And what is it really, under the robot?"),
            maya("A standard test is a web request sent by Microsoft's test agents, from up to sixteen regions. It checks the status code, the page content, and that the certificate behind the padlock is still valid. Each try is written as a row of availability results in Application Insights. The Application Insights episode covers where those rows live."),
            leo("So what did the results say on Saturday?"),
            maya("Red, from two minutes past nine. Every five minutes, from five regions, all failing. The robot customer knew straight away."),
            leo("Then suspect two. The alert rule never fired."),
            maya("Let's check. An alert rule is a smoke detector. It watches a signal, and when a condition is met, it goes off. Theirs was a metric alert, which compares a number against a threshold, a line you draw. Availability below a hundred percent, over five minutes. And the alert history says: fired, at seven minutes past nine."),
            leo("How does it actually decide? Is it watching every second?"),
            maya("Not every second. On a schedule, say every minute, it takes the metric over a window, say the last five minutes, and compares it with the line. Cross the line, and the alert's state becomes fired. When the condition clears, a metric alert can resolve itself."),
            leo("So the robot saw it, the smoke detector went off, and still nobody came. [pause] Who did the alert tell?"),
            maya("That's the clue that cracks it. The alert sent an email to one address. Jonah's. The developer who built the backend. Who was on a beach that Saturday, with his phone at the bottom of a bag."),
            leo("[sigh] Poor Jonah. So the fix is to add more emails to the alert?"),
            maya("Close, but not like that. The fix is an action group. Think of a phone tree: a reusable list of who to tell and how. An email, a text, a push to the Azure mobile app, or automation, like running a function. Every alert rule points at the group. When the on-call person changes, you update the group once, not every rule."),
            leo("Would a smarter kind of alert have caught it any sooner?"),
            maya("Not here, but know the others. A log search alert runs a KQL query on a schedule, like more than ten errors of one type in fifteen minutes. An activity log alert watches management actions, like someone deleting a resource."),
            leo("And for a bike app that's quiet all week and wild on Saturday? A fixed line would be wrong half the time."),
            maya("Then a dynamic threshold, which learns what normal looks like, instead of a fixed line. Smart detection spots unusual patterns on its own. And an availability alert fires when, say, three of five regions fail."),
            leo("Okay. Action group, everyone gets told. Done?"),
            maya("Not yet. Ask why nobody noticed the red test on their own dashboard. Because there was a second test on that dashboard, and it had been red for six weeks."),
            leo("Six weeks! Why?"),
            maya("Someone pointed a standard test at the unlock service, which lives inside a private network. A standard test runs from Microsoft's public regions, so it can't reach a private endpoint at all. It failed every five minutes, for weeks, and the team learned to ignore red."),
            leo("The test that cried wolf. So how do you watch something private?"),
            maya("From inside. A small function on a timer, running in the same network, calls the endpoint and reports the result with TrackAvailability. The result shows up beside the standard tests, and alerts can use it the same way. The Azure Functions episode covers timer triggers."),
            leo("Two clues down. Anything else in the file?"),
            maya("The bill. Last month's App Service bill had doubled, and the autoscale settings explain it. Autoscale adds or removes copies of your app, called instances, as the load changes. It lives in Azure Monitor, with a minimum, a maximum, a default count, and rules."),
            leo("And the rules were what?"),
            maya("Only one. Scale out: CPU above seventy percent for ten minutes, add one. No scale in rule at all. So after a sunny weekend it grew to five instances, and never shrank. Always pair a scale out rule with a scale in rule."),
            leo("Easy. I'll add one. Remove an instance when CPU is below seventy."),
            maya("[laugh] And that's how you open the next case. Out above seventy, in below seventy. The two rules meet at the same number, so they argue. Add, remove, add, remove. It's called flapping."),
            leo("So what's the right fix?"),
            maya("A gap between the thresholds: out above seventy, in below thirty. And a cooldown, a pause after each scale action before it checks again. Under the hood, autoscale averages the metric over the whole duration you set, ten minutes here, so one short spike doesn't count."),
            leo("What about busy Saturday mornings? Those are predictable."),
            maya("A schedule rule. Weekends, eight till noon, keep at least three. And if work piles up in a queue, a rule can follow another resource, like more than five hundred Service Bus messages, add two. The Service Bus episode covers the queue itself."),
            leo("Where does autoscale stop working?"),
            maya("Two edges. It changes how many instances you have, scaling out and in. A bigger tier is scaling up, and that's a manual change. And on App Service it needs a plan of Standard or above. A Functions consumption plan scales itself, without your rules. The Deployment slots and scaling episode goes further."),
            leo("So, the case summary for the exam?"),
            maya("Case closed. If the endpoint is public, it's a standard availability test. If it's inside a private network, it's TrackAvailability from code running inside it. If the question says run a KQL query on a schedule, it's a log search alert. If a metric crosses a line, it's a metric alert."),
            leo("And for the autoscale questions?"),
            maya("If instances flap up and down, add a gap between the thresholds and a cooldown. If the plan never shrinks, add a scale in rule. And alerts go to an action group, never one person's inbox."),
            leo("Solved. Anything to try today?"),
            maya("Recreate the case. Add a standard availability test to any public URL. Create an action group with your email. Create a metric alert on availability results below a hundred. Then stop the app, and wait for the email to arrive."),
            leo("And this time, someone reads it. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val availabilityGossip = Podcast(
        "az-availability",
        listOf(
            maya("Did you hear what Dana did with the quiz app? The Android trivia game, with the small backend on App Service. Dana set up autoscale for the big Thursday quiz night, and then went on holiday."),
            leo("[laugh] I heard the word invoice said very loudly in the kitchen. What happened?"),
            maya("Autoscale first, in plain words. It adds or removes copies of your app, called instances, as the load changes. Dana wrote one rule. CPU above seventy percent for ten minutes, add an instance. And that was it."),
            leo("Sounds sensible. Busy quiz night, more copies."),
            maya("Busy quiz night, more copies. Then Friday, Saturday, the whole next week, still six copies. There was no scale in rule, so the plan grew and never shrank, and the bill grew with it. Always pair a scale out rule with a scale in rule."),
            leo("And what is autoscale, really? A button on the app?"),
            maya("It lives in Azure Monitor, as a setting attached to the App Service plan. A profile with a minimum, a maximum and a default count, plus rules. Every so often it averages the metric over the time you chose, compares it with each rule, and adds or removes instances."),
            leo("So who added the scale in rule?"),
            maya("Well, here's where Tom comes in. Tom saw Dana's bill and added the scale in rule himself. Remove one when CPU drops below seventy."),
            leo("[sigh] Below seventy. The same number as the other rule."),
            maya("You see it. Above seventy, add. That spreads the load, so CPU drops below seventy, remove. Now it's back above. Add, remove, add, remove, all evening. It's called flapping, and the instance chart looked like a saw blade."),
            leo("How did it get fixed?"),
            maya("Priya fixed it. A gap between the thresholds: out above seventy, in below thirty. And a cooldown, a pause after each scale action before it checks again. Plus a schedule rule for the quiz itself: Thursdays, seven till ten in the evening, keep at least three."),
            leo("Does autoscale make the server bigger too?"),
            maya("No, and that's an exam favourite. Autoscale changes how many instances, scaling out and in. A bigger tier is scaling up, and that's a manual change. On App Service it also needs a plan of Standard or above. The Deployment slots and scaling episode goes deeper."),
            leo("Okay, autoscale drama done. What else happened this week?"),
            maya("Marcus. Marcus wanted to know whether the question service was up. It's only reachable inside the company's private network. So he added a standard availability test to it."),
            leo("Availability test, in plain words?"),
            maya("A robot customer. On a schedule, it visits your endpoint from up to sixteen regions, and checks the status code, the content, and the certificate. Under the picture, it's Microsoft's test agents sending web requests, and each try is written to the availability results in Application Insights."),
            leo("Is that the old ping test I keep hearing about?"),
            maya("The URL ping test is the classic one, a simple get, and it's retired. Move those to standard tests. For a public endpoint, like the quiz app's own sign-up page, a standard test is the default: every five minutes, from five regions."),
            leo("So Marcus's robot couldn't get in."),
            maya("Never. Those agents run from Microsoft's public regions, so a private endpoint is out of reach. Red, every five minutes. Aisha fixed it: a small function on a timer, inside the network, calls the service and reports with TrackAvailability. The Azure Functions episode covers timer triggers."),
            leo("[chuckle] So the inside reports out. Anyone else?"),
            maya("Nina. Nina made a metric alert, a rule that fires when a number crosses a threshold over a window. Server errors above five in five minutes. A perfect rule. And it emailed one inbox. Nina's."),
            leo("Wait, how does a metric alert decide? Is it watching every second?"),
            maya("On a schedule, say every minute, it takes the metric over the window, five minutes here, and compares it with the line. Cross it, and the alert's state becomes fired. When the errors stop, a metric alert can close itself again."),
            leo("Let me guess. Nina was off that night."),
            maya("At her sister's wedding. The quiz backend threw errors for an hour, and Nina's phone was in her bag. Priya moved every alert to an action group. A phone tree: a reusable list of who to tell and how. Email, text, a push to the Azure mobile app, or automation, like a function. Change the on-call person once, in the group."),
            leo("Are there other kinds of alert I should know?"),
            maya("A log search alert runs a KQL query on a schedule, like quiz games finished dropping to zero for a day. An activity log alert watches management actions, like someone deleting a resource. Smart detection spots unusual patterns. And an availability alert fires when, say, three of five regions fail."),
            leo("And a fixed threshold? Quiz traffic is quiet all week and wild on Thursday."),
            maya("That's what a dynamic threshold is for. It learns what normal looks like, instead of a fixed number. Severity runs from zero, critical, to four, verbose. And alert processing rules can mute notifications during a maintenance window."),
            leo("Where does all this stop working? Can an alert fix things too?"),
            maya("It tells people, and through the action group it can start automation, but it doesn't know why things broke. For that you open the telemetry itself, which is the Application Insights episode. And a green test only means the endpoint answered what you asked it to check, not that every feature works."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the endpoint is public, the answer is a standard availability test. If it's in a private network, TrackAvailability from code inside it. If it says run a KQL query on a schedule, it's a log search alert. If instances flap, add a gap and a cooldown. And if the plan never shrinks, add a scale in rule."),
            leo("And something to try today, so I'm not the story next week?"),
            maya("Add a standard availability test to any public URL. Create an action group with your email. Create a metric alert on availability results below a hundred. Then stop the app, and wait for the email. Alerts cost cents, and a few tests are free."),
            leo("And keep my phone out of my bag. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(appInsightsCase, appInsightsGossip, availabilityCase, availabilityGossip)

    private fun appInsightsCaseBoard() = PodcastBoard(
        "az-appinsights--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Missing Exceptions..."
            Scene(
                0, "The Missing Exceptions",
                DiagramSpec(
                    "Leafy's Android app saves a watering schedule through its backend; saves fail, yet the exceptions chart shows zero.",
                    nodes = listOf(
                        DNode("phone", "Leafy app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "Save failed", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("chart", "Exceptions: zero", 0.76f, 0.66f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "a flat chart"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "save"),
                        DEdge("phone", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "err", "chart"), light = setOf("err", "chart"), pulse = "phone" to "app", card = BoardCard("The case", "Saves fail since Tuesday. The exceptions chart says zero errors.")),
                ),
            ),
            // 2: "Where the team should have been looking all along. Application Insights..."
            Scene(
                2, "A flight recorder",
                DiagramSpec(
                    "Application Insights records what the backend does; each item becomes a row in a table of a Log Analytics workspace.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.2f, 0.2f, w = 0.32f, h = 0.2f),
                        DNode("ai", "Application Insights", 0.7f, 0.2f, w = 0.48f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("req", "requests", 0.26f, 0.6f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "calls coming in"),
                        DNode("dep", "dependencies", 0.74f, 0.6f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "calls going out"),
                        DNode("exc", "exceptions", 0.26f, 0.86f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "crashes"),
                        DNode("trc", "traces", 0.74f, 0.86f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "log lines"),
                    ),
                    edges = listOf(
                        DEdge("app", "ai", "telemetry"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("app", "ai"), light = setOf("ai"), pulse = "app" to "ai", card = BoardCard("Application Insights", "A flight recorder for your app. Each recorded item is telemetry.")),
                    Beat(4, show = setOf("req", "dep", "exc", "trc"), light = setOf("req", "dep", "exc", "trc"), card = BoardCard("Under the picture", "Rows in tables, in a Log Analytics workspace. Part of Azure Monitor.")),
                ),
            ),
            // 5: "Then my first suspect is obvious. The telemetry isn't arriving at all..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Telemetry is arriving and sampling never removes everything, so neither suspect explains a perfectly flat chart.",
                    nodes = listOf(
                        DNode("none", "Not arriving?", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "requests are full"),
                        DNode("samp", "Sampling?", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "keeps related items"),
                        DNode("conn", "Connection string", 0.5f, 0.74f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "not the key alone"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("none"), light = setOf("none")),
                    Beat(6, show = setOf("conn"), light = setOf("none", "conn"), card = BoardCard("Suspect one: cleared", "The requests table is full of failed saves, so telemetry arrives.")),
                    Beat(7, show = setOf("samp"), light = setOf("samp")),
                    Beat(8, card = BoardCard("Suspect two: cleared", "Sampling thins items out and scales counts back up. It never wipes them all.")),
                ),
            ),
            // 9: "So requests failed, and no exceptions were recorded..."
            Scene(
                9, "One request, one id",
                DiagramSpec(
                    "Every item one request causes shares its operation id, and the SDK passes the id on as a header to the next service.",
                    nodes = listOf(
                        DNode("req", "Failed save", 0.5f, 0.14f, w = 0.5f, h = 0.18f),
                        DNode("dep", "DB call", 0.18f, 0.5f, w = 0.3f, h = 0.16f, tone = 1),
                        DNode("trc", "Log line", 0.5f, 0.5f, w = 0.28f, h = 0.16f, tone = 1),
                        DNode("next", "Next service", 0.82f, 0.5f, w = 0.3f, h = 0.16f, tone = 1),
                        DNode("op", "Operation id", 0.5f, 0.84f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, sub = "the tracking number"),
                    ),
                    edges = listOf(
                        DEdge("req", "dep"),
                        DEdge("req", "trc"),
                        DEdge("req", "next", "header"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("req")),
                    Beat(10, show = setOf("dep", "trc", "op"), light = setOf("op"), pulse = "req" to "dep", card = BoardCard("Operation id", "One id on every item of a request. The end-to-end view follows it.")),
                    Beat(12, show = setOf("next"), light = setOf("next"), pulse = "req" to "next", card = BoardCard("No magic: a header", "The SDK adds the id to every outgoing call; the next service carries it on.")),
                ),
            ),
            // 13: "So what does the chain of a failed save show?"
            Scene(
                13, "The wrong drawer",
                DiagramSpec(
                    "An error written as a plain log line lands in traces; pass the exception object and it lands in exceptions.",
                    nodes = listOf(
                        DNode("line", "Save failed", 0.24f, 0.22f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "via ILogger"),
                        DNode("traces", "traces", 0.76f, 0.22f, w = 0.38f, h = 0.2f, tone = 1, sub = "where it landed"),
                        DNode("obj", "Pass the exception", 0.26f, 0.72f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("exc", "exceptions", 0.76f, 0.72f, w = 0.38f, h = 0.2f, tone = 2, sub = "where they looked"),
                    ),
                    edges = listOf(
                        DEdge("line", "traces", "message only"),
                        DEdge("obj", "exc"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("line")),
                    Beat(14, show = setOf("traces"), light = setOf("line", "traces"), pulse = "line" to "traces", card = BoardCard("The clue", "The error was caught and written as a plain log line, message only.")),
                    Beat(16, show = setOf("obj", "exc"), light = setOf("obj", "exc"), pulse = "obj" to "exc", card = BoardCard("C#", "logger.LogError(ex, \"Save failed\");", code = true)),
                ),
            ),
            // 17: "Then that's it. Pass the exception, done. Can I go home?"
            Scene(
                17, "The second witness",
                DiagramSpec(
                    "Telemetry is kept for ninety days and anyone with reader access can query it, so personal data stays out of properties.",
                    nodes = listOf(
                        DNode("line", "Log line", 0.24f, 0.24f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "email, home town"),
                        DNode("tel", "Telemetry", 0.76f, 0.24f, w = 0.36f, h = 0.2f, sub = "kept 90 days"),
                        DNode("readers", "Anyone with reader access", 0.5f, 0.76f, w = 0.7f, h = 0.2f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("line", "tel"),
                        DEdge("readers", "tel", "can query"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("line", "tel"), light = setOf("line"), pulse = "line" to "tel", card = BoardCard("The second witness", "The user's email and home town, written into a custom property.")),
                    Beat(20, show = setOf("readers"), light = setOf("readers"), pulse = "readers" to "tel", card = BoardCard("No personal data in properties", "Kept for 90 days by default; anyone with reader access can query it.")),
                    Beat(22, light = setOf("tel"), card = BoardCard("Sampling switched off", "Under heavy load you pay for every single item. Leave it on.")),
                ),
            ),
            // 23: "Fine. Sampling stays on. Now, how would I actually ask the workspace a question..."
            Scene(
                23, "Asking with KQL",
                DiagramSpec(
                    "Telemetry lands in a Log Analytics workspace, one table per kind, and KQL asks it questions step by step.",
                    nodes = listOf(
                        DNode("ai", "Application Insights", 0.26f, 0.2f, w = 0.48f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("la", "Log Analytics workspace", 0.76f, 0.2f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("kql", "KQL query", 0.5f, 0.62f, w = 0.4f, kind = NodeKind.PILL),
                        DNode("steps", "where, summarize, render", 0.5f, 0.88f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("ai", "la"),
                        DEdge("kql", "la", "asks"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("ai", "la"), pulse = "ai" to "la"),
                    Beat(24, show = setOf("kql", "steps"), light = setOf("kql", "steps"), pulse = "kql" to "la", card = BoardCard("KQL", "requests | where success == false | summarize count() by name, resultCode", code = true)),
                ),
            ),
            // 25: "And if Leafy wants to know which plants people schedule most?"
            Scene(
                25, "Custom events",
                DiagramSpec(
                    "A custom event marks a business moment; its properties are text for filtering, its measurements numbers for charts.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.22f, 0.22f, w = 0.36f, h = 0.2f),
                        DNode("ev", "ScheduleSaved", 0.72f, 0.22f, w = 0.46f, kind = NodeKind.PILL, sub = "a custom event"),
                        DNode("prop", "plantType", 0.26f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "property: text"),
                        DNode("meas", "plants", 0.74f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "measurement: number"),
                    ),
                    edges = listOf(
                        DEdge("app", "ev", "TrackEvent"),
                        DEdge("ev", "prop"),
                        DEdge("ev", "meas"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("app")),
                    Beat(26, show = setOf("ev", "prop", "meas"), light = setOf("ev", "prop"), pulse = "app" to "ev", card = BoardCard("Which plants?", "TrackEvent, schedule saved, with the plant type as a property.")),
                    Beat(28, light = setOf("app"), card = BoardCard("Two ways in", "Auto-instrumentation: no code. Custom events: the SDK and a connection string.")),
                ),
            ),
            // 29: "Where does this stop working? Can it watch the Android app itself?"
            Scene(
                29, "Where it stops",
                DiagramSpec(
                    "Application Insights watches the backend from the moment a request arrives; alerts are what tell people.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.22f, w = 0.4f, h = 0.2f, sub = "crash before a call"),
                        DNode("app", "Backend", 0.24f, 0.74f, w = 0.36f, h = 0.2f),
                        DNode("ai", "Application Insights", 0.76f, 0.74f, w = 0.44f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("alerts", "Alerts", 0.76f, 0.22f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "the next episode"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "calls"),
                        DEdge("app", "ai", "telemetry"),
                        DEdge("ai", "alerts", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("phone", "app", "ai"), light = setOf("phone"), pulse = "app" to "ai"),
                    Beat(30, show = setOf("alerts"), light = setOf("app", "alerts"), card = BoardCard("The edge", "It watches the backend. Being told: the Availability tests and alerts episode.")),
                ),
            ),
            // 31: "And when they deploy the fix?"
            Scene(
                31, "Live Metrics",
                DiagramSpec(
                    "Live Metrics shows requests, failures and CPU with about one second of delay while the fix is deployed.",
                    nodes = listOf(
                        DNode("fix", "The fix", 0.2f, 0.3f, w = 0.32f, h = 0.2f, tone = 1),
                        DNode("app", "Leafy backend", 0.72f, 0.3f, w = 0.44f, h = 0.2f),
                        DNode("live", "Live Metrics", 0.5f, 0.8f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "about one second behind"),
                    ),
                    edges = listOf(
                        DEdge("fix", "app", "deploy"),
                        DEdge("app", "live"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("fix", "app"), pulse = "fix" to "app"),
                    Beat(32, show = setOf("live"), light = setOf("live"), pulse = "app" to "live", card = BoardCard("Watch the deploy live", "Requests, failures and CPU. The failure line should drop to zero.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed",
                DiagramSpec(
                    "The exam's Application Insights questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("cost", "Cut cost, keep counts", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sampling"),
                        DNode("trace", "Follow a slow request", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "operation id"),
                        DNode("live", "Watch a deploy live", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Live Metrics"),
                        DNode("count", "Count business actions", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TrackEvent"),
                        DNode("lost", "Errors gone missing", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "look in traces"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("cost", "trace", "live", "count"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, show = setOf("lost"), light = setOf("lost"), card = BoardCard("Errors missing?", "Plain log lines land in traces. Everything is queried with KQL.")),
                    Beat(38, card = BoardCard("Today", "A resource, a sample app, a failing request, then a KQL count by result code.")),
                ),
            ),
        ),
    )

    private fun appInsightsGossipBoard() = PodcastBoard(
        "az-appinsights--gossip",
        listOf(
            // 0: "Did you hear what Sam did on Monday? Sam from the gym team..."
            Scene(
                0, "What Sam did",
                DiagramSpec(
                    "The gym app's backend sends telemetry, and Sam attached every member's email and phone number to it.",
                    nodes = listOf(
                        DNode("phone", "Gym app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("prop", "Email, phone number", 0.76f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "custom properties"),
                        DNode("tel", "Telemetry", 0.76f, 0.66f, w = 0.36f, h = 0.2f, sub = "kept 90 days"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "book"),
                        DEdge("app", "tel"),
                        DEdge("prop", "tel", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app"), pulse = "phone" to "app", card = BoardCard("The gossip", "Sam wanted support to find members faster.")),
                    Beat(2, show = setOf("prop", "tel"), light = setOf("prop"), pulse = "prop" to "tel", card = BoardCard("What Sam did", "Every member's email and phone number, in the telemetry's properties.")),
                    Beat(4, light = setOf("tel"), pulse = "app" to "tel", card = BoardCard("Application Insights", "A flight recorder for your app. Each recorded item is telemetry.")),
                    Beat(6, light = setOf("prop", "tel"), card = BoardCard("No personal data in properties", "Kept 90 days; anyone with reader access can query it. Aisha's fix.")),
                ),
            ),
            // 7: "And under the flight recorder, what is the thing, actually?"
            Scene(
                7, "Under the picture",
                DiagramSpec(
                    "Telemetry lands in a Log Analytics workspace, one table per kind, and KQL asks it questions step by step.",
                    nodes = listOf(
                        DNode("ai", "Application Insights", 0.26f, 0.2f, w = 0.48f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("la", "Log Analytics workspace", 0.76f, 0.2f, w = 0.44f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("kql", "KQL query", 0.5f, 0.62f, w = 0.4f, kind = NodeKind.PILL),
                        DNode("steps", "where, summarize, render", 0.5f, 0.88f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("ai", "la"),
                        DEdge("kql", "la", "asks"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("ai")),
                    Beat(8, show = setOf("la"), light = setOf("la"), pulse = "ai" to "la", card = BoardCard("Log Analytics workspace", "One table per kind, part of Azure Monitor, asked with KQL.")),
                    Beat(12, show = setOf("kql", "steps"), light = setOf("kql", "steps"), pulse = "kql" to "la", card = BoardCard("KQL", "requests | where success == false | summarize count() by resultCode", code = true)),
                ),
            ),
            // 13: "Okay. What happened on Tuesday?"
            Scene(
                13, "Tuesday: Ben",
                DiagramSpec(
                    "Adaptive sampling keeps a share of related items under load and scales the counts back up.",
                    nodes = listOf(
                        DNode("all", "All telemetry", 0.2f, 0.3f, w = 0.34f, h = 0.22f, tone = 1),
                        DNode("samp", "Adaptive sampling", 0.72f, 0.3f, w = 0.46f, h = 0.22f, sub = "keeps related items"),
                        DNode("counts", "Counts stay roughly right", 0.5f, 0.8f, w = 0.64f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("all", "samp", "under load"),
                        DEdge("samp", "counts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("all"), light = setOf("all"), card = BoardCard("Sampling off", "A sign-up rush, and Ben paid for every single item.")),
                    Beat(16, show = setOf("samp"), light = setOf("samp"), pulse = "all" to "samp", card = BoardCard("Adaptive sampling", "The default in the dot net SDK. Keeps a share when traffic is heavy.")),
                    Beat(18, show = setOf("counts"), light = setOf("counts"), pulse = "samp" to "counts", card = BoardCard("How it works", "Each kept item remembers how many it stands for; charts scale back up.")),
                ),
            ),
            // 19: "Nice. Wednesday?"
            Scene(
                19, "Wednesday: Dana",
                DiagramSpec(
                    "The connection string tells the SDK where to send telemetry; auto-instrumentation needs no code at all.",
                    nodes = listOf(
                        DNode("auto", "Auto-instrumentation", 0.26f, 0.2f, w = 0.48f, h = 0.2f, tone = 1, sub = "no code change"),
                        DNode("sdk", "SDK", 0.26f, 0.58f, w = 0.48f, h = 0.2f, tone = 1, sub = "adds custom events"),
                        DNode("ai", "Application Insights", 0.78f, 0.4f, w = 0.4f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("conn", "Connection string", 0.3f, 0.88f, w = 0.5f, kind = NodeKind.NOTE, sub = "where to send it"),
                        DNode("key", "Key alone", 0.78f, 0.86f, w = 0.4f, kind = NodeKind.NOTE, tone = 2, sub = "new region: silence"),
                    ),
                    edges = listOf(
                        DEdge("auto", "ai"),
                        DEdge("sdk", "ai"),
                        DEdge("conn", "sdk", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("ai", "key"), light = setOf("key"), card = BoardCard("Dana's new region", "The old instrumentation key, on its own. No telemetry arrived at all.")),
                    Beat(22, show = setOf("sdk", "conn"), light = setOf("conn", "sdk"), pulse = "conn" to "sdk", card = BoardCard("Connection string", "The parcel's address label. New regions require it.")),
                    Beat(24, show = setOf("auto"), light = setOf("auto"), pulse = "auto" to "ai", card = BoardCard("Two ways in", "Auto-instrumentation: no code. The SDK: your own custom events.")),
                ),
            ),
            // 25: "Thursday. I know there was a Thursday."
            Scene(
                25, "Thursday: the intern",
                DiagramSpec(
                    "An error written as a plain log line lands in traces; pass the exception object and it lands in exceptions.",
                    nodes = listOf(
                        DNode("line", "Booking failed", 0.24f, 0.22f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "via ILogger"),
                        DNode("traces", "traces", 0.76f, 0.22f, w = 0.38f, h = 0.2f, tone = 1, sub = "where it landed"),
                        DNode("obj", "Pass the exception", 0.26f, 0.72f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("exc", "exceptions", 0.76f, 0.72f, w = 0.38f, h = 0.2f, tone = 2, sub = "empty"),
                    ),
                    edges = listOf(
                        DEdge("line", "traces", "message only"),
                        DEdge("obj", "exc"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("exc"), light = setOf("exc"), card = BoardCard("Exceptions table: empty", "So the intern announced the backend had no errors at all.")),
                    Beat(28, show = setOf("line", "traces", "obj"), light = setOf("line", "traces"), pulse = "line" to "traces", card = BoardCard("In traces all along", "Plain log lines land in traces unless you pass the exception object.")),
                ),
            ),
            // 29: "How did she find one failed booking among thousands?"
            Scene(
                29, "One booking, one id",
                DiagramSpec(
                    "Every item one request causes shares its operation id, and the SDK passes the id on as a header to the next service.",
                    nodes = listOf(
                        DNode("req", "Failed booking", 0.5f, 0.14f, w = 0.5f, h = 0.18f),
                        DNode("dep", "DB call", 0.18f, 0.5f, w = 0.3f, h = 0.16f, tone = 1),
                        DNode("trc", "Log line", 0.5f, 0.5f, w = 0.28f, h = 0.16f, tone = 1),
                        DNode("next", "Next service", 0.82f, 0.5f, w = 0.3f, h = 0.16f, tone = 1),
                        DNode("op", "Operation id", 0.5f, 0.84f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, sub = "the tracking number"),
                    ),
                    edges = listOf(
                        DEdge("req", "dep"),
                        DEdge("req", "trc"),
                        DEdge("req", "next", "header"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("req")),
                    Beat(30, show = setOf("dep", "trc", "next", "op"), light = setOf("op", "next"), pulse = "req" to "next", card = BoardCard("Operation id", "A tracking number on every item. The SDK sends it on as a header.")),
                ),
            ),
            // 31: "So what did the team do on Friday, after all that?"
            Scene(
                31, "Friday: something nice",
                DiagramSpec(
                    "A custom event marks a business moment; its properties are text for filtering, its measurements numbers for charts.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.22f, 0.22f, w = 0.36f, h = 0.2f),
                        DNode("ev", "ClassBooked", 0.72f, 0.22f, w = 0.46f, kind = NodeKind.PILL, sub = "a custom event"),
                        DNode("prop", "classType", 0.26f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "property: text"),
                        DNode("meas", "minutes", 0.74f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "measurement: number"),
                    ),
                    edges = listOf(
                        DEdge("app", "ev", "TrackEvent"),
                        DEdge("ev", "prop"),
                        DEdge("ev", "meas"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("app")),
                    Beat(32, show = setOf("ev", "prop", "meas"), light = setOf("ev", "prop"), pulse = "app" to "ev", card = BoardCard("Bookings per hour, by class", "TrackEvent, class booked, with the class type as a property.")),
                ),
            ),
            // 33: "Why watch it live? Couldn't they just check the charts the next morning?"
            Scene(
                33, "Live Metrics",
                DiagramSpec(
                    "Live Metrics shows requests, failures and CPU with about one second of delay while a new version ships.",
                    nodes = listOf(
                        DNode("ver", "New version", 0.2f, 0.3f, w = 0.32f, h = 0.2f, tone = 1),
                        DNode("app", "Gym backend", 0.72f, 0.3f, w = 0.44f, h = 0.2f),
                        DNode("live", "Live Metrics", 0.5f, 0.8f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "about one second behind"),
                    ),
                    edges = listOf(
                        DEdge("ver", "app", "deploy"),
                        DEdge("app", "live"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("ver", "app"), pulse = "ver" to "app"),
                    Beat(34, show = setOf("live"), light = setOf("live"), pulse = "app" to "live", card = BoardCard("Watch as it happens", "Failures, requests and CPU, a second behind. Roll back within minutes.")),
                ),
            ),
            // 35: "Is there anything it can't do? I bet the intern wants to know."
            Scene(
                35, "Where it stops",
                DiagramSpec(
                    "Application Insights watches the backend from the moment a request arrives; alerts are what tell people.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.22f, w = 0.4f, h = 0.2f, sub = "crash before a call"),
                        DNode("app", "Backend", 0.24f, 0.74f, w = 0.36f, h = 0.2f),
                        DNode("ai", "Application Insights", 0.76f, 0.74f, w = 0.44f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("alerts", "Alerts", 0.76f, 0.22f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "the next episode"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "calls"),
                        DEdge("app", "ai", "telemetry"),
                        DEdge("ai", "alerts", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("phone", "app", "ai", "alerts"), light = setOf("app", "alerts"), pulse = "app" to "ai", card = BoardCard("The edge", "Backend only. Alerts: their own episode. Repeated slow reads: Redis cache.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam",
                DiagramSpec(
                    "The exam's Application Insights questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("cost", "Cut cost, keep counts", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sampling"),
                        DNode("trace", "Follow a slow request", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "operation id"),
                        DNode("live", "Watch a deploy live", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Live Metrics"),
                        DNode("count", "Count business actions", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TrackEvent"),
                        DNode("kql", "Ask the data a question", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "KQL in Log Analytics"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("cost", "trace", "live", "count", "kql"), light = setOf("count"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, card = BoardCard("Today", "A resource, a sample app, a failing request, then a KQL count by result code.")),
                ),
            ),
        ),
    )

    private fun availabilityCaseBoard() = PodcastBoard(
        "az-availability--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Alert Nobody Read..."
            Scene(
                0, "The Alert Nobody Read",
                DiagramSpec(
                    "The bike app calls its backend to unlock a bike; for three hours nothing unlocked, and a review told the team.",
                    nodes = listOf(
                        DNode("phone", "Bike app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("lock", "No bike unlocks", 0.76f, 0.2f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("review", "One-star review", 0.76f, 0.66f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "how they found out"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "unlock"),
                        DEdge("phone", "lock", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "lock", "review"), light = setOf("lock", "review"), pulse = "phone" to "app", card = BoardCard("The case", "Saturday, nine to noon, no bike would unlock. A review told the team.")),
                ),
            ),
            // 2: "The obvious suspect, and wrong. There was an availability test..."
            Scene(
                2, "A robot customer",
                DiagramSpec(
                    "A standard availability test visits the backend from up to sixteen regions, and each try becomes an availability result.",
                    nodes = listOf(
                        DNode("test", "Standard test", 0.24f, 0.24f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "up to 16 regions"),
                        DNode("site", "Bike backend", 0.76f, 0.24f, w = 0.42f, h = 0.22f),
                        DNode("results", "Availability results", 0.5f, 0.76f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "red from 9:02"),
                    ),
                    edges = listOf(
                        DEdge("test", "site", "every 5 min"),
                        DEdge("site", "results", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("test"), light = setOf("test"), card = BoardCard("Suspect one: cleared", "There was an availability test, a robot customer on a schedule.")),
                    Beat(4, show = setOf("site", "results"), light = setOf("test", "results"), pulse = "test" to "site", card = BoardCard("Under the robot", "Web requests from Microsoft's test agents, written as availability results.")),
                    Beat(6, light = setOf("results"), pulse = "site" to "results", card = BoardCard("Red from 9:02", "Every five minutes, from five regions, all failing.")),
                ),
            ),
            // 7: "Then suspect two. The alert rule never fired."
            Scene(
                7, "The smoke detector",
                DiagramSpec(
                    "The metric alert watched availability, fired at seven past nine, and emailed one person's inbox.",
                    nodes = listOf(
                        DNode("sig", "Availability", 0.18f, 0.3f, w = 0.32f, h = 0.22f, tone = 1, sub = "below 100%"),
                        DNode("rule", "Metric alert", 0.7f, 0.3f, w = 0.4f, h = 0.22f, sub = "fired at 9:07"),
                        DNode("inbox", "Jonah's inbox", 0.7f, 0.78f, w = 0.4f, h = 0.2f, tone = 2, sub = "on a beach"),
                    ),
                    edges = listOf(
                        DEdge("sig", "rule"),
                        DEdge("rule", "inbox", "email"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("rule"), light = setOf("rule")),
                    Beat(8, show = setOf("sig"), light = setOf("sig", "rule"), pulse = "sig" to "rule", card = BoardCard("Suspect two: cleared", "A metric alert: a number against a line. It fired at 9:07.")),
                    Beat(10, light = setOf("rule"), card = BoardCard("How it decides", "Every minute, the metric over a window, compared with the line.")),
                    Beat(12, show = setOf("inbox"), light = setOf("inbox"), pulse = "rule" to "inbox", card = BoardCard("The clue", "One email address: Jonah's. On a beach, phone at the bottom of a bag.")),
                ),
            ),
            // 13: "Poor Jonah. So the fix is to add more emails to the alert?"
            Scene(
                13, "Who gets told",
                DiagramSpec(
                    "Many alert rules point at one action group, a reusable list of who to tell and how.",
                    nodes = listOf(
                        DNode("r1", "Rule", 0.14f, 0.2f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("r2", "Rule", 0.14f, 0.5f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("r3", "Rule", 0.14f, 0.8f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("ag", "Action group", 0.5f, 0.5f, w = 0.36f, h = 0.24f, sub = "the phone tree"),
                        DNode("email", "Email", 0.86f, 0.2f, h = 0.14f, tone = 2),
                        DNode("push", "Push", 0.86f, 0.5f, h = 0.14f, tone = 2),
                        DNode("auto", "Function", 0.86f, 0.8f, h = 0.14f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("r1", "ag"),
                        DEdge("r2", "ag"),
                        DEdge("r3", "ag"),
                        DEdge("ag", "email"),
                        DEdge("ag", "push"),
                        DEdge("ag", "auto"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("r1")),
                    Beat(14, show = setOf("r2", "r3", "ag", "email", "push", "auto"), light = setOf("ag"), pulse = "ag" to "push", card = BoardCard("Action group", "A phone tree. Change the on-call person once, not every rule.")),
                ),
            ),
            // 15: "Would a smarter kind of alert have caught it any sooner?"
            Scene(
                15, "Kinds of alert",
                DiagramSpec(
                    "Metric, log search, activity log, smart detection and availability alerts each watch a different signal.",
                    nodes = listOf(
                        DNode("metric", "Metric alert", 0.26f, 0.13f, w = 0.46f, h = 0.2f, sub = "a number vs a line"),
                        DNode("log", "Log search alert", 0.76f, 0.13f, w = 0.44f, h = 0.2f, sub = "KQL on a schedule"),
                        DNode("act", "Activity log alert", 0.26f, 0.47f, w = 0.46f, h = 0.2f, tone = 1, sub = "management actions"),
                        DNode("smart", "Smart detection", 0.76f, 0.47f, w = 0.44f, h = 0.2f, tone = 1, sub = "unusual patterns"),
                        DNode("avail", "Availability alert", 0.26f, 0.83f, w = 0.46f, h = 0.2f, tone = 1, sub = "3 of 5 regions fail"),
                        DNode("dyn", "Dynamic threshold", 0.76f, 0.83f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "learns normal"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("metric", "log", "act", "smart", "avail", "dyn"), light = setOf("log", "dyn"), card = BoardCard("Metric or log search?", "A number crossing a line, or a KQL query run on a schedule.")),
                ),
            ),
            // 19: "Okay. Action group, everyone gets told. Done?"
            Scene(
                19, "The test that cried wolf",
                DiagramSpec(
                    "A standard test cannot reach a private network; a timer function inside it reports with TrackAvailability.",
                    nodes = listOf(
                        DNode("test", "Standard test", 0.24f, 0.14f, w = 0.42f, kind = NodeKind.PILL, tone = 2),
                        DNode("tool", "Unlock service", 0.24f, 0.56f, w = 0.4f, h = 0.2f, sub = "private network"),
                        DNode("fn", "Timer function", 0.24f, 0.86f, w = 0.4f, tone = 1, sub = "inside the network"),
                        DNode("ai", "Application Insights", 0.76f, 0.66f, w = 0.42f, h = 0.28f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("test", "tool", "can't reach", dashed = true),
                        DEdge("fn", "tool"),
                        DEdge("fn", "ai", "TrackAvailability"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("test", "tool"), light = setOf("test"), card = BoardCard("Red for six weeks", "A second test the whole team had learned to ignore.")),
                    Beat(22, light = setOf("test", "tool"), pulse = "test" to "tool", card = BoardCard("A private endpoint", "Standard tests run from public regions. They can't reach it.")),
                    Beat(24, show = setOf("fn", "ai"), light = setOf("fn"), pulse = "fn" to "ai", card = BoardCard("From inside", "A timer function in the network reports with TrackAvailability.")),
                ),
            ),
            // 25: "Two clues down. Anything else in the file?"
            Scene(
                25, "The bill",
                DiagramSpec(
                    "Autoscale in Azure Monitor adds and removes instances between a minimum and a maximum, following its rules.",
                    nodes = listOf(
                        DNode("cpu", "CPU over 70%", 0.22f, 0.2f, w = 0.4f, kind = NodeKind.PILL, sub = "for 10 minutes"),
                        DNode("as", "Autoscale", 0.74f, 0.2f, w = 0.4f, h = 0.2f, sub = "min, max, default"),
                        DNode("i1", "Instance", 0.18f, 0.62f, w = 0.28f, tone = 1),
                        DNode("i2", "Instance", 0.5f, 0.62f, w = 0.28f, tone = 1),
                        DNode("i3", "Instance", 0.82f, 0.62f, w = 0.28f, tone = 1),
                        DNode("never", "No scale in rule", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("cpu", "as"),
                        DEdge("as", "i3", "add one"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("as", "i1", "i2"), light = setOf("as"), card = BoardCard("Autoscale", "Adds or removes instances. In Azure Monitor: min, max, default.")),
                    Beat(28, show = setOf("cpu", "i3", "never"), light = setOf("cpu", "never"), pulse = "as" to "i3", card = BoardCard("Only a scale out rule", "It grew to five instances and never shrank. Pair it with scale in.")),
                ),
            ),
            // 29: "Easy. I'll add one. Remove an instance when CPU is below seventy."
            Scene(
                29, "Flapping",
                DiagramSpec(
                    "Thresholds that meet make the rules fight; a gap between them and a cooldown keep it steady.",
                    nodes = listOf(
                        DNode("out", "Out above 70%", 0.26f, 0.16f, w = 0.44f, h = 0.18f, tone = 1),
                        DNode("in", "In below 70%", 0.76f, 0.16f, w = 0.42f, h = 0.18f, tone = 2),
                        DNode("flap", "Add, remove, add...", 0.5f, 0.46f, w = 0.56f, kind = NodeKind.NOTE),
                        DNode("fix", "In below 30%", 0.26f, 0.8f, w = 0.44f, h = 0.2f, sub = "a gap"),
                        DNode("cool", "Cooldown", 0.76f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "pause, then check"),
                    ),
                    edges = listOf(
                        DEdge("out", "in", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("out", "in"), pulse = "out" to "in"),
                    Beat(30, show = setOf("flap"), light = setOf("flap"), card = BoardCard("It flaps", "The same number in both rules. Add, remove, add, remove.")),
                    Beat(32, show = setOf("fix", "cool"), light = setOf("fix", "cool"), card = BoardCard("The fix", "Out at 70, in at 30, and a cooldown after each scale action.")),
                    Beat(34, light = setOf("out"), card = BoardCard("Schedule and queue rules", "Weekend mornings, at least three. Or Service Bus messages above 500.")),
                ),
            ),
            // 35: "Where does autoscale stop working?"
            Scene(
                35, "Where autoscale stops",
                DiagramSpec(
                    "Autoscale changes how many instances run; a bigger tier is a manual change, and a consumption plan scales itself.",
                    nodes = listOf(
                        DNode("out", "Scale out", 0.26f, 0.22f, w = 0.44f, h = 0.2f, tone = 1, sub = "automatic"),
                        DNode("up", "Scale up", 0.76f, 0.22f, w = 0.42f, h = 0.2f, tone = 2, sub = "manual"),
                        DNode("std", "Standard or above", 0.26f, 0.74f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "App Service"),
                        DNode("fn", "Consumption plan", 0.76f, 0.74f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "scales itself"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("out", "up"), light = setOf("out")),
                    Beat(36, show = setOf("std", "fn"), light = setOf("up", "fn"), card = BoardCard("The edges", "Scaling up is manual. A Functions consumption plan scales itself.")),
                ),
            ),
            // 37: "So, the case summary for the exam?"
            Scene(
                37, "Case closed",
                DiagramSpec(
                    "The exam's monitoring questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("pub", "Public endpoint", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "standard test"),
                        DNode("priv", "Private endpoint", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TrackAvailability"),
                        DNode("kql", "KQL on a schedule", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "log search alert"),
                        DNode("thr", "Metric over a line", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "metric alert"),
                        DNode("flap", "Instances flap", 0.26f, 0.83f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "gap and cooldown"),
                        DNode("who", "Who gets told", 0.76f, 0.83f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "an action group"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("pub", "priv", "kql", "thr"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, show = setOf("flap", "who"), light = setOf("flap", "who"), card = BoardCard("Never shrinks?", "Add a scale in rule to match the scale out rule.")),
                    Beat(42, card = BoardCard("Today", "A standard test, an action group, an alert below 100; stop the app, get an email.")),
                ),
            ),
        ),
    )

    private fun availabilityGossipBoard() = PodcastBoard(
        "az-availability--gossip",
        listOf(
            // 0: "Did you hear what Dana did with the quiz app?..."
            Scene(
                0, "What Dana did",
                DiagramSpec(
                    "Dana's autoscale had one rule, to add instances above 70 percent CPU, and nothing to take them away.",
                    nodes = listOf(
                        DNode("cpu", "CPU over 70%", 0.22f, 0.2f, w = 0.4f, kind = NodeKind.PILL, sub = "for 10 minutes"),
                        DNode("as", "Autoscale", 0.74f, 0.2f, w = 0.4f, h = 0.2f, sub = "min, max, default"),
                        DNode("i1", "Instance", 0.18f, 0.62f, w = 0.28f, tone = 1),
                        DNode("i2", "Instance", 0.5f, 0.62f, w = 0.28f, tone = 1),
                        DNode("i3", "Instance", 0.82f, 0.62f, w = 0.28f, tone = 1),
                        DNode("never", "No scale in rule", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("cpu", "as"),
                        DEdge("as", "i3", "add one"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("as", "i1"), light = setOf("as"), card = BoardCard("The gossip", "Dana set up autoscale for quiz night, then went on holiday.")),
                    Beat(2, show = setOf("cpu", "i2", "i3"), light = setOf("cpu"), pulse = "cpu" to "as", card = BoardCard("Autoscale", "Adds or removes copies of your app, called instances.")),
                    Beat(4, show = setOf("never"), light = setOf("i1", "i2", "i3", "never"), pulse = "as" to "i3", card = BoardCard("Six copies all week", "No scale in rule: it grew and never shrank. Pair out with in.")),
                    Beat(6, light = setOf("as"), card = BoardCard("Under the picture", "A setting on the App Service plan, in Azure Monitor: min, max, default.")),
                ),
            ),
            // 7: "So who added the scale in rule?"
            Scene(
                7, "Tom's scale in rule",
                DiagramSpec(
                    "Thresholds that meet make the rules fight; a gap between them and a cooldown keep it steady.",
                    nodes = listOf(
                        DNode("out", "Out above 70%", 0.26f, 0.16f, w = 0.44f, h = 0.18f, tone = 1),
                        DNode("in", "In below 70%", 0.76f, 0.16f, w = 0.42f, h = 0.18f, tone = 2),
                        DNode("flap", "Add, remove, add...", 0.5f, 0.46f, w = 0.56f, kind = NodeKind.NOTE),
                        DNode("fix", "In below 30%", 0.26f, 0.8f, w = 0.44f, h = 0.2f, sub = "a gap"),
                        DNode("cool", "Cooldown", 0.76f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "pause, then check"),
                    ),
                    edges = listOf(
                        DEdge("out", "in", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("out", "in"), light = setOf("in"), pulse = "out" to "in", card = BoardCard("Tom's rule", "Remove one below seventy. The same number as the other rule.")),
                    Beat(10, show = setOf("flap"), light = setOf("flap"), card = BoardCard("It flaps", "Add, remove, add, remove. The chart looked like a saw blade.")),
                    Beat(12, show = setOf("fix", "cool"), light = setOf("fix", "cool"), card = BoardCard("Priya's fix", "Out at 70, in at 30, a cooldown, and a schedule for quiz night.")),
                ),
            ),
            // 13: "Does autoscale make the server bigger too?"
            Scene(
                13, "Out, not up",
                DiagramSpec(
                    "Autoscale changes how many instances run; a bigger tier is scaling up, a manual change, and it needs Standard or above.",
                    nodes = listOf(
                        DNode("out", "Scale out", 0.26f, 0.22f, w = 0.44f, h = 0.2f, tone = 1, sub = "automatic"),
                        DNode("up", "Scale up", 0.76f, 0.22f, w = 0.42f, h = 0.2f, tone = 2, sub = "manual"),
                        DNode("std", "Standard or above", 0.5f, 0.74f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "App Service"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("out", "up"), light = setOf("up")),
                    Beat(14, show = setOf("std"), light = setOf("out", "up", "std"), card = BoardCard("An exam favourite", "Autoscale changes how many. A bigger tier is scaling up: manual.")),
                ),
            ),
            // 15: "Okay, autoscale drama done. What else happened this week?"
            Scene(
                15, "Marcus's robot",
                DiagramSpec(
                    "A standard test cannot reach a private network; a timer function inside it reports with TrackAvailability.",
                    nodes = listOf(
                        DNode("test", "Standard test", 0.24f, 0.14f, w = 0.42f, kind = NodeKind.PILL, tone = 2, sub = "up to 16 regions"),
                        DNode("tool", "Question service", 0.24f, 0.56f, w = 0.42f, h = 0.2f, sub = "private network"),
                        DNode("fn", "Timer function", 0.24f, 0.86f, w = 0.4f, tone = 1, sub = "inside the network"),
                        DNode("ai", "Application Insights", 0.76f, 0.66f, w = 0.42f, h = 0.28f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("test", "tool", "can't reach", dashed = true),
                        DEdge("fn", "tool"),
                        DEdge("fn", "ai", "TrackAvailability"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("test", "tool"), light = setOf("test", "tool"), card = BoardCard("What Marcus did", "A standard test on a service inside the private network.")),
                    Beat(18, light = setOf("test"), card = BoardCard("Availability test", "A robot customer: up to 16 regions; status, content, certificate.")),
                    Beat(20, card = BoardCard("URL ping test", "Retired. Move those to standard tests.")),
                    Beat(22, show = setOf("fn", "ai"), light = setOf("fn"), pulse = "fn" to "ai", card = BoardCard("Aisha's fix", "A timer function inside the network reports with TrackAvailability.")),
                ),
            ),
            // 23: "So the inside reports out. Anyone else?"
            Scene(
                23, "Nina's inbox",
                DiagramSpec(
                    "Nina's metric alert watched server errors and emailed only her own inbox.",
                    nodes = listOf(
                        DNode("sig", "Server errors", 0.18f, 0.3f, w = 0.32f, h = 0.22f, tone = 1, sub = "over 5 in 5 min"),
                        DNode("rule", "Metric alert", 0.7f, 0.3f, w = 0.4f, h = 0.22f, sub = "the smoke detector"),
                        DNode("inbox", "Nina's inbox", 0.7f, 0.78f, w = 0.4f, h = 0.2f, tone = 2, sub = "at a wedding"),
                    ),
                    edges = listOf(
                        DEdge("sig", "rule"),
                        DEdge("rule", "inbox", "email"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("sig", "rule", "inbox"), light = setOf("inbox"), pulse = "rule" to "inbox", card = BoardCard("Nina's metric alert", "Server errors above five in five minutes. Emailed one inbox.")),
                    Beat(26, light = setOf("sig", "rule"), pulse = "sig" to "rule", card = BoardCard("How it decides", "Every minute, the metric over the window, compared with the line.")),
                ),
            ),
            // 27: "Let me guess. Nina was off that night."
            Scene(
                27, "Who gets told",
                DiagramSpec(
                    "Many alert rules point at one action group, a reusable list of who to tell and how.",
                    nodes = listOf(
                        DNode("r1", "Rule", 0.14f, 0.2f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("r2", "Rule", 0.14f, 0.5f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("r3", "Rule", 0.14f, 0.8f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("ag", "Action group", 0.5f, 0.5f, w = 0.36f, h = 0.24f, sub = "the phone tree"),
                        DNode("email", "Email", 0.86f, 0.2f, h = 0.14f, tone = 2),
                        DNode("push", "Push", 0.86f, 0.5f, h = 0.14f, tone = 2),
                        DNode("auto", "Function", 0.86f, 0.8f, h = 0.14f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("r1", "ag"),
                        DEdge("r2", "ag"),
                        DEdge("r3", "ag"),
                        DEdge("ag", "email"),
                        DEdge("ag", "push"),
                        DEdge("ag", "auto"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("r1", "r2", "r3", "ag", "email", "push", "auto"), light = setOf("ag"), pulse = "ag" to "push", card = BoardCard("Action group", "A phone tree. Change the on-call person once, in the group.")),
                ),
            ),
            // 29: "Are there other kinds of alert I should know?"
            Scene(
                29, "Kinds of alert",
                DiagramSpec(
                    "Metric, log search, activity log, smart detection and availability alerts each watch a different signal.",
                    nodes = listOf(
                        DNode("metric", "Metric alert", 0.26f, 0.13f, w = 0.46f, h = 0.2f, sub = "a number vs a line"),
                        DNode("log", "Log search alert", 0.76f, 0.13f, w = 0.44f, h = 0.2f, sub = "KQL on a schedule"),
                        DNode("act", "Activity log alert", 0.26f, 0.47f, w = 0.46f, h = 0.2f, tone = 1, sub = "management actions"),
                        DNode("smart", "Smart detection", 0.76f, 0.47f, w = 0.44f, h = 0.2f, tone = 1, sub = "unusual patterns"),
                        DNode("avail", "Availability alert", 0.26f, 0.83f, w = 0.46f, h = 0.2f, tone = 1, sub = "3 of 5 regions fail"),
                        DNode("dyn", "Dynamic threshold", 0.76f, 0.83f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "learns normal"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("metric", "log", "act", "smart", "avail"), light = setOf("log"), card = BoardCard("Log search alert", "A KQL query on a schedule, like quiz games finished dropping to zero.")),
                    Beat(32, show = setOf("dyn"), light = setOf("dyn"), card = BoardCard("Dynamic threshold", "Learns what normal looks like. Quiet all week, wild on Thursday.")),
                ),
            ),
            // 33: "Where does all this stop working? Can an alert fix things too?"
            Scene(
                33, "Where it stops",
                DiagramSpec(
                    "Alerts tell people and can start automation; finding out why means opening the telemetry itself.",
                    nodes = listOf(
                        DNode("ag", "Action group", 0.24f, 0.3f, w = 0.4f, h = 0.22f, sub = "tells, automates"),
                        DNode("ai", "Application Insights", 0.76f, 0.3f, w = 0.44f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("green", "A green test", 0.5f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "it answered, that's all"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("ag"), light = setOf("ag")),
                    Beat(34, show = setOf("ai", "green"), light = setOf("ai", "green"), card = BoardCard("The edge", "Alerts tell. Why it broke: the Application Insights episode.")),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's monitoring questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("pub", "Public endpoint", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "standard test"),
                        DNode("priv", "Private endpoint", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "TrackAvailability"),
                        DNode("kql", "KQL on a schedule", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "log search alert"),
                        DNode("flap", "Instances flap", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "gap and cooldown"),
                        DNode("shrink", "Never shrinks", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "add a scale in rule"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("pub", "priv", "kql", "flap", "shrink"), light = setOf("shrink"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(38, card = BoardCard("Today", "A standard test, an action group, an alert below 100; stop the app, get an email.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy {
        listOf(appInsightsCaseBoard(), appInsightsGossipBoard(), availabilityCaseBoard(), availabilityGossipBoard())
    }
}
