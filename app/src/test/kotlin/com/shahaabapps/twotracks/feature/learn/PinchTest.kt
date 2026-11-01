package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * A pinch over a column that also scrolls.
 *
 * This is the one hard thing about zooming a PDF: a pinch and the drag that
 * scrolls the pages are the same gesture until the second finger lands, and
 * whichever of the two claims it first takes the other's gesture for the rest
 * of the touch. The pages themselves need a renderer that does not exist off
 * a device, but this half does not — [pinchable] goes on an ordinary column
 * here, and both directions are checked: one finger must still scroll and
 * must not zoom, two fingers must zoom and must not scroll the page out from
 * under the fingers while they do it.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class, qualifiers = "w400dp-h800dp-xhdpi")
class PinchTest {

    @get:Rule
    val compose = createComposeRule()

    private val factors = mutableListOf<Float>()
    private val centroids = mutableListOf<Offset>()
    private lateinit var listState: LazyListState

    private fun show() {
        compose.setContent {
            listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(COLUMN)
                    .pinchable { factor, at -> factors += factor; centroids += at },
            ) {
                items(40) { Text("Page ${it + 1}", modifier = Modifier.height(200.dp)) }
            }
        }
    }

    @Test
    fun `one finger scrolls the column and does not zoom it`() {
        show()

        compose.onNodeWithTag(COLUMN).performTouchInput { swipeUp() }
        compose.waitForIdle()

        assertTrue("the column did not scroll", listState.firstVisibleItemIndex > 0)
        assertEquals("a one-finger drag was taken for a pinch: $factors", 0, factors.size)
    }

    @Test
    fun `two fingers spread apart zoom in, and the column stays put`() {
        show()

        compose.onNodeWithTag(COLUMN).performTouchInput {
            val middle = center
            down(0, middle - Offset(60f, 0f))
            down(1, middle + Offset(60f, 0f))
            moveTo(0, middle - Offset(240f, 0f))
            moveTo(1, middle + Offset(240f, 0f))
            up(0)
            up(1)
        }
        compose.waitForIdle()

        assertTrue("nothing was reported as a pinch", factors.isNotEmpty())
        // Spread apart, so every step of it is larger than it was.
        assertTrue("the pinch read as $factors, which is not growing", factors.all { it > 1f })
        assertTrue("the fingers moved the page as well as zooming it", listState.firstVisibleItemIndex == 0)
    }

    @Test
    fun `the pinch says where the fingers are, not just how far apart`() {
        show()

        // Both fingers in the top left quarter: a zoom that does not know
        // this can only grow the page from the corner of the screen.
        val bounds = compose.onNodeWithTag(COLUMN).fetchSemanticsNode().size
        val here = Offset(bounds.width * 0.25f, bounds.height * 0.25f)
        compose.onNodeWithTag(COLUMN).performTouchInput {
            down(0, here - Offset(40f, 0f))
            down(1, here + Offset(40f, 0f))
            moveTo(0, here - Offset(160f, 0f))
            moveTo(1, here + Offset(160f, 0f))
            up(0)
            up(1)
        }
        compose.waitForIdle()

        assertTrue("nothing was reported as a pinch", centroids.isNotEmpty())
        val last = centroids.last()
        assertTrue("the centroid came back as $last, not near $here", (last - here).getDistance() < 40f)
    }

    @Test
    fun `two fingers brought together zoom out`() {
        show()

        compose.onNodeWithTag(COLUMN).performTouchInput {
            val middle = center
            down(0, middle - Offset(240f, 0f))
            down(1, middle + Offset(240f, 0f))
            moveTo(0, middle - Offset(60f, 0f))
            moveTo(1, middle + Offset(60f, 0f))
            up(0)
            up(1)
        }
        compose.waitForIdle()

        assertTrue("nothing was reported as a pinch", factors.isNotEmpty())
        assertTrue("the pinch read as $factors, which is not shrinking", factors.all { it < 1f })
    }

    private companion object {
        const val COLUMN = "pinch-column"
    }
}
