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
}

data class Topic(
    val id: String,
    val title: String,
    val minutes: Int,
    val blocks: List<Block>,
)

sealed interface Block {
    data class Heading(val text: String) : Block
    data class Paragraph(val text: String) : Block
    data class Bullets(val items: List<String>) : Block
    data class Callout(val kind: CalloutKind, val text: String) : Block
    data class Code(val language: String, val code: String) : Block
    data class Table(val headers: List<String>, val rows: List<List<String>>) : Block
    data class Diagram(val spec: DiagramSpec) : Block
    data class Recap(val items: List<String>) : Block
}

enum class CalloutKind { PLAIN_WORDS, EXAM_TIP, REMEMBER, EXAMPLE }

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
