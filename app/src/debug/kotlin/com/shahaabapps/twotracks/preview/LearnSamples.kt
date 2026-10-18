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

/**
 * Key Vault's episode, in the player and on its topic: partway through, so
 * the transcript has a line lit, the scrubber has a run behind it and the
 * Listen card says how much is left.
 */
internal object ListenSamples {
    private val az = Courses.byId("az-204")!!
    const val TOPIC = "az-keyvault"
    private val module = az.modules.first { m -> m.topics.any { it.id == TOPIC } }
    private val topic = module.topics.first { it.id == TOPIC }
    private val podcast = com.shahaabapps.core.content.Podcasts.forTopic(TOPIC)!!

    /**
     * When each line would be spoken at the hosts' pace, for when the real
     * timing is not to hand: the tap tests do not read the app's assets.
     */
    private fun paced(): com.shahaabapps.core.content.PodcastTimeline {
        var t = 0.0
        val spans = podcast.lines.mapIndexed { i, line ->
            val words = com.shahaabapps.core.content.PodcastCue.strip(line.text).split(Regex("\\s+")).count { it.isNotBlank() }
            val span = com.shahaabapps.core.content.PodcastTimeline.Span(i, t, t + words * 0.36 + 0.4)
            t = span.end + 0.32
            span
        }
        return com.shahaabapps.core.content.PodcastTimeline(t, spans)
    }

    fun listen(timeline: com.shahaabapps.core.content.PodcastTimeline? = null, playing: Boolean = true, line: Int = 6, board: Boolean = true): com.shahaabapps.twotracks.feature.listen.ListenUiState {
        val time = timeline ?: paced()
        return com.shahaabapps.twotracks.feature.listen.ListenUiState(
            topicId = TOPIC, title = topic.title, module = module.number, lines = podcast.lines, timeline = time,
            loaded = true, playing = playing, position = time.startOf(line)!! + 2.0, speed = 1.2f,
            board = if (board) com.shahaabapps.core.content.PodcastBoards.forTopic(TOPIC) else null,
        )
    }

    /** Any episode at any line, with its real timing, for looking at its board: --es topic ID --ei line N. */
    fun listenTo(topicId: String, timeline: com.shahaabapps.core.content.PodcastTimeline, line: Int): com.shahaabapps.twotracks.feature.listen.ListenUiState {
        val (m, t) = az.modules.flatMap { m -> m.topics.map { m to it } }.first { it.second.id == topicId }
        return com.shahaabapps.twotracks.feature.listen.ListenUiState(
            topicId = topicId, title = t.title, module = m.number, lines = com.shahaabapps.core.content.Podcasts.forTopic(topicId)!!.lines,
            timeline = timeline, loaded = true, playing = true, position = (timeline.startOf(line) ?: 0.0) + 2.0, speed = 1f,
            board = com.shahaabapps.core.content.PodcastBoards.forTopic(topicId),
        )
    }

    fun nowPlaying(): com.shahaabapps.twotracks.feature.listen.NowPlaying {
        val s = listen()
        return com.shahaabapps.twotracks.feature.listen.NowPlaying(TOPIC, s.title, playing = true, position = s.position, duration = s.duration, speed = s.speed)
    }

    fun topic() = TopicUiState(
        course = az, module = module, topic = topic, learned = false,
        index = module.topics.indexOf(topic), count = module.topics.size,
        nextTopicId = module.topics.getOrNull(module.topics.indexOf(topic) + 1)?.id,
        listen = com.shahaabapps.twotracks.feature.listen.ListenCardState(minutes = 5, left = 151.0),
    )
}
