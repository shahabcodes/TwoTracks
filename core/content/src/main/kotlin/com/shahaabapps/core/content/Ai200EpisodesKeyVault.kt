package com.shahaabapps.core.content

/** Key Vault for AI apps: secrets, rotation and retrieval: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesKeyVault {
    private val classic = Podcast(
        "ai-keyvault",
        listOf(
            maya("Hi, and welcome back. Today is Key Vault, for AI apps. Here's the puzzle. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. It calls an AI model, and the model wants an API key: a long password that proves you're allowed to call it. Where does that key live?"),
            leo("In the app, obviously. Somewhere hidden. [chuckle] I'm guessing that's wrong."),
            maya("Very wrong. Anything shipped inside an Android app can be pulled out of it, hidden or not. So the phone never holds the key. The phone calls your backend, and only the backend talks to the place where the key lives. That place is Azure Key Vault."),
            leo("And Key Vault is, in plain words?"),
            maya("A locked safe in Azure, for secrets. A secret is anything you'd never say out loud: API keys, passwords, connection strings, which are database addresses with a password baked in. Only the people and apps you choose can open the safe."),
            leo("But then the backend needs a password to open the safe. Doesn't that just move the problem?"),
            maya("That's the clever part. It uses a managed identity instead. Picture an employee badge that Azure makes for your app, and looks after itself. Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system. Nobody ever sees its password, so nobody can leak it."),
            leo("And how does the vault decide whether the badge is allowed in?"),
            maya("With RBAC, role-based access control. Permissions come as roles, like job titles. Key Vault Secrets User can read secret values, nothing more. Secrets Officer can create, change and delete them. Key Vault Administrator can do everything, and that's for a person, rarely. Always the smallest role that works."),
            leo("So the Coach backend gets Secrets User. Now, I've heard there are two kinds of badge."),
            maya("Two. A system-assigned identity is switched on inside one app. It belongs to that app alone, and when the app is deleted, the badge goes with it. It can't be shared, and it can't be given a role before its app exists."),
            leo("And the other kind?"),
            maya("A user-assigned identity. This one is a resource of its own. You create it by itself, before any app exists, and give it a name, like coach-id. Then you grant it its role once, and attach it to as many apps as you like."),
            leo("Why would I want one badge for several apps?"),
            maya("For example, the Coach API runs on Container Apps, Azure's service for running a packaged app, and a nightly function rebuilds the search data. Both read the same AI key. With one user-assigned identity, you grant Key Vault Secrets User once, and attach the identity to both. Add a third app next month, and you just attach it. No new role."),
            leo("And if I delete one of those apps?"),
            maya("The identity stays, still holding its role, ready for the next app. It outlives any one app. That's the point. Just never delete the identity itself while apps still use it, or they all lose access at once. So if the exam says shared, or created first, or must outlive the app, the answer is user-assigned."),
            leo("And in the Python code? How does it show the badge?"),
            maya("With one line, DefaultAzureCredential. It finds the right sign-in by itself. On your laptop, it uses your own sign-in, after az login, the Azure command line's way to sign in. In Azure, it uses the managed identity. Same code in both places."),
            leo("And if the app wears a user-assigned badge? How does it know which one?"),
            maya("You set an environment variable, a named setting the app reads when it starts, called AZURE CLIENT ID. Its value is the identity's client ID, the badge's own number. Then DefaultAzureCredential knows exactly which badge to wear."),
            leo("Okay. The backend reads the key. Every time someone asks a question?"),
            maya("No, and that's a common mistake. Calling the vault on every request is slow, and at busy times you get throttled, told to slow down. So cache it: keep the value in memory for a few minutes, five, say, then fetch it again."),
            leo("Fetch it again? Does the key change?"),
            maya("It should, now and then. Every time you write a secret, Key Vault keeps it as a new secret version, with its own id. The old versions stay. Ask without a version, and you get the latest. Pin a version number, and you get that one forever, so a new key never reaches you."),
            leo("So changing the key on purpose is called?"),
            maya("Rotation. Replacing a secret on a schedule, so a leaked value stops working. Now, the vault also holds keys for encryption, which scramble data so only the right holder can read it. Those have a key rotation policy: the vault makes new key versions by itself. But an API key comes from the AI provider, so secrets have no built-in rotation."),
            leo("Then how does an API key get rotated, with no person involved?"),
            maya("You give the secret an expiry date. About 30 days before it, Key Vault sends an event called SecretNearExpiry to Event Grid, Azure's notice board that pushes news. A function, a small piece of code that runs when something happens, wakes up, gets a new key from the provider, and writes it to the vault as a new version."),
            leo("And the backend picks it up, because it reads the latest and its cache runs out."),
            maya("Exactly. And if the model rejects the old key first, drop the cached value and read again. If you'd rather write no code, App Service, Azure's home for web apps, and Functions can use a Key Vault reference: a setting that says, fetch this from the vault. It refreshes within 24 hours, as long as you don't pin a version."),
            leo("Last scary thing. What if someone deletes the key by accident?"),
            maya("Soft delete has you covered. It's a recycle bin. A deleted secret, or even a whole vault, waits there for a retention period of 7 to 90 days, 90 by default, and you can recover it. You choose the period when you create the vault, and it's fixed after that. Soft delete is always on for new vaults."),
            leo("So deleted means waiting in the bin. Can anyone empty the bin?"),
            maya("That's a purge. Purging erases a deleted secret for good, right now, with no way back. It needs its own permission, such as the Key Vault Purge Operator role. And while a secret sits in the bin, its name is taken. You can't create a new one with that name: recover it, or wait."),
            leo("So someone with that permission, careless or worse, could still make it vanish today."),
            maya("[pause] Unless purge protection is on. It's off by default. With it on, nothing in the bin can be purged before its retention period ends. Not by you, not by an administrator, not by anyone. And once it's on, it can never be turned off, so nobody can quietly remove the safety net later."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If several apps share one identity, or it must exist first, user-assigned. If an app only reads secrets, Key Vault Secrets User. If a secret must rotate itself, an expiry date, SecretNearExpiry and a function. If rotation never reaches the app, it pinned a version. And if nobody may purge early, purge protection."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            leo("A safe with a recycle bin nobody can empty early. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-keyvault",
        listOf(
            maya("Welcome to Case Files. Today's case: The Key That Walked Out. QuizQuest is a small Android quiz app, with a Python backend on Azure. It calls an AI model to write quiz questions, and the model needs an API key, a long password that proves you may call it. On Monday, the AI bill was ten times its usual size."),
            leo("Detective Leo, on the case. Someone was using our key."),
            maya("Someone was. Clue one: the key was inside the Android app itself, tucked away and scrambled a little. Somebody unpacked the app, found it, and used it for their own projects."),
            leo("Then my first suspect is the scrambling. Scramble it harder, and hide it better."),
            maya("[pause] Wrong suspect. Anything shipped inside an app can be pulled out, however well it's hidden. The fix isn't a better hiding place. It's that the phone never holds the key at all. The phone calls the backend, and only the backend reads the key, from Azure Key Vault."),
            leo("Key Vault being, in plain words?"),
            maya("A locked safe in Azure for secrets: API keys, passwords, connection strings. Only the people and apps you choose can open it."),
            leo("And the backend opens it with, what, another password?"),
            maya("With a managed identity. Picture an employee badge Azure makes for your app. Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system, and it gets short-lived tokens, signed passes that run out within hours. Nobody ever sees a password, so nothing can leak."),
            leo("How does the backend get a token without a password? That sounds like magic."),
            maya("Not magic. Azure started the backend, so Azure knows who's running there. It answers the backend's token request on a private local address that nothing outside can reach. The backend shows the token to the vault, and the vault checks its role. In Python, one line does it all: DefaultAzureCredential. On a laptop, it uses your own sign-in after az login, the Azure command line's sign-in."),
            leo("Role. As in a job title?"),
            maya("Exactly. That's RBAC, role-based access control. And here's clue two. The team, in a hurry, gave the backend Key Vault Administrator. Everything, on every secret."),
            leo("Suspect: too much power. If the backend is ever broken into, the attacker gets the lot."),
            maya("Now you're detecting. An app that only reads gets Key Vault Secrets User: read secret values, nothing more. The rotation code gets Secrets Officer, to create and change secrets. Administrator is for a person, rarely."),
            leo("Second witness?"),
            maya("The nightly job. QuizQuest added a function, a small piece of code that runs on a schedule, to pre-write tomorrow's quizzes. It needs the same key. Its first night, it was refused at the vault door."),
            leo("Suspect: the vault is full. Or it only allows one app."),
            maya("Neither. The function had a system-assigned identity: a badge switched on inside one app, born with it and deleted with it. That badge didn't exist until the function was deployed, so nobody could grant its role in advance. It ran before anyone remembered."),
            leo("So what should they use?"),
            maya("A user-assigned identity. It's a resource of its own. You create it first, grant Key Vault Secrets User once, then attach it to the backend and the nightly function both. A third app next month? Just attach it. Delete one app? The identity stays, with its role, ready for the next. It outlives any one app."),
            leo("And how does the code know which badge to wear?"),
            maya("An environment variable, a named setting the app reads at start, called AZURE CLIENT ID, set to the identity's client ID. DefaultAzureCredential reads it. Just never delete the shared identity while apps still use it, or they all lose access at once."),
            leo("Third witness. I can feel it."),
            maya("[chuckle] The leaked key was replaced with a new one. A new secret version: every write keeps the old versions and adds a new one. But the backend kept failing, with the model saying the key wasn't valid, for days."),
            leo("Suspect: the vault didn't save the new key."),
            maya("It did. The backend's setting named a version number. Pinned. So it read the old, dead key forever. Read without a version, and you get the latest. And cache it for a few minutes, so you're not calling the vault on every request and getting throttled, told to slow down."),
            leo("And next time, can the key change itself?"),
            maya("Not by itself; only the provider can issue a new API key. So give the secret an expiry date. About 30 days before it, Key Vault sends SecretNearExpiry to Event Grid, Azure's notice board that pushes news. A function wakes, gets a new key, and writes a new version. The Event Grid and Functions episodes cover both halves."),
            leo("Couldn't they skip the code, and just point a setting at the vault?"),
            maya("They could. App Service, Azure's home for web apps, and Functions take a Key Vault reference, a setting that says fetch this from the vault, refreshed within 24 hours. Container Apps, Azure's service for running a packaged app, can point a secret at the vault too, picked up within about 30 minutes. Either way, no version in it."),
            leo("Last witness. Who's left?"),
            maya("The intern, cleaning up. They deleted the old secret. Then, to be tidy, they purged it. Then a manager asked for it back, for the audit, a check of past records."),
            leo("Deleted is recoverable, though. Soft delete."),
            maya("Soft delete is a recycle bin: a deleted secret waits there for 7 to 90 days, and can be recovered. But a purge erases it from the bin for good, right now. QuizQuest's vault didn't have purge protection. With it on, nothing can be purged before the retention period ends, not even by an administrator. And once it's on, it can never be turned off."),
            leo("Could they at least make a new secret with the same name?"),
            maya("Not while the old one sits in the bin: its name is taken. Recover it, or wait. The bin's retention period, 90 days by default, is chosen when the vault is created and fixed after that. And purging needs its own permission, such as the Key Vault Purge Operator role."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. The phone never holds a secret. An app that only reads gets Secrets User. Shared, or created first, or outliving an app: a user-assigned identity. Rotation never arrives: a pinned version. And nobody may purge early: purge protection."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            leo("No more keys walking out. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-keyvault",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Sit down. They moved every secret for the Two Tracks Coach into Key Vault. That's the study assistant in the Android app, with its Python backend on Azure. And it was a week."),
            leo("[laugh] I heard someone asked for the vault's password. What's Key Vault, in plain words?"),
            maya("A locked safe in Azure for secrets. API keys, which are long passwords that prove you may call a service, plus passwords and connection strings, database addresses with a password baked in. Only the people and apps you choose can open it."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom wanted the Android app to call the AI model directly. So he put the model's API key inside the app. Hidden, he said."),
            leo("And?"),
            maya("And anything shipped inside an app can be pulled out of it. The phone never holds a secret. It calls the backend, and only the backend reads the key from the vault. Tom took it out by lunch."),
            leo("[sigh] Poor Tom. And how does the backend open the safe?"),
            maya("With a managed identity, a badge Azure makes for the app. Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system. Azure answers the app's request for a token, a short-lived signed pass, on a private address nobody outside can reach. No password, anywhere. In Python it's one line: DefaultAzureCredential."),
            leo("Tuesday?"),
            maya("Tuesday was Dana. Dana set up RBAC, role-based access control, where permissions come as roles, like job titles. And she gave the backend Key Vault Administrator. Just so it works."),
            leo("Is that bad? It worked."),
            maya("It's everything, on every secret. An app that only reads gets Key Vault Secrets User: read values, nothing more. Code that writes new versions gets Secrets Officer. Administrator is for a person, rarely. Dana swapped it before anyone else noticed. Well. Before almost anyone."),
            leo("Wednesday?"),
            maya("Marcus. Marcus built the nightly function, a small piece of code that runs on a schedule and rebuilds the search data. It needs the same AI key. He switched on a system-assigned identity, the badge that lives and dies with one app. And on its first night, the vault said no."),
            leo("Why no?"),
            maya("Because a system-assigned badge only exists once its app does, so nobody had granted its role yet. And the Coach API needed a badge too. So Priya, of course, suggested a user-assigned identity."),
            leo("Which is different how?"),
            maya("It's a resource of its own. You create it first, grant Key Vault Secrets User once, and attach it to the API and the nightly function both. A new app next month just gets attached. Delete one app, and the identity stays, with its role. It outlives any one app. Just never delete it while apps still wear it."),
            leo("And the code knows which badge?"),
            maya("An environment variable, a named setting the app reads at start: AZURE CLIENT ID, set to the identity's client ID. DefaultAzureCredential picks it up. Marcus was very relieved."),
            leo("Thursday, I'm guessing, the intern."),
            maya("[chuckle] The intern. Bless them. They read the key from the vault on every single request. Thousands an hour. And at the busy time, the vault started throttling them, telling them to slow down."),
            leo("So, cache it?"),
            maya("Cache it. Keep the value in memory for about five minutes, then read again. And read it without a version. Every write makes a new secret version, and the old ones stay. Without a version you get the latest; pin one, and rotation never reaches you."),
            leo("Rotation being?"),
            maya("Replacing a secret on a schedule, so a leaked one stops working. Secrets don't rotate themselves, because only the provider can issue a new API key. So the key gets an expiry date, and about 30 days before, Key Vault sends SecretNearExpiry to Event Grid, the notice board that pushes. A function writes the new version. The Event Grid episode has the rest."),
            leo("Couldn't someone skip the code entirely?"),
            maya("Dana did, for the admin website. App Service, Azure's home for web apps, can use a Key Vault reference: a setting that says, fetch this from the vault, using the app's own identity. It refreshes within 24 hours. As long as nobody pins a version."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She deleted an old test secret, called OpenAiKey. Then she tried to create a fresh one with the same name. And the vault refused."),
            leo("Why?"),
            maya("Soft delete. It's a recycle bin, always on for new vaults. A deleted secret waits there for its retention period, 7 to 90 days, 90 by default, and while it's in the bin, its name is taken. Recover it, or wait. Or purge it, which erases it from the bin for good, right now."),
            leo("So she purged it?"),
            maya("She tried. And she couldn't. Because Priya had switched on purge protection when she created the vault. With it on, nothing in the bin can be purged before its retention ends. Not by her, not by an administrator, not by anyone. And it can never be turned off. She'd outsmarted herself."),
            leo("Wait. Who's allowed to purge, normally?"),
            maya("Anyone with the purge permission, like the Key Vault Purge Operator role. That's exactly why purge protection matters. A careless click, or someone with bad intentions, can't erase anything early, and nobody can switch the protection off to try."),
            leo("So where does all this stop working?"),
            maya("At the phone. No identity, no secret, ever: it calls the backend. And on your laptop there's no managed identity; DefaultAzureCredential uses your own sign-in after az login instead."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Several apps share one identity, or it must exist first: user-assigned. An app that only reads: Key Vault Secrets User. A secret that must rotate itself: expiry date, SecretNearExpiry and a function. Rotation never arrives: a pinned version. Nobody may purge early: purge protection, which can't be turned off."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            leo("Recover, don't purge. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-keyvault",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about keeping secrets."),
            ivy("Mm. My favourite kind of conversation. Where does a careful app keep its secrets, Nova?"),
            nova("Somewhere safe. Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. It calls an AI model, and the model wants an API key, a long password that proves you may call it. That key must never sit in the phone."),
            ivy("Because anything in the app can be taken out of it."),
            nova("Exactly. So the phone calls the backend, and only the backend reads the key, from Azure Key Vault. A locked safe in the cloud for secrets: API keys, passwords, connection strings, which are database addresses with a password inside. Only the ones you choose can open it."),
            ivy("And how does the backend open it? Without a password of its own?"),
            nova("With a managed identity. Picture an employee badge that Azure makes for your app. [pause] Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system. It never has a password you can see, so you can never lose one."),
            ivy("Then how does the backend prove it's really the backend? Not even a whisper of a password?"),
            nova("Azure started the backend, so Azure knows who's running there. It gives the backend a private little address to ask for tokens, signed passes that fade within hours. The backend asks, shows the token to the vault, and the vault checks its role. In Python, that's one line: DefaultAzureCredential."),
            ivy("Role. Tell me about roles."),
            nova("That's RBAC, role-based access control. Permissions come as roles, like job titles. Key Vault Secrets User reads secret values, and nothing more. Secrets Officer can create and change them. Administrator does everything, and that's for a person, rarely. Always the smallest role that works."),
            ivy("And I hear there are two kinds of badge."),
            nova("Two. A system-assigned identity belongs to one app alone. It's switched on inside that app, and when the app is deleted, the badge goes with it. Faithful, simple. But it can't be shared, and it can't get its role before the app exists."),
            ivy("And the other kind? The one that gets around?"),
            nova("[chuckle] A user-assigned identity. You create it on its own, as a resource of its own, before any app exists. You grant it Key Vault Secrets User once. Then you attach it to several apps at once: the Coach API, and the nightly function that rebuilds the search data."),
            ivy("And if one of those apps is deleted, late one night?"),
            nova("The identity stays, still holding its role, ready for the next app. It outlives any one app. [pause] Just never delete the identity itself while apps are still wearing it, or they all lose access at once. And in the code, you set an environment variable, AZURE CLIENT ID, to its client ID, so DefaultAzureCredential knows which badge to wear."),
            ivy("Mm. And once the backend has the key? Does it ask the vault every time?"),
            nova("No. That's slow, and at busy times the vault throttles you, tells you to slow down. So keep the key in memory for a few minutes, five, say. Then read again, without a version number."),
            ivy("Version number? Secrets have versions?"),
            nova("Every write makes a new secret version, and the old ones stay. Ask without a version, and you get the latest. Pin a version, and you're stuck with that one forever, so a new key never reaches you."),
            ivy("And when the model refuses the old key?"),
            nova("That's your signal. Drop the copy in memory, and read the vault again. [pause] The newest version will be waiting."),
            ivy("And why would the key change?"),
            nova("Rotation. Replacing it on a schedule, so a leaked one stops working. Keys for encryption, which scramble data, have a key rotation policy; the vault makes new ones by itself. But an API key comes from the provider, so it can't rotate by itself."),
            ivy("Then how does an API key change, with nobody awake?"),
            nova("You give the secret an expiry date. About 30 days before, Key Vault sends SecretNearExpiry to Event Grid, Azure's notice board that pushes, and a small function, code that wakes when something happens, writes a new version."),
            ivy("And if I'd rather write no code at all?"),
            nova("Then a Key Vault reference. App Service, Azure's home for web apps, and Functions can hold a setting that says, fetch this from the vault. It refreshes within a day, as long as there's no version pinned in it."),
            ivy("Now, the part that keeps me up. Someone deletes the key. Late at night, tired fingers."),
            nova("Then soft delete catches it. A recycle bin. The deleted secret waits there for a retention period you chose when you made the vault, from 7 to 90 days, 90 by default. You can recover it. [pause] And while it's in the bin, its name is taken."),
            ivy("And can someone empty the bin?"),
            nova("That's a purge. It erases the secret from the bin, for good, right away. It needs its own permission, such as the Key Vault Purge Operator role. Anyone holding it could do it. Careless, or with bad intentions."),
            ivy("So it could still vanish tonight."),
            nova("Not with purge protection. [long pause] With it on, nothing in the bin can be purged before its retention ends. Not by you, not by an administrator, not by anyone. And once it's on, it can never be turned off. So nobody can quietly take the safety net away."),
            ivy("Where does all of this stop working, Nova?"),
            nova("On the phone. It never gets a managed identity and never holds a secret; it calls the backend. And on your laptop, there's no badge either; DefaultAzureCredential uses your own sign-in, after az login. The Event Grid and Functions episodes are your next late nights, for the rotation side."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. Shared by several apps, or created first, means user-assigned. An app that only reads gets Key Vault Secrets User. A secret that rotates itself needs an expiry date, SecretNearExpiry and a function. Rotation that never arrives means a pinned version. And nobody may purge early means purge protection."),
            ivy("And something to try today, before I sleep?"),
            nova("Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. [pause] Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            ivy("Secrets kept safe, even from tired fingers. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-keyvault",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about the secrets we keep."),
            rafe("I don't keep secrets from you. Well. Not many."),
            nova("[laugh] Key Vault. A locked safe in Azure for secrets: API keys, passwords, connection strings, which are database addresses with a password inside. Only the ones you choose can open it."),
            rafe("API key being a long password that proves you may call a service. Like the Two Tracks Coach, the study assistant in the Android app, Python backend on Azure. It calls an AI model with one."),
            nova("And here's my first question, exam style. Where should the Android app keep that key?"),
            rafe("Nowhere. Anything in the app can be pulled out of it. The phone calls the backend, and only the backend reads the key from the vault."),
            nova("Right answer. Some things you just don't carry around. [kiss]"),
            rafe("Mm. So how does the backend get in? It can't have a password either."),
            nova("A managed identity. A badge Azure makes for the app. Under the picture, it's a real account in Microsoft Entra ID, Azure's sign-in system. Azure answers the app's request for a token, a signed pass that fades within hours, on a private address nobody outside can reach. In Python, one line: DefaultAzureCredential, which finds the right sign-in by itself."),
            rafe("A token that doesn't last the night. And the vault lets it in?"),
            nova("If it has the right role. That's RBAC, role-based access control: permissions as job titles. So, darling. The Coach API only reads the key. Which role?"),
            rafe("Key Vault Administrator. Go big."),
            nova("[sigh] No kiss for that one. Key Vault Secrets User: read values, nothing more. Officer creates and changes secrets. Administrator is for a person, rarely. It's all about knowing your role, Rafe."),
            rafe("I know my role. Now two kinds of badge. Tell me about the one that's faithful."),
            nova("A system-assigned identity. Switched on inside one app, belongs only to that app, and deleted when the app is. It can't be shared, and it can't be given a role before its app exists. Faithful, but a little possessive."),
            rafe("And the one that gets around?"),
            nova("A user-assigned identity. A resource of its own, created before any app, with its role granted once. Then you attach it to several apps."),
            rafe("Then my question. The Coach API and a nightly function must read the same vault, and the identity must have its role before either is deployed. Which identity?"),
            nova("One user-assigned identity on both. Grant Key Vault Secrets User once, attach it to the API and the nightly function. Delete one app, and the identity stays with its role. It outlives any one app. Set AZURE CLIENT ID, an environment variable the app reads at start, to its client ID, so DefaultAzureCredential knows which badge."),
            rafe("Right answer. One badge, shared, and faithful to everyone. [kiss]"),
            nova("Just never delete it while apps still wear it. Now, versions. Every write makes a new secret version, and the old ones stay. Read without a version, and you get the latest. So. The key was rotated, and the app still uses the old one. Why?"),
            rafe("It pinned a version. Stuck on an old flame. And it should cache the key for a few minutes, not call the vault on every request and get throttled, told to slow down."),
            nova("Right answer. Let the old ones go. [kiss]"),
            rafe("And if I'd rather not write any code to read it?"),
            nova("Then a Key Vault reference. App Service, Azure's home for web apps, and Functions take a setting that says, fetch this from the vault, using the app's identity. It refreshes within a day. Low effort, high reward. Like you."),
            rafe("My question, then. The secret OpenAiKey must be replaced every 90 days, with no person involved. What do you build?"),
            nova("A key rotation policy on the secret."),
            rafe("Close, but no kiss. Rotation policies are for encryption keys, which scramble data, and which the vault can make itself. An API key comes from the provider. So, an expiry date. About 30 days before, Key Vault sends SecretNearExpiry to Event Grid, the notice board that pushes, and a function, code that wakes when something happens, writes a new version."),
            nova("Mm. Fair. Then here's mine. Someone deletes the secret called OpenAiKey by mistake. Soft delete is on, with 90 days. What's true?"),
            rafe("Soft delete is a recycle bin. The secret waits there for its retention period, 7 to 90 days, and you can recover it. While it's there, its name is taken. And someone with the purge permission could erase it for good, right away."),
            nova("Right answer. Nothing's over until it's really over. [kiss]"),
            rafe("Unless we stop that purge. Your turn to tell me, darling. Purge protection."),
            nova("Off by default. With it on, nothing in the bin can be purged before its retention ends. Not by us, not by an administrator, not by anyone. And once it's on, it can never be turned off. A commitment you can't take back. And the retention period itself is fixed when the vault is made."),
            rafe("Like us. Who can purge, normally?"),
            nova("Anyone with the purge permission, like the Key Vault Purge Operator role. Which is why the protection matters: a careless click, or someone with bad intentions, can't erase anything early."),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("At the phone. No identity, no secret, ever. And on your laptop there's no badge either: DefaultAzureCredential uses your own sign-in, after az login, the Azure command line's sign-in. The Event Grid episode covers the rotation side, and the Functions episode the code that does it."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Shared by several apps, or created first: user-assigned. Only reads: Key Vault Secrets User. Rotates itself: expiry date, SecretNearExpiry and a function. Rotation never arrives: a pinned version. Nobody may purge early: purge protection, which can't be turned off."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            nova("And you're the one secret I'll never purge. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "The phone calls the Coach API; the API reads the key with its managed identity. Near expiry, Event Grid wakes a function that writes a new version.",
        nodes = listOf(
            DNode("phone", "Android app", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("api", "Coach API", 0.45f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "managed identity"),
            DNode("kv", "Key Vault", 0.82f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "secret: OpenAiKey"),
            DNode("eg", "Event Grid", 0.82f, 0.56f, w = 0.3f, h = 0.2f, tone = 2, sub = "SecretNearExpiry"),
            DNode("fn", "Rotation function", 0.45f, 0.56f, w = 0.3f, h = 0.2f, tone = 1, sub = "new version"),
            DNode("note", "The phone never holds a secret", 0.5f, 0.87f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("phone", "api", "HTTPS"),
            DEdge("api", "kv", "get_secret"),
            DEdge("kv", "eg", "near expiry"),
            DEdge("eg", "fn", "trigger"),
            DEdge("fn", "kv", "set_secret", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun roles() = DiagramSpec(
        "Each identity gets the smallest role that works: Secrets User reads, Secrets Officer writes, Administrator is for a person, rarely.",
        nodes = listOf(
            DNode("api", "Coach API", 0.24f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "reads only"),
            DNode("fn", "Rotation function", 0.24f, 0.5f, w = 0.4f, h = 0.2f, tone = 1, sub = "writes versions"),
            DNode("person", "A person", 0.24f, 0.85f, w = 0.4f, h = 0.2f, kind = NodeKind.USER),
            DNode("user", "Secrets User", 0.76f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "read values"),
            DNode("officer", "Secrets Officer", 0.76f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "create, update, delete"),
            DNode("admin", "Administrator", 0.76f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "everything, rarely"),
        ),
        edges = listOf(
            DEdge("api", "user"),
            DEdge("fn", "officer"),
            DEdge("person", "admin"),
        ),
        aspect = 0.8f,
    )

    private fun identities() = DiagramSpec(
        "One user-assigned identity, created first and granted Secrets User once, is attached to both apps; a system-assigned one lives and dies with its app.",
        nodes = listOf(
            DNode("ua", "User-assigned identity", 0.5f, 0.14f, w = 0.6f, h = 0.2f, sub = "a resource of its own"),
            DNode("kv", "Key Vault", 0.5f, 0.46f, w = 0.4f, h = 0.18f, kind = NodeKind.CYLINDER),
            DNode("api", "Coach API", 0.18f, 0.8f, w = 0.3f, h = 0.2f, tone = 1, sub = "Container Apps"),
            DNode("fn", "Nightly job", 0.5f, 0.8f, w = 0.3f, h = 0.2f, tone = 1, sub = "a function"),
            DNode("sys", "System-assigned", 0.82f, 0.8f, w = 0.3f, h = 0.2f, tone = 2, sub = "dies with its app"),
        ),
        edges = listOf(
            DEdge("ua", "kv", "Secrets User, once"),
            DEdge("ua", "api", "attached"),
            DEdge("ua", "fn", "attached"),
        ),
        aspect = 0.8f,
    )

    private fun versions() = DiagramSpec(
        "Every write is a new version; reading without one gets the latest, cached for a few minutes. A pinned version never sees rotation.",
        nodes = listOf(
            DNode("v1", "Version 1", 0.18f, 0.2f, w = 0.28f, h = 0.2f, tone = 2, sub = "still kept"),
            DNode("v2", "Version 2", 0.5f, 0.2f, w = 0.28f, h = 0.2f, tone = 2, sub = "still kept"),
            DNode("v3", "Version 3", 0.82f, 0.2f, w = 0.28f, h = 0.2f, sub = "the latest"),
            DNode("app", "Coach API", 0.25f, 0.58f, w = 0.42f, h = 0.2f, tone = 1, sub = "no version: latest"),
            DNode("cache", "Memory cache", 0.75f, 0.58f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "about 5 minutes"),
            DNode("pin", "Pinned: rotation never arrives", 0.5f, 0.88f, w = 0.84f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("v3", "app", "reads latest"),
            DEdge("app", "cache"),
            DEdge("v1", "pin", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun rotation() = DiagramSpec(
        "A secret with an expiry date raises SecretNearExpiry about 30 days before; a function gets a new key and writes a new version. Key rotation policies are for keys.",
        nodes = listOf(
            DNode("kv", "Key Vault", 0.25f, 0.16f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "expiry date set"),
            DNode("eg", "Event Grid", 0.75f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "SecretNearExpiry"),
            DNode("prov", "AI provider", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "issues a new key"),
            DNode("fn", "Rotation function", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "Secrets Officer"),
            DNode("policy", "Key rotation policy", 0.5f, 0.85f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "for encryption keys only"),
        ),
        edges = listOf(
            DEdge("kv", "eg", "30 days before"),
            DEdge("eg", "fn", "trigger"),
            DEdge("fn", "prov", "new key"),
            DEdge("fn", "kv", "new version", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun recycle() = DiagramSpec(
        "A deleted secret waits soft-deleted for 7 to 90 days, where it can be recovered or, without purge protection, purged for good.",
        nodes = listOf(
            DNode("active", "Active secret", 0.15f, 0.3f, w = 0.26f, h = 0.26f, tone = 1),
            DNode("deleted", "Soft-deleted", 0.5f, 0.3f, w = 0.3f, h = 0.26f, sub = "kept 7 to 90 days"),
            DNode("gone", "Purged for good", 0.85f, 0.3f, w = 0.26f, h = 0.26f, tone = 2),
            DNode("note", "Purge protection", 0.5f, 0.76f, w = 0.9f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "no early purge, for anyone; never off"),
        ),
        edges = listOf(
            DEdge("active", "deleted", "delete / recover", bidirectional = true),
            DEdge("deleted", "gone", "purge"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "DefaultAzureCredential uses az login on a laptop and the managed identity in Azure; the phone never has either, or any secret.",
        nodes = listOf(
            DNode("cred", "DefaultAzureCredential", 0.5f, 0.15f, w = 0.8f, h = 0.18f, kind = NodeKind.PILL),
            DNode("laptop", "Your laptop", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "az login"),
            DNode("azure", "In Azure", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "managed identity"),
            DNode("phone", "Phone: no identity, no secret", 0.5f, 0.85f, w = 0.84f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("cred", "laptop"),
            DEdge("cred", "azure"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Key Vault questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("shared", "Shared, or created first", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "user-assigned"),
            DNode("reads", "Only reads secrets", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Secrets User"),
            DNode("rotate", "Rotates itself", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "SecretNearExpiry"),
            DNode("pinned", "Rotation never arrives", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pinned version"),
            DNode("purge", "Nobody purges early", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "purge protection"),
            DNode("phone", "Key in the app?", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "no: the backend"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("shared", "reads", "rotate", "pinned", "purge", "phone")
    private val allRoles = setOf("api", "fn", "person", "user", "officer", "admin")
    private val readCode = BoardCard("Python", "SecretClient(vault_url, DefaultAzureCredential()).get_secret(\"OpenAiKey\").value", code = true)
    private val today = BoardCard("Today", "An RBAC vault, two versions, a Python read, then delete and recover.")
    private val protection = BoardCard("Purge protection", "No purge before retention ends, for anyone. It can't be turned off.")

    private fun classicBoard() = PodcastBoard(
        "ai-keyvault",
        listOf(
            // 0: "Hi, and welcome back. Today is Key Vault, for AI apps..."
            Scene(
                0, "Where does the key live?", flow(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("The puzzle", "The AI model wants an API key. Where does it live?")),
                    Beat(2, show = setOf("kv", "note"), light = setOf("api", "kv"), pulse = "api" to "kv", card = BoardCard("Never on the phone", "The phone calls the backend; only the backend reads the key.")),
                    Beat(4, light = setOf("kv"), card = BoardCard("Key Vault", "A locked safe for secrets: API keys, passwords, connection strings.")),
                    Beat(6, light = setOf("api"), card = BoardCard("Managed identity", "A badge Azure makes and looks after: an account in Entra ID.")),
                ),
            ),
            // 7: "And how does the vault decide whether the badge is allowed in?"
            Scene(
                7, "The smallest role", roles(),
                beats = listOf(
                    Beat(8, show = allRoles, light = setOf("api", "user"), pulse = "api" to "user", card = BoardCard("RBAC", "Roles like job titles. The Coach API: Key Vault Secrets User.")),
                ),
            ),
            // 9: "So the Coach backend gets Secrets User. Now, two kinds of badge."
            Scene(
                9, "Two kinds of badge", identities(),
                beats = listOf(
                    Beat(10, show = setOf("sys"), light = setOf("sys"), card = BoardCard("System-assigned", "Inside one app, deleted with it. Can't be shared or granted early.")),
                    Beat(12, show = setOf("ua", "kv"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "A resource of its own: created first, its role granted once.")),
                    Beat(14, show = setOf("api", "fn"), light = setOf("api", "fn"), pulse = "ua" to "api", card = BoardCard("One badge, several apps", "The Coach API and the nightly job share it. A new app: just attach.")),
                    Beat(16, light = setOf("ua"), card = BoardCard("It outlives any one app", "Delete an app; the identity keeps its role. Don't delete it in use.")),
                ),
            ),
            // 17: "And in the Python code? How does it show the badge?"
            Scene(
                17, "Showing the badge", limits(),
                beats = listOf(
                    Beat(18, show = setOf("cred", "laptop", "azure"), light = setOf("cred"), pulse = "cred" to "azure", card = BoardCard("DefaultAzureCredential", "az login on your laptop, the managed identity in Azure. Same code.")),
                    Beat(20, light = setOf("azure"), card = BoardCard("AZURE_CLIENT_ID", "Set to the user-assigned identity's client ID: which badge to wear.")),
                ),
            ),
            // 21: "Okay. The backend reads the key. Every time someone asks a question?"
            Scene(
                21, "Versions and caching", versions(),
                beats = listOf(
                    Beat(22, show = setOf("app", "cache"), light = setOf("cache"), pulse = "app" to "cache", card = BoardCard("Cache it", "Calling the vault every request is slow and gets throttled.")),
                    Beat(24, show = setOf("v1", "v2", "v3", "pin"), light = setOf("v3", "pin"), pulse = "v3" to "app", card = BoardCard("Secret versions", "Each write is a new version. No version: the latest. Pinned: stuck.")),
                ),
            ),
            // 25: "So changing the key on purpose is called?"
            Scene(
                25, "Rotation", rotation(),
                beats = listOf(
                    Beat(26, show = setOf("kv", "policy"), light = setOf("policy"), card = BoardCard("Key rotation policy", "Encryption keys rotate themselves. API keys come from the provider.")),
                    Beat(28, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = BoardCard("SecretNearExpiry", "About 30 days before expiry; a function writes a new version.")),
                    Beat(30, light = setOf("kv"), pulse = "fn" to "kv", card = BoardCard("Key Vault reference", "A setting that fetches from the vault, refreshed within 24 hours.")),
                ),
            ),
            // 31: "Last scary thing. What if someone deletes the key by accident?"
            Scene(
                31, "The recycle bin", recycle(),
                beats = listOf(
                    Beat(32, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Soft delete", "Kept 7 to 90 days, 90 by default, set at creation. Always on.")),
                    Beat(34, show = setOf("gone"), light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Purge", "Erased for good, now. Its own permission. The name is taken till then.")),
                    Beat(36, show = setOf("note"), light = setOf("note"), card = protection),
                ),
            ),
            // 37: "So what do I remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("shared", "reads", "rotate", "pinned", "purge"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: identity, role, rotation, purge.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-keyvault--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Key That Walked Out..."
            Scene(
                0, "The Key That Walked Out", flow(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), light = setOf("phone"), card = BoardCard("The case", "QuizQuest's AI bill was ten times its usual size.")),
                    Beat(2, card = BoardCard("Clue one", "The API key was inside the Android app, lightly scrambled.")),
                    Beat(4, show = setOf("api", "kv", "note"), light = setOf("note"), pulse = "phone" to "api", card = BoardCard("Wrong suspect", "Hiding it better won't help. The phone never holds the key.")),
                    Beat(6, light = setOf("kv"), pulse = "api" to "kv", card = BoardCard("Key Vault", "A locked safe for secrets. Only those you choose can open it.")),
                    Beat(8, light = setOf("api"), card = BoardCard("Managed identity", "An account in Entra ID that gets short-lived tokens.")),
                    Beat(10, card = readCode),
                ),
            ),
            // 11: "Role. As in a job title?"
            Scene(
                11, "Clue two", roles(),
                beats = listOf(
                    Beat(12, show = setOf("api", "admin"), light = setOf("admin"), card = BoardCard("Key Vault Administrator", "Everything, on every secret. Far too much for an app.")),
                    Beat(14, show = setOf("user", "fn", "officer", "person"), light = setOf("user", "officer"), pulse = "api" to "user", card = BoardCard("The smallest role", "Reads: Secrets User. Writes: Secrets Officer. Admin: a person.")),
                ),
            ),
            // 15: "Second witness?"
            Scene(
                15, "The second witness", identities(),
                beats = listOf(
                    Beat(16, show = setOf("fn"), light = setOf("fn"), card = BoardCard("Refused at the door", "The nightly job's first run was refused by the vault.")),
                    Beat(18, show = setOf("sys"), light = setOf("sys"), card = BoardCard("System-assigned", "Born with its app, so its role couldn't be granted in advance.")),
                    Beat(20, show = setOf("ua", "kv", "api"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Created first, granted once, attached to both. Outlives any app.")),
                    Beat(22, light = setOf("api", "fn"), pulse = "ua" to "api", card = BoardCard("AZURE_CLIENT_ID", "Tells DefaultAzureCredential which badge to wear.")),
                ),
            ),
            // 23: "Third witness. I can feel it."
            Scene(
                23, "The third witness", versions(),
                beats = listOf(
                    Beat(24, show = setOf("v1", "v2", "v3"), light = setOf("v3"), card = BoardCard("A new version", "Every write adds a version; the old ones stay.")),
                    Beat(26, show = setOf("app", "pin", "cache"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Pinned", "The backend named a version, so it read the dead key forever.")),
                    Beat(28, card = BoardCard("SecretNearExpiry", "An expiry date, Event Grid, and a function that writes a version.")),
                    Beat(30, light = setOf("app"), pulse = "v3" to "app", card = BoardCard("Key Vault reference", "A setting that fetches from the vault. No version in it.")),
                ),
            ),
            // 31: "Last witness. Who's left?"
            Scene(
                31, "The last witness", recycle(),
                beats = listOf(
                    Beat(32, show = setOf("active", "deleted", "gone"), light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Deleted, then purged", "A purge erases it from the bin for good, right now.")),
                    Beat(34, show = setOf("note"), light = setOf("note"), card = protection),
                    Beat(36, light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("The name is taken", "While soft-deleted: recover it or wait. Retention is fixed at creation.")),
                ),
            ),
            // 37: "So, the case summary for the exam?"
            Scene(
                37, "Case closed", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("phone", "reads", "shared", "pinned", "purge"), card = BoardCard("Listen for the phrase", "Phone, role, identity, version, purge: one answer each.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-keyvault--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "The Coach team's week", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api", "kv"), light = setOf("kv"), card = BoardCard("Key Vault", "A locked safe in Azure for every secret the Coach has.")),
                    Beat(4, show = setOf("phone"), light = setOf("phone"), card = BoardCard("Monday: Tom", "The AI key inside the Android app. Hidden, he said.")),
                    Beat(6, show = setOf("note"), light = setOf("note"), pulse = "phone" to "api", card = BoardCard("Never on the phone", "The phone calls the backend; only the backend reads the key.")),
                    Beat(8, light = setOf("api"), pulse = "api" to "kv", card = BoardCard("Managed identity", "A badge: an Entra ID account with short-lived tokens.")),
                ),
            ),
            // 9: "Tuesday?"
            Scene(
                9, "Tuesday: Dana", roles(),
                beats = listOf(
                    Beat(10, show = setOf("api", "admin"), light = setOf("admin"), card = BoardCard("Administrator, so it works", "Everything, on every secret, for an app that only reads.")),
                    Beat(12, show = setOf("user", "fn", "officer", "person"), light = setOf("user"), pulse = "api" to "user", card = BoardCard("The smallest role", "Reads: Secrets User. Writes: Secrets Officer.")),
                ),
            ),
            // 13: "Wednesday?"
            Scene(
                13, "Wednesday: Marcus", identities(),
                beats = listOf(
                    Beat(14, show = setOf("fn", "sys"), light = setOf("sys"), card = BoardCard("Refused on night one", "A system-assigned badge didn't exist until its app did.")),
                    Beat(18, show = setOf("ua", "kv", "api"), light = setOf("ua"), pulse = "ua" to "api", card = BoardCard("User-assigned", "Created first, granted once, attached to both. Outlives any app.")),
                    Beat(20, light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("AZURE_CLIENT_ID", "Set to the identity's client ID, for DefaultAzureCredential.")),
                ),
            ),
            // 21: "Thursday, I'm guessing, the intern."
            Scene(
                21, "Thursday: the intern", versions(),
                beats = listOf(
                    Beat(22, show = setOf("app"), light = setOf("app"), card = BoardCard("Thousands of reads an hour", "And at the busy time, the vault throttled them.")),
                    Beat(24, show = setOf("cache", "v1", "v2", "v3"), light = setOf("cache", "v3"), pulse = "app" to "cache", card = BoardCard("Cache, then read the latest", "About five minutes in memory; no version, so rotation arrives.")),
                    Beat(26, card = BoardCard("SecretNearExpiry", "Expiry date, Event Grid, and a function that writes a new version.")),
                    Beat(28, show = setOf("pin"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Key Vault reference", "A setting that fetches from the vault. Never pin a version.")),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", recycle(),
                beats = listOf(
                    Beat(30, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("The name was taken", "OpenAiKey sat in the bin, so a new one couldn't use its name.")),
                    Beat(32, card = BoardCard("Soft delete", "A recycle bin: 7 to 90 days, always on for new vaults.")),
                    Beat(34, show = setOf("gone", "note"), light = setOf("note"), card = protection),
                    Beat(36, light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Purge", "Erases it for good. It needs its own permission.")),
                ),
            ),
            // 37: "So where does all this stop working?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, show = setOf("cred", "laptop", "azure", "phone"), light = setOf("phone", "laptop"), pulse = "cred" to "laptop", card = BoardCard("Phone and laptop", "No secret on the phone. On a laptop, az login instead.")),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("shared", "reads", "rotate", "pinned", "purge"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. One colleague, one lesson.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-keyvault--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Keeping secrets", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Keeping secrets.")),
                    Beat(2, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("The API key", "The model wants a key. It must never sit in the phone.")),
                    Beat(4, show = setOf("kv", "note"), light = setOf("kv"), pulse = "api" to "kv", card = BoardCard("Key Vault", "A locked safe in the cloud for secrets.")),
                    Beat(6, light = setOf("api"), card = BoardCard("Managed identity", "A badge Azure makes: an account in Entra ID, no password to lose.")),
                    Beat(8, card = BoardCard("A token, not a password", "Asked for on a private address; signed, and gone within hours.")),
                ),
            ),
            // 9: "Role. Tell me about roles."
            Scene(
                9, "Roles", roles(),
                beats = listOf(
                    Beat(10, show = allRoles, light = setOf("api", "user"), pulse = "api" to "user", card = BoardCard("RBAC", "Secrets User reads; Officer writes; Administrator is for a person.")),
                ),
            ),
            // 11: "And I hear there are two kinds of badge."
            Scene(
                11, "Two kinds of badge", identities(),
                beats = listOf(
                    Beat(12, show = setOf("sys"), light = setOf("sys"), card = BoardCard("System-assigned", "Faithful to one app, deleted with it. Can't be shared.")),
                    Beat(14, show = setOf("ua", "kv", "api", "fn"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Its own resource, created first; Secrets User granted once.")),
                    Beat(16, light = setOf("api", "fn"), pulse = "ua" to "fn", card = BoardCard("It outlives any app", "Delete an app; the badge stays. AZURE_CLIENT_ID names it.")),
                ),
            ),
            // 17: "Mm. And once the backend has the key?..."
            Scene(
                17, "Versions", versions(),
                beats = listOf(
                    Beat(18, show = setOf("app", "cache"), light = setOf("cache"), pulse = "app" to "cache", card = BoardCard("Cache it", "A few minutes in memory, or the vault throttles you.")),
                    Beat(20, show = setOf("v1", "v2", "v3", "pin"), light = setOf("v3"), pulse = "v3" to "app", card = BoardCard("Secret versions", "No version: the latest. Pinned: stuck forever.")),
                    Beat(22, light = setOf("app"), card = BoardCard("A refused key", "Drop the cached copy and read the vault again.")),
                ),
            ),
            // 23: "And why would the key change?"
            Scene(
                23, "Rotation", rotation(),
                beats = listOf(
                    Beat(24, show = setOf("kv", "policy"), light = setOf("policy"), card = BoardCard("Key rotation policy", "Encryption keys rotate themselves. API keys come from the provider.")),
                    Beat(26, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = BoardCard("SecretNearExpiry", "About 30 days before expiry; a function writes a new version.")),
                    Beat(28, light = setOf("kv"), pulse = "fn" to "kv", card = BoardCard("Key Vault reference", "A setting that fetches from the vault, refreshed within a day.")),
                ),
            ),
            // 29: "Now, the part that keeps me up. Someone deletes the key."
            Scene(
                29, "Tired fingers", recycle(),
                beats = listOf(
                    Beat(30, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Soft delete", "A recycle bin: 7 to 90 days, chosen at creation. The name is taken.")),
                    Beat(32, show = setOf("gone"), light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Purge", "Erased for good, now. It needs its own permission.")),
                    Beat(34, show = setOf("note"), light = setOf("note"), card = protection),
                ),
            ),
            // 35: "Where does all of this stop working, Nova?"
            Scene(
                35, "Where it stops", limits(),
                beats = listOf(
                    Beat(36, show = setOf("cred", "laptop", "azure", "phone"), light = setOf("phone", "laptop"), pulse = "cred" to "laptop", card = BoardCard("Phone and laptop", "No secret on the phone. On a laptop, az login instead.")),
                ),
            ),
            // 37: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("shared", "reads", "rotate", "pinned", "purge"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-keyvault--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "The secrets we keep: Key Vault.")),
                    Beat(2, show = setOf("kv"), light = setOf("kv"), card = BoardCard("Key Vault", "A locked safe in Azure for secrets.")),
                    Beat(3, show = setOf("api", "phone"), light = setOf("api"), pulse = "api" to "kv", card = BoardCard("The Coach's API key", "A long password that proves the backend may call the AI model.")),
                    Beat(4, light = setOf("phone"), card = BoardCard("Quiz: the Android app", "Where should the Android app keep the API key?")),
                    Beat(5, show = setOf("note"), light = setOf("note"), pulse = "phone" to "api", card = BoardCard("Right answer", "Nowhere. The phone calls the backend, which reads the vault.")),
                    Beat(8, light = setOf("api"), card = BoardCard("Managed identity", "A badge Azure makes; tokens that fade within hours.")),
                ),
            ),
            // 9: "A token that doesn't last the night..."
            Scene(
                9, "Knowing your role", roles(),
                beats = listOf(
                    Beat(10, show = setOf("api"), light = setOf("api"), card = BoardCard("Quiz: the Coach's role", "The Coach API only reads the key. Which role?")),
                    Beat(11, show = setOf("admin"), light = setOf("admin"), card = BoardCard("Administrator? Go big?", "Everything, on every secret.")),
                    Beat(12, show = setOf("user", "fn", "officer", "person"), light = setOf("user"), pulse = "api" to "user", card = BoardCard("No kiss: Secrets User", "Read values, nothing more. Administrator is for a person.")),
                ),
            ),
            // 13: "I know my role. Now two kinds of badge..."
            Scene(
                13, "Faithful or shared", identities(),
                beats = listOf(
                    Beat(14, show = setOf("sys"), light = setOf("sys"), card = BoardCard("System-assigned", "One app, deleted with it. Can't be shared or granted early.")),
                    Beat(16, show = setOf("ua", "kv"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Its own resource, created first, its role granted once.")),
                    Beat(17, show = setOf("api", "fn"), light = setOf("api", "fn"), card = BoardCard("Quiz: two apps, role first", "API and nightly job, role granted before deploying. Which identity?")),
                    Beat(18, light = setOf("ua"), pulse = "ua" to "api", card = BoardCard("Right answer", "One user-assigned identity on both. It outlives any one app.")),
                ),
            ),
            // 20: "Just never delete it while apps still wear it. Now, versions..."
            Scene(
                20, "Old flames", versions(),
                beats = listOf(
                    Beat(20, show = setOf("v1", "v2", "v3", "app"), light = setOf("v3"), pulse = "v3" to "app", card = BoardCard("Quiz: rotated, still old", "The key was rotated; the app still uses the old one. Why?")),
                    Beat(21, show = setOf("pin", "cache"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Right answer", "A pinned version. Read the latest, and cache a few minutes.")),
                    Beat(24, light = setOf("app"), card = BoardCard("Key Vault reference", "A setting that fetches from the vault, refreshed within a day.")),
                ),
            ),
            // 25: "My question, then. The secret OpenAiKey must be replaced every 90 days..."
            Scene(
                25, "Every 90 days", rotation(),
                beats = listOf(
                    Beat(25, show = setOf("kv"), light = setOf("kv"), card = BoardCard("Quiz: every 90 days", "Replace OpenAiKey every 90 days, no person involved. Build what?")),
                    Beat(26, show = setOf("policy"), light = setOf("policy"), card = BoardCard("A rotation policy?", "Those are for encryption keys. No kiss.")),
                    Beat(27, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = BoardCard("SecretNearExpiry", "Expiry date, Event Grid, and a function that writes a version.")),
                ),
            ),
            // 28: "Mm. Fair. Then here's mine. Someone deletes the secret..."
            Scene(
                28, "Not over till it's over", recycle(),
                beats = listOf(
                    Beat(28, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Quiz: deleted by mistake", "Soft delete is on, with 90 days. What's true?")),
                    Beat(29, show = setOf("gone"), light = setOf("deleted", "gone"), card = BoardCard("Right answer", "Recoverable for 7 to 90 days; the name is taken; a purge erases it.")),
                    Beat(32, show = setOf("note"), light = setOf("note"), card = protection),
                    Beat(34, light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Who can purge?", "Anyone with the purge permission, unless protection is on.")),
                ),
            ),
            // 35: "And where does it all stop working? Every romance has its limits."
            Scene(
                35, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(36, show = setOf("cred", "laptop", "azure", "phone"), light = setOf("phone", "laptop"), pulse = "cred" to "laptop", card = BoardCard("Phone and laptop", "No secret on the phone. On a laptop, az login instead.")),
                ),
            ),
            // 37: "Last question, for the exam. What do we remember?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("shared", "reads", "rotate", "pinned", "purge"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
