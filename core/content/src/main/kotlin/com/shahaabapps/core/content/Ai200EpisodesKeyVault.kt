package com.shahaabapps.core.content

/** Key Vault for AI apps: secrets, rotation and retrieval: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesKeyVault {
    private val classic = Podcast(
        "ai-keyvault",
        listOf(
            maya("Hi, and welcome back. Zaiqa is running: the Zaiqa API, the .NET service the phone talks to, lives in Container Apps, orders sit in Azure SQL, and the fast, huge things sit in Cosmos. Today, the one real secret in the whole app."),
            leo("One? I would have guessed dozens. Database passwords, the registry, Cosmos keys."),
            maya("That is the point of today. Picture a shelf of passwords, and then notice most of them are gone. The API pulls its image with a managed identity, the badge Azure gives your app, and reads Cosmos with the same badge."),
            leo("So what is left that actually needs a password?"),
            maya("The payment provider. Zaiqa takes money through a payment company, and they hand us an API key: a long password that proves the caller is us. They are not in Azure, so there is no badge of ours they would recognise."),
            leo("And that key goes where?"),
            maya("Azure Key Vault. A locked safe in Azure, holding three kinds of thing: secrets, keys and certificates. Ours is a secret."),
            leo("What is the difference? They all sound like secrets to me."),
            maya("A secret is any value you put in and read back out: a password, an API key, a connection string. A key is a cryptographic one that never comes out; you send data to the vault and it signs or wraps it for you."),
            leo("And a certificate?"),
            maya("A certificate is what proves a website is who it says it is. The vault stores it and can renew it before it runs out. For example, a site that goes dark because nobody remembered to renew it by hand."),
            leo("Right. So the Zaiqa API reads the payment key from the vault. With what password?"),
            maya("None. The same badge it already uses for the registry and for Cosmos. That is the shape of the whole episode: a managed identity means most things need no secret at all, so the vault holds only what genuinely cannot be an identity."),
            leo("And who checks that badge at the vault's door?"),
            maya("Microsoft Entra ID, which is Azure's sign-in system. Think of the reception desk of a big office, keeping the list of everyone allowed in, people and apps. The badge is a real account on that list."),
            leo("And what does the app actually hand over?"),
            maya("A token: a short-lived signed pass, like a festival wristband. Azure started our container, so it hands tokens to it on a private local address nothing outside can reach."),
            leo("Role. As in a job title?"),
            maya("Exactly. That is RBAC, which is role-based access control: permissions handed out as roles. The Zaiqa API only reads, so it gets Key Vault Secrets User. Secrets Officer creates and changes. Administrator does everything, and that is for a person, rarely."),
            leo("I would have given it administrator and moved on. [chuckle]"),
            maya("Everyone does, once. Then somebody gets into the API and empties the safe. Always the smallest role that works."),
            leo("You said RBAC as though there were another way."),
            maya("There is an older one: access policies, a permission list kept on the vault itself, saying which identity may do which operation. They still work, but they sit apart from every other permission in Azure."),
            leo("So a vault we make now gets RBAC."),
            maya("One flag when you create it, and your role assignments behave the way they do everywhere else."),
            leo("Back to the badge. Is there more than one kind?"),
            maya("Two. A system-assigned identity is switched on inside one app and dies with it: it cannot be shared, and it cannot be given a role before its app exists. A user-assigned identity is a resource of its own."),
            leo("And Zaiqa uses?"),
            maya("The user-assigned one, because the API and the order worker both charge cards, and both need that key. Create it first, grant Key Vault Secrets User once, attach it to both. Delete one app and the identity stays, role and all."),
            leo("And how does the code know which badge to wear, if two are attached?"),
            maya("You set an environment variable, a named setting the app reads when it starts, called AZURE CLIENT ID, to the identity's own number. DefaultAzureCredential reads it."),
            leo("Hold on. Does the API call the vault on every payment?"),
            maya("It does not call the vault at all. Container Apps can hold a secret that points at the vault, and a setting that points at the secret. The platform fetches the value with our identity. App Service and Functions do the same, and it is called a Key Vault reference."),
            leo("Why is that better than three lines of code?"),
            maya("Nothing to cache, nothing to retry, and no vault call while a diner waits to pay. Say you're building it yourself instead: then keep the value in memory for a few minutes, or the vault throttles you, which means tells you to slow down."),
            leo("And when the payment company issues us a new key?"),
            maya("That is rotation: replacing a secret on a schedule, so a leaked value stops working. Every write stores a new secret version, and the old ones stay. Ask without a version and you get the latest. Name one and you are pinned to it."),
            leo("Pinned, meaning rotation never reaches me."),
            maya("Never. [pause] It is the most common Key Vault question there is. Our Container Apps secret has no version in it, so a new value arrives within about thirty minutes. App Service refreshes a versionless reference within twenty-four hours."),
            leo("Can the vault rotate the key by itself?"),
            maya("For its own cryptographic keys, yes: a key rotation policy makes new versions on a schedule, because the vault can generate those. It cannot invent a payment company's key."),
            leo("Then who does, with nobody awake?"),
            maya("You give the secret an expiry date. About thirty days before it, Key Vault raises SecretNearExpiry on Event Grid, Azure's notice board. A function, a small piece of code that runs when something happens, writes the new version."),
            leo("And the API picks it up because its reference has no version in it."),
            maya("Exactly. Nothing is ever pushed to the API. It reads the latest, always."),
            leo("Last worry. What if somebody deletes the payment secret at eight on a Friday?"),
            maya("Soft delete catches it. It is a recycle bin, always on for new vaults. A deleted secret waits there for the retention period: seven to ninety days, ninety by default, chosen when the vault is created and fixed after that."),
            leo("And while it waits, can I make a new one with the same name?"),
            maya("No. The name is taken until you recover it or the period ends."),
            leo("Can somebody empty the bin early?"),
            maya("That is a purge: it erases the secret for good, right now, and it needs its own permission, such as Key Vault Purge Operator. Unless purge protection is on. It is off by default, it blocks every early purge, even an administrator's, and once on it can never be turned off."),
            leo("One more. Our vault has an address on the internet, does it not?"),
            maya("By default, yes. So you add a private endpoint, which is a private address for the vault inside Zaiqa's own network, and then turn public access off. The Container Apps environment is joined to that network, so the API still reaches it."),
            leo("So what do I remember for the exam?"),
            maya("Five. Several apps share one identity, or it needs its role before it exists: user-assigned. An app that only reads: Key Vault Secrets User. A secret that must rotate itself: an expiry date, SecretNearExpiry and a function. Rotation never arrives: a pinned version. Nobody may purge early: purge protection."),
            leo("And one for the vault itself?"),
            maya("Reachable only from inside the network: a private endpoint with public access off. And a vault made now: RBAC, not access policies."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            leo("Most things need no secret; the safe keeps the one that does. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-keyvault",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Charge That Failed. Eight on a Friday, and every Zaiqa order that reached the payment step came back declined. Food cooked, riders waiting, nobody able to pay for anything."),
            leo("Detective Leo, reporting. Was the service even up?"),
            maya("Up and healthy. The Zaiqa API, the .NET service the phone talks to, was answering everything else: menus out of Azure SQL, the tracking feed out of Cosmos. Only the charge failed."),
            leo("Then the payment company was down."),
            maya("They were not, and their answer was plain: the key we sent was not valid. Zaiqa pays through a payment company, and they hand us an API key, a long password that proves the caller is us."),
            leo("And where do we keep that?"),
            maya("Azure Key Vault: a locked safe in Azure holding secrets, keys and certificates. Ours is a secret, which is any value you put in and read back out."),
            leo("And the other two?"),
            maya("A key is cryptographic and never comes out; you send data in and the vault signs it for you. A certificate proves a website is who it says it is, and the vault can renew it. A secret is the plain one: a password, an API key, a connection string."),
            leo("Noted. First clue?"),
            maya("On Thursday the payment company issued a fresh key, and our rotation job wrote it into the vault. The old key stopped working at eight on Friday, exactly as they had promised."),
            leo("Then the vault had the new key and the app read the old one. Somebody failed to save it properly."),
            maya("[pause] Wrong suspect. Every write stores a new secret version, and the old ones stay. The new value was there: version four, written Thursday afternoon."),
            leo("Then the app never asked for it."),
            maya("Now you are detecting. Our Container Apps secret points at the vault and the app reads it as an ordinary setting. That is a Key Vault reference: the platform fetches the value with the app's own managed identity, the badge Azure gives it."),
            leo("And the reference was broken?"),
            maya("Worse. It named a version. Pinned to version three, the dead one, for ever. Leave the version out and you always get the latest."),
            leo("[sigh] So the rotation happened and nobody received it."),
            maya("That is the single most common Key Vault question on the exam. With no version, Container Apps picks a new value up within about thirty minutes; App Service and Functions refresh within twenty-four hours."),
            leo("Case nearly closed. But I saw other files on this vault."),
            maya("Three more. Open the second. When we first built that rotation job, it could not write at all. Refused at the door."),
            leo("Wrong password, then."),
            maya("There is no password anywhere. The job proves who it is to Microsoft Entra ID, which is Azure's sign-in system: the reception desk keeping the list of everyone allowed in, people and apps."),
            leo("And it hands the vault what?"),
            maya("A token: a short-lived signed pass, like a festival wristband. Azure started the job, so it hands tokens over on a private local address nothing outside can reach. The vault checks the pass, then the role."),
            leo("Role. As in a job title."),
            maya("Exactly. RBAC, which is role-based access control. The job had Key Vault Secrets User, which only reads. Writing a new version needs Key Vault Secrets Officer."),
            leo("And the API itself?"),
            maya("Secrets User, and that is right: it only reads. Administrator does everything, and that is for a person, rarely. The smallest role that works, every time."),
            leo("Third file."),
            maya("The order worker, which also charges cards, had a badge switched on inside it. A system-assigned identity dies with its app, cannot be shared, and cannot be granted a role before the app exists. Its first run was refused."),
            leo("So give it the API's badge."),
            maya("You cannot hand a system-assigned one round. We made a user-assigned identity instead: a resource of its own, created first, granted Key Vault Secrets User once, attached to both services."),
            leo("And the code knows which it is wearing how?"),
            maya("An environment variable, a named setting the app reads at start, called AZURE CLIENT ID, set to the identity's number. DefaultAzureCredential picks it up, and on a laptop the same line falls back to your az login."),
            leo("Fourth file, and then I want this closed."),
            maya("The vault itself. Somebody tidying up deleted an old test secret and then purged it, and a week later the auditors asked for it."),
            leo("Deleted is recoverable, surely. Soft delete."),
            maya("Soft delete is a recycle bin, always on for new vaults, keeping a deleted secret seven to ninety days, ninety by default, fixed when the vault is created. But a purge erases it from the bin, for good, right now."),
            leo("Who is allowed to purge?"),
            maya("Anyone with the permission, such as Key Vault Purge Operator. Unless purge protection is on, which it was not. With it on, nothing can be purged early, not even by an administrator, and once on it can never be turned off."),
            leo("And could they have made a new secret with the same name?"),
            maya("Not while the old one sat in the bin. The name is taken until it is recovered, or the period runs out."),
            leo("One thing still bothers me. Could anyone outside have read this vault?"),
            maya("It had an address on the public internet, which is the default. We added a private endpoint, which is a private address for the vault inside Zaiqa's own network, joined the Container Apps environment to that network, and turned public access off."),
            leo("And the older permission list I keep seeing in the portal?"),
            maya("Access policies: a list kept on the vault itself saying who may do which operation. They work, but they sit apart from every other permission in Azure. A vault made now uses RBAC."),
            leo("Last thought. Why is there only one secret in there?"),
            maya("Because a managed identity means most things need no secret at all. The image pull, Cosmos, Azure SQL: all badges. The safe holds the payment key because the payment company is not in Azure and has no badge of ours to check."),
            leo("So the safe is small on purpose."),
            maya("Small on purpose. And the secret carries an expiry date: about thirty days before it, Key Vault raises SecretNearExpiry on Event Grid, Azure's notice board, and the rotation job writes the new version."),
            leo("A job that wakes up by itself?"),
            maya("A function: a small piece of code that runs when something happens. The Event Grid and Functions episodes take both halves properly. A key rotation policy would do it unaided, but only for the vault's own cryptographic keys."),
            leo("Case closed. So what goes in the summary for the exam?"),
            maya("Five. Shared by several apps, or granted before it exists: user-assigned. An app that only reads: Key Vault Secrets User. Rotation that never arrives: a pinned version. Nobody may purge early: purge protection. Reachable only from inside: a private endpoint."),
            leo("And the vault we would build now?"),
            maya("RBAC rather than access policies, purge protection on, a reference with no version in it, and the one secret that genuinely cannot be an identity."),
            leo("Case closed. Anything to try today?"),
            maya("Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            leo("Nothing pinned, nothing purged. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-keyvault",
        listOf(
            maya("Okay, did you hear what the Zaiqa team did this week? Sit down. They finally moved the payment provider's key into Key Vault, and then spent five days finding every possible way to get it wrong."),
            leo("I heard somebody shouting about a pinned version. Start at Monday. What is Key Vault, in plain words?"),
            maya("A locked safe in Azure, holding three kinds of thing: secrets, keys and certificates. A secret is any value you put in and read back out. Zaiqa's whole safe has one thing in it."),
            leo("One? With a dozen services?"),
            maya("That is the good news. The Zaiqa API, the .NET service the phone talks to, pulls its image and reads Cosmos with a managed identity, the badge Azure gives your app. No password either time."),
            leo("So why does the payment key need a safe at all?"),
            maya("Because the payment company is not in Azure. They hand us an API key, a long password that proves the caller is us, and they have no badge of ours to check. The safe holds only what genuinely cannot be an identity."),
            leo("Fine. Monday."),
            maya("Tom. Tom put the key straight into the container app's settings, in plain text, and committed the deployment file to the repository."),
            leo("[sigh] Oh, Tom."),
            maya("By Tuesday morning a scanner had flagged it and the key had to be replaced. The fix took ten minutes: a Key Vault reference, meaning a setting that points at the vault, which the platform fetches with the app's badge."),
            leo("So the app never calls the vault itself?"),
            maya("Never. Container Apps does it, and App Service and Functions do the same. Nothing to cache, nothing to retry, and no vault call while a diner is waiting to pay."),
            leo("Tuesday."),
            maya("Dana. Dana set the vault up and gave the API Key Vault Administrator, which is everything on every secret, so that it would work first time."),
            leo("It did work, though."),
            maya("Until somebody gets into the API. Only reading? Key Vault Secrets User. Writing new values? Key Vault Secrets Officer. Administrator is for a person, rarely. That is RBAC, which is role-based access control: permissions handed out as roles."),
            leo("I thought vaults kept their own permission list."),
            maya("They used to, and still can: access policies, a list kept on the vault itself saying who may do which operation. They sit apart from every other permission in Azure. New vaults use RBAC, and Dana had at least got that part right."),
            leo("Wednesday."),
            maya("Marcus. The order worker, which also charges cards, got its own system-assigned identity, switched on inside it, and on its first night the vault refused it flatly."),
            leo("Why?"),
            maya("Because that kind of badge only exists once its app does, so nobody could grant it a role in advance. And it cannot be shared. Priya moved both services onto one user-assigned identity: a resource of its own, created first, granted once, attached to both."),
            leo("And the code knows which?"),
            maya("An environment variable, a named setting the app reads at start, called AZURE CLIENT ID, set to the identity's number. DefaultAzureCredential reads it, and on a laptop it falls back to your az login."),
            leo("Who checks any of this at the vault's door?"),
            maya("Microsoft Entra ID, which is Azure's sign-in system: the reception desk with the list of everyone allowed in. The app collects a token from it, a short-lived signed pass, like a festival wristband."),
            leo("Thursday. I can feel it coming."),
            maya("The intern. Lovely person. They wrote code to read the secret from the vault on every single payment. Thousands an hour, and at dinner rush the vault began throttling them, which means telling them to slow down."),
            leo("Even with the reference in place?"),
            maya("They had not used it. If you do read it yourself, keep the value in memory for a few minutes, the way your Android app caches a profile instead of calling the server on every screen."),
            leo("And Friday? There is always a Friday."),
            maya("Friday was Priya, and Priya had done everything right, which is what makes it a good story. The payment company issued a new key, the rotation job wrote it, and every charge still failed."),
            leo("The new value was not saved?"),
            maya("It was. Every write stores a new secret version, and the old ones stay. Version four was sitting right there. The reference named version three."),
            leo("Pinned."),
            maya("Pinned. Leave the version out and you always get the latest: Container Apps within about thirty minutes, App Service within twenty-four hours. Priya had copied the secret's full address, version and all, out of the portal."),
            leo("[chuckle] So the portal did it."),
            maya("The portal helped. That is the most common Key Vault question on the exam, and now you will never miss it."),
            leo("Does the vault not rotate the key by itself?"),
            maya("For its own cryptographic keys, yes: a key rotation policy makes new versions on a schedule, because the vault can generate those. It cannot invent a payment company's key."),
            leo("Then what wakes the rotation job?"),
            maya("An expiry date on the secret. About thirty days before it, Key Vault raises SecretNearExpiry on Event Grid, Azure's notice board, and a function, a small piece of code that runs when something happens, writes the new version. The Event Grid episode has the rest."),
            leo("And the weekend? Please say nothing happened."),
            maya("Saturday. Somebody tidying up deleted an old test secret and purged it. Soft delete is a recycle bin, always on for new vaults, keeping a deleted secret seven to ninety days, ninety by default."),
            leo("Then surely it was recoverable."),
            maya("A purge empties the bin for that one secret, for good, right now. It needs its own permission, such as Key Vault Purge Operator. Priya had not switched purge protection on, so there was nothing to stop it."),
            leo("And with it on?"),
            maya("Nothing can be purged before the retention period ends, not by anyone, not by an administrator. And once it is on it can never be turned off. Monday's first job was switching it on."),
            leo("One more. Could anyone outside read that vault?"),
            maya("It had an address on the public internet, which is the default. Now it has a private endpoint, which is a private address inside Zaiqa's own network, the Container Apps environment is joined to that network, and public access is off."),
            leo("And the name of the deleted test secret, could they reuse it?"),
            maya("Not while it sat in the bin. The name is taken until it is recovered, or the period runs out."),
            leo("So what is the gossip I should remember for the exam?"),
            maya("Five. Shared by several apps, or granted before it exists: user-assigned. Only reads: Key Vault Secrets User. Rotation never arrives: a pinned version. Nobody may purge early: purge protection. Reachable only from inside: a private endpoint."),
            leo("And Tom's?"),
            maya("A secret in a settings file is a secret in the repository. Use a Key Vault reference, and keep the safe to the one thing that cannot be a badge."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            leo("Versionless, protected, private. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-keyvault",
        listOf(
            nova("Hey, you. It is late, the kitchen is closed, and it is just the three of us. Me, Ivy, and you, still up. [pause] Tonight: the one secret Zaiqa actually keeps."),
            ivy("Only one? Go on, Nova."),
            nova("Only one. The Zaiqa API, the .NET service the phone talks to, pulls its image and reads Cosmos with a managed identity, the badge Azure gives your app. [pause] No password in either place."),
            ivy("Then what is left to keep?"),
            nova("The payment provider's key. Zaiqa takes money through a payment company, and they hand us an API key, a long password that proves the caller is us. They are not in Azure, so there is no badge of ours for them to check."),
            ivy("And where does it live, this late at night?"),
            nova("Azure Key Vault. A locked safe in Azure, holding three kinds of thing. [pause] Secrets, keys and certificates."),
            ivy("Tell me the difference, slowly."),
            nova("A secret is any value you put in and read back out: a password, an API key, a connection string. A key is cryptographic and never comes out; you send data in and the vault signs it for you."),
            ivy("And a certificate?"),
            nova("It proves a website is who it says it is, and the vault renews it before it runs out. [pause] Zaiqa's partner dashboard has one. Our payment key is a plain secret."),
            ivy("And how does the API get in, with no password?"),
            nova("The same badge. It proves who it is to Microsoft Entra ID, which is Azure's sign-in system: the reception desk that keeps the list of everyone allowed in, people and apps."),
            ivy("And it shows the vault what?"),
            nova("A token: a short-lived signed pass, like a festival wristband. Azure started our container, so it hands tokens over on a private local address nothing outside can reach. [pause] The vault checks the pass, then the role."),
            ivy("Roles. My favourite part."),
            nova("That is RBAC, which is role-based access control. The API only reads, so it gets Key Vault Secrets User. The rotation job writes, so Secrets Officer. Administrator does everything, and that is for a person, rarely."),
            ivy("And the older way? I have seen another list in the portal."),
            nova("Access policies: a permission list kept on the vault itself, saying who may do which operation. They still work, but they sit apart from everything else in Azure. [pause] A vault made tonight uses RBAC."),
            ivy("Mm. And there are two kinds of badge, I am told."),
            nova("Two. A system-assigned identity is switched on inside one app and dies with it: it cannot be shared, and cannot be given a role before its app exists."),
            ivy("And the other?"),
            nova("A user-assigned identity. A resource of its own, created first, granted once, attached to as many apps as you like. Zaiqa's API and its order worker both charge cards, so they share one."),
            ivy("And if one of those is deleted, late one night?"),
            nova("The identity stays, still holding its role. [pause] You set an environment variable, a named setting the app reads at start, called AZURE CLIENT ID, so DefaultAzureCredential knows which badge to wear."),
            ivy("And then the code calls the vault."),
            nova("It does not have to. Container Apps can hold a secret that points at the vault, and a setting that points at that secret. App Service and Functions do the same, and it is called a Key Vault reference."),
            ivy("So the app only ever reads a setting."),
            nova("Only a setting. Nothing to cache, nothing to retry, no vault call while a diner waits to pay. [pause] If you do read it yourself, keep it in memory for a few minutes."),
            ivy("Why a few minutes? Does it change?"),
            nova("It should. That is rotation: replacing a secret on a schedule, so a leaked value stops working. Every write stores a new secret version, and the old ones stay."),
            ivy("And I ask for which one?"),
            nova("None of them. Ask without a version and you always get the latest. [pause] Name a version and you are pinned to it for ever, and rotation never reaches you. That is the one people miss."),
            ivy("How long until a new value arrives?"),
            nova("Container Apps, about thirty minutes. App Service and Functions, within twenty-four hours. [pause] As long as there is no version in the reference."),
            ivy("And can the vault make the new key itself?"),
            nova("Only its own cryptographic keys. A key rotation policy makes new versions on a schedule, because the vault can generate those. It cannot invent a payment company's key."),
            ivy("Then who does, with nobody awake?"),
            nova("You give the secret an expiry date. About thirty days before it, Key Vault raises SecretNearExpiry on Event Grid, Azure's notice board, and a function, a small piece of code that runs when something happens, writes the new version."),
            ivy("Mm. Now the part that keeps me up. Somebody deletes it."),
            nova("Soft delete catches it. A recycle bin, always on for new vaults. [pause] The secret waits there for the retention period: seven to ninety days, ninety by default, chosen when the vault is made and fixed after that."),
            ivy("And can someone empty the bin tonight?"),
            nova("That is a purge. It erases the secret for good, right away, and it needs its own permission, such as Key Vault Purge Operator."),
            ivy("[sigh] So it could still vanish."),
            nova("Not with purge protection. [long pause] With it on, nothing can be purged before the retention ends, not by you, not by an administrator. And once it is on, it can never be turned off."),
            ivy("And while it waits in the bin?"),
            nova("Its name is taken. Recover it, or wait. [pause] You cannot make a new secret with that name in the meantime."),
            ivy("One last worry, and then bed. Who else can reach our vault?"),
            nova("By default, anyone on the internet with the address and a badge we trust. So you add a private endpoint, which is a private address for the vault inside Zaiqa's own network, and you turn public access off."),
            ivy("And the API still reaches it?"),
            nova("The Container Apps environment is joined to that network, so yes. [pause] And remember why the safe is nearly empty: a managed identity means most things need no secret at all."),
            ivy("Mm. Before you send me off. What do I remember for the exam?"),
            nova("Five little things. Shared by several apps, or granted before it exists: user-assigned. Only reads: Key Vault Secrets User. Rotation never arrives: a pinned version. Nobody may purge early: purge protection. Reachable only from inside: a private endpoint."),
            ivy("And the vault we would build tonight?"),
            nova("RBAC rather than access policies, purge protection on, and no version in the reference."),
            ivy("And something to try today, before I sleep?"),
            nova("Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. [pause] Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            ivy("One safe, one secret, nothing pinned. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-keyvault",
        listOf(
            nova("Tonight is date night, darling. Candles, something with far too much chilli in it, and you and me talking about the one secret Zaiqa keeps."),
            rafe("Only one? I was rather hoping you kept several."),
            nova("[laugh] Only one from Azure. The Zaiqa API, the .NET service the phone talks to, pulls its image and reads Cosmos with a managed identity, the badge Azure gives your app. No password either time."),
            rafe("And the one that is left?"),
            nova("The payment provider's key. They are not in Azure, love, so there is no badge of ours for them to check. So, exam style: where does a long password like that live?"),
            rafe("Azure Key Vault. A locked safe in Azure, holding secrets, keys and certificates."),
            nova("Right answer. [kiss] Now tell me which of the three ours is."),
            rafe("A secret: any value you put in and read back out. A key is cryptographic and never comes out. A certificate proves a website is who it says it is, and the vault renews it."),
            nova("Mm. All three, and quick with it. Now my difficult one. How does the API get through the vault's door with no password?"),
            rafe("It proves who it is to Microsoft Entra ID, which is Azure's sign-in system, the reception desk with the list of everyone allowed in. Then it shows the vault a token."),
            nova("A token: a short-lived signed pass, like a festival wristband, handed over on a private address nothing outside can reach. A pass that does not last the night."),
            rafe("Nothing lasts the night in this house. And the role it carries?"),
            nova("RBAC, which is role-based access control. The API only reads, so Key Vault Secrets User. The rotation job writes, so Secrets Officer. Administrator is for a person, rarely."),
            rafe("And the older list kept on the vault itself?"),
            nova("Access policies: a permission list on the vault, saying who may do which operation. They still work, but they live apart from every other permission in Azure. A vault made tonight uses RBAC."),
            rafe("My turn, darling. The Zaiqa API and the order worker both charge cards, and the badge must hold its role before either is deployed. Which badge?"),
            nova("One user-assigned identity: a resource of its own, created first, granted Key Vault Secrets User once, attached to both. A system-assigned one is switched on inside an app, dies with it, and cannot be granted anything early."),
            rafe("Right answer. One badge, worn by two. [kiss]"),
            nova("And the code is told which by an environment variable, a named setting the app reads at start, called AZURE CLIENT ID. DefaultAzureCredential does the rest."),
            rafe("Next from me. Should the API read the vault on every payment?"),
            nova("Never. Container Apps holds a secret that points at the vault, and a setting that points at that secret. That is a Key Vault reference, and App Service and Functions have the same thing."),
            rafe("Right answer. Let somebody else do the fetching. [kiss]"),
            nova("And if you do read it yourself, keep it in memory for a few minutes, or the vault throttles you, which means tells you to slow down."),
            rafe("Now the one you will enjoy. The payment company issues a new key, the rotation job writes it, and every charge still fails. Why?"),
            nova("The reference named a version. Pinned to the old one, for ever. Every write stores a new secret version and the old ones stay; ask without a version and you get the latest."),
            rafe("Right answer. Never pin yourself to an old flame. [kiss]"),
            nova("About thirty minutes for Container Apps, darling, and twenty-four hours for App Service. Now mine. The payment key must be replaced every ninety days with nobody involved. What do you build?"),
            rafe("A key rotation policy on the secret."),
            nova("[sigh] No kiss for that one. Rotation policies are for the vault's own cryptographic keys, which it can generate itself. A payment company's key it cannot invent."),
            rafe("Then an expiry date on the secret."),
            nova("Go on, finish it."),
            rafe("About thirty days before it, Key Vault raises SecretNearExpiry on Event Grid, Azure's notice board, and a function, a small piece of code that runs when something happens, writes the new version."),
            nova("Right answer, and a lovely recovery. [kiss]"),
            rafe("My turn again. Somebody deletes the payment secret by mistake, and the vault has ninety-day retention. What is true?"),
            nova("Soft delete is a recycle bin: it waits there seven to ninety days, ninety by default, and can be recovered. Its name is taken while it waits. And anyone with the purge permission could erase it for good."),
            rafe("Right answer. Nothing is over until it is really over. [kiss]"),
            nova("Unless we stop them. Your turn, love: purge protection."),
            rafe("Off by default. With it on, nothing can be purged before the retention ends, not by us, not by an administrator."),
            nova("And if we change our minds?"),
            rafe("We cannot. Once it is on, it can never be turned off. A commitment you do not take back."),
            nova("Mm. Like us. And the retention period itself is chosen when the vault is made, and fixed after that."),
            rafe("Who can purge, when the protection is off?"),
            nova("Anyone with the permission, such as Key Vault Purge Operator. Which is exactly why the protection matters."),
            rafe("Last from me. Our vault has an address on the public internet. Fix it."),
            nova("A private endpoint, which is a private address for the vault inside Zaiqa's own network, with public access turned off. The Container Apps environment is joined to that network, so the API still reaches it."),
            rafe("Right answer. Somewhere only we can go. [kiss]"),
            nova("And the reason the safe is nearly empty, love: a managed identity means most things need no secret at all. The image pull, Cosmos, Azure SQL, all badges."),
            rafe("A small safe is a safe worth guarding."),
            nova("Exactly. And a vault made now: RBAC rather than access policies, purge protection on, and no version in the reference."),
            rafe("Then the last question, for the exam. What do we remember, darling?"),
            nova("Shared by several apps, or granted before it exists: user-assigned. Only reads: Key Vault Secrets User. Rotation never arrives: a pinned version. Nobody may purge early: purge protection. Reachable only from inside: a private endpoint."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try tonight, darling?"),
            rafe("Today, rather. Create an RBAC vault and give yourself Key Vault Secrets Officer. Store a secret twice and list its versions. Read it in Python with DefaultAzureCredential after az login. Then delete it, find it in the deleted list, and recover it."),
            nova("And you are the one secret I will never purge. Goodnight, everyone."),
            rafe("Versionless, protected, private. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "The phone calls the Zaiqa API; the API reads the payment key with its managed identity. Near expiry, Event Grid wakes a function that writes a new version.",
        nodes = listOf(
            DNode("phone", "Zaiqa app", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("api", "Zaiqa API", 0.45f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "managed identity"),
            DNode("kv", "Key Vault", 0.82f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "the payment key"),
            DNode("eg", "Event Grid", 0.82f, 0.56f, w = 0.3f, h = 0.2f, tone = 2, sub = "near expiry"),
            DNode("fn", "Rotation function", 0.45f, 0.56f, w = 0.3f, h = 0.2f, tone = 1, sub = "new version"),
            DNode("note", "The phone never holds a secret", 0.5f, 0.87f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("phone", "api", "HTTPS"),
            DEdge("api", "kv", "reads"),
            DEdge("kv", "eg", "near expiry"),
            DEdge("eg", "fn", "trigger"),
            DEdge("fn", "kv", "new version", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun kinds() = DiagramSpec(
        "A vault holds secrets, keys and certificates; Zaiqa's one secret is the payment provider's key, because nothing else needs a password.",
        nodes = listOf(
            DNode("kv", "Key Vault", 0.5f, 0.12f, w = 0.4f, h = 0.18f, kind = NodeKind.CYLINDER),
            DNode("sec", "Secrets", 0.18f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "read back out"),
            DNode("keys", "Keys", 0.5f, 0.5f, w = 0.3f, h = 0.2f, sub = "never come out"),
            DNode("cert", "Certificates", 0.82f, 0.5f, w = 0.3f, h = 0.2f, sub = "renewed for you"),
            DNode("pay", "The payment key", 0.5f, 0.85f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the only one"),
        ),
        edges = listOf(
            DEdge("kv", "sec"),
            DEdge("kv", "keys"),
            DEdge("kv", "cert"),
            DEdge("sec", "pay"),
        ),
        aspect = 0.8f,
    )

    private fun door() = DiagramSpec(
        "The API proves who it is to Entra, gets a short-lived token, and shows it to the vault, which then checks its role. No password anywhere.",
        nodes = listOf(
            DNode("app", "Zaiqa API", 0.24f, 0.16f, w = 0.42f, h = 0.2f, tone = 1, sub = "managed identity"),
            DNode("entra", "Entra ID", 0.76f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "reception desk"),
            DNode("token", "A token", 0.76f, 0.5f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "a short pass"),
            DNode("kv", "Key Vault", 0.24f, 0.5f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "checks the role"),
            DNode("note", "No password anywhere", 0.5f, 0.84f, w = 0.8f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("app", "entra", "asks"),
            DEdge("entra", "token", "issues"),
            DEdge("app", "kv", "shows it"),
        ),
        aspect = 0.8f,
    )

    private fun roles() = DiagramSpec(
        "Each identity gets the smallest role that works: Secrets User reads, Secrets Officer writes, Administrator is for a person, rarely.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.24f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "reads only"),
            DNode("fn", "Rotation job", 0.24f, 0.5f, w = 0.4f, h = 0.2f, tone = 1, sub = "writes versions"),
            DNode("person", "A person", 0.24f, 0.85f, w = 0.4f, h = 0.2f, kind = NodeKind.USER),
            DNode("user", "Secrets User", 0.76f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "read values"),
            DNode("officer", "Secrets Officer", 0.76f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "create and update"),
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
            DNode("api", "Zaiqa API", 0.18f, 0.8f, w = 0.3f, h = 0.2f, tone = 1, sub = "charges cards"),
            DNode("fn", "Order worker", 0.5f, 0.8f, w = 0.3f, h = 0.2f, tone = 1, sub = "charges cards"),
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
            DNode("app", "Zaiqa API", 0.25f, 0.58f, w = 0.42f, h = 0.2f, tone = 1, sub = "no version: latest"),
            DNode("cache", "Memory cache", 0.75f, 0.58f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "a few minutes"),
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
            DNode("eg", "Event Grid", 0.75f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "near expiry"),
            DNode("prov", "Payment company", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "issues a new key"),
            DNode("fn", "Rotation function", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "Secrets Officer"),
            DNode("policy", "Key rotation policy", 0.5f, 0.85f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "for keys only"),
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

    private fun network() = DiagramSpec(
        "A private endpoint gives the vault an address inside Zaiqa's own network; with public access off, nothing on the internet can reach it.",
        nodes = listOf(
            DNode("net", "Zaiqa network", 0.5f, 0.14f, w = 0.6f, h = 0.18f, kind = NodeKind.PILL),
            DNode("aca", "Container Apps", 0.24f, 0.48f, w = 0.4f, h = 0.2f, tone = 1, sub = "joined to it"),
            DNode("pe", "Private endpoint", 0.76f, 0.48f, w = 0.4f, h = 0.2f, sub = "a private address"),
            DNode("pub", "The internet", 0.24f, 0.82f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "no way in"),
            DNode("kv", "Key Vault", 0.76f, 0.82f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "public access off"),
        ),
        edges = listOf(
            DEdge("aca", "pe", "reaches"),
            DEdge("pe", "kv"),
            DEdge("pub", "kv", "blocked", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Key Vault questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("shared", "Shared, or granted first", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "user-assigned"),
            DNode("reads", "Only reads secrets", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Secrets User"),
            DNode("rotate", "Rotates itself", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "SecretNearExpiry"),
            DNode("pinned", "Rotation never arrives", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pinned version"),
            DNode("purge", "Nobody purges early", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "purge protection"),
            DNode("private", "Inside the network only", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "private endpoint"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("shared", "reads", "rotate", "pinned", "purge", "private")
    private val allRoles = setOf("api", "fn", "person", "user", "officer", "admin")
    private val today = BoardCard("Today", "An RBAC vault, two versions, a Python read, then delete and recover.")
    private val protection = BoardCard("Purge protection", "No purge before retention ends, for anyone. It can never be turned off.")
    private val clientId = BoardCard("AZURE CLIENT ID", "Tells DefaultAzureCredential which badge to wear.")
    private val reference = BoardCard("Key Vault reference", "A setting that points at the vault; the platform fetches it for you.")
    private val smallest = BoardCard("The smallest role", "Reads: Secrets User. Writes: Secrets Officer. Admin: a person.")
    private val nearExpiry = BoardCard("SecretNearExpiry", "Thirty days before expiry; a function writes the new version.")
    private val policyCard = BoardCard("Key rotation policy", "For the vault's own keys. It cannot invent a provider's key.")
    private val privateCard = BoardCard("Private endpoint", "A private address inside Zaiqa's network; public access off.")
    private val badge = BoardCard("Managed identity", "The badge Azure gives your app: image pull and Cosmos, no password.")
    private val nameTaken = BoardCard("The name is taken", "Recover it, or wait for the retention period to end.")
    private val phrases = BoardCard("Listen for the phrase", "Identity, role, pinned version, purge, private endpoint.")

    private fun classicBoard() = PodcastBoard(
        "ai-keyvault",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is running..."
            Scene(
                0, "Zaiqa's one real secret", flow(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("Today", "The API on Container Apps, SQL and Cosmos. One secret is left.")),
                    Beat(2, light = setOf("api"), card = badge),
                    Beat(4, show = setOf("kv", "note"), light = setOf("kv"), pulse = "api" to "kv", card = BoardCard("The payment key", "The provider is not in Azure, so there is no badge to check.")),
                    Beat(6, light = setOf("kv"), card = BoardCard("Key Vault", "A locked safe in Azure: secrets, keys and certificates.")),
                ),
            ),
            // 7: "What is the difference? They all sound like secrets to me."
            Scene(
                7, "Secrets, keys, certificates", kinds(),
                beats = listOf(
                    Beat(8, show = setOf("kv", "sec"), light = setOf("sec"), pulse = "kv" to "sec", card = BoardCard("A secret", "Any value you put in and read back out: a password, an API key.")),
                    Beat(10, show = setOf("keys", "cert"), light = setOf("cert"), card = BoardCard("A certificate", "Proves a website is who it says it is. The vault can renew it.")),
                    Beat(12, show = setOf("pay"), light = setOf("pay"), card = BoardCard("The safe stays small", "A managed identity means most things need no secret at all.")),
                ),
            ),
            // 13: "And who checks that badge at the vault's door?"
            Scene(
                13, "At the vault's door", door(),
                beats = listOf(
                    Beat(14, show = setOf("app", "entra"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Microsoft Entra ID", "The reception desk: the list of everyone allowed in, people and apps.")),
                    Beat(16, show = setOf("token", "kv", "note"), light = setOf("token"), pulse = "entra" to "token", card = BoardCard("A token", "A short-lived signed pass, like a festival wristband.")),
                ),
            ),
            // 17: "Role. As in a job title?"
            Scene(
                17, "The smallest role", roles(),
                beats = listOf(
                    Beat(18, show = allRoles, light = setOf("api", "user"), pulse = "api" to "user", card = BoardCard("RBAC", "Roles like job titles. The Zaiqa API reads: Key Vault Secrets User.")),
                    Beat(20, light = setOf("admin"), card = BoardCard("Not administrator", "Everything, on every secret. That is for a person, rarely.")),
                    Beat(22, light = setOf("person"), card = BoardCard("Access policies", "The older list kept on the vault itself, apart from Azure's roles.")),
                    Beat(24, light = setOf("user", "officer"), card = BoardCard("New vaults use RBAC", "One flag at creation, and role assignments behave as everywhere.")),
                ),
            ),
            // 25: "Back to the badge. Is there more than one kind?"
            Scene(
                25, "Two kinds of badge", identities(),
                beats = listOf(
                    Beat(26, show = setOf("sys"), light = setOf("sys"), card = BoardCard("System-assigned", "Inside one app, dies with it. Cannot be shared or granted early.")),
                    Beat(28, show = setOf("ua", "kv", "api", "fn"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Its own resource: created first, granted once, attached to both.")),
                    Beat(30, light = setOf("api", "fn"), pulse = "ua" to "api", card = clientId),
                ),
            ),
            // 31: "Hold on. Does the API call the vault on every payment?"
            Scene(
                31, "No vault call at all", versions(),
                beats = listOf(
                    Beat(32, show = setOf("app"), light = setOf("app"), card = reference),
                    Beat(34, show = setOf("cache"), light = setOf("cache"), pulse = "app" to "cache", card = BoardCard("If you do read it", "Cache a few minutes, or the vault tells you to slow down.")),
                    Beat(36, show = setOf("v1", "v2", "v3"), light = setOf("v3"), pulse = "v3" to "app", card = BoardCard("Secret versions", "Every write adds one, and the old ones stay. No version: the latest.")),
                    Beat(38, show = setOf("pin"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Pinned", "Thirty minutes for Container Apps, a day for App Service.")),
                ),
            ),
            // 39: "Can the vault rotate the key by itself?"
            Scene(
                39, "Rotation", rotation(),
                beats = listOf(
                    Beat(40, show = setOf("kv", "policy"), light = setOf("policy"), card = policyCard),
                    Beat(42, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = nearExpiry),
                    Beat(44, light = setOf("kv"), pulse = "fn" to "kv", card = BoardCard("Nothing is pushed", "The app reads the latest, always.")),
                ),
            ),
            // 45: "Last worry. What if somebody deletes the payment secret..."
            Scene(
                45, "The recycle bin", recycle(),
                beats = listOf(
                    Beat(46, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Soft delete", "Seven to ninety days, ninety by default, fixed at creation.")),
                    Beat(48, light = setOf("deleted"), card = nameTaken),
                    Beat(50, show = setOf("gone", "note"), light = setOf("gone", "note"), pulse = "deleted" to "gone", card = protection),
                ),
            ),
            // 51: "One more. Our vault has an address on the internet, does it not?"
            Scene(
                51, "Private only", network(),
                beats = listOf(
                    Beat(52, light = setOf("pe"), pulse = "pe" to "kv", card = privateCard),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("shared", "reads", "rotate", "pinned", "purge"), card = phrases),
                    Beat(56, light = setOf("private"), card = BoardCard("And the vault itself", "A private endpoint with public access off. RBAC, not policies.")),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-keyvault--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Charge That Failed..."
            Scene(
                0, "The Charge That Failed", flow(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("The case", "Every Zaiqa charge declined at eight on a Friday.")),
                    Beat(2, light = setOf("api"), card = BoardCard("The API was healthy", "Menus from SQL, tracking from Cosmos. Only the charge failed.")),
                    Beat(4, show = setOf("kv"), light = setOf("kv"), pulse = "api" to "kv", card = BoardCard("The payment key", "A long password that proves the caller is us.")),
                    Beat(6, show = setOf("note"), light = setOf("note"), card = BoardCard("Key Vault", "A locked safe in Azure: secrets, keys and certificates.")),
                ),
            ),
            // 7: "And the other two?"
            Scene(
                7, "Which of the three", kinds(),
                beats = listOf(
                    Beat(8, show = setOf("kv", "sec", "keys", "cert"), light = setOf("sec"), pulse = "kv" to "sec", card = BoardCard("A secret", "Any value you put in and read back out.")),
                    Beat(10, show = setOf("pay"), light = setOf("pay"), card = BoardCard("Clue one", "Thursday: the provider issued a new key, written to the vault.")),
                ),
            ),
            // 11: "Then the vault had the new key and the app read the old one..."
            Scene(
                11, "Clue two: pinned", versions(),
                beats = listOf(
                    Beat(12, show = setOf("v1", "v2", "v3"), light = setOf("v3"), card = BoardCard("A new secret version", "Every write adds one; the old ones stay. Version four was there.")),
                    Beat(14, show = setOf("app", "cache"), light = setOf("app"), pulse = "v3" to "app", card = reference),
                    Beat(16, show = setOf("pin"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Pinned to version three", "Leave the version out and you always get the latest.")),
                    Beat(18, light = setOf("app"), card = BoardCard("How fast", "Container Apps about thirty minutes, App Service within a day.")),
                ),
            ),
            // 19: "Case nearly closed. But I saw other files on this vault."
            Scene(
                19, "Refused at the door", door(),
                beats = listOf(
                    Beat(20, show = setOf("app", "kv"), light = setOf("app"), card = BoardCard("The second file", "The rotation job could not write at all. Refused at the door.")),
                    Beat(22, show = setOf("entra", "note"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Microsoft Entra ID", "The reception desk: the list of everyone allowed in.")),
                    Beat(24, show = setOf("token"), light = setOf("token"), pulse = "entra" to "token", card = BoardCard("A token", "A short-lived signed pass, like a festival wristband.")),
                ),
            ),
            // 25: "Role. As in a job title."
            Scene(
                25, "The role was too small", roles(),
                beats = listOf(
                    Beat(26, show = allRoles, light = setOf("user", "officer"), card = BoardCard("Secrets User reads", "Writing a new version needs Key Vault Secrets Officer.")),
                    Beat(28, light = setOf("api", "admin"), pulse = "api" to "user", card = smallest),
                ),
            ),
            // 29: "Third file."
            Scene(
                29, "The third file", identities(),
                beats = listOf(
                    Beat(30, show = setOf("sys", "fn"), light = setOf("sys"), card = BoardCard("System-assigned", "Dies with its app, cannot be shared or granted early.")),
                    Beat(32, show = setOf("ua", "kv", "api"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Created first, granted once, attached to the API and the worker.")),
                    Beat(34, light = setOf("api", "fn"), pulse = "ua" to "api", card = clientId),
                ),
            ),
            // 35: "Fourth file, and then I want this closed."
            Scene(
                35, "The fourth file", recycle(),
                beats = listOf(
                    Beat(36, show = setOf("active", "deleted"), light = setOf("active"), pulse = "active" to "deleted", card = BoardCard("Tidying up", "An old test secret deleted, then purged. The auditors wanted it.")),
                    Beat(38, light = setOf("deleted"), card = BoardCard("Soft delete", "A recycle bin: seven to ninety days, ninety by default.")),
                    Beat(40, show = setOf("gone", "note"), light = setOf("gone", "note"), pulse = "deleted" to "gone", card = protection),
                    Beat(42, light = setOf("deleted"), card = nameTaken),
                ),
            ),
            // 43: "One thing still bothers me. Could anyone outside have read this vault?"
            Scene(
                43, "Reachable from where?", network(),
                beats = listOf(
                    Beat(44, light = setOf("pe"), pulse = "pe" to "kv", card = privateCard),
                    Beat(46, light = setOf("net"), card = BoardCard("Access policies", "The older list on the vault itself. A vault made now uses RBAC.")),
                ),
            ),
            // 47: "Last thought. Why is there only one secret in there?"
            Scene(
                47, "Only what cannot be a badge", door(),
                beats = listOf(
                    Beat(48, light = setOf("note"), card = BoardCard("Why one secret", "A managed identity means most things need no secret at all.")),
                ),
            ),
            // 49: "So the safe is small on purpose."
            Scene(
                49, "Rotation, for next time", rotation(),
                beats = listOf(
                    Beat(50, show = setOf("kv", "eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = nearExpiry),
                    Beat(52, show = setOf("policy"), light = setOf("policy"), card = policyCard),
                ),
            ),
            // 53: "Case closed. So what goes in the summary for the exam?"
            Scene(
                53, "Case closed", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("shared", "reads", "pinned"), card = BoardCard("Listen for the phrase", "Identity, role, pinned version: one answer each.")),
                    Beat(56, light = setOf("purge", "private", "rotate"), card = BoardCard("And three more", "Purge protection, a private endpoint, and the rotation event.")),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-keyvault--gossip",
        listOf(
            // 0: "Okay, did you hear what the Zaiqa team did this week?..."
            Scene(
                0, "The Zaiqa team's week", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api", "kv"), light = setOf("kv"), card = BoardCard("Key Vault", "A locked safe in Azure. Zaiqa's holds exactly one thing.")),
                    Beat(4, show = setOf("phone"), light = setOf("api"), pulse = "api" to "kv", card = badge),
                    Beat(6, show = setOf("note"), light = setOf("note"), card = BoardCard("Why the payment key", "The provider is not in Azure, so there is no badge to check.")),
                ),
            ),
            // 7: "Fine. Monday."
            Scene(
                7, "Monday: Tom", kinds(),
                beats = listOf(
                    Beat(8, show = setOf("kv", "sec", "pay"), light = setOf("pay"), pulse = "kv" to "sec", card = BoardCard("Monday: Tom", "The key in plain text in the deployment file, committed.")),
                    Beat(10, show = setOf("keys", "cert"), light = setOf("sec"), card = reference),
                    Beat(12, light = setOf("sec"), card = BoardCard("No vault call", "Nothing to cache, nothing to retry, nobody waiting to pay.")),
                ),
            ),
            // 13: "Tuesday."
            Scene(
                13, "Tuesday: Dana", roles(),
                beats = listOf(
                    Beat(14, show = setOf("api", "admin"), light = setOf("admin"), card = BoardCard("Administrator, so it works", "Everything on every secret, for an app that only reads.")),
                    Beat(16, show = setOf("user", "fn", "officer", "person"), light = setOf("user", "officer"), pulse = "api" to "user", card = smallest),
                    Beat(18, light = setOf("person"), card = BoardCard("Access policies", "The older list kept on the vault itself. New vaults use RBAC.")),
                ),
            ),
            // 19: "Wednesday."
            Scene(
                19, "Wednesday: Marcus", identities(),
                beats = listOf(
                    Beat(20, show = setOf("sys", "fn"), light = setOf("sys"), card = BoardCard("System-assigned", "Only exists once its app does, so no role in advance.")),
                    Beat(22, show = setOf("ua", "kv", "api"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Created first, granted once, attached to the API and the worker.")),
                    Beat(24, light = setOf("api", "fn"), pulse = "ua" to "api", card = clientId),
                ),
            ),
            // 25: "Who checks any of this at the vault's door?"
            Scene(
                25, "Who checks the badge", door(),
                beats = listOf(
                    Beat(26, light = setOf("entra", "token"), pulse = "app" to "entra", card = BoardCard("Entra and a token", "The reception desk, and a short-lived signed pass.")),
                ),
            ),
            // 27: "Thursday. I can feel it coming."
            Scene(
                27, "Thursday: the intern", versions(),
                beats = listOf(
                    Beat(28, show = setOf("app", "cache"), light = setOf("app"), card = BoardCard("Thursday: the intern", "The vault read on every payment, until it told them to slow down.")),
                    Beat(30, light = setOf("cache"), pulse = "app" to "cache", card = BoardCard("Cache it", "Keep the value in memory for a few minutes.")),
                ),
            ),
            // 31: "And Friday? There is always a Friday."
            Scene(
                31, "Friday: Priya", versions(),
                beats = listOf(
                    Beat(32, show = setOf("v1", "v2", "v3", "app"), light = setOf("v3"), card = BoardCard("Friday: Priya", "A new key written, and every charge still failed.")),
                    Beat(34, light = setOf("v3"), pulse = "v3" to "app", card = BoardCard("Secret versions", "Every write adds one; version four was sitting right there.")),
                    Beat(36, show = setOf("pin"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Pinned", "The reference named version three. Leave the version out.")),
                    Beat(38, light = setOf("app"), card = BoardCard("The exam's favourite", "Thirty minutes for Container Apps, a day for App Service.")),
                ),
            ),
            // 39: "Does the vault not rotate the key by itself?"
            Scene(
                39, "Who rotates it", rotation(),
                beats = listOf(
                    Beat(40, show = setOf("kv", "policy"), light = setOf("policy"), card = policyCard),
                    Beat(42, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = nearExpiry),
                ),
            ),
            // 43: "And the weekend? Please say nothing happened."
            Scene(
                43, "Saturday: the bin", recycle(),
                beats = listOf(
                    Beat(44, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Soft delete", "A recycle bin: seven to ninety days, ninety by default.")),
                    Beat(46, show = setOf("gone"), light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Purged", "Erased for good, right now. It needs its own permission.")),
                    Beat(48, show = setOf("note"), light = setOf("note"), card = protection),
                ),
            ),
            // 49: "One more. Could anyone outside read that vault?"
            Scene(
                49, "The last worry", network(),
                beats = listOf(
                    Beat(50, light = setOf("pe"), pulse = "pe" to "kv", card = privateCard),
                    Beat(52, light = setOf("kv"), card = nameTaken),
                ),
            ),
            // 53: "So what is the gossip I should remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("shared", "reads", "pinned", "purge", "private"), card = phrases),
                    Beat(56, light = setOf("rotate"), card = BoardCard("And Tom's", "A secret in a settings file is a secret in the repository.")),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-keyvault--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen is closed..."
            Scene(
                0, "The one secret", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api", "kv"), light = setOf("api"), card = BoardCard("Tonight", "Zaiqa keeps exactly one secret. Everything else is a badge.")),
                    Beat(2, light = setOf("api"), card = badge),
                    Beat(4, show = setOf("phone", "note"), light = setOf("note"), card = BoardCard("The payment key", "The provider is not in Azure, so there is no badge to check.")),
                    Beat(6, light = setOf("kv"), pulse = "api" to "kv", card = BoardCard("Key Vault", "A locked safe in Azure: secrets, keys and certificates.")),
                ),
            ),
            // 7: "Tell me the difference, slowly."
            Scene(
                7, "Three kinds of thing", kinds(),
                beats = listOf(
                    Beat(8, show = setOf("kv", "sec"), light = setOf("sec"), pulse = "kv" to "sec", card = BoardCard("A secret", "Any value you put in and read back out.")),
                    Beat(10, show = setOf("keys", "cert", "pay"), light = setOf("cert"), card = BoardCard("A certificate", "Proves a website is who it says it is; the vault renews it.")),
                ),
            ),
            // 11: "And how does the API get in, with no password?"
            Scene(
                11, "At the door", door(),
                beats = listOf(
                    Beat(12, show = setOf("app", "entra"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Microsoft Entra ID", "The reception desk: the list of everyone allowed in.")),
                    Beat(14, show = setOf("token", "kv", "note"), light = setOf("token"), pulse = "entra" to "token", card = BoardCard("A token", "A short-lived signed pass, like a festival wristband.")),
                ),
            ),
            // 15: "Roles. My favourite part."
            Scene(
                15, "Roles, and the older list", roles(),
                beats = listOf(
                    Beat(16, show = allRoles, light = setOf("api", "user"), pulse = "api" to "user", card = smallest),
                    Beat(18, light = setOf("person"), card = BoardCard("Access policies", "The older list on the vault itself. A vault made tonight uses RBAC.")),
                ),
            ),
            // 19: "Mm. And there are two kinds of badge, I am told."
            Scene(
                19, "Two kinds of badge", identities(),
                beats = listOf(
                    Beat(20, show = setOf("sys"), light = setOf("sys"), card = BoardCard("System-assigned", "Inside one app, dies with it. No role before it exists.")),
                    Beat(22, show = setOf("ua", "kv", "api", "fn"), light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("User-assigned", "Created first, granted once, attached to both.")),
                    Beat(24, light = setOf("api", "fn"), pulse = "ua" to "api", card = clientId),
                ),
            ),
            // 25: "And then the code calls the vault."
            Scene(
                25, "No vault call", versions(),
                beats = listOf(
                    Beat(26, show = setOf("app"), light = setOf("app"), card = reference),
                    Beat(28, show = setOf("cache"), light = setOf("cache"), pulse = "app" to "cache", card = BoardCard("If you read it yourself", "Keep it in memory for a few minutes.")),
                    Beat(30, show = setOf("v1", "v2", "v3"), light = setOf("v3"), pulse = "v3" to "app", card = BoardCard("Secret versions", "Every write adds one, and the old ones stay.")),
                    Beat(32, show = setOf("pin"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Pinned", "Ask without a version and you always get the latest.")),
                    Beat(34, light = setOf("app"), card = BoardCard("How fast", "Container Apps about thirty minutes, App Service within a day.")),
                ),
            ),
            // 35: "And can the vault make the new key itself?"
            Scene(
                35, "Rotation", rotation(),
                beats = listOf(
                    Beat(36, show = setOf("kv", "policy"), light = setOf("policy"), card = policyCard),
                    Beat(38, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = nearExpiry),
                ),
            ),
            // 39: "Mm. Now the part that keeps me up. Somebody deletes it."
            Scene(
                39, "The recycle bin", recycle(),
                beats = listOf(
                    Beat(40, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Soft delete", "Seven to ninety days, ninety by default, fixed at creation.")),
                    Beat(42, show = setOf("gone"), light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Purge", "Erased for good, right away. It needs its own permission.")),
                    Beat(44, show = setOf("note"), light = setOf("note"), card = protection),
                    Beat(46, light = setOf("deleted"), card = nameTaken),
                ),
            ),
            // 47: "One last worry, and then bed. Who else can reach our vault?"
            Scene(
                47, "Who else can reach it", network(),
                beats = listOf(
                    Beat(48, light = setOf("pe"), pulse = "pe" to "kv", card = privateCard),
                    Beat(50, light = setOf("aca"), pulse = "aca" to "pe", card = BoardCard("Still reachable", "The Container Apps environment is joined to that network.")),
                ),
            ),
            // 51: "Mm. Before you send me off. What do I remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("shared", "reads", "pinned", "purge", "private"), card = phrases),
                    Beat(54, light = setOf("rotate"), card = BoardCard("And the vault itself", "RBAC, purge protection on, and no version in the reference.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-keyvault--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night, one secret", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api", "kv"), light = setOf("api"), card = BoardCard("Tonight", "Zaiqa keeps one secret. Everything else is a badge.")),
                    Beat(2, light = setOf("api"), card = badge),
                    Beat(4, show = setOf("phone", "note"), light = setOf("note"), card = BoardCard("Quiz: where does it live?", "A long password from a provider who is not in Azure.")),
                    Beat(5, light = setOf("kv"), pulse = "api" to "kv", card = BoardCard("Right answer", "Azure Key Vault: secrets, keys and certificates.")),
                ),
            ),
            // 7: "A secret: any value you put in and read back out..."
            Scene(
                7, "Which of the three", kinds(),
                beats = listOf(
                    Beat(7, show = setOf("kv", "sec", "keys", "cert"), light = setOf("sec"), pulse = "kv" to "sec", card = BoardCard("A secret", "Any value you put in and read back out.")),
                    Beat(8, show = setOf("pay"), light = setOf("pay"), card = BoardCard("All three", "Keys never come out; a certificate proves a website is itself.")),
                ),
            ),
            // 9: "It proves who it is to Microsoft Entra ID..."
            Scene(
                9, "At the door", door(),
                beats = listOf(
                    Beat(9, show = setOf("app", "entra", "token"), light = setOf("entra"), pulse = "app" to "entra", card = BoardCard("Entra and a token", "The reception desk, and a short-lived signed pass.")),
                    Beat(10, show = setOf("kv", "note"), light = setOf("token"), pulse = "entra" to "token", card = BoardCard("A pass that fades", "Handed over on a private address nothing outside can reach.")),
                ),
            ),
            // 11: "Nothing lasts the night in this house. And the role it carries?"
            Scene(
                11, "Knowing your role", roles(),
                beats = listOf(
                    Beat(12, show = allRoles, light = setOf("api", "user"), pulse = "api" to "user", card = smallest),
                    Beat(14, light = setOf("person"), card = BoardCard("Access policies", "The older list on the vault itself. A vault made tonight uses RBAC.")),
                ),
            ),
            // 15: "My turn, darling. The Zaiqa API and the order worker both charge cards..."
            Scene(
                15, "One badge, worn by two", identities(),
                beats = listOf(
                    Beat(15, show = setOf("ua", "kv", "api", "fn"), light = setOf("api", "fn"), card = BoardCard("Quiz: two apps, role first", "Both charge cards, and the role must exist before deploying.")),
                    Beat(16, light = setOf("ua"), pulse = "ua" to "kv", card = BoardCard("Right answer", "One user-assigned identity: created first, granted once.")),
                    Beat(18, show = setOf("sys"), light = setOf("sys"), card = clientId),
                ),
            ),
            // 19: "Next from me. Should the API read the vault on every payment?"
            Scene(
                19, "Let someone else fetch it", versions(),
                beats = listOf(
                    Beat(19, show = setOf("app"), light = setOf("app"), card = BoardCard("Quiz: call the vault?", "Should the API read the vault on every payment?")),
                    Beat(20, show = setOf("cache"), light = setOf("cache"), pulse = "app" to "cache", card = BoardCard("Right answer", "A Key Vault reference. Otherwise cache it a few minutes.")),
                    Beat(23, show = setOf("v1", "v2", "v3"), light = setOf("v3"), card = BoardCard("Quiz: new key, still failing", "The job wrote the new key and every charge still failed. Why?")),
                    Beat(24, light = setOf("v3"), pulse = "v3" to "app", card = BoardCard("Secret versions", "Every write adds one; ask without a version for the latest.")),
                    Beat(26, show = setOf("pin"), light = setOf("pin"), pulse = "v1" to "pin", card = BoardCard("Pinned", "Thirty minutes for Container Apps, a day for App Service.")),
                ),
            ),
            // 27: "A key rotation policy on the secret."
            Scene(
                27, "Every ninety days", rotation(),
                beats = listOf(
                    Beat(27, show = setOf("kv", "policy"), light = setOf("policy"), card = BoardCard("A rotation policy?", "Those are for the vault's own keys. No kiss for that one.")),
                    Beat(31, show = setOf("eg", "fn", "prov"), light = setOf("eg", "fn"), pulse = "kv" to "eg", card = nearExpiry),
                ),
            ),
            // 33: "My turn again. Somebody deletes the payment secret by mistake..."
            Scene(
                33, "Not over till it is over", recycle(),
                beats = listOf(
                    Beat(33, show = setOf("active", "deleted"), light = setOf("deleted"), pulse = "active" to "deleted", card = BoardCard("Quiz: deleted by mistake", "Ninety-day retention is on. What is true?")),
                    Beat(34, show = setOf("gone"), light = setOf("deleted", "gone"), card = BoardCard("Right answer", "Recoverable, the name is taken, and a purge erases it.")),
                    Beat(37, show = setOf("note"), light = setOf("note"), card = protection),
                    Beat(42, light = setOf("gone"), pulse = "deleted" to "gone", card = BoardCard("Who can purge?", "Anyone with the permission, unless the protection is on.")),
                ),
            ),
            // 43: "Last from me. Our vault has an address on the public internet."
            Scene(
                43, "Somewhere only we can go", network(),
                beats = listOf(
                    Beat(44, light = setOf("pe"), pulse = "pe" to "kv", card = privateCard),
                    Beat(46, light = setOf("aca"), pulse = "aca" to "pe", card = BoardCard("Why one secret", "A managed identity means most things need no secret at all.")),
                ),
            ),
            // 49: "Then the last question, for the exam. What do we remember, darling?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("shared", "reads", "pinned", "purge", "private"), card = phrases),
                    Beat(53, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
