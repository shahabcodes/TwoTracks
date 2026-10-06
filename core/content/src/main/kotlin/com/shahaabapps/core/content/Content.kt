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
) {
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
) {
    val available: Boolean get() = topics.isNotEmpty()
    val readingMinutes: Int get() = topics.sumOf { it.minutes }
    /** Reading, a 15-minute try-it per topic and the test, doubled for a revision pass. */
    val studyMinutes: Int get() = (readingMinutes + topics.size * 15 + (quiz?.questions?.size ?: 0) * 2) * 2
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
}

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
    fun code(language: String, code: String) = Block.Code(language, code.trimIndent())
    fun table(headers: List<String>, vararg rows: List<String>) = Block.Table(headers, rows.toList())
    fun diagram(caption: String, nodes: List<DNode>, edges: List<DEdge> = emptyList(), aspect: Float = 0.62f) =
        Block.Diagram(DiagramSpec(caption, nodes, edges, aspect))
    fun recap(vararg items: String) = Block.Recap(items.toList())
    fun q(id: String, topic: String, prompt: String, options: List<String>, correct: Set<Int>, explanation: String, scenario: String? = null) =
        Question(id, topic, prompt, scenario, options, correct, explanation.trimIndent().replace("\n", " ").trim())
}

/** All courses the app ships. */
object Courses {
    const val VERSION = 1
    val all: List<Course> get() = listOf(Az204.course)
    fun byId(id: String): Course? = all.firstOrNull { it.id == id }
    fun topic(courseId: String, topicId: String): Pair<Module, Topic>? {
        val course = byId(courseId) ?: return null
        for (m in course.modules) m.topics.firstOrNull { it.id == topicId }?.let { return m to it }
        return null
    }
}
