package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.twotracks.session.CourseBridge
import kotlinx.coroutines.flow.first
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.draw
import com.shahaabapps.core.content.Module
import com.shahaabapps.core.content.Question
import com.shahaabapps.core.content.Quiz
import com.shahaabapps.core.content.Topic
import com.shahaabapps.core.data.learning.LearningRepository
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.QuizAttempt
import com.shahaabapps.core.model.TopicProgress
import com.shahaabapps.twotracks.session.Celebrations
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.session.StudyActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// ----------------------------------------------------------------- progress

/** One person's view of a course: what is learned, what is passed. */
data class CourseProgress(
    val learnedTopicIds: Set<String>,
    val passedModuleIds: Set<String>,
    val bestPercentByModule: Map<String, Int>,
    val missedQuestionIds: Set<String>,
) {
    fun moduleLearned(m: Module): Int = m.topics.count { it.id in learnedTopicIds }
    fun moduleFraction(m: Module): Float = if (m.topics.isEmpty()) 0f else moduleLearned(m).toFloat() / m.topics.size
    fun courseFraction(c: Course): Float {
        val total = c.modules.sumOf { it.topics.size }
        return if (total == 0) 0f else c.modules.sumOf { moduleLearned(it) }.toFloat() / total
    }
    fun nextTopic(c: Course): Pair<Module, Topic>? {
        for (m in c.modules) m.topics.firstOrNull { it.id !in learnedTopicIds }?.let { return m to it }
        return null
    }
    /** Module tests passed, leaving out the mock exam and review sittings. */
    fun modulesPassed(c: Course): Int = c.modules.count { it.id in passedModuleIds }
    /** The missed questions that still need a correct answer, in course order. */
    fun reviewQuestions(c: Course): List<Question> = c.allQuestions.filter { it.id in missedQuestionIds }
    val mockBest: Int? get() = bestPercentByModule[Courses.MOCK_MODULE_ID]
    val mockPassed: Boolean get() = Courses.MOCK_MODULE_ID in passedModuleIds

    companion object {
        fun of(owner: PersonRole, courseId: String, progress: List<TopicProgress>, attempts: List<QuizAttempt>): CourseProgress {
            val mine = progress.filter { it.owner == owner && it.courseId == courseId }
            val att = attempts.filter { it.owner == owner && it.courseId == courseId }
            val passed = att.filter { it.passed }.map { it.moduleId }.toSet()
            val best = att.groupBy { it.moduleId }.mapValues { (_, a) -> a.maxOf { it.percent } }
            // A question joins the review set when missed and leaves it when a later sitting
            // (module test, mock exam or review) asks it again and gets it right.
            val course = Courses.byId(courseId)
            val missed = linkedSetOf<String>()
            att.sortedWith(compareBy({ it.dateIso }, { it.at }, { it.id })).forEach { a ->
                val asked = a.asked.ifEmpty { askedByDefault(course, a.moduleId) }
                missed -= asked.toSet()
                missed += a.missed
            }
            return CourseProgress(mine.map { it.topicId }.toSet(), passed, best, missed)
        }

        /** Older attempts did not record what was asked: assume the whole test of that module. */
        private fun askedByDefault(course: Course?, moduleId: String): List<String> = when (moduleId) {
            Courses.MOCK_MODULE_ID -> course?.mockExam?.questions?.map { it.id }.orEmpty()
            Courses.REVIEW_MODULE_ID, Courses.CHECK_MODULE_ID -> emptyList()
            else -> course?.modules?.firstOrNull { it.id == moduleId }?.quiz?.questions?.map { it.id }.orEmpty()
        }
    }
}

// ------------------------------------------------------------------ actions

