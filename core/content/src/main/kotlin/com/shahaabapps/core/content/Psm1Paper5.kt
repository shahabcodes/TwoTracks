package com.shahaabapps.core.content

/**
 * PSM I mock assessment 5: ninety new original questions on the hardest mix: questions that need two rules of the Guide at once.
 * Written from The Scrum Guide (November 2020); none is taken from Scrum.org.
 * Timed like the real sitting, 80 drawn for 60 minutes, pass at 85 percent.
 */
internal object Psm1Paper5 {
    val quiz: Quiz = Quiz(
        id = "psm-mock-5", title = "PSM I mock assessment 5", passPercent = 85, timeLimitMinutes = 60, instantFeedback = false, drawCount = 80,
        questions = B.spread(Psm1Paper5A.questions + Psm1Paper5B.questions),
    )
}
