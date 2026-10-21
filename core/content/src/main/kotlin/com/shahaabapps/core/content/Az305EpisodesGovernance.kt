package com.shahaabapps.core.content

/** Governance: management groups, policy, tags and identity governance: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesGovernance {
    private val classic = Podcast(
        "sa-governance",
        listOf(
            maya("Hi, and welcome back. Today is governance: the house rules for a growing estate in Azure, Microsoft's cloud, where you rent computers and services. Our story is Two Tracks goes global. The Android app is a company now, with teams, many Azure accounts, users in Europe and Australia, and GDPR, the EU's privacy law, to obey."),
            leo("House rules. Like no shoes indoors?"),
            maya("[chuckle] Close. Governance is how things are filed, who pays for what, and what is never allowed. Picture an apartment block. The building has rules for every apartment. Each apartment has its own bills. Each room holds things that belong together. Azure has the same layers."),
            leo("Walk me down them. Top floor first."),
            maya("The top is a management group: a folder that holds subscriptions, for shared rules and access. Every directory has one at the very top, the Tenant root group, and new subscriptions land there unless you move them."),
            leo("And a subscription is?"),
            maya("One Azure account with its own bill and its own limits, like one apartment with its own meter. Below it, a resource group, a box of things that live and die together. And at the bottom, a resource: one database, or one virtual machine, which is a computer rented by the hour."),
            leo("Why does the order matter?"),
            maya("Because a rule set high up flows down to everything below it, including what's added later. Set a rule once on a management group, and every subscription inside obeys, even one created next year."),
            leo("So I build the folders like the org chart? Marketing, finance, engineering?"),
            maya("That's the classic mistake. Org charts change with every reorganisation, and then you rebuild the tree. Group by kinds of workload that need different rules. Keep it shallow, three or four levels. Up to six are allowed below the root, and each subscription has exactly one parent."),
            leo("And how do I split subscriptions?"),
            maya("By workload and environment. Production and test apart, so a test can't eat production's limits or break its rules. And resource groups by lifecycle: what is deployed and deleted as one."),
            leo("Is there a ready-made layout?"),
            maya("Microsoft's Cloud Adoption Framework, its guide for moving to Azure, describes landing zones. A landing zone is a subscription made ready before any app moves in, with network, sign-in, rules and monitoring in place, like a building plot with water and power already connected."),
            leo("And how are they arranged?"),
            maya("Platform landing zones hold shared services, such as the network links. Application landing zones hold the apps, often split into Corp, private and linked to the office, and Online, public facing. And a Sandbox group for experiments, with looser rules."),
            leo("Okay, filing done. Who pays for what?"),
            maya("That's tags. A tag is a name and value label stuck on a resource, like cost centre: mobile. Tags let finance split the bill, find an owner at three in the morning, or spot sensitive data. For example, Two Tracks agrees five: cost centre, owner, environment, application and data classification."),
            leo("And if a resource group has a tag, its resources get it too, right?"),
            maya("[pause] No, and the exam loves that. Tags are not inherited by default. You need a rule to copy them down. Which brings us to Azure Policy."),
            leo("Policy, in plain words?"),
            maya("Azure Policy is a building inspector at the door. It checks every new or changed resource against rules, whoever creates it. And it scans what already exists, and reports which resources follow the rules. That report is called compliance."),
            leo("And a rule looks like?"),
            maya("A policy definition is one rule, such as only these regions. A region is one of Azure's data-centre locations, like West Europe. An initiative is a bundle of definitions. An assignment attaches either one to a scope, which is the place it applies: a management group, a subscription or a resource group."),
            leo("And when something breaks a rule?"),
            maya("The effect decides. Deny blocks the create or change. Audit allows it, but marks it non-compliant. Listen for the words: must not, or only allow, means deny. Report, or without blocking, means audit."),
            leo("And the one that copies tags down?"),
            maya("Modify. It adds or changes tags and some settings. Then there's deployIfNotExists: it deploys a missing companion, something that should sit beside a resource, like a diagnostic setting that sends its logs to a central place. And auditIfNotExists only reports the missing one."),
            leo("So modify fixes everything, old and new?"),
            maya("New ones, at once. Existing resources are fixed only by a remediation task, a clean-up run you start. It acts as the assignment's managed identity, a badge Azure gives the assignment so it may make changes. Assuming old resources fix themselves is a classic mistake."),
            leo("How is Policy different from roles? Both sound like rules."),
            maya("Azure RBAC, the role-based access control from the authorization episode, decides who may act. Policy decides what may exist, whoever acts. Say your app's team are Owners of their subscription. They still can't create a database in Brazil if a deny policy says Europe only."),
            leo("Then how do I stop accidents? Someone deleting the production database?"),
            maya("A resource lock, a guard against mistakes. CanNotDelete lets people change a resource but not delete it. ReadOnly allows neither. Locks are inherited, and stop everyone, Owners included, until the lock is removed."),
            leo("And proving to auditors we follow the rules?"),
            maya("Microsoft Defender for Cloud, Azure's security check-up. Its regulatory compliance dashboard is a report card against a standard, a published list of rules. It starts with Microsoft's own benchmark. Adding GDPR, or ISO 27001, an international security standard, needs a paid Defender plan. Each control shows pass or fail."),
            leo("Didn't people use Blueprints for this?"),
            maya("Azure Blueprints is being retired, finishing in January 2027. Microsoft points to deployment stacks instead, which deploy a set of resources as one unit and guard it."),
            leo("Last part. People. Who still has access?"),
            maya("Identity governance, in Microsoft Entra ID, Azure's sign-in system. Access reviews ask managers or owners, on a schedule, to confirm who still needs access, and can remove the rest. Entitlement management offers access packages: a bundle of access people request, with approval and an end date."),
            leo("And joiners and leavers?"),
            maya("Lifecycle workflows: automatic tasks when someone joins, moves or leaves, like disabling an account on the last day. Picture a small agency with partner guests: a quarterly check is an access review; partners asking for a bundle that expires is an access package. These need paid licences."),
            leo("So what do I remember for the exam?"),
            maya("If rules must cover every subscription, including new ones, a management group. Must not be created: deny. Only report: audit. Add or inherit a tag: modify. Ensure diagnostics: deployIfNotExists, with a remediation task for existing ones. Stop accidental deletion: a CanNotDelete lock. Guests checked every quarter: access reviews."),
            leo("And anything to try today?"),
            maya("Fifteen minutes, and free. Create a management group and move a test subscription under it. Assign Allowed locations and watch a deployment in another region fail. Add a tag to a resource group and see that its resources do not have it. Then put a CanNotDelete lock on a resource group and try to delete it."),
            leo("House rules, set once, at the top. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-governance",
        listOf(
            maya("Welcome to Case Files. Today's case: The Data That Left Europe. Two Tracks goes global: the Android app is a company now, with a backend on Azure, Microsoft's cloud, and users in Europe and Australia. Then an auditor found European users' data stored in a US data centre. GDPR, the EU's privacy law, says it must stay in the EU."),
            leo("Detective Leo, on the case. Somebody clicked the wrong region."),
            maya("Somebody did. A region is one of Azure's data-centre locations, like West Europe. But the question for a designer is why the click was allowed. Clue one: Two Tracks did have a rule saying Europe only."),
            leo("Then the rule was broken. Suspect: Azure ignored it."),
            maya("[pause] Wrong suspect. The rule was an Azure Policy assignment. Azure Policy is a building inspector at the door: it checks every new or changed resource against rules, whoever creates it. And that rule had been assigned subscription by subscription, by hand."),
            leo("Subscription being, in plain words?"),
            maya("One Azure account with its own bill and limits, like one apartment with its own meter. Clue two: a new analytics subscription was created in March. Nobody remembered to assign the rule there."),
            leo("So the new apartment had no house rules. Suspect: the developer was an Owner. Take Owner away."),
            maya("Still not it. Owner is a role, and roles are Azure RBAC, the role-based access control that decides who may act. Policy decides what may exist, whoever acts. Even an Owner is blocked by a deny policy. The design should have put the rule higher up."),
            leo("Higher up being?"),
            maya("A management group: a folder that holds subscriptions. Rules set on a folder flow down to every subscription inside, including ones added later. Every directory has one at the very top, the Tenant root group."),
            leo("So: assign Europe only on the folder, once."),
            maya("Yes. The built-in Allowed locations policy, with the deny effect, on the Landing zones management group. A landing zone is a subscription made ready before any app moves in, with network, rules and monitoring in place. The Sandbox group stays outside it, for experiments."),
            leo("What's a policy, really, under the picture?"),
            maya("A policy definition is a short rule written as if this, then that: if the location is not on the list, then deny. An initiative bundles several definitions. An assignment attaches one to a scope, which is the place it applies, with exclusions if needed."),
            leo("And how does it catch things? Magic?"),
            maya("Not magic. Every create or change request goes through Azure Resource Manager, the front desk for all management requests. Before it goes ahead, the policies assigned at that scope and above are checked. Existing resources are scanned too, for a compliance report: who follows the rules."),
            leo("Clue three. I can feel it."),
            maya("Clue three: the old rule wasn't deny at all. It was audit. Audit allows the create, and only marks it non-compliant. The report said red for six months. Nobody read it."),
            leo("So when the words say must not, the answer is deny."),
            maya("Exactly. Must not, prevent, only allow: deny. Report, find out, without blocking: audit. Using audit when the requirement says must not be created is a classic mistake."),
            leo("Is that the whole case?"),
            maya("Two side cases. Finance couldn't split the bill. Every resource group, a box of things that live and die together, had a tag: a name and value label like cost centre: mobile. But the resources inside didn't."),
            leo("Suspect: the tags got deleted."),
            maya("They were never there. Tags are not inherited from the resource group by default. The fix is the built-in inherit-a-tag policy with the modify effect, which adds or changes tags as resources are created."),
            leo("And the hundreds that already exist?"),
            maya("Modify alone won't touch them. You run a remediation task, a clean-up run you start. It acts as the assignment's managed identity, a badge Azure gives the assignment so it may change things. Same for deployIfNotExists, which deploys a missing companion, like a diagnostic setting that sends logs."),
            leo("Second side case?"),
            maya("During the clean-up, an engineer almost deleted the production database. The delete button was one click away."),
            leo("Suspect: the engineer. Fire him. [chuckle]"),
            maya("[sigh] Design for tired fingers instead. A resource lock: CanNotDelete lets people change a resource but not delete it; ReadOnly allows neither. Locks are inherited, and stop everyone, Owners included, until removed."),
            leo("And the auditor? Will she be back?"),
            maya("Every quarter. So Two Tracks turned on Microsoft Defender for Cloud, Azure's security check-up. Its regulatory compliance dashboard is a report card against a standard. It starts with Microsoft's own benchmark; adding GDPR or ISO 27001 needs a paid Defender plan. Each control shows pass or fail."),
            leo("And the old way? Blueprints?"),
            maya("Azure Blueprints is being retired, finishing in January 2027. Microsoft points to deployment stacks, which deploy a set of resources as one unit and guard it."),
            leo("Anything about people?"),
            maya("The auditor found partner guests from last year, still with access. That's identity governance, in Microsoft Entra ID, which is Azure's sign-in system. Access reviews ask a reviewer, every quarter, to confirm who still needs access, and remove the rest automatically."),
            leo("And partners who need access next month?"),
            maya("Entitlement management: they request an access package, a bundle of access with approval and an end date. Lifecycle workflows handle joiners and leavers. These need paid licences. And Policy never decides who may act; for roles and just-in-time admin, see the authorization episode."),
            leo("So, the case summary for the exam?"),
            maya("Every subscription, now and later: assign on a management group. Must not be created: deny. Only report: audit. Add or inherit a tag: modify. Ensure diagnostics: deployIfNotExists. Existing resources: a remediation task. Stop accidental deletion: a CanNotDelete lock. Guests checked every quarter: access reviews."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a management group and move a test subscription under it. Assign Allowed locations and watch a deployment in another region fail. Add a tag to a resource group and see that its resources do not have it. Put a CanNotDelete lock on a resource group and try to delete it."),
            leo("Data stays home. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-governance",
        listOf(
            maya("Okay, did you hear what the Two Tracks cloud team did this week? Sit down. Two Tracks is going global, with many Azure accounts now, and the boss said: set house rules. That's governance. How things are filed, who pays for what, and what's never allowed. And it was a week."),
            leo("[laugh] I heard there was a reorganisation. Start with the filing."),
            maya("Picture an apartment block. The building's rules cover every apartment; each apartment has its own bills. In Azure, Microsoft's cloud, a subscription is one apartment: one Azure account with its own bill. A management group is the building: a folder that holds subscriptions."),
            leo("And rules on the building reach every apartment?"),
            maya("Every one, including apartments added later. Below a subscription sit resource groups, boxes of things that live and die together, and in those, the resources themselves. Every directory has one folder at the very top, the Tenant root group."),
            leo("So who went first?"),
            maya("Tom. Monday. Tom built the management groups like the org chart: Marketing, Finance, Engineering. Very tidy. And on Tuesday morning, there was a reorganisation."),
            leo("Oh no. He rebuilt it?"),
            maya("All afternoon. Org charts change. Group by kinds of workload that need different rules instead. Keep it shallow, three or four levels. Up to six are allowed below the root, and each subscription has exactly one parent."),
            leo("So what did they end up with?"),
            maya("Landing zones, from Microsoft's Cloud Adoption Framework, its guide for moving to Azure. A landing zone is a subscription made ready before an app moves in, with network, rules and monitoring in place, like a plot with water and power connected. Platform for shared services, Landing zones for apps, and a Sandbox for experiments."),
            leo("Tuesday?"),
            maya("Dana. Dana had to keep European data in European regions, the data-centre locations Azure runs, for GDPR, the EU's privacy law. So she wrote the Allowed locations rule in Azure Policy, and assigned it to each subscription. One by one."),
            leo("Azure Policy being?"),
            maya("A building inspector at the door. It checks every new or changed resource against rules, whoever creates it, and scans existing ones for a compliance report. One rule is a policy definition; a bundle of them is an initiative; attaching one to a scope, the place it applies, is an assignment."),
            leo("And Dana's one-by-one problem?"),
            maya("A new subscription arrived on Wednesday, without the rule. And worse, she'd used audit, not deny. Audit allows the create and only reports it. If the words say must not, the answer is deny, on the management group, once."),
            leo("What is a policy, really, under the inspector?"),
            maya("A short if-then rule: if the location isn't on the list, then deny. Every create or change goes through Azure Resource Manager, the front desk for management requests, and it checks the policies at that scope and above before letting it through."),
            leo("Wednesday, then?"),
            maya("Marcus. Finance wanted the bill split by cost centre. Marcus put a tag on every resource group: a name and value label, like cost centre: mobile. Then he waited for the bill."),
            leo("And the resources had no tags. [chuckle]"),
            maya("None. Tags are not inherited by default. The fix is the built-in inherit-a-tag policy with the modify effect, which adds or changes tags as resources are created. Priya set it up by lunch."),
            leo("Thursday?"),
            maya("The intern. Bless them. Cleaning up old test resources, they clicked delete on the production database. And nothing happened."),
            leo("Nothing?"),
            maya("A resource lock. Priya had put a CanNotDelete lock on it: you can change it, not delete it. ReadOnly allows neither. Locks are inherited, and stop everyone, even Owners, until removed. The intern went for a long walk."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes. Every new database must send its logs to the central workspace, so she assigned deployIfNotExists, which deploys a missing companion, here a diagnostic setting. New databases got it. The hundred old ones didn't."),
            leo("Priya. Really?"),
            maya("Really. Policy fixes existing resources only with a remediation task, a clean-up run you start. It acts as the assignment's managed identity, a badge Azure gives the assignment so it may change things. She ran it Friday afternoon. Very quietly."),
            leo("And how do they prove all this to an auditor?"),
            maya("Microsoft Defender for Cloud, Azure's security check-up. Its regulatory compliance dashboard is a report card against a standard, starting with Microsoft's own benchmark. Adding GDPR or ISO 27001 needs a paid Defender plan. Each control shows pass or fail. Blueprints, the old way, is retired by January 2027; deployment stacks replace it."),
            leo("And the weekend gossip? People?"),
            maya("Partner guests. Last year's agency still had access. Now it's identity governance, in Microsoft Entra ID, which is Azure's sign-in system. Access reviews ask a reviewer, every quarter, who still needs access, and remove the rest automatically."),
            leo("And new partners?"),
            maya("Entitlement management: they request an access package, a bundle of access with approval and an end date. Lifecycle workflows handle joiners and leavers. These are paid features. And Policy never decides who may act; that's roles, in the authorization episode."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Every subscription, now and later: a management group, Dana's lesson. Must not be created: deny. Only report: audit. Inherit a tag: modify, Marcus's fix. Ensure diagnostics: deployIfNotExists, plus a remediation task for old ones, Priya's Friday. Stop accidental deletion: a CanNotDelete lock. Quarterly guest checks: access reviews."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a management group and move a test subscription under it. Assign Allowed locations and watch a deployment in another region fail. Add a tag to a resource group and see that its resources do not have it. Put a CanNotDelete lock on a resource group and try to delete it."),
            leo("House rules at the top, locks on the good stuff. Got it, intern. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-governance",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about house rules."),
            ivy("Mm. I've never been good with rules, Nova."),
            nova("[chuckle] These ones you'll like. Two Tracks goes global: the little Android app is a company now, with users in Europe and Australia, and many accounts in Azure, Microsoft's cloud. Governance is the house rules for all of it: how things are filed, who pays, and what's never allowed."),
            ivy("Start with the filing. Slowly."),
            nova("Picture an apartment block at night. A subscription is one apartment: one Azure account, with its own bill. A management group is the building: a folder that holds subscriptions. And at the very top, every directory has one root, the Tenant root group."),
            ivy("And inside each apartment?"),
            nova("Resource groups, boxes of things that live and die together. And in each box, the resources: one database, one virtual machine, which is a computer rented by the hour. [pause] A rule set on the building reaches every apartment, even ones built next year."),
            ivy("So how should I arrange the building?"),
            nova("Not like the org chart, which changes with every reorganisation. By kinds of workload that need different rules. Shallow, three or four levels; six at most below the root. Production apart from test, so a test can't eat production's limits."),
            ivy("Is there a pattern people use?"),
            nova("Landing zones, from Microsoft's Cloud Adoption Framework, its guide for moving to Azure. A landing zone is a subscription made ready before an app moves in, network, rules and monitoring all in place. Like a plot with water and power already on. Platform, Landing zones, and a Sandbox to play in."),
            ivy("Now. What's never allowed?"),
            nova("That's Azure Policy. Think of a building inspector at the door, awake all night. Every new or changed resource is checked against the rules, whoever creates it. And the existing ones are scanned, and you get a compliance report."),
            ivy("And what is a policy, really, under the inspector's coat?"),
            nova("A short if-then rule. If the region, one of Azure's data-centre locations, isn't on the list, then deny. One rule is a definition, a bundle is an initiative, and attaching one to a scope, the place it applies, is an assignment. [pause] Every request passes Azure Resource Manager, the front desk, which checks the rules at that scope and above."),
            ivy("And when something breaks a rule?"),
            nova("The effect decides. Deny blocks it. Audit lets it through and quietly marks it non-compliant. Must not, means deny. Report without blocking, means audit."),
            ivy("Mm. And what about tags? I hear they're pretty."),
            nova("A tag is a name and value label on a resource, like cost centre: mobile. They split the bill, and find an owner at three in the morning. A short list, agreed up front: cost centre, owner, environment, application, data classification."),
            ivy("And if I tag the box, the things inside get it too?"),
            nova("[pause] No. Tags are not inherited by default. The modify effect fixes that: the built-in inherit-a-tag policy adds the tag from the resource group as things are created."),
            ivy("And the things already in the box?"),
            nova("A remediation task, a clean-up run you start. It works as the assignment's managed identity, a badge Azure gives it so it may change things. Same for deployIfNotExists, which deploys a missing companion, like a diagnostic setting that sends a database's logs to a central place."),
            ivy("Policy sounds like it can do anything."),
            nova("Not quite. It decides what may exist, never who may act; that's Azure RBAC, the role-based access control in the authorization episode. But even an Owner, who may do everything, is stopped by a deny."),
            ivy("And what stops tired fingers deleting something precious?"),
            nova("A resource lock. CanNotDelete lets you change it but not delete it. ReadOnly allows neither. Locks are inherited, and stop everyone, Owners included, until someone removes the lock. A little guard by the door."),
            ivy("And proving we follow the rules?"),
            nova("Microsoft Defender for Cloud, Azure's security check-up. Its regulatory compliance dashboard is a report card against a standard, starting with Microsoft's own benchmark. GDPR, the EU's privacy law, can be added with a paid Defender plan. Each control, pass or fail. Blueprints, the old way, is retired by January 2027."),
            ivy("And people? Who still has a key to the building?"),
            nova("That's identity governance, in Microsoft Entra ID, which is Azure's sign-in system. Access reviews ask someone, every quarter, to confirm who still needs access, and remove the rest. Entitlement management offers access packages, a bundle of access people request, with approval and an end date."),
            ivy("And the comings and goings?"),
            nova("Lifecycle workflows. Automatic tasks when someone joins, moves or leaves, like disabling an account on the last day. [pause] These are paid features, with Entra ID P2 or ID Governance."),
            ivy("[sigh] Before you send me off to bed. What do I remember for the exam?"),
            nova("Every subscription, now and later: a management group. Must not be created: deny. Only report: audit. Add or inherit a tag: modify. Ensure diagnostics: deployIfNotExists, with a remediation task for old ones. Stop accidental deletion: a CanNotDelete lock. Guests checked every quarter: access reviews."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a management group and move a test subscription under it. Assign Allowed locations and watch a deployment in another region fail. [pause] Add a tag to a resource group and see that its resources do not have it. Put a CanNotDelete lock on a resource group and try to delete it."),
            ivy("House rules, set high, and a lock on what I love. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-governance",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about house rules."),
            rafe("House rules. Like who does the dishes? [chuckle]"),
            nova("Like what's never allowed. Governance: how things are filed, who pays for what, and what may never happen. Two Tracks goes global, with users in Europe and Australia and many accounts in Azure, Microsoft's cloud. So it needs rules."),
            rafe("Start with the filing."),
            nova("A subscription is one Azure account with its own bill, like one apartment with its own meter. A management group is a folder that holds subscriptions, like the whole building. Below a subscription, resource groups, boxes of things that live and die together. Rules set high up flow down to everything below."),
            rafe("And the very top of the building?"),
            nova("Every directory has one root, the Tenant root group. New subscriptions land there unless you move them. And each subscription has exactly one parent folder. Faithful, like me."),
            rafe("Then my first question, exam style. Nothing may be created outside EU regions, in every subscription, now and later. Where do you set the rule?"),
            nova("Once, on a management group above them all. The built-in Allowed locations policy, with deny. A region being one of Azure's data-centre locations, like West Europe."),
            rafe("Right answer. Set it high, and it covers everyone who moves in later. [kiss]"),
            nova("Mm. And the rule itself lives in Azure Policy, a building inspector at the door. It checks every new or changed resource against rules, whoever creates it, and scans what already exists for a compliance report."),
            rafe("What is a policy, really? Under all that charm?"),
            nova("A short if-then rule: if the region isn't on the list, then deny. A bundle of rules is an initiative. Attaching one to a scope, the place it applies, is an assignment. And every request passes Azure Resource Manager, the front desk, which checks the rules at that scope and above."),
            rafe("And how do I arrange the folders? Like the org chart?"),
            nova("Never. Org charts change. By kinds of workload. Landing zones, from Microsoft's Cloud Adoption Framework: a landing zone is a subscription made ready before an app moves in, network and rules in place. Platform, Landing zones, Sandbox. Shallow, three or four levels."),
            rafe("Now my question. Finance wants every resource to carry its resource group's cost centre tag, automatically. A tag being a name and value label, like cost centre: mobile. Which effect?"),
            nova("Deny. Block anything without it."),
            rafe("No kiss for that one. Deny blocks deployments and still doesn't copy the value. Tags are not inherited by default. The answer is modify, with the built-in inherit-a-tag policy. And a remediation task for the ones already there."),
            nova("A remediation task: a clean-up run you start, acting as the assignment's managed identity, a badge Azure gives it so it may change things. Fair. I deserved that."),
            rafe("And which tags would you wear?"),
            nova("A short list, agreed up front: cost centre, owner, environment, application and data classification. They split the bill, and find an owner at three in the morning."),
            rafe("You'll earn it back. Your question."),
            nova("Every new database must send its logs to the central workspace, with no manual step. Which effect?"),
            rafe("DeployIfNotExists. It deploys the missing companion, a diagnostic setting, as each database is created."),
            nova("Right answer. You always make sure nothing's missing. [kiss]"),
            rafe("And audit?"),
            nova("Audit allows it and only reports it. If the words say report, or without blocking: audit. If they say must not: deny. And here's the edge. Policy decides what may exist, never who may act; that's Azure RBAC, the role-based access control in the authorization episode. Even an Owner is stopped by deny."),
            rafe("Then mine. Nobody may delete the production database by accident. What do you use?"),
            nova("A resource lock. CanNotDelete: you may change it, not delete it. ReadOnly allows neither. And it stops everyone, Owners included, until it's removed."),
            rafe("Right answer. Some things are worth protecting. [kiss]"),
            nova("[laugh] And to prove it all to an auditor, Microsoft Defender for Cloud, Azure's security check-up. Its regulatory compliance dashboard is a report card against a standard, like GDPR, the EU's privacy law, with a paid Defender plan. Each control, pass or fail. Blueprints, the old way, retires by January 2027."),
            rafe("Now people. Guests from partner companies must be checked every quarter, and removed if nobody confirms them. What do you recommend?"),
            nova("Access reviews, in Microsoft Entra ID, Azure's sign-in system, the reception desk with the list of everyone. A reviewer, such as a guest's sponsor, confirms who still needs access, and the rest are removed automatically."),
            rafe("Right answer. Every quarter, we check who's still welcome. [kiss]"),
            nova("And my last one. Partners must request a bundle of resources, with approval, and the access must expire. Which feature?"),
            rafe("Entitlement management. They request an access package, a bundle of access with approval and an end date. And for joiners and leavers, lifecycle workflows."),
            nova("Right answer. Nothing lasts forever, except us. [kiss]"),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Every subscription, now and later: a management group. Must not be created: deny. Only report: audit. Inherit a tag: modify. Ensure diagnostics: deployIfNotExists, and a remediation task for old ones. Stop accidental deletion: a CanNotDelete lock. Quarterly guest checks: access reviews. Partners' expiring bundle: an access package."),
            rafe("Eight for eight. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a management group and move a test subscription under it. Assign Allowed locations and watch a deployment in another region fail. Add a tag to a resource group and see that its resources do not have it. Put a CanNotDelete lock on a resource group and try to delete it."),
            nova("And you're the one rule I'd never break. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun hierarchy() = DiagramSpec(
        "Management groups hold subscriptions, which hold resource groups; a rule set high up flows down to everything below, new subscriptions too.",
        nodes = listOf(
            DNode("root", "Tenant root group", 0.5f, 0.08f, w = 0.5f, h = 0.12f, tone = 2),
            DNode("tt", "Two Tracks", 0.5f, 0.27f, w = 0.44f, h = 0.12f),
            DNode("plat", "Platform", 0.17f, 0.47f, w = 0.3f, h = 0.12f, tone = 1),
            DNode("lz", "Landing zones", 0.5f, 0.47f, w = 0.3f, h = 0.12f, tone = 1),
            DNode("sand", "Sandbox", 0.83f, 0.47f, w = 0.3f, h = 0.12f, tone = 1),
            DNode("s1", "Connectivity sub", 0.17f, 0.67f, w = 0.3f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
            DNode("s2", "App sub: tt-prod", 0.5f, 0.67f, w = 0.3f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
            DNode("s3", "Dev sub", 0.83f, 0.67f, w = 0.3f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
            DNode("rg", "Resource groups", 0.5f, 0.88f, w = 0.4f, h = 0.12f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("root", "tt"), DEdge("tt", "plat"), DEdge("tt", "lz"), DEdge("tt", "sand"),
            DEdge("plat", "s1"), DEdge("lz", "s2"), DEdge("sand", "s3"), DEdge("s2", "rg"),
        ),
        aspect = 0.8f,
    )

    private fun policy() = DiagramSpec(
        "Every create or change passes Azure Resource Manager, which checks the policies assigned at that scope and above; existing resources get a compliance report.",
        nodes = listOf(
            DNode("req", "Create request", 0.18f, 0.18f, w = 0.32f, h = 0.2f, tone = 1, sub = "anyone, even Owner"),
            DNode("arm", "Resource Manager", 0.6f, 0.18f, w = 0.4f, h = 0.2f, tone = 2, sub = "checks the rules"),
            DNode("def", "Definition", 0.18f, 0.52f, w = 0.32f, h = 0.2f, sub = "one if-then rule"),
            DNode("assign", "Assignment", 0.6f, 0.52f, w = 0.4f, h = 0.2f, sub = "rule + scope"),
            DNode("init", "Initiative", 0.18f, 0.84f, w = 0.32f, h = 0.2f, sub = "a bundle of rules"),
            DNode("report", "Compliance report", 0.6f, 0.84f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "existing resources"),
        ),
        edges = listOf(
            DEdge("req", "arm"), DEdge("def", "assign"), DEdge("init", "assign"),
            DEdge("assign", "arm", "checked"), DEdge("assign", "report", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun effects() = DiagramSpec(
        "The effect is what a rule does: deny blocks, audit reports, modify fixes tags, deployIfNotExists adds what is missing; a remediation task fixes what already exists.",
        nodes = listOf(
            DNode("deny", "deny", 0.26f, 0.14f, w = 0.44f, h = 0.2f, tone = 2, sub = "blocks it"),
            DNode("audit", "audit", 0.76f, 0.14f, w = 0.44f, h = 0.2f, sub = "allows, reports"),
            DNode("modify", "modify", 0.26f, 0.47f, w = 0.44f, h = 0.2f, tone = 1, sub = "adds or fixes tags"),
            DNode("dine", "deployIfNotExists", 0.76f, 0.47f, w = 0.44f, h = 0.2f, tone = 1, sub = "adds a companion"),
            DNode("remed", "Remediation task", 0.5f, 0.8f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, sub = "fixes existing ones, as the managed identity"),
        ),
        edges = listOf(DEdge("modify", "remed", dashed = true), DEdge("dine", "remed", dashed = true)),
        aspect = 0.8f,
    )

    private fun tags() = DiagramSpec(
        "A tag on a resource group does not reach the resources inside; the modify effect copies it down.",
        nodes = listOf(
            DNode("rg", "Resource group", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "cost centre: mobile"),
            DNode("r1", "Database", 0.18f, 0.5f, w = 0.28f, h = 0.2f, tone = 2, sub = "no tag"),
            DNode("r2", "Web app", 0.5f, 0.5f, w = 0.28f, h = 0.2f, tone = 2, sub = "no tag"),
            DNode("r3", "Storage", 0.82f, 0.5f, w = 0.28f, h = 0.2f, tone = 2, sub = "no tag"),
            DNode("mod", "modify: inherit the tag", 0.5f, 0.84f, w = 0.84f, h = 0.18f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("rg", "r1", dashed = true), DEdge("rg", "r2", dashed = true), DEdge("rg", "r3", dashed = true),
            DEdge("mod", "r2", "copies down"),
        ),
        aspect = 0.8f,
    )

    private fun locks() = DiagramSpec(
        "A CanNotDelete lock allows changes but not deletion, ReadOnly allows neither, and both stop everyone, Owners included.",
        nodes = listOf(
            DNode("db", "Production database", 0.5f, 0.18f, w = 0.6f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1),
            DNode("cnd", "CanNotDelete", 0.26f, 0.52f, w = 0.44f, h = 0.2f, sub = "change yes, delete no"),
            DNode("ro", "ReadOnly", 0.76f, 0.52f, w = 0.44f, h = 0.2f, tone = 2, sub = "neither"),
            DNode("note", "Stops everyone, Owners included", 0.5f, 0.84f, w = 0.84f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(DEdge("cnd", "db"), DEdge("ro", "db", dashed = true)),
        aspect = 0.8f,
    )

    private fun compliance() = DiagramSpec(
        "Defender for Cloud scores the estate against standards, the Microsoft benchmark by default and others such as GDPR with a paid plan.",
        nodes = listOf(
            DNode("def", "Defender for Cloud", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "regulatory compliance"),
            DNode("mcsb", "Microsoft benchmark", 0.18f, 0.5f, w = 0.32f, h = 0.2f, sub = "on by default"),
            DNode("gdpr", "GDPR", 0.5f, 0.5f, w = 0.28f, h = 0.2f, sub = "paid plan"),
            DNode("iso", "ISO 27001", 0.82f, 0.5f, w = 0.32f, h = 0.2f, sub = "paid plan"),
            DNode("stacks", "Blueprints retired: deployment stacks", 0.5f, 0.84f, w = 0.88f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(DEdge("def", "mcsb"), DEdge("def", "gdpr"), DEdge("def", "iso")),
        aspect = 0.8f,
    )

    private fun people() = DiagramSpec(
        "Identity governance keeps access current: access reviews confirm it, access packages grant it with an end date, lifecycle workflows follow joiners and leavers.",
        nodes = listOf(
            DNode("entra", "Entra ID Governance", 0.5f, 0.13f, w = 0.6f, h = 0.18f, tone = 2),
            DNode("rev", "Access reviews", 0.18f, 0.48f, w = 0.32f, h = 0.22f, tone = 1, sub = "confirm every quarter"),
            DNode("pkg", "Access packages", 0.5f, 0.48f, w = 0.28f, h = 0.22f, tone = 1, sub = "approved, expiring"),
            DNode("life", "Lifecycle workflows", 0.82f, 0.48f, w = 0.32f, h = 0.22f, tone = 1, sub = "join, move, leave"),
            DNode("lic", "Paid: Entra ID P2 or ID Governance", 0.5f, 0.84f, w = 0.88f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(DEdge("entra", "rev"), DEdge("entra", "pkg"), DEdge("entra", "life")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's governance questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("mg", "Every subscription, later too", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a management group"),
            DNode("deny", "Must not be created", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "deny"),
            DNode("audit", "Only report it", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "audit"),
            DNode("tag", "Add or inherit a tag", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "modify"),
            DNode("diag", "Ensure diagnostics", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "deployIfNotExists"),
            DNode("lock", "No accidental deletion", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "CanNotDelete lock"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("mg", "deny", "audit", "tag", "diag", "lock")
    private val tree = setOf("root", "tt", "plat", "lz", "sand")
    private val today = BoardCard("Today", "A management group, Allowed locations, a tag that doesn't flow down, a lock.")
    private val phrase = BoardCard("Listen for the phrase", "Each requirement in the question points to one answer.")

    private fun classicBoard() = PodcastBoard(
        "sa-governance",
        listOf(
            // 0: "Hi, and welcome back. Today is governance..."
            Scene(
                0, "House rules", hierarchy(),
                beats = listOf(
                    Beat(0, card = BoardCard("Two Tracks goes global", "Many Azure accounts, users in Europe and Australia, and GDPR.")),
                    Beat(2, card = BoardCard("Governance", "How things are filed, who pays for what, what is never allowed.")),
                    Beat(4, show = tree, light = setOf("root"), card = BoardCard("Management group", "A folder of subscriptions. One root at the very top.")),
                    Beat(6, show = setOf("s1", "s2", "s3", "rg"), light = setOf("s2", "rg"), pulse = "lz" to "s2", card = BoardCard("Subscription, resource group", "One account with its own bill; a box that lives and dies as one.")),
                    Beat(8, light = setOf("tt"), pulse = "tt" to "lz", card = BoardCard("Rules flow down", "Set once high up; every subscription below obeys, even new ones.")),
                    Beat(10, light = setOf("plat", "lz", "sand"), card = BoardCard("By workload, not org chart", "Shallow: three or four levels. Up to six below the root.")),
                    Beat(12, light = setOf("s2", "s3"), card = BoardCard("Production and test apart", "A test can't eat production's limits or break its rules.")),
                    Beat(14, light = setOf("lz"), card = BoardCard("Landing zone", "A subscription ready before the app moves in: water and power on.")),
                    Beat(16, light = setOf("plat", "lz", "sand"), card = BoardCard("Platform, apps, Sandbox", "Shared services; Corp and Online apps; experiments.")),
                ),
            ),
            // 17: "Okay, filing done. Who pays for what?"
            Scene(
                17, "Who pays for what", tags(),
                beats = listOf(
                    Beat(18, show = setOf("rg", "r1", "r2", "r3"), light = setOf("rg"), card = BoardCard("A tag", "A name and value label: cost centre, owner, environment and more.")),
                    Beat(20, light = setOf("r1", "r2", "r3"), card = BoardCard("Not inherited", "Resource group tags don't reach the resources by default.")),
                ),
            ),
            // 21: "Policy, in plain words?"
            Scene(
                21, "The building inspector", policy(),
                beats = listOf(
                    Beat(22, show = setOf("req", "arm", "report"), light = setOf("arm", "report"), pulse = "req" to "arm", card = BoardCard("Azure Policy", "Checks every new or changed resource, whoever creates it.")),
                    Beat(24, show = setOf("def", "init", "assign"), light = setOf("def", "init", "assign"), pulse = "def" to "assign", card = BoardCard("Definition, initiative", "One rule; a bundle; attached to a scope by an assignment.")),
                ),
            ),
            // 25: "And when something breaks a rule?"
            Scene(
                25, "What a rule does", effects(),
                beats = listOf(
                    Beat(26, show = setOf("deny", "audit"), light = setOf("deny", "audit"), card = BoardCard("deny or audit", "Must not: deny. Report without blocking: audit.")),
                    Beat(28, show = setOf("modify", "dine"), light = setOf("modify", "dine"), card = BoardCard("modify, deployIfNotExists", "Fix tags; deploy a missing companion, like diagnostics.")),
                    Beat(30, show = setOf("remed"), light = setOf("remed"), pulse = "modify" to "remed", card = BoardCard("Remediation task", "Fixes existing resources, as the assignment's managed identity.")),
                ),
            ),
            // 31: "How is Policy different from roles?"
            Scene(
                31, "Policy or roles?", policy(),
                beats = listOf(
                    Beat(32, show = setOf("req", "arm", "assign"), light = setOf("req", "arm"), pulse = "assign" to "arm", card = BoardCard("Who versus what", "Azure RBAC: who may act. Policy: what may exist. Deny stops Owners.")),
                ),
            ),
            // 33: "Then how do I stop accidents?"
            Scene(
                33, "A guard against mistakes", locks(),
                beats = listOf(
                    Beat(34, show = setOf("db", "cnd", "ro", "note"), light = setOf("cnd"), pulse = "cnd" to "db", card = BoardCard("Resource lock", "CanNotDelete: change, not delete. ReadOnly: neither.")),
                ),
            ),
            // 35: "And proving to auditors we follow the rules?"
            Scene(
                35, "Proving it", compliance(),
                beats = listOf(
                    Beat(36, show = setOf("def", "mcsb", "gdpr", "iso"), light = setOf("def"), pulse = "def" to "gdpr", card = BoardCard("Defender for Cloud", "A report card against a standard, control by control.")),
                    Beat(38, show = setOf("stacks"), light = setOf("stacks"), card = BoardCard("Blueprints retired", "It ends in January 2027. Deployment stacks replace it.")),
                ),
            ),
            // 39: "Last part. People."
            Scene(
                39, "People", people(),
                beats = listOf(
                    Beat(40, show = setOf("entra", "rev", "pkg"), light = setOf("rev", "pkg"), pulse = "entra" to "rev", card = BoardCard("Access reviews, packages", "Confirm who still needs access; request a bundle that expires.")),
                    Beat(42, show = setOf("life", "lic"), light = setOf("life"), pulse = "entra" to "life", card = BoardCard("Lifecycle workflows", "Joiners, movers and leavers. Paid licences.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = all6, card = phrase),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-governance--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Data That Left Europe..."
            Scene(
                0, "The Data That Left Europe", policy(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "European users' data found in a US data centre.")),
                    Beat(2, card = BoardCard("Clue one", "There was a rule: Europe only.")),
                    Beat(4, show = setOf("req", "arm", "assign"), light = setOf("assign"), pulse = "assign" to "arm", card = BoardCard("Azure Policy", "A building inspector: checks every new or changed resource.")),
                ),
            ),
            // 5: "Subscription being, in plain words?"
            Scene(
                5, "Clue two", hierarchy(),
                beats = listOf(
                    Beat(6, show = setOf("s1", "s2", "s3"), light = setOf("s3"), card = BoardCard("A new subscription", "Created in March. Nobody assigned the rule there.")),
                    Beat(8, card = BoardCard("Not the Owner", "RBAC decides who acts; Policy decides what may exist.")),
                    Beat(10, show = tree, light = setOf("lz"), pulse = "lz" to "s2", card = BoardCard("Higher up", "A management group: rules flow down, even to new subscriptions.")),
                    Beat(12, light = setOf("lz", "sand"), card = BoardCard("Allowed locations, deny", "Assigned once on Landing zones. Sandbox stays outside.")),
                ),
            ),
            // 13: "What's a policy, really, under the picture?"
            Scene(
                13, "Under the picture", policy(),
                beats = listOf(
                    Beat(14, show = setOf("def", "init", "assign"), light = setOf("def"), pulse = "def" to "assign", card = BoardCard("If this, then that", "If the location is not on the list, then deny.")),
                    Beat(16, show = setOf("req", "arm", "report"), light = setOf("arm", "report"), pulse = "req" to "arm", card = BoardCard("Azure Resource Manager", "Checks the rules at that scope and above, then scans existing ones.")),
                ),
            ),
            // 17: "Clue three. I can feel it."
            Scene(
                17, "Clue three", effects(),
                beats = listOf(
                    Beat(18, show = setOf("audit"), light = setOf("audit"), card = BoardCard("It was audit", "Allowed, marked non-compliant. Red for six months.")),
                    Beat(20, show = setOf("deny"), light = setOf("deny"), card = BoardCard("Must not means deny", "Report without blocking means audit.")),
                ),
            ),
            // 21: "Is that the whole case?"
            Scene(
                21, "Side case: the bill", tags(),
                beats = listOf(
                    Beat(22, show = setOf("rg", "r1", "r2", "r3"), light = setOf("rg"), card = BoardCard("A tag", "A name and value label, like cost centre: mobile.")),
                    Beat(24, show = setOf("mod"), light = setOf("mod"), pulse = "mod" to "r2", card = BoardCard("Not inherited", "The inherit-a-tag policy, with the modify effect.")),
                ),
            ),
            // 25: "And the hundreds that already exist?"
            Scene(
                25, "The hundreds that exist", effects(),
                beats = listOf(
                    Beat(26, show = setOf("modify", "dine", "remed"), light = setOf("remed"), pulse = "modify" to "remed", card = BoardCard("Remediation task", "A clean-up run you start, as the assignment's managed identity.")),
                ),
            ),
            // 27: "Second side case?"
            Scene(
                27, "Side case: one click", locks(),
                beats = listOf(
                    Beat(28, show = setOf("db"), light = setOf("db"), card = BoardCard("One click away", "The production database, nearly deleted.")),
                    Beat(30, show = setOf("cnd", "ro", "note"), light = setOf("cnd", "note"), pulse = "cnd" to "db", card = BoardCard("Resource lock", "CanNotDelete: change, not delete. It stops Owners too.")),
                ),
            ),
            // 31: "And the auditor? Will she be back?"
            Scene(
                31, "The auditor returns", compliance(),
                beats = listOf(
                    Beat(32, show = setOf("def", "mcsb", "gdpr", "iso"), light = setOf("def", "gdpr"), pulse = "def" to "gdpr", card = BoardCard("Defender for Cloud", "Regulatory compliance: GDPR, control by control.")),
                    Beat(34, show = setOf("stacks"), light = setOf("stacks"), card = BoardCard("Blueprints retired", "It ends in January 2027. Deployment stacks replace it.")),
                ),
            ),
            // 35: "Anything about people?"
            Scene(
                35, "People", people(),
                beats = listOf(
                    Beat(36, show = setOf("entra", "rev"), light = setOf("rev"), pulse = "entra" to "rev", card = BoardCard("Access reviews", "Every quarter: who still needs access? The rest removed.")),
                    Beat(38, show = setOf("pkg", "life", "lic"), light = setOf("pkg"), pulse = "entra" to "pkg", card = BoardCard("Access packages", "Request, approve, expire. And Policy never decides who.")),
                ),
            ),
            // 39: "So, the case summary for the exam?"
            Scene(
                39, "Case closed", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = all6, card = phrase),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-governance--gossip",
        listOf(
            // 0: "Okay, did you hear what the Two Tracks cloud team did this week?"
            Scene(
                0, "The cloud team's week", hierarchy(),
                beats = listOf(
                    Beat(0, card = BoardCard("Governance", "How things are filed, who pays for what, what's never allowed.")),
                    Beat(2, show = setOf("tt", "s1", "s2", "s3"), light = setOf("tt", "s2"), card = BoardCard("An apartment block", "Subscription: one apartment. Management group: the building.")),
                    Beat(4, show = setOf("root", "rg"), light = setOf("root"), pulse = "root" to "tt", card = BoardCard("Tenant root group", "One folder at the very top of every directory.")),
                ),
            ),
            // 5: "So who went first?"
            Scene(
                5, "Monday: Tom", hierarchy(),
                beats = listOf(
                    Beat(6, show = tree + setOf("s1", "s2", "s3"), light = setOf("tt"), card = BoardCard("The org chart", "Marketing, Finance, Engineering. Then a reorganisation.")),
                    Beat(8, light = setOf("plat", "lz", "sand"), pulse = "tt" to "lz", card = BoardCard("By workload", "Shallow: three or four levels. Up to six below the root.")),
                    Beat(10, light = setOf("lz"), pulse = "lz" to "s2", card = BoardCard("Landing zones", "Subscriptions made ready first. Platform, apps, Sandbox.")),
                ),
            ),
            // 11: "Tuesday?"
            Scene(
                11, "Tuesday: Dana", policy(),
                beats = listOf(
                    Beat(12, show = setOf("assign"), light = setOf("assign"), card = BoardCard("One by one", "Allowed locations, assigned to each subscription by hand.")),
                    Beat(14, show = setOf("req", "arm", "def", "init", "report"), light = setOf("arm"), pulse = "req" to "arm", card = BoardCard("Azure Policy", "A building inspector: definition, initiative, assignment.")),
                    Beat(16, light = setOf("assign"), card = BoardCard("Deny, once, high up", "A new subscription missed it, and audit only reported.")),
                    Beat(18, light = setOf("def", "arm"), pulse = "assign" to "arm", card = BoardCard("If this, then that", "Resource Manager checks the rules at that scope and above.")),
                ),
            ),
            // 19: "Wednesday, then?"
            Scene(
                19, "Wednesday: Marcus", tags(),
                beats = listOf(
                    Beat(20, show = setOf("rg"), light = setOf("rg"), card = BoardCard("A tag on every box", "A name and value label, like cost centre: mobile.")),
                    Beat(21, show = setOf("r1", "r2", "r3"), light = setOf("r1", "r2", "r3"), card = BoardCard("No tags at all", "The resources inside had none.")),
                    Beat(22, show = setOf("mod"), light = setOf("mod"), pulse = "mod" to "r2", card = BoardCard("Not inherited", "Fixed by the inherit-a-tag policy, with modify.")),
                ),
            ),
            // 23: "Thursday?"
            Scene(
                23, "Thursday: the intern", locks(),
                beats = listOf(
                    Beat(24, show = setOf("db"), light = setOf("db"), card = BoardCard("Delete, clicked", "On the production database. Nothing happened.")),
                    Beat(26, show = setOf("cnd", "ro", "note"), light = setOf("cnd"), pulse = "cnd" to "db", card = BoardCard("CanNotDelete lock", "Change yes, delete no. It stops everyone, even Owners.")),
                ),
            ),
            // 27: "And Friday? There's always a Friday."
            Scene(
                27, "Friday: Priya", effects(),
                beats = listOf(
                    Beat(28, show = setOf("dine"), light = setOf("dine"), card = BoardCard("deployIfNotExists", "New databases got their diagnostics. Old ones didn't.")),
                    Beat(30, show = setOf("modify", "remed"), light = setOf("remed"), pulse = "dine" to "remed", card = BoardCard("Remediation task", "A clean-up run you start, as the assignment's managed identity.")),
                ),
            ),
            // 31: "And how do they prove all this to an auditor?"
            Scene(
                31, "The auditor", compliance(),
                beats = listOf(
                    Beat(32, show = setOf("def", "mcsb", "gdpr", "iso", "stacks"), light = setOf("def"), pulse = "def" to "gdpr", card = BoardCard("Defender for Cloud", "A report card against a standard. Blueprints: retired.")),
                ),
            ),
            // 33: "And the weekend gossip? People?"
            Scene(
                33, "The weekend: people", people(),
                beats = listOf(
                    Beat(34, show = setOf("entra", "rev"), light = setOf("rev"), pulse = "entra" to "rev", card = BoardCard("Access reviews", "Every quarter: who still needs access? The rest removed.")),
                    Beat(36, show = setOf("pkg", "life", "lic"), light = setOf("pkg", "life"), pulse = "entra" to "pkg", card = BoardCard("Access packages", "Request, approve, expire. Lifecycle workflows for joiners.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = all6, card = phrase),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-governance--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "House rules", hierarchy(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "House rules.")),
                    Beat(2, card = BoardCard("Governance", "How things are filed, who pays, and what's never allowed.")),
                    Beat(4, show = tree, light = setOf("root"), card = BoardCard("Management group", "The building: a folder of subscriptions. One root on top.")),
                    Beat(6, show = setOf("s1", "s2", "s3", "rg"), light = setOf("s2", "rg"), pulse = "lz" to "s2", card = BoardCard("Rules flow down", "To every apartment, even ones built next year.")),
                    Beat(8, light = setOf("plat", "lz", "sand"), card = BoardCard("By workload", "Not the org chart. Shallow. Production apart from test.")),
                    Beat(10, light = setOf("lz"), card = BoardCard("Landing zone", "Ready before the app moves in: water and power already on.")),
                ),
            ),
            // 11: "Now. What's never allowed?"
            Scene(
                11, "The inspector", policy(),
                beats = listOf(
                    Beat(12, show = setOf("req", "arm", "report"), light = setOf("arm"), pulse = "req" to "arm", card = BoardCard("Azure Policy", "Checks every new or changed resource; scans the rest.")),
                    Beat(14, show = setOf("def", "init", "assign"), light = setOf("def", "assign"), pulse = "assign" to "arm", card = BoardCard("If this, then that", "Definition, initiative, and an assignment to a scope.")),
                ),
            ),
            // 15: "And when something breaks a rule?"
            Scene(
                15, "What a rule does", effects(),
                beats = listOf(
                    Beat(16, show = setOf("deny", "audit"), light = setOf("deny", "audit"), card = BoardCard("deny or audit", "Must not: deny. Report without blocking: audit.")),
                ),
            ),
            // 17: "Mm. And what about tags?"
            Scene(
                17, "Tags", tags(),
                beats = listOf(
                    Beat(18, show = setOf("rg"), light = setOf("rg"), card = BoardCard("A tag", "A name and value label: split the bill, find an owner.")),
                    Beat(20, show = setOf("r1", "r2", "r3", "mod"), light = setOf("mod"), pulse = "mod" to "r2", card = BoardCard("Not inherited", "The modify effect copies the tag down.")),
                ),
            ),
            // 21: "And the things already in the box?"
            Scene(
                21, "Already in the box", effects(),
                beats = listOf(
                    Beat(22, show = setOf("modify", "dine", "remed"), light = setOf("remed"), pulse = "dine" to "remed", card = BoardCard("Remediation task", "Fixes what already exists, as the assignment's managed identity.")),
                ),
            ),
            // 23: "Policy sounds like it can do anything."
            Scene(
                23, "Who, or what", policy(),
                beats = listOf(
                    Beat(24, show = setOf("req", "arm", "assign"), light = setOf("req"), pulse = "assign" to "arm", card = BoardCard("Who versus what", "Azure RBAC: who may act. Policy: what may exist.")),
                ),
            ),
            // 25: "And what stops tired fingers deleting something precious?"
            Scene(
                25, "Tired fingers", locks(),
                beats = listOf(
                    Beat(26, show = setOf("db", "cnd", "ro", "note"), light = setOf("cnd", "note"), pulse = "cnd" to "db", card = BoardCard("Resource lock", "CanNotDelete or ReadOnly. It stops everyone, Owners too.")),
                ),
            ),
            // 27: "And proving we follow the rules?"
            Scene(
                27, "A report card", compliance(),
                beats = listOf(
                    Beat(28, show = setOf("def", "mcsb", "gdpr", "iso", "stacks"), light = setOf("def", "gdpr"), pulse = "def" to "gdpr", card = BoardCard("Defender for Cloud", "GDPR with a paid plan. Blueprints retired by January 2027.")),
                ),
            ),
            // 29: "And people? Who still has a key to the building?"
            Scene(
                29, "A key to the building", people(),
                beats = listOf(
                    Beat(30, show = setOf("entra", "rev", "pkg"), light = setOf("rev", "pkg"), pulse = "entra" to "rev", card = BoardCard("Access reviews, packages", "Confirm every quarter; request a bundle that expires.")),
                    Beat(32, show = setOf("life", "lic"), light = setOf("life"), pulse = "entra" to "life", card = BoardCard("Lifecycle workflows", "Join, move, leave. Paid features.")),
                ),
            ),
            // 33: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = phrase),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-governance--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", hierarchy(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "House rules.")),
                    Beat(2, card = BoardCard("Governance", "How things are filed, who pays, what may never happen.")),
                    Beat(4, show = setOf("tt", "plat", "lz", "sand", "s1", "s2", "s3", "rg"), light = setOf("s2"), card = BoardCard("Apartment and building", "Subscription: one apartment. Management group: the building.")),
                    Beat(6, show = setOf("root"), light = setOf("root"), pulse = "root" to "tt", card = BoardCard("Tenant root group", "One root on top; each subscription has one parent.")),
                    Beat(7, light = setOf("lz"), card = BoardCard("Quiz: EU only", "Every subscription, now and later. Where does the rule go?")),
                    Beat(8, light = setOf("lz", "s2"), pulse = "lz" to "s2", card = BoardCard("Right answer", "Once, on a management group: Allowed locations, with deny.")),
                ),
            ),
            // 10: "Mm. And the rule itself lives in Azure Policy..."
            Scene(
                10, "Under all that charm", policy(),
                beats = listOf(
                    Beat(10, show = setOf("req", "arm", "report"), light = setOf("arm"), pulse = "req" to "arm", card = BoardCard("Azure Policy", "An inspector at the door; scans what already exists.")),
                    Beat(12, show = setOf("def", "init", "assign"), light = setOf("def", "assign"), pulse = "assign" to "arm", card = BoardCard("If this, then that", "Definition, initiative, and an assignment to a scope.")),
                ),
            ),
            // 13: "And how do I arrange the folders?"
            Scene(
                13, "Never the org chart", hierarchy(),
                beats = listOf(
                    Beat(14, show = tree, light = setOf("plat", "lz", "sand"), pulse = "tt" to "lz", card = BoardCard("By workload", "Landing zones: Platform, apps, Sandbox. Shallow.")),
                ),
            ),
            // 15: "Now my question. Finance wants every resource to carry..."
            Scene(
                15, "Which effect?", tags(),
                beats = listOf(
                    Beat(15, show = setOf("rg", "r1", "r2", "r3"), light = setOf("rg"), card = BoardCard("Quiz: the cost centre tag", "Every resource carries its group's tag, automatically. Which effect?")),
                    Beat(16, card = BoardCard("Deny?", "It blocks deployments and copies nothing. No kiss.")),
                    Beat(17, show = setOf("mod"), light = setOf("mod"), pulse = "mod" to "r2", card = BoardCard("modify", "The inherit-a-tag policy, plus a remediation task.")),
                    Beat(18, card = BoardCard("Remediation task", "A clean-up run, as the assignment's managed identity.")),
                    Beat(20, light = setOf("rg"), card = BoardCard("A short tag list", "Cost centre, owner, environment, application, data class.")),
                ),
            ),
            // 21: "You'll earn it back. Your question."
            Scene(
                21, "Nothing missing", effects(),
                beats = listOf(
                    Beat(22, show = setOf("dine"), light = setOf("dine"), card = BoardCard("Quiz: database logs", "Every new database sends logs, no manual step. Which effect?")),
                    Beat(23, show = setOf("modify", "remed"), light = setOf("dine"), pulse = "dine" to "remed", card = BoardCard("Right answer", "deployIfNotExists adds the diagnostic setting.")),
                    Beat(26, show = setOf("deny", "audit"), light = setOf("deny", "audit"), card = BoardCard("deny or audit", "Must not: deny. Report: audit. Policy never decides who.")),
                ),
            ),
            // 27: "Then mine. Nobody may delete the production database by accident."
            Scene(
                27, "Worth protecting", locks(),
                beats = listOf(
                    Beat(27, show = setOf("db"), light = setOf("db"), card = BoardCard("Quiz: by accident", "Nobody may delete the production database by accident.")),
                    Beat(28, show = setOf("cnd", "ro", "note"), light = setOf("cnd"), pulse = "cnd" to "db", card = BoardCard("Right answer", "A CanNotDelete lock. It stops Owners too.")),
                ),
            ),
            // 30: "And to prove it all to an auditor..."
            Scene(
                30, "The auditor", compliance(),
                beats = listOf(
                    Beat(30, show = setOf("def", "mcsb", "gdpr", "iso", "stacks"), light = setOf("def"), pulse = "def" to "gdpr", card = BoardCard("Defender for Cloud", "A report card against GDPR and more. Blueprints: retired.")),
                ),
            ),
            // 31: "Now people. Guests from partner companies..."
            Scene(
                31, "Still welcome", people(),
                beats = listOf(
                    Beat(31, show = setOf("entra"), light = setOf("entra"), card = BoardCard("Quiz: partner guests", "Checked every quarter, removed if nobody confirms them?")),
                    Beat(32, show = setOf("rev"), light = setOf("rev"), pulse = "entra" to "rev", card = BoardCard("Right answer", "Access reviews: a sponsor confirms, the rest are removed.")),
                    Beat(34, show = setOf("pkg"), light = setOf("pkg"), card = BoardCard("Quiz: a bundle that expires", "Partners request resources, with approval and an end date?")),
                    Beat(35, show = setOf("life", "lic"), light = setOf("pkg"), pulse = "entra" to "pkg", card = BoardCard("Right answer", "Entitlement management: an access package.")),
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
