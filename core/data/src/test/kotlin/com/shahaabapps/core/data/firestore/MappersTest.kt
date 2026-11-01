package com.shahaabapps.core.data.firestore

import com.shahaabapps.core.data.firestore.Mappers.toAvailability
import com.shahaabapps.core.data.firestore.Mappers.toBudget
import com.shahaabapps.core.data.firestore.Mappers.toCountry
import com.shahaabapps.core.data.firestore.Mappers.toCouple
import com.shahaabapps.core.data.firestore.Mappers.toDeadline
import com.shahaabapps.core.data.firestore.Mappers.toDocument
import com.shahaabapps.core.data.firestore.Mappers.toFlashcard
import com.shahaabapps.core.data.firestore.Mappers.toGoal
import com.shahaabapps.core.data.firestore.Mappers.toHabit
import com.shahaabapps.core.data.firestore.Mappers.toHabitLog
import com.shahaabapps.core.data.firestore.Mappers.toLevel
import com.shahaabapps.core.data.firestore.Mappers.toMap
import com.shahaabapps.core.data.firestore.Mappers.toMilestone
import com.shahaabapps.core.data.firestore.Mappers.toModuleFile
import com.shahaabapps.core.data.firestore.Mappers.toMockScore
import com.shahaabapps.core.data.firestore.Mappers.toProfile
import com.shahaabapps.core.data.firestore.Mappers.toQuizAttempt
import com.shahaabapps.core.data.firestore.Mappers.toSession
import com.shahaabapps.core.data.firestore.Mappers.toStudyLog
import com.shahaabapps.core.data.firestore.Mappers.toTest
import com.shahaabapps.core.data.firestore.Mappers.toTopicProgress
import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.CertTier
import com.shahaabapps.core.model.CountryTodo
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.Couple
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.Milestone
import com.shahaabapps.core.model.MilestoneStatus
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.model.TestRecord
import com.shahaabapps.core.model.TopicProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Everything this app shows is folded from stored records, and every stored
 * record passes through one of these nineteen pairs of functions. They had no
 * tests, because the reader took a `DocumentSnapshot` and that needs a device.
 *
 * A mistyped key does not crash. It reads back as the default — zero minutes,
 * an empty list, the first enum constant — and the screens go on deriving from
 * it, which is the kind of wrong that looks like real data. So there are two
 * rules here. Every model survives a round trip through the shape Firestore
 * would store it in, and every key a writer writes is read by its reader: drop
 * one and the value must change.
 *
 * The second rule is what actually catches a typo, because a key written as
 * "startMinutes" and read as "startMinute" round-trips perfectly as long as
 * you only look at the fields you remembered to assert.
 */
class MappersTest {

    /**
     * What the wire does to a value on the way out and back.
     *
     * Firestore has one integer type and one floating type: every Int comes
     * back a Long and every Float a Double, however it was written. A reader
     * that cast rather than coerced would pass a naive round trip and fail on
     * a real device, so the test does the same thing to the map first.
     */
    private fun wire(value: Any?): Any? = when (value) {
        is Int -> value.toLong()
        is Float -> value.toDouble()
        is Map<*, *> -> value.entries.associate { (k, v) -> k.toString() to wire(v) }
        is List<*> -> value.map { wire(it) }
        else -> value
    }

    private fun stored(map: Map<String, Any?>): Map<String, Any?> = map.mapValues { wire(it.value) }

    private fun doc(id: String, map: Map<String, Any?>): Doc = docOf(id, stored(map))

    // ---- one fully populated instance of every model -----------------------
    //
    // Every nullable field is set, every collection has something in it and no
    // field holds its own default, because the second rule below works by
    // dropping a key and watching the value change. A field already equal to
    // its default would not change, and the key would look read when it is not.

