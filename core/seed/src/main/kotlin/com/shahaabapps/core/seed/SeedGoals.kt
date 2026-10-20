package com.shahaabapps.core.seed

import com.shahaabapps.core.model.CertTier
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal

/**
 * Study goals from PLAN.md sections 5 and 5a, with skills listed in the order
 * the examining body uses:
 *  - IELTS: Listening, Reading, Writing, Speaking (ielts.org test format).
 *  - Goethe-Zertifikat A1/A2/B1: Hören, Lesen, Schreiben, Sprechen, preceded by
 *    the vocabulary and grammar foundation the modules draw on.
 *  - TEF Canada: compréhension orale, compréhension écrite, expression écrite,
 *    expression orale.
 *  - Microsoft exams: the "skills measured" areas with their published weights
 *    (AZ-204, AZ-400, DP-300, AI-102 study guides on Microsoft Learn).
 *  - CKAD: the CNCF curriculum domains and weights.
 *  - AWS DVA-C02 and Oracle 1Z0-830: their published exam domains / topics.
 * Hour budgets follow those weights. German and French totals come from the guide.
 * Component ids are kept stable across versions so logged hours stay attached.
 */
object SeedGoals {

    /** Goethe module order with the vocabulary/grammar foundation first. */
    private fun german(vocab: Double, grammar: Double, hoeren: Double, lesen: Double, schreiben: Double, sprechen: Double) = listOf(
        GoalComponent("vocabulary", "Vocabulary (Wortschatz)", vocab),
        GoalComponent("grammar", "Grammar (Grammatik)", grammar),
        GoalComponent("listening", "Listening (Hören)", hoeren),
        GoalComponent("reading", "Reading (Lesen)", lesen),
        GoalComponent("writing", "Writing (Schreiben)", schreiben),
        GoalComponent("speaking", "Speaking (Sprechen)", sprechen),
    )

    private fun ielts(l: Double, r: Double, w: Double, s: Double, tl: String, tr: String, tw: String, ts: String) = listOf(
        GoalComponent("listening", "Listening", l, tl),
        GoalComponent("reading", "Reading", r, tr),
        GoalComponent("writing", "Writing", w, tw),
        GoalComponent("speaking", "Speaking", s, ts),
    )

