package com.shahaabapps.core.content

/**
 * A course a person can study inside the app. Content ships with the app
 * and is written as Kotlin so it is type-checked and tested like code.
 *
 * Course -> Module -> Topic -> blocks. Each module ends with a [Quiz]. A
 * module maps to one component of the matching study goal, so time spent
 * reading logs against the right skill and fills the bars the planner
 * already shows.
 */
data class Course(
    val id: String,
    val title: String,
    val subtitle: String,
    /** The StudyGoal this course feeds (for example "az-204"). */
    val goalId: String,
    val modules: List<Module>,
    /** Other goals the same course feeds, for example the partner's copy of German A1. */
    val extraGoalIds: List<String> = emptyList(),
    /** BCP-47 tag for text-to-speech of the foreign-language blocks, or null for none. */
    val speechLanguage: String? = null,
    /** A timed, course-wide practice exam, when the course has one. */
    val mockExam: Quiz? = null,
) {
    val goalIds: List<String> get() = listOf(goalId) + extraGoalIds
    /** The language the course's voice speaks, for hints in the UI. */
    val languageName: String get() = when (speechLanguage?.substringBefore("-")) { "de" -> "German"; "en" -> "English"; "fr" -> "French"; null -> "the course"; else -> speechLanguage }
    val allTopics: List<Topic> get() = modules.flatMap { it.topics }
    /** Every question in the course: module tests, the mock exam and the in-topic checks. */
    val allQuestions: List<Question>
        get() = modules.mapNotNull { it.quiz }.flatMap { it.questions } + (mockExam?.questions ?: emptyList()) +
            allTopics.flatMap { it.blocks }.filterIsInstance<Block.Check>().map { it.question }

    /** The mock exam wrapped as a module so the quiz screen can run it; logs to the practice component. */
    fun mockModule(): Module? = mockExam?.let { Module(Courses.MOCK_MODULE_ID, 0, "Mock exam", "A timed practice exam across every module.", "", "practice", allTopics, it) }

    /**
     * A short sitting of questions met before and now due again, so the course
     * still has something to give once the module tests are passed. Logs to
     * practice like the other sittings.
     */
    fun dailyModule(questions: List<Question>): Module =
        Module(
            Courses.DAILY_MODULE_ID, 0, "Daily review",
            "Questions you have met before, back just before they fade.", "", "practice", allTopics,
            Quiz(Courses.DAILY_MODULE_ID, "Daily review", passPercent = 0, questions = questions),
        )

    /** A review test built from [questions] the person got wrong; also logs to practice. */
    fun reviewModule(questions: List<Question>): Module =
        Module(Courses.REVIEW_MODULE_ID, 0, "Review", "The questions you missed, until you get them right.", "", "practice", allTopics, Quiz(Courses.REVIEW_MODULE_ID, "Review missed questions", passPercent = 0, questions = questions))
    /** A readable name for a goal component id, for hints in the UI. */
    fun skillName(componentId: String): String = when (componentId) {
        "vocabulary" -> "Vocabulary"; "grammar" -> "Grammar"; "listening" -> "Listening"; "reading" -> "Reading"; "writing" -> "Writing"; "speaking" -> "Speaking"
        "compute" -> "Compute"; "storage" -> "Storage"; "security" -> "Security"; "monitoring" -> "Monitoring"; "connect" -> "Connect"; "practice" -> "Practice"
        else -> componentId.replaceFirstChar { it.uppercase() }
    }
    fun feeds(goalId: String): Boolean = goalId in goalIds
    val topicCount: Int get() = modules.sumOf { it.topics.size }

    /**
     * Hours the content sets for each goal component, for modules that have
     * content: reading, a try-it task per topic, the test, then the same
     * again as a revision pass. Locked modules are left out so the seeded
     * budget still applies to them.
     */
    fun hoursByComponent(): Map<String, Double> {
        val byComponent = modules.filter { it.available }.groupBy { it.componentId }.mapValues { (_, ms) ->
            ms.sumOf { m -> m.studyMinutes } / 60.0
        }
        return byComponent + ("practice" to practiceHours)
    }

    /** Budget for the practice-exam component: the mock exam plus two rereads of the tests. */
    val practiceHours: Double get() = 8.0
}

