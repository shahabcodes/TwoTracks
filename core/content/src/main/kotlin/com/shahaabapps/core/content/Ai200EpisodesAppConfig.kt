package com.shahaabapps.core.content

/** App Configuration: settings and feature flags in one place: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAppConfig {
    private val classic = Podcast(
        "ai-appconfig",
        listOf(
            maya("Hi, and welcome back. Today is about settings. A setting is a small value your app reads to decide how to behave: how long an answer may be, or whether a new button shows. The question is where settings should live, so you can change them without rebuilding anything."),
            leo("Mine live in a file next to the code. Is that so bad?"),
            maya("For one copy of one app, it's fine. But say you're building the Two Tracks Coach, a study assistant inside an Android app. The phone talks to a small Python program running on Azure, Microsoft's cloud, where you rent computers by the hour."),
            leo("And the settings file sits next to that program."),
            maya("Right. We call that program the Coach API: the part your Android app calls, like the kitchen behind the counter. On a busy evening, Azure runs three copies of it side by side, to share the work."),
            leo("And now I want shorter answers. So I edit the file..."),
            maya("Then build again, and restart all three copies. Miss one, and one kitchen cooks to the old recipe."),
            leo("[sigh] I have done exactly that. So where should settings live?"),
            maya("In Azure App Configuration. Picture a noticeboard in the staff room. Every copy of the API reads the same board, so you change a value once, and they all follow. The board itself is called a store: you create one App Configuration store for the Coach."),
            leo("What's written on the board?"),
            maya("Keys, each with a value. A key is a setting's name. For example, Coach MaxTokens, the longest answer the Coach may give. Names often start with the app's name and a colon, so they group together like files in a folder."),
            leo("And the value is the number itself?"),
            maya("Yes. The value is what the setting is set to, say 800. It's stored as text, and your code turns it into a number when it reads it."),
            leo("But I want long answers while I'm testing, and short ones for real students. Two boards?"),
            maya("One board, with labels. A label is a sticky tag on a key, so one key can hold a different value for each place the app runs. Dev, short for development, is your testing copy. Prod, short for production, is the real one students use."),
            leo("So MaxTokens could be 2,000 with the dev label and 800 with the prod label."),
            maya("Exactly. And a key can have no label at all, which makes it the shared default. The app loads the no-label keys first, then its own label on top. Where a key appears in both, the later one wins."),
            leo("Okay. Can the password for the AI model go on the board too?"),
            maya("No, and that's the first big rule. App Configuration is for settings that are not secrets. A secret is anything that unlocks something, like a password. Secrets live in Azure Key Vault, which is a locked safe built for exactly that."),
            leo("Then how does the Coach find its secret, if it's not on the board?"),
            maya("The board holds a Key Vault reference. Picture a note on the board that says: the password is in the safe, in the drawer called OpenAiKey. The note is not the secret. It's only the secret's address."),
            leo("And who opens the safe?"),
            maya("The Coach API does, with its own managed identity. That's like an employee badge Azure gives your app, so it can sign in without any password in the code. And the badge needs two permissions: one to read the board, and one to read secrets from the safe."),
            leo("What are those permissions called?"),
            maya("Roles. A role is a named set of permissions, like a door code that opens some rooms and not others. To read the board, the role is App Configuration Data Reader. To read the safe, Key Vault Secrets User. Forget the safe's role, and every plain setting loads but the secret is refused."),
            leo("And if I want to change values, not just read them?"),
            maya("That's App Configuration Data Owner. It can read and write, and take snapshots, which we'll get to. Your app only reads, so it gets Data Reader. You, the person editing values, get Data Owner."),
            leo("How does the Python code actually read the board?"),
            maya("With the configuration provider. That's a library, a ready-made package of Python code, from Microsoft. You call its load function once, when the API starts."),
            leo("Like loading my Android app's preferences once, at launch?"),
            maya("Very like it. It fetches the keys and labels you ask for, follows the Key Vault references, and hands back something that works like a dictionary: ask for MaxTokens, get 800."),
            leo("So I change MaxTokens on the board. When does the running API notice?"),
            maya("Only when it checks. Nothing pushes the change to your API. The provider has a refresh function, and you call it at the start of every request."),
            leo("Every request? So every time the phone asks something, my API calls Azure too?"),
            maya("No. Refresh quietly does nothing until a set time has passed, thirty seconds by default. Only then does it check the store. Never call refresh, and changes never arrive."),
            leo("Does it check every key, every time? That sounds like a lot."),
            maya("It would be. So you pick one key to watch: a sentinel key. A sentinel is a guard on watch. The API watches only that one key, and when its value changes, the API reloads everything. Say the sentinel is 12; you set it to 13, and that's the signal."),
            leo("So I change three settings, then the sentinel last."),
            maya("Last, yes. Then every copy reloads once, with all three new values in place, and never sees half a change. No restart needed. That's what people mean by dynamic refresh."),
            leo("[pause] Okay. What about switching a whole feature on and off?"),
            maya("That's a feature flag: an on and off switch stored on the board. Picture a small app with a new voice answers button. You ship the code with the button hidden behind a flag called VoiceAnswers. Flip the flag, and the button appears, with no new release."),
            leo("Can it be on for some people only?"),
            maya("Yes, with a filter: a rule attached to the flag. On for ten percent of users, on during a time window, or on for named users and groups."),
            leo("And how does my code ask whether the button is on?"),
            maya("Through a second Python library, called feature management. It reads the flags and answers one question: is this on for this user?"),
            leo("And snapshots? You promised."),
            maya("A snapshot is a photo of the board: a named copy of chosen settings that can never be changed once it's taken. Before each release, the team takes one, say release 1.4."),
            leo("And if someone makes a bad edit on the board the next day?"),
            maya("The release still loads its snapshot by name, untouched. Going back means loading an older one."),
            leo("Got it. What goes wrong most often?"),
            maya("Four things. Putting the password on the board as plain text, instead of a Key Vault reference. And watching every key, instead of one sentinel."),
            leo("And the other two?"),
            maya("Never calling refresh. And giving the app Data Reader on the board but no role on the safe, so every Key Vault reference fails."),
            leo("So what do I remember for the exam?"),
            maya("If the question says reload when one key changes, the answer is a sentinel key. If changed values never appear, call refresh. If a release must use exactly the tested settings, a snapshot. If a feature should reach ten percent of users without new code, a feature flag with a filter. And if the app only reads settings, Data Reader."),
            leo("And anything to try today?"),
            maya("About fifteen minutes, on the free tier, the no-cost price plan. Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            leo("One noticeboard, one guard on watch. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-appconfig",
        listOf(
            maya("Welcome to Case Files. Today's case: The Setting That Wouldn't Change. FlashBack is a small Android flashcard app. Behind it runs a Python program on Azure, Microsoft's cloud, that writes a hint for each card. We call that program the backend: the kitchen behind the counter. On Monday the team made hints shorter. On Tuesday, hints were as long as ever."),
            leo("Detective Leo, on the case. First question. Where did they make the hints shorter?"),
            maya("In Azure App Configuration. Picture a noticeboard in the staff room, for settings. A setting is a small value the app reads to decide how to behave, like how long a hint may be. Every copy of the backend reads the same board, so you change a value once and they all follow."),
            leo("Every copy? There's more than one kitchen?"),
            maya("On a busy evening, Azure runs three copies of the backend side by side, to share the work. With a settings file, you'd edit it and restart all three. With the board, nothing needs a restart. That's the promise, anyway."),
            leo("Under the picture, what is this board, really?"),
            maya("A service Azure runs for you, called a store. It's a small database of names and values, with its own web address. The backend signs in to it and asks for the settings it wants. There's no server of your own to look after."),
            leo("Start with the names."),
            maya("Each name is a key. Here it's FlashBack HintLength, the longest a hint may be. Names often start with the app's name and a colon, so related ones group together, like files in a folder."),
            leo("And the number beside it?"),
            maya("That's the value: what the setting is set to. On Monday they changed HintLength from 300 to 100. And the hints didn't budge."),
            leo("My first suspect: nobody restarted the backend. Restart it, case closed."),
            maya("[pause] A restart would work. But it throws away the whole point of the board. Clue one is in the code. The backend reads the board with the configuration provider, a ready-made Python library from Microsoft."),
            leo("And it reads the board when?"),
            maya("Once, at start-up. It calls load, and gets the settings back like a dictionary."),
            leo("And after start-up?"),
            maya("That's the clue. The provider also has a refresh function, and FlashBack never calls it. The board never pushes a change to your backend; your code has to ask."),
            leo("Ask how often? On every request?"),
            maya("Call refresh at the start of each request. It does nothing until a set time has passed, thirty seconds by default, then checks the store. Never call it, and changes never arrive."),
            leo("Suspect two, then. Call refresh, and set the wait to one second, so changes land fast."),
            maya("Wrong suspect again. Checking more often just adds load. And here's how it really works. Checking every key each time would cost a request per key. So you watch one key only: a sentinel key. A sentinel is a guard on watch."),
            leo("One guard for the whole board?"),
            maya("One. When refresh runs, it asks the store a single question: has the sentinel changed? If not, nothing happens. If it has, the provider reloads every key in one go. So to publish a change, you edit the values, then bump the sentinel, say from 12 to 13."),
            leo("Then Wednesday must have been fine."),
            maya("Wednesday brought clue two. They changed the hint length and the hint tone, but bumped the sentinel first. Some copies reloaded between the two edits. For an hour, students got short hints in the old tone."),
            leo("So the sentinel goes last."),
            maya("Always last. Then every copy reloads once, with every new value in place, and never sees half a change. That's dynamic refresh."),
            leo("Thursday, then. What broke on Thursday?"),
            maya("Security. Someone had put the password for the AI model on the board, in plain text. A secret is anything that unlocks something, and the board is only for settings that aren't secret."),
            leo("So where should the password live?"),
            maya("In Azure Key Vault, a locked safe built for secrets. The board keeps a Key Vault reference instead: a note that says, the password is in the safe, in the drawer called OpenAiKey. It's the secret's address, not the secret. The provider follows the address and fetches it."),
            leo("And let me guess. Access denied."),
            maya("Access denied, on that one key, while every plain setting loaded fine. Who opens the safe? The backend, with its managed identity. Think of an employee badge Azure gives your app, so it signs in without a password in the code."),
            leo("Suspect three: the badge needs App Configuration Data Owner. More power, more access."),
            maya("[sigh] No. A role is a named set of permissions, like a door code. Data Owner lets you change the board, which the backend never does; it only needs Data Reader."),
            leo("Then which door code was missing?"),
            maya("The one on the safe: Key Vault Secrets User. The board never opens the safe. The app does, with its own badge."),
            leo("Friday, then. I'm almost afraid to ask."),
            maya("Friday was calm, thanks to two good tools. First, a feature flag: an on and off switch stored on the board. The new spoken hints shipped hidden behind a flag, with a filter, a rule that turns it on for only ten percent of users."),
            leo("And the second?"),
            maya("A snapshot. A photo of chosen settings, with a name, that can never be changed once it's taken. Before Friday's release they took one, and the release loads it by name. If someone edits the board later, the release still gets exactly what was tested."),
            leo("I saw labels on the evidence board too. Dev and prod?"),
            maya("Good eye. A label is a tag on a key, so one key holds a value per place the app runs. Dev, for development, is the testing copy; prod, for production, is the real app. FlashBack loads the no-label keys first, then its own label, and the later one wins."),
            leo("Any loose ends in the file?"),
            maya("One: where the board stops working. It's for settings, never secrets. And the phone never reads it; it asks the backend, which does."),
            leo("Why not let the Android app read the board directly? One less hop."),
            maya("Because then every phone would need a way to sign in to your store. Keep it behind the backend. And a backend with no requests calls no refresh, so it sees changes on its next request. The Key Vault episode covers the safe."),
            leo("So, the case summary for the exam?"),
            maya("If changed values never appear, call refresh. If the question says reload when one key changes, a sentinel key. If a Key Vault reference fails but plain keys load, give the app Key Vault Secrets User. If a release must use exactly the tested settings, a snapshot. And for ten percent of users without new code, a feature flag with a filter."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, without the long hints. Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            leo("Short hints, long memory. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-appconfig",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Grab a coffee. They moved every setting of the Two Tracks Coach onto Azure App Configuration. The Coach is the study assistant in the Android app, with a small Python program on Azure, Microsoft's cloud, doing the work behind it. Let's just say, it was a week."),
            leo("[laugh] I heard a name came up in the security meeting. But first, what's App Configuration?"),
            maya("A shared noticeboard for settings. A setting is a small value the app reads to decide how to behave, like how long an answer may be. Azure runs several copies of the Coach's Python program, and every copy reads the same board. Change a value once, and they all follow, with no restart."),
            leo("Under the picture, what is it?"),
            maya("A service Azure runs for you, called a store: a small database of names and values, with its own web address. The Python program signs in and asks for what it wants. Each name is a key, like Coach MaxTokens, the longest answer allowed."),
            leo("And the number next to it?"),
            maya("The value. 800, say, stored as text, and the code turns it into a number. Right. Monday. Tom."),
            leo("What did Tom do?"),
            maya("Tom put the password for the AI model on the board. As a plain value. For anyone who can read the board to see."),
            leo("[sigh] Oh, Tom. Where should it have gone?"),
            maya("Azure Key Vault, a locked safe built for secrets. A secret is anything that unlocks something. The board gets a Key Vault reference instead: a note that says, the password is in the safe, drawer OpenAiKey. The address, never the secret."),
            leo("And who opens the safe?"),
            maya("The Coach's own program, with its managed identity. That's an employee badge Azure gives your app, so it signs in with no password in the code. The board never opens the safe. The app follows the address itself."),
            leo("Tuesday?"),
            maya("Tuesday was the intern. Bless them. They gave the badge a role on the board, and nothing on the safe. A role is a named set of permissions, like a door code. Every plain setting loaded, and the secret came back access denied."),
            leo("So the badge needs two door codes."),
            maya("Two. App Configuration Data Reader to read the board. Key Vault Secrets User to read secrets from the safe. And not Data Owner for the app: that one can change values and take snapshots, and it's for the people who edit the board."),
            leo("Wednesday. Go."),
            maya("Marcus. Marcus loaded the settings with the configuration provider, Microsoft's ready-made Python library. It loads once at start-up and hands back something like a dictionary. Lovely. Then he changed the answer length, and nothing happened. All afternoon."),
            leo("Let me guess. A restart fixed it."),
            maya("It did, and that's the bad news. Marcus never called refresh. The provider doesn't check in the background, and the board never pushes changes to it."),
            leo("So who's supposed to ask?"),
            maya("Your code. Call refresh at the start of each request; once a set time has passed, thirty seconds by default, it checks the store. No refresh call, no changes, ever."),
            leo("[chuckle] Poor Marcus. Thursday?"),
            maya("Dana. Dana fixed refresh by telling it to watch every key. All two hundred of them. So every check was two hundred little requests, from every copy."),
            leo("What should Dana have watched?"),
            maya("One sentinel key. A sentinel is a guard on watch. Refresh asks one question: has the sentinel changed? If not, nothing. If yes, it reloads everything at once."),
            leo("So Dana edits her values, then pokes the guard?"),
            maya("Last, say from 12 to 13. Bump it first, and a copy might reload halfway through her edits."),
            leo("And Friday? There's always a Friday."),
            maya("Friday was Priya, and Priya saved the week. A bad edit landed on the board at four in the afternoon, an hour before a release. But Priya had taken a snapshot that morning."),
            leo("A snapshot of the board?"),
            maya("Exactly that. A named copy of chosen settings that can never be changed once it's taken. The release loads its snapshot by name, release 1.4, so it got exactly the tested settings. Going back just means loading an older snapshot."),
            leo("Anything else in the office this week?"),
            maya("The new voice answers. Priya shipped them behind a feature flag, an on and off switch stored on the board, with a filter that turns it on for ten percent of users. A second library, feature management, asks whether it's on for this user. No new release to switch it."),
            leo("And labels? I heard dev and prod in the kitchen."),
            maya("A label is a tag on a key, so one key can hold a value per place the app runs. Dev, for development, is the team's testing copy; prod, for production, is what students use. The Coach loads the no-label keys first, then its own label, and the later one wins."),
            leo("Did anything still go wrong?"),
            maya("Only where the board stops working. It's for settings, never secrets. And the phone app never reads it; it asks the Coach's program, which does."),
            leo("And a copy that nobody's calling?"),
            maya("Refresh only runs when requests come in, so a quiet copy sees changes on its next request. The Key Vault episode has the rest of the safe's story."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the question says a secret is on the board, the answer is a Key Vault reference. If a reference fails while plain keys load, Key Vault Secrets User. If values never arrive, call refresh. If it says reload when one key changes, a sentinel key. And if a release needs exactly the tested settings, a snapshot."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            leo("One sentinel, and no Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-appconfig",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about settings, and where they should sleep."),
            ivy("Settings, at this hour. Tell me what a setting is, slowly."),
            nova("A small value your app reads to decide how to behave. How long an answer may be. Whether a new button shows. [pause] Take the Two Tracks Coach, the study assistant in the Android app. Behind it, a small Python program runs on Azure, Microsoft's cloud, doing the thinking."),
            ivy("And where do its settings live?"),
            nova("Tonight, in Azure App Configuration. Picture a noticeboard in a quiet staff room. On busy nights, Azure runs several copies of the Coach's program, and every copy reads the same board. Change a value once, and they all follow."),
            ivy("No restarts?"),
            nova("None. Under the picture, the board is a store: a service Azure runs for you, a small database of names and values, with its own web address. The program signs in and asks for the settings it wants."),
            ivy("Names and values. Start with the names."),
            nova("Each name is a key. Coach MaxTokens, for instance: the longest answer the Coach may give. Names often begin with the app's name and a colon, so they gather together, like files in one folder."),
            ivy("And beside each name, a value."),
            nova("Mm. The value is what the setting is set to. 800, say. It's kept as text, and the code turns it into a number. [pause] Now, a little twist."),
            ivy("I do like a twist."),
            nova("You want long answers while you're testing, and short ones for real students. So you add a label: a small tag on the key. One key, two values. Dev, for development, is the copy you test on. Prod, for production, is the one students use."),
            ivy("And if a key has no tag at all?"),
            nova("Then it's the shared default. The Coach loads the no-label keys first, then its own label on top. Where both have the key, the later one wins."),
            ivy("Can I put my secrets on the board? Just between us."),
            nova("[pause] Never. A secret is anything that unlocks something, like the password for the AI model. Secrets sleep in Azure Key Vault, a locked safe."),
            ivy("Then what goes on the board, in its place?"),
            nova("A Key Vault reference: a note that says, it's in the safe, in this drawer. The address, not the secret."),
            ivy("Who's allowed to open the safe?"),
            nova("The Coach's program, with its managed identity: an employee badge Azure gives your app, so it signs in with no password in its code. The board never opens the safe. The program follows the address, with its own badge."),
            ivy("And what's written on the badge?"),
            nova("Roles. A role is a named set of permissions, like a door code. App Configuration Data Reader, to read the board. Key Vault Secrets User, to read the safe. Forget the second, and every plain setting loads while the secret is refused."),
            ivy("And the people who change the values?"),
            nova("They get App Configuration Data Owner. It can write, and take snapshots. The app only reads, so the app never needs it."),
            ivy("How does the program actually read the board?"),
            nova("With the configuration provider, a ready-made Python library from Microsoft. It calls load once, as it wakes up, and gets the settings back like a dictionary. Ask for MaxTokens, and it says 800."),
            ivy("And when I change a value at midnight? When does it notice?"),
            nova("Only when it's asked to look. Nothing taps it on the shoulder. The provider has a refresh function, and your code calls it at the start of each request."),
            ivy("Every request? Even at three in the morning?"),
            nova("Every one. But it stays quiet until a set time has passed, thirty seconds by default. Then it checks. [pause] Never call refresh, and your midnight change never arrives."),
            ivy("Does it check every single key? That sounds exhausting."),
            nova("It would be. So it watches just one: a sentinel key. A sentinel is a guard on night watch. Refresh asks one quiet question: has the sentinel changed? If not, back to sleep. If it has, everything reloads at once."),
            ivy("So I change my values, and then I wake the guard."),
            nova("Last, always last. Edit the values, then bump the sentinel, from 12 to 13. Every copy reloads once, with everything in place. Wake the guard first, and a copy might reload halfway through."),
            ivy("And switching a whole feature on and off?"),
            nova("A feature flag. An on and off switch, on the board. The Coach's new voice answers hide behind one. And a filter, a rule on the flag, can turn it on for only ten percent of users, while you watch how it goes."),
            ivy("A little at a time. Tender. And snapshots?"),
            nova("A snapshot is a photograph of chosen settings, with a name, that can never be changed once it's taken. Before each release, you take one. The release loads it by name, so even if someone edits the board later tonight, it gets exactly what you tested."),
            ivy("Now the part I've been waiting for. Where does all this stop working?"),
            nova("Here. [pause] The board is for settings, never secrets. The phone app never reads it; it asks the Coach's program, which does."),
            ivy("And a copy nobody's talking to tonight?"),
            nova("Refresh only runs when requests arrive, so it sees a change on its next request. The Key Vault episode tells you all about the safe."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says reload when one key changes, a sentinel key. If changed values never appear, call refresh. If a Key Vault reference fails while plain keys load, Key Vault Secrets User. If a release needs exactly the tested settings, a snapshot. And if the app only reads, Data Reader."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. [pause] Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            ivy("One guard on watch, all night. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-appconfig",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about the most organised thing in the cloud."),
            rafe("Let me guess. Your settings. You do have a lot of settings."),
            nova("[laugh] Azure App Configuration. It's a shared noticeboard for settings, the small values an app reads to decide how to behave. Like how long an answer may be."),
            rafe("And who reads this noticeboard?"),
            nova("Picture the Two Tracks Coach, the study assistant in the Android app. Behind it, a small Python program on Azure, Microsoft's cloud, does the thinking, and on busy nights Azure runs several copies. Every copy reads the same board. Change a value once, and they all follow."),
            rafe("So everyone's on the same page. I like that in a relationship."),
            nova("Under the picture, the board is a store, a service Azure runs for you: a small database of names and values, with its own web address. Each name is a key, like Coach MaxTokens, the longest answer the Coach may give."),
            rafe("And beside each key, its value. 800, say. Stored as text, and turned into a number by the code."),
            nova("Look at you, paying attention. Now, labels. A label is a tag on a key, so one key holds a different value for each place the app runs. Dev, for development, where you test. Prod, for production, the real thing."),
            rafe("One key, two personalities. Long answers in private, short ones in public."),
            nova("Mm. Quiz time, exam style. The Coach loads the keys with no label, then the prod label. MaxTokens is in both. Which value wins?"),
            rafe("The prod one. The later load wins, so the no-label value is just the default."),
            nova("Right answer. Defaults are for strangers. [kiss]"),
            rafe("My turn. Can I put the password for the AI model on the board?"),
            nova("Never, darling. Some things you keep in a safe. A secret is anything that unlocks something, and secrets live in Azure Key Vault, a locked safe. The board holds a Key Vault reference: a little note that says, it's in the safe, this drawer."),
            rafe("A note that tells you where the secret is, without telling the secret. Very flirtatious."),
            nova("And who opens the safe? The Coach's program, with its managed identity: an employee badge Azure gives the app, so it signs in with no password in its code. The board never opens the safe itself."),
            rafe("So the badge needs the right permissions. Roles, yes? A role is a named set of permissions, like a door code."),
            nova("Exactly. Here's your question. The program reads every plain setting fine, but the Key Vault reference comes back access denied. What's missing?"),
            rafe("App Configuration Data Owner. More power."),
            nova("[sigh] No kiss for that one. Data Owner lets you change the board, which the app never does; it only needs Data Reader there. The missing door code is on the safe: Key Vault Secrets User. It's all about knowing your role."),
            rafe("Knowing my role. Noted. How does the program actually read the board?"),
            nova("With the configuration provider, a ready-made Python library from Microsoft. It calls load once at start-up, and gets the settings back like a dictionary."),
            rafe("And when you change your mind later? Does the board call to say so?"),
            nova("No, the program has to ask. It calls refresh at the start of each request, and refresh waits until a set time has passed, thirty seconds by default, then checks the store."),
            rafe("Checks every key? That's a lot of checking."),
            nova("That's why you watch only one: a sentinel key. A sentinel is a guard on watch. Refresh asks, has the sentinel changed? If yes, everything reloads at once. So, Rafe. You change three settings and want every copy to take all three together. What do you do last?"),
            rafe("Change the sentinel, say from 12 to 13. Last. So nobody reloads halfway through and catches me half dressed."),
            nova("Right answer, and a cheeky one. [kiss]"),
            rafe("My question. Someone set it all up, but changed values never arrive, ever. What did they forget?"),
            nova("They never called refresh. The provider doesn't check in the background. No refresh, no news. Like a partner who never asks how your day was."),
            rafe("Right answer. And I always ask. [kiss]"),
            nova("You do. Now, feature flags. A feature flag is an on and off switch stored on the board. The Coach's new voice answers hide behind one. Your question: they must reach ten percent of students, without shipping new code. How?"),
            rafe("A feature flag with a filter. A filter is a rule on the flag, like on for ten percent of users. Start slow, see how it goes."),
            nova("Right answer. Slow is nice. [kiss]"),
            rafe("Last one from me. A release must use exactly the settings that were tested, even if someone edits the board later. What do you use?"),
            nova("A snapshot. A named photo of chosen settings that can never be changed once it's taken. The release loads it by name. Like the photo of our first date. Nobody is editing that."),
            rafe("Right answer. Some things should stay exactly as they were. [kiss]"),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("The board is for settings, never secrets. And the phone app never reads it; it asks the Coach's program, which does."),
            nova("And a copy that's been quiet all evening?"),
            rafe("Refresh only runs when requests come in, so it waits for its next request. The Key Vault episode covers the safe, if you want more secrets."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("If the question says reload when one key changes, a sentinel key. If values never arrive, call refresh. If a Key Vault reference fails while plain keys load, Key Vault Secrets User. If the app only reads, Data Reader. And if a release needs exactly the tested settings, a snapshot."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            rafe("And I'll be watching you, my favourite sentinel. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun board(app: String, api: String) = DiagramSpec(
        "One App Configuration store that every copy of the backend reads, instead of a settings file per copy.",
        nodes = listOf(
            DNode("phone", "Android app", 0.24f, 0.18f, w = 0.4f, h = 0.2f, sub = app),
            DNode("api", api, 0.76f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "three copies"),
            DNode("file", "Settings file", 0.24f, 0.74f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "edit and restart"),
            DNode("store", "App Configuration", 0.76f, 0.74f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "one store"),
        ),
        edges = listOf(
            DEdge("phone", "api", "asks"),
            DEdge("api", "file", dashed = true),
            DEdge("api", "store", "reads"),
        ),
        aspect = 0.8f,
    )

    private fun keys(key: String) = DiagramSpec(
        "One key, a value per label: the app loads the no-label default first, then its own label, and the later one wins.",
        nodes = listOf(
            DNode("key", key, 0.5f, 0.14f, w = 0.64f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "a setting's name"),
            DNode("dev", "dev label", 0.24f, 0.5f, w = 0.4f, h = 0.2f, sub = "for testing"),
            DNode("prod", "prod label", 0.76f, 0.5f, w = 0.4f, h = 0.2f, sub = "for real users"),
            DNode("def", "No label", 0.5f, 0.85f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "the shared default"),
        ),
        edges = listOf(
            DEdge("key", "dev"),
            DEdge("key", "prod"),
            DEdge("key", "def"),
        ),
        aspect = 0.8f,
    )

    private fun vault(api: String) = DiagramSpec(
        "The store holds a Key Vault reference; the app follows it with its own identity, so it needs a role on both.",
        nodes = listOf(
            DNode("store", "App Configuration", 0.24f, 0.17f, w = 0.42f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "settings only"),
            DNode("ref", "Key Vault reference", 0.24f, 0.52f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "an address"),
            DNode("vault", "Key Vault", 0.76f, 0.52f, w = 0.4f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "the safe"),
            DNode("api", api, 0.5f, 0.86f, w = 0.5f, h = 0.2f, tone = 1, sub = "managed identity"),
        ),
        edges = listOf(
            DEdge("store", "ref"),
            DEdge("ref", "vault", "points to"),
            DEdge("api", "store", "Data Reader"),
            DEdge("api", "vault", "Secrets User"),
        ),
        aspect = 0.8f,
    )

    private fun refresh(api: String) = DiagramSpec(
        "refresh() checks one sentinel key once the interval has passed; when it changes, every key reloads at once.",
        nodes = listOf(
            DNode("api", api, 0.24f, 0.17f, w = 0.42f, h = 0.2f, tone = 1, sub = "refresh() per request"),
            DNode("store", "App Configuration", 0.76f, 0.17f, w = 0.42f, h = 0.28f, kind = NodeKind.CYLINDER),
            DNode("sentinel", "Sentinel key", 0.76f, 0.56f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "12 becomes 13"),
            DNode("reload", "Reload everything", 0.24f, 0.56f, w = 0.42f, h = 0.2f, sub = "all keys at once"),
            DNode("order", "Edit the values, then the sentinel last", 0.5f, 0.87f, w = 0.84f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("api", "store", "every 30 s at most"),
            DEdge("store", "sentinel", "watched"),
            DEdge("sentinel", "reload"),
        ),
        aspect = 0.8f,
    )

    private fun flags() = DiagramSpec(
        "A feature flag with a filter switches a feature on for some users; a snapshot freezes a release's settings.",
        nodes = listOf(
            DNode("flag", "Feature flag", 0.24f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.PILL, sub = "on and off switch"),
            DNode("filter", "Filter", 0.24f, 0.62f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "10 percent of users"),
            DNode("snap", "Snapshot", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 2, sub = "named and frozen"),
            DNode("release", "Release", 0.76f, 0.62f, w = 0.42f, h = 0.22f, tone = 1, sub = "loads it by name"),
        ),
        edges = listOf(
            DEdge("flag", "filter"),
            DEdge("snap", "release"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where the board stops: secrets go to Key Vault, the phone asks the API, and a quiet copy refreshes on its next request.",
        nodes = listOf(
            DNode("secrets", "Secrets", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "Key Vault, not the board"),
            DNode("kv", "Key Vault episode", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "the safe itself"),
            DNode("phone", "Phone app", 0.26f, 0.76f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "asks the API instead"),
            DNode("quiet", "Quiet copy", 0.76f, 0.76f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "sees it next request"),
        ),
        edges = listOf(DEdge("secrets", "kv", "more in")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's App Configuration questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("sentinel", "Reload on one key", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sentinel key"),
            DNode("refresh", "Changes never arrive", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "call refresh()"),
            DNode("ref", "Reference refused", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Key Vault Secrets User"),
            DNode("snap", "Exactly what was tested", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "snapshot"),
            DNode("flag", "10 percent, no new code", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "flag with a filter"),
            DNode("reader", "The app only reads", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Data Reader"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("sentinel", "refresh", "ref", "snap", "flag", "reader")
    private val refreshCode = BoardCard("Python", "config.refresh()  # at the start of each request", code = true)
    private val watchCode = BoardCard("Python", "load(endpoint=url, credential=cred, refresh_on=[WatchKey(\"Coach:Sentinel\")])", code = true)
    private val today = BoardCard("Today", "A store, a dev and a prod label, load in Python, then a sentinel refresh.")

    private fun classicBoard() = PodcastBoard(
        "ai-appconfig",
        listOf(
            // 0: "Hi, and welcome back. Today is about settings..."
            Scene(
                0, "Where settings live", board("Two Tracks Coach", "Coach API"),
                beats = listOf(
                    Beat(0, show = setOf("file"), light = setOf("file"), card = BoardCard("A setting", "A small value the app reads to decide how to behave.")),
                    Beat(2, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The Two Tracks Coach", "An Android app, with a Python program on Azure, Microsoft's cloud.")),
                    Beat(4, light = setOf("api", "file"), pulse = "api" to "file", card = BoardCard("Three copies, one file each", "Edit, rebuild, restart all three. Miss one, and it cooks the old recipe.")),
                    Beat(8, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("App Configuration store", "One noticeboard every copy reads. Change once, and they all follow.")),
                ),
            ),
            // 9: "What's written on the board?"
            Scene(
                9, "Keys, values, labels", keys("Coach:MaxTokens"),
                beats = listOf(
                    Beat(10, show = setOf("key"), light = setOf("key"), card = BoardCard("Key", "A setting's name. Coach:MaxTokens is the longest answer allowed.")),
                    Beat(12, card = BoardCard("Value", "What the setting is set to, stored as text: 800.")),
                    Beat(14, show = setOf("dev", "prod"), light = setOf("dev", "prod"), pulse = "key" to "prod", card = BoardCard("Label", "A tag on a key: one value per place the app runs, dev or prod.")),
                    Beat(16, show = setOf("def"), light = setOf("def", "prod"), pulse = "key" to "def", card = BoardCard("No label first", "Load the no-label defaults, then the app's own label. The later wins.")),
                ),
            ),
            // 17: "Okay. Can the password for the AI model go on the board too?"
            Scene(
                17, "Secrets stay in the safe", vault("Coach API"),
                beats = listOf(
                    Beat(18, show = setOf("store", "vault"), light = setOf("vault"), card = BoardCard("Secret", "Anything that unlocks something. It lives in Key Vault, the safe.")),
                    Beat(20, show = setOf("ref"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A note on the board with the secret's address, never the secret.")),
                    Beat(22, show = setOf("api"), light = setOf("api"), pulse = "api" to "vault", card = BoardCard("Managed identity", "A badge Azure gives the app, so no password sits in the code.")),
                    Beat(24, light = setOf("api", "store", "vault"), pulse = "api" to "store", card = BoardCard("Two roles", "App Configuration Data Reader on the store; Key Vault Secrets User.")),
                    Beat(26, light = setOf("store"), card = BoardCard("Data Owner", "Reads, writes and takes snapshots. For the people editing, not the app.")),
                ),
            ),
            // 27: "How does the Python code actually read the board?"
            Scene(
                27, "Load, then refresh", refresh("Coach API"),
                beats = listOf(
                    Beat(28, show = setOf("api", "store"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Configuration provider", "A Python library: load() once at start, then read it like a dictionary.")),
                    Beat(32, light = setOf("api", "store"), card = refreshCode),
                    Beat(36, show = setOf("sentinel"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = watchCode),
                    Beat(38, show = setOf("reload", "order"), light = setOf("reload", "order"), pulse = "sentinel" to "reload", card = BoardCard("Sentinel last", "Edit the values, then bump the sentinel: every copy reloads once.")),
                ),
            ),
            // 39: "Okay. What about switching a whole feature on and off?"
            Scene(
                39, "Flags and snapshots", flags(),
                beats = listOf(
                    Beat(40, show = setOf("flag"), light = setOf("flag"), card = BoardCard("Feature flag", "An on and off switch on the board, such as VoiceAnswers.")),
                    Beat(42, show = setOf("filter"), light = setOf("filter"), pulse = "flag" to "filter", card = BoardCard("Filter", "On for 10 percent of users, in a time window, or for named groups.")),
                    Beat(46, show = setOf("snap", "release"), light = setOf("snap"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named copy of chosen settings that never changes. Load it by name.")),
                    Beat(50, light = setOf("flag", "snap"), card = BoardCard("The usual mistakes", "A plain secret, every key watched, no refresh(), no role on the safe.")),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("sentinel", "refresh", "snap"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: sentinel, refresh, snapshot.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-appconfig--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Setting That Wouldn't Change..."
            Scene(
                0, "The setting that won't change", board("FlashBack", "Backend"),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The case", "Hints made shorter on Monday, and just as long on Tuesday.")),
                    Beat(2, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("App Configuration", "A noticeboard for settings. Every copy reads the same board.")),
                    Beat(4, show = setOf("file"), light = setOf("file"), pulse = "api" to "file", card = BoardCard("Three copies", "A file means editing and restarting all three. The board doesn't.")),
                    Beat(6, light = setOf("store"), card = BoardCard("Under the picture", "A store: a small database of names and values, at its own address.")),
                ),
            ),
            // 7: "Start with the names."
            Scene(
                7, "Keys and values", keys("FlashBack:HintLength"),
                beats = listOf(
                    Beat(8, show = setOf("key"), light = setOf("key"), card = BoardCard("Key", "FlashBack:HintLength, the longest a hint may be.")),
                    Beat(10, card = BoardCard("Value", "Changed from 300 to 100 on Monday. The hints didn't budge.")),
                ),
            ),
            // 11: "My first suspect: nobody restarted the backend..."
            Scene(
                11, "Clue one: no refresh", refresh("Backend"),
                beats = listOf(
                    Beat(11, show = setOf("api"), light = setOf("api"), card = BoardCard("Suspect: no restart", "A restart works, but throws away the point of the board.")),
                    Beat(12, show = setOf("store"), light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Configuration provider", "load() once at start-up; the settings come back like a dictionary.")),
                    Beat(16, card = refreshCode),
                    Beat(20, show = setOf("sentinel"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Sentinel key", "The one key watched. Checking faster only adds load.")),
                    Beat(22, show = setOf("reload"), light = setOf("reload"), pulse = "sentinel" to "reload", card = watchCode),
                    Beat(24, show = setOf("order"), light = setOf("order"), card = BoardCard("Clue two: sentinel first", "Copies reloaded between edits: short hints in the old tone.")),
                    Beat(26, light = setOf("order", "reload"), card = BoardCard("Sentinel last", "Every copy reloads once, with every new value in place.")),
                ),
            ),
            // 27: "Thursday, then. What broke on Thursday?"
            Scene(
                27, "Thursday: the safe", vault("Backend"),
                beats = listOf(
                    Beat(28, show = setOf("store"), light = setOf("store"), card = BoardCard("A secret on the board", "The AI model's password, in plain text. The board isn't for secrets.")),
                    Beat(30, show = setOf("ref", "vault"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "The secret's address on the board; the secret stays in the safe.")),
                    Beat(32, show = setOf("api"), light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("Access denied", "Plain settings load; the reference fails. The badge can't open the safe.")),
                    Beat(36, light = setOf("vault"), card = BoardCard("Key Vault Secrets User", "The missing role was on the safe. Data Owner was the wrong suspect.")),
                ),
            ),
            // 37: "Friday, then. I'm almost afraid to ask."
            Scene(
                37, "Friday: two good tools", flags(),
                beats = listOf(
                    Beat(38, show = setOf("flag", "filter"), light = setOf("filter"), pulse = "flag" to "filter", card = BoardCard("Feature flag and filter", "Spoken hints behind a flag, on for 10 percent of users.")),
                    Beat(40, show = setOf("snap", "release"), light = setOf("snap"), pulse = "snap" to "release", card = BoardCard("Snapshot", "Taken before the release and loaded by name: exactly what was tested.")),
                ),
            ),
            // 41: "I saw labels on the evidence board too. Dev and prod?"
            Scene(
                41, "Labels on the evidence", keys("FlashBack:HintLength"),
                beats = listOf(
                    Beat(42, show = setOf("dev", "prod", "def"), light = setOf("dev", "prod"), pulse = "key" to "prod", card = BoardCard("Label", "dev or prod: one value per place. No-label first, the later wins.")),
                ),
            ),
            // 43: "Any loose ends in the file?"
            Scene(
                43, "Loose ends", limits(),
                beats = listOf(
                    Beat(44, light = setOf("phone", "quiet", "kv"), pulse = "secrets" to "kv", card = BoardCard("Where the board stops", "Never secrets; the phone asks the backend; quiet copies wait.")),
                ),
            ),
            // 47: "So, the case summary for the exam?"
            Scene(
                47, "Case closed", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("refresh", "sentinel", "ref"), card = BoardCard("Listen for the phrase", "Refresh, a sentinel, the safe's role, a snapshot, a flag.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-appconfig--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "What the Coach team did", board("Two Tracks Coach", "Coach API"),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("The move", "Every Coach setting onto Azure App Configuration.")),
                    Beat(2, show = setOf("store", "file"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("A shared noticeboard", "Every copy reads the same board. Change once, no restart.")),
                    Beat(4, light = setOf("store"), card = BoardCard("Under the picture", "A store of names and values; each name is a key, like MaxTokens.")),
                    Beat(6, card = BoardCard("Value", "800, stored as text, turned into a number by the code.")),
                ),
            ),
            // 7: "What did Tom do?"
            Scene(
                7, "Monday: Tom", vault("Coach API"),
                beats = listOf(
                    Beat(8, show = setOf("store"), light = setOf("store"), card = BoardCard("Tom", "The AI model's password, as a plain value on the board.")),
                    Beat(10, show = setOf("ref", "vault"), light = setOf("ref", "vault"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A note with the secret's address. The secret stays in the safe.")),
                    Beat(12, show = setOf("api"), light = setOf("api"), pulse = "api" to "vault", card = BoardCard("Managed identity", "A badge Azure gives the app, so no password sits in its code.")),
                ),
            ),
            // 13: "Tuesday?"
            Scene(
                13, "Tuesday: the intern", vault("Coach API"),
                beats = listOf(
                    Beat(14, light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Only one role", "Data Reader on the board, nothing on the safe: access denied.")),
                    Beat(16, light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("Two door codes", "Data Reader on the store, Key Vault Secrets User on the safe.")),
                ),
            ),
            // 17: "Wednesday. Go."
            Scene(
                17, "Wednesday: Marcus", refresh("Coach API"),
                beats = listOf(
                    Beat(18, show = setOf("api", "store"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Configuration provider", "Loads once at start-up, then works like a dictionary.")),
                    Beat(20, light = setOf("api", "store"), card = refreshCode),
                ),
            ),
            // 23: "Poor Marcus. Thursday?"
            Scene(
                23, "Thursday: Dana", refresh("Coach API"),
                beats = listOf(
                    Beat(24, show = setOf("api", "store"), light = setOf("store"), card = BoardCard("Every key watched", "Two hundred little requests per check, from every copy.")),
                    Beat(26, show = setOf("sentinel", "reload", "order"), light = setOf("sentinel", "order"), pulse = "store" to "sentinel", card = watchCode),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", flags(),
                beats = listOf(
                    Beat(30, show = setOf("snap"), light = setOf("snap"), card = BoardCard("A bad edit at four", "An hour before a release. But Priya had a snapshot.")),
                    Beat(32, show = setOf("release"), light = setOf("snap", "release"), pulse = "snap" to "release", card = BoardCard("Snapshot", "Named and frozen. The release loads it by name: release 1.4.")),
                    Beat(34, show = setOf("flag", "filter"), light = setOf("flag", "filter"), pulse = "flag" to "filter", card = BoardCard("Feature flag", "Voice answers behind a switch, on for 10 percent of users.")),
                ),
            ),
            // 35: "And labels? I heard dev and prod in the kitchen."
            Scene(
                35, "Labels", keys("Coach:MaxTokens"),
                beats = listOf(
                    Beat(36, show = setOf("dev", "prod", "def"), light = setOf("dev", "prod"), pulse = "key" to "prod", card = BoardCard("Label", "dev for testing, prod for students. No-label first, later wins.")),
                ),
            ),
            // 37: "Did anything still go wrong?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, light = setOf("secrets", "phone", "quiet"), pulse = "secrets" to "kv", card = BoardCard("Where the board stops", "Never secrets; the phone asks the API; quiet copies wait.")),
                ),
            ),
            // 41: "So, the gossip I should remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("ref", "refresh", "sentinel", "snap"), card = BoardCard("Listen for the phrase", "Tom, the intern, Marcus, Dana and Priya: one answer each.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-appconfig--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Where settings sleep", board("Two Tracks Coach", "Coach API"),
                beats = listOf(
                    Beat(0, show = setOf("file"), card = BoardCard("Tonight", "Settings, and where they should sleep.")),
                    Beat(2, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("A setting", "A small value the app reads to decide how to behave.")),
                    Beat(4, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("App Configuration", "One noticeboard every copy reads. Change once, they all follow.")),
                    Beat(6, light = setOf("store"), card = BoardCard("Under the picture", "A store: a small database of names and values, at its own address.")),
                ),
            ),
            // 7: "Names and values. Start with the names."
            Scene(
                7, "Keys, values, labels", keys("Coach:MaxTokens"),
                beats = listOf(
                    Beat(8, show = setOf("key"), light = setOf("key"), card = BoardCard("Key", "A setting's name: Coach:MaxTokens, the longest answer.")),
                    Beat(10, card = BoardCard("Value", "What it's set to: 800, kept as text.")),
                    Beat(12, show = setOf("dev", "prod"), light = setOf("dev", "prod"), pulse = "key" to "prod", card = BoardCard("Label", "A small tag on the key: dev to test on, prod for students.")),
                    Beat(14, show = setOf("def"), light = setOf("def"), pulse = "key" to "def", card = BoardCard("No label first", "The shared default loads first; the app's label wins.")),
                ),
            ),
            // 15: "Can I put my secrets on the board? Just between us."
            Scene(
                15, "Secrets sleep in the safe", vault("Coach API"),
                beats = listOf(
                    Beat(18, show = setOf("store", "ref", "vault"), light = setOf("ref", "vault"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "The board holds the address; the secret sleeps in Key Vault.")),
                    Beat(20, show = setOf("api"), light = setOf("api"), pulse = "api" to "vault", card = BoardCard("Managed identity", "An employee badge for the app: no password in its code.")),
                    Beat(22, light = setOf("api", "store", "vault"), pulse = "api" to "store", card = BoardCard("Two roles", "App Configuration Data Reader; Key Vault Secrets User.")),
                    Beat(24, light = setOf("store"), card = BoardCard("Data Owner", "For the people who change values and take snapshots.")),
                ),
            ),
            // 25: "How does the program actually read the board?"
            Scene(
                25, "The guard on night watch", refresh("Coach API"),
                beats = listOf(
                    Beat(26, show = setOf("api", "store"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Configuration provider", "load() once as it wakes up; read it like a dictionary.")),
                    Beat(28, light = setOf("api", "store"), card = refreshCode),
                    Beat(32, show = setOf("sentinel"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = watchCode),
                    Beat(34, show = setOf("reload", "order"), light = setOf("order", "reload"), pulse = "sentinel" to "reload", card = BoardCard("Sentinel last", "Edit the values, then wake the guard: every copy reloads once.")),
                ),
            ),
            // 35: "And switching a whole feature on and off?"
            Scene(
                35, "Flags and snapshots", flags(),
                beats = listOf(
                    Beat(36, show = setOf("flag", "filter"), light = setOf("flag", "filter"), pulse = "flag" to "filter", card = BoardCard("Feature flag", "Voice answers behind a switch, on for 10 percent of users.")),
                    Beat(38, show = setOf("snap", "release"), light = setOf("snap"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named photograph of settings that never changes.")),
                ),
            ),
            // 39: "Now the part I've been waiting for. Where does all this stop working?"
            Scene(
                39, "Where it stops", limits(),
                beats = listOf(
                    Beat(40, light = setOf("secrets", "phone", "quiet"), pulse = "secrets" to "kv", card = BoardCard("Where the board stops", "Never secrets; the phone asks the API; quiet copies wait.")),
                ),
            ),
            // 43: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("sentinel", "refresh", "ref", "snap", "reader"), card = BoardCard("Listen for the phrase", "Sentinel, refresh, the safe's role, a snapshot, Data Reader.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-appconfig--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", board("Two Tracks Coach", "Coach API"),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "The most organised thing in the cloud.")),
                    Beat(2, show = setOf("store"), light = setOf("store"), card = BoardCard("App Configuration", "A shared noticeboard for an app's settings.")),
                    Beat(4, show = setOf("phone", "api", "file"), light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Every copy reads it", "Change a value once, and they all follow.")),
                    Beat(6, light = setOf("store"), card = BoardCard("Under the picture", "A store of names and values. Each name is a key.")),
                ),
            ),
            // 7: "And beside each key, its value..."
            Scene(
                7, "Two personalities", keys("Coach:MaxTokens"),
                beats = listOf(
                    Beat(7, show = setOf("key"), light = setOf("key"), card = BoardCard("Key and value", "Coach:MaxTokens is 800, stored as text.")),
                    Beat(8, show = setOf("dev", "prod"), light = setOf("dev", "prod"), pulse = "key" to "prod", card = BoardCard("Label", "One key, a value per place: dev to test, prod for real.")),
                    Beat(10, show = setOf("def"), light = setOf("def", "prod"), pulse = "key" to "def", card = BoardCard("Quiz: which value wins?", "No-label keys load first, then prod. MaxTokens is in both.")),
                    Beat(12, light = setOf("prod"), card = BoardCard("Right answer", "The later load wins: prod over the no-label default.")),
                ),
            ),
            // 13: "My turn. Can I put the password for the AI model on the board?"
            Scene(
                13, "Knowing your role", vault("Coach API"),
                beats = listOf(
                    Beat(14, show = setOf("store", "ref", "vault"), light = setOf("ref", "vault"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A note that says where the secret is, without the secret.")),
                    Beat(16, show = setOf("api"), light = setOf("api"), pulse = "api" to "vault", card = BoardCard("Managed identity", "A badge Azure gives the app: no password in its code.")),
                    Beat(18, light = setOf("api", "vault"), card = BoardCard("Quiz: access denied", "Plain settings load, the reference is refused. What's missing?")),
                    Beat(20, light = setOf("vault"), card = BoardCard("No kiss: it's the safe's role", "Key Vault Secrets User on the vault, not Data Owner on the board.")),
                ),
            ),
            // 21: "Knowing my role. Noted. How does the program actually read the board?"
            Scene(
                21, "A guard on watch", refresh("Coach API"),
                beats = listOf(
                    Beat(22, show = setOf("api", "store"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Configuration provider", "load() once, then refresh() at the start of each request.")),
                    Beat(26, show = setOf("sentinel", "reload", "order"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Quiz: what do you do last?", "Three settings changed. Every copy must take all three together.")),
                    Beat(28, light = setOf("order", "reload"), pulse = "sentinel" to "reload", card = BoardCard("Right answer", "Change the sentinel last, and every copy reloads once.")),
                    Beat(30, light = setOf("api"), card = refreshCode),
                ),
            ),
            // 31: "Right answer. And I always ask."
            Scene(
                31, "Flags and photos", flags(),
                beats = listOf(
                    Beat(32, show = setOf("flag", "filter"), light = setOf("flag"), card = BoardCard("Quiz: ten percent", "Voice answers for 10 percent of students, with no new code. How?")),
                    Beat(34, light = setOf("filter"), pulse = "flag" to "filter", card = BoardCard("Right answer", "A feature flag with a filter: on for 10 percent of users.")),
                    Beat(36, show = setOf("snap", "release"), light = setOf("snap"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named photo of settings that never changes. Load it by name.")),
                ),
            ),
            // 38: "And where does it all stop working, Rafe?..."
            Scene(
                38, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(39, light = setOf("secrets", "phone", "quiet"), pulse = "secrets" to "kv", card = BoardCard("Where the board stops", "Never secrets; the phone asks the API; quiet copies wait.")),
                ),
            ),
            // 42: "Last question, for the exam. What do we remember?"
            Scene(
                42, "For the exam", exam(),
                beats = listOf(
                    Beat(43, show = all6, light = setOf("sentinel", "refresh", "ref", "reader", "snap"), card = BoardCard("Listen for the phrase", "Five for five: sentinel, refresh, the safe's role, reader, snapshot.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
