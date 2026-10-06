package com.shahaabapps.twotracks.feature.learn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.Courses
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

    companion object {
        fun of(owner: PersonRole, courseId: String, progress: List<TopicProgress>, attempts: List<QuizAttempt>): CourseProgress {
            val mine = progress.filter { it.owner == owner && it.courseId == courseId }
            val att = attempts.filter { it.owner == owner && it.courseId == courseId }
            val passed = att.filter { it.passed }.map { it.moduleId }.toSet()
            val best = att.groupBy { it.moduleId }.mapValues { (_, a) -> a.maxOf { it.percent } }
            // A question stays in the review set until a later attempt gets it right (it is absent from "missed").
            val latestByModule = att.groupBy { it.moduleId }.mapValues { (_, a) -> a.maxByOrNull { it.dateIso + it.id } }
            val missed = latestByModule.values.filterNotNull().flatMap { it.missed }.toSet()
            return CourseProgress(mine.map { it.topicId }.toSet(), passed, best, missed)
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
        studyActions.logMinutes(course.goalId, module.componentId, minutes, today, note = "Learn: ${topic.title}", quiet = true)
        celebrations.logged("$minutes min learned")
        return null
    }

    suspend fun clearLearned(topicId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (learningRepository.clearLearned(s.coupleId, s.myRole, topicId) as? AppResult.Failure)?.error?.message
    }

    suspend fun recordAttempt(course: Course, module: Module, quiz: Quiz, score: Int, missed: List<String>, minutes: Int): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val today = Clock.todayIso(s.me)
        val passed = score * 100 / quiz.questions.size >= quiz.passPercent
        val r = learningRepository.addAttempt(
            s.coupleId,
            QuizAttempt(owner = s.myRole, courseId = course.id, moduleId = module.id, dateIso = today, score = score, total = quiz.questions.size, passed = passed, missed = missed),
        )
        if (r is AppResult.Failure) return r.error.message ?: "Could not save"
        studyActions.logMinutes(course.goalId, module.componentId, minutes.coerceIn(2, 60), today, note = "Test: ${module.title}", quiet = true)
        if (passed) celebrations.passed("${module.title} passed") else celebrations.logged("Test logged")
        return null
    }
}

// --------------------------------------------------------------- view models

data class LearnUiState(
    val courses: List<Course> = Courses.all,
    val progress: Map<String, CourseProgress> = emptyMap(),
    val partnerProgress: Map<String, CourseProgress> = emptyMap(),
    val partnerName: String = "",
)

@HiltViewModel
class LearnViewModel @Inject constructor(
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
) : ViewModel() {
    val uiState: StateFlow<LearnUiState> = combine(sessionStore.state, dataStore.topicProgress, dataStore.quizAttempts) { state, progress, attempts ->
        val session = (state as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session ?: return@combine LearnUiState()
        val partnerRole = if (session.myRole == PersonRole.TECH) PersonRole.TEACHER else PersonRole.TECH
        LearnUiState(
            progress = Courses.all.associate { it.id to CourseProgress.of(session.myRole, it.id, progress, attempts) },
            partnerProgress = Courses.all.associate { it.id to CourseProgress.of(partnerRole, it.id, progress, attempts) },
            partnerName = session.partner?.name ?: "Partner",
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
) : ViewModel() {
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val topicId: String = savedStateHandle.get<String>("topicId").orEmpty()
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
    fun clearMessage() { message.value = null }
}

/** The quiz runs entirely in memory until the results page, then one attempt is written. */
data class QuizUiState(
    val course: Course? = null,
    val module: Module? = null,
    val quiz: Quiz? = null,
    val index: Int = 0,
    val selected: Set<Int> = emptySet(),
    val revealed: Boolean = false,
    val answers: Map<String, Boolean> = emptyMap(),
    val finished: Boolean = false,
    val saved: Boolean = false,
    val message: String? = null,
) {
    val question: Question? get() = quiz?.questions?.getOrNull(index)
    val correctCount: Int get() = answers.count { it.value }
    val total: Int get() = quiz?.questions?.size ?: 0
    val percent: Int get() = if (total == 0) 0 else correctCount * 100 / total
    val passed: Boolean get() = quiz != null && percent >= quiz.passPercent
    val missedQuestions: List<Question> get() = quiz?.questions?.filter { answers[it.id] == false }.orEmpty()
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val actions: LearningActions,
) : ViewModel() {
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val moduleId: String = savedStateHandle.get<String>("moduleId").orEmpty()
    private val startedAt = System.currentTimeMillis()
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        val course = Courses.byId(courseId)
        val module = course?.modules?.firstOrNull { it.id == moduleId }
        _uiState.value = QuizUiState(course = course, module = module, quiz = module?.quiz)
    }

    fun toggle(option: Int) = _uiState.update { s ->
        if (s.revealed) return@update s
        val q = s.question ?: return@update s
        val next = if (q.multi) (if (option in s.selected) s.selected - option else s.selected + option) else setOf(option)
        s.copy(selected = next)
    }

    fun check() = _uiState.update { s ->
        val q = s.question ?: return@update s
        if (s.selected.isEmpty()) return@update s
        s.copy(revealed = true, answers = s.answers + (q.id to (s.selected == q.correct)))
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
        val err = actions.recordAttempt(c, m, q, s.correctCount, s.missedQuestions.map { it.id }, minutes)
        _uiState.update { it.copy(saved = err == null, message = err) }
    }

    fun retake() {
        _uiState.update { QuizUiState(course = it.course, module = it.module, quiz = it.quiz) }
    }
}
