package com.shahaabapps.core.seed

import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TestRecord

/** Official tests, the document checklist (page 12) and budget lines. */
object SeedRecords {

    private val bands = listOf("Listening", "Reading", "Writing", "Speaking")

    val tests: List<TestRecord> = listOf(
        TestRecord(
            id = "t-ielts-academic", owner = PersonRole.TEACHER, name = "IELTS Academic", order = 1,
            purpose = "AITSL (Australia), NZ teacher registration, Australian points. One sitting.",
            targetBands = mapOf("Listening" to "8.0", "Reading" to "7.0", "Writing" to "7.0", "Speaking" to "8.0"),
        ),
        TestRecord(
            id = "t-ielts-general-tech", owner = PersonRole.TECH, name = "IELTS General Training", order = 2,
            purpose = "Canada Express Entry (CLB 9-10) and Australian points.",
            targetBands = mapOf("Listening" to "8.0", "Reading" to "7.0", "Writing" to "7.0", "Speaking" to "7.0"),
        ),
        TestRecord(
            id = "t-german", owner = PersonRole.TECH, name = "German (Goethe / telc)", order = 3,
            purpose = "A2 by month 6, B1 by month 10-12. B1 required for citizenship.",
            targetBands = mapOf("Level" to "B1"),
        ),
        TestRecord(
            id = "t-az204", owner = PersonRole.TECH, name = "AZ-204 exam", order = 4,
            purpose = "Azure Developer Associate for the CV.",
            targetBands = mapOf("Score" to "700+"),
        ),
        TestRecord(
            id = "t-ielts-general-teacher", owner = PersonRole.TEACHER, name = "IELTS General Training (if Canada)", order = 5,
            purpose = "Canada Express Entry only accepts General Training.",
            targetBands = bands.associateWith { "7.0" },
        ),
        TestRecord(
            id = "t-french", owner = PersonRole.TEACHER, name = "TEF / TCF Canada (if Canada)", order = 6,
            purpose = "NCLC 7 in all four skills unlocks +50 points.",
            targetBands = bands.associateWith { "NCLC 7" },
        ),
        TestRecord(
            id = "t-naati-tech", owner = PersonRole.TECH, name = "NAATI CCL (Hindi)", order = 7, purpose = "+5 Australian points.",
            targetBands = mapOf("Result" to "Pass"),
        ),
        TestRecord(
            id = "t-naati-teacher", owner = PersonRole.TEACHER, name = "NAATI CCL (Urdu)", order = 8, purpose = "+5 Australian points.",
            targetBands = mapOf("Result" to "Pass"),
        ),
    )

    private var docCounter = 0
    private fun doc(owner: PersonRole, name: String, detail: String, usedFor: String) =
        DocumentItem(id = "doc-${++docCounter}", owner = owner, name = name, detail = detail, usedFor = usedFor, order = docCounter)

    val documents: List<DocumentItem> = listOf(
        doc(PersonRole.TECH, "Passport (valid 2+ years)", "", "All countries"),
        doc(PersonRole.TEACHER, "Passport (valid 2+ years)", "", "All countries"),
        doc(PersonRole.TECH, "Degree certificates + transcripts", "Attested", "All countries"),
        doc(PersonRole.TEACHER, "Degree certificates + transcripts", "HEC-attested", "All countries"),
        doc(PersonRole.TECH, "Degree recognition: anabin / ZAB", "Germany", "Germany"),
        doc(PersonRole.TECH, "Degree recognition: WES", "Canada ECA", "Canada"),
        doc(PersonRole.TECH, "Skills assessment: ACS", "May count only 4 of 6 years", "Australia"),
        doc(PersonRole.TEACHER, "Degree recognition: ECA (WES)", "Canada", "Canada"),
        doc(PersonRole.TEACHER, "Teacher assessment: AITSL", "Needs IELTS Academic R7 W7 S8 L8", "Australia"),
        doc(PersonRole.TEACHER, "Qualification assessment: NZQA", "If degree not pre-approved", "New Zealand"),
        doc(PersonRole.TECH, "Experience letters (dates, hours, salary, duties)", "Each employer", "Canada, Australia, NZ, CVs"),
        doc(PersonRole.TEACHER, "Experience letters (dates, hours, salary, duties)", "Each school", "Canada, Australia, NZ, CVs"),
        doc(PersonRole.TECH, "Payslips + bank statements", "Proves paid experience", "All permanent routes"),
        doc(PersonRole.TEACHER, "Payslips + bank statements", "Proves paid experience", "All permanent routes"),
        doc(PersonRole.TECH, "IELTS results", "General", "Canada, Australia, NZ"),
        doc(PersonRole.TEACHER, "IELTS results", "Academic (+ General for Canada)", "Canada, Australia, NZ"),
        doc(PersonRole.TECH, "Marriage certificate (registered + legalised)", "Names must match both passports", "All spouse visas"),
        doc(PersonRole.TEACHER, "Marriage certificate (registered + legalised)", "Names must match both passports", "All spouse visas"),
        doc(PersonRole.TECH, "Police certificates", "India, Qatar", "All permanent routes"),
        doc(PersonRole.TEACHER, "Police certificates", "Pakistan (+ Qatar if resident)", "All permanent routes"),
        doc(PersonRole.TECH, "Medical exam", "When asked", "Canada, Australia, NZ"),
        doc(PersonRole.TEACHER, "Medical exam", "When asked", "Canada, Australia, NZ"),
        doc(PersonRole.TECH, "Proof of funds / savings", "Keep savings in your own account for months", "Canada, Opportunity Card"),
        doc(PersonRole.TECH, "Wedding photos + proof of living together", "Every country checks the marriage is genuine", "All spouse visas"),
    )

    private var budgetCounter = 0
    private fun b(owner: PersonRole?, item: String, planned: Double, currency: String = "USD") =
        BudgetItem(id = "budget-${++budgetCounter}", owner = owner, item = item, planned = planned, currency = currency, order = budgetCounter)

    val budget: List<BudgetItem> = listOf(
        b(PersonRole.TEACHER, "IELTS Academic test", 250.0),
        b(PersonRole.TECH, "IELTS General test", 250.0),
        b(PersonRole.TECH, "AZ-204 exam", 165.0),
        b(PersonRole.TECH, "PSM I exam", 200.0),
        b(PersonRole.TECH, "German course + exam (A1-B1)", 900.0, "EUR"),
        b(PersonRole.TECH, "ZAB Statement of Comparability", 200.0, "EUR"),
        b(PersonRole.TECH, "WES ECA", 250.0, "CAD"),
        b(PersonRole.TECH, "ACS skills assessment", 550.0, "AUD"),
        b(PersonRole.TEACHER, "HEC attestation", 50.0),
        b(PersonRole.TEACHER, "WES ECA", 250.0, "CAD"),
        b(PersonRole.TEACHER, "AITSL assessment", 1154.0, "AUD"),
        b(null, "NAATI CCL (both)", 1600.0, "AUD"),
        b(null, "Translations + attestation", 500.0),
        b(null, "Marriage certificate legalisation", 300.0),
        b(null, "Police certificates + medicals", 400.0),
        b(null, "Visa fees (couple)", 1500.0, "EUR"),
        b(null, "Flights", 1500.0),
        b(null, "2-3 months of living costs on arrival", 7000.0, "EUR"),
    )
}
