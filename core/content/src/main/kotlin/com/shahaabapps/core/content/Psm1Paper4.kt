package com.shahaabapps.core.content

/**
 * PSM I mock assessment 4: ninety new original questions on myths and traps: practices people think are Scrum and the Guide never says.
 * Written from The Scrum Guide (November 2020); none is taken from Scrum.org.
 * Timed like the real sitting, 80 drawn for 60 minutes, pass at 85 percent.
 */
internal object Psm1Paper4 {
    val quiz: Quiz = Quiz(
        id = "psm-mock-4", title = "PSM I mock assessment 4", passPercent = 85, timeLimitMinutes = 60, instantFeedback = false, drawCount = 80,
        questions = B.spread(Psm1Paper4A.questions + Psm1Paper4B.questions),
    )
}