    private val couple = Couple(id = "c1", members = listOf("u1", "u2"), inviteCode = "ABCD", createdAtMillis = 1_700_000_000_000L, seededVersion = 4)
    private val profile = Profile(
        uid = "u1", name = "Sanya", role = PersonRole.TEACHER, timezone = "Asia/Karachi",
        dateOfBirthIso = "1998-05-05", photoUrl = "https://example.invalid/p.png", reminderMinutes = 1_140, startDateIso = "2026-01-06",
    )
    private val availability = Availability(
        owner = PersonRole.TEACHER,
        blocks = listOf(
            AvailabilityBlock(id = "b1", dayOfWeek = 4, startMinutes = 480, endMinutes = 600, kind = BlockKind.CLASS, label = "IELTS class", goalId = "g1"),
        ),
    )
    private val goal = StudyGoal(
        id = "g1", owner = PersonRole.TEACHER, kind = GoalKind.LANGUAGE_TEST, title = "IELTS 7", subtitle = "Academic",
        why = "Teacher registration", priority = 7, targetDateIso = "2026-06-01", active = false,
        components = listOf(GoalComponent(id = "listening", name = "Listening", hoursRequired = 40.5, targetScore = "7.0")),
        tier = CertTier.TIER_2, feeUsd = 250, defaultHours = false, ladder = "IELTS", ladderStep = 2,
        passed = true, passedAtIso = "2026-06-10",
    )
    private val studyLog = StudyLog(
        id = "sl1", owner = PersonRole.TEACHER, dateIso = "2026-02-02", goalId = "g1", componentId = "listening",
        minutes = 45, note = "Part 4 only", fromClass = true, createdAtMillis = 1_700_000_000_001L,
    )
    private val mockScore = MockScore(id = "m1", owner = PersonRole.TEACHER, goalId = "g1", dateIso = "2026-02-03", scores = mapOf("listening" to "7.5"), note = "timed")
    private val session = PlannedSession(
        id = "ps1", owner = PersonRole.TEACHER, dateIso = "2026-02-04", blockId = "b1", startMinutes = 480, endMinutes = 600,
        goalId = "g1", componentId = "listening", status = SessionStatus.DONE, isClass = true,
    )
    private val level = Level(
        id = "l1", owner = PersonRole.TEACHER, number = 3, name = "Level 3", summary = "Sit the test",
        startDateIso = "2026-01-01", dueDateIso = "2026-03-31", startedEarly = true, completedAtIso = "2026-03-20", branch = "canada",
        items = listOf(
            LevelItem(
                id = "li1", kind = LevelItemKind.COUNT, title = "Applications", detail = "per week", goalId = "g1",
                habitId = "h1", dueDateIso = "2026-02-28", required = false, done = true, doneAtIso = "2026-02-27",
            ),
        ),
    )
    private val habit = Habit(id = "h1", owner = PersonRole.TEACHER, name = "Applications", unit = "sent", weeklyTarget = 5, totalTarget = 100, startDateIso = "2026-01-01", active = false)
    private val habitLog = HabitLog(id = "hl1", owner = PersonRole.TEACHER, habitId = "h1", dateIso = "2026-02-05", value = 3, note = "two referrals")
    private val milestone = Milestone(
        id = "ms1", owner = PersonRole.TEACHER, quarter = "Q1 2026", quarterOrder = 1, title = "Book the test",
        status = MilestoneStatus.IN_PROGRESS, dueDateIso = "2026-02-15", countryCode = "ca", isCheckpoint = true,
    )
    private val country = CountryTrack(
        code = "ca", name = "Canada", flag = "🇨🇦", tagline = "Express Entry", lead = "TEACHER", chanceTech = "good",
        chanceTeacher = "strong", citizenship = "3 years", permanentResidence = "on arrival",
        keyFacts = listOf("Language" to "English or French"), todos = listOf(CountryTodo(id = "t1", text = "ECA", done = true)),
        risks = listOf("Draw scores move"), order = 2, notes = "Check the NOC",
    )
    private val test = TestRecord(
        id = "t1", owner = PersonRole.TEACHER, name = "IELTS", purpose = "Registration", targetBands = mapOf("overall" to "7.0"),
        actualBands = mapOf("overall" to "7.5"), takenOnIso = "2026-06-10", expiresOnIso = "2028-06-10", order = 1,
    )
    private val document = DocumentItem(
        id = "d1", owner = PersonRole.TEACHER, name = "Passport", detail = "Pakistani", usedFor = "Everything",
        status = DocStatus.IN_PROGRESS, expiresOnIso = "2030-01-01", note = "renew early", order = 3,
    )
    private val budget = BudgetItem(id = "bi1", owner = PersonRole.TEACHER, item = "IELTS fee", planned = 250.0, spent = 245.5, currency = "QAR", paidOnIso = "2026-05-01", order = 4)
    private val deadline = Deadline(id = "dl1", title = "Book IELTS", dateIso = "2026-04-01", owner = PersonRole.TEACHER, detail = "British Council")
    private val flashcard = Flashcard(id = "f1", owner = PersonRole.TEACHER, courseId = "de-a1", wordId = "apfel", step = 3, dueIso = "2026-02-10", lastReviewIso = "2026-02-03", correct = 5, misses = 2)
    private val topicProgress = TopicProgress(id = "tp1", owner = PersonRole.TEACHER, courseId = "de-a1", topicId = "de4-grammar", learnedAtIso = "2026-02-03", minutes = 26)
    private val attempt = QuizAttempt(
        id = "qa1", owner = PersonRole.TEACHER, courseId = "de-a1", moduleId = "de-m4", dateIso = "2026-02-03",
        score = 9, total = 12, passed = true, missed = listOf("de4-q3"), asked = listOf("de4-q1", "de4-q3"), at = 1_700_000_000_002L,
        picked = mapOf("de4-q3" to listOf(0, 2)), seconds = mapOf("de4-q3" to 41), sure = mapOf("de4-q3" to false),
    )

