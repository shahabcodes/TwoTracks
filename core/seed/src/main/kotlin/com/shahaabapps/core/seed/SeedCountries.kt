package com.shahaabapps.core.seed

import com.shahaabapps.core.model.CountryTodo
import com.shahaabapps.core.model.CountryTrack

/** The six country pages from the guide (pages 6 to 11), rules checked 28 Sep 2026. */
object SeedCountries {

    private fun todos(code: String, vararg items: String) =
        items.mapIndexed { i, t -> CountryTodo(id = "$code-todo-${i + 1}", text = t) }

    val all: List<CountryTrack> = listOf(
        CountryTrack(
            code = "DE", name = "Germany", flag = "🇩🇪", order = 1,
            tagline = "Largest tech job market of the six and the best fit for your .NET / Angular / SQL profile",
            lead = "You", chanceTech = "45-60% (65-75% with B1 + referrals)", chanceTeacher = "Under 10%",
            citizenship = "5 years + B1 + test", permanentResidence = "21 months with B1 German (else 27)",
            keyFacts = listOf(
                "Visa" to "EU Blue Card: job offer matching your degree, contract of at least 6 months.",
                "Salary (2026)" to "EUR 50,700 gross/year standard, or EUR 45,934.20 for shortage jobs (IT qualifies).",
                "Degree" to "Check university and degree on anabin (H+, equivalent). If unclear, get a ZAB Statement of Comparability; it takes months.",
                "Language" to "None for the visa. B1 cuts permanent residence to 21 months and is required for citizenship.",
                "Backup" to "Opportunity Card (Chancenkarte): 1 year to job-hunt inside Germany; German A1 or English B2 plus EUR 1,091/month in funds.",
                "Spouse" to "Applies at the same time as you, can work any job, no German needed first.",
            ),
            todos = todos("DE",
                "Months 1-3: anabin check, German A1, AZ-204, German-style CV (2 pages, photo is common)",
                "Months 2-6: GitHub project (.NET API + Angular + Docker + Azure); German A2",
                "Months 4-10: 10-15 targeted applications a week on LinkedIn, StepStone, XING, Make it in Germany, Arbeitnow; ask for referrals",
                "On an offer: check base salary clears the threshold; apply for both visas together at the German mission in Qatar",
            ),
            risks = listOf(
                "Proposals to tighten citizenship rules (none passed as of mid-2026)",
                "Embassy waits, especially for a Pakistani spouse: book early",
                "Employers often prefer EU-based candidates: say you can relocate in 6-8 weeks",
            ),
        ),
        CountryTrack(
            code = "IE", name = "Ireland", flag = "🇮🇪", order = 2,
            tagline = "English-speaking, quick to permanent status, strong .NET / SQL demand in finance and insurance",
            lead = "You", chanceTech = "30-45%", chanceTeacher = "Under 10%",
            citizenship = "5 years (8-year bill proposed Sep 2026)", permanentResidence = "Stamp 4 after 21 months",
            keyFacts = listOf(
                "Visa" to "Critical Skills Employment Permit: job on the Critical Skills list (software developers are), 2+ years, hired directly (not via agency).",
                "Salary (Mar 2026)" to "At least EUR 40,904/year for listed jobs; EUR 68,173+ for almost any job.",
                "Language" to "No test for the permit; the employer judges your English.",
                "Processing" to "Fast: recent complete applications decided in about 1-2 weeks.",
                "Spouse" to "Stamp 1G: can take any job immediately; counts toward her own citizenship.",
            ),
            todos = todos("IE",
                "Use the same CV as for Germany but without a photo",
                "Target Dublin, Cork, Galway: fintech, payments, banks, insurers, consultancies",
                "Write in every application: 'Eligible for Critical Skills permit; can relocate in 4-6 weeks'",
                "Start applying from month 3, alongside Germany",
            ),
            risks = listOf("Small market and strong local competition", "Very expensive, scarce housing in Dublin", "Proposed 8-year citizenship bill"),
        ),
        CountryTrack(
            code = "NL", name = "Netherlands", flag = "🇳🇱", order = 3,
            tagline = "Many English-speaking tech jobs, and being under 30 makes you cheaper to sponsor",
            lead = "You", chanceTech = "25-40%", chanceTeacher = "Under 10%",
            citizenship = "5 years + B1; give up other nationality", permanentResidence = "5 years + A2 Dutch",
            keyFacts = listOf(
                "Visa" to "Highly Skilled Migrant permit (employer must be a recognised sponsor) or EU Blue Card.",
                "Salary" to "Age-based. About EUR 5,688/month gross if 30+, EUR 4,171/month if under 30 (2025 levels). You're under 30 until Feb 2029.",
                "Language" to "None for the permit. A2 Dutch for permanent residence; B1 for citizenship.",
                "Spouse" to "Applies with you through the employer; free to work.",
            ),
            todos = todos("NL",
                "Filter LinkedIn jobs by 'visa sponsorship'",
                "Check the employer is on the IND public register of recognised sponsors",
                "Apply before you turn 30 if possible",
            ),
            risks = listOf("Severe housing shortage", "Expat tax benefit (30% ruling) is being reduced", "Possible future tightening of citizenship rules"),
        ),
        CountryTrack(
            code = "CA", name = "Canada", flag = "🇨🇦", order = 4,
            tagline = "Fastest citizenship, no job offer needed, but invitations are competitive: her profile leads",
            lead = "Her", chanceTech = "10-20% without French", chanceTeacher = "15-25% without French; 45-60% with French NCLC 7",
            citizenship = "3 years as permanent resident", permanentResidence = "On arrival",
            keyFacts = listOf(
                "System" to "Express Entry points (CRS). Most 2026 invitations went through category draws. Job offers give no points since March 2025.",
                "Entry rules" to "1 year continuous full-time paid skilled work, CLB 7 English, degree assessment (ECA, e.g. WES), proof of funds (about CAD 19,000-20,000 for a couple).",
                "Teachers" to "Education category: NOC 41221 (elementary) and 41220 (secondary). She needs 1 year of full-time teaching within the last 3 years.",
                "French bonus" to "NCLC 7 in all four skills: +50 points, plus up to 22 second-language points, and eligibility for French draws.",
                "2026 cut-offs" to "Canadian Experience Class 507-523. French 382. Senior managers 389. Healthcare 475.",
                "Spouse" to "Same application; you both become permanent residents.",
            ),
            todos = todos("CA",
                "Get her degrees HEC-attested and assessed (ECA); get yours assessed by WES",
                "She completes 1 full year of documented, paid, full-time teaching",
                "Both take IELTS General (aim for CLB 9-10)",
                "If French: start after IELTS, target TEF/TCF Canada NCLC 7",
                "Create her profile as soon as she has 1 year's experience",
            ),
            risks = listOf("IRCC consulted in 2026 on major Express Entry changes", "Draw types and cut-offs change often", "Confirm all points with the official CRS calculator"),
        ),
        CountryTrack(
            code = "AU", name = "Australia", flag = "🇦🇺", order = 5,
            tagline = "Permanent residence on arrival and no job offer needed: IELTS scores decide who leads",
            lead = "Higher scorer", chanceTech = "25-40% (needs 8 in every band)", chanceTeacher = "25-40% (needs AITSL + high IELTS)",
            citizenship = "About 4 years", permanentResidence = "On arrival",
            keyFacts = listOf(
                "Visas" to "189 independent. 190 state-nominated (+5 points). 491 regional (+15 points, PR after 3 years).",
                "Points needed" to "Minimum 65, but ICT and professional jobs have needed about 85-90+; education about 80-90.",
                "Assessments" to "You: ACS (may count only 4 of your 6 years). Her: AITSL (IELTS Academic R7 W7 S8 L8 in one sitting; fee about AUD 1,154).",
                "Your points (189)" to "About 75-85, +5 with state nomination.",
                "Her points (189)" to "About 70-80, +5 (190) or +15 (491).",
                "Spouse" to "Same application; you both get permanent residence.",
            ),
            todos = todos("AU",
                "Both take IELTS (she: Academic); 8 in every band is worth 20 points",
                "Take NAATI CCL in Hindi/Urdu for +5",
                "You apply for ACS; she applies to AITSL once her IELTS meets its bar",
                "Lodge an EOI for the higher scorer; also apply for state nomination",
            ),
            risks = listOf("High cut-offs; offshore applicants at a disadvantage", "Invitation rounds now roughly quarterly and may change"),
        ),
        CountryTrack(
            code = "NZ", name = "New Zealand", flag = "🇳🇿", order = 6,
            tagline = "Teachers are on the Straight to Residence list: her strongest single route",
            lead = "Her", chanceTech = "15-25% (needs NZ tech job offer)", chanceTeacher = "30-45% if she lands an NZ teaching job",
            citizenship = "About 5 years as a resident", permanentResidence = "With residence visa",
            keyFacts = listOf(
                "Her route" to "Green List Tier 1 Straight to Residence: full-time offer from an accredited NZ school at the median wage (NZ$35/hour from March 2026), NZ teacher registration, NZQA assessment if needed. She can apply from abroad.",
                "Your route" to "Skilled Migrant Category: 6 points. A bachelor's gives about 3; an NZ job paying 1.5x the median wage gives 3 more.",
                "Partner" to "Included in her residence application. If on a work visa first, a partner work visa needs NZ$28+/hour.",
                "Demand" to "NZ secondary schools reported serious shortages in 2026; primary shortages also projected.",
            ),
            todos = todos("NZ",
                "She checks Teaching Council NZ registration and whether her degrees need NZQA assessment",
                "She takes IELTS Academic (target 7+ each; confirm exact rule)",
                "She applies via the Education Gazette (NZ teaching jobs board)",
                "You add NZ tech jobs to your search",
            ),
            risks = listOf("Registration and assessment can take months", "Some schools prefer teachers who can start immediately"),
        ),
    )
}
