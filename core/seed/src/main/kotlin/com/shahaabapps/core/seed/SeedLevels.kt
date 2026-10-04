package com.shahaabapps.core.seed

import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.PersonRole

/** The two ladders: the guide's quarter-by-quarter roadmap (page 12) as levels. */
object SeedLevels {

    private fun goal(id: String, title: String, goalId: String, due: String? = null, required: Boolean = true, detail: String = "") =
        LevelItem(id = id, kind = LevelItemKind.GOAL, title = title, goalId = goalId, dueDateIso = due, required = required, detail = detail)

    private fun task(id: String, title: String, detail: String = "", due: String? = null, required: Boolean = true) =
        LevelItem(id = id, kind = LevelItemKind.TASK, title = title, detail = detail, dueDateIso = due, required = required)

    private fun count(id: String, title: String, habitId: String, detail: String = "") =
        LevelItem(id = id, kind = LevelItemKind.COUNT, title = title, habitId = habitId, detail = detail, required = false)

    val tech: List<Level> = listOf(
        Level(
            id = "tech-1", owner = PersonRole.TECH, number = 1, name = "Foundations",
            summary = "German A1 and AZ-204, plus the paperwork that makes Germany possible.",
            startDateIso = "2026-10-01", dueDateIso = "2027-03-31",
            items = listOf(
                goal("t1-de", "German A1", "de-a1", due = "2026-12-31"),
                goal("t1-az", "AZ-204 passed", "az-204", due = "2027-03-31"),
                task("t1-anabin", "anabin or ZAB degree check", "Check your university and degree on anabin; if unclear, order a ZAB Statement of Comparability (takes months)."),
                task("t1-cv", "German-style CV", "2 pages, photo is common."),
                task("t1-profiles", "LinkedIn and XING profiles", "Say you can relocate in 6 to 8 weeks."),
                goal("t1-psm", "PSM I (optional)", "psm-1", required = false, detail = "Two weeks of evenings; Scrum appears in most German and Irish ads."),
            ),
        ),
        Level(
            id = "tech-2", owner = PersonRole.TECH, number = 2, name = "Launch applications",
            summary = "Start applying to Germany and Ireland while German moves to A2.",
            startDateIso = "2027-01-01", dueDateIso = "2027-03-31",
            items = listOf(
                goal("t2-de", "German A2", "de-a2", due = "2027-03-31"),
                task("t2-github", "GitHub project live", ".NET API + Angular + Docker + Azure."),
                count("t2-apps", "12 applications a week", "applications", "LinkedIn, StepStone, XING, Make it in Germany, Arbeitnow; ask for referrals."),
            ),
        ),
        Level(
            id = "tech-3", owner = PersonRole.TECH, number = 3, name = "Widen the net",
            summary = "Add the Netherlands and get the Australia and Canada paperwork moving.",
            startDateIso = "2027-04-01", dueDateIso = "2027-06-30",
            items = listOf(
                task("t3-nl", "Add the Netherlands to applications", "Filter LinkedIn by visa sponsorship; check the IND sponsor register."),
                goal("t3-ielts", "IELTS General taken", "ielts-general-tech", due = "2027-06-30"),
                task("t3-acs", "ACS skills assessment lodged", "Australia; may count only 4 of your 6 years."),
                task("t3-wes", "WES assessment ordered", "Canada ECA.", required = false),
                goal("t3-cert", "One Tier 2 cert (optional)", "ckad", required = false, detail = "Pick CKAD, AZ-400 or Java SE 21. Only if weekend slots exist."),
            ),
        ),
        Level(
            id = "tech-4", owner = PersonRole.TECH, number = 4, name = "Land the offer",
            summary = "German B1 course, interviews, and an offer that clears the Blue Card threshold.",
            startDateIso = "2027-07-01", dueDateIso = "2027-09-30",
            items = listOf(
                goal("t4-de", "German B1", "de-b1", due = "2027-09-30"),
                task("t4-interviews", "Interviews under way"),
                task("t4-offer", "Job offer received", "Check base salary clears EUR 50,700 (or EUR 45,934 for IT shortage roles)."),
                goal("t4-naati", "NAATI CCL Hindi (optional)", "naati-tech", required = false),
            ),
        ),
        Level(
            id = "tech-5", owner = PersonRole.TECH, number = 5, name = "Checkpoint",
            summary = "European offer? Apply for both visas together. No offer? Open the backup routes.",
            startDateIso = "2027-09-30", dueDateIso = "2027-12-31",
            items = listOf(
                task("t5-visas", "Offer: apply for both visas at the German mission in Qatar"),
                task("t5-eoi", "No offer: lodge Australian EOI for the higher scorer", required = false),
                task("t5-backups", "Keep backup routes open until one succeeds", required = false),
            ),
        ),
    )