    /** Every model, with the id the document would carry and both halves of its mapping. */
    private val moduleFile = ModuleFile(
        id = "mf1", courseId = "az-204", moduleId = "az-compute", topicId = "az-app-service", name = "exam-guide.pdf",
        mimeType = "application/pdf", sizeBytes = 2_400_000, path = "couples/c1/modules/az-204/az-compute/mf1",
        uploadedBy = "u1", uploadedAtIso = "2026-11-01T09:00:00Z",
    )

    private val all: List<Triple<String, Any, Pair<Map<String, Any?>, (Doc) -> Any>>> = with(Mappers) {
        listOf(
            Triple("c1", couple, couple.toMap() to { d: Doc -> d.toCouple() }),
            Triple("u1", profile, profile.toMap() to { d: Doc -> d.toProfile() }),
            Triple("av1", availability, availability.toMap() to { d: Doc -> d.toAvailability() }),
            Triple("g1", goal, goal.toMap() to { d: Doc -> d.toGoal() }),
            Triple("sl1", studyLog, studyLog.toMap() to { d: Doc -> d.toStudyLog() }),
            Triple("m1", mockScore, mockScore.toMap() to { d: Doc -> d.toMockScore() }),
            Triple("ps1", session, session.toMap() to { d: Doc -> d.toSession() }),
            Triple("l1", level, level.toMap() to { d: Doc -> d.toLevel() }),
            Triple("h1", habit, habit.toMap() to { d: Doc -> d.toHabit() }),
            Triple("hl1", habitLog, habitLog.toMap() to { d: Doc -> d.toHabitLog() }),
            Triple("ms1", milestone, milestone.toMap() to { d: Doc -> d.toMilestone() }),
            Triple("ca", country, country.toMap() to { d: Doc -> d.toCountry() }),
            Triple("t1", test, test.toMap() to { d: Doc -> d.toTest() }),
            Triple("d1", document, document.toMap() to { d: Doc -> d.toDocument() }),
            Triple("bi1", budget, budget.toMap() to { d: Doc -> d.toBudget() }),
            Triple("dl1", deadline, deadline.toMap() to { d: Doc -> d.toDeadline() }),
            Triple("f1", flashcard, flashcard.toMap() to { d: Doc -> d.toFlashcard() }),
            Triple("tp1", topicProgress, topicProgress.toMap() to { d: Doc -> d.toTopicProgress() }),
            Triple("qa1", attempt, attempt.toMap() to { d: Doc -> d.toQuizAttempt() }),
            Triple("mf1", moduleFile, moduleFile.toMap() to { d: Doc -> d.toModuleFile() }),
        )
    }

