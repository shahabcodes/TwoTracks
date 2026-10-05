package com.shahaabapps.core.seed

/**
 * Entry point for everything seeded into a new couple space. Bump [VERSION]
 * when the seed content changes; the app re-seeds spaces that are behind.
 */
object Seed {
    const val VERSION = 3

    val goals get() = SeedGoals.all
    val levels get() = SeedLevels.all
    val habits get() = SeedSchedule.habits
    val deadlines get() = SeedSchedule.deadlines
    val countries get() = SeedCountries.all
    val tests get() = SeedRecords.tests
    val documents get() = SeedRecords.documents
    val budget get() = SeedRecords.budget
}
