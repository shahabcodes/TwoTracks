package com.shahaabapps.core.model

/** What a weekly time block is used for. */
enum class BlockKind {
    /** Paid work or school: unavailable. */
    WORK,
    /** A scheduled class (e.g. IELTS) that automatically logs study time. */
    CLASS,
    /** A self-study slot the planner may fill with a session. */
    STUDY,
}

/**
 * One recurring block in a person's weekly template.
 *
 * @param dayOfWeek ISO day: 1 = Monday ... 7 = Sunday.
 * @param startMinutes minutes from local midnight.
 * @param endMinutes minutes from local midnight (exclusive).
 * @param goalId for CLASS blocks: the study goal the class time counts toward.
 */
data class AvailabilityBlock(
    val id: String = "",
    val dayOfWeek: Int = 1,
    val startMinutes: Int = 0,
    val endMinutes: Int = 0,
    val kind: BlockKind = BlockKind.STUDY,
    val label: String = "",
    val goalId: String? = null,
) {
    val durationMinutes: Int get() = (endMinutes - startMinutes).coerceAtLeast(0)
}

/** A person's full weekly template. */
data class Availability(
    val owner: PersonRole = PersonRole.TECH,
    val blocks: List<AvailabilityBlock> = emptyList(),
) {
    /** Minutes per week available for study (STUDY + CLASS blocks). */
    val weeklyStudyMinutes: Int
        get() = blocks.filter { it.kind != BlockKind.WORK }.sumOf { it.durationMinutes }
}
