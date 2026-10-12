package com.shahaabapps.twotracks.ui.guide

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
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

    /**
     * A lazy screen offers itself differently: nothing below the fold has
     * been composed, so there are no bounds to scroll towards. What it can
     * give is which item each anchor lives in, which is enough to scroll
     * there and let the anchor report itself once it exists.
     */
    private var lazyList: LazyListState? = null
    private var lazyItems: Map<String, Int> = emptyMap()

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

    fun setLazyHost(state: LazyListState, items: Map<String, Int>, bounds: Rect) {
        lazyList = state
        lazyItems = items
        viewport = bounds
    }

    fun clearLazyHost(state: LazyListState) {
        if (lazyList === state) { lazyList = null; lazyItems = emptyMap(); viewport = Rect.Zero }
    }

    /** Whether the ring for the current step is on screen and inside the page. */
    val revealed: Boolean
        get() {
            val key = step?.anchor ?: return true
            val bounds = anchors[key] ?: return false
            if (viewport.isEmpty) return true
            return bounds.top >= viewport.top && bounds.bottom <= viewport.bottom
        }

    /**
     * Brings the current step's anchor into view.
     *
     * A plain scrolling screen is driven by measuring: the anchor has already
     * reported where it is, so the distance to move is known. A lazy screen
     * has not composed what is below the fold, so it is driven in two moves —
     * scroll to the item the anchor lives in, which composes it, and then
     * measure and close the rest of the gap. The second move is what several
     * steps needed: Settings puts four rows in one item, and landing the top
     * of that item at the top of the page leaves the fourth row off it.
     */
    suspend fun revealCurrent() {
        val key = step?.anchor ?: return
        lazyList?.let { list ->
            val index = lazyItems[key] ?: return@let
            // The step can arrive before the screen has laid out, and a list
            // with nothing in it yet cannot be scrolled: animateScrollToItem
            // on an empty list simply never returns, which is how the tour
            // came to sit on whatever happened to be on screen.
            var waited = 0
            while (list.layoutInfo.totalItemsCount == 0 && waited < 2_000) {
                delay(50)
                waited += 50
            }
            if (list.layoutInfo.totalItemsCount == 0) return
            // Always, not only when the item is off screen. One pixel of a
            // tall item counts as visible, and Settings puts four rows in a
            // single item: the sliver test kept deciding there was nothing to
            // do while the row the step meant sat well below the fold. That
            // is why half the Settings steps ringed nothing.
            if (!revealed) {
                runCatching { withTimeoutOrNull(1_000) { list.animateScrollToItem(index) } }
            }
            // The item is composed now, so the anchor inside it can say where
            // it is. Give it a frame or two, then close what is left.
            waited = 0
            while (anchors[key] == null && waited < 600) {
                delay(50)
                waited += 50
            }
            val delta = gapToAnchor(key) ?: return
            // Clamped to one page: a measurement taken before the scroll had
            // settled would otherwise fling the list the length of Settings.
            runCatching {
                withTimeoutOrNull(1_000) {
                    list.animateScrollBy(delta.coerceIn(-viewport.height, viewport.height))
                }
            }
            return
        }
        val state = scroll ?: return
        val delta = gapToAnchor(key) ?: return
        val to = (state.value + delta).roundToInt().coerceIn(0, state.maxValue)
        state.animateScrollTo(to)
    }

    /**
     * How far the page must move for the anchor to sit inside it, or null
     * when it already does.
     *
     * The margin leaves room for the card, which sits at whichever end the
     * anchor is not: landing a ring hard against either edge reads as cut off.
     */
    private fun gapToAnchor(key: String): Float? {
        val bounds = anchors[key] ?: return null
        if (viewport.isEmpty) return null
        val margin = viewport.height * 0.28f
        val top = viewport.top + margin
        val bottom = viewport.bottom - margin
        val delta = when {
            bounds.top < top -> bounds.top - top
            bounds.bottom > bottom -> bounds.bottom - bottom
            else -> 0f
        }
        return delta.takeIf { it != 0f }
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
    const val LEVELS_LADDER = "levels.ladder"
    const val US_LEVELS = "us.levels"
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
    const val SET_VOICES = "set.voices"
    const val SET_WIDGETS = "set.widgets"
    const val SET_SEED = "set.seed"
    const val SET_TOUR = "set.tour"
    const val SET_SIGN_OUT = "set.signOut"
}
