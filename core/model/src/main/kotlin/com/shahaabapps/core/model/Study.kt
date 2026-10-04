package com.shahaabapps.core.model

enum class GoalKind {
    LANGUAGE_TEST,
    LANGUAGE_LEVEL,
    CERTIFICATION,
    CUSTOM,
}

/** Certification tiers from the plan (section 5a). Null for non-certs. */
enum class CertTier { TIER_1, TIER_2, TIER_3 }

/**
 * One sub-skill of a goal, e.g. IELTS "Reading" or AZ-204 "Compute".
 *
 * @param hoursRequired the editable hour budget for this component.
 * @param targetScore optional target, e.g. "7.0" for an IELTS band.
 */
data class GoalComponent(
    val id: String = "",
    val name: String = "",
    val hoursRequired: Double = 0.0,
    val targetScore: String? = null,
)

/**
 * A study goal with per-component hour budgets.
 *
 * @param priority lower = scheduled first by the planner.
 * @param targetDateIso the roadmap date this should be finished by (yyyy-MM-dd).
 * @param active inactive goals are listed but never scheduled.
 * @param defaultHours true while the hour budgets are still the seeded defaults.
 */
data class StudyGoal(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val kind: GoalKind = GoalKind.CUSTOM,
    val title: String = "",
    val subtitle: String = "",
    val why: String = "",
    val priority: Int = 100,
    val targetDateIso: String? = null,
    val active: Boolean = true,
    val components: List<GoalComponent> = emptyList(),
    val tier: CertTier? = null,
    val feeUsd: Int? = null,
    val defaultHours: Boolean = true,
    /** Optional group label, e.g. "German" to show A1/A2/B1 as one ladder. */
    val ladder: String? = null,
    val ladderStep: Int = 0,
    /** Set when the user marks the goal achieved (exam passed, level reached). */
    val passed: Boolean = false,
    val passedAtIso: String? = null,
) {
    val totalHoursRequired: Double get() = components.sumOf { it.hoursRequired }
}

/** Minutes studied for one component on one day (several entries per day allowed). */
data class StudyLog(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val dateIso: String = "",
    val goalId: String = "",
    val componentId: String = "",
    val minutes: Int = 0,
    val note: String = "",
    /** True when logged automatically from a CLASS block. */
    val fromClass: Boolean = false,
    val createdAtMillis: Long = 0L,
)

/** A practice-test result: component id -> score string (e.g. "6.5"). */
data class MockScore(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val goalId: String = "",
    val dateIso: String = "",
    val scores: Map<String, String> = emptyMap(),
    val note: String = "",
)

enum class SessionStatus { PLANNED, DONE, SKIPPED }

/** A concrete study session on a date, generated from a STUDY/CLASS block. */
data class PlannedSession(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val dateIso: String = "",
    val blockId: String = "",
    val startMinutes: Int = 0,
    val endMinutes: Int = 0,
    val goalId: String = "",
    val componentId: String = "",
    val status: SessionStatus = SessionStatus.PLANNED,
    val isClass: Boolean = false,
) {
    val durationMinutes: Int get() = endMinutes - startMinutes
}
