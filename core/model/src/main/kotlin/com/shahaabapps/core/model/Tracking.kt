package com.shahaabapps.core.model

/** A count-based habit, e.g. job applications per week or teaching days. */
data class Habit(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val name: String = "",
    val unit: String = "",
    val weeklyTarget: Int? = null,
    val totalTarget: Int? = null,
    val startDateIso: String? = null,
    val active: Boolean = true,
)

data class HabitLog(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val habitId: String = "",
    val dateIso: String = "",
    val value: Int = 0,
    val note: String = "",
)

enum class MilestoneStatus { TODO, IN_PROGRESS, DONE, BLOCKED }

/** A roadmap item from the guide's quarter-by-quarter plan (page 12). */
data class Milestone(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val quarter: String = "",
    val quarterOrder: Int = 0,
    val title: String = "",
    val status: MilestoneStatus = MilestoneStatus.TODO,
    val dueDateIso: String? = null,
    val countryCode: String? = null,
    val isCheckpoint: Boolean = false,
)

data class CountryTodo(
    val id: String = "",
    val text: String = "",
    val done: Boolean = false,
)

/** One of the six country tracks (guide pages 6 to 11). */
data class CountryTrack(
    val code: String = "",
    val name: String = "",
    val flag: String = "",
    val tagline: String = "",
    val lead: String = "",
    val chanceTech: String = "",
    val chanceTeacher: String = "",
    val citizenship: String = "",
    val permanentResidence: String = "",
    val keyFacts: List<Pair<String, String>> = emptyList(),
    val todos: List<CountryTodo> = emptyList(),
    val risks: List<String> = emptyList(),
    val order: Int = 0,
    val notes: String = "",
)

/** An official test result against its target. Bands are component -> score. */
data class TestRecord(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val name: String = "",
    val purpose: String = "",
    val targetBands: Map<String, String> = emptyMap(),
    val actualBands: Map<String, String> = emptyMap(),
    val takenOnIso: String? = null,
    val expiresOnIso: String? = null,
    val order: Int = 0,
)

enum class DocStatus { NOT_STARTED, IN_PROGRESS, DONE }

data class DocumentItem(
    val id: String = "",
    val owner: PersonRole = PersonRole.TECH,
    val name: String = "",
    val detail: String = "",
    val usedFor: String = "",
    val status: DocStatus = DocStatus.NOT_STARTED,
    val expiresOnIso: String? = null,
    val note: String = "",
    val order: Int = 0,
)

data class BudgetItem(
    val id: String = "",
    val owner: PersonRole? = null,
    val item: String = "",
    val planned: Double = 0.0,
    val spent: Double = 0.0,
    val currency: String = "USD",
    val paidOnIso: String? = null,
    val order: Int = 0,
)

/** A fixed date worth counting down to. */
data class Deadline(
    val id: String = "",
    val title: String = "",
    val dateIso: String = "",
    val owner: PersonRole? = null,
    val detail: String = "",
)