    @Test
    fun `every model survives a round trip through the shape Firestore stores`() {
        assertEquals("a model was added without a round trip", 20, all.size)
        all.forEach { (id, model, mapping) ->
            val (map, read) = mapping
            assertEquals(model::class.simpleName, model, read(doc(id, map)))
        }
    }

    @Test
    fun `every key a writer writes is read back`() {
        // The real check: drop one key and the value has to change. A key that
        // makes no difference is one the reader never asks for, which is what
        // a typo on either side looks like.
        all.forEach { (id, _, mapping) ->
            val (map, read) = mapping
            val whole = read(doc(id, map))
            map.keys.forEach { key ->
                val without = read(doc(id, map - key))
                assertNotEquals("${whole::class.simpleName}: nothing reads \"$key\"", whole, without)
            }
        }
    }

    @Test
    fun `every key inside a nested list is read back too`() {
        // blocks, items, components, todos and keyFacts are lists of maps, and
        // their keys are not top-level keys, so the rule above never sees them.
        all.forEach { (id, _, mapping) ->
            val (map, read) = mapping
            val whole = read(doc(id, map))
            map.forEach { (field, value) ->
                val rows = value as? List<*> ?: return@forEach
                val first = rows.firstOrNull() as? Map<*, *> ?: return@forEach
                first.keys.forEach { nested ->
                    val trimmed = rows.map { row -> (row as Map<*, *>) - nested }
                    val without = read(doc(id, map + (field to trimmed)))
                    assertNotEquals(
                        "${whole::class.simpleName}: nothing reads \"$field.$nested\"",
                        whole, without,
                    )
                }
            }
        }
    }

    @Test
    fun `an empty document reads as the model's own defaults, not a crash`() {
        // asFlow drops a document whose mapper throws, so a missing field has
        // to be survivable: a space part-way through seeding, or a record
        // written by an older version, should read rather than disappear.
        all.forEach { (id, model, mapping) ->
            val read = mapping.second
            val bare = read(docOf(id, emptyMap()))
            assertEquals(model::class, bare::class)
        }
    }

    @Test
    fun `a field of the wrong type reads as the default rather than throwing`() {
        // Firestore does not promise the type that was written: an older
        // version, a hand edit in the console, or a half-migrated field can
        // all hand back something else.
        val nonsense = mapOf<String, Any?>(
            "minutes" to "not a number", "owner" to 7, "fromClass" to "yes",
            "missed" to "not a list", "picked" to listOf(1, 2), "at" to "soon",
        )
        with(Mappers) {
            val log = docOf("sl1", nonsense).toStudyLog()
            assertEquals(0, log.minutes)
            assertEquals(PersonRole.TECH, log.owner)
            assertEquals(false, log.fromClass)
            val qa = docOf("qa1", nonsense).toQuizAttempt()
            assertTrue(qa.missed.isEmpty())
            assertTrue(qa.picked.isEmpty())
            assertEquals(0L, qa.at)
        }
    }

    @Test
    fun `a whole number written as Int comes back from a Long`() {
        // The one behaviour that makes a naive round trip lie.
        with(Mappers) {
            val asLong = docOf("sl1", mapOf("minutes" to 45L, "createdAtMillis" to 1_700_000_000_001L)).toStudyLog()
            assertEquals(45, asLong.minutes)
            assertEquals(1_700_000_000_001L, asLong.createdAtMillis)
            // A Double where an Int is expected, which is what a console edit gives you.
            assertEquals(45, docOf("sl1", mapOf("minutes" to 45.0)).toStudyLog().minutes)
        }
    }
}
