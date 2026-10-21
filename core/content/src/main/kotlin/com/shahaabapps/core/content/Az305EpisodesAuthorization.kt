package com.shahaabapps.core.content

/** Authorizing access: roles, on-premises access and secrets: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesAuthorization {
    private val classic = Podcast(
        "sa-authorization",
        listOf(
            maya("Hi, and welcome back. Today is authorization: deciding who may do what in Azure, Microsoft's cloud, where you rent computers and services by the hour. Our story is Two Tracks goes global. The Android app is now a company, with teams, a backend on Azure, users in Europe and Australia, and an old timesheet system still running in the office."),
            leo("Big step. But isn't authorization just signing in?"),
            maya("Signing in is a different job. Picture an office building. Your badge proves your name at the door: that's signing in. Which doors the badge opens is a separate list. That list is authorization."),
            leo("And in Azure, that list is called?"),
            maya("Azure RBAC. RBAC stands for role-based access control: rules saying which person or app may do which actions on which things in Azure. Each rule is a role assignment, and it joins three things. Who, what, and where."),
            leo("Let's go one at a time. Who?"),
            maya("Anyone or anything that can sign in. A user, like you. A group, like the mobile team. Or an app. An app's neatest option is a managed identity, a badge Azure makes for the app and looks after itself, so no password is ever stored."),
            leo("And what?"),
            maya("The role: a named list of allowed actions, like a job description. Owner can do everything, including giving access to others. Contributor can build and change everything, but can't give access. Reader looks and changes nothing. User Access Administrator only manages who has access."),
            leo("Are there smaller roles than those?"),
            maya("Yes, service roles, for one kind of thing. For example, Virtual Machine Contributor manages virtual machines and nothing else. A virtual machine, a VM, is a computer you rent in Azure by the hour."),
            leo("Okay. And where?"),
            maya("The scope: where the role applies. There are four sizes. The smallest is one single resource, like one VM or one database. Next up is a resource group: a box of things that live and die together, like the Two Tracks backend and its database."),
            leo("And above the box?"),
            maya("A subscription: one Azure account with its own bill, like one apartment with its own electricity meter. And above that, a management group, which is a folder of subscriptions, like the whole apartment block."),
            leo("So if I give someone a role on the box?"),
            maya("It flows down to everything inside. Access at a scope is inherited below it, the way a floor key opens every room on that floor. Reader on a subscription reads every resource group in it."),
            leo("Then easiest is Owner for everyone, on the subscription. Fewer tickets. [chuckle]"),
            maya("[sigh] That's the classic mistake. The rule is least privilege: the smallest role at the narrowest scope that still does the job. Say you're building the Two Tracks support team. They restart the backend's VMs in one resource group, and nothing else."),
            leo("So Virtual Machine Contributor, on that one resource group."),
            maya("Exactly. And give it to a group, not to each person. Make the support team a group in Entra, so a joiner or a leaver changes one membership, not ten assignments."),
            leo("Entra being, in plain words?"),
            maya("Microsoft Entra ID is Azure's sign-in system. Think of the reception desk of a big office: it keeps the list of every person, group and app, and checks who is who."),
            leo("And if no built-in role fits the job?"),
            maya("You write a custom role: your own list of actions, in a short text file that also says where it may be assigned. One more split. Managing a thing, like building or knocking down a shed, is the control plane. What's inside the shed, like reading a file or a secret, is the data plane, with its own data roles."),
            leo("Here's what confuses me. Our boss is a Global Administrator. Doesn't she see everything?"),
            maya("[pause] No, and the exam loves this. There are two kinds of role. Entra roles run the directory: users, groups, licences. Global Administrator is one. Azure roles run resources: VMs, storage, databases. A Global Administrator has no Azure role by default, so she sees no VMs at all."),
            leo("Can she fix that herself?"),
            maya("She can elevate herself to User Access Administrator at the very top, then grant access. But it's a deliberate step, never a default."),
            leo("Now, admins. Somebody must hold Owner on production."),
            maya("Somebody must be able to. But standing admin rights are a gift to anyone who steals one password. So we use PIM, Privileged Identity Management. It makes a powerful role something you switch on for a short time, like borrowing the master key from reception and signing it back."),
            leo("How does that work, step by step?"),
            maya("A person is made eligible for a role, not active. When they need it, they activate it for a few hours. They give a reason, pass multifactor sign-in, a second proof such as a code on their phone, and, if set, wait for an approver. Every activation is logged, and it ends on its own."),
            leo("Is it free?"),
            maya("No. It needs a paid licence, Microsoft Entra ID P2 or Microsoft Entra ID Governance. And it covers Entra roles, Azure roles and group membership."),
            leo("Now the old timesheet app, on a server in the office."),
            maya("On-premises, we say: in your own building, not the cloud. First, one account for both worlds. A sync tool, Entra Connect Sync or Cloud Sync, copies the office directory, called Active Directory, into Entra ID."),
            leo("And remote staff reach the app over a VPN?"),
            maya("A VPN is a private tunnel into the office network, and it works. But application proxy removes the need. A small program, the private network connector, sits inside the office and only ever calls out to the cloud, like a receptionist who phones out but never unlocks the front door."),
            leo("So no inbound firewall port. Who checks the user?"),
            maya("Entra, first, with multifactor sign-in and Conditional Access, its if-then sign-in rules. Only then does the request go in, and the connector signs the user in to the app, even one using Kerberos, the built-in Windows sign-in."),
            leo("Last piece. Secrets. The database password, the keys."),
            maya("They go in Azure Key Vault, a locked safe for three things. A secret is a password or connection string. A key encrypts, which means scrambles, or signs data. A certificate proves a website is who it says, the padlock in your browser."),
            leo("And the safe comes in sizes?"),
            maya("Three. Standard keeps keys in software. Premium keeps them in an HSM, a hardware security module: a tamper-proof box that does the maths inside, so the key never leaves. Premium's boxes are shared between customers, and pass FIPS 140-3 Level 3, a US government test for such boxes."),
            leo("And the third?"),
            maya("Managed HSM: a pool of boxes for you alone, single-tenant. You hold its security domain, the only spare key to the safe. But it holds keys only. Picture a small app needing a signing key and a database password: Premium holds both, Managed HSM can't hold the password."),
            leo("And the backend reads the vault how?"),
            maya("With its managed identity, and the Key Vault Secrets User role, which reads secrets and nothing else. One vault per app, per environment. And purge protection on, so a deleted secret can't be erased early by anyone."),
            leo("So what do I remember for the exam?"),
            maya("If the question says one resource group only, that narrow scope. If no built-in role fits, a custom role. If it says users and licences, an Entra role. If admin rights only when needed, with approval, PIM. If an internal web app with no VPN, application proxy. If HSM at low cost, Premium; single-tenant, Managed HSM."),
            leo("And anything to try today?"),
            maya("Fifteen minutes, free. Create a resource group and assign a test group Reader on it. Open Access control, then Check access, and confirm what that group can do. Find the Virtual Machine Contributor role and read its actions list. Then create a Key Vault with RBAC, grant yourself Key Vault Secrets User, and read a secret back."),
            leo("Smallest key, narrowest door. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-authorization",
        listOf(
            maya("Welcome to Case Files. Today's case: The Door Left Open. Two Tracks goes global: the Android app is now a company, with a backend on Azure, Microsoft's cloud. On Sunday night someone signed in as Sam, an engineer, and deleted a whole resource group, a box of related things in Azure. Production went dark."),
            leo("Detective Leo, on the case. Sam did it. Case closed. [chuckle]"),
            maya("Sam was asleep. Clue one: Sam's password was in a leaked list from another website. Someone simply signed in as Sam."),
            leo("Then my suspect is the password. Make everyone's longer."),
            maya("[pause] Wrong suspect, or only a small one. A stronger password helps, but the real question is why one stolen password could delete production. Clue two: Sam was Owner on the whole subscription."),
            leo("Subscription being, in plain words?"),
            maya("One Azure account with its own bill, like one apartment with its own meter. Owner is a role, a named list of allowed actions, like a job description. Owner can do everything, including deleting anything and giving access to others."),
            leo("And Sam needed all that?"),
            maya("Sam restarts the backend's virtual machines, the computers Azure rents you by the hour. In one resource group. That's all. The design broke the rule called least privilege: the smallest role at the narrowest scope that still does the job."),
            leo("Scope?"),
            maya("Where a role applies. Four sizes: one resource, a resource group, a subscription, or a management group, a folder of subscriptions. And it's inherited downwards, like a floor key opening every room on its floor. Owner on the subscription opened every room."),
            leo("So what should Sam have had?"),
            maya("Virtual Machine Contributor, a service role that manages virtual machines and nothing else, on that one resource group. Who, which role, and where, joined together, is a role assignment. The whole system is Azure RBAC, role-based access control."),
            leo("What is a role assignment, really? A setting somewhere?"),
            maya("Under the picture, it's a small record Azure keeps: who gets access, which role, and which scope. The who is a user, a group, or an app."),
            leo("And how does Azure use that record? Is it magic?"),
            maya("Not magic. Every change you ask for, deleting, restarting, goes through Azure Resource Manager, the front desk for all management requests. It checks who you are, then looks for a role assignment at that scope or any scope above. No match, and the request is refused."),
            leo("So with the right design, the thief, signed in as Sam, could only restart machines. Annoying, not fatal."),
            maya("Exactly. And the role should go to a group, not to Sam himself, so joiners and leavers change one membership. The groups live in Microsoft Entra ID, which is Azure's sign-in system, the reception desk that keeps the list of every person, group and app."),
            leo("Clue three, then. Why did nobody see it sooner?"),
            maya("A witness. The boss, Rita, is a Global Administrator. She rushed to look at the machines on Sunday night, and saw nothing. Not a single one."),
            leo("Suspect: the thief hid them."),
            maya("Nobody hid them. Global Administrator is an Entra role. Entra roles run the directory: users, groups, licences. Azure roles run resources. A Global Administrator has no Azure role by default, so she sees no machines."),
            leo("So she was locked out of her own company?"),
            maya("Not quite. She can elevate herself to User Access Administrator at the very top and grant access. A deliberate step, not a default. That's also where Azure RBAC stops: it never manages the directory. And it never says what may exist; that's Azure Policy, in the governance episode."),
            leo("Clue four. I can feel it. Owner rights in general."),
            maya("Right. Even after the fix, three engineers kept standing Owner on production: there all day, every day, for emergencies. Standing admin rights are a gift to anyone with a stolen password."),
            leo("But someone needs Owner at three in the morning."),
            maya("Then make them eligible, not active, with PIM, Privileged Identity Management. When they need it, they activate the role for a few hours. They give a reason, pass multifactor sign-in, a second proof like a code on their phone, and, if set, wait for an approver."),
            leo("And afterwards?"),
            maya("The role expires on its own, and every activation is logged. Like borrowing the master key from reception and signing it back. It needs a paid licence, Microsoft Entra ID P2 or ID Governance."),
            leo("Last clue. How did the thief find Sam's name at all?"),
            maya("The old timesheet app, on a server in the office, on-premises. To let remote staff reach it, the team opened an inbound port in the office firewall. A door to the internet. The thief walked in and read the staff list."),
            leo("Suspect: they should have used a VPN."),
            maya("A VPN, a private tunnel into the office network, would work. But Entra application proxy is the better answer. A small program, the private network connector, sits inside the office and only calls out to the cloud. Like a receptionist who phones out but never unlocks the front door."),
            leo("So nothing comes in uninvited. Who checks the user?"),
            maya("Entra, first, with multifactor sign-in and Conditional Access, its if-then sign-in rules. Then the connector signs the user in to the app, even an old one using Kerberos, the Windows sign-in. And Entra Connect Sync copies office accounts into Entra, so it's one account for both."),
            leo("One more thing in the file. A database password, sitting in a plain app setting?"),
            maya("[sigh] Yes. The team had picked Managed HSM for their keys: tamper-proof hardware boxes, HSMs, used by you alone. Then they found it holds keys only. No secrets. So the password sat in a setting."),
            leo("What should they have picked?"),
            maya("Azure Key Vault, the safe for secrets, keys and certificates. Standard keeps keys in software. Premium keeps them in shared HSMs that pass FIPS 140-3 Level 3, a US government test. Managed HSM is single-tenant, with a security domain you hold, the only spare key. Nobody asked for that."),
            leo("So Premium, read by the backend's managed identity."),
            maya("Yes. A managed identity is a badge Azure makes for the app, with no password stored. Give it Key Vault Secrets User, which reads secrets and nothing else. The Key Vault episode goes deeper."),
            leo("So, the case summary for the exam?"),
            maya("Only one resource group: that narrow scope, with a service role. Users and licences: an Entra role. Admin rights only when needed, with approval: PIM. Internal web app, no VPN, no inbound port: application proxy. HSM-protected keys at low cost: Premium. Single-tenant, your own security domain: Managed HSM."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a resource group and assign a test group Reader on it. Open Access control, then Check access, and confirm what that group can do. Read the Virtual Machine Contributor actions list. Then create a Key Vault with RBAC and grant yourself Key Vault Secrets User."),
            leo("No more doors left open. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-authorization",
        listOf(
            maya("Okay, did you hear about the Two Tracks platform team's week? Sit down. Two Tracks is going global: the Android app is a company now, with a backend on Azure, Microsoft's cloud, and the team spent the week deciding who may do what. That's authorization. And it was a week."),
            leo("[laugh] I heard someone deleted something. Start at the start. Authorization, in plain words?"),
            maya("Picture an office. Your badge proves your name at the door. Which doors it opens is a separate list. In Azure that list is Azure RBAC, role-based access control: rules for who may do which actions on which things."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. Tom needed to restart the backend's virtual machines, the computers Azure rents by the hour. So he gave himself Owner on the whole subscription."),
            leo("Is that bad? Sounds efficient."),
            maya("A subscription is one Azure account with its own bill, like one apartment with its own meter. Owner is a role, a list of allowed actions, like a job description, and Owner's list is everything. Including deleting the lot, and giving access to others."),
            leo("And he only needed?"),
            maya("Virtual Machine Contributor, a service role that manages virtual machines and nothing else, on one resource group, which is a box of things that live and die together. The rule is least privilege: the smallest role at the narrowest scope that does the job."),
            leo("Scope?"),
            maya("Where the role applies: one resource, a resource group, a subscription, or a management group, a folder of subscriptions. It's inherited downwards, like a floor key opening every room on the floor. Who, what role, and where, joined together, is a role assignment."),
            leo("And what is a role assignment, really, under the picture?"),
            maya("A small record Azure keeps: who, which role, which scope. Every change request goes through Azure Resource Manager, the front desk for all management requests. It checks who you are, then looks for an assignment at that scope or above. None, and you're refused."),
            leo("Tuesday?"),
            maya("Dana. Dana fixed Tom's role, then went further: she put roles on groups, not people. The backend team is one group in Microsoft Entra ID, which is Azure's sign-in system, the reception desk that keeps the list of every person, group and app."),
            leo("Why groups?"),
            maya("Because when someone joins or leaves, you change one membership, not ten assignments. And when no built-in role fits, Dana writes a custom role: her own list of actions, in a short text file that says where it may be assigned."),
            leo("Wednesday. I'm guessing the boss."),
            maya("[chuckle] Rita. Global Administrator. She opened the portal to check Tom's machines, and saw nothing. Not one. She was furious for about ten minutes."),
            leo("Why couldn't she see them? She's the top admin."),
            maya("Of the directory. Global Administrator is an Entra role, and Entra roles run users, groups and licences. Azure roles run resources. A Global Administrator holds no Azure role by default. She can elevate herself to User Access Administrator at the very top, but it's a deliberate step."),
            leo("So that's where Azure RBAC stops. At the directory."),
            maya("And at what may exist: that's Azure Policy, which the governance episode covers. Also, reading a file inside storage is the data plane, with its own data roles. Managing the storage itself is the control plane."),
            leo("Thursday?"),
            maya("Marcus. Marcus held standing Owner on production. All day, every day, just in case. And on Thursday, a fake sign-in page caught his password."),
            leo("Oh no."),
            maya("Oh yes. Luckily, Priya had already moved him to PIM, Privileged Identity Management. He was only eligible for Owner, not active. So the stolen password got the thief nothing."),
            leo("How does eligible work, then?"),
            maya("When Marcus needs Owner, he activates it for a few hours. He gives a reason, passes multifactor sign-in, a second proof like a code on his phone, and waits for an approver. It's logged, and it expires on its own. It needs a paid licence, Entra ID P2 or ID Governance."),
            leo("Friday. Let me guess. The intern."),
            maya("The intern. Bless them. Remote staff needed the old timesheet app, which runs on a server in the office, on-premises. So the intern opened an inbound port in the office firewall. A door, straight from the internet."),
            leo("[sigh] And the fix?"),
            maya("Entra application proxy. A small program, the private network connector, sits inside the office and only calls out to the cloud, like a receptionist who phones out but never unlocks the front door. No VPN, the private tunnel into the office, and no inbound port."),
            leo("Who checks the user then?"),
            maya("Entra, first, with multifactor sign-in and Conditional Access, its if-then sign-in rules. Then the connector signs them in to the app, even an old one using Kerberos, the Windows sign-in. Entra Connect Sync copies office accounts up, so it's one account for both."),
            leo("Is that everyone? Where's Priya's mistake?"),
            maya("Saturday. Priya picked Managed HSM for the database password. A Managed HSM is a pool of tamper-proof hardware boxes, used by you alone, with a security domain you hold, the only spare key. And it holds keys only. No passwords."),
            leo("Priya made a mistake. Somebody write it down."),
            maya("She moved it the same day. Key Vault Premium holds secrets, keys and certificates, with keys in shared HSMs that pass FIPS 140-3 Level 3, a US government test. Standard keeps keys in software. Managed HSM only when the words say single-tenant."),
            leo("And the backend reads the password how?"),
            maya("With its managed identity, a badge Azure makes for the app, so no password is stored to fetch the passwords. It gets Key Vault Secrets User, which reads secrets and nothing else. The Key Vault episode has the rest."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Only one resource group: that narrow scope, like Tom should have done. No built-in role fits: a custom role. Users and licences: an Entra role, Rita's. Admin rights only when needed, with approval: PIM, Marcus's rescue. Internal web app, no VPN: application proxy. HSM at low cost: Premium; single-tenant: Managed HSM."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a resource group and assign a test group Reader on it. Open Access control, then Check access, and confirm what that group can do. Read the Virtual Machine Contributor actions list. Then create a Key Vault with RBAC, grant yourself Key Vault Secrets User, and read a secret back."),
            leo("Smallest role, narrowest scope. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-authorization",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about who gets the keys."),
            ivy("Mm. And who does get the keys, Nova?"),
            nova("That's authorization. Picture an office building at night. Your badge proves your name at the door. Which doors it opens is a separate list. Signing in is the badge. Authorization is the list."),
            ivy("And our list, tonight, is for Two Tracks."),
            nova("Two Tracks goes global. The little Android app is a company now, with teams, a backend on Azure, Microsoft's cloud, and users in Europe and Australia. In Azure, the list is Azure RBAC, which stands for role-based access control: who may do which actions, on which things."),
            ivy("Who, which actions, which things. Slowly."),
            nova("Who is anyone who can sign in: a person, a group, or an app. What is the role, a named list of allowed actions, like a job description. Owner does everything. Contributor builds and changes, but can't give access. Reader only looks."),
            ivy("And which things? Where?"),
            nova("The scope. [pause] One resource, like one database. Then a resource group, a box of things that live and die together. Then a subscription, one Azure account with its own bill. Then a management group, a folder of subscriptions."),
            ivy("And a role on the box reaches everything inside?"),
            nova("Inherited, all the way down, like a floor key that opens every room on the floor. That's why you choose carefully. Who, what and where together make a role assignment."),
            ivy("And what is that, really? Under the picture?"),
            nova("A small record Azure keeps: who, which role, which scope. Every change you ask for goes through Azure Resource Manager, the front desk for management requests. It checks who you are, then looks for an assignment at that scope or above. None, and the door stays shut."),
            ivy("So how generous should I be with the keys?"),
            nova("Not very. [chuckle] Least privilege: the smallest role at the narrowest scope that still does the job. The support team restarts the backend's virtual machines, computers rented by the hour, in one resource group. So, Virtual Machine Contributor, on that one box. Nothing more."),
            ivy("And I give it to each person on the team?"),
            nova("To a group. The support team is a group in Microsoft Entra ID, which is Azure's sign-in system, the reception desk with the list of every person, group and app. Someone joins or leaves, you change one membership. And if no built-in role fits, you write a custom role: your own list of actions."),
            ivy("Now, our boss is a Global Administrator. Surely she has every key in the building."),
            nova("[pause] Not these keys. Global Administrator is an Entra role, and Entra roles run the directory: users, groups, licences. Azure roles run resources. By default she holds none, so she sees no virtual machines at all. She can elevate herself to User Access Administrator at the top, on purpose."),
            ivy("So that's where the role rules end."),
            nova("At the directory. And at what may exist; that's Azure Policy, and the governance episode is your next late night for that. Also, reading the files inside storage is the data plane, with its own data roles."),
            ivy("And the admins. Who keeps the master key overnight?"),
            nova("Nobody, ideally. Standing admin rights are a gift to anyone who steals one password. So we use PIM, Privileged Identity Management. You're eligible for a role, not active. Like the master key that stays at reception until you sign it out."),
            ivy("And when I sign it out, late at night?"),
            nova("You activate it for a few hours. You give a reason, pass multifactor sign-in, a second proof like a code on your phone, and, if it's set, an approver says yes. [pause] Every activation is logged, and the role slips away on its own. It needs a paid licence, Entra ID P2 or ID Governance."),
            ivy("Mm. Now the old timesheet app, still on a server in the office."),
            nova("On-premises. In your own building, not the cloud. First, one account for both worlds: Entra Connect Sync copies the office directory, Active Directory, into Entra ID. Then, remote staff need a way in."),
            ivy("A VPN?"),
            nova("A VPN is a private tunnel into the office network. It works. But application proxy needs no tunnel and no open door. A small program, the private network connector, sits inside the office and only ever calls out. Like a night porter who makes calls, but never unlocks the front door."),
            ivy("And who checks me before I'm let through?"),
            nova("Entra, first, with multifactor sign-in and Conditional Access, its if-then sign-in rules. Then the connector signs you in to the old app, even one that uses Kerberos, the built-in Windows sign-in. No inbound firewall port, ever."),
            ivy("And the secrets. Where do the passwords sleep?"),
            nova("In Azure Key Vault, a locked safe for secrets, keys and certificates. A secret is a password. A key encrypts or signs. A certificate proves a website is really yours, the padlock in the browser."),
            ivy("And how strong is the safe?"),
            nova("Three sizes. Standard keeps keys in software. Premium keeps them in an HSM, a hardware security module, a tamper-proof box that does the maths inside, so the key never leaves. Premium's boxes are shared, and pass FIPS 140-3 Level 3, a US government test."),
            ivy("And the most private one?"),
            nova("Managed HSM. Boxes for you alone, single-tenant, and you hold the security domain, the only spare key. But it keeps keys only, no secrets or certificates. [pause] So the database password still needs an ordinary vault."),
            ivy("And the backend opens the safe with a password of its own?"),
            nova("With a managed identity, a badge Azure makes for the app, so there's no password at all. It gets Key Vault Secrets User, which reads secrets and nothing else. The Key Vault episode has more, for another night."),
            ivy("[sigh] Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says one resource group only, that narrow scope. No built-in role fits, a custom role. Users and licences, an Entra role. Admin rights only when needed, with approval, PIM. An internal web app with no VPN, application proxy. HSM at low cost, Premium. Single-tenant, Managed HSM."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a resource group and assign a test group Reader on it. Open Access control, then Check access, and confirm what that group can do. [pause] Read the Virtual Machine Contributor actions list. Then create a Key Vault with RBAC and grant yourself Key Vault Secrets User."),
            ivy("The smallest key, for the shortest time. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-authorization",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about who gets the keys."),
            rafe("To your place? [chuckle] I'd like to think I'm on the list."),
            nova("That's authorization: the list of doors your badge opens. Signing in only proves your name. In Azure, Microsoft's cloud, the list is Azure RBAC, which stands for role-based access control: who may do which actions, on which things."),
            rafe("And our things tonight belong to Two Tracks."),
            nova("Two Tracks goes global. The Android app is a company now, with teams, a backend on Azure, and users in Europe and Australia. Each rule is a role assignment: who, plus a role, which is a list of allowed actions like a job description, plus a scope, which is where."),
            rafe("Where being?"),
            nova("One resource, like a database. A resource group, a box of things that live and die together. A subscription, one Azure account with its own bill. Or a management group, a folder of subscriptions. And whatever you're given flows down to everything inside."),
            rafe("Like a floor key that opens every room. Dangerous, in the wrong hands."),
            nova("So here's my first question, exam style. The support team must restart the virtual machines, the rented computers, in one resource group, and nothing else. What do you give them?"),
            rafe("Virtual Machine Contributor, on that resource group. Given to their group, not to each person. Least privilege: the smallest role at the narrowest scope."),
            nova("Right answer. You never take more than you need. [kiss]"),
            rafe("Mm. And what is a role assignment, really? Love letters?"),
            nova("A small record Azure keeps: who, which role, which scope. Every change you ask for passes Azure Resource Manager, the front desk for management requests. It checks who you are and looks for an assignment at that scope or above. No match, no entry."),
            rafe("Then my question. Our boss is a Global Administrator, and she can't see a single virtual machine. Why?"),
            nova("A resource lock is hiding them."),
            rafe("No kiss for that one. A lock stops changes, it never hides anything. Microsoft Entra ID, Azure's sign-in system, is the reception desk with the list of every person and app. Global Administrator is an Entra role, and Entra roles run the directory: users, groups, licences. Azure roles run resources. She holds none by default."),
            nova("Fair. She can elevate herself to User Access Administrator at the top, on purpose. And that's where Azure RBAC stops: it never runs the directory, and it never decides what may exist. That's Azure Policy, in the governance episode."),
            rafe("And the admins? Someone holds Owner on production all night?"),
            nova("Not all night. Standing admin rights are a gift to anyone with a stolen password. So, PIM, Privileged Identity Management. You're eligible for a role, not active. You activate it for a few hours, with a reason, multifactor sign-in, a second proof like a code on your phone, and an approver."),
            rafe("Eligible, not active. Story of my youth. My question: admins may hold Owner only when needed, with approval, for at most four hours. What do you recommend?"),
            nova("PIM, with eligible assignments and approval. Every activation is logged, and the role expires on its own. It needs a paid licence, Entra ID P2 or ID Governance."),
            rafe("Right answer. Short, approved, and fully logged. [kiss]"),
            nova("Mm. Now, the old timesheet app, on a server in the office. On-premises, in our own building. Remote staff must reach it, signing in with Entra first, with no VPN, the private tunnel, and no inbound firewall port. What do you recommend?"),
            rafe("Entra application proxy. A small program, the private network connector, sits inside the office and only calls out to the cloud. Like me: I call, but I never let strangers in."),
            nova("Right answer, and very loyal. [kiss]"),
            rafe("And Entra checks them first, with multifactor sign-in and Conditional Access, its if-then sign-in rules. Then the connector signs them in to the app, even one using Kerberos, the Windows sign-in. And Entra Connect Sync copies office accounts up, so it's one account for both."),
            nova("Now secrets. Azure Key Vault is a locked safe for secrets, keys and certificates. A secret is a password, a key encrypts or signs, a certificate proves a website is really yours."),
            rafe("And the safe comes in sizes?"),
            nova("Three. Standard keeps keys in software. Premium keeps them in an HSM, a hardware security module: a tamper-proof box that does the maths inside, so the key never leaves. Premium's are shared between customers, and pass FIPS 140-3 Level 3, a US government test."),
            rafe("And the exclusive one?"),
            nova("Managed HSM: boxes for you alone, single-tenant. You hold the security domain, the only spare key. But it keeps keys only. Question for you, darling. A bank wants its encryption key in a single-tenant HSM whose security domain it controls. Which option?"),
            rafe("Managed HSM. And its app passwords go in an ordinary vault, because Managed HSM can't hold secrets."),
            nova("Right answer. Some things you keep only for yourself. [kiss]"),
            rafe("My turn. Keys must be HSM-protected, FIPS 140-3 Level 3, at the lowest cost. Which?"),
            nova("Premium. Shared boxes, low cost, and it holds the passwords too."),
            rafe("Right answer. Cheap date, high standards. [kiss]"),
            nova("[laugh] And the backend opens the vault with a managed identity, a badge Azure makes for the app, so no password is stored. It gets Key Vault Secrets User, which reads secrets and nothing else. The Key Vault episode goes deeper."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("One resource group only: that narrow scope. No built-in role fits: a custom role, your own list of actions. Users and licences: an Entra role. Admin rights when needed, with approval: PIM. Internal web app, no VPN: application proxy. HSM at low cost: Premium. Single-tenant: Managed HSM."),
            rafe("Seven for seven. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a resource group and assign a test group Reader on it. Open Access control, then Check access, and confirm what that group can do. Read the Virtual Machine Contributor actions list. Then create a Key Vault with RBAC and grant yourself Key Vault Secrets User."),
            nova("And you're the only one with the key to me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun assignment() = DiagramSpec(
        "A role assignment joins who, what and where; Azure Resource Manager checks every request against it, and it is inherited below its scope.",
        nodes = listOf(
            DNode("who", "Who", 0.2f, 0.15f, w = 0.34f, h = 0.2f, tone = 1, sub = "user, group or app"),
            DNode("what", "What: a role", 0.2f, 0.45f, w = 0.34f, h = 0.2f, tone = 1, sub = "allowed actions"),
            DNode("where", "Where: a scope", 0.2f, 0.75f, w = 0.34f, h = 0.2f, tone = 1, sub = "MG, sub, RG, resource"),
            DNode("arm", "Resource Manager", 0.72f, 0.15f, w = 0.4f, h = 0.2f, tone = 2, sub = "checks every request"),
            DNode("assign", "Role assignment", 0.72f, 0.45f, w = 0.4f, h = 0.2f, sub = "who + what + where"),
            DNode("note", "Inherited downwards", 0.72f, 0.78f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("who", "assign"), DEdge("what", "assign"), DEdge("where", "assign"),
            DEdge("arm", "assign", "looks up"), DEdge("assign", "note", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun scopes() = DiagramSpec(
        "Four sizes of scope, one inside the next; a role granted high up reaches everything below, so grant the smallest role on the smallest box.",
        nodes = listOf(
            DNode("mg", "Management group", 0.36f, 0.1f, w = 0.6f, h = 0.14f, tone = 2),
            DNode("sub", "Subscription", 0.36f, 0.32f, w = 0.6f, h = 0.14f, tone = 2),
            DNode("rg", "Resource group", 0.36f, 0.54f, w = 0.6f, h = 0.14f, tone = 1),
            DNode("res", "Resource", 0.36f, 0.76f, w = 0.6f, h = 0.14f),
            DNode("owner", "Owner: too wide", 0.83f, 0.32f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
            DNode("vmc", "VM Contributor", 0.83f, 0.54f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE, tone = 1),
            DNode("note", "Granted high, inherited below", 0.5f, 0.93f, w = 0.9f, h = 0.1f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("mg", "sub"), DEdge("sub", "rg"), DEdge("rg", "res"),
            DEdge("owner", "sub", dashed = true), DEdge("vmc", "rg"),
        ),
        aspect = 0.8f,
    )

    private fun planes() = DiagramSpec(
        "A custom role is your own list of actions; managing a thing is the control plane, what is inside it is the data plane.",
        nodes = listOf(
            DNode("custom", "Custom role", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "your own list of actions"),
            DNode("control", "Control plane", 0.26f, 0.5f, w = 0.42f, h = 0.2f, sub = "build, change, delete"),
            DNode("data", "Data plane", 0.76f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "read files, secrets"),
            DNode("note", "Data needs data roles", 0.5f, 0.84f, w = 0.8f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(DEdge("custom", "control"), DEdge("custom", "data", dashed = true), DEdge("data", "note")),
        aspect = 0.8f,
    )

    private fun kinds() = DiagramSpec(
        "Entra roles run the directory and Azure roles run resources; a Global Administrator holds no Azure role until they elevate.",
        nodes = listOf(
            DNode("entra", "Entra roles", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 2, sub = "the directory"),
            DNode("azure", "Azure roles", 0.75f, 0.15f, w = 0.42f, h = 0.2f, tone = 1, sub = "resources"),
            DNode("ga", "Global Administrator", 0.25f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "users, licences"),
            DNode("owner", "Owner, Contributor", 0.75f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "VMs, storage"),
            DNode("elevate", "Elevate: User Access Admin at the top", 0.5f, 0.84f, w = 0.84f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(DEdge("entra", "ga"), DEdge("azure", "owner"), DEdge("ga", "elevate", dashed = true)),
        aspect = 0.8f,
    )

    private fun pim() = DiagramSpec(
        "With PIM a person is eligible, not active; they activate a role for a few hours with a reason, multifactor sign-in and approval, and it is logged.",
        nodes = listOf(
            DNode("person", "On-call engineer", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("eligible", "Eligible", 0.45f, 0.2f, w = 0.3f, h = 0.2f, tone = 2, sub = "not active"),
            DNode("active", "Active", 0.82f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "a few hours"),
            DNode("checks", "Reason, MFA, approval", 0.45f, 0.55f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
            DNode("log", "Logged; expires by itself", 0.82f, 0.55f, w = 0.3f, h = 0.18f, kind = NodeKind.NOTE),
            DNode("lic", "Needs Entra ID P2 or ID Governance", 0.5f, 0.86f, w = 0.84f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("person", "eligible"), DEdge("eligible", "active", "activate"),
            DEdge("eligible", "checks"), DEdge("active", "log", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun appProxy() = DiagramSpec(
        "The user signs in to Entra first; the connector inside the office only calls out, so no inbound port is ever opened.",
        nodes = listOf(
            DNode("user", "Remote user", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("entra", "Entra sign-in", 0.5f, 0.2f, w = 0.36f, h = 0.2f, tone = 2, sub = "MFA, Conditional Access"),
            DNode("proxy", "App proxy service", 0.86f, 0.2f, w = 0.26f, h = 0.2f, tone = 1),
            DNode("fw", "No inbound port open", 0.14f, 0.58f, w = 0.24f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
            DNode("conn", "Private network connector", 0.5f, 0.58f, w = 0.44f, h = 0.2f, sub = "calls out only"),
            DNode("app", "Timesheet app", 0.86f, 0.58f, w = 0.24f, h = 0.2f, sub = "Kerberos"),
            DNode("sync", "Entra Connect Sync: one account", 0.5f, 0.88f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("user", "entra", "1 sign in"), DEdge("entra", "proxy"),
            DEdge("conn", "proxy", "outbound only"), DEdge("conn", "app", "signs in"),
        ),
        aspect = 0.8f,
    )

    private fun vault() = DiagramSpec(
        "Standard keeps keys in software, Premium in shared HSMs, Managed HSM in HSMs of your own that hold keys only; the backend reads with a managed identity.",
        nodes = listOf(
            DNode("std", "Standard", 0.18f, 0.2f, w = 0.3f, h = 0.22f, sub = "software keys"),
            DNode("prem", "Premium", 0.5f, 0.2f, w = 0.3f, h = 0.22f, tone = 1, sub = "shared HSMs"),
            DNode("mhsm", "Managed HSM", 0.82f, 0.2f, w = 0.3f, h = 0.22f, tone = 2, sub = "yours alone"),
            DNode("holds", "Secrets, keys, certificates", 0.34f, 0.52f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
            DNode("keysonly", "Keys only", 0.82f, 0.52f, w = 0.3f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
            DNode("api", "Two Tracks backend", 0.3f, 0.84f, w = 0.44f, h = 0.18f, tone = 1, sub = "managed identity"),
            DNode("role", "Secrets User", 0.76f, 0.84f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "reads secrets only"),
        ),
        edges = listOf(
            DEdge("std", "holds"), DEdge("prem", "holds"), DEdge("mhsm", "keysonly"),
            DEdge("api", "prem", "reads"), DEdge("api", "role"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's authorization questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("narrow", "One resource group only", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "that narrow scope"),
            DNode("custom", "No built-in role fits", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a custom role"),
            DNode("entra", "Users and licences", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "an Entra role"),
            DNode("pim", "Admin only when needed", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "PIM"),
            DNode("proxy", "Internal app, no VPN", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "application proxy"),
            DNode("hsm", "HSM: cheap or yours alone", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium or Managed HSM"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("narrow", "custom", "entra", "pim", "proxy", "hsm")
    private val today = BoardCard("Today", "Reader for a test group, Check access, the VM role's actions, a vault read.")
    private val phrase = BoardCard("Listen for the phrase", "Each requirement in the question points to one answer.")

    private fun classicBoard() = PodcastBoard(
        "sa-authorization",
        listOf(
            // 0: "Hi, and welcome back. Today is authorization..."
            Scene(
                0, "Who may do what", assignment(),
                beats = listOf(
                    Beat(0, card = BoardCard("Two Tracks goes global", "A company now: teams, a backend on Azure, an office timesheet app.")),
                    Beat(2, card = BoardCard("Badge and list", "The badge proves your name; a separate list says which doors open.")),
                    Beat(4, show = setOf("who", "what", "where", "assign"), light = setOf("assign"), card = BoardCard("Azure RBAC", "Role-based access control: who may do which actions, on what.")),
                    Beat(6, light = setOf("who"), pulse = "who" to "assign", card = BoardCard("Managed identity", "A badge Azure makes for an app. No password stored.")),
                    Beat(8, light = setOf("what"), pulse = "what" to "assign", card = BoardCard("A role", "A list of allowed actions: Owner, Contributor, Reader.")),
                    Beat(10, card = BoardCard("Virtual Machine Contributor", "A service role: manages VMs and nothing else.")),
                ),
            ),
            // 11: "Okay. And where?"
            Scene(
                11, "Four sizes of scope", scopes(),
                beats = listOf(
                    Beat(12, show = setOf("res", "rg"), light = setOf("rg"), card = BoardCard("Resource group", "A box of things that live and die together.")),
                    Beat(14, show = setOf("sub", "mg"), light = setOf("sub", "mg"), pulse = "mg" to "sub", card = BoardCard("Subscription, management group", "One account with its own bill; a folder of subscriptions.")),
                    Beat(16, show = setOf("note"), light = setOf("note"), pulse = "sub" to "rg", card = BoardCard("Inherited", "Like a floor key: every room on that floor.")),
                    Beat(18, show = setOf("owner", "vmc"), light = setOf("vmc", "rg"), pulse = "vmc" to "rg", card = BoardCard("Least privilege", "The smallest role at the narrowest scope that still does the job.")),
                    Beat(20, card = BoardCard("Assign to groups", "A joiner or a leaver changes one membership.")),
                    Beat(22, card = BoardCard("Microsoft Entra ID", "The reception desk: the list of people, groups and apps.")),
                ),
            ),
            // 23: "And if no built-in role fits the job?"
            Scene(
                23, "Your own list", planes(),
                beats = listOf(
                    Beat(23, show = setOf("custom"), light = setOf("custom"), card = BoardCard("No role fits?", "Write a custom role: your own list of actions.")),
                    Beat(24, show = setOf("control", "data", "note"), light = setOf("control", "data"), pulse = "custom" to "control", card = BoardCard("Control or data plane", "Managing the shed, or what's inside it: files and secrets.")),
                ),
            ),
            // 25: "Our boss is a Global Administrator..."
            Scene(
                25, "Two kinds of role", kinds(),
                beats = listOf(
                    Beat(26, show = setOf("entra", "azure", "ga", "owner"), light = setOf("ga"), pulse = "entra" to "ga", card = BoardCard("Global Administrator", "An Entra role: the directory. No Azure role by default.")),
                    Beat(28, show = setOf("elevate"), light = setOf("elevate"), pulse = "ga" to "elevate", card = BoardCard("Elevate, on purpose", "User Access Administrator at the very top.")),
                ),
            ),
            // 29: "Now, admins. Somebody must hold Owner on production."
            Scene(
                29, "Admin rights, just in time", pim(),
                beats = listOf(
                    Beat(30, show = setOf("person", "eligible"), light = setOf("eligible"), pulse = "person" to "eligible", card = BoardCard("PIM", "Privileged Identity Management: switch a role on, briefly.")),
                    Beat(32, show = setOf("active", "checks", "log"), light = setOf("checks", "active"), pulse = "eligible" to "active", card = BoardCard("Activate", "A reason, multifactor sign-in, an approver. A few hours.")),
                    Beat(34, show = setOf("lic"), light = setOf("lic"), card = BoardCard("A paid licence", "Microsoft Entra ID P2 or Microsoft Entra ID Governance.")),
                ),
            ),
            // 35: "Now the old timesheet app, on a server in the office."
            Scene(
                35, "The office timesheet app", appProxy(),
                beats = listOf(
                    Beat(36, show = setOf("sync"), light = setOf("sync"), card = BoardCard("One account, both worlds", "Entra Connect Sync copies Active Directory into Entra ID.")),
                    Beat(38, show = setOf("user", "conn", "proxy", "app", "fw"), light = setOf("conn"), pulse = "conn" to "proxy", card = BoardCard("Application proxy", "A connector inside the office that only calls out.")),
                    Beat(40, show = setOf("entra"), light = setOf("entra", "app"), pulse = "user" to "entra", card = BoardCard("Entra checks first", "Multifactor sign-in and Conditional Access, then the app.")),
                ),
            ),
            // 41: "Last piece. Secrets."
            Scene(
                41, "A safe for secrets", vault(),
                beats = listOf(
                    Beat(42, show = setOf("std", "holds"), light = setOf("holds"), pulse = "std" to "holds", card = BoardCard("Key Vault", "A locked safe for secrets, keys and certificates.")),
                    Beat(44, show = setOf("prem"), light = setOf("prem"), pulse = "prem" to "holds", card = BoardCard("Premium", "Keys in shared HSMs, FIPS 140-3 Level 3, at low cost.")),
                    Beat(46, show = setOf("mhsm", "keysonly"), light = setOf("mhsm", "keysonly"), pulse = "mhsm" to "keysonly", card = BoardCard("Managed HSM", "Single-tenant, your own security domain. Keys only.")),
                    Beat(48, show = setOf("api", "role"), light = setOf("api", "role"), pulse = "api" to "role", card = BoardCard("Key Vault Secrets User", "Read by managed identity: reads secrets, nothing else.")),
                ),
            ),
            // 49: "So what do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = all6, card = phrase),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-authorization--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Door Left Open..."
            Scene(
                0, "The Door Left Open", scopes(),
                beats = listOf(
                    Beat(0, show = setOf("rg"), light = setOf("rg"), card = BoardCard("The case", "Someone signed in as Sam and deleted a resource group.")),
                    Beat(2, card = BoardCard("Clue one", "Sam's password was in a leaked list from another website.")),
                    Beat(4, show = setOf("sub", "owner"), light = setOf("owner"), pulse = "owner" to "sub", card = BoardCard("Clue two", "Sam was Owner on the whole subscription.")),
                    Beat(6, light = setOf("sub"), card = BoardCard("Owner", "A role that can do everything, even give access to others.")),
                    Beat(8, show = setOf("vmc"), light = setOf("vmc", "rg"), pulse = "vmc" to "rg", card = BoardCard("Least privilege", "The smallest role at the narrowest scope that does the job.")),
                    Beat(10, show = setOf("mg", "res", "note"), light = setOf("note"), pulse = "sub" to "rg", card = BoardCard("Scope", "Inherited downwards, like a floor key for every room.")),
                ),
            ),
            // 11: "So what should Sam have had?"
            Scene(
                11, "How Azure decides", assignment(),
                beats = listOf(
                    Beat(12, show = setOf("who", "what", "where", "assign"), light = setOf("assign"), card = BoardCard("Role assignment", "Who, which role, which scope. The whole system: Azure RBAC.")),
                    Beat(14, light = setOf("who"), pulse = "who" to "assign", card = BoardCard("Under the picture", "A small record Azure keeps: who, which role, which scope.")),
                    Beat(16, show = setOf("arm", "note"), light = setOf("arm"), pulse = "arm" to "assign", card = BoardCard("Azure Resource Manager", "The front desk: checks every request against assignments.")),
                    Beat(18, light = setOf("who"), card = BoardCard("Groups, in Entra", "Roles on groups, not people. Entra ID is the reception desk.")),
                ),
            ),
            // 19: "Clue three, then."
            Scene(
                19, "Clue three", kinds(),
                beats = listOf(
                    Beat(20, show = setOf("ga"), light = setOf("ga"), card = BoardCard("The witness", "Rita, a Global Administrator, saw no machines at all.")),
                    Beat(22, show = setOf("entra", "azure", "owner"), light = setOf("entra", "azure"), pulse = "entra" to "ga", card = BoardCard("Two kinds of role", "Entra roles run the directory. Azure roles run resources.")),
                    Beat(24, show = setOf("elevate"), light = setOf("elevate"), pulse = "ga" to "elevate", card = BoardCard("Where RBAC stops", "Not the directory; not what may exist: that's Azure Policy.")),
                ),
            ),
            // 25: "Clue four. Owner rights in general."
            Scene(
                25, "Clue four", pim(),
                beats = listOf(
                    Beat(26, show = setOf("person", "active"), light = setOf("active"), card = BoardCard("Standing Owner", "Three engineers held it all day, every day.")),
                    Beat(28, show = setOf("eligible", "checks"), light = setOf("eligible", "checks"), pulse = "person" to "eligible", card = BoardCard("PIM", "Eligible, not active. Activate for hours, with approval.")),
                    Beat(30, show = setOf("log", "lic"), light = setOf("log"), pulse = "active" to "log", card = BoardCard("Logged, then gone", "Expires on its own. Needs Entra ID P2 or ID Governance.")),
                ),
            ),
            // 31: "Last clue."
            Scene(
                31, "The last clue", appProxy(),
                beats = listOf(
                    Beat(32, show = setOf("app", "fw"), light = setOf("app"), card = BoardCard("An inbound port", "Opened for the timesheet app: a door from the internet.")),
                    Beat(34, show = setOf("conn", "proxy"), light = setOf("conn", "fw"), pulse = "conn" to "proxy", card = BoardCard("Application proxy", "The connector only calls out. No inbound port.")),
                    Beat(36, show = setOf("user", "entra", "sync"), light = setOf("entra"), pulse = "user" to "entra", card = BoardCard("Entra checks first", "MFA and Conditional Access; Kerberos for old apps.")),
                ),
            ),
            // 37: "One more thing in the file."
            Scene(
                37, "One more thing", vault(),
                beats = listOf(
                    Beat(38, show = setOf("mhsm", "keysonly"), light = setOf("keysonly"), pulse = "mhsm" to "keysonly", card = BoardCard("Managed HSM", "Keys only, so the password sat in a plain setting.")),
                    Beat(40, show = setOf("std", "prem", "holds"), light = setOf("prem", "holds"), pulse = "prem" to "holds", card = BoardCard("Premium", "Secrets, keys and certificates; shared HSMs; low cost.")),
                    Beat(42, show = setOf("api", "role"), light = setOf("api", "role"), pulse = "api" to "role", card = BoardCard("Managed identity", "A badge for the app; Key Vault Secrets User to read.")),
                ),
            ),
            // 43: "So, the case summary for the exam?"
            Scene(
                43, "Case closed", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = all6, card = phrase),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-authorization--gossip",
        listOf(
            // 0: "Okay, did you hear about the Two Tracks platform team's week?"
            Scene(
                0, "The platform team's week", assignment(),
                beats = listOf(
                    Beat(0, card = BoardCard("Two Tracks goes global", "A week of deciding who may do what.")),
                    Beat(2, show = setOf("who", "what", "where", "assign"), light = setOf("assign"), card = BoardCard("Azure RBAC", "Role-based access control: who may do which actions, on what.")),
                ),
            ),
            // 3: "Okay. Who went first?"
            Scene(
                3, "Monday: Tom", scopes(),
                beats = listOf(
                    Beat(4, show = setOf("sub", "owner", "rg"), light = setOf("owner"), pulse = "owner" to "sub", card = BoardCard("Owner on everything", "Tom only needed to restart some machines.")),
                    Beat(6, show = setOf("mg"), light = setOf("sub"), card = BoardCard("Subscription", "One Azure account with its own bill.")),
                    Beat(8, show = setOf("vmc"), light = setOf("vmc", "rg"), pulse = "vmc" to "rg", card = BoardCard("Least privilege", "Virtual Machine Contributor, on one resource group.")),
                    Beat(10, show = setOf("res", "note"), light = setOf("note"), pulse = "sub" to "rg", card = BoardCard("Inherited", "Like a floor key: every room on the floor.")),
                ),
            ),
            // 11: "And what is a role assignment, really, under the picture?"
            Scene(
                11, "Under the picture", assignment(),
                beats = listOf(
                    Beat(12, show = setOf("who", "what", "where", "assign", "arm", "note"), light = setOf("arm"), pulse = "arm" to "assign", card = BoardCard("Azure Resource Manager", "Checks every request for an assignment at that scope or above.")),
                ),
            ),
            // 13: "Tuesday?"
            Scene(
                13, "Tuesday: Dana", planes(),
                beats = listOf(
                    Beat(14, card = BoardCard("Groups, in Entra", "Entra ID is the reception desk: people, groups and apps.")),
                    Beat(16, show = setOf("custom", "control"), light = setOf("custom"), pulse = "custom" to "control", card = BoardCard("Custom role", "Her own list of actions, when no built-in role fits.")),
                ),
            ),
            // 17: "Wednesday. I'm guessing the boss."
            Scene(
                17, "Wednesday: Rita", kinds(),
                beats = listOf(
                    Beat(18, show = setOf("ga"), light = setOf("ga"), card = BoardCard("No machines at all", "The Global Administrator saw nothing.")),
                    Beat(20, show = setOf("entra", "azure", "owner", "elevate"), light = setOf("entra", "azure"), pulse = "ga" to "elevate", card = BoardCard("Two kinds of role", "Entra roles: the directory. Azure roles: resources.")),
                    Beat(22, light = setOf("elevate"), card = BoardCard("Where RBAC stops", "What may exist is Azure Policy: the governance episode.")),
                ),
            ),
            // 23: "Thursday?"
            Scene(
                23, "Thursday: Marcus", pim(),
                beats = listOf(
                    Beat(24, show = setOf("person", "active"), light = setOf("active"), card = BoardCard("Standing Owner", "All day, every day. Then a fake page caught his password.")),
                    Beat(26, show = setOf("eligible"), light = setOf("eligible"), pulse = "person" to "eligible", card = BoardCard("Only eligible", "PIM: the stolen password got the thief nothing.")),
                    Beat(28, show = setOf("checks", "log", "lic"), light = setOf("checks", "log"), pulse = "eligible" to "active", card = BoardCard("Activate", "A reason, MFA, an approver; logged; expires on its own.")),
                ),
            ),
            // 29: "Friday. Let me guess. The intern."
            Scene(
                29, "Friday: the intern", appProxy(),
                beats = listOf(
                    Beat(30, show = setOf("app", "fw"), light = setOf("app"), card = BoardCard("An inbound port", "A door from the internet, for the timesheet app.")),
                    Beat(32, show = setOf("conn", "proxy"), light = setOf("conn", "fw"), pulse = "conn" to "proxy", card = BoardCard("Application proxy", "The connector only calls out. No VPN, no inbound port.")),
                    Beat(34, show = setOf("user", "entra", "sync"), light = setOf("entra"), pulse = "user" to "entra", card = BoardCard("Entra checks first", "MFA and Conditional Access, then Kerberos to the app.")),
                ),
            ),
            // 35: "Is that everyone? Where's Priya's mistake?"
            Scene(
                35, "Saturday: Priya", vault(),
                beats = listOf(
                    Beat(36, show = setOf("mhsm", "keysonly"), light = setOf("keysonly"), pulse = "mhsm" to "keysonly", card = BoardCard("Managed HSM", "Single-tenant, your own security domain. Keys only.")),
                    Beat(38, show = setOf("std", "prem", "holds"), light = setOf("prem"), pulse = "prem" to "holds", card = BoardCard("Premium", "Secrets, keys and certificates; shared HSMs; low cost.")),
                    Beat(40, show = setOf("api", "role"), light = setOf("api", "role"), pulse = "api" to "role", card = BoardCard("Managed identity", "Key Vault Secrets User: reads secrets, nothing else.")),
                ),
            ),
            // 41: "So, the gossip I should remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = all6, card = phrase),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-authorization--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Who gets the keys", assignment(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Who gets the keys.")),
                    Beat(2, card = BoardCard("Badge and list", "Signing in is the badge; authorization is the list.")),
                    Beat(4, show = setOf("who", "what", "where", "assign"), light = setOf("assign"), card = BoardCard("Azure RBAC", "Role-based access control: who may do which actions, on what.")),
                    Beat(6, light = setOf("who", "what"), pulse = "what" to "assign", card = BoardCard("Who and what", "A person, group or app; a role, a list of allowed actions.")),
                ),
            ),
            // 7: "And which things? Where?"
            Scene(
                7, "Where", scopes(),
                beats = listOf(
                    Beat(8, show = setOf("mg", "sub", "rg", "res"), light = setOf("rg"), card = BoardCard("Four sizes of scope", "Resource, resource group, subscription, management group.")),
                    Beat(10, show = setOf("note"), light = setOf("note"), pulse = "sub" to "rg", card = BoardCard("Inherited", "Like a floor key that opens every room on the floor.")),
                ),
            ),
            // 11: "And what is that, really? Under the picture?"
            Scene(
                11, "Under the picture", assignment(),
                beats = listOf(
                    Beat(12, show = setOf("who", "what", "where", "assign", "arm"), light = setOf("arm"), pulse = "arm" to "assign", card = BoardCard("Azure Resource Manager", "The front desk: no matching assignment, the door stays shut.")),
                ),
            ),
            // 13: "So how generous should I be with the keys?"
            Scene(
                13, "Least privilege", scopes(),
                beats = listOf(
                    Beat(14, show = setOf("mg", "sub", "rg", "res", "vmc"), light = setOf("vmc", "rg"), pulse = "vmc" to "rg", card = BoardCard("Least privilege", "Virtual Machine Contributor, on one box. Nothing more.")),
                    Beat(16, card = BoardCard("Groups, in Entra", "Entra ID is the reception desk. Change one membership.")),
                ),
            ),
            // 17: "Now, our boss is a Global Administrator."
            Scene(
                17, "Not these keys", kinds(),
                beats = listOf(
                    Beat(18, show = setOf("entra", "azure", "ga", "owner", "elevate"), light = setOf("ga"), pulse = "ga" to "elevate", card = BoardCard("Global Administrator", "An Entra role. No Azure role by default; elevate on purpose.")),
                    Beat(20, light = setOf("azure"), card = BoardCard("Where it ends", "Not the directory; not what may exist. Data plane: data roles.")),
                ),
            ),
            // 21: "And the admins. Who keeps the master key overnight?"
            Scene(
                21, "The master key", pim(),
                beats = listOf(
                    Beat(22, show = setOf("person", "eligible"), light = setOf("eligible"), pulse = "person" to "eligible", card = BoardCard("PIM", "Eligible, not active: the master key stays at reception.")),
                    Beat(24, show = setOf("active", "checks", "log", "lic"), light = setOf("active", "checks"), pulse = "eligible" to "active", card = BoardCard("Activate", "A reason, multifactor sign-in, an approver. Then it slips away.")),
                ),
            ),
            // 25: "Mm. Now the old timesheet app..."
            Scene(
                25, "The office at night", appProxy(),
                beats = listOf(
                    Beat(26, show = setOf("sync"), light = setOf("sync"), card = BoardCard("One account, both worlds", "Entra Connect Sync copies Active Directory into Entra ID.")),
                    Beat(28, show = setOf("conn", "proxy", "app", "fw"), light = setOf("conn"), pulse = "conn" to "proxy", card = BoardCard("Application proxy", "A night porter who calls out, but never unlocks the door.")),
                    Beat(30, show = setOf("user", "entra"), light = setOf("entra"), pulse = "user" to "entra", card = BoardCard("Entra checks first", "Multifactor sign-in and Conditional Access, then Kerberos.")),
                ),
            ),
            // 31: "And the secrets. Where do the passwords sleep?"
            Scene(
                31, "Where secrets sleep", vault(),
                beats = listOf(
                    Beat(32, show = setOf("holds"), light = setOf("holds"), card = BoardCard("Key Vault", "A locked safe for secrets, keys and certificates.")),
                    Beat(34, show = setOf("std", "prem"), light = setOf("prem"), pulse = "prem" to "holds", card = BoardCard("Premium", "Shared HSMs, tamper-proof boxes; FIPS 140-3 Level 3.")),
                    Beat(36, show = setOf("mhsm", "keysonly"), light = setOf("mhsm"), pulse = "mhsm" to "keysonly", card = BoardCard("Managed HSM", "Yours alone, your own security domain. Keys only.")),
                    Beat(38, show = setOf("api", "role"), light = setOf("api", "role"), pulse = "api" to "role", card = BoardCard("Managed identity", "No password at all. Key Vault Secrets User reads.")),
                ),
            ),
            // 39: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = all6, card = phrase),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-authorization--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", assignment(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Who gets the keys.")),
                    Beat(2, show = setOf("who", "what", "where", "assign"), light = setOf("assign"), card = BoardCard("Azure RBAC", "Role-based access control: who may do which actions, on what.")),
                    Beat(4, light = setOf("what", "where"), card = BoardCard("Role and scope", "A list of allowed actions, and where it applies.")),
                ),
            ),
            // 5: "Where being?"
            Scene(
                5, "Where being?", scopes(),
                beats = listOf(
                    Beat(6, show = setOf("mg", "sub", "rg", "res", "note"), light = setOf("note"), pulse = "sub" to "rg", card = BoardCard("It flows down", "Whatever you're given reaches everything inside.")),
                    Beat(8, light = setOf("rg"), card = BoardCard("Quiz: the support team", "Restart VMs in one resource group, nothing else. Which role?")),
                    Beat(9, show = setOf("vmc"), light = setOf("vmc", "rg"), pulse = "vmc" to "rg", card = BoardCard("Right answer", "Virtual Machine Contributor, on that resource group, to a group.")),
                ),
            ),
            // 11: "Mm. And what is a role assignment, really?"
            Scene(
                11, "Love letters", assignment(),
                beats = listOf(
                    Beat(12, show = setOf("who", "what", "where", "assign", "arm"), light = setOf("arm"), pulse = "arm" to "assign", card = BoardCard("Azure Resource Manager", "Looks for an assignment at that scope or above. No match, no entry.")),
                ),
            ),
            // 13: "Then my question. Our boss is a Global Administrator..."
            Scene(
                13, "Can't see a thing", kinds(),
                beats = listOf(
                    Beat(13, show = setOf("ga"), light = setOf("ga"), card = BoardCard("Quiz: the boss", "A Global Administrator sees no virtual machines. Why?")),
                    Beat(14, card = BoardCard("A resource lock?", "Locks stop changes; they never hide anything. No kiss.")),
                    Beat(15, show = setOf("entra", "azure", "owner"), light = setOf("entra", "azure"), pulse = "entra" to "ga", card = BoardCard("Two kinds of role", "Entra roles: the directory. Azure roles: resources.")),
                    Beat(16, show = setOf("elevate"), light = setOf("elevate"), pulse = "ga" to "elevate", card = BoardCard("Where RBAC stops", "Not the directory; not what may exist: Azure Policy.")),
                ),
            ),
            // 17: "And the admins?"
            Scene(
                17, "Eligible, not active", pim(),
                beats = listOf(
                    Beat(18, show = setOf("person", "eligible", "active", "checks"), light = setOf("eligible"), pulse = "person" to "eligible", card = BoardCard("PIM", "Eligible for a role; activate it for hours, with a reason.")),
                    Beat(19, light = setOf("active"), card = BoardCard("Quiz: Owner, four hours", "Only when needed, with approval, at most four hours?")),
                    Beat(20, show = setOf("log", "lic"), light = setOf("active", "log"), pulse = "eligible" to "active", card = BoardCard("Right answer", "PIM eligible assignments with approval. All logged.")),
                ),
            ),
            // 22: "Mm. Now, the old timesheet app..."
            Scene(
                22, "Calls out, never in", appProxy(),
                beats = listOf(
                    Beat(22, show = setOf("user", "app", "fw"), light = setOf("app"), card = BoardCard("Quiz: the timesheet app", "From home, Entra first, no VPN, no inbound port?")),
                    Beat(23, show = setOf("conn", "proxy"), light = setOf("conn"), pulse = "conn" to "proxy", card = BoardCard("Right answer", "Entra application proxy: the connector only calls out.")),
                    Beat(25, show = setOf("entra", "sync"), light = setOf("entra"), pulse = "user" to "entra", card = BoardCard("Entra checks first", "MFA and Conditional Access, then Kerberos to the app.")),
                ),
            ),
            // 26: "Now secrets."
            Scene(
                26, "Kept for yourself", vault(),
                beats = listOf(
                    Beat(26, show = setOf("holds"), light = setOf("holds"), card = BoardCard("Key Vault", "A locked safe for secrets, keys and certificates.")),
                    Beat(28, show = setOf("std", "prem", "mhsm"), light = setOf("prem"), pulse = "prem" to "holds", card = BoardCard("Premium", "Shared HSMs, FIPS 140-3 Level 3.")),
                    Beat(30, show = setOf("keysonly"), light = setOf("mhsm"), card = BoardCard("Quiz: the bank", "Single-tenant HSM, its own security domain. Which option?")),
                    Beat(31, light = setOf("mhsm", "keysonly"), pulse = "mhsm" to "keysonly", card = BoardCard("Right answer", "Managed HSM, with the passwords in an ordinary vault.")),
                    Beat(33, light = setOf("prem"), card = BoardCard("Quiz: lowest cost", "HSM-protected, FIPS 140-3 Level 3, and cheapest?")),
                    Beat(34, light = setOf("prem", "holds"), pulse = "prem" to "holds", card = BoardCard("Right answer", "Premium: shared boxes, and it holds the passwords too.")),
                    Beat(36, show = setOf("api", "role"), light = setOf("api", "role"), pulse = "api" to "role", card = BoardCard("Managed identity", "A badge for the app. Key Vault Secrets User reads.")),
                ),
            ),
            // 37: "Last question, for the exam. What do we remember?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = all6, card = phrase),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
