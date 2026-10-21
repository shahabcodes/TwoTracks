package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.twotracks.session.CourseBridge
import kotlinx.coroutines.flow.first
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.content.Course
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.DailyStudy
import com.shahaabapps.core.content.draw
import com.shahaabapps.core.content.Module
import com.shahaabapps.core.content.Question
import com.shahaabapps.core.content.Quiz
import com.shahaabapps.core.content.Topic
import com.shahaabapps.core.content.WordDeck
import com.shahaabapps.core.data.learning.LearningRepository
import com.shahaabapps.core.planner.Pace
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
    suspend fun recordCheck(course: Course, question: Question, correct: Boolean, picked: Int, seconds: Int): String? {
        val s = sessionStore.current() ?: return null
        val r = learningRepository.addAttempt(
            s.coupleId,
            QuizAttempt(
                owner = s.myRole, courseId = course.id, moduleId = Courses.CHECK_MODULE_ID,
                dateIso = Clock.todayIso(s.me), score = if (correct) 1 else 0, total = 1, passed = correct,
                missed = if (correct) emptyList() else listOf(question.id), asked = listOf(question.id),
                at = System.currentTimeMillis(),
                picked = mapOf(question.id to listOf(picked)),
                seconds = mapOf(question.id to seconds),
            ),
        )
        return (r as? AppResult.Success)?.data
    }

    /**
     * The reply to "how sure are you?", which arrives after the answer.
     *
     * Nothing depends on it landing: an answer with no reply is somebody who
     * picked an option and scrolled on, and that is allowed. It is worth
     * having because it is the person saying how sure they were rather than
     * the clock guessing.
     */
    suspend fun recordSure(attemptId: String, questionId: String, sure: Boolean) {
        val s = sessionStore.current() ?: return
        learningRepository.setSure(s.coupleId, attemptId, questionId, sure)
    }

    suspend fun clearLearned(topicId: String): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        return (learningRepository.clearLearned(s.coupleId, s.myRole, topicId) as? AppResult.Failure)?.error?.message
    }

    suspend fun recordAttempt(
        course: Course,
        module: Module,
        quiz: Quiz,
        score: Int,
        missed: List<String>,
        minutes: Int,
        asked: List<String> = quiz.questions.map { it.id },
        picked: Map<String, List<Int>> = emptyMap(),
        seconds: Map<String, Int> = emptyMap(),
    ): String? {
        val s = sessionStore.current() ?: return "Not signed in"
        val today = Clock.todayIso(s.me)
        val total = asked.size.coerceAtLeast(1)
        val passed = score * 100 / total >= quiz.passPercent
        val attempt = QuizAttempt(
            owner = s.myRole, courseId = course.id, moduleId = module.id, dateIso = today, score = score,
            total = total, passed = passed, missed = missed, asked = asked, at = System.currentTimeMillis(),
            picked = picked, seconds = seconds,
        )
        val r = learningRepository.addAttempt(s.coupleId, attempt)
        if (r is AppResult.Failure) return r.error.message ?: "Could not save"
        val cap = quiz.timeLimitMinutes ?: 60
        studyActions.logMinutes(myGoalId(course, s.myRole), module.componentId, minutes.coerceIn(2, cap), today, note = "Test: ${module.title}", quiet = true)
        when (module.id) {
            Courses.DAILY_MODULE_ID -> celebrations.logged("Daily review done")
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
    /** Questions due for a spaced review today, per course. */
    val dailyDue: Map<String, Int> = emptyMap(),
    /** Today's loop across every course: one topic, the words due, the questions back round. */
    val daily: DailyStudy = DailyStudy(),
    /** Whose goals each course serves, by course id: the reader, the partner, or both. */
    val owners: Map<String, Set<PersonRole>> = emptyMap(),
    val myRole: PersonRole = PersonRole.TECH,
    /** The courses starred on this phone; [courses] lists them first. */
    val favorites: Set<String> = emptySet(),
)

@HiltViewModel
class LearnViewModel @Inject constructor(
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val favorites: CourseFavorites,
) : ViewModel() {
    fun toggleFavorite(courseId: String) = favorites.toggle(courseId)

    private val base: kotlinx.coroutines.flow.Flow<LearnUiState> = combine(sessionStore.state, dataStore.topicProgress, dataStore.quizAttempts, dataStore.flashcards, dataStore.goals) { state, progress, attempts, cards, goals ->
        val session = (state as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session ?: return@combine LearnUiState()
        val partnerRole = if (session.myRole == PersonRole.TECH) PersonRole.TEACHER else PersonRole.TECH
        val today = Clock.today(session.me)
        val mine = Courses.all.associate { it.id to CourseProgress.of(session.myRole, it.id, progress, attempts) }
        LearnUiState(
            progress = mine,
            dailyDue = mine.mapValues { (_, p) -> p.dailyDueCount(today) },
            daily = DailyStudy.of(session.myRole, Courses.all, mine, progress, attempts, cards, today),
            partnerProgress = Courses.all.associate { it.id to CourseProgress.of(partnerRole, it.id, progress, attempts) },
            partnerName = session.partner?.name ?: "Partner",
            owners = Courses.all.associate { c -> c.id to goals.filter { it.id in c.goalIds }.map { it.owner }.toSet() },
            myRole = session.myRole,
            decks = Courses.all.filter { Courses.words(it).isNotEmpty() }.associate { c ->
                c.id to DeckInfo(WordDeck.dueCount(session.myRole, c, cards, today), WordDeck.newCount(session.myRole, c, cards, progress), cards.count { it.owner == session.myRole && it.courseId == c.id })
            },
        )
    }

    val uiState: StateFlow<LearnUiState> = combine(base, favorites.ids) { s, starred ->
        s.copy(courses = CourseFavorites.sorted(s.courses, starred), favorites = starred)
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
    /** The topic's episode, when it has one in the app. */
    val listen: com.shahaabapps.twotracks.feature.listen.ListenCardState? = null,
)

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    sessionStore: SessionStore,
    dataStore: CoupleDataStore,
    private val actions: LearningActions,
    val speaker: Speaker,
    val recognizer: Recognizer,
    private val episodes: com.shahaabapps.twotracks.feature.listen.Episodes,
    private val place: com.shahaabapps.twotracks.feature.listen.ListenPlace,
    player: com.shahaabapps.twotracks.feature.listen.ListenPlayer,
) : ViewModel() {
    private val courseId: String = savedStateHandle.get<String>("courseId").orEmpty()
    private val topicId: String = savedStateHandle.get<String>("topicId").orEmpty()
    init { Courses.byId(courseId)?.speechLanguage?.let { recognizer.setLanguage(it) } }
    private val message = MutableStateFlow<String?>(null)
    private val openedAt = System.currentTimeMillis()

    val uiState: StateFlow<TopicUiState> = combine(sessionStore.state, dataStore.topicProgress, message, player.state) { state, progress, msg, now ->
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
            listen = listenCard(now),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TopicUiState())

    /** The Listen card: how long the episode runs, and how much is left once you have started it. */
    private fun listenCard(now: com.shahaabapps.twotracks.feature.listen.NowPlaying?): com.shahaabapps.twotracks.feature.listen.ListenCardState? {
        // The telling playing now if one of this topic's is, otherwise the one last picked, otherwise classic.
        val options = episodes.styles(topicId)
        val chosen = options.firstOrNull { it.episodeId == now?.episodeId } ?: options.firstOrNull { it.style == place.style } ?: options.firstOrNull() ?: return null
        val timeline = episodes.timeline(chosen.episodeId) ?: return null
        val minutes = kotlin.math.max(1, ((timeline.seconds + 30) / 60).toInt())
        val here = now?.takeIf { it.episodeId == chosen.episodeId }
        val at = here?.position ?: place.position(chosen.episodeId).takeIf { timeline.resumable(it) }
        return com.shahaabapps.twotracks.feature.listen.ListenCardState(
            minutes, playing = here?.playing == true, left = at?.let { timeline.seconds - it }, series = chosen.series,
            episodeId = chosen.episodeId, style = chosen.style.takeIf { options.size > 1 },
        )
    }

    fun markLearned() = viewModelScope.launch {
        val s = uiState.value
        val c = s.course ?: return@launch; val m = s.module ?: return@launch; val t = s.topic ?: return@launch
        val minutes = ((System.currentTimeMillis() - openedAt) / 60_000L).toInt()
        message.value = actions.markLearned(c, m, t, minutes)
    }

    fun unlearn() = viewModelScope.launch { message.value = actions.clearLearned(topicId) }

    /** The attempt each answered check was written as, so its reply can find it. */
    private val checkAttempts = mutableMapOf<String, String>()

    /** Called when the reader answers a one-question check inside the topic. */
    fun checkAnswered(question: Question, correct: Boolean, picked: Int, seconds: Int) = viewModelScope.launch {
        val c = uiState.value.course ?: return@launch
        actions.recordCheck(c, question, correct, picked, seconds)?.let { checkAttempts[question.id] = it }
    }

    /** Called when the reader says how sure they were, a moment later. */
    fun checkConfidence(question: Question, sure: Boolean) = viewModelScope.launch {
        val id = checkAttempts[question.id] ?: return@launch
        actions.recordSure(id, question.id, sure)
    }
    fun clearMessage() { message.value = null }
}

/** Which kind of sitting the quiz screen is running. */
enum class QuizKind { MODULE, MOCK, REVIEW, DAILY }

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
    /** What was chosen for each question answered, for the notes on the results page. */
    val picked: Map<String, Set<Int>> = emptyMap(),
    /** How long each answered question took. A question the clock beat has no entry. */
    val seconds: Map<String, Int> = emptyMap(),
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

    /** How each answered question was paced, which is what the results page reads. */
    val pace: Map<String, Pace.Verdict>
        get() = asked.associate { it.id to Pace.verdict(seconds[it.id], it.expectedSeconds) }
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

    /**
     * When the question on screen was first shown.
     *
     * Kept here rather than in the state because it is not drawn, and a value
     * that changes every second would recompose the whole paper.
     */
    private var questionShownAt = System.currentTimeMillis()
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    private var timer: Job? = null

    init {
        val course = Courses.byId(courseId)
        val session = (sessionStore.state.value as? com.shahaabapps.twotracks.session.SessionState.Ready)?.session
        val me = session?.myRole
        fun progressNow() = if (course != null && me != null) {
            CourseProgress.of(me, course.id, dataStore.topicProgress.value, dataStore.quizAttempts.value)
        } else {
            null
        }
        val (module, kind) = when (moduleId) {
            Courses.MOCK_MODULE_ID -> course?.mockModule() to QuizKind.MOCK
            Courses.REVIEW_MODULE_ID -> {
                val missed = course?.let { progressNow()?.reviewQuestions(it) }.orEmpty()
                course?.takeIf { missed.isNotEmpty() }?.reviewModule(missed) to QuizKind.REVIEW
            }
            Courses.DAILY_MODULE_ID -> {
                val due = if (course != null && session != null) {
                    progressNow()?.dailyDue(course, Clock.today(session.me)).orEmpty()
                } else {
                    emptyList()
                }
                course?.takeIf { due.isNotEmpty() }?.dailyModule(due) to QuizKind.DAILY
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

    fun toggle(option: Int) = _uiState.update { QuizFlow.toggled(it, option) }

    /** Records the answer. With instant feedback the explanation shows; in exam mode the next question follows at once. */
    fun check() {
        val before = _uiState.value
        val took = ((System.currentTimeMillis() - questionShownAt) / 1_000L).toInt()
        val after = QuizFlow.answered(before, took)
        // Nothing selected, or Check tapped twice: neither records anything,
        // and neither should move the paper on.
        if (after === before) return
        _uiState.value = after
        if (!before.instant) next()
    }

    fun next() {
        val after = QuizFlow.advanced(_uiState.value)
        _uiState.value = after
        if (after.finished) save() else questionShownAt = System.currentTimeMillis()
    }

    private fun save() = viewModelScope.launch {
        val s = _uiState.value
        val c = s.course ?: return@launch; val m = s.module ?: return@launch; val q = s.quiz ?: return@launch
        if (s.saved) return@launch
        val minutes = ((System.currentTimeMillis() - startedAt) / 60_000L).toInt()
        val err = actions.recordAttempt(
            c, m, q, s.correctCount, s.missedQuestions.map { it.id }, minutes, s.asked.map { it.id },
            picked = s.picked.mapValues { (_, v) -> v.sorted() }, seconds = s.seconds,
        )
        _uiState.update { it.copy(saved = err == null, message = err) }
    }

    /** A retake draws again, so a randomised paper is a different paper. */
    fun retake() {
        questionShownAt = System.currentTimeMillis()
        _uiState.update {
            QuizUiState(
                course = it.course, module = it.module, quiz = it.quiz, asked = it.quiz?.draw().orEmpty(), kind = it.kind,
                secondsLeft = it.quiz?.timeLimitMinutes?.let { m -> m * 60 },
            )
        }
        startTimer()
    }
}
