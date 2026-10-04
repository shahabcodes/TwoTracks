package com.shahaabapps.core.seed

import com.shahaabapps.core.model.Milestone
import com.shahaabapps.core.model.PersonRole

/** The quarter-by-quarter roadmap from guide page 12. */
object SeedRoadmap {

    private var counter = 0
    private fun m(owner: PersonRole, quarter: String, order: Int, title: String, due: String, country: String? = null, checkpoint: Boolean = false) =
        Milestone(
            id = "ms-${++counter}",
            owner = owner,
            quarter = quarter,
            quarterOrder = order,
            title = title,
            dueDateIso = due,
            countryCode = country,
            isCheckpoint = checkpoint,
        )

    val all: List<Milestone> = listOf(
        // Oct-Dec 2026
        m(PersonRole.TECH, "Oct-Dec 2026", 1, "anabin / ZAB degree check", "2026-12-31", "DE"),
        m(PersonRole.TECH, "Oct-Dec 2026", 1, "German A1", "2026-12-31"),
        m(PersonRole.TECH, "Oct-Dec 2026", 1, "Start AZ-204", "2026-12-31"),
        m(PersonRole.TECH, "Oct-Dec 2026", 1, "German-style CV (2 pages, photo)", "2026-12-31", "DE"),
        m(PersonRole.TECH, "Oct-Dec 2026", 1, "LinkedIn + XING profiles", "2026-12-31", "DE"),
        m(PersonRole.TEACHER, "Oct-Dec 2026", 1, "Keep teaching on a formal contract (salary into bank)", "2026-12-31"),
        m(PersonRole.TEACHER, "Oct-Dec 2026", 1, "IELTS Academic prep", "2026-12-31"),
        m(PersonRole.TEACHER, "Oct-Dec 2026", 1, "HEC-attest degrees", "2026-12-31"),
        // Jan-Mar 2027
        m(PersonRole.TECH, "Jan-Mar 2027", 2, "Pass AZ-204", "2027-03-31"),
        m(PersonRole.TECH, "Jan-Mar 2027", 2, "GitHub project (.NET API + Angular + Docker + Azure)", "2027-03-31", "DE"),
        m(PersonRole.TECH, "Jan-Mar 2027", 2, "German A2", "2027-03-31"),
        m(PersonRole.TECH, "Jan-Mar 2027", 2, "Apply to Germany + Ireland (10-15 per week)", "2027-03-31", "DE"),
        m(PersonRole.TEACHER, "Jan-Mar 2027", 2, "Take IELTS Academic (target R7 W7 S8 L8+)", "2027-03-31"),
        m(PersonRole.TEACHER, "Jan-Mar 2027", 2, "Marriage (Feb): register + attest certificate", "2027-02-28"),
        // Apr-Jun 2027
        m(PersonRole.TECH, "Apr-Jun 2027", 3, "Add the Netherlands to applications", "2027-06-30", "NL"),
        m(PersonRole.TECH, "Apr-Jun 2027", 3, "IELTS General", "2027-06-30"),
        m(PersonRole.TECH, "Apr-Jun 2027", 3, "ACS skills assessment", "2027-06-30", "AU"),
        m(PersonRole.TEACHER, "Apr-Jun 2027", 3, "Keep teaching (Pakistan or Qatar school)", "2027-06-30"),
        m(PersonRole.TEACHER, "Apr-Jun 2027", 3, "AITSL assessment if IELTS meets the bar", "2027-06-30", "AU"),
        m(PersonRole.TEACHER, "Apr-Jun 2027", 3, "Check Teaching Council NZ registration", "2027-06-30", "NZ"),
        m(PersonRole.TEACHER, "Apr-Jun 2027", 3, "Start French if Canada is chosen", "2027-06-30", "CA"),
        // Jul-Sep 2027
        m(PersonRole.TECH, "Jul-Sep 2027", 4, "German B1 course", "2027-09-30"),
        m(PersonRole.TECH, "Jul-Sep 2027", 4, "Interviews; aim for an offer", "2027-09-30", "DE"),
        m(PersonRole.TEACHER, "Jul-Sep 2027", 4, "Turns 25 (top Australian age band)", "2027-07-01", "AU"),
        m(PersonRole.TEACHER, "Jul-Sep 2027", 4, "About 1 year of teaching by September", "2027-09-30"),
        // Checkpoint
        m(PersonRole.TECH, "Checkpoint Sep 2027", 5, "European offer? Apply for both visas together and move", "2027-09-30", checkpoint = true),
        m(PersonRole.TEACHER, "Checkpoint Sep 2027", 5, "No offer? Compare Australian points, lodge EOI for the higher scorer", "2027-09-30", "AU", checkpoint = true),
        m(PersonRole.TEACHER, "Checkpoint Sep 2027", 5, "Create her Canada Express Entry profile", "2027-09-30", "CA", checkpoint = true),
        m(PersonRole.TEACHER, "Checkpoint Sep 2027", 5, "Apply for NZ teaching jobs (Education Gazette)", "2027-09-30", "NZ", checkpoint = true),
        // 2028
        m(PersonRole.TECH, "2028", 6, "Keep backup routes open until one succeeds", "2028-12-31"),
        m(PersonRole.TEACHER, "2028", 6, "French practice test / TEF Canada if on that route", "2028-06-30", "CA"),
    )
}
