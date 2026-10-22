package com.shahaabapps.core.content

/** Authentication and identity management: one sign-in for everyone: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesIdentity {
    private val classic = Podcast(
        "sa-identity",
        listOf(
            maya("Hi, and welcome back. Today is identity: who you are, and how you prove it. Our running story: Two Tracks goes global. The Android app is now a company, with office staff, a backend on Azure, a partner agency in Australia, and app users everywhere. And every one of them needs to sign in to something."),
            leo("And I bet everyone has a different password for everything. [sigh]"),
            maya("That's what we fix. At the centre sits Microsoft Entra ID. Picture the company's ID-card office in the cloud: it holds every person and every app, and checks who they are when they sign in."),
            leo("In my own app I keep a users table, with passwords, and a login page that checks them. Is this the same thing, only bigger?"),
            maya("Same idea, but you stop writing it yourself. Entra ID keeps the accounts, checks the passwords, and hands out passes that apps and Azure accept. Your code just checks the pass."),
            leo("Passes? What kind of pass?"),
            maya("A token. That's a short-lived signed pass, like a festival wristband: proof you were checked at the gate, and it runs out within hours. One identity, one sign-in, and the wristband gets you into email, internal apps and the Azure portal, Azure's website for managing everything."),
            leo("And where do all these people live?"),
            maya("In a tenant. A tenant is one organisation's own copy of Entra ID, its directory, like one company's private address book. Most companies need just one. Inside it live users and groups."),
            leo("Groups, like the payments team?"),
            maya("Exactly. And here's a rule: give access to groups, never to single people. For example, twenty people look after the backend. Give the role to the group, once. When someone joins or leaves, you change the group, not twenty separate permissions."),
            leo("Okay. Now proving it's really me. A password?"),
            maya("A password alone isn't enough; passwords get stolen. MFA, multifactor authentication, asks for a second proof, like a prompt on your phone. Think of a door that needs a key and a fingerprint: a stolen key alone won't open it."),
            leo("But I don't want a phone prompt every time I open my email."),
            maya("That's Conditional Access. It decides when to ask. It's a set of if-then rules, like a doorman with a checklist. If the user is an admin, or signs in from an unknown country, then require MFA, require a company laptop, or block. From the office, just let them in."),
            leo("Is it free?"),
            maya("No. Conditional Access needs the paid licence called Entra ID P1. A licence is the right to use a paid feature, per person. For a tiny company with no budget, there's security defaults: one free switch that turns on basic protection, MFA included, for everyone. All or nothing, no exceptions."),
            leo("And what if a sign-in just looks weird?"),
            maya("ID Protection is Entra's risk spotter. For example, a login from Sydney, then from Paris an hour later: nobody flies that fast. Rules that ask for MFA only when a sign-in looks risky need the P2 licence, the bigger one."),
            leo("Could we drop passwords entirely?"),
            maya("Yes, that's passwordless sign-in. Your face, a fingerprint or a phone prompt proves it's you. A passkey is a secret key kept on your own device, so there's nothing to type, and nothing a fake website can trick out of you."),
            leo("Okay. Now the office. They already sign in to something."),
            maya("Active Directory. It's the directory Windows offices have used for decades, on their own servers in their own building. That's on-premises: in the building, not in the cloud. Hybrid identity means the same people exist there and in Entra ID."),
            leo("How do they get from there to there?"),
            maya("A sync tool copies users and groups from Active Directory in the office up to Entra ID, like a clerk copying the staff list every few minutes."),
            leo("Which tool? Something I have to run on a server?"),
            maya("Entra Connect Sync is: a full server you install and look after. Cloud Sync uses small agents, little programs that only call out, with the settings kept in the cloud. Less to run."),
            leo("And the password? Where's it checked?"),
            maya("Three choices. The default is password hash sync. A hash is a fingerprint of the password: the same password always gives the same fingerprint, but the fingerprint can't be turned back into the password. A fingerprint of that fingerprint is copied to the cloud, and Entra ID checks sign-ins itself."),
            leo("Why is that the default?"),
            maya("Least to run, and it works even if the office is down. And because Entra holds those fingerprints, it can spot leaked passwords."),
            leo("But say the office locks someone's account. The cloud still has their old fingerprint. Can they still get in?"),
            maya("[pause] Good doubt. With hash sync, the cloud doesn't see that lockout at once. If office rules like that must apply at once, pick the second choice, pass-through authentication: agents in the office check each cloud sign-in against Active Directory, live."),
            leo("And the third?"),
            maya("Federation, with AD FS, Active Directory Federation Services: every sign-in is handed to your own servers. It's the most effort and the most to fail, with servers facing the internet. Only when Entra can't do something itself, like a third-party MFA system you must keep."),
            leo("Now outsiders. Two Tracks hires that translation agency in Australia."),
            maya("Invite them as a B2B guest. B2B means business to business: the guest signs in with their own company's account, so you never manage their password. And you can still apply Conditional Access to them."),
            leo("And the millions of app users signing up with email or Google?"),
            maya("That's Microsoft Entra External ID, in an external tenant: a separate address book just for customers, with your app's own branded sign-up page. Azure AD B2C is the older product for this, not sold to new customers since May 2025, so new designs use External ID."),
            leo("Last piece. The backend itself has to read storage. Does it get a password?"),
            maya("No. It gets a managed identity. Picture an employee badge that Azure issues and renews for your app. No secret exists, so none can leak."),
            leo("Right now my API keeps a storage key in its config file. That's what the badge replaces?"),
            maya("Exactly that. The old way is a service principal, an app's own account, which needs a secret or a certificate that someone must store and renew. The badge needs neither: your code asks Azure for a token, and Azure answers."),
            leo("And there are two kinds of badge, right?"),
            maya("Two. A system-assigned one lives and dies with one resource. A user-assigned one is its own resource that several apps can share. Either way, give it one narrow role, such as reading files in storage, and nothing more."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If the question says least effort, or keep working when the office is down, password hash sync. If office lockout must apply at once, pass-through. If it says MFA only outside the office, Conditional Access. If partners use their own accounts, B2B guests. And if there must be no secrets to store, a managed identity."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a security group and add yourself to it. Invite a second email address of yours as a B2B guest, and accept the invitation. Turn on the managed identity of an App Service or Function app. And if your tenant has P1, draft a Conditional Access policy in report-only mode, which only records who it would affect."),
            leo("One sign-in, and no secrets. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-identity",
        listOf(
            maya("Welcome to Case Files. Today's case: The Admin Who Wasn't. Two Tracks has gone global: office staff, a backend on Azure, a partner agency in Australia, and app users everywhere. On Sunday night, someone signed in to the Azure portal, Azure's website for managing everything, as Sam, an admin. Sam was asleep."),
            leo("Detective Leo, on the case. Stolen password, obviously."),
            maya("Obviously. But first, where did that sign-in happen? In Microsoft Entra ID. Picture the company's ID-card office in the cloud: it holds every person and app, checks who they are, and hands out passes that apps and Azure accept."),
            leo("Passes? What did the intruder walk away with?"),
            maya("A token. That's a short-lived signed pass, like a festival wristband: proof you were checked at the gate, and it runs out within hours. Under the picture, it's a small signed message from Entra ID saying who you are, and every app checks the signature."),
            leo("Then my first suspect: Sam's password was too short. Make everyone use longer ones."),
            maya("[pause] Wrong suspect. A long password, once stolen, works just as well. Clue one: Sam had no second proof. MFA, multifactor authentication, asks for something more, like a prompt on your phone, so a stolen password alone isn't enough."),
            leo("So switch MFA on for everyone, every single time."),
            maya("Staff would revolt. The right tool is Conditional Access: if-then rules, like a doorman with a checklist. If the user is an admin, or signs in from outside the office, then require MFA. From the office network, let them in. The office becomes a trusted named location."),
            leo("Why not just security defaults? I heard they're free."),
            maya("They are: one switch that turns on basic protection, MFA included, for everyone. But it's all or nothing; no exception for the office. Conditional Access needs the paid licence called Entra ID P1. A licence is the right to use a paid feature, per person."),
            leo("And how would anyone have known Sunday's sign-in was odd?"),
            maya("ID Protection, Entra's risk spotter. Sam's account signed in from Sydney, then Lisbon an hour later. Nobody flies that fast. Rules that ask for MFA only when a sign-in looks risky need the P2 licence. Where those sign-in records are kept is the monitoring episode."),
            leo("Second clue?"),
            maya("Monday morning, the office servers crashed, and nobody could sign in to anything in the cloud either. Not email, not the portal."),
            leo("Suspect: Azure was down."),
            maya("Azure was fine. Two Tracks had chosen federation, with AD FS, Active Directory Federation Services. That hands every sign-in to your own servers. It sounded enterprise. But when those servers fell over, everyone was locked out."),
            leo("Active Directory being the office one?"),
            maya("Yes. It's the directory Windows offices have used for decades, on their own servers in their own building. That's on-premises. Hybrid identity means the same people exist there and in Entra ID. A sync tool copies them from the office up to Entra ID, like a clerk copying the staff list every few minutes."),
            leo("So what should they have picked instead of federation?"),
            maya("Password hash sync, the default. A hash is a fingerprint of the password: same password, same fingerprint, but you can't turn the fingerprint back into the password. A fingerprint of that fingerprint goes to the cloud, and Entra ID checks sign-ins itself. Office down? Sign-in still works."),
            leo("And it can spot leaked passwords too?"),
            maya("Exactly. ID Protection compares those fingerprints with leaked ones. Federation is only for when Entra can't do something itself, like a third-party MFA system you must keep. It's the most effort, and the most to fail."),
            leo("Is there anything between the two?"),
            maya("Pass-through authentication. Small agents in the office check each cloud sign-in against Active Directory, live, using outbound connections only. Pick it when office rules, like a locked-out account, must apply at once. And keep hash sync on beside it, as the backup."),
            leo("Third clue. I'm ready."),
            maya("An old partner account. A designer from an agency had been given a Two Tracks cloud account and password. The contract ended. The account didn't. Someone had been using it."),
            leo("Suspect: the designer."),
            maya("Maybe. But the real culprit is the design. Partners should be B2B guests. B2B means business to business: the guest signs in with their own company's account. When they leave their company, that account dies, and so does their way in. You never hold their password."),
            leo("And Two Tracks' millions of app users? Guest accounts too?"),
            maya("No. Customers signing up with email or Google go to Microsoft Entra External ID."),
            leo("Why not the same place as the staff? One user list sounds simpler."),
            maya("A tenant is one organisation's own copy of Entra ID. Millions of customers mixed in with your staff would be a mess, so External ID gives customers a separate tenant of their own, with your branded sign-up page. Azure AD B2C is the older product; new designs use External ID."),
            leo("Fourth witness?"),
            maya("The backend. It reads files from storage, and it signed in as a service principal: an app's own account, with a secret, a password for the app, pasted into its settings. That secret ended up in a screenshot."),
            leo("Suspect: whoever took the screenshot."),
            maya("[chuckle] The screenshot is innocent. The fix is a managed identity: an employee badge Azure issues and renews for your app. Under the picture, it's an account in Entra ID with no secret at all. Azure started your app, so it answers the app's request for a token on a private local address nobody outside can reach."),
            leo("And where does that stop working?"),
            maya("On your laptop there's no badge, so you sign in with az login, the Azure command line's sign-in. A pipeline outside Azure, like GitHub Actions, uses workload identity federation: Entra trusts the pass GitHub gives it."),
            leo("And my Android app? Can the phone get a badge, and read storage directly?"),
            maya("Never. The phone isn't an Azure resource, so it never gets a managed identity, and anything shipped on it can be pulled apart. It calls the backend, and the backend uses its badge."),
            leo("Any last witness?"),
            maya("One. Every permission was given to people one by one. When Sam left, twenty separate permissions had to be hunted down. Give access to groups, never to single people: change the group, not twenty assignments."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Admins, or MFA only outside the office: Conditional Access. Least effort, and still working when the office is down: password hash sync. Office lockout must apply at once: pass-through. Partners with their own accounts: B2B guests. No secrets to store: a managed identity."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a security group and add yourself to it. Invite a second email address of yours as a B2B guest, and accept the invitation. Turn on the managed identity of an App Service or Function app. And if your tenant has P1, draft a Conditional Access policy in report-only mode."),
            leo("One sign-in, and no strangers. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-identity",
        listOf(
            maya("Okay, did you hear what happened at Two Tracks this week? The app's gone global, with office staff, a partner agency in Australia and app users everywhere, and the team rebuilt how everyone signs in. Five people. Five mistakes."),
            leo("[laugh] I heard someone locked the whole office out. Start from the start. Signs in to what?"),
            maya("To Microsoft Entra ID. Picture the company's ID-card office in the cloud: it holds every person and app, checks who they are when they sign in, and hands out a token. That's a short-lived signed pass, like a festival wristband, that apps and Azure accept."),
            leo("One wristband for everything?"),
            maya("One identity, one sign-in: email, internal apps, and the Azure portal, Azure's website for managing everything. All in one tenant, which is the company's own copy of Entra ID, like a private address book. Most companies need just one."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. The office already signs in with Active Directory, the directory Windows offices have used for decades, on their own servers in the building. That's called on-premises. Tom had to bring those same people into the cloud."),
            leo("Sounds reasonable."),
            maya("It is. A sync tool copies users and groups from the office up to Entra ID, like a clerk copying the staff list every few minutes. Tom picked Cloud Sync, with small agents, little programs that only call out. Good choice. Then he picked federation, with AD FS, because it sounded enterprise."),
            leo("And federation means?"),
            maya("Every sign-in is handed to your own servers in the office. So when those servers crashed on Monday afternoon, nobody could sign in to anything. Not email, not the portal. That's the lockout you heard about."),
            leo("Ouch. What should he have picked?"),
            maya("Password hash sync, the default. A hash is a fingerprint of the password: same password, same fingerprint, but it can't be turned back."),
            leo("Wait, so the office sends our passwords to Microsoft? Tom would hate that."),
            maya("Not the passwords. A fingerprint of that fingerprint goes up to the cloud, and Entra ID checks sign-ins itself. Office down? Still works. Federation only when Entra can't do something itself."),
            leo("Tuesday?"),
            maya("Dana. Dana's rule: a locked-out office account must be refused in the cloud immediately. So she chose pass-through authentication. Agents in the office check each cloud sign-in against Active Directory, live. Correct, so far."),
            leo("Then what went wrong?"),
            maya("She switched hash sync off. So there was no backup if the agents went down, and Entra could no longer spot leaked passwords. Microsoft says keep password hash sync on, whatever you choose. Dana switched it back on by lunch."),
            leo("Wednesday?"),
            maya("Marcus. A translation agency in Australia joined. Marcus made each translator a brand-new Two Tracks account, with a password he emailed them."),
            leo("[sigh] Emailed. Okay. And the right way?"),
            maya("Invite them as B2B guests. B2B means business to business: they sign in with their own agency's account, so you never hold their password. When they leave the agency, their way in leaves too. And your own sign-in rules still apply to them."),
            leo("And the app users? The millions?"),
            maya("Not guests. Customers signing up with email or Google go to Microsoft Entra External ID, a separate tenant just for customers, with your own branded sign-up page. Azure AD B2C is the older product; new designs use External ID."),
            leo("Thursday, I'm guessing, the intern."),
            maya("[chuckle] The intern. The backend reads files from storage. So the intern made a service principal, an app's own account, and pasted its secret, a password for the app, straight into the app settings."),
            leo("Where anyone with access can read it."),
            maya("Exactly. The fix is a managed identity. Picture an employee badge that Azure issues and renews for your app. Under the picture, it's an account in Entra ID with no secret at all, so nothing can leak. Azure started the app, so the app asks a private local address for tokens, and Azure answers."),
            leo("And there are two kinds, I heard."),
            maya("A system-assigned one lives and dies with one resource. A user-assigned one is its own resource that several apps can share. The intern now gives it one narrow role, reading storage, and nothing more."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya protected every admin with MFA, multifactor authentication: a second proof, like a phone prompt, so a stolen password alone isn't enough. A door that needs a key and a fingerprint."),
            leo("Every time they open their email?"),
            maya("No, she was smarter than that. She used Conditional Access: if-then rules, like a doorman with a checklist. Admins, or anyone outside the office, prove it twice. It needs P1, a paid licence, the right to use the feature, per person."),
            leo("That sounds perfect. Where's the gossip?"),
            maya("She gave every permission to people one by one. Twenty of them. When someone left, she had twenty edits. Give access to groups, never to single people. Change the group, done. Even Priya."),
            leo("And where does all this stop working?"),
            maya("On your laptop there's no badge; you sign in with az login, the Azure command line's sign-in. A GitHub Actions pipeline uses workload identity federation: Entra trusts GitHub's own pass for it. And the Android app never gets a managed identity; it calls the backend. Where sign-in records go is the monitoring episode."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Least effort, works when the office is down: password hash sync. Office lockout must apply at once: pass-through. MFA only for admins or outside the office: Conditional Access. Partners with their own accounts: B2B guests. No secrets to store: a managed identity."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a security group and add yourself to it. Invite a second email address of yours as a B2B guest, and accept the invitation. Turn on the managed identity of an App Service or Function app. And if your tenant has P1, draft a Conditional Access policy in report-only mode."),
            leo("Groups, guests and badges. Got it. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-identity",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about who gets in, and how they prove it."),
            ivy("Mm. Proving who you are. At this hour, that's personal."),
            nova("It is. Take Two Tracks, the Android app that's gone global: office staff, a backend on Azure, a partner agency in Australia, and app users everywhere. Every one of them signs in through Microsoft Entra ID. Picture the company's ID-card office in the cloud, open all night."),
            ivy("And what does the office hand you, once you're checked?"),
            nova("A token. That's a short-lived signed pass, like a festival wristband: proof you were checked at the gate. It fades within hours. Apps and Azure accept it, so one sign-in opens email, internal apps and the Azure portal."),
            ivy("And where does everyone live, inside Entra?"),
            nova("In a tenant, which is one organisation's own copy of Entra ID, like a private address book. Most companies need just one. Inside it, users and groups. [pause] And give access to groups, never to single people, so when someone leaves, you change the group, not twenty permissions."),
            ivy("Now, proving it's really me. A password?"),
            nova("A password alone gets stolen. MFA, multifactor authentication, asks for a second proof, like a little prompt on your phone. A door that wants a key and a fingerprint."),
            ivy("But not every single time, I hope."),
            nova("No. Conditional Access decides when. If-then rules, like a doorman with a checklist. If you're an admin, or you're signing in from outside the office, then prove it twice. From inside, come in. It needs the paid licence called Entra ID P1: the right to use that feature, per person."),
            ivy("And for a tiny company with no budget?"),
            nova("Security defaults. One free switch that turns on basic protection, MFA included, for everyone. All or nothing, no exceptions. [pause] And if you'd like no password at all, there's passwordless: a face, a fingerprint, or a phone prompt, and nothing to type."),
            ivy("And if a sign-in just feels wrong? Late, from somewhere strange?"),
            nova("ID Protection notices. It's Entra's risk spotter: Sydney, then Paris an hour later, and nobody flies that fast. Rules that ask for MFA only when a sign-in looks risky need the P2 licence, the bigger one."),
            ivy("Now the office people. They already sign in to something."),
            nova("Active Directory. It's the directory Windows offices have used for decades, on their own servers in the building. On-premises, we say. Hybrid identity means the same people live there and in Entra ID."),
            ivy("How do they cross over?"),
            nova("A sync tool copies them from the office up to Entra ID, like a clerk copying the staff list every few minutes. Entra Connect Sync is a full server you look after. Cloud Sync uses small agents, little programs that only call out, with its settings in the cloud. Less to run, at three in the morning or any time."),
            ivy("And the password? Who checks it?"),
            nova("The default is password hash sync. A hash is a fingerprint of the password: same password, same fingerprint, but it can't be turned back into the password. [pause] A fingerprint of that fingerprint goes to the cloud, and Entra ID checks sign-ins itself, even when the office is dark."),
            ivy("And if the office rules must apply at once? A locked-out account, refused right now?"),
            nova("Pass-through authentication. Agents in the office check each cloud sign-in against Active Directory, live, calling out only. Keep hash sync on beside it, as the backup."),
            ivy("Calling out only? So nothing from the internet knocks on the office door?"),
            nova("Nothing. The agents reach out to Entra and collect the sign-ins waiting for them. [pause] Federation, with AD FS, is the opposite: it hands sign-in to your own servers, facing the internet. Most effort, most to fail. Only when Entra can't do it itself."),
            ivy("Now the visitors. A translation agency in Australia, working late with us."),
            nova("They come in as B2B guests. B2B means business to business: they sign in with their own company's account, so you never hold their password. And your Conditional Access rules still apply to them."),
            ivy("And the strangers? Millions of app users, signing up with email or Google?"),
            nova("Microsoft Entra External ID, in an external tenant: a separate address book just for customers, with your own branded sign-up page. Azure AD B2C is the older product; new designs use External ID."),
            ivy("And the backend itself? It reads storage. Does it whisper a password?"),
            nova("No password at all. A managed identity. Picture an employee badge that Azure issues and renews for your app. Under the picture, it's an account in Entra ID with no secret. Azure started your app, so your app asks a private local address for tokens, one nobody outside can reach, and Azure answers."),
            ivy("And the old way?"),
            nova("A service principal, an app's own account, signing in with a secret or a certificate, a password-like file someone must store and renew. [pause] Secrets get pasted into settings. Badges don't."),
            ivy("Where does all of this stop working, Nova?"),
            nova("On your laptop, no badge: you use az login, the Azure command line's sign-in. A GitHub Actions pipeline uses workload identity federation, where Entra trusts GitHub's own pass. And the Android app never gets a badge; it calls the backend. Where sign-in records go is the monitoring episode, another late night."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. Least effort, still working when the office is down, means password hash sync. Lockout applied at once means pass-through. MFA only outside the office means Conditional Access. Partners with their own accounts means B2B guests. And no secrets to store means a managed identity."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a security group and add yourself to it. Invite a second email address of yours as a B2B guest, and accept the invitation. [pause] Turn on the managed identity of an App Service or Function app. And if your tenant has P1, draft a Conditional Access policy in report-only mode."),
            ivy("One sign-in, and no secrets. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-identity",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about identity. Who you are, and how you prove it."),
            rafe("I can prove who I am. Ask me anything."),
            nova("[laugh] Then let's start with who checks. Microsoft Entra ID. Picture the company's ID-card office in the cloud: it holds every person and app, checks who they are, and hands out passes."),
            rafe("Like the login code I wrote for my first app? A users table and a password check?"),
            nova("The same job, darling, done for you. Two Tracks has gone global: office staff, a backend on Azure, a partner agency in Australia, and app users everywhere. All of them sign in there."),
            rafe("And the pass it hands out?"),
            nova("A token. That's a short-lived signed pass, like a festival wristband: proof you were checked at the gate. It fades within hours."),
            rafe("A token that doesn't last the night. I'll try not to take that personally. Where do we all live, inside Entra?"),
            nova("In a tenant, which is one organisation's own copy of Entra ID, like a private address book. Users and groups live there. And always give access to groups, never to single people, so a leaver means one change."),
            rafe("Commitment to the group. I like it."),
            nova("Now, my first question, exam style. The office already signs in with Active Directory, the directory Windows offices have used for decades, on their own servers in the building. A sync tool copies the staff into the cloud. What's Microsoft's default way to check their password there?"),
            rafe("Password hash sync. A hash is a fingerprint of the password: same password, same fingerprint, but it can't be turned back. A fingerprint of that fingerprint goes to the cloud, and Entra ID checks sign-ins itself. Least to run, and it works even when the office is down."),
            nova("Right answer. You leave nothing to chance. [kiss]"),
            rafe("And it spots leaked passwords, too. My turn. Policy says a locked-out office account must be refused in the cloud immediately, and no servers may face the internet. Which sign-in method?"),
            nova("Federation, with AD FS. Hand every sign-in to our own servers."),
            rafe("Close, but no kiss. Federation would enforce it, but it needs servers reachable from the internet, and it's the most to fail. The answer is pass-through authentication: small agents in the office check each sign-in against Active Directory, calling out only. And keep hash sync on beside it."),
            nova("Mm. Fair. Then here's mine. Two Tracks wants MFA, multifactor authentication, a second proof like a phone prompt, for anyone opening the Azure portal from outside the office. But not inside. What do you recommend?"),
            rafe("Conditional Access. If-then rules, like a doorman with a checklist, with the office as a trusted named location. It needs the P1 licence, the paid right to use it."),
            nova("Right answer. You always know when to ask for more. [kiss]"),
            rafe("And security defaults won't do?"),
            nova("That's one free switch that turns on basic protection for everyone, all or nothing. No exception for the office. And risky sign-ins, Sydney then Paris an hour later, are spotted by ID Protection; rules on that risk need P2."),
            rafe("My question. A translation agency in Australia must edit content. Each translator has a work account at the agency. How do they get in?"),
            nova("As B2B guests. Business to business: they sign in with their own agency's account, so we never hold their passwords. When they leave the agency, their way in leaves too."),
            rafe("Right answer. No strangers' passwords in our house. [kiss]"),
            nova("Then the millions of app users, signing up with email or Google. Where do they live?"),
            rafe("Microsoft Entra External ID, in an external tenant: a separate address book for customers, with our own branded sign-up page. Not Azure AD B2C, the older product, no longer sold to new customers."),
            nova("Right answer. And branded beautifully. [kiss]"),
            rafe("Last quiz. The backend reads storage. Requirement: no credentials to store or rotate. What do you recommend?"),
            nova("A managed identity. Picture an employee badge that Azure issues and renews for the app. Under the picture, it's an account in Entra ID with no secret at all. Azure started the app, so the app asks a private local address for tokens, and Azure answers. Then grant it one narrow storage role."),
            rafe("Right answer. No secrets between us. [kiss]"),
            nova("Unlike a service principal, an app's own account with a secret that someone pastes into settings and forgets to renew."),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("On your laptop, no badge: you use az login, the Azure command line's sign-in. GitHub Actions uses workload identity federation, where Entra trusts GitHub's own pass. And the Android app never gets a badge; it calls the backend. Where sign-in records go is the monitoring episode."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Least effort, works when the office is down: password hash sync. Lockout at once: pass-through. MFA only outside the office: Conditional Access. Partners with their own accounts: B2B guests. Customers: External ID. No secrets: a managed identity."),
            rafe("Six for six. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a security group and add yourself to it. Invite a second email address of yours as a B2B guest, and accept the invitation. Turn on the managed identity of an App Service or Function app. And if your tenant has P1, draft a Conditional Access policy in report-only mode."),
            nova("And you're the only guest I'd ever invite. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun overview() = DiagramSpec(
        "Office users are synced into Entra ID; every sign-in passes Conditional Access before a token reaches the apps.",
        nodes = listOf(
            DNode("ad", "On-prem AD", 0.17f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "office users"),
            DNode("sync", "Cloud Sync", 0.5f, 0.2f, w = 0.28f, h = 0.2f, tone = 2, sub = "copies the staff"),
            DNode("entra", "Entra ID", 0.83f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "the tenant"),
            DNode("user", "Staff and guests", 0.15f, 0.55f, w = 0.24f, h = 0.18f, kind = NodeKind.USER),
            DNode("ca", "Conditional Access", 0.5f, 0.55f, w = 0.3f, h = 0.2f, tone = 2, sub = "the doorman"),
            DNode("apps", "Apps and Azure", 0.84f, 0.55f, w = 0.3f, h = 0.2f, sub = "accept the token"),
            DNode("note", "Managed identities: apps sign in with no secret", 0.5f, 0.87f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("ad", "sync", "sync"),
            DEdge("sync", "entra"),
            DEdge("user", "ca", "sign in"),
            DEdge("entra", "ca", "policy", dashed = true),
            DEdge("ca", "apps", "token"),
        ),
        aspect = 0.8f,
    )

    private fun doorman() = DiagramSpec(
        "Conditional Access reads each sign-in and decides: allow, require MFA, or block. Security defaults are the free all-or-nothing switch.",
        nodes = listOf(
            DNode("signin", "A sign-in", 0.17f, 0.18f, w = 0.3f, h = 0.2f, sub = "who, where, device"),
            DNode("ca", "Conditional Access", 0.5f, 0.18f, w = 0.3f, h = 0.2f, tone = 2, sub = "if-then rules, P1"),
            DNode("allow", "Allow", 0.17f, 0.5f, w = 0.3f, h = 0.18f, sub = "from the office"),
            DNode("mfa", "Require MFA", 0.5f, 0.5f, w = 0.3f, h = 0.18f, tone = 1, sub = "admins, outside"),
            DNode("block", "Block", 0.83f, 0.5f, w = 0.3f, h = 0.18f, tone = 2, sub = "unknown country"),
            DNode("defaults", "Security defaults", 0.26f, 0.84f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "free, all or nothing"),
            DNode("idp", "ID Protection", 0.76f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "risky sign-ins, P2"),
        ),
        edges = listOf(
            DEdge("signin", "ca"),
            DEdge("ca", "allow"),
            DEdge("ca", "mfa"),
            DEdge("ca", "block"),
        ),
        aspect = 0.8f,
    )

    private fun hashSync() = DiagramSpec(
        "Office staff are synced from Active Directory; the password is checked by hash sync (the default), pass-through agents, or federation.",
        nodes = listOf(
            DNode("ad", "Active Directory", 0.2f, 0.18f, w = 0.36f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "on-premises"),
            DNode("entra", "Entra ID", 0.8f, 0.18f, w = 0.36f, h = 0.22f, tone = 1, sub = "in the cloud"),
            DNode("phs", "Password hash sync", 0.17f, 0.53f, w = 0.32f, h = 0.24f, kind = NodeKind.NOTE, tone = 1, sub = "the default"),
            DNode("pta", "Pass-through", 0.5f, 0.53f, w = 0.32f, h = 0.24f, kind = NodeKind.NOTE, sub = "lockout at once"),
            DNode("fed", "Federation (AD FS)", 0.83f, 0.53f, w = 0.32f, h = 0.24f, kind = NodeKind.NOTE, tone = 2, sub = "only when forced"),
            DNode("note", "A hash: a fingerprint that can't be turned back", 0.5f, 0.87f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("ad", "entra", "sync"),
        ),
        aspect = 0.8f,
    )

    private fun guests() = DiagramSpec(
        "Partners come into the Two Tracks tenant as B2B guests with their own accounts; customers sign up in a separate External ID tenant.",
        nodes = listOf(
            DNode("tt", "Two Tracks tenant", 0.5f, 0.15f, w = 0.5f, h = 0.2f, tone = 1, sub = "staff and guests"),
            DNode("guest", "B2B guests", 0.22f, 0.5f, w = 0.4f, h = 0.22f, sub = "their own accounts"),
            DNode("ext", "External ID", 0.78f, 0.5f, w = 0.4f, h = 0.22f, tone = 1, sub = "a customer tenant"),
            DNode("b2c", "Azure AD B2C", 0.22f, 0.85f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "older, not sold new"),
            DNode("users", "App users", 0.78f, 0.85f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "email or Google"),
        ),
        edges = listOf(
            DEdge("guest", "tt", "invited"),
            DEdge("users", "ext", "sign up"),
        ),
        aspect = 0.8f,
    )

    private fun badge() = DiagramSpec(
        "The backend wears a managed identity with one narrow storage role; a service principal needs a secret someone must store.",
        nodes = listOf(
            DNode("api", "Two Tracks backend", 0.25f, 0.17f, w = 0.42f, h = 0.2f, tone = 1, sub = "App Service"),
            DNode("st", "Storage", 0.75f, 0.17f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "files to read"),
            DNode("mi", "Managed identity", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "no secret at all"),
            DNode("sp", "Service principal", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "a secret to store"),
            DNode("sys", "System-assigned: dies with its app", 0.26f, 0.84f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("ua", "User-assigned: shared by apps", 0.76f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("api", "st", "one narrow role"),
            DEdge("api", "mi", "wears"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "No badge on a laptop (az login) or GitHub (federation); the Android app has no identity at all and calls the backend.",
        nodes = listOf(
            DNode("laptop", "Your laptop", 0.18f, 0.18f, w = 0.32f, h = 0.2f, tone = 1, sub = "az login"),
            DNode("gh", "GitHub Actions", 0.5f, 0.18f, w = 0.3f, h = 0.2f, sub = "trusts GitHub's pass"),
            DNode("azure", "In Azure", 0.82f, 0.18f, w = 0.32f, h = 0.2f, tone = 1, sub = "managed identity"),
            DNode("phone", "Android app", 0.27f, 0.55f, w = 0.42f, h = 0.2f, sub = "no identity, no secret"),
            DNode("api", "Backend", 0.75f, 0.55f, w = 0.42f, h = 0.2f, tone = 1, sub = "holds the badge"),
            DNode("note", "Sign-in records: the monitoring episode", 0.5f, 0.87f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("phone", "api", "calls"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's identity questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("phs", "Least effort, office down", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "password hash sync"),
            DNode("pta", "Lockout applies at once", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "pass-through"),
            DNode("ca", "MFA outside the office", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Conditional Access"),
            DNode("b2b", "Partners, own accounts", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "B2B guests"),
            DNode("ext", "Customers sign up", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "External ID"),
            DNode("mi", "No secrets to store", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a managed identity"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("phs", "pta", "ca", "b2b", "ext", "mi")
    private val today = BoardCard("Today", "A group, a B2B guest, a managed identity, a report-only policy.")
    private val entraCard = BoardCard("Microsoft Entra ID", "The ID-card office in the cloud: people, apps, and passes.")
    private val tokenCard = BoardCard("A token", "A short-lived signed pass, like a festival wristband.")
    private val tenantCard = BoardCard("Tenant", "One organisation's own copy of Entra ID: its address book.")
    private val mfaCard = BoardCard("MFA", "Multifactor authentication: a second proof, like a phone prompt.")
    private val caCard = BoardCard("Conditional Access", "If-then rules, like a doorman with a checklist. Needs P1.")
    private val idpCard = BoardCard("ID Protection", "Spots risky sign-ins, like Sydney then Paris in an hour. Needs P2.")
    private val adCard = BoardCard("Active Directory", "The office's own directory, on its own servers: on-premises.")
    private val syncCard = BoardCard("A sync tool", "Cloud Sync or Connect Sync copies staff from the office up to Entra.")
    private val phsCard = BoardCard("Password hash sync", "The default: a fingerprint of the fingerprint goes to the cloud.")
    private val ptaCard = BoardCard("Pass-through", "Office agents check each sign-in live: lockout applies at once.")
    private val b2bCard = BoardCard("B2B guest", "Signs in with their own company's account. No password to hold.")
    private val extCard = BoardCard("External ID", "A separate tenant for customers, with your own sign-up page.")
    private val miCard = BoardCard("Managed identity", "A badge Azure issues and renews: an Entra account with no secret.")
    private val badgesCard = BoardCard("Two kinds of badge", "System-assigned dies with its app; user-assigned can be shared.")
    private val stopsCard = BoardCard("Where it stops", "Laptop: az login. GitHub: federation. Phone: calls the backend.")
    private val groupsCard = BoardCard("Groups, not people", "Give the role to a group once; change the group, not twenty.")

    private fun classicBoard() = PodcastBoard(
        "sa-identity",
        listOf(
            // 0: "Hi, and welcome back. Today is identity..."
            Scene(
                0, "One sign-in for everyone", overview(),
                beats = listOf(
                    Beat(0, show = setOf("user"), light = setOf("user"), card = BoardCard("Two Tracks goes global", "Staff, partners, app users and apps: all need to sign in.")),
                    Beat(2, show = setOf("entra", "apps"), light = setOf("entra"), card = entraCard),
                    Beat(6, light = setOf("apps"), pulse = "ca" to "apps", card = tokenCard),
                    Beat(8, light = setOf("entra"), card = tenantCard),
                    Beat(10, card = groupsCard),
                ),
            ),
            // 11: "Okay. Now proving it's really me. A password?"
            Scene(
                11, "Proving it's you", doorman(),
                beats = listOf(
                    Beat(12, show = setOf("signin", "mfa"), light = setOf("mfa"), card = mfaCard),
                    Beat(14, show = setOf("ca", "allow", "block"), light = setOf("ca"), pulse = "signin" to "ca", card = caCard),
                    Beat(16, show = setOf("defaults"), light = setOf("defaults"), card = BoardCard("Security defaults", "One free switch: basic protection for all. No exceptions.")),
                    Beat(18, show = setOf("idp"), light = setOf("idp"), card = idpCard),
                    Beat(20, light = setOf("allow"), card = BoardCard("Passwordless", "A face, a fingerprint or a phone prompt. Nothing to type.")),
                ),
            ),
            // 21: "Okay. Now the office. They already sign in to something."
            Scene(
                21, "The office", hashSync(),
                beats = listOf(
                    Beat(22, show = setOf("ad"), light = setOf("ad"), card = adCard),
                    Beat(24, show = setOf("entra"), light = setOf("entra"), pulse = "ad" to "entra", card = syncCard),
                    Beat(28, show = setOf("phs", "note"), light = setOf("phs"), card = phsCard),
                    Beat(32, show = setOf("pta"), light = setOf("pta"), card = ptaCard),
                    Beat(34, show = setOf("fed"), light = setOf("fed"), card = BoardCard("Federation (AD FS)", "Sign-in on your own servers. Only when Entra can't do it.")),
                ),
            ),
            // 35: "Now outsiders. Two Tracks hires that translation agency..."
            Scene(
                35, "Outsiders", guests(),
                beats = listOf(
                    Beat(36, show = setOf("guest"), light = setOf("guest"), pulse = "guest" to "tt", card = b2bCard),
                    Beat(38, show = setOf("ext", "users", "b2c"), light = setOf("ext"), pulse = "users" to "ext", card = extCard),
                ),
            ),
            // 39: "Last piece. The backend itself has to read storage..."
            Scene(
                39, "Apps sign in too", badge(),
                beats = listOf(
                    Beat(40, show = setOf("mi", "sp"), light = setOf("mi"), pulse = "api" to "mi", card = miCard),
                    Beat(44, show = setOf("sys", "ua"), light = setOf("sys", "ua"), pulse = "api" to "st", card = badgesCard),
                ),
            ),
            // 45: "So what do I remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("phs", "pta", "ca", "b2b", "mi"), card = BoardCard("Listen for the phrase", "Effort, lockout, location, partners, secrets: one answer each.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-identity--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Admin Who Wasn't..."
            Scene(
                0, "The Admin Who Wasn't", overview(),
                beats = listOf(
                    Beat(0, show = setOf("user"), light = setOf("user"), card = BoardCard("The case", "Someone signed in as Sam, an admin. Sam was asleep.")),
                    Beat(2, show = setOf("entra"), light = setOf("entra"), card = entraCard),
                    Beat(4, show = setOf("apps"), light = setOf("apps"), pulse = "ca" to "apps", card = tokenCard),
                ),
            ),
            // 5: "Then my first suspect: Sam's password was too short..."
            Scene(
                5, "Clue one", doorman(),
                beats = listOf(
                    Beat(6, show = setOf("signin", "mfa"), light = setOf("mfa"), card = BoardCard("Clue one", "No second proof. MFA: a stolen password alone isn't enough.")),
                    Beat(8, show = setOf("ca", "allow", "block"), light = setOf("ca"), pulse = "signin" to "ca", card = BoardCard("Conditional Access", "Admins, or outside the office: MFA. The office: a named location.")),
                    Beat(10, show = setOf("defaults"), light = setOf("defaults"), card = BoardCard("Security defaults", "Free, but all or nothing. Conditional Access needs P1.")),
                    Beat(12, show = setOf("idp"), light = setOf("idp"), card = idpCard),
                ),
            ),
            // 13: "Second clue?"
            Scene(
                13, "Clue two", hashSync(),
                beats = listOf(
                    Beat(14, show = setOf("ad"), light = setOf("ad"), card = BoardCard("Clue two", "The office servers crashed; nobody could sign in anywhere.")),
                    Beat(16, show = setOf("fed"), light = setOf("fed"), card = BoardCard("Federation (AD FS)", "Every sign-in on your own servers. They fall, everyone falls.")),
                    Beat(18, show = setOf("entra"), light = setOf("entra"), pulse = "ad" to "entra", card = BoardCard("Hybrid identity", "The same people in the office directory and in Entra ID.")),
                    Beat(20, show = setOf("phs", "note"), light = setOf("phs"), card = phsCard),
                    Beat(24, show = setOf("pta"), light = setOf("pta"), card = ptaCard),
                ),
            ),
            // 25: "Third clue. I'm ready."
            Scene(
                25, "Clue three", guests(),
                beats = listOf(
                    Beat(26, card = BoardCard("Clue three", "A partner's cloud account outlived the contract.")),
                    Beat(28, show = setOf("guest"), light = setOf("guest"), pulse = "guest" to "tt", card = BoardCard("B2B guest", "Their own company's account. They leave; their way in leaves.")),
                    Beat(30, show = setOf("ext", "users", "b2c"), light = setOf("ext"), pulse = "users" to "ext", card = extCard),
                ),
            ),
            // 33: "Fourth witness?"
            Scene(
                33, "The fourth witness", badge(),
                beats = listOf(
                    Beat(34, show = setOf("sp"), light = setOf("sp"), card = BoardCard("Service principal", "An app's own account, its secret pasted into settings.")),
                    Beat(36, show = setOf("mi", "sys", "ua"), light = setOf("mi"), pulse = "api" to "mi", card = miCard),
                ),
            ),
            // 37: "And where does that stop working?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, light = setOf("laptop", "gh", "phone"), pulse = "phone" to "api", card = stopsCard),
                ),
            ),
            // 41: "Any last witness?"
            Scene(
                41, "The last witness", overview(),
                beats = listOf(
                    Beat(42, light = setOf("user"), card = groupsCard),
                ),
            ),
            // 43: "So, the case summary for the exam?"
            Scene(
                43, "Case closed", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("phs", "pta", "ca", "b2b", "mi"), card = BoardCard("Listen for the phrase", "Location, effort, lockout, partners, secrets: one answer each.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-identity--gossip",
        listOf(
            // 0: "Okay, did you hear what happened at Two Tracks this week?..."
            Scene(
                0, "A week of sign-ins", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Five people, five mistakes", "Two Tracks rebuilds how everyone signs in.")),
                    Beat(2, show = setOf("entra", "apps"), light = setOf("entra"), card = BoardCard("Microsoft Entra ID", "The ID-card office. Its tokens are passes, like wristbands.")),
                    Beat(4, light = setOf("entra"), card = tenantCard),
                ),
            ),
            // 5: "Okay. So who went first?"
            Scene(
                5, "Monday: Tom", hashSync(),
                beats = listOf(
                    Beat(6, show = setOf("ad"), light = setOf("ad"), card = BoardCard("Monday: Tom", "Bring the office's Active Directory staff into the cloud.")),
                    Beat(8, show = setOf("entra", "fed"), light = setOf("fed"), pulse = "ad" to "entra", card = BoardCard("Cloud Sync, then AD FS", "A good sync choice. Then federation, because it sounded enterprise.")),
                    Beat(10, light = setOf("fed"), card = BoardCard("Locked out", "The office servers crashed; nobody could sign in.")),
                    Beat(12, show = setOf("phs", "note"), light = setOf("phs"), card = phsCard),
                ),
            ),
            // 15: "Tuesday?"
            Scene(
                15, "Tuesday: Dana", hashSync(),
                beats = listOf(
                    Beat(16, light = setOf("pta"), card = ptaCard),
                    Beat(18, light = setOf("phs", "pta"), card = BoardCard("Keep hash sync on", "The backup, and the way Entra spots leaked passwords.")),
                ),
            ),
            // 19: "Wednesday?"
            Scene(
                19, "Wednesday: Marcus", guests(),
                beats = listOf(
                    Beat(20, card = BoardCard("Wednesday: Marcus", "New accounts for translators, passwords sent by email.")),
                    Beat(22, show = setOf("guest"), light = setOf("guest"), pulse = "guest" to "tt", card = b2bCard),
                    Beat(24, show = setOf("ext", "users", "b2c"), light = setOf("ext"), pulse = "users" to "ext", card = extCard),
                ),
            ),
            // 25: "Thursday, I'm guessing, the intern."
            Scene(
                25, "Thursday: the intern", badge(),
                beats = listOf(
                    Beat(26, show = setOf("sp"), light = setOf("sp"), card = BoardCard("Thursday: the intern", "A service principal's secret, pasted into app settings.")),
                    Beat(28, show = setOf("mi"), light = setOf("mi"), pulse = "api" to "mi", card = miCard),
                    Beat(30, show = setOf("sys", "ua"), light = setOf("sys", "ua"), pulse = "api" to "st", card = badgesCard),
                ),
            ),
            // 31: "And Friday? There's always a Friday."
            Scene(
                31, "Friday: Priya", doorman(),
                beats = listOf(
                    Beat(32, show = setOf("signin", "mfa"), light = setOf("mfa"), card = mfaCard),
                    Beat(34, show = setOf("ca", "allow", "block"), light = setOf("ca"), pulse = "signin" to "ca", card = caCard),
                    Beat(36, card = groupsCard),
                ),
            ),
            // 37: "And where does all this stop working?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, light = setOf("laptop", "gh", "phone"), pulse = "phone" to "api", card = stopsCard),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("phs", "pta", "ca", "b2b", "mi"), card = BoardCard("Listen for the phrase", "One colleague, one lesson, one answer.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-identity--late-night",
        listOf(
            // 0: "Hey, you. It's late, and it's just us..."
            Scene(
                0, "Who gets in tonight", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Who gets in, and how they prove it.")),
                    Beat(2, show = setOf("entra", "user"), light = setOf("entra"), card = entraCard),
                    Beat(4, show = setOf("apps"), light = setOf("apps"), pulse = "ca" to "apps", card = tokenCard),
                    Beat(6, light = setOf("entra"), card = tenantCard),
                ),
            ),
            // 7: "Now, proving it's really me. A password?"
            Scene(
                7, "Proving it's you", doorman(),
                beats = listOf(
                    Beat(8, show = setOf("signin", "mfa"), light = setOf("mfa"), card = mfaCard),
                    Beat(10, show = setOf("ca", "allow", "block"), light = setOf("ca"), pulse = "signin" to "ca", card = caCard),
                    Beat(12, show = setOf("defaults"), light = setOf("defaults"), card = BoardCard("Security defaults", "One free switch, all or nothing. Or passwordless: nothing to type.")),
                    Beat(14, show = setOf("idp"), light = setOf("idp"), card = idpCard),
                ),
            ),
            // 15: "Now the office people..."
            Scene(
                15, "The office", hashSync(),
                beats = listOf(
                    Beat(16, show = setOf("ad"), light = setOf("ad"), card = adCard),
                    Beat(18, show = setOf("entra"), light = setOf("entra"), pulse = "ad" to "entra", card = syncCard),
                    Beat(20, show = setOf("phs", "note"), light = setOf("phs"), card = phsCard),
                    Beat(22, show = setOf("pta", "fed"), light = setOf("pta"), card = BoardCard("Pass-through", "Live checks, so lockout applies at once. Federation only when forced.")),
                ),
            ),
            // 25: "Now the visitors..."
            Scene(
                25, "The visitors", guests(),
                beats = listOf(
                    Beat(26, show = setOf("guest"), light = setOf("guest"), pulse = "guest" to "tt", card = b2bCard),
                    Beat(28, show = setOf("ext", "users", "b2c"), light = setOf("ext"), pulse = "users" to "ext", card = extCard),
                ),
            ),
            // 29: "And the backend itself? It reads storage..."
            Scene(
                29, "No whispered passwords", badge(),
                beats = listOf(
                    Beat(30, show = setOf("mi"), light = setOf("mi"), pulse = "api" to "mi", card = miCard),
                    Beat(32, show = setOf("sp", "sys", "ua"), light = setOf("sp"), card = BoardCard("Service principal", "An app's own account, with a secret someone must store and renew.")),
                ),
            ),
            // 33: "Where does all of this stop working, Nova?"
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, light = setOf("laptop", "gh", "phone"), pulse = "phone" to "api", card = stopsCard),
                ),
            ),
            // 35: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("phs", "pta", "ca", "b2b", "mi"), card = BoardCard("Listen for the phrase", "Five little things, one answer each.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-identity--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Who you are, and how you prove it.")),
                    Beat(2, show = setOf("entra", "user"), light = setOf("entra"), card = entraCard),
                    Beat(6, show = setOf("apps"), light = setOf("apps"), pulse = "ca" to "apps", card = tokenCard),
                    Beat(8, light = setOf("entra"), card = tenantCard),
                ),
            ),
            // 10: "Now, my first question, exam style..."
            Scene(
                10, "First question", hashSync(),
                beats = listOf(
                    Beat(10, show = setOf("ad", "entra"), light = setOf("ad"), pulse = "ad" to "entra", card = BoardCard("Quiz: the default", "The same password in the cloud: Microsoft's default?")),
                    Beat(11, show = setOf("phs", "note"), light = setOf("phs"), card = BoardCard("Right answer", "Password hash sync: least to run, works when the office is down.")),
                    Beat(13, card = BoardCard("Quiz: lockout at once", "Refused at once, and no servers facing the internet. Which?")),
                    Beat(14, show = setOf("fed"), light = setOf("fed"), card = BoardCard("Federation? No kiss", "It needs servers the internet can reach; most to fail.")),
                    Beat(15, show = setOf("pta"), light = setOf("pta"), card = ptaCard),
                ),
            ),
            // 16: "Mm. Fair. Then here's mine. Two Tracks wants MFA..."
            Scene(
                16, "Knowing when to ask", doorman(),
                beats = listOf(
                    Beat(16, show = setOf("signin", "mfa"), light = setOf("mfa"), card = BoardCard("Quiz: MFA outside", "MFA for the portal from outside the office, not inside.")),
                    Beat(17, show = setOf("ca", "allow", "block"), light = setOf("ca"), pulse = "signin" to "ca", card = BoardCard("Right answer", "Conditional Access, the office as a named location. Needs P1.")),
                    Beat(20, show = setOf("defaults", "idp"), light = setOf("defaults", "idp"), card = BoardCard("Defaults and risk", "Defaults: free, all or nothing. Risky sign-ins: ID Protection, P2.")),
                ),
            ),
            // 21: "My question. A translation agency in Australia..."
            Scene(
                21, "Guests and strangers", guests(),
                beats = listOf(
                    Beat(21, card = BoardCard("Quiz: the translators", "Each has a work account at the agency. How do they get in?")),
                    Beat(22, show = setOf("guest"), light = setOf("guest"), pulse = "guest" to "tt", card = BoardCard("Right answer", "B2B guests: their own accounts, never our passwords.")),
                    Beat(24, show = setOf("users"), light = setOf("users"), card = BoardCard("Quiz: app users", "Millions, signing up with email or Google. Where?")),
                    Beat(25, show = setOf("ext", "b2c"), light = setOf("ext"), pulse = "users" to "ext", card = BoardCard("Right answer", "External ID, a customer tenant. B2C is the older product.")),
                ),
            ),
            // 27: "Last quiz. The backend reads storage..."
            Scene(
                27, "No secrets between us", badge(),
                beats = listOf(
                    Beat(27, card = BoardCard("Quiz: no credentials", "The backend reads storage; nothing to store or rotate.")),
                    Beat(28, show = setOf("mi"), light = setOf("mi"), pulse = "api" to "mi", card = BoardCard("Right answer", "A managed identity, with one narrow storage role.")),
                    Beat(30, show = setOf("sp", "sys", "ua"), light = setOf("sp"), card = BoardCard("Service principal", "A secret someone pastes into settings and forgets to renew.")),
                ),
            ),
            // 31: "And where does it all stop working? Every romance has its limits."
            Scene(
                31, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(32, light = setOf("laptop", "gh", "phone"), pulse = "phone" to "api", card = stopsCard),
                ),
            ),
            // 33: "Last question, for the exam. What do we remember?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Six for six: one answer each.")),
                    Beat(37, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
