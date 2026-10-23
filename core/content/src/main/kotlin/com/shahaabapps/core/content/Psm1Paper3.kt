package com.shahaabapps.core.content

/**
 * PSM I mock assessment 3: ninety new original questions on the Scrum Guide's exact wording, true or false and multiple-answer questions.
 * Written from The Scrum Guide (November 2020); none is taken from Scrum.org.
 * Timed like the real sitting, 80 drawn for 60 minutes, pass at 85 percent.
 */
internal object Psm1Paper3 {
    val quiz: Quiz = Quiz(
        id = "psm-mock-3", title = "PSM I mock assessment 3", passPercent = 85, timeLimitMinutes = 60, instantFeedback = false, drawCount = 80,
        questions = B.spread(Psm1Paper3A.questions + Psm1Paper3B.questions),
    )
}
