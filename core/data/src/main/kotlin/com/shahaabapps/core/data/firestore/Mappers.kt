package com.shahaabapps.core.data.firestore

import com.google.firebase.firestore.DocumentSnapshot
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
import com.shahaabapps.core.model.GoalComponent
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.LevelItemKind
import com.shahaabapps.core.model.Milestone
import com.shahaabapps.core.model.MilestoneStatus
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.model.TestRecord

/** Manual two-way mapping between models and Firestore documents. */
object Mappers {

    // ---------------- Couple / Profile ----------------
    fun Couple.toMap() = mapOf(
        "members" to members, "inviteCode" to inviteCode, "createdAtMillis" to createdAtMillis, "seededVersion" to seededVersion,
    )

    fun DocumentSnapshot.toCouple() = Couple(
        id = id, members = stringList("members"), inviteCode = str("inviteCode"),
        createdAtMillis = lng("createdAtMillis"), seededVersion = int("seededVersion"),
    )

    fun Profile.toMap() = mapOf(
        "name" to name, "role" to role.name, "timezone" to timezone, "dateOfBirthIso" to dateOfBirthIso,
        "photoUrl" to photoUrl, "reminderMinutes" to reminderMinutes,
    )

    fun DocumentSnapshot.toProfile() = Profile(
        uid = id, name = str("name"), role = enum("role", PersonRole.TECH), timezone = str("timezone").ifBlank { "UTC" },
        dateOfBirthIso = strOrNull("dateOfBirthIso"), photoUrl = strOrNull("photoUrl"), reminderMinutes = intOrNull("reminderMinutes"),
    )

    // ---------------- Availability ----------------
    fun Availability.toMap() = mapOf(
        "owner" to owner.name,
        "blocks" to blocks.map {
            mapOf(
                "id" to it.id, "dayOfWeek" to it.dayOfWeek, "startMinutes" to it.startMinutes, "endMinutes" to it.endMinutes,
                "kind" to it.kind.name, "label" to it.label, "goalId" to it.goalId,
            )
        },
    )

    fun DocumentSnapshot.toAvailability() = Availability(
        owner = enum("owner", PersonRole.TECH),
        blocks = mapList("blocks").map { m ->
            AvailabilityBlock(
                id = m.s("id"), dayOfWeek = m.i("dayOfWeek"), startMinutes = m.i("startMinutes"), endMinutes = m.i("endMinutes"),
                kind = BlockKind.entries.firstOrNull { it.name == m.s("kind") } ?: BlockKind.STUDY,
                label = m.s("label"), goalId = m.sOrNull("goalId"),
            )
        },
    )

    // ---------------- Study ----------------
    fun StudyGoal.toMap() = mapOf(
        "owner" to owner.name, "kind" to kind.name, "title" to title, "subtitle" to subtitle, "why" to why,
        "priority" to priority, "targetDateIso" to targetDateIso, "active" to active,
        "components" to components.map { mapOf("id" to it.id, "name" to it.name, "hoursRequired" to it.hoursRequired, "targetScore" to it.targetScore) },
        "tier" to tier?.name, "feeUsd" to feeUsd, "defaultHours" to defaultHours, "ladder" to ladder, "ladderStep" to ladderStep,
        "passed" to passed, "passedAtIso" to passedAtIso,
    )

    fun DocumentSnapshot.toGoal() = StudyGoal(
        id = id, owner = enum("owner", PersonRole.TECH), kind = enum("kind", GoalKind.CUSTOM), title = str("title"),
        subtitle = str("subtitle"), why = str("why"), priority = int("priority", 100), targetDateIso = strOrNull("targetDateIso"),
        active = bool("active", true),
        components = mapList("components").map { GoalComponent(it.s("id"), it.s("name"), it.d("hoursRequired"), it.sOrNull("targetScore")) },
        tier = enumOrNull<CertTier>("tier"), feeUsd = intOrNull("feeUsd"), defaultHours = bool("defaultHours", true),
        ladder = strOrNull("ladder"), ladderStep = int("ladderStep"),
        passed = bool("passed"), passedAtIso = strOrNull("passedAtIso"),
    )

