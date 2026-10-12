package com.shahaabapps.twotracks.preview

import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.DailyStudy
import com.shahaabapps.core.content.StudyKind
import com.shahaabapps.core.content.StudyStep
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.twotracks.feature.learn.DeckInfo
import com.shahaabapps.twotracks.feature.learn.DeckUiState
import com.shahaabapps.twotracks.feature.learn.LearnUiState
import com.shahaabapps.twotracks.feature.learn.QuizKind
import com.shahaabapps.twotracks.feature.learn.QuizUiState
import com.shahaabapps.twotracks.feature.learn.Speaker
import com.shahaabapps.twotracks.feature.learn.TopicUiState

/**
 * The prototype's Learn screens on the real course content: Usaid on the
 * developer's track, four topics into German A1 and one into AZ-204, Sanya
 * six into German and IELTS.
 */
internal object LearnSamples {
    private val az = Courses.byId("az-204")!!
    private val de = Courses.byId("de-a1")!!
    private val ielts = Courses.all.first { it.id != az.id && it.id != de.id }
    private val m4 = de.modules.first { it.id == "de-m4" }

    private fun first(course: com.shahaabapps.core.content.Course, n: Int) = course.modules.flatMap { it.topics }.take(n).map { it.id }.toSet()
    private fun progress(learned: Set<String>, passed: Set<String> = emptySet(), best: Map<String, Int> = emptyMap(), missed: Set<String> = emptySet()) =
        CourseProgress(learnedTopicIds = learned, passedModuleIds = passed, bestPercentByModule = best, missedQuestionIds = missed)

    private val myGerman = progress(de.modules.first().topics.map { it.id }.toSet() + "de4-words", passed = setOf(de.modules.first().id), best = mapOf(de.modules.first().id to 92))

    fun learn() = LearnUiState(
        courses = Courses.all,
        progress = mapOf(az.id to progress(first(az, 1)), de.id to myGerman, ielts.id to progress(emptySet())),
        partnerProgress = mapOf(az.id to progress(emptySet()), de.id to progress(first(de, 6)), ielts.id to progress(first(ielts, 6))),
        partnerName = "Sanya",
        decks = mapOf(de.id to DeckInfo(due = 14, fresh = 4, size = 120)),
        dailyDue = mapOf(az.id to 0, de.id to 5, ielts.id to 0),
        daily = DailyStudy(
            listOf(
                StudyStep(StudyKind.QUESTIONS, de.id, "Daily review", "5 questions", 4, done = false),
                StudyStep(StudyKind.WORDS, de.id, "Words", "18 cards", 4, done = false),
            ),
        ),
        owners = mapOf(az.id to setOf(PersonRole.TECH), de.id to setOf(PersonRole.TECH, PersonRole.TEACHER), ielts.id to setOf(PersonRole.TEACHER)),
        myRole = PersonRole.TECH,
    )

    val germanId: String get() = de.id

    fun topic() = TopicUiState(
        course = de, module = m4, topic = m4.topics.first { it.id == "de4-grammar" },
        learned = false, index = 1, count = m4.topics.size, nextTopicId = "de4-dialogue",
    )

    private val quizQuestions = m4.quiz!!.questions.take(12)

    fun quiz() = QuizUiState(
        course = de, module = m4, quiz = m4.quiz, asked = quizQuestions, kind = QuizKind.MODULE, index = 2,
        answers = mapOf(quizQuestions[0].id to true, quizQuestions[1].id to false),
        picked = mapOf(quizQuestions[0].id to quizQuestions[0].correct.toSet(), quizQuestions[1].id to setOf((quizQuestions[1].correct.first() + 1) % quizQuestions[1].options.size)),
    )

    fun results(): QuizUiState {
        val answers = quizQuestions.mapIndexed { i, q -> q.id to (i !in setOf(1, 4, 7)) }.toMap()
        val picked = quizQuestions.associate { q -> q.id to if (answers[q.id] == true) q.correct.toSet() else setOf((q.correct.first() + 1) % q.options.size) }
        return QuizUiState(
            course = de, module = m4, quiz = m4.quiz, asked = quizQuestions, kind = QuizKind.MODULE, index = quizQuestions.lastIndex,
            answers = answers, picked = picked, seconds = quizQuestions.associate { it.id to 70 }, finished = true, saved = true,
        )
    }

    fun deck() = DeckUiState(
        course = de, session = Courses.words(de).drop(40).take(18), index = 3, flipped = false,
        dueCount = 14, newCount = 4, deckSize = 120, speakerState = Speaker.State.READY, nextIntervalDays = 6,
    )
}
