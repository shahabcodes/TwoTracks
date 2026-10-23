package com.shahaabapps.core.content

/** Data integration and analysis: moving and making sense of data: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesDataIntegration {
    private val classic = Podcast(
        "sa-data-integration",
        listOf(
            maya("Hi, and welcome back. Today is data integration and analysis. Our story: Two Tracks goes global. The little Android study app is now a company, with a backend on Azure, the server side the app talks to, users in Europe and Australia, and an old office system in London full of bookings."),
            leo("And somebody wants reports from all of it, I'm guessing. [chuckle]"),
            maya("Exactly. Data integration means gathering data from many places into one, on a schedule, cleaning it on the way. Data analysis means asking big questions of that gathered data, or of data still streaming in. On the exam, every question here is a choice: which service fits the requirement words."),
            leo("So what are the choices, in plain words?"),
            maya("A mover, called Data Factory. Three places to analyse: Microsoft Fabric, Synapse and Databricks. And a real-time engine, Stream Analytics. Let's take them one at a time, starting with the mover."),
            leo("Data Factory. Sounds like it makes data."),
            maya("It moves it. Picture a removal company with a timetable. It does ETL, which stands for extract, transform, load: take data out of a source, reshape it, and put it somewhere useful."),
            leo("Somewhere useful being where?"),
            maya("Often a data lake. That's a big, cheap store for raw files of every kind, like a warehouse where you can drop boxes without unpacking them. In Azure it's Data Lake Gen2: Blob Storage, Azure's big store for files, with proper folders added."),
            leo("Okay. And how do I tell the removal company what to do?"),
            maya("With a pipeline. That's the job list. Each step on it is an activity, for example a copy from one place to another. A linked service holds the connection details for one store, like the address and door code for one house."),
            leo("And who says when the job starts?"),
            maya("A trigger. It starts the pipeline on a schedule, say every night at two, or when a new file arrives. And if the data needs reshaping, mapping data flows let you design that by drawing boxes, instead of writing code."),
            leo("Who actually carries the boxes, though? Someone has to do the lifting."),
            maya("That's the integration runtime: the worker that does the lifting. And where it sits matters a lot. There are three kinds, and the exam loves them."),
            leo("Three? In my day job, the code that copies data just runs on my own server."),
            maya("And that's the real question here: whose machine runs the copy. The first is the Azure integration runtime, computing power that Azure runs for you. Say your app's files, which Azure calls blobs, sit in Blob Storage, and you copy them into the lake. Cloud to cloud, so the Azure runtime does it, and you look after nothing."),
            leo("And the London office? That's not in the cloud."),
            maya("No. That's on-premises, which means on the company's own machines, in its own building. And the office has a firewall, a guard on the door that turns away anyone trying to come in from outside. So the Azure runtime can't reach in."),
            leo("So how do we get the bookings out?"),
            maya("With a self-hosted integration runtime. It's software you install on a Windows machine inside the office, and it only connects outbound: it phones out to Azure."),
            leo("Hang on. The data still crosses the door. Why is outbound fine?"),
            maya("Think of your Android app. The phone calls your API; your API never rings the phone, because the phone's network turns strangers away. Same here: the office PC asks Azure whether there's work, then reads the database and sends the rows up. Nobody opens the door to callers from outside."),
            leo("Phoning out instead of being phoned. And if that one PC restarts for updates?"),
            maya("Then the copy doesn't run. So install it on two office machines, and either one can take the job."),
            leo("Good. And the third kind?"),
            maya("The Azure-SSIS runtime. SSIS, SQL Server Integration Services, is SQL Server's older tool for the same job, and a package is one saved SSIS job. If the finance team already has 150 packages, the Azure-SSIS runtime runs them in Azure with little change."),
            leo("So nobody rewrites 150 packages. Good, because I wasn't going to. [laugh]"),
            maya("Nobody should. That's a classic wrong answer: rewriting when the question says minimal rework. Now, the data has landed in the lake. Time to ask it questions."),
            leo("Where do I ask them? You said three places."),
            maya("First, two plain words. Spark is an engine that splits a big job across many machines, like a hundred people each counting one box of votes."),
            leo("And a data warehouse? I already have a database. My API writes workouts into it all day."),
            maya("That one is built for many small writes, one workout at a time. A data warehouse is shaped the other way round: few writes, and huge read-only questions, like lessons finished per country per month."),
            leo("Got it. Now the three."),
            maya("Microsoft Fabric is SaaS, software as a service: you sign in and use it, with nothing to deploy. It puts data movement, Spark, a warehouse, real time and reports in one place. Its storage is OneLake, which is one data lake for the whole organisation."),
            leo("Reports in what?"),
            maya("Power BI, Microsoft's tool for charts and dashboards that people actually look at. Fabric is the natural pick for a new project. Synapse Analytics is the older workspace with SQL and Spark together, which you set up and run yourself. It stays supported for existing warehouses."),
            leo("And Databricks?"),
            maya("Azure Databricks is the Spark-heavy choice, for data engineering and machine learning. Data scientists work there in notebooks, which are pages that mix code, results and notes."),
            leo("So how do I pick, in a question?"),
            maya("Listen for the team. SQL analysts and a warehouse suggest Fabric or Synapse. Data scientists in Python notebooks suggest Databricks."),
            leo("Any trap in there?"),
            maya("One. Synapse has two kinds of SQL pool. A dedicated pool is a warehouse you keep running, and pay for while it runs. Serverless SQL reads files right where they lie in the lake, and you pay per query. For example, analysts running a few queries a month over lake files: serverless, every time."),
            leo("Right. Now the live stuff. You said real time."),
            maya("The status page. Two Tracks wants to show how many lessons were finished per minute, updated within seconds. Each finished lesson sends an event, which is a small message saying something happened."),
            leo("Couldn't my API just write each one to the database, and the page count them every second?"),
            maya("With ten users, sure. With millions, that database drowns. So the events go into Event Hubs, a front door that can take in millions of events a second. IoT Hub is the same idea for devices and sensors."),
            leo("And who does the counting, then?"),
            maya("Stream Analytics. It reads the events out of Event Hubs, runs a SQL-like query over them as they arrive, and writes results every few seconds to a Power BI report."),
            leo("And how does it count per minute, if events never stop?"),
            maya("Windowing. It groups events into slices of time, like counting cars per minute at a junction. Every minute, the count for that slice comes out, and a new count starts."),
            leo("So could Data Factory do the status page instead? It's all just moving data."),
            maya("No, and that's the other classic trap. Data Factory runs batches, on a schedule. Nightly is the opposite of within seconds. If the words say within seconds, it's Stream Analytics."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If it says nightly copy from an on-premises source, Data Factory. If the source is behind a firewall with no inbound ports, a self-hosted integration runtime. If there are existing SSIS packages, the Azure-SSIS runtime. If it says within seconds or live dashboard, Stream Analytics. And data scientists with notebooks and Spark, Databricks."),
            leo("And anything to try today?"),
            maya("An idle factory costs nothing. Create a data factory with az datafactory create. Make a pipeline that copies a blob from one container to another, then Debug it. Add a schedule trigger and look at the next run time. Then open Monitor and read the copy activity's rows and duration."),
            leo("A removal company with a timetable, and a car counter at the junction. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-data-integration",
        listOf(
            maya("Welcome to Case Files. Today's case: The Dashboard That Lived in Yesterday. Two Tracks has gone global. The Android study app is a company now, with a backend on Azure, users in Europe and Australia, and an old booking system in the London office. On launch day in Australia, the boss opened the live dashboard. It showed yesterday."),
            leo("Detective Leo, on the case. A dashboard that's a day late. Somebody broke the clock."),
            maya("[chuckle] Nobody touched a clock. Let's set the scene first. The team needs two things: reports from all the company's data, and a live count of lessons finished per minute. Gathering data from many places into one is data integration. Asking big questions of it is data analysis."),
            leo("And the tool they used?"),
            maya("Azure Data Factory, for everything. Picture a removal company with a timetable. It does ETL, which stands for extract, transform, load: take data out of a source, reshape it, and put it somewhere useful."),
            leo("Somewhere useful, like?"),
            maya("A data lake. That's a big, cheap store for raw files of every kind, like a warehouse where you drop boxes without unpacking them."),
            leo("And how does the factory know what to move, and when?"),
            maya("Its job list is a pipeline. Each step on it is an activity, such as a copy. And a trigger starts the pipeline, on a schedule or when a file arrives."),
            leo("So the dashboard was a pipeline on a nightly trigger. My suspect: the trigger. Make it run every minute."),
            maya("[pause] Wrong suspect, though a popular one. Data Factory moves batches on a timetable. Nightly is the opposite of within seconds, and even a pipeline every minute is still a removal van, starting up, loading, driving. A live count needs a different machine."),
            leo("Which machine?"),
            maya("Each finished lesson sends an event, which is a small message saying something happened. Picture your Android app firing one tiny message the moment a lesson ends."),
            leo("Thousands at once, on launch day. Where do they all land?"),
            maya("In Event Hubs, a front door that takes in millions of events a second. Stream Analytics reads them from there, runs a SQL-like query as they arrive, and writes results every few seconds to the dashboard."),
            leo("Counting how, when they never stop coming?"),
            maya("Windowing. It groups events into slices of time, like counting cars per minute at a junction. Under the picture, it keeps a running count in memory for the current minute; when the minute closes, it writes the total and starts again. No waiting for the night."),
            leo("Clue two, then. I can feel one coming."),
            maya("The nightly copy from the London office failed too. Error: could not connect to the office database. The office is on-premises, which means the company's own machines in its own building."),
            leo("Suspect: the password. Somebody changed it."),
            maya("Wrong again. The password was fine. Look at which worker ran the copy: the Azure integration runtime."),
            leo("Integration runtime. Meaning?"),
            maya("The worker that does the lifting, and the Azure one is computing power inside Azure. But the office has a firewall, a guard on the door that turns away callers from outside. Azure couldn't reach in."),
            leo("So open the door?"),
            maya("No. The requirement said no inbound ports. Use a self-hosted integration runtime: software installed on a Windows machine inside the office. How it works: it phones out to Azure over the normal secure web connection, asks whether there's work, then reads the office database and sends the data up. Nothing ever calls in."),
            leo("Clever. Any catch?"),
            maya("Where it stops working: one machine is one point of failure. Register the runtime on two office machines, so the copy still runs if one is switched off. And under it all, Data Factory itself holds no data. It's the conductor; the runtimes do the lifting."),
            leo("Clue three. Who's next?"),
            maya("Finance. They had 150 SSIS packages. SSIS, SQL Server Integration Services, is SQL Server's older tool for the same job, and a package is one saved job. The team began rewriting them all as Databricks notebooks. Three months in, they'd done twelve."),
            leo("Suspect: not enough people. Hire more."),
            maya("[sigh] The real culprit is the plan. The requirement said minimal rework. Data Factory has a third worker, the Azure-SSIS integration runtime, which runs SSIS packages in Azure as they are. Lift and shift: move them over, change almost nothing."),
            leo("Last clue. There's always a money one."),
            maya("The bill. The analysts wanted a warehouse, which is a database shaped for big read-only questions. They created a Synapse dedicated SQL pool, a warehouse you keep running and pay for while it runs. Then they ran four queries a month."),
            leo("Suspect: they bought the wrong size."),
            maya("Close, but no. They bought the wrong kind. Synapse serverless SQL reads files right where they lie in the lake, and charges per query. A few queries a month over lake files: serverless."),
            leo("And if they were starting from scratch today?"),
            maya("Then Microsoft Fabric is the natural pick: SaaS, software as a service, nothing to deploy, with OneLake as one lake for everyone."),
            leo("And when would Databricks have been right?"),
            maya("When the team is data scientists in Python notebooks, pages that mix code and results, using Spark."),
            leo("Spark. In plain words?"),
            maya("An engine that splits a big job across many machines, like a hundred people each counting one box of votes. The storage episode covers Data Lake Gen2 itself, and the application architecture episode has more on Event Hubs."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If it says within seconds or live dashboard, Stream Analytics. If it says nightly copy from on-premises, Data Factory. If the source is behind a firewall with no inbound ports, a self-hosted integration runtime. Existing SSIS packages: the Azure-SSIS runtime. And occasional queries over lake files: serverless SQL."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a data factory with az datafactory create. Make a pipeline that copies a blob from one container to another, then Debug it. Add a schedule trigger and look at the next run time. Then open Monitor and read the copy activity's rows and duration."),
            leo("No more dashboards living in yesterday. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-data-integration",
        listOf(
            maya("Okay, did you hear what the Two Tracks data team did this week? Sit down. The Android study app has gone global, users in Europe and Australia, and the bosses want reports from everything. Including the old booking system in the London office."),
            leo("[laugh] I heard there was a dashboard incident. Start at the start. What's the team actually doing?"),
            maya("Data integration: gathering data from many places into one, on a schedule, cleaning it on the way. And data analysis: asking big questions of it. Their main tool is Azure Data Factory. Picture a removal company with a timetable."),
            leo("A removal company for data. What does it move?"),
            maya("It does ETL, which stands for extract, transform, load: take data out, reshape it, put it somewhere useful."),
            leo("Like the script I wrote that dumps my app's database to a file every night?"),
            maya("That, grown up. And somewhere useful is usually a data lake, a big, cheap store for raw files, like a warehouse where you drop boxes without unpacking. In Azure it's Data Lake Gen2."),
            leo("And the timetable part?"),
            maya("A pipeline is the job list, and each step is an activity, like a copy. A trigger starts it, every night or when a file arrives. And the worker that actually lifts the boxes is the integration runtime. Which brings us to Monday."),
            leo("Monday. Who?"),
            maya("Tom. Tom built the nightly copy from the London office. The office is on-premises, meaning the company's own machines in its own building, behind a firewall, a guard that turns away callers from outside. And Tom used the Azure integration runtime, the one that lives inside Azure."),
            leo("So it couldn't get in."),
            maya("Not a chance. The fix is a self-hosted integration runtime: software on a Windows machine inside the office. Under the hood, it phones out to Azure, asks if there's work, then reads the office database and sends the data up. Outbound only. Nobody has to open the door."),
            leo("Tuesday?"),
            maya("Tuesday was Dana. The boss wanted a live dashboard for the Australian launch: lessons finished per minute, updated within seconds. Dana built it as a Data Factory pipeline. Then, to make it live, she set the trigger to run every minute."),
            leo("Is that bad? Every minute sounds live."),
            maya("It's a removal van leaving every minute. Data Factory runs batches. Within seconds needs a stream."),
            leo("A stream of what, though?"),
            maya("Events. Each finished lesson sends one, a small message saying something happened. They go into Event Hubs, a front door that takes millions a second, and Stream Analytics reads them from there and queries them as they arrive."),
            leo("How does it count per minute, though?"),
            maya("Windowing: it groups events into slices of time, like counting cars per minute at a junction. Each minute, the count comes out, and a new one starts. Dana rebuilt it by Wednesday lunch. Well. Wednesday tea."),
            leo("[chuckle] And Wednesday's story?"),
            maya("Marcus, in finance. Finance has 150 SSIS packages. SSIS is SQL Server Integration Services, SQL Server's older tool for this job, and a package is one saved job. Marcus decided to rewrite every one as a Databricks notebook."),
            leo("All 150? By hand?"),
            maya("By hand. He'd done eleven when Priya, of course, mentioned the Azure-SSIS integration runtime. It runs SSIS packages in Azure as they are. The requirement said minimal rework. That's lift and shift, not a rewrite."),
            leo("Thursday, I'm guessing, the intern."),
            maya("The intern. Bless them. The analysts wanted to ask a few questions of the lake. So the intern created a Synapse dedicated SQL pool. That's a warehouse, a database shaped for big read-only questions, that you keep running and pay for while it runs. Four queries in a month."),
            leo("Ouch. What should they have used?"),
            maya("Serverless SQL, also in Synapse. It reads files right where they lie in the lake and charges per query. Occasional questions, small bill. The dedicated pool is for a busy warehouse that runs all day."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She set up the self-hosted runtime properly, outbound only. But on one office PC. That weekend, the PC restarted for updates, and the nightly copy simply didn't run."),
            leo("So that's where it stops working."),
            maya("Exactly. One machine is one point of failure. Register the runtime on two machines, so one can rest. Priya did, Monday morning, and then picked the analytics platform for the new project: Microsoft Fabric."),
            leo("Why Fabric?"),
            maya("It's SaaS, software as a service: you sign in and use it, with nothing to deploy. Data movement, Spark, a warehouse, real time and Power BI reports in one place, on OneLake, one lake for the whole company."),
            leo("Everyone at lunch kept saying Spark. What is it?"),
            maya("An engine that splits a big job across many machines, like a hundred people each counting one box of votes."),
            leo("And Databricks? Marcus's favourite."),
            maya("Databricks is right when the team is data scientists in Python notebooks, pages that mix code and results, doing machine learning on Spark. The storage episode covers Data Lake Gen2, and the application architecture episode tells you more about Event Hubs."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Nightly copy from on-premises: Data Factory. Behind a firewall, no inbound ports: a self-hosted integration runtime, on two machines. Existing SSIS packages: the Azure-SSIS runtime. Within seconds or live dashboard: Stream Analytics. Data scientists, notebooks, Spark: Databricks."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a data factory with az datafactory create. Make a pipeline that copies a blob from one container to another, then Debug it. Add a schedule trigger and look at the next run time. Then open Monitor and read the copy activity's rows and duration."),
            leo("Two machines, not one. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-data-integration",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about moving data, and making sense of it."),
            ivy("Mm. Moving things in the dark. Where are we moving it from, Nova?"),
            nova("From everywhere. Two Tracks has gone global. The little Android study app is a company now, with a backend on Azure, users in Europe and Australia, and an old booking system in the London office. And somebody wants reports from all of it."),
            ivy("So what's the plan, in plain words?"),
            nova("Data integration: gathering data from many places into one, on a schedule, cleaning it on the way. Then data analysis: asking big questions of it. [pause] The mover is Azure Data Factory. Picture a removal company with a timetable, working while everyone sleeps."),
            ivy("A night shift. What exactly does it do?"),
            nova("ETL, which stands for extract, transform, load. Take data out of a source, reshape it, and put it somewhere useful. Often a data lake, which is a big, cheap store for raw files, like a warehouse where you drop boxes without unpacking them."),
            ivy("And how do you tell the movers what to do?"),
            nova("With a pipeline: the job list. Each step on it is an activity, such as a copy. A trigger starts it, say at two in the morning, or when a file arrives. And the one who actually lifts the boxes is the integration runtime."),
            ivy("Tell me about the one who lifts."),
            nova("There are three. The Azure integration runtime lives inside Azure, and copies cloud to cloud. It's lovely, until it meets the London office."),
            ivy("What's wrong with the London office?"),
            nova("It's on-premises, the company's own machines in its own building, behind a firewall. [pause] A guard on the door who turns away every caller from outside."),
            ivy("So the Azure one can't get in."),
            nova("Never. So you use a self-hosted integration runtime. It's software on a Windows machine inside the office. [pause] Here's how it really works: it phones out to Azure, asks whether there's work tonight, then reads the office database and sends the data up. Outbound only. The guard never opens the door."),
            ivy("Mm. Discreet. And if that one machine falls asleep?"),
            nova("Then nothing moves. That's where it stops working. Register the runtime on two office machines, so one can always answer. [pause] And the third worker is the Azure-SSIS runtime."),
            ivy("SSIS sounds like something hissing in the dark."),
            nova("[chuckle] SQL Server Integration Services. SQL Server's older tool for the same job, and a package is one saved job. If finance already has 150 packages, the Azure-SSIS runtime runs them in Azure as they are. Lift and shift. Nobody rewrites them."),
            ivy("Good. Nobody should work that hard at this hour. Then the data's in the lake. What now?"),
            nova("Now we ask it questions. Two plain words first. Spark is an engine that splits a big job across many machines, like a hundred people each counting one box of votes. And a data warehouse is a database shaped for big read-only questions."),
            ivy("And where do the questions get asked?"),
            nova("Microsoft Fabric, for a new project. It's SaaS, software as a service: you sign in and use it, nothing to deploy. Movement, Spark, a warehouse, real time and Power BI reports, all on OneLake, one lake for the whole company."),
            ivy("And if the company built its warehouse years ago?"),
            nova("Then Synapse Analytics, the older workspace you run yourself. It's still supported for existing warehouses."),
            ivy("And Databricks?"),
            nova("For data scientists in Python notebooks, pages that mix code and results, doing machine learning on Spark. [pause] Listen for the team. SQL analysts and a warehouse: Fabric or Synapse. Data scientists and notebooks: Databricks."),
            ivy("Any trap waiting for me in the dark?"),
            nova("One. A Synapse dedicated SQL pool is a warehouse you keep running and pay for while it runs. Serverless SQL reads files right where they lie, and you pay per query. A few questions a month? Serverless. Don't leave the lights on all night for four queries."),
            ivy("Now the live part. The thing that never sleeps."),
            nova("The status page. Lessons finished per minute, updated within seconds. Each finished lesson sends an event, which is a small message saying something happened. They pour into Event Hubs, a front door that takes in millions of events a second."),
            ivy("And who reads them, that fast?"),
            nova("Stream Analytics. It reads them out of Event Hubs, runs a SQL-like query as they arrive, and writes results every few seconds to a report."),
            ivy("But they never stop. How do you count per minute in an endless stream?"),
            nova("Windowing. It groups events into slices of time, like counting cars per minute at a junction. [pause] A running count, kept in memory, written out when the minute closes."),
            ivy("Couldn't the night movers do that too?"),
            nova("No. Data Factory runs batches. Nightly is the opposite of within seconds."),
            ivy("And Stream Analytics? Can it do everything, then?"),
            nova("It stops working the other way: it answers this minute, not ten years of history. That belongs in the lake. The storage episode covers the lake itself, and the application architecture episode is your next late night, for Event Hubs."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. Nightly copy from on-premises means Data Factory. Behind a firewall, no inbound ports, means a self-hosted integration runtime. Existing SSIS packages mean the Azure-SSIS runtime. Within seconds, or live dashboard, means Stream Analytics. And data scientists with notebooks and Spark mean Databricks."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a data factory with az datafactory create. Make a pipeline that copies a blob from one container to another, then Debug it. [pause] Add a schedule trigger and look at the next run time. Then open Monitor and read the copy activity's rows and duration."),
            ivy("Movers by night, a counter that never sleeps. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-data-integration",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about moving in together. Well. Moving data in together."),
            rafe("I knew there was a catch. [laugh] Whose data are we moving?"),
            nova("Two Tracks. The Android study app has gone global: a company with a backend on Azure, users in Europe and Australia, and an old booking system in the London office. Data integration means gathering it all into one place, on a schedule, cleaning it on the way."),
            rafe("And then we stare at it lovingly. That's data analysis, asking big questions of it."),
            nova("Exactly. The mover is Azure Data Factory: a removal company with a timetable. It does ETL, which stands for extract, transform, load. Take data out, reshape it, put it somewhere useful."),
            rafe("Somewhere useful being a data lake. A big, cheap store for raw files, like a warehouse where you drop boxes without unpacking. My kind of tidiness."),
            nova("Mm. And the job list is a pipeline, each step an activity, started by a trigger, every night or when a file arrives."),
            rafe("And who does the heavy lifting? Not me, I hope."),
            nova("The integration runtime, the worker that lifts the boxes. First question, exam style. The London office is behind a firewall, and no inbound ports may be opened. Which runtime?"),
            rafe("A firewall being the guard on the door who turns away callers from outside. So, the self-hosted integration runtime. Software on a Windows machine inside the office. It phones out to Azure, asks if there's work, then sends the data up. Nobody ever calls in."),
            nova("Right answer. Some of us only go outbound. [kiss]"),
            rafe("And it shouldn't sleep alone. One machine is one point of failure, so register it on two. Now mine. Cloud to cloud, blobs from Blob Storage into the lake. Which runtime?"),
            nova("The Azure integration runtime. Computing power Azure runs for us; we look after nothing."),
            rafe("Right answer. Low maintenance. Like you, mostly. [kiss]"),
            nova("Mostly? Here's a harder one, then. Finance has 150 SSIS packages, and wants them in Azure with minimal rework. What do you recommend?"),
            rafe("SSIS, SQL Server Integration Services, SQL Server's older tool for moving data, and a package is one saved job. I'd rewrite them in Databricks notebooks. Fresh start."),
            nova("[sigh] No kiss for that one. Minimal rework means the Azure-SSIS integration runtime: it runs the packages in Azure as they are. Lift and shift. Rewriting 150 is the opposite of minimal. Some baggage you just carry with you."),
            rafe("Fair. So when is Databricks right?"),
            nova("When the team is data scientists in Python notebooks, pages that mix code and results, doing machine learning on Spark. Spark is an engine that splits a big job across many machines, like a hundred people each counting one box of votes."),
            rafe("And a data warehouse is a database shaped for big read-only questions. Where do the SQL analysts go?"),
            nova("For a new project, Microsoft Fabric. It's SaaS, software as a service: sign in and use it, nothing to deploy. Data movement, Spark, a warehouse, real time and Power BI reports, all on OneLake, one lake for the whole company. Synapse Analytics is the older workspace you run yourself, still supported."),
            rafe("Then my question. Analysts want a few SQL queries a month straight over files in the lake, without keeping servers running. Which fits?"),
            nova("Synapse serverless SQL. It reads files right where they lie, and you pay per query. A dedicated SQL pool is a warehouse you keep running and pay for all the time."),
            rafe("Right answer. Only pay for what you use. [kiss]"),
            nova("Mm. Now the live part. The status page wants lessons finished per minute, updated within seconds. Each finished lesson sends an event, which is a small message saying something happened. Which service processes them?"),
            rafe("A Data Factory pipeline, every minute. Quick little trips."),
            nova("No kiss. A pipeline is a removal van; it runs batches. Within seconds needs a stream."),
            rafe("A stream into where?"),
            nova("Into Event Hubs, a front door that takes in millions of events a second. Then Stream Analytics reads them from there, runs a SQL-like query as they arrive, and writes results every few seconds."),
            rafe("And counting per minute, when they never stop coming?"),
            nova("Windowing. It groups events into slices of time, like counting cars per minute at a junction. Under the picture, a running count kept in memory, written out when the minute closes. And where it stops working: it answers this minute, not years of history. That's the lake's job."),
            rafe("So the same question again, darling. Live dashboard, within seconds?"),
            nova("Stream Analytics, reading from Event Hubs."),
            rafe("Right answer. Second time lucky. [kiss]"),
            nova("And under it all, Data Factory holds no data. It's the conductor; the runtimes do the lifting. The storage episode covers Data Lake Gen2, and the application architecture episode has more on Event Hubs."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Nightly copy from on-premises: Data Factory. Behind a firewall, no inbound ports: a self-hosted integration runtime. Existing SSIS packages: the Azure-SSIS runtime. Within seconds or live dashboard: Stream Analytics. Data scientists with notebooks and Spark: Databricks."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a data factory with az datafactory create. Make a pipeline that copies a blob from one container to another, then Debug it. Add a schedule trigger and look at the next run time. Then open Monitor and read the copy activity's rows and duration."),
            nova("And you're the only pipeline I'd run every night. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "Nightly, Data Factory copies office data into the lake for analysis; live app events flow through Stream Analytics.",
        nodes = listOf(
            DNode("onprem", "Office SQL Server", 0.15f, 0.2f, w = 0.26f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
            DNode("shir", "Self-hosted IR", 0.5f, 0.2f, w = 0.26f, h = 0.2f, tone = 1, sub = "on an office PC"),
            DNode("adf", "Data Factory", 0.85f, 0.2f, w = 0.26f, h = 0.2f, sub = "pipeline, nightly"),
            DNode("lake", "Data Lake Gen2", 0.85f, 0.55f, w = 0.26f, h = 0.2f, kind = NodeKind.CYLINDER),
            DNode("dbx", "Databricks", 0.5f, 0.55f, w = 0.26f, h = 0.2f, tone = 1, sub = "clean and join"),
            DNode("bi", "Power BI report", 0.15f, 0.55f, w = 0.26f, h = 0.2f, tone = 2),
            DNode("app", "App events", 0.15f, 0.87f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
            DNode("eh", "Event Hubs", 0.5f, 0.87f, w = 0.26f, h = 0.18f, tone = 2),
            DNode("asa", "Stream Analytics", 0.85f, 0.87f, w = 0.26f, h = 0.18f, tone = 1),
        ),
        edges = listOf(
            DEdge("onprem", "shir"),
            DEdge("shir", "adf", "outbound"),
            DEdge("adf", "lake", "copy"),
            DEdge("lake", "dbx"),
            DEdge("dbx", "bi"),
            DEdge("app", "eh"),
            DEdge("eh", "asa"),
            DEdge("asa", "bi", "live", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun pipeline() = DiagramSpec(
        "A trigger starts the pipeline; its copy activity runs on an integration runtime and lands the data in the lake.",
        nodes = listOf(
            DNode("trigger", "Trigger", 0.18f, 0.15f, w = 0.3f, h = 0.2f, kind = NodeKind.PILL, sub = "2 am, or a file"),
            DNode("pipe", "Pipeline", 0.5f, 0.15f, w = 0.3f, h = 0.2f, tone = 1, sub = "the job list"),
            DNode("act", "Copy activity", 0.82f, 0.15f, w = 0.3f, h = 0.2f, sub = "one step"),
            DNode("linked", "Linked service", 0.25f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "address and door code"),
            DNode("ir", "Integration runtime", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "does the lifting"),
            DNode("lake", "Data Lake Gen2", 0.5f, 0.85f, w = 0.5f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "the data lake"),
        ),
        edges = listOf(
            DEdge("trigger", "pipe", "starts"),
            DEdge("pipe", "act"),
            DEdge("linked", "act", dashed = true),
            DEdge("act", "ir", "runs on"),
            DEdge("ir", "lake", "copy"),
        ),
        aspect = 0.8f,
    )

    private fun runtimes() = DiagramSpec(
        "Three integration runtimes: Azure for cloud to cloud, self-hosted for sources behind a firewall, Azure-SSIS for existing packages.",
        nodes = listOf(
            DNode("blob", "Blob Storage", 0.24f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER),
            DNode("air", "Azure IR", 0.76f, 0.15f, w = 0.42f, h = 0.2f, tone = 1, sub = "cloud to cloud"),
            DNode("office", "Office database", 0.24f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
            DNode("shir", "Self-hosted IR", 0.76f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "phones out only"),
            DNode("ssis", "150 SSIS packages", 0.24f, 0.85f, w = 0.42f, h = 0.2f, tone = 2),
            DNode("assis", "Azure-SSIS IR", 0.76f, 0.85f, w = 0.42f, h = 0.2f, tone = 1, sub = "runs them as they are"),
        ),
        edges = listOf(
            DEdge("blob", "air", "copy"),
            DEdge("office", "shir", "outbound"),
            DEdge("ssis", "assis", "lift and shift"),
        ),
        aspect = 0.8f,
    )

    private fun analyse() = DiagramSpec(
        "The lake feeds three places to analyse; the team decides: SQL analysts lean to Fabric or Synapse, data scientists to Databricks.",
        nodes = listOf(
            DNode("lake", "The data lake", 0.5f, 0.14f, w = 0.5f, h = 0.2f, kind = NodeKind.CYLINDER),
            DNode("fabric", "Fabric", 0.18f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "SaaS, OneLake"),
            DNode("syn", "Synapse", 0.5f, 0.5f, w = 0.3f, h = 0.2f, sub = "SQL and Spark"),
            DNode("dbx", "Databricks", 0.82f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "notebooks, ML"),
            DNode("analysts", "SQL analysts", 0.26f, 0.85f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Fabric or Synapse"),
            DNode("sci", "Data scientists", 0.76f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Databricks"),
        ),
        edges = listOf(
            DEdge("lake", "fabric"),
            DEdge("lake", "syn"),
            DEdge("lake", "dbx"),
            DEdge("analysts", "fabric"),
            DEdge("sci", "dbx"),
        ),
        aspect = 0.8f,
    )

    private fun pools() = DiagramSpec(
        "A dedicated SQL pool runs and charges all the time; serverless SQL reads lake files where they lie and charges per query.",
        nodes = listOf(
            DNode("syn", "Synapse", 0.5f, 0.14f, w = 0.5f, h = 0.2f, tone = 1),
            DNode("ded", "Dedicated SQL pool", 0.25f, 0.5f, w = 0.42f, h = 0.22f, tone = 2, sub = "pay while it runs"),
            DNode("sls", "Serverless SQL", 0.75f, 0.5f, w = 0.42f, h = 0.22f, tone = 1, sub = "pay per query"),
            DNode("busy", "Busy all day", 0.25f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "a real warehouse"),
            DNode("lake", "Lake files", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER),
        ),
        edges = listOf(
            DEdge("syn", "ded"),
            DEdge("syn", "sls"),
            DEdge("ded", "busy"),
            DEdge("sls", "lake", "reads in place"),
        ),
        aspect = 0.8f,
    )

    private fun stream() = DiagramSpec(
        "App events pour into Event Hubs; Stream Analytics counts them in one-minute windows and updates the report within seconds.",
        nodes = listOf(
            DNode("app", "App events", 0.15f, 0.2f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
            DNode("eh", "Event Hubs", 0.5f, 0.2f, w = 0.3f, h = 0.2f, tone = 2, sub = "millions a second"),
            DNode("asa", "Stream Analytics", 0.83f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "SQL-like query"),
            DNode("bi", "Power BI report", 0.45f, 0.55f, w = 0.36f, h = 0.2f, tone = 2, sub = "within seconds"),
            DNode("win", "One-minute window", 0.83f, 0.55f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, sub = "a running count"),
            DNode("batch", "Nightly batch is not within seconds", 0.5f, 0.87f, w = 0.84f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("app", "eh", "events"),
            DEdge("eh", "asa"),
            DEdge("asa", "win"),
            DEdge("asa", "bi", "results"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's data integration questions, each as the phrase to listen for and the service it points to.",
        nodes = listOf(
            DNode("nightly", "Nightly copy, on-premises", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Data Factory"),
            DNode("firewall", "No inbound ports", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "self-hosted IR"),
            DNode("ssis", "Existing SSIS packages", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Azure-SSIS IR"),
            DNode("live", "Within seconds", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Stream Analytics"),
            DNode("sci", "Notebooks and Spark", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Databricks"),
            DNode("few", "A few lake queries", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "serverless SQL"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("nightly", "firewall", "ssis", "live", "sci", "few")
    private val etl = BoardCard("ETL", "Extract, transform, load: take data out, reshape it, put it somewhere useful.")
    private val dataLake = BoardCard("A data lake", "A big, cheap store for raw files: boxes dropped without unpacking.")
    private val selfHosted = BoardCard("Self-hosted IR", "On an office Windows PC. Phones out, asks for work, sends data up.")
    private val twoMachines = BoardCard("Two machines", "One machine is one point of failure. Register the runtime on two.")
    private val azureSsis = BoardCard("Azure-SSIS IR", "Runs existing SSIS packages as they are: lift and shift.")
    private val spark = BoardCard("Spark", "Splits a big job across many machines: a hundred vote counters.")
    private val fabric = BoardCard("Microsoft Fabric", "SaaS: sign in, nothing to deploy. OneLake for the whole company.")
    private val team = BoardCard("Listen for the team", "SQL analysts: Fabric or Synapse. Data scientists: Databricks.")
    private val serverless = BoardCard("Serverless SQL", "Reads lake files where they lie; pay per query. Ideal for a few.")
    private val windowing = BoardCard("Windowing", "Slices of time, like counting cars per minute at a junction.")
    private val eventHubs = BoardCard("Event Hubs", "A front door for millions of events a second.")
    private val examCard = BoardCard("Listen for the phrase", "Each requirement points to one service.")
    private val today = BoardCard("Today", "A factory, a blob copy pipeline, a schedule trigger, then Monitor.")

    private fun classicBoard() = PodcastBoard(
        "sa-data-integration",
        listOf(
            // 0: "Hi, and welcome back. Today is data integration and analysis..."
            Scene(
                0, "Two Tracks goes global", flow(),
                beats = listOf(
                    Beat(0, show = setOf("onprem", "app"), light = setOf("onprem"), card = BoardCard("Two Tracks goes global", "An Android app turned company, with an old office system in London.")),
                    Beat(2, show = setOf("shir", "adf", "lake", "dbx", "bi", "eh", "asa"), light = setOf("adf", "dbx"), card = BoardCard("Integration and analysis", "Gather data into one place, then ask it big questions.")),
                    Beat(4, light = setOf("adf", "dbx", "asa"), card = BoardCard("The choices", "A mover, three places to analyse, and a real-time engine.")),
                ),
            ),
            // 5: "Data Factory. Sounds like it makes data."
            Scene(
                5, "The mover", pipeline(),
                beats = listOf(
                    Beat(6, show = setOf("pipe"), light = setOf("pipe"), card = etl),
                    Beat(8, show = setOf("lake"), light = setOf("lake"), card = dataLake),
                    Beat(10, show = setOf("act", "linked"), light = setOf("pipe", "act"), pulse = "pipe" to "act", card = BoardCard("Pipeline", "The job list. Each step is an activity, such as a copy.")),
                    Beat(12, show = setOf("trigger"), light = setOf("trigger"), pulse = "trigger" to "pipe", card = BoardCard("Trigger", "Starts the pipeline on a schedule, or when a file arrives.")),
                    Beat(14, show = setOf("ir"), light = setOf("ir"), pulse = "act" to "ir", card = BoardCard("Integration runtime", "The worker that does the lifting. Where it sits matters.")),
                ),
            ),
            // 15: "Three? In my day job..."
            Scene(
                15, "Three workers", runtimes(),
                beats = listOf(
                    Beat(16, show = setOf("blob", "air"), light = setOf("air"), pulse = "blob" to "air", card = BoardCard("Azure IR", "Cloud to cloud. Azure runs it; you look after nothing.")),
                    Beat(18, show = setOf("office"), light = setOf("office"), card = BoardCard("Behind a firewall", "On-premises, with a guard on the door that turns callers away.")),
                    Beat(20, show = setOf("shir"), light = setOf("shir"), pulse = "office" to "shir", card = selfHosted),
                    Beat(26, show = setOf("ssis", "assis"), light = setOf("assis"), pulse = "ssis" to "assis", card = azureSsis),
                    Beat(28, light = setOf("ssis"), card = BoardCard("Minimal rework", "Rewriting 150 packages is the classic wrong answer.")),
                ),
            ),
            // 29: "Where do I ask them? You said three places."
            Scene(
                29, "Where to analyse", analyse(),
                beats = listOf(
                    Beat(30, show = setOf("lake"), light = setOf("lake"), card = spark),
                    Beat(34, show = setOf("fabric"), light = setOf("fabric"), pulse = "lake" to "fabric", card = fabric),
                    Beat(36, show = setOf("syn"), light = setOf("syn"), pulse = "lake" to "syn", card = BoardCard("Synapse Analytics", "SQL and Spark in a workspace you run. Fine for existing warehouses.")),
                    Beat(40, show = setOf("dbx", "analysts", "sci"), light = setOf("dbx", "sci"), pulse = "sci" to "dbx", card = team),
                ),
            ),
            // 41: "Any trap in there?"
            Scene(
                41, "Dedicated or serverless", pools(),
                beats = listOf(
                    Beat(42, show = setOf("syn", "ded", "sls", "busy", "lake"), light = setOf("sls", "lake"), pulse = "sls" to "lake", card = serverless),
                ),
            ),
            // 43: "Right. Now the live stuff. You said real time."
            Scene(
                43, "Within seconds", stream(),
                beats = listOf(
                    Beat(44, show = setOf("app"), light = setOf("app"), card = BoardCard("An event", "A small message saying something happened: a lesson finished.")),
                    Beat(46, show = setOf("eh", "asa", "bi"), light = setOf("eh", "asa"), pulse = "eh" to "asa", card = eventHubs),
                    Beat(50, show = setOf("win"), light = setOf("win"), pulse = "asa" to "win", card = windowing),
                    Beat(52, show = setOf("batch"), light = setOf("batch"), card = BoardCard("Not Data Factory", "It runs batches. Nightly is the opposite of within seconds.")),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("nightly", "firewall", "ssis", "live", "sci"), card = examCard),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-data-integration--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Dashboard That Lived in Yesterday..."
            Scene(
                0, "The Dashboard in Yesterday", stream(),
                beats = listOf(
                    Beat(0, show = setOf("bi"), light = setOf("bi"), card = BoardCard("The case", "Launch day in Australia: the live dashboard showed yesterday.")),
                    Beat(2, card = BoardCard("Integration and analysis", "Gather data into one place, then ask it big questions.")),
                ),
            ),
            // 3: "And the tool they used?"
            Scene(
                3, "Data Factory for everything", pipeline(),
                beats = listOf(
                    Beat(4, show = setOf("pipe"), light = setOf("pipe"), card = etl),
                    Beat(8, show = setOf("lake", "act", "trigger"), light = setOf("trigger"), pulse = "trigger" to "pipe", card = BoardCard("Pipeline and trigger", "The job list, started nightly or when a file arrives.")),
                    Beat(9, card = BoardCard("Suspect: the trigger", "Run it every minute? It's still a removal van.")),
                ),
            ),
            // 10: "Wrong suspect, though a popular one..."
            Scene(
                10, "Clue one: a stream", stream(),
                beats = listOf(
                    Beat(10, show = setOf("batch"), light = setOf("batch"), card = BoardCard("Wrong suspect", "Data Factory runs batches. Nightly is not within seconds.")),
                    Beat(14, show = setOf("app", "eh", "asa"), light = setOf("eh", "asa"), pulse = "eh" to "asa", card = BoardCard("Event Hubs, Stream Analytics", "Millions of events a second, queried as they arrive.")),
                    Beat(16, show = setOf("win", "bi"), light = setOf("win"), pulse = "asa" to "win", card = BoardCard("Windowing", "A running count per minute, written out when the minute closes.")),
                ),
            ),
            // 17: "Clue two, then. I can feel one coming."
            Scene(
                17, "Clue two: the office", runtimes(),
                beats = listOf(
                    Beat(18, show = setOf("office"), light = setOf("office"), card = BoardCard("Could not connect", "The nightly copy from the London office failed.")),
                    Beat(20, show = setOf("blob", "air"), light = setOf("air"), card = BoardCard("Azure IR", "Lives inside Azure. The office firewall turns it away.")),
                    Beat(24, show = setOf("shir"), light = setOf("shir"), pulse = "office" to "shir", card = selfHosted),
                    Beat(26, card = twoMachines),
                ),
            ),
            // 27: "Clue three. Who's next?"
            Scene(
                27, "Clue three: finance", runtimes(),
                beats = listOf(
                    Beat(28, show = setOf("ssis"), light = setOf("ssis"), card = BoardCard("150 SSIS packages", "Being rewritten as notebooks: twelve done in three months.")),
                    Beat(30, show = setOf("assis"), light = setOf("assis"), pulse = "ssis" to "assis", card = azureSsis),
                ),
            ),
            // 31: "Last clue. There's always a money one."
            Scene(
                31, "The last clue: the bill", pools(),
                beats = listOf(
                    Beat(32, show = setOf("syn", "ded", "busy"), light = setOf("ded"), card = BoardCard("Dedicated SQL pool", "Kept running and paid for, for four queries a month.")),
                    Beat(34, show = setOf("sls", "lake"), light = setOf("sls"), pulse = "sls" to "lake", card = serverless),
                ),
            ),
            // 37: "And when would Databricks have been right?"
            Scene(
                37, "Who analyses?", analyse(),
                beats = listOf(
                    Beat(38, show = setOf("lake", "fabric", "syn", "dbx", "analysts", "sci"), light = setOf("dbx", "sci"), pulse = "sci" to "dbx", card = team),
                ),
            ),
            // 41: "So, the case summary for the exam?"
            Scene(
                41, "Case closed", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("live", "nightly", "firewall", "ssis", "few"), card = examCard),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-data-integration--gossip",
        listOf(
            // 0: "Okay, did you hear what the Two Tracks data team did this week?..."
            Scene(
                0, "The data team's week", flow(),
                beats = listOf(
                    Beat(0, show = setOf("onprem"), light = setOf("onprem"), card = BoardCard("Two Tracks goes global", "Reports from everything, even the London office system.")),
                    Beat(2, show = setOf("adf", "lake"), light = setOf("adf"), card = BoardCard("Data integration", "Gather data into one place on a schedule, cleaning it on the way.")),
                    Beat(4, light = setOf("lake"), pulse = "adf" to "lake", card = etl),
                    Beat(8, card = BoardCard("Pipeline, trigger, runtime", "The job list, its start time, and the worker that lifts.")),
                ),
            ),
            // 9: "Monday. Who?"
            Scene(
                9, "Monday: Tom", runtimes(),
                beats = listOf(
                    Beat(10, show = setOf("office", "blob", "air"), light = setOf("air"), card = BoardCard("Azure IR at the office", "The firewall turns it away. It can't reach in.")),
                    Beat(12, show = setOf("shir"), light = setOf("shir"), pulse = "office" to "shir", card = selfHosted),
                ),
            ),
            // 13: "Tuesday?"
            Scene(
                13, "Tuesday: Dana", stream(),
                beats = listOf(
                    Beat(14, show = setOf("batch"), light = setOf("batch"), card = BoardCard("A pipeline every minute", "Still a removal van, just leaving more often.")),
                    Beat(18, show = setOf("app", "eh", "asa"), light = setOf("asa"), pulse = "eh" to "asa", card = BoardCard("Stream Analytics", "Queries events from Event Hubs as they arrive.")),
                    Beat(20, show = setOf("win", "bi"), light = setOf("win"), pulse = "asa" to "win", card = windowing),
                ),
            ),
            // 21: "And Wednesday's story?"
            Scene(
                21, "Wednesday: Marcus", runtimes(),
                beats = listOf(
                    Beat(22, show = setOf("ssis"), light = setOf("ssis"), card = BoardCard("150 SSIS packages", "Marcus began rewriting every one as a notebook.")),
                    Beat(24, show = setOf("assis"), light = setOf("assis"), pulse = "ssis" to "assis", card = azureSsis),
                ),
            ),
            // 25: "Thursday, I'm guessing, the intern."
            Scene(
                25, "Thursday: the intern", pools(),
                beats = listOf(
                    Beat(26, show = setOf("syn", "ded", "busy"), light = setOf("ded"), card = BoardCard("Dedicated SQL pool", "Kept running and paid for, for four queries a month.")),
                    Beat(28, show = setOf("sls", "lake"), light = setOf("sls"), pulse = "sls" to "lake", card = serverless),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", runtimes(),
                beats = listOf(
                    Beat(30, show = setOf("office", "shir"), light = setOf("shir"), pulse = "office" to "shir", card = BoardCard("One office PC", "It restarted for updates, and the nightly copy never ran.")),
                    Beat(32, card = twoMachines),
                ),
            ),
            // 33: "Why Fabric?"
            Scene(
                33, "Picking a platform", analyse(),
                beats = listOf(
                    Beat(34, show = setOf("lake", "fabric", "syn"), light = setOf("fabric"), pulse = "lake" to "fabric", card = fabric),
                    Beat(38, show = setOf("dbx", "analysts", "sci"), light = setOf("dbx"), pulse = "sci" to "dbx", card = team),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("nightly", "firewall", "ssis", "live", "sci"), card = examCard),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-data-integration--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Moving data at night", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Moving data, and making sense of it.")),
                    Beat(2, show = setOf("onprem", "app"), light = setOf("onprem"), card = BoardCard("Two Tracks goes global", "Users in Europe and Australia, and an old office system.")),
                    Beat(4, show = setOf("shir", "adf", "lake", "dbx", "bi", "eh", "asa"), light = setOf("adf"), card = BoardCard("Data integration", "Gather data into one place on a schedule; then analyse it.")),
                ),
            ),
            // 5: "A night shift. What exactly does it do?"
            Scene(
                5, "The night movers", pipeline(),
                beats = listOf(
                    Beat(6, show = setOf("pipe", "lake"), light = setOf("lake"), card = etl),
                    Beat(8, show = setOf("trigger", "act", "ir"), light = setOf("pipe"), pulse = "trigger" to "pipe", card = BoardCard("Pipeline and trigger", "The job list, started at two in the morning or when a file lands.")),
                ),
            ),
            // 9: "Tell me about the one who lifts."
            Scene(
                9, "Who lifts the boxes", runtimes(),
                beats = listOf(
                    Beat(10, show = setOf("blob", "air", "office"), light = setOf("office"), card = BoardCard("Azure IR and the firewall", "Cloud to cloud is fine; the office guard turns it away.")),
                    Beat(14, show = setOf("shir"), light = setOf("shir"), pulse = "office" to "shir", card = selfHosted),
                    Beat(16, card = twoMachines),
                    Beat(18, show = setOf("ssis", "assis"), light = setOf("assis"), pulse = "ssis" to "assis", card = azureSsis),
                ),
            ),
            // 19: "Good. Nobody should work that hard at this hour..."
            Scene(
                19, "Asking questions", analyse(),
                beats = listOf(
                    Beat(20, show = setOf("lake"), light = setOf("lake"), card = spark),
                    Beat(22, show = setOf("fabric", "syn"), light = setOf("fabric"), pulse = "lake" to "fabric", card = fabric),
                    Beat(26, show = setOf("dbx", "analysts", "sci"), light = setOf("dbx"), pulse = "sci" to "dbx", card = team),
                ),
            ),
            // 27: "Any trap waiting for me in the dark?"
            Scene(
                27, "Lights left on", pools(),
                beats = listOf(
                    Beat(28, show = setOf("syn", "ded", "sls", "busy", "lake"), light = setOf("ded", "sls"), pulse = "sls" to "lake", card = serverless),
                ),
            ),
            // 29: "Now the live part. The thing that never sleeps."
            Scene(
                29, "The one that never sleeps", stream(),
                beats = listOf(
                    Beat(30, show = setOf("app", "eh"), light = setOf("eh"), pulse = "app" to "eh", card = eventHubs),
                    Beat(34, show = setOf("asa", "win", "bi"), light = setOf("win"), pulse = "asa" to "win", card = windowing),
                    Beat(36, show = setOf("batch"), light = setOf("batch"), card = BoardCard("Where it stops", "Batches aren't seconds; a stream isn't years of history.")),
                ),
            ),
            // 39: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("nightly", "firewall", "ssis", "live", "sci"), card = examCard),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-data-integration--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Moving data in together: integration and analysis.")),
                    Beat(2, show = setOf("onprem", "adf", "lake", "app"), light = setOf("onprem"), card = BoardCard("Two Tracks goes global", "Europe, Australia, and an old booking system in London.")),
                    Beat(4, show = setOf("shir", "dbx", "bi", "eh", "asa"), light = setOf("adf"), pulse = "adf" to "lake", card = etl),
                ),
            ),
            // 6: "Mm. And the job list is a pipeline... First question, exam style."
            Scene(
                6, "Who lifts?", runtimes(),
                beats = listOf(
                    Beat(8, show = setOf("office"), light = setOf("office"), card = BoardCard("Quiz: behind a firewall", "No inbound ports may be opened. Which runtime?")),
                    Beat(10, show = setOf("shir"), light = setOf("shir"), pulse = "office" to "shir", card = BoardCard("Right answer", "Self-hosted IR: phones out only. Register it on two machines.")),
                    Beat(11, show = setOf("blob", "air"), light = setOf("blob"), card = BoardCard("Quiz: cloud to cloud", "Blobs into the lake. Which runtime?")),
                    Beat(13, light = setOf("air"), pulse = "blob" to "air", card = BoardCard("Right answer", "The Azure IR. Azure runs it; you look after nothing.")),
                    Beat(14, show = setOf("ssis"), light = setOf("ssis"), card = BoardCard("Quiz: 150 SSIS packages", "Into Azure with minimal rework. What do you recommend?")),
                    Beat(16, show = setOf("assis"), light = setOf("assis"), pulse = "ssis" to "assis", card = BoardCard("No kiss: Azure-SSIS IR", "Runs them as they are. Rewriting 150 is not minimal.")),
                ),
            ),
            // 17: "Fair. So when is Databricks right?"
            Scene(
                17, "Where to analyse", analyse(),
                beats = listOf(
                    Beat(18, show = setOf("lake", "dbx", "sci"), light = setOf("dbx"), pulse = "sci" to "dbx", card = spark),
                    Beat(20, show = setOf("fabric", "syn", "analysts"), light = setOf("fabric"), pulse = "lake" to "fabric", card = fabric),
                ),
            ),
            // 21: "Then my question. Analysts want a few SQL queries a month..."
            Scene(
                21, "Pay for what you use", pools(),
                beats = listOf(
                    Beat(21, show = setOf("syn", "lake"), light = setOf("lake"), card = BoardCard("Quiz: a few lake queries", "SQL over lake files, no servers kept running. Which fits?")),
                    Beat(23, show = setOf("sls", "ded", "busy"), light = setOf("sls"), pulse = "sls" to "lake", card = BoardCard("Right answer", "Serverless SQL: pay per query. A dedicated pool runs all day.")),
                ),
            ),
            // 24: "Mm. Now the live part. The status page..."
            Scene(
                24, "Within seconds", stream(),
                beats = listOf(
                    Beat(24, show = setOf("app", "eh"), light = setOf("app"), card = BoardCard("Quiz: the status page", "Lessons per minute, within seconds. Which service?")),
                    Beat(26, show = setOf("batch", "asa"), light = setOf("asa"), pulse = "eh" to "asa", card = BoardCard("No kiss: not a pipeline", "Event Hubs, then Stream Analytics, as events arrive.")),
                    Beat(30, show = setOf("win", "bi"), light = setOf("win"), pulse = "asa" to "win", card = windowing),
                    Beat(33, light = setOf("asa"), pulse = "asa" to "bi", card = BoardCard("Right answer", "Stream Analytics, reading from Event Hubs.")),
                ),
            ),
            // 35: "Last question, for the exam. What do we remember?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("nightly", "firewall", "ssis", "live", "sci"), card = BoardCard("Listen for the phrase", "Each requirement points to one service. Five for five.")),
                    Beat(39, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
