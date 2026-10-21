package com.shahaabapps.core.content

/** Logging and monitoring: where logs go and who watches: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesMonitoring {
    private val classic = Podcast(
        "sa-monitoring",
        listOf(
            maya("Hi, and welcome back. Today is logging and monitoring, for AZ-305, the exam about designing Azure solutions. Our running story: Two Tracks goes global. The little Android app is now a company, with its backend, the server code the app talks to, on Azure, and users in Europe and Australia. And one night at 3 a.m., the API, the doorway the app calls, stopped answering."),
            leo("Let me guess. Nobody noticed until the users did. [sigh]"),
            maya("Until the one-star reviews did. So today is about three design choices: which records to collect, where to keep them, and who gets told. Azure has one service for all of it, Azure Monitor. Picture a building's security desk: cameras feed one wall of screens, a guard watches, and a phone list says who to ring at 3 a.m."),
            leo("Nice. So what are the cameras recording, in plain words?"),
            maya("Four kinds of data. First, metrics: numbers over time, like requests per second, or how busy a server is. Picture the speedometer in a car. Azure collects them on its own, and keeps them 93 days."),
            leo("Second?"),
            maya("The Activity log. It's the building's visitor book for management: who created, changed or deleted a resource. A resource is anything you make in Azure: a database, a web app, a vault. Also collected on its own, and kept 90 days."),
            leo("Ninety days. I'll remember that. Third?"),
            maya("Resource logs: what happened inside a resource. For example, every time someone reads a secret from Key Vault, the locked safe where Two Tracks keeps its passwords. And here's the trap. Resource logs are not collected at all, until you ask."),
            leo("Wait. Azure just drops them?"),
            maya("Gone. The way you ask is a diagnostic setting. Think of a post office redirect on the resource: it says which kinds of log to send, and where. One resource can have up to five, so audit logs can go one way and noisy logs another. The Activity log is routed the same way."),
            leo("And the fourth kind of data?"),
            maya("App telemetry, which means the records your own code sends home: each request, each error, each slow call to a database. It comes from Application Insights, a watcher you add to your app. Picture a fitness tracker on your code's wrist."),
            leo("Got it. So the post office redirect sends logs where, exactly?"),
            maya("Three main places. The first is a Log Analytics workspace. That's a searchable library of logs. Alerts and reports all read from it, so anything you must search or be warned about goes here."),
            leo("And how do I search a library of logs?"),
            maya("With KQL, the Kusto Query Language. It reads almost like a sentence: take this table, keep last night's rows, count them. Every record is a row in a table, and your question runs over the rows."),
            leo("Second place?"),
            maya("A storage account: a plain filing cabinet in the cloud. It's the cheapest place to park huge piles of logs for years, but it's hard to search. Good for an archive that auditors, the people who check your records, read once in a blue moon."),
            leo("And the third?"),
            maya("Event Hubs. It's a fast pipe, not a store. Logs flow through it, as they arrive, into some other tool. Say your security team uses a SIEM, short for security information and event management: software from another company that reads logs and hunts for attacks. Event Hubs streams the logs to it within seconds."),
            leo("So: library to search, cabinet to archive, pipe to stream."),
            maya("Exactly that. Now the big design question: how many workspaces? Microsoft's advice is to start with one, and add another only when a requirement forces it. One workspace is simpler to search, and simpler to look after."),
            leo("But Two Tracks has Europe and Australia. Doesn't that force it?"),
            maya("It does, because of data residency: a rule that data must stay inside one place. Logs about European users must stay in Europe, and Australian ones in Australia. So Two Tracks gets one workspace in each region."),
            leo("Region, in plain words?"),
            maya("A region is a group of Azure data centres in one part of the world, like a branch office. West Europe is one, Australia East another. You pick where each thing lives."),
            leo("Okay, but each team wants its own workspace too. The payments team, the app team."),
            maya("[pause] Tempting, and wrong. Inside one workspace, people already see only the logs of resources they have rights to. That's called resource-context access, like a hotel key card that only opens your own room. So split for residency, for separate companies, or for split billing. Not for teams."),
            leo("Right. Now the 3 a.m. problem. How does anyone get woken up?"),
            maya("With an alert rule. It's a tripwire. It can watch a metric going over a limit, a log search that finds rows, or an Activity log event, such as someone deleting a resource. For example, more than five percent of requests failing for five minutes."),
            leo("And when the tripwire goes off, who does it ring?"),
            maya("An action group. That's the phone list: email, text message, an app notification, a voice call. It can also call a webhook, which is a web address another system listens on, so a message lands in your team chat. And the key part: many alert rules share one action group."),
            leo("Why does sharing matter?"),
            maya("Picture twenty alert rules. With the phone number copied into each, the on-call engineer changes, and you edit twenty places. With one shared action group, you change one number."),
            leo("And if someone just wants to look around, not be woken?"),
            maya("A workbook: an interactive report mixing text, searches and charts, for people to explore. It notifies nobody. And on top of the workspace sits Microsoft Sentinel, a security guard for the logs: it hunts for signs of attack, and can respond on its own."),
            leo("How long does the workspace keep everything?"),
            maya("Most data, 30 days by default, and you can keep it searchable for up to two years. Long-term retention is a cheaper back room, up to 12 years in all. But cost comes mostly from how much you send in, priced per gigabyte. So send only the logs you need."),
            leo("And the auditors want seven years of the Activity log."),
            maya("Then route it with a diagnostic setting on the subscription, the billing account your resources live in, to a storage account. On its own, the Activity log forgets after 90 days. So Two Tracks sends audit logs twice: to the regional workspace to search, and to storage for seven years."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If logs must be searched or alerted on, a Log Analytics workspace. If they must be kept for years cheaply, a storage account. If they stream to an outside tool, Event Hubs. If the question says one place, or least effort, fewer workspaces; split only for residency. And if many alerts tell the same people, one action group."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a Log Analytics workspace in your nearest region. Add a diagnostic setting on a Key Vault, sending the audit category to it. Read a secret, then find the row with a short KQL query. Last, create an action group with your email, and an alert rule that uses it."),
            leo("Collect it, keep it, and tell someone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-monitoring",
        listOf(
            maya("Welcome to Case Files. Today's case: The Vault That Kept No Diary. Two Tracks has gone global: the Android app's backend, the server code the app talks to, runs on Azure for users in Europe and Australia. On Tuesday, a secret was read from Key Vault, the locked safe for passwords, by someone nobody recognised."),
            leo("Detective Leo, on the case. Easy. We check who read it."),
            maya("That's what the team said. They opened Azure Monitor, the service that collects records from everything you run in Azure. Picture a building's security desk: cameras, a wall of screens, and a phone list for 3 a.m. And for the vault, the screens were blank. Not one record of a single read."),
            leo("Then my first suspect: someone switched the logging off."),
            maya("[pause] Wrong suspect. Nobody switched anything off, because nothing was ever on. Clue one: the vault's resource logs, the records of what happens inside a resource, like each secret read, are not collected on their own. Azure drops them unless you ask."),
            leo("Drops them? Then how do you ask?"),
            maya("With a diagnostic setting. Think of a post office redirect on the vault: it says which kinds of log to send, and where. Under the picture, it's a setting on the resource, and Azure's own pipeline copies each new record to the places you name, within minutes."),
            leo("And Two Tracks never added one. So, case closed?"),
            maya("Not yet. They added one that afternoon. Now, where should it send the logs? Three choices. A Log Analytics workspace, which is a searchable library of logs. A storage account, a cheap filing cabinet. Or Event Hubs, a fast pipe to some other tool."),
            leo("Storage. Cheapest. Done."),
            maya("That was the junior admin's choice too, and it's clue two. The requirement said the security team must be alerted within minutes when a secret is read at an odd hour. A filing cabinet can't raise an alarm. It's hard to even search. For searching and alerting, logs go to the workspace."),
            leo("So how do you actually search that library?"),
            maya("With KQL, the Kusto Query Language. It reads almost like a sentence: take this table, keep last night's rows, count them. How it works: every record lands as a row in a table, and Azure Monitor runs your question over those rows."),
            leo("And the security team's own tool? They mentioned a SIEM."),
            maya("SIEM is short for security information and event management: software that reads logs from everywhere and hunts for attacks. If it lives outside Azure and needs the logs in near real time, a diagnostic setting streams them to Event Hubs, and the tool reads them as they arrive. Storage would leave it checking files."),
            leo("Third clue. Hit me."),
            maya("The auditors arrived. They want every management change from the last seven years: who created, changed or deleted each resource. That's the Activity log, the subscription's visitor book. A subscription is the billing account your resources live in."),
            leo("Suspect: easy. The Activity log keeps everything forever."),
            maya("Wrong again. It keeps 90 days on its own. After that it's gone, unless a diagnostic setting on the subscription routes it somewhere. For seven years, rarely read, at the lowest cost, that's a storage account. Finally, the filing cabinet earns its place."),
            leo("[chuckle] Fine. Fourth witness?"),
            maya("The workspace count. Two Tracks had one workspace per team: app, payments, data, security. Four. And the on-call engineer had to search all four at 3 a.m."),
            leo("Suspect: each team wanted privacy. Fair enough."),
            maya("Fair, but not needed. Inside one workspace, people see only the logs of resources they already have rights to. That's called resource-context access, like a hotel key card that opens only your own room. Microsoft's advice: start with one workspace, and add another only when a requirement forces it."),
            leo("And for Two Tracks, something does force it?"),
            maya("Data residency: a rule that data must stay in one place. European users' logs stay in Europe, Australian ones in Australia. So two workspaces, one per region. A region is a group of Azure data centres in one part of the world, like West Europe or Australia East."),
            leo("Last witness. Who's left?"),
            maya("The alerts. Twenty alert rules, each a tripwire on a metric or a log search. And each one had the on-call phone number typed in by hand. The engineer changed. Twelve alerts kept texting someone who'd left."),
            leo("Suspect: the phone company."),
            maya("[laugh] No. The fix is an action group: one shared phone list of email, text and voice calls. It can also call a webhook, a web address another system listens on. Define it once, point all twenty alert rules at it, and changing the engineer is one edit."),
            leo("And where does all this stop working?"),
            maya("At the edge of a virtual machine, a computer you rent in Azure. A diagnostic setting sees the machine from outside. For the logs inside it, you install the Azure Monitor Agent, a small program on the machine, and write a data collection rule, a recipe saying which logs to pick up."),
            leo("And the app's own errors? Who watches those?"),
            maya("Application Insights: a watcher inside your app, recording each request, failure and slow call. That's app telemetry, meaning the records your code sends home. New ones keep their data in the same workspace, so one search covers the app and the vault."),
            leo("And who would have caught Tuesday's odd read?"),
            maya("Microsoft Sentinel, a security guard that reads the workspace and hunts for signs of attack, like a secret read at 3 a.m. from a strange place. And who may read the vault at all? The identity episode covers that."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Resource logs need a diagnostic setting, or they're lost. Searched or alerted on: a workspace. Kept for years cheaply: storage. Streamed to an outside tool: Event Hubs. Many alerts, same people: one action group. And split workspaces only for residency."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a Log Analytics workspace in your nearest region. Add a diagnostic setting on a Key Vault, sending the audit category to it. Read a secret, then find the row with a short KQL query. Then create an action group with your email and an alert rule that uses it."),
            leo("A vault that keeps a diary. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-monitoring",
        listOf(
            maya("Okay, did you hear what happened at Two Tracks this week? Sit down. The Android app has gone global, with users in Europe and Australia, and its backend, the server code the app talks to, on Azure. And the team set up monitoring. Five people, five mistakes."),
            leo("[laugh] I heard there was a 3 a.m. incident. Monitoring, in plain words?"),
            maya("Watching everything you run, so you hear about trouble before your users do. Azure's service for it is Azure Monitor. Picture a building's security desk: cameras feed a wall of screens, a guard watches, and a phone list says who to ring at 3 a.m."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. The security team asked who had read a secret from Key Vault, the locked safe for passwords. Tom opened the logs, and there was nothing. Not one row."),
            leo("Someone deleted them?"),
            maya("They never existed. A vault's resource logs, the records of what happens inside it, like each secret read, aren't collected until you ask. You ask with a diagnostic setting. Think of a post office redirect on the resource: which kinds of log to send, and where to."),
            leo("Poor Tom. Send them where, though?"),
            maya("Three places. A Log Analytics workspace is a searchable library of logs; alerts and reports read from it. A storage account is a cheap filing cabinet for years of archive. And Event Hubs is a fast pipe that streams logs to another tool as they arrive."),
            leo("And how do you search the library?"),
            maya("In KQL, the Kusto Query Language. It reads almost like a sentence: take this table, keep last night's rows, count them. Under the hood, every record is a row in a table, and your question runs over the rows. Tom wrote his first one by lunch."),
            leo("Tuesday?"),
            maya("Tuesday was Dana. Dana made a workspace for every team. App, payments, data, security. Privacy, she said."),
            leo("Is that bad? Sounds tidy."),
            maya("It's four places to search at 3 a.m. Inside one workspace, people already see only the logs of resources they have rights to. That's called resource-context access: a hotel key card that opens only your own room. Microsoft says start with one workspace, and split only when a requirement forces it."),
            leo("So nothing forces it?"),
            maya("One thing did: data residency, a rule that data must stay in one place. European users' logs stay in Europe, Australian ones in Australia. So Priya merged Dana's four into two, one per region. A region is a group of Azure data centres in one part of the world."),
            leo("Wednesday?"),
            maya("Marcus. The rule said audit logs must raise an alarm within minutes. Marcus sent them only to the storage account. Cheapest, he said."),
            leo("And a filing cabinet doesn't ring anyone."),
            maya("Exactly. Searched or alerted on means the workspace. Marcus now sends audit logs twice: to the workspace for alarms, and to storage for the long archive. A resource can have up to five diagnostic settings, so that's easy."),
            leo("Thursday, I'm guessing, the intern."),
            maya("[chuckle] The intern. Bless them. They made twenty alert rules. An alert rule is a tripwire: a number over a limit, or a log search that finds rows. And into every single one, they typed the on-call engineer's phone number."),
            leo("And then the engineer changed."),
            maya("And twelve alerts kept texting someone who'd left. The fix is an action group: one shared phone list, with email, text, voice calls, or a webhook, which is a web address another system listens on. Every alert rule points at it. Change the engineer once."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. The auditors asked for seven years of the Activity log. That's the subscription's visitor book: who created, changed or deleted each resource. A subscription is the billing account your resources live in. And Priya said, it's all there forever."),
            leo("Priya, wrong? Never."),
            maya("Wrong. The Activity log keeps 90 days on its own. To keep it longer, a diagnostic setting on the subscription routes it out. Seven years, rarely read, cheapest: a storage account. She fixed it before lunch, and nobody's letting her forget it."),
            leo("What about the app's own errors? The 3 a.m. incident?"),
            maya("That's Application Insights, a watcher inside your code. It records app telemetry, which means each request, failure and slow call your code makes. New ones keep their data in the regional workspace, so one search covers the app and the vault."),
            leo("And the security team's fancy tool?"),
            maya("Microsoft Sentinel is a security guard that reads the workspace and hunts for attacks. If they'd rather use a SIEM, a security tool from another company, Event Hubs streams the logs to it within seconds."),
            leo("So where does all this stop working?"),
            maya("Inside a virtual machine, a computer you rent in Azure. Diagnostic settings only see it from outside. For the logs inside, install the Azure Monitor Agent, a small program, plus a data collection rule, a recipe for which logs to pick up. And who may read the vault at all? That's the identity episode."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Resource logs need a diagnostic setting. Searched or alerted on: a workspace. Kept cheaply for years: storage. Streamed to an outside tool: Event Hubs. One place, least effort: fewer workspaces, split only for residency. And many alerts, same people: one action group."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Log Analytics workspace in your nearest region. Add a diagnostic setting on a Key Vault, sending the audit category to it. Read a secret, then find the row with a short KQL query. Then create an action group with your email, and an alert rule that uses it."),
            leo("One phone list, not twenty. Got it. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-monitoring",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about who's watching while everyone sleeps."),
            ivy("Mm. Someone's always watching, Nova. Who, tonight?"),
            nova("Azure Monitor. Picture a building's night security desk: cameras feed a wall of screens, a guard watches, and a phone list says who to ring at 3 a.m. Take Two Tracks, the Android app that's gone global, with its backend, the server code the app talks to, on Azure, and users in Europe and Australia."),
            ivy("And what do the cameras see?"),
            nova("Metrics, first. Numbers over time, like a heartbeat on a hospital monitor: requests per second, how busy a server is. Azure collects them on its own, and keeps them 93 days."),
            ivy("And second?"),
            nova("The Activity log. The building's visitor book: who created, changed or deleted a resource, which is anything you make in Azure, a database, an app, a vault. Also collected on its own. [pause] But it only remembers 90 days."),
            ivy("Ninety days. Then it forgets? How sad."),
            nova("Unless you keep it somewhere. We'll come back to that. Third, resource logs: what happens inside a resource. Like each time someone reads a secret from Key Vault, the locked safe for passwords. And those, Azure doesn't collect at all. Not until you ask."),
            ivy("How do I ask? Nicely?"),
            nova("With a diagnostic setting. Think of a post office redirect on the resource: which kinds of log to send, and where. Under the picture, it's a setting Azure's own pipeline reads, and it copies each new record to the places you name, within minutes."),
            ivy("And the fourth kind?"),
            nova("App telemetry, which means the records your own code sends home: each request, each failure, each slow call. Application Insights gathers it, a watcher you add to your app. Like a fitness tracker on your code's wrist."),
            ivy("So the redirect sends logs where, at this hour?"),
            nova("Three places. A Log Analytics workspace: a searchable library of logs, where alerts and reports read from. A storage account: a cheap filing cabinet for years. And Event Hubs: a fast pipe that streams logs to another tool, as they arrive."),
            ivy("And how do I ask the library a question?"),
            nova("In KQL, the Kusto Query Language. [pause] It reads like a quiet sentence: take this table, keep last night's rows, count them. Every record is a row, and your question runs over the rows."),
            ivy("How many libraries should one company keep?"),
            nova("As few as you can. Start with one workspace, and add another only when a requirement forces it. Each team doesn't need its own: inside one workspace, people only see logs for resources they're allowed to touch. That's resource-context access, like a hotel key card for your room alone."),
            ivy("And what forces a second?"),
            nova("Data residency: a rule that data must stay in one place. Logs about European users stay in Europe, Australian ones in Australia. So one workspace per region. A region is a group of Azure data centres in one part of the world: West Europe, Australia East."),
            ivy("And the auditors, who want seven years of that visitor book?"),
            nova("A diagnostic setting on the subscription, the billing account your resources live in, sends the Activity log to a storage account. Rarely read, cheapest, kept for years. [pause] The forgetting stops."),
            ivy("Now the part that keeps me up. 3 a.m. Something breaks. Who wakes?"),
            nova("An alert rule notices. It's a tripwire: a number over a limit, or a log search that finds rows. Say, more than five percent of requests failing for five minutes. Then the alert calls its action group."),
            ivy("Action group. Tell me."),
            nova("The phone list. Email, a text message, a voice call. It can call a webhook too, which is a web address another system listens on. And one action group serves many alert rules, so when the on-call engineer changes, you change one number, not twenty."),
            ivy("And if I just want to look, not be woken?"),
            nova("A workbook. An interactive report of text, searches and charts, for wandering through. It wakes nobody. And Microsoft Sentinel is a security guard on top of the workspace, reading the logs for signs of attack, all night long."),
            ivy("Where does all of this stop working, Nova?"),
            nova("Inside a virtual machine, a computer you rent in Azure. A diagnostic setting only sees it from outside. For its inner logs, install the Azure Monitor Agent, a small program, and a data collection rule, a recipe for which logs to pick up. And who may read the vault at all? That's the identity episode, another late night."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. Searched or alerted on means a workspace. Kept cheaply for years means storage. Streamed to an outside tool means Event Hubs. One place, least effort means fewer workspaces, split only for residency. And many alerts telling the same people means one action group."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Log Analytics workspace in your nearest region. Add a diagnostic setting on a Key Vault, sending the audit category to it. [pause] Read a secret, then find the row with a short KQL query. Then create an action group with your email, and an alert rule that uses it."),
            ivy("Someone's always watching. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-monitoring",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about who's watching whom."),
            rafe("I'm always watching you. That's monitoring, isn't it?"),
            nova("[laugh] In Azure, it's Azure Monitor. Picture a building's security desk: cameras on a wall of screens, a guard, and a phone list for 3 a.m. Two Tracks has gone global: the Android app, with its backend, the server code the app talks to, on Azure, for users in Europe and Australia."),
            rafe("And what does the desk record? Everything I do?"),
            nova("Some of it on its own. Metrics, numbers over time, like your heartbeat when I walk in. And the Activity log, the visitor book: who created, changed or deleted a resource, which is anything you make in Azure. It remembers 90 days, then forgets."),
            rafe("Ninety days and you forget me? Harsh. And the rest?"),
            nova("Resource logs: what happens inside a resource, like each time someone reads a secret from Key Vault, the locked safe for passwords. And here's my first question, exam style. Where do those secret reads appear by default?"),
            rafe("Nowhere, until a diagnostic setting sends them somewhere. That's a post office redirect on the vault: which kinds of log, and where to."),
            nova("Right answer. You never forget to ask. [kiss]"),
            rafe("Mm. And under the picture, it's just a setting Azure's own pipeline reads, copying each new record to the places you name, within minutes. Where can it send them?"),
            nova("Three places. A Log Analytics workspace, a searchable library of logs, where alerts read from. A storage account, a cheap filing cabinet for years. And Event Hubs, a fast pipe to another tool, as the logs arrive."),
            rafe("And how do I whisper a question to the library?"),
            nova("In KQL, the Kusto Query Language. It reads like a sentence: take this table, keep last night's rows, count them. So, darling, my question. The security team wants Key Vault logs in their outside security tool, in near real time. Where do they go?"),
            rafe("A storage account. Safe and cheap, like me."),
            nova("[sigh] No kiss for that one. Storage is an archive; the tool would have to keep checking files. Near real time means Event Hubs, the pipe, which streams logs to it within seconds. Cheap you may be. Quick you are not."),
            rafe("Fair. My turn. Auditors want every Activity log entry kept seven years, cheaply, rarely read. What do you recommend?"),
            nova("A diagnostic setting on the subscription, the billing account your resources live in, sending the Activity log to a storage account. On its own, it forgets after 90 days."),
            rafe("Right answer. Some of us remember everything. [kiss]"),
            nova("Then here's one about commitment. Two Tracks has European and Australian users, and logs must stay in their own region, a group of Azure data centres in one part of the world. Each team wants to see only its own logs. How many workspaces?"),
            rafe("One per team, plus one per region. Lots of privacy."),
            nova("No kiss. Two, one per region, because data residency, a rule that data stays in one place, forces that split and nothing else does. Inside a workspace, resource-context access already shows people only logs for resources they may touch. Like a hotel key card for one room. Ours."),
            rafe("Then the one that wakes us at 3 a.m. Twenty alert rules, tripwires that fire when a number crosses a limit, must all text the on-call engineer. How do you keep it easy to change?"),
            nova("One action group, the shared phone list, used by all twenty. Change the engineer once, and every alert follows."),
            rafe("Right answer. One list, and you're on it. [kiss]"),
            nova("And an action group can call a webhook, which is a web address another system listens on, so a message lands in the team chat. Now, your own code. Who watches that?"),
            rafe("Application Insights, a watcher inside the app. It gathers app telemetry, meaning the records your code sends home: requests, failures, slow calls. New ones keep their data in the same workspace."),
            nova("Mm. And who watches for intruders?"),
            rafe("Microsoft Sentinel, a security guard that reads the workspace and hunts for signs of attack. The jealous type."),
            nova("And where does it all stop working? Every romance has its limits."),
            rafe("Inside a virtual machine, a computer you rent in Azure. Diagnostic settings see it from outside only. Inside, you need the Azure Monitor Agent, a small program, plus a data collection rule, the recipe for which logs to pick up. And who may read the vault at all? That's the identity episode."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("Resource logs need a diagnostic setting. Searched or alerted on: a workspace. Years, cheaply: storage. Streamed to an outside tool: Event Hubs. Split workspaces only for residency. Many alerts, same people: one action group."),
            nova("Six for six. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a Log Analytics workspace in your nearest region. Add a diagnostic setting on a Key Vault, sending the audit category to it. Read a secret, then find the row with a short KQL query. Then create an action group with your email, and an alert rule that uses it."),
            rafe("And I'll always be your action group. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun kinds() = DiagramSpec(
        "Azure Monitor is the security desk; metrics and the Activity log arrive on their own, resource logs only when asked for.",
        nodes = listOf(
            DNode("metrics", "Metrics", 0.26f, 0.2f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE, sub = "on their own, 93 days"),
            DNode("activity", "Activity log", 0.76f, 0.2f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE, sub = "on its own, 90 days"),
            DNode("reslogs", "Resource logs", 0.26f, 0.55f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE, tone = 2, sub = "only with a setting"),
            DNode("tele", "App telemetry", 0.76f, 0.55f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE, sub = "Application Insights"),
            DNode("monitor", "Azure Monitor: the security desk", 0.5f, 0.87f, w = 0.9f, h = 0.16f, tone = 1),
        ),
        aspect = 0.8f,
    )

    private fun routes() = DiagramSpec(
        "A diagnostic setting routes resource logs and the Activity log to a workspace, a storage account or Event Hubs; Application Insights writes to the workspace.",
        nodes = listOf(
            DNode("res", "Key Vault", 0.17f, 0.15f, w = 0.3f, h = 0.18f, sub = "resource logs"),
            DNode("sub", "Subscription", 0.17f, 0.42f, w = 0.3f, h = 0.18f, sub = "Activity log"),
            DNode("app", "App Insights", 0.17f, 0.69f, w = 0.3f, h = 0.18f, tone = 1, sub = "app telemetry"),
            DNode("ds", "Diagnostic setting", 0.5f, 0.29f, w = 0.28f, h = 0.2f, tone = 2, sub = "the redirect"),
            DNode("law", "Log Analytics", 0.83f, 0.15f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "search, alert"),
            DNode("st", "Storage account", 0.83f, 0.42f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "cheap archive"),
            DNode("eh", "Event Hubs", 0.83f, 0.69f, w = 0.3f, h = 0.18f, sub = "stream to a SIEM"),
            DNode("note", "Not collected until you ask", 0.5f, 0.91f, w = 0.9f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("res", "ds"),
            DEdge("sub", "ds"),
            DEdge("ds", "law"),
            DEdge("ds", "st"),
            DEdge("ds", "eh"),
            DEdge("app", "law", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun workspaces() = DiagramSpec(
        "Two Tracks keeps one workspace per region because data residency forces it; teams share a workspace through resource-context access.",
        nodes = listOf(
            DNode("eu", "West Europe", 0.25f, 0.18f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 1, sub = "workspace: EU logs"),
            DNode("au", "Australia East", 0.75f, 0.18f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 1, sub = "workspace: AU logs"),
            DNode("euapi", "EU API", 0.25f, 0.52f, w = 0.42f, h = 0.18f, sub = "App Insights"),
            DNode("auapi", "Australia API", 0.75f, 0.52f, w = 0.42f, h = 0.18f, sub = "App Insights"),
            DNode("teams", "Teams share one: resource-context access", 0.5f, 0.84f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("euapi", "eu", "stays in Europe"),
            DEdge("auapi", "au", "stays in Australia"),
        ),
        aspect = 0.8f,
    )

    private fun alerts() = DiagramSpec(
        "Many alert rules share one action group, the phone list that emails, texts, calls or calls a webhook; workbooks and Sentinel watch without waking anyone.",
        nodes = listOf(
            DNode("rules", "Alert rules", 0.2f, 0.18f, w = 0.34f, h = 0.2f, tone = 1, sub = "the tripwires"),
            DNode("ag", "Action group", 0.62f, 0.18f, w = 0.34f, h = 0.2f, sub = "one phone list"),
            DNode("people", "Email, text, call", 0.38f, 0.5f, w = 0.34f, h = 0.18f, sub = "on-call engineer"),
            DNode("hook", "Webhook", 0.8f, 0.5f, w = 0.34f, h = 0.18f, sub = "team chat"),
            DNode("wb", "Workbook", 0.25f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "a report to explore"),
            DNode("sen", "Microsoft Sentinel", 0.75f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "hunts for attacks"),
        ),
        edges = listOf(
            DEdge("rules", "ag", "fires"),
            DEdge("ag", "people"),
            DEdge("ag", "hook"),
        ),
        aspect = 0.8f,
    )

    private fun retention() = DiagramSpec(
        "The Activity log forgets after 90 days unless a diagnostic setting routes it to storage; a workspace keeps 30 days by default, longer if you pay.",
        nodes = listOf(
            DNode("al", "Activity log", 0.17f, 0.2f, w = 0.32f, h = 0.2f, sub = "forgets at 90 days"),
            DNode("ds", "Diagnostic setting", 0.5f, 0.2f, w = 0.28f, h = 0.2f, tone = 2, sub = "on the subscription"),
            DNode("st", "Storage account", 0.83f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "seven years, cheap"),
            DNode("ws", "Log Analytics workspace", 0.5f, 0.55f, w = 0.6f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 1, sub = "30 days; 2 years searchable"),
            DNode("ltr", "Long-term retention: up to 12 years", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("al", "ds"),
            DEdge("ds", "st", "archive"),
            DEdge("ds", "ws", "search", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun vm() = DiagramSpec(
        "A diagnostic setting sees a virtual machine from outside; for logs inside it, the Azure Monitor Agent follows a data collection rule.",
        nodes = listOf(
            DNode("vm", "Virtual machine", 0.25f, 0.2f, w = 0.42f, h = 0.2f, sub = "a rented computer"),
            DNode("ws", "Workspace", 0.75f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1),
            DNode("agent", "Azure Monitor Agent", 0.25f, 0.55f, w = 0.42f, h = 0.2f, tone = 1, sub = "a small program"),
            DNode("dcr", "Data collection rule", 0.75f, 0.55f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the recipe"),
            DNode("ds", "Diagnostic setting: outside only", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("vm", "agent", "installed"),
            DEdge("agent", "dcr", "follows"),
            DEdge("dcr", "ws", "sends to"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's monitoring questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("diag", "Resource logs?", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a diagnostic setting"),
            DNode("search", "Searched or alerted on", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a workspace"),
            DNode("years", "Years, cheaply", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a storage account"),
            DNode("stream", "Stream to a SIEM", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Hubs"),
            DNode("split", "One place, least effort", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "split for residency"),
            DNode("ag", "Many alerts, same people", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "one action group"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("diag", "search", "years", "stream", "split", "ag")
    private val kql = BoardCard("KQL", "AzureDiagnostics | take 10", code = true)
    private val today = BoardCard("Today", "A workspace, a vault's audit logs, one KQL query, an action group.")
    private val diagnostic = BoardCard("Diagnostic setting", "A post office redirect on a resource: which logs, and where to.")
    private val actionGroup = BoardCard("Action group", "One shared phone list: email, text, call, webhook. Change it once.")
    private val inside = BoardCard("Inside a machine", "The Azure Monitor Agent, plus a data collection rule: the recipe.")

    private fun classicBoard() = PodcastBoard(
        "sa-monitoring",
        listOf(
            // 0: "Hi, and welcome back. Today is logging and monitoring..."
            Scene(
                0, "Who's watching?", kinds(),
                beats = listOf(
                    Beat(0, card = BoardCard("Two Tracks goes global", "Europe and Australia. At 3 a.m., the API stopped answering.")),
                    Beat(2, show = setOf("monitor"), light = setOf("monitor"), card = BoardCard("Azure Monitor", "The security desk: collect, keep, and tell someone.")),
                    Beat(4, show = setOf("metrics"), light = setOf("metrics"), card = BoardCard("Metrics", "Numbers over time, like a speedometer. On their own, 93 days.")),
                    Beat(6, show = setOf("activity"), light = setOf("activity"), card = BoardCard("Activity log", "Who created, changed or deleted a resource. Kept 90 days.")),
                    Beat(8, show = setOf("reslogs"), light = setOf("reslogs"), card = BoardCard("Resource logs", "What happens inside a resource. Not collected until you ask.")),
                ),
            ),
            // 10: "Gone. The way you ask is a diagnostic setting..."
            Scene(
                10, "Where logs go", routes(),
                beats = listOf(
                    Beat(10, show = setOf("res", "sub", "ds", "note"), light = setOf("ds"), pulse = "res" to "ds", card = diagnostic),
                    Beat(12, show = setOf("app"), light = setOf("app"), card = BoardCard("App telemetry", "Requests, errors, slow calls, from Application Insights.")),
                    Beat(14, show = setOf("law"), light = setOf("law"), pulse = "ds" to "law", card = BoardCard("Log Analytics workspace", "A searchable library of logs. Alerts and reports read from it.")),
                    Beat(16, card = kql),
                    Beat(18, show = setOf("st"), light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Storage account", "A cheap filing cabinet for years. Hard to search.")),
                    Beat(20, show = setOf("eh"), light = setOf("eh"), pulse = "ds" to "eh", card = BoardCard("Event Hubs", "A fast pipe to an outside tool, such as a SIEM, within seconds.")),
                    Beat(21, light = setOf("law", "st", "eh"), card = BoardCard("Three places", "Library to search, cabinet to archive, pipe to stream.")),
                ),
            ),
            // 22: "Exactly that. Now the big design question: how many workspaces?"
            Scene(
                22, "How many workspaces?", workspaces(),
                beats = listOf(
                    Beat(22, card = BoardCard("Start with one", "Add a workspace only when a requirement forces it.")),
                    Beat(24, show = setOf("eu", "au"), light = setOf("eu", "au"), card = BoardCard("Data residency", "EU logs stay in Europe, Australian ones in Australia.")),
                    Beat(26, show = setOf("euapi", "auapi"), light = setOf("euapi", "eu"), pulse = "euapi" to "eu", card = BoardCard("Region", "A group of Azure data centres in one part of the world.")),
                    Beat(28, show = setOf("teams"), light = setOf("teams"), card = BoardCard("Not for teams", "Resource-context access: each sees only its own resources' logs.")),
                ),
            ),
            // 29: "Right. Now the 3 a.m. problem..."
            Scene(
                29, "Who gets woken", alerts(),
                beats = listOf(
                    Beat(30, show = setOf("rules"), light = setOf("rules"), card = BoardCard("Alert rule", "A tripwire: a metric over a limit, or a log search that finds rows.")),
                    Beat(32, show = setOf("ag", "people", "hook"), light = setOf("ag"), pulse = "rules" to "ag", card = actionGroup),
                    Beat(34, light = setOf("ag"), pulse = "ag" to "people", card = BoardCard("Twenty rules, one list", "The on-call engineer changes: you change one number.")),
                    Beat(36, show = setOf("wb", "sen"), light = setOf("wb", "sen"), card = BoardCard("Workbook and Sentinel", "A report to explore; a guard that hunts for attacks.")),
                ),
            ),
            // 37: "How long does the workspace keep everything?"
            Scene(
                37, "Keeping it", retention(),
                beats = listOf(
                    Beat(38, show = setOf("ws", "ltr"), light = setOf("ws"), card = BoardCard("Retention", "30 days by default, 2 years searchable, 12 years in all.")),
                    Beat(40, show = setOf("al", "ds", "st"), light = setOf("al", "st"), pulse = "ds" to "st", card = BoardCard("Seven years of audit", "The Activity log routed to storage. On its own: 90 days.")),
                ),
            ),
            // 41: "So what do I remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("search", "years", "stream", "split", "ag"), card = BoardCard("Listen for the phrase", "Search, archive, stream, one place, one phone list.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-monitoring--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Vault That Kept No Diary..."
            Scene(
                0, "The Vault That Kept No Diary", routes(),
                beats = listOf(
                    Beat(0, show = setOf("res"), light = setOf("res"), card = BoardCard("The case", "A secret was read on Tuesday. Nobody knows by whom.")),
                    Beat(2, card = BoardCard("Azure Monitor", "The security desk. For the vault, the screens were blank.")),
                    Beat(4, show = setOf("note"), light = setOf("note"), card = BoardCard("Clue one", "Resource logs aren't collected until you ask.")),
                    Beat(6, show = setOf("ds", "sub"), light = setOf("ds"), pulse = "res" to "ds", card = diagnostic),
                    Beat(8, show = setOf("law", "st", "eh", "app"), light = setOf("law", "st", "eh"), card = BoardCard("Three places", "Library to search, cabinet to archive, pipe to stream.")),
                ),
            ),
            // 9: "Storage. Cheapest. Done."
            Scene(
                9, "Clue two", routes(),
                beats = listOf(
                    Beat(10, light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Clue two", "Storage can't raise an alarm. Search and alerts need the workspace.")),
                    Beat(12, light = setOf("law"), pulse = "ds" to "law", card = kql),
                    Beat(14, light = setOf("eh"), pulse = "ds" to "eh", card = BoardCard("A SIEM, via Event Hubs", "An outside security tool reads the logs as they arrive.")),
                ),
            ),
            // 15: "Third clue. Hit me."
            Scene(
                15, "Clue three", retention(),
                beats = listOf(
                    Beat(16, show = setOf("al"), light = setOf("al"), card = BoardCard("The Activity log", "Who created, changed or deleted each resource.")),
                    Beat(18, show = setOf("ds", "st"), light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Kept 90 days", "For seven years, cheaply: a diagnostic setting to storage.")),
                ),
            ),
            // 19: "Fine. Fourth witness?"
            Scene(
                19, "The fourth witness", workspaces(),
                beats = listOf(
                    Beat(20, card = BoardCard("Four workspaces", "One per team: four places to search at 3 a.m.")),
                    Beat(22, show = setOf("teams"), light = setOf("teams"), card = BoardCard("Resource-context access", "One workspace; each team sees only its own resources' logs.")),
                    Beat(24, show = setOf("eu", "au", "euapi", "auapi"), light = setOf("eu", "au"), pulse = "euapi" to "eu", card = BoardCard("Data residency", "The one reason here to split: Europe and Australia.")),
                ),
            ),
            // 25: "Last witness. Who's left?"
            Scene(
                25, "The last witness", alerts(),
                beats = listOf(
                    Beat(26, show = setOf("rules"), light = setOf("rules"), card = BoardCard("Twenty alert rules", "Each with the phone number typed in by hand.")),
                    Beat(28, show = setOf("ag", "people", "hook", "wb", "sen"), light = setOf("ag"), pulse = "rules" to "ag", card = actionGroup),
                ),
            ),
            // 29: "And where does all this stop working?"
            Scene(
                29, "Where it stops", vm(),
                beats = listOf(
                    Beat(30, light = setOf("agent", "dcr"), pulse = "agent" to "dcr", card = inside),
                ),
            ),
            // 31: "And the app's own errors? Who watches those?"
            Scene(
                31, "Who would have caught it", routes(),
                beats = listOf(
                    Beat(32, light = setOf("app", "law"), pulse = "app" to "law", card = BoardCard("Application Insights", "A watcher in your code, writing to the same workspace.")),
                    Beat(34, light = setOf("law"), card = BoardCard("Microsoft Sentinel", "A guard on the workspace that hunts for signs of attack.")),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("diag", "search", "years", "stream", "ag"), card = BoardCard("Listen for the phrase", "Collect it, search it, archive it, stream it, tell someone.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-monitoring--gossip",
        listOf(
            // 0: "Okay, did you hear what happened at Two Tracks this week?..."
            Scene(
                0, "The team's week", kinds(),
                beats = listOf(
                    Beat(0, card = BoardCard("Five people, five mistakes", "Two Tracks sets up monitoring for Europe and Australia.")),
                    Beat(2, show = setOf("monitor"), light = setOf("monitor"), card = BoardCard("Azure Monitor", "The security desk: screens, a guard, a phone list.")),
                ),
            ),
            // 3: "Okay. So who went first?"
            Scene(
                3, "Monday: Tom", routes(),
                beats = listOf(
                    Beat(4, show = setOf("res"), light = setOf("res"), card = BoardCard("Monday: Tom", "Asked who read a secret. Not one row.")),
                    Beat(6, show = setOf("ds", "note"), light = setOf("ds"), pulse = "res" to "ds", card = diagnostic),
                    Beat(8, show = setOf("law", "st", "eh"), light = setOf("law", "st", "eh"), card = BoardCard("Three places", "Library to search, cabinet to archive, pipe to stream.")),
                    Beat(10, light = setOf("law"), card = kql),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Dana", workspaces(),
                beats = listOf(
                    Beat(12, card = BoardCard("A workspace per team", "App, payments, data, security. Four places to search.")),
                    Beat(14, show = setOf("teams"), light = setOf("teams"), card = BoardCard("Resource-context access", "One workspace; each team sees only its own resources' logs.")),
                    Beat(16, show = setOf("eu", "au", "euapi", "auapi"), light = setOf("eu", "au"), pulse = "auapi" to "au", card = BoardCard("Data residency", "Priya merged four into two: one per region.")),
                ),
            ),
            // 17: "Wednesday?"
            Scene(
                17, "Wednesday: Marcus", routes(),
                beats = listOf(
                    Beat(18, show = setOf("res", "ds", "st"), light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Only to storage", "A filing cabinet can't ring anyone.")),
                    Beat(20, show = setOf("law"), light = setOf("law"), pulse = "ds" to "law", card = BoardCard("Send it twice", "Workspace for alarms, storage for the archive.")),
                ),
            ),
            // 21: "Thursday, I'm guessing, the intern."
            Scene(
                21, "Thursday: the intern", alerts(),
                beats = listOf(
                    Beat(22, show = setOf("rules"), light = setOf("rules"), card = BoardCard("Twenty alert rules", "The phone number typed into every one.")),
                    Beat(24, show = setOf("ag", "people", "hook", "wb", "sen"), light = setOf("ag"), pulse = "rules" to "ag", card = actionGroup),
                ),
            ),
            // 25: "And Friday? There's always a Friday."
            Scene(
                25, "Friday: Priya", retention(),
                beats = listOf(
                    Beat(26, show = setOf("al"), light = setOf("al"), card = BoardCard("All there forever?", "The Activity log: who created, changed or deleted what.")),
                    Beat(28, show = setOf("ds", "st"), light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Only 90 days", "Then gone, unless routed. Seven years: storage.")),
                ),
            ),
            // 29: "What about the app's own errors? The 3 a.m. incident?"
            Scene(
                29, "The 3 a.m. incident", routes(),
                beats = listOf(
                    Beat(30, show = setOf("app", "law"), light = setOf("app"), pulse = "app" to "law", card = BoardCard("Application Insights", "A watcher in your code, writing to the regional workspace.")),
                    Beat(32, show = setOf("eh"), light = setOf("eh"), card = BoardCard("Sentinel, or a SIEM", "Sentinel hunts in the workspace; Event Hubs feeds an outside tool.")),
                ),
            ),
            // 33: "So where does all this stop working?"
            Scene(
                33, "Where it stops", vm(),
                beats = listOf(
                    Beat(34, light = setOf("agent", "dcr"), pulse = "agent" to "dcr", card = inside),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("diag", "search", "years", "split", "ag"), card = BoardCard("Listen for the phrase", "One colleague, one lesson, one answer.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-monitoring--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Who's watching tonight", kinds(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Who's watching while everyone sleeps.")),
                    Beat(2, show = setOf("monitor"), light = setOf("monitor"), card = BoardCard("Azure Monitor", "The night security desk: screens, a guard, a phone list.")),
                    Beat(4, show = setOf("metrics"), light = setOf("metrics"), card = BoardCard("Metrics", "Numbers over time, like a heartbeat. On their own, 93 days.")),
                    Beat(6, show = setOf("activity"), light = setOf("activity"), card = BoardCard("Activity log", "The visitor book. It remembers only 90 days.")),
                    Beat(8, show = setOf("reslogs"), light = setOf("reslogs"), card = BoardCard("Resource logs", "Inside a resource. Not collected until you ask.")),
                ),
            ),
            // 10: "With a diagnostic setting. Think of a post office redirect..."
            Scene(
                10, "Where logs go", routes(),
                beats = listOf(
                    Beat(10, show = setOf("res", "sub", "ds", "note"), light = setOf("ds"), pulse = "res" to "ds", card = diagnostic),
                    Beat(12, show = setOf("app"), light = setOf("app"), card = BoardCard("App telemetry", "Requests, failures, slow calls, from Application Insights.")),
                    Beat(14, show = setOf("law", "st", "eh"), light = setOf("law", "st", "eh"), card = BoardCard("Three places", "Library to search, cabinet to archive, pipe to stream.")),
                    Beat(16, light = setOf("law"), card = kql),
                ),
            ),
            // 17: "How many libraries should one company keep?"
            Scene(
                17, "How many libraries", workspaces(),
                beats = listOf(
                    Beat(18, show = setOf("teams"), light = setOf("teams"), card = BoardCard("As few as you can", "One workspace; resource-context access keeps teams apart.")),
                    Beat(20, show = setOf("eu", "au", "euapi", "auapi"), light = setOf("eu", "au"), pulse = "euapi" to "eu", card = BoardCard("Data residency", "One workspace per region: West Europe, Australia East.")),
                ),
            ),
            // 21: "And the auditors, who want seven years of that visitor book?"
            Scene(
                21, "The forgetting stops", retention(),
                beats = listOf(
                    Beat(22, show = setOf("al", "ds", "st"), light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Seven years", "The Activity log routed to storage. Rarely read, cheapest.")),
                ),
            ),
            // 23: "Now the part that keeps me up. 3 a.m..."
            Scene(
                23, "3 a.m.", alerts(),
                beats = listOf(
                    Beat(24, show = setOf("rules"), light = setOf("rules"), card = BoardCard("Alert rule", "A tripwire: say, 5% of requests failing for five minutes.")),
                    Beat(26, show = setOf("ag", "people", "hook"), light = setOf("ag"), pulse = "rules" to "ag", card = actionGroup),
                    Beat(28, show = setOf("wb", "sen"), light = setOf("wb", "sen"), card = BoardCard("Workbook and Sentinel", "A report that wakes nobody; a guard that hunts all night.")),
                ),
            ),
            // 29: "Where does all of this stop working, Nova?"
            Scene(
                29, "Where it stops", vm(),
                beats = listOf(
                    Beat(30, light = setOf("agent", "dcr"), pulse = "agent" to "dcr", card = inside),
                ),
            ),
            // 31: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("search", "years", "stream", "split", "ag"), card = BoardCard("Listen for the phrase", "Five little things, one answer each.")),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-monitoring--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", kinds(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Who's watching whom: Azure Monitor.")),
                    Beat(2, show = setOf("monitor"), light = setOf("monitor"), card = BoardCard("Azure Monitor", "The security desk: screens, a guard, a phone list.")),
                    Beat(4, show = setOf("metrics", "activity", "reslogs", "tele"), light = setOf("metrics", "activity"), card = BoardCard("On their own", "Metrics, 93 days. The Activity log, 90 days, then it forgets.")),
                ),
            ),
            // 6: "Resource logs... my first question, exam style."
            Scene(
                6, "First question", routes(),
                beats = listOf(
                    Beat(6, show = setOf("res"), light = setOf("res"), card = BoardCard("Quiz: secret reads", "Where do Key Vault secret reads appear by default?")),
                    Beat(7, show = setOf("ds", "note", "sub"), light = setOf("ds"), pulse = "res" to "ds", card = BoardCard("Right answer", "Nowhere, until a diagnostic setting sends them.")),
                    Beat(10, show = setOf("law", "st", "eh", "app"), light = setOf("law", "st", "eh"), card = BoardCard("Three places", "Library to search, cabinet to archive, pipe to stream.")),
                    Beat(12, light = setOf("eh"), card = BoardCard("Quiz: near real time", "An outside security tool wants the logs now. Where?")),
                    Beat(13, light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Storage? No kiss", "An archive; the tool would keep checking files.")),
                    Beat(14, light = setOf("eh"), pulse = "ds" to "eh", card = BoardCard("Event Hubs", "The pipe: it streams logs to the tool within seconds.")),
                ),
            ),
            // 15: "Fair. My turn. Auditors want every Activity log entry kept seven years..."
            Scene(
                15, "Seven years", retention(),
                beats = listOf(
                    Beat(15, show = setOf("al"), light = setOf("al"), card = BoardCard("Quiz: seven years", "Every Activity log entry, cheaply, rarely read.")),
                    Beat(16, show = setOf("ds", "st"), light = setOf("st"), pulse = "ds" to "st", card = BoardCard("Right answer", "A diagnostic setting on the subscription, to storage.")),
                ),
            ),
            // 18: "Then here's one about commitment..."
            Scene(
                18, "Commitment", workspaces(),
                beats = listOf(
                    Beat(18, show = setOf("euapi", "auapi"), light = setOf("euapi", "auapi"), card = BoardCard("Quiz: how many workspaces?", "Logs stay in their region; each team sees its own.")),
                    Beat(19, show = setOf("teams"), light = setOf("teams"), card = BoardCard("One per team? No kiss", "Resource-context access already keeps teams apart.")),
                    Beat(20, show = setOf("eu", "au"), light = setOf("eu", "au"), pulse = "euapi" to "eu", card = BoardCard("Two: one per region", "Data residency forces that split, and nothing else does.")),
                ),
            ),
            // 21: "Then the one that wakes us at 3 a.m..."
            Scene(
                21, "Who wakes us", alerts(),
                beats = listOf(
                    Beat(21, show = setOf("rules"), light = setOf("rules"), card = BoardCard("Quiz: twenty alerts", "All must text the on-call engineer. Easy to change?")),
                    Beat(22, show = setOf("ag", "people"), light = setOf("ag"), pulse = "rules" to "ag", card = BoardCard("Right answer", "One action group, the shared phone list, used by all twenty.")),
                    Beat(24, show = setOf("hook"), light = setOf("hook"), pulse = "ag" to "hook", card = BoardCard("Webhook", "A web address another system listens on, like the team chat.")),
                    Beat(25, card = BoardCard("Application Insights", "A watcher in your code: requests, failures, slow calls.")),
                    Beat(27, show = setOf("wb", "sen"), light = setOf("sen"), card = BoardCard("Microsoft Sentinel", "A guard that reads the workspace and hunts for attacks.")),
                ),
            ),
            // 28: "And where does it all stop working? Every romance has its limits."
            Scene(
                28, "Every romance has limits", vm(),
                beats = listOf(
                    Beat(29, light = setOf("agent", "dcr"), pulse = "agent" to "dcr", card = inside),
                ),
            ),
            // 30: "Last question, for the exam. What do we remember?"
            Scene(
                30, "For the exam", exam(),
                beats = listOf(
                    Beat(31, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Six for six: one answer each.")),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
