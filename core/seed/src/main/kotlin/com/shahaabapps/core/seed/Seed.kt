package com.shahaabapps.core.seed

/**
 * Entry point for everything seeded into a new couple space. Bump [VERSION]
 * when the seed content changes so the app can offer a "reset to guide defaults".
 */
object Seed {
    const val VERSION = 1

    val goals get() = SeedGoals.all
    val habits get() = SeedSchedule.habits
    val deadlines get() = SeedSchedule.deadlines
    val milestones get() = SeedRoadmap.all
    val countries get() = SeedCountries.all
    val tests get() = SeedRecords.tests
    val documents get() = SeedRecords.documents
    val budget get() = SeedRecords.budget
}
