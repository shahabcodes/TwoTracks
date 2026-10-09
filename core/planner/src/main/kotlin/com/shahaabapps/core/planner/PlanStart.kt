package com.shahaabapps.core.planner

/**
 * The day a person's plan starts counting against them.
 *
 * Two dates could answer this and they mean different things. The levels
 * carry a start date, but it is seeded from the guide and rebased to the day
 * the space was made, so it says when the plan was *written*. The person
 * carries one too, and it says when they actually began.
 *
 * Where someone has said when they begin, that is the answer, even when it is
 * later than the plan's own dates — the common case being a space made in
 * advance and opened for real a fortnight later. Where they have not, the
 * earliest level start is the best guess available, which is what the app did
 * before anyone could say otherwise.
 *
 * [MissedDays] treats everything before the answer as a rest day, so this is
 * the single place that decides whether a day can be held against someone.
 */
object PlanStart {

    fun effective(personStartIso: String?, levelStartIsos: List<String>): String? {
        val chosen = personStartIso?.takeIf { it.isNotBlank() }
        if (chosen != null) return chosen
        return levelStartIsos.filter { it.isNotBlank() }.minOrNull()
    }
}
