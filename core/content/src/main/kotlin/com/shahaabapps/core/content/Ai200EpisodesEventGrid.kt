package com.shahaabapps.core.content

/** Event Grid: custom events, filters and retries: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesEventGrid {
    private val classic = Podcast(
        "ai-eventgrid",
        listOf(
            maya("Hi, and welcome back. Zaiqa is in good shape. The Zaiqa API, the service the phone talks to, runs in Container Apps, orders sit in Azure SQL, the event feed is in Cosmos, and the dish photos are in Blob Storage."),
            leo("And the photos just sit there."),
            maya("They do, and that is today's problem. A restaurant uploads a photo of their new paneer tikka from the partner dashboard. It lands in Blob Storage, and nothing happens. No thumbnail, nothing in search."),
            leo("So the dashboard should call something after the upload."),
            maya("It could, and then every new job means changing the dashboard. Instead we let the upload announce itself. That is Azure Event Grid: a notice board that pushes. News goes up, and everyone who asked gets a copy."),
            leo("News. We already push every paid order to the order worker. Why is this different?"),
            maya("Because an order is a message, and a photo upload is an event. A message is a job handed to one worker, with an outcome the sender waits for: take this order to the restaurant. That goes on a Service Bus queue."),
            leo("And an event?"),
            maya("An event is only news: a photo was uploaded. The publisher does not know who is listening, does not wait, and does not mind."),
            leo("That is the exam's favourite pair, I imagine."),
            maya("It is. If one item must be worked through once, by one worker, in order, it is a message and a queue. If something happened and several parts may care, it is an event."),
            leo("Fine. So who publishes the photo news? The dashboard?"),
            maya("Nobody of ours. The storage account posts it itself, onto a system topic. A topic is one notice board, and a system topic is where an Azure service posts news about itself."),
            leo("So we write no publishing code at all?"),
            maya("None. We make the system topic on the photos storage account, and subscribe. Storage posts Blob Created every time a file lands. The other kind of board is a custom topic, which our own code posts to."),
            leo("For example?"),
            maya("Say you're building a feature that announces a restaurant going offline. No Azure service knows that, so we make a custom topic and publish it ourselves, with the Data Sender role on the API's managed identity, the badge Azure gives your app."),
            leo("Right. And signing up?"),
            maya("A subscription: a standing order that says send me these events, at this address. Ours points at the photo function, Zaiqa's Python one that cuts thumbnails. One topic carries many subscriptions, each with its own copy."),
            leo("Does my function get every file in the account?"),
            maya("It would, which is why a subscription carries filters: rules deciding which events get through. There are three kinds."),
            leo("Start with the first."),
            maya("An event type filter. Blob Created only. Without it you also get Blob Deleted, and your function makes a thumbnail of a photo that no longer exists."),
            leo("Second?"),
            maya("A subject filter. Every event carries a subject saying what it happened to, written like a path. For a blob: slash blob services, slash default, slash containers, slash dish photos, then the file name."),
            leo("So I can take one container only."),
            maya("Begins with that container path, yes. Or ends with dot jpeg, so the invoices nobody wants thumbnails of are left alone. Begins with and ends with are all it can do."),
            leo("Then the third does more."),
            maya("An advanced filter. It looks inside the event's data and tests a field with an operator, like greater than. Picture a tiny placeholder the dashboard uploads: data dot contentLength, NumberGreaterThan, ten thousand, and the function sleeps on."),
            leo("Good. What does the event itself look like?"),
            maya("Like a letter in a standard envelope, and the envelope's layout is the schema. Prefer CloudEvents 1.0: an open standard, meaning a layout many companies agreed on."),
            leo("And what is written on the envelope?"),
            maya("An id, a source saying who sent it, a type saying what happened, the subject, a time, and the data: the blob's address, its size, its content type. You choose the schema when you make the topic."),
            leo("Our photo function is Python. Does it do anything special to receive one?"),
            maya("Almost nothing, thanks to its Event Grid trigger: the setting that wakes it for each event. That trigger also answers the handshake, and the handshake is the part that catches people."),
            leo("What handshake?"),
            maya("Event Grid will not push to a plain webhook, which is just a web address on your own server, until that address proves it wants the events. Otherwise anyone could aim Event Grid at somebody else's server and flood it."),
            leo("And proving it means?"),
            maya("With CloudEvents, it sends an options request with a request origin header, and your address answers with an allowed origin header. With the older Event Grid schema it sends a validation event, and you echo the code back, or open the link before it expires."),
            leo("And if I miss it?"),
            maya("The subscription never comes alive, and not one event arrives. [pause] A webhook that receives nothing at all is the handshake, almost never a filter."),
            leo("Now the frightening part. My function is down during a release. What happens to the photos?"),
            maya("Event Grid is patient. Your handler must answer with a success code, a number like 200 that means OK, within 30 seconds. Anything else is a failure, and it tries again, waiting longer each time: ten seconds, thirty, a minute, up to hours."),
            leo("Exponential backoff, like my Android app retrying a failed call."),
            maya("The same idea, done for you. By default it gives up after 30 attempts or 24 hours, whichever comes first, and you can lower both. A 400, meaning the event itself was bad, is not retried."),
            leo("And then it keeps the ones it could not deliver."),
            maya("[pause] No. By default it drops them. Gone. That is the big trap. The fix is dead-lettering: point the subscription at a blob container, and undelivered events are written there instead."),
            leo("[chuckle] Like the post office's pile of letters nobody could deliver. Could a photo arrive twice instead?"),
            maya("Easily. Delivery is at least once: every event gets there, and some get there twice. If the answer was lost coming back, Event Grid cannot tell the work was done, so it sends the event again."),
            leo("So two thumbnails."),
            maya("Only if the second run changes something. Handlers must be idempotent, a long word for safe to run twice: the same event leaves the same result. Ours writes the thumbnail under the blob's name, and upserts the dish's search entry by dish id."),
            leo("And order? Uploaded, then replaced a minute later."),
            maya("Event Grid does not promise order, so the second photo can land first. When order matters you are back on a Service Bus queue. Compare the two, and the exam answers itself."),
            leo("Go on."),
            maya("Service Bus: one message, one worker, in order if you ask, with a lock while the work is done. Event Grid: one event, every matching subscription, each with its own copy and retries."),
            leo("Last worry. All of this is crossing the public internet."),
            maya("The delivery is, which is why the handler checks who is calling. The publishing side you can close: a private endpoint gives the topic an address inside our own network, and public access goes off."),
            leo("Both ends private, then."),
            maya("Not quite. A private endpoint is for events coming in. The push out to your handler does not, so protect the handler itself, with a managed identity on delivery or its own key."),
            leo("Understood. So what do I remember for the exam?"),
            maya("Five. Undelivered events must be kept: a dead-letter blob container. Only one container's files: a subject begins-with filter. A number inside the data: an advanced filter. The handler sees the same event twice: make it idempotent. Azure's own news: a system topic."),
            leo("And one more, for the pair?"),
            maya("If each item must be worked through once, by one worker, that is a message on a Service Bus queue, and not Event Grid at all."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container, and check the subscription's delivery metrics."),
            leo("A photo that announces itself, and never gets lost. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-eventgrid",
        listOf(
            maya("Welcome to Case Files. Today's case: The Dish That Never Appeared. A restaurant uploaded eleven new dish photos on Monday. By Thursday not one of them had a thumbnail in the Zaiqa app, and searching for their paneer tikka found nothing at all."),
            leo("Detective Leo, reporting. Remind me where a Zaiqa dish photo even lives."),
            maya("In Blob Storage, as a file. Orders and payments are in Azure SQL, the dish search data is in Cosmos, and the photos are files, because big files are not database rows. The partner dashboard uploads them into a container called dish photos."),
            leo("And something is meant to notice."),
            maya("The photo function: Zaiqa's small Python function that cuts a thumbnail and writes the dish's search entry so search can find it. It is woken by Azure Event Grid, a notice board that pushes. The moment news goes up, it hands a copy to everyone who asked."),
            leo("First suspect, then. The dashboard forgot to tell Event Grid."),
            maya("[chuckle] Wrong suspect, and a good one. The dashboard tells nobody. The storage account publishes its own Blob Created event, onto a system topic: the board where an Azure service posts news about its own resources."),
            leo("So our code publishes nothing."),
            maya("Not for photos. A custom topic is the other kind, a board our own code posts to with news we invent, and we have none here. The first clue is elsewhere. Look at the subscription."),
            leo("The standing order that says send me these events, at this address."),
            maya("That one. Ours points at the photo function, and it carries filters: rules that decide which events get through. An event type filter, Blob Created only. A subject filter, where the subject is a path, beginning with the dish photos container."),
            leo("And the subjects on Monday's photos?"),
            maya("Perfect. Clue one is the third filter. Somebody had added an advanced filter, which looks inside the event's data and tests a field with an operator. Data dot contentLength, NumberGreaterThan, two million."),
            leo("Two million bytes. So only photos over two megabytes woke the function."),
            maya("And the dashboard had just started shrinking photos before upload. Eleven photos, every one under a megabyte, every one filtered out. That filter had been written for a different week."),
            leo("Case closed, surely."),
            maya("One line of it. Because the same restaurant uploaded four more photos on Tuesday, well over two megabytes each, and those vanished too."),
            leo("Second suspect. Event Grid lost them."),
            maya("It did not. It tried for a day. The handler must answer with a success code, a number like 200 that means OK, within 30 seconds. The function was being redeployed, and answered nothing for thirty-one hours."),
            leo("So it kept knocking."),
            maya("Waiting longer each time: ten seconds, thirty, a minute, on up to hours. By default it gives up after 30 attempts or 24 hours, whichever comes first. You can lower both on the subscription, and a 400, meaning the event itself was bad, is never retried."),
            leo("Then it filed them away somewhere."),
            maya("[pause] That is clue two, and the culprit. Dead-lettering was off, as it is by default, so the events were simply dropped. Turn it on and you point the subscription at a blob container, and undelivered events are written there for you to read and replay."),
            leo("Noted. Anything else in the file?"),
            maya("A third witness. Some dishes ended up with two thumbnails, cropped two different ways."),
            leo("Third suspect. Two restaurants uploaded the same photo."),
            maya("No. Delivery is at least once, by design: every event arrives, and some arrive twice. If the function did the work and its answer was lost coming back, Event Grid cannot tell, so it sends the event again."),
            leo("So the function must not mind."),
            maya("It must be idempotent, which is a long word for safe to run twice: the same event again leaves the same result. Ours now writes the thumbnail under the blob's own name, and upserts the dish's search entry by dish id. A repeat changes nothing."),
            leo("Fourth witness? I can feel one coming."),
            maya("The audit team. They pointed a subscription at a webhook, which is just a web address on their own server, and received nothing at all. Not one event, ever."),
            leo("Their filters, then."),
            maya("Their filters were fine. They never answered the validation handshake. Event Grid will not push to a webhook until the address proves it wants the events, so nobody can aim the service at somebody else's server and flood it."),
            leo("How does it ask?"),
            maya("With CloudEvents it sends an options request carrying a request origin header, and your address answers with an allowed origin header. With the older Event Grid schema, which is the envelope's layout, it sends a validation event, and you echo the code back, or open the link before it expires."),
            leo("And our photo function never had to?"),
            maya("Its Event Grid trigger, the setting that wakes it for each event, answers for it. That is why only the webhook was silent."),
            leo("What is in the envelope, while we are here?"),
            maya("CloudEvents 1.0: an open standard, a layout many companies agreed on. An id, a source saying who sent it, a type, the subject, a time, and the data: the blob's address, its size, its content type."),
            leo("Last loose end. One photo was replaced, and the app kept showing the old one."),
            maya("Event Grid does not promise order, and that is where it stops being the answer. Which brings us to the pair the exam loves: the photo was an event, but Zaiqa's paid orders are messages."),
            leo("Meaning?"),
            maya("A message is a job handed to one worker, with an outcome the sender wants: take this order to the restaurant. That is a Service Bus queue, one worker per job, in order if you ask, and the Service Bus episode has it in full. An event is only news, and everyone who asked gets a copy."),
            leo("And the private side? Our topic sits on the open internet."),
            maya("The publishing side need not. A private endpoint is a door into the topic from inside Zaiqa's own network, so publishing never crosses the open internet. The push out to the handler does not come through it, so the handler guards itself with a key of its own."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Undelivered events must be kept: a dead-letter blob container. Only one container's files: a subject begins-with filter. A number inside the data: an advanced filter. The same event twice: an idempotent handler. Azure's own news, like an upload: a system topic."),
            leo("And the pair?"),
            maya("One worker, each job once, in order: a message on a Service Bus queue. Something happened, and several parts may care: an event on Event Grid."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate it safely. Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            leo("No more dishes that never appear. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-eventgrid",
        listOf(
            maya("Okay, did you hear what happened to the Zaiqa dish photos this week? Sit down. They finally wired the uploads up to Event Grid, and it took the team five days and four apologies."),
            leo("I heard somebody shouting about a handshake. Start at the beginning. What was wrong with the photos?"),
            maya("Nothing was wrong. Nothing happened at all. A restaurant uploads a dish photo from the partner dashboard, it lands in Blob Storage as a file, and there it sat. No thumbnail, and nothing in search."),
            leo("And Event Grid fixes that how?"),
            maya("It is a notice board that pushes. The moment news goes up, it hands a copy to everyone who asked for that kind of news. The news here is simply: a photo was uploaded."),
            leo("Right. So who went first?"),
            maya("Tom. Monday. Tom wrote a publisher: a routine in the Zaiqa API, the service the phone talks to, that sent a photo uploaded event every time the dashboard finished an upload."),
            leo("That sounds reasonable to me."),
            maya("It is reasonable, and it is wasted. The storage account publishes Blob Created itself, to a system topic: the board where an Azure service posts news about its own resources. You subscribe, you do not publish."),
            leo("So when would Tom's routine have been right?"),
            maya("When the news is ours and no Azure service knows it. Something like a restaurant going offline. That is a custom topic, a board our own code posts to. Tom deleted his routine by lunch."),
            leo("[sigh] Poor Tom. Tuesday?"),
            maya("Dana. Dana made the subscription, the standing order that says send me these events, at this address, pointing at the photo function, Zaiqa's Python one that cuts thumbnails."),
            leo("And?"),
            maya("And she left the filters off. A filter is a rule on the subscription deciding which events get through. Without one the function woke for every file in the account, Blob Deleted included."),
            leo("[laugh] Thumbnails of deleted photos."),
            maya("For an hour. Now it has an event type filter, Blob Created only, and a subject filter: the subject is written like a path, so it begins with the dish photos container, and ends with dot jpeg."),
            leo("Ends with. Is that as clever as a subject filter gets?"),
            maya("It is. Begins with, ends with, nothing else. For anything inside the event there is the third kind, an advanced filter: it tests a field with an operator, like data dot contentLength, NumberGreaterThan, ten thousand, to skip the tiny placeholder images."),
            leo("Wednesday. Go on."),
            maya("Marcus. Marcus found dishes with two thumbnails, and opened a support case saying Event Grid was sending duplicates."),
            leo("Was it?"),
            maya("Of course it was. Delivery is at least once, by design: every event arrives, and some arrive twice. If a handler's answer is lost coming back, Event Grid cannot tell the work was done, so it knocks again."),
            leo("And the support case?"),
            maya("Closed in nine minutes, kindly. His handler had to be idempotent, which means safe to run twice: the same event again leaves the same result. It writes the thumbnail under the blob's own name now, and upserts the dish's search entry by dish id."),
            leo("Thursday. The intern, I assume."),
            maya("[chuckle] The intern. Bless them. They wanted the payout run to go through Event Grid: each restaurant paid exactly once, in order, one at a time."),
            leo("Which is wrong, because that is a job, not news."),
            maya("You have been listening. It is the pair the exam keeps asking about. A message is a job handed to one worker with an outcome the sender wants, and that is a Service Bus queue: one worker per job, in order if you ask. The Service Bus episode has the rest."),
            leo("And an event?"),
            maya("News that something happened, sent to everyone who asked, in no promised order. A paid order is a message. A photo upload is an event. The intern now loves queues."),
            leo("And Friday? There is always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everybody is still talking about it. She subscribed the audit team's webhook, which is just a web address on their own server. For a whole day, nothing arrived."),
            leo("Wrong filter."),
            maya("Her filters were perfect. It was the validation handshake. Event Grid will not push to a webhook until that address proves it wants the events, so nobody can aim the service at somebody else's server and flood it."),
            leo("And proving it means what, exactly?"),
            maya("With CloudEvents, Event Grid sends an options request carrying a request origin header, and the address must answer with an allowed origin header. With the older schema, which is the envelope's layout, it sends a validation event, and you echo the code back, or open the link before it expires."),
            leo("And Priya missed the link."),
            maya("By an afternoon. The photo function never had the problem, because its Event Grid trigger, the setting that wakes it, answers for it."),
            leo("What about the envelope all this travels in? I heard an argument about that too."),
            maya("Tom won that one. CloudEvents 1.0: an open standard, meaning a layout many companies agreed on. An id, a source, a type, a subject, a time, and the data inside: the blob's address, its size, its content type. You choose it when you create the topic."),
            leo("And the week's worst?"),
            maya("Saturday, and it was nobody's fault. The function was down for thirty-one hours during a release. Event Grid wants a success code, a number like 200 meaning OK, within 30 seconds, then it retries, waiting longer each time."),
            leo("How long does it keep that up?"),
            maya("30 attempts or 24 hours by default, whichever comes first, and both can be lowered on the subscription. Thirty-one hours was too long, and every one of those photos was dropped."),
            leo("Dropped? Not kept anywhere?"),
            maya("Dead-lettering was off, which is the default, and that is the thing everybody learns exactly once. Point the subscription at a blob container and undelivered events are written there instead, to read and replay. It is on everywhere now."),
            leo("And the thing security asked for?"),
            maya("A private endpoint on the topic, which is a door into it from inside our own network, so the API publishes privately and public access goes off. The push out to a handler does not come through it, mind, so the handler still guards its own door."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Undelivered events must be kept: a dead-letter blob container. Only one container's files: a subject begins-with filter. A number in the data: an advanced filter. The same event twice: an idempotent handler. Azure's own news, like an upload: a system topic."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            leo("Dead letters on, support cases off. Got it, Marcus. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-eventgrid",
        listOf(
            nova("Hey, you. It is late, the kitchen light is still on, and it is just the three of us. Me, Ivy, and you. [pause] Tonight: the dish photo nobody was waiting for."),
            ivy("Nobody was waiting for it. That sounds a little sad, Nova."),
            nova("It is the whole trick. A restaurant uploads a photo of their new paneer tikka from the partner dashboard, and it lands in Blob Storage as a file. Orders are in Azure SQL, the dish search data in Cosmos, and the photo is just a file, sitting there."),
            ivy("And something should notice it."),
            nova("Zaiqa's photo function should: the little Python function that cuts a thumbnail and writes the dish's search entry, so search can find it. [pause] But nobody tells it. Nobody is watching the folder."),
            ivy("So who wakes it up?"),
            nova("Event Grid. Picture a notice board that does not wait for anyone to walk past. The moment a notice goes up, it carries a copy to everyone who asked for that kind of news."),
            ivy("News. Is that different from the orders you push around all evening?"),
            nova("Very. An order is a message: a job handed to one worker, with an outcome somebody is waiting for. Take this paid order to the restaurant. [pause] That belongs on a Service Bus queue."),
            ivy("And a photo?"),
            nova("A photo upload is only news. The publisher does not know who is listening, does not wait, and does not mind what they do. The upload is finished either way."),
            ivy("Mm. And who posts that notice, at this hour?"),
            nova("The storage account itself, onto a system topic: the board where an Azure service puts news about its own resources. Blob Created, every time a file lands. [pause] We write no publishing code at all."),
            ivy("And if the news were ours?"),
            nova("Then a custom topic. A board our own code posts to, with news we invent, like a restaurant going offline. Two kinds of board, and the exam does like to ask which."),
            ivy("Tell me how the function hears about it."),
            nova("A subscription: a standing order that says send me these events, at this address. One topic can carry many of them, and each one gets its own copy."),
            ivy("Does my little function get everything in the account, then?"),
            nova("It would, so a subscription carries filters, rules about what gets through. An event type filter: Blob Created only, not Blob Deleted. [pause] Nobody wants a thumbnail of a photo that was just removed."),
            ivy("And something finer?"),
            nova("A subject filter. Every event has a subject saying what it happened to, written like a path, so you can take the dish photos container only, or anything ending in dot jpeg. Begins with, ends with. That is all it can do."),
            ivy("And if I am fussier than that?"),
            nova("[pause] Then an advanced filter. It looks inside the event's data and tests a field with an operator: data dot contentLength, NumberGreaterThan, ten thousand, so the tiny placeholder images never wake you."),
            ivy("What does one of these letters look like, up close?"),
            nova("Like a letter in a standard envelope, and the envelope's layout is the schema. Prefer CloudEvents 1.0: an open standard, a layout many companies agreed on, so many systems can read it."),
            ivy("And on the front?"),
            nova("An id, a source saying who sent it, a type saying what happened, the subject, a time. [pause] And inside, the data: the blob's address, its size, its content type."),
            ivy("Now the part that keeps me up. What if the function is asleep?"),
            nova("Event Grid is patient with it. It knocks, and wants a success code, a number like 200 meaning OK, within 30 seconds. Otherwise it waits and knocks again, a little later each time."),
            ivy("How much later?"),
            nova("Ten seconds, thirty, a minute, then on up to hours. By default it gives up after 30 attempts or 24 hours, whichever comes first, and you can lower both. A 400 means the event itself was bad, [pause] so knocking again would not help."),
            ivy("And when it finally gives up? It leaves the photo at the door?"),
            nova("[long pause] Not unless you ask. By default it is dropped. Gone. To keep it, you turn on dead-lettering: give the subscription a blob container, and the undelivered events wait there, for you to read and replay."),
            ivy("I like that better. Left at the door until morning."),
            nova("Me too. And a quiet truth: delivery is at least once. Every event arrives, and some arrive twice. If the answer is lost coming back, Event Grid cannot tell the work was done, so it knocks again."),
            ivy("Twice. So what does a careful function do?"),
            nova("It is idempotent, which means safe to run twice: the same event again leaves the same result. Ours writes the thumbnail under the blob's own name, and upserts the dish's search entry by dish id. A repeat changes nothing."),
            ivy("And if the restaurant replaces the photo a minute later?"),
            nova("Then be careful, because Event Grid does not promise order. The second can land first. [pause] When order matters, you are back on a Service Bus queue: one worker, one job at a time. That is your next late night."),
            ivy("And can anyone just put their address on your board?"),
            nova("No, and I rather like this part. A webhook is just a web address on your own server. Before anything arrives, it must prove it wants the events. That is the validation handshake."),
            ivy("A handshake. With whom?"),
            nova("With Event Grid. On CloudEvents it sends an options request with a request origin header, and your address answers with an allowed origin header. On the older schema it sends a validation event, and you echo the code back, or open the link it gives you before it expires."),
            ivy("And your photo function?"),
            nova("Its Event Grid trigger, the setting that wakes it for each event, answers for it. [pause] Which is why a silent webhook is almost always the handshake, and almost never a filter."),
            ivy("One more worry before I sleep. Does all of this go over the open internet?"),
            nova("The publishing side need not. A private endpoint is a door into the topic from inside Zaiqa's own network, so publishing never crosses the open internet. But the push out to a handler does not come through it, so the handler keeps its own guard."),
            ivy("[sigh] Mm. Then before you send me off. What do I remember for the exam?"),
            nova("Five little things. Undelivered events must be kept: a dead-letter blob container. Only one container's files: a subject begins-with filter. A number in the data: an advanced filter. The same event twice: an idempotent handler. And Azure's own news, like our upload: a system topic."),
            ivy("And the pair you keep coming back to?"),
            nova("One worker, each job once, in order: a message on a Service Bus queue. Something happened, and several parts may care: an event on Event Grid."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. [pause] Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            ivy("A photo that knocks on the door itself. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-eventgrid",
        listOf(
            nova("Tonight is date night, darling. Candles, something with chilli in it, and you and me talking about a photograph that announces itself."),
            rafe("A photograph that announces itself. Go on, I am listening."),
            nova("A restaurant uploads a dish photo from the partner dashboard. It lands in Blob Storage, as a file, and nothing happens. No thumbnail in the app, and nothing in search."),
            rafe("And Zaiqa's photo function is sitting there, waiting to be asked."),
            nova("Which is where Event Grid comes in: a notice board that pushes. The moment news goes up, it carries a copy to everyone who asked for that kind of news."),
            rafe("Then quiz me, love, and be strict. First one. We also push every paid order to the order worker. Why is that not Event Grid?"),
            nova("Because an order is a message: a job handed to one worker, with an outcome the sender wants. That is a Service Bus queue. A photo upload is an event: news, sent to everyone who asked, and nobody is waiting on it."),
            rafe("Right answer, and the exam's favourite pair. [kiss]"),
            nova("My turn, then. The photo lands in a storage account. Which kind of topic carries that news, and who publishes it?"),
            rafe("A custom topic. The Zaiqa API, the service the phone talks to, publishes it after the upload."),
            nova("[sigh] No kiss for that one, darling. The storage account publishes Blob Created itself, to a system topic: the board where an Azure service posts news about its own resources. We write nothing. We only subscribe."),
            rafe("And a custom topic is for?"),
            nova("News our own code invents, that no Azure service knows. A restaurant going offline. Two kinds of board, and the exam does like to ask."),
            rafe("Let me earn it back. A subscription is a standing order: send me these events, at this address. Ours points at the photo function, and every subscription gets its own copy."),
            nova("Right answer, and a lovely recovery. [kiss]"),
            rafe("Now filters. Three kinds, I am told, and you always save the best for last."),
            nova("An event type filter: Blob Created only, not Blob Deleted. A subject filter: the subject is written like a path, so it begins with the dish photos container, or ends with dot jpeg."),
            rafe("Begins and ends. That is all it can see?"),
            nova("That is all. So here is yours, exam style. The dashboard keeps uploading tiny placeholder images, and you want the function left alone unless the file is real. Which filter, and how?"),
            rafe("An advanced filter. It tests a field inside the data with an operator: data dot contentLength, NumberGreaterThan, ten thousand. The subject cannot see inside the data."),
            nova("Right answer. Size does matter, to that filter. [kiss]"),
            rafe("Behave. And the envelope it all travels in?"),
            nova("The schema, meaning the envelope's layout, and the one to prefer is CloudEvents 1.0: an open standard many companies agreed on. An id, a source, a type, the subject, a time, and the data: the blob's address, its size, its content type."),
            rafe("A well-addressed envelope. My favourite thing about you."),
            nova("Then my question. The photo function was down for two days during a release. Afterwards, every one of those photos must still be there. What do you set up, and what happens without it?"),
            rafe("Dead-lettering. Point the subscription at a blob container, and undelivered events are written there instead. Without it they are simply dropped, because it is off by default."),
            nova("Right answer. Nothing we say gets lost. [kiss]"),
            rafe("And how long does it try, before it gives up on me?"),
            nova("It wants a success code, a number like 200 meaning OK, within 30 seconds. Then it waits longer each time: ten seconds, thirty, a minute, on up to hours. By default 30 attempts or 24 hours, whichever comes first, and a 400 is never retried."),
            rafe("Persistent, but not forever. I respect that. My question now. The function sometimes gets the same Blob Created event twice. What is the right response?"),
            nova("Make it idempotent, which means safe to run twice: the same event again leaves the same result. Delivery is at least once by design, so a lost answer means another knock. Ours writes the thumbnail by the blob's own name and upserts the dish's search entry by dish id."),
            rafe("Right answer. Twice is never too much, as long as nothing changes. [kiss]"),
            nova("And where does this romance have its limits, Rafe?"),
            rafe("Order. Event Grid does not promise it, so a replaced photo can land before the first one. When order matters, it is a queue again, one worker at a time. Which is the Service Bus episode, darling."),
            nova("Exactly. Now a hard one, darling, and it has caught better people than us. The audit team pointed a subscription at their own webhook, a web address on their server. The filters are right. Not one event has ever arrived. Why?"),
            rafe("They never answered the validation handshake. Event Grid will not push to a webhook until the address proves it wants the events, so nobody can aim it at somebody else's server and flood it."),
            nova("Right answer, and now tell me how it asks."),
            rafe("On CloudEvents, an options request with a request origin header, answered with an allowed origin header. On the older schema, a validation event: echo the code back, or open the link it gives you before it expires."),
            nova("Right answer twice over. Always answer when someone knocks. [kiss]"),
            rafe("And our function never has to, because its Event Grid trigger answers for it. Last question from me, and it is the security one. All of this crosses the public internet. Does it have to?"),
            nova("The publishing side does not. A private endpoint is a door into the topic from inside Zaiqa's own network, so public access on the topic goes off. But the push out to a handler does not come through it, so the handler guards its own door."),
            rafe("Right answer, and nicely hedged. [kiss]"),
            nova("Then the last one, for the exam. What do we remember?"),
            rafe("Undelivered events must be kept: a dead-letter blob container. Only one container's files: a subject begins-with filter. A number in the data: an advanced filter. The same event twice: an idempotent handler. And an upload to storage: a system topic."),
            nova("Five for five, love. And the pair?"),
            rafe("One worker, each job once, in order: a message on a Service Bus queue. Something happened, and several parts may care: an event on Event Grid."),
            nova("And something to try today, darling?"),
            rafe("Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            nova("And you are the only notice I ever want pushed to me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "Blob Storage posts a dish photo upload to a system topic; each subscription pushes its own copy on, and events out of retries go to dead-letter blobs.",
        nodes = listOf(
            DNode("pub", "Blob Storage", 0.17f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "a photo lands"),
            DNode("topic", "System topic", 0.5f, 0.5f, w = 0.28f, h = 0.26f, kind = NodeKind.CLOUD),
            DNode("fn", "Photo function", 0.83f, 0.18f, w = 0.32f, h = 0.2f, sub = "type filter"),
            DNode("q", "Audit queue", 0.83f, 0.5f, w = 0.32f, h = 0.2f, sub = "subject filter"),
            DNode("dl", "Dead-letter blobs", 0.83f, 0.82f, w = 0.32f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
        ),
        edges = listOf(
            DEdge("pub", "topic", "posts"),
            DEdge("topic", "fn", "push"),
            DEdge("topic", "q", "push"),
            DEdge("topic", "dl", "retries out", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun topics() = DiagramSpec(
        "Azure services post their own news to system topics; our code publishes to a custom topic, in the CloudEvents 1.0 schema.",
        nodes = listOf(
            DNode("storage", "Storage account", 0.24f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "a photo lands"),
            DNode("sys", "System topic", 0.76f, 0.18f, w = 0.42f, h = 0.2f, tone = 2, sub = "Blob Created"),
            DNode("api", "Zaiqa API", 0.24f, 0.52f, w = 0.42f, h = 0.2f, tone = 1, sub = "our own code"),
            DNode("cust", "Custom topic", 0.76f, 0.52f, w = 0.42f, h = 0.2f, sub = "news we invent"),
            DNode("ce", "CloudEvents 1.0", 0.5f, 0.85f, w = 0.7f, h = 0.18f, kind = NodeKind.PILL, sub = "type, source, subject, data"),
        ),
        edges = listOf(
            DEdge("storage", "sys", "posts itself"),
            DEdge("api", "cust", "publishes"),
            DEdge("cust", "ce", "schema"),
        ),
        aspect = 0.8f,
    )

    private fun subs() = DiagramSpec(
        "Each subscription on the topic is a standing order, and each handler gets its own copy of the event.",
        nodes = listOf(
            DNode("topic", "System topic", 0.5f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "Blob Created"),
            DNode("fn", "Function", 0.18f, 0.55f, w = 0.3f, h = 0.2f, sub = "thumbnail"),
            DNode("hook", "Webhook", 0.5f, 0.55f, w = 0.3f, h = 0.2f, sub = "a web address"),
            DNode("q", "Queue", 0.82f, 0.55f, w = 0.3f, h = 0.2f, sub = "Service Bus"),
            DNode("note", "One copy each", 0.5f, 0.88f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("topic", "fn", "subscription"),
            DEdge("topic", "hook"),
            DEdge("topic", "q"),
        ),
        aspect = 0.8f,
    )

    private fun filters() = DiagramSpec(
        "A subscription's filters let through only some events: by event type, by how the subject begins or ends, or by a field in the data.",
        nodes = listOf(
            DNode("topic", "System topic", 0.5f, 0.14f, w = 0.4f, h = 0.2f, tone = 1),
            DNode("type", "Event type", 0.18f, 0.5f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "Blob Created"),
            DNode("subj", "Subject", 0.5f, 0.5f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "begins with"),
            DNode("adv", "Advanced", 0.82f, 0.5f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "size over 10 KB"),
            DNode("handler", "Photo function", 0.5f, 0.86f, w = 0.4f, h = 0.18f, tone = 2),
        ),
        edges = listOf(
            DEdge("topic", "type"),
            DEdge("topic", "subj"),
            DEdge("topic", "adv"),
            DEdge("type", "handler"),
            DEdge("subj", "handler"),
            DEdge("adv", "handler"),
        ),
        aspect = 0.8f,
    )

    private fun delivery() = DiagramSpec(
        "No OK within 30 seconds means a retry; out of retries, an event is dead-lettered or dropped. Repeats are normal, so handlers are idempotent.",
        nodes = listOf(
            DNode("topic", "Topic", 0.2f, 0.16f, w = 0.32f, h = 0.2f, tone = 1),
            DNode("handler", "Photo function", 0.75f, 0.16f, w = 0.42f, h = 0.2f, sub = "OK within 30 s"),
            DNode("retry", "Retries", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "30 tries or 24 hours"),
            DNode("dl", "Dead-letter container", 0.75f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "or else dropped"),
            DNode("idem", "Idempotent", 0.24f, 0.62f, w = 0.4f, h = 0.24f, kind = NodeKind.NOTE, sub = "safe to run twice"),
        ),
        edges = listOf(
            DEdge("topic", "handler", "push"),
            DEdge("handler", "retry", "no OK"),
            DEdge("retry", "dl", "out of tries", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where Event Grid stops: jobs for one worker go on a Service Bus queue, order is not promised, and webhooks must pass the handshake.",
        nodes = listOf(
            DNode("jobs", "Jobs, one by one", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "a paid order"),
            DNode("sb", "Service Bus queue", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "another episode"),
            DNode("order", "No order promised", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("hs", "Validation handshake", 0.76f, 0.76f, w = 0.44f, h = 0.2f, tone = 2, sub = "webhooks prove it"),
        ),
        edges = listOf(DEdge("jobs", "sb", "use instead")),
        aspect = 0.8f,
    )

    private fun privately() = DiagramSpec(
        "A private endpoint keeps publishing inside Zaiqa's own network; the push out to a handler does not come through it.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.24f, 0.2f, w = 0.42f, h = 0.2f, tone = 1, sub = "publishes"),
            DNode("pe", "Private endpoint", 0.76f, 0.2f, w = 0.42f, h = 0.2f, sub = "on our network"),
            DNode("topic", "The topic", 0.5f, 0.55f, w = 0.4f, h = 0.2f, tone = 2, sub = "public access off"),
            DNode("handler", "Photo function", 0.5f, 0.88f, w = 0.4f, h = 0.18f, sub = "guards its door"),
        ),
        edges = listOf(
            DEdge("api", "pe", "private"),
            DEdge("pe", "topic", "publish"),
            DEdge("topic", "handler", "push, public", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Event Grid questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("kept", "Keep undelivered", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter container"),
            DNode("branch", "Only one container", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "subject begins-with"),
            DNode("number", "A number in the data", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "advanced filter"),
            DNode("twice", "Same event twice", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "idempotent handler"),
            DNode("azure", "Azure's own news", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "system topic"),
            DNode("jobs", "Jobs, one by one", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Service Bus queue"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("kept", "branch", "number", "twice", "azure", "jobs")
    private val today = BoardCard("Today", "A CloudEvents topic, a filtered subscription, two events, dead letters.")
    private val pairCard = BoardCard("The pair", "One worker per job: a message. Something happened: an event.")
    private val idemCard = BoardCard("Idempotent", "Safe to run twice: the thumbnail by blob name, the entry by dish id.")
    private val deadCard = BoardCard("Dead-lettering", "Off by default, so events are dropped. Point it at a blob container.")
    private val retryCard = BoardCard("Retries", "OK within 30 seconds, or retry: 30 attempts or 24 hours.")
    private val onceCard = BoardCard("At least once", "A lost answer means Event Grid sends the same event again.")
    private val peCard = BoardCard("Private endpoint", "Publishing stays inside the network. The push out does not.")
    private val askCard = BoardCard("How it asks", "An options request and an allowed origin header, or a validation code.")

    private fun classicBoard() = PodcastBoard(
        "ai-eventgrid",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is in good shape..."
            Scene(
                0, "A photo with no voice", flow(),
                beats = listOf(
                    Beat(0, show = setOf("pub"), light = setOf("pub"), card = BoardCard("Today", "The dish photo a restaurant uploads, and how anything hears of it.")),
                    Beat(2, light = setOf("pub"), card = BoardCard("The photo just sits there", "No thumbnail for the app, and the dish never reaches search.")),
                    Beat(4, show = setOf("topic"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that pushes: news goes to everyone who asked for it.")),
                    Beat(6, show = setOf("q"), light = setOf("q"), card = BoardCard("Event or message", "An order is a job for one worker. A photo upload is only news.")),
                    Beat(8, light = setOf("topic"), card = BoardCard("News, not a job", "The publisher does not wait, and does not mind who listens.")),
                ),
            ),
            // 11: "Fine. So who publishes the photo news?"
            Scene(
                11, "Two kinds of board", topics(),
                beats = listOf(
                    Beat(12, show = setOf("storage", "sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "An Azure service posts news about its own resources.")),
                    Beat(14, light = setOf("sys"), card = BoardCard("Blob Created", "Storage posts it every time a file lands. We only subscribe.")),
                    Beat(16, show = setOf("api", "cust"), light = setOf("cust"), pulse = "api" to "cust", card = BoardCard("Custom topic", "A board our own code posts to, with news we invent.")),
                ),
            ),
            // 17: "Right. And signing up?"
            Scene(
                17, "Signing up", subs(),
                beats = listOf(
                    Beat(18, show = setOf("topic", "fn"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Subscription", "A standing order: send me these events, at this address.")),
                    Beat(20, show = setOf("hook", "q"), light = setOf("fn", "hook", "q"), card = BoardCard("Handlers", "A function, a webhook or a queue. Each one gets its own copy.")),
                ),
            ),
            // 21: "Start with the first."
            Scene(
                21, "Three kinds of filter", filters(),
                beats = listOf(
                    Beat(22, show = setOf("topic", "type"), light = setOf("type"), pulse = "topic" to "type", card = BoardCard("Event type filter", "Blob Created only, or you make thumbnails of deleted photos.")),
                    Beat(24, show = setOf("subj"), light = setOf("subj"), pulse = "topic" to "subj", card = BoardCard("Subject filter", "The subject is a path: begins with the dish photos container.")),
                    Beat(26, light = setOf("subj"), card = BoardCard("Begins with, ends with", "Or ends with dot jpeg. That is all a subject filter can do.")),
                    Beat(28, show = setOf("adv"), light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Advanced filter", "contentLength NumberGreaterThan: a field inside the data.")),
                ),
            ),
            // 29: "Good. What does the event itself look like?"
            Scene(
                29, "The envelope", topics(),
                beats = listOf(
                    Beat(30, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "The open standard schema to prefer, chosen when you make the topic.")),
                    Beat(32, light = setOf("ce"), card = BoardCard("On the envelope", "An id, a source, a type, the subject, a time, and the data inside.")),
                ),
            ),
            // 33: "Our photo function is Python..."
            Scene(
                33, "The handshake", limits(),
                beats = listOf(
                    Beat(34, show = setOf("hs"), light = setOf("hs"), card = BoardCard("The Event Grid trigger", "The setting that wakes the function, and answers the handshake.")),
                    Beat(36, light = setOf("hs"), card = BoardCard("Endpoint validation", "A webhook must prove it wants the events before any arrive.")),
                    Beat(38, light = setOf("hs"), card = askCard),
                    Beat(40, light = setOf("hs"), card = BoardCard("A silent webhook", "Nothing at all arriving is the handshake, almost never a filter.")),
                ),
            ),
            // 41: "Now the frightening part..."
            Scene(
                41, "If the handler is down", delivery(),
                beats = listOf(
                    Beat(42, show = setOf("topic", "handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Answer within 30 seconds", "A success code like 200, or Event Grid counts it a failure.")),
                    Beat(44, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = retryCard),
                    Beat(46, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = deadCard),
                    Beat(48, light = setOf("handler"), pulse = "topic" to "handler", card = onceCard),
                    Beat(50, show = setOf("idem"), light = setOf("idem"), card = idemCard),
                ),
            ),
            // 51: "And order? Uploaded, then replaced a minute later."
            Scene(
                51, "Where it stops", limits(),
                beats = listOf(
                    Beat(52, show = setOf("order"), light = setOf("order"), card = BoardCard("No order promised", "The replacement photo can land before the first one.")),
                    Beat(54, show = setOf("jobs", "sb"), light = setOf("sb"), pulse = "jobs" to "sb", card = BoardCard("Service Bus, side by side", "One message, one worker, a lock, and its own dead-letter queue.")),
                ),
            ),
            // 55: "Last worry. All of this is crossing the public internet."
            Scene(
                55, "Keeping it private", privately(),
                beats = listOf(
                    Beat(56, show = setOf("api", "pe", "topic"), light = setOf("pe"), pulse = "api" to "pe", card = peCard),
                    Beat(58, show = setOf("handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("The push out", "Not through the private endpoint, so the handler guards itself.")),
                ),
            ),
            // 59: "Understood. So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Dead letters, filters, idempotent, a system topic: one answer each.")),
                    Beat(62, light = setOf("jobs"), card = pairCard),
                    Beat(64, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-eventgrid--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Dish That Never Appeared..."
            Scene(
                0, "The Dish That Never Appeared", flow(),
                beats = listOf(
                    Beat(0, show = setOf("pub"), light = setOf("pub"), card = BoardCard("The case", "Eleven dish photos uploaded, and not one thumbnail by Thursday.")),
                    Beat(2, light = setOf("pub"), card = BoardCard("Where a photo lives", "Blob Storage, as a file. Orders in SQL, the dish search data in Cosmos.")),
                    Beat(4, show = setOf("topic", "fn"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Event Grid", "A notice board that pushes, waking Zaiqa's photo function.")),
                    Beat(6, light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("System topic", "Storage publishes Blob Created itself. The dashboard tells nobody.")),
                ),
            ),
            // 8: "Not for photos. A custom topic is the other kind..."
            Scene(
                8, "Clue one: the third filter", filters(),
                beats = listOf(
                    Beat(9, show = setOf("topic", "type", "subj"), light = setOf("type", "subj"), pulse = "topic" to "subj", card = BoardCard("The subscription", "Blob Created only, and a subject beginning with dish photos.")),
                    Beat(12, show = setOf("adv"), light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Clue one", "An advanced filter: contentLength NumberGreaterThan two million.")),
                    Beat(14, light = setOf("adv"), card = BoardCard("Written for another week", "The dashboard had started shrinking photos before upload.")),
                ),
            ),
            // 16: "One line of it. Because the same restaurant uploaded four more..."
            Scene(
                16, "Nobody home", delivery(),
                beats = listOf(
                    Beat(16, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Four more vanished", "Tuesday's photos were well over two megabytes, and went too.")),
                    Beat(18, light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Thirty-one hours", "Redeployed, and answering nothing. Event Grid tried for a day.")),
                    Beat(20, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = retryCard),
                    Beat(22, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("The culprit", "No dead-letter container, so the events were simply dropped.")),
                ),
            ),
            // 24: "A third witness. Some dishes ended up with two thumbnails..."
            Scene(
                24, "The third witness", delivery(),
                beats = listOf(
                    Beat(24, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Two thumbnails", "Some dishes came out cropped two different ways.")),
                    Beat(26, light = setOf("topic"), pulse = "topic" to "handler", card = onceCard),
                    Beat(28, show = setOf("idem"), light = setOf("idem"), card = idemCard),
                ),
            ),
            // 29: "Fourth witness? I can feel one coming."
            Scene(
                29, "The fourth witness", limits(),
                beats = listOf(
                    Beat(30, show = setOf("hs"), light = setOf("hs"), card = BoardCard("Nothing arrived", "The audit team's webhook received no events at all, ever.")),
                    Beat(32, light = setOf("hs"), card = BoardCard("Endpoint validation", "A webhook must prove it wants the events before any arrive.")),
                    Beat(34, light = setOf("hs"), card = askCard),
                    Beat(36, light = setOf("hs"), card = BoardCard("The trigger answers", "The function's Event Grid trigger does the handshake for it.")),
                ),
            ),
            // 37: "What is in the envelope, while we are here?"
            Scene(
                37, "The envelope", topics(),
                beats = listOf(
                    Beat(38, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "An id, a source, a type, the subject, a time, and the data inside.")),
                ),
            ),
            // 39: "Last loose end. One photo was replaced..."
            Scene(
                39, "Loose ends", limits(),
                beats = listOf(
                    Beat(40, show = setOf("order"), light = setOf("order"), card = BoardCard("No order promised", "The replacement landed first, so the app kept the old photo.")),
                    Beat(42, show = setOf("jobs", "sb"), light = setOf("sb"), pulse = "jobs" to "sb", card = pairCard),
                ),
            ),
            // 43: "And the private side? Our topic sits on the open internet."
            Scene(
                43, "Keeping it private", privately(),
                beats = listOf(
                    Beat(44, show = setOf("api", "pe", "topic", "handler"), light = setOf("pe"), pulse = "api" to "pe", card = peCard),
                ),
            ),
            // 45: "So, the case summary for the exam?"
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Dead letters, two filters, idempotent, and a system topic.")),
                    Beat(48, light = setOf("jobs"), card = pairCard),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-eventgrid--gossip",
        listOf(
            // 0: "Okay, did you hear what happened to the Zaiqa dish photos this week?"
            Scene(
                0, "The photos that sat there", flow(),
                beats = listOf(
                    Beat(0, show = setOf("pub"), light = setOf("pub"), card = BoardCard("The week", "Five days and four apologies to wire the uploads up.")),
                    Beat(2, light = setOf("pub"), card = BoardCard("Nothing happened", "The photo landed in Blob Storage, and there it sat.")),
                    Beat(4, show = setOf("topic"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that pushes a copy to everyone who asked.")),
                ),
            ),
            // 5: "Right. So who went first?"
            Scene(
                5, "Monday: Tom", topics(),
                beats = listOf(
                    Beat(6, show = setOf("api", "cust"), light = setOf("api"), pulse = "api" to "cust", card = BoardCard("Tom's publisher", "A routine in the Zaiqa API to announce every upload itself.")),
                    Beat(8, show = setOf("storage", "sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "Storage posts Blob Created itself. You subscribe, you do not publish.")),
                    Beat(10, light = setOf("cust"), card = BoardCard("When Tom would be right", "News no Azure service knows, like a restaurant going offline.")),
                ),
            ),
            // 11: "Poor Tom. Tuesday?"
            Scene(
                11, "Tuesday: Dana", filters(),
                beats = listOf(
                    Beat(12, show = setOf("topic"), light = setOf("topic"), card = BoardCard("The subscription", "A standing order pointing at the photo function.")),
                    Beat(14, show = setOf("handler"), light = setOf("handler"), card = BoardCard("No filters at all", "It woke for every file in the account, Blob Deleted included.")),
                    Beat(16, show = setOf("type", "subj"), light = setOf("type", "subj"), pulse = "topic" to "subj", card = BoardCard("Type and subject", "Blob Created only; a subject beginning with dish photos.")),
                    Beat(18, show = setOf("adv"), light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Advanced filter", "contentLength NumberGreaterThan: skip the tiny placeholders.")),
                ),
            ),
            // 19: "Wednesday. Go on."
            Scene(
                19, "Wednesday: Marcus", delivery(),
                beats = listOf(
                    Beat(20, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Two thumbnails", "Marcus opened a support case about duplicate events.")),
                    Beat(22, light = setOf("topic"), pulse = "topic" to "handler", card = onceCard),
                    Beat(24, show = setOf("idem"), light = setOf("idem"), card = idemCard),
                ),
            ),
            // 25: "Thursday. The intern, I assume."
            Scene(
                25, "Thursday: the intern", limits(),
                beats = listOf(
                    Beat(26, show = setOf("jobs"), light = setOf("jobs"), card = BoardCard("The payout run", "Each restaurant paid once, in order, one at a time.")),
                    Beat(28, show = setOf("sb"), light = setOf("sb"), pulse = "jobs" to "sb", card = BoardCard("That is a queue", "A job for one worker is a message on a Service Bus queue.")),
                    Beat(30, light = setOf("order"), card = BoardCard("And an event", "News to everyone who asked, in no promised order.")),
                ),
            ),
            // 31: "And Friday? There is always a Friday."
            Scene(
                31, "Friday: Priya", limits(),
                beats = listOf(
                    Beat(32, show = setOf("hs"), light = setOf("hs"), card = BoardCard("Nothing arrived", "A whole day, and the audit webhook received not one event.")),
                    Beat(34, light = setOf("hs"), card = BoardCard("The handshake", "Event Grid will not push until the address proves it wants them.")),
                    Beat(36, light = setOf("hs"), card = askCard),
                    Beat(38, light = setOf("hs"), card = BoardCard("The trigger answers", "The function's Event Grid trigger does the handshake for it.")),
                ),
            ),
            // 39: "What about the envelope all this travels in?"
            Scene(
                39, "The envelope", topics(),
                beats = listOf(
                    Beat(40, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "An id, a source, a type, the subject, a time, and the data inside.")),
                ),
            ),
            // 41: "And the week's worst?"
            Scene(
                41, "Saturday: thirty-one hours", delivery(),
                beats = listOf(
                    Beat(42, show = setOf("topic", "handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Down for a day and a half", "A release, and nothing answering within 30 seconds.")),
                    Beat(44, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = retryCard),
                    Beat(46, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = deadCard),
                ),
            ),
            // 47: "And the thing security asked for?"
            Scene(
                47, "What security asked for", privately(),
                beats = listOf(
                    Beat(48, show = setOf("api", "pe", "topic", "handler"), light = setOf("pe"), pulse = "api" to "pe", card = peCard),
                ),
            ),
            // 49: "So, the gossip I should remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "One colleague, one lesson, one answer each.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-eventgrid--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen light is still on..."
            Scene(
                0, "A photo nobody heard", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The dish photo nobody was waiting for.")),
                    Beat(2, show = setOf("pub"), light = setOf("pub"), card = BoardCard("A file, sitting there", "The photo lands in Blob Storage, and nobody is watching.")),
                    Beat(4, show = setOf("fn"), light = setOf("fn"), card = BoardCard("The photo function", "It cuts a thumbnail and writes the dish's search entry for search.")),
                    Beat(6, show = setOf("topic"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that carries a copy to everyone who asked.")),
                ),
            ),
            // 7: "News. Is that different from the orders you push around?"
            Scene(
                7, "News, not a job", limits(),
                beats = listOf(
                    Beat(8, show = setOf("jobs", "sb"), light = setOf("sb"), pulse = "jobs" to "sb", card = BoardCard("A message", "A job for one worker, with an outcome somebody waits for.")),
                    Beat(10, light = setOf("order"), card = BoardCard("An event", "Only news. The publisher does not wait, and does not mind.")),
                ),
            ),
            // 11: "Mm. And who posts that notice, at this hour?"
            Scene(
                11, "Two kinds of board", topics(),
                beats = listOf(
                    Beat(12, show = setOf("storage", "sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "Storage posts Blob Created itself. We write no publishing code.")),
                    Beat(14, show = setOf("api", "cust"), light = setOf("cust"), pulse = "api" to "cust", card = BoardCard("Custom topic", "A board our own code posts to, with news we invent.")),
                ),
            ),
            // 15: "Tell me how the function hears about it."
            Scene(
                15, "Who is listening", subs(),
                beats = listOf(
                    Beat(16, show = setOf("topic", "fn"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Subscription", "A standing order: send me these events, at this address.")),
                ),
            ),
            // 17: "Does my little function get everything in the account?"
            Scene(
                17, "Choosing your letters", filters(),
                beats = listOf(
                    Beat(18, show = setOf("topic", "type"), light = setOf("type"), pulse = "topic" to "type", card = BoardCard("Event type filter", "Blob Created only, not Blob Deleted.")),
                    Beat(20, show = setOf("subj"), light = setOf("subj"), pulse = "topic" to "subj", card = BoardCard("Subject filter", "A path: the dish photos container, or anything ending in jpeg.")),
                    Beat(22, show = setOf("adv"), light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Advanced filter", "A field inside the data: contentLength NumberGreaterThan.")),
                ),
            ),
            // 23: "What does one of these letters look like, up close?"
            Scene(
                23, "The envelope", topics(),
                beats = listOf(
                    Beat(24, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "The open standard layout many companies agreed on.")),
                    Beat(26, light = setOf("ce"), card = BoardCard("On the envelope", "An id, a source, a type, the subject, a time, and the data inside.")),
                ),
            ),
            // 27: "Now the part that keeps me up. What if the function is asleep?"
            Scene(
                27, "If the handler sleeps", delivery(),
                beats = listOf(
                    Beat(28, show = setOf("topic", "handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("A knock at the door", "A success code like 200, within 30 seconds, or it is a failure.")),
                    Beat(30, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = retryCard),
                    Beat(32, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = deadCard),
                    Beat(34, light = setOf("handler"), card = onceCard),
                    Beat(36, show = setOf("idem"), light = setOf("idem"), card = idemCard),
                ),
            ),
            // 37: "And if the restaurant replaces the photo a minute later?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, show = setOf("jobs", "sb"), light = setOf("order"), pulse = "jobs" to "sb", card = BoardCard("No order promised", "The second photo can land first. When order matters, a queue.")),
                    Beat(40, light = setOf("hs"), card = BoardCard("The handshake", "A webhook must prove it wants the events before any arrive.")),
                    Beat(42, light = setOf("hs"), card = askCard),
                    Beat(44, light = setOf("hs"), card = BoardCard("The trigger answers", "A silent webhook is the handshake, almost never a filter.")),
                ),
            ),
            // 45: "One more worry before I sleep. The open internet?"
            Scene(
                45, "Keeping it private", privately(),
                beats = listOf(
                    Beat(46, show = setOf("api", "pe", "topic", "handler"), light = setOf("pe"), pulse = "api" to "pe", card = peCard),
                ),
            ),
            // 47: "Then before you send me off. What do I remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Five little things, and one answer each.")),
                    Beat(50, light = setOf("jobs"), card = pairCard),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-eventgrid--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "A photograph that announces itself.")),
                    Beat(2, show = setOf("pub"), light = setOf("pub"), card = BoardCard("The dish photo", "It lands in Blob Storage, and nothing happens.")),
                    Beat(4, show = setOf("topic"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that carries a copy to everyone who asked.")),
                ),
            ),
            // 5: "Then quiz me, love, and be strict..."
            Scene(
                5, "Event or message", limits(),
                beats = listOf(
                    Beat(5, show = setOf("jobs"), light = setOf("jobs"), card = BoardCard("Quiz: why not Event Grid?", "Every paid order goes to the order worker. Why not this way?")),
                    Beat(6, show = setOf("sb"), light = setOf("sb"), pulse = "jobs" to "sb", card = BoardCard("Right answer", "An order is a message for one worker: a Service Bus queue.")),
                    Beat(7, light = setOf("order"), card = BoardCard("News, not a job", "A photo upload is an event, and nobody is waiting on it.")),
                ),
            ),
            // 8: "My turn, then. The photo lands in a storage account."
            Scene(
                8, "Which kind of topic", topics(),
                beats = listOf(
                    Beat(8, show = setOf("storage"), light = setOf("storage"), card = BoardCard("Quiz: a photo upload", "Which kind of topic carries it, and who publishes it?")),
                    Beat(10, show = setOf("sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("No kiss: a system topic", "Storage publishes Blob Created itself. We only subscribe.")),
                    Beat(12, show = setOf("api", "cust"), light = setOf("cust"), pulse = "api" to "cust", card = BoardCard("Custom topic", "News our own code invents, that no Azure service knows.")),
                ),
            ),
            // 13: "Let me earn it back. A subscription is a standing order..."
            Scene(
                13, "Who is listening", subs(),
                beats = listOf(
                    Beat(13, show = setOf("topic", "fn"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Right answer", "A subscription: send me these events, at this address.")),
                ),
            ),
            // 15: "Now filters. Three kinds, I am told..."
            Scene(
                15, "Who gets in", filters(),
                beats = listOf(
                    Beat(16, show = setOf("topic", "type", "subj"), light = setOf("type", "subj"), pulse = "topic" to "subj", card = BoardCard("Type and subject", "Blob Created only; a subject beginning with dish photos.")),
                    Beat(18, show = setOf("adv"), light = setOf("adv"), card = BoardCard("Quiz: the placeholders", "Leave the function alone unless the file is real. Which filter?")),
                    Beat(19, light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Right answer", "An advanced filter: contentLength NumberGreaterThan ten thousand.")),
                ),
            ),
            // 21: "Behave. And the envelope it all travels in?"
            Scene(
                21, "A well-addressed envelope", topics(),
                beats = listOf(
                    Beat(22, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "An id, a source, a type, the subject, a time, and the data inside.")),
                ),
            ),
            // 24: "Then my question. The photo function was down for two days..."
            Scene(
                24, "Two days down", delivery(),
                beats = listOf(
                    Beat(24, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Quiz: down for two days", "Afterwards the photos must still be there. What do you set up?")),
                    Beat(25, show = setOf("retry", "dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("Right answer", "Dead-lettering to a blob container. Otherwise they are dropped.")),
                    Beat(28, light = setOf("retry"), card = retryCard),
                    Beat(29, light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Quiz: the same event twice", "The function gets the same Blob Created twice. The response?")),
                    Beat(30, show = setOf("idem"), light = setOf("idem"), card = idemCard),
                ),
            ),
            // 32: "And where does this romance have its limits, Rafe?"
            Scene(
                32, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(33, show = setOf("order"), light = setOf("order"), card = BoardCard("No order promised", "A replaced photo can land before the first one.")),
                    Beat(34, show = setOf("hs"), light = setOf("hs"), card = BoardCard("Quiz: a silent webhook", "The filters are right, and not one event has ever arrived. Why?")),
                    Beat(35, light = setOf("hs"), card = BoardCard("Right answer", "It never answered the validation handshake.")),
                    Beat(37, light = setOf("hs"), card = askCard),
                ),
            ),
            // 39: "And our function never has to... the security one."
            Scene(
                39, "Keeping it private", privately(),
                beats = listOf(
                    Beat(40, show = setOf("api", "pe", "topic", "handler"), light = setOf("pe"), pulse = "api" to "pe", card = peCard),
                ),
            ),
            // 42: "Then the last one, for the exam. What do we remember?"
            Scene(
                42, "For the exam", exam(),
                beats = listOf(
                    Beat(43, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Five for five, and one answer each.")),
                    Beat(45, light = setOf("jobs"), card = pairCard),
                    Beat(47, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