/** Write side for the Learn tab: marking topics learned (and logging the time) and recording tests. */
class LearningActions @Inject constructor(
    private val sessionStore: SessionStore,
    private val learningRepository: LearningRepository,
    private val studyActions: StudyActions,
    private val celebrations: Celebrations,
    private val dataStore: CoupleDataStore,
) {
    /**
     * Marks a topic learned and logs [minutes] to the course goal's component for
     * the module, so the hours count toward the plan. Logging is capped to the
     * topic's stated length plus a little, so a page left open overnight does not
     * record a night of study.
     */
    suspend fun markLearned(course: Course, module: Module, topic: Topic, minutesOnPage: Int): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val today = Clock.todayIso(s.me)
        val minutes = minutesOnPage.coerceIn(3, topic.minutes + 10)
        val r = learningRepository.markLearned(
            s.coupleId, TopicProgress(owner = s.myRole, courseId = course.id, topicId = topic.id, learnedAtIso = today, minutes = minutes),
        )
        if (r is AppResult.Failure) return r.error.message ?: "Could not save"
        studyActions.logMinutes(myGoalId(course, s.myRole), module.componentId, minutes, today, note = "Learn: ${topic.title}", quiet = true)
        celebrations.logged("$minutes min learned")
        autoPassIfFinished(course, s.myRole, extraTopic = topic.id)
        return null
    }

    /** When every module is read and passed, the goal is marked passed so the level ticks. */
    private suspend fun autoPassIfFinished(course: Course, owner: PersonRole, extraTopic: String? = null, extraAttempt: QuizAttempt? = null) {
        val s = sessionStore.current() ?: return
        val progress = learningRepository.observeTopicProgress(s.coupleId).first().toMutableList()
        if (extraTopic != null && progress.none { it.owner == owner && it.topicId == extraTopic }) progress += TopicProgress(owner = owner, courseId = course.id, topicId = extraTopic)
        val attempts = learningRepository.observeQuizAttempts(s.coupleId).first().toMutableList()
        if (extraAttempt != null) attempts += extraAttempt
        if (!CourseBridge.courseFinished(course, owner, progress, attempts)) return
        val goal = dataStoreGoals().firstOrNull { it.id == myGoalId(course, owner) } ?: return
        if (!goal.passed) studyActions.setGoalPassed(goal.id, true)
    }

    private suspend fun dataStoreGoals() = dataStore.goals.value

    /** The copy of the course goal this person owns (German A1 exists for both tracks). */
    private fun myGoalId(course: Course, owner: PersonRole): String =
        dataStore.goals.value.firstOrNull { it.owner == owner && course.feeds(it.id) }?.id ?: course.goalId

    /**
     * Records a one-question check inside a topic so a wrong answer joins the
     * review set and a later right answer clears it. No minutes are logged,
     * because the time is already counted in the topic's reading time.
     */
    suspend fun recordCheck(course: Course, question: Question, correct: Boolean) {
        val s = sessionStore.current() ?: return
        learningRepository.addAttempt(
            s.coupleId,
            QuizAttempt(
                owner = s.myRole, courseId = course.id, moduleId = Courses.CHECK_MODULE_ID,
                dateIso = Clock.todayIso(s.me), score = if (correct) 1 else 0, total = 1, passed = correct,
                missed = if (correct) emptyList() else listOf(question.id), asked = listOf(question.id),
                at = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun clearLearned(topicId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (learningRepository.clearLearned(s.coupleId, s.myRole, topicId) as? AppResult.Failure)?.error?.message
    }

    suspend fun recordAttempt(course: Course, module: Module, quiz: Quiz, score: Int, missed: List<String>, minutes: Int, asked: List<String> = quiz.questions.map { it.id }): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val today = Clock.todayIso(s.me)
        val total = asked.size.coerceAtLeast(1)
        val passed = score * 100 / total >= quiz.passPercent
        val attempt = QuizAttempt(owner = s.myRole, courseId = course.id, moduleId = module.id, dateIso = today, score = score, total = total, passed = passed, missed = missed, asked = asked, at = System.currentTimeMillis())
        val r = learningRepository.addAttempt(s.coupleId, attempt)
        if (r is AppResult.Failure) return r.error.message ?: "Could not save"
        val cap = quiz.timeLimitMinutes ?: 60
        studyActions.logMinutes(myGoalId(course, s.myRole), module.componentId, minutes.coerceIn(2, cap), today, note = "Test: ${module.title}", quiet = true)
        when (module.id) {
            Courses.REVIEW_MODULE_ID -> celebrations.logged("Review logged")
            Courses.MOCK_MODULE_ID -> if (passed) celebrations.passed("Mock exam passed") else celebrations.logged("Mock exam logged")
            else -> if (passed) celebrations.passed("${module.title} passed") else celebrations.logged("Test logged")
        }
        if (passed && module.id in course.modules.map { it.id }) autoPassIfFinished(course, s.myRole, extraAttempt = attempt)
        return null
    }
}

// --------------------------------------------------------------- view models

data class DeckInfo(val due: Int, val fresh: Int, val size: Int)

data class LearnUiState(
    val courses: List<Course> = Courses.all,
    val progress: Map<String, CourseProgress> = emptyMap(),
    val partnerProgress: Map<String, CourseProgress> = emptyMap(),
    val partnerName: String = "",
    /** Flashcard deck state per course, only for courses with vocabulary. */
    val decks: Map<String, DeckInfo> = emptyMap(),
)

@HiltViewModel
class LearnViewModel @Inject constructor(
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
) : ViewModel() {
    val uiState: StateFlow<LearnUiState> = combine(sessionStore.state, dataStore.topicProgress, dataStore.quizAttempts, dataStore.flashcards) { state, progress, attempts, cards ->
        val session = (state as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session ?: return@combine LearnUiState()
        val partnerRole = if (session.myRole == PersonRole.TECH) PersonRole.TEACHER else PersonRole.TECH
        val today = Clock.today(session.me)
        LearnUiState(
            progress = Courses.all.associate { it.id to CourseProgress.of(session.myRole, it.id, progress, attempts) },
            partnerProgress = Courses.all.associate { it.id to CourseProgress.of(partnerRole, it.id, progress, attempts) },
            partnerName = session.partner?.name ?: "Partner",
            decks = Courses.all.filter { Courses.words(it).isNotEmpty() }.associate { c ->
                c.id to DeckInfo(Deck.dueCount(session.myRole, c, cards, today), Deck.newCount(session.myRole, c, cards, progress), cards.count { it.owner == session.myRole && it.courseId == c.id })
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LearnUiState())
}

data class TopicUiState(
    val course: Course? = null,
    val module: Module? = null,
    val topic: Topic? = null,
    val learned: Boolean = false,
    val index: Int = 0,
    val count: Int = 0,
    val nextTopicId: String? = null,
    val message: String? = null,
)

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val actions: LearningActions,
    val speaker: Speaker,
    val recognizer: Recognizer,
) : ViewModel() {
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val topicId: String = savedStateHandle.get<String>("topicId").orEmpty()
    init { Courses.byId(courseId)?.speechLanguage?.let { recognizer.setLanguage(it) } }
    private val message = MutableStateFlow<String?>(null)
    private val openedAt = System.currentTimeMillis()

    val uiState: StateFlow<TopicUiState> = combine(sessionStore.state, dataStore.topicProgress, message) { state, progress, msg ->
        val session = (state as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session
        val course = Courses.byId(courseId) ?: return@combine TopicUiState(message = msg)
        val (module, topic) = Courses.topic(courseId, topicId) ?: return@combine TopicUiState(course = course, message = msg)
        val idx = module.topics.indexOf(topic)
        TopicUiState(
            course = course, module = module, topic = topic,
            learned = session != null && progress.any { it.owner == session.myRole && it.topicId == topicId },
            index = idx, count = module.topics.size,
            nextTopicId = module.topics.getOrNull(idx + 1)?.id,
            message = msg,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TopicUiState())

    fun markLearned() = viewModelScope.launch {
        val s = uiState.value
        val c = s.course ?: return@launch; val m = s.module ?: return@launch; val t = s.topic ?: return@launch
        val minutes = ((System.currentTimeMillis() - openedAt) / 60_000L).toInt()
        message.value = actions.markLearned(c, m, t, minutes)
    }

    fun unlearn() = viewModelScope.launch { message.value = actions.clearLearned(topicId) }

    /** Called when the reader answers a one-question check inside the topic. */
    fun checkAnswered(question: Question, correct: Boolean) = viewModelScope.launch {
        val c = uiState.value.course ?: return@launch
        actions.recordCheck(c, question, correct)
    }
    fun clearMessage() { message.value = null }
}

/** Which kind of sitting the quiz screen is running. */
enum class QuizKind { MODULE, MOCK, REVIEW }

/** The quiz runs entirely in memory until the results page, then one attempt is written. */
data class QuizUiState(
    val course: Course? = null,
    val module: Module? = null,
    val quiz: Quiz? = null,
    /** The questions for this sitting: the whole paper, or a random draw. */
    val asked: List<Question> = emptyList(),
    val kind: QuizKind = QuizKind.MODULE,
    val index: Int = 0,
    val selected: Set<Int> = emptySet(),
    val revealed: Boolean = false,
    val answers: Map<String, Boolean> = emptyMap(),
    val finished: Boolean = false,
    val saved: Boolean = false,
    /** Seconds left on a timed test, or null when untimed. */
    val secondsLeft: Int? = null,
    val timedOut: Boolean = false,
    val message: String? = null,
) {
    val question: Question? get() = asked.getOrNull(index)
    val correctCount: Int get() = answers.count { it.value }
    val total: Int get() = asked.size
    val percent: Int get() = if (total == 0) 0 else correctCount * 100 / total
    val passed: Boolean get() = quiz != null && percent >= quiz.passPercent
    /** Missed includes questions never reached when the clock ran out. */
    val missedQuestions: List<Question> get() = asked.filter { answers[it.id] != true }
    val instant: Boolean get() = quiz?.instantFeedback != false
    val clock: String? get() = secondsLeft?.let { "%d:%02d".format(it / 60, it % 60) }
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val actions: LearningActions,
    val speaker: Speaker,
) : ViewModel() {
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val moduleId: String = savedStateHandle.get<String>("moduleId").orEmpty()
    private val startedAt = System.currentTimeMillis()
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    private var timer: Job? = null

    init {
        val course = Courses.byId(courseId)
        val me = (sessionStore.state.value as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session?.myRole
        val (module, kind) = when (moduleId) {
            Courses.MOCK_MODULE_ID -> course?.mockModule() to QuizKind.MOCK
            Courses.REVIEW_MODULE_ID -> {
                val missed = if (course != null && me != null) CourseProgress.of(me, course.id, dataStore.topicProgress.value, dataStore.quizAttempts.value).reviewQuestions(course) else emptyList()
                course?.takeIf { missed.isNotEmpty() }?.reviewModule(missed) to QuizKind.REVIEW
            }
            else -> course?.modules?.firstOrNull { it.id == moduleId } to QuizKind.MODULE
        }
        val quiz = module?.quiz
        _uiState.value = QuizUiState(
            course = course, module = module, quiz = quiz, asked = quiz?.draw().orEmpty(), kind = kind,
            secondsLeft = quiz?.timeLimitMinutes?.let { it * 60 },
        )
        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        if (_uiState.value.secondsLeft == null) return
        timer = viewModelScope.launch {
            while (true) {
                delay(1_000)
                val s = _uiState.value
                val left = s.secondsLeft ?: return@launch
                if (s.finished) return@launch
                if (left <= 1) {
                    _uiState.update { it.copy(secondsLeft = 0, finished = true, timedOut = true) }
                    save()
                    return@launch
                }
                _uiState.update { it.copy(secondsLeft = left - 1) }
            }
        }
    }

    fun toggle(option: Int) = _uiState.update { s ->
        if (s.revealed) return@update s
        val q = s.question ?: return@update s
        val next = if (q.multi) (if (option in s.selected) s.selected - option else s.selected + option) else setOf(option)
        s.copy(selected = next)
    }

    /** Records the answer. With instant feedback the explanation shows; in exam mode the next question follows at once. */
    fun check() {
        val s = _uiState.value
        val q = s.question ?: return
        if (s.selected.isEmpty()) return
        _uiState.update { it.copy(revealed = true, answers = it.answers + (q.id to (it.selected == q.correct))) }
        if (!s.instant) next()
    }

    fun next() {
        val s = _uiState.value
        if (s.index + 1 >= s.total) {
            _uiState.update { it.copy(finished = true) }
            save()
        } else {
            _uiState.update { it.copy(index = it.index + 1, selected = emptySet(), revealed = false) }
        }
    }

    private fun save() = viewModelScope.launch {
        val s = _uiState.value
        val c = s.course ?: return@launch; val m = s.module ?: return@launch; val q = s.quiz ?: return@launch
        if (s.saved) return@launch
        val minutes = ((System.currentTimeMillis() - startedAt) / 60_000L).toInt()
        val err = actions.recordAttempt(c, m, q, s.correctCount, s.missedQuestions.map { it.id }, minutes, s.asked.map { it.id })
        _uiState.update { it.copy(saved = err == null, message = err) }
    }

    /** A retake draws again, so a randomised paper is a different paper. */
    fun retake() {
        _uiState.update {
            QuizUiState(
                course = it.course, module = it.module, quiz = it.quiz, asked = it.quiz?.draw().orEmpty(), kind = it.kind,
                secondsLeft = it.quiz?.timeLimitMinutes?.let { m -> m * 60 },
            )
        }
        startTimer()
    }
}