data class Module(
    val id: String,
    val number: Int,
    val title: String,
    val summary: String,
    /** How much of the exam this area carries, as the exam guide states it. */
    val examWeight: String,
    /** The goal component minutes log against (for example "compute"). */
    val componentId: String,
    val topics: List<Topic>,
    val quiz: Quiz?,
    /** Extra drill time the module asks for beyond its pages, for example full practice papers. */
    val practiceMinutes: Int = 0,
) {
    val available: Boolean get() = topics.isNotEmpty()
    val readingMinutes: Int get() = topics.sumOf { it.minutes }
    /** Reading, a 15-minute try-it per topic and the test, doubled for a revision pass, plus the module's own drills. */
    val studyMinutes: Int get() = (readingMinutes + topics.size * 15 + (quiz?.questions?.size ?: 0) * 2) * 2 + practiceMinutes
}

data class Topic(
    val id: String,
    val title: String,
    val minutes: Int,
    val blocks: List<Block>,
    /** Official pages to read further, shown at the end of the topic. */
    val references: List<Reference> = emptyList(),
)

data class Reference(val title: String, val url: String)

/** One term and its meaning, listed at the end of a topic. */
data class Term(val term: String, val meaning: String)

sealed interface Block {
    data class Heading(val text: String) : Block
    data class Paragraph(val text: String) : Block
    data class Bullets(val items: List<String>) : Block
    data class Callout(val kind: CalloutKind, val text: String) : Block
    data class Code(val language: String, val code: String) : Block
    data class Table(val headers: List<String>, val rows: List<List<String>>) : Block
    data class Diagram(val spec: DiagramSpec) : Block
    data class Recap(val items: List<String>) : Block
    /** Numbered steps as you would do them, each with what you should see. */
    data class Steps(val title: String, val steps: List<Step>) : Block
    /** Side-by-side cards for a choice the exam likes to test. */
    data class Compare(val title: String, val cards: List<CompareCard>) : Block
    /** A short hands-on task with a checklist the reader can tick. */
    data class TryIt(val title: String, val intro: String, val checklist: List<String>) : Block
    /** A one-question check between sections; answered inline, not scored. */
    data class Check(val question: Question) : Block
    /** Terms used in this topic. */
    data class Glossary(val terms: List<Term>) : Block
    /** Vocabulary cards that flip and speak; every word joins the flashcard deck. */
    data class Vocab(val words: List<Word>) : Block
    /** A short conversation, spoken line by line, with translations on demand. */
    data class Dialogue(val title: String, val lines: List<Line>) : Block
    /** A checked exercise: gap fill, reorder, match or a free writing task with a model. */
    data class Exercise(val exercise: Ex) : Block
}

/** A vocabulary item. [id] is stable across content versions; it keys the flashcard state. */
data class Word(val id: String, val de: String, val en: String, val example: String, val exampleEn: String, val article: String? = null, val plural: String? = null) {
    val display: String get() = listOfNotNull(article, de).joinToString(" ")
}

data class Line(val speaker: String, val de: String, val en: String)

sealed interface Ex {
    val id: String
    val prompt: String
    /** Type the missing word; several accepted spellings. */
    data class Gap(override val id: String, override val prompt: String, val sentence: String, val answers: List<String>, val hint: String? = null, val audio: String? = null) : Ex
    /** Tap the pieces in the right order. */
    data class Reorder(override val id: String, override val prompt: String, val pieces: List<String>, val answer: String) : Ex
    /** Pair each left item with a right item. */
    data class Match(override val id: String, override val prompt: String, val pairs: List<Pair<String, String>>) : Ex
    /**
     * Write a short text; marked on content points, then a model answer is shown.
     * [keywords] holds, per point, the words any one of which shows the point was
     * covered; empty means the learner ticks the points by hand.
     */
    data class Writing(override val id: String, override val prompt: String, val points: List<String>, val model: String, val minWords: Int = 20, val keywords: List<List<String>> = emptyList(), val form: WritingForm = WritingForm.LETTER) : Ex
    /** Hören: the app speaks [audio] (never shown until answered); the learner answers [question]. */
    data class Listen(override val id: String, override val prompt: String, val audio: String, val question: String, val options: List<String>, val correct: Int) : Ex
    /** Sprechen: hear the model, say it, and the phone's speech recognition checks the words. */
    data class Speak(override val id: String, override val prompt: String, val target: String, val en: String) : Ex
}

/** What the marker checks beyond the content points: a letter's greeting and sign-off, an essay's paragraphs and linking, a report's overview and figures. */
enum class WritingForm { LETTER, ESSAY, REPORT }

data class Step(val action: String, val expect: String? = null)
data class CompareCard(val title: String, val points: List<String>, val pickWhen: String)

enum class CalloutKind { PLAIN_WORDS, EXAM_TIP, REMEMBER, EXAMPLE, MISTAKES, WORKED }

