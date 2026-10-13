package com.shahaabapps.twotracks.tap

import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.budget.BudgetContent
import com.shahaabapps.twotracks.feature.countries.CountriesContent
import com.shahaabapps.twotracks.feature.countries.CountryContent
import com.shahaabapps.twotracks.feature.documents.DocFilter
import com.shahaabapps.twotracks.feature.documents.DocumentsContent
import com.shahaabapps.twotracks.feature.partner.PartnerContent
import com.shahaabapps.twotracks.feature.tests.TestsContent
import com.shahaabapps.twotracks.feature.us.UsContent
import com.shahaabapps.twotracks.preview.TogetherSamples
import com.shahaabapps.twotracks.ui.Routes
import org.junit.Assert.assertTrue
import org.junit.Test

/** Route (Together), your partner, the destinations, tests, documents and budget: what each press reaches. */
class RouteTapTest(look: AppLook) : TapTest(look) {
    @Test
    fun `route opens your partner, the levels, a destination, all destinations and the paperwork`() {
        val state = TogetherSamples.us()
        show { UsContent(state, onNavigate = record1("go"), onOpenLevel = record1("level")) }
        tap(label("Sanya", "Sanya ›"))
        expect("go(${Routes.PARTNER})")
        tap(label("Levels", "All stops ›"))
        expect("go(${Routes.LEVELS})")
        tap("Germany")
        expect("go(${Routes.country(state.countries.first { it.name == "Germany" }.code)})")
        tap(label("The move", "Destinations ›"))
        expect("go(${Routes.COUNTRIES})")
        tap("Tests")
        tap("Documents")
        tap("Budget")
        expect("go(${Routes.TESTS})", "go(${Routes.DOCUMENTS})", "go(${Routes.BUDGET})")
    }

    @Test
    fun `your partner's page opens their level and goes back`() {
        val state = TogetherSamples.partner()
        show { PartnerContent(state, onBack = record("back"), onOpenLevel = record1("level"), onOpenGoal = record1("goal")) }
        tap(label("Level 1 · Build the base", "Stop 01 · Build the base"))
        expect("level(${state.level!!.level.id})")
        tapDescribed(label("Back to Together", "Back to Route"))
        expect("back")
    }

    @Test
    fun `the destinations open one, which ticks a step and goes back`() {
        val list = TogetherSamples.countries()
        show { CountriesContent(list, onBack = record("back"), onOpen = record1("open")) }
        tap("Germany")
        expect("open(${list.countries.first { it.name == "Germany" }.code})")
    }

    @Test
    fun `a country ticks a step and goes back`() {
        val state = TogetherSamples.country()
        val step = state.country!!.todos[1]
        show { CountryContent(state, onBack = record("back"), onToggle = { _, t -> calls += "toggle(${t.id})" }, onSaveNotes = { _, _ -> calls += "notes" }, onClearNotes = { calls += "clear" }) }
        tap(step.text)
        expect("toggle(${step.id})")
        tapDescribed(label("Back to countries", "Back to destinations"))
        expect("back")
    }

    @Test
    fun `a test opens for its result, which saves`() {
        val state = TogetherSamples.tests()
        show { TestsContent(state, onBack = record("back"), onSave = { calls += "save(${it.id})" }) }
        tap(state.tests.first().name)
        assertTrue("the result form opens in the $look look", seen("Taken on (yyyy-mm-dd)"))
        tap("Save")
        expect("save(${state.tests.first().id})")
    }

    @Test
    fun `documents filter to your partner's, move a status on and open one`() {
        val state = TogetherSamples.documents()
        show { DocumentsContent(state, onBack = record("back"), onFilter = record1("filter"), onCycle = { calls += "cycle(${it.id})" }, onSave = { calls += "save(${it.id})" }) }
        tap(label("Sanya's", "Sanya"))
        expect("filter(${DocFilter.THEIRS})")
        val first = state.shown.first()
        tapDescribed("${first.name}: ${label("done", "cleared")}. Tap to change")
        expect("cycle(${first.id})")
        tap(first.name, index = 0)
        assertTrue("the note and expiry open in the $look look", seen("Expires on (yyyy-mm-dd)"))
        tap("Save")
        expect("save(${first.id})")
    }

    @Test
    fun `the budget adds an item and edits one`() {
        val state = TogetherSamples.budget()
        val item = state.items.first()
        show { BudgetContent(state, onBack = record("back"), onSave = { calls += "save(${it.id})" }, onDelete = record1("delete")) }
        if (board) tap("+ Add") else tapDescribed("Add an item")
        assertTrue("the new item form opens in the $look look", seen("New item"))
        tap("Cancel")
        tap(item.item)
        assertTrue("the edit form opens in the $look look", seen("Edit item"))
        tap("Save")
        expect("save(${item.id})")
    }
}
