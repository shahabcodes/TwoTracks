package com.shahaabapps.core.content

/**
 * PSM I mock assessment 2: ninety new original questions on workplace scenarios: what a Scrum Team member should do next.
 * Written from The Scrum Guide (November 2020); none is taken from Scrum.org.
 * Timed like the real sitting, 80 drawn for 60 minutes, pass at 85 percent.
 */
internal object Psm1Paper2 {
    val quiz: Quiz = Quiz(
        id = "psm-mock-2", title = "PSM I mock assessment 2", passPercent = 85, timeLimitMinutes = 60, instantFeedback = false, drawCount = 80,
        questions = B.spread(Psm1Paper2A.questions + Psm1Paper2B.questions),
    )
}
