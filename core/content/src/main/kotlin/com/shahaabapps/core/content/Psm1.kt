package com.shahaabapps.core.content

/**
 * PSM I, Scrum.org's Professional Scrum Master I assessment, written for this
 * app in plain English from The Scrum Guide (November 2020) by Ken Schwaber and
 * Jeff Sutherland, which is offered under the Creative Commons
 * Attribution-ShareAlike 4.0 licence (creativecommons.org/licenses/by-sa/4.0).
 * The assessment is 80 questions in 60 minutes, passed at 85 percent, and
 * everything in it comes from the Scrum Guide; the last module is about the
 * traps in its wording and how to pace the hour.
 */
object Psm1 {
    /** Built once, as the other courses are: the content never changes at run time. */
    val course: Course by lazy {
        Course(
            id = "psm-1",
            title = "PSM I Professional Scrum Master",
            subtitle = "Five modules, one test each, then five mock assessments.",
            goalId = "psm-1",
            mockExam = Psm1Mock.quiz,
            extraMocks = listOf(Psm1Paper2.quiz, Psm1Paper3.quiz, Psm1Paper4.quiz, Psm1Paper5.quiz),
            modules = listOf(
                Module(
                    id = "psm-theory", number = 1, title = "Scrum theory and values",
                    summary = "Why Scrum works: empiricism, lean thinking, and the values that make it hold.",
                    examWeight = "Every question rests on it", componentId = "guide",
                    topics = Psm1Theory.topics, quiz = Psm1Theory.quiz,
                ),
                Module(
                    id = "psm-team", number = 2, title = "The Scrum Team",
                    summary = "One team, no sub-teams or hierarchy, and the three accountabilities inside it.",
                    examWeight = "About a third of the questions", componentId = "roles",
                    topics = Psm1Team.topics, quiz = Psm1Team.quiz,
                ),
                Module(
                    id = "psm-events", number = 3, title = "The events",
                    summary = "The Sprint and the four events inside it: purpose, timebox and who attends.",
                    examWeight = "About a third of the questions", componentId = "roles",
                    topics = Psm1Events.topics, quiz = Psm1Events.quiz,
                ),
                Module(
                    id = "psm-artifacts", number = 4, title = "Artifacts and commitments",
                    summary = "Product Backlog, Sprint Backlog and Increment, each with the commitment that focuses it.",
                    examWeight = "About a quarter of the questions", componentId = "roles",
                    topics = Psm1Artifacts.topics, quiz = Psm1Artifacts.quiz,
                ),
                Module(
                    id = "psm-exam", number = 5, title = "Passing the assessment",
                    summary = "The myths the questions are built to catch, and how to pace 80 questions in an hour.",
                    examWeight = "How you sit it", componentId = "assessments",
                    topics = Psm1Exam.topics, quiz = Psm1Exam.quiz,
                ),
            ),
        )
    }
}
