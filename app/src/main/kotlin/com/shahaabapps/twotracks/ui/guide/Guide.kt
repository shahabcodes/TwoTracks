package com.shahaabapps.twotracks.ui.guide

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt

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
 * positioned by hand against one phone. They are dropped again when a
 * composable leaves, so a step never rings the ghost of a control from the
 * screen before.
 */
class GuideState(private val steps: List<GuideStep>) {

    private val anchors = mutableStateMapOf<String, Rect>()
    var index by mutableStateOf(-1)
        private set

    /**
     * The scrolling screen currently showing, if it offered itself, and where
     * on the display its window is. Without both, a control below the fold can
     * be ringed only where it happens already to be in view.
     */
    private var scroll: ScrollState? = null
    private var viewport: Rect = Rect.Zero

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

    fun setScrollHost(state: ScrollState?, bounds: Rect) {
        scroll = state
        viewport = bounds
    }

    fun clearScrollHost(state: ScrollState) {
        if (scroll === state) { scroll = null; viewport = Rect.Zero }
    }

    /**
     * Brings the current step's anchor into view, where the screen showing it
     * is one that scrolls as a whole.
     *
     * Only a plain scrolling screen can be driven this way. A lazy list has
     * not composed what is below the fold, so there is nothing there to
     * measure and nothing to scroll to; on those screens the tour rings what
     * is already on display and says the rest in words.
     */
    suspend fun revealCurrent() {
        val key = step?.anchor ?: return
        val bounds = anchors[key] ?: return
        val state = scroll ?: return
        if (viewport.isEmpty) return
        // Leave room for the card, which sits at whichever end the anchor is
        // not: landing a ring hard against either edge reads as cut off.
        val margin = viewport.height * 0.28f
        val top = viewport.top + margin
        val bottom = viewport.bottom - margin
        val delta = when {
            bounds.top < top -> bounds.top - top
            bounds.bottom > bottom -> bounds.bottom - bottom
            else -> 0f
        }
        if (delta == 0f) return
        val to = (state.value + delta).roundToInt().coerceIn(0, state.maxValue)
        state.animateScrollTo(to)
    }
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

    // Settings, top to bottom. It scrolls as one piece, so every row here can
    // be brought into view and ringed.
    const val SET_NAME = "set.name"
    const val SET_TRACK = "set.track"
    const val SET_PASSWORD = "set.password"
    const val SET_TIMEZONE = "set.timezone"
    const val SET_INVITE = "set.invite"
    const val SET_TEMPLATE = "set.template"
    const val SET_START_DATE = "set.startDate"
    const val SET_FULL_SCREEN = "set.fullScreen"
    const val SET_BIOMETRICS = "set.biometrics"
    const val SET_APPEARANCE = "set.appearance"
    const val SET_COMPANION = "set.companion"
    const val SET_VOICES = "set.voices"
    const val SET_SEED = "set.seed"
    const val SET_TOUR = "set.tour"
    const val SET_SIGN_OUT = "set.signOut"
}
