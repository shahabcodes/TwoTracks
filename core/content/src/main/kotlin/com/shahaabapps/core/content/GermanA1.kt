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

    /**
     * Built once. This was a getter, so every read rebuilt the whole course —
     * every module, topic, block and question — and the screens read it
     * several times per state computation. The content never changes at run
     * time, so one copy is both correct and all that is needed.
     */
    val course: Course by lazy {
        Course(
            id = "de-a1",
            title = "German A1",
            subtitle = "Twelve modules to Goethe-Zertifikat A1, with words that come back until they stick.",
            goalId = "de-a1",
            extraGoalIds = listOf("de-a1-teacher"),
            speechLanguage = "de-DE",
            mockExam = GermanA1Mock.quiz,
            modules = listOf(
                GermanA1M1.module, GermanA1M2.module, GermanA1M3.module, GermanA1M4.module,
                GermanA1M5.module,
                GermanA1M6.module,
                GermanA1M7.module,
                GermanA1M8.module,
                GermanA1M9.module,
                GermanA1M10.module,
                GermanA1M11.module,
                GermanA1M12.module,
            ),
        )
    }
}
