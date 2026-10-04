package com.shahaabapps.core.seed

import com.shahaabapps.core.model.CertTier
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.StudyGoal

/**
 * Study goals from PLAN.md sections 5 and 5a. German and French hour budgets come
 * from the guide; the rest are editable defaults.
 */
object SeedGoals {

    private fun lang(vararg hours: Double, exam: Double? = null): List<GoalComponent> {
        val names = listOf("Vocabulary", "Grammar", "Listening", "Speaking")
        val base = names.zip(hours.toList()).map { (n, h) -> GoalComponent(n.lowercase(), n, h) }
        return if (exam != null) base + GoalComponent("exam", "Exam practice", exam) else base
    }

    private fun ielts(l: Double, r: Double, w: Double, s: Double, target: String): List<GoalComponent> = listOf(
        GoalComponent("listening", "Listening", l, target),
        GoalComponent("reading", "Reading", r, target),
        GoalComponent("writing", "Writing", w, target),
        GoalComponent("speaking", "Speaking", s, target),
    )

    val all: List<StudyGoal> = listOf(
        // ---------------- TECH: German ladder ----------------
        StudyGoal(
            id = "de-a1", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_LEVEL, title = "German A1",
            subtitle = "Guide: A1 is roughly 80 to 120 hours",
            why = "No German is needed for the Blue Card, but B1 cuts permanent residence to 21 months and is required for citizenship.",
            priority = 10, targetDateIso = "2026-12-31", ladder = "German", ladderStep = 1,
            components = lang(30.0, 30.0, 20.0, 20.0),
        ),
        StudyGoal(
            id = "de-a2", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_LEVEL, title = "German A2",
            subtitle = "Cumulative about 200 to 220 hours",
            why = "Roadmap: A2 by March 2027 while applying to Germany and Ireland.",
            priority = 11, targetDateIso = "2027-03-31", ladder = "German", ladderStep = 2, active = false,
            components = lang(35.0, 35.0, 25.0, 25.0),
        ),
        StudyGoal(
            id = "de-b1", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_LEVEL, title = "German B1",
            subtitle = "Guide: B1 is roughly 350 to 500 hours in total",
            why = "B1 course in Jul to Sep 2027; permanent residence at 21 months instead of 27.",
            priority = 12, targetDateIso = "2027-09-30", ladder = "German", ladderStep = 3, active = false,
            components = lang(45.0, 45.0, 40.0, 40.0, exam = 30.0),
        ),
        // ---------------- TECH: tests ----------------
        StudyGoal(
            id = "ielts-general-tech", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_TEST, title = "IELTS General",
            subtitle = "Target CLB 9 to 10: L 8, R 7, W 7, S 7",
            why = "Serves Canada and Australia points; Europe needs no test.",
            priority = 30, targetDateIso = "2027-06-30", active = false,
            components = listOf(
                GoalComponent("listening", "Listening", 30.0, "8.0"),
                GoalComponent("reading", "Reading", 40.0, "7.0"),
                GoalComponent("writing", "Writing", 50.0, "7.0"),
                GoalComponent("speaking", "Speaking", 30.0, "7.0"),
            ),
        ),
        // ---------------- TECH: certifications ----------------
        StudyGoal(
            id = "az-204", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AZ-204 Azure Developer Associate",
            subtitle = "Tier 1. Fee about USD 165",
            why = "The guide's pick; Azure dominates .NET shops in Germany, Ireland and the Netherlands.",
            priority = 20, targetDateIso = "2027-03-31", tier = CertTier.TIER_1, feeUsd = 165,
            components = listOf(
                GoalComponent("compute", "Compute solutions", 12.0),
                GoalComponent("storage", "Storage", 8.0),
                GoalComponent("security", "Security", 8.0),
                GoalComponent("monitoring", "Monitoring and troubleshooting", 8.0),
                GoalComponent("connect", "Connect and consume services", 12.0),
                GoalComponent("practice", "Practice exams", 12.0),
            ),
        ),
        StudyGoal(
            id = "psm-1", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "PSM I Professional Scrum Master",
            subtitle = "Tier 1. Fee about USD 200, no renewal",
            why = "Scrum appears in most German and Irish developer ads; two weeks of evenings.",
            priority = 25, targetDateIso = "2026-12-20", tier = CertTier.TIER_1, feeUsd = 200, active = false,
            components = listOf(
                GoalComponent("guide", "Scrum Guide", 5.0),
                GoalComponent("roles", "Roles and events", 5.0),
                GoalComponent("assessments", "Open assessments", 5.0),
            ),
        ),
        StudyGoal(
            id = "ckad", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "CKAD Kubernetes Application Developer",
            subtitle = "Tier 2. Fee about USD 445, hands-on exam",
            why = "German .NET and Java ads routinely list Docker and Kubernetes; pairs with the GitHub project.",
            priority = 40, tier = CertTier.TIER_2, feeUsd = 445, active = false,
            components = listOf(
                GoalComponent("design", "Application design and build", 10.0),
                GoalComponent("deploy", "Deployment", 8.0),
                GoalComponent("observe", "Observability", 6.0),
                GoalComponent("env", "Environment, config and security", 10.0),
                GoalComponent("network", "Services and networking", 8.0),
                GoalComponent("kubectl", "kubectl practice", 8.0),
            ),
        ),
        StudyGoal(
            id = "az-400", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AZ-400 DevOps Engineer Expert",
            subtitle = "Tier 2. Fee about USD 165, needs AZ-204 first",
            why = "CI/CD and infrastructure as code are the most common add-on after Azure itself.",
            priority = 41, tier = CertTier.TIER_2, feeUsd = 165, active = false,
            components = listOf(
                GoalComponent("process", "Process and communication", 12.0),
                GoalComponent("source", "Source control", 12.0),
                GoalComponent("pipelines", "Pipelines", 24.0),
                GoalComponent("security", "Security and compliance", 16.0),
                GoalComponent("instrument", "Instrumentation", 16.0),
            ),
        ),
        StudyGoal(
            id = "java-se-21", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "Oracle Java SE 21 Developer (1Z0-830)",
            subtitle = "Tier 2. Fee about USD 245",
            why = "Java is the largest backend language in Germany; proves the Java/Kotlin claim on your CV.",
            priority = 42, tier = CertTier.TIER_2, feeUsd = 245, active = false,
            components = listOf(
                GoalComponent("basics", "Language basics", 8.0),
                GoalComponent("oop", "OOP", 10.0),
                GoalComponent("collections", "Collections and generics", 12.0),
                GoalComponent("streams", "Streams and lambdas", 14.0),
                GoalComponent("concurrency", "Concurrency", 12.0),
                GoalComponent("modules", "Modules and I/O", 10.0),
                GoalComponent("practice", "Practice exams", 14.0),
            ),
        ),
        StudyGoal(
            id = "aws-dva", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AWS Certified Developer Associate (DVA-C02)",
            subtitle = "Tier 3. Fee about USD 150. Pick if Ireland leads",
            why = "Ireland: AWS Dublin and many fintechs are AWS-first; the Netherlands is mixed cloud.",
            priority = 50, tier = CertTier.TIER_3, feeUsd = 150, active = false,
            components = listOf(
                GoalComponent("dev", "Development with AWS services", 25.0),
                GoalComponent("security", "Security", 15.0),
                GoalComponent("deploy", "Deployment", 15.0),
                GoalComponent("troubleshoot", "Troubleshooting and optimisation", 15.0),
            ),
        ),
        StudyGoal(
            id = "dp-300", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "DP-300 Azure Database Administrator",
            subtitle = "Tier 3. Fee about USD 165. Fits T-SQL and data migration",
            why = "Banks and insurers in Germany and Ireland value it; alternative is PL-300 Power BI to convert SSRS experience.",
            priority = 51, tier = CertTier.TIER_3, feeUsd = 165, active = false,
            components = listOf(
                GoalComponent("plan", "Plan and implement", 10.0),
                GoalComponent("monitor", "Monitor and optimise", 12.0),
                GoalComponent("automate", "Automate", 8.0),
                GoalComponent("hadr", "HA/DR", 10.0),
                GoalComponent("tsql", "T-SQL administration", 10.0),
            ),
        ),
        StudyGoal(
            id = "ai-102", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "AI-102 Azure AI Engineer Associate",
            subtitle = "Tier 3. Fee about USD 165. Only with an AI feature in the GitHub project",
            why = "2026 ads increasingly ask .NET developers for LLM and AI integration.",
            priority = 52, tier = CertTier.TIER_3, feeUsd = 165, active = false,
            components = listOf(
                GoalComponent("plan", "Plan and manage", 10.0),
                GoalComponent("genai", "Generative AI (Azure OpenAI)", 16.0),
                GoalComponent("vision", "Computer vision", 10.0),
                GoalComponent("nlp", "NLP", 12.0),
                GoalComponent("mining", "Knowledge mining", 12.0),
            ),
        ),
        StudyGoal(
            id = "terraform", owner = PersonRole.TECH, kind = GoalKind.CERTIFICATION, title = "HashiCorp Terraform Associate (003)",
            subtitle = "Tier 3. Fee about USD 70",
            why = "Small and common in Dutch and German platform-team ads; cheap add-on after AZ-400 or CKAD.",
            priority = 53, tier = CertTier.TIER_3, feeUsd = 70, active = false,
            components = listOf(
                GoalComponent("concepts", "IaC concepts", 4.0),
                GoalComponent("basics", "Terraform basics", 6.0),
                GoalComponent("cli", "CLI and workflow", 6.0),
                GoalComponent("state", "State", 5.0),
                GoalComponent("modules", "Modules", 5.0),
                GoalComponent("hcp", "HCP Terraform", 4.0),
            ),
        ),
        StudyGoal(
            id = "naati-tech", owner = PersonRole.TECH, kind = GoalKind.LANGUAGE_TEST, title = "NAATI CCL (Hindi)",
            subtitle = "+5 Australian points", why = "Community language credential for Australia.",
            priority = 60, active = false,
            components = listOf(GoalComponent("dialogue", "Dialogue practice", 30.0)),
        ),
        // ---------------- TEACHER ----------------
        StudyGoal(
            id = "ielts-academic", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "IELTS Academic",
            subtitle = "Target R 7, W 7, S 8, L 8 in one sitting",
            why = "Serves AITSL (Australia), NZ teacher registration and Australian points. Class covers 10 h/week.",
            priority = 10, targetDateIso = "2027-03-31",
            components = listOf(
                GoalComponent("listening", "Listening", 30.0, "8.0"),
                GoalComponent("reading", "Reading", 40.0, "7.0"),
                GoalComponent("writing", "Writing", 50.0, "7.0"),
                GoalComponent("speaking", "Speaking", 40.0, "8.0"),
            ),
        ),
        StudyGoal(
            id = "ielts-general-teacher", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "IELTS General (if Canada)",
            subtitle = "Top-up only if Canada becomes the target",
            why = "Canada Express Entry accepts General Training only.",
            priority = 30, active = false,
            components = ielts(25.0, 25.0, 30.0, 20.0, "7.0"),
        ),
        StudyGoal(
            id = "french", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_LEVEL, title = "French to NCLC 7",
            subtitle = "Guide: 500 to 650 hours over 12 to 18 months, only if Canada",
            why = "NCLC 7 in all four skills gives up to +50 Express Entry points; stopping at B1 gives almost nothing.",
            priority = 20, active = false,
            components = listOf(
                GoalComponent("listening", "Listening", 150.0, "NCLC 7"),
                GoalComponent("reading", "Reading", 125.0, "NCLC 7"),
                GoalComponent("writing", "Writing", 150.0, "NCLC 7"),
                GoalComponent("speaking", "Speaking", 150.0, "NCLC 7"),
            ),
        ),
        StudyGoal(
            id = "de-a1-teacher", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_LEVEL, title = "German A1 (optional)",
            subtitle = "Guide: 80 to 120 hours, helps daily life after arrival",
            why = "Not needed for the spouse visa.",
            priority = 40, active = false,
            components = lang(30.0, 30.0, 20.0, 20.0),
        ),
        StudyGoal(
            id = "naati-teacher", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "NAATI CCL (Urdu)",
            subtitle = "+5 Australian points", why = "Community language credential for Australia.",
            priority = 60, active = false,
            components = listOf(GoalComponent("dialogue", "Dialogue practice", 30.0)),
        ),
    )
}