    val teacher: List<Level> = listOf(
        Level(
            id = "teach-1", owner = PersonRole.TEACHER, number = 1, name = "Build the base",
            summary = "IELTS prep in class, degrees attested, teaching on a formal contract.",
            startDateIso = "2026-10-01", dueDateIso = "2026-12-31",
            items = listOf(
                goal("h1-ielts", "IELTS Academic prep hours", "ielts-academic", due = "2026-12-31", detail = "Class covers about 10 hours a week."),
                task("h1-hec", "HEC attestation of degrees"),
                task("h1-contract", "Teaching on a formal contract, salary into bank", "Needed for Canada and Australia experience proof."),
            ),
        ),
        Level(
            id = "teach-2", owner = PersonRole.TEACHER, number = 2, name = "Take the test",
            summary = "Sit IELTS Academic and get the marriage paperwork right.",
            startDateIso = "2027-01-01", dueDateIso = "2027-03-31",
            items = listOf(
                task("h2-ielts", "IELTS Academic taken: R7 W7 S8 L8", "One sitting. Record the result under Tests."),
                task("h2-marriage", "Marriage registered and certificate attested", "Names must match both passports exactly.", due = "2027-02-28"),
            ),
        ),
        Level(
            id = "teach-3", owner = PersonRole.TEACHER, number = 3, name = "Assessments",
            summary = "Australia and New Zealand paperwork, and the French decision.",
            startDateIso = "2027-04-01", dueDateIso = "2027-06-30",
            items = listOf(
                task("h3-aitsl", "AITSL assessment lodged", "Needs the IELTS bar; fee about AUD 1,154."),
                task("h3-nz", "Teaching Council NZ registration checked", "And whether degrees need NZQA assessment."),
                task("h3-french", "Decide on French", "Only if Canada becomes the target and about 1 hour a day is possible for 12 to 18 months."),
                goal("h3-french-goal", "French started (optional)", "french", required = false),
            ),
        ),
        Level(
            id = "teach-4", owner = PersonRole.TEACHER, number = 4, name = "One year taught",
            summary = "Reach 12 months of documented teaching; turn 25 into the top Australian age band.",
            startDateIso = "2027-07-01", dueDateIso = "2027-09-30",
            items = listOf(
                count("h4-days", "Teaching days toward 12 months", "teaching-days"),
                task("h4-year", "12 months of full-time teaching documented", "Experience letters with dates, hours, salary, duties."),
                goal("h4-de", "German A1 (optional)", "de-a1-teacher", required = false),
            ),
        ),
        Level(
            id = "teach-5", owner = PersonRole.TEACHER, number = 5, name = "Checkpoint",
            summary = "NZ teaching jobs and the Canada profile if Europe has not come through.",
            startDateIso = "2027-09-30", dueDateIso = "2027-12-31",
            items = listOf(
                task("h5-nz", "Apply for NZ teaching jobs via the Education Gazette"),
                task("h5-canada", "Create the Canada Express Entry profile"),
                goal("h5-tef", "TEF Canada (optional)", "french", required = false),
            ),
        ),
    )

    val all: List<Level> get() = tech + teacher
}