/**
 * A diagram described as nodes on a unit grid (0..1 in both axes) joined by
 * edges. The app draws it natively so it follows the theme and dark mode.
 */
data class DiagramSpec(
    val caption: String,
    val nodes: List<DNode>,
    val edges: List<DEdge> = emptyList(),
    /** Height relative to width; 0.6 is a wide banner, 1.0 a square. */
    val aspect: Float = 0.62f,
)

enum class NodeKind { BOX, CYLINDER, CLOUD, USER, PILL, NOTE }

data class DNode(
    val id: String,
    val label: String,
    val x: Float,
    val y: Float,
    val w: Float = 0.26f,
    val h: Float = 0.16f,
    val kind: NodeKind = NodeKind.BOX,
    /** 0 = accent, 1 = secondary, 2 = muted. */
    val tone: Int = 0,
    val sub: String? = null,
)

data class DEdge(
    val from: String,
    val to: String,
    val label: String? = null,
    val dashed: Boolean = false,
    val bidirectional: Boolean = false,
)

data class Quiz(
    val id: String,
    val title: String,
    val passPercent: Int = 80,
    val questions: List<Question>,
    /** When set, the test is timed and ends itself when the clock runs out. */
    val timeLimitMinutes: Int? = null,
    /**
     * When set, each sitting draws this many questions at random from
     * [questions], so a retake is a different paper rather than a memory test.
     */
    val drawCount: Int? = null,
    /** False hides the explanation until the results page, like a real exam sitting. */
    val instantFeedback: Boolean = true,
)

data class Question(
    val id: String,
    /** The topic the question checks, for the review links on the results page. */
    val topicId: String,
    val prompt: String,
    /** Optional scenario shown above the prompt in a card. */
    val scenario: String? = null,
    val options: List<String>,
    /** Indices into [options]. More than one means "select all that apply". */
    val correct: Set<Int>,
    val explanation: String,
    /** Hören questions: text the app speaks and hides until the answer is in. */
    val listen: String? = null,
) {
    val multi: Boolean get() = correct.size > 1
}

