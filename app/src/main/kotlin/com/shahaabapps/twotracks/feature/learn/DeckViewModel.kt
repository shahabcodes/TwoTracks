package com.shahaabapps.twotracks.feature.learn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Word
import com.shahaabapps.core.content.WordDeck
import com.shahaabapps.core.data.learning.LearningRepository
import com.shahaabapps.core.model.Flashcard
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.core.planner.Srs
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.session.StudyActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DeckUiState(
    val course: Course? = null,
    val session: List<Word> = emptyList(),
    val index: Int = 0,
    val flipped: Boolean = false,
    val results: Map<String, Boolean> = emptyMap(),
    val finished: Boolean = false,
    val minutes: Int = 0,
    val dueCount: Int = 0,
    val newCount: Int = 0,
    val deckSize: Int = 0,
    val speakerState: Speaker.State = Speaker.State.INIT,
    val nextIntervalDays: Long = 1,
) {
    val current: Word? get() = session.getOrNull(index)
    val known: Int get() = results.count { it.value }
    val missed: Int get() = results.count { !it.value }
    val canSpeak: Boolean get() = speakerState == Speaker.State.READY
}

@HiltViewModel
class DeckViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val learningRepository: LearningRepository,
    private val studyActions: StudyActions,
    private val celebrations: Celebrations,
    val speaker: Speaker,
) : ViewModel() {
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val startedAt = System.currentTimeMillis()
    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState.asStateFlow()
    private var cards: MutableMap<String, Srs.Card> = mutableMapOf()
    private var today: LocalDate = LocalDate.now()

    init {
        viewModelScope.launch { speaker.state.collect { st -> _uiState.update { it.copy(speakerState = st) } } }
        build()
    }

    private fun build(force: Boolean = false) {
        val s = sessionStore.current() ?: return
        val course = Courses.byId(courseId) ?: return
        today = Clock.today(s.me)
        val all = dataStore.flashcards.value
        cards = WordDeck.cardsFor(s.myRole, course, all).associateBy { it.itemId }.toMutableMap()
        val met = WordDeck.metWordIds(s.myRole, course, dataStore.topicProgress.value)
        val wordsById = Courses.words(course).associateBy { it.id }
        var ids = Srs.session(cards.values.toList(), met, today)
        if (force && ids.isEmpty()) ids = (cards.keys.toList() + met).distinct().shuffled().take(10)
        _uiState.value = DeckUiState(
            course = course, session = ids.mapNotNull { wordsById[it] },
            dueCount = Srs.dueCount(cards.values.toList(), today), newCount = ids.count { it !in cards },
            deckSize = cards.size, speakerState = speaker.state.value,
        )
        updateInterval()
    }

    private fun updateInterval() {
        val w = _uiState.value.current ?: return
        val c = cards[w.id] ?: Srs.Card(w.id)
        _uiState.update { it.copy(nextIntervalDays = Srs.LADDER[c.step.coerceIn(0, Srs.LADDER.size - 1)]) }
    }

    fun flip() = _uiState.update { it.copy(flipped = !it.flipped) }

    fun answer(correct: Boolean) {
        val s = _uiState.value
        val w = s.current ?: return
        val base = cards[w.id] ?: Srs.Card(w.id)
        cards[w.id] = Srs.review(base, correct, today)
        val results = s.results + (w.id to correct)
        if (s.index + 1 >= s.session.size) {
            _uiState.update { it.copy(results = results, finished = true) }
            save(results)
        } else {
            _uiState.update { it.copy(results = results, index = it.index + 1, flipped = false) }
            updateInterval()
        }
    }

    fun practiseAnyway() = build(force = true)

    fun again() {
        val s = _uiState.value
        val missed = s.session.filter { s.results[it.id] == false }
        _uiState.update { it.copy(session = missed, index = 0, flipped = false, results = emptyMap(), finished = false, minutes = 0) }
        updateInterval()
    }

    private fun save(results: Map<String, Boolean>) = viewModelScope.launch {
        val s = sessionStore.current() ?: return@launch
        val course = _uiState.value.course ?: return@launch
        val changed = results.keys.mapNotNull { cards[it] }.map { c ->
            Flashcard(owner = s.myRole, courseId = course.id, wordId = c.itemId, step = c.step, dueIso = c.dueIso, lastReviewIso = c.lastReviewIso, correct = c.correct, misses = c.misses)
        }
        val r = learningRepository.saveFlashcards(s.coupleId, changed)
        if (r is AppResult.Failure) return@launch
        val minutes = (((System.currentTimeMillis() - startedAt) / 60_000L).toInt()).coerceIn(2, 30)
        val goalId = dataStore.goals.value.firstOrNull { it.owner == s.myRole && course.feeds(it.id) }?.id ?: course.goalId
        studyActions.logMinutes(goalId, "vocabulary", minutes, Clock.todayIso(s.me), note = "Words: ${results.size} cards", quiet = true)
        celebrations.logged("${results.count { it.value }} of ${results.size} words")
        _uiState.update { it.copy(minutes = minutes, deckSize = cards.size) }
    }
}
