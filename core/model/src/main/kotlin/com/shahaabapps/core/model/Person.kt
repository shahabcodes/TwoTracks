package com.shahaabapps.core.model

/**
 * Exactly two people share a couple space. Everything seeded from the guide is
 * keyed by [PersonRole] rather than by Firebase uid so that the content exists
 * before the second person has signed up.
 *
 * TECH = the software developer (leads Germany / Ireland / Netherlands).
 * TEACHER = the teacher (leads Canada / New Zealand).
 */
enum class PersonRole {
    TECH,
    TEACHER;

    val other: PersonRole get() = if (this == TECH) TEACHER else TECH
}

/** A member of the couple space. */
data class Profile(
    val uid: String = "",
    val name: String = "",
    val role: PersonRole = PersonRole.TECH,
    /** IANA zone id, e.g. "Asia/Qatar" or "Asia/Karachi". */
    val timezone: String = "UTC",
    val dateOfBirthIso: String? = null,
    val photoUrl: String? = null,
    /** Local time (minutes from midnight) for the daily reminder; null = off. */
    val reminderMinutes: Int? = null,
)

/** The shared space that holds all data for the two of them. */
data class Couple(
    val id: String = "",
    val members: List<String> = emptyList(),
    val inviteCode: String = "",
    val createdAtMillis: Long = 0L,
    val seededVersion: Int = 0,
)