/** Small helpers so content reads cleanly. */
object B {
    fun h(text: String) = Block.Heading(text)
    fun p(text: String) = Block.Paragraph(text.trimIndent().replace("\n", " ").replace("  ", " ").trim())
    fun bullets(vararg items: String) = Block.Bullets(items.toList())
    fun plain(text: String) = Block.Callout(CalloutKind.PLAIN_WORDS, text.trimIndent().replace("\n", " ").trim())
    fun tip(text: String) = Block.Callout(CalloutKind.EXAM_TIP, text.trimIndent().replace("\n", " ").trim())
    fun remember(text: String) = Block.Callout(CalloutKind.REMEMBER, text.trimIndent().replace("\n", " ").trim())
    fun example(text: String) = Block.Callout(CalloutKind.EXAMPLE, text.trimIndent().replace("\n", " ").trim())
    fun mistakes(vararg items: String) = Block.Callout(CalloutKind.MISTAKES, items.joinToString("\n") { "• $it" })
    fun worked(text: String) = Block.Callout(CalloutKind.WORKED, text.trimIndent().replace("\n", " ").trim())
    fun steps(title: String, vararg steps: Pair<String, String?>) = Block.Steps(title, steps.map { Step(it.first, it.second) })
    fun compare(title: String, vararg cards: CompareCard) = Block.Compare(title, cards.toList())
    fun card(title: String, pickWhen: String, vararg points: String) = CompareCard(title, points.toList(), pickWhen)
    fun tryIt(title: String, intro: String, vararg checklist: String) = Block.TryIt(title, intro.trimIndent().replace("\n", " ").trim(), checklist.toList())
    fun check(id: String, topic: String, prompt: String, options: List<String>, correct: Int, explanation: String) =
        Block.Check(Question(id, topic, prompt, null, options, setOf(correct), explanation.trimIndent().replace("\n", " ").trim()))
    fun glossary(vararg terms: Pair<String, String>) = Block.Glossary(terms.map { Term(it.first, it.second) })
    fun ref(title: String, url: String) = Reference(title, url)
    fun vocab(vararg words: Word) = Block.Vocab(words.toList())
    fun w(id: String, de: String, en: String, example: String, exampleEn: String, article: String? = null, plural: String? = null) = Word(id, de, en, example, exampleEn, article, plural)
    fun dialogue(title: String, vararg lines: Line) = Block.Dialogue(title, lines.toList())
    fun line(speaker: String, de: String, en: String) = Line(speaker, de, en)
    fun gap(id: String, prompt: String, sentence: String, vararg answers: String, hint: String? = null, audio: String? = null) = Block.Exercise(Ex.Gap(id, prompt, sentence, answers.toList(), hint, audio))
    fun reorder(id: String, prompt: String, answer: String) = Block.Exercise(Ex.Reorder(id, prompt, answer.split(" ").shuffled(kotlin.random.Random(id.hashCode())), answer))
    fun match(id: String, prompt: String, vararg pairs: Pair<String, String>) = Block.Exercise(Ex.Match(id, prompt, pairs.toList()))
    fun writing(id: String, prompt: String, model: String, vararg points: String, minWords: Int = 20) = Block.Exercise(Ex.Writing(id, prompt, points.toList(), model.trimIndent(), minWords))
    /** A writing task the app marks itself: each point is a label and the words that prove it. */
    fun marked(id: String, prompt: String, model: String, vararg points: Pair<String, List<String>>, minWords: Int = 20, form: WritingForm = WritingForm.LETTER) =
        Block.Exercise(Ex.Writing(id, prompt, points.map { it.first }, model.trimIndent(), minWords, points.map { it.second }, form))
    fun listen(id: String, audio: String, question: String, options: List<String>, correct: Int, prompt: String = "Play the audio, then answer. You can play it twice, like the exam.") =
        Block.Exercise(Ex.Listen(id, prompt, audio.trimIndent().replace("\n", " ").trim(), question, options, correct))
    fun speak(id: String, target: String, en: String, prompt: String = "Listen, then say it. The phone checks your words.") = Block.Exercise(Ex.Speak(id, prompt, target, en))
    fun code(language: String, code: String) = Block.Code(language, code.trimIndent())
    fun table(headers: List<String>, vararg rows: List<String>) = Block.Table(headers, rows.toList())
    fun diagram(caption: String, nodes: List<DNode>, edges: List<DEdge> = emptyList(), aspect: Float = 0.62f) =
        Block.Diagram(DiagramSpec(caption, nodes, edges, aspect))
    fun recap(vararg items: String) = Block.Recap(items.toList())
    /**
     * Rotates each question's options by its position so the right answer is
     * not usually in the same slot. Explanations never mention letters, so
     * the text stays correct.
     */
    fun spread(questions: List<Question>): List<Question> = questions.mapIndexed { i, q ->
        // Fixed-order answer sets (True/False/Not Given and the like) keep their order.
        if (q.options.size < 3 || q.options.first().uppercase() in setOf("TRUE", "YES", "RICHTIG")) return@mapIndexed q
        val n = q.options.size
        val r = i % n
        q.copy(options = q.options.drop(r) + q.options.take(r), correct = q.correct.map { (it - r + n) % n }.toSet())
    }
    fun q(id: String, topic: String, prompt: String, options: List<String>, correct: Set<Int>, explanation: String, scenario: String? = null, listen: String? = null) =
        Question(id, topic, prompt, scenario, options, correct, explanation.trimIndent().replace("\n", " ").trim(), listen?.trimIndent()?.replace("\n", " ")?.trim())
}

/** A sitting of a quiz: the whole paper, or a random draw when the quiz asks for one. */
fun Quiz.draw(seed: Long = System.nanoTime()): List<Question> {
    val n = drawCount ?: return questions
    if (n >= questions.size) return questions
    return questions.shuffled(kotlin.random.Random(seed)).take(n)
}

/** All courses the app ships. */
object Courses {
    const val VERSION = 1
    /** Module ids the quiz route accepts besides real modules. */
    const val MOCK_MODULE_ID = "mock-exam"
    const val REVIEW_MODULE_ID = "review"
    /** A sitting of questions due again on the spaced-repetition schedule. */
    const val DAILY_MODULE_ID = "daily"
    /** Attempts recorded for the one-question checks inside a topic. */
    const val CHECK_MODULE_ID = "checks"
    val all: List<Course> get() = listOf(Az204.course, GermanA1.course, Ielts.course)
    fun forGoal(goalId: String): Course? = all.firstOrNull { it.feeds(goalId) }
    /** Every vocabulary word in a course, in module order. */
    fun words(course: Course): List<Word> = course.modules.flatMap { m -> m.topics.flatMap { t -> t.blocks.filterIsInstance<Block.Vocab>().flatMap { it.words } } }
    fun byId(id: String): Course? = all.firstOrNull { it.id == id }
    fun topic(courseId: String, topicId: String): Pair<Module, Topic>? {
        val course = byId(courseId) ?: return null
        for (m in course.modules) m.topics.firstOrNull { it.id == topicId }?.let { return m to it }
        return null
    }
}