    fun StudyLog.toMap() = mapOf(
        "owner" to owner.name, "dateIso" to dateIso, "goalId" to goalId, "componentId" to componentId, "minutes" to minutes,
        "note" to note, "fromClass" to fromClass, "createdAtMillis" to createdAtMillis,
    )

    fun DocumentSnapshot.toStudyLog() = StudyLog(
        id = id, owner = enum("owner", PersonRole.TECH), dateIso = str("dateIso"), goalId = str("goalId"), componentId = str("componentId"),
        minutes = int("minutes"), note = str("note"), fromClass = bool("fromClass"), createdAtMillis = lng("createdAtMillis"),
    )

    fun MockScore.toMap() = mapOf("owner" to owner.name, "goalId" to goalId, "dateIso" to dateIso, "scores" to scores, "note" to note)

    fun DocumentSnapshot.toMockScore() = MockScore(
        id = id, owner = enum("owner", PersonRole.TECH), goalId = str("goalId"), dateIso = str("dateIso"), scores = stringMap("scores"), note = str("note"),
    )

    fun PlannedSession.toMap() = mapOf(
        "owner" to owner.name, "dateIso" to dateIso, "blockId" to blockId, "startMinutes" to startMinutes, "endMinutes" to endMinutes,
        "goalId" to goalId, "componentId" to componentId, "status" to status.name, "isClass" to isClass,
    )

    fun DocumentSnapshot.toSession() = PlannedSession(
        id = id, owner = enum("owner", PersonRole.TECH), dateIso = str("dateIso"), blockId = str("blockId"), startMinutes = int("startMinutes"),
        endMinutes = int("endMinutes"), goalId = str("goalId"), componentId = str("componentId"), status = enum("status", SessionStatus.PLANNED), isClass = bool("isClass"),
    )

    // ---------------- Levels ----------------
    fun Level.toMap() = mapOf(
        "owner" to owner.name, "number" to number, "name" to name, "summary" to summary,
        "startDateIso" to startDateIso, "dueDateIso" to dueDateIso, "startedEarly" to startedEarly,
        "completedAtIso" to completedAtIso, "branch" to branch,
        "items" to items.map {
            mapOf(
                "id" to it.id, "kind" to it.kind.name, "title" to it.title, "detail" to it.detail, "goalId" to it.goalId,
                "habitId" to it.habitId, "dueDateIso" to it.dueDateIso, "required" to it.required, "done" to it.done, "doneAtIso" to it.doneAtIso,
            )
        },
    )

    fun DocumentSnapshot.toLevel() = Level(
        id = id, owner = enum("owner", PersonRole.TECH), number = int("number", 1), name = str("name"), summary = str("summary"),
        startDateIso = str("startDateIso"), dueDateIso = str("dueDateIso"), startedEarly = bool("startedEarly"),
        completedAtIso = strOrNull("completedAtIso"), branch = strOrNull("branch"),
        items = mapList("items").map { m ->
            LevelItem(
                id = m.s("id"), kind = LevelItemKind.entries.firstOrNull { it.name == m.s("kind") } ?: LevelItemKind.TASK,
                title = m.s("title"), detail = m.s("detail"), goalId = m.sOrNull("goalId"), habitId = m.sOrNull("habitId"),
                dueDateIso = m.sOrNull("dueDateIso"), required = (m["required"] as? Boolean) ?: true, done = m.b("done"), doneAtIso = m.sOrNull("doneAtIso"),
            )
        },
    )

    // ---------------- Tracking ----------------
    fun Habit.toMap() = mapOf(
        "owner" to owner.name, "name" to name, "unit" to unit, "weeklyTarget" to weeklyTarget, "totalTarget" to totalTarget,
        "startDateIso" to startDateIso, "active" to active,
    )

    fun DocumentSnapshot.toHabit() = Habit(
        id = id, owner = enum("owner", PersonRole.TECH), name = str("name"), unit = str("unit"), weeklyTarget = intOrNull("weeklyTarget"),
        totalTarget = intOrNull("totalTarget"), startDateIso = strOrNull("startDateIso"), active = bool("active", true),
    )

    fun HabitLog.toMap() = mapOf("owner" to owner.name, "habitId" to habitId, "dateIso" to dateIso, "value" to value, "note" to note)

    fun DocumentSnapshot.toHabitLog() = HabitLog(
        id = id, owner = enum("owner", PersonRole.TECH), habitId = str("habitId"), dateIso = str("dateIso"), value = int("value"), note = str("note"),
    )

