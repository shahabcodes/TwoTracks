package com.shahaabapps.core.content

/** Backup and disaster recovery: meeting RPO and RTO: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesBackupDr {
    private val classic = Podcast(
        "sa-backup-dr",
        listOf(
            maya("Hi, and welcome back. Today is backup and disaster recovery, a design topic for AZ-305. Our story is Two Tracks goes global. The Android study app is a company now, with a backend on Azure, users in Europe and Australia, an orders database, an old office system in London, and a promise to customers that the app stays up."),
            leo("A promise. That sounds like something that gets broken. [chuckle]"),
            maya("That's why we plan. Two words first. A backup is a copy you keep so you can go back after a mistake, like a photo of your notes each night. Disaster recovery, DR for short, is the plan for carrying on when a whole site is gone, like a spare office you can move into."),
            leo("And how do I know how much of each I need?"),
            maya("Two numbers set the whole design. RPO, the recovery point objective, is how much recent work you can afford to lose. If the last photo of your notes is from last night, and the house floods at teatime, you lose today."),
            leo("And the other number?"),
            maya("RTO, the recovery time objective. It's how long the shop may stay shut before it opens again. RPO looks back from the disaster to the last good copy. RTO looks forward, to the moment the app runs again."),
            leo("So the exam gives me those numbers and I pick?"),
            maya("Exactly. Say the office booking system may lose at most 15 minutes of data if its region fails. A region is one area of the world where Azure has its datacentres, like West Europe. Is a nightly backup enough?"),
            leo("No. A nightly backup could be almost a day old."),
            maya("Right. Minutes of data loss need replication: a live copy that follows every change, like a mirror. The copy itself is called a replica. But here's the twist. A mirror copies mistakes too."),
            leo("Meaning if somebody deletes a table..."),
            maya("It's gone from the replica seconds later. So surviving a lost region and undoing a mistake are two different needs. One wants replication, the other wants backup. Often you need both."),
            leo("Okay. Start with backup. What does Azure give me?"),
            maya("Azure Backup, the managed backup service. You choose what to protect, and a backup policy, which is the rule: how often to copy, and how long to keep each copy. The copies live in a vault, a locked store apart from your own resources, like a safe-deposit box at the bank."),
            leo("One vault for everything?"),
            maya("Two kinds, and the exam checks you know which. The Recovery Services vault is the older, broad one. It holds VMs, which are computers you rent in Azure, plus Azure Files shares, SQL Server inside a VM, and office servers. Site Recovery is managed from here too."),
            leo("And the other?"),
            maya("The Backup vault, the newer one for newer things: blobs, which are files in Blob Storage, managed disks, which are a VM's hard drives, and PostgreSQL databases. Put blob backup in a Recovery Services vault, and that's a wrong answer."),
            leo("How does a VM backup actually work?"),
            maya("First it takes a snapshot, a point-in-time photo of the disks. It keeps that close by for a fast restore, then copies it into the vault. For office servers, the MARS agent, a small program on each Windows server, sends files and folders straight to the vault."),
            leo("And blobs?"),
            maya("Two kinds. Operational backup stays inside the storage account, for a quick undo. Vaulted backup copies the blobs into a Backup vault, so they survive even if someone deletes the whole storage account. For example, if the question says survive deletion of the account, it's vaulted."),
            leo("What if the whole region with the vault goes down?"),
            maya("Make the vault geo-redundant: it also copies backups to the paired region, Azure's partner region far away. Cross Region Restore lets you restore there whenever you choose. Set this before protecting anything. And soft delete keeps deleted backups for 14 days, like a recycle bin, so an attacker can't wipe them at once."),
            leo("Now databases. The orders database is Azure SQL."),
            maya("Then it backs itself up with no set-up, saving its log of changes about every 10 minutes. Point-in-time restore, PITR, is a rewind button. It rebuilds the database as it was at any moment in the last 1 to 35 days, 7 by default, always as a new database."),
            leo("So someone deletes a row on Tuesday, I rewind to Monday night."),
            maya("Exactly. Now picture a small app with an auditor, who wants monthly copies kept for seven years. PITR stops at 35 days. Long-term retention, LTR, is the archive box in the loft: weekly, monthly or yearly copies kept for up to 10 years."),
            leo("And if the database's whole region goes down?"),
            maya("Cheapest is geo-restore: build a new database in another region from backups already copied there. You lose and wait minutes to hours. If that's too slow, a failover group keeps a standby database in another region, losing seconds and switching in under a minute, for the price of a second database."),
            leo("And the office machines? The London booking system?"),
            maya("Azure Site Recovery. It keeps a copy of whole machines somewhere else, by copying every change to their disks as it happens. It covers Azure VMs between regions, and office machines into Azure. When disaster strikes, you fail over: Azure builds the VMs from the copy, like moving into the spare office."),
            leo("And when the office is back?"),
            maya("You fail back home. A recovery plan sets the order, such as the database first, then the backend. But the most important bit is the test failover. It's a fire drill: it builds the copies in a sealed-off network while the real machines carry on. Users notice nothing."),
            leo("So you can prove the RTO without any downtime."),
            maya("Every quarter. The classic mistake is never testing, so the first real failover is also the first time anyone tries. And remember, Site Recovery is replication, so you still add Azure Backup for mistakes."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If it says undo a deleted row from last week, point-in-time restore. If it says keep yearly copies for years, long-term retention. If it says survive a region outage with an RPO of minutes, Site Recovery or a failover group. If it says prove DR with no downtime, a test failover. And if it says back up blobs, a Backup vault."),
            leo("And anything to try today?"),
            maya("Fifteen minutes with a small test VM. Create a Recovery Services vault and set its redundancy before protecting anything. Enable backup on the VM with the default policy and trigger a backup now. Open the policy and note how often it runs and how long it keeps points. Then read the Site Recovery steps to replicate a VM to another region."),
            leo("A photo every night, a spare office, and fire drills. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-backup-dr",
        listOf(
            maya("Welcome to Case Files. Today's case: The Table That Vanished Twice. Two Tracks has gone global. The Android study app is a company now, with a backend on Azure, an orders database, an old booking system in the London office, and a promise to customers that the app stays up. On Tuesday, a developer deleted the lessons table. By mistake."),
            leo("Detective Leo, on the case. Easy. Switch to the copy."),
            maya("That's what they tried. The database had a replica, a live copy in another region that follows every change, like a mirror. A region is an area of the world where Azure has its datacentres. They opened the replica. The table was gone there too."),
            leo("Suspect: the replica was broken."),
            maya("[pause] Wrong suspect. The replica worked perfectly. That's the problem. A mirror copies mistakes too: the deletion reached it seconds later. Replication is for surviving a lost region. Undoing a mistake needs a backup, a copy you can go back to, like a photo of your notes each night."),
            leo("So did they have one?"),
            maya("Luckily, yes. Azure SQL backs itself up with no set-up, saving its log of changes about every 10 minutes. Point-in-time restore, PITR, is a rewind button: it rebuilds the database as it was at any moment in the last 1 to 35 days, as a new database. They rewound to five minutes before the delete."),
            leo("Case closed, then?"),
            maya("Not even close. Clue two arrived on Wednesday: an auditor. Under GDPR, Europe's data protection law, the company keeps records for audits, and the auditor asked for a monthly copy of the orders database from three years ago."),
            leo("Suspect: PITR. Just stretch it to three years."),
            maya("Can't. PITR stops at 35 days. The right tool is long-term retention, LTR, the archive box in the loft: weekly, monthly or yearly copies kept for up to 10 years. Nobody had switched it on, so those copies never existed."),
            leo("Ouch. Clue three?"),
            maya("Thursday. The London booking system runs on office VMs, virtual machines: computers that run as software on a bigger machine. The design promised an RPO of 15 minutes. RPO, the recovery point objective, is how much recent work you can afford to lose."),
            leo("And the RTO, the recovery time objective, is how long you can be down. I've done my homework."),
            maya("[chuckle] Good. Then on Thursday the office server room flooded. The team rebuilt the booking system in Azure from the nightly backup. They lost almost a whole day of bookings."),
            leo("Suspect: the backup was slow."),
            maya("No. A backup is only as fresh as its last run. Promising minutes with a nightly backup was the mistake. Minutes of data loss need replication. For whole machines, that's Azure Site Recovery: it copies every change to the disks, as it happens, into Azure."),
            leo("And when disaster strikes?"),
            maya("You fail over: Azure builds the VMs from the copy, like moving into a spare office. Under the picture, it keeps copies of the disks up to date in Azure, plus a recipe for each machine, and builds the machines only when needed. Later, you fail back home. It's managed from a Recovery Services vault."),
            leo("Clue four. I can feel it."),
            maya("Friday. Somebody cleaned up an old storage account, the place the app's blobs, its files, live. It held the lesson images. The team had blob backup switched on. Operational backup."),
            leo("So they restored from that."),
            maya("They couldn't. Operational backup stays inside the storage account. Delete the account, and the backup goes with it. Vaulted backup copies the blobs into a Backup vault, the newer vault for blobs, disks and PostgreSQL, so they survive the account."),
            leo("Two vaults. Which holds what?"),
            maya("A vault is a locked store, apart from your own things, like a safe-deposit box at the bank. The Recovery Services vault holds VMs, Azure Files shares, SQL Server inside a VM, and office servers through the MARS agent, a small program on each Windows server. The Backup vault holds blobs, managed disks and PostgreSQL."),
            leo("Last witness?"),
            maya("The real disaster, a month later. When they finally failed over the booking system for real, it took six hours, not one. Nobody had ever tried it. The database came up after the app that needed it."),
            leo("Suspect: bad luck."),
            maya("[sigh] No. Never running a test failover. A test failover is a fire drill: it builds the copies in a sealed-off network while production carries on, so users notice nothing. And a recovery plan sets the order, database first, then the backend. Run it every quarter."),
            leo("Where does all this stop working?"),
            maya("Replication stops at mistakes; backup stops at freshness. And geo-restore, building a database in another region from copied backups, is cheapest but loses and waits minutes to hours. The high availability episode covers staying up inside a region, and the relational data episode covers the SQL tiers."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Undo a deleted row: point-in-time restore. Keep copies for years: long-term retention. Survive a region outage with an RPO of minutes: Site Recovery, not a nightly backup. Prove DR with no downtime: a test failover. Blobs that survive the account: vaulted backup in a Backup vault."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely, with a small test VM. Create a Recovery Services vault and set its redundancy before protecting anything. Enable backup on the VM with the default policy and trigger a backup now. Open the policy and note how often it runs and how long it keeps points. Then read the Site Recovery steps to replicate it to another region."),
            leo("No more tables vanishing twice. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-backup-dr",
        listOf(
            maya("Okay, did you hear what happened on the Two Tracks operations team this week? Sit down. The Android study app has gone global, users in Europe and Australia, a promise that the app stays up, and the boss asked one question: if everything breaks, how fast are we back?"),
            leo("[laugh] I heard the answer was not great. Where do we start?"),
            maya("With two numbers. RPO, the recovery point objective: how much recent work you can afford to lose. Picture a photo of your notes each night. If the house floods at teatime, you lose today. RTO, the recovery time objective: how long the shop may stay shut."),
            leo("And backup versus disaster recovery?"),
            maya("A backup is a copy you keep to go back after a mistake. Disaster recovery, DR, is the plan for carrying on when a whole site is gone, like a spare office you can move into. Which brings us to Monday, and Tom."),
            leo("Tom. What did Tom do?"),
            maya("Tom set up a replica of the orders database in another region. A replica is a live copy that follows every change, like a mirror, and a region is an area of the world where Azure has its datacentres. Then Tom said, we have a copy, so who needs backups?"),
            leo("And?"),
            maya("And on Tuesday, a script deleted half the lessons. The mirror copied it seconds later. Replication survives a lost region; it doesn't undo mistakes. Luckily Azure SQL backs itself up anyway, so the team used point-in-time restore, PITR: a rewind button to any moment in the last 1 to 35 days, as a new database."),
            leo("Lucky Tom. Wednesday?"),
            maya("Dana. The auditor wanted monthly copies of the orders database for seven years, for GDPR, Europe's data protection law. Dana set PITR to 35 days and said, that'll stretch."),
            leo("It doesn't stretch, does it."),
            maya("35 days is the ceiling. Long-term retention, LTR, is the archive box in the loft: weekly, monthly or yearly copies kept up to 10 years. Dana switched it on that afternoon. Only the years before that were missing. Well. Only."),
            leo("Thursday, then."),
            maya("Marcus. Marcus owns the London booking system, running on office VMs, virtual machines: computers that run as software on a bigger machine. He promised an RPO of 15 minutes. His protection? A nightly backup."),
            leo("Even I can see that. A nightly backup can be a day old."),
            maya("Exactly. Minutes need replication. For whole machines, that's Azure Site Recovery. It copies every change to their disks, as it happens, into Azure. When disaster strikes, you fail over, and Azure builds the VMs from the copy. Under the picture, it keeps the disk copies fresh and builds machines only when needed."),
            leo("And the backups for mistakes?"),
            maya("Azure Backup, the managed service. Copies live in a vault, a locked store apart from your resources, like a safe-deposit box at the bank. A backup policy is its rule: how often to copy, how long to keep. Marcus now has both. And Thursday night, the intern."),
            leo("Bless them. What did the intern do?"),
            maya("Tried to back up the lesson images, which are blobs, files in Blob Storage, into a Recovery Services vault. That's the older vault, for VMs, Azure Files, SQL Server in a VM, and office servers through the MARS agent, a small program on each Windows server."),
            leo("So blobs go where?"),
            maya("The Backup vault, the newer one for blobs, disks and PostgreSQL. And the intern needs vaulted backup, which copies blobs into that vault, not operational backup, which stays inside the storage account. Delete the account, and operational backup goes with it."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She set up Site Recovery perfectly, with a recovery plan, which sets the order: the database first, then the backend. Then she ran a test failover. On Friday afternoon."),
            leo("On a Friday? Is that bad?"),
            maya("That's the twist. It's not. A test failover is a fire drill: it builds the copies in a sealed-off network while production carries on. Users noticed nothing. Then she showed the boss the RTO, measured. Everyone else had just guessed theirs."),
            leo("[chuckle] Show-off. One thing. What if the vault's own region goes down?"),
            maya("Make the vault geo-redundant, so backups are also copied to the paired region, and turn on Cross Region Restore. Set it before protecting anything. And soft delete keeps deleted backups for 14 days, a recycle bin attackers can't empty at once."),
            leo("So where does all this stop working?"),
            maya("Replication stops at mistakes, and backup stops at freshness. Geo-restore of a database, from backups copied to another region, is cheapest, but loses and waits minutes to hours. The high availability episode covers staying up inside a region, and the relational data episode covers the SQL side."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Undo a deleted row: point-in-time restore. Copies for years: long-term retention. A region outage with an RPO of minutes: Site Recovery, never a nightly backup. Prove DR with no downtime: a test failover. Blobs: vaulted backup in a Backup vault."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Recovery Services vault and set its redundancy before protecting anything. Enable backup on a small VM with the default policy and trigger a backup now. Open the policy and note how often it runs and how long it keeps points. Then read the Site Recovery steps to replicate a VM to another region."),
            leo("Mirrors copy mistakes. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-backup-dr",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about what happens when things go wrong, and how to come back."),
            ivy("Mm. The things we lose, and the things we keep. Where do we start, Nova?"),
            nova("With Two Tracks. The Android study app has gone global: a company with a backend on Azure, an orders database, an old booking system in the London office, and a promise to customers that the app stays up. Even at three in the morning."),
            ivy("So what keeps that promise?"),
            nova("Two things. A backup is a copy you keep so you can go back after a mistake, like a photo of your notes each night. And disaster recovery, DR, is the plan for carrying on when a whole site is gone. Like a spare flat across town, with a key under the mat."),
            ivy("And how much do we need of each?"),
            nova("Two numbers decide. RPO, the recovery point objective, is how much recent work you can afford to lose. If your last photo is from last night, and the house floods at teatime, you lose today. [pause] RTO, the recovery time objective, is how long you can stay shut before you open again."),
            ivy("Looking back, and looking forward."),
            nova("Exactly. RPO looks back from the disaster to the last good copy. RTO looks forward to the moment the app runs again. Now. If the RPO is minutes, and a region could fail, a nightly backup can't do it. A region is an area of the world where Azure has its datacentres."),
            ivy("So what can?"),
            nova("Replication: a live copy that follows every change, like a mirror. The copy is called a replica. [pause] But a mirror copies mistakes too. Delete a table, and it's gone from the replica seconds later. So you need replication for regions, and backup for mistakes. Usually both."),
            ivy("Tell me about the backups, then. Where do they sleep?"),
            nova("In a vault. Azure Backup takes the copies, following a backup policy: how often to copy, how long to keep each one. A vault is a locked store apart from your own things, like a safe-deposit box at the bank."),
            ivy("One box, or two?"),
            nova("Two. The Recovery Services vault is the older one: VMs, which are computers you rent in Azure, Azure Files shares, SQL Server inside a VM, and office servers through the MARS agent, a little program on each Windows server. The Backup vault is the newer one: blobs, disks and PostgreSQL."),
            ivy("Blobs being?"),
            nova("Files in Blob Storage. Like the lesson images. Operational blob backup stays inside the storage account, for a quick undo. Vaulted backup copies them into the Backup vault, so they survive even if someone deletes the whole account. [pause] Some losses you can't come back from without it."),
            ivy("And if the vault's own region goes dark?"),
            nova("Make it geo-redundant, so backups are copied to the paired region far away, and turn on Cross Region Restore. Set that before you protect anything. And soft delete keeps deleted backups for 14 days, so nobody can wipe them in one night."),
            ivy("Now the orders database. What does it keep, on its own?"),
            nova("Azure SQL backs itself up, quietly, saving its log of changes about every 10 minutes. Point-in-time restore is a rewind button: any moment in the last 1 to 35 days, rebuilt as a new database. Long-term retention is the archive box in the loft: weekly, monthly or yearly copies, for up to 10 years."),
            ivy("And if its region goes down?"),
            nova("Geo-restore builds a new database in another region from backups copied there. Cheap, but you lose and wait minutes to hours. If that's too slow, a failover group keeps a standby database in another region, switching in under a minute. It costs a second database."),
            ivy("And the old booking system in London? The machines themselves?"),
            nova("Azure Site Recovery. Here's how it really works: it copies every change to the machines' disks as it happens, into Azure. When disaster strikes, you fail over, and Azure builds the VMs from that copy. Like moving into the spare flat. [pause] Later, you fail back home."),
            ivy("Mm. And how do you know it'll work, before the night it has to?"),
            nova("A test failover. A fire drill. It builds the copies in a sealed-off network while the real machines carry on, and nobody notices. A recovery plan sets the order: the database wakes first, then the backend. Do it every quarter, and you measure the RTO instead of guessing."),
            ivy("Where does all of this stop working, Nova?"),
            nova("Replication stops at mistakes. Backup stops at freshness: it's only as new as its last run. And point-in-time restore stops at 35 days. The high availability episode is your next late night, for staying up inside one region."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. Undo a deleted row means point-in-time restore. Copies for years mean long-term retention. A region outage with an RPO of minutes means Site Recovery, not a nightly backup. Prove DR with no downtime means a test failover. And blobs mean a Backup vault."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Recovery Services vault and set its redundancy before protecting anything. Enable backup on a small VM with the default policy and trigger a backup now. [pause] Open the policy and note how often it runs and how long it keeps points. Then read the Site Recovery steps to replicate a VM to another region."),
            ivy("A photo every night, and a spare flat across town. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-backup-dr",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about commitment. How much we can afford to lose, and how fast we bounce back."),
            rafe("Backup and disaster recovery. You really know how to set a mood. [laugh]"),
            nova("Two Tracks has gone global: a company with an orders database, an old booking system in the London office, and a promise to customers that the app stays up. A backup is a copy you keep to go back after a mistake. Disaster recovery, DR, is carrying on when a whole site is gone, like moving into a spare flat."),
            rafe("And two numbers decide everything. RPO, the recovery point objective, is how much recent work you can afford to lose. RTO, the recovery time objective, is how long you can be down. One looks back, one looks forward."),
            nova("Mm. First question, exam style. The booking system may lose at most 15 minutes of data if its region fails. A region is an area of the world where Azure has its datacentres. Is a nightly backup enough?"),
            rafe("No. A nightly backup can be a whole day old. Minutes need replication, a live copy that follows every change, like a mirror."),
            nova("Right answer. You never let me get too far behind. [kiss]"),
            rafe("And the copy is called a replica. But a mirror copies mistakes too: delete a table, and the replica loses it seconds later. So my question. Someone deletes a row from the orders database last Tuesday. Undo it how?"),
            nova("Point-in-time restore. Azure SQL backs itself up on its own, and PITR is a rewind button to any moment in the last 1 to 35 days, rebuilt as a new database."),
            rafe("Right answer. Rewind to before the argument. [kiss]"),
            nova("If only. Now mine. An auditor wants monthly copies of the orders database for seven years. What do you configure?"),
            rafe("PITR, stretched to seven years."),
            nova("[sigh] No kiss for that one. PITR stops at 35 days. Long-term retention, LTR, is the archive box in the loft: weekly, monthly or yearly copies, up to 10 years. Some memories you keep for a long time."),
            rafe("Fair. Now the vaults. Azure Backup keeps copies in a vault, a locked store like a safe-deposit box, following a backup policy: how often, how long. Which vault for the lesson images in blob containers?"),
            nova("Blobs being files in Blob Storage. The Backup vault, the newer one for blobs, disks and PostgreSQL. With vaulted backup, which copies them into the vault, so they survive even if the storage account is deleted."),
            rafe("Right answer. Some things should outlive the account. [kiss]"),
            nova("And the older one, the Recovery Services vault, holds VMs, which are computers you rent in Azure, Azure Files, SQL Server inside a VM, and office servers through the MARS agent, a small program on each Windows server. Operational blob backup stays inside the account, so it dies with it."),
            rafe("Then the big one. The London booking system, on VMs, must survive a region outage with an RPO of minutes and an RTO of an hour. What do you recommend?"),
            nova("Azure Site Recovery. It copies every change to the machines' disks as it happens, into a second region. When disaster strikes, you fail over, and Azure builds the VMs from the copy. Later, you fail back home. Plus Azure Backup, because replication copies mistakes."),
            rafe("Right answer, and the backup too. Thorough. [kiss]"),
            nova("Now I want proof you'll be there when it counts. The plan must be proven every quarter with no downtime for users. What do you run?"),
            rafe("A planned failover. Move it all over, then back."),
            nova("No kiss. A planned failover really moves production, and users see the switch. A test failover is the fire drill: it builds the copies in a sealed-off network while the real machines carry on. And a recovery plan sets the order, database first, then the backend."),
            rafe("A rehearsal before the real thing. And if the vault's own region goes down?"),
            nova("Make the vault geo-redundant, copying backups to the paired region, with Cross Region Restore. Set it before protecting anything. Soft delete keeps deleted backups 14 days, so nobody wipes them in one night."),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("Replication stops at mistakes; backup stops at freshness. And geo-restore, rebuilding a database in another region from copied backups, is cheapest but loses and waits minutes to hours. The high availability episode covers staying up inside a region."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Undo a deleted row: point-in-time restore. Copies for years: long-term retention. A region outage with an RPO of minutes: Site Recovery. Prove DR with no downtime: a test failover. Blobs: vaulted backup in a Backup vault."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a Recovery Services vault and set its redundancy before protecting anything. Enable backup on a small VM with the default policy and trigger a backup now. Open the policy and note how often it runs and how long it keeps points. Then read the Site Recovery steps to replicate a VM to another region."),
            nova("And you're the one thing I'd never let go for longer than my RPO. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun timeline() = DiagramSpec(
        "RPO looks back from the disaster to the last good copy; RTO looks forward to the moment the app runs again.",
        nodes = listOf(
            DNode("last", "Last good copy", 0.15f, 0.25f, w = 0.26f, h = 0.2f, tone = 1),
            DNode("boom", "Disaster", 0.5f, 0.25f, w = 0.22f, h = 0.2f, tone = 2),
            DNode("back", "Running again", 0.85f, 0.25f, w = 0.26f, h = 0.2f, tone = 1),
            DNode("rpo", "RPO: data you can lose", 0.26f, 0.7f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
            DNode("rto", "RTO: time until running", 0.75f, 0.7f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("last", "boom", "RPO"),
            DEdge("boom", "back", "RTO"),
        ),
        aspect = 0.8f,
    )

    private fun mirror() = DiagramSpec(
        "A replica follows every change, mistakes included; only a backup can rewind to before the mistake.",
        nodes = listOf(
            DNode("db", "Orders database", 0.25f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1),
            DNode("rep", "Replica", 0.75f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "another region"),
            DNode("oops", "Deleted table", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "a mistake"),
            DNode("gone", "Gone here too", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "seconds later"),
            DNode("backup", "Backup: rewind", 0.5f, 0.85f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "undoes the mistake"),
        ),
        edges = listOf(
            DEdge("db", "rep", "every change"),
            DEdge("oops", "gone", "copied"),
            DEdge("oops", "backup", "restore", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun vaults() = DiagramSpec(
        "The Recovery Services vault holds VMs, Files and office servers; the Backup vault holds blobs, disks and PostgreSQL.",
        nodes = listOf(
            DNode("rsv", "Recovery Services vault", 0.25f, 0.15f, w = 0.46f, h = 0.2f, kind = NodeKind.CYLINDER),
            DNode("bv", "Backup vault", 0.76f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER),
            DNode("vms", "VMs, Azure Files", 0.25f, 0.5f, w = 0.46f, h = 0.2f, sub = "SQL in a VM"),
            DNode("blobs", "Blobs, disks", 0.76f, 0.5f, w = 0.44f, h = 0.2f, sub = "PostgreSQL"),
            DNode("mars", "MARS agent", 0.25f, 0.85f, w = 0.46f, h = 0.2f, tone = 2, sub = "office servers"),
            DNode("vaulted", "Vaulted blob backup", 0.76f, 0.85f, w = 0.44f, h = 0.2f, tone = 1, sub = "survives the account"),
        ),
        edges = listOf(
            DEdge("vms", "rsv", "backup"),
            DEdge("blobs", "bv", "backup"),
            DEdge("mars", "rsv", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun sqlProt() = DiagramSpec(
        "Azure SQL rewinds with point-in-time restore, keeps years with long-term retention, and survives a region with geo-restore or a failover group.",
        nodes = listOf(
            DNode("db", "Orders database", 0.5f, 0.13f, w = 0.5f, h = 0.18f, kind = NodeKind.CYLINDER, tone = 1),
            DNode("pitr", "Point-in-time restore", 0.25f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "1 to 35 days"),
            DNode("ltr", "Long-term retention", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "up to 10 years"),
            DNode("geo", "Geo-restore", 0.25f, 0.8f, w = 0.46f, h = 0.2f, tone = 2, sub = "minutes to hours"),
            DNode("fog", "Failover group", 0.76f, 0.8f, w = 0.44f, h = 0.2f, tone = 1, sub = "under a minute"),
        ),
        edges = listOf(
            DEdge("db", "pitr"),
            DEdge("db", "ltr"),
            DEdge("db", "geo", dashed = true),
            DEdge("db", "fog", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun asr() = DiagramSpec(
        "Site Recovery copies every disk change to a second site and builds the VMs there on failover; a test failover is a sealed-off drill.",
        nodes = listOf(
            DNode("office", "Office VMs", 0.18f, 0.18f, w = 0.32f, h = 0.2f, tone = 2, sub = "London"),
            DNode("asr", "Site Recovery", 0.5f, 0.5f, w = 0.34f, h = 0.18f, tone = 1, sub = "copies every change"),
            DNode("azure", "Second region", 0.82f, 0.18f, w = 0.32f, h = 0.2f, sub = "VMs built on failover"),
            DNode("test", "Test failover", 0.25f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a sealed-off drill"),
            DNode("plan", "Recovery plan", 0.76f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "database first"),
        ),
        edges = listOf(
            DEdge("office", "asr", "replicate"),
            DEdge("asr", "azure", "fail over"),
            DEdge("asr", "test", dashed = true),
            DEdge("asr", "plan", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's backup and DR questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("row", "Undo a deleted row", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "point-in-time restore"),
            DNode("years", "Copies for years", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "long-term retention"),
            DNode("region", "Region lost, RPO minutes", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Site Recovery"),
            DNode("drill", "Prove DR, no downtime", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "test failover"),
            DNode("blobs", "Back up blobs", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Backup vault"),
            DNode("mirror", "Replication copies mistakes", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "add a backup"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("row", "years", "region", "drill", "blobs", "mirror")
    private val global = BoardCard("Two Tracks goes global", "Orders, an old office system, and a promise that the app stays up.")
    private val rpoRto = BoardCard("RPO and RTO", "Data you can lose, looking back; time you're shut, looking forward.")
    private val azureBackup = BoardCard("Azure Backup", "A backup policy: how often, how long. Copies live in a vault.")
    private val rsvCard = BoardCard("Recovery Services vault", "VMs, Azure Files, SQL in a VM, office servers by the MARS agent.")
    private val vaultedCard = BoardCard("Vaulted blob backup", "Copied into a Backup vault, so it survives deleting the account.")
    private val geoVault = BoardCard("Geo-redundant vault", "Copies in the paired region; Cross Region Restore. Set it first.")
    private val pitrCard = BoardCard("Point-in-time restore", "A rewind button: any moment in 1 to 35 days, as a new database.")
    private val ltrCard = BoardCard("Long-term retention", "PITR stops at 35 days. LTR keeps copies for up to 10 years.")
    private val siteRecovery = BoardCard("Azure Site Recovery", "Copies every disk change as it happens; fail over to build the VMs.")
    private val testFailover = BoardCard("Test failover", "A fire drill in a sealed-off network. Users notice nothing.")
    private val geoRestore = BoardCard("Geo-restore", "Cheapest way back after a lost region, but minutes to hours.")
    private val examCard = BoardCard("Listen for the phrase", "Each requirement points to one answer.")
    private val today = BoardCard("Today", "A vault with redundancy set, a VM backup now, the policy, the ASR steps.")

    private fun classicBoard() = PodcastBoard(
        "sa-backup-dr",
        listOf(
            // 0: "Hi, and welcome back. Today is backup and disaster recovery..."
            Scene(
                0, "Two Tracks goes global", timeline(),
                beats = listOf(
                    Beat(0, card = global),
                    Beat(2, show = setOf("last", "boom", "back"), light = setOf("boom"), card = BoardCard("Backup and DR", "A photo of your notes each night, and a spare office to move into.")),
                    Beat(4, show = setOf("rpo"), light = setOf("last", "rpo"), pulse = "last" to "boom", card = BoardCard("RPO", "How much recent work you can afford to lose. It looks back.")),
                    Beat(6, show = setOf("rto"), light = setOf("back", "rto"), pulse = "boom" to "back", card = BoardCard("RTO", "How long the shop may stay shut. It looks forward.")),
                ),
            ),
            // 7: "So the exam gives me those numbers and I pick?"
            Scene(
                7, "A mirror copies mistakes", mirror(),
                beats = listOf(
                    Beat(8, show = setOf("db"), light = setOf("db"), card = BoardCard("A region", "An area of the world with Azure datacentres. 15 minutes of loss?")),
                    Beat(10, show = setOf("rep"), light = setOf("rep"), pulse = "db" to "rep", card = BoardCard("Replication", "A live copy that follows every change, like a mirror: a replica.")),
                    Beat(12, show = setOf("oops", "gone", "backup"), light = setOf("gone", "backup"), pulse = "oops" to "gone", card = BoardCard("Mistakes copied too", "Regions need replication; mistakes need backup. Often both.")),
                ),
            ),
            // 13: "Okay. Start with backup. What does Azure give me?"
            Scene(
                13, "Two vaults", vaults(),
                beats = listOf(
                    Beat(14, show = setOf("rsv", "bv"), light = setOf("rsv", "bv"), card = azureBackup),
                    Beat(16, show = setOf("vms"), light = setOf("rsv", "vms"), pulse = "vms" to "rsv", card = rsvCard),
                    Beat(18, show = setOf("blobs"), light = setOf("bv", "blobs"), pulse = "blobs" to "bv", card = BoardCard("Backup vault", "The newer vault: blobs, managed disks and PostgreSQL.")),
                    Beat(20, show = setOf("mars"), light = setOf("mars"), pulse = "mars" to "rsv", card = BoardCard("Snapshot, then vault", "A photo of the disks for a fast restore; MARS for office servers.")),
                    Beat(22, show = setOf("vaulted"), light = setOf("vaulted"), card = vaultedCard),
                    Beat(24, light = setOf("rsv", "bv"), card = geoVault),
                ),
            ),
            // 25: "Now databases. The orders database is Azure SQL."
            Scene(
                25, "The orders database", sqlProt(),
                beats = listOf(
                    Beat(26, show = setOf("db", "pitr"), light = setOf("pitr"), pulse = "db" to "pitr", card = pitrCard),
                    Beat(28, show = setOf("ltr"), light = setOf("ltr"), pulse = "db" to "ltr", card = ltrCard),
                    Beat(30, show = setOf("geo", "fog"), light = setOf("geo", "fog"), pulse = "db" to "fog", card = BoardCard("Region lost", "Geo-restore is cheapest; a failover group switches in a minute.")),
                ),
            ),
            // 31: "And the office machines? The London booking system?"
            Scene(
                31, "A spare office", asr(),
                beats = listOf(
                    Beat(32, show = setOf("office", "asr", "azure"), light = setOf("asr"), pulse = "office" to "asr", card = siteRecovery),
                    Beat(34, show = setOf("test", "plan"), light = setOf("test", "plan"), pulse = "asr" to "test", card = testFailover),
                    Beat(36, light = setOf("test"), card = BoardCard("Test every quarter", "Never testing is the classic mistake. Add backup for mistakes.")),
                ),
            ),
            // 37: "So what do I remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("row", "years", "region", "drill", "blobs"), card = examCard),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-backup-dr--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Table That Vanished Twice..."
            Scene(
                0, "The Table That Vanished Twice", mirror(),
                beats = listOf(
                    Beat(0, show = setOf("db", "oops"), light = setOf("oops"), card = BoardCard("The case", "A developer deleted the lessons table. By mistake.")),
                    Beat(2, show = setOf("rep"), light = setOf("rep"), pulse = "db" to "rep", card = BoardCard("The replica", "A live copy in another region, like a mirror. Gone there too.")),
                    Beat(4, show = setOf("gone", "backup"), light = setOf("gone"), pulse = "oops" to "gone", card = BoardCard("Wrong suspect", "The replica worked. A mirror copies mistakes too.")),
                    Beat(6, light = setOf("backup"), pulse = "oops" to "backup", card = pitrCard),
                ),
            ),
            // 7: "Case closed, then?"
            Scene(
                7, "Clue two: the auditor", sqlProt(),
                beats = listOf(
                    Beat(8, show = setOf("db"), light = setOf("db"), card = BoardCard("A GDPR audit", "A monthly copy of the orders database from three years ago.")),
                    Beat(10, show = setOf("pitr", "ltr"), light = setOf("ltr"), pulse = "db" to "ltr", card = ltrCard),
                ),
            ),
            // 11: "Ouch. Clue three?"
            Scene(
                11, "Clue three: the flood", timeline(),
                beats = listOf(
                    Beat(12, show = setOf("last", "boom", "back", "rpo"), light = setOf("rpo"), pulse = "last" to "boom", card = BoardCard("RPO promised: 15 minutes", "Protected only by a nightly backup.")),
                    Beat(13, show = setOf("rto"), light = setOf("rto"), pulse = "boom" to "back", card = BoardCard("RTO", "The recovery time objective: how long you can be down.")),
                    Beat(14, light = setOf("last"), card = BoardCard("A day of bookings lost", "A backup is only as fresh as its last run.")),
                ),
            ),
            // 15: "Suspect: the backup was slow."
            Scene(
                15, "Minutes need replication", asr(),
                beats = listOf(
                    Beat(16, show = setOf("office", "asr", "azure"), light = setOf("asr"), pulse = "office" to "asr", card = siteRecovery),
                    Beat(18, light = setOf("azure"), pulse = "asr" to "azure", card = BoardCard("Fail over, fail back", "Azure builds the VMs from the disk copies. Later, back home.")),
                ),
            ),
            // 19: "Clue four. I can feel it."
            Scene(
                19, "Clue four: the account", vaults(),
                beats = listOf(
                    Beat(20, show = setOf("blobs"), light = setOf("blobs"), card = BoardCard("A storage account deleted", "The lesson images had operational backup only.")),
                    Beat(22, show = setOf("bv", "vaulted"), light = setOf("bv", "vaulted"), pulse = "blobs" to "bv", card = vaultedCard),
                    Beat(24, show = setOf("rsv", "vms", "mars"), light = setOf("rsv"), pulse = "vms" to "rsv", card = rsvCard),
                ),
            ),
            // 25: "Last witness?"
            Scene(
                25, "The last witness", asr(),
                beats = listOf(
                    Beat(26, show = setOf("office", "asr", "azure"), light = setOf("azure"), card = BoardCard("Six hours, not one", "The first real failover was also the first try.")),
                    Beat(28, show = setOf("test", "plan"), light = setOf("test", "plan"), pulse = "asr" to "test", card = BoardCard("Test failover", "A sealed-off drill each quarter. A recovery plan sets the order.")),
                ),
            ),
            // 29: "Where does all this stop working?"
            Scene(
                29, "Where it stops", sqlProt(),
                beats = listOf(
                    Beat(30, show = setOf("db", "pitr", "ltr", "geo", "fog"), light = setOf("geo"), pulse = "db" to "geo", card = geoRestore),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("row", "years", "region", "drill", "blobs"), card = examCard),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-backup-dr--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Two Tracks operations team this week?..."
            Scene(
                0, "The operations team's week", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("How fast are we back?", "The boss's question, now that Two Tracks has gone global.")),
                    Beat(2, show = setOf("last", "boom", "back", "rpo", "rto"), light = setOf("rpo", "rto"), pulse = "last" to "boom", card = rpoRto),
                    Beat(4, light = setOf("boom"), card = BoardCard("Backup and DR", "A copy to undo a mistake; a plan to carry on without a site.")),
                ),
            ),
            // 5: "Tom. What did Tom do?"
            Scene(
                5, "Monday: Tom", mirror(),
                beats = listOf(
                    Beat(6, show = setOf("db", "rep"), light = setOf("rep"), pulse = "db" to "rep", card = BoardCard("A replica, no backups", "A live copy in another region. Who needs backups, he said.")),
                    Beat(8, show = setOf("oops", "gone", "backup"), light = setOf("gone"), pulse = "oops" to "gone", card = BoardCard("The mirror copied it", "Replication survives regions, not mistakes. PITR saved them.")),
                ),
            ),
            // 9: "Lucky Tom. Wednesday?"
            Scene(
                9, "Wednesday: Dana", sqlProt(),
                beats = listOf(
                    Beat(10, show = setOf("db", "pitr"), light = setOf("pitr"), pulse = "db" to "pitr", card = BoardCard("Seven years of PITR?", "Monthly copies for seven years, for GDPR.")),
                    Beat(12, show = setOf("ltr"), light = setOf("ltr"), pulse = "db" to "ltr", card = ltrCard),
                ),
            ),
            // 13: "Thursday, then."
            Scene(
                13, "Thursday: Marcus", asr(),
                beats = listOf(
                    Beat(14, show = setOf("office"), light = setOf("office"), card = BoardCard("RPO of 15 minutes", "Promised, with only a nightly backup behind it.")),
                    Beat(16, show = setOf("asr", "azure"), light = setOf("asr"), pulse = "office" to "asr", card = siteRecovery),
                ),
            ),
            // 17: "And the backups for mistakes?"
            Scene(
                17, "Thursday night: the intern", vaults(),
                beats = listOf(
                    Beat(18, show = setOf("rsv", "bv"), light = setOf("rsv", "bv"), card = azureBackup),
                    Beat(20, show = setOf("vms", "mars", "blobs"), light = setOf("rsv"), pulse = "vms" to "rsv", card = BoardCard("Not for blobs", "The Recovery Services vault: VMs, Files, SQL in a VM, MARS.")),
                    Beat(22, show = setOf("vaulted"), light = setOf("bv", "vaulted"), pulse = "blobs" to "bv", card = vaultedCard),
                ),
            ),
            // 23: "And Friday? There's always a Friday."
            Scene(
                23, "Friday: Priya", asr(),
                beats = listOf(
                    Beat(24, show = setOf("office", "asr", "azure", "plan"), light = setOf("plan"), card = BoardCard("A recovery plan", "The database first, then the backend.")),
                    Beat(26, show = setOf("test"), light = setOf("test"), pulse = "asr" to "test", card = BoardCard("A Friday test failover", "A sealed-off drill. The RTO measured, not guessed.")),
                    Beat(28, light = setOf("azure"), card = geoVault),
                ),
            ),
            // 29: "So where does all this stop working?"
            Scene(
                29, "Where it stops", sqlProt(),
                beats = listOf(
                    Beat(30, show = setOf("db", "pitr", "ltr", "geo", "fog"), light = setOf("geo"), pulse = "db" to "geo", card = geoRestore),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("row", "years", "region", "drill", "blobs"), card = examCard),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-backup-dr--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "When things go wrong", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "What happens when things go wrong, and how to come back.")),
                    Beat(2, card = global),
                    Beat(4, show = setOf("last", "boom", "back"), light = setOf("boom"), card = BoardCard("Backup and DR", "A photo of your notes each night; a spare flat across town.")),
                    Beat(6, show = setOf("rpo", "rto"), light = setOf("rpo", "rto"), pulse = "last" to "boom", card = rpoRto),
                ),
            ),
            // 7: "Looking back, and looking forward."
            Scene(
                7, "The mirror", mirror(),
                beats = listOf(
                    Beat(8, show = setOf("db"), light = setOf("db"), card = BoardCard("A region", "An area of the world where Azure has its datacentres.")),
                    Beat(10, show = setOf("rep", "oops", "gone", "backup"), light = setOf("gone"), pulse = "oops" to "gone", card = BoardCard("A replica copies mistakes", "Replication for regions, backup for mistakes. Usually both.")),
                ),
            ),
            // 11: "Tell me about the backups, then. Where do they sleep?"
            Scene(
                11, "Where backups sleep", vaults(),
                beats = listOf(
                    Beat(12, show = setOf("rsv", "bv"), light = setOf("rsv", "bv"), card = azureBackup),
                    Beat(14, show = setOf("vms", "mars", "blobs"), light = setOf("rsv", "bv"), pulse = "vms" to "rsv", card = BoardCard("Two vaults", "Older: VMs, Files, MARS. Newer: blobs, disks, PostgreSQL.")),
                    Beat(16, show = setOf("vaulted"), light = setOf("vaulted"), pulse = "blobs" to "bv", card = vaultedCard),
                    Beat(18, card = geoVault),
                ),
            ),
            // 19: "Now the orders database. What does it keep, on its own?"
            Scene(
                19, "The orders database", sqlProt(),
                beats = listOf(
                    Beat(20, show = setOf("db", "pitr", "ltr"), light = setOf("pitr", "ltr"), pulse = "db" to "pitr", card = BoardCard("PITR and LTR", "Rewind 1 to 35 days; archive copies kept up to 10 years.")),
                    Beat(22, show = setOf("geo", "fog"), light = setOf("geo", "fog"), pulse = "db" to "fog", card = BoardCard("Region lost", "Geo-restore is cheap and slow; a failover group, under a minute.")),
                ),
            ),
            // 23: "And the old booking system in London? The machines themselves?"
            Scene(
                23, "The spare flat", asr(),
                beats = listOf(
                    Beat(24, show = setOf("office", "asr", "azure"), light = setOf("asr"), pulse = "office" to "asr", card = siteRecovery),
                    Beat(26, show = setOf("test", "plan"), light = setOf("test"), pulse = "asr" to "test", card = testFailover),
                    Beat(28, card = BoardCard("Where it stops", "Replication copies mistakes; backup is only as new as its last run.")),
                ),
            ),
            // 29: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all6, light = setOf("row", "years", "region", "drill", "blobs"), card = examCard),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-backup-dr--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", timeline(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Commitment: how much we can lose, how fast we bounce back.")),
                    Beat(2, show = setOf("last", "boom", "back"), light = setOf("boom"), card = BoardCard("Backup and DR", "A copy to undo a mistake; a spare flat when a site is gone.")),
                    Beat(3, show = setOf("rpo", "rto"), light = setOf("rpo", "rto"), pulse = "last" to "boom", card = rpoRto),
                ),
            ),
            // 4: "Mm. First question, exam style..."
            Scene(
                4, "Too far behind", mirror(),
                beats = listOf(
                    Beat(4, show = setOf("db"), light = setOf("db"), card = BoardCard("Quiz: region lost, 15 minutes", "Is a nightly backup enough?")),
                    Beat(5, show = setOf("rep"), light = setOf("rep"), pulse = "db" to "rep", card = BoardCard("Right answer", "No. Minutes need replication: a live copy, like a mirror.")),
                    Beat(7, show = setOf("oops", "gone", "backup"), light = setOf("gone"), pulse = "oops" to "gone", card = BoardCard("Quiz: a deleted row", "The mirror copied the delete. Undo it how?")),
                ),
            ),
            // 8: "Point-in-time restore..."
            Scene(
                8, "Rewind", sqlProt(),
                beats = listOf(
                    Beat(8, show = setOf("db", "pitr"), light = setOf("pitr"), pulse = "db" to "pitr", card = BoardCard("Right answer", "Point-in-time restore: any moment in 1 to 35 days.")),
                    Beat(10, show = setOf("ltr"), light = setOf("db"), card = BoardCard("Quiz: seven years", "Monthly copies for an auditor. What do you configure?")),
                    Beat(12, light = setOf("ltr"), pulse = "db" to "ltr", card = BoardCard("No kiss: LTR", "PITR stops at 35 days. Long-term retention keeps up to 10 years.")),
                ),
            ),
            // 13: "Fair. Now the vaults..."
            Scene(
                13, "Outliving the account", vaults(),
                beats = listOf(
                    Beat(13, show = setOf("blobs"), light = setOf("blobs"), card = BoardCard("Quiz: blob containers", "Which vault for the lesson images?")),
                    Beat(14, show = setOf("bv", "vaulted"), light = setOf("bv", "vaulted"), pulse = "blobs" to "bv", card = BoardCard("Right answer", "The Backup vault, vaulted backup: it survives the account.")),
                    Beat(16, show = setOf("rsv", "vms", "mars"), light = setOf("rsv"), pulse = "vms" to "rsv", card = rsvCard),
                ),
            ),
            // 17: "Then the big one. The London booking system..."
            Scene(
                17, "The big one", asr(),
                beats = listOf(
                    Beat(17, show = setOf("office"), light = setOf("office"), card = BoardCard("Quiz: RPO minutes, RTO an hour", "The London VMs must survive a region outage.")),
                    Beat(18, show = setOf("asr", "azure"), light = setOf("asr"), pulse = "office" to "asr", card = BoardCard("Right answer", "Site Recovery, plus Azure Backup for mistakes.")),
                    Beat(20, show = setOf("test", "plan"), light = setOf("test"), card = BoardCard("Quiz: proven, no downtime", "Every quarter, no downtime for users. What do you run?")),
                    Beat(22, light = setOf("test", "plan"), pulse = "asr" to "test", card = BoardCard("No kiss: test failover", "A planned failover moves production. A drill doesn't.")),
                    Beat(24, light = setOf("azure"), card = geoVault),
                ),
            ),
            // 25: "And where does it all stop working? Every romance has its limits."
            Scene(
                25, "Every romance has limits", sqlProt(),
                beats = listOf(
                    Beat(26, show = setOf("db", "pitr", "ltr", "geo", "fog"), light = setOf("geo"), pulse = "db" to "geo", card = geoRestore),
                ),
            ),
            // 27: "Last question, for the exam. What do we remember?"
            Scene(
                27, "For the exam", exam(),
                beats = listOf(
                    Beat(28, show = all6, light = setOf("row", "years", "region", "drill", "blobs"), card = BoardCard("Listen for the phrase", "Each requirement points to one answer. Five for five.")),
                    Beat(31, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
