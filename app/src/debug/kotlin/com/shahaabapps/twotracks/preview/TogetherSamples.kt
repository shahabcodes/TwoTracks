package com.shahaabapps.twotracks.preview

import com.shahaabapps.core.model.DocStatus
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.seed.Seed
import com.shahaabapps.twotracks.feature.budget.BudgetUiState
import com.shahaabapps.twotracks.feature.countries.CountriesUiState
import com.shahaabapps.twotracks.feature.countries.CountryUiState
import com.shahaabapps.twotracks.feature.countries.Reader
import com.shahaabapps.twotracks.feature.documents.DocumentsUiState
import com.shahaabapps.twotracks.feature.partner.Band
import com.shahaabapps.twotracks.feature.partner.Paper
import com.shahaabapps.twotracks.feature.partner.PartnerSessionRow
import com.shahaabapps.twotracks.feature.partner.PartnerUiState
import com.shahaabapps.twotracks.feature.tests.TestsUiState
import com.shahaabapps.twotracks.feature.us.GoalBar
import com.shahaabapps.twotracks.feature.us.TogetherPerson
import com.shahaabapps.twotracks.feature.us.UsUiState
import com.shahaabapps.twotracks.feature.us.WeekDay
import com.shahaabapps.twotracks.feature.us.budgetLine
import com.shahaabapps.twotracks.feature.us.documentsLine
import com.shahaabapps.twotracks.feature.us.moveRow
import com.shahaabapps.twotracks.feature.us.testsLine

/** The prototype's Together, from the real seed and the plan samples. */
internal object TogetherSamples {
    private val reader = Reader(PersonRole.TECH, "Usaid", "Sanya")
    private val levels = PlanSamples.levels()
    private val documents = Seed.documents.mapIndexed { i, d -> d.copy(id = "d$i", status = when (i) { 0, 1, 4 -> DocStatus.DONE; 2, 3 -> DocStatus.IN_PROGRESS; else -> DocStatus.NOT_STARTED }) }
    private val budget = Seed.budget.mapIndexed { i, b -> b.copy(id = "b$i", spent = when (i) { 2 -> 150.0; 4 -> b.planned; else -> 0.0 }) }

    fun us() = UsUiState(
        me = TogetherPerson(
            PersonRole.TECH, "Usaid", true, "DOHA · 09:12", levels.me?.levels?.first(),
            listOf(GoalBar("AZ-204", "12/60 h", 0.2f), GoalBar("German A1", "18/100 h", 0.18f)),
            ladder = levels.me?.levels.orEmpty(),
        ),
        partner = TogetherPerson(
            PersonRole.TEACHER, "Sanya", true, "KARACHI · 11:12", levels.partner?.levels?.first(),
            listOf(GoalBar("IELTS Academic", "34/120 h", 0.28f), GoalBar("German A1", "15/150 h", 0.1f)),
            missed = "1 missed day · lands about 2 days later",
            ladder = levels.partner?.levels.orEmpty(),
        ),
        streak = 6,
        week = listOf("T", "W", "T", "F", "S", "S", "M").mapIndexed { i, l -> WeekDay(l, true, i < 6, i == 6) },
        nextDate = "MARRIAGE · 112 DAYS", nextDateRole = null,
        countries = Seed.countries.sortedBy { it.order }.map { moveRow(it, "Usaid", "Sanya") },
        tests = testsLine(Seed.tests), documents = documentsLine(documents), budget = budgetLine(Seed.budget),
    )

    fun partner() = PartnerUiState(
        loaded = true, name = "Sanya", isTech = false, roleLine = "THE TEACHER · KARACHI 11:12", city = "Karachi",
        sessions = listOf(
            PartnerSessionRow("16:00", "IELTS Academic · Listening class", "90 min · counts by itself · 15:00 for you", false, null),
            PartnerSessionRow("23:30", "German A1 · Grammar", "30 min · 22:30 for you", false, null),
        ),
        level = levels.partner?.levels?.first(), weekHours = "6.9 h", weekLine = "every day so far",
        goals = listOf(GoalBar("IELTS Academic", "34/120 h", 0.28f), GoalBar("German A1", "15/150 h", 0.1f)),
        status = "On track for IELTS Academic by 31 Dec 2026.", statusGood = true,
        paper = Paper("4 Oct", listOf(Band("Listening", "7.5", "8.0", false), Band("Reading", "7.0", "7.0", true), Band("Writing", "6.5", "7.0", false), Band("Speaking", "7.0", "8.0", false)), "Listening, Writing and Speaking are the gap."),
    )

    fun countries() = CountriesUiState(Seed.countries.sortedBy { it.order }, reader)

    fun country() = CountryUiState(Seed.countries.first().let { c -> c.copy(todos = c.todos.mapIndexed { i, t -> t.copy(done = i == 0) }) }, reader)

    fun tests() = TestsUiState(Seed.tests.mapIndexed { i, t -> t.copy(id = "t$i") }.sortedWith(compareBy({ it.order }, { it.owner })), PersonRole.TECH, Samples.today)

    fun documents() = DocumentsUiState(PersonRole.TECH, "Sanya", all = documents)

    fun budget() = BudgetUiState(budget, PersonRole.TECH, "Sanya")
}
