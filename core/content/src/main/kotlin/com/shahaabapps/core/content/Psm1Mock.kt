package com.shahaabapps.core.content

/** The PSM I mock assessment (Psm1). To be written. */
internal object Psm1Mock {
    val quiz: Quiz = Quiz(id = "psm-mock", title = "PSM I mock assessment", passPercent = 85, timeLimitMinutes = 60, instantFeedback = false, drawCount = 80, questions = emptyList())
}