    val all: List<StudyGoal> = listOf(
        // ---------------- TECH: German ladder (Goethe-Zertifikat) ----------------
        StudyGoal(
            id = "de-a1", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_LEVEL, title = "German A1",
            subtitle = "Goethe-Zertifikat A1. Guide: about 80 to 120 hours",
            why = "No German is needed for the Blue Card, but B1 cuts permanent residence to 21 months and is required for citizenship.",
            priority = 10, targetDateIso = "2026-12-31", ladder = "German", ladderStep = 1,
            components = german(20.0, 20.0, 15.0, 15.0, 10.0, 20.0),
        ),
        StudyGoal(
            id = "de-a2", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_LEVEL, title = "German A2",
            subtitle = "Goethe-Zertifikat A2. Cumulative about 200 to 220 hours",
            why = "Roadmap: A2 by March 2027 while applying to Germany and Ireland.",
            priority = 11, targetDateIso = "2027-03-31", ladder = "German", ladderStep = 2, active = false,
            components = german(25.0, 25.0, 18.0, 17.0, 15.0, 20.0),
        ),
        StudyGoal(
            id = "de-b1", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_LEVEL, title = "German B1",
            subtitle = "Goethe-Zertifikat B1. Guide: 350 to 500 hours in total",
            why = "B1 course in Jul to Sep 2027; permanent residence at 21 months instead of 27.",
            priority = 12, targetDateIso = "2027-09-30", ladder = "German", ladderStep = 3, active = false,
            components = german(40.0, 40.0, 30.0, 30.0, 25.0, 35.0),
        ),
        // ---------------- TECH: tests ----------------
        StudyGoal(
            id = "ielts-general-tech", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_TEST, title = "IELTS General Training",
            subtitle = "Target CLB 9 to 10: L 8, R 7, W 7, S 7",
            why = "Serves Canada and Australia points; Europe needs no test.",
            priority = 30, targetDateIso = "2027-06-30", active = false,
            components = ielts(30.0, 40.0, 50.0, 30.0, "8.0", "7.0", "7.0", "7.0"),
        ),
        // ---------------- TECH: certifications ----------------
        StudyGoal(
            id = "az-204", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AZ-204 Azure Developer Associate",
            subtitle = "Tier 1. Fee about USD 165",
            why = "The guide's pick; Azure dominates .NET shops in Germany, Ireland and the Netherlands.",
            priority = 20, targetDateIso = "2027-03-31", tier = CertTier.TIER_1, feeUsd = 165,
            components = listOf(
                GoalComponent("compute", "Develop Azure compute solutions (25-30%)", 15.0),
                GoalComponent("storage", "Develop for Azure storage (15-20%)", 10.0),
                GoalComponent("security", "Implement Azure security (15-20%)", 10.0),
                GoalComponent("monitoring", "Monitor, troubleshoot and optimize Azure solutions (5-10%)", 5.0),
                GoalComponent("connect", "Connect to and consume Azure and third-party services (20-25%)", 12.0),
                GoalComponent("practice", "Practice exams", 8.0),
            ),
        ),
        StudyGoal(
            id = "ai-200", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AI-200 Azure AI Cloud Developer Associate",
            subtitle = "Tier 1. AZ-204's successor. Fee about USD 165",
            why = "AZ-204 retired on 31 July 2026; this is the developer exam that replaced it, with AI data services and Python.",
            priority = 19, targetDateIso = "2027-04-30", tier = CertTier.TIER_1, feeUsd = 165,
            components = listOf(
                GoalComponent("containers", "Develop containerized solutions on Azure (20-25%)", 12.0),
                GoalComponent("aidata", "Develop AI solutions by using Azure data management services (30-35%)", 16.0),
                GoalComponent("connect", "Connect to and consume Azure services (20-25%)", 10.0),
                GoalComponent("secure", "Secure, monitor and troubleshoot Azure solutions (20-25%)", 10.0),
                GoalComponent("practice", "Practice exams", 8.0),
            ),
        ),
        StudyGoal(
            id = "psm-1", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "PSM I Professional Scrum Master",
            subtitle = "Tier 1. Fee about USD 200, no renewal",
            why = "Scrum appears in most German and Irish developer ads; two weeks of evenings.",
            priority = 25, targetDateIso = "2026-12-20", tier = CertTier.TIER_1, feeUsd = 200, active = false,
            components = listOf(
                GoalComponent("guide", "The Scrum Guide: theory and values", 4.0),
                GoalComponent("roles", "Scrum team, events and artifacts", 6.0),
                GoalComponent("assessments", "Scrum.org open assessments", 5.0),
            ),
        ),
        StudyGoal(
            id = "ckad", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "CKAD Kubernetes Application Developer",
            subtitle = "Tier 2. Fee about USD 445, hands-on exam",
            why = "German .NET and Java ads routinely list Docker and Kubernetes; pairs with the GitHub project.",
            priority = 40, tier = CertTier.TIER_2, feeUsd = 445, active = false,
            components = listOf(
                GoalComponent("design", "Application design and build (20%)", 10.0),
                GoalComponent("deploy", "Application deployment (20%)", 10.0),
                GoalComponent("observe", "Application observability and maintenance (15%)", 8.0),
                GoalComponent("env", "Application environment, configuration and security (25%)", 12.0),
                GoalComponent("network", "Services and networking (20%)", 10.0),
            ),
        ),
        StudyGoal(
            id = "az-400", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AZ-400 DevOps Engineer Expert",
            subtitle = "Tier 2. Fee about USD 165, needs AZ-204 first",
            why = "CI/CD and infrastructure as code are the most common add-on after Azure itself.",
            priority = 41, tier = CertTier.TIER_2, feeUsd = 165, active = false,
            components = listOf(
                GoalComponent("process", "Design and implement processes and communications (10-15%)", 10.0),
                GoalComponent("source", "Design and implement a source control strategy (10-15%)", 10.0),
                GoalComponent("pipelines", "Design and implement build and release pipelines (50-55%)", 42.0),
                GoalComponent("security", "Develop a security and compliance plan (10-15%)", 10.0),
                GoalComponent("instrument", "Implement an instrumentation strategy (5-10%)", 8.0),
            ),
        ),
        StudyGoal(
            id = "java-se-21", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "Oracle Java SE 21 Developer (1Z0-830)",
            subtitle = "Tier 2. Fee about USD 245",
            why = "Java is the largest backend language in Germany; proves the Java/Kotlin claim on your CV.",
            priority = 42, tier = CertTier.TIER_2, feeUsd = 245, active = false,
            components = listOf(
                GoalComponent("basics", "Data types, text, dates and program flow", 10.0),
                GoalComponent("oop", "Object-oriented concepts", 10.0),
                GoalComponent("exceptions", "Handling exceptions", 5.0),
                GoalComponent("collections", "Arrays, collections and generics", 12.0),
                GoalComponent("streams", "Streams and lambda expressions", 12.0),
                GoalComponent("modules", "Packaging, deployment and the module system", 6.0),
                GoalComponent("concurrency", "Concurrent code execution", 10.0),
                GoalComponent("io", "Java I/O API and localization", 5.0),
                GoalComponent("practice", "Practice exams", 10.0),
            ),
        ),
        StudyGoal(
            id = "aws-dva", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AWS Certified Developer Associate (DVA-C02)",
            subtitle = "Tier 3. Fee about USD 150. Pick if Ireland leads",
            why = "Ireland: AWS Dublin and many fintechs are AWS-first; the Netherlands is mixed cloud.",
            priority = 50, tier = CertTier.TIER_3, feeUsd = 150, active = false,
            components = listOf(
                GoalComponent("dev", "Development with AWS services (32%)", 22.0),
                GoalComponent("security", "Security (26%)", 18.0),
                GoalComponent("deploy", "Deployment (24%)", 17.0),
                GoalComponent("troubleshoot", "Troubleshooting and optimization (18%)", 13.0),
            ),
        ),
        StudyGoal(
            id = "dp-300", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "DP-300 Azure Database Administrator",
            subtitle = "Tier 3. Fee about USD 165. Fits T-SQL and data migration",
            why = "Banks and insurers in Germany and Ireland value it; alternative is PL-300 Power BI to convert SSRS experience.",
            priority = 51, tier = CertTier.TIER_3, feeUsd = 165, active = false,
            components = listOf(
                GoalComponent("plan", "Plan and implement data platform resources (20-25%)", 11.0),
                GoalComponent("secure", "Implement a secure environment (15-20%)", 9.0),
                GoalComponent("monitor", "Monitor, configure and optimize database resources (20-25%)", 11.0),
                GoalComponent("automate", "Configure and manage automation of tasks (15-20%)", 8.0),
                GoalComponent("hadr", "Plan and configure high availability and disaster recovery (20-25%)", 11.0),
            ),
        ),
        StudyGoal(
            id = "ai-102", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AI-102 Azure AI Engineer Associate",
            subtitle = "Tier 3. Fee about USD 165. Only with an AI feature in the GitHub project",
            why = "2026 ads increasingly ask .NET developers for LLM and AI integration.",
            priority = 52, tier = CertTier.TIER_3, feeUsd = 165, active = false,
            components = listOf(
                GoalComponent("plan", "Plan and manage an Azure AI solution (20-25%)", 13.0),
                GoalComponent("genai", "Implement generative AI solutions (15-20%)", 11.0),
                GoalComponent("agentic", "Implement an agentic solution (5-10%)", 5.0),
                GoalComponent("vision", "Implement computer vision solutions (10-15%)", 8.0),
                GoalComponent("nlp", "Implement natural language processing solutions (15-20%)", 11.0),
                GoalComponent("mining", "Implement knowledge mining and information extraction (15-20%)", 12.0),
            ),
        ),
        StudyGoal(
            id = "terraform", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "HashiCorp Terraform Associate (003)",
            subtitle = "Tier 3. Fee about USD 70",
            why = "Small and common in Dutch and German platform-team ads; cheap add-on after AZ-400 or CKAD.",
            priority = 53, tier = CertTier.TIER_3, feeUsd = 70, active = false,
            components = listOf(
                GoalComponent("concepts", "Infrastructure as code concepts and Terraform's purpose", 3.0),
                GoalComponent("basics", "Terraform basics", 5.0),
                GoalComponent("workflow", "The core workflow and working outside it", 6.0),
                GoalComponent("modules", "Interact with Terraform modules", 4.0),
                GoalComponent("state", "Implement and maintain state", 5.0),
                GoalComponent("config", "Read, generate and modify configuration", 5.0),
                GoalComponent("hcp", "HCP Terraform capabilities", 2.0),
            ),
        ),
        StudyGoal(
            id = "naati-tech", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_TEST, title = "NAATI CCL (Hindi)",
            subtitle = "+5 Australian points", why = "Community language credential for Australia: two recorded dialogues, English to Hindi and back.",
            priority = 60, active = false,
            components = listOf(GoalComponent("dialogue", "Dialogue practice (both directions)", 30.0)),
        ),
        // ---------------- TEACHER ----------------
        StudyGoal(
            id = "ielts-academic", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "IELTS Academic",
            subtitle = "Target L 8, R 7, W 7, S 8 in one sitting (AITSL)",
            why = "Serves AITSL (Australia), NZ teacher registration and Australian points. Class covers 10 h/week.",
            priority = 10, targetDateIso = "2027-03-31",
            components = ielts(30.0, 40.0, 50.0, 40.0, "8.0", "7.0", "7.0", "8.0"),
        ),
        StudyGoal(
            id = "ielts-general-teacher", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "IELTS General Training (if Canada)",
            subtitle = "Top-up only if Canada becomes the target",
            why = "Canada Express Entry accepts General Training only.",
            priority = 30, active = false,
            components = ielts(25.0, 25.0, 30.0, 20.0, "7.0", "7.0", "7.0", "7.0"),
        ),
        StudyGoal(
            id = "french", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_LEVEL, title = "French to NCLC 7 (TEF Canada)",
            subtitle = "Guide: 500 to 650 hours over 12 to 18 months, only if Canada",
            why = "NCLC 7 in all four skills gives up to +50 Express Entry points; stopping at B1 gives almost nothing.",
            priority = 20, active = false,
            components = listOf(
                GoalComponent("listening", "Listening (compréhension orale)", 150.0, "NCLC 7"),
                GoalComponent("reading", "Reading (compréhension écrite)", 125.0, "NCLC 7"),
                GoalComponent("writing", "Writing (expression écrite)", 150.0, "NCLC 7"),
                GoalComponent("speaking", "Speaking (expression orale)", 150.0, "NCLC 7"),
            ),
        ),
        StudyGoal(
            id = "de-a1-teacher", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_LEVEL, title = "German A1 (optional)",
            subtitle = "Goethe-Zertifikat A1. Guide: 80 to 120 hours, helps daily life after arrival",
            why = "Not needed for the spouse visa.",
            priority = 40, active = false,
            components = german(20.0, 20.0, 15.0, 15.0, 10.0, 20.0),
        ),
        StudyGoal(
            id = "naati-teacher", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "NAATI CCL (Urdu)",
            subtitle = "+5 Australian points", why = "Community language credential for Australia: two recorded dialogues, English to Urdu and back.",
            priority = 60, active = false,
            components = listOf(GoalComponent("dialogue", "Dialogue practice (both directions)", 30.0)),
        ),
    )
}