    fun Milestone.toMap() = mapOf(
        "owner" to owner.name, "quarter" to quarter, "quarterOrder" to quarterOrder, "title" to title, "status" to status.name,
        "dueDateIso" to dueDateIso, "countryCode" to countryCode, "isCheckpoint" to isCheckpoint,
    )

    fun DocumentSnapshot.toMilestone() = Milestone(
        id = id, owner = enum("owner", PersonRole.TECH), quarter = str("quarter"), quarterOrder = int("quarterOrder"), title = str("title"),
        status = enum("status", MilestoneStatus.TODO), dueDateIso = strOrNull("dueDateIso"), countryCode = strOrNull("countryCode"), isCheckpoint = bool("isCheckpoint"),
    )

    fun CountryTrack.toMap() = mapOf(
        "name" to name, "flag" to flag, "tagline" to tagline, "lead" to lead, "chanceTech" to chanceTech, "chanceTeacher" to chanceTeacher,
        "citizenship" to citizenship, "permanentResidence" to permanentResidence,
        "keyFacts" to keyFacts.map { mapOf("label" to it.first, "value" to it.second) },
        "todos" to todos.map { mapOf("id" to it.id, "text" to it.text, "done" to it.done) },
        "risks" to risks, "order" to order, "notes" to notes,
    )

    fun DocumentSnapshot.toCountry() = CountryTrack(
        code = id, name = str("name"), flag = str("flag"), tagline = str("tagline"), lead = str("lead"), chanceTech = str("chanceTech"),
        chanceTeacher = str("chanceTeacher"), citizenship = str("citizenship"), permanentResidence = str("permanentResidence"),
        keyFacts = mapList("keyFacts").map { it.s("label") to it.s("value") },
        todos = mapList("todos").map { CountryTodo(it.s("id"), it.s("text"), it.b("done")) },
        risks = stringList("risks"), order = int("order"), notes = str("notes"),
    )

    fun TestRecord.toMap() = mapOf(
        "owner" to owner.name, "name" to name, "purpose" to purpose, "targetBands" to targetBands, "actualBands" to actualBands,
        "takenOnIso" to takenOnIso, "expiresOnIso" to expiresOnIso, "order" to order,
    )

    fun DocumentSnapshot.toTest() = TestRecord(
        id = id, owner = enum("owner", PersonRole.TECH), name = str("name"), purpose = str("purpose"), targetBands = stringMap("targetBands"),
        actualBands = stringMap("actualBands"), takenOnIso = strOrNull("takenOnIso"), expiresOnIso = strOrNull("expiresOnIso"), order = int("order"),
    )

    fun DocumentItem.toMap() = mapOf(
        "owner" to owner.name, "name" to name, "detail" to detail, "usedFor" to usedFor, "status" to status.name,
        "expiresOnIso" to expiresOnIso, "note" to note, "order" to order,
    )

    fun DocumentSnapshot.toDocument() = DocumentItem(
        id = id, owner = enum("owner", PersonRole.TECH), name = str("name"), detail = str("detail"), usedFor = str("usedFor"),
        status = enum("status", DocStatus.NOT_STARTED), expiresOnIso = strOrNull("expiresOnIso"), note = str("note"), order = int("order"),
    )

    fun BudgetItem.toMap() = mapOf(
        "owner" to owner?.name, "item" to item, "planned" to planned, "spent" to spent, "currency" to currency, "paidOnIso" to paidOnIso, "order" to order,
    )

    fun DocumentSnapshot.toBudget() = BudgetItem(
        id = id, owner = enumOrNull<PersonRole>("owner"), item = str("item"), planned = dbl("planned"), spent = dbl("spent"),
        currency = str("currency").ifBlank { "USD" }, paidOnIso = strOrNull("paidOnIso"), order = int("order"),
    )

    fun Deadline.toMap() = mapOf("title" to title, "dateIso" to dateIso, "owner" to owner?.name, "detail" to detail)

    fun DocumentSnapshot.toDeadline() = Deadline(
        id = id, title = str("title"), dateIso = str("dateIso"), owner = enumOrNull<PersonRole>("owner"), detail = str("detail"),
    )
}
