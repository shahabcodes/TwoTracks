package com.shahaabapps.core.content

/**
 * IELTS, Academic and General Training, aimed at band 7 and above. One course
 * feeds all three seeded IELTS goals: Listening and Speaking are the same in
 * both versions; Reading and Writing Task 1 topics cover both.
 */
object Ielts {
    internal const val ORG = "https://ielts.org/"
    // Six ielts.org urls in this course were 404 by the time anyone tapped
    // them: take-a-test/test-format, its four per-paper children, which each
    // module added to its own topics, and .../free-practice-tests. ielts.org
    // moved its preparation section and the app kept the old paths. Every url
    // in the course was fetched when these were written, and returned 200.
    internal val commonRefs = listOf(
        B.ref("IELTS Academic: the four papers", ORG + "take-a-test/test-types/ielts-academic-test"),
        B.ref("IELTS General Training: the four papers", ORG + "take-a-test/test-types/ielts-general-training-test"),
        B.ref("Sample test questions and practice papers", ORG + "take-a-test/preparation-resources/sample-test-questions"),
    )

    /**
     * Built once. This was a getter, so every read rebuilt the whole course —
     * every module, topic, block and question — and the screens read it
     * several times per state computation. The content never changes at run
     * time, so one copy is both correct and all that is needed.
     */
    val course: Course by lazy {
        Course(
            id = "ielts",
            title = "IELTS band 7+",
            subtitle = "Listening, Reading, Writing and Speaking for Academic and General Training, with the techniques that separate band 6 from band 7.",
            goalId = "ielts-general-tech",
            extraGoalIds = listOf("ielts-academic", "ielts-general-teacher"),
            speechLanguage = "en-GB",
            mockExam = IeltsMock.quiz,
            modules = listOf(
                IeltsM1.module, IeltsM2.module, IeltsM3.module, IeltsM4.module, IeltsM5.module, IeltsM6.module, IeltsM7.module, IeltsM8.module,
            ),
        )
    }
}
