package com.shahaabapps.twotracks.ui.guide

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect

/**
 * The tour, over the app's own screens.
 *
 * A guide drawn as its own pages has to describe the app from the outside and
 * then be kept in step with it. This one points at the real thing: each step
 * names a route to be on and a piece of that screen to ring, and the app
 * navigates there as the tour moves.
 *
 * @param anchor the key of the thing to ring. A step whose anchor is not on
 *   screen still shows its words, with nothing ringed, so a tour never stalls
 *   on a control that happens not to be there.
 */
data class GuideStep(
    val route: String,
    val anchor: String?,
    val title: String,
    val body: String,
)

/**
 * Where the tour is, and where everything it points at happens to be.
 *
 * Bounds are reported by the composables themselves as they are laid out, so
 * the ring follows the real control across screen sizes rather than being
 * positioned by hand against one phone.
 */
class GuideState(private val steps: List<GuideStep>) {

    private val anchors = mutableStateMapOf<String, Rect>()
    var index by mutableStateOf(-1)
        private set

    val running: Boolean get() = index in steps.indices
    val step: GuideStep? get() = steps.getOrNull(index)
    val position: Int get() = index + 1
    val total: Int get() = steps.size
    val isLast: Boolean get() = index == steps.lastIndex

    /** The rectangle to ring, or null when this step rings nothing. */
    val target: Rect? get() = step?.anchor?.let { anchors[it] }

    fun start() { index = 0 }
    fun next() { if (isLast) stop() else index += 1 }
    fun back() { if (index > 0) index -= 1 }
    fun stop() { index = -1 }

    fun put(key: String, bounds: Rect) { anchors[key] = bounds }
    fun remove(key: String) { anchors.remove(key) }
}

/**
 * Null where no tour exists, which is every preview and every screen rendered
 * outside the main graph; anchoring is then a no-op rather than a crash.
 */
val LocalGuide = compositionLocalOf<GuideState?> { null }

/** Keys for the things the tour points at. */
object GuideAnchors {
    const val TAB_LEARN = "tab.learn"
    const val TAB_TODAY = "tab.today"
    const val TAB_US = "tab.us"
    const val TAB_LEVELS = "tab.levels"
    const val LEARN_TODAY_CARD = "learn.today"
    const val LEARN_COURSES = "learn.courses"
}
