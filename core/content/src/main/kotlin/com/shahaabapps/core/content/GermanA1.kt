package com.shahaabapps.core.content

/**
 * Goethe-Zertifikat A1 (Start Deutsch 1), written for this app. Twelve themed
 * modules, each with Words, Grammar, Dialogue and Practice topics and a test
 * shaped like the real papers. Modules 1 to 4 ship first.
 */
object GermanA1 {
    internal const val GOETHE = "https://www.goethe.de/"
    internal val commonRefs = listOf(
        B.ref("Goethe-Zertifikat A1: exam overview", GOETHE + "en/spr/kup/prf/prf/gzsd1.html"),
        B.ref("A1 practice materials and model papers", GOETHE + "en/spr/kup/prf/prf/gzsd1/ueb.html"),
    )

    val course: Course
        get() = Course(
            id = "de-a1",
            title = "German A1",
            subtitle = "Twelve modules to Goethe-Zertifikat A1, with words that come back until they stick.",
            goalId = "de-a1",
            extraGoalIds = listOf("de-a1-teacher"),
            speechLanguage = "de-DE",
            modules = listOf(
                GermanA1M1.module, GermanA1M2.module, GermanA1M3.module, GermanA1M4.module,
                locked(5, "Home and city", "Rooms and furniture, es gibt, first prepositions with the dative.", "reading"),
                locked(6, "Work and school", "Jobs, können, müssen and möchten, the verb bracket.", "grammar"),
                locked(7, "Travel and directions", "Transport, asking the way, separable verbs.", "listening"),
                locked(8, "Health and body", "Body parts, at the doctor, the imperative.", "speaking"),
                locked(9, "Free time and weather", "Hobbies, gern and lieber, a first look at the past.", "vocabulary"),
                locked(10, "Talking about the past", "Perfekt with haben and sein, time expressions.", "grammar"),
                locked(11, "Writing to people", "Forms, short emails and invitations, exactly as the exam asks.", "writing"),
                locked(12, "Exam preparation", "How each paper works, strategies, a full mock exam.", "listening"),
            ),
        )

    private fun locked(n: Int, title: String, summary: String, component: String) =
        Module(id = "de-m$n", number = n, title = title, summary = summary, examWeight = "A1", componentId = component, topics = emptyList(), quiz = null)
}
